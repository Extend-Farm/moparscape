package com.veyrmoor.client.desktop.world.terrain;

import java.util.Arrays;

final class FloorColorShadePalette {

  static final int BASELINE_LIGHT = 96;
  private static final int UNSET_RGB = Integer.MIN_VALUE;
  private static final int LOOKUP_MASK = (1 << 17) - 1;
  private static final int[] HSL_TO_RGB = buildReferenceHslToRgb();
  private static final int[] BASELINE_RGB_KEYS = new int[LOOKUP_MASK + 1];
  private static final int[] BASELINE_RGB_VALUES = new int[LOOKUP_MASK + 1];

  static {
    Arrays.fill(BASELINE_RGB_KEYS, UNSET_RGB);
    for (int encodedHsl = 0; encodedHsl < 0x10000; encodedHsl++) {
      putBaselineRgb(hslToRgb(checkedLight(encodedHsl, BASELINE_LIGHT)), encodedHsl);
    }
  }

  private FloorColorShadePalette() {
  }

  static int checkedLight(int color, int light) {
    if (color == -2) {
      return 0xbc614e;
    }
    if (color == -1) {
      return 127 - clamp(light, 0, 127);
    }
    int shadedLight = (light * (color & 0x7f)) / 128;
    return (color & 0xff80) + clamp(shadedLight, 2, 126);
  }

  static int hslToRgb(int encodedHsl) {
    return HSL_TO_RGB[encodedHsl & 0xffff];
  }

  static int baselineHsl16ForRgb(int rgb) {
    int index = mix(rgb) & LOOKUP_MASK;
    while (true) {
      int storedRgb = BASELINE_RGB_KEYS[index];
      if (storedRgb == UNSET_RGB) {
        return -1;
      }
      if (storedRgb == rgb) {
        return BASELINE_RGB_VALUES[index];
      }
      index = (index + 1) & LOOKUP_MASK;
    }
  }

  static int hueComponent(int encodedHsl) {
    return ((encodedHsl >> 10) & 0x3f) * 4;
  }

  static int saturationComponent(int encodedHsl) {
    int luminance = luminanceComponent(encodedHsl);
    int saturation = ((encodedHsl >> 7) & 0x07) * 32;
    if (luminance > 179) {
      saturation *= 2;
    }
    if (luminance > 192) {
      saturation *= 2;
    }
    if (luminance > 217) {
      saturation *= 2;
    }
    if (luminance > 243) {
      saturation *= 2;
    }
    return clamp(saturation, 0, 255);
  }

  static int luminanceComponent(int encodedHsl) {
    return (encodedHsl & 0x7f) * 2;
  }

  static int encodeHsl16(int hue, int saturation, int luminance) {
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

  private static void putBaselineRgb(int rgb, int encodedHsl) {
    int index = mix(rgb) & LOOKUP_MASK;
    while (BASELINE_RGB_KEYS[index] != UNSET_RGB && BASELINE_RGB_KEYS[index] != rgb) {
      index = (index + 1) & LOOKUP_MASK;
    }
    if (BASELINE_RGB_KEYS[index] == UNSET_RGB) {
      BASELINE_RGB_KEYS[index] = rgb;
      BASELINE_RGB_VALUES[index] = encodedHsl;
    }
  }

  private static int[] buildReferenceHslToRgb() {
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
        red = hueToRgb(p, q, hue + 1.0D / 3.0D);
        green = hueToRgb(p, q, hue);
        blue = hueToRgb(p, q, hue - 1.0D / 3.0D);
      }
      int rgb = ((int) (red * 256.0D) << 16)
          | ((int) (green * 256.0D) << 8)
          | (int) (blue * 256.0D);
      hslToRgb[encodedHsl] = rgb == 0 ? 1 : rgb;
    }
    return hslToRgb;
  }

  private static double hueToRgb(double p, double q, double hue) {
    double normalizedHue = hue;
    if (normalizedHue < 0.0D) {
      normalizedHue += 1.0D;
    }
    if (normalizedHue > 1.0D) {
      normalizedHue -= 1.0D;
    }
    if (normalizedHue * 6.0D < 1.0D) {
      return p + (q - p) * 6.0D * normalizedHue;
    }
    if (normalizedHue * 2.0D < 1.0D) {
      return q;
    }
    if (normalizedHue * 3.0D < 2.0D) {
      return p + (q - p) * (2.0D / 3.0D - normalizedHue) * 6.0D;
    }
    return p;
  }

  private static int mix(int value) {
    int mixed = value;
    mixed ^= mixed >>> 16;
    mixed *= 0x7feb352d;
    mixed ^= mixed >>> 15;
    mixed *= 0x846ca68b;
    mixed ^= mixed >>> 16;
    return mixed;
  }

  private static int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }
}
