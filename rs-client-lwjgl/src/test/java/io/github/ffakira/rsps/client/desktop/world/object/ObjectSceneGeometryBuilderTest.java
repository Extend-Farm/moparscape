package io.github.ffakira.rsps.client.desktop.world.object;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.cache.RawModelData;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.ObjectDefinition;
import io.github.ffakira.rsps.content.ObjectDefinitionCatalog;
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
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "test",
        List.of(),
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
        false,
        16,
        -1,
        0,
        128,
        128,
        128,
        0,
        0,
        0
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
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "wide-object",
        List.of(),
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
        false,
        16,
        -1,
        0,
        128,
        128,
        128,
        0,
        0,
        0
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
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[0],
        new int[0],
        new int[0]
    );
    ObjectDefinition definition = new ObjectDefinition(
        1,
        "centered-tree",
        List.of(),
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
        false,
        16,
        -1,
        0,
        128,
        128,
        128,
        0,
        0,
        0
    );

    float[][] vertices = ObjectSceneGeometryBuilder.transformVertices(rawModelData, definition, 0);

    assertThat(vertices[0][0]).isEqualTo(-0.5f);
    assertThat(vertices[0][1]).isEqualTo(0.5f);
    assertThat(vertices[2][0]).isEqualTo(-0.5f);
    assertThat(vertices[2][2]).isEqualTo(0.5f);
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
