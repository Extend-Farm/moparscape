package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CharacterModelAssemblerTest {

  @Test
  void assemblesNativeCharacterGeometryFromCacheBackedIdentityKits() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneObjectGeometry geometry = assembler.assemble(
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of()
    );

    assertThat(geometry).isNotNull();
    assertThat(geometry.vertexX()).isNotEmpty();
    assertThat(geometry.vertexY()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.vertexZ()).hasSameSizeAs(geometry.vertexX());
    assertThat(geometry.faceVertexA()).isNotEmpty();
    assertThat(geometry.faceVertexB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceVertexC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorA()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceColorC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceAlpha()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceRasterModes()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceTextureIds()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexA()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexB()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.textureVertexC()).hasSameSizeAs(geometry.faceVertexA());
    assertThat(geometry.faceRasterModes()).contains(SceneRasterMode.GOURAUD);
    for (int faceIndex = 0; faceIndex < geometry.faceVertexA().length; faceIndex++) {
      assertThat(geometry.faceAlpha()[faceIndex]).isBetween(0, 255);
      if (geometry.faceRasterModes()[faceIndex] == SceneRasterMode.TEXTURED) {
        assertThat(geometry.faceTextureIds()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexA()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexB()[faceIndex]).isGreaterThanOrEqualTo(0);
        assertThat(geometry.textureVertexC()[faceIndex]).isGreaterThanOrEqualTo(0);
      } else {
        assertThat(geometry.faceTextureIds()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexA()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexB()[faceIndex]).isEqualTo(-1);
        assertThat(geometry.textureVertexC()[faceIndex]).isEqualTo(-1);
      }
    }
  }

  @Test
  void cachesGeometryByAppearanceAndEquipmentKey() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry first = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry second = assembler.assemble(appearance, List.of());

    assertThat(second).isSameAs(first);
  }
}
