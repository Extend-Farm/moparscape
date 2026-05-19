package io.github.ffakira.rsps.client.desktop.login;

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
}
