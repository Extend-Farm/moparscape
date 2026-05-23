package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

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
