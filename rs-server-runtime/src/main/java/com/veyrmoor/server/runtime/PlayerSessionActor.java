package com.veyrmoor.server.runtime;

import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.chat.PublicChatMessage;
import com.veyrmoor.protocol.session.DisconnectNotice;
import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.protocol.world.EntityPositionMessage;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginRejected;
import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.input.MoveIntentMessage;
import com.veyrmoor.protocol.input.PublicChatIntentMessage;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.sim.EntityMovedEvent;
import com.veyrmoor.sim.MoveEntityCommand;
import com.veyrmoor.sim.WorldEvent;
import java.util.List;

public final class PlayerSessionActor extends MailboxActor<SessionMessage> {

  private final ProtocolSession protocolSession;
  private final ActorRef<LoginGatewayMessage> loginGatewayActor;
  private final WorldShardActor worldShardActor;
  private final PublicChatBroadcaster publicChatBroadcaster;
  private volatile SessionPhase phase = SessionPhase.CONNECTED;
  private volatile WorldShardAdmission worldShardAdmission;
  private LoginRequest deferredLoginRequest;

  public PlayerSessionActor(
      ProtocolSession protocolSession,
      ActorRef<LoginGatewayMessage> loginGatewayActor,
      WorldShardActor worldShardActor,
      PublicChatBroadcaster publicChatBroadcaster
  ) {
    this.protocolSession = protocolSession;
    this.loginGatewayActor = loginGatewayActor;
    this.worldShardActor = worldShardActor;
    this.publicChatBroadcaster = publicChatBroadcaster;
  }

  public SessionPhase phase() {
    return phase;
  }

  public WorldShardAdmission worldShardAdmission() {
    return worldShardAdmission;
  }

  public void accept(ClientMessage message) {
    tell(new SessionMessage.InboundClientMessage(message));
  }

  @Override
  public void close() {
    phase = SessionPhase.CLOSED;
    protocolSession.close();
    super.close();
  }

  @Override
  protected String actorName() {
    return "PlayerSession-" + protocolSession.sessionId();
  }

  @Override
  protected void onMessage(SessionMessage message) {
    switch (message) {
      case SessionMessage.InboundClientMessage inboundClientMessage -> handleClientMessage(inboundClientMessage.message());
      case SessionMessage.HandshakeCompletedMessage handshakeCompletedMessage -> completeHandshake(handshakeCompletedMessage);
      case SessionMessage.LoginSucceededMessage loginSucceededMessage -> completeLogin(loginSucceededMessage);
      case SessionMessage.LoginRejectedMessage loginRejectedMessage -> rejectLogin(loginRejectedMessage);
      case SessionMessage.WorldCommandAppliedMessage worldCommandAppliedMessage ->
          forwardWorldEvents(worldCommandAppliedMessage.worldEvents());
      case SessionMessage.PublicChatBroadcastMessage publicChatBroadcastMessage ->
          forwardPublicChat(publicChatBroadcastMessage);
      case SessionMessage.CloseSessionMessage closeSessionMessage -> terminate(closeSessionMessage.reason());
    }
  }

  private void handleClientMessage(ClientMessage message) {
    switch (message) {
      case HandshakeRequest handshakeRequest -> handleHandshake(handshakeRequest);
      case LoginRequest loginRequest -> handleLogin(loginRequest);
      case MoveIntentMessage moveIntentMessage -> handleMove(moveIntentMessage);
      case ActionSequenceIntentMessage actionSequenceIntentMessage -> handleActionSequence(actionSequenceIntentMessage);
      case PublicChatIntentMessage publicChatIntentMessage -> handlePublicChat(publicChatIntentMessage);
      case DisconnectNotice disconnectNotice -> terminate(disconnectNotice.reason());
      default -> throw new IllegalArgumentException("Unsupported client message: " + message.getClass().getName());
    }
  }

  private void handleHandshake(HandshakeRequest request) {
    if (phase != SessionPhase.CONNECTED) {
      protocolSession.send(new LoginRejected("Handshake already completed"));
      return;
    }
    phase = SessionPhase.HANDSHAKE_IN_PROGRESS;
    loginGatewayActor.tell(new LoginGatewayMessage.HandleHandshakeMessage(this, request));
  }

  private void handleLogin(LoginRequest request) {
    if (phase == SessionPhase.CONNECTED) {
      protocolSession.send(new LoginRejected("Handshake required before login"));
      return;
    }
    if (phase == SessionPhase.HANDSHAKE_IN_PROGRESS) {
      deferredLoginRequest = request;
      return;
    }
    if (phase == SessionPhase.LOGIN_IN_PROGRESS) {
      protocolSession.send(new LoginRejected("Login already in progress"));
      return;
    }
    if (phase == SessionPhase.IN_WORLD) {
      protocolSession.send(new LoginRejected("Session already authenticated"));
      return;
    }
    if (phase == SessionPhase.CLOSED) {
      return;
    }
    phase = SessionPhase.LOGIN_IN_PROGRESS;
    loginGatewayActor.tell(new LoginGatewayMessage.HandleLoginMessage(this, request));
  }

  private void handleMove(MoveIntentMessage message) {
    if (phase != SessionPhase.IN_WORLD || worldShardAdmission == null) {
      return;
    }
    worldShardActor.applyWorldCommandAndTick(new MoveEntityCommand(
        worldShardAdmission.entityId(),
        message.deltaX(),
        message.deltaY(),
        message.movementMode()
    )).whenComplete((worldEvents, throwable) -> {
      if (throwable != null) {
        tell(new SessionMessage.CloseSessionMessage("World command failed"));
        return;
      }
      tell(new SessionMessage.WorldCommandAppliedMessage(worldEvents));
    });
  }

  private void handleActionSequence(ActionSequenceIntentMessage message) {
    if (phase != SessionPhase.IN_WORLD || worldShardAdmission == null) {
      return;
    }
    protocolSession.send(new EntityActionSequenceMessage(
        worldShardAdmission.entityId().value(),
        message.actionSequenceId()
    ));
  }

  private void handlePublicChat(PublicChatIntentMessage message) {
    if (phase != SessionPhase.IN_WORLD || worldShardAdmission == null) {
      return;
    }
    publicChatBroadcaster.broadcast(worldShardAdmission.characterSnapshot().displayName(), message.text());
  }

  private void completeLogin(SessionMessage.LoginSucceededMessage message) {
    worldShardAdmission = message.admission();
    phase = SessionPhase.IN_WORLD;
    protocolSession.sendAll(BootstrapMessageBatch.create(message.admission()));
  }

  private void rejectLogin(SessionMessage.LoginRejectedMessage message) {
    if (!message.closeSession() && phase == SessionPhase.LOGIN_IN_PROGRESS) {
      phase = SessionPhase.READY_FOR_LOGIN;
    }
    protocolSession.send(new LoginRejected(message.reason()));
    if (message.closeSession()) {
      terminate(message.reason());
    }
  }

  private void completeHandshake(SessionMessage.HandshakeCompletedMessage message) {
    phase = SessionPhase.READY_FOR_LOGIN;
    protocolSession.send(message.response());
    if (deferredLoginRequest == null) {
      return;
    }
    LoginRequest queuedLoginRequest = deferredLoginRequest;
    deferredLoginRequest = null;
    handleLogin(queuedLoginRequest);
  }

  private void forwardWorldEvents(List<WorldEvent> worldEvents) {
    if (worldShardAdmission == null) {
      return;
    }
    for (WorldEvent worldEvent : worldEvents) {
      if (worldEvent instanceof EntityMovedEvent entityMovedEvent
          && entityMovedEvent.entityId().equals(worldShardAdmission.entityId())) {
        protocolSession.send(new EntityPositionMessage(entityMovedEvent.entityId().value(), entityMovedEvent.to()));
      }
    }
  }

  void deliverPublicChat(String speakerDisplayName, String text) {
    tell(new SessionMessage.PublicChatBroadcastMessage(speakerDisplayName, text));
  }

  private void forwardPublicChat(SessionMessage.PublicChatBroadcastMessage message) {
    if (phase != SessionPhase.IN_WORLD) {
      return;
    }
    protocolSession.send(new PublicChatMessage(message.speakerDisplayName(), message.text()));
  }

  private void terminate(String reason) {
    if (phase == SessionPhase.CLOSED) {
      return;
    }
    close();
  }
}
