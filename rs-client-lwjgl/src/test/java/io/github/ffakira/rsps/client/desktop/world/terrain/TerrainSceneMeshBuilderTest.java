package io.github.ffakira.rsps.client.desktop.world.terrain;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneProjection;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMesh;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerrainSceneMeshBuilderTest {

  @Test
  void emitsTexturedPaintWhenFlatTilesCarryOverlayTextureIds() {
    WorldScene worldScene = testWorldScene((byte) 0, 12, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh gouraudMesh = builder.buildTilePaintMesh(worldScene);
    SceneTriangleMesh texturedMesh = builder.buildTexturedTilePaintMesh(worldScene);

    assertThat(gouraudMesh.isEmpty()).isTrue();
    assertThat(texturedMesh.isEmpty()).isFalse();
  }

  @Test
  void splitsFlatPaintTilesAcrossTheLegacyNorthEastSouthWestDiagonal() {
    WorldScene worldScene = smallWorldScene((byte) 0, -1, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTilePaintMesh(worldScene);

    assertThat(mesh.faceVertexA()).hasSize(2);
    assertThat(trianglePoints(mesh, 0)).containsExactlyInAnyOrder("1,1", "0,1", "1,0");
    assertThat(trianglePoints(mesh, 1)).containsExactlyInAnyOrder("1,0", "0,1", "0,0");
  }

  @Test
  void blendsNeighboringCornerColorsForUntexturedPaintTiles() {
    WorldScene worldScene = smallWorldScene(
        new int[]{0x803000, 0x208020, 0x203090, 0xb0b0b0},
        new int[]{0x803000, 0x208020, 0x203090, 0xb0b0b0},
        new int[]{0, 0, 0, 0},
        (byte) 0,
        -1,
        -1
    );
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTilePaintMesh(worldScene);

    assertThat(distinctFaceColors(mesh)).hasSizeGreaterThan(1);
  }

  @Test
  void splitsShapedTilesAcrossGouraudUnderlayAndTexturedOverlayBatches() {
    WorldScene worldScene = testWorldScene((byte) 2, 12, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh gouraudMesh = builder.buildTileModelMesh(worldScene);
    SceneTriangleMesh texturedMesh = builder.buildTexturedTileModelMesh(worldScene);

    assertThat(gouraudMesh.isEmpty()).isFalse();
    assertThat(texturedMesh.isEmpty()).isFalse();
  }

  @Test
  void usesFaceLocalTextureAnchorsForNonFlatTexturedPaintTriangles() {
    WorldScene worldScene = smallWorldScene(
        new int[]{0, 16, 24, 8},
        (byte) 0,
        12,
        -1
    );
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTexturedTilePaintMesh(worldScene);

    assertThat(mesh.faceVertexA()).hasSize(2);
    assertThat(textureAnchorOrder(mesh, 0)).isEqualTo(faceVertexOrder(mesh, 0));
    assertThat(textureAnchorOrder(mesh, 1)).isNotEqualTo(faceVertexOrder(mesh, 1));
  }

  @Test
  void usesFaceLocalTextureAnchorsForNonFlatTexturedShapedTiles() {
    WorldScene worldScene = smallWorldScene(
        new int[]{0, 16, 24, 8},
        (byte) 3,
        12,
        -1
    );
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTexturedTileModelMesh(worldScene);

    assertThat(mesh.isEmpty()).isFalse();
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      assertThat(textureAnchorSet(mesh, faceIndex)).isEqualTo(faceVertexSet(mesh, faceIndex));
    }
  }

  @Test
  void normalizesShapedTileTriangleWindingToPositiveTileArea() {
    WorldScene worldScene = testWorldScene((byte) 3, -1, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTileModelMesh(worldScene);

    assertThat(mesh.isEmpty()).isFalse();
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      assertThat(signedArea(mesh, faceIndex)).isGreaterThanOrEqualTo(0.0f);
    }
  }

  @Test
  void emitsTheLowerBridgeSurfaceUnderBridgeDeckTiles() {
    int width = 2;
    int height = 2;
    int[] elevations = new int[]{20, 20, 20, 20};
    int[] tileColors = filledInts(width * height, 0x505050);
    int[] underlayColors = filledInts(width * height, 0x3f5a35);
    int[] overlayColors = filledInts(width * height, 0x505050);
    int[] underlayTextureIds = filledInts(width * height, -1);
    int[] overlayTextureIds = filledInts(width * height, -1);
    byte[] overlayShapes = new byte[width * height];
    byte[] overlayRotations = new byte[width * height];
    byte[] tileFlags = new byte[width * height];

    int[] bridgeElevations = new int[]{0, 0, 0, 0};
    int[] bridgeTileColors = filledInts(width * height, 0x5a7ea3);
    int[] bridgeUnderlayColors = filledInts(width * height, 0x3f5a35);
    int[] bridgeOverlayColors = filledInts(width * height, 0x5a7ea3);
    int[] bridgeUnderlayTextureIds = filledInts(width * height, -1);
    int[] bridgeOverlayTextureIds = filledInts(width * height, 1);
    byte[] bridgeOverlayShapes = new byte[width * height];
    byte[] bridgeOverlayRotations = new byte[width * height];
    byte[] bridgeActiveTiles = new byte[]{1, 0, 0, 0};

    WorldScene worldScene = new WorldScene(
        "bridge-terrain-test",
        3200,
        3200,
        0,
        width,
        height,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width, height, filledInts(width * height, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0),
        new BridgeTerrainLayer(
            width,
            height,
            bridgeElevations,
            bridgeTileColors,
            bridgeUnderlayColors,
            bridgeOverlayColors,
            bridgeUnderlayTextureIds,
            bridgeOverlayTextureIds,
            bridgeOverlayShapes,
            bridgeOverlayRotations,
            bridgeActiveTiles
        )
    );
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh mesh = builder.buildTexturedTilePaintMesh(worldScene);

    assertThat(mesh.isEmpty()).isFalse();
    assertThat(mesh.faceTextureIds()).contains(1);
  }

  private static java.util.List<String> trianglePoints(SceneTriangleMesh mesh, int faceIndex) {
    return java.util.List.of(
        point(mesh, mesh.faceVertexA()[faceIndex]),
        point(mesh, mesh.faceVertexB()[faceIndex]),
        point(mesh, mesh.faceVertexC()[faceIndex])
    );
  }

  private static String point(SceneTriangleMesh mesh, int vertexIndex) {
    return Math.round(mesh.vertexX()[vertexIndex]) + "," + Math.round(mesh.vertexZ()[vertexIndex]);
  }

  private static float signedArea(SceneTriangleMesh mesh, int faceIndex) {
    int vertexA = mesh.faceVertexA()[faceIndex];
    int vertexB = mesh.faceVertexB()[faceIndex];
    int vertexC = mesh.faceVertexC()[faceIndex];
    float pointAX = mesh.vertexX()[vertexA];
    float pointAZ = mesh.vertexZ()[vertexA];
    float pointBX = mesh.vertexX()[vertexB];
    float pointBZ = mesh.vertexZ()[vertexB];
    float pointCX = mesh.vertexX()[vertexC];
    float pointCZ = mesh.vertexZ()[vertexC];
    return (pointBX - pointAX) * (pointCZ - pointAZ) - (pointBZ - pointAZ) * (pointCX - pointAX);
  }

  private static java.util.Set<Integer> faceVertexSet(SceneTriangleMesh mesh, int faceIndex) {
    return java.util.Set.of(
        mesh.faceVertexA()[faceIndex],
        mesh.faceVertexB()[faceIndex],
        mesh.faceVertexC()[faceIndex]
    );
  }

  private static java.util.Set<Integer> textureAnchorSet(SceneTriangleMesh mesh, int faceIndex) {
    return java.util.Set.of(
        mesh.textureVertexA()[faceIndex],
        mesh.textureVertexB()[faceIndex],
        mesh.textureVertexC()[faceIndex]
    );
  }

  private static java.util.List<Integer> faceVertexOrder(SceneTriangleMesh mesh, int faceIndex) {
    return java.util.List.of(
        mesh.faceVertexA()[faceIndex],
        mesh.faceVertexB()[faceIndex],
        mesh.faceVertexC()[faceIndex]
    );
  }

  private static java.util.List<Integer> textureAnchorOrder(SceneTriangleMesh mesh, int faceIndex) {
    return java.util.List.of(
        mesh.textureVertexA()[faceIndex],
        mesh.textureVertexB()[faceIndex],
        mesh.textureVertexC()[faceIndex]
    );
  }

  private static java.util.Set<Integer> distinctFaceColors(SceneTriangleMesh mesh) {
    java.util.Set<Integer> colors = new java.util.HashSet<>();
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      colors.add(mesh.faceColorA()[faceIndex]);
      colors.add(mesh.faceColorB()[faceIndex]);
      colors.add(mesh.faceColorC()[faceIndex]);
    }
    return colors;
  }

  private static WorldScene testWorldScene(byte overlayShape, int overlayTextureId, int underlayTextureId) {
    int width = 8;
    int height = 8;
    int[] elevations = new int[width * height];
    int[] tileColors = filledInts(width * height, 0x4a6a3c);
    int[] underlayColors = filledInts(width * height, 0x4a6a3c);
    int[] overlayColors = filledInts(width * height, 0x7d9150);
    int[] underlayTextureIds = filledInts(width * height, underlayTextureId);
    int[] overlayTextureIds = filledInts(width * height, overlayTextureId);
    byte[] overlayShapes = new byte[width * height];
    byte[] overlayRotations = new byte[width * height];
    byte[] tileFlags = new byte[width * height];
    if (overlayShape != 0) {
      overlayShapes[3 * width + 3] = overlayShape;
    }
    return new WorldScene(
        "terrain-test",
        3200,
        3200,
        0,
        width,
        height,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width, height, filledInts(width * height, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }

  private static WorldScene smallWorldScene(byte overlayShape, int overlayTextureId, int underlayTextureId) {
    int width = 2;
    int height = 2;
    int[] elevations = new int[width * height];
    int[] tileColors = filledInts(width * height, 0x4a6a3c);
    int[] underlayColors = filledInts(width * height, 0x4a6a3c);
    int[] overlayColors = filledInts(width * height, 0x7d9150);
    return smallWorldScene(
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        overlayShape,
        overlayTextureId,
        underlayTextureId
    );
  }

  private static WorldScene smallWorldScene(int[] elevations, byte overlayShape, int overlayTextureId, int underlayTextureId) {
    int[] tileColors = filledInts(elevations.length, 0x4a6a3c);
    int[] underlayColors = filledInts(elevations.length, 0x4a6a3c);
    int[] overlayColors = filledInts(elevations.length, 0x7d9150);
    return smallWorldScene(
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        overlayShape,
        overlayTextureId,
        underlayTextureId
    );
  }

  private static WorldScene smallWorldScene(
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      byte overlayShape,
      int overlayTextureId,
      int underlayTextureId
  ) {
    int[] elevations = new int[tileColors.length];
    return smallWorldScene(
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        overlayShape,
        overlayTextureId,
        underlayTextureId
    );
  }

  private static WorldScene smallWorldScene(
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      byte overlayShape,
      int overlayTextureId,
      int underlayTextureId
  ) {
    int width = 2;
    int height = 2;
    int[] underlayTextureIds = filledInts(width * height, underlayTextureId);
    int[] overlayTextureIds = filledInts(width * height, overlayTextureId);
    byte[] overlayShapes = new byte[width * height];
    byte[] overlayRotations = new byte[width * height];
    byte[] tileFlags = new byte[width * height];
    if (overlayShape != 0) {
      overlayShapes[0] = overlayShape;
    }
    return new WorldScene(
        "small-terrain-test",
        3200,
        3200,
        0,
        width,
        height,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width, height, filledInts(width * height, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }

  private static int[] filledInts(int size, int value) {
    int[] values = new int[size];
    java.util.Arrays.fill(values, value);
    return values;
  }
}
