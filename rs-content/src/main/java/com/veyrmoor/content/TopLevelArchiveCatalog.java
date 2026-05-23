package com.veyrmoor.content;

import com.veyrmoor.cache.CacheArchiveContainer;
import com.veyrmoor.cache.CacheArchiveRepository;
import java.util.EnumMap;
import java.util.Map;

public class TopLevelArchiveCatalog {

  public Map<TopLevelArchiveId, CacheArchiveContainer> loadAll(ContentManifest manifest) {
    CacheArchiveRepository repository = new CacheArchiveRepository(manifest.cacheStore());
    EnumMap<TopLevelArchiveId, CacheArchiveContainer> archives = new EnumMap<>(TopLevelArchiveId.class);
    for (TopLevelArchiveId archiveId : TopLevelArchiveId.values()) {
      archives.put(archiveId, repository.loadArchive(0, archiveId.archiveId()));
    }
    return Map.copyOf(archives);
  }
}
