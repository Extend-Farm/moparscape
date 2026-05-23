package com.veyrmoor.server.runtime;

import com.veyrmoor.model.MovementMode;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginRejected;
import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.input.MoveIntentMessage;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.sim.EntityMovedEvent;
import com.veyrmoor.sim.MoveEntityCommand;
import com.veyrmoor.sim.PlayerSpawnedEvent;
import com.veyrmoor.sim.SpawnPlayerCommand;
import com.veyrmoor.sim.WorldEvent;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameplayRuntimeCoordinator {

  private static final Duration EVENT_WAIT_TIMEOUT = Duration.ofSeconds(2);

  private final AccountRepository accountRepository;
  private final CharacterRepository characterRepository;
  private final WorldShardActor worldShardActor;
  private final CredentialVerifier credentialVerifier;
  private final Map<UUID, SessionState> sessionsById = new ConcurrentHashMap<>();

  public GameplayRuntimeCoordinator(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      WorldShardActor worldShardActor
  ) {
    this(accountRepository, characterRepository, worldShardActor, CredentialVerifier.compatible());
  }

  public GameplayRuntimeCoordinator(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      WorldShardActor worldShardActor,
      CredentialVerifier credentialVerifier
  ) {
    this.accountRepository = accountRepository;
    this.characterRepository = characterRepository;
    this.worldShardActor = worldShardActor;
    this.credentialVerifier = credentialVerifier;
  }

  public void handleHandshake(ProtocolSession session, HandshakeRequest request) {
    session.send(new HandshakeAccepted(ProtocolVersion.current(), "Welcome to RSPS Modern"));
  }

  public void handleLogin(ProtocolSession session, LoginRequest request) {
    String normalizedUsername = normalize(request.username());
    AccountRecord account = accountRepository.findByUsername(normalizedUsername)
        .orElse(null);
    if (account == null || !credentialVerifier.matches(account, request.password())) {
      session.send(new LoginRejected("Invalid credentials"));
      return;
    }

    CharacterSnapshot character = characterRepository.loadByAccountId(account.id()).orElse(null);
    if (character == null) {
      session.send(new LoginRejected("Missing character"));
      return;
    }

    worldShardActor.tell(
        new WorldShardMessage.ApplyWorldCommandMessage(
            new SpawnPlayerCommand(character.id(), character.worldPoint())
        )
    );
    PlayerSpawnedEvent spawnedEvent = awaitSpawn(character.id());
    SessionState sessionState = new SessionState(session.sessionId(), account, character, spawnedEvent.entityId(), -1);
    sessionsById.put(session.sessionId(), sessionState);
    session.sendAll(BootstrapMessageBatch.create(new WorldShardAdmission(
        character,
        spawnedEvent.entityId(),
        regionKey(character.worldPoint())
    )));
  }

  public void handleMove(ProtocolSession session, MoveIntentMessage request) {
    SessionState sessionState = sessionsById.get(session.sessionId());
    if (sessionState == null) {
      session.send(new LoginRejected("Session not authenticated"));
      return;
    }

    worldShardActor.tell(
        new WorldShardMessage.ApplyWorldCommandMessage(
            new MoveEntityCommand(
                sessionState.entityId(),
                request.deltaX(),
                request.deltaY(),
                request.movementMode()
            )
        )
    );
    worldShardActor.tell(new WorldShardMessage.TickWorldMessage());
    EntityMovedEvent movedEvent = awaitMovement(sessionState.entityId());
    session.send(new com.veyrmoor.protocol.world.EntityPositionMessage(sessionState.entityId().value(), movedEvent.to()));
  }

  public void handleActionSequence(ProtocolSession session, ActionSequenceIntentMessage request) {
    SessionState sessionState = sessionsById.get(session.sessionId());
    if (sessionState == null) {
      session.send(new LoginRejected("Session not authenticated"));
      return;
    }
    sessionsById.put(session.sessionId(), sessionState.withActionSequenceId(request.actionSequenceId()));
    session.send(new EntityActionSequenceMessage(sessionState.entityId().value(), request.actionSequenceId()));
  }

  public void handleDemoMove(ProtocolSession session) {
    handleMove(session, new MoveIntentMessage(1, 0, MovementMode.WALK));
  }

  private PlayerSpawnedEvent awaitSpawn(com.veyrmoor.model.CharacterId characterId) {
    long deadline = System.nanoTime() + EVENT_WAIT_TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      List<WorldEvent> events = worldShardActor.drainEvents();
      for (WorldEvent event : events) {
        if (event instanceof PlayerSpawnedEvent playerSpawnedEvent
            && playerSpawnedEvent.characterId().equals(characterId)) {
          return playerSpawnedEvent;
        }
      }
      sleepBriefly();
    }
    throw new IllegalStateException("Timed out waiting for spawned player " + characterId.value());
  }

  private EntityMovedEvent awaitMovement(com.veyrmoor.model.EntityId entityId) {
    long deadline = System.nanoTime() + EVENT_WAIT_TIMEOUT.toNanos();
    while (System.nanoTime() < deadline) {
      List<WorldEvent> events = worldShardActor.drainEvents();
      for (WorldEvent event : events) {
        if (event instanceof EntityMovedEvent entityMovedEvent
            && entityMovedEvent.entityId().equals(entityId)) {
          return entityMovedEvent;
        }
      }
      sleepBriefly();
    }
    throw new IllegalStateException("Timed out waiting for entity movement " + entityId.value());
  }

  private String regionKey(WorldPoint point) {
    return (point.x() >> 6) + "_" + (point.y() >> 6) + "_" + point.plane();
  }

  private String normalize(String value) {
    return value.trim().toLowerCase(Locale.ROOT);
  }

  private void sleepBriefly() {
    try {
      Thread.sleep(10L);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting for world event", interruptedException);
    }
  }
}
