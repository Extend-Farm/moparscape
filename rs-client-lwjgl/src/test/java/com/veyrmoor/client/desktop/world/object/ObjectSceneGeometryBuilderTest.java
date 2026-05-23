package com.veyrmoor.client.desktop.world.object;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.RawModelRepository;
import com.veyrmoor.cache.RawModelData;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.ObjectDefinition;
import com.veyrmoor.content.ObjectDefinitionCatalog;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObjectSceneGeometryBuilderTest {

  @Test
  void preservesCacheModelYOriginForStaticObjects() {
    RawModelData rawModelData = new RawModelData(
        4,
        2,
        0,
        new int[]{0, 128, 128, 0},
        new int[]{128, 128, 0, 0},
        new int[]{0, 0, 128, 128},
        new int[0],
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "test",
        List.of(),
        List.of(),
        -1,
        -1,
        List.of(),
        List.of(),
        List.of(),
        1,
        1,
        true,
        true,
        true,
        false,
        false,
        true,
        false,
        16,
        -1,
        -1,
        0,
        0,
        0,
        128,
        128,
        128,
        0,
        0,
        0,
        -1
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 0);

    assertThat(vertices[1][0]).isEqualTo(-1.0f);
    assertThat(vertices[1][1]).isEqualTo(-1.0f);
    assertThat(vertices[1][2]).isEqualTo(0.0f);
    assertThat(vertices[1][3]).isEqualTo(0.0f);
  }

  @Test
  void preservesAuthoredObjectFootprintInsteadOfRefittingTheMeshToTileSize() {
    RawModelData rawModelData = new RawModelData(
        4,
        2,
        0,
        new int[]{0, 256, 256, 0},
        new int[]{0, 0, 0, 0},
        new int[]{0, 0, 128, 128},
        new int[0],
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "wide-object",
        List.of(),
        List.of(),
        -1,
        -1,
        List.of(),
        List.of(),
        List.of(),
        1,
        1,
        true,
        true,
        true,
        false,
        false,
        true,
        false,
        16,
        -1,
        -1,
        0,
        0,
        0,
        128,
        128,
        128,
        0,
        0,
        0,
        -1
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 0);

    assertThat(vertices[0][0]).isEqualTo(0.0f);
    assertThat(vertices[0][1]).isEqualTo(2.0f);
    assertThat(vertices[2][2]).isEqualTo(1.0f);
  }

  @Test
  void preservesCenteredObjectOriginsForSingleTileProps() {
    RawModelData rawModelData = new RawModelData(
        4,
        2,
        0,
        new int[]{-64, 64, 64, -64},
        new int[]{0, 0, 0, 0},
        new int[]{-64, -64, 64, 64},
        new int[0],
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "centered-tree",
        List.of(),
        List.of(),
        -1,
        -1,
        List.of(),
        List.of(),
        List.of(),
        1,
        1,
        true,
        true,
        true,
        false,
        false,
        true,
        false,
        16,
        -1,
        -1,
        0,
        0,
        0,
        128,
        128,
        128,
        0,
        0,
        0,
        -1
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 0);

    assertThat(vertices[0][0]).isEqualTo(-0.5f);
    assertThat(vertices[0][1]).isEqualTo(0.5f);
    assertThat(vertices[2][0]).isEqualTo(-0.5f);
    assertThat(vertices[2][2]).isEqualTo(0.5f);
  }

  @Test
  void mirrorsStaticObjectsAcrossLegacyZAxisInsteadOfX() {
    RawModelData rawModelData = new RawModelData(
        2,
        1,
        0,
        new int[]{64, -64},
        new int[]{0, 0},
        new int[]{32, -96},
        new int[0],
        new int[]{0},
        new int[]{1},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "mirrored-statue",
        List.of(),
        List.of(),
        -1,
        -1,
        List.of(),
        List.of(),
        List.of(),
        1,
        1,
        true,
        true,
        true,
        false,
        true,
        true,
        false,
        16,
        -1,
        -1,
        0,
        0,
        0,
        128,
        128,
        128,
        0,
        0,
        0,
        -1
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 0);

    assertThat(vertices[0][0]).isEqualTo(0.5f);
    assertThat(vertices[2][0]).isEqualTo(-0.25f);
    assertThat(vertices[0][1]).isEqualTo(-0.5f);
    assertThat(vertices[2][1]).isEqualTo(0.75f);
  }

  @Test
  void swapsFaceWindingWhenUsingMirroredGeometry() {
    RawModelData rawModelData = new RawModelData(
        3,
        1,
        0,
        new int[]{0, 128, 0},
        new int[]{0, 0, 0},
        new int[]{0, 0, 128},
        new int[0],
        new int[]{0},
        new int[]{1},
        new int[]{2},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );

    assertThat(ObjectSceneGeometryBuilder.faceVertexA(rawModelData, 0, true)).isEqualTo(2);
    assertThat(ObjectSceneGeometryBuilder.faceVertexB(rawModelData, 0)).isEqualTo(1);
    assertThat(ObjectSceneGeometryBuilder.faceVertexC(rawModelData, 0, true)).isEqualTo(0);
  }

  @Test
  void appliesObjectTranslationsAfterOrientationRotationLikeLegacy() {
    RawModelData rawModelData = new RawModelData(
        1,
        0,
        0,
        new int[]{128},
        new int[]{0},
        new int[]{0},
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "translated-statue",
        List.of(),
        List.of(),
        -1,
        -1,
        List.of(),
        List.of(),
        List.of(),
        1,
        1,
        true,
        true,
        true,
        false,
        false,
        true,
        false,
        16,
        -1,
        -1,
        0,
        0,
        0,
        128,
        128,
        128,
        64,
        0,
        0,
        -1
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 1);

    assertThat(vertices[0][0]).isEqualTo(0.5f);
    assertThat(vertices[2][0]).isEqualTo(-1.0f);
  }

  @Test
  void usesNeutralLightValuesForTexturedFountainFaces() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    ObjectDefinition definition = catalog.require(879);
    ObjectSceneGeometryBuilder builder = new ObjectSceneGeometryBuilder(new RawModelRepository(manifest.cacheStore()));

    WorldSceneObjectGeometry geometry = builder.build(
        definition,
        0,
        definition.modelIds()
    );

    assertThat(geometry).isNotNull();
    boolean foundWaterFace = false;
    for (int faceIndex = 0; faceIndex < geometry.faceTextureIds().length; faceIndex++) {
      if (geometry.faceTextureIds()[faceIndex] != 17) {
        continue;
      }
      foundWaterFace = true;
      assertThat(red(geometry.faceColorA()[faceIndex])).isEqualTo(green(geometry.faceColorA()[faceIndex]));
      assertThat(green(geometry.faceColorA()[faceIndex])).isEqualTo(blue(geometry.faceColorA()[faceIndex]));
      assertThat(red(geometry.faceColorB()[faceIndex])).isEqualTo(green(geometry.faceColorB()[faceIndex]));
      assertThat(green(geometry.faceColorB()[faceIndex])).isEqualTo(blue(geometry.faceColorB()[faceIndex]));
      assertThat(red(geometry.faceColorC()[faceIndex])).isEqualTo(green(geometry.faceColorC()[faceIndex]));
      assertThat(green(geometry.faceColorC()[faceIndex])).isEqualTo(blue(geometry.faceColorC()[faceIndex]));
    }
    assertThat(foundWaterFace).isTrue();
  }

  @Test
  void keepsTexturedTreeCanopiesAboveTheOldNearBlackBrightnessFloor() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    ObjectDefinition definition = catalog.require(1276);
    ObjectSceneGeometryBuilder builder = new ObjectSceneGeometryBuilder(new RawModelRepository(manifest.cacheStore()));

    WorldSceneObjectGeometry geometry = builder.build(
        definition,
        0,
        definition.modelIds()
    );

    assertThat(geometry).isNotNull();
    int minimumTextureBrightness = Integer.MAX_VALUE;
    boolean foundLeafTextureFace = false;
    for (int faceIndex = 0; faceIndex < geometry.faceTextureIds().length; faceIndex++) {
      if (geometry.faceTextureIds()[faceIndex] != 8) {
        continue;
      }
      foundLeafTextureFace = true;
      minimumTextureBrightness = Math.min(minimumTextureBrightness, red(geometry.faceColorA()[faceIndex]));
      minimumTextureBrightness = Math.min(minimumTextureBrightness, red(geometry.faceColorB()[faceIndex]));
      minimumTextureBrightness = Math.min(minimumTextureBrightness, red(geometry.faceColorC()[faceIndex]));
    }
    assertThat(foundLeafTextureFace).isTrue();
    assertThat(minimumTextureBrightness).isGreaterThanOrEqualTo(108);
  }

  @Test
  void returnsNullForBridgeWallDefinitionsWhoseCacheModelsHaveNoFaces() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    ObjectDefinition definition = catalog.require(85);
    ObjectSceneGeometryBuilder builder = new ObjectSceneGeometryBuilder(new RawModelRepository(manifest.cacheStore()));

    assertThat(builder.build(definition, 0, definition.modelIdsForType(0))).isNull();
    assertThat(builder.build(definition, 0, definition.modelIdsForType(9))).isNull();
    assertThat(builder.hasRenderableSourceModels(definition.modelIdsForType(0))).isFalse();
    assertThat(builder.hasRenderableSourceModels(definition.modelIdsForType(9))).isFalse();
  }

  private static int red(int rgb) {
    return (rgb >>> 16) & 0xff;
  }

  private static int green(int rgb) {
    return (rgb >>> 8) & 0xff;
  }

  private static int blue(int rgb) {
    return rgb & 0xff;
  }
}
