package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneProjection;
import java.util.List;
import org.junit.jupiter.api.Test;

class TerrainTileColorResolverTest {

  @Test
  void keepsFlatTerrainWithinLegacyBrightnessRange() {
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
  void differentiatesOpposingSlopeDirectionsAgainstTheLegacyLightVector() {
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

  private static WorldScene testWorldScene(int width, int height, int[] elevations) {
    int[] tileColors = filledInts(width * height, 0x4a6a3c);
    int[] underlayColors = filledInts(width * height, 0x4a6a3c);
    int[] overlayColors = filledInts(width * height, 0x7d9150);
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
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width, height, filledInts(width * height, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
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
}
