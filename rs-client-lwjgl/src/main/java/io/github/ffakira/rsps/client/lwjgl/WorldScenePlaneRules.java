package io.github.ffakira.rsps.client.lwjgl;

import io.github.ffakira.rsps.content.TerrainRegionData;

final class WorldScenePlaneRules {

  private WorldScenePlaneRules() {
  }

  static int effectivePlane(TerrainRegionData terrainRegionData, int sourcePlane, int tileX, int tileY) {
    if ((terrainRegionData.tileFlagAt(sourcePlane, tileX, tileY) & 8) != 0) {
      return 0;
    }
    if (sourcePlane > 0 && (terrainRegionData.tileFlagAt(1, tileX, tileY) & 2) != 0) {
      return sourcePlane - 1;
    }
    return sourcePlane;
  }

  static boolean hasRoofFlag(WorldScene worldScene, int localX, int localY) {
    return (worldScene.tileFlagAt(localX, localY) & 4) != 0;
  }

  static boolean roofFlagOnPath(WorldScene worldScene, int startX, int startY, int endX, int endY) {
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
}
