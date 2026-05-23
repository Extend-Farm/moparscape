package com.veyrmoor.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.veyrmoor.model.MovementMode;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.BootstrapAnimationProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapMessage;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import com.veyrmoor.protocol.chat.PublicChatMessage;
import com.veyrmoor.protocol.input.ActionSequenceIntentMessage;
import com.veyrmoor.protocol.input.MoveIntentMessage;
import com.veyrmoor.protocol.input.PublicChatIntentMessage;
import com.veyrmoor.protocol.session.DisconnectNotice;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.session.LoginAccepted;
import com.veyrmoor.protocol.session.LoginRejected;
import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.protocol.world.EntityPositionMessage;
import com.veyrmoor.protocol.world.WorldSnapshotMessage;
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
        new PublicChatIntentMessage("Selling lobster"),
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
        new EntityActionSequenceMessage(1, 866),
        new PublicChatMessage("Akira", "Selling lobster")
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
