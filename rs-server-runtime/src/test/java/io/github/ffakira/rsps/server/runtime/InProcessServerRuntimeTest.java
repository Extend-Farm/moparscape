package io.github.ffakira.rsps.server.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.model.CharacterId;
import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.persistence.AccountRecord;
import io.github.ffakira.rsps.persistence.AccountRepository;
import io.github.ffakira.rsps.persistence.CharacterAppearance;
import io.github.ffakira.rsps.persistence.CharacterItemSlot;
import io.github.ffakira.rsps.persistence.CharacterProfile;
import io.github.ffakira.rsps.persistence.CharacterRepository;
import io.github.ffakira.rsps.persistence.CharacterSkill;
import io.github.ffakira.rsps.persistence.CharacterSnapshot;
import io.github.ffakira.rsps.persistence.ItemContainerKind;
import io.github.ffakira.rsps.protocol.input.ActionSequenceIntentMessage;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAnimationProfile;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.ClientMessage;
import io.github.ffakira.rsps.protocol.world.EntityActionSequenceMessage;
import io.github.ffakira.rsps.protocol.world.EntityPositionMessage;
import io.github.ffakira.rsps.protocol.session.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.session.HandshakeRequest;
import io.github.ffakira.rsps.protocol.session.LoginAccepted;
import io.github.ffakira.rsps.protocol.session.LoginRejected;
import io.github.ffakira.rsps.protocol.session.LoginRequest;
import io.github.ffakira.rsps.protocol.input.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.ProtocolSession;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.protocol.world.WorldSnapshotMessage;
import io.github.ffakira.rsps.sim.EntityMovedEvent;
import io.github.ffakira.rsps.sim.PlayerSpawnedEvent;
import io.github.ffakira.rsps.sim.WorldEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class InProcessServerRuntimeTest {

  @Test
  void authenticatesCharacterAndEntersWorldShardInProcess() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(new CharacterSkill(0, 99, 14_000_000)),
        List.of(new CharacterItemSlot(ItemContainerKind.INVENTORY, 0, 555, 1000)),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot),
        CredentialVerifier.plainText(),
        ProtocolVersion.current(),
        "Welcome back"
    )) {
      RecordingProtocolSession protocolSession = new RecordingProtocolSession();
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolSession);

      playerSessionActor.accept(new HandshakeRequest(ProtocolVersion.current(), "integration-test"));
      playerSessionActor.accept(new LoginRequest("akira", "swordfish"));

      assertThat(protocolSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();
      assertThat(playerSessionActor.phase()).isEqualTo(SessionPhase.IN_WORLD);
      assertThat(protocolSession.sentMessages()).hasSize(5);
      assertThat(protocolSession.sentMessages().get(0)).isEqualTo(new HandshakeAccepted(ProtocolVersion.current(), "Welcome back"));
      assertThat(protocolSession.sentMessages().get(1)).isEqualTo(new LoginAccepted(101L, 202L, characterSnapshot.worldPoint()));
      assertThat(protocolSession.sentMessages().get(2))
          .isInstanceOfSatisfying(
              CharacterBootstrapMessage.class,
              message -> {
                assertThat(message.bootstrap().displayName()).isEqualTo("Akira");
                assertThat(message.bootstrap().appearance().animationProfile()).isEqualTo(BootstrapAnimationProfile.referencePlayer());
                assertThat(message.bootstrap().inventory()).singleElement()
                    .satisfies(slot -> assertThat(slot.itemId()).isEqualTo(555));
              }
          );
      assertThat(protocolSession.sentMessages().get(3)).isEqualTo(new WorldSnapshotMessage("50_50", characterSnapshot.worldPoint()));
      assertThat(protocolSession.sentMessages().get(4)).isEqualTo(new EntityPositionMessage(1, characterSnapshot.worldPoint()));

      assertThat(awaitWorldEvents(runtime.worldShardActor(), 1, Duration.ofSeconds(2))).containsExactly(
          new PlayerSpawnedEvent(playerSessionActor.worldShardAdmission().entityId(), characterSnapshot.id(), characterSnapshot.worldPoint())
      );
    }
  }

  @Test
  void rejectsInvalidCredentialsWithoutEnteringWorld() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        List.of(),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot),
        CredentialVerifier.plainText(),
        ProtocolVersion.current(),
        "Welcome back"
    )) {
      RecordingProtocolSession protocolSession = new RecordingProtocolSession();
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolSession);

      playerSessionActor.accept(new HandshakeRequest(ProtocolVersion.current(), "integration-test"));
      playerSessionActor.accept(new LoginRequest("akira", "wrong-password"));

      assertThat(protocolSession.awaitMessageCount(2, Duration.ofSeconds(2))).isTrue();
      assertThat(playerSessionActor.phase()).isEqualTo(SessionPhase.READY_FOR_LOGIN);
      assertThat(protocolSession.sentMessages()).containsExactly(
          new HandshakeAccepted(ProtocolVersion.current(), "Welcome back"),
          new LoginRejected("Invalid credentials")
      );
      assertThat(runtime.worldShardActor().drainEvents()).isEmpty();
    }
  }

  @Test
  void forwardsMovementToTheWorldShardAfterLogin() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        List.of(),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    )) {
      RecordingProtocolSession protocolSession = new RecordingProtocolSession();
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolSession);

      playerSessionActor.accept(new HandshakeRequest(ProtocolVersion.current(), "integration-test"));
      playerSessionActor.accept(new LoginRequest("akira", "swordfish"));
      assertThat(protocolSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      runtime.worldShardActor().drainEvents();
      playerSessionActor.accept(new MoveIntentMessage(1, 0, MovementMode.WALK));
      assertThat(protocolSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(protocolSession.sentMessages()).last().isEqualTo(new EntityPositionMessage(
          playerSessionActor.worldShardAdmission().entityId().value(),
          characterSnapshot.worldPoint().translate(1, 0)
      ));
      assertThat(awaitMovementEvents(runtime.worldShardActor(), Duration.ofSeconds(2)))
          .containsExactly(new EntityMovedEvent(
              playerSessionActor.worldShardAdmission().entityId(),
              characterSnapshot.worldPoint(),
              characterSnapshot.worldPoint().translate(1, 0)
          ));
    }
  }

  @Test
  void forwardsLocalActionSequenceChangesToTheProtocolSession() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        List.of(),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    )) {
      RecordingProtocolSession protocolSession = new RecordingProtocolSession();
      PlayerSessionActor playerSessionActor = runtime.openSession(protocolSession);

      playerSessionActor.accept(new HandshakeRequest(ProtocolVersion.current(), "integration-test"));
      playerSessionActor.accept(new LoginRequest("akira", "swordfish"));
      assertThat(protocolSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      playerSessionActor.accept(new ActionSequenceIntentMessage(866));
      assertThat(protocolSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(protocolSession.sentMessages()).last().isEqualTo(new EntityActionSequenceMessage(
          playerSessionActor.worldShardAdmission().entityId().value(),
          866
      ));
    }
  }

  private static List<WorldEvent> awaitWorldEvents(WorldShardActor worldShardActor, int minimumCount, Duration timeout) {
    long deadline = System.nanoTime() + timeout.toNanos();
    List<WorldEvent> events = List.of();
    while (System.nanoTime() < deadline) {
      events = worldShardActor.drainEvents();
      if (events.size() >= minimumCount) {
        return events;
      }
      sleepBriefly();
    }
    return events;
  }

  private static List<WorldEvent> awaitMovementEvents(WorldShardActor worldShardActor, Duration timeout) {
    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      List<WorldEvent> events = awaitWorldEvents(worldShardActor, 1, Duration.ofMillis(100));
      if (!events.isEmpty()) {
        return events;
      }
    }
    return List.of();
  }

  private static void sleepBriefly() {
    try {
      Thread.sleep(10L);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new AssertionError("Interrupted while waiting for async state", interruptedException);
    }
  }

  private static final class InMemoryAccountRepository implements AccountRepository {

    private final AccountRecord accountRecord;

    private InMemoryAccountRepository(AccountRecord accountRecord) {
      this.accountRecord = accountRecord;
    }

    @Override
    public Optional<AccountRecord> findByUsername(String username) {
      if (accountRecord.username().equals(username)) {
        return Optional.of(accountRecord);
      }
      return Optional.empty();
    }

    @Override
    public AccountRecord save(AccountRecord accountRecord) {
      return accountRecord;
    }
  }

  private static final class InMemoryCharacterRepository implements CharacterRepository {

    private final CharacterSnapshot characterSnapshot;

    private InMemoryCharacterRepository(CharacterSnapshot characterSnapshot) {
      this.characterSnapshot = characterSnapshot;
    }

    @Override
    public Optional<CharacterSnapshot> loadByAccountId(AccountId accountId) {
      if (characterSnapshot.accountId().equals(accountId)) {
        return Optional.of(characterSnapshot);
      }
      return Optional.empty();
    }

    @Override
    public CharacterSnapshot save(CharacterSnapshot characterSnapshot) {
      return characterSnapshot;
    }
  }

  private static final class RecordingProtocolSession implements ProtocolSession {

    private final UUID sessionId = UUID.randomUUID();
    private final List<ServerMessage> sentMessages = new ArrayList<>();
    private final AtomicBoolean closed = new AtomicBoolean();

    @Override
    public UUID sessionId() {
      return sessionId;
    }

    @Override
    public void send(ServerMessage message) {
      synchronized (sentMessages) {
        sentMessages.add(message);
      }
    }

    @Override
    public void close() {
      closed.set(true);
    }

    private boolean awaitMessageCount(int expectedCount, Duration timeout) {
      long deadline = System.nanoTime() + timeout.toNanos();
      while (System.nanoTime() < deadline) {
        synchronized (sentMessages) {
          if (sentMessages.size() >= expectedCount) {
            return true;
          }
        }
        sleepBriefly();
      }
      return false;
    }

    private List<ServerMessage> sentMessages() {
      synchronized (sentMessages) {
        return List.copyOf(sentMessages);
      }
    }
  }
}
