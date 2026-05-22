package io.github.ffakira.rsps.protocol.io;

import io.github.ffakira.rsps.protocol.PacketFrame;
import io.github.ffakira.rsps.protocol.ProtocolCodecException;

public final class PacketFrameDecoder {

  private PacketFrameDecoder() {
  }

  public static PacketFrame decode(byte[] encodedFrame) {
    PacketReader reader = new PacketReader(encodedFrame);
    if (encodedFrame.length < 2) {
      throw new ProtocolCodecException("Packet frame is too short");
    }
    int opcode = reader.readUnsignedShort();
    byte[] payload = reader.readRemainingBytes();
    reader.assertFullyRead();
    return new PacketFrame(opcode, payload);
  }
}
