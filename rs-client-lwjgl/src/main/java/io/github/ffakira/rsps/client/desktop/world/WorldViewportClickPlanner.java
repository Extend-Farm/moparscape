package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.core.ScreenRect;

public final class WorldViewportClickPlanner {

  private static final float MAX_RAY_DISTANCE = WorldViewportProjection.FAR_PLANE;
  private static final float RAY_STEP = 0.12f;
  private static final float SURFACE_EPSILON = 0.05f;
  private static final int HIT_REFINEMENT_ITERATIONS = 10;

  public WorldViewportClickTarget pickTile(
      WorldScene worldScene,
      WorldCameraState cameraState,
      ScreenRect viewportRect,
      double screenX,
      double screenY
  ) {
    if (!viewportRect.contains(screenX, screenY)) {
      return null;
    }

    float viewportWidth = Math.max(1.0f, viewportRect.width());
    float viewportHeight = Math.max(1.0f, viewportRect.height());
    float ndcX = (float) (((screenX - viewportRect.left()) / viewportWidth) * 2.0 - 1.0);
    float ndcY = (float) (1.0 - ((screenY - viewportRect.top()) / viewportHeight) * 2.0);
    float aspectRatio = viewportWidth / viewportHeight;
    float frustumTop = WorldViewportProjection.frustumTop();
    float frustumRight = WorldViewportProjection.frustumRight(aspectRatio);

    float[] viewRay = normalize(ndcX * frustumRight, ndcY * frustumTop, -WorldViewportProjection.NEAR_PLANE);
    float[] worldOrigin = cameraOrigin(cameraState);
    float[] worldDirection = viewToWorldDirection(viewRay, cameraState);
    return marchToTerrain(worldScene, worldOrigin, worldDirection);
  }

  private WorldViewportClickTarget marchToTerrain(WorldScene worldScene, float[] worldOrigin, float[] worldDirection) {
    boolean enteredSceneBounds = false;
    float previousDistance = 0.0f;
    for (float distance = 0.0f; distance <= MAX_RAY_DISTANCE; distance += RAY_STEP) {
      float worldX = worldOrigin[0] + worldDirection[0] * distance;
      float worldY = worldOrigin[1] + worldDirection[1] * distance;
      float worldZ = worldOrigin[2] + worldDirection[2] * distance;
      if (!isInsideWalkableTerrain(worldScene, worldX, worldZ)) {
        if (enteredSceneBounds) {
          break;
        }
        continue;
      }
      enteredSceneBounds = true;
      float terrainHeight = sampleTerrainHeight(worldScene, worldX, worldZ);
      if (worldY <= terrainHeight + SURFACE_EPSILON) {
        return refineHit(worldScene, worldOrigin, worldDirection, previousDistance, distance);
      }
      previousDistance = distance;
    }
    return null;
  }

  private WorldViewportClickTarget refineHit(
      WorldScene worldScene,
      float[] worldOrigin,
      float[] worldDirection,
      float minDistance,
      float maxDistance
  ) {
    float lowerBound = minDistance;
    float upperBound = maxDistance;
    for (int iteration = 0; iteration < HIT_REFINEMENT_ITERATIONS; iteration++) {
      float midpoint = (lowerBound + upperBound) * 0.5f;
      float worldX = worldOrigin[0] + worldDirection[0] * midpoint;
      float worldY = worldOrigin[1] + worldDirection[1] * midpoint;
      float worldZ = worldOrigin[2] + worldDirection[2] * midpoint;
      float terrainHeight = sampleTerrainHeight(worldScene, worldX, worldZ);
      if (worldY <= terrainHeight + SURFACE_EPSILON) {
        upperBound = midpoint;
      } else {
        lowerBound = midpoint;
      }
    }
    float hitX = worldOrigin[0] + worldDirection[0] * upperBound;
    float hitZ = worldOrigin[2] + worldDirection[2] * upperBound;
    int localX = clamp((int) Math.floor(hitX), 0, worldScene.tileWidth() - 2);
    int localY = clamp((int) Math.floor(hitZ), 0, worldScene.tileHeight() - 2);
    return new WorldViewportClickTarget(localX, localY);
  }

  private float sampleTerrainHeight(WorldScene worldScene, float worldX, float worldZ) {
    int localX = clamp((int) Math.floor(worldX), 0, worldScene.tileWidth() - 2);
    int localY = clamp((int) Math.floor(worldZ), 0, worldScene.tileHeight() - 2);
    float offsetX = clamp(worldX - localX, 0.0f, 1.0f);
    float offsetZ = clamp(worldZ - localY, 0.0f, 1.0f);
    float northWest = worldScene.elevationAt(localX, localY) * WorldSceneScale.HEIGHT_SCALE;
    float northEast = worldScene.elevationAt(localX + 1, localY) * WorldSceneScale.HEIGHT_SCALE;
    float southEast = worldScene.elevationAt(localX + 1, localY + 1) * WorldSceneScale.HEIGHT_SCALE;
    float southWest = worldScene.elevationAt(localX, localY + 1) * WorldSceneScale.HEIGHT_SCALE;
    float northBlend = northWest + (northEast - northWest) * offsetX;
    float southBlend = southWest + (southEast - southWest) * offsetX;
    return northBlend + (southBlend - northBlend) * offsetZ;
  }

  private boolean isInsideWalkableTerrain(WorldScene worldScene, float worldX, float worldZ) {
    return worldX >= 0.0f
        && worldZ >= 0.0f
        && worldX < worldScene.tileWidth() - 1.0f
        && worldZ < worldScene.tileHeight() - 1.0f;
  }

  private float[] cameraOrigin(WorldCameraState cameraState) {
    float[] translatedOrigin = new float[]{0.0f, -cameraState.screenOffsetY(), cameraState.distance()};
    float[] pitchAdjusted = rotateAroundX(translatedOrigin, -cameraState.pitchDegrees());
    float[] yawAdjusted = rotateAroundY(pitchAdjusted, cameraState.yawDegrees());
    yawAdjusted[0] += cameraState.focusX();
    yawAdjusted[1] += cameraState.focusHeight();
    yawAdjusted[2] += cameraState.focusY();
    return yawAdjusted;
  }

  private float[] viewToWorldDirection(float[] viewDirection, WorldCameraState cameraState) {
    float[] pitchAdjusted = rotateAroundX(viewDirection, -cameraState.pitchDegrees());
    return normalize(rotateAroundY(pitchAdjusted, cameraState.yawDegrees()));
  }

  private float[] rotateAroundX(float[] vector, float degrees) {
    float radians = (float) Math.toRadians(degrees);
    float cosine = (float) Math.cos(radians);
    float sine = (float) Math.sin(radians);
    return new float[]{
        vector[0],
        vector[1] * cosine - vector[2] * sine,
        vector[1] * sine + vector[2] * cosine
    };
  }

  private float[] rotateAroundY(float[] vector, float degrees) {
    float radians = (float) Math.toRadians(degrees);
    float cosine = (float) Math.cos(radians);
    float sine = (float) Math.sin(radians);
    return new float[]{
        vector[0] * cosine + vector[2] * sine,
        vector[1],
        -vector[0] * sine + vector[2] * cosine
    };
  }

  private float[] normalize(float x, float y, float z) {
    float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
    if (magnitude <= 0.0001f) {
      return new float[]{0.0f, 0.0f, -1.0f};
    }
    return new float[]{x / magnitude, y / magnitude, z / magnitude};
  }

  private float[] normalize(float[] vector) {
    return normalize(vector[0], vector[1], vector[2]);
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  public record WorldViewportClickTarget(int localX, int localY) {
  }
}
