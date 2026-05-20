package io.github.ffakira.rsps.client.desktop.world.object;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.MapObjectPlacement;
import io.github.ffakira.rsps.content.ObjectDefinitionCatalog;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WorldSceneObjectAssemblerTest {

  @Test
  void enablesFallbackProxiesForStructuralObjectsWhoseCacheModelsProduceNoGeometry() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    WorldSceneObjectAssembler assembler = new WorldSceneObjectAssembler(
        ObjectDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneObject worldSceneObject = assembler.assemble(
        new MapObjectPlacement(85, 3203, 3203, 0, 0, 0),
        3200,
        3200
    );

    assertThat(worldSceneObject.geometry()).isNull();
    assertThat(worldSceneObject.allowFallbackProxy()).isTrue();
  }
}
