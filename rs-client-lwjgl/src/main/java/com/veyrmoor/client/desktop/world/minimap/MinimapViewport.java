package com.veyrmoor.client.desktop.world.minimap;

public record MinimapViewport(
    int sourceX,
    int sourceY,
    int sourceWidth,
    int sourceHeight,
    int markerSourceX,
    int markerSourceY
) {
}
