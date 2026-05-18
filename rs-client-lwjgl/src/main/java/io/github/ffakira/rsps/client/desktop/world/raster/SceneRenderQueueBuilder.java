package io.github.ffakira.rsps.client.desktop.world.raster;

import java.util.ArrayList;
import java.util.List;

public final class SceneRenderQueueBuilder {

  private final ArrayList<SceneRenderBatch> batches = new ArrayList<>();

  public void add(SceneSubmissionKind kind, SceneRasterMode rasterMode, SceneTriangleMesh mesh) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    batches.add(new SceneRenderBatch(kind, rasterMode, mesh));
  }

  public SceneRenderQueue build() {
    return new SceneRenderQueue(List.copyOf(batches));
  }
}
