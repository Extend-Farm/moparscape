package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ContentArchiveCatalogTest {

  @Test
  void exposesSelectedLegacyArchiveEntriesFromParsedTopLevelContainers() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ContentArchiveSnapshot snapshot = new ContentArchiveCatalog().load(manifest);

    assertThat(snapshot.configArchive().entryReferences()).hasSize(24);
    assertThat(snapshot.readConfigEntry("obj.idx")).hasSize(13084);
    assertThat(snapshot.readConfigEntry("obj.dat")).hasSize(372323);
    assertThat(snapshot.readUpdateListEntry("map_index")).hasSize(4186);
  }

  @Test
  void exposesCharacterizedDefinitionCounts() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ContentArchiveSnapshot snapshot = new ContentArchiveCatalog().load(manifest);

    assertThat(snapshot.definitionSummary()).isEqualTo(new ContentDefinitionSummary(6541, 2633, 9399, 598));
  }
}
