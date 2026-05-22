package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.character.ActorAnimationState;
import io.github.ffakira.rsps.client.desktop.character.CharacterModelAssembler;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueueBuilder;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneSubmissionKind;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMesh;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionContext;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionPlanner;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityWindow;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

final class WorldSceneActorBatchBuilder {

  private final CharacterModelAssembler characterModelAssembler;

  WorldSceneActorBatchBuilder(CharacterModelAssembler characterModelAssembler) {
    this.characterModelAssembler = characterModelAssembler;
  }

  void addBatches(
      SceneRenderQueueBuilder renderQueueBuilder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      WorldSceneOcclusionContext occlusionContext,
      WorldCameraState cameraState,
      float actorLocalX,
      float actorLocalY,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState actorAnimationState
  ) {
    if (actorLocalX < 0.0f || actorLocalY < 0.0f || actorLocalX >= worldScene.tileWidth() || actorLocalY >= worldScene.tileHeight()) {
      return;
    }
    int actorTileX = clampTile((int) Math.floor(actorLocalX), worldScene.tileWidth());
    int actorTileY = clampTile((int) Math.floor(actorLocalY), worldScene.tileHeight());
    if (!visibilityWindow.containsTile(actorTileX, actorTileY)) {
      return;
    }
    float baseHeight = sampleTerrainHeight(worldScene, actorLocalX, actorLocalY);
    if (WorldSceneOcclusionPlanner.isOccluded(
        occlusionContext,
        actorLocalX,
        baseHeight + 1.1f,
        actorLocalY
    )) {
      return;
    }
    if (characterModelAssembler != null) {
      WorldSceneObjectGeometry geometry = characterModelAssembler.assemble(appearance, equipment, actorAnimationState);
      if (geometry != null) {
        float actorYawDegrees = actorYawDegrees(actorAnimationState);
        SceneTriangleMeshBuilder actorBuilder = new SceneTriangleMeshBuilder();
        // The player model has to stay as one ordered face stream. Splitting it across raster-mode
        // buckets changes cross-face order and can bury worn items like the amulet behind torso
        // faces that submit later.
        actorBuilder.addGeometry(geometry, actorLocalX, baseHeight, actorLocalY, actorYawDegrees, null);
        renderQueueBuilder.add(
            SceneSubmissionKind.ACTOR,
            SceneRasterMode.GOURAUD,
            sortActorMeshForSubmission(actorBuilder.build(), cameraState)
        );
        return;
      }
    }
    renderQueueBuilder.add(
        SceneSubmissionKind.ACTOR,
        SceneRasterMode.GOURAUD,
        sortActorMeshForSubmission(buildFallbackActorMesh(actorLocalX, actorLocalY, baseHeight, appearance, equipment), cameraState)
    );
  }

  static float renderedLocalAxis(int rawTile, float positionOffset, int sceneSize) {
    return clamp(rawTile + 0.5f + positionOffset, 0.5f, sceneSize - 0.5f);
  }

  static float actorYawDegrees(ActorAnimationState actorAnimationState) {
    if (actorAnimationState == null) {
      return 180.0f;
    }
    // The assembled player model's authored forward axis is opposite the world-space heading
    // convention used by movement deltas. Apply the 317-style forward correction here so the
    // actor faces and walks into the path instead of moonwalking backward.
    return normalizeDegrees(actorAnimationState.headingDegrees() + 180.0f);
  }

  static SceneTriangleMesh sortActorMeshForSubmission(SceneTriangleMesh mesh, WorldCameraState cameraState) {
    if (mesh == null || mesh.isEmpty() || cameraState == null) {
      return mesh;
    }
    ActorViewProjection projection = ActorViewProjection.project(mesh, cameraState);
    @SuppressWarnings("unchecked")
    List<FaceDepth>[] facesByPriority = new List[12];
    for (int priority = 0; priority < facesByPriority.length; priority++) {
      facesByPriority[priority] = new ArrayList<>();
    }
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      if (!projection.isFrontFacing(mesh, faceIndex)) {
        continue;
      }
      facesByPriority[normalizedPriority(mesh.facePriorities()[faceIndex])]
          .add(new FaceDepth(faceIndex, projection.faceAverageDepth(mesh, faceIndex)));
    }
    for (List<FaceDepth> faces : facesByPriority) {
      faces.sort(
          Comparator.comparingDouble(FaceDepth::depth).reversed()
              .thenComparingInt(FaceDepth::faceIndex)
      );
    }
    float priorityOneTwoMeanDepth = averageDepth(facesByPriority[1], facesByPriority[2]);
    float priorityThreeFourMeanDepth = averageDepth(facesByPriority[3], facesByPriority[4]);
    float prioritySixEightMeanDepth = averageDepth(facesByPriority[6], facesByPriority[8]);
    SpecialPriorityCursor specialPriorityCursor = new SpecialPriorityCursor(
        facesByPriority[10],
        facesByPriority[11]
    );
    int[] faceOrder = new int[mesh.faceVertexA().length];
    int orderedFaceCount = 0;
    for (int priority = 0; priority < 10; priority++) {
      if (priority == 0) {
        orderedFaceCount = appendSpecialFacesAboveDepth(
            specialPriorityCursor,
            faceOrder,
            orderedFaceCount,
            priorityOneTwoMeanDepth
        );
      } else if (priority == 3) {
        orderedFaceCount = appendSpecialFacesAboveDepth(
            specialPriorityCursor,
            faceOrder,
            orderedFaceCount,
            priorityThreeFourMeanDepth
        );
      } else if (priority == 5) {
        orderedFaceCount = appendSpecialFacesAboveDepth(
            specialPriorityCursor,
            faceOrder,
            orderedFaceCount,
            prioritySixEightMeanDepth
        );
      }
      orderedFaceCount = appendFaces(
          facesByPriority[priority],
          faceOrder,
          orderedFaceCount
      );
    }
    orderedFaceCount = appendSpecialFacesAboveDepth(
        specialPriorityCursor,
        faceOrder,
        orderedFaceCount,
        Float.NEGATIVE_INFINITY
    );
    if (orderedFaceCount != faceOrder.length) {
      faceOrder = Arrays.copyOf(faceOrder, orderedFaceCount);
    }
    return reorderFaces(mesh, faceOrder);
  }

  private SceneTriangleMesh buildFallbackActorMesh(
      float playerLocalX,
      float playerLocalY,
      float baseHeight,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment
  ) {
    int appearanceSeed = 0;
    if (appearance != null) {
      for (Integer lookValue : appearance.lookValues()) {
        appearanceSeed = appearanceSeed * 31 + (lookValue == null ? -1 : lookValue);
      }
    }
    int equipmentSeed = 0;
    for (BootstrapItemSlot equipmentSlot : equipment) {
      equipmentSeed = equipmentSeed * 17 + equipmentSlot.itemId();
    }

    int bodyRgb = hashedColor(appearanceSeed, 0x6b6f7c, 0xc2c6d2);
    int accentRgb = hashedColor(equipmentSeed == 0 ? appearanceSeed + 7 : equipmentSeed, 0x8b7342, 0xe2d39c);
    float centerX = playerLocalX;
    float centerZ = playerLocalY;

    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    appendCuboid(builder, centerX - 0.16f, centerX - 0.05f, centerZ - 0.05f, centerZ + 0.06f, baseHeight, baseHeight + 0.82f, bodyRgb);
    appendCuboid(builder, centerX + 0.05f, centerX + 0.16f, centerZ - 0.05f, centerZ + 0.06f, baseHeight, baseHeight + 0.82f, bodyRgb);
    appendCuboid(builder, centerX - 0.19f, centerX - 0.11f, centerZ - 0.03f, centerZ + 0.05f, baseHeight + 0.68f, baseHeight + 1.35f, accentRgb);
    appendCuboid(builder, centerX + 0.11f, centerX + 0.19f, centerZ - 0.03f, centerZ + 0.05f, baseHeight + 0.68f, baseHeight + 1.35f, accentRgb);
    appendCuboid(builder, centerX - 0.14f, centerX + 0.14f, centerZ - 0.12f, centerZ + 0.12f, baseHeight + 0.74f, baseHeight + 1.42f, bodyRgb);
    appendCuboid(builder, centerX - 0.09f, centerX + 0.09f, centerZ - 0.09f, centerZ + 0.09f, baseHeight + 1.42f, baseHeight + 1.76f, accentRgb);
    if (equipment.stream().anyMatch(slot -> slot.slotIndex() == 3)) {
      appendCuboid(builder, centerX + 0.17f, centerX + 0.23f, centerZ - 0.02f, centerZ + 0.04f, baseHeight + 0.62f, baseHeight + 1.52f, 0x9f8350);
    }
    return builder.build();
  }

  private void appendCuboid(
      SceneTriangleMeshBuilder builder,
      float minX,
      float maxX,
      float minZ,
      float maxZ,
      float baseHeight,
      float topHeight,
      int rgb
  ) {
    int topRgb = rgb;
    int northRgb = shade(rgb, 0.84f);
    int eastRgb = shade(rgb, 0.76f);
    int southRgb = shade(rgb, 0.70f);
    int westRgb = shade(rgb, 0.78f);

    builder.addQuad(minX, topHeight, minZ, maxX, topHeight, minZ, maxX, topHeight, maxZ, minX, topHeight, maxZ, topRgb);
    builder.addQuad(minX, baseHeight, minZ, maxX, baseHeight, minZ, maxX, topHeight, minZ, minX, topHeight, minZ, northRgb);
    builder.addQuad(maxX, baseHeight, minZ, maxX, baseHeight, maxZ, maxX, topHeight, maxZ, maxX, topHeight, minZ, eastRgb);
    builder.addQuad(maxX, baseHeight, maxZ, minX, baseHeight, maxZ, minX, topHeight, maxZ, maxX, topHeight, maxZ, southRgb);
    builder.addQuad(minX, baseHeight, maxZ, minX, baseHeight, minZ, minX, topHeight, minZ, minX, topHeight, maxZ, westRgb);
  }

  private int shade(int rgb, float factor) {
    int red = Math.max(0, Math.min(255, Math.round(((rgb >>> 16) & 0xff) * factor)));
    int green = Math.max(0, Math.min(255, Math.round(((rgb >>> 8) & 0xff) * factor)));
    int blue = Math.max(0, Math.min(255, Math.round((rgb & 0xff) * factor)));
    return (red << 16) | (green << 8) | blue;
  }

  private int hashedColor(int seed, int darkRgb, int lightRgb) {
    int mixed = Integer.rotateLeft(seed * 0x45d9f3b, 11);
    float blend = ((mixed >>> 16) & 0xff) / 255.0f;
    int darkRed = (darkRgb >>> 16) & 0xff;
    int darkGreen = (darkRgb >>> 8) & 0xff;
    int darkBlue = darkRgb & 0xff;
    int lightRed = (lightRgb >>> 16) & 0xff;
    int lightGreen = (lightRgb >>> 8) & 0xff;
    int lightBlue = lightRgb & 0xff;
    int red = (int) (darkRed + (lightRed - darkRed) * blend);
    int green = (int) (darkGreen + (lightGreen - darkGreen) * blend);
    int blue = (int) (darkBlue + (lightBlue - darkBlue) * blend);
    return (red << 16) | (green << 8) | blue;
  }

  private static int appendFaces(
      List<FaceDepth> faces,
      int[] faceOrder,
      int orderedFaceCount
  ) {
    for (FaceDepth face : faces) {
      faceOrder[orderedFaceCount++] = face.faceIndex();
    }
    return orderedFaceCount;
  }

  private static int appendSpecialFacesAboveDepth(
      SpecialPriorityCursor cursor,
      int[] faceOrder,
      int orderedFaceCount,
      float thresholdDepth
  ) {
    while (cursor.currentDepth() > thresholdDepth) {
      faceOrder[orderedFaceCount++] = cursor.consumeFaceIndex();
    }
    return orderedFaceCount;
  }

  private static float averageDepth(List<FaceDepth> firstBucket, List<FaceDepth> secondBucket) {
    float depthSum = 0.0f;
    int faceCount = 0;
    for (FaceDepth face : firstBucket) {
      depthSum += face.depth();
      faceCount++;
    }
    for (FaceDepth face : secondBucket) {
      depthSum += face.depth();
      faceCount++;
    }
    return faceCount == 0 ? 0.0f : depthSum / faceCount;
  }

  private static int normalizedPriority(int priority) {
    return Math.max(0, Math.min(11, priority));
  }

  private static SceneTriangleMesh reorderFaces(SceneTriangleMesh mesh, int[] faceOrder) {
    int[] sortedFaceVertexA = new int[faceOrder.length];
    int[] sortedFaceVertexB = new int[faceOrder.length];
    int[] sortedFaceVertexC = new int[faceOrder.length];
    int[] sortedFaceColorA = new int[faceOrder.length];
    int[] sortedFaceColorB = new int[faceOrder.length];
    int[] sortedFaceColorC = new int[faceOrder.length];
    int[] sortedFaceAlpha = new int[faceOrder.length];
    int[] sortedFaceTextureIds = new int[faceOrder.length];
    int[] sortedTextureVertexA = new int[faceOrder.length];
    int[] sortedTextureVertexB = new int[faceOrder.length];
    int[] sortedTextureVertexC = new int[faceOrder.length];
    int[] sortedFacePriorities = new int[faceOrder.length];
    for (int sortedIndex = 0; sortedIndex < faceOrder.length; sortedIndex++) {
      int sourceFaceIndex = faceOrder[sortedIndex];
      sortedFaceVertexA[sortedIndex] = mesh.faceVertexA()[sourceFaceIndex];
      sortedFaceVertexB[sortedIndex] = mesh.faceVertexB()[sourceFaceIndex];
      sortedFaceVertexC[sortedIndex] = mesh.faceVertexC()[sourceFaceIndex];
      sortedFaceColorA[sortedIndex] = mesh.faceColorA()[sourceFaceIndex];
      sortedFaceColorB[sortedIndex] = mesh.faceColorB()[sourceFaceIndex];
      sortedFaceColorC[sortedIndex] = mesh.faceColorC()[sourceFaceIndex];
      sortedFaceAlpha[sortedIndex] = mesh.faceAlpha()[sourceFaceIndex];
      sortedFaceTextureIds[sortedIndex] = mesh.faceTextureIds()[sourceFaceIndex];
      sortedTextureVertexA[sortedIndex] = mesh.textureVertexA()[sourceFaceIndex];
      sortedTextureVertexB[sortedIndex] = mesh.textureVertexB()[sourceFaceIndex];
      sortedTextureVertexC[sortedIndex] = mesh.textureVertexC()[sourceFaceIndex];
      sortedFacePriorities[sortedIndex] = mesh.facePriorities()[sourceFaceIndex];
    }
    return new SceneTriangleMesh(
        mesh.vertexX(),
        mesh.vertexY(),
        mesh.vertexZ(),
        sortedFaceVertexA,
        sortedFaceVertexB,
        sortedFaceVertexC,
        sortedFaceColorA,
        sortedFaceColorB,
        sortedFaceColorC,
        sortedFaceAlpha,
        sortedFaceTextureIds,
        sortedTextureVertexA,
        sortedTextureVertexB,
        sortedTextureVertexC,
        sortedFacePriorities
    );
  }

  private record FaceDepth(int faceIndex, float depth) {
  }

  private static final class SpecialPriorityCursor {
    private final List<FaceDepth> priorityTenFaces;
    private final List<FaceDepth> priorityElevenFaces;
    private int activePriority;
    private int faceIndex;

    private SpecialPriorityCursor(List<FaceDepth> priorityTenFaces, List<FaceDepth> priorityElevenFaces) {
      this.priorityTenFaces = priorityTenFaces;
      this.priorityElevenFaces = priorityElevenFaces;
      this.activePriority = priorityTenFaces.isEmpty()
          ? (priorityElevenFaces.isEmpty() ? -1 : 11)
          : 10;
    }

    private float currentDepth() {
      FaceDepth currentFace = currentFace();
      return currentFace == null ? Float.NEGATIVE_INFINITY : currentFace.depth();
    }

    private int consumeFaceIndex() {
      FaceDepth currentFace = currentFace();
      int currentFaceIndex = currentFace == null ? -1 : currentFace.faceIndex();
      advance();
      return currentFaceIndex;
    }

    private FaceDepth currentFace() {
      List<FaceDepth> activeFaces = activeFaces();
      if (activeFaces == null || faceIndex >= activeFaces.size()) {
        return null;
      }
      return activeFaces.get(faceIndex);
    }

    private void advance() {
      faceIndex++;
      List<FaceDepth> activeFaces = activeFaces();
      if (activeFaces != null && faceIndex >= activeFaces.size()) {
        if (activePriority == 10 && !priorityElevenFaces.isEmpty()) {
          activePriority = 11;
          faceIndex = 0;
          return;
        }
        activePriority = -1;
        faceIndex = 0;
      }
    }

    private List<FaceDepth> activeFaces() {
      return switch (activePriority) {
        case 10 -> priorityTenFaces;
        case 11 -> priorityElevenFaces;
        default -> null;
      };
    }
  }

  private static final class ActorViewProjection {
    private final float[] depth;
    private final float[] projectedX;
    private final float[] projectedY;

    private ActorViewProjection(float[] depth, float[] projectedX, float[] projectedY) {
      this.depth = depth;
      this.projectedX = projectedX;
      this.projectedY = projectedY;
    }

    private static ActorViewProjection project(SceneTriangleMesh mesh, WorldCameraState cameraState) {
      float[] depth = new float[mesh.vertexX().length];
      float[] projectedX = new float[mesh.vertexX().length];
      float[] projectedY = new float[mesh.vertexX().length];
      float yawRadians = (float) Math.toRadians(-cameraState.yawDegrees());
      float yawCosine = (float) Math.cos(yawRadians);
      float yawSine = (float) Math.sin(yawRadians);
      float pitchRadians = (float) Math.toRadians(cameraState.pitchDegrees());
      float pitchCosine = (float) Math.cos(pitchRadians);
      float pitchSine = (float) Math.sin(pitchRadians);
      for (int vertexIndex = 0; vertexIndex < mesh.vertexX().length; vertexIndex++) {
        float localX = mesh.vertexX()[vertexIndex] - cameraState.focusX();
        float localY = mesh.vertexY()[vertexIndex] - cameraState.focusHeight();
        float localZ = -(mesh.vertexZ()[vertexIndex] - cameraState.focusY());
        float yawAdjustedX = localX * yawCosine + localZ * yawSine;
        float yawAdjustedZ = -localX * yawSine + localZ * yawCosine;
        float viewY = localY * pitchCosine - yawAdjustedZ * pitchSine + cameraState.screenOffsetY();
        float viewZ = localY * pitchSine + yawAdjustedZ * pitchCosine - cameraState.distance();
        depth[vertexIndex] = -viewZ;
        if (depth[vertexIndex] <= 0.0001f) {
          projectedX[vertexIndex] = 0.0f;
          projectedY[vertexIndex] = 0.0f;
        } else {
          projectedX[vertexIndex] = yawAdjustedX / depth[vertexIndex];
          projectedY[vertexIndex] = -viewY / depth[vertexIndex];
        }
      }
      return new ActorViewProjection(depth, projectedX, projectedY);
    }

    private float faceAverageDepth(SceneTriangleMesh mesh, int faceIndex) {
      return (depth[mesh.faceVertexA()[faceIndex]]
          + depth[mesh.faceVertexB()[faceIndex]]
          + depth[mesh.faceVertexC()[faceIndex]]) / 3.0f;
    }

    private boolean isFrontFacing(SceneTriangleMesh mesh, int faceIndex) {
      int vertexA = mesh.faceVertexA()[faceIndex];
      int vertexB = mesh.faceVertexB()[faceIndex];
      int vertexC = mesh.faceVertexC()[faceIndex];
      if (depth[vertexA] <= WorldViewportProjection.NEAR_PLANE
          || depth[vertexB] <= WorldViewportProjection.NEAR_PLANE
          || depth[vertexC] <= WorldViewportProjection.NEAR_PLANE) {
        return true;
      }
      float crossProduct =
          (projectedX[vertexA] - projectedX[vertexB]) * (projectedY[vertexC] - projectedY[vertexB])
              - (projectedY[vertexA] - projectedY[vertexB]) * (projectedX[vertexC] - projectedX[vertexB]);
      return crossProduct > 0.0f;
    }
  }

  private static float sampleTerrainHeight(WorldScene worldScene, float localX, float localY) {
    int tileX = clampTile((int) Math.floor(localX), worldScene.tileWidth());
    int tileY = clampTile((int) Math.floor(localY), worldScene.tileHeight());
    int eastTileX = Math.min(worldScene.tileWidth() - 1, tileX + 1);
    int southTileY = Math.min(worldScene.tileHeight() - 1, tileY + 1);
    float offsetX = clamp(localX - tileX, 0.0f, 1.0f);
    float offsetY = clamp(localY - tileY, 0.0f, 1.0f);
    float northWest = worldScene.elevationAt(tileX, tileY) * WorldSceneScale.HEIGHT_SCALE;
    float northEast = worldScene.elevationAt(eastTileX, tileY) * WorldSceneScale.HEIGHT_SCALE;
    float southEast = worldScene.elevationAt(eastTileX, southTileY) * WorldSceneScale.HEIGHT_SCALE;
    float southWest = worldScene.elevationAt(tileX, southTileY) * WorldSceneScale.HEIGHT_SCALE;
    float northBlend = northWest + (northEast - northWest) * offsetX;
    float southBlend = southWest + (southEast - southWest) * offsetX;
    return northBlend + (southBlend - northBlend) * offsetY;
  }

  private static int clampTile(int value, int tileBound) {
    return Math.max(0, Math.min(tileBound - 1, value));
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
