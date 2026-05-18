package io.github.ffakira.rsps.client.lwjgl;

record SceneMeshFacePassPlan(
    int[] opaqueFaces,
    int[] translucentFaces
) {

  boolean hasOpaqueFaces() {
    return opaqueFaces.length > 0;
  }

  boolean hasTranslucentFaces() {
    return translucentFaces.length > 0;
  }
}
