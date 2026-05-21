package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder.IndexedArgbSprite;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;

/**
 * Stamps cache {@code mapscene} sprites onto the minimap for objects that carry a non-negative
 * {@code mapSceneId}. The footprint width/height calculation matches legacy {@code method50}: the
 * raw object-definition dimensions are used so rotation does not mis-centre the sprite against
 * the visible tile span.
 */
final class MinimapMapSceneSpriteRasterizer {

  private final IndexedArgbSprite[] mapSceneSprites;

  MinimapMapSceneSpriteRasterizer(IndexedArgbSprite[] mapSceneSprites) {
    this.mapSceneSprites = mapSceneSprites.clone();
  }

  static IndexedArgbSprite[] convert(ArgbImage[] mapSceneSprites) {
    IndexedArgbSprite[] convertedSprites = new IndexedArgbSprite[mapSceneSprites.length];
    for (int spriteIndex = 0; spriteIndex < mapSceneSprites.length; spriteIndex++) {
      ArgbImage sprite = mapSceneSprites[spriteIndex];
      convertedSprites[spriteIndex] = sprite == null ? null : IndexedArgbSprite.fromArgbImage(sprite);
    }
    return convertedSprites;
  }

  boolean drawIfPresent(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject
  ) {
    if (sceneObject.mapSceneId() < 0 || sceneObject.mapSceneId() >= mapSceneSprites.length) {
      return false;
    }
    IndexedArgbSprite mapSceneSprite = mapSceneSprites[sceneObject.mapSceneId()];
    if (mapSceneSprite == null) {
      return false;
    }
    drawSprite(tileHeight, pixelWidth, pixelHeight, pixels, sceneObject, mapSceneSprite);
    return true;
  }

  private void drawSprite(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      WorldSceneObject sceneObject,
      IndexedArgbSprite sprite
  ) {
    int footprintWidth = footprintWidth(sceneObject) * MinimapPixels.TILE_PIXELS;
    int footprintHeight = footprintHeight(sceneObject) * MinimapPixels.TILE_PIXELS;
    int drawX = sceneObject.localX() * MinimapPixels.TILE_PIXELS + (footprintWidth - sprite.width()) / 2 + sprite.offsetX();
    int drawY = (tileHeight - sceneObject.localY() - footprintHeight(sceneObject)) * MinimapPixels.TILE_PIXELS
        + (footprintHeight - sprite.height()) / 2 + sprite.offsetY();
    int[] spritePixels = sprite.pixels();
    for (int spriteY = 0; spriteY < sprite.height(); spriteY++) {
      for (int spriteX = 0; spriteX < sprite.width(); spriteX++) {
        int argb = spritePixels[spriteY * sprite.width() + spriteX];
        if ((argb >>> 24) == 0) {
          continue;
        }
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, drawX + spriteX, drawY + spriteY, argb & 0x00ffffff);
      }
    }
  }

  private int footprintWidth(WorldSceneObject sceneObject) {
    if ((sceneObject.orientation() & 1) == 1) {
      return Math.max(1, sceneObject.sizeY());
    }
    return Math.max(1, sceneObject.sizeX());
  }

  private int footprintHeight(WorldSceneObject sceneObject) {
    if ((sceneObject.orientation() & 1) == 1) {
      return Math.max(1, sceneObject.sizeX());
    }
    return Math.max(1, sceneObject.sizeY());
  }
}
