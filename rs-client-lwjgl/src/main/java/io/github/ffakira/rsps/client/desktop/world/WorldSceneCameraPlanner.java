package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.character.ActorAnimationState;

final class WorldSceneCameraPlanner {

  private static final int SAMPLE_RADIUS = 5;
  private static final float BASE_MIN_PITCH_DEGREES = 28.0f;
  private static final float BASE_MAX_PITCH_DEGREES = 36.0f;
  private static final float MIN_PITCH_DEGREES = 22.5f;
  private static final float MAX_PITCH_DEGREES = 50.0f;
  private static final float DEFAULT_YAW_DEGREES = 180.0f;
  private static final float MIN_DISTANCE = 12.4f;
  private static final float MAX_DISTANCE = 16.8f;

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
        focusTileX + 0.5f,
        focusTileY + 0.5f,
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
      float focusLocalX,
      float focusLocalY,
      ActorAnimationState actorAnimationState,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight
  ) {
    return plan(
        worldScene,
        focusLocalX,
        focusLocalY,
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
      float focusLocalX,
      float focusLocalY,
      ActorAnimationState actorAnimationState,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight,
      float cameraYawOffsetDegrees,
      float cameraPitchOffsetDegrees
  ) {
    // Official 317 keeps the camera centered on the smoothed player world position, then derives
    // pitch from nearby relief. It does not bias the focal point forward into the walk direction.
    int focusTileX = clamp((int) Math.floor(focusLocalX), 0, worldScene.tileWidth() - 1);
    int focusTileY = clamp((int) Math.floor(focusLocalY), 0, worldScene.tileHeight() - 1);
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
        13.0f + relief * 0.0090f + Math.max(0.0f, 1.28f - aspectRatio) * 1.05f,
        MIN_DISTANCE,
        MAX_DISTANCE
    );
    float focusX = clamp(focusLocalX, 0.5f, worldScene.tileWidth() - 0.5f);
    float focusY = clamp(focusLocalY, 0.5f, worldScene.tileHeight() - 0.5f);
    float screenOffsetY = -0.50f - relief * 0.0010f;
    float focusHeight = ((sampleHeight(worldScene, focusX, focusY) * 0.6f) + (averageElevation * 0.4f)) * worldHeightScale;
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

  private static float sampleHeight(WorldScene worldScene, float localX, float localY) {
    int tileX = clamp((int) Math.floor(localX), 0, worldScene.tileWidth() - 2);
    int tileY = clamp((int) Math.floor(localY), 0, worldScene.tileHeight() - 2);
    float offsetX = clamp(localX - tileX, 0.0f, 1.0f);
    float offsetY = clamp(localY - tileY, 0.0f, 1.0f);
    float northWest = worldScene.elevationAt(tileX, tileY);
    float northEast = worldScene.elevationAt(tileX + 1, tileY);
    float southEast = worldScene.elevationAt(tileX + 1, tileY + 1);
    float southWest = worldScene.elevationAt(tileX, tileY + 1);
    float northBlend = northWest + (northEast - northWest) * offsetX;
    float southBlend = southWest + (southEast - southWest) * offsetX;
    return northBlend + (southBlend - northBlend) * offsetY;
  }
}
