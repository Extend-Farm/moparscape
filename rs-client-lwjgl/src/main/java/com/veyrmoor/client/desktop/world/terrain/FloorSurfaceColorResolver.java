package com.veyrmoor.client.desktop.world.terrain;

import com.veyrmoor.content.FloorColorCatalog;
import com.veyrmoor.content.FloorColorDefinition;
import java.util.Objects;

public final class FloorSurfaceColorResolver {

  private static final int DEFAULT_TILE_RGB = 0x2f3946;
  private static final int WATER_TEXTURE_ID = 1;
  private static final int WATER_SURFACE_RGB = 0x5a7ea3;
  private static final int UNDERLAY_BLEND_RADIUS = 5;
  private static final double LEGACY_RASTER_BRIGHTNESS = 0.8D;
  private static final double LEGACY_TEXTURE_AVERAGE_BRIGHTNESS = 1.4D;
  private static final int[] LEGACY_HSL_TO_RGB = buildLegacyHslToRgb();

  private final FloorColorCatalog floorColors;
  private final int[] legacyTextureAverageColors;

  public FloorSurfaceColorResolver(FloorColorCatalog floorColors) {
    this(floorColors, new int[0]);
  }

  public FloorSurfaceColorResolver(FloorColorCatalog floorColors, int[] legacyTextureAverageColors) {
    this.floorColors = Objects.requireNonNull(floorColors, "floorColors");
    this.legacyTextureAverageColors = legacyTextureAverageColors.clone();
  }

  public void resolveSceneColors(
      int tileWidth,
      int tileHeight,
      int[] underlayIds,
      int[] overlayIds,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      int[] tileColors
  ) {
    resolveUnderlayColors(tileWidth, tileHeight, underlayIds, underlayColors);
    for (int index = 0; index < tileWidth * tileHeight; index++) {
      FloorColorDefinition overlayDefinition = floorColors.definitionFor(overlayIds[index]);
      overlayTextureIds[index] = normalizedOverlayTextureId(overlayDefinition);
      // Legacy terrain keeps two overlay color products: the visible overlay surface color used by
      // SceneTilePaint/SceneTileModel corners, and the tile-level base color used for minimap/base
      // paint selection. Textured and opcode-7 overlays diverge here, so keep them separate.
      overlayColors[index] = overlaySurfaceColor(overlayDefinition);
      underlayTextureIds[index] = floorColors.definitionFor(underlayIds[index]).textureId();
      tileColors[index] = activeTileColor(
          underlayColors[index],
          overlayBaseColor(overlayDefinition, overlayTextureIds[index])
      );
    }
  }

  private void resolveUnderlayColors(int tileWidth, int tileHeight, int[] underlayIds, int[] underlayColors) {
    int[] blendHuesByRow = new int[tileHeight];
    int[] saturationsByRow = new int[tileHeight];
    int[] luminancesByRow = new int[tileHeight];
    int[] chromasByRow = new int[tileHeight];
    int[] countsByRow = new int[tileHeight];

    for (int tileX = -UNDERLAY_BLEND_RADIUS; tileX < tileWidth + UNDERLAY_BLEND_RADIUS; tileX++) {
      for (int tileY = 0; tileY < tileHeight; tileY++) {
        addUnderlayContribution(tileWidth, tileHeight, underlayIds, tileX + UNDERLAY_BLEND_RADIUS, tileY, 1,
            blendHuesByRow, saturationsByRow, luminancesByRow, chromasByRow, countsByRow);
        addUnderlayContribution(tileWidth, tileHeight, underlayIds, tileX - UNDERLAY_BLEND_RADIUS, tileY, -1,
            blendHuesByRow, saturationsByRow, luminancesByRow, chromasByRow, countsByRow);
      }

      if (tileX < 0 || tileX >= tileWidth) {
        continue;
      }

      int hueSum = 0;
      int saturationSum = 0;
      int luminanceSum = 0;
      int chromaSum = 0;
      int countSum = 0;
      for (int tileY = -UNDERLAY_BLEND_RADIUS; tileY < tileHeight + UNDERLAY_BLEND_RADIUS; tileY++) {
        int addRow = tileY + UNDERLAY_BLEND_RADIUS;
        if (addRow >= 0 && addRow < tileHeight) {
          hueSum += blendHuesByRow[addRow];
          saturationSum += saturationsByRow[addRow];
          luminanceSum += luminancesByRow[addRow];
          chromaSum += chromasByRow[addRow];
          countSum += countsByRow[addRow];
        }
        int removeRow = tileY - UNDERLAY_BLEND_RADIUS;
        if (removeRow >= 0 && removeRow < tileHeight) {
          hueSum -= blendHuesByRow[removeRow];
          saturationSum -= saturationsByRow[removeRow];
          luminanceSum -= luminancesByRow[removeRow];
          chromaSum -= chromasByRow[removeRow];
          countSum -= countsByRow[removeRow];
        }
        if (tileY < 0 || tileY >= tileHeight) {
          continue;
        }
        int sceneIndex = tileY * tileWidth + tileX;
        underlayColors[sceneIndex] = blendedUnderlayColor(underlayIds[sceneIndex], hueSum, saturationSum, luminanceSum, chromaSum, countSum);
      }
    }
  }

  private void addUnderlayContribution(
      int tileWidth,
      int tileHeight,
      int[] underlayIds,
      int tileX,
      int tileY,
      int sign,
      int[] blendHuesByRow,
      int[] saturationsByRow,
      int[] luminancesByRow,
      int[] chromasByRow,
      int[] countsByRow
  ) {
    if (tileX < 0 || tileY < 0 || tileX >= tileWidth || tileY >= tileHeight) {
      return;
    }
    FloorColorDefinition definition = floorColors.definitionFor(underlayIds[tileY * tileWidth + tileX]);
    if (definition.rgb() == 0) {
      return;
    }
    blendHuesByRow[tileY] += definition.blendHue() * sign;
    saturationsByRow[tileY] += definition.saturation() * sign;
    luminancesByRow[tileY] += definition.luminance() * sign;
    chromasByRow[tileY] += definition.blendHueMultiplier() * sign;
    countsByRow[tileY] += sign;
  }

  private int blendedUnderlayColor(
      int underlayId,
      int hueSum,
      int saturationSum,
      int luminanceSum,
      int chromaSum,
      int countSum
  ) {
    if (underlayId <= 0 || countSum <= 0 || chromaSum <= 0) {
      return 0;
    }
    int hue = (hueSum * 256) / chromaSum;
    int saturation = clamp(saturationSum / countSum, 0, 255);
    int luminance = clamp(luminanceSum / countSum, 0, 255);
    return hslToRgb(checkedLight(encodeHsl16(hue, saturation, luminance), 96));
  }

  private int normalizedOverlayTextureId(FloorColorDefinition definition) {
    if (definition.rgb() == 0xff00ff) {
      return -1;
    }
    return definition.textureId();
  }

  private int overlaySurfaceColor(FloorColorDefinition definition) {
    if (definition.rgb() == 0xff00ff) {
      return 0;
    }
    if (definition.hsl16() != 0) {
      return hslToRgb(checkedLight(definition.hsl16(), 96));
    }
    // Purely textured floors (water, dirt textures) have hsl16 == 0 in the cache because their
    // tint is derived from the sprite, not an explicit colour byte. Fall through to the texture
    // average so the bridge layer / minimap / shaped overlay corner colour can still pick up the
    // right blue tint for water instead of reading as transparent.
    int textureId = normalizedOverlayTextureId(definition);
    if (textureId >= 0) {
      int textureAverageColor = legacyTextureAverageColor(textureId);
      if (textureAverageColor != 0) {
        return textureAverageColor;
      }
      if (definition.rgb() == 0 && textureId == WATER_TEXTURE_ID) {
        return WATER_SURFACE_RGB;
      }
    }
    return 0;
  }

  private int overlayBaseColor(FloorColorDefinition definition, int textureId) {
    if (definition.rgb() == 0xff00ff) {
      return 0;
    }
    if (textureId >= 0) {
      int textureAverageColor = legacyTextureAverageColor(textureId);
      if (textureAverageColor != 0) {
        return textureAverageColor;
      }
      if (definition.rgb() == 0 && textureId == WATER_TEXTURE_ID) {
        return WATER_SURFACE_RGB;
      }
    }
    if (definition.secondaryHsl16() != -1) {
      return hslToRgb(checkedLight(definition.secondaryHsl16(), 96));
    }
    if (definition.hsl16() != 0) {
      return hslToRgb(checkedLight(definition.hsl16(), 96));
    }
    return 0;
  }

  private int legacyTextureAverageColor(int textureId) {
    if (textureId < 0 || textureId >= legacyTextureAverageColors.length) {
      return 0;
    }
    return legacyTextureAverageColors[textureId];
  }

  private int activeTileColor(int underlayColor, int overlayColor) {
    if (overlayColor != 0) {
      return overlayColor;
    }
    if (underlayColor != 0) {
      return underlayColor;
    }
    return DEFAULT_TILE_RGB;
  }

  private int checkedLight(int color, int light) {
    if (color == -2) {
      return 0xbc614e;
    }
    if (color == -1) {
      int clampedLight = clamp(light, 0, 127);
      return 127 - clampedLight;
    }
    int shadedLight = (light * (color & 0x7f)) / 128;
    return (color & 0xff80) + clamp(shadedLight, 2, 126);
  }

  private int encodeHsl16(int hue, int saturation, int luminance) {
    int adjustedSaturation = saturation;
    if (luminance > 179) {
      adjustedSaturation /= 2;
    }
    if (luminance > 192) {
      adjustedSaturation /= 2;
    }
    if (luminance > 217) {
      adjustedSaturation /= 2;
    }
    if (luminance > 243) {
      adjustedSaturation /= 2;
    }
    return (hue / 4 << 10) + (adjustedSaturation / 32 << 7) + luminance / 2;
  }

  private int hslToRgb(int colorHsl) {
    return LEGACY_HSL_TO_RGB[colorHsl & 0xffff];
  }

  private static int[] buildLegacyHslToRgb() {
    int[] hslToRgb = new int[0x10000];
    for (int encodedHsl = 0; encodedHsl < hslToRgb.length; encodedHsl++) {
      double hue = ((encodedHsl >> 10) & 0x3f) / 64.0D + 0.0078125D;
      double saturation = ((encodedHsl >> 7) & 0x07) / 8.0D + 0.0625D;
      double luminance = (encodedHsl & 0x7f) / 128.0D;
      double red = luminance;
      double green = luminance;
      double blue = luminance;
      if (saturation != 0.0D) {
        double q = luminance < 0.5D
            ? luminance * (1.0D + saturation)
            : luminance + saturation - luminance * saturation;
        double p = 2.0D * luminance - q;
        red = hueToChannel(p, q, hue + 0.33333333333333331D);
        green = hueToChannel(p, q, hue);
        blue = hueToChannel(p, q, hue - 0.33333333333333331D);
      }
      int rgb = ((int) (red * 256.0D) << 16)
          | ((int) (green * 256.0D) << 8)
          | (int) (blue * 256.0D);
      rgb = adjustBrightness(rgb, LEGACY_RASTER_BRIGHTNESS);
      hslToRgb[encodedHsl] = rgb == 0 ? 1 : rgb;
    }
    return hslToRgb;
  }

  private static double hueToChannel(double p, double q, double value) {
    double wrapped = value;
    if (wrapped > 1.0D) {
      wrapped--;
    }
    if (wrapped < 0.0D) {
      wrapped++;
    }
    if (6.0D * wrapped < 1.0D) {
      return p + (q - p) * 6.0D * wrapped;
    }
    if (2.0D * wrapped < 1.0D) {
      return q;
    }
    if (3.0D * wrapped < 2.0D) {
      return p + (q - p) * (0.66666666666666663D - wrapped) * 6.0D;
    }
    return p;
  }

  private static int adjustBrightness(int rgb, double intensity) {
    double red = Math.pow(((rgb >> 16) & 0xff) / 256.0D, intensity);
    double green = Math.pow(((rgb >> 8) & 0xff) / 256.0D, intensity);
    double blue = Math.pow((rgb & 0xff) / 256.0D, intensity);
    return ((int) (red * 256.0D) << 16) | ((int) (green * 256.0D) << 8) | (int) (blue * 256.0D);
  }

  public static int legacyTextureAverageColorFromPalette(int[] palette) {
    if (palette.length == 0) {
      return 0;
    }
    long red = 0;
    long green = 0;
    long blue = 0;
    for (int paletteIndex = 0; paletteIndex < palette.length; paletteIndex++) {
      int adjustedColor = adjustBrightness(palette[paletteIndex], LEGACY_RASTER_BRIGHTNESS);
      if ((adjustedColor & 0xf8f8ff) == 0 && paletteIndex != 0) {
        adjustedColor = 1;
      }
      red += (adjustedColor >>> 16) & 0xff;
      green += (adjustedColor >>> 8) & 0xff;
      blue += adjustedColor & 0xff;
    }
    int averageColor = ((int) (red / palette.length) << 16)
        | ((int) (green / palette.length) << 8)
        | (int) (blue / palette.length);
    int brightenedAverage = adjustBrightness(averageColor, LEGACY_TEXTURE_AVERAGE_BRIGHTNESS);
    return brightenedAverage == 0 ? 1 : brightenedAverage;
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
