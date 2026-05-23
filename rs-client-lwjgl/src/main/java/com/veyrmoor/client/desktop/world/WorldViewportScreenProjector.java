package com.veyrmoor.client.desktop.world;

import com.veyrmoor.client.desktop.render.common.ScreenRect;

public final class WorldViewportScreenProjector {

  private WorldViewportScreenProjector() {
  }

  public static ScreenProjection project(
      ScreenRect viewportRect,
      WorldCameraState cameraState,
      float worldX,
      float worldY,
      float worldZ
  ) {
    if (viewportRect == null || cameraState == null) {
      return null;
    }
    float localX = worldX - cameraState.focusX();
    float localY = worldY - cameraState.focusHeight();
    float localZ = -(worldZ - cameraState.focusY());
    float yawRadians = (float) Math.toRadians(-cameraState.yawDegrees());
    float yawCosine = (float) Math.cos(yawRadians);
    float yawSine = (float) Math.sin(yawRadians);
    float pitchRadians = (float) Math.toRadians(cameraState.pitchDegrees());
    float pitchCosine = (float) Math.cos(pitchRadians);
    float pitchSine = (float) Math.sin(pitchRadians);
    float yawAdjustedX = localX * yawCosine + localZ * yawSine;
    float yawAdjustedZ = -localX * yawSine + localZ * yawCosine;
    float viewY = localY * pitchCosine - yawAdjustedZ * pitchSine + cameraState.screenOffsetY();
    float viewZ = localY * pitchSine + yawAdjustedZ * pitchCosine - cameraState.distance();
    float depth = -viewZ;
    if (depth <= WorldViewportProjection.NEAR_PLANE) {
      return null;
    }
    float projectedX = yawAdjustedX / depth;
    float projectedY = -viewY / depth;
    float screenX = viewportRect.left()
        + viewportRect.width() * 0.5f
        + projectedX * WorldViewportProjection.LEGACY_FOCAL_LENGTH_PIXELS;
    float screenY = viewportRect.top()
        + viewportRect.height() * 0.5f
        + projectedY * WorldViewportProjection.LEGACY_FOCAL_LENGTH_PIXELS;
    return new ScreenProjection(screenX, screenY, depth);
  }

  public record ScreenProjection(float screenX, float screenY, float depth) {
  }
}
