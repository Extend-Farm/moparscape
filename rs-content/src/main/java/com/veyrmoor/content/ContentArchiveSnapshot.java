package com.veyrmoor.content;

import com.veyrmoor.cache.CacheArchiveContainer;

public record ContentArchiveSnapshot(
    CacheArchiveContainer configArchive,
    CacheArchiveContainer updateListArchive,
    ContentDefinitionSummary definitionSummary
) {

  public byte[] readConfigEntry(String entryName) {
    return configArchive.readEntry(entryName);
  }

  public byte[] readUpdateListEntry(String entryName) {
    return updateListArchive.readEntry(entryName);
  }
}
