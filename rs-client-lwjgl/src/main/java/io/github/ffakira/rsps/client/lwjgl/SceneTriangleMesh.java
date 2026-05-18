package io.github.ffakira.rsps.client.lwjgl;

record SceneTriangleMesh(
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

  static final SceneTriangleMesh EMPTY = new SceneTriangleMesh(
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

  boolean isEmpty() {
    return faceVertexA.length == 0;
  }
}
