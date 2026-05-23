package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.session.LoginRejected;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.ProtocolLimits;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

public final class LoginRejectedCodec implements PacketCodec<LoginRejected> {

  @Override
  public int opcode() {
    return ServerOpcodes.LOGIN_REJECTED;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<LoginRejected> packetType() {
    return LoginRejected.class;
  }

  @Override
  public void encode(PacketWriter out, LoginRejected packet) {
    out.writeString(packet.reason(), ProtocolLimits.DISCONNECT_REASON_BYTES);
  }

  @Override
  public LoginRejected decode(PacketReader in) {
    return new LoginRejected(in.readString(ProtocolLimits.DISCONNECT_REASON_BYTES));
  }
}
