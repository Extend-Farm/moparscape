package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerrainSceneMeshBuilderTest {

  @Test
  void fallsBackToGouraudPaintWhenFlatTilesCarryOverlayTextureIds() {
    WorldScene worldScene = testWorldScene((byte) 0, 12, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh gouraudMesh = builder.buildTilePaintMesh(worldScene);
    SceneTriangleMesh texturedMesh = builder.buildTexturedTilePaintMesh(worldScene);

    assertThat(gouraudMesh.isEmpty()).isFalse();
    assertThat(texturedMesh.isEmpty()).isTrue();
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
  void fallsBackToGouraudTileModelsWhenShapedTilesCarryOverlayTextureIds() {
    WorldScene worldScene = testWorldScene((byte) 2, 12, -1);
    TerrainSceneMeshBuilder builder = new TerrainSceneMeshBuilder();

    SceneTriangleMesh gouraudMesh = builder.buildTileModelMesh(worldScene);
    SceneTriangleMesh texturedMesh = builder.buildTexturedTileModelMesh(worldScene);

    assertThat(gouraudMesh.isEmpty()).isFalse();
    assertThat(texturedMesh.isEmpty()).isTrue();
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
