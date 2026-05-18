package io.github.ffakira.rsps.client.desktop.world.visibility;

import io.github.ffakira.rsps.client.desktop.core.WorldScene;
import io.github.ffakira.rsps.content.TerrainRegionData;

public final class WorldScenePlaneRules {

  private WorldScenePlaneRules() {
  }

  public static int effectivePlane(TerrainRegionData terrainRegionData, int sourcePlane, int tileX, int tileY) {
    if ((terrainRegionData.tileFlagAt(sourcePlane, tileX, tileY) & 8) != 0) {
      return 0;
    }
    if (sourcePlane > 0 && (terrainRegionData.tileFlagAt(1, tileX, tileY) & 2) != 0) {
      return sourcePlane - 1;
    }
    return sourcePlane;
  }

  public static int surfacePlane(TerrainRegionData terrainRegionData, int requestedPlane, int tileX, int tileY) {
    // The old client renders bridge decks from plane 1 while the player's effective collision
    // plane remains 0 underneath them. The rewrite still stores one terrain surface per local
    // tile, so bridge-marked tiles need an explicit "visible surface" rule or the scene can only
    // show the river below or the bridge above, never both in the right places.
    if (requestedPlane == 0
        && (terrainRegionData.tileFlagAt(1, tileX, tileY) & 2) != 0
        && hasSurfaceData(terrainRegionData, 1, tileX, tileY)) {
      return 1;
    }
    return effectivePlane(terrainRegionData, requestedPlane, tileX, tileY);
  }

  public static boolean hasRoofFlag(WorldScene worldScene, int localX, int localY) {
    return (worldScene.tileFlagAt(localX, localY) & 4) != 0;
  }

  public static boolean roofFlagOnPath(WorldScene worldScene, int startX, int startY, int endX, int endY) {
    int currentX = clamp(startX, 0, worldScene.tileWidth() - 1);
    int currentY = clamp(startY, 0, worldScene.tileHeight() - 1);
    int targetX = clamp(endX, 0, worldScene.tileWidth() - 1);
    int targetY = clamp(endY, 0, worldScene.tileHeight() - 1);
    if (hasRoofFlag(worldScene, currentX, currentY)) {
      return true;
    }
    int deltaX = Math.abs(targetX - currentX);
    int deltaY = Math.abs(targetY - currentY);
    int stepX = Integer.compare(targetX, currentX);
    int stepY = Integer.compare(targetY, currentY);
    if (deltaX > deltaY) {
      int scaledY = 32768;
      int slope = deltaY == 0 ? 0 : (deltaY * 0x10000) / deltaX;
      while (currentX != targetX) {
        currentX += stepX;
        if (hasRoofFlag(worldScene, currentX, currentY)) {
          return true;
        }
        scaledY += slope;
        if (scaledY >= 0x10000) {
          scaledY -= 0x10000;
          currentY += stepY;
          if (hasRoofFlag(worldScene, currentX, currentY)) {
            return true;
          }
        }
      }
      return false;
    }
    int scaledX = 32768;
    int slope = deltaY == 0 ? 0 : (deltaX * 0x10000) / deltaY;
    while (currentY != targetY) {
      currentY += stepY;
      if (hasRoofFlag(worldScene, currentX, currentY)) {
        return true;
      }
      scaledX += slope;
      if (scaledX >= 0x10000) {
        scaledX -= 0x10000;
        currentX += stepX;
        if (hasRoofFlag(worldScene, currentX, currentY)) {
          return true;
        }
      }
    }
    return false;
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static boolean hasSurfaceData(TerrainRegionData terrainRegionData, int plane, int tileX, int tileY) {
    return terrainRegionData.underlayIdAt(plane, tileX, tileY) > 0
        || terrainRegionData.overlayIdAt(plane, tileX, tileY) > 0;
  }
}
