package io.github.ffakira.rsps.client.desktop.world;

final class WorldSceneCameraPlanner {

  private static final float CAMERA_UNITS_PER_DEGREE = 2048.0f / 360.0f;
  private static final float DEGREES_PER_CAMERA_UNIT = 360.0f / 2048.0f;
  private static final int MIN_CAMERA_PITCH_UNITS = 128;
  private static final int MAX_CAMERA_PITCH_UNITS = 383;
  private static final int MIN_CAMERA_PITCH_CLAMP = 32768;
  private static final int MAX_CAMERA_PITCH_CLAMP = 98048;
  private static final int PITCH_CLAMP_SAMPLE_RADIUS = 4;
  private static final float FOCUS_SNAP_DISTANCE_TILES = 500.0f / 128.0f;
  private static final float ORBIT_FOCUS_SMOOTHING = 16.0f;
  private static final float DEFAULT_YAW_DEGREES = 225.0f;
  private static final float DEFAULT_PITCH_DEGREES = 31.0f;
  private static final float COMPATIBILITY_DISTANCE_DIVISOR = 80.0f;
  private static final float COMPATIBILITY_SCREEN_OFFSET_Y = -0.65f;

  private String lastSceneKey;
  private float smoothedFocusX;
  private float smoothedFocusY;
  private int cameraPitchClamp = MIN_CAMERA_PITCH_CLAMP;
  private boolean initialized;

  WorldCameraState plan(
      WorldScene worldScene,
      int focusTileX,
      int focusTileY,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight
  ) {
    return plan(
        worldScene,
        focusTileX + 0.5f,
        focusTileY + 0.5f,
        worldHeightScale,
        viewportWidth,
        viewportHeight,
        DEFAULT_YAW_DEGREES,
        DEFAULT_PITCH_DEGREES
    );
  }

  WorldCameraState plan(
      WorldScene worldScene,
      float focusLocalX,
      float focusLocalY,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight
  ) {
    return plan(
        worldScene,
        focusLocalX,
        focusLocalY,
        worldHeightScale,
        viewportWidth,
        viewportHeight,
        DEFAULT_YAW_DEGREES,
        DEFAULT_PITCH_DEGREES
    );
  }

  WorldCameraState plan(
      WorldScene worldScene,
      float focusLocalX,
      float focusLocalY,
      float worldHeightScale,
      int viewportWidth,
      int viewportHeight,
      float cameraYawDegrees,
      float cameraPitchDegrees
  ) {
    float targetFocusX = clamp(focusLocalX, 0.5f, worldScene.tileWidth() - 0.5f);
    float targetFocusY = clamp(focusLocalY, 0.5f, worldScene.tileHeight() - 0.5f);
    updateFocus(worldScene, targetFocusX, targetFocusY);
    cameraPitchClamp = smoothPitchClamp(cameraPitchClamp, targetPitchClamp(worldScene));

    int requestedPitchUnits = clamp(Math.round(cameraPitchDegrees * CAMERA_UNITS_PER_DEGREE), MIN_CAMERA_PITCH_UNITS, MAX_CAMERA_PITCH_UNITS);
    int finalPitchUnits = Math.max(requestedPitchUnits, cameraPitchClamp / 256);
    int yawUnits = normalizeUnits(Math.round(cameraYawDegrees * CAMERA_UNITS_PER_DEGREE));
    float focusHeight = sampleHeight(worldScene, smoothedFocusX, smoothedFocusY) * worldHeightScale;
    return new WorldCameraState(
        degreesFromUnits(finalPitchUnits),
        normalizeDegrees(degreesFromUnits(yawUnits)),
        (finalPitchUnits * 3.0f + 600.0f) / COMPATIBILITY_DISTANCE_DIVISOR,
        COMPATIBILITY_SCREEN_OFFSET_Y,
        smoothedFocusX,
        smoothedFocusY,
        focusHeight
    );
  }

  private void updateFocus(WorldScene worldScene, float targetFocusX, float targetFocusY) {
    if (!initialized
        || !worldScene.sceneKey().equals(lastSceneKey)
        || Math.hypot(targetFocusX - smoothedFocusX, targetFocusY - smoothedFocusY) > FOCUS_SNAP_DISTANCE_TILES) {
      smoothedFocusX = targetFocusX;
      smoothedFocusY = targetFocusY;
      lastSceneKey = worldScene.sceneKey();
      initialized = true;
      return;
    }
    smoothedFocusX += (targetFocusX - smoothedFocusX) / ORBIT_FOCUS_SMOOTHING;
    smoothedFocusY += (targetFocusY - smoothedFocusY) / ORBIT_FOCUS_SMOOTHING;
  }

  private int targetPitchClamp(WorldScene worldScene) {
    int orbitTileX = clamp((int) smoothedFocusX, 0, worldScene.tileWidth() - 1);
    int orbitTileY = clamp((int) smoothedFocusY, 0, worldScene.tileHeight() - 1);
    int orbitHeight = Math.round(sampleHeight(worldScene, smoothedFocusX, smoothedFocusY));
    int maxRelief = 0;
    for (int tileY = Math.max(0, orbitTileY - PITCH_CLAMP_SAMPLE_RADIUS);
         tileY <= Math.min(worldScene.tileHeight() - 1, orbitTileY + PITCH_CLAMP_SAMPLE_RADIUS);
         tileY++) {
      for (int tileX = Math.max(0, orbitTileX - PITCH_CLAMP_SAMPLE_RADIUS);
           tileX <= Math.min(worldScene.tileWidth() - 1, orbitTileX + PITCH_CLAMP_SAMPLE_RADIUS);
           tileX++) {
        maxRelief = Math.max(maxRelief, orbitHeight - worldScene.elevationAt(tileX, tileY));
      }
    }
    return clamp(maxRelief * 192, MIN_CAMERA_PITCH_CLAMP, MAX_CAMERA_PITCH_CLAMP);
  }

  private int smoothPitchClamp(int currentClamp, int targetClamp) {
    if (targetClamp > currentClamp) {
      return currentClamp + (targetClamp - currentClamp) / 24;
    }
    if (targetClamp < currentClamp) {
      return currentClamp + (targetClamp - currentClamp) / 80;
    }
    return currentClamp;
  }

  private static int normalizeUnits(int units) {
    int normalized = units % 2048;
    return normalized < 0 ? normalized + 2048 : normalized;
  }

  private static float degreesFromUnits(int units) {
    return units * DEGREES_PER_CAMERA_UNIT;
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
