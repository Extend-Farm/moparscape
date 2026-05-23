package com.veyrmoor.cache;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

public final class CacheArchiveCompression {

  private static final byte[] BZIP_STREAM_HEADER = new byte[] {'B', 'Z', 'h', '1'};

  private CacheArchiveCompression() {
  }

  public static byte[] decompressBzipBlock(byte[] compressedBytes, int expectedLength) {
    byte[] prefixedBytes = new byte[BZIP_STREAM_HEADER.length + compressedBytes.length];
    System.arraycopy(BZIP_STREAM_HEADER, 0, prefixedBytes, 0, BZIP_STREAM_HEADER.length);
    System.arraycopy(compressedBytes, 0, prefixedBytes, BZIP_STREAM_HEADER.length, compressedBytes.length);

    try (BZip2CompressorInputStream inputStream =
             new BZip2CompressorInputStream(new ByteArrayInputStream(prefixedBytes), true)) {
      byte[] decompressedBytes = inputStream.readNBytes(expectedLength);
      if (decompressedBytes.length != expectedLength) {
        throw new IllegalStateException(
            "Expected " + expectedLength + " decompressed bytes but got " + decompressedBytes.length
        );
      }
      return decompressedBytes;
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to decompress cache archive block", ioException);
    }
  }
}
