package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.content.FloorColorCatalog;
import org.junit.jupiter.api.Test;

class TerrainOcclusionFlagResolverTest {

  @Test
  void marksFlatUpperPlaneTilesWithOccludingFloors() {
    TerrainOcclusionFlagResolver resolver = new TerrainOcclusionFlagResolver(testFloorColors());
    int tileWidth = 2;
    int tileHeight = 2;
    byte[] surfacePlanes = new byte[]{1, 0, 0, 0};
    int[][] heightSamplesByPlane = new int[4][tileWidth * tileHeight];
    int[] underlayIds = new int[]{1, 0, 0, 0};
    int[] overlayIds = new int[tileWidth * tileHeight];
    byte[] overlayShapes = new byte[tileWidth * tileHeight];

    int[] terrainOcclusionFlags = resolver.resolve(
        tileWidth,
        tileHeight,
        surfacePlanes,
        heightSamplesByPlane,
        underlayIds,
        overlayIds,
        overlayShapes
    );

    assertThat(terrainOcclusionFlags[0]).isEqualTo(TerrainOcclusionFlagResolver.LEGACY_FLAT_TILE_OCCLUSION_MASK);
  }

  @Test
  void skipsFlatTileOcclusionWhenTheOverlayDefinitionDoesNotOcclude() {
    TerrainOcclusionFlagResolver resolver = new TerrainOcclusionFlagResolver(testFloorColors());
    int tileWidth = 2;
    int tileHeight = 2;
    byte[] surfacePlanes = new byte[]{1, 0, 0, 0};
    int[][] heightSamplesByPlane = new int[4][tileWidth * tileHeight];
    int[] underlayIds = new int[]{1, 0, 0, 0};
    int[] overlayIds = new int[]{2, 0, 0, 0};
    byte[] overlayShapes = new byte[tileWidth * tileHeight];

    int[] terrainOcclusionFlags = resolver.resolve(
        tileWidth,
        tileHeight,
        surfacePlanes,
        heightSamplesByPlane,
        underlayIds,
        overlayIds,
        overlayShapes
    );

    assertThat(terrainOcclusionFlags[0]).isZero();
  }

  @Test
  void skipsShapedUpperPlaneTilesWithoutAnUnderlay() {
    TerrainOcclusionFlagResolver resolver = new TerrainOcclusionFlagResolver(testFloorColors());
    int tileWidth = 2;
    int tileHeight = 2;
    byte[] surfacePlanes = new byte[]{1, 0, 0, 0};
    int[][] heightSamplesByPlane = new int[4][tileWidth * tileHeight];
    int[] underlayIds = new int[tileWidth * tileHeight];
    int[] overlayIds = new int[]{1, 0, 0, 0};
    byte[] overlayShapes = new byte[]{1, 0, 0, 0};

    int[] terrainOcclusionFlags = resolver.resolve(
        tileWidth,
        tileHeight,
        surfacePlanes,
        heightSamplesByPlane,
        underlayIds,
        overlayIds,
        overlayShapes
    );

    assertThat(terrainOcclusionFlags[0]).isZero();
  }

  private static FloorColorCatalog testFloorColors() {
    return FloorColorCatalog.parse(new byte[]{
        0, 2,
        1, 0x22, 0x55, 0x22, 0,
        1, 0x44, 0x66, (byte) 0x88, 5, 0
    });
  }
}
