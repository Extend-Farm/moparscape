package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneProjection;
import java.util.List;
import org.junit.jupiter.api.Test;

class TerrainTileColorResolverTest {

  @Test
  void keepsFlatTerrainWithinReferenceBrightnessRange() {
    WorldScene worldScene = testWorldScene(
        3,
        3,
        new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        }
    );

    int brightness = TerrainTileColorResolver.terrainLightBrightness(worldScene, 1, 1);

    assertThat(brightness).isBetween(84, 196);
  }

  @Test
  void differentiatesOpposingSlopeDirectionsAgainstTheReferenceLightVector() {
    WorldScene eastRisingScene = testWorldScene(
        3,
        3,
        new int[]{
            0, 24, 48,
            0, 24, 48,
            0, 24, 48
        }
    );
    WorldScene westRisingScene = testWorldScene(
        3,
        3,
        new int[]{
            48, 24, 0,
            48, 24, 0,
            48, 24, 0
        }
    );

    int eastRisingBrightness = TerrainTileColorResolver.terrainLightBrightness(eastRisingScene, 1, 1);
    int westRisingBrightness = TerrainTileColorResolver.terrainLightBrightness(westRisingScene, 1, 1);

    assertThat(westRisingBrightness).isGreaterThan(eastRisingBrightness);
  }

  @Test
  void brightensExposedCornersMoreThanShelteredInteriorGround() {
    WorldScene worldScene = testWorldScene(
        5,
        5,
        new int[]{
            64, 48, 32, 48, 64,
            48, 32, 16, 32, 48,
            32, 16, 0, 16, 32,
            48, 32, 16, 32, 48,
            64, 48, 32, 48, 64
        }
    );

    int exposedCorner = TerrainTileColorResolver.cornerColor(
        worldScene,
        0,
        0,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        0x5f7f47
    );
    int shelteredInterior = TerrainTileColorResolver.cornerColor(
        worldScene,
        2,
        2,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        0x5f7f47
    );

    assertThat(averageChannel(exposedCorner)).isGreaterThan(averageChannel(shelteredInterior));
  }

  @Test
  void texturesUseNeutralLightingInsteadOfTintingTheTextureByFloorRgb() {
    WorldScene worldScene = testWorldScene(
        3,
        3,
        new int[]{
            0, 12, 24,
            0, 12, 24,
            0, 12, 24
        }
    );

    int texturedColor = TerrainTileColorResolver.textureCornerColor(worldScene, 1, 1);

    assertThat(Math.abs(channel(texturedColor, 16) - channel(texturedColor, 8))).isLessThanOrEqualTo(1);
    assertThat(Math.abs(channel(texturedColor, 8) - channel(texturedColor, 0))).isLessThanOrEqualTo(1);
  }

  @Test
  void treatsStoredFloorRgbAsReferenceBaselineLight() {
    WorldScene worldScene = testWorldScene(
        3,
        3,
        new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        },
        filledInts(9, 0x404305),
        filledInts(9, 0),
        filledInts(9, 0x404305)
    );

    int cornerColor = TerrainTileColorResolver.cornerColor(
        worldScene,
        1,
        1,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        0x404305
    );

    assertThat(channel(cornerColor, 8)).isGreaterThan(45);
    assertThat(channel(cornerColor, 8)).isGreaterThan(channel(cornerColor, 16));
  }

  @Test
  void relightsPaletteDerivedFloorRgbThroughTheReferenceHslPath() {
    int floorHsl16 = encodeHsl16(84, 160, 92);
    int storedFloorRgb = FloorColorShadePalette.hslToRgb(
        FloorColorShadePalette.checkedLight(floorHsl16, FloorColorShadePalette.BASELINE_LIGHT)
    );
    WorldScene worldScene = testWorldScene(
        3,
        3,
        new int[]{
            0, 24, 48,
            0, 24, 48,
            0, 24, 48
        },
        filledInts(9, storedFloorRgb),
        filledInts(9, 0),
        filledInts(9, storedFloorRgb)
    );

    int brightness = TerrainTileColorResolver.terrainLightBrightness(worldScene, 1, 1);
    int cornerColor = TerrainTileColorResolver.cornerColor(
        worldScene,
        1,
        1,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        storedFloorRgb
    );

    assertThat(brightness).isNotEqualTo(FloorColorShadePalette.BASELINE_LIGHT);
    assertThat(cornerColor).isEqualTo(
        FloorColorShadePalette.hslToRgb(
            FloorColorShadePalette.checkedLight(floorHsl16, brightness)
        )
    );
  }

  @Test
  void averagesSharedCornerPaletteContributorsInReferenceHslSpace() {
    int northWestHsl16 = encodeHsl16(12, 160, 88);
    int northEastHsl16 = encodeHsl16(52, 192, 112);
    int southWestHsl16 = encodeHsl16(104, 128, 72);
    int southEastHsl16 = encodeHsl16(156, 224, 136);
    int[] underlayColors = new int[]{
        referenceFloorRgb(northWestHsl16),
        referenceFloorRgb(northEastHsl16),
        referenceFloorRgb(southWestHsl16),
        referenceFloorRgb(southEastHsl16)
    };
    WorldScene worldScene = testWorldScene(
        2,
        2,
        new int[]{
            0, 16,
            8, 24
        },
        underlayColors,
        filledInts(4, 0),
        underlayColors
    );

    int brightness = TerrainTileColorResolver.terrainLightBrightness(worldScene, 1, 1);
    int cornerColor = TerrainTileColorResolver.cornerColor(
        worldScene,
        1,
        1,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        underlayColors[0]
    );

    int expectedCornerHsl16 = FloorColorShadePalette.encodeHsl16(
        average(
            FloorColorShadePalette.hueComponent(northWestHsl16),
            FloorColorShadePalette.hueComponent(northEastHsl16),
            FloorColorShadePalette.hueComponent(southWestHsl16),
            FloorColorShadePalette.hueComponent(southEastHsl16)
        ),
        average(
            FloorColorShadePalette.saturationComponent(northWestHsl16),
            FloorColorShadePalette.saturationComponent(northEastHsl16),
            FloorColorShadePalette.saturationComponent(southWestHsl16),
            FloorColorShadePalette.saturationComponent(southEastHsl16)
        ),
        average(
            FloorColorShadePalette.luminanceComponent(northWestHsl16),
            FloorColorShadePalette.luminanceComponent(northEastHsl16),
            FloorColorShadePalette.luminanceComponent(southWestHsl16),
            FloorColorShadePalette.luminanceComponent(southEastHsl16)
        )
    );

    assertThat(cornerColor).isEqualTo(
        FloorColorShadePalette.hslToRgb(
            FloorColorShadePalette.checkedLight(expectedCornerHsl16, brightness)
        )
    );
  }

  @Test
  void objectShadowSamplesDarkenOtherwiseFlatTerrain() {
    WorldScene unshadowedScene = testWorldScene(
        3,
        3,
        new int[]{
            48, 24, 0,
            48, 24, 0,
            48, 24, 0
        }
    );
    WorldScene shadowedScene = testWorldScene(
        3,
        3,
        new int[]{
            48, 24, 0,
            48, 24, 0,
            48, 24, 0
        },
        filledInts(9, 0x4a6a3c),
        filledInts(9, 0x7d9150),
        filledInts(9, 0x4a6a3c),
        new byte[]{
            0, 0, 0,
            0, 50, 0,
            0, 0, 0
        }
    );

    int unshadowedBrightness = TerrainTileColorResolver.terrainLightBrightness(unshadowedScene, 1, 1);
    int shadowedBrightness = TerrainTileColorResolver.terrainLightBrightness(shadowedScene, 1, 1);

    assertThat(shadowedBrightness).isLessThan(unshadowedBrightness);
  }

  @Test
  void doesNotRenderFlatPaintWhenTheOverlayBranchIsPresentButHidden() {
    WorldScene worldScene = new WorldScene(
        "hidden-overlay-paint-decision",
        3200,
        3200,
        0,
        1,
        1,
        new int[]{0},
        new int[]{1},
        new int[]{4},
        new int[]{0x4a6a3c},
        new int[]{0x4a6a3c},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff334455}),
        new WorldSceneProjection(5, 3, 0, 0),
        null
    );

    assertThat(TerrainTileColorResolver.paintLayer(worldScene, 0, 0))
        .isEqualTo(TerrainTileColorResolver.FloorColorLayer.OVERLAY);
    assertThat(TerrainTileColorResolver.shouldRenderPaintTile(worldScene, 0, 0)).isFalse();
  }

  private static WorldScene testWorldScene(int width, int height, int[] elevations) {
    return testWorldScene(
        width,
        height,
        elevations,
        filledInts(width * height, 0x4a6a3c),
        filledInts(width * height, 0x7d9150),
        filledInts(width * height, 0x4a6a3c)
    );
  }

  private static WorldScene testWorldScene(
      int width,
      int height,
      int[] elevations,
      int[] underlayColors,
      int[] overlayColors,
      int[] tileColors
  ) {
    return testWorldScene(width, height, elevations, underlayColors, overlayColors, tileColors, new byte[width * height]);
  }

  private static WorldScene testWorldScene(
      int width,
      int height,
      int[] elevations,
      int[] underlayColors,
      int[] overlayColors,
      int[] tileColors,
      byte[] shadowSamples
  ) {
    int[] underlayTextureIds = filledInts(width * height, -1);
    int[] overlayTextureIds = filledInts(width * height, -1);
    byte[] overlayShapes = new byte[width * height];
    byte[] overlayRotations = new byte[width * height];
    byte[] tileFlags = new byte[width * height];
    return new WorldScene(
        "terrain-light-test",
        3200,
        3200,
        0,
        width,
        height,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        shadowSamples,
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width, height, filledInts(width * height, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0),
        null
    );
  }

  private static int[] filledInts(int size, int value) {
    int[] values = new int[size];
    java.util.Arrays.fill(values, value);
    return values;
  }

  private static int averageChannel(int rgb) {
    return (((rgb >>> 16) & 0xff) + ((rgb >>> 8) & 0xff) + (rgb & 0xff)) / 3;
  }

  private static int channel(int rgb, int shift) {
    return (rgb >>> shift) & 0xff;
  }

  private static int referenceFloorRgb(int hsl16) {
    return FloorColorShadePalette.hslToRgb(
        FloorColorShadePalette.checkedLight(hsl16, FloorColorShadePalette.BASELINE_LIGHT)
    );
  }

  private static int average(int... values) {
    int sum = 0;
    for (int value : values) {
      sum += value;
    }
    return sum / values.length;
  }

  private static int encodeHsl16(int hue, int saturation, int luminance) {
    return FloorColorShadePalette.encodeHsl16(hue, saturation, luminance);
  }
}
