package io.github.ffakira.rsps.client.desktop.world.raster;

public record SceneTriangleMesh(
    float[] vertexX,
    float[] vertexY,
    float[] vertexZ,
    int[] faceVertexA,
    int[] faceVertexB,
    int[] faceVertexC,
    int[] faceColorA,
    int[] faceColorB,
    int[] faceColorC,
    int[] faceAlpha,
    int[] faceTextureIds,
    int[] textureVertexA,
    int[] textureVertexB,
    int[] textureVertexC
) {

  public static final SceneTriangleMesh EMPTY = new SceneTriangleMesh(
      new float[0],
      new float[0],
      new float[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0],
      new int[0]
  );

  public boolean isEmpty() {
    return faceVertexA.length == 0;
  }
}
