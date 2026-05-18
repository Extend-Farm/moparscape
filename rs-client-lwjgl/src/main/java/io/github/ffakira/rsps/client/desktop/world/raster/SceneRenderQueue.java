package io.github.ffakira.rsps.client.desktop.world.raster;

import java.util.List;

public record SceneRenderQueue(List<SceneRenderBatch> batches) {

  public SceneRenderQueue {
    batches = List.copyOf(batches);
  }

  public boolean isEmpty() {
    return batches.isEmpty();
  }
}
