package io.github.ffakira.rsps.server.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.protocol.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.HandshakeRequest;
import io.github.ffakira.rsps.protocol.LoginAccepted;
import io.github.ffakira.rsps.protocol.LoginRequest;
import io.github.ffakira.rsps.protocol.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.ProtocolSession;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.protocol.WorldSnapshotMessage;
import io.github.ffakira.rsps.model.MovementMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class GameplayRuntimeCoordinatorTest {

  @TempDir
  Path tempDirectory;

  @Test
  void authenticatesAndMovesUsingCharacterFiles() throws Exception {
    Path charactersDirectory = Files.createDirectory(tempDirectory.resolve("characters"));
    Files.writeString(
        charactersDirectory.resolve("akira.txt"),
        """
        [ACCOUNT]
        character-username = akira
        character-password = password

        [CHARACTER]
        character-height = 0
        character-posx = 3250
        character-posy = 3227
        character-rights = 2

        [LOOK]
        character-look = 0 -1
        character-look = 1 -1
        character-look = 2 -1
        character-look = 3 -1
        character-look = 4 -1
        character-look = 5 -1

        [SKILLS]
        character-skill = 0 99 14000000

        [ITEMS]
        character-item = 0 555 1000
        [EOF]
        """,
        StandardCharsets.UTF_8
    );

    WorldShardActor worldShardActor = new WorldShardActor();
    worldShardActor.start();
    try {
      CharacterFileRepository repository = new CharacterFileRepository(charactersDirectory);
      GameplayRuntimeCoordinator coordinator =
          new GameplayRuntimeCoordinator(repository, repository, worldShardActor);
      RecordingProtocolSession session = new RecordingProtocolSession();

      coordinator.handleHandshake(session, new HandshakeRequest(ProtocolVersion.current(), "test-client"));
      coordinator.handleLogin(session, new LoginRequest("akira", "password"));
      coordinator.handleMove(session, new MoveIntentMessage(1, 0, MovementMode.WALK));

      assertThat(session.messages().get(0)).isInstanceOf(HandshakeAccepted.class);
      assertThat(session.messages().get(1)).isInstanceOf(LoginAccepted.class);
      assertThat(session.messages().get(2))
          .isInstanceOfSatisfying(
              CharacterBootstrapMessage.class,
              message -> assertThat(message.bootstrap().inventory()).singleElement()
                  .satisfies(slot -> assertThat(slot.itemId()).isEqualTo(555))
          );
      assertThat(session.messages().get(3)).isInstanceOf(WorldSnapshotMessage.class);
      assertThat(session.messages().get(4))
          .isInstanceOfSatisfying(
              io.github.ffakira.rsps.protocol.EntityPositionMessage.class,
              message -> assertThat(message.worldPoint().x()).isEqualTo(3250)
          );
      assertThat(session.messages().get(5))
          .isInstanceOfSatisfying(
              io.github.ffakira.rsps.protocol.EntityPositionMessage.class,
              message -> assertThat(message.worldPoint().x()).isEqualTo(3251)
          );
    } finally {
      worldShardActor.close();
    }
  }

  private static final class RecordingProtocolSession implements ProtocolSession {

    private final UUID sessionId = UUID.randomUUID();
    private final List<ServerMessage> messages = new ArrayList<>();

    @Override
    public UUID sessionId() {
      return sessionId;
    }

    @Override
    public void send(ServerMessage message) {
      messages.add(message);
    }

    @Override
    public void close() {
    }

    private List<ServerMessage> messages() {
      return List.copyOf(messages);
    }
  }
}
