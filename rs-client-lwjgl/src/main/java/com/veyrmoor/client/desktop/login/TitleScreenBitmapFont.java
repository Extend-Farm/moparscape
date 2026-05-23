package com.veyrmoor.client.desktop.login;

public final class TitleScreenBitmapFont {

  private final byte[][] glyphMasks;
  private final int[] glyphWidths;
  private final int[] glyphHeights;
  private final int[] glyphXOffsets;
  private final int[] glyphYOffsets;
  private final int[] glyphAdvances;
  private final int maxGlyphHeight;

  public TitleScreenBitmapFont(
      byte[][] glyphMasks,
      int[] glyphWidths,
      int[] glyphHeights,
      int[] glyphXOffsets,
      int[] glyphYOffsets,
      int[] glyphAdvances,
      int maxGlyphHeight
  ) {
    this.glyphMasks = glyphMasks;
    this.glyphWidths = glyphWidths;
    this.glyphHeights = glyphHeights;
    this.glyphXOffsets = glyphXOffsets;
    this.glyphYOffsets = glyphYOffsets;
    this.glyphAdvances = glyphAdvances;
    this.maxGlyphHeight = maxGlyphHeight;
  }

  public int measureText(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    int width = 0;
    for (int index = 0; index < text.length(); index++) {
      width += glyphAdvances[text.charAt(index) & 0xff];
    }
    return width;
  }

  public int measureGlyph(char glyph) {
    return glyphAdvances[glyph & 0xff];
  }

  public int measureVisibleWidth(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    int cursorX = 0;
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    for (int index = 0; index < text.length(); index++) {
      int glyph = text.charAt(index) & 0xff;
      if (glyph != ' ') {
        minX = Math.min(minX, cursorX + glyphXOffsets[glyph]);
        maxX = Math.max(maxX, cursorX + glyphXOffsets[glyph] + glyphWidths[glyph]);
      }
      cursorX += glyphAdvances[glyph];
    }
    return minX == Integer.MAX_VALUE ? 0 : Math.max(0, maxX - minX);
  }

  public int visibleLeftOffset(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    int cursorX = 0;
    int minX = Integer.MAX_VALUE;
    for (int index = 0; index < text.length(); index++) {
      int glyph = text.charAt(index) & 0xff;
      if (glyph != ' ') {
        minX = Math.min(minX, cursorX + glyphXOffsets[glyph]);
      }
      cursorX += glyphAdvances[glyph];
    }
    return minX == Integer.MAX_VALUE ? 0 : minX;
  }

  public int measureVisibleHeight(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int index = 0; index < text.length(); index++) {
      int glyph = text.charAt(index) & 0xff;
      if (glyph == ' ') {
        continue;
      }
      minY = Math.min(minY, glyphYOffsets[glyph]);
      maxY = Math.max(maxY, glyphYOffsets[glyph] + glyphHeights[glyph]);
    }
    return minY == Integer.MAX_VALUE ? 0 : Math.max(0, maxY - minY);
  }

  public int visibleTopOffset(String text) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    int minY = Integer.MAX_VALUE;
    for (int index = 0; index < text.length(); index++) {
      int glyph = text.charAt(index) & 0xff;
      if (glyph == ' ') {
        continue;
      }
      minY = Math.min(minY, glyphYOffsets[glyph]);
    }
    return minY == Integer.MAX_VALUE ? 0 : minY;
  }

  public int baselineForVerticalCenter(String text, int centerY) {
    int visibleHeight = measureVisibleHeight(text);
    int visibleTopOffset = visibleTopOffset(text);
    return Math.round(centerY - visibleHeight / 2.0f + maxGlyphHeight - visibleTopOffset);
  }

  public int maxGlyphHeight() {
    return maxGlyphHeight;
  }

  public void drawText(
      int[] pixels,
      int canvasWidth,
      int canvasHeight,
      String text,
      int left,
      int baselineY,
      int rgb,
      boolean shadow
  ) {
    if (text == null || text.isEmpty()) {
      return;
    }
    int argb = 0xff000000 | rgb;
    if (shadow) {
      drawTextInternal(pixels, canvasWidth, canvasHeight, text, left + 1, baselineY + 1, 0xff000000);
    }
    drawTextInternal(pixels, canvasWidth, canvasHeight, text, left, baselineY, argb);
  }

  public void drawAnimatedMenuText(
      int[] pixels,
      int canvasWidth,
      int canvasHeight,
      String text,
      int left,
      int baselineY,
      int defaultRgb,
      int seed,
      boolean shadow
  ) {
    if (text == null || text.isEmpty()) {
      return;
    }
    java.util.Random random = new java.util.Random(seed);
    int argb = ((192 + (random.nextInt() & 0x1f)) << 24) | defaultRgb;
    int shadowArgb = shadow ? 0xc0000000 : 0;
    int cursorX = left;
    int baseTop = baselineY - maxGlyphHeight;
    int rgb = defaultRgb;
    for (int index = 0; index < text.length(); index++) {
      if (text.charAt(index) == '@' && index + 4 < text.length() && text.charAt(index + 4) == '@') {
        int tagColor = menuColor(text.substring(index + 1, index + 4));
        if (tagColor >= 0) {
          rgb = tagColor;
          argb = (argb & 0xff000000) | rgb;
        }
        index += 4;
        continue;
      }
      int glyph = text.charAt(index) & 0xff;
      if (glyph != ' ') {
        if (shadow) {
          blendGlyph(
              pixels,
              canvasWidth,
              canvasHeight,
              glyphMasks[glyph],
              cursorX + glyphXOffsets[glyph] + 1,
              baseTop + glyphYOffsets[glyph] + 1,
              glyphWidths[glyph],
              glyphHeights[glyph],
              shadowArgb
          );
        }
        blendGlyph(
            pixels,
            canvasWidth,
            canvasHeight,
            glyphMasks[glyph],
            cursorX + glyphXOffsets[glyph],
            baseTop + glyphYOffsets[glyph],
            glyphWidths[glyph],
            glyphHeights[glyph],
            argb
        );
      }
      cursorX += glyphAdvances[glyph];
      if ((random.nextInt() & 3) == 0) {
        cursorX++;
      }
    }
  }

  public int measureAnimatedMenuText(String text, int seed) {
    if (text == null || text.isEmpty()) {
      return 0;
    }
    java.util.Random random = new java.util.Random(seed);
    random.nextInt();
    int width = 0;
    for (int index = 0; index < text.length(); index++) {
      if (text.charAt(index) == '@' && index + 4 < text.length() && text.charAt(index + 4) == '@') {
        index += 4;
        continue;
      }
      int glyph = text.charAt(index) & 0xff;
      width += glyphAdvances[glyph];
      if ((random.nextInt() & 3) == 0) {
        width++;
      }
    }
    return width;
  }

  private void drawTextInternal(
      int[] pixels,
      int canvasWidth,
      int canvasHeight,
      String text,
      int left,
      int baselineY,
      int argb
  ) {
    int cursorX = left;
    int baseTop = baselineY - maxGlyphHeight;
    for (int index = 0; index < text.length(); index++) {
      int glyph = text.charAt(index) & 0xff;
      if (glyph != ' ') {
        drawGlyph(
            pixels,
            canvasWidth,
            canvasHeight,
            glyphMasks[glyph],
            cursorX + glyphXOffsets[glyph],
            baseTop + glyphYOffsets[glyph],
            glyphWidths[glyph],
            glyphHeights[glyph],
            argb
        );
      }
      cursorX += glyphAdvances[glyph];
    }
  }

  private void drawGlyph(
      int[] pixels,
      int canvasWidth,
      int canvasHeight,
      byte[] glyphMask,
      int left,
      int top,
      int glyphWidth,
      int glyphHeight,
      int argb
  ) {
    for (int y = 0; y < glyphHeight; y++) {
      int targetY = top + y;
      if (targetY < 0 || targetY >= canvasHeight) {
        continue;
      }
      int glyphRowOffset = y * glyphWidth;
      int targetRowOffset = targetY * canvasWidth;
      for (int x = 0; x < glyphWidth; x++) {
        if (glyphMask[glyphRowOffset + x] == 0) {
          continue;
        }
        int targetX = left + x;
        if (targetX < 0 || targetX >= canvasWidth) {
          continue;
        }
        pixels[targetRowOffset + targetX] = argb;
      }
    }
  }

  private void blendGlyph(
      int[] pixels,
      int canvasWidth,
      int canvasHeight,
      byte[] glyphMask,
      int left,
      int top,
      int glyphWidth,
      int glyphHeight,
      int argb
  ) {
    int alpha = (argb >>> 24) & 0xff;
    if (alpha <= 0) {
      return;
    }
    for (int y = 0; y < glyphHeight; y++) {
      int targetY = top + y;
      if (targetY < 0 || targetY >= canvasHeight) {
        continue;
      }
      int glyphRowOffset = y * glyphWidth;
      int targetRowOffset = targetY * canvasWidth;
      for (int x = 0; x < glyphWidth; x++) {
        if (glyphMask[glyphRowOffset + x] == 0) {
          continue;
        }
        int targetX = left + x;
        if (targetX < 0 || targetX >= canvasWidth) {
          continue;
        }
        int targetIndex = targetRowOffset + targetX;
        pixels[targetIndex] = blend(pixels[targetIndex], argb);
      }
    }
  }

  private int blend(int destinationArgb, int sourceArgb) {
    int sourceAlpha = (sourceArgb >>> 24) & 0xff;
    if (sourceAlpha >= 255 || destinationArgb == 0) {
      return sourceArgb;
    }
    int destinationAlpha = (destinationArgb >>> 24) & 0xff;
    int inverseAlpha = 255 - sourceAlpha;
    int outAlpha = sourceAlpha + destinationAlpha * inverseAlpha / 255;
    int sourceRed = (sourceArgb >>> 16) & 0xff;
    int sourceGreen = (sourceArgb >>> 8) & 0xff;
    int sourceBlue = sourceArgb & 0xff;
    int destinationRed = (destinationArgb >>> 16) & 0xff;
    int destinationGreen = (destinationArgb >>> 8) & 0xff;
    int destinationBlue = destinationArgb & 0xff;
    int outRed = (sourceRed * sourceAlpha + destinationRed * destinationAlpha * inverseAlpha / 255) / Math.max(1, outAlpha);
    int outGreen = (sourceGreen * sourceAlpha + destinationGreen * destinationAlpha * inverseAlpha / 255) / Math.max(1, outAlpha);
    int outBlue = (sourceBlue * sourceAlpha + destinationBlue * destinationAlpha * inverseAlpha / 255) / Math.max(1, outAlpha);
    return (outAlpha << 24) | (outRed << 16) | (outGreen << 8) | outBlue;
  }

  private int menuColor(String tag) {
    return switch (tag) {
      case "red" -> 0xff0000;
      case "gre" -> 0x00ff00;
      case "blu" -> 0x0000ff;
      case "yel" -> 0xffff00;
      case "cya" -> 0x00ffff;
      case "mag" -> 0xff00ff;
      case "whi" -> 0xffffff;
      case "bla" -> 0x000000;
      case "lre" -> 0xff9040;
      case "dre" -> 0x800000;
      case "dbl" -> 0x000080;
      case "or1" -> 0xffb000;
      case "or2" -> 0xff7000;
      case "or3" -> 0xff3000;
      case "gr1" -> 0xc0ff00;
      case "gr2" -> 0x80ff00;
      case "gr3" -> 0x40ff00;
      default -> -1;
    };
  }
}
