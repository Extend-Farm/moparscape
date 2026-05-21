package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder.IndexedArgbSprite;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import java.util.List;

/**
 * Orchestrates minimap rasterization. The class deliberately stays narrow — terrain tiles,
 * walls, mapscene sprites, and object dispatch all live in their own files
 * ({@link MinimapTileRasterizer}, {@link MinimapWallRasterizer},
 * {@link MinimapMapSceneSpriteRasterizer}, {@link MinimapObjectRasterizer}). This class owns:
 *
 * <ul>
 *   <li>the public {@code rasterize(...)} API surface and its overload chain,</li>
 *   <li>the main per-tile loop including bridge-above overpaint per legacy method24,</li>
 *   <li>delegation order: terrain first, then objects.</li>
 * </ul>
 */
public final class WorldSceneMinimapRasterizer {

  private final MinimapMapSceneSpriteRasterizer mapSceneSpriteRasterizer;
  private final MinimapObjectRasterizer objectRasterizer;

  public WorldSceneMinimapRasterizer() {
    this(new IndexedArgbSprite[0]);
  }

  public WorldSceneMinimapRasterizer(ArgbImage[] mapSceneSprites) {
    this(MinimapMapSceneSpriteRasterizer.convert(mapSceneSprites));
  }

  public WorldSceneMinimapRasterizer(IndexedArgbSprite[] mapSceneSprites) {
    this.mapSceneSpriteRasterizer = new MinimapMapSceneSpriteRasterizer(mapSceneSprites);
    this.objectRasterizer = new MinimapObjectRasterizer(mapSceneSpriteRasterizer);
  }

  public ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] shadowSamples,
      List<WorldSceneObject> sceneObjects
  ) {
    int[] emptyIds = new int[tileWidth * tileHeight];
    return rasterize(
        tileWidth, tileHeight, elevations, emptyIds, emptyIds, tileColors, underlayColors, overlayColors,
        underlayTextureIds, overlayTextureIds, overlayShapes, overlayRotations, shadowSamples, sceneObjects);
  }

  public ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] underlayIds,
      int[] overlayIds,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] shadowSamples,
      List<WorldSceneObject> sceneObjects
  ) {
    return rasterize(
        tileWidth, tileHeight, elevations, underlayIds, overlayIds, tileColors, underlayColors, overlayColors,
        underlayTextureIds, overlayTextureIds, overlayShapes, overlayRotations,
        new byte[tileWidth * tileHeight], new byte[tileWidth * tileHeight],
        BridgeTerrainLayer.empty(tileWidth, tileHeight), shadowSamples, sceneObjects);
  }

  public ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] tileFlags,
      byte[] bridgeAboveFlags,
      BridgeTerrainLayer bridgeTerrainLayer,
      byte[] shadowSamples,
      List<WorldSceneObject> sceneObjects
  ) {
    int[] emptyIds = new int[tileWidth * tileHeight];
    return rasterize(
        tileWidth, tileHeight, elevations, emptyIds, emptyIds, tileColors, underlayColors, overlayColors,
        underlayTextureIds, overlayTextureIds, overlayShapes, overlayRotations,
        tileFlags, bridgeAboveFlags, bridgeTerrainLayer, shadowSamples, sceneObjects);
  }

  public ArgbImage rasterize(
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] underlayIds,
      int[] overlayIds,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] tileFlags,
      byte[] bridgeAboveFlags,
      BridgeTerrainLayer bridgeTerrainLayer,
      byte[] shadowSamples,
      List<WorldSceneObject> sceneObjects
  ) {
    TerrainLayerSource mainTerrainLayerSource = new MinimapTerrainLayerSource(
        tileWidth,
        tileHeight,
        elevations,
        underlayIds,
        overlayIds,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        shadowSamples
    );
    int pixelWidth = tileWidth * MinimapPixels.TILE_PIXELS;
    int pixelHeight = tileHeight * MinimapPixels.TILE_PIXELS;
    int[] pixels = new int[pixelWidth * pixelHeight];
    for (int sceneY = 0; sceneY < tileHeight; sceneY++) {
      for (int sceneX = 0; sceneX < tileWidth; sceneX++) {
        int sourceIndex = sceneY * tileWidth + sceneX;
        if ((tileFlags[sourceIndex] & 0x18) == 0) {
          // The captured main terrain layer already applies the visible-surface rule for
          // bridge-lowered tiles, so the minimap current-plane pass must paint that promoted deck
          // instead of falling back to the stored under-bridge water layer.
          MinimapTileRasterizer.rasterizeTile(mainTerrainLayerSource, tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY);
        }
        if (bridgeAboveFlags[sourceIndex] != 0) {
          // Legacy method24 paints the current plane first and then explicitly overpaints bridge
          // decks from plane + 1 when that upper tile carries flag 8.
          MinimapTileRasterizer.rasterizeTile(mainTerrainLayerSource, tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY);
        }
      }
    }
    // Legacy method50 does three ordered scene queries per tile: boundary object, interactive
    // object, then ground decoration. The dispatch helper preserves that order.
    objectRasterizer.rasterizeAll(tileWidth, tileHeight, pixelWidth, pixelHeight, pixels, sceneObjects);
    return new ArgbImage(pixelWidth, pixelHeight, pixels);
  }
}
