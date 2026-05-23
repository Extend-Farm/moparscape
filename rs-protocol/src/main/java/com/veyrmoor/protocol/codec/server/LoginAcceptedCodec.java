package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.session.LoginAccepted;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.codec.shared.WorldPointCodec;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

public final class LoginAcceptedCodec implements PacketCodec<LoginAccepted> {

  @Override
  public int opcode() {
    return ServerOpcodes.LOGIN_ACCEPTED;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<LoginAccepted> packetType() {
    return LoginAccepted.class;
  }

  @Override
  public void encode(PacketWriter out, LoginAccepted packet) {
    out.writeLong(packet.accountId());
    out.writeLong(packet.characterId());
    WorldPointCodec.INSTANCE.encode(out, packet.spawnPoint());
  }

  @Override
  public LoginAccepted decode(PacketReader in) {
    return new LoginAccepted(in.readLong(), in.readLong(), WorldPointCodec.INSTANCE.decode(in));
  }
}
