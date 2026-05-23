package com.veyrmoor.cache;

public record ArchiveEntryReference(
    int nameHash,
    int uncompressedLength,
    int storedLength,
    int dataOffset
) {
}
