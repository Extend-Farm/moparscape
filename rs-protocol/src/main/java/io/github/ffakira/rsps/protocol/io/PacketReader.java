package io.github.ffakira.rsps.protocol.io;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.protocol.ProtocolCodecException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class PacketReader {

  private final ByteArrayInputStream inputStream;
  private final DataInputStream input;

  public PacketReader(byte[] payload) {
    this.inputStream = new ByteArrayInputStream(payload);
    this.input = new DataInputStream(inputStream);
  }

  public void assertFullyRead() {
    if (inputStream.available() != 0) {
      throw new ProtocolCodecException("Trailing bytes remain after packet decode");
    }
  }

  public boolean readBoolean() {
    return readUnchecked(input::readBoolean);
  }

  public int readSignedByte() {
    return readUnchecked(input::readByte);
  }

  public int readUnsignedByte() {
    return readUnchecked(input::readUnsignedByte);
  }

  public int readUnsignedShort() {
    return readUnchecked(input::readUnsignedShort);
  }

  public int readInt() {
    return readUnchecked(input::readInt);
  }

  public long readLong() {
    return readUnchecked(input::readLong);
  }

  public MovementMode readMovementMode() {
    try {
      return MovementMode.fromProtocolId(readUnsignedByte());
    } catch (IllegalArgumentException exception) {
      throw new ProtocolCodecException(exception.getMessage(), exception);
    }
  }

  public byte[] readRemainingBytes() {
    return readBytes(inputStream.available());
  }

  public byte[] readBytes(int expectedLength) {
    if (expectedLength < 0) {
      throw new ProtocolCodecException("Negative byte length: " + expectedLength);
    }
    byte[] bytes = new byte[expectedLength];
    int actualLength = readUnchecked(() -> input.read(bytes));
    if (actualLength != expectedLength) {
      throw new ProtocolCodecException(
          "Unexpected end of packet payload while reading %d bytes".formatted(expectedLength)
      );
    }
    return bytes;
  }

  public int readListSize(int maxEntries) {
    int size = readUnsignedShort();
    if (size > maxEntries) {
      throw new ProtocolCodecException("List size %d exceeds %d".formatted(size, maxEntries));
    }
    return size;
  }

  public String readString(int maxBytes) {
    int byteLength = readUnsignedShort();
    if (byteLength > maxBytes) {
      throw new ProtocolCodecException("String length %d exceeds %d".formatted(byteLength, maxBytes));
    }
    return new String(readBytes(byteLength), StandardCharsets.UTF_8);
  }

  @FunctionalInterface
  private interface IoRead<T> {

    T read() throws IOException;
  }

  private static <T> T readUnchecked(IoRead<T> ioRead) {
    try {
      return ioRead.read();
    } catch (IOException exception) {
      throw new ProtocolCodecException("Unable to decode packet payload", exception);
    }
  }
}
