package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.protocol.codec.client.ActionSequenceIntentCodec;
import io.github.ffakira.rsps.protocol.codec.client.DisconnectNoticeCodec;
import io.github.ffakira.rsps.protocol.codec.client.HandshakeRequestCodec;
import io.github.ffakira.rsps.protocol.codec.client.LoginRequestCodec;
import io.github.ffakira.rsps.protocol.codec.client.MoveIntentCodec;
import io.github.ffakira.rsps.protocol.codec.server.CharacterBootstrapCodec;
import io.github.ffakira.rsps.protocol.codec.server.EntityActionSequenceCodec;
import io.github.ffakira.rsps.protocol.codec.server.EntityPositionCodec;
import io.github.ffakira.rsps.protocol.codec.server.HandshakeAcceptedCodec;
import io.github.ffakira.rsps.protocol.codec.server.LoginAcceptedCodec;
import io.github.ffakira.rsps.protocol.codec.server.LoginRejectedCodec;
import io.github.ffakira.rsps.protocol.codec.server.WorldSnapshotCodec;

final class ProtocolPacketCodecs {

  private ProtocolPacketCodecs() {
  }

  static PacketCodecRegistry createRegistry() {
    return PacketCodecRegistry.create(
        new HandshakeRequestCodec(),
        new LoginRequestCodec(),
        new MoveIntentCodec(),
        new ActionSequenceIntentCodec(),
        new DisconnectNoticeCodec(),
        new HandshakeAcceptedCodec(),
        new LoginAcceptedCodec(),
        new LoginRejectedCodec(),
        new CharacterBootstrapCodec(),
        new WorldSnapshotCodec(),
        new EntityPositionCodec(),
        new EntityActionSequenceCodec()
    );
  }
}
