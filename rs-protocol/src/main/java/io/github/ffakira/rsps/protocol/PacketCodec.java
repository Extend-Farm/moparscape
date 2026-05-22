package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.protocol.io.PacketReader;
import io.github.ffakira.rsps.protocol.io.PacketWriter;

public interface PacketCodec<T extends Packet> {

  int opcode();

  PacketDirection direction();

  Class<T> packetType();

  void encode(PacketWriter out, T packet);

  T decode(PacketReader in);
}
