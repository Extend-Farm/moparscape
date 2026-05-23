package com.veyrmoor.client.desktop.world;

final class WorldViewportProjection {

  static final float NEAR_PLANE = 1.2f;
  static final float FAR_PLANE = 128.0f;
  static final float LEGACY_FOCAL_LENGTH_PIXELS = 512.0f;

  private WorldViewportProjection() {
  }

  static float frustumTop(float viewportHeight) {
    return NEAR_PLANE * (Math.max(1.0f, viewportHeight) * 0.5f) / LEGACY_FOCAL_LENGTH_PIXELS;
  }

  static float frustumRight(float viewportWidth) {
    return NEAR_PLANE * (Math.max(1.0f, viewportWidth) * 0.5f) / LEGACY_FOCAL_LENGTH_PIXELS;
  }

  static float halfVerticalFovDegrees(float viewportHeight) {
    return (float) Math.toDegrees(
        Math.atan((Math.max(1.0f, viewportHeight) * 0.5f) / LEGACY_FOCAL_LENGTH_PIXELS)
    );
  }
}
