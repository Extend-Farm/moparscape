package com.veyrmoor.client.desktop.character;

final class CharacterColorPalette {

  private static final double REFERENCE_GAMMA = 0.8D;
  private static final int[] SHADED_HSL_TO_RGB = buildPalette();

  private CharacterColorPalette() {
  }

  // Matches the reference client's Rasterizer3D shaded-HSL palette without importing that
  // runtime directly into the native LWJGL path.
  static int rgb(int shadedHsl) {
    int paletteIndex = clamp(shadedHsl, 0, SHADED_HSL_TO_RGB.length - 1);
    int rgb = SHADED_HSL_TO_RGB[paletteIndex];
    return rgb == 0 ? 1 : rgb;
  }

  private static int[] buildPalette() {
    int[] palette = new int[0x10000];
    int paletteIndex = 0;
    for (int hueIndex = 0; hueIndex < 512; hueIndex++) {
      double hue = (hueIndex / 8) / 64.0D + 0.0078125D;
      double saturation = (hueIndex & 7) / 8.0D + 0.0625D;
      for (int lightnessIndex = 0; lightnessIndex < 128; lightnessIndex++) {
        double lightness = lightnessIndex / 128.0D;
        double red = lightness;
        double green = lightness;
        double blue = lightness;
        if (saturation != 0.0D) {
          double q = lightness < 0.5D
              ? lightness * (1.0D + saturation)
              : (lightness + saturation) - lightness * saturation;
          double p = 2.0D * lightness - q;
          red = paletteChannel(p, q, hue + 0.3333333333333333D);
          green = paletteChannel(p, q, hue);
          blue = paletteChannel(p, q, hue - 0.3333333333333333D);
        }
        int rgb = ((int) (red * 256.0D) << 16)
            + ((int) (green * 256.0D) << 8)
            + (int) (blue * 256.0D);
        palette[paletteIndex++] = gammaAdjust(rgb);
      }
    }
    return palette;
  }

  private static double paletteChannel(double p, double q, double value) {
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
      return p + (q - p) * (0.6666666666666666D - wrapped) * 6.0D;
    }
    return p;
  }

  private static int gammaAdjust(int rgb) {
    double red = Math.pow((rgb >>> 16) / 256.0D, REFERENCE_GAMMA);
    double green = Math.pow(((rgb >>> 8) & 0xff) / 256.0D, REFERENCE_GAMMA);
    double blue = Math.pow((rgb & 0xff) / 256.0D, REFERENCE_GAMMA);
    return ((int) (red * 256.0D) << 16)
        | ((int) (green * 256.0D) << 8)
        | (int) (blue * 256.0D);
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
