package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.character.ActorAnimationState;
import io.github.ffakira.rsps.client.desktop.character.CharacterModelAssembler;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueueBuilder;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneSubmissionKind;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMesh;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainSceneMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionContext;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionPlanner;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityPlanner;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityWindow;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.List;

public final class WorldSceneSubmissionBuilder {

  private final CharacterModelAssembler characterModelAssembler;
  private final TerrainSceneMeshBuilder terrainSceneMeshBuilder;

  public WorldSceneSubmissionBuilder(CharacterModelAssembler characterModelAssembler) {
    this.characterModelAssembler = characterModelAssembler;
    this.terrainSceneMeshBuilder = new TerrainSceneMeshBuilder();
  }

  // This is the first native scene-submission boundary for the rewrite. It does not attempt full
  // legacy raster parity yet, but it does turn terrain, static objects, and the local player into
  // one coherent queued submission payload instead of rendering them through disconnected helper
  // passes. The queue keeps primitive ownership explicit so the raster layer can grow without
  // pushing tile/object/actor loops back into the viewport renderer.
  public WorldSceneRenderSubmission build(
      WorldScene worldScene,
      WorldPoint worldPoint,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState actorAnimationState,
      int viewportWidth,
      int viewportHeight
  ) {
    return build(
        worldScene,
        worldPoint,
        appearance,
        equipment,
        actorAnimationState,
        viewportWidth,
        viewportHeight,
        0.0f,
        0.0f
    );
  }

  public WorldSceneRenderSubmission build(
      WorldScene worldScene,
      WorldPoint worldPoint,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState actorAnimationState,
      int viewportWidth,
      int viewportHeight,
      float cameraYawOffsetDegrees,
      float cameraPitchOffsetDegrees
  ) {
    int playerLocalX = worldPoint.x() - worldScene.originWorldX();
    int playerLocalY = worldPoint.y() - worldScene.originWorldY();
    float actorLocalX = renderedLocalAxis(
        playerLocalX,
        actorAnimationState == null ? 0.0f : actorAnimationState.positionOffsetX(),
        worldScene.tileWidth()
    );
    float actorLocalY = renderedLocalAxis(
        playerLocalY,
        actorAnimationState == null ? 0.0f : actorAnimationState.positionOffsetY(),
        worldScene.tileHeight()
    );
    int focusTileX = clampTile((int) Math.floor(actorLocalX), worldScene.tileWidth());
    int focusTileY = clampTile((int) Math.floor(actorLocalY), worldScene.tileHeight());
    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        worldScene,
        actorLocalX,
        actorLocalY,
        actorAnimationState,
        WorldSceneScale.HEIGHT_SCALE,
        viewportWidth,
        viewportHeight,
        cameraYawOffsetDegrees,
        cameraPitchOffsetDegrees
    );
    WorldSceneVisibilityWindow visibilityWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        cameraState,
        focusTileX,
        focusTileY,
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
        terrainSceneMeshBuilder.buildTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_PAINT,
        SceneRasterMode.TEXTURED,
        terrainSceneMeshBuilder.buildTexturedTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.GOURAUD,
        terrainSceneMeshBuilder.buildTileModelMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.TEXTURED,
        terrainSceneMeshBuilder.buildTexturedTileModelMesh(worldScene)
    );
    addObjectBatches(renderQueueBuilder, worldScene, visibilityWindow, occlusionContext);
    addActorBatches(
        renderQueueBuilder,
        worldScene,
        visibilityWindow,
        occlusionContext,
        actorLocalX,
        actorLocalY,
        appearance,
        equipment,
        actorAnimationState
    );
    return new WorldSceneRenderSubmission(cameraState, renderQueueBuilder.build());
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
      float baseHeight = sampleObjectBaseHeight(worldScene, worldSceneObject, maxX, maxY);
      if (WorldSceneOcclusionPlanner.isOccluded(
          occlusionContext,
          (worldSceneObject.localX() + maxX) * 0.5f,
          occlusionTargetHeight(worldSceneObject, baseHeight),
          (worldSceneObject.localY() + maxY) * 0.5f
      )) {
        continue;
      }
      if (worldSceneObject.geometry() != null) {
        WorldSceneObjectGeometry geometry = terrainContouredGeometry(worldScene, worldSceneObject, baseHeight);
        float objectCenterX = worldSceneObject.centerX();
        float objectCenterZ = worldSceneObject.centerY();
        flatBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.FLAT);
        gouraudBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.GOURAUD);
        texturedBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.TEXTURED);
        continue;
      }
      if (worldSceneObject.allowFallbackProxy()) {
        appendFallbackObject(flatBuilder, worldScene, worldSceneObject);
      }
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
        float actorX = actorLocalX;
        float actorZ = actorLocalY;
        float actorYawDegrees = actorYawDegrees(actorAnimationState);
        SceneTriangleMeshBuilder flatBuilder = new SceneTriangleMeshBuilder();
        SceneTriangleMeshBuilder gouraudBuilder = new SceneTriangleMeshBuilder();
        SceneTriangleMeshBuilder texturedBuilder = new SceneTriangleMeshBuilder();
        flatBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, actorYawDegrees, SceneRasterMode.FLAT);
        gouraudBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, actorYawDegrees, SceneRasterMode.GOURAUD);
        texturedBuilder.addGeometry(geometry, actorX, baseHeight, actorZ, actorYawDegrees, SceneRasterMode.TEXTURED);
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT, flatBuilder.build());
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD, gouraudBuilder.build());
        renderQueueBuilder.add(SceneSubmissionKind.ACTOR, SceneRasterMode.TEXTURED, texturedBuilder.build());
        return;
      }
    }
    renderQueueBuilder.add(
        SceneSubmissionKind.ACTOR,
        SceneRasterMode.FLAT,
        buildFallbackActorMesh(actorLocalX, actorLocalY, baseHeight, appearance, equipment)
    );
  }

  private void appendFallbackObject(SceneTriangleMeshBuilder builder, WorldScene worldScene, WorldSceneObject worldSceneObject) {
    float minX = worldSceneObject.localX();
    float minZ = worldSceneObject.localY();
    float maxX = minX + Math.max(0.25f, worldSceneObject.sizeX());
    float maxZ = minZ + Math.max(0.25f, worldSceneObject.sizeY());
    float baseHeight = sampleObjectBaseHeight(worldScene, worldSceneObject, maxX, maxZ);
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

  static float actorYawDegrees(ActorAnimationState actorAnimationState) {
    if (actorAnimationState == null) {
      return 180.0f;
    }
    // The assembled player model's authored forward axis is opposite the world-space heading
    // convention used by movement deltas. Apply the 317-style forward correction here so the
    // actor faces and walks into the path instead of moonwalking backward.
    return normalizeDegrees(actorAnimationState.headingDegrees() + 180.0f);
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

  private float sampleObjectBaseHeight(
      WorldScene worldScene,
      WorldSceneObject worldSceneObject,
      float maxX,
      float maxZ
  ) {
    TerrainLayerSource terrainSource = shouldUseBridgeLowerSurface(worldScene, worldSceneObject)
        ? worldScene.bridgeTerrainLayer()
        : worldScene;
    return sampleBaseHeight(terrainSource, worldScene.tileWidth(), worldScene.tileHeight(), worldSceneObject.localX(), worldSceneObject.localY(), maxX, maxZ);
  }

  private float sampleBaseHeight(
      TerrainLayerSource terrainSource,
      int tileWidth,
      int tileHeight,
      float minX,
      float minZ,
      float maxX,
      float maxZ
  ) {
    int startX = clampTile((int) Math.floor(minX), tileWidth);
    int startY = clampTile((int) Math.floor(minZ), tileHeight);
    int endX = clampTile((int) Math.floor(maxX), tileWidth);
    int endY = clampTile((int) Math.floor(maxZ), tileHeight);
    float total = 0.0f;
    int samples = 0;
    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        total += terrainSource.elevationAt(x, y) * WorldSceneScale.HEIGHT_SCALE;
        samples++;
      }
    }
    return samples == 0 ? 0.0f : total / samples;
  }

  private boolean shouldUseBridgeLowerSurface(WorldScene worldScene, WorldSceneObject worldSceneObject) {
    if (worldSceneObject.plane() != 0 || (worldSceneObject.type() != 10 && worldSceneObject.type() != 11)) {
      return false;
    }
    for (int tileX = worldSceneObject.localX(); tileX < worldSceneObject.localX() + Math.max(1, worldSceneObject.sizeX()); tileX++) {
      for (int tileY = worldSceneObject.localY(); tileY < worldSceneObject.localY() + Math.max(1, worldSceneObject.sizeY()); tileY++) {
        if (worldScene.bridgeTerrainLayer().activeAt(tileX, tileY)) {
          return true;
        }
      }
    }
    return false;
  }

  private WorldSceneObjectGeometry terrainContouredGeometry(
      WorldScene worldScene,
      WorldSceneObject worldSceneObject,
      float baseHeight
  ) {
    WorldSceneObjectGeometry geometry = worldSceneObject.geometry();
    if (geometry == null || !worldSceneObject.contouredGround() || geometry.vertexY().length == 0) {
      return geometry;
    }
    // 317 object defs with opcode 21 are reprojected against the tile corner heights at submit
    // time. Fence and bridge-side trim pieces rely on this instead of staying as rigid meshes.
    GroundHeightProfile heightProfile = sampleGroundHeightProfile(worldScene, worldSceneObject);
    float footprintWidth = Math.max(1, worldSceneObject.sizeX());
    float footprintDepth = Math.max(1, worldSceneObject.sizeY());
    float[] contouredVertexY = new float[geometry.vertexY().length];
    for (int index = 0; index < geometry.vertexY().length; index++) {
      float xBlend = (geometry.vertexX()[index] + footprintWidth * 0.5f) / footprintWidth;
      float zBlend = (geometry.vertexZ()[index] + footprintDepth * 0.5f) / footprintDepth;
      float southHeight = interpolate(heightProfile.southWest(), heightProfile.southEast(), xBlend);
      float northHeight = interpolate(heightProfile.northWest(), heightProfile.northEast(), xBlend);
      float terrainHeight = interpolate(southHeight, northHeight, zBlend);
      contouredVertexY[index] = geometry.vertexY()[index] + (terrainHeight - baseHeight);
    }
    return new WorldSceneObjectGeometry(
        geometry.vertexX(),
        contouredVertexY,
        geometry.vertexZ(),
        geometry.faceVertexA(),
        geometry.faceVertexB(),
        geometry.faceVertexC(),
        geometry.faceColorA(),
        geometry.faceColorB(),
        geometry.faceColorC(),
        geometry.faceAlpha(),
        geometry.faceRasterModes(),
        geometry.faceTextureIds(),
        geometry.textureVertexA(),
        geometry.textureVertexB(),
        geometry.textureVertexC()
    );
  }

  private GroundHeightProfile sampleGroundHeightProfile(WorldScene worldScene, WorldSceneObject worldSceneObject) {
    int localX = worldSceneObject.localX();
    int localY = worldSceneObject.localY();
    int eastX = localX + Math.max(1, worldSceneObject.sizeX());
    int northY = localY + Math.max(1, worldSceneObject.sizeY());
    float southWest = sampleTerrainCornerHeight(worldScene, localX, localY);
    float southEast = sampleTerrainCornerHeight(worldScene, eastX, localY);
    float northEast = sampleTerrainCornerHeight(worldScene, eastX, northY);
    float northWest = sampleTerrainCornerHeight(worldScene, localX, northY);
    return new GroundHeightProfile(southWest, southEast, northEast, northWest);
  }

  private float sampleTerrainCornerHeight(WorldScene worldScene, int tileX, int tileY) {
    int clampedX = clampTile(tileX, worldScene.tileWidth());
    int clampedY = clampTile(tileY, worldScene.tileHeight());
    return worldScene.elevationAt(clampedX, clampedY) * WorldSceneScale.HEIGHT_SCALE;
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

  private float sampleTerrainHeight(WorldScene worldScene, float localX, float localY) {
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

  private float renderedLocalAxis(int rawTile, float positionOffset, int sceneSize) {
    return clamp(rawTile + 0.5f + positionOffset, 0.5f, sceneSize - 0.5f);
  }

  private int clampTile(int value, int tileBound) {
    return Math.max(0, Math.min(tileBound - 1, value));
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private float interpolate(float start, float end, float blend) {
    return start + (end - start) * blend;
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

  private record GroundHeightProfile(
      float southWest,
      float southEast,
      float northEast,
      float northWest
  ) {
  }
}
