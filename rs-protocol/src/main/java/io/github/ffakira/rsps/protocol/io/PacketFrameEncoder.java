package io.github.ffakira.rsps.protocol.io;

import io.github.ffakira.rsps.protocol.PacketFrame;

public final class PacketFrameEncoder {

  private PacketFrameEncoder() {
  }

  public static byte[] encode(PacketFrame frame) {
    PacketWriter writer = new PacketWriter();
    writer.writeShort(frame.opcode());
    writer.writeBytes(frame.payload());
    return writer.toByteArray();
  }
}
