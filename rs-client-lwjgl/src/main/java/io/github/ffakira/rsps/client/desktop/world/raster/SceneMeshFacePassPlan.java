package io.github.ffakira.rsps.client.desktop.world.raster;

final class SceneMeshFacePassPlan {

  private int[] opaqueFaces = new int[0];
  private int opaqueFaceCount;
  private int[] translucentFaces = new int[0];
  private int translucentFaceCount;

  int[] opaqueFaces() {
    return opaqueFaces;
  }

  int opaqueFaceCount() {
    return opaqueFaceCount;
  }

  int[] translucentFaces() {
    return translucentFaces;
  }

  int translucentFaceCount() {
    return translucentFaceCount;
  }

  boolean hasOpaqueFaces() {
    return opaqueFaceCount > 0;
  }

  boolean hasTranslucentFaces() {
    return translucentFaceCount > 0;
  }

  void clear() {
    opaqueFaceCount = 0;
    translucentFaceCount = 0;
  }

  void ensureCapacity(int requiredCapacity) {
    if (opaqueFaces.length < requiredCapacity) {
      opaqueFaces = new int[requiredCapacity];
    }
    if (translucentFaces.length < requiredCapacity) {
      translucentFaces = new int[requiredCapacity];
    }
  }

  void appendOpaqueFace(int faceIndex) {
    opaqueFaces[opaqueFaceCount++] = faceIndex;
  }

  void appendTranslucentFace(int faceIndex) {
    translucentFaces[translucentFaceCount++] = faceIndex;
  }
}
