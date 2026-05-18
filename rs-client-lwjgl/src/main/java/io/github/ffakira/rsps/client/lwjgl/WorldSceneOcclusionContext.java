package io.github.ffakira.rsps.client.lwjgl;

import java.util.List;

record WorldSceneOcclusionContext(
    float cameraX,
    float cameraY,
    float cameraZ,
    List<WorldSceneOccluder> activeOccluders
) {

  WorldSceneOcclusionContext {
    activeOccluders = List.copyOf(activeOccluders);
  }
}
