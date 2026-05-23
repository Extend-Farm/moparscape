package com.veyrmoor.protocol.io;

import com.veyrmoor.model.MovementMode;
import com.veyrmoor.protocol.ProtocolCodecException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class PacketWriter {

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final DataOutputStream output = new DataOutputStream(outputStream);

  public void writeBoolean(boolean value) {
    writeUnchecked(() -> output.writeBoolean(value));
  }

  public void writeByte(int value) {
    writeUnchecked(() -> output.writeByte(value));
  }

  public void writeBytes(byte[] value) {
    byte[] bytes = Objects.requireNonNull(value, "value");
    writeUnchecked(() -> output.write(bytes));
  }

  public void writeInt(int value) {
    writeUnchecked(() -> output.writeInt(value));
  }

  public void writeLong(long value) {
    writeUnchecked(() -> output.writeLong(value));
  }

  public void writeShort(int value) {
    writeUnchecked(() -> output.writeShort(value));
  }

  public void writeMovementMode(MovementMode movementMode) {
    writeByte(Objects.requireNonNull(movementMode, "movementMode").protocolId());
  }

  public void writeString(String value, int maxBytes) {
    String text = Objects.requireNonNull(value, "value");
    byte[] encoded = text.getBytes(StandardCharsets.UTF_8);
    if (encoded.length > maxBytes) {
      throw new ProtocolCodecException("String exceeds " + maxBytes + " bytes");
    }
    writeShort(encoded.length);
    writeBytes(encoded);
  }

  public byte[] toByteArray() {
    return outputStream.toByteArray();
  }

  @FunctionalInterface
  private interface IoWrite {

    void write() throws IOException;
  }

  private static void writeUnchecked(IoWrite ioWrite) {
    try {
      ioWrite.write();
    } catch (IOException exception) {
      throw new ProtocolCodecException("Unable to encode packet payload", exception);
    }
  }
}
