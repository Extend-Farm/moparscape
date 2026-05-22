package io.github.ffakira.rsps.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAnimationProfile;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapProfile;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapSkill;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapPayload;
import io.github.ffakira.rsps.protocol.input.ActionSequenceIntentMessage;
import io.github.ffakira.rsps.protocol.input.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.session.DisconnectNotice;
import io.github.ffakira.rsps.protocol.session.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.session.HandshakeRequest;
import io.github.ffakira.rsps.protocol.session.LoginAccepted;
import io.github.ffakira.rsps.protocol.session.LoginRejected;
import io.github.ffakira.rsps.protocol.session.LoginRequest;
import io.github.ffakira.rsps.protocol.world.EntityActionSequenceMessage;
import io.github.ffakira.rsps.protocol.world.EntityPositionMessage;
import io.github.ffakira.rsps.protocol.world.WorldSnapshotMessage;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProtocolBinaryCodecTest {

  @Test
  void roundTripsEveryClientMessage() {
    List<ClientMessage> messages = List.of(
        new HandshakeRequest(ProtocolVersion.current(), "rs-client-lwjgl"),
        new LoginRequest("akira", "swordfish"),
        new MoveIntentMessage(2, -1, MovementMode.RUN),
        new ActionSequenceIntentMessage(866),
        new DisconnectNotice("Client shutdown")
    );

    for (ClientMessage message : messages) {
      assertThat(ProtocolBinaryCodec.decodeClientMessage(ProtocolBinaryCodec.encodeClientMessage(message)))
          .isEqualTo(message);
    }
  }

  @Test
  void roundTripsEveryServerMessage() {
    CharacterBootstrapPayload bootstrap = new CharacterBootstrapPayload(
        101L,
        202L,
        "Akira",
        "50_50",
        new WorldPoint(3200, 3201, 0),
        new BootstrapProfile((short) 2, true, 100),
        new BootstrapAppearance(
            List.of(0x100, 0x101, 0x102, 0x103, 0x104, 0x105),
            new BootstrapAnimationProfile(808, 809, 810, 811, 812, 813, 814)
        ),
        List.of(new BootstrapItemSlot(0, 555, 1000)),
        List.of(new BootstrapItemSlot(2, 1712, 1)),
        List.of(new BootstrapSkill(0, 99, 14_000_000))
    );
    List<ServerMessage> messages = List.of(
        new HandshakeAccepted(ProtocolVersion.current(), "Welcome back"),
        new LoginAccepted(101L, 202L, new WorldPoint(3200, 3201, 0)),
        new LoginRejected("Invalid credentials"),
        new CharacterBootstrapMessage(bootstrap),
        new WorldSnapshotMessage("50_50", new WorldPoint(3200, 3201, 0)),
        new EntityPositionMessage(1, new WorldPoint(3201, 3201, 0)),
        new EntityActionSequenceMessage(1, 866)
    );

    for (ServerMessage message : messages) {
      assertThat(ProtocolBinaryCodec.decodeServerMessage(ProtocolBinaryCodec.encodeServerMessage(message)))
          .isEqualTo(message);
    }
  }

  @Test
  void rejectsMovementDeltasOutsideTheProtocolRange() {
    assertThatThrownBy(() -> ProtocolBinaryCodec.decodeClientMessage(new byte[] {
        0,
        3,
        3,
        0,
        1
    })).isInstanceOf(ProtocolCodecException.class)
        .hasMessageContaining("Invalid movement delta");
  }
}
