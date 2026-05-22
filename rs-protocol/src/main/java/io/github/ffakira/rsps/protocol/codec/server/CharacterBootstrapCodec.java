package io.github.ffakira.rsps.protocol.codec.server;

import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.PacketCodec;
import io.github.ffakira.rsps.protocol.PacketDirection;
import io.github.ffakira.rsps.protocol.codec.shared.CharacterBootstrapPayloadCodec;
import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;
import io.github.ffakira.rsps.protocol.opcode.ServerOpcodes;

public final class CharacterBootstrapCodec implements PacketCodec<CharacterBootstrapMessage> {

  @Override
  public int opcode() {
    return ServerOpcodes.CHARACTER_BOOTSTRAP;
  }

  @Override
  public PacketDirection direction() {
    return PacketDirection.SERVER_TO_CLIENT;
  }

  @Override
  public Class<CharacterBootstrapMessage> packetType() {
    return CharacterBootstrapMessage.class;
  }

  @Override
  public void encode(PacketWriter out, CharacterBootstrapMessage packet) {
    CharacterBootstrapPayloadCodec.INSTANCE.encode(out, packet.bootstrap());
  }

  @Override
  public CharacterBootstrapMessage decode(PacketReader in) {
    return new CharacterBootstrapMessage(CharacterBootstrapPayloadCodec.INSTANCE.decode(in));
  }
}
