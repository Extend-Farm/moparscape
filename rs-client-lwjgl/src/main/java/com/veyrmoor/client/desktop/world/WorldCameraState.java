package com.veyrmoor.client.desktop.world;

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
