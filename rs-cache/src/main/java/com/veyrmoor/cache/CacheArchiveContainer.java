package com.veyrmoor.cache;

import java.util.List;

public record CacheArchiveContainer(
    RawCacheArchive rawArchive,
    boolean compressedAsWhole,
    byte[] containerBytes,
    List<ArchiveEntryReference> entryReferences
) {

  public ArchiveEntryReference entryByName(String entryName) {
    int nameHash = ArchiveNameHasher.hash(entryName);
    for (ArchiveEntryReference entryReference : entryReferences) {
      if (entryReference.nameHash() == nameHash) {
        return entryReference;
      }
    }
    throw new IllegalStateException("Missing archive entry " + entryName);
  }

  public byte[] readEntry(String entryName) {
    return readEntry(entryByName(entryName));
  }

  public byte[] readEntry(ArchiveEntryReference entryReference) {
    byte[] storedBytes = new byte[entryReference.storedLength()];
    System.arraycopy(containerBytes, entryReference.dataOffset(), storedBytes, 0, storedBytes.length);
    if (compressedAsWhole) {
      return storedBytes;
    }
    return CacheArchiveCompression.decompressBzipBlock(storedBytes, entryReference.uncompressedLength());
  }
}
