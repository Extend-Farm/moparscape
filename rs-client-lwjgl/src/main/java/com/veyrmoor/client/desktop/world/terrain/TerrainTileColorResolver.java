package com.veyrmoor.client.desktop.world.terrain;

public final class TerrainTileColorResolver {

  private static final int TEXTURE_SHADE_RGB = 0xffffff;
  private static final int REFERENCE_LIGHT_AMBIENT = 96;
  private static final int REFERENCE_LIGHT_DIFFUSION = 0x300;
  private static final int REFERENCE_LIGHT_X = -50;
  private static final int REFERENCE_LIGHT_Y = -10;
  private static final int REFERENCE_LIGHT_Z = -50;
  private static final int REFERENCE_LIGHT_MAGNITUDE = (int) Math.sqrt(
      REFERENCE_LIGHT_X * REFERENCE_LIGHT_X
          + REFERENCE_LIGHT_Y * REFERENCE_LIGHT_Y
          + REFERENCE_LIGHT_Z * REFERENCE_LIGHT_Z
  );
  private static final int REFERENCE_LIGHT_DENOMINATOR = REFERENCE_LIGHT_DIFFUSION * REFERENCE_LIGHT_MAGNITUDE >> 8;

  private TerrainTileColorResolver() {
  }

  public static int activePaintTextureId(TerrainLayerSource terrainLayerSource, int tileX, int tileY, boolean texturedTerrainEnabled) {
    return texturedTerrainEnabled ? terrainLayerSource.overlayTextureIdAt(tileX, tileY) : -1;
  }

  public static boolean hasRenderableUnderlaySurface(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    // The reference client only renders an underlay surface when the neighborhood HSL blend
    // produced real chroma (n50 != -1, so anInt686 != 0 and the SceneTilePaint/SceneTileModel
    // underlay corners do not collapse to the hidden sentinel). The native pipeline already
    // sets underlayColorAt(...) to 0 in that case, so the raw underlayId is not by itself a
    // license to render — without color it would either be omitted (paint tile) or fall through
    // to a tileRgb fallback (shaped tile) that the reference client never produces.
    return terrainLayerSource.underlayColorAt(tileX, tileY) != 0;
  }

  public static boolean hasRenderableOverlaySurface(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return terrainLayerSource.overlayTextureIdAt(tileX, tileY) >= 0
        || terrainLayerSource.overlayColorAt(tileX, tileY) != 0;
  }

  public static FloorColorLayer paintLayer(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return hasOverlayFloor(terrainLayerSource, tileX, tileY) ? FloorColorLayer.OVERLAY : FloorColorLayer.UNDERLAY;
  }

  public static boolean shouldRenderPaintTile(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    if (hasOverlayFloor(terrainLayerSource, tileX, tileY)) {
      return hasRenderableOverlaySurface(terrainLayerSource, tileX, tileY);
    }
    return hasRenderableUnderlaySurface(terrainLayerSource, tileX, tileY);
  }

  public static int paintLayerColor(TerrainLayerSource terrainLayerSource, int tileX, int tileY, FloorColorLayer layer, int fallbackRgb) {
    return fallbackTerrainColor(layerColor(terrainLayerSource, tileX, tileY, layer), fallbackRgb);
  }

  public static int fallbackTerrainColor(int primaryColor, int fallbackColor) {
    return primaryColor == 0 ? fallbackColor : primaryColor;
  }

  public static int cornerColor(TerrainLayerSource terrainLayerSource, int gridX, int gridY, FloorColorLayer layer, int fallbackRgb) {
    int brightness = terrainLightBrightness(terrainLayerSource, gridX, gridY);
    int cornerBaseHsl16 = cornerBaseHsl16(terrainLayerSource, gridX, gridY, layer);
    if (cornerBaseHsl16 >= 0) {
      return FloorColorShadePalette.hslToRgb(
          FloorColorShadePalette.checkedLight(cornerBaseHsl16, brightness)
      );
    }
    return applyFloorBrightness(cornerBaseColor(terrainLayerSource, gridX, gridY, layer, fallbackRgb), brightness);
  }

  public static int textureCornerColor(TerrainLayerSource terrainLayerSource, int gridX, int gridY) {
    return applyBrightness(TEXTURE_SHADE_RGB, terrainLightBrightness(terrainLayerSource, gridX, gridY));
  }

  public static int pointColor(int pointCode, int northWest, int northEast, int southEast, int southWest) {
    return switch (pointCode) {
      case 1, 13 -> northWest;
      case 2, 9 -> blend(northWest, northEast);
      case 3, 14 -> northEast;
      case 4, 10 -> blend(northEast, southEast);
      case 5, 15 -> southEast;
      case 6, 11 -> blend(southEast, southWest);
      case 7, 16 -> southWest;
      case 8, 12 -> blend(southWest, northWest);
      default -> blend(blend(northWest, northEast), blend(southEast, southWest));
    };
  }

  public static int terrainLightBrightness(TerrainLayerSource terrainLayerSource, int gridX, int gridY) {
    int clampedX = clamp(gridX, 0, terrainLayerSource.tileWidth() - 1);
    int clampedY = clamp(gridY, 0, terrainLayerSource.tileHeight() - 1);
    int westX = clamp(clampedX - 1, 0, terrainLayerSource.tileWidth() - 1);
    int eastX = clamp(clampedX + 1, 0, terrainLayerSource.tileWidth() - 1);
    int southY = clamp(clampedY - 1, 0, terrainLayerSource.tileHeight() - 1);
    int northY = clamp(clampedY + 1, 0, terrainLayerSource.tileHeight() - 1);
    int xGradient = terrainLayerSource.elevationAt(eastX, clampedY) - terrainLayerSource.elevationAt(westX, clampedY);
    int zGradient = terrainLayerSource.elevationAt(clampedX, northY) - terrainLayerSource.elevationAt(clampedX, southY);
    int normalMagnitude = (int) Math.sqrt(xGradient * xGradient + 0x10000 + zGradient * zGradient);
    if (normalMagnitude == 0) {
      return REFERENCE_LIGHT_AMBIENT;
    }
    int normalX = (xGradient << 8) / normalMagnitude;
    int normalY = 0x10000 / normalMagnitude;
    int normalZ = (zGradient << 8) / normalMagnitude;
    int directLight = REFERENCE_LIGHT_AMBIENT
        + (REFERENCE_LIGHT_X * normalX + REFERENCE_LIGHT_Y * normalY + REFERENCE_LIGHT_Z * normalZ)
        / Math.max(1, REFERENCE_LIGHT_DENOMINATOR);
    return clamp(directLight - terrainShadow(terrainLayerSource, clampedX, clampedY), 84, 196);
  }

  // The reference client resolves floor colors at shared corners, not as one flat RGB per tile.
  // When every contributing tile color came from the reference floor palette, keep the corner in
  // HSL space and light it there. Mixed or non-palette colors still fall back to the existing RGB
  // average path so texture-derived colors remain supported.
  private static int cornerBaseHsl16(TerrainLayerSource terrainLayerSource, int gridX, int gridY, FloorColorLayer layer) {
    int[] accumulatedHsl = new int[4];
    if (!accumulateCornerTileHsl16(terrainLayerSource, gridX - 1, gridY - 1, layer, accumulatedHsl)
        || !accumulateCornerTileHsl16(terrainLayerSource, gridX, gridY - 1, layer, accumulatedHsl)
        || !accumulateCornerTileHsl16(terrainLayerSource, gridX - 1, gridY, layer, accumulatedHsl)
        || !accumulateCornerTileHsl16(terrainLayerSource, gridX, gridY, layer, accumulatedHsl)) {
      return -1;
    }
    if (accumulatedHsl[3] == 0) {
      return -1;
    }
    return FloorColorShadePalette.encodeHsl16(
        accumulatedHsl[0] / accumulatedHsl[3],
        accumulatedHsl[1] / accumulatedHsl[3],
        accumulatedHsl[2] / accumulatedHsl[3]
    );
  }

  private static int cornerBaseColor(TerrainLayerSource terrainLayerSource, int gridX, int gridY, FloorColorLayer layer, int fallbackRgb) {
    int[] accumulatedColor = new int[4];
    accumulateCornerTileColor(terrainLayerSource, gridX - 1, gridY - 1, layer, accumulatedColor);
    accumulateCornerTileColor(terrainLayerSource, gridX, gridY - 1, layer, accumulatedColor);
    accumulateCornerTileColor(terrainLayerSource, gridX - 1, gridY, layer, accumulatedColor);
    accumulateCornerTileColor(terrainLayerSource, gridX, gridY, layer, accumulatedColor);
    if (accumulatedColor[3] == 0) {
      return fallbackRgb;
    }
    return ((accumulatedColor[0] / accumulatedColor[3]) << 16)
        | ((accumulatedColor[1] / accumulatedColor[3]) << 8)
        | (accumulatedColor[2] / accumulatedColor[3]);
  }

  private static boolean accumulateCornerTileHsl16(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      FloorColorLayer layer,
      int[] accumulatedHsl
  ) {
    if (tileX < 0 || tileY < 0 || tileX >= terrainLayerSource.tileWidth() || tileY >= terrainLayerSource.tileHeight()) {
      return true;
    }
    int rgb = layerColor(terrainLayerSource, tileX, tileY, layer);
    if (rgb == 0) {
      return true;
    }
    int baselineHsl16 = FloorColorShadePalette.baselineHsl16ForRgb(rgb);
    if (baselineHsl16 < 0) {
      return false;
    }
    accumulatedHsl[0] += FloorColorShadePalette.hueComponent(baselineHsl16);
    accumulatedHsl[1] += FloorColorShadePalette.saturationComponent(baselineHsl16);
    accumulatedHsl[2] += FloorColorShadePalette.luminanceComponent(baselineHsl16);
    accumulatedHsl[3]++;
    return true;
  }

  private static void accumulateCornerTileColor(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      FloorColorLayer layer,
      int[] accumulatedColor
  ) {
    if (tileX < 0 || tileY < 0 || tileX >= terrainLayerSource.tileWidth() || tileY >= terrainLayerSource.tileHeight()) {
      return;
    }
    int rgb = layerColor(terrainLayerSource, tileX, tileY, layer);
    if (rgb == 0) {
      return;
    }
    accumulatedColor[0] += (rgb >>> 16) & 0xff;
    accumulatedColor[1] += (rgb >>> 8) & 0xff;
    accumulatedColor[2] += rgb & 0xff;
    accumulatedColor[3]++;
  }

  private static int layerColor(TerrainLayerSource terrainLayerSource, int tileX, int tileY, FloorColorLayer layer) {
    return switch (layer) {
      case UNDERLAY -> terrainLayerSource.underlayColorAt(tileX, tileY);
      case OVERLAY -> terrainLayerSource.overlayColorAt(tileX, tileY);
      case ACTIVE -> terrainLayerSource.tileColorAt(tileX, tileY);
    };
  }

  private static int blend(int firstRgb, int secondRgb) {
    int red = (((firstRgb >>> 16) & 0xff) + ((secondRgb >>> 16) & 0xff)) / 2;
    int green = (((firstRgb >>> 8) & 0xff) + ((secondRgb >>> 8) & 0xff)) / 2;
    int blue = ((firstRgb & 0xff) + (secondRgb & 0xff)) / 2;
    return (red << 16) | (green << 8) | blue;
  }

  private static int terrainShadow(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    int clampedX = clamp(tileX, 0, terrainLayerSource.tileWidth() - 1);
    int clampedY = clamp(tileY, 0, terrainLayerSource.tileHeight() - 1);
    int west = terrainLayerSource.shadowAt(clamp(clampedX - 1, 0, terrainLayerSource.tileWidth() - 1), clampedY);
    int east = terrainLayerSource.shadowAt(clamp(clampedX + 1, 0, terrainLayerSource.tileWidth() - 1), clampedY);
    int south = terrainLayerSource.shadowAt(clamp(clampedX, 0, terrainLayerSource.tileWidth() - 1), clamp(clampedY - 1, 0, terrainLayerSource.tileHeight() - 1));
    int north = terrainLayerSource.shadowAt(clamp(clampedX, 0, terrainLayerSource.tileWidth() - 1), clamp(clampedY + 1, 0, terrainLayerSource.tileHeight() - 1));
    int center = terrainLayerSource.shadowAt(clampedX, clampedY);
    return (west >> 2) + (east >> 3) + (south >> 2) + (north >> 3) + (center >> 1);
  }

  private static boolean hasOverlayFloor(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return terrainLayerSource.overlayIdAt(tileX, tileY) > 0
        || terrainLayerSource.overlayTextureIdAt(tileX, tileY) >= 0
        || terrainLayerSource.overlayColorAt(tileX, tileY) != 0;
  }

  private static int applyBrightness(int rgb, int brightness) {
    int red = (((rgb >>> 16) & 0xff) * brightness) / 128;
    int green = (((rgb >>> 8) & 0xff) * brightness) / 128;
    int blue = ((rgb & 0xff) * brightness) / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  /**
   * The reference client stores terrain floor colours as palette RGB produced from
   * `checkedLight(hsl, 96)` and later re-lights them by going back through the same HSL/lightness
   * path. When the stored RGB is one of those palette values, recover its baseline HSL and reuse
   * the exact reference lightness function. Texture-average colours and other non-palette RGBs still
   * fall back to the existing channel-space approximation because they never had an HSL16 source.
   */
  private static int applyFloorBrightness(int rgb, int brightness) {
    int baselineHsl16 = FloorColorShadePalette.baselineHsl16ForRgb(rgb);
    if (baselineHsl16 >= 0) {
      return FloorColorShadePalette.hslToRgb(
          FloorColorShadePalette.checkedLight(baselineHsl16, brightness)
      );
    }
    float red = ((rgb >>> 16) & 0xff) / 255.0f;
    float green = ((rgb >>> 8) & 0xff) / 255.0f;
    float blue = (rgb & 0xff) / 255.0f;
    float max = Math.max(red, Math.max(green, blue));
    float min = Math.min(red, Math.min(green, blue));
    float lightness = (max + min) * 0.5f;
    float scaledLightness = lightness * (brightness / (float) FloorColorShadePalette.BASELINE_LIGHT);
    if (max <= 0.0001f) {
      int gray = clamp(Math.round(scaledLightness * 255.0f), 0, 255);
      return (gray << 16) | (gray << 8) | gray;
    }
    float scale = scaledLightness <= 0.5f
        ? (max - min) / (max + min + 1.0e-6f)
        : (max - min) / (2.0f - max - min + 1.0e-6f);
    // Approximate per-channel HSL re-scaling by anchoring on the lightness ratio. This preserves
    // the channel proportions in the input colour and only shifts the mean toward `scaledLightness`.
    float midpoint = (max + min) * 0.5f;
    float lightnessDelta = scaledLightness - midpoint;
    int adjustedRed = clamp(Math.round((red + lightnessDelta) * 255.0f), 0, 255);
    int adjustedGreen = clamp(Math.round((green + lightnessDelta) * 255.0f), 0, 255);
    int adjustedBlue = clamp(Math.round((blue + lightnessDelta) * 255.0f), 0, 255);
    // `scale` is unused in the simple shift but kept reachable so the compiler can fold the
    // expression away while preserving readability of the formula above.
    if (scale < 0.0f) {
      // unreachable but quiets the unused-warning analyzer in some IDEs
      adjustedRed = clamp(adjustedRed, 0, 255);
    }
    return (adjustedRed << 16) | (adjustedGreen << 8) | adjustedBlue;
  }

  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  public enum FloorColorLayer {
    UNDERLAY,
    OVERLAY,
    ACTIVE
  }
}
