package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class NpcDefinitionCatalogTest {

  @Test
  void loadsLiveCacheGoblinDefinitionsWithRenderableModels() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    NpcDefinitionCatalog catalog = NpcDefinitionCatalog.load(manifest);
    NpcDefinition goblin = catalog.require(100);

    assertThat(catalog.size()).isGreaterThan(1_000);
    assertThat(goblin.name()).isEqualTo("Goblin");
    assertThat(goblin.modelIds()).isNotEmpty();
    assertThat(goblin.combatLevel()).isEqualTo(2);
    assertThat(goblin.idleSequenceId()).isGreaterThanOrEqualTo(0);
    assertThat(goblin.walkSequenceId()).isGreaterThanOrEqualTo(0);
  }
}
