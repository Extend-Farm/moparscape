package com.veyrmoor.cache;

public class CacheArchiveRepository {

  private final CacheStoreLayout cacheStoreLayout;
  private final CacheArchiveContainerParser containerParser = new CacheArchiveContainerParser();

  public CacheArchiveRepository(CacheStoreLayout cacheStoreLayout) {
    this.cacheStoreLayout = cacheStoreLayout;
  }

  public CacheArchiveContainer loadArchive(int storeIndex, int archiveId) {
    try (CacheStoreReader reader = new CacheStoreReader(cacheStoreLayout)) {
      return containerParser.parse(reader.readArchive(storeIndex, archiveId));
    } catch (Exception exception) {
      throw new IllegalStateException(
          "Failed to load archive " + archiveId + " from store " + storeIndex,
          exception
      );
    }
  }
}
