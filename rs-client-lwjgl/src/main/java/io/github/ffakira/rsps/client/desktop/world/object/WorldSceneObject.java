package io.github.ffakira.rsps.client.desktop.world.object;

import java.util.List;

public record WorldSceneObject(
    int objectId,
    String name,
    int localX,
    int localY,
    int plane,
    int type,
    int orientation,
    int sizeX,
    int sizeY,
    int mapSceneId,
    List<Integer> modelIds,
    WorldSceneObjectGeometry geometry
) {

  public WorldSceneObject {
    modelIds = List.copyOf(modelIds);
  }

  public float centerX() {
    return localX + Math.max(1, sizeX) * 0.5f;
  }

  public float centerY() {
    return localY + Math.max(1, sizeY) * 0.5f;
  }
}
