package com.veyrmoor.server.runtime;

import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ProtocolVersion;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class InProcessServerRuntime implements AutoCloseable {

  private final WorldShardActor worldShardActor;
  private final LoginGatewayActor loginGatewayActor;
  private final List<PlayerSessionActor> sessions = new CopyOnWriteArrayList<>();

  public InProcessServerRuntime(AccountRepository accountRepository, CharacterRepository characterRepository) {
    this(
        accountRepository,
        characterRepository,
        CredentialVerifier.compatible(),
        LoginAccountProvisioner.disabled(),
        ProtocolVersion.current(),
        "Welcome to the modern RSPS runtime"
    );
  }

  public InProcessServerRuntime(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      CredentialVerifier credentialVerifier,
      ProtocolVersion acceptedProtocolVersion,
      String motd
  ) {
    this(
        accountRepository,
        characterRepository,
        credentialVerifier,
        LoginAccountProvisioner.disabled(),
        acceptedProtocolVersion,
        motd
    );
  }

  public InProcessServerRuntime(
      AccountRepository accountRepository,
      CharacterRepository characterRepository,
      CredentialVerifier credentialVerifier,
      LoginAccountProvisioner loginAccountProvisioner,
      ProtocolVersion acceptedProtocolVersion,
      String motd
  ) {
    this.worldShardActor = new WorldShardActor();
    this.loginGatewayActor = new LoginGatewayActor(
        accountRepository,
        characterRepository,
        credentialVerifier,
        loginAccountProvisioner,
        acceptedProtocolVersion,
        motd,
        worldShardActor
    );
    worldShardActor.start();
    loginGatewayActor.start();
  }

  public PlayerSessionActor openSession(ProtocolSession protocolSession) {
    PlayerSessionActor session = new PlayerSessionActor(
        protocolSession,
        loginGatewayActor,
        worldShardActor,
        this::broadcastPublicChat
    );
    session.start();
    sessions.add(session);
    return session;
  }

  public WorldShardActor worldShardActor() {
    return worldShardActor;
  }

  @Override
  public void close() {
    for (PlayerSessionActor session : sessions) {
      session.close();
    }
    loginGatewayActor.close();
    worldShardActor.close();
  }

  private void broadcastPublicChat(String speakerDisplayName, String text) {
    for (PlayerSessionActor session : sessions) {
      session.deliverPublicChat(speakerDisplayName, text);
    }
  }
}
