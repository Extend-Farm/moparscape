package com.veyrmoor.client.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.model.AccountId;
import com.veyrmoor.model.CharacterId;
import com.veyrmoor.model.MovementMode;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.persistence.AccountRecord;
import com.veyrmoor.persistence.CharacterAppearance;
import com.veyrmoor.persistence.CharacterItemSlot;
import com.veyrmoor.persistence.CharacterProfile;
import com.veyrmoor.persistence.AccountRepository;
import com.veyrmoor.persistence.CharacterRepository;
import com.veyrmoor.persistence.CharacterSkill;
import com.veyrmoor.persistence.CharacterSnapshot;
import com.veyrmoor.persistence.ItemContainerKind;
import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ServerMessage;
import com.veyrmoor.protocol.chat.PublicChatMessage;
import com.veyrmoor.server.runtime.InProcessServerRuntime;
import com.veyrmoor.server.runtime.PlayerSessionActor;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InProcessGameplayFlowTest {

  @Test
  void reachesWorldAndUpdatesLocalPositionAfterMovement() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(new CharacterSkill(0, 99, 14_000_000)),
        List.of(
            new CharacterItemSlot(ItemContainerKind.INVENTORY, 0, 555, 1000),
            new CharacterItemSlot(ItemContainerKind.EQUIPMENT, 0, 1048, 1)
        ),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    )) {
      ClientCore clientCore = new ClientCore();
      BridgedProtocolSession bridgedSession = new BridgedProtocolSession();
      GameplayClientSession gameplayClientSession =
          new GameplayClientSession(clientCore, bridgedSession, "integration-test");
      bridgedSession.bindInbound(message -> gameplayClientSession.accept(message));
      PlayerSessionActor playerSessionActor = runtime.openSession(bridgedSession);
      bridgedSession.bindOutbound(playerSessionActor::accept);

      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(bridgedSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      gameplayClientSession.move(1, 0, MovementMode.WALK);
      assertThat(bridgedSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();

      assertThat(gameplayClientSession.viewModel().loggedIn()).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3201, 3201, 0));
      assertThat(gameplayClientSession.viewModel().inventory().getFirst().itemId()).isEqualTo(555);
      assertThat(gameplayClientSession.viewModel().equipment().getFirst().itemId()).isEqualTo(1048);
      assertThat(gameplayClientSession.viewModel().inventoryPresentation().getFirst().name()).isEqualTo("Water rune");
      assertThat(gameplayClientSession.viewModel().equipmentPresentation().getFirst().slotName()).isEqualTo("Head");
      assertThat(gameplayClientSession.viewModel().skillPresentation().getFirst().name()).isEqualTo("Attack");
    }
  }

  @Test
  void expandsClickMovementIntoAPacedWalkPath() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(new CharacterSkill(0, 99, 14_000_000)),
        List.of(),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    )) {
      ClientCore clientCore = new ClientCore();
      BridgedProtocolSession bridgedSession = new BridgedProtocolSession();
      TestNanoClock clock = new TestNanoClock();
      GameplayClientSession gameplayClientSession = new GameplayClientSession(
          clientCore,
          bridgedSession,
          "integration-test",
          new DefaultSceneAssetService(),
          Path.of("."),
          clock::now
      );
      bridgedSession.bindInbound(gameplayClientSession::accept);
      PlayerSessionActor playerSessionActor = runtime.openSession(bridgedSession);
      bridgedSession.bindOutbound(playerSessionActor::accept);

      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(bridgedSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      gameplayClientSession.move(3, 2, MovementMode.WALK);
      gameplayClientSession.pumpMovement();
      assertThat(bridgedSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3201, 3202, 0));

      clock.advanceNanos(120_000_000L);
      gameplayClientSession.pumpMovement();
      assertThat(bridgedSession.awaitMessageCount(7, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3202, 3202, 0));

      clock.advanceNanos(120_000_000L);
      gameplayClientSession.pumpMovement();
      assertThat(bridgedSession.awaitMessageCount(8, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3203, 3203, 0));
    }
  }

  @Test
  void expandsRunMovementIntoTwoTileBursts() {
    AccountRecord accountRecord = new AccountRecord(new AccountId(101L), "akira", "swordfish");
    CharacterSnapshot characterSnapshot = new CharacterSnapshot(
        new CharacterId(202L),
        accountRecord.id(),
        "Akira",
        new WorldPoint(3200, 3201, 0),
        new CharacterProfile((short) 2, true, 100, null, 0L, 0L),
        new CharacterAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(new CharacterSkill(0, 99, 14_000_000)),
        List.of(),
        List.of()
    );

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    )) {
      ClientCore clientCore = new ClientCore();
      BridgedProtocolSession bridgedSession = new BridgedProtocolSession();
      TestNanoClock clock = new TestNanoClock();
      GameplayClientSession gameplayClientSession = new GameplayClientSession(
          clientCore,
          bridgedSession,
          "integration-test",
          new DefaultSceneAssetService(),
          Path.of("."),
          clock::now
      );
      bridgedSession.bindInbound(gameplayClientSession::accept);
      PlayerSessionActor playerSessionActor = runtime.openSession(bridgedSession);
      bridgedSession.bindOutbound(playerSessionActor::accept);

      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(bridgedSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      gameplayClientSession.move(3, 0, MovementMode.RUN);
      gameplayClientSession.pumpMovement();
      assertThat(bridgedSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3202, 3201, 0));

      clock.advanceNanos(120_000_000L);
      gameplayClientSession.pumpMovement();
      assertThat(bridgedSession.awaitMessageCount(7, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3203, 3201, 0));
    }
  }

  @Test
  void updatesLocalActionSequenceFromRuntimePacketsWhileMovementContinues() {
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
      ClientCore clientCore = new ClientCore();
      BridgedProtocolSession bridgedSession = new BridgedProtocolSession();
      GameplayClientSession gameplayClientSession =
          new GameplayClientSession(clientCore, bridgedSession, "integration-test");
      bridgedSession.bindInbound(gameplayClientSession::accept);
      PlayerSessionActor playerSessionActor = runtime.openSession(bridgedSession);
      bridgedSession.bindOutbound(playerSessionActor::accept);

      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(bridgedSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      gameplayClientSession.setActionSequence(866);
      assertThat(bridgedSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerActionSequenceId()).isEqualTo(866);

      gameplayClientSession.move(1, 0, MovementMode.WALK);
      assertThat(bridgedSession.awaitMessageCount(7, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3201, 3201, 0));
      assertThat(gameplayClientSession.viewModel().localPlayerActionSequenceId()).isEqualTo(866);

      gameplayClientSession.setActionSequence(-1);
      assertThat(bridgedSession.awaitMessageCount(8, Duration.ofSeconds(2))).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerActionSequenceId()).isEqualTo(-1);
    }
  }

  @Test
  void appendsPublicChatMessagesAfterTheLocalPlayerChats() {
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
      ClientCore clientCore = new ClientCore();
      BridgedProtocolSession bridgedSession = new BridgedProtocolSession();
      GameplayClientSession gameplayClientSession =
          new GameplayClientSession(clientCore, bridgedSession, "integration-test");
      bridgedSession.bindInbound(gameplayClientSession::accept);
      PlayerSessionActor playerSessionActor = runtime.openSession(bridgedSession);
      bridgedSession.bindOutbound(playerSessionActor::accept);

      gameplayClientSession.bootstrap();
      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(bridgedSession.awaitMessageCount(5, Duration.ofSeconds(2))).isTrue();

      gameplayClientSession.sendPublicChat("sELLING   lobster. bUY nOW");
      assertThat(bridgedSession.awaitMessageCount(6, Duration.ofSeconds(2))).isTrue();
      assertThat(bridgedSession.receivedMessages()).last()
          .isEqualTo(new PublicChatMessage("Akira", "Selling lobster. Buy now"));
      assertThat(gameplayClientSession.viewModel().chatMessages()).singleElement()
          .satisfies(message -> assertThat(message.formattedText()).isEqualTo("Akira: Selling lobster. Buy now"));
    }
  }

  private static final class BridgedProtocolSession implements ProtocolSession, ClientTransport {

    private final UUID sessionId = UUID.randomUUID();
    private final List<ServerMessage> messages = new ArrayList<>();
    private Consumer<ServerMessage> inboundConsumer;
    private Consumer<ClientMessage> outboundConsumer;

    private void bindInbound(Consumer<ServerMessage> inboundConsumer) {
      this.inboundConsumer = inboundConsumer;
    }

    private void bindOutbound(Consumer<ClientMessage> outboundConsumer) {
      this.outboundConsumer = outboundConsumer;
    }

    @Override
    public UUID sessionId() {
      return sessionId;
    }

    @Override
    public void send(ServerMessage message) {
      synchronized (messages) {
        messages.add(message);
      }
      if (inboundConsumer != null) {
        inboundConsumer.accept(message);
      }
    }

    @Override
    public void send(ClientMessage message) {
      if (outboundConsumer != null) {
        outboundConsumer.accept(message);
      }
    }

    @Override
    public void close() {
    }

    private boolean awaitMessageCount(int expectedCount, Duration timeout) {
      long deadline = System.nanoTime() + timeout.toNanos();
      while (System.nanoTime() < deadline) {
        synchronized (messages) {
          if (messages.size() >= expectedCount) {
            return true;
          }
        }
        try {
          Thread.sleep(10L);
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
          throw new AssertionError("Interrupted while waiting for protocol messages", interruptedException);
        }
      }
      return false;
    }

    private List<ServerMessage> receivedMessages() {
      synchronized (messages) {
        return List.copyOf(messages);
      }
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

  private static final class TestNanoClock {

    private long now;

    long now() {
      return now;
    }

    void advanceNanos(long nanos) {
      now += nanos;
    }
  }
}
