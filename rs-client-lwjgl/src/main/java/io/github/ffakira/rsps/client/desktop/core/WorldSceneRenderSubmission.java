package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueue;

record WorldSceneRenderSubmission(
    WorldCameraState cameraState,
    SceneRenderQueue renderQueue
) {
}
