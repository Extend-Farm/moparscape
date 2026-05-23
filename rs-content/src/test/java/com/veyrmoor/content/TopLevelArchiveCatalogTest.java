package com.veyrmoor.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.CacheArchiveContainer;
import java.nio.file.Path;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TopLevelArchiveCatalogTest {

  @Test
  void loadsKnownTopLevelArchivesFromLegacyStore() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    Map<TopLevelArchiveId, CacheArchiveContainer> archives = new TopLevelArchiveCatalog().loadAll(manifest);

    assertThat(archives).containsKeys(
        TopLevelArchiveId.TITLE,
        TopLevelArchiveId.CONFIG,
        TopLevelArchiveId.MEDIA,
        TopLevelArchiveId.UPDATE_LIST
    );
    assertThat(archives.get(TopLevelArchiveId.CONFIG).entryReferences()).hasSize(24);
  }

  @Test
  void loadsCharacterizedDefinitionCountsFromLiveCache() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ContentDefinitionSummary summary = new ContentDefinitionCatalog().loadSummary(manifest);

    assertThat(summary.itemCount()).isEqualTo(6541);
    assertThat(summary.npcCount()).isEqualTo(2633);
    assertThat(summary.objectCount()).isEqualTo(9399);
    assertThat(summary.mapRegionCount()).isEqualTo(598);
  }
}
