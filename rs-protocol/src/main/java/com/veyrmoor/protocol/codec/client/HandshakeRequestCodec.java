package com.veyrmoor.protocol.codec.client;

import com.veyrmoor.protocol.session.HandshakeRequest;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.ProtocolVersion;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;

public final class HandshakeRequestCodec implements PacketCodec<HandshakeRequest> {

  @Override
  public int opcode() {
    return ClientOpcodes.HANDSHAKE_REQUEST;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<HandshakeRequest> packetType() {
    return HandshakeRequest.class;
  }

  @Override
  public void encode(PacketWriter out, HandshakeRequest packet) {
    out.writeString(packet.protocolVersion().value(), ProtocolLimits.PROTOCOL_VERSION_BYTES);
    out.writeString(packet.clientBuild(), ProtocolLimits.CLIENT_BUILD_BYTES);
  }

  @Override
  public HandshakeRequest decode(PacketReader in) {
    return new HandshakeRequest(
        new ProtocolVersion(in.readString(ProtocolLimits.PROTOCOL_VERSION_BYTES)),
        in.readString(ProtocolLimits.CLIENT_BUILD_BYTES)
    );
  }
}
