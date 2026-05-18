package io.github.ffakira.rsps.client.desktop.core;

final class WorldSceneCameraPlanner {

  private static final int SAMPLE_RADIUS = 5;
  private static final float BASE_MIN_PITCH_DEGREES = 26.0f;
  private static final float BASE_MAX_PITCH_DEGREES = 32.5f;
  private static final float MIN_PITCH_DEGREES = 22.0f;
  private static final float MAX_PITCH_DEGREES = 38.5f;
  private static final float DEFAULT_YAW_DEGREES = 180.0f;
  private static final float MIN_DISTANCE = 12.1f;
  private static final float MAX_DISTANCE = 16.3f;
  private static final float MIN_FORWARD_FOCUS = 0.35f;
  private static final float MAX_FORWARD_FOCUS = 1.10f;
  private static final float MAX_MOVEMENT_LOOK_AHEAD = 0.42f;
  private static final float MAX_MOVEMENT_LATERAL_BIAS = 0.22f;

  private WorldSceneCameraPlanner() {
  }

  static WorldCameraState plan(
      WorldScene worldScene,
      int focusTileX,
      int focusTileY,
      ActorAnimationState actorAnimationState,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight
  ) {
    return plan(
        worldScene,
        focusTileX,
        focusTileY,
        actorAnimationState,
        worldHeightScale,
        viewportWidth,
        viewportHeight,
        0.0f,
        0.0f
    );
  }

  static WorldCameraState plan(
      WorldScene worldScene,
      int focusTileX,
      int focusTileY,
      ActorAnimationState actorAnimationState,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight,
      float cameraYawOffsetDegrees,
      float cameraPitchOffsetDegrees
  ) {
    // This is a transitional player-follow camera, not a region-overview planner. The native
    // viewport should feel closer to RuneScape's play camera even before full scene-graph parity
    // exists, so the focal point is biased forward from the player while the camera stays on a
    // slightly lifted oblique lane instead of the old stitched-region overview.
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
    float basePitchDegrees = clamp(
        BASE_MIN_PITCH_DEGREES + relief * 0.0035f,
        BASE_MIN_PITCH_DEGREES,
        BASE_MAX_PITCH_DEGREES
    );
    float pitchDegrees = clamp(basePitchDegrees + cameraPitchOffsetDegrees, MIN_PITCH_DEGREES, MAX_PITCH_DEGREES);
    float distance = clamp(
        12.85f + relief * 0.0090f + Math.max(0.0f, 1.28f - aspectRatio) * 1.10f,
        MIN_DISTANCE,
        MAX_DISTANCE
    );
    float forwardFocus = clamp(0.58f + relief * 0.0014f, MIN_FORWARD_FOCUS, MAX_FORWARD_FOCUS);
    float movementLookAhead = actorAnimationState == null
        ? 0.0f
        : actorAnimationState.strideWeight() * MAX_MOVEMENT_LOOK_AHEAD;
    float movementLateralBias = actorAnimationState == null
        ? 0.0f
        : actorAnimationState.strideWeight() * MAX_MOVEMENT_LATERAL_BIAS;
    float headingForwardX = actorAnimationState == null ? 0.0f : actorAnimationState.forwardX();
    float headingForwardY = actorAnimationState == null ? 1.0f : actorAnimationState.forwardY();
    float focusX = clamp(
        focusTileX + 0.5f + headingForwardX * movementLateralBias,
        0.5f,
        worldScene.tileWidth() - 0.5f
    );
    float focusY = clamp(
        focusTileY + forwardFocus + Math.max(-0.25f, headingForwardY) * movementLookAhead,
        0.5f,
        worldScene.tileHeight() - 0.5f
    );
    float screenOffsetY = -0.56f - relief * 0.0010f - movementLookAhead * 0.14f;
    float focusHeight = ((worldScene.elevationAt(focusTileX, focusTileY) * 0.6f) + (averageElevation * 0.4f)) * worldHeightScale;
    return new WorldCameraState(
        pitchDegrees,
        normalizeDegrees(DEFAULT_YAW_DEGREES + cameraYawOffsetDegrees),
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

  private static float normalizeDegrees(float degrees) {
    float normalized = degrees % 360.0f;
    if (normalized > 180.0f) {
      normalized -= 360.0f;
    } else if (normalized <= -180.0f) {
      normalized += 360.0f;
    }
    return normalized;
  }
}
