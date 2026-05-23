package com.veyrmoor.cache;

final class CacheDataReader {

  private final byte[] bytes;
  private int position;

  CacheDataReader(byte[] bytes) {
    this.bytes = bytes;
  }

  void position(int position) {
    this.position = position;
  }

  int readUnsignedByte() {
    return bytes[position++] & 0xff;
  }

  int readUnsignedShort() {
    int value = ((bytes[position] & 0xff) << 8) | (bytes[position + 1] & 0xff);
    position += 2;
    return value;
  }

  int readSignedSmart() {
    int value = bytes[position] & 0xff;
    if (value < 128) {
      return readUnsignedByte() - 64;
    }
    return readUnsignedShort() - 49152;
  }
}
