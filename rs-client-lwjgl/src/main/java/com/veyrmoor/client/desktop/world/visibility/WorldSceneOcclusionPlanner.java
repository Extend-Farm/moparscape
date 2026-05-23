package com.veyrmoor.client.desktop.world.visibility;

import com.veyrmoor.client.desktop.world.WorldCameraState;
import com.veyrmoor.client.desktop.world.WorldScene;
import com.veyrmoor.client.desktop.world.WorldSceneScale;
import java.util.ArrayList;
import java.util.List;

public final class WorldSceneOcclusionPlanner {

  private static final float OCCLUSION_EPSILON = 0.01f;
  // Legacy SceneGraph only activates horizontal occluders when the occluding plane is at least
  // 128 raw height units above the camera. Native world heights are already scaled down from raw
  // cache units, so convert that threshold into the rewrite's world-space height scale.
  private static final float HORIZONTAL_ACTIVATION_MIN_HEIGHT = 128.0f * WorldSceneScale.HEIGHT_SCALE / 12.0f;

  private WorldSceneOcclusionPlanner() {
  }

  public static WorldSceneOcclusionContext plan(
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      WorldCameraState cameraState
  ) {
    // Legacy occluders are activated per frame from camera-relative state. The native client is
    // not at full SceneGraph parity yet, but it can still prefilter scene occluders here so
    // queue submission stops treating every visible tile/object as equally drawable.
    float[] cameraPosition = cameraPosition(cameraState);
    List<WorldSceneOccluder> activeOccluders = new ArrayList<>();
    for (WorldSceneOccluder occluder : worldScene.occluders()) {
      if (!occluder.intersectsWindow(visibilityWindow)) {
        continue;
      }
      if (!isActiveForCamera(occluder, cameraPosition[0], cameraPosition[1], cameraPosition[2])) {
        continue;
      }
      activeOccluders.add(occluder);
    }
    return new WorldSceneOcclusionContext(
        cameraPosition[0],
        cameraPosition[1],
        cameraPosition[2],
        activeOccluders
    );
  }

  public static boolean isOccluded(
      WorldSceneOcclusionContext occlusionContext,
      float targetX,
      float targetY,
      float targetZ
  ) {
    for (WorldSceneOccluder occluder : occlusionContext.activeOccluders()) {
      if (intersectsOccluder(
          occlusionContext.cameraX(),
          occlusionContext.cameraY(),
          occlusionContext.cameraZ(),
          targetX,
          targetY,
          targetZ,
          occluder
      )) {
        return true;
      }
    }
    return false;
  }

  private static boolean isActiveForCamera(
      WorldSceneOccluder occluder,
      float cameraX,
      float cameraY,
      float cameraZ
  ) {
    return switch (occluder.type()) {
      case X_WALL -> Math.abs(cameraX - occluder.axis()) > 0.25f;
      case Z_WALL -> Math.abs(cameraZ - occluder.axis()) > 0.25f;
      case HORIZONTAL_PLANE -> occluder.axis() - cameraY > HORIZONTAL_ACTIVATION_MIN_HEIGHT;
    };
  }

  private static boolean intersectsOccluder(
      float cameraX,
      float cameraY,
      float cameraZ,
      float targetX,
      float targetY,
      float targetZ,
      WorldSceneOccluder occluder
  ) {
    return switch (occluder.type()) {
      case X_WALL -> intersectsXWall(cameraX, cameraY, cameraZ, targetX, targetY, targetZ, occluder);
      case Z_WALL -> intersectsZWall(cameraX, cameraY, cameraZ, targetX, targetY, targetZ, occluder);
      case HORIZONTAL_PLANE -> intersectsHorizontalPlane(cameraX, cameraY, cameraZ, targetX, targetY, targetZ, occluder);
    };
  }

  private static boolean intersectsXWall(
      float cameraX,
      float cameraY,
      float cameraZ,
      float targetX,
      float targetY,
      float targetZ,
      WorldSceneOccluder occluder
  ) {
    float deltaX = targetX - cameraX;
    if (Math.abs(deltaX) <= OCCLUSION_EPSILON || sameSide(cameraX - occluder.axis(), targetX - occluder.axis())) {
      return false;
    }
    float t = (occluder.axis() - cameraX) / deltaX;
    if (t <= OCCLUSION_EPSILON || t >= 1.0f - OCCLUSION_EPSILON) {
      return false;
    }
    float hitY = lerp(cameraY, targetY, t);
    float hitZ = lerp(cameraZ, targetZ, t);
    return hitY >= occluder.minHeight()
        && hitY <= occluder.maxHeight()
        && hitZ >= occluder.minLocalY()
        && hitZ <= occluder.maxLocalY();
  }

  private static boolean intersectsZWall(
      float cameraX,
      float cameraY,
      float cameraZ,
      float targetX,
      float targetY,
      float targetZ,
      WorldSceneOccluder occluder
  ) {
    float deltaZ = targetZ - cameraZ;
    if (Math.abs(deltaZ) <= OCCLUSION_EPSILON || sameSide(cameraZ - occluder.axis(), targetZ - occluder.axis())) {
      return false;
    }
    float t = (occluder.axis() - cameraZ) / deltaZ;
    if (t <= OCCLUSION_EPSILON || t >= 1.0f - OCCLUSION_EPSILON) {
      return false;
    }
    float hitY = lerp(cameraY, targetY, t);
    float hitX = lerp(cameraX, targetX, t);
    return hitY >= occluder.minHeight()
        && hitY <= occluder.maxHeight()
        && hitX >= occluder.minLocalX()
        && hitX <= occluder.maxLocalX();
  }

  private static boolean intersectsHorizontalPlane(
      float cameraX,
      float cameraY,
      float cameraZ,
      float targetX,
      float targetY,
      float targetZ,
      WorldSceneOccluder occluder
  ) {
    if (cameraY >= occluder.axis() - OCCLUSION_EPSILON || targetY <= occluder.axis() + OCCLUSION_EPSILON) {
      return false;
    }
    float deltaY = targetY - cameraY;
    if (Math.abs(deltaY) <= OCCLUSION_EPSILON) {
      return false;
    }
    float t = (occluder.axis() - cameraY) / deltaY;
    if (t <= OCCLUSION_EPSILON || t >= 1.0f - OCCLUSION_EPSILON) {
      return false;
    }
    float hitX = lerp(cameraX, targetX, t);
    float hitZ = lerp(cameraZ, targetZ, t);
    return hitX >= occluder.minLocalX()
        && hitX <= occluder.maxLocalX()
        && hitZ >= occluder.minLocalY()
        && hitZ <= occluder.maxLocalY();
  }

  private static float[] cameraPosition(WorldCameraState cameraState) {
    float focusX = cameraState.focusX();
    float focusY = cameraState.focusY();
    float focusHeight = cameraState.focusHeight();
    float pitchRadians = (float) Math.toRadians(cameraState.pitchDegrees());
    float yawRadians = (float) Math.toRadians(-cameraState.yawDegrees());
    float offsetY = -cameraState.screenOffsetY();
    float offsetZ = cameraState.distance();

    float rotatedY = (float) (offsetY * Math.cos(pitchRadians) + offsetZ * Math.sin(pitchRadians));
    float rotatedZ = (float) (-offsetY * Math.sin(pitchRadians) + offsetZ * Math.cos(pitchRadians));
    float rotatedX = (float) (-rotatedZ * Math.sin(yawRadians));
    float worldZ = (float) (rotatedZ * Math.cos(yawRadians));
    // Mirror of the glScalef(1, 1, -1) the renderer applies between R_yaw and T_focus. Without
    // this, the recovered camera world position would be on the wrong side of the focus along
    // north/south and the occluder ray would cull tiles incorrectly.
    return new float[]{focusX + rotatedX, focusHeight + rotatedY, focusY - worldZ};
  }

  private static float lerp(float start, float end, float t) {
    return start + (end - start) * t;
  }

  private static boolean sameSide(float a, float b) {
    return (a > 0.0f && b > 0.0f) || (a < 0.0f && b < 0.0f);
  }
}
