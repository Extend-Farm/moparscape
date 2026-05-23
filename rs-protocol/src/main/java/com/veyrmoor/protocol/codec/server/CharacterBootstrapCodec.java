package com.veyrmoor.protocol.codec.server;

import com.veyrmoor.protocol.bootstrap.CharacterBootstrapMessage;
import com.veyrmoor.protocol.PacketCodec;
import com.veyrmoor.protocol.PacketDirection;
import com.veyrmoor.protocol.codec.shared.CharacterBootstrapPayloadCodec;
import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;
import com.veyrmoor.protocol.opcode.ServerOpcodes;

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
