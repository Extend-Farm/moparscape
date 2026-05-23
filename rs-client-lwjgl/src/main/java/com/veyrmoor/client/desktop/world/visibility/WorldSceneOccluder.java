package com.veyrmoor.client.desktop.world.visibility;

public record WorldSceneOccluder(
    WorldSceneOccluderType type,
    int plane,
    float axis,
    float minLocalX,
    float maxLocalX,
    float minLocalY,
    float maxLocalY,
    float minHeight,
    float maxHeight
) {

  boolean intersectsWindow(WorldSceneVisibilityWindow visibilityWindow) {
    return maxLocalX >= visibilityWindow.minLocalX()
        && minLocalX <= visibilityWindow.maxLocalX() + 1.0f
        && maxLocalY >= visibilityWindow.minLocalY()
        && minLocalY <= visibilityWindow.maxLocalY() + 1.0f;
  }
}
