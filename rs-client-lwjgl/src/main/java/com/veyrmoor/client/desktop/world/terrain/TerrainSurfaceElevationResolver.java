package com.veyrmoor.client.desktop.world.terrain;

/**
 * Resolves the scene-local terrain corner height after terrain capture has already chosen one
 * visible surface plane per tile.
 *
 * <p>Bridge seams are a corner problem, not only a tile problem: the visible deck can sit on plane
 * 1 while the adjacent river remains on plane 0. If the rewrite collapses each shared corner to
 * the height of just one chosen tile, the deck edge turns into the wrong wall/ramp shape. This
 * resolver preserves all per-plane height samples and then picks the highest visible adjacent
 * surface for each shared corner sample.
 */
public final class TerrainSurfaceElevationResolver {

  public int[] resolve(int tileWidth, int tileHeight, byte[] surfacePlanes, int[][] heightSamplesByPlane) {
    int[] resolvedElevations = new int[tileWidth * tileHeight];
    for (int sampleY = 0; sampleY < tileHeight; sampleY++) {
      for (int sampleX = 0; sampleX < tileWidth; sampleX++) {
        int sampleIndex = sampleY * tileWidth + sampleX;
        resolvedElevations[sampleIndex] = resolveSampleElevation(
            tileWidth,
            tileHeight,
            sampleX,
            sampleY,
            sampleIndex,
            surfacePlanes,
            heightSamplesByPlane
        );
      }
    }
    return resolvedElevations;
  }

  private int resolveSampleElevation(
      int tileWidth,
      int tileHeight,
      int sampleX,
      int sampleY,
      int sampleIndex,
      byte[] surfacePlanes,
      int[][] heightSamplesByPlane
  ) {
    int highestVisibleElevation = Integer.MIN_VALUE;
    boolean foundVisibleSurface = false;
    for (int offsetY = 0; offsetY <= 1; offsetY++) {
      for (int offsetX = 0; offsetX <= 1; offsetX++) {
        int tileX = sampleX - offsetX;
        int tileY = sampleY - offsetY;
        if (tileX < 0 || tileY < 0 || tileX >= tileWidth || tileY >= tileHeight) {
          continue;
        }
        int surfacePlane = surfacePlanes[tileY * tileWidth + tileX] & 0xff;
        if (surfacePlane < 0 || surfacePlane >= heightSamplesByPlane.length) {
          continue;
        }
        highestVisibleElevation = Math.max(highestVisibleElevation, heightSamplesByPlane[surfacePlane][sampleIndex]);
        foundVisibleSurface = true;
      }
    }
    if (foundVisibleSurface) {
      return highestVisibleElevation;
    }
    return heightSamplesByPlane[0][sampleIndex];
  }
}
