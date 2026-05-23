package com.veyrmoor.protocol;

import com.veyrmoor.protocol.codec.client.ActionSequenceIntentCodec;
import com.veyrmoor.protocol.codec.client.DisconnectNoticeCodec;
import com.veyrmoor.protocol.codec.client.HandshakeRequestCodec;
import com.veyrmoor.protocol.codec.client.LoginRequestCodec;
import com.veyrmoor.protocol.codec.client.MoveIntentCodec;
import com.veyrmoor.protocol.codec.client.PublicChatIntentCodec;
import com.veyrmoor.protocol.codec.server.CharacterBootstrapCodec;
import com.veyrmoor.protocol.codec.server.EntityActionSequenceCodec;
import com.veyrmoor.protocol.codec.server.EntityPositionCodec;
import com.veyrmoor.protocol.codec.server.HandshakeAcceptedCodec;
import com.veyrmoor.protocol.codec.server.LoginAcceptedCodec;
import com.veyrmoor.protocol.codec.server.LoginRejectedCodec;
import com.veyrmoor.protocol.codec.server.PublicChatCodec;
import com.veyrmoor.protocol.codec.server.WorldSnapshotCodec;

final class ProtocolPacketCodecs {

  private ProtocolPacketCodecs() {
  }

  static PacketCodecRegistry createRegistry() {
    return PacketCodecRegistry.create(
        new HandshakeRequestCodec(),
        new LoginRequestCodec(),
        new MoveIntentCodec(),
        new ActionSequenceIntentCodec(),
        new PublicChatIntentCodec(),
        new DisconnectNoticeCodec(),
        new HandshakeAcceptedCodec(),
        new LoginAcceptedCodec(),
        new LoginRejectedCodec(),
        new CharacterBootstrapCodec(),
        new WorldSnapshotCodec(),
        new EntityPositionCodec(),
        new EntityActionSequenceCodec(),
        new PublicChatCodec()
    );
  }
}
