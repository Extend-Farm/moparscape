package com.veyrmoor.client.desktop.world.raster;

public record SceneRenderBatch(
    SceneSubmissionKind kind,
    SceneRasterMode rasterMode,
    SceneTriangleMesh mesh
) {

  public boolean isEmpty() {
    return mesh == null || mesh.isEmpty();
  }
}
