package io.github.ffakira.rsps.client.desktop.world.visibility;

import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;

public final class WorldSceneVisibilityPlanner {

  private static final int MIN_HALF_WIDTH = 12;
  private static final int MAX_HALF_WIDTH = 25;
  private static final int MIN_HALF_DEPTH = 11;
  private static final int MAX_HALF_DEPTH = 25;
  private static final int VISIBILITY_MARGIN = 5;
  private static final int ROOFED_HALF_WIDTH_REDUCTION = 6;
  private static final int ROOFED_HALF_DEPTH_REDUCTION = 5;

  private WorldSceneVisibilityPlanner() {
  }

  public static WorldSceneVisibilityWindow plan(
      WorldScene worldScene,
      WorldCameraState cameraState,
      int focusTileX,
      int focusTileY,
      int viewportWidth,
      int viewportHeight
  ) {
    float aspectRatio = viewportWidth / (float) Math.max(1, viewportHeight);
    int halfWidth = clamp(
        Math.round(cameraState.distance() * 0.55f * Math.max(0.90f, aspectRatio)) + VISIBILITY_MARGIN,
        MIN_HALF_WIDTH,
        MAX_HALF_WIDTH
    );
    int halfDepth = clamp(
        Math.round(cameraState.distance() * 0.60f) + VISIBILITY_MARGIN,
        MIN_HALF_DEPTH,
        MAX_HALF_DEPTH
    );

    // The native renderer still lacks the legacy visible-tile traversal, so submission keeps a
    // coarse camera-centered window. Bias it toward the old 51x51 working area rather than the
    // earlier tiny window, otherwise the play viewport reads like a clipped scene preview.
    int centerX = clamp(Math.round(cameraState.focusX()), 0, worldScene.tileWidth() - 2);
    int centerY = clamp(Math.round(cameraState.focusY()), 0, worldScene.tileHeight() - 2);
    boolean roofedPath = WorldScenePlaneRules.roofFlagOnPath(worldScene, centerX, centerY, focusTileX, focusTileY)
        || WorldScenePlaneRules.hasRoofFlag(worldScene, focusTileX, focusTileY);
    if (roofedPath) {
      // Legacy camera-plane selection line-steps between the camera tile and player tile looking
      // for roof flag `& 4`. The native client does not yet switch scene planes like the old
      // client, but it can still react to that same signal by tightening the submitted view.
      halfWidth = Math.max(MIN_HALF_WIDTH, halfWidth - ROOFED_HALF_WIDTH_REDUCTION);
      halfDepth = Math.max(MIN_HALF_DEPTH, halfDepth - ROOFED_HALF_DEPTH_REDUCTION);
      centerX = clamp(focusTileX, 0, worldScene.tileWidth() - 2);
      centerY = clamp(focusTileY, 0, worldScene.tileHeight() - 2);
    }
    int minLocalX = clamp(centerX - halfWidth, 0, worldScene.tileWidth() - 2);
    int maxLocalX = clamp(centerX + halfWidth, 0, worldScene.tileWidth() - 2);
    int minLocalY = clamp(centerY - halfDepth, 0, worldScene.tileHeight() - 2);
    int maxLocalY = clamp(centerY + halfDepth, 0, worldScene.tileHeight() - 2);

    minLocalX = Math.min(minLocalX, clamp(focusTileX, 0, worldScene.tileWidth() - 2));
    maxLocalX = Math.max(maxLocalX, clamp(focusTileX, 0, worldScene.tileWidth() - 2));
    minLocalY = Math.min(minLocalY, clamp(focusTileY, 0, worldScene.tileHeight() - 2));
    maxLocalY = Math.max(maxLocalY, clamp(focusTileY, 0, worldScene.tileHeight() - 2));
    return new WorldSceneVisibilityWindow(minLocalX, maxLocalX, minLocalY, maxLocalY);
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
