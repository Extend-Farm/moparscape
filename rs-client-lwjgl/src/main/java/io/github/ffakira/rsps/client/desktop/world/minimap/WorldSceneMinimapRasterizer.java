package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;

public final class WorldSceneMinimapRasterizer {

  private static final int TILE_PIXELS = 4;
  private static final int WATER_TEXTURE_ID = 1;
  private static final int MINIMAP_WATER_RGB = 0x5a7ea3;
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
  private static final int WALL_COLOR_RGB = 0xeeeeee;
  private static final int OBJECT_COLOR_RGB = 0x5c4a34;
  private static final int DIAGONAL_WALL_COLOR_RGB = 0xeeeeee;
  private final ArgbImage[] mapSceneSprites;

  public WorldSceneMinimapRasterizer() {
    this(new ArgbImage[0]);
  }

  public WorldSceneMinimapRasterizer(ArgbImage[] mapSceneSprites) {
    this.mapSceneSprites = mapSceneSprites.clone();
  }

  public ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
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
            tileHeight,
            pixelWidth,
            pixelHeight,
            pixels,
            sceneX,
            sceneY,
            tileColors[sourceIndex],
            underlayColors[sourceIndex],
            overlayColors[sourceIndex],
            underlayTextureIds[sourceIndex],
            overlayTextureIds[sourceIndex],
            overlayShapes[sourceIndex] & 0xff,
            overlayRotations[sourceIndex] & 0x03
        );
      }
    }
    for (WorldSceneObject sceneObject : sceneObjects) {
      rasterizeObject(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject);
    }
    return new ArgbImage(pixelWidth, pixelHeight, pixels);
  }

  private void rasterizeTile(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int sceneX,
      int sceneY,
      int tileRgb,
      int underlayRgb,
      int overlayRgb,
      int underlayTextureId,
      int overlayTextureId,
      int overlayShape,
      int overlayRotation
  ) {
    int paintRgb = minimapColor(tileRgb, underlayRgb, overlayRgb, underlayTextureId, overlayTextureId);
    if (overlayShape <= 1 || overlayShape >= TILE_SHAPE_MASKS.length) {
      fillTile(tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY, paintRgb);
      return;
    }
    int fillUnderlayRgb = minimapColor(underlayRgb, tileRgb, overlayRgb, underlayTextureId, -1);
    int fillOverlayRgb = minimapColor(overlayRgb, tileRgb, underlayRgb, -1, overlayTextureId);
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
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (sceneObject.mapSceneId() >= 0 && sceneObject.mapSceneId() < mapSceneSprites.length) {
      ArgbImage mapSceneSprite = mapSceneSprites[sceneObject.mapSceneId()];
      if (mapSceneSprite != null) {
        drawMapSceneSprite(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject, mapSceneSprite);
        return;
      }
    }
    if (isWall(sceneObject.type())) {
      rasterizeWall(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject, wallColor(sceneObject));
      return;
    }
    if (sceneObject.type() == 9) {
      rasterizeDiagonalWall(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject, DIAGONAL_WALL_COLOR_RGB);
      return;
    }
  }

  private void rasterizeWall(
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

  private void rasterizeDiagonalWall(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      int rgb
  ) {
    int startX = sceneObject.localX() * TILE_PIXELS;
    int startY = tileTopY(tileHeight, sceneObject.localY());
    if ((sceneObject.orientation() & 3) == 0 || (sceneObject.orientation() & 3) == 2) {
      for (int offset = 0; offset < TILE_PIXELS; offset++) {
        setPixel(pixelWidth, pixelHeight, pixels, startX + (TILE_PIXELS - 1 - offset), startY + offset, rgb);
      }
      return;
    }
    for (int offset = 0; offset < TILE_PIXELS; offset++) {
      setPixel(pixelWidth, pixelHeight, pixels, startX + offset, startY + offset, rgb);
    }
  }

  private void drawMapSceneSprite(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      ArgbImage sprite
  ) {
    int footprintWidth = Math.max(1, sceneObject.sizeX()) * TILE_PIXELS;
    int footprintHeight = Math.max(1, sceneObject.sizeY()) * TILE_PIXELS;
    int drawX = sceneObject.localX() * TILE_PIXELS + (footprintWidth - sprite.width()) / 2;
    int drawY = (tileHeight - sceneObject.localY() - Math.max(1, sceneObject.sizeY())) * TILE_PIXELS
        + (footprintHeight - sprite.height()) / 2;
    int[] spritePixels = sprite.pixels();
    for (int spriteY = 0; spriteY < sprite.height(); spriteY++) {
      for (int spriteX = 0; spriteX < sprite.width(); spriteX++) {
        int argb = spritePixels[spriteY * sprite.width() + spriteX];
        if ((argb >>> 24) == 0) {
          continue;
        }
        setPixel(pixelWidth, pixelHeight, pixels, drawX + spriteX, drawY + spriteY, argb & 0x00ffffff);
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

  private int minimapColor(
      int primaryRgb,
      int secondaryRgb,
      int tertiaryRgb,
      int primaryTextureId,
      int fallbackTextureId
  ) {
    if (isWaterTexture(primaryTextureId) || isWaterTexture(fallbackTextureId)) {
      return applyBrightness(MINIMAP_WATER_RGB, 126);
    }
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

  private boolean isWaterTexture(int textureId) {
    return textureId == WATER_TEXTURE_ID;
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
