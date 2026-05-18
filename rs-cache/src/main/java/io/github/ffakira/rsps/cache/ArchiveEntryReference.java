package io.github.ffakira.rsps.cache;

public record ArchiveEntryReference(
    int nameHash,
    int uncompressedLength,
    int storedLength,
    int dataOffset
) {
}
