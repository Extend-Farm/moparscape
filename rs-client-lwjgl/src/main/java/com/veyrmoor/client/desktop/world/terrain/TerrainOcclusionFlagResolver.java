package com.veyrmoor.client.desktop.world.terrain;

import com.veyrmoor.content.FloorColorCatalog;

public final class TerrainOcclusionFlagResolver {

  public static final int LEGACY_FLAT_TILE_OCCLUSION_MASK = 0x924;
  private final FloorColorCatalog floorColors;

  public TerrainOcclusionFlagResolver(FloorColorCatalog floorColors) {
    this.floorColors = floorColors;
  }

  public int[] resolve(
      int tileWidth,
      int tileHeight,
      byte[] surfacePlanes,
      int[][] heightSamplesByPlane,
      int[] underlayIds,
      int[] overlayIds,
      byte[] overlayShapes
  ) {
    int[] terrainOcclusionFlags = new int[tileWidth * tileHeight];
    for (int tileY = 0; tileY < tileHeight - 1; tileY++) {
      for (int tileX = 0; tileX < tileWidth - 1; tileX++) {
        int tileIndex = tileY * tileWidth + tileX;
        int surfacePlane = surfacePlanes[tileIndex] & 0xff;
        if (surfacePlane <= 0 || surfacePlane >= heightSamplesByPlane.length) {
          continue;
        }
        if (!canOcclude(underlayIds[tileIndex], overlayIds[tileIndex], overlayShapes[tileIndex] & 0xff)) {
          continue;
        }
        if (!isFlatTile(heightSamplesByPlane[surfacePlane], tileWidth, tileX, tileY)) {
          continue;
        }
        terrainOcclusionFlags[tileIndex] |= LEGACY_FLAT_TILE_OCCLUSION_MASK;
      }
    }
    return terrainOcclusionFlags;
  }

  private boolean canOcclude(int underlayId, int overlayId, int overlayShape) {
    if (underlayId == 0 && overlayShape != 0) {
      return false;
    }
    return overlayId <= 0 || floorColors.definitionFor(overlayId).occludes();
  }

  private boolean isFlatTile(int[] planeHeights, int tileWidth, int tileX, int tileY) {
    int northWest = planeHeights[tileY * tileWidth + tileX];
    int northEast = planeHeights[tileY * tileWidth + tileX + 1];
    int southWest = planeHeights[(tileY + 1) * tileWidth + tileX];
    int southEast = planeHeights[(tileY + 1) * tileWidth + tileX + 1];
    return northWest == northEast
        && northWest == southEast
        && northWest == southWest;
  }
}
