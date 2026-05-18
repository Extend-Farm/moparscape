package io.github.ffakira.rsps.client.lwjgl;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;

public final class TitleArchiveFontDecoder {

  private static final String INDEX_ENTRY = "index.dat";

  public TitleScreenBitmapFont decodeFont(
      CacheArchiveContainer archive,
      String fontName,
      boolean uppercaseSpaceWidth
  ) {
    ByteCursor dataCursor = new ByteCursor(archive.readEntry(fontName + ".dat"));
    ByteCursor indexCursor = new ByteCursor(archive.readEntry(INDEX_ENTRY), dataCursor.readUnsignedShort() + 4);

    int paletteCount = indexCursor.readUnsignedByte();
    if (paletteCount > 0) {
      indexCursor.skip(3 * (paletteCount - 1));
    }

    byte[][] glyphMasks = new byte[256][];
    int[] glyphWidths = new int[256];
    int[] glyphHeights = new int[256];
    int[] glyphXOffsets = new int[256];
    int[] glyphYOffsets = new int[256];
    int[] glyphAdvances = new int[256];
    int maxGlyphHeight = 0;

    for (int glyph = 0; glyph < 256; glyph++) {
      glyphXOffsets[glyph] = indexCursor.readUnsignedByte();
      glyphYOffsets[glyph] = indexCursor.readUnsignedByte();
      int width = glyphWidths[glyph] = indexCursor.readUnsignedShort();
      int height = glyphHeights[glyph] = indexCursor.readUnsignedShort();
      int storageType = indexCursor.readUnsignedByte();
      int pixelCount = width * height;
      byte[] glyphMask = glyphMasks[glyph] = new byte[pixelCount];

      if (storageType == 0) {
        for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
          glyphMask[pixelIndex] = dataCursor.readByte();
        }
      } else if (storageType == 1) {
        for (int x = 0; x < width; x++) {
          for (int y = 0; y < height; y++) {
            glyphMask[x + y * width] = dataCursor.readByte();
          }
        }
      } else {
        throw new IllegalStateException("Unsupported font storage type " + storageType + " for " + fontName);
      }

      if (height > maxGlyphHeight && glyph < 128) {
        maxGlyphHeight = height;
      }

      glyphXOffsets[glyph] = 1;
      glyphAdvances[glyph] = width + 2;

      int leftInk = 0;
      for (int y = height / 7; y < height; y++) {
        leftInk += glyphMask[y * width];
      }
      if (leftInk <= height / 7) {
        glyphAdvances[glyph]--;
        glyphXOffsets[glyph] = 0;
      }

      int rightInk = 0;
      for (int y = height / 7; y < height; y++) {
        rightInk += glyphMask[(width - 1) + y * width];
      }
      if (rightInk <= height / 7) {
        glyphAdvances[glyph]--;
      }
    }

    glyphAdvances[' '] = glyphAdvances[uppercaseSpaceWidth ? 'I' : 'i'];
    return new TitleScreenBitmapFont(
        glyphMasks,
        glyphWidths,
        glyphHeights,
        glyphXOffsets,
        glyphYOffsets,
        glyphAdvances,
        maxGlyphHeight
    );
  }

  private static final class ByteCursor {
    private final byte[] bytes;
    private int offset;

    private ByteCursor(byte[] bytes) {
      this(bytes, 0);
    }

    private ByteCursor(byte[] bytes, int offset) {
      this.bytes = bytes;
      this.offset = offset;
    }

    private byte readByte() {
      return bytes[offset++];
    }

    private int readUnsignedByte() {
      return readByte() & 0xff;
    }

    private int readUnsignedShort() {
      return (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private void skip(int count) {
      offset += count;
    }
  }
}
