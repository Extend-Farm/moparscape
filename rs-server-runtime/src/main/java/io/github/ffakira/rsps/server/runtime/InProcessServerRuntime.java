package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.protocol.ProtocolSession;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
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
    this.worldShardActor = new WorldShardActor();
    this.loginGatewayActor = new LoginGatewayActor(
        accountRepository,
        characterRepository,
        credentialVerifier,
        acceptedProtocolVersion,
        motd,
        worldShardActor
    );
    worldShardActor.start();
    loginGatewayActor.start();
  }

  public PlayerSessionActor openSession(ProtocolSession protocolSession) {
    PlayerSessionActor session = new PlayerSessionActor(protocolSession, loginGatewayActor, worldShardActor);
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
}
