package io.github.ffakira.rsps.cache;

public record RawCacheArchive(
    int storeIndex,
    CacheIndexEntry indexEntry,
    byte[] bytes
) {

  public CacheArchiveCompressionHeader compressionHeader() {
    return CacheArchiveCompressionHeader.parse(bytes);
  }
}
