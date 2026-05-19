package io.github.ffakira.rsps.client.desktop.world.terrain;

import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneScale;
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

  private static final boolean ENABLE_TEXTURED_TERRAIN = true;

  public SceneTriangleMesh buildTilePaintMesh(WorldScene worldScene) {
    return buildTilePaintMesh(worldScene, fullWindow(worldScene));
  }

  public SceneTriangleMesh buildTilePaintMesh(WorldScene worldScene, WorldSceneVisibilityWindow visibilityWindow) {
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    appendBridgeLowerPaintTiles(builder, worldScene, visibilityWindow);
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (worldScene.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        if (TerrainTileColorResolver.activePaintTextureId(worldScene, tileX, tileY, ENABLE_TEXTURED_TERRAIN) >= 0) {
          continue;
        }
        TerrainTileColorResolver.FloorColorLayer paintLayer = TerrainTileColorResolver.paintLayer(worldScene, tileX, tileY);
        int rgb = TerrainTileColorResolver.paintLayerColor(worldScene, tileX, tileY, paintLayer, worldScene.tileColorAt(tileX, tileY));
        appendPaintTile(builder, tileX, tileY, cornerHeights(worldScene, tileX, tileY), cornerColors(worldScene, tileX, tileY, paintLayer, rgb));
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
    appendBridgeLowerTexturedPaintTiles(builder, worldScene, visibilityWindow);
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (worldScene.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        int textureId = TerrainTileColorResolver.activePaintTextureId(worldScene, tileX, tileY, ENABLE_TEXTURED_TERRAIN);
        if (textureId < 0) {
          continue;
        }
        appendTexturedPaintTile(
            builder,
            tileX,
            tileY,
            cornerHeights(worldScene, tileX, tileY),
            textureCornerColors(worldScene, tileX, tileY),
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
    appendBridgeLowerShapedTiles(builder, worldScene, visibilityWindow, false);
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        int shape = worldScene.overlayShapeAt(tileX, tileY);
        if (!TerrainShapeDefinitions.isSupportedShape(shape)) {
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
    appendBridgeLowerShapedTiles(builder, worldScene, visibilityWindow, true);
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        int shape = worldScene.overlayShapeAt(tileX, tileY);
        if (!TerrainShapeDefinitions.isSupportedShape(shape)) {
          continue;
        }
        appendShapedTile(builder, worldScene, tileX, tileY, shape, worldScene.overlayRotationAt(tileX, tileY), true);
      }
    }
    return builder.build();
  }

  private void appendShapedTile(
      SceneTriangleMeshBuilder builder,
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      int shape,
      int rotation,
      boolean texturedOnly
  ) {
    int underlayRgb = TerrainTileColorResolver.fallbackTerrainColor(terrainLayerSource.underlayColorAt(tileX, tileY), terrainLayerSource.tileColorAt(tileX, tileY));
    int overlayRgb = TerrainTileColorResolver.fallbackTerrainColor(terrainLayerSource.overlayColorAt(tileX, tileY), terrainLayerSource.tileColorAt(tileX, tileY));
    int overlayTextureId = TerrainTileColorResolver.activePaintTextureId(terrainLayerSource, tileX, tileY, ENABLE_TEXTURED_TERRAIN);
    int[] triangles = TerrainShapeDefinitions.triangles(shape);
    if (texturedOnly && !TerrainShapeDefinitions.hasTexturedTriangle(triangles, overlayTextureId)) {
      return;
    }
    if (!texturedOnly && !TerrainShapeDefinitions.hasGouraudTriangle(triangles, overlayTextureId)) {
      return;
    }

    TerrainCornerHeights heights = cornerHeights(terrainLayerSource, tileX, tileY);
    TerrainCornerColors underlayColors = cornerColors(
        terrainLayerSource,
        tileX,
        tileY,
        TerrainTileColorResolver.FloorColorLayer.UNDERLAY,
        underlayRgb
    );
    TerrainCornerColors overlayColors = cornerColors(
        terrainLayerSource,
        tileX,
        tileY,
        TerrainTileColorResolver.FloorColorLayer.OVERLAY,
        overlayRgb
    );
    TerrainCornerColors texturedColors = textureCornerColors(terrainLayerSource, tileX, tileY);
    boolean flatTerrainQuad = isFlatTerrainQuad(heights);

    int textureNorthWest = -1;
    int textureNorthEast = -1;
    int textureSouthWest = -1;
    if (texturedOnly && flatTerrainQuad) {
      textureNorthWest = builder.addVertex(tileX, heights.northWest(), tileY);
      textureNorthEast = builder.addVertex(tileX + 1.0f, heights.northEast(), tileY);
      textureSouthWest = builder.addVertex(tileX, heights.southWest(), tileY + 1.0f);
    }

    int[] pointCodes = TerrainShapeDefinitions.pointCodes(shape);
    int[] vertexIndices = new int[pointCodes.length];
    int[] underlayVertexColors = new int[pointCodes.length];
    int[] overlayVertexColors = new int[pointCodes.length];
    int[] texturedVertexColors = new int[pointCodes.length];
    float[] terrainVertexX = new float[pointCodes.length];
    float[] terrainVertexZ = new float[pointCodes.length];
    for (int pointIndex = 0; pointIndex < pointCodes.length; pointIndex++) {
      int pointCode = TerrainShapeDefinitions.rotatePointCode(pointCodes[pointIndex], rotation);
      vertexIndices[pointIndex] = builder.addVertexPoint(
          tileX,
          tileY,
          pointCode,
          heights.northWest(),
          heights.northEast(),
          heights.southEast(),
          heights.southWest()
      );
      terrainVertexX[pointIndex] = TerrainShapeDefinitions.pointX(tileX, pointCode);
      terrainVertexZ[pointIndex] = TerrainShapeDefinitions.pointZ(tileY, pointCode);
      underlayVertexColors[pointIndex] = pointColor(pointCode, underlayColors);
      overlayVertexColors[pointIndex] = pointColor(pointCode, overlayColors);
      texturedVertexColors[pointIndex] = pointColor(pointCode, texturedColors);
    }

    for (int triangleOffset = 0; triangleOffset < triangles.length; triangleOffset += 4) {
      int colorSelector = triangles[triangleOffset];
      int vertexA = TerrainShapeDefinitions.rotateTriangleVertexIndex(triangles[triangleOffset + 1], rotation);
      int vertexB = TerrainShapeDefinitions.rotateTriangleVertexIndex(triangles[triangleOffset + 2], rotation);
      int vertexC = TerrainShapeDefinitions.rotateTriangleVertexIndex(triangles[triangleOffset + 3], rotation);
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
      TerrainCornerHeights heights,
      TerrainCornerColors colors
  ) {
    int vertexNorthWest = builder.addVertex(tileX, heights.northWest(), tileY);
    int vertexNorthEast = builder.addVertex(tileX + 1.0f, heights.northEast(), tileY);
    int vertexSouthEast = builder.addVertex(tileX + 1.0f, heights.southEast(), tileY + 1.0f);
    int vertexSouthWest = builder.addVertex(tileX, heights.southWest(), tileY + 1.0f);

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
        colors.southEast(),
        colors.southWest(),
        colors.northEast()
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
        colors.northEast(),
        colors.southWest(),
        colors.northWest()
    );
  }

  private void appendTexturedPaintTile(
      SceneTriangleMeshBuilder builder,
      int tileX,
      int tileY,
      TerrainCornerHeights heights,
      TerrainCornerColors colors,
      int textureId
  ) {
    int vertexNorthWest = builder.addVertex(tileX, heights.northWest(), tileY);
    int vertexNorthEast = builder.addVertex(tileX + 1.0f, heights.northEast(), tileY);
    int vertexSouthEast = builder.addVertex(tileX + 1.0f, heights.southEast(), tileY + 1.0f);
    int vertexSouthWest = builder.addVertex(tileX, heights.southWest(), tileY + 1.0f);
    boolean flatTerrainQuad = isFlatTerrainQuad(heights);

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
        colors.southEast(),
        colors.southWest(),
        colors.northEast(),
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
        colors.northEast(),
        colors.southWest(),
        colors.northWest(),
        255,
        textureId,
        vertexNorthWest,
        vertexNorthEast,
        vertexSouthWest
    );
  }

  private boolean isFlatTerrainQuad(TerrainCornerHeights heights) {
    return heights.northWest() == heights.northEast()
        && heights.northWest() == heights.southEast()
        && heights.northWest() == heights.southWest();
  }

  private void appendBridgeLowerPaintTiles(
      SceneTriangleMeshBuilder builder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow
  ) {
    BridgeTerrainLayer bridgeTerrainLayer = worldScene.bridgeTerrainLayer();
    if (bridgeTerrainLayer.isEmpty()) {
      return;
    }
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (!bridgeTerrainLayer.activeAt(tileX, tileY) || bridgeTerrainLayer.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        if (TerrainTileColorResolver.activePaintTextureId(bridgeTerrainLayer, tileX, tileY, ENABLE_TEXTURED_TERRAIN) >= 0) {
          continue;
        }
        TerrainTileColorResolver.FloorColorLayer paintLayer = TerrainTileColorResolver.paintLayer(bridgeTerrainLayer, tileX, tileY);
        int rgb = TerrainTileColorResolver.paintLayerColor(
            bridgeTerrainLayer,
            tileX,
            tileY,
            paintLayer,
            bridgeTerrainLayer.tileColorAt(tileX, tileY)
        );
        appendPaintTile(
            builder,
            tileX,
            tileY,
            cornerHeights(bridgeTerrainLayer, tileX, tileY),
            cornerColors(bridgeTerrainLayer, tileX, tileY, paintLayer, rgb)
        );
      }
    }
  }

  private void appendBridgeLowerTexturedPaintTiles(
      SceneTriangleMeshBuilder builder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow
  ) {
    BridgeTerrainLayer bridgeTerrainLayer = worldScene.bridgeTerrainLayer();
    if (bridgeTerrainLayer.isEmpty()) {
      return;
    }
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (!bridgeTerrainLayer.activeAt(tileX, tileY) || bridgeTerrainLayer.overlayShapeAt(tileX, tileY) > 0) {
          continue;
        }
        int textureId = TerrainTileColorResolver.activePaintTextureId(bridgeTerrainLayer, tileX, tileY, ENABLE_TEXTURED_TERRAIN);
        if (textureId < 0) {
          continue;
        }
        appendTexturedPaintTile(
            builder,
            tileX,
            tileY,
            cornerHeights(bridgeTerrainLayer, tileX, tileY),
            textureCornerColors(bridgeTerrainLayer, tileX, tileY),
            textureId
        );
      }
    }
  }

  private void appendBridgeLowerShapedTiles(
      SceneTriangleMeshBuilder builder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      boolean texturedOnly
  ) {
    BridgeTerrainLayer bridgeTerrainLayer = worldScene.bridgeTerrainLayer();
    if (bridgeTerrainLayer.isEmpty()) {
      return;
    }
    for (int tileY = visibilityWindow.minLocalY(); tileY <= visibilityWindow.maxLocalY(); tileY++) {
      for (int tileX = visibilityWindow.minLocalX(); tileX <= visibilityWindow.maxLocalX(); tileX++) {
        if (!bridgeTerrainLayer.activeAt(tileX, tileY)) {
          continue;
        }
        int shape = bridgeTerrainLayer.overlayShapeAt(tileX, tileY);
        if (!TerrainShapeDefinitions.isSupportedShape(shape)) {
          continue;
        }
        appendShapedTile(builder, bridgeTerrainLayer, tileX, tileY, shape, bridgeTerrainLayer.overlayRotationAt(tileX, tileY), texturedOnly);
      }
    }
  }

  private TerrainCornerHeights cornerHeights(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return new TerrainCornerHeights(
        terrainLayerSource.elevationAt(tileX, tileY) * WorldSceneScale.HEIGHT_SCALE,
        terrainLayerSource.elevationAt(tileX + 1, tileY) * WorldSceneScale.HEIGHT_SCALE,
        terrainLayerSource.elevationAt(tileX + 1, tileY + 1) * WorldSceneScale.HEIGHT_SCALE,
        terrainLayerSource.elevationAt(tileX, tileY + 1) * WorldSceneScale.HEIGHT_SCALE
    );
  }

  private TerrainCornerColors cornerColors(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      TerrainTileColorResolver.FloorColorLayer layer,
      int fallbackRgb
  ) {
    return new TerrainCornerColors(
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX, tileY, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX + 1, tileY, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX + 1, tileY + 1, layer, fallbackRgb),
        TerrainTileColorResolver.cornerColor(terrainLayerSource, tileX, tileY + 1, layer, fallbackRgb)
    );
  }

  private TerrainCornerColors textureCornerColors(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    return new TerrainCornerColors(
        TerrainTileColorResolver.textureCornerColor(terrainLayerSource, tileX, tileY),
        TerrainTileColorResolver.textureCornerColor(terrainLayerSource, tileX + 1, tileY),
        TerrainTileColorResolver.textureCornerColor(terrainLayerSource, tileX + 1, tileY + 1),
        TerrainTileColorResolver.textureCornerColor(terrainLayerSource, tileX, tileY + 1)
    );
  }

  private int pointColor(int pointCode, TerrainCornerColors colors) {
    return TerrainTileColorResolver.pointColor(
        pointCode,
        colors.northWest(),
        colors.northEast(),
        colors.southEast(),
        colors.southWest()
    );
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

  private WorldSceneVisibilityWindow fullWindow(WorldScene worldScene) {
    return new WorldSceneVisibilityWindow(0, worldScene.tileWidth() - 2, 0, worldScene.tileHeight() - 2);
  }

  private record TerrainCornerHeights(float northWest, float northEast, float southEast, float southWest) {
  }

  private record TerrainCornerColors(int northWest, int northEast, int southEast, int southWest) {
  }
}
