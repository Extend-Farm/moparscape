package io.github.ffakira.rsps.content;

import java.nio.charset.StandardCharsets;

final class ContentDataReader {

  private final byte[] bytes;
  private int position;

  ContentDataReader(byte[] bytes) {
    this.bytes = bytes;
  }

  void position(int position) {
    this.position = position;
  }

  int readUnsignedByte() {
    return bytes[position++] & 0xff;
  }

  int readSignedByte() {
    return bytes[position++];
  }

  int readUnsignedShort() {
    int value = ((bytes[position] & 0xff) << 8) | (bytes[position + 1] & 0xff);
    position += 2;
    return value;
  }

  int readSignedShort() {
    int value = readUnsignedShort();
    if (value > 0x7fff) {
      value -= 0x10000;
    }
    return value;
  }

  int readInt() {
    int value = ((bytes[position] & 0xff) << 24)
        | ((bytes[position + 1] & 0xff) << 16)
        | ((bytes[position + 2] & 0xff) << 8)
        | (bytes[position + 3] & 0xff);
    position += 4;
    return value;
  }

  int readUnsignedSmart() {
    int value = bytes[position] & 0xff;
    if (value < 128) {
      return readUnsignedByte();
    }
    return readUnsignedShort() - 32768;
  }

  String readString() {
    int start = position;
    while (bytes[position++] != 10) {
      // scan to terminator
    }
    return new String(bytes, start, position - start - 1, StandardCharsets.ISO_8859_1);
  }

  void readByteArray() {
    while (bytes[position++] != 10) {
      // scan to terminator
    }
  }

  void skip(int byteCount) {
    position += byteCount;
  }
}
