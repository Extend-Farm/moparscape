package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.session.LoginRejected;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.ProtocolLimits;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

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
