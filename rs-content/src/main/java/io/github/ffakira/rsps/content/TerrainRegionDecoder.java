package io.github.ffakira.rsps.content;

public final class TerrainRegionDecoder {

  private static final int PLANE_COUNT = 4;
  private static final int REGION_WIDTH = 64;
  private static final int REGION_HEIGHT = 64;

  public TerrainRegionData decode(int regionX, int regionY, byte[] bytes) {
    int[][] heightsByPlane = new int[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    byte[][] underlaysByPlane = new byte[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    byte[][] overlaysByPlane = new byte[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    byte[][] overlayShapesByPlane = new byte[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    byte[][] overlayRotationsByPlane = new byte[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    byte[][] tileFlagsByPlane = new byte[PLANE_COUNT][REGION_WIDTH * REGION_HEIGHT];
    ByteCursor cursor = new ByteCursor(bytes);

    int baseWorldX = regionX << 6;
    int baseWorldY = regionY << 6;
    for (int plane = 0; plane < PLANE_COUNT; plane++) {
      for (int tileX = 0; tileX < REGION_WIDTH; tileX++) {
        for (int tileY = 0; tileY < REGION_HEIGHT; tileY++) {
          decodeTile(
              plane,
              tileX,
              tileY,
              baseWorldX + tileX,
              baseWorldY + tileY,
              cursor,
              heightsByPlane,
              underlaysByPlane,
              overlaysByPlane,
              overlayShapesByPlane,
              overlayRotationsByPlane,
              tileFlagsByPlane
          );
        }
      }
    }

    return new TerrainRegionData(
        regionX,
        regionY,
        heightsByPlane,
        underlaysByPlane,
        overlaysByPlane,
        overlayShapesByPlane,
        overlayRotationsByPlane,
        tileFlagsByPlane
    );
  }

  private void decodeTile(
      int plane,
      int tileX,
      int tileY,
      int worldX,
      int worldY,
      ByteCursor cursor,
      int[][] heightsByPlane,
      byte[][] underlaysByPlane,
      byte[][] overlaysByPlane,
      byte[][] overlayShapesByPlane,
      byte[][] overlayRotationsByPlane,
      byte[][] tileFlagsByPlane
  ) {
    int tileIndex = tileIndex(tileX, tileY);
    while (true) {
      int opcode = cursor.readUnsignedByte();
      if (opcode == 0) {
        if (plane == 0) {
          heightsByPlane[plane][tileIndex] = -terrainNoise(932731 + worldX, 556238 + worldY) * 8;
        } else {
          heightsByPlane[plane][tileIndex] = heightsByPlane[plane - 1][tileIndex] - 240;
        }
        return;
      }
      if (opcode == 1) {
        int explicitHeight = cursor.readUnsignedByte();
        if (explicitHeight == 1) {
          explicitHeight = 0;
        }
        if (plane == 0) {
          heightsByPlane[plane][tileIndex] = -explicitHeight * 8;
        } else {
          heightsByPlane[plane][tileIndex] = heightsByPlane[plane - 1][tileIndex] - explicitHeight * 8;
        }
        return;
      }
      if (opcode <= 49) {
        overlaysByPlane[plane][tileIndex] = (byte) cursor.readUnsignedByte();
        // Legacy terrain opcodes encode more than "overlay present": they also carry the shaped
        // tile path and rotation that decide whether a tile becomes a simple paint or a true
        // tile-model submission in the scene renderer.
        overlayShapesByPlane[plane][tileIndex] = (byte) ((opcode - 2) / 4);
        overlayRotationsByPlane[plane][tileIndex] = (byte) ((opcode - 2) & 3);
        continue;
      }
      if (opcode <= 81) {
        tileFlagsByPlane[plane][tileIndex] = (byte) (opcode - 49);
        continue;
      }
      underlaysByPlane[plane][tileIndex] = (byte) (opcode - 81);
    }
  }

  private static int terrainNoise(int x, int y) {
    int noise = interpolatedNoise(x + 45365, y + 91923, 4) - 128;
    noise += interpolatedNoise(x + 10294, y + 37821, 2) - 128 >> 1;
    noise += interpolatedNoise(x, y, 1) - 128 >> 2;
    noise = (int) (noise * 0.3D) + 35;
    if (noise < 10) {
      return 10;
    }
    if (noise > 60) {
      return 60;
    }
    return noise;
  }

  private static int interpolatedNoise(int x, int y, int scale) {
    int baseX = x / scale;
    int localX = x & scale - 1;
    int baseY = y / scale;
    int localY = y & scale - 1;
    int corner00 = smoothedNoise(baseX, baseY);
    int corner10 = smoothedNoise(baseX + 1, baseY);
    int corner01 = smoothedNoise(baseX, baseY + 1);
    int corner11 = smoothedNoise(baseX + 1, baseY + 1);
    int blendedTop = blend(corner00, corner10, localX, scale);
    int blendedBottom = blend(corner01, corner11, localX, scale);
    return blend(blendedTop, blendedBottom, localY, scale);
  }

  private static int smoothedNoise(int x, int y) {
    int corners = pseudoNoise(x - 1, y - 1)
        + pseudoNoise(x + 1, y - 1)
        + pseudoNoise(x - 1, y + 1)
        + pseudoNoise(x + 1, y + 1);
    int sides = pseudoNoise(x - 1, y)
        + pseudoNoise(x + 1, y)
        + pseudoNoise(x, y - 1)
        + pseudoNoise(x, y + 1);
    int center = pseudoNoise(x, y);
    return corners / 16 + sides / 8 + center / 4;
  }

  private static int pseudoNoise(int x, int y) {
    int noise = x + y * 57;
    noise = noise << 13 ^ noise;
    int value = noise * (noise * noise * 15731 + 789221) + 1376312589 & Integer.MAX_VALUE;
    return value >> 19 & 0xff;
  }

  private static int blend(int first, int second, int position, int scale) {
    int weight = 65536 - cosineLut(position * 1024 / scale) >> 1;
    return (first * (65536 - weight) >> 16) + (second * weight >> 16);
  }

  private static int cosineLut(int index) {
    return COSINE[index];
  }

  private static int tileIndex(int tileX, int tileY) {
    return tileY * REGION_WIDTH + tileX;
  }

  private static final int[] COSINE = buildCosineLut();

  private static int[] buildCosineLut() {
    int[] cosine = new int[2048];
    for (int index = 0; index < cosine.length; index++) {
      cosine[index] = (int) (65536.0D * Math.cos(index * 0.0030679614999999999D));
    }
    return cosine;
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
  }
}
