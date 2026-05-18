package io.github.ffakira.rsps.client.desktop.core;

final class WorldViewportProjection {

  static final float NEAR_PLANE = 1.2f;
  static final float FAR_PLANE = 96.0f;
  static final float HALF_VERTICAL_FOV_DEGREES = 12.0f;

  private WorldViewportProjection() {
  }

  static float frustumTop() {
    return (float) (Math.tan(Math.toRadians(HALF_VERTICAL_FOV_DEGREES)) * NEAR_PLANE);
  }

  static float frustumRight(float aspectRatio) {
    return frustumTop() * aspectRatio;
  }
}
