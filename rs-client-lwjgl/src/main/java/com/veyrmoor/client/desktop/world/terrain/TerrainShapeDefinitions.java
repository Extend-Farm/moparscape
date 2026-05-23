package com.veyrmoor.client.desktop.world.terrain;

final class TerrainShapeDefinitions {

  private static final int[][] POINTS = {
      {1, 3, 5, 7},
      {1, 3, 5, 7},
      {1, 3, 5, 7},
      {1, 3, 5, 7, 6},
      {1, 3, 5, 7, 6},
      {1, 3, 5, 7, 6},
      {1, 3, 5, 7, 6},
      {1, 3, 5, 7, 2, 6},
      {1, 3, 5, 7, 2, 8},
      {1, 3, 5, 7, 2, 8},
      {1, 3, 5, 7, 11, 12},
      {1, 3, 5, 7, 11, 12},
      {1, 3, 5, 7, 13, 14}
  };

  private static final int[][] TRIANGLES = {
      {0, 1, 2, 3, 0, 0, 1, 3},
      {1, 1, 2, 3, 1, 0, 1, 3},
      {0, 1, 2, 3, 1, 0, 1, 3},
      {0, 0, 1, 2, 0, 0, 2, 4, 1, 0, 4, 3},
      {0, 0, 1, 4, 0, 0, 4, 3, 1, 1, 2, 4},
      {0, 0, 4, 3, 1, 0, 1, 2, 1, 0, 2, 4},
      {0, 1, 2, 4, 1, 0, 1, 4, 1, 0, 4, 3},
      {0, 4, 1, 2, 0, 4, 2, 5, 1, 0, 4, 5, 1, 0, 5, 3},
      {0, 4, 1, 2, 0, 4, 2, 3, 0, 4, 3, 5, 1, 0, 4, 5},
      {0, 0, 4, 5, 1, 4, 1, 2, 1, 4, 2, 3, 1, 4, 3, 5},
      {0, 0, 1, 5, 0, 1, 4, 5, 0, 1, 2, 4, 1, 0, 5, 3, 1, 5, 4, 3, 1, 4, 2, 3},
      {1, 0, 1, 5, 1, 1, 4, 5, 1, 1, 2, 4, 0, 0, 5, 3, 0, 5, 4, 3, 0, 4, 2, 3},
      {1, 0, 5, 4, 1, 0, 1, 5, 0, 0, 4, 3, 0, 4, 5, 3, 0, 5, 2, 3, 0, 1, 2, 5}
  };

  private TerrainShapeDefinitions() {
  }

  static boolean isSupportedShape(int shape) {
    return shape > 1 && shape < POINTS.length;
  }

  static int[] pointCodes(int shape) {
    return POINTS[shape];
  }

  static int[] triangles(int shape) {
    return TRIANGLES[shape];
  }

  static boolean hasTexturedTriangle(int[] triangles, int overlayTextureId) {
    if (overlayTextureId < 0) {
      return false;
    }
    for (int triangleOffset = 0; triangleOffset < triangles.length; triangleOffset += 4) {
      if (triangles[triangleOffset] != 0) {
        return true;
      }
    }
    return false;
  }

  static boolean hasGouraudTriangle(int[] triangles, int overlayTextureId) {
    for (int triangleOffset = 0; triangleOffset < triangles.length; triangleOffset += 4) {
      if (triangles[triangleOffset] == 0 || overlayTextureId < 0) {
        return true;
      }
    }
    return false;
  }

  static int rotatePointCode(int pointCode, int rotation) {
    if ((pointCode & 1) == 0 && pointCode <= 8) {
      return ((pointCode - rotation - rotation - 1) & 7) + 1;
    }
    if (pointCode > 8 && pointCode <= 12) {
      return ((pointCode - 9 - rotation) & 3) + 9;
    }
    if (pointCode > 12 && pointCode <= 16) {
      return ((pointCode - 13 - rotation) & 3) + 13;
    }
    return pointCode;
  }

  static int rotateTriangleVertexIndex(int vertexIndex, int rotation) {
    return vertexIndex < 4 ? (vertexIndex - rotation) & 3 : vertexIndex;
  }

  static float pointX(int tileX, int pointCode) {
    return switch (pointCode) {
      case 1, 7 -> tileX;
      case 2, 6, 9, 11 -> tileX + 0.5f;
      case 3, 4, 5 -> tileX + 1.0f;
      case 8, 12, 13, 16 -> tileX + 0.25f;
      case 10, 14, 15 -> tileX + 0.75f;
      default -> tileX + 0.5f;
    };
  }

  static float pointZ(int tileY, int pointCode) {
    return switch (pointCode) {
      case 1, 2, 3 -> tileY;
      case 4, 8, 10, 12 -> tileY + 0.5f;
      case 5, 6, 7 -> tileY + 1.0f;
      case 9, 13, 14 -> tileY + 0.25f;
      case 11, 15, 16 -> tileY + 0.75f;
      default -> tileY + 0.5f;
    };
  }
}
