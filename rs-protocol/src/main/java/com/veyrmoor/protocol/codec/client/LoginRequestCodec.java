package com.veyrmoor.protocol.codec.client;

import com.veyrmoor.protocol.session.LoginRequest;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ClientOpcodes;

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
