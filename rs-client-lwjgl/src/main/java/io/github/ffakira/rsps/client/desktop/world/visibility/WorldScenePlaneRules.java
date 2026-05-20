package io.github.ffakira.rsps.client.desktop.world.visibility;

import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.content.TerrainRegionData;

public final class WorldScenePlaneRules {

  private static final float ROOFED_RENDER_PITCH_THRESHOLD_DEGREES = 310.0f * 360.0f / 2048.0f;

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

  public static int objectScenePlane(
      TerrainRegionData terrainRegionData,
      int requestedPlane,
      int placementPlane,
      int tileX,
      int tileY
  ) {
    if (placementPlane == requestedPlane) {
      return requestedPlane;
    }
    // Bridge objects follow the same visible-surface contract as the deck terrain: the old client
    // can show plane-1 bridge pieces while the active gameplay plane remains 0 underneath them.
    // Without this explicit projection rule, the native scene keeps the ground deck but drops the
    // matching plane-1 bridge walls and trims, which is why Lumbridge bridge still looks broken.
    if (requestedPlane == 0
        && placementPlane == 1
        && (terrainRegionData.tileFlagAt(1, tileX, tileY) & 2) != 0) {
      return 0;
    }
    return -1;
  }

  public static boolean hasRoofFlag(WorldScene worldScene, int localX, int localY) {
    return (worldScene.tileFlagAt(localX, localY) & 4) != 0;
  }

  public static int renderPlane(
      WorldScene worldScene,
      WorldCameraState cameraState,
      int focusTileX,
      int focusTileY
  ) {
    if (cameraState.pitchDegrees() >= ROOFED_RENDER_PITCH_THRESHOLD_DEGREES) {
      return 3;
    }
    int[] cameraTile = cameraTile(worldScene, cameraState);
    if (hasRoofFlag(worldScene, cameraTile[0], cameraTile[1])
        || roofFlagOnPath(worldScene, cameraTile[0], cameraTile[1], focusTileX, focusTileY)
        || hasRoofFlag(worldScene, focusTileX, focusTileY)) {
      return worldScene.plane();
    }
    return 3;
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

  static int[] cameraTile(WorldScene worldScene, WorldCameraState cameraState) {
    float[] cameraPosition = cameraPosition(cameraState);
    return new int[]{
        clamp((int) Math.floor(cameraPosition[0]), 0, worldScene.tileWidth() - 2),
        clamp((int) Math.floor(cameraPosition[2]), 0, worldScene.tileHeight() - 2)
    };
  }

  private static float[] cameraPosition(WorldCameraState cameraState) {
    float pitchRadians = (float) Math.toRadians(cameraState.pitchDegrees());
    float yawRadians = (float) Math.toRadians(-cameraState.yawDegrees());
    float offsetY = -cameraState.screenOffsetY();
    float offsetZ = cameraState.distance();
    float rotatedY = (float) (offsetY * Math.cos(pitchRadians) + offsetZ * Math.sin(pitchRadians));
    float rotatedZ = (float) (-offsetY * Math.sin(pitchRadians) + offsetZ * Math.cos(pitchRadians));
    float rotatedX = (float) (-rotatedZ * Math.sin(yawRadians));
    float worldZ = (float) (rotatedZ * Math.cos(yawRadians));
    return new float[]{cameraState.focusX() + rotatedX, rotatedY + cameraState.focusHeight(), cameraState.focusY() + worldZ};
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static boolean hasSurfaceData(TerrainRegionData terrainRegionData, int plane, int tileX, int tileY) {
    return terrainRegionData.underlayIdAt(plane, tileX, tileY) > 0
        || terrainRegionData.overlayIdAt(plane, tileX, tileY) > 0;
  }
}
