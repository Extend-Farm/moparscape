package io.github.ffakira.rsps.client.desktop.world.visibility;

import io.github.ffakira.rsps.client.desktop.world.WorldSceneScale;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainOcclusionFlagResolver;
import java.util.ArrayList;
import java.util.List;

public final class WorldSceneOccluderBuilder {

  private static final float WALL_THICKNESS = 0.12f;
  private static final float HORIZONTAL_OCCLUDER_THICKNESS = 0.08f;
  private static final int MIN_HORIZONTAL_OCCLUDER_TILE_AREA = 4;

  public List<WorldSceneOccluder> build(
      int scenePlane,
      int tileWidth,
      int tileHeight,
      int[] elevations,
      byte[] surfacePlanes,
      int[] terrainOcclusionFlags,
      List<WorldSceneObject> objects
  ) {
    // This is intentionally scene-side work, not viewport work. The loader captures cache-backed
    // walls/structures once, and submission can then reason about occlusion without pushing map
    // semantics down into the OpenGL render pass.
    List<WorldSceneOccluder> occluders = new ArrayList<>();
    addHorizontalTerrainOccluders(
        occluders,
        scenePlane,
        tileWidth,
        tileHeight,
        elevations,
        surfacePlanes,
        terrainOcclusionFlags
    );
    for (WorldSceneObject object : objects) {
      if (object.plane() != scenePlane) {
        continue;
      }
      float baseHeight = sampleBaseHeight(object, tileWidth, tileHeight, elevations);
      GeometryBounds bounds = geometryBounds(object, baseHeight);
      addWallOccluders(occluders, object, bounds);
    }
    return List.copyOf(occluders);
  }

  public List<WorldSceneOccluder> build(
      int scenePlane,
      int tileWidth,
      int tileHeight,
      int[] elevations,
      List<WorldSceneObject> objects
  ) {
    return build(
        scenePlane,
        tileWidth,
        tileHeight,
        elevations,
        new byte[tileWidth * tileHeight],
        new int[tileWidth * tileHeight],
        objects
    );
  }

  private void addWallOccluders(
      List<WorldSceneOccluder> occluders,
      WorldSceneObject object,
      GeometryBounds bounds
  ) {
    if (object.type() == 0) {
      addStraightWallOccluder(occluders, object, bounds);
      return;
    }
    if (object.type() == 1 || object.type() == 3) {
      addThinWallOccluder(occluders, object, bounds);
      return;
    }
    if (object.type() == 2) {
      addCornerWallOccluders(occluders, object, bounds);
    }
  }

  private void addStraightWallOccluder(
      List<WorldSceneOccluder> occluders,
      WorldSceneObject object,
      GeometryBounds bounds
  ) {
    if ((object.orientation() & 1) == 0) {
      float axis = object.orientation() == 0 ? object.localX() : object.localX() + object.sizeX();
      occluders.add(xWallOccluder(object.plane(), axis, object.localY(), object.localY() + object.sizeY(), bounds));
      return;
    }
    float axis = object.orientation() == 1 ? object.localY() + object.sizeY() : object.localY();
    occluders.add(zWallOccluder(object.plane(), object.localX(), object.localX() + object.sizeX(), axis, bounds));
  }

  private void addThinWallOccluder(
      List<WorldSceneOccluder> occluders,
      WorldSceneObject object,
      GeometryBounds bounds
  ) {
    float width = bounds.maxX() - bounds.minX();
    float depth = bounds.maxZ() - bounds.minZ();
    if (width <= depth) {
      occluders.add(xWallOccluder(
          object.plane(),
          (bounds.minX() + bounds.maxX()) * 0.5f,
          bounds.minZ(),
          bounds.maxZ(),
          bounds
      ));
      return;
    }
    occluders.add(zWallOccluder(
        object.plane(),
        bounds.minX(),
        bounds.maxX(),
        (bounds.minZ() + bounds.maxZ()) * 0.5f,
        bounds
    ));
  }

  private void addCornerWallOccluders(
      List<WorldSceneOccluder> occluders,
      WorldSceneObject object,
      GeometryBounds bounds
  ) {
    switch (object.orientation() & 3) {
      case 0 -> {
        occluders.add(xWallOccluder(object.plane(), object.localX(), object.localY(), object.localY() + object.sizeY(), bounds));
        occluders.add(zWallOccluder(object.plane(), object.localX(), object.localX() + object.sizeX(), object.localY() + object.sizeY(), bounds));
      }
      case 1 -> {
        occluders.add(xWallOccluder(object.plane(), object.localX() + object.sizeX(), object.localY(), object.localY() + object.sizeY(), bounds));
        occluders.add(zWallOccluder(object.plane(), object.localX(), object.localX() + object.sizeX(), object.localY() + object.sizeY(), bounds));
      }
      case 2 -> {
        occluders.add(xWallOccluder(object.plane(), object.localX() + object.sizeX(), object.localY(), object.localY() + object.sizeY(), bounds));
        occluders.add(zWallOccluder(object.plane(), object.localX(), object.localX() + object.sizeX(), object.localY(), bounds));
      }
      default -> {
        occluders.add(xWallOccluder(object.plane(), object.localX(), object.localY(), object.localY() + object.sizeY(), bounds));
        occluders.add(zWallOccluder(object.plane(), object.localX(), object.localX() + object.sizeX(), object.localY(), bounds));
      }
    }
  }

  private void addHorizontalTerrainOccluders(
      List<WorldSceneOccluder> occluders,
      int scenePlane,
      int tileWidth,
      int tileHeight,
      int[] elevations,
      byte[] surfacePlanes,
      int[] terrainOcclusionFlags
  ) {
    boolean[] consumedTiles = new boolean[tileWidth * tileHeight];
    for (int tileY = 0; tileY < tileHeight - 1; tileY++) {
      for (int tileX = 0; tileX < tileWidth - 1; tileX++) {
        int tileIndex = tileY * tileWidth + tileX;
        if (consumedTiles[tileIndex] || !hasHorizontalOccluderFlag(terrainOcclusionFlags[tileIndex])) {
          continue;
        }
        int surfacePlane = surfacePlanes[tileIndex] & 0xff;
        if (surfacePlane <= scenePlane) {
          continue;
        }
        int maxTileX = tileX;
        while (maxTileX + 1 < tileWidth - 1 && matchesHorizontalOccluderTile(
            maxTileX + 1,
            tileY,
            tileWidth,
            scenePlane,
            surfacePlane,
            surfacePlanes,
            terrainOcclusionFlags,
            consumedTiles
        )) {
          maxTileX++;
        }
        int maxTileY = tileY;
        boolean extendSouth = true;
        while (extendSouth && maxTileY + 1 < tileHeight - 1) {
          for (int spanTileX = tileX; spanTileX <= maxTileX; spanTileX++) {
            if (!matchesHorizontalOccluderTile(
                spanTileX,
                maxTileY + 1,
                tileWidth,
                scenePlane,
                surfacePlane,
                surfacePlanes,
                terrainOcclusionFlags,
                consumedTiles
            )) {
              extendSouth = false;
              break;
            }
          }
          if (extendSouth) {
            maxTileY++;
          }
        }
        int tileArea = (maxTileX - tileX + 1) * (maxTileY - tileY + 1);
        if (tileArea >= MIN_HORIZONTAL_OCCLUDER_TILE_AREA) {
          float height = elevations[tileIndex] * WorldSceneScale.HEIGHT_SCALE;
          occluders.add(new WorldSceneOccluder(
              WorldSceneOccluderType.HORIZONTAL_PLANE,
              scenePlane,
              height,
              tileX,
              maxTileX + 1.0f,
              tileY,
              maxTileY + 1.0f,
              height - HORIZONTAL_OCCLUDER_THICKNESS,
              height + HORIZONTAL_OCCLUDER_THICKNESS
          ));
          markConsumed(consumedTiles, tileWidth, tileX, maxTileX, tileY, maxTileY);
        }
      }
    }
  }

  private WorldSceneOccluder xWallOccluder(
      int plane,
      float axis,
      float minZ,
      float maxZ,
      GeometryBounds bounds
  ) {
    return new WorldSceneOccluder(
        WorldSceneOccluderType.X_WALL,
        plane,
        axis,
        axis - WALL_THICKNESS,
        axis + WALL_THICKNESS,
        minZ,
        maxZ,
        bounds.minY(),
        bounds.maxY()
    );
  }

  private WorldSceneOccluder zWallOccluder(
      int plane,
      float minX,
      float maxX,
      float axis,
      GeometryBounds bounds
  ) {
    return new WorldSceneOccluder(
        WorldSceneOccluderType.Z_WALL,
        plane,
        axis,
        minX,
        maxX,
        axis - WALL_THICKNESS,
        axis + WALL_THICKNESS,
        bounds.minY(),
        bounds.maxY()
    );
  }

  private float sampleBaseHeight(
      WorldSceneObject object,
      int tileWidth,
      int tileHeight,
      int[] elevations
  ) {
    int startX = clamp(object.localX(), 0, tileWidth - 1);
    int startY = clamp(object.localY(), 0, tileHeight - 1);
    int endX = clamp(object.localX() + Math.max(1, object.sizeX()) - 1, 0, tileWidth - 1);
    int endY = clamp(object.localY() + Math.max(1, object.sizeY()) - 1, 0, tileHeight - 1);
    float total = 0.0f;
    int samples = 0;
    for (int x = startX; x <= endX; x++) {
      for (int y = startY; y <= endY; y++) {
        total += elevations[y * tileWidth + x] * WorldSceneScale.HEIGHT_SCALE;
        samples++;
      }
    }
    return samples == 0 ? 0.0f : total / samples;
  }

  private GeometryBounds geometryBounds(WorldSceneObject object, float baseHeight) {
    if (object.geometry() == null || object.geometry().vertexX().length == 0) {
      return new GeometryBounds(
          object.localX(),
          object.localX() + Math.max(0.25f, object.sizeX()),
          baseHeight,
          baseHeight + fallbackObjectHeight(object),
          object.localY(),
          object.localY() + Math.max(0.25f, object.sizeY())
      );
    }
    float minX = Float.POSITIVE_INFINITY;
    float maxX = Float.NEGATIVE_INFINITY;
    float minY = Float.POSITIVE_INFINITY;
    float maxY = Float.NEGATIVE_INFINITY;
    float minZ = Float.POSITIVE_INFINITY;
    float maxZ = Float.NEGATIVE_INFINITY;
    float objectCenterX = object.centerX();
    float objectCenterZ = object.centerY();
    for (int index = 0; index < object.geometry().vertexX().length; index++) {
      minX = Math.min(minX, objectCenterX + object.geometry().vertexX()[index]);
      maxX = Math.max(maxX, objectCenterX + object.geometry().vertexX()[index]);
      minY = Math.min(minY, baseHeight + object.geometry().vertexY()[index]);
      maxY = Math.max(maxY, baseHeight + object.geometry().vertexY()[index]);
      minZ = Math.min(minZ, objectCenterZ + object.geometry().vertexZ()[index]);
      maxZ = Math.max(maxZ, objectCenterZ + object.geometry().vertexZ()[index]);
    }
    return new GeometryBounds(minX, maxX, minY, maxY, minZ, maxZ);
  }

  private float fallbackObjectHeight(WorldSceneObject object) {
    return switch (object.type()) {
      case 0, 1, 2, 3 -> 2.0f;
      case 10, 11 -> 2.4f;
      case 12, 13, 14, 15, 16, 17 -> 2.0f;
      case 22 -> 0.25f;
      default -> 1.0f;
    };
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private boolean hasHorizontalOccluderFlag(int terrainOcclusionFlags) {
    return (terrainOcclusionFlags & TerrainOcclusionFlagResolver.LEGACY_FLAT_TILE_OCCLUSION_MASK) != 0;
  }

  private boolean matchesHorizontalOccluderTile(
      int tileX,
      int tileY,
      int tileWidth,
      int scenePlane,
      int surfacePlane,
      byte[] surfacePlanes,
      int[] terrainOcclusionFlags,
      boolean[] consumedTiles
  ) {
    int tileIndex = tileY * tileWidth + tileX;
    return !consumedTiles[tileIndex]
        && hasHorizontalOccluderFlag(terrainOcclusionFlags[tileIndex])
        && (surfacePlanes[tileIndex] & 0xff) == surfacePlane
        && surfacePlane > scenePlane;
  }

  private void markConsumed(boolean[] consumedTiles, int tileWidth, int minTileX, int maxTileX, int minTileY, int maxTileY) {
    for (int tileY = minTileY; tileY <= maxTileY; tileY++) {
      for (int tileX = minTileX; tileX <= maxTileX; tileX++) {
        consumedTiles[tileY * tileWidth + tileX] = true;
      }
    }
  }

  private record GeometryBounds(
      float minX,
      float maxX,
      float minY,
      float maxY,
      float minZ,
      float maxZ
  ) {
  }
}
