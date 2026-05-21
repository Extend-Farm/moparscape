package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SplittableRandom;

public final class WorldSceneMapFunctionMarkerCollector {

  private static final Set<Integer> STATIONARY_MAP_FUNCTION_IDS = Set.of(22, 29, 34, 36, 46, 47, 48);
  private static final int DRIFT_STEPS = 10;
  private static final int DRIFT_LIMIT_TILES = 3;
  private static final int BLOCKED_TILE_MASK = 0x200000;
  private static final int SOLID_OBJECT_MASK = 0x100;
  private static final int SOUTH_WALL_MASK = 0x2;
  private static final int WEST_WALL_MASK = 0x8;
  private static final int NORTH_WALL_MASK = 0x20;
  private static final int EAST_WALL_MASK = 0x80;
  private static final int EDGE_BLOCK_MASK = BLOCKED_TILE_MASK
      | SOLID_OBJECT_MASK
      | SOUTH_WALL_MASK
      | WEST_WALL_MASK
      | NORTH_WALL_MASK
      | EAST_WALL_MASK;

  public List<WorldSceneMapFunctionMarker> collect(
      List<WorldSceneObject> sceneObjects,
      int sceneTileWidth,
      int sceneTileHeight
  ) {
    ArrayList<WorldSceneMapFunctionMarker> markers = new ArrayList<>();
    int[] collisionFlags = buildCollisionFlags(sceneObjects, sceneTileWidth, sceneTileHeight);
    for (WorldSceneObject sceneObject : sceneObjects) {
      // Legacy method24 caches mapfunction icons from the ground-decoration lookup rather than
      // rediscovering them from the whole visible object list during every radar draw.
      if (sceneObject.type() != 22 || sceneObject.mapFunctionId() < 0) {
        continue;
      }
      int markerLocalX = sceneObject.localX();
      int markerLocalY = sceneObject.localY();
      if (!STATIONARY_MAP_FUNCTION_IDS.contains(sceneObject.mapFunctionId())) {
        int[] driftedTile = driftMarker(sceneObject, collisionFlags, sceneTileWidth, sceneTileHeight);
        markerLocalX = driftedTile[0];
        markerLocalY = driftedTile[1];
      }
      markers.add(new WorldSceneMapFunctionMarker(markerLocalX, markerLocalY, sceneObject.mapFunctionId()));
    }
    return List.copyOf(markers);
  }

  private static int[] buildCollisionFlags(
      List<WorldSceneObject> sceneObjects,
      int sceneTileWidth,
      int sceneTileHeight
  ) {
    int[] collisionFlags = new int[sceneTileWidth * sceneTileHeight];
    for (int localY = 0; localY < sceneTileHeight; localY++) {
      for (int localX = 0; localX < sceneTileWidth; localX++) {
        if (localX == 0 || localY == 0 || localX == sceneTileWidth - 1 || localY == sceneTileHeight - 1) {
          collisionFlags[index(localX, localY, sceneTileWidth)] = EDGE_BLOCK_MASK;
        }
      }
    }
    for (WorldSceneObject sceneObject : sceneObjects) {
      if (!sceneObject.solid()) {
        continue;
      }
      switch (sceneObject.type()) {
        case 0, 1, 2, 3 -> stampWall(collisionFlags, sceneTileWidth, sceneTileHeight, sceneObject);
        case 9, 10, 11 -> stampSolidFootprint(collisionFlags, sceneTileWidth, sceneTileHeight, sceneObject);
        case 22 -> {
          if (sceneObject.interactive()) {
            stampHardBlockedTile(collisionFlags, sceneTileWidth, sceneTileHeight, sceneObject.localX(), sceneObject.localY());
          }
        }
        default -> {
          if (sceneObject.type() >= 12) {
            stampSolidFootprint(collisionFlags, sceneTileWidth, sceneTileHeight, sceneObject);
          }
        }
      }
    }
    return collisionFlags;
  }

  private static int[] driftMarker(
      WorldSceneObject sceneObject,
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight
  ) {
    int originX = sceneObject.localX();
    int originY = sceneObject.localY();
    int driftedX = originX;
    int driftedY = originY;
    // Legacy method24 rebuilds marker drift from Math.random(). Keep the same 10-step / 3-tile
    // walk contract, but seed it per marker so the native client stays stable across redraws.
    SplittableRandom random = new SplittableRandom(markerSeed(sceneObject));
    for (int step = 0; step < DRIFT_STEPS; step++) {
      int direction = random.nextInt(4);
      if (direction == 0
          && driftedX > 0
          && driftedX > originX - DRIFT_LIMIT_TILES
          && (flagAt(collisionFlags, sceneTileWidth, sceneTileHeight, driftedX - 1, driftedY)
          & (BLOCKED_TILE_MASK | SOLID_OBJECT_MASK | WEST_WALL_MASK)) == 0) {
        driftedX--;
      }
      if (direction == 1
          && driftedX < sceneTileWidth - 1
          && driftedX < originX + DRIFT_LIMIT_TILES
          && (flagAt(collisionFlags, sceneTileWidth, sceneTileHeight, driftedX + 1, driftedY)
          & (BLOCKED_TILE_MASK | SOLID_OBJECT_MASK | EAST_WALL_MASK)) == 0) {
        driftedX++;
      }
      if (direction == 2
          && driftedY > 0
          && driftedY > originY - DRIFT_LIMIT_TILES
          && (flagAt(collisionFlags, sceneTileWidth, sceneTileHeight, driftedX, driftedY - 1)
          & (BLOCKED_TILE_MASK | SOLID_OBJECT_MASK | SOUTH_WALL_MASK)) == 0) {
        driftedY--;
      }
      if (direction == 3
          && driftedY < sceneTileHeight - 1
          && driftedY < originY + DRIFT_LIMIT_TILES
          && (flagAt(collisionFlags, sceneTileWidth, sceneTileHeight, driftedX, driftedY + 1)
          & (BLOCKED_TILE_MASK | SOLID_OBJECT_MASK | NORTH_WALL_MASK)) == 0) {
        driftedY++;
      }
    }
    return new int[]{driftedX, driftedY};
  }

  private static void stampWall(
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight,
      WorldSceneObject sceneObject
  ) {
    int localX = sceneObject.localX();
    int localY = sceneObject.localY();
    int orientation = sceneObject.orientation() & 3;
    switch (sceneObject.type()) {
      case 0 -> {
        if (orientation == 0) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, EAST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX - 1, localY, WEST_WALL_MASK);
        }
        if (orientation == 1) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, SOUTH_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY + 1, NORTH_WALL_MASK);
        }
        if (orientation == 2) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, WEST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX + 1, localY, EAST_WALL_MASK);
        }
        if (orientation == 3) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, NORTH_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY - 1, SOUTH_WALL_MASK);
        }
      }
      case 2 -> {
        if (orientation == 0) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, EAST_WALL_MASK | SOUTH_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX - 1, localY, WEST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY + 1, NORTH_WALL_MASK);
        }
        if (orientation == 1) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, SOUTH_WALL_MASK | WEST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY + 1, NORTH_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX + 1, localY, EAST_WALL_MASK);
        }
        if (orientation == 2) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, NORTH_WALL_MASK | WEST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX + 1, localY, EAST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY - 1, SOUTH_WALL_MASK);
        }
        if (orientation == 3) {
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, NORTH_WALL_MASK | EAST_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY - 1, SOUTH_WALL_MASK);
          addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX - 1, localY, WEST_WALL_MASK);
        }
      }
      default -> {
        // Diagonal wall bits do not participate in the cardinal-only legacy marker drift checks.
      }
    }
  }

  private static void stampSolidFootprint(
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight,
      WorldSceneObject sceneObject
  ) {
    int width = sceneObject.sizeX();
    int height = sceneObject.sizeY();
    if ((sceneObject.orientation() & 1) == 1) {
      int swapped = width;
      width = height;
      height = swapped;
    }
    for (int localX = sceneObject.localX(); localX < sceneObject.localX() + width; localX++) {
      for (int localY = sceneObject.localY(); localY < sceneObject.localY() + height; localY++) {
        addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, SOLID_OBJECT_MASK);
      }
    }
  }

  private static void stampHardBlockedTile(
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight,
      int localX,
      int localY
  ) {
    addFlag(collisionFlags, sceneTileWidth, sceneTileHeight, localX, localY, BLOCKED_TILE_MASK);
  }

  private static void addFlag(
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight,
      int localX,
      int localY,
      int flagMask
  ) {
    if (localX < 0 || localY < 0 || localX >= sceneTileWidth || localY >= sceneTileHeight) {
      return;
    }
    collisionFlags[index(localX, localY, sceneTileWidth)] |= flagMask;
  }

  private static int flagAt(
      int[] collisionFlags,
      int sceneTileWidth,
      int sceneTileHeight,
      int localX,
      int localY
  ) {
    if (localX < 0 || localY < 0 || localX >= sceneTileWidth || localY >= sceneTileHeight) {
      return EDGE_BLOCK_MASK;
    }
    return collisionFlags[index(localX, localY, sceneTileWidth)];
  }

  private static int index(int localX, int localY, int sceneTileWidth) {
    return localX + localY * sceneTileWidth;
  }

  private static long markerSeed(WorldSceneObject sceneObject) {
    return ((long) sceneObject.objectId() << 32)
        ^ ((long) sceneObject.mapFunctionId() << 24)
        ^ ((long) sceneObject.localX() << 12)
        ^ sceneObject.localY();
  }
}
