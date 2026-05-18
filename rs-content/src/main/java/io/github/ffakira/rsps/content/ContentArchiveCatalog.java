package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import java.util.Map;

public class ContentArchiveCatalog {

  private static final String ITEM_INDEX_ENTRY = "obj.idx";
  private static final String NPC_INDEX_ENTRY = "npc.idx";
  private static final String OBJECT_INDEX_ENTRY = "loc.idx";
  private static final String MAP_INDEX_ENTRY = "map_index";
  private static final int MAP_INDEX_RECORD_LENGTH = 7;

  private final TopLevelArchiveCatalog topLevelArchiveCatalog;

  public ContentArchiveCatalog() {
    this(new TopLevelArchiveCatalog());
  }

  ContentArchiveCatalog(TopLevelArchiveCatalog topLevelArchiveCatalog) {
    this.topLevelArchiveCatalog = topLevelArchiveCatalog;
  }

  public ContentArchiveSnapshot load(ContentManifest manifest) {
    Map<TopLevelArchiveId, CacheArchiveContainer> archives = topLevelArchiveCatalog.loadAll(manifest);
    CacheArchiveContainer configArchive = requireArchive(archives, TopLevelArchiveId.CONFIG);
    CacheArchiveContainer updateListArchive = requireArchive(archives, TopLevelArchiveId.UPDATE_LIST);
    ContentDefinitionSummary definitionSummary = new ContentDefinitionSummary(
        readCountFromIndex(configArchive.readEntry(ITEM_INDEX_ENTRY)),
        readCountFromIndex(configArchive.readEntry(NPC_INDEX_ENTRY)),
        readCountFromIndex(configArchive.readEntry(OBJECT_INDEX_ENTRY)),
        updateListArchive.readEntry(MAP_INDEX_ENTRY).length / MAP_INDEX_RECORD_LENGTH
    );
    return new ContentArchiveSnapshot(configArchive, updateListArchive, definitionSummary);
  }

  private CacheArchiveContainer requireArchive(
      Map<TopLevelArchiveId, CacheArchiveContainer> archives,
      TopLevelArchiveId archiveId
  ) {
    CacheArchiveContainer archive = archives.get(archiveId);
    if (archive == null) {
      throw new IllegalStateException("Missing top-level archive " + archiveId);
    }
    return archive;
  }

  private int readCountFromIndex(byte[] bytes) {
    if (bytes.length < 2) {
      throw new IllegalArgumentException("Expected at least 2 bytes, got " + bytes.length);
    }
    return ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
  }
}
