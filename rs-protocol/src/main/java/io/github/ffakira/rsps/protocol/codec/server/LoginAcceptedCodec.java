package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.session.LoginAccepted;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.codec.shared.WorldPointCodec;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

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
