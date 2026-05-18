package io.github.ffakira.rsps.client.lwjgl;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.List;

final class WorldSceneSubmissionBuilder {

  private static final float WORLD_HEIGHT_SCALE = 0.18f;

  private final CharacterModelAssembler characterModelAssembler;
  private final TerrainSceneMeshBuilder terrainSceneMeshBuilder;

  WorldSceneSubmissionBuilder(CharacterModelAssembler characterModelAssembler) {
    this.characterModelAssembler = characterModelAssembler;
    this.terrainSceneMeshBuilder = new TerrainSceneMeshBuilder();
  }

  // This is the first native scene-submission boundary for the rewrite. It does not attempt full
  // legacy raster parity yet, but it does turn terrain, static objects, and the local player into
  // one coherent queued submission payload instead of rendering them through disconnected helper
  // passes. The queue keeps primitive ownership explicit so the raster layer can grow without
  // pushing tile/object/actor loops back into the viewport renderer.
  WorldSceneRenderSubmission build(
      WorldScene worldScene,
      WorldPoint worldPoint,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      int viewportWidth,
      int viewportHeight
  ) {
    int playerLocalX = worldPoint.x() - worldScene.originWorldX();
    int playerLocalY = worldPoint.y() - worldScene.originWorldY();
    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        worldScene,
        playerLocalX,
        playerLocalY,
        WORLD_HEIGHT_SCALE,
        viewportWidth,
        viewportHeight
    );
    WorldSceneVisibilityWindow visibilityWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        cameraState,
        playerLocalX,
        playerLocalY,
        viewportWidth,
        viewportHeight
    );
    WorldSceneOcclusionContext occlusionContext = WorldSceneOcclusionPlanner.plan(
        worldScene,
        visibilityWindow,
        cameraState
    );
    // The native client still lacks legacy SceneGraph visibility traversal, so submission owns the
    // first coarse camera-centered culling boundary instead of pushing whole-region meshes into
    // the raster layer.
    SceneRenderQueueBuilder renderQueueBuilder = new SceneRenderQueueBuilder();
    // Terrain now stays on the full stitched scene window for correctness. The earlier
    // camera-centered terrain cull was creating obvious black voids because the native client
    // still lacks the legacy scene graph's visible-tile traversal. Objects and actors remain on
    // the narrower submission window so the rewrite does not regress straight back to uncapped
    // full-scene submission everywhere.
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_PAINT,
        SceneRasterMode.GOURAUD,
        terrainScene_meshBuilder().buildTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_PAINT,
        SceneRasterMode.TEXTURED,
        terrainScene_meshBuilder().buildTexturedTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.GOURAUD,
        terrainScene_meshBuilder().buildTileModelMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.TEXTURED,
        terrainScene_meshBuilder().buildTexturedTileModelMesh(worldScene)
    );
    addObjectBatches(renderQueueBuilder, worldScene, visibilityWindow, occlusionContext);
    addActorBatches(renderQueueBuilder, worldScene, visibilityWindow, occlusionContext, playerLocalX, playerLocalY, appearance, equipment);
    return new WorldSceneRenderSubmission(cameraState, renderQueueBuilder.build());
  }

  private TerrainSceneMeshBuilder terrainScene_meshBuilder() {
    return terrainSceneMeshBuilder;
  }

  private void addObjectBatches(
      SceneRenderQueueBuilder renderQueueBuilder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      WorldSceneOcclusionContext occlusionContext
  ) {
    SceneTriangleMeshBuilder flatBuilder = new SceneTriangleMeshBuilder();
    SceneTriangleMeshBuilder gouraudBuilder = new SceneTriangleMeshBuilder();
    SceneTriangleMeshBuilder texturedBuilder = new SceneTriangleMeshBuilder();
    for (WorldSceneObject worldSceneObject : worldScene.objects()) {
      float maxX = worldSceneObject.localX() + Math.max(1, worldSceneObject.sizeX());
      float maxY = worldSceneObject.localY() + Math.max(1, worldSceneObject.sizeY());
      if (!visibilityWindow.intersectsArea(worldSceneObject.localX(), worldSceneObject.localY(), maxX, maxY)) {
        continue;
      }
      float baseHeight = sampleBaseHeight(
          worldScene,
          worldSceneObject.localX(),
          worldSceneObject.localY(),
          maxX,
          maxY
      );
      if (WorldSceneOcclusionPlanner.isOccluded(
          occlusionContext,
          (worldSceneObject.localX() + maxX) * 0.5f,
          occlusionTargetHeight(worldSceneObject, baseHeight),
          (worldSceneObject.localY() + maxY) * 0.5f
      )) {
        continue;
      }
      if (worldSceneObject.geometry() != null) {
        flatBuilder.addGeometry(worldSceneObject.geometry(), worldSceneObject.localX(), baseHeight, worldSceneObject.localY(), SceneRasterMode.FLAT);
        gouraudBuilder.addGeometry(worldSceneObject.geometry(), worldSceneObject.localX(), baseHeight, worldSceneObject.localY(), SceneRasterMode.GOURAUD);
        texturedBuilder.addGeometry(worldSceneObject.geometry(), worldSceneObject.localX(), baseHeight, worldSceneObject.localY(), SceneRasterMode.TEXTURED);
        continue;
      }
      appendFallbackObject(flatBuilder, worldScene, worldSceneObject);
    }
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT, flatBuilder.build());
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD, gouraudBuilder.build());
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.TEXTURED, texturedBuilder.build());
  }

  private void addActorBatches(
      SceneRenderQueueBuilder renderQueueBuilder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      WorldSceneOcclusionContext occlusionContext,
      int playerLocalX,
      int playerLocalY,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment
  ) {
    if (playerLocalX < 0 || playerLocalY < 0 || playerLocalX >= worldScene.tileWidth() || playerLocalY >= worldScene.tileHeight()) {
      return;
    }
    if (!visibilityWindow.containsTile(playerLocalX, playerLocalY)) {
      return;
    }
    float baseHeight = worldScene.elevationAt(playerLocalX, playerLocalY) * WORLD_HEIGHT_SCALE;
    if (WorldSceneOcclusionPlanner.isOccluded(
        occlusionContext,
        playerLocalX + 0.5f,
        baseHeight + 1.1f,
        playerLocalY + 0.5f
    )) {
      return;
    }
    if (characterModelAssembler != null) {
      WorldSceneObjectGeometry geometry = characterModelAssembler.assemble(appearance, equipment);
      if (geometry != null) {
        float actorX = playerLocalX + 0.5f;
        float actorZ = playerLocalY + 0.5f;
        SceneTriangleMeshBuilder flatBuilder = new SceneTriangleMeshBuilder();
        SceneTriangleMeshBuilder gouraudBuilder = new SceneTriangleMeshBuilder();
        SceneTriangleMeshBuilder texturedBuilder = new SceneTriangleMeshBuilder();
        flatBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, SceneRasterMode.FLAT);
        gouraudBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, SceneRasterMode.GOURAUD);
        texturedBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, SceneRasterMode.TEXTURED);
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT, flatBuilder.build());
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD, gouraudBuilder.build());
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.TEXTURED, texturedBuilder.build());
        return;
      }
    }
    renderQueueBuilder.add(
        SceneSubmissionKind.ACTOR,
        SceneRasterMode.FLAT,
        buildFallbackActorMesh(playerLocalX, playerLocalY, baseHeight, appearance, equipment)
    );
  }

  private void appendFallbackObject(SceneTriangleMeshBuilder builder, WorldScene worldScene, WorldSceneObject worldSceneObject) {
    float minX = worldSceneObject.localX();
    float minZ = worldSceneObject.localY();
    float maxX = minX + Math.max(0.25f, worldSceneObject.sizeX());
    float maxZ = minZ + Math.max(0.25f, worldSceneObject.sizeY());
    float baseHeight = sampleBaseHeight(worldScene, minX, minZ, maxX, maxZ);
    float objectHeight = fallbackObjectHeight(worldSceneObject);
    float topHeight = baseHeight + objectHeight;

    float widthAdjustment = worldSceneObject.type() <= 3 ? 0.18f : 0.0f;
    if (worldSceneObject.type() <= 3) {
      if ((worldSceneObject.orientation() & 1) == 0) {
        minX += widthAdjustment;
        maxX -= widthAdjustment;
      } else {
        minZ += widthAdjustment;
        maxZ -= widthAdjustment;
      }
    }

    appendCuboid(builder, minX, maxX, minZ, maxZ, baseHeight, topHeight, fallbackObjectColor(worldSceneObject));
  }

  private SceneTriangleMesh buildFallbackActorMesh(
      int playerLocalX,
      int playerLocalY,
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
    float centerX = playerLocalX + 0.5f;
    float centerZ = playerLocalY + 0.5f;

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

  private float sampleBaseHeight(WorldScene worldScene, float minX, float minZ, float maxX, float maxZ) {
    int startX = clampTile((int) Math.floor(minX), worldScene.tileWidth());
    int startY = clampTile((int) Math.floor(minZ), worldScene.tileHeight());
    int endX = clampTile((int) Math.floor(maxX), worldScene.tileWidth());
    int endY = clampTile((int) Math.floor(maxZ), worldScene.tileHeight());
    float total = 0.0f;
    int samples = 0;
    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        total += worldScene.elevationAt(x, y) * WORLD_HEIGHT_SCALE;
        samples++;
      }
    }
    return samples == 0 ? 0.0f : total / samples;
  }

  private int fallbackObjectHeight(WorldSceneObject worldSceneObject) {
    return switch (worldSceneObject.type()) {
      case 0, 1, 2, 3 -> 2;
      case 22 -> 0;
      case 4, 5, 6, 7, 8 -> 1;
      default -> 1;
    };
  }

  private float occlusionTargetHeight(WorldSceneObject worldSceneObject, float baseHeight) {
    if (worldSceneObject.geometry() == null || worldSceneObject.geometry().vertexY().length == 0) {
      return baseHeight + fallbackObjectHeight(worldSceneObject) * 0.55f;
    }
    float minY = Float.POSITIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;
    for (float vertexY : worldSceneObject.geometry().vertexY()) {
      minY = Math.min(minY, vertexY);
      maxY = Math.max(maxY, vertexY);
    }
    return baseHeight + minY + (maxY - minY) * 0.58f;
  }

  private int fallbackObjectColor(WorldSceneObject worldSceneObject) {
    String lowercaseName = worldSceneObject.name().toLowerCase();
    if (lowercaseName.contains("tree") || lowercaseName.contains("bush")) {
      return 0x557c39;
    }
    if (lowercaseName.contains("rock") || lowercaseName.contains("stone") || lowercaseName.contains("altar")) {
      return 0x7b7b77;
    }
    if (lowercaseName.contains("fence") || lowercaseName.contains("gate") || lowercaseName.contains("door")) {
      return 0x7b5b33;
    }
    if (lowercaseName.contains("wall")) {
      return 0x6f665d;
    }
    return hashedColor(worldSceneObject.objectId(), 0x5e4f39, 0xa88d63);
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

  private int clampTile(int value, int tileBound) {
    return Math.max(0, Math.min(tileBound - 1, value));
  }
}
