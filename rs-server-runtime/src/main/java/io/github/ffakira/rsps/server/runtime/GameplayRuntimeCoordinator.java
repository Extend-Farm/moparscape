package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.protocol.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.HandshakeRequest;
import io.github.ffakira.rsps.protocol.LoginRejected;
import io.github.ffakira.rsps.protocol.LoginRequest;
import io.github.ffakira.rsps.protocol.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.ProtocolSession;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.sim.EntityMovedEvent;
import io.github.ffakira.rsps.sim.MoveEntityCommand;
import io.github.ffakira.rsps.sim.PlayerSpawnedEvent;
import io.github.ffakira.rsps.sim.SpawnPlayerCommand;
import io.github.ffakira.rsps.sim.WorldEvent;
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
    SessionState sessionState = new SessionState(session.sessionId(), account, character, spawnedEvent.entityId());
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
    session.send(new io.github.ffakira.rsps.protocol.EntityPositionMessage(sessionState.entityId().value(), movedEvent.to()));
  }

  public void handleDemoMove(ProtocolSession session) {
    handleMove(session, new MoveIntentMessage(1, 0, MovementMode.WALK));
  }

  private PlayerSpawnedEvent awaitSpawn(io.github.ffakira.rsps.model.CharacterId characterId) {
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

  private EntityMovedEvent awaitMovement(io.github.ffakira.rsps.model.EntityId entityId) {
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
