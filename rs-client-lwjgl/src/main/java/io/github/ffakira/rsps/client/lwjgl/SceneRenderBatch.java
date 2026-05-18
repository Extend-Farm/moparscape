package io.github.ffakira.rsps.client.lwjgl;

record SceneRenderBatch(
    SceneSubmissionKind kind,
    SceneRasterMode rasterMode,
    SceneTriangleMesh mesh
) {

  boolean isEmpty() {
    return mesh == null || mesh.isEmpty();
  }
}
