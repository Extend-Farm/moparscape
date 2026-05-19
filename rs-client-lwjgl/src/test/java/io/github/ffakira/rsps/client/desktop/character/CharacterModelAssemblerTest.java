package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
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

  @Test
  void appliesAWalkPoseWhenAnimationStateIsActive() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry walkingGeometry = assembler.assemble(
        appearance,
        List.of(),
        new ActorAnimationState(1.0f, (float) (Math.PI / 2.0), 0.0f)
    );

    assertThat(walkingGeometry).isNotNull();
    assertThat(walkingGeometry.faceVertexA()).containsExactly(idleGeometry.faceVertexA());
    assertThat(walkingGeometry.faceVertexB()).containsExactly(idleGeometry.faceVertexB());
    assertThat(walkingGeometry.faceVertexC()).containsExactly(idleGeometry.faceVertexC());

    boolean anyAnimatedVertex = false;
    for (int index = 0; index < walkingGeometry.vertexY().length; index++) {
      if (Math.abs(walkingGeometry.vertexX()[index] - idleGeometry.vertexX()[index]) > 0.0001f
          || Math.abs(walkingGeometry.vertexY()[index] - idleGeometry.vertexY()[index]) > 0.0001f
          || Math.abs(walkingGeometry.vertexZ()[index] - idleGeometry.vertexZ()[index]) > 0.0001f) {
        anyAnimatedVertex = true;
        break;
      }
    }

    assertThat(anyAnimatedVertex).isTrue();
  }

  @Test
  void keepsTheAssembledActorOnAHumanSizedFootprint() {
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
    float minX = Float.POSITIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY;
    float minZ = Float.POSITIVE_INFINITY;
    float maxX = Float.NEGATIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;
    float maxZ = Float.NEGATIVE_INFINITY;
    for (int index = 0; index < geometry.vertexX().length; index++) {
      minX = Math.min(minX, geometry.vertexX()[index]);
      minY = Math.min(minY, geometry.vertexY()[index]);
      minZ = Math.min(minZ, geometry.vertexZ()[index]);
      maxX = Math.max(maxX, geometry.vertexX()[index]);
      maxY = Math.max(maxY, geometry.vertexY()[index]);
      maxZ = Math.max(maxZ, geometry.vertexZ()[index]);
    }

    assertThat(maxX - minX).isLessThanOrEqualTo(0.70f);
    assertThat(maxZ - minZ).isLessThanOrEqualTo(0.70f);
    assertThat(maxY - minY).isBetween(1.5f, 1.82f);
    assertThat(minY).isBetween(-0.05f, 0.05f);
  }

  @Test
  void preservesEquipmentSilhouetteInsteadOfRescalingEveryAppearanceToOneHeight() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    BootstrapAppearance appearance = new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1));

    WorldSceneObjectGeometry idleGeometry = assembler.assemble(appearance, List.of());
    WorldSceneObjectGeometry partyhatGeometry = assembler.assemble(
        appearance,
        List.of(new BootstrapItemSlot(0, 1048, 1))
    );

    assertThat(partyhatGeometry).isNotNull();
    assertThat(maxVertexY(partyhatGeometry) - minVertexY(partyhatGeometry))
        .isGreaterThan(maxVertexY(idleGeometry) - minVertexY(idleGeometry));
    assertThat(Math.abs(minVertexY(partyhatGeometry))).isLessThan(0.05f);
  }

  private float minVertexY(WorldSceneObjectGeometry geometry) {
    float minY = Float.POSITIVE_INFINITY;
    for (float y : geometry.vertexY()) {
      minY = Math.min(minY, y);
    }
    return minY;
  }

  private float maxVertexY(WorldSceneObjectGeometry geometry) {
    float maxY = Float.NEGATIVE_INFINITY;
    for (float y : geometry.vertexY()) {
      maxY = Math.max(maxY, y);
    }
    return maxY;
  }
}
