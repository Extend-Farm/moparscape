package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainTileColorResolver;
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
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      List<WorldSceneObject> sceneObjects
  ) {
    TerrainLayerSource terrainLayerSource = new MinimapTerrainLayerSource(
        tileWidth,
        tileHeight,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations
    );
    int pixelWidth = tileWidth * TILE_PIXELS;
    int pixelHeight = tileHeight * TILE_PIXELS;
    int[] pixels = new int[pixelWidth * pixelHeight];
    for (int sceneY = 0; sceneY < tileHeight; sceneY++) {
      for (int sceneX = 0; sceneX < tileWidth; sceneX++) {
        int sourceIndex = sceneY * tileWidth + sceneX;
        rasterizeTile(
            terrainLayerSource,
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
      TerrainLayerSource terrainLayerSource,
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
    TileCornerColors paintColors = activePaintCornerColors(
        terrainLayerSource,
        sceneX,
        sceneY,
        tileRgb,
        underlayRgb,
        overlayRgb,
        underlayTextureId,
        overlayTextureId
    );
    if (overlayShape <= 1 || overlayShape >= TILE_SHAPE_MASKS.length) {
      fillGradientTile(tileHeight, pixelWidth, pixelHeight, pixels, sceneX, sceneY, paintColors);
      return;
    }
    TileCornerColors underlayCornerColors = layerCornerColors(
        terrainLayerSource,
        sceneX,
        sceneY,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        minimapBaseColor(underlayRgb, tileRgb, overlayRgb, underlayTextureId, -1)
    );
    TileCornerColors overlayCornerColors = layerCornerColors(
        terrainLayerSource,
        sceneX,
        sceneY,
        TerrainTileColorResolver.FloorColorLayer.OVERLAY,
        minimapBaseColor(overlayRgb, tileRgb, underlayRgb, -1, overlayTextureId)
    );
    int[] shapeMask = TILE_SHAPE_MASKS[overlayShape];
    int[] rotationMap = TILE_ROTATION_MAP[overlayRotation];
    int startX = sceneX * TILE_PIXELS;
    int startY = tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < TILE_PIXELS; offsetX++) {
        int maskIndex = offsetY * TILE_PIXELS + offsetX;
        boolean useOverlay = shapeMask[rotationMap[maskIndex]] != 0;
        int pixelRgb = sampleTileGradient(
            useOverlay ? overlayCornerColors : underlayCornerColors,
            offsetX,
            offsetY
        );
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

  private void fillGradientTile(
      int tileHeight,
      int pixelWidth,
      int pixelHeight,
      int[] pixels,
      int sceneX,
      int sceneY,
      TileCornerColors colors
  ) {
    int startX = sceneX * TILE_PIXELS;
    int startY = tileTopY(tileHeight, sceneY);
    for (int offsetY = 0; offsetY < TILE_PIXELS; offsetY++) {
      for (int offsetX = 0; offsetX < TILE_PIXELS; offsetX++) {
        setPixel(
            pixelWidth,
            pixelHeight,
            pixels,
            startX + offsetX,
            startY + offsetY,
            sampleTileGradient(colors, offsetX, offsetY)
        );
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

  private TileCornerColors activePaintCornerColors(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      int tileRgb,
      int underlayRgb,
      int overlayRgb,
      int underlayTextureId,
      int overlayTextureId
  ) {
    if (isWaterTexture(underlayTextureId) || isWaterTexture(overlayTextureId)) {
      return shadedBaseCornerColors(terrainLayerSource, tileX, tileY, MINIMAP_WATER_RGB);
    }
    TerrainTileColorResolver.FloorColorLayer paintLayer = TerrainTileColorResolver.paintLayer(
        terrainLayerSource,
        tileX,
        tileY
    );
    int fallbackRgb = paintLayer == TerrainTileColorResolver.FloorColorLayer.OVERLAY
        ? minimapBaseColor(overlayRgb, tileRgb, underlayRgb, -1, overlayTextureId)
        : minimapBaseColor(underlayRgb, tileRgb, overlayRgb, underlayTextureId, overlayTextureId);
    return layerCornerColors(terrainLayerSource, tileX, tileY, paintLayer, fallbackRgb);
  }

  private TileCornerColors layerCornerColors(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      TerrainTileColorResolver.FloorColorLayer layer,
      int fallbackRgb
  ) {
    if (fallbackRgb == MINIMAP_WATER_RGB) {
      return shadedBaseCornerColors(terrainLayerSource, tileX, tileY, fallbackRgb);
    }
    return new TileCornerColors(
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX, tileY, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX + 1, tileY, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX + 1, tileY + 1, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX, tileY + 1, layer, fallbackRgb)
    );
  }

  private TileCornerColors shadedBaseCornerColors(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      int baseRgb
  ) {
    return new TileCornerColors(
        applyBrightness(baseRgb, TerrainTileColorResolver.terrainLightBrightness(terrainLayerSource, tileX, tileY)),
        applyBrightness(baseRgb, TerrainTileColorResolver.terrainLightBrightness(terrainLayerSource, tileX + 1, tileY)),
        applyBrightness(baseRgb, TerrainTileColorResolver.terrainLightBrightness(terrainLayerSource, tileX + 1, tileY + 1)),
        applyBrightness(baseRgb, TerrainTileColorResolver.terrainLightBrightness(terrainLayerSource, tileX, tileY + 1))
    );
  }

  private int sampleTileGradient(TileCornerColors colors, int offsetX, int offsetY) {
    float u = (offsetX + 0.5f) / TILE_PIXELS;
    float v = (offsetY + 0.5f) / TILE_PIXELS;
    float topRed = lerp(channel(colors.northWest(), 16), channel(colors.northEast(), 16), u);
    float topGreen = lerp(channel(colors.northWest(), 8), channel(colors.northEast(), 8), u);
    float topBlue = lerp(channel(colors.northWest(), 0), channel(colors.northEast(), 0), u);
    float bottomRed = lerp(channel(colors.southWest(), 16), channel(colors.southEast(), 16), u);
    float bottomGreen = lerp(channel(colors.southWest(), 8), channel(colors.southEast(), 8), u);
    float bottomBlue = lerp(channel(colors.southWest(), 0), channel(colors.southEast(), 0), u);
    int red = clamp(Math.round(lerp(topRed, bottomRed, v)), 0, 255);
    int green = clamp(Math.round(lerp(topGreen, bottomGreen, v)), 0, 255);
    int blue = clamp(Math.round(lerp(topBlue, bottomBlue, v)), 0, 255);
    return (red << 16) | (green << 8) | blue;
  }

  private int minimapBaseColor(
      int primaryRgb,
      int secondaryRgb,
      int tertiaryRgb,
      int primaryTextureId,
      int fallbackTextureId
  ) {
    if (isWaterTexture(primaryTextureId) || isWaterTexture(fallbackTextureId)) {
      return MINIMAP_WATER_RGB;
    }
    if (primaryRgb != 0) {
      return primaryRgb;
    }
    if (secondaryRgb != 0) {
      return secondaryRgb;
    }
    if (tertiaryRgb != 0) {
      return tertiaryRgb;
    }
    return 0x2f3946;
  }

  private boolean isWaterTexture(int textureId) {
    return textureId == WATER_TEXTURE_ID;
  }

  private int channel(int rgb, int shift) {
    return (rgb >>> shift) & 0xff;
  }

  private int applyBrightness(int rgb, int brightness) {
    float factor = brightness / 128.0f;
    int red = clamp(Math.round(((rgb >>> 16) & 0xff) * factor), 0, 255);
    int green = clamp(Math.round(((rgb >>> 8) & 0xff) * factor), 0, 255);
    int blue = clamp(Math.round((rgb & 0xff) * factor), 0, 255);
    return (red << 16) | (green << 8) | blue;
  }

  private float lerp(float start, float end, float amount) {
    return start + (end - start) * amount;
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private record TileCornerColors(int northWest, int northEast, int southEast, int southWest) {
  }

  private record MinimapTerrainLayerSource(
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations
  ) implements TerrainLayerSource {

    @Override
    public int elevationAt(int localX, int localY) {
      return elevations[localY * tileWidth + localX];
    }

    @Override
    public int tileColorAt(int localX, int localY) {
      return tileColors[localY * tileWidth + localX];
    }

    @Override
    public int underlayColorAt(int localX, int localY) {
      return underlayColors[localY * tileWidth + localX];
    }

    @Override
    public int overlayColorAt(int localX, int localY) {
      return overlayColors[localY * tileWidth + localX];
    }

    @Override
    public int underlayTextureIdAt(int localX, int localY) {
      return underlayTextureIds[localY * tileWidth + localX];
    }

    @Override
    public int overlayTextureIdAt(int localX, int localY) {
      return overlayTextureIds[localY * tileWidth + localX];
    }

    @Override
    public int overlayShapeAt(int localX, int localY) {
      return overlayShapes[localY * tileWidth + localX] & 0xff;
    }

    @Override
    public int overlayRotationAt(int localX, int localY) {
      return overlayRotations[localY * tileWidth + localX] & 0xff;
    }
  }
}
