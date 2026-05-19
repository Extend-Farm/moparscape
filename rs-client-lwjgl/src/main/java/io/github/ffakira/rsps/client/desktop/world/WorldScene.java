package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOccluder;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.List;

public record WorldScene(
    String sceneKey,
    int originWorldX,
    int originWorldY,
    int plane,
    int tileWidth,
    int tileHeight,
    int[] elevations,
    int[] tileColors,
    int[] underlayColors,
    int[] overlayColors,
    int[] underlayTextureIds,
    int[] overlayTextureIds,
    byte[] overlayShapes,
    byte[] overlayRotations,
    byte[] tileFlags,
    List<WorldSceneObject> objects,
    List<WorldSceneOccluder> occluders,
    ArgbImage image,
    ArgbImage minimapImage,
    WorldSceneProjection projection,
    BridgeTerrainLayer bridgeTerrainLayer
) implements TerrainLayerSource {

  public WorldScene(
      String sceneKey,
      int originWorldX,
      int originWorldY,
      int plane,
      int tileWidth,
      int tileHeight,
      int[] elevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] tileFlags,
      List<WorldSceneObject> objects,
      List<WorldSceneOccluder> occluders,
      ArgbImage image,
      ArgbImage minimapImage,
      WorldSceneProjection projection
  ) {
    this(
        sceneKey,
        originWorldX,
        originWorldY,
        plane,
        tileWidth,
        tileHeight,
        elevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        objects,
        occluders,
        image,
        minimapImage,
        projection,
        BridgeTerrainLayer.empty(tileWidth, tileHeight)
    );
  }

  public WorldScene {
    objects = List.copyOf(objects);
    occluders = List.copyOf(occluders);
    bridgeTerrainLayer = bridgeTerrainLayer == null
        ? BridgeTerrainLayer.empty(tileWidth, tileHeight)
        : bridgeTerrainLayer;
  }

  public boolean contains(WorldPoint worldPoint) {
    if (worldPoint.plane() != plane) {
      return false;
    }
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return localX >= 0 && localY >= 0 && localX < tileWidth && localY < tileHeight;
  }

  public int projectPixelX(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return projection.projectPixelX(localX, localY);
  }

  public int projectPixelY(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return projection.projectPixelY(localX, localY, elevationAt(localX, localY));
  }

  public int projectMinimapX(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int pixelScale = minimapPixelWidthPerTile();
    return localX * pixelScale + pixelScale / 2;
  }

  public int projectMinimapY(WorldPoint worldPoint) {
    int localY = worldPoint.y() - originWorldY;
    int pixelScale = minimapPixelHeightPerTile();
    return ((tileHeight - 1) - localY) * pixelScale + pixelScale / 2;
  }

  public int minimapPixelWidthPerTile() {
    return Math.max(1, minimapImage.width() / tileWidth);
  }

  public int minimapPixelHeightPerTile() {
    return Math.max(1, minimapImage.height() / tileHeight);
  }

  public int elevationAt(int localX, int localY) {
    return elevations[localY * tileWidth + localX];
  }

  public int tileColorAt(int localX, int localY) {
    return tileColors[localY * tileWidth + localX];
  }

  public int underlayColorAt(int localX, int localY) {
    return underlayColors[localY * tileWidth + localX];
  }

  public int overlayColorAt(int localX, int localY) {
    return overlayColors[localY * tileWidth + localX];
  }

  public int underlayTextureIdAt(int localX, int localY) {
    return underlayTextureIds[localY * tileWidth + localX];
  }

  public int overlayTextureIdAt(int localX, int localY) {
    return overlayTextureIds[localY * tileWidth + localX];
  }

  public int overlayShapeAt(int localX, int localY) {
    return overlayShapes[localY * tileWidth + localX] & 0xff;
  }

  public int overlayRotationAt(int localX, int localY) {
    return overlayRotations[localY * tileWidth + localX] & 0xff;
  }

  public int tileFlagAt(int localX, int localY) {
    return tileFlags[localY * tileWidth + localX] & 0xff;
  }
}
