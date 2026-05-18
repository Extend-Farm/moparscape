package io.github.ffakira.rsps.client.lwjgl;

import java.util.List;

final class WorldSceneMinimapRasterizer {

  private static final int TILE_PIXELS = 4;
  private static final int[][] TILE_SHAPE_MASKS = {
      new int[16],
      {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1},
      {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
      {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
      {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
      {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}
  };
  private static final int[][] TILE_ROTATION_MAP = {
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
      {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
      {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
      {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}
  };
  private static final int WALL_COLOR_RGB = 0x4c3f35;
  private static final int DECORATION_COLOR_RGB = 0xcfcc9b;
  private static final int OBJECT_COLOR_RGB = 0x5c4a34;
  private static final int TREE_COLOR_RGB = 0x436c33;
  private static final int ROCK_COLOR_RGB = 0x7c7c76;
  private static final int WATER_COLOR_RGB = 0x365f84;

  ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      byte[] overlayShapes,
      byte[] overlayRotations,
      List<WorldSceneObject> sceneObjects
  ) {
    int pixelWidth = tileWidth * TILE_PIXELS;
    int pixelHeight = tileHeight * TILE_PIXELS;
    int[] pixels = new int[pixelWidth * pixelHeight];
    for (int sceneY = 0; sceneY < tileHeight; sceneY++) {
      for (int sceneX = 0; sceneX < tileWidth; sceneX++) {
        int sourceIndex = sceneY * tileWidth + sceneX;
        rasterizeTile(
            tileWidth,
            tileHeight,
            pixelWidth,
            pixelHeight,
            pixels,
            sceneX,
            sceneY,
            tileColors[sourceIndex],
            underlayColors[sourceIndex],
            overlayColors[sourceIndex],
            overlayShapes[sourceIndex] & 0xff,
            overlayRotations[sourceIndex] & 0x03
        );
      }
    }
    for (WorldSceneObject sceneObject : sceneObjects) {
      rasterizeObject(tileWidth, tileHeight, pixelWidth, pixelHeight, pixels, sceneObject);
    }
    return new ArgbImage(pixelWidth, pixelHeight, pixels);
  }

  private void rasterizeTile(
      int tileWidth,
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int sceneX,
      int sceneY,
      int tileRgb,
      int underlayRgb,
      int overlayRgb,
      int overlayShape,
      int overlayRotation
  ) {
    int paintRgb = minimapColor(tileRgb, underlayRgb, overlayRgb);
    if (overlayShape <= 1 || overlayShape >= TILE_SHAPE_MASKS.length) {
      fillTile(tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY, paintRgb);
      return;
    }
    int fillUnderlayRgb = minimapColor(underlayRgb, tileRgb, overlayRgb);
    int fillOverlayRgb = minimapColor(overlayRgb, tileRgb, underlayRgb);
    int[] shapeMask = TILE_SHAPE_MASKS[overlayShape];
    int[] rotationMap = TILE_ROTATION_MAP[overlayRotation];
    int startX = sceneX * TILE_PIXELS;
    int startY = tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < TILE_PIXELS; offsetX++) {
        int maskIndex = offsetY * TILE_PIXELS + offsetX;
        boolean useOverlay = shapeMask[rotationMap[maskIndex]] != 0;
        int pixelRgb = useOverlay ? fillOverlayRgb : fillUnderlayRgb;
        setPixel(pixelWidth, pixelHeight, pixels, startX + offsetX, startY + offsetY, pixelRgb);
      }
    }
  }

  private void rasterizeObject(
      int tileWidth,
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (isWall(sceneObject.type())) {
      rasterizeWall(tileWidth, tileHeight, pixelWidth, pixelHeight, pixels, sceneObject, wallColor(sceneObject));
      return;
    }
    if (sceneObject.type() == 22) {
      fillTileInset(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject.localX(), sceneObject.localY(), 1, DECORATION_COLOR_RGB);
      return;
    }
    int color = areaObjectColor(sceneObject);
    for (int x = 0; x < Math.max(1, sceneObject.sizeX()); x++) {
      for (int y = 0; y < Math.max(1, sceneObject.sizeY()); y++) {
        fillTileInset(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject.localX() + x, sceneObject.localY() + y, 1, color);
      }
    }
  }

  private void rasterizeWall(
      int tileWidth,
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      int rgb
  ) {
    int x = sceneObject.localX();
    int y = sceneObject.localY();
    switch (sceneObject.orientation() & 3) {
      case 0 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x, y, Math.max(1, sceneObject.sizeY()), rgb, 0);
      case 1 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y + Math.max(0, sceneObject.sizeY() - 1), Math.max(1, sceneObject.sizeX()), rgb, TILE_PIXELS - 1);
      case 2 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x + Math.max(0, sceneObject.sizeX() - 1), y, Math.max(1, sceneObject.sizeY()), rgb, TILE_PIXELS - 1);
      case 3 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y, Math.max(1, sceneObject.sizeX()), rgb, 0);
      default -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x, y, 1, rgb, 0);
    }
    if (sceneObject.type() == 2) {
      int secondaryRgb = applyBrightness(rgb, 152);
      switch (sceneObject.orientation() & 3) {
        case 0 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y + Math.max(0, sceneObject.sizeY() - 1), Math.max(1, sceneObject.sizeX()), secondaryRgb, TILE_PIXELS - 1);
        case 1 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x + Math.max(0, sceneObject.sizeX() - 1), y, Math.max(1, sceneObject.sizeY()), secondaryRgb, TILE_PIXELS - 1);
        case 2 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y, Math.max(1, sceneObject.sizeX()), secondaryRgb, 0);
        case 3 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x, y, Math.max(1, sceneObject.sizeY()), secondaryRgb, 0);
        default -> {
        }
      }
    }
  }

  private void drawVertical(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int x, int y, int length, int rgb, int pixelOffset) {
    for (int offset = 0; offset < length; offset++) {
      int pixelX = x * TILE_PIXELS + pixelOffset;
      int startY = tileTopY(tileHeight, y + offset);
      for (int pixel = 0; pixel < TILE_PIXELS; pixel++) {
        setPixel(pixelWidth, pixelHeight, pixels, pixelX, startY + pixel, rgb);
      }
    }
  }

  private void drawHorizontal(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int x, int y, int length, int rgb, int pixelOffset) {
    for (int offset = 0; offset < length; offset++) {
      int pixelY = tileTopY(tileHeight, y) + pixelOffset;
      int startX = (x + offset) * TILE_PIXELS;
      for (int pixel = 0; pixel < TILE_PIXELS; pixel++) {
        setPixel(pixelWidth, pixelHeight, pixels, startX + pixel, pixelY, rgb);
      }
    }
  }

  private void fillTileInset(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int sceneX, int sceneY, int inset, int rgb) {
    int startX = sceneX * TILE_PIXELS + inset;
    int startY = tileTopY(tileHeight, sceneY) + inset;
    int size = Math.max(1, TILE_PIXELS - inset * 2);
    for (int offsetY = 0; offsetY < size; offsetY++) {
      for (int offsetX = 0; offsetX < size; offsetX++) {
        setPixel(pixelWidth, pixelHeight, pixels, startX + offsetX, startY + offsetY, rgb);
      }
    }
  }

  private void fillTile(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int sceneX, int sceneY, int rgb) {
    int startX = sceneX * TILE_PIXELS;
    int startY = tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < TILE_PIXELS; offsetX++) {
        setPixel(pixelWidth, pixelHeight, pixels, startX + offsetX, startY + offsetY, rgb);
      }
    }
  }

  private int tileTopY(int tileHeight, int sceneY) {
    return (tileHeight - 1 - sceneY) * TILE_PIXELS;
  }

  private void setPixel(int pixelWidth, int pixelHeight, int[] pixels, int pixelX, int pixelY, int rgb) {
    if (pixelX < 0 || pixelY < 0 || pixelX >= pixelWidth || pixelY >= pixelHeight) {
      return;
    }
    pixels[pixelY * pixelWidth + pixelX] = 0xff000000 | rgb;
  }

  private boolean isWall(int objectType) {
    return objectType >= 0 && objectType <= 3;
  }

  private int wallColor(WorldSceneObject sceneObject) {
    String lowercaseName = sceneObject.name().toLowerCase();
    if (lowercaseName.contains("fence") || lowercaseName.contains("gate") || lowercaseName.contains("door")) {
      return OBJECT_COLOR_RGB;
    }
    return WALL_COLOR_RGB;
  }

  private int areaObjectColor(WorldSceneObject sceneObject) {
    String lowercaseName = sceneObject.name().toLowerCase();
    if (lowercaseName.contains("tree") || lowercaseName.contains("bush")) {
      return TREE_COLOR_RGB;
    }
    if (lowercaseName.contains("rock") || lowercaseName.contains("stone") || lowercaseName.contains("altar")) {
      return ROCK_COLOR_RGB;
    }
    if (lowercaseName.contains("water")) {
      return WATER_COLOR_RGB;
    }
    return OBJECT_COLOR_RGB;
  }

  private int minimapColor(int primaryRgb, int secondaryRgb, int tertiaryRgb) {
    if (primaryRgb != 0) {
      return applyBrightness(primaryRgb, 118);
    }
    if (secondaryRgb != 0) {
      return applyBrightness(secondaryRgb, 118);
    }
    if (tertiaryRgb != 0) {
      return applyBrightness(tertiaryRgb, 118);
    }
    return applyBrightness(0x2f3946, 118);
  }

  private int applyBrightness(int rgb, int brightness) {
    float factor = brightness / 128.0f;
    int red = clamp(Math.round(((rgb >>> 16) & 0xff) * factor), 0, 255);
    int green = clamp(Math.round(((rgb >>> 8) & 0xff) * factor), 0, 255);
    int blue = clamp(Math.round((rgb & 0xff) * factor), 0, 255);
    return (red << 16) | (green << 8) | blue;
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
