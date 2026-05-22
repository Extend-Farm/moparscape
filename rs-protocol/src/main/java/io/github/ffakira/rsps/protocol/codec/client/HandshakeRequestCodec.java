package io.github.ffakira.rsps.protocol.codec.client;

import io.github.ffakira.rsps.protocol.session.HandshakeRequest;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ClientOpcodes;

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
