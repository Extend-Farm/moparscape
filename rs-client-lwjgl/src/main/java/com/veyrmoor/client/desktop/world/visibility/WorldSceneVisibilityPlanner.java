package com.veyrmoor.client.desktop.world.visibility;

import com.veyrmoor.client.desktop.world.WorldCameraState;
import com.veyrmoor.client.desktop.world.WorldScene;

public final class WorldSceneVisibilityPlanner {

  private static final int LEGACY_HALF_WINDOW = 25;
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
    int halfWidth = LEGACY_HALF_WINDOW;
    int halfDepth = LEGACY_HALF_WINDOW;
    int renderPlane = WorldScenePlaneRules.renderPlane(worldScene, cameraState, focusTileX, focusTileY);
    // Anchor the visibility window on the player (focus), not on the camera tile. The camera
    // sits ~14 tiles away from focus and orbits as the user changes yaw, so a camera-anchored
    // window slides off the player as the view rotates and tiles "disappear" on the opposite
    // side of the orbit. Legacy SceneGraph also does additional per-tile visibility masks that
    // we don't replicate; without those, the raw 51x51 window must be focus-centred to keep the
    // played area in view consistently.
    int centerX = clamp(focusTileX, 0, worldScene.tileWidth() - 2);
    int centerY = clamp(focusTileY, 0, worldScene.tileHeight() - 2);
    // Legacy SceneGraph traversal works from the camera tile and always considers a 51x51 tile
    // neighborhood around that tile before the deeper visibility tests trim it down. The native
    // client still lacks those per-tile visibility masks, but keeping the same camera-anchored
    // working area is much closer than a player-centered radius heuristic.
    if (renderPlane == worldScene.plane()) {
      // Legacy camera-plane selection line-steps between the camera tile and player tile looking
      // for roof flag `& 4`, but only while the camera pitch is below the classic 310-unit
      // threshold. The rewrite still renders one stitched surface instead of full per-plane
      // traversal, so it reacts to that selected roofed plane by tightening the submitted view.
      halfWidth = LEGACY_HALF_WINDOW - ROOFED_HALF_WIDTH_REDUCTION;
      halfDepth = LEGACY_HALF_WINDOW - ROOFED_HALF_DEPTH_REDUCTION;
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
