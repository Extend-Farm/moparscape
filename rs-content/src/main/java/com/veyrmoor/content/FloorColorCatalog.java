package com.veyrmoor.content;

public final class FloorColorCatalog {

  private static final FloorColorDefinition DEFAULT_DEFINITION = buildDefinition(0, -1, true, -1);
  private final FloorColorDefinition[] definitionsById;

  private FloorColorCatalog(FloorColorDefinition[] definitionsById) {
    this.definitionsById = definitionsById;
  }

  public static FloorColorCatalog parse(byte[] bytes) {
    ByteCursor cursor = new ByteCursor(bytes);
    int definitionCount = cursor.readUnsignedShort();
    FloorColorDefinition[] definitions = new FloorColorDefinition[definitionCount + 1];
    definitions[0] = DEFAULT_DEFINITION;
    for (int definitionIndex = 1; definitionIndex <= definitionCount; definitionIndex++) {
      definitions[definitionIndex] = parseDefinition(cursor);
    }
    return new FloorColorCatalog(definitions);
  }

  public int colorFor(int floorId) {
    if (floorId <= 0 || floorId >= definitionsById.length) {
      return 0;
    }
    return definitionsById[floorId].rgb();
  }

  public int textureIdFor(int floorId) {
    if (floorId <= 0 || floorId >= definitionsById.length) {
      return -1;
    }
    return definitionsById[floorId].textureId();
  }

  public FloorColorDefinition definitionFor(int floorId) {
    if (floorId <= 0 || floorId >= definitionsById.length) {
      return DEFAULT_DEFINITION;
    }
    return definitionsById[floorId];
  }

  private static FloorColorDefinition parseDefinition(ByteCursor cursor) {
    int rgb = 0;
    int textureId = -1;
    boolean occludes = true;
    int secondaryRgb = -1;
    while (true) {
      int opcode = cursor.readUnsignedByte();
      if (opcode == 0) {
        return buildDefinition(rgb, textureId, occludes, secondaryRgb);
      }
      switch (opcode) {
        case 1 -> rgb = cursor.readMedium();
        case 2 -> textureId = cursor.readUnsignedByte();
        case 3 -> {
          // Preserve compatibility with the old format; the flag is unused here.
        }
        case 5 -> occludes = false;
        case 6 -> cursor.skipString();
        case 7 -> secondaryRgb = cursor.readMedium();
        default -> throw new IllegalStateException("Unsupported floor definition opcode " + opcode);
      }
    }
  }

  private static FloorColorDefinition buildDefinition(int rgb, int textureId, boolean occludes, int secondaryRgb) {
    Hsl primary = rgbToHsl(rgb);
    int secondaryHsl16 = secondaryRgb == -1 ? -1 : rgbToHsl(secondaryRgb).hsl16();
    return new FloorColorDefinition(
        rgb,
        textureId,
        occludes,
        secondaryRgb,
        primary.hue(),
        primary.saturation(),
        primary.luminance(),
        primary.blendHue(),
        primary.blendHueMultiplier(),
        primary.hsl16(),
        secondaryHsl16
    );
  }

  private static Hsl rgbToHsl(int rgb) {
    double red = ((rgb >> 16) & 0xff) / 256.0;
    double green = ((rgb >> 8) & 0xff) / 256.0;
    double blue = (rgb & 0xff) / 256.0;
    double minimum = Math.min(red, Math.min(green, blue));
    double maximum = Math.max(red, Math.max(green, blue));
    double hue = 0.0;
    double saturation = 0.0;
    double luminance = (minimum + maximum) * 0.5;
    if (minimum != maximum) {
      saturation = luminance < 0.5
          ? (maximum - minimum) / (maximum + minimum)
          : (maximum - minimum) / (2.0 - maximum - minimum);
      if (red == maximum) {
        hue = (green - blue) / (maximum - minimum);
      } else if (green == maximum) {
        hue = 2.0 + (blue - red) / (maximum - minimum);
      } else {
        hue = 4.0 + (red - green) / (maximum - minimum);
      }
    }
    hue /= 6.0;
    int hueValue = clamp((int) (hue * 256.0), 0, 255);
    int saturationValue = clamp((int) (saturation * 256.0), 0, 255);
    int luminanceValue = clamp((int) (luminance * 256.0), 0, 255);
    int blendHueMultiplier = luminance > 0.5
        ? (int) ((1.0 - luminance) * saturation * 512.0)
        : (int) (luminance * saturation * 512.0);
    if (blendHueMultiplier < 1) {
      blendHueMultiplier = 1;
    }
    int blendHue = (int) (hue * blendHueMultiplier);
    return new Hsl(
        hueValue,
        saturationValue,
        luminanceValue,
        blendHue,
        blendHueMultiplier,
        encodeHsl16(hueValue, saturationValue, luminanceValue)
    );
  }

  private static int encodeHsl16(int hue, int saturation, int luminance) {
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

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static final class ByteCursor {
    private final byte[] bytes;
    private int offset;

    private ByteCursor(byte[] bytes) {
      this.bytes = bytes;
    }

    private int readUnsignedByte() {
      return bytes[offset++] & 0xff;
    }

    private int readUnsignedShort() {
      return (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private int readMedium() {
      return (readUnsignedByte() << 16) | (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private void skipString() {
      while (bytes[offset++] != 10) {
        // advance
      }
    }
  }

  private record Hsl(
      int hue,
      int saturation,
      int luminance,
      int blendHue,
      int blendHueMultiplier,
      int hsl16
  ) {
  }
}
