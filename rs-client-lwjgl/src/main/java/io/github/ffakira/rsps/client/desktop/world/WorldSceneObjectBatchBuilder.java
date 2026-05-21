package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueueBuilder;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneSubmissionKind;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionContext;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionPlanner;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityWindow;
import java.util.Set;

final class WorldSceneObjectBatchBuilder {

  // Object ids that should never even fall back to a proxy — anonymous-name auto-deny isn't
  // enough for these because the cache does have a definition yet its model is missing here and
  // the proxy box adds nothing recognisable.
  private static final Set<Integer> FALLBACK_PROXY_DENYLIST = Set.of();
  private static final String ANONYMOUS_OBJECT_NAME_PREFIX = "object-";

  void addBatches(
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
        float[] wallDecorOffset = wallDecorationCenterOffset(worldSceneObject);
        objectCenterX += wallDecorOffset[0];
        objectCenterZ += wallDecorOffset[1];
        flatBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.FLAT);
        gouraudBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.GOURAUD);
        texturedBuilder.addGeometry(geometry, objectCenterX, baseHeight, objectCenterZ, SceneRasterMode.TEXTURED);
        continue;
      }
      if (worldSceneObject.allowFallbackProxy() && !isFallbackProxyDenied(worldSceneObject)) {
        appendFallbackObject(flatBuilder, worldScene, worldSceneObject);
      }
    }
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT, flatBuilder.build());
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD, gouraudBuilder.build());
    renderQueueBuilder.add(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.TEXTURED, texturedBuilder.build());
  }

  private void appendFallbackObject(SceneTriangleMeshBuilder builder, WorldScene worldScene, WorldSceneObject worldSceneObject) {
    float minX = worldSceneObject.localX();
    float minZ = worldSceneObject.localY();
    float maxX = minX + Math.max(0.25f, worldSceneObject.sizeX());
    float maxZ = minZ + Math.max(0.25f, worldSceneObject.sizeY());
    float baseHeight = sampleObjectBaseHeight(worldScene, worldSceneObject, maxX, maxZ);
    float objectHeight = fallbackObjectHeight(worldSceneObject);
    float topHeight = baseHeight + objectHeight;
    int objectColor = fallbackObjectColor(worldSceneObject);
    if (isStraightWallType(worldSceneObject.type())) {
      appendEdgeWallFallback(
          builder,
          minX,
          maxX,
          minZ,
          maxZ,
          baseHeight,
          topHeight,
          worldSceneObject.orientation(),
          fallbackWallThickness(worldSceneObject),
          objectColor
      );
      return;
    }
    if (worldSceneObject.type() == 2) {
      float wallThickness = fallbackWallThickness(worldSceneObject);
      appendEdgeWallFallback(
          builder,
          minX,
          maxX,
          minZ,
          maxZ,
          baseHeight,
          topHeight,
          worldSceneObject.orientation(),
          wallThickness,
          objectColor
      );
      appendEdgeWallFallback(
          builder,
          minX,
          maxX,
          minZ,
          maxZ,
          baseHeight,
          topHeight,
          worldSceneObject.orientation() + 1,
          wallThickness,
          objectColor
      );
      return;
    }
    if (isCornerWallType(worldSceneObject.type())) {
      appendCornerPostFallback(
          builder,
          minX,
          maxX,
          minZ,
          maxZ,
          baseHeight,
          topHeight,
          worldSceneObject.orientation(),
          fallbackWallThickness(worldSceneObject),
          objectColor
      );
      return;
    }
    if (isLargeStructureType(worldSceneObject.type())
        && (worldSceneObject.sizeX() > 1 || worldSceneObject.sizeY() > 1)) {
      appendPerimeterFallback(builder, minX, maxX, minZ, maxZ, baseHeight, topHeight, objectColor);
      return;
    }
    if (isColumnLikeObject(worldSceneObject)) {
      appendColumnFallback(builder, minX, maxX, minZ, maxZ, baseHeight, topHeight, objectColor);
      return;
    }
    appendCuboid(builder, minX, maxX, minZ, maxZ, baseHeight, topHeight, objectColor);
  }

  private boolean isColumnLikeObject(WorldSceneObject worldSceneObject) {
    String lowercaseName = worldSceneObject.name().toLowerCase();
    return lowercaseName.contains("statue")
        || lowercaseName.contains("monument")
        || lowercaseName.contains("pillar")
        || lowercaseName.contains("altar")
        || lowercaseName.contains("torch")
        || lowercaseName.contains("lamp")
        || lowercaseName.contains("candle");
  }

  private void appendColumnFallback(
      SceneTriangleMeshBuilder builder,
      float minX,
      float maxX,
      float minZ,
      float maxZ,
      float baseHeight,
      float topHeight,
      int rgb
  ) {
    float centerX = (minX + maxX) * 0.5f;
    float centerZ = (minZ + maxZ) * 0.5f;
    float tileSizeX = maxX - minX;
    float tileSizeZ = maxZ - minZ;
    float baseRadius = Math.min(tileSizeX, tileSizeZ) * 0.42f;
    float columnRadius = Math.min(tileSizeX, tileSizeZ) * 0.22f;
    float pedestalHeight = baseHeight + (topHeight - baseHeight) * 0.18f;
    float columnTop = baseHeight + (topHeight - baseHeight) * 0.92f;
    appendCuboid(builder,
        centerX - baseRadius, centerX + baseRadius,
        centerZ - baseRadius, centerZ + baseRadius,
        baseHeight, pedestalHeight, rgb);
    appendCuboid(builder,
        centerX - columnRadius, centerX + columnRadius,
        centerZ - columnRadius, centerZ + columnRadius,
        pedestalHeight, columnTop, rgb);
    int capRgb = applyBrightness(rgb, 0.78f);
    appendCuboid(builder,
        centerX - columnRadius * 0.85f, centerX + columnRadius * 0.85f,
        centerZ - columnRadius * 0.85f, centerZ + columnRadius * 0.85f,
        columnTop, topHeight, capRgb);
  }

  private static int applyBrightness(int rgb, float factor) {
    int red = Math.max(0, Math.min(255, Math.round(((rgb >>> 16) & 0xff) * factor)));
    int green = Math.max(0, Math.min(255, Math.round(((rgb >>> 8) & 0xff) * factor)));
    int blue = Math.max(0, Math.min(255, Math.round((rgb & 0xff) * factor)));
    return (red << 16) | (green << 8) | blue;
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
    int startX = Math.max(0, worldSceneObject.localX());
    int startY = Math.max(0, worldSceneObject.localY());
    int endX = Math.min(worldScene.tileWidth(), worldSceneObject.localX() + Math.max(1, worldSceneObject.sizeX()));
    int endY = Math.min(worldScene.tileHeight(), worldSceneObject.localY() + Math.max(1, worldSceneObject.sizeY()));
    for (int tileX = startX; tileX < endX; tileX++) {
      for (int tileY = startY; tileY < endY; tileY++) {
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
        geometry.textureVertexC(),
        geometry.facePriorities()
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

  private void appendPerimeterFallback(
      SceneTriangleMeshBuilder builder,
      float minX,
      float maxX,
      float minZ,
      float maxZ,
      float baseHeight,
      float topHeight,
      int rgb
  ) {
    float thickness = Math.min(0.18f, Math.min((maxX - minX) * 0.25f, (maxZ - minZ) * 0.25f));
    appendCuboid(builder, minX, minX + thickness, minZ, maxZ, baseHeight, topHeight, rgb);
    appendCuboid(builder, maxX - thickness, maxX, minZ, maxZ, baseHeight, topHeight, rgb);
    appendCuboid(builder, minX + thickness, maxX - thickness, minZ, minZ + thickness, baseHeight, topHeight, rgb);
    appendCuboid(builder, minX + thickness, maxX - thickness, maxZ - thickness, maxZ, baseHeight, topHeight, rgb);
  }

  private void appendEdgeWallFallback(
      SceneTriangleMeshBuilder builder,
      float minX,
      float maxX,
      float minZ,
      float maxZ,
      float baseHeight,
      float topHeight,
      int orientation,
      float thickness,
      int rgb
  ) {
    switch (orientation & 3) {
      case 0 -> appendCuboid(builder, minX, minX + thickness, minZ, maxZ, baseHeight, topHeight, rgb);
      case 1 -> appendCuboid(builder, minX, maxX, maxZ - thickness, maxZ, baseHeight, topHeight, rgb);
      case 2 -> appendCuboid(builder, maxX - thickness, maxX, minZ, maxZ, baseHeight, topHeight, rgb);
      default -> appendCuboid(builder, minX, maxX, minZ, minZ + thickness, baseHeight, topHeight, rgb);
    }
  }

  private void appendCornerPostFallback(
      SceneTriangleMeshBuilder builder,
      float minX,
      float maxX,
      float minZ,
      float maxZ,
      float baseHeight,
      float topHeight,
      int orientation,
      float thickness,
      int rgb
  ) {
    switch (orientation & 3) {
      case 0 -> appendCuboid(builder, minX, minX + thickness, maxZ - thickness, maxZ, baseHeight, topHeight, rgb);
      case 1 -> appendCuboid(builder, maxX - thickness, maxX, maxZ - thickness, maxZ, baseHeight, topHeight, rgb);
      case 2 -> appendCuboid(builder, maxX - thickness, maxX, minZ, minZ + thickness, baseHeight, topHeight, rgb);
      default -> appendCuboid(builder, minX, minX + thickness, minZ, minZ + thickness, baseHeight, topHeight, rgb);
    }
  }

  private float fallbackObjectHeight(WorldSceneObject worldSceneObject) {
    return switch (worldSceneObject.type()) {
      case 0, 1, 2, 3 -> 1.9f;
      case 10, 11 -> 2.4f;
      case 12, 13, 14, 15, 16, 17 -> 2.0f;
      case 22 -> 0.25f;
      default -> 1.0f;
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

  private float[] wallDecorationCenterOffset(WorldSceneObject worldSceneObject) {
    int type = worldSceneObject.type();
    if (type < 4 || type > 8) {
      return new float[]{0.0f, 0.0f};
    }
    float displacement = worldSceneObject.wallDecorDisplacement() / 128.0f;
    float halfTile = 0.5f;
    float pushFromCenter = halfTile - displacement;
    int orientation = worldSceneObject.orientation() & 3;
    return switch (orientation) {
      case 0 -> new float[]{-pushFromCenter, 0.0f};
      case 1 -> new float[]{0.0f, pushFromCenter};
      case 2 -> new float[]{pushFromCenter, 0.0f};
      default -> new float[]{0.0f, -pushFromCenter};
    };
  }

  private boolean isFallbackProxyDenied(WorldSceneObject worldSceneObject) {
    if (FALLBACK_PROXY_DENYLIST.contains(worldSceneObject.objectId())) {
      return true;
    }
    String name = worldSceneObject.name();
    return name != null && name.startsWith(ANONYMOUS_OBJECT_NAME_PREFIX);
  }

  private int fallbackObjectColor(WorldSceneObject worldSceneObject) {
    String lowercaseName = worldSceneObject.name().toLowerCase();
    if (lowercaseName.contains("tree") || lowercaseName.contains("bush")
        || lowercaseName.contains("cactus") || lowercaseName.contains("hedge")) {
      return 0x557c39;
    }
    if (lowercaseName.contains("rock") || lowercaseName.contains("stone")
        || lowercaseName.contains("altar") || lowercaseName.contains("statue")
        || lowercaseName.contains("monument") || lowercaseName.contains("pillar")
        || lowercaseName.contains("ruin")) {
      return 0x9c9a92;
    }
    if (lowercaseName.contains("fence") || lowercaseName.contains("gate") || lowercaseName.contains("door")) {
      return 0x7b5b33;
    }
    if (lowercaseName.contains("wall")) {
      return 0x6f665d;
    }
    if (lowercaseName.contains("stall") || lowercaseName.contains("crate")
        || lowercaseName.contains("box") || lowercaseName.contains("barrel")) {
      return 0x7a5e2f;
    }
    if (lowercaseName.contains("shield") || lowercaseName.contains("banner")) {
      return 0x355d8a;
    }
    return hashedColor(worldSceneObject.objectId(), 0x5e4f39, 0xa88d63);
  }

  private boolean isStraightWallType(int objectType) {
    return objectType == 0;
  }

  private boolean isCornerWallType(int objectType) {
    return objectType == 1 || objectType == 3;
  }

  private boolean isLargeStructureType(int objectType) {
    return switch (objectType) {
      case 9, 10, 11, 12, 13, 14, 15, 16, 17 -> true;
      default -> false;
    };
  }

  private float fallbackWallThickness(WorldSceneObject worldSceneObject) {
    String lowercaseName = worldSceneObject.name().toLowerCase();
    if (lowercaseName.contains("door") || lowercaseName.contains("gate")) {
      return 0.12f;
    }
    if (lowercaseName.contains("fence")) {
      return 0.10f;
    }
    return 0.18f;
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

  private float interpolate(float start, float end, float blend) {
    return start + (end - start) * blend;
  }

  private record GroundHeightProfile(
      float southWest,
      float southEast,
      float northEast,
      float northWest
  ) {
  }
}
