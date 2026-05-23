package com.veyrmoor.client.desktop.world.object;

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
    boolean contouredGround,
    int mapSceneId,
    int mapFunctionId,
    List<Integer> modelIds,
    boolean allowFallbackProxy,
    boolean castsShadow,
    boolean solid,
    boolean interactive,
    int wallDecorDisplacement,
    int animationId,
    WorldSceneObjectGeometry geometry
) {

  public WorldSceneObject {
    modelIds = List.copyOf(modelIds);
  }

  public WorldSceneObject(
      int objectId,
      String name,
      int localX,
      int localY,
      int plane,
      int type,
      int orientation,
      int sizeX,
      int sizeY,
      boolean contouredGround,
      int mapSceneId,
      int mapFunctionId,
      List<Integer> modelIds,
      boolean allowFallbackProxy,
      WorldSceneObjectGeometry geometry
  ) {
    this(
        objectId, name, localX, localY, plane, type, orientation, sizeX, sizeY,
        contouredGround, mapSceneId, mapFunctionId, modelIds, allowFallbackProxy, true, true, false, 16, -1, geometry
    );
  }

  public WorldSceneObject(
      int objectId,
      String name,
      int localX,
      int localY,
      int plane,
      int type,
      int orientation,
      int sizeX,
      int sizeY,
      boolean contouredGround,
      int mapSceneId,
      int mapFunctionId,
      List<Integer> modelIds,
      boolean allowFallbackProxy,
      boolean castsShadow,
      WorldSceneObjectGeometry geometry
  ) {
    this(
        objectId, name, localX, localY, plane, type, orientation, sizeX, sizeY,
        contouredGround, mapSceneId, mapFunctionId, modelIds, allowFallbackProxy, castsShadow, true, false, 16, -1, geometry
    );
  }

  public WorldSceneObject(
      int objectId,
      String name,
      int localX,
      int localY,
      int plane,
      int type,
      int orientation,
      int sizeX,
      int sizeY,
      boolean contouredGround,
      int mapSceneId,
      int mapFunctionId,
      List<Integer> modelIds,
      boolean allowFallbackProxy,
      boolean castsShadow,
      boolean solid,
      boolean interactive,
      WorldSceneObjectGeometry geometry
  ) {
    this(
        objectId, name, localX, localY, plane, type, orientation, sizeX, sizeY,
        contouredGround, mapSceneId, mapFunctionId, modelIds, allowFallbackProxy,
        castsShadow, solid, interactive, 16, -1, geometry
    );
  }

  public float centerX() {
    return localX + Math.max(1, sizeX) * 0.5f;
  }

  public float centerY() {
    return localY + Math.max(1, sizeY) * 0.5f;
  }
}
