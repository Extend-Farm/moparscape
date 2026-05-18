package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;

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
