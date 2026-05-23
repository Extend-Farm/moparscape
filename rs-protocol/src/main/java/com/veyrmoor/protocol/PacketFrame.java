package com.veyrmoor.protocol;

import java.util.Arrays;

public record PacketFrame(int opcode, byte[] payload) {

  public PacketFrame {
    if (opcode < 0 || opcode > 0xFFFF) {
      throw new IllegalArgumentException("Packet opcode out of bounds: " + opcode);
    }
    payload = payload == null ? new byte[0] : Arrays.copyOf(payload, payload.length);
  }
}
