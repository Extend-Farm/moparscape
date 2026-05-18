package io.github.ffakira.rsps.client.lwjgl;

final class WorldSceneCameraPlanner {

  private static final int SAMPLE_RADIUS = 5;
  private static final float MIN_PITCH_DEGREES = 24.0f;
  private static final float MAX_PITCH_DEGREES = 31.0f;
  private static final float DEFAULT_YAW_DEGREES = -45.0f;
  private static final float MIN_DISTANCE = 16.0f;
  private static final float MAX_DISTANCE = 27.0f;
  private static final float MIN_FORWARD_FOCUS = 1.25f;
  private static final float MAX_FORWARD_FOCUS = 3.0f;
  private static final float SIDE_FOCUS_FACTOR = 0.62f;

  private WorldSceneCameraPlanner() {
  }

  static WorldCameraState plan(
      WorldScene worldScene,
      int focusTileX,
      int focusTileY,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight
  ) {
    // This is a transitional player-follow camera, not a region-overview planner. The native
    // viewport should feel closer to RuneScape's play camera even before full scene-graph parity
    // exists, so the focal point is biased forward from the player and the camera distance/pitch
    // are intentionally much tighter than the earlier stitched-region preview.
    int minLocalX = clamp(focusTileX - SAMPLE_RADIUS, 0, worldScene.tileWidth() - 1);
    int maxLocalX = clamp(focusTileX + SAMPLE_RADIUS, 0, worldScene.tileWidth() - 1);
    int minLocalY = clamp(focusTileY - SAMPLE_RADIUS, 0, worldScene.tileHeight() - 1);
    int maxLocalY = clamp(focusTileY + SAMPLE_RADIUS, 0, worldScene.tileHeight() - 1);

    int minElevation = Integer.MAX_VALUE;
    int maxElevation = Integer.MIN_VALUE;
    long elevationTotal = 0L;
    int samples = 0;
    for (int localY = minLocalY; localY <= maxLocalY; localY++) {
      for (int localX = minLocalX; localX <= maxLocalX; localX++) {
        int elevation = worldScene.elevationAt(localX, localY);
        minElevation = Math.min(minElevation, elevation);
        maxElevation = Math.max(maxElevation, elevation);
        elevationTotal += elevation;
        samples++;
      }
    }

    float averageElevation = samples == 0 ? worldScene.elevationAt(focusTileX, focusTileY) : (float) elevationTotal / samples;
    float relief = Math.max(0.0f, maxElevation - minElevation);
    float aspectRatio = viewportWidth / (float) Math.max(1, viewportHeight);
    float pitchDegrees = clamp(MIN_PITCH_DEGREES + relief * 0.01f, MIN_PITCH_DEGREES, MAX_PITCH_DEGREES);
    float distance = clamp(
        19.0f + relief * 0.028f + Math.max(0.0f, 1.35f - aspectRatio) * 3.0f,
        MIN_DISTANCE,
        MAX_DISTANCE
    );
    float forwardFocus = clamp(1.6f + relief * 0.009f, MIN_FORWARD_FOCUS, MAX_FORWARD_FOCUS);
    float focusX = clamp(focusTileX + forwardFocus, 0.5f, worldScene.tileWidth() - 0.5f);
    float focusY = clamp(focusTileY + forwardFocus * SIDE_FOCUS_FACTOR, 0.5f, worldScene.tileHeight() - 0.5f);
    float screenOffsetY = -0.9f - relief * 0.003f;
    float focusHeight = ((worldScene.elevationAt(focusTileX, focusTileY) * 0.6f) + (averageElevation * 0.4f)) * worldHeightScale;
    return new WorldCameraState(
        pitchDegrees,
        DEFAULT_YAW_DEGREES,
        distance,
        screenOffsetY,
        focusX,
        focusY,
        focusHeight
    );
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
