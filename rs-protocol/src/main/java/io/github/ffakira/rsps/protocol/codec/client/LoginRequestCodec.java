package io.github.ffakira.rsps.protocol.codec.client;

import io.github.ffakira.rsps.protocol.session.LoginRequest;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ClientOpcodes;

public final class LoginRequestCodec implements PacketCodec<LoginRequest> {

  @Override
  public int opcode() {
    return ClientOpcodes.LOGIN_REQUEST;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.CLIENT_TO_SERVER;
  }

  @Override
  public Class<LoginRequest> packetType() {
    return LoginRequest.class;
  }

  @Override
  public void encode(PacketWriter out, LoginRequest packet) {
    out.writeString(packet.username(), ProtocolLimits.USERNAME_BYTES);
    out.writeString(packet.password(), ProtocolLimits.PASSWORD_BYTES);
  }

  @Override
  public LoginRequest decode(PacketReader in) {
    return new LoginRequest(
        in.readString(ProtocolLimits.USERNAME_BYTES),
        in.readString(ProtocolLimits.PASSWORD_BYTES)
    );
  }
}
