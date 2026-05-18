package io.github.ffakira.rsps.client.desktop.core;

public record WorldCameraState(
    float pitchDegrees,
    float yawDegrees,
    float distance,
    float screenOffsetY,
    float focusX,
    float focusY,
    float focusHeight
) {
}
