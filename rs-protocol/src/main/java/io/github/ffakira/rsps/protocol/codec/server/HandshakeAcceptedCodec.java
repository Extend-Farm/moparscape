package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.session.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

public final class HandshakeAcceptedCodec implements PacketCodec<HandshakeAccepted> {

  @Override
  public int opcode() {
    return ServerOpcodes.HANDSHAKE_ACCEPTED;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<HandshakeAccepted> packetType() {
    return HandshakeAccepted.class;
  }

  @Override
  public void encode(PacketWriter out, HandshakeAccepted packet) {
    out.writeString(packet.protocolVersion().value(), ProtocolLimits.PROTOCOL_VERSION_BYTES);
    out.writeString(packet.motd(), ProtocolLimits.MOTD_BYTES);
  }

  @Override
  public HandshakeAccepted decode(PacketReader in) {
    return new HandshakeAccepted(
        new ProtocolVersion(in.readString(ProtocolLimits.PROTOCOL_VERSION_BYTES)),
        in.readString(ProtocolLimits.MOTD_BYTES)
    );
  }
}
