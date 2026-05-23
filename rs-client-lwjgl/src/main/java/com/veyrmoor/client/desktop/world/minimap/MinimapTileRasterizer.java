package com.veyrmoor.client.desktop.world.minimap;

import com.veyrmoor.client.desktop.world.terrain.TerrainLayerSource;
import com.veyrmoor.client.desktop.world.terrain.TerrainOverlayShapeResolver;
import com.veyrmoor.client.desktop.world.terrain.TerrainSceneTileForm;
import com.veyrmoor.client.desktop.world.terrain.TerrainSceneTileFormResolver;
import com.veyrmoor.client.desktop.world.terrain.TerrainTileColorResolver;

/**
 * Rasterizes a single terrain tile (paint or shaped) onto the minimap pixel buffer. Owns the
 * tile-form decision (paint vs model), the shape-mask + rotation lookup, and the underlay/
 * overlay colour selection used at the per-pixel level. Wall, mapscene, and object rendering all
 * live in their own classes.
 */
final class MinimapTileRasterizer {

  private static final int WATER_TEXTURE_ID = 1;
  private static final int MINIMAP_WATER_RGB = 0x5a7ea3;
  private static final boolean ENABLE_TEXTURED_TERRAIN = true;

  private MinimapTileRasterizer() {
  }

  static void rasterizeTile(
      TerrainLayerSource terrainLayerSource,
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int sceneX,
      int sceneY
  ) {
    int overlayRotation = terrainLayerSource.overlayRotationAt(sceneX, sceneY) & 0x03;
    TerrainSceneTileForm tileForm = TerrainSceneTileFormResolver.resolve(
        terrainLayerSource,
        sceneX,
        sceneY,
        ENABLE_TEXTURED_TERRAIN
    );
    if (tileForm == TerrainSceneTileForm.NONE) {
      return;
    }
    if (tileForm.usesPaintTile()) {
      int paintRgb = activePaintColor(terrainLayerSource, sceneX, sceneY);
      fillSolidTile(tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY, paintRgb);
      return;
    }
    int sceneShape = TerrainOverlayShapeResolver.sceneShapeId(terrainLayerSource, sceneX, sceneY);
    if (sceneShape >= MinimapTileShapes.TILE_SHAPE_MASKS.length) {
      return;
    }
    int tileRgb = terrainLayerSource.tileColorAt(sceneX, sceneY);
    int underlayRgb = terrainLayerSource.underlayColorAt(sceneX, sceneY);
    int overlayRgb = terrainLayerSource.overlayColorAt(sceneX, sceneY);
    int underlayTextureId = terrainLayerSource.underlayTextureIdAt(sceneX, sceneY);
    int overlayTextureId = terrainLayerSource.overlayTextureIdAt(sceneX, sceneY);
    int underlayColor = shapedUnderlayColor(terrainLayerSource, sceneX, sceneY, underlayRgb, underlayTextureId);
    int overlayColor = shapedOverlayColor(terrainLayerSource, sceneX, sceneY, tileRgb, overlayRgb, overlayTextureId);
    int[] shapeMask = MinimapTileShapes.TILE_SHAPE_MASKS[sceneShape];
    int[] rotationMap = MinimapTileShapes.TILE_ROTATION_MAP[overlayRotation];
    int startX = sceneX * MinimapPixels.TILE_PIXELS;
    int startY = MinimapPixels.tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < MinimapPixels.TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < MinimapPixels.TILE_PIXELS; offsetX++) {
        int maskIndex = offsetY * MinimapPixels.TILE_PIXELS + offsetX;
        boolean useOverlay = shapeMask[rotationMap[maskIndex]] != 0;
        int pixelRgb = useOverlay ? overlayColor : underlayColor;
        if (pixelRgb == 0) {
          continue;
        }
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, startX + offsetX, startY + offsetY, pixelRgb);
      }
    }
  }

  private static void fillSolidTile(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int sceneX,
      int sceneY,
      int rgb
  ) {
    int startX = sceneX * MinimapPixels.TILE_PIXELS;
    int startY = MinimapPixels.tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < MinimapPixels.TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < MinimapPixels.TILE_PIXELS; offsetX++) {
        MinimapPixels.setPixel(pixelWidth, pixelHeight, pixels, startX + offsetX, startY + offsetY, rgb);
      }
    }
  }

  private static int activePaintColor(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return terrainLayerSource.tileColorAt(tileX, tileY);
  }

  private static int shapedUnderlayColor(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      int underlayRgb,
      int underlayTextureId
  ) {
    if (!TerrainTileColorResolver.hasRenderableUnderlaySurface(terrainLayerSource, tileX, tileY)) {
      return 0;
    }
    if (isWaterTexture(underlayTextureId)) {
      return MINIMAP_WATER_RGB;
    }
    // Legacy SceneGraph.method309 leaves underlay pixels untouched when SceneTileModel.anInt686 == 0
    // (the lit underlay HSL produced no colour). Native blendedUnderlayColor returns 0 for the same
    // condition, so propagate that as "skip the underlay half" instead of substituting tileRgb,
    // which would fill the whole 4x4 with the overlay colour.
    return underlayRgb;
  }

  private static int shapedOverlayColor(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      int tileRgb,
      int overlayRgb,
      int overlayTextureId
  ) {
    if (!TerrainTileColorResolver.hasRenderableOverlaySurface(terrainLayerSource, tileX, tileY)) {
      return 0;
    }
    return tileRgb;
  }

  private static boolean isWaterTexture(int textureId) {
    return textureId == WATER_TEXTURE_ID;
  }
}
