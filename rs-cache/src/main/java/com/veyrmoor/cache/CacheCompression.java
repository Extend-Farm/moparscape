package com.veyrmoor.cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

final class CacheCompression {

  private CacheCompression() {
  }

  static byte[] decompressGzipIfNeeded(byte[] bytes, String description) {
    if (bytes.length < 2 || (bytes[0] & 0xff) != 0x1f || (bytes[1] & 0xff) != 0x8b) {
      return bytes;
    }
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
      return gzipInputStream.readAllBytes();
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to decompress " + description, ioException);
    }
  }
}
