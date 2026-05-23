package com.veyrmoor.server.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.protocol.bootstrap.BootstrapAnimationProfile;
import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapMessage;
import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginAccepted;
import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.input.MoveIntentMessage;
import com.veyrmoor.protocol.ProtocolSession;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.ServerMessage;
import com.veyrmoor.protocol.world.WorldSnapshotMessage;
import com.veyrmoor.model.MovementMode;
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
              message -> {
                assertThat(message.bootstrap().appearance().animationProfile()).isEqualTo(BootstrapAnimationProfile.referencePlayer());
                assertThat(message.bootstrap().inventory()).singleElement()
                    .satisfies(slot -> assertThat(slot.itemId()).isEqualTo(554));
              }
          );
      assertThat(session.messages().get(3)).isInstanceOf(WorldSnapshotMessage.class);
      assertThat(session.messages().get(4))
          .isInstanceOfSatisfying(
              com.veyrmoor.protocol.world.EntityPositionMessage.class,
              message -> assertThat(message.worldPoint().x()).isEqualTo(3250)
          );
      assertThat(session.messages().get(5))
          .isInstanceOfSatisfying(
              com.veyrmoor.protocol.world.EntityPositionMessage.class,
              message -> assertThat(message.worldPoint().x()).isEqualTo(3251)
          );
    } finally {
      worldShardActor.close();
    }
  }

  @Test
  void echoesActionSequenceChangesForAuthenticatedSessions() throws Exception {
    Path charactersDirectory = Files.createDirectory(tempDirectory.resolve("action-characters"));
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
      coordinator.handleActionSequence(session, new ActionSequenceIntentMessage(866));

      assertThat(session.messages().get(5)).isEqualTo(new EntityActionSequenceMessage(1, 866));
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
