package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;

/**
 * Dispatches scene objects through the legacy minimap render categories: boundary objects first
 * (walls / mapscene sprites), then interactive objects (mapscene sprites / diagonal walls), then
 * ground decorations (mapscene sprites only). For each tile only the first object in each
 * category renders, matching {@code GameClientCore.method50}'s "first-hit per query" behaviour.
 */
final class MinimapObjectRasterizer {

  private final MinimapMapSceneSpriteRasterizer mapSceneSprites;

  MinimapObjectRasterizer(MinimapMapSceneSpriteRasterizer mapSceneSprites) {
    this.mapSceneSprites = mapSceneSprites;
  }

  void rasterizeAll(
      int tileWidth,
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      List<WorldSceneObject> sceneObjects
  ) {
    MinimapSceneObjects indexed = indexSceneObjects(tileWidth, tileHeight, sceneObjects);
    for (int sceneY = 0; sceneY < tileHeight; sceneY++) {
      for (int sceneX = 0; sceneX < tileWidth; sceneX++) {
        int tileIndex = sceneY * tileWidth + sceneX;
        rasterizeBoundary(tileHeight, pixelWidth, pixelHeight, pixels, indexed.boundaryObjects()[tileIndex]);
        rasterizeInteractive(tileHeight, pixelWidth, pixelHeight, pixels, indexed.interactiveObjects()[tileIndex]);
        rasterizeGroundDecoration(tileHeight, pixelWidth, pixelHeight, pixels, indexed.groundDecorationObjects()[tileIndex]);
      }
    }
  }

  private void rasterizeBoundary(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (sceneObject == null) {
      return;
    }
    if (mapSceneSprites.drawIfPresent(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject)) {
      return;
    }
    if (MinimapWallRasterizer.isWall(sceneObject.type())) {
      MinimapWallRasterizer.rasterizeWall(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject,
          MinimapWallRasterizer.wallColor(sceneObject));
    }
  }

  private void rasterizeInteractive(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (sceneObject == null) {
      return;
    }
    if (mapSceneSprites.drawIfPresent(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject)) {
      return;
    }
    if (sceneObject.type() == 9) {
      MinimapWallRasterizer.rasterizeDiagonalWall(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject,
          MinimapWallRasterizer.wallColor(sceneObject));
    }
  }

  private void rasterizeGroundDecoration(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (sceneObject == null) {
      return;
    }
    mapSceneSprites.drawIfPresent(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject);
  }

  private MinimapSceneObjects indexSceneObjects(int tileWidth, int tileHeight, List<WorldSceneObject> sceneObjects) {
    WorldSceneObject[] boundaryObjects = new WorldSceneObject[tileWidth * tileHeight];
    WorldSceneObject[] interactiveObjects = new WorldSceneObject[tileWidth * tileHeight];
    WorldSceneObject[] groundDecorationObjects = new WorldSceneObject[tileWidth * tileHeight];
    for (WorldSceneObject sceneObject : sceneObjects) {
      if (sceneObject.localX() < 0
          || sceneObject.localY() < 0
          || sceneObject.localX() >= tileWidth
          || sceneObject.localY() >= tileHeight) {
        continue;
      }
      int tileIndex = sceneObject.localY() * tileWidth + sceneObject.localX();
      switch (queryCategoryFor(sceneObject.type())) {
        case BOUNDARY -> {
          if (boundaryObjects[tileIndex] == null) {
            boundaryObjects[tileIndex] = sceneObject;
          }
        }
        case INTERACTIVE -> {
          if (interactiveObjects[tileIndex] == null) {
            interactiveObjects[tileIndex] = sceneObject;
          }
        }
        case GROUND_DECORATION -> {
          if (groundDecorationObjects[tileIndex] == null) {
            groundDecorationObjects[tileIndex] = sceneObject;
          }
        }
        case NONE -> {
        }
      }
    }
    return new MinimapSceneObjects(boundaryObjects, interactiveObjects, groundDecorationObjects);
  }

  private QueryCategory queryCategoryFor(int objectType) {
    if (objectType >= 0 && objectType <= 3) {
      return QueryCategory.BOUNDARY;
    }
    if (objectType == 22) {
      return QueryCategory.GROUND_DECORATION;
    }
    if (objectType == 9 || objectType == 10 || objectType == 11 || objectType >= 12 && objectType <= 21) {
      return QueryCategory.INTERACTIVE;
    }
    return QueryCategory.NONE;
  }

  private enum QueryCategory {
    BOUNDARY,
    INTERACTIVE,
    GROUND_DECORATION,
    NONE
  }

  private record MinimapSceneObjects(
      WorldSceneObject[] boundaryObjects,
      WorldSceneObject[] interactiveObjects,
      WorldSceneObject[] groundDecorationObjects
  ) {
  }
}
