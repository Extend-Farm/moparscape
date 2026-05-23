package com.veyrmoor.client.desktop.world;

import com.veyrmoor.client.desktop.world.raster.SceneRenderQueue;

public record WorldSceneRenderSubmission(
    int renderPlane,
    WorldCameraState cameraState,
    SceneRenderQueue renderQueue,
    ActorOverheadAnchor localPlayerOverheadAnchor
) {

  public WorldSceneRenderSubmission(int renderPlane, WorldCameraState cameraState, SceneRenderQueue renderQueue) {
    this(renderPlane, cameraState, renderQueue, null);
  }

  public record ActorOverheadAnchor(
      float localX,
      float localY,
      float worldHeight
  ) {
  }
}
