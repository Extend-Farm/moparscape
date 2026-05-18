package io.github.ffakira.rsps.cache;

public record CacheArchiveCompressionHeader(
    int decompressedLength,
    int storedLength
) {

  public static final int HEADER_LENGTH = 6;

  public static CacheArchiveCompressionHeader parse(byte[] archiveBytes) {
    if (archiveBytes.length < HEADER_LENGTH) {
      throw new IllegalArgumentException("Archive bytes are too short: " + archiveBytes.length);
    }
    int decompressedLength = readMedium(archiveBytes, 0);
    int storedLength = readMedium(archiveBytes, 3);
    return new CacheArchiveCompressionHeader(decompressedLength, storedLength);
  }

  public boolean compressed() {
    return decompressedLength != storedLength;
  }

  private static int readMedium(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 16)
        | ((bytes[offset + 1] & 0xff) << 8)
        | (bytes[offset + 2] & 0xff);
  }
}
