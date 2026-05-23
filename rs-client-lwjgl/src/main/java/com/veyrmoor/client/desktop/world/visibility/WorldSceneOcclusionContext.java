package com.veyrmoor.client.desktop.world.visibility;

import java.util.List;

public record WorldSceneOcclusionContext(
    float cameraX,
    float cameraY,
    float cameraZ,
    List<WorldSceneOccluder> activeOccluders
) {

  public WorldSceneOcclusionContext {
    activeOccluders = List.copyOf(activeOccluders);
  }
}
