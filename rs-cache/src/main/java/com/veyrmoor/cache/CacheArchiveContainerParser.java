package com.veyrmoor.cache;

import java.util.ArrayList;
import java.util.List;

public class CacheArchiveContainerParser {

  public CacheArchiveContainer parse(RawCacheArchive rawArchive) {
    CacheArchiveCompressionHeader compressionHeader = rawArchive.compressionHeader();
    boolean compressedAsWhole = compressionHeader.compressed();
    byte[] containerBytes = compressedAsWhole
        ? parseCompressedContainer(rawArchive.bytes(), compressionHeader)
        : rawArchive.bytes();

    int cursor = compressedAsWhole ? 0 : CacheArchiveCompressionHeader.HEADER_LENGTH;
    int entryCount = readUnsignedShort(containerBytes, cursor);
    cursor += 2;

    int dataOffset = cursor + entryCount * 10;
    List<ArchiveEntryReference> entryReferences = new ArrayList<>(entryCount);
    for (int index = 0; index < entryCount; index++) {
      int nameHash = readInt(containerBytes, cursor);
      int uncompressedLength = readMedium(containerBytes, cursor + 4);
      int storedLength = readMedium(containerBytes, cursor + 7);
      entryReferences.add(new ArchiveEntryReference(nameHash, uncompressedLength, storedLength, dataOffset));
      dataOffset += storedLength;
      cursor += 10;
    }

    return new CacheArchiveContainer(rawArchive, compressedAsWhole, containerBytes, List.copyOf(entryReferences));
  }

  private byte[] parseCompressedContainer(byte[] archiveBytes, CacheArchiveCompressionHeader compressionHeader) {
    byte[] compressedBytes = new byte[compressionHeader.storedLength()];
    System.arraycopy(
        archiveBytes,
        CacheArchiveCompressionHeader.HEADER_LENGTH,
        compressedBytes,
        0,
        compressedBytes.length
    );
    return CacheArchiveCompression.decompressBzipBlock(compressedBytes, compressionHeader.decompressedLength());
  }

  private static int readUnsignedShort(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 8) | (bytes[offset + 1] & 0xff);
  }

  private static int readMedium(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 16)
        | ((bytes[offset + 1] & 0xff) << 8)
        | (bytes[offset + 2] & 0xff);
  }

  private static int readInt(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 24)
        | ((bytes[offset + 1] & 0xff) << 16)
        | ((bytes[offset + 2] & 0xff) << 8)
        | (bytes[offset + 3] & 0xff);
  }
}
