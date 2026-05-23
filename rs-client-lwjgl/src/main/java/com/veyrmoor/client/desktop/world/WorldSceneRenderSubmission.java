package com.veyrmoor.client.desktop.world;

import com.veyrmoor.client.desktop.world.raster.SceneRenderQueue;

public record WorldSceneRenderSubmission(
    int renderPlane,
    WorldCameraState cameraState,
    SceneRenderQueue renderQueue
) {
}
