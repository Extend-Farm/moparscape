package com.veyrmoor.client.desktop.world.object;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.MapObjectPlacement;
import com.veyrmoor.content.ObjectDefinition;
import com.veyrmoor.content.ObjectDefinitionCatalog;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WorldSceneObjectAssemblerTest {

  @Test
  void enablesFallbackProxiesForStructuralObjectsWhoseCacheModelsProduceNoGeometry() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    WorldSceneObjectAssembler assembler = new WorldSceneObjectAssembler(
        catalog,
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

  @Test
  void resolvesMorphBackedStatuesBeforeFallingBackToProxyGeometry() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    WorldSceneObjectAssembler assembler = new WorldSceneObjectAssembler(
        catalog,
        new RawModelRepository(manifest.cacheStore())
    );
    ObjectDefinition resolvedDefinition = catalog.resolveSceneDefinition(9251);
    int placementType = resolvedDefinition.modelTypes().isEmpty() ? 10 : resolvedDefinition.modelTypes().getFirst();

    WorldSceneObject worldSceneObject = assembler.assemble(
        new MapObjectPlacement(9251, 3200, 3200, 0, placementType, 0),
        3200,
        3200
    );

    assertThat(worldSceneObject.geometry()).isNotNull();
    assertThat(worldSceneObject.allowFallbackProxy()).isFalse();
    assertThat(worldSceneObject.name()).isEqualTo(resolvedDefinition.name());
  }
}
