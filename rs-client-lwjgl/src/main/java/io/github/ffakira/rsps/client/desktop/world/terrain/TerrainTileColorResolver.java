package io.github.ffakira.rsps.client.desktop.world.terrain;

public final class TerrainTileColorResolver {

  private static final int TEXTURE_SHADE_RGB = 0xffffff;
  private static final int LEGACY_FLOOR_RGB_BASELINE_LIGHT = 96;
  private static final int LEGACY_LIGHT_AMBIENT = 96;
  private static final int LEGACY_LIGHT_DIFFUSION = 0x300;
  private static final int LEGACY_LIGHT_X = -50;
  private static final int LEGACY_LIGHT_Y = -10;
  private static final int LEGACY_LIGHT_Z = -50;
  private static final int LEGACY_LIGHT_MAGNITUDE = (int) Math.sqrt(
      LEGACY_LIGHT_X * LEGACY_LIGHT_X
          + LEGACY_LIGHT_Y * LEGACY_LIGHT_Y
          + LEGACY_LIGHT_Z * LEGACY_LIGHT_Z
  );
  private static final int LEGACY_LIGHT_DENOMINATOR = LEGACY_LIGHT_DIFFUSION * LEGACY_LIGHT_MAGNITUDE >> 8;

  private TerrainTileColorResolver() {
  }

  public static int activePaintTextureId(TerrainLayerSource terrainLayerSource, int tileX, int tileY, boolean texturedTerrainEnabled) {
    return texturedTerrainEnabled ? terrainLayerSource.overlayTextureIdAt(tileX, tileY) : -1;
  }

  public static FloorColorLayer paintLayer(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return terrainLayerSource.overlayColorAt(tileX, tileY) != 0 ? FloorColorLayer.OVERLAY : FloorColorLayer.UNDERLAY;
  }

  public static int paintLayerColor(TerrainLayerSource terrainLayerSource, int tileX, int tileY, FloorColorLayer layer, int fallbackRgb) {
    return fallbackTerrainColor(layerColor(terrainLayerSource, tileX, tileY, layer), fallbackRgb);
  }

  public static int fallbackTerrainColor(int primaryColor, int fallbackColor) {
    return primaryColor == 0 ? fallbackColor : primaryColor;
  }

  public static int cornerColor(TerrainLayerSource terrainLayerSource, int gridX, int gridY, FloorColorLayer layer, int fallbackRgb) {
    int brightness = terrainLightBrightness(terrainLayerSource, gridX, gridY);
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
      return LEGACY_LIGHT_AMBIENT;
    }
    int normalX = (xGradient << 8) / normalMagnitude;
    int normalY = 0x10000 / normalMagnitude;
    int normalZ = (zGradient << 8) / normalMagnitude;
    int directLight = LEGACY_LIGHT_AMBIENT
        + (LEGACY_LIGHT_X * normalX + LEGACY_LIGHT_Y * normalY + LEGACY_LIGHT_Z * normalZ)
        / Math.max(1, LEGACY_LIGHT_DENOMINATOR);
    return clamp(directLight - terrainShadow(terrainLayerSource, clampedX, clampedY), 84, 196);
  }

  // Legacy terrain colors are resolved at shared corners, not as one flat RGB per tile. The
  // native scene only carries per-tile colors today, so average the adjacent tiles that touch a
  // corner before applying the slope light. That keeps untextured floors from reading like solid
  // color slabs in places where the cache has no floor texture ids at all.
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

  private static int applyBrightness(int rgb, int brightness) {
    int red = (((rgb >>> 16) & 0xff) * brightness) / 128;
    int green = (((rgb >>> 8) & 0xff) * brightness) / 128;
    int blue = ((rgb & 0xff) * brightness) / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  private static int applyFloorBrightness(int rgb, int brightness) {
    int red = (((rgb >>> 16) & 0xff) * brightness) / LEGACY_FLOOR_RGB_BASELINE_LIGHT;
    int green = (((rgb >>> 8) & 0xff) * brightness) / LEGACY_FLOOR_RGB_BASELINE_LIGHT;
    int blue = ((rgb & 0xff) * brightness) / LEGACY_FLOOR_RGB_BASELINE_LIGHT;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
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
