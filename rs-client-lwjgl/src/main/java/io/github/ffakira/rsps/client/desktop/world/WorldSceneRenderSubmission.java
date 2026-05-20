package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueue;

public record WorldSceneRenderSubmission(
    int renderPlane,
    WorldCameraState cameraState,
    SceneRenderQueue renderQueue
) {
}
