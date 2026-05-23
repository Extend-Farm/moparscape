package com.veyrmoor.client.desktop.world.minimap;

import com.veyrmoor.client.desktop.world.terrain.TerrainLayerSource;

/**
 * Adapts the raw per-tile arrays the minimap rasterizer receives into the {@link
 * TerrainLayerSource} contract that the terrain-classification helpers expect. Lives in its own
 * file so {@link WorldSceneMinimapRasterizer} stays focused on orchestration.
 */
record MinimapTerrainLayerSource(
    int tileWidth,
    int tileHeight,
    int[] elevations,
    int[] underlayIds,
    int[] overlayIds,
    int[] tileColors,
    int[] underlayColors,
    int[] overlayColors,
    int[] underlayTextureIds,
    int[] overlayTextureIds,
    byte[] overlayShapes,
    byte[] overlayRotations,
    byte[] shadowSamples
) implements TerrainLayerSource {

  @Override
  public int elevationAt(int localX, int localY) {
    return elevations[localY * tileWidth + localX];
  }

  @Override
  public int underlayIdAt(int localX, int localY) {
    return underlayIds[localY * tileWidth + localX];
  }

  @Override
  public int overlayIdAt(int localX, int localY) {
    return overlayIds[localY * tileWidth + localX];
  }

  @Override
  public int tileColorAt(int localX, int localY) {
    return tileColors[localY * tileWidth + localX];
  }

  @Override
  public int underlayColorAt(int localX, int localY) {
    return underlayColors[localY * tileWidth + localX];
  }

  @Override
  public int overlayColorAt(int localX, int localY) {
    return overlayColors[localY * tileWidth + localX];
  }

  @Override
  public int underlayTextureIdAt(int localX, int localY) {
    return underlayTextureIds[localY * tileWidth + localX];
  }

  @Override
  public int overlayTextureIdAt(int localX, int localY) {
    return overlayTextureIds[localY * tileWidth + localX];
  }

  @Override
  public int overlayShapeAt(int localX, int localY) {
    return overlayShapes[localY * tileWidth + localX] & 0xff;
  }

  @Override
  public int overlayRotationAt(int localX, int localY) {
    return overlayRotations[localY * tileWidth + localX] & 0xff;
  }

  @Override
  public int shadowAt(int localX, int localY) {
    return shadowSamples[localY * tileWidth + localX] & 0xff;
  }
}
