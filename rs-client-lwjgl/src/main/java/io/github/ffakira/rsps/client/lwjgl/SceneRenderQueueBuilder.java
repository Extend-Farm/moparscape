package io.github.ffakira.rsps.client.lwjgl;

import java.util.ArrayList;
import java.util.List;

final class SceneRenderQueueBuilder {

  private final ArrayList<SceneRenderBatch> batches = new ArrayList<>();

  void add(SceneSubmissionKind kind, SceneRasterMode rasterMode, SceneTriangleMesh mesh) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    batches.add(new SceneRenderBatch(kind, rasterMode, mesh));
  }

  SceneRenderQueue build() {
    return new SceneRenderQueue(List.copyOf(batches));
  }
}
