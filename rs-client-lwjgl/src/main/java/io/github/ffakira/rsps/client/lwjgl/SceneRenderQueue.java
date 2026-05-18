package io.github.ffakira.rsps.client.lwjgl;

import java.util.List;

record SceneRenderQueue(List<SceneRenderBatch> batches) {

  SceneRenderQueue {
    batches = List.copyOf(batches);
  }

  boolean isEmpty() {
    return batches.isEmpty();
  }
}
