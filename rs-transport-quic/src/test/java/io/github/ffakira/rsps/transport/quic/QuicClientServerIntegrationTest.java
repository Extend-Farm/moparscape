package io.github.ffakira.rsps.transport.quic;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.core.ClientCore;
import io.github.ffakira.rsps.client.core.GameplayClientSession;
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
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.server.runtime.InProcessServerRuntime;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class QuicClientServerIntegrationTest {

  @TempDir
  Path tempDirectory;

  @Test
  void reachesWorldAndPropagatesMovementOverQuic() throws Exception {
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
            new CharacterItemSlot(ItemContainerKind.EQUIPMENT, 2, 1712, 1)
        ),
        List.of()
    );
    int port = nextFreePort();
    QuicTransportConfiguration configuration = new QuicTransportConfiguration(
        "localhost",
        "127.0.0.1",
        port,
        ProtocolVersion.current().value(),
        tempDirectory.resolve("quic")
    );
    ConcurrentLinkedQueue<ServerMessage> inboundMessages = new ConcurrentLinkedQueue<>();

    try (InProcessServerRuntime runtime = new InProcessServerRuntime(
        new InMemoryAccountRepository(accountRecord),
        new InMemoryCharacterRepository(characterSnapshot)
    );
         QuicServerEndpoint ignored = QuicServerEndpoint.start(runtime, configuration);
         QuicClientTransport transport = QuicClientTransport.connect(configuration, inboundMessages::add)) {
      GameplayClientSession gameplayClientSession = new GameplayClientSession(new ClientCore(), transport, "integration-test");

      gameplayClientSession.connect();
      gameplayClientSession.login("akira", "swordfish");
      assertThat(awaitWorldBootstrap(gameplayClientSession, inboundMessages, Duration.ofSeconds(5))).isTrue();

      gameplayClientSession.move(1, 0, MovementMode.WALK);
      assertThat(awaitPosition(gameplayClientSession, inboundMessages, new WorldPoint(3201, 3201, 0), Duration.ofSeconds(5)))
          .isTrue();

      assertThat(gameplayClientSession.viewModel().loggedIn()).isTrue();
      assertThat(gameplayClientSession.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3201, 3201, 0));
      assertThat(gameplayClientSession.viewModel().inventory().getFirst().itemId()).isEqualTo(555);
    }
  }

  private static boolean awaitWorldBootstrap(
      GameplayClientSession gameplayClientSession,
      ConcurrentLinkedQueue<ServerMessage> inboundMessages,
      Duration timeout
  ) {
    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      drainMessages(gameplayClientSession, inboundMessages);
      if (gameplayClientSession.viewModel().loggedIn() && gameplayClientSession.viewModel().localPlayerPosition() != null) {
        return true;
      }
      sleepBriefly();
    }
    return false;
  }

  private static boolean awaitPosition(
      GameplayClientSession gameplayClientSession,
      ConcurrentLinkedQueue<ServerMessage> inboundMessages,
      WorldPoint expectedPosition,
      Duration timeout
  ) {
    long deadline = System.nanoTime() + timeout.toNanos();
    while (System.nanoTime() < deadline) {
      drainMessages(gameplayClientSession, inboundMessages);
      gameplayClientSession.pumpMovement();
      if (expectedPosition.equals(gameplayClientSession.viewModel().localPlayerPosition())) {
        return true;
      }
      sleepBriefly();
    }
    return false;
  }

  private static void drainMessages(
      GameplayClientSession gameplayClientSession,
      ConcurrentLinkedQueue<ServerMessage> inboundMessages
  ) {
    ServerMessage message;
    while ((message = inboundMessages.poll()) != null) {
      gameplayClientSession.accept(message);
    }
  }

  private static int nextFreePort() throws Exception {
    try (ServerSocket serverSocket = new ServerSocket(0)) {
      return serverSocket.getLocalPort();
    }
  }

  private static void sleepBriefly() {
    try {
      Thread.sleep(10L);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      throw new AssertionError("Interrupted while waiting for QUIC gameplay state", interruptedException);
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
}
