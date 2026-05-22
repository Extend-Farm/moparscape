package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.NpcDefinitionCatalog;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class NpcModelAssemblerTest {

  @Test
  void assemblesCacheBackedGoblinGeometry() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    NpcModelAssembler assembler = new NpcModelAssembler(
        NpcDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneObjectGeometry geometry = assembler.assemble(100);

    assertThat(geometry).isNotNull();
    assertThat(geometry.vertexX()).isNotEmpty();
    assertThat(geometry.vertexY()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.vertexZ()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.faceVertexA()).isNotEmpty();
    assertThat(geometry.faceColorA()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceAlpha()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.facePriorities()).hasSameSizeAs(geometry.faceVertexA());
  }
}
