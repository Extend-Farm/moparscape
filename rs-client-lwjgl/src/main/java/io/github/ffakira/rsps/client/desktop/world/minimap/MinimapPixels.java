package io.github.ffakira.rsps.client.desktop.world.minimap;

/**
 * Shared pixel-buffer helpers for the minimap rasterization stack. Each minimap-tile is 4x4
 * pixels; tile-y is inverted to image-y because scene Y grows north while image Y grows down.
 */
final class MinimapPixels {

  static final int TILE_PIXELS = 4;

  private MinimapPixels() {
  }

  static void setPixel(int pixelWidth, int pixelHeight, int[] pixels, int pixelX, int pixelY, int rgb) {
    if (pixelX < 0 || pixelY < 0 || pixelX >= pixelWidth || pixelY >= pixelHeight) {
      return;
    }
    pixels[pixelY * pixelWidth + pixelX] = 0xff000000 | rgb;
  }

  static int tileTopY(int tileHeight, int sceneY) {
    return (tileHeight - 1 - sceneY) * TILE_PIXELS;
  }

  static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
