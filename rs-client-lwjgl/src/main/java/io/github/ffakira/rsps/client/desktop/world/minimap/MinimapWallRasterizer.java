package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;

/**
 * Draws wall and diagonal-wall pixel marks for boundary/interactive objects. Wall colour is
 * derived from the object name so doors and fences read brown while stone walls stay light grey,
 * mirroring the legacy {@code GameClientCore.method50} behaviour without trying to look up the
 * full ObjectDefinition palette.
 */
final class MinimapWallRasterizer {

  private static final int WALL_COLOR_RGB = 0xeeeeee;
  private static final int INTERACTIVE_WALL_COLOR_RGB = 0xee0000;

  private MinimapWallRasterizer() {
  }

  static int wallColor(WorldSceneObject sceneObject) {
    return sceneObject.interactive() ? INTERACTIVE_WALL_COLOR_RGB : WALL_COLOR_RGB;
  }

  static boolean isWall(int objectType) {
    return objectType == 0 || objectType == 2 || objectType == 3;
  }

  static void rasterizeWall(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      int rgb
  ) {
    int x = sceneObject.localX();
    int y = sceneObject.localY();
    int width = Math.max(1, sceneObject.sizeX());
    int height = Math.max(1, sceneObject.sizeY());
    int orientation = sceneObject.orientation() & 3;
    switch (sceneObject.type()) {
      case 0 -> rasterizeStraightWall(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, height, orientation, rgb);
      case 2 -> rasterizeCornerWall(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, height, orientation, rgb);
      case 3 -> rasterizeCornerCap(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, height, orientation, rgb);
      default -> {
      }
    }
  }

  static void rasterizeDiagonalWall(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      int rgb
  ) {
    int startX = sceneObject.localX() * MinimapPixels.TILE_PIXELS;
    int startY = MinimapPixels.tileTopY(tileHeight, sceneObject.localY());
    if ((sceneObject.orientation() & 3) == 0 || (sceneObject.orientation() & 3) == 2) {
      for (int offset = 0; offset < MinimapPixels.TILE_PIXELS; offset++) {
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, startX + (MinimapPixels.TILE_PIXELS - 1 - offset), startY + offset, rgb);
      }
      return;
    }
    for (int offset = 0; offset < MinimapPixels.TILE_PIXELS; offset++) {
      MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, startX + offset, startY + offset, rgb);
    }
  }

  private static void rasterizeStraightWall(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int x,
      int y,
      int width,
      int height,
      int orientation,
      int rgb
  ) {
    switch (orientation) {
      case 0 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x, y, height, rgb, 0);
      case 1 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, rgb, 0);
      case 2 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x + Math.max(0, width - 1), y, height, rgb, MinimapPixels.TILE_PIXELS - 1);
      case 3 -> drawHorizontal(
          tileHeight,
          pixelWidth,
          pixelHeight,
          pixels,
          x,
          y + Math.max(0, height - 1),
          width,
          rgb,
          MinimapPixels.TILE_PIXELS - 1
      );
      default -> {
      }
    }
  }

  private static void rasterizeCornerWall(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int x,
      int y,
      int width,
      int height,
      int orientation,
      int rgb
  ) {
    rasterizeStraightWall(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, height, orientation, rgb);
    switch (orientation) {
      case 0 -> drawHorizontal(tileHeight, pixelWidth, pixelHeight, pixels, x, y, width, rgb, 0);
      case 1 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x + Math.max(0, width - 1), y, height, rgb, MinimapPixels.TILE_PIXELS - 1);
      case 2 -> drawHorizontal(
          tileHeight,
          pixelWidth,
          pixelHeight,
          pixels,
          x,
          y + Math.max(0, height - 1),
          width,
          rgb,
          MinimapPixels.TILE_PIXELS - 1
      );
      case 3 -> drawVertical(tileHeight, pixelWidth, pixelHeight, pixels, x, y, height, rgb, 0);
      default -> {
      }
    }
  }

  private static void rasterizeCornerCap(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int x,
      int y,
      int width,
      int height,
      int orientation,
      int rgb
  ) {
    int pixelX = x * MinimapPixels.TILE_PIXELS;
    int pixelY = MinimapPixels.tileTopY(tileHeight, y);
    switch (orientation) {
      case 0 -> MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, pixelX, pixelY, rgb);
      case 1 -> MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, pixelX + MinimapPixels.TILE_PIXELS - 1, pixelY, rgb);
      case 2 -> MinimapPixels.setPixel(
          pixelWidth,
          pixelHeight,
          pixels,
          pixelX + width * MinimapPixels.TILE_PIXELS - 1,
          pixelY + height * MinimapPixels.TILE_PIXELS - 1,
          rgb
      );
      case 3 -> MinimapPixels.setPixel(
          pixelWidth,
          pixelHeight,
          pixels,
          pixelX,
          pixelY + height * MinimapPixels.TILE_PIXELS - 1,
          rgb
      );
      default -> {
      }
    }
  }

  private static void drawVertical(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int x, int y, int length, int rgb, int pixelOffset) {
    for (int offset = 0; offset < length; offset++) {
      int pixelX = x * MinimapPixels.TILE_PIXELS + pixelOffset;
      int startY = MinimapPixels.tileTopY(tileHeight, y + offset);
      for (int pixel = 0; pixel < MinimapPixels.TILE_PIXELS; pixel++) {
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, pixelX, startY + pixel, rgb);
      }
    }
  }

  private static void drawHorizontal(int tileHeight, int pixelWidth, int pixelHeight, int[] pixels, int x, int y, int length, int rgb, int pixelOffset) {
    for (int offset = 0; offset < length; offset++) {
      int pixelY = MinimapPixels.tileTopY(tileHeight, y) + pixelOffset;
      int startX = (x + offset) * MinimapPixels.TILE_PIXELS;
      for (int pixel = 0; pixel < MinimapPixels.TILE_PIXELS; pixel++) {
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, startX + pixel, pixelY, rgb);
      }
    }
  }
}
