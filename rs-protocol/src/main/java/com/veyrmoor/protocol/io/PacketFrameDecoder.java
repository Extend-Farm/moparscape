package com.veyrmoor.protocol.io;

import com.veyrmoor.protocol.PacketFrame;
import com.veyrmoor.protocol.ProtocolCodecException;

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
