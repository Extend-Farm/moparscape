package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerrainSurfaceElevationResolverTest {

  @Test
  void choosesTheHighestAdjacentVisibleSurfaceAtBridgeCorners() {
    TerrainSurfaceElevationResolver resolver = new TerrainSurfaceElevationResolver();
    int tileWidth = 3;
    int tileHeight = 3;
    byte[] surfacePlanes = new byte[tileWidth * tileHeight];
    int[][] heightSamplesByPlane = new int[4][tileWidth * tileHeight];

    int bridgeTileIndex = 1;
    int sharedCornerIndex = 1 * tileWidth + 1;
    surfacePlanes[bridgeTileIndex] = 1;
    heightSamplesByPlane[0][sharedCornerIndex] = 4;
    heightSamplesByPlane[1][sharedCornerIndex] = 22;

    int[] resolvedElevations = resolver.resolve(tileWidth, tileHeight, surfacePlanes, heightSamplesByPlane);

    assertThat(resolvedElevations[sharedCornerIndex]).isEqualTo(22);
  }

  @Test
  void keepsGroundPlaneHeightWhenNoAdjacentTileUsesAnUpperSurface() {
    TerrainSurfaceElevationResolver resolver = new TerrainSurfaceElevationResolver();
    int tileWidth = 3;
    int tileHeight = 3;
    byte[] surfacePlanes = new byte[tileWidth * tileHeight];
    int[][] heightSamplesByPlane = new int[4][tileWidth * tileHeight];

    int sharedCornerIndex = 1 * tileWidth + 1;
    heightSamplesByPlane[0][sharedCornerIndex] = 7;
    heightSamplesByPlane[1][sharedCornerIndex] = 22;

    int[] resolvedElevations = resolver.resolve(tileWidth, tileHeight, surfacePlanes, heightSamplesByPlane);

    assertThat(resolvedElevations[sharedCornerIndex]).isEqualTo(7);
  }
}
