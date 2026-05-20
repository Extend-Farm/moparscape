package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.content.FloorColorCatalog;
import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

class FloorSurfaceColorResolverTest {

  @Test
  void blendsUnderlaysAcrossTheElvargNeighborhoodWindow() {
    FloorSurfaceColorResolver resolver = new FloorSurfaceColorResolver(floorCatalog(
        new FloorEntry(0x2f7f2f, -1, -1),
        new FloorEntry(0x9a5b1f, -1, -1)
    ));
    int width = 24;
    int height = 24;
    int[] underlayIds = new int[width * height];
    int[] overlayIds = new int[width * height];
    int[] underlayColors = new int[width * height];
    int[] overlayColors = new int[width * height];
    int[] underlayTextureIds = new int[width * height];
    int[] overlayTextureIds = new int[width * height];
    int[] tileColors = new int[width * height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        underlayIds[y * width + x] = x < width / 2 ? 1 : 2;
      }
    }

    resolver.resolveSceneColors(
        width,
        height,
        underlayIds,
        overlayIds,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        tileColors
    );

    int leftColor = underlayColors[12 * width + 4];
    int blendedColor = underlayColors[12 * width + 11];
    int rightColor = underlayColors[12 * width + 19];

    assertThat(leftColor).isNotEqualTo(0);
    assertThat(rightColor).isNotEqualTo(0);
    assertThat(blendedColor).isNotEqualTo(leftColor);
    assertThat(blendedColor).isNotEqualTo(rightColor);
    assertThat(channel(blendedColor, 16)).isBetween(
        Math.min(channel(leftColor, 16), channel(rightColor, 16)),
        Math.max(channel(leftColor, 16), channel(rightColor, 16))
    );
  }

  @Test
  void resolvesOverlaySentinelsAndWaterTextureFallbacks() {
    FloorSurfaceColorResolver resolver = new FloorSurfaceColorResolver(floorCatalog(
        new FloorEntry(0x3f5f2f, -1, -1),
        new FloorEntry(0xff00ff, -1, -1),
        new FloorEntry(0, 1, -1),
        new FloorEntry(0, -1, 0x4f82c8)
    ));
    int[] underlayIds = new int[]{1, 1, 1, 1};
    int[] overlayIds = new int[]{2, 3, 4, 0};
    int[] underlayColors = new int[4];
    int[] overlayColors = new int[4];
    int[] underlayTextureIds = new int[4];
    int[] overlayTextureIds = new int[4];
    int[] tileColors = new int[4];

    resolver.resolveSceneColors(
        2,
        2,
        underlayIds,
        overlayIds,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        tileColors
    );

    assertThat(overlayColors[0]).isZero();
    assertThat(overlayTextureIds[0]).isEqualTo(-1);
    assertThat(overlayColors[1]).isEqualTo(0x5a7ea3);
    assertThat(overlayTextureIds[1]).isEqualTo(1);
    assertThat(overlayColors[2]).isNotZero();
    assertThat(tileColors[3]).isEqualTo(underlayColors[3]);
  }

  private static FloorColorCatalog floorCatalog(FloorEntry... entries) {
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      writeUnsignedShort(bytes, entries.length);
      for (FloorEntry entry : entries) {
        writeFloor(bytes, entry);
      }
      return FloorColorCatalog.parse(bytes.toByteArray());
    } catch (java.io.IOException ioException) {
      throw new IllegalStateException(ioException);
    }
  }

  private static void writeFloor(ByteArrayOutputStream bytes, FloorEntry entry) throws java.io.IOException {
    if (entry.rgb() != 0) {
      bytes.write(1);
      writeMedium(bytes, entry.rgb());
    }
    if (entry.textureId() >= 0) {
      bytes.write(2);
      bytes.write(entry.textureId());
    }
    if (entry.secondaryRgb() >= 0) {
      bytes.write(7);
      writeMedium(bytes, entry.secondaryRgb());
    }
    bytes.write(0);
  }

  private static void writeUnsignedShort(ByteArrayOutputStream bytes, int value) throws java.io.IOException {
    bytes.write((value >>> 8) & 0xff);
    bytes.write(value & 0xff);
  }

  private static void writeMedium(ByteArrayOutputStream bytes, int value) throws java.io.IOException {
    bytes.write((value >>> 16) & 0xff);
    bytes.write((value >>> 8) & 0xff);
    bytes.write(value & 0xff);
  }

  private static int channel(int rgb, int shift) {
    return (rgb >>> shift) & 0xff;
  }

  private record FloorEntry(int rgb, int textureId, int secondaryRgb) {
  }
}
