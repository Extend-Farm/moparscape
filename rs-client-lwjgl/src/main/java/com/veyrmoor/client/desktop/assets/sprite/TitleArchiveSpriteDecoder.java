package com.veyrmoor.client.desktop.assets.sprite;

import com.veyrmoor.cache.CacheArchiveContainer;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.login.TitleScreenRuneMask;
import java.util.ArrayList;
import java.util.List;

public final class TitleArchiveSpriteDecoder {

  private static final String INDEX_ENTRY = "index.dat";

  private final byte[] indexBytes;

  public TitleArchiveSpriteDecoder(CacheArchiveContainer archive) {
    this.indexBytes = archive.readEntry(INDEX_ENTRY);
  }

  public ArgbImage decodeSprite(CacheArchiveContainer archive, String entryName, int frameIndex, boolean normalizeZeroPaletteEntries) {
    DecodedSprite decodedSprite = decode(archive, entryName, frameIndex, normalizeZeroPaletteEntries);
    return decodedSprite.toArgbImage();
  }

  public int[] decodePalette(CacheArchiveContainer archive, String entryName, int frameIndex, boolean normalizeZeroPaletteEntries) {
    return decode(archive, entryName, frameIndex, normalizeZeroPaletteEntries).palette().clone();
  }

  public ArgbImage[] decodeSprites(CacheArchiveContainer archive, String entryName, boolean normalizeZeroPaletteEntries) {
    byte[] dataBytes = archive.readEntry(entryName + ".dat");
    ByteCursor dataCursor = new ByteCursor(dataBytes);
    ByteCursor indexCursor = new ByteCursor(indexBytes, dataCursor.readUnsignedShort());
    SpriteSheetHeader header = readHeader(indexCursor, normalizeZeroPaletteEntries);
    List<ArgbImage> frames = new ArrayList<>();
    while (dataCursor.hasRemaining() && indexCursor.remaining() >= 7) {
      frames.add(readFrame(dataCursor, indexCursor, header, entryName).toArgbImage());
    }
    return frames.toArray(ArgbImage[]::new);
  }

  public IndexedArgbSprite[] decodeIndexedSprites(
      CacheArchiveContainer archive,
      String entryName,
      boolean normalizeZeroPaletteEntries
  ) {
    byte[] dataBytes = archive.readEntry(entryName + ".dat");
    ByteCursor dataCursor = new ByteCursor(dataBytes);
    ByteCursor indexCursor = new ByteCursor(indexBytes, dataCursor.readUnsignedShort());
    SpriteSheetHeader header = readHeader(indexCursor, normalizeZeroPaletteEntries);
    List<IndexedArgbSprite> frames = new ArrayList<>();
    while (dataCursor.hasRemaining() && indexCursor.remaining() >= 7) {
      frames.add(readFrame(dataCursor, indexCursor, header, entryName).toIndexedArgbSprite());
    }
    return frames.toArray(IndexedArgbSprite[]::new);
  }

  public TitleScreenRuneMask decodeRuneMask(CacheArchiveContainer archive, String entryName, int frameIndex) {
    DecodedSprite decodedSprite = decode(archive, entryName, frameIndex, false);
    return new TitleScreenRuneMask(decodedSprite.toMask(128, 256, 16, 16));
  }

  private DecodedSprite decode(
      CacheArchiveContainer archive,
      String entryName,
      int frameIndex,
      boolean normalizeZeroPaletteEntries
  ) {
    ByteCursor dataCursor = new ByteCursor(archive.readEntry(entryName + ".dat"));
    ByteCursor indexCursor = new ByteCursor(indexBytes, dataCursor.readUnsignedShort());
    SpriteSheetHeader header = readHeader(indexCursor, normalizeZeroPaletteEntries);

    for (int skippedFrame = 0; skippedFrame < frameIndex; skippedFrame++) {
      indexCursor.skip(2);
      int skippedWidth = indexCursor.readUnsignedShort();
      int skippedHeight = indexCursor.readUnsignedShort();
      dataCursor.skip(skippedWidth * skippedHeight);
      indexCursor.skip(1);
    }
    return readFrame(dataCursor, indexCursor, header, entryName);
  }

  private SpriteSheetHeader readHeader(ByteCursor indexCursor, boolean normalizeZeroPaletteEntries) {
    int canvasWidth = indexCursor.readUnsignedShort();
    int canvasHeight = indexCursor.readUnsignedShort();
    int paletteCount = indexCursor.readUnsignedByte();
    int[] palette = new int[paletteCount];
    for (int paletteIndex = 1; paletteIndex < paletteCount; paletteIndex++) {
      int color = indexCursor.readMedium();
      if (normalizeZeroPaletteEntries && color == 0) {
        color = 1;
      }
      palette[paletteIndex] = color;
    }
    return new SpriteSheetHeader(canvasWidth, canvasHeight, palette);
  }

  private DecodedSprite readFrame(
      ByteCursor dataCursor,
      ByteCursor indexCursor,
      SpriteSheetHeader header,
      String entryName
  ) {
    int offsetX = indexCursor.readUnsignedByte();
    int offsetY = indexCursor.readUnsignedByte();
    int width = indexCursor.readUnsignedShort();
    int height = indexCursor.readUnsignedShort();
    int storageType = indexCursor.readUnsignedByte();
    byte[] pixelIndices = new byte[width * height];

    if (storageType == 0) {
      for (int pixelIndex = 0; pixelIndex < pixelIndices.length; pixelIndex++) {
        pixelIndices[pixelIndex] = dataCursor.readByte();
      }
    } else if (storageType == 1) {
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          pixelIndices[x + y * width] = dataCursor.readByte();
        }
      }
    } else {
      throw new IllegalStateException("Unsupported sprite storage type " + storageType + " for " + entryName);
    }

    return new DecodedSprite(
        header.canvasWidth(),
        header.canvasHeight(),
        offsetX,
        offsetY,
        width,
        height,
        header.palette(),
        pixelIndices
    );
  }

  private record SpriteSheetHeader(int canvasWidth, int canvasHeight, int[] palette) {
  }

  private record DecodedSprite(
      int canvasWidth,
      int canvasHeight,
      int offsetX,
      int offsetY,
      int width,
      int height,
      int[] palette,
      byte[] pixelIndices
  ) {

    private IndexedArgbSprite toIndexedArgbSprite() {
      int[] pixels = new int[width * height];
      for (int pixelIndex = 0; pixelIndex < pixelIndices.length; pixelIndex++) {
        int paletteIndex = pixelIndices[pixelIndex] & 0xff;
        if (paletteIndex == 0) {
          continue;
        }
        pixels[pixelIndex] = 0xff000000 | palette[paletteIndex];
      }
      return new IndexedArgbSprite(offsetX, offsetY, width, height, pixels);
    }

    private ArgbImage toArgbImage() {
      int[] pixels = new int[canvasWidth * canvasHeight];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int paletteIndex = pixelIndices[x + y * width] & 0xff;
          if (paletteIndex == 0) {
            continue;
          }
          int targetX = offsetX + x;
          int targetY = offsetY + y;
          if (targetX < 0 || targetY < 0 || targetX >= canvasWidth || targetY >= canvasHeight) {
            continue;
          }
          pixels[targetX + targetY * canvasWidth] = 0xff000000 | palette[paletteIndex];
        }
      }
      return new ArgbImage(canvasWidth, canvasHeight, pixels);
    }

    private boolean[] toMask(int targetWidth, int targetHeight, int drawX, int drawY) {
      boolean[] maskPixels = new boolean[targetWidth * targetHeight];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          int paletteIndex = pixelIndices[x + y * width] & 0xff;
          if (paletteIndex == 0) {
            continue;
          }
          int targetX = drawX + offsetX + x;
          int targetY = drawY + offsetY + y;
          if (targetX < 0 || targetY < 0 || targetX >= targetWidth || targetY >= targetHeight) {
            continue;
          }
          maskPixels[targetX + targetY * targetWidth] = true;
        }
      }
      return maskPixels;
    }
  }

  public record IndexedArgbSprite(
      int offsetX,
      int offsetY,
      int width,
      int height,
      int[] pixels
  ) {

    public IndexedArgbSprite {
      pixels = pixels.clone();
    }

    public static IndexedArgbSprite fromArgbImage(ArgbImage image) {
      return new IndexedArgbSprite(0, 0, image.width(), image.height(), image.pixels());
    }
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

    private int readMedium() {
      return (readUnsignedByte() << 16) | (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private void skip(int count) {
      offset += count;
    }

    private boolean hasRemaining() {
      return offset < bytes.length;
    }

    private int remaining() {
      return bytes.length - offset;
    }
  }
}
