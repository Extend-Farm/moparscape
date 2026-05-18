package io.github.ffakira.rsps.client.lwjgl;

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
    List<Integer> modelIds,
    WorldSceneObjectGeometry geometry
) {

  public WorldSceneObject {
    modelIds = List.copyOf(modelIds);
  }
}
