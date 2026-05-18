package io.github.ffakira.rsps.client.lwjgl;

record WorldCameraState(
    float pitchDegrees,
    float yawDegrees,
    float distance,
    float screenOffsetY,
    float focusX,
    float focusY,
    float focusHeight
) {
}
