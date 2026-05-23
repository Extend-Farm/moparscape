package com.veyrmoor.protocol;

import com.veyrmoor.protocol.io.PacketReader;
import com.veyrmoor.protocol.io.PacketWriter;

public interface PacketCodec<T extends Packet> {

  int opcode();

  PacketDirection direction();

  Class<T> packetType();

  void encode(PacketWriter out, T packet);

  T decode(PacketReader in);
}
