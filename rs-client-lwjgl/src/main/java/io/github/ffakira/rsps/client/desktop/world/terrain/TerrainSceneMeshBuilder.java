package io.github.ffakira.rsps.client.desktop.world.terrain;

import io.github.ffakira.rsps.client.desktop.core.WorldScene;
import io.github.ffakira.rsps.client.desktop.core.WorldSceneScale;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMesh;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityWindow;

public final class TerrainSceneMeshBuilder {

  // This builder is the native terrain-side equivalent of the old paint-vs-model split:
  // overlay shape/rotation now decide whether a tile is emitted as a full paint quad or as a
  // shaped tile-model batch. Legacy MapRegion keeps the underlay on the lit-color path and only
  // applies floor textures from the overlay definition. The world scene now carries raw floor
  // colors, so this builder owns the single terrain-lighting pass for both Gouraud and textured
  // overlay terrain.

  private static final int[][] SHAPE_POINTS = {
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

  private static final int[][] SHAPE_TRIANGLES = {
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

  private static final int TEXTURE_SHADE_RGB = 0xffffff;
  private static final boolean ENABLE_TEXTURED_TERRAIN = true;

  public SceneTriangleMesh buildTilePaintMesh(WorldScene worldScene) {
    return buildTilePaintMesh(worldScene, fullWindow(worldScene));
  }

  public SceneTriangleMesh buildTilePaintMesh(WorldScene worldScene, WorldSceneVisibilityWindow visibilityWindow) {
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (worldScene.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        if (activePaintTextureId(worldScene, tileX, tileY) >= 0) {
          continue;
        }
        FloorColorLayer paintLayer = paintLayer(worldScene, tileX, tileY);
        int rgb = paintLayerColor(worldScene, tileX, tileY, paintLayer, worldScene.tileColorAt(tileX, tileY));
        float heightNorthWest = worldScene.elevationAt(tileX, tileY) * WorldSceneScale.HEIGHT_SCALE;
        float heightNorthEast = worldScene.elevationAt(tileX + 1, tileY) * WorldSceneScale.HEIGHT_SCALE;
        float heightSouthEast = worldScene.elevationAt(tileX + 1, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
        float heightSouthWest = worldScene.elevationAt(tileX, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
        int colorNorthWest = cornerColor(worldScene, tileX, tileY, paintLayer, rgb);
        int colorNorthEast = cornerColor(worldScene, tileX + 1, tileY, paintLayer, rgb);
        int colorSouthEast = cornerColor(worldScene, tileX + 1, tileY + 1, paintLayer, rgb);
        int colorSouthWest = cornerColor(worldScene, tileX, tileY + 1, paintLayer, rgb);
        appendPaintTile(
            builder,
            tileX,
            tileY,
            heightNorthWest,
            heightNorthEast,
            heightSouthEast,
            heightSouthWest,
            colorNorthWest,
            colorNorthEast,
            colorSouthEast,
            colorSouthWest
        );
      }
    }
    return builder.build();
  }

  public SceneTriangleMesh buildTexturedTilePaintMesh(WorldScene worldScene) {
    return buildTexturedTilePaintMesh(worldScene, fullWindow(worldScene));
  }

  public SceneTriangleMesh buildTexturedTilePaintMesh(WorldScene worldScene, WorldSceneVisibilityWindow visibilityWindow) {
    if (!ENABLE_TEXTURED_TERRAIN) {
      return SceneTriangleMesh.EMPTY;
    }
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (worldScene.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        int textureId = activePaintTextureId(worldScene, tileX, tileY);
        if (textureId < 0) {
          continue;
        }
        float heightNorthWest = worldScene.elevationAt(tileX, tileY) * WorldSceneScale.HEIGHT_SCALE;
        float heightNorthEast = worldScene.elevationAt(tileX + 1, tileY) * WorldSceneScale.HEIGHT_SCALE;
        float heightSouthEast = worldScene.elevationAt(tileX + 1, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
        float heightSouthWest = worldScene.elevationAt(tileX, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
        int colorNorthWest = textureCornerColor(worldScene, tileX, tileY);
        int colorNorthEast = textureCornerColor(worldScene, tileX + 1, tileY);
        int colorSouthEast = textureCornerColor(worldScene, tileX + 1, tileY + 1);
        int colorSouthWest = textureCornerColor(worldScene, tileX, tileY + 1);
        appendTexturedPaintTile(
            builder,
            tileX,
            tileY,
            heightNorthWest,
            heightNorthEast,
            heightSouthEast,
            heightSouthWest,
            colorNorthWest,
            colorNorthEast,
            colorSouthEast,
            colorSouthWest,
            textureId
        );
      }
    }
    return builder.build();
  }

  public SceneTriangleMesh buildTileModelMesh(WorldScene worldScene) {
    return buildTileModelMesh(worldScene, fullWindow(worldScene));
  }

  public SceneTriangleMesh buildTileModelMesh(WorldScene worldScene, WorldSceneVisibilityWindow visibilityWindow) {
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        int shape = worldScene.overlayShapeAt(tileX, tileY);
        if (shape <= 0 || shape >= SHAPE_POINTS.length) {
          continue;
        }
        appendShapedTile(builder, worldScene, tileX, tileY, shape, worldScene.overlayRotationAt(tileX, tileY), false);
      }
    }
    return builder.build();
  }

  public SceneTriangleMesh buildTexturedTileModelMesh(WorldScene worldScene) {
    return buildTexturedTileModelMesh(worldScene, fullWindow(worldScene));
  }

  public SceneTriangleMesh buildTexturedTileModelMesh(WorldScene worldScene, WorldSceneVisibilityWindow visibilityWindow) {
    if (!ENABLE_TEXTURED_TERRAIN) {
      return SceneTriangleMesh.EMPTY;
    }
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        int shape = worldScene.overlayShapeAt(tileX, tileY);
        if (shape <= 0 || shape >= SHAPE_POINTS.length) {
          continue;
        }
        appendShapedTile(builder, worldScene, tileX, tileY, shape, worldScene.overlayRotationAt(tileX, tileY), true);
      }
    }
    return builder.build();
  }

  private void appendShapedTile(
      SceneTriangleMeshBuilder builder,
      WorldScene worldScene,
      int tileX,
      int tileY,
      int shape,
      int rotation,
      boolean texturedOnly
  ) {
    int underlayRgb = fallbackTerrainColor(worldScene.underlayColorAt(tileX, tileY), worldScene.tileColorAt(tileX, tileY));
    int overlayRgb = fallbackTerrainColor(worldScene.overlayColorAt(tileX, tileY), worldScene.tileColorAt(tileX, tileY));
    int overlayTextureId = ENABLE_TEXTURED_TERRAIN ? worldScene.overlayTextureIdAt(tileX, tileY) : -1;
    int[] triangles = SHAPE_TRIANGLES[shape];
    if (texturedOnly && !hasTexturedTriangle(triangles, overlayTextureId)) {
      return;
    }
    if (!texturedOnly && !hasGouraudTriangle(triangles, overlayTextureId)) {
      return;
    }

    float heightNorthWest = worldScene.elevationAt(tileX, tileY) * WorldSceneScale.HEIGHT_SCALE;
    float heightNorthEast = worldScene.elevationAt(tileX + 1, tileY) * WorldSceneScale.HEIGHT_SCALE;
    float heightSouthEast = worldScene.elevationAt(tileX + 1, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
    float heightSouthWest = worldScene.elevationAt(tileX, tileY + 1) * WorldSceneScale.HEIGHT_SCALE;
    int underlayNorthWest = cornerColor(worldScene, tileX, tileY, FloorColorLayer.UNDERLAY, underlayRgb);
    int underlayNorthEast = cornerColor(worldScene, tileX + 1, tileY, FloorColorLayer.UNDERLAY, underlayRgb);
    int underlaySouthEast = cornerColor(worldScene, tileX + 1, tileY + 1, FloorColorLayer.UNDERLAY, underlayRgb);
    int underlaySouthWest = cornerColor(worldScene, tileX, tileY + 1, FloorColorLayer.UNDERLAY, underlayRgb);
    int overlayNorthWest = cornerColor(worldScene, tileX, tileY, FloorColorLayer.OVERLAY, overlayRgb);
    int overlayNorthEast = cornerColor(worldScene, tileX + 1, tileY, FloorColorLayer.OVERLAY, overlayRgb);
    int overlaySouthEast = cornerColor(worldScene, tileX + 1, tileY + 1, FloorColorLayer.OVERLAY, overlayRgb);
    int overlaySouthWest = cornerColor(worldScene, tileX, tileY + 1, FloorColorLayer.OVERLAY, overlayRgb);
    int texturedNorthWest = textureCornerColor(worldScene, tileX, tileY);
    int texturedNorthEast = textureCornerColor(worldScene, tileX + 1, tileY);
    int texturedSouthEast = textureCornerColor(worldScene, tileX + 1, tileY + 1);
    int texturedSouthWest = textureCornerColor(worldScene, tileX, tileY + 1);
    boolean flatTerrainQuad = isFlatTerrainQuad(heightNorthWest, heightNorthEast, heightSouthEast, heightSouthWest);

    int textureNorthWest = -1;
    int textureNorthEast = -1;
    int textureSouthWest = -1;
    if (texturedOnly && flatTerrainQuad) {
      textureNorthWest = builder.addVertex(tileX, heightNorthWest, tileY);
      textureNorthEast = builder.addVertex(tileX + 1.0f, heightNorthEast, tileY);
      textureSouthWest = builder.addVertex(tileX, heightSouthWest, tileY + 1.0f);
    }

    int[] pointCodes = SHAPE_POINTS[shape];
    int[] vertexIndices = new int[pointCodes.length];
    int[] underlayVertexColors = new int[pointCodes.length];
    int[] overlayVertexColors = new int[pointCodes.length];
    int[] texturedVertexColors = new int[pointCodes.length];
    float[] terrainVertexX = new float[pointCodes.length];
    float[] terrainVertexZ = new float[pointCodes.length];
    for (int pointIndex = 0; pointIndex < pointCodes.length; pointIndex++) {
      int pointCode = rotatePointCode(pointCodes[pointIndex], rotation);
      vertexIndices[pointIndex] = builder.addVertexPoint(
          tileX,
          tileY,
          pointCode,
          heightNorthWest,
          heightNorthEast,
          heightSouthEast,
          heightSouthWest
      );
      terrainVertexX[pointIndex] = pointX(tileX, pointCode);
      terrainVertexZ[pointIndex] = pointZ(tileY, pointCode);
      underlayVertexColors[pointIndex] = pointColor(pointCode, underlayNorthWest, underlayNorthEast, underlaySouthEast, underlaySouthWest);
      overlayVertexColors[pointIndex] = pointColor(pointCode, overlayNorthWest, overlayNorthEast, overlaySouthEast, overlaySouthWest);
      texturedVertexColors[pointIndex] = pointColor(pointCode, texturedNorthWest, texturedNorthEast, texturedSouthEast, texturedSouthWest);
    }

    for (int triangleOffset = 0; triangleOffset < triangles.length; triangleOffset += 4) {
      int colorSelector = triangles[triangleOffset];
      int vertexA = rotateTriangleVertexIndex(triangles[triangleOffset + 1], rotation);
      int vertexB = rotateTriangleVertexIndex(triangles[triangleOffset + 2], rotation);
      int vertexC = rotateTriangleVertexIndex(triangles[triangleOffset + 3], rotation);
      int textureId = colorSelector == 0 ? -1 : overlayTextureId;
      if (texturedOnly) {
        if (textureId < 0) {
          continue;
        }
        int textureVertexA = flatTerrainQuad ? textureNorthWest : vertexIndices[vertexA];
        int textureVertexB = flatTerrainQuad ? textureNorthEast : vertexIndices[vertexB];
        int textureVertexC = flatTerrainQuad ? textureSouthWest : vertexIndices[vertexC];
        addTexturedTerrainTriangle(
            builder,
            vertexIndices[vertexA],
            terrainVertexX[vertexA],
            terrainVertexZ[vertexA],
            vertexIndices[vertexB],
            terrainVertexX[vertexB],
            terrainVertexZ[vertexB],
            vertexIndices[vertexC],
            terrainVertexX[vertexC],
            terrainVertexZ[vertexC],
            texturedVertexColors[vertexA],
            texturedVertexColors[vertexB],
            texturedVertexColors[vertexC],
            255,
            textureId,
            textureVertexA,
            textureVertexB,
            textureVertexC
        );
        continue;
      }
      if (textureId >= 0) {
        continue;
      }
      addTerrainTriangle(
          builder,
          vertexIndices[vertexA],
          terrainVertexX[vertexA],
          terrainVertexZ[vertexA],
          vertexIndices[vertexB],
          terrainVertexX[vertexB],
          terrainVertexZ[vertexB],
          vertexIndices[vertexC],
          terrainVertexX[vertexC],
          terrainVertexZ[vertexC],
          colorSelector == 0 ? underlayVertexColors[vertexA] : overlayVertexColors[vertexA],
          colorSelector == 0 ? underlayVertexColors[vertexB] : overlayVertexColors[vertexB],
          colorSelector == 0 ? underlayVertexColors[vertexC] : overlayVertexColors[vertexC]
      );
    }
  }

  private int activePaintTextureId(WorldScene worldScene, int tileX, int tileY) {
    if (!ENABLE_TEXTURED_TERRAIN) {
      return -1;
    }
    int overlayTextureId = worldScene.overlayTextureIdAt(tileX, tileY);
    if (overlayTextureId >= 0) {
      return overlayTextureId;
    }
    return -1;
  }

  private void addTerrainTriangle(
      SceneTriangleMeshBuilder builder,
      int vertexA,
      float pointAX,
      float pointAZ,
      int vertexB,
      float pointBX,
      float pointBZ,
      int vertexC,
      float pointCX,
      float pointCZ,
      int colorA,
      int colorB,
      int colorC
  ) {
    if (signedArea(pointAX, pointAZ, pointBX, pointBZ, pointCX, pointCZ) < 0.0f) {
      builder.addTriangle(vertexA, vertexC, vertexB, colorA, colorC, colorB);
      return;
    }
    builder.addTriangle(vertexA, vertexB, vertexC, colorA, colorB, colorC);
  }

  private void addTexturedTerrainTriangle(
      SceneTriangleMeshBuilder builder,
      int vertexA,
      float pointAX,
      float pointAZ,
      int vertexB,
      float pointBX,
      float pointBZ,
      int vertexC,
      float pointCX,
      float pointCZ,
      int colorA,
      int colorB,
      int colorC,
      int alpha,
      int textureId,
      int textureA,
      int textureB,
      int textureC
  ) {
    if (signedArea(pointAX, pointAZ, pointBX, pointBZ, pointCX, pointCZ) < 0.0f) {
      builder.addTriangle(vertexA, vertexC, vertexB, colorA, colorC, colorB, alpha, textureId, textureA, textureB, textureC);
      return;
    }
    builder.addTriangle(vertexA, vertexB, vertexC, colorA, colorB, colorC, alpha, textureId, textureA, textureB, textureC);
  }

  private void appendPaintTile(
      SceneTriangleMeshBuilder builder,
      int tileX,
      int tileY,
      float heightNorthWest,
      float heightNorthEast,
      float heightSouthEast,
      float heightSouthWest,
      int colorNorthWest,
      int colorNorthEast,
      int colorSouthEast,
      int colorSouthWest
  ) {
    int vertexNorthWest = builder.addVertex(tileX, heightNorthWest, tileY);
    int vertexNorthEast = builder.addVertex(tileX + 1.0f, heightNorthEast, tileY);
    int vertexSouthEast = builder.addVertex(tileX + 1.0f, heightSouthEast, tileY + 1.0f);
    int vertexSouthWest = builder.addVertex(tileX, heightSouthWest, tileY + 1.0f);

    // Legacy SceneTilePaint splits a flat tile across the north-east/south-west diagonal rather
    // than the north-west/south-east diagonal. The diagonal choice materially changes the slope
    // silhouette on uneven terrain, so matching it is part of terrain parity, not cosmetic cleanup.
    addTerrainTriangle(
        builder,
        vertexSouthEast,
        tileX + 1.0f,
        tileY + 1.0f,
        vertexSouthWest,
        tileX,
        tileY + 1.0f,
        vertexNorthEast,
        tileX + 1.0f,
        tileY,
        colorSouthEast,
        colorSouthWest,
        colorNorthEast
    );
    addTerrainTriangle(
        builder,
        vertexNorthEast,
        tileX + 1.0f,
        tileY,
        vertexSouthWest,
        tileX,
        tileY + 1.0f,
        vertexNorthWest,
        tileX,
        tileY,
        colorNorthEast,
        colorSouthWest,
        colorNorthWest
    );
  }

  private void appendTexturedPaintTile(
      SceneTriangleMeshBuilder builder,
      int tileX,
      int tileY,
      float heightNorthWest,
      float heightNorthEast,
      float heightSouthEast,
      float heightSouthWest,
      int colorNorthWest,
      int colorNorthEast,
      int colorSouthEast,
      int colorSouthWest,
      int textureId
  ) {
    int vertexNorthWest = builder.addVertex(tileX, heightNorthWest, tileY);
    int vertexNorthEast = builder.addVertex(tileX + 1.0f, heightNorthEast, tileY);
    int vertexSouthEast = builder.addVertex(tileX + 1.0f, heightSouthEast, tileY + 1.0f);
    int vertexSouthWest = builder.addVertex(tileX, heightSouthWest, tileY + 1.0f);
    boolean flatTerrainQuad = isFlatTerrainQuad(heightNorthWest, heightNorthEast, heightSouthEast, heightSouthWest);

    // Legacy SceneTilePaint only reuses the shared tile anchor triangle on flat quads. Once the
    // ground slopes, the south-east/south-west/north-east triangle switches back to face-local
    // anchors instead of borrowing the full-tile texture basis.
    addTexturedTerrainTriangle(
        builder,
        vertexSouthEast,
        tileX + 1.0f,
        tileY + 1.0f,
        vertexSouthWest,
        tileX,
        tileY + 1.0f,
        vertexNorthEast,
        tileX + 1.0f,
        tileY,
        colorSouthEast,
        colorSouthWest,
        colorNorthEast,
        255,
        textureId,
        flatTerrainQuad ? vertexNorthWest : vertexSouthEast,
        flatTerrainQuad ? vertexNorthEast : vertexSouthWest,
        flatTerrainQuad ? vertexSouthWest : vertexNorthEast
    );
    addTexturedTerrainTriangle(
        builder,
        vertexNorthEast,
        tileX + 1.0f,
        tileY,
        vertexSouthWest,
        tileX,
        tileY + 1.0f,
        vertexNorthWest,
        tileX,
        tileY,
        colorNorthEast,
        colorSouthWest,
        colorNorthWest,
        255,
        textureId,
        vertexNorthWest,
        vertexNorthEast,
        vertexSouthWest
    );
  }

  private boolean isFlatTerrainQuad(
      float heightNorthWest,
      float heightNorthEast,
      float heightSouthEast,
      float heightSouthWest
  ) {
    return heightNorthWest == heightNorthEast
        && heightNorthWest == heightSouthEast
        && heightNorthWest == heightSouthWest;
  }

  private boolean hasTexturedTriangle(int[] triangles, int overlayTextureId) {
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

  private boolean hasGouraudTriangle(int[] triangles, int overlayTextureId) {
    for (int triangleOffset = 0; triangleOffset < triangles.length; triangleOffset += 4) {
      if (triangles[triangleOffset] == 0 || overlayTextureId < 0) {
        return true;
      }
    }
    return false;
  }

  private int rotatePointCode(int pointCode, int rotation) {
    if ((pointCode & 1) == 0 && pointCode <= 8) {
      return ((pointCode - rotation - rotation - 1) & 7) + 1;
    }
    if (pointCode <= 12) {
      return ((pointCode - 9 - rotation) & 3) + 9;
    }
    if (pointCode <= 16) {
      return ((pointCode - 13 - rotation) & 3) + 13;
    }
    return pointCode;
  }

  private int rotateTriangleVertexIndex(int vertexIndex, int rotation) {
    return vertexIndex < 4 ? (vertexIndex - rotation) & 3 : vertexIndex;
  }

  private float pointX(int tileX, int pointCode) {
    return switch (pointCode) {
      case 1, 7 -> tileX;
      case 2, 6, 9, 11 -> tileX + 0.5f;
      case 3, 4, 5 -> tileX + 1.0f;
      case 8, 12, 13, 16 -> tileX + 0.25f;
      case 10, 14, 15 -> tileX + 0.75f;
      default -> tileX + 0.5f;
    };
  }

  private float pointZ(int tileY, int pointCode) {
    return switch (pointCode) {
      case 1, 2, 3 -> tileY;
      case 4, 8, 10, 12 -> tileY + 0.5f;
      case 5, 6, 7 -> tileY + 1.0f;
      case 9, 13, 14 -> tileY + 0.25f;
      case 11, 15, 16 -> tileY + 0.75f;
      default -> tileY + 0.5f;
    };
  }

  private int fallbackTerrainColor(int primaryColor, int fallbackColor) {
    return primaryColor == 0 ? fallbackColor : primaryColor;
  }

  private FloorColorLayer paintLayer(WorldScene worldScene, int tileX, int tileY) {
    return worldScene.overlayColorAt(tileX, tileY) != 0 ? FloorColorLayer.OVERLAY : FloorColorLayer.UNDERLAY;
  }

  private int paintLayerColor(WorldScene worldScene, int tileX, int tileY, FloorColorLayer layer, int fallbackRgb) {
    return fallbackTerrainColor(layerColor(worldScene, tileX, tileY, layer), fallbackRgb);
  }

  private int cornerColor(WorldScene worldScene, int gridX, int gridY, FloorColorLayer layer, int fallbackRgb) {
    int clampedX = clamp(gridX, 0, worldScene.tileWidth() - 1);
    int clampedY = clamp(gridY, 0, worldScene.tileHeight() - 1);
    int westX = clamp(clampedX - 1, 0, worldScene.tileWidth() - 1);
    int eastX = clamp(clampedX + 1, 0, worldScene.tileWidth() - 1);
    int southY = clamp(clampedY - 1, 0, worldScene.tileHeight() - 1);
    int northY = clamp(clampedY + 1, 0, worldScene.tileHeight() - 1);
    int xGradient = worldScene.elevationAt(eastX, clampedY) - worldScene.elevationAt(westX, clampedY);
    int yGradient = worldScene.elevationAt(clampedX, northY) - worldScene.elevationAt(clampedX, southY);
    int brightness = clamp(132 - xGradient / 8 - yGradient / 8, 84, 196);
    return applyBrightness(cornerBaseColor(worldScene, gridX, gridY, layer, fallbackRgb), brightness);
  }

  private int textureCornerColor(WorldScene worldScene, int gridX, int gridY) {
    return cornerColor(worldScene, gridX, gridY, FloorColorLayer.ACTIVE, TEXTURE_SHADE_RGB);
  }

  // Legacy terrain colors are resolved at shared corners, not as one flat RGB per tile. The
  // native scene only carries per-tile colors today, so average the adjacent tiles that touch a
  // corner before applying the slope light. That keeps untextured floors from reading like solid
  // color slabs in places where the cache has no floor texture ids at all.
  private int cornerBaseColor(WorldScene worldScene, int gridX, int gridY, FloorColorLayer layer, int fallbackRgb) {
    int[] accumulatedColor = new int[4];
    accumulateCornerTileColor(worldScene, gridX - 1, gridY - 1, layer, accumulatedColor);
    accumulateCornerTileColor(worldScene, gridX, gridY - 1, layer, accumulatedColor);
    accumulateCornerTileColor(worldScene, gridX - 1, gridY, layer, accumulatedColor);
    accumulateCornerTileColor(worldScene, gridX, gridY, layer, accumulatedColor);
    if (accumulatedColor[3] == 0) {
      return fallbackRgb;
    }
    return ((accumulatedColor[0] / accumulatedColor[3]) << 16)
        | ((accumulatedColor[1] / accumulatedColor[3]) << 8)
        | (accumulatedColor[2] / accumulatedColor[3]);
  }

  private void accumulateCornerTileColor(
      WorldScene worldScene,
      int tileX,
      int tileY,
      FloorColorLayer layer,
      int[] accumulatedColor
  ) {
    if (tileX < 0 || tileY < 0 || tileX >= worldScene.tileWidth() || tileY >= worldScene.tileHeight()) {
      return;
    }
    int rgb = layerColor(worldScene, tileX, tileY, layer);
    if (rgb == 0) {
      return;
    }
    accumulatedColor[0] += (rgb >>> 16) & 0xff;
    accumulatedColor[1] += (rgb >>> 8) & 0xff;
    accumulatedColor[2] += rgb & 0xff;
    accumulatedColor[3]++;
  }

  private int layerColor(WorldScene worldScene, int tileX, int tileY, FloorColorLayer layer) {
    return switch (layer) {
      case UNDERLAY -> worldScene.underlayColorAt(tileX, tileY);
      case OVERLAY -> worldScene.overlayColorAt(tileX, tileY);
      case ACTIVE -> worldScene.tileColorAt(tileX, tileY);
    };
  }

  private int pointColor(int pointCode, int northWest, int northEast, int southEast, int southWest) {
    return switch (pointCode) {
      case 1, 13 -> northWest;
      case 2, 9 -> blend(northWest, northEast);
      case 3, 14 -> northEast;
      case 4, 10 -> blend(northEast, southEast);
      case 5, 15 -> southEast;
      case 6, 11 -> blend(southEast, southWest);
      case 7, 16 -> southWest;
      case 8, 12 -> blend(southWest, northWest);
      default -> blend(blend(northWest, northEast), blend(southEast, southWest));
    };
  }

  private int blend(int firstRgb, int secondRgb) {
    int red = (((firstRgb >>> 16) & 0xff) + ((secondRgb >>> 16) & 0xff)) / 2;
    int green = (((firstRgb >>> 8) & 0xff) + ((secondRgb >>> 8) & 0xff)) / 2;
    int blue = ((firstRgb & 0xff) + (secondRgb & 0xff)) / 2;
    return (red << 16) | (green << 8) | blue;
  }

  private int applyBrightness(int rgb, int brightness) {
    int red = (((rgb >>> 16) & 0xff) * brightness) / 128;
    int green = (((rgb >>> 8) & 0xff) * brightness) / 128;
    int blue = ((rgb & 0xff) * brightness) / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  private float signedArea(
      float pointAX,
      float pointAZ,
      float pointBX,
      float pointBZ,
      float pointCX,
      float pointCZ
  ) {
    return (pointBX - pointAX) * (pointCZ - pointAZ) - (pointBZ - pointAZ) * (pointCX - pointAX);
  }

  private int clamp(int value, int min, int max) {
    return Math.max(min, Math.min(max, value));
  }

  private WorldSceneVisibilityWindow fullWindow(WorldScene worldScene) {
    return new WorldSceneVisibilityWindow(0, worldScene.tileWidth() - 2, 0, worldScene.tileHeight() - 2);
  }

  private enum FloorColorLayer {
    UNDERLAY,
    OVERLAY,
    ACTIVE
  }
}
