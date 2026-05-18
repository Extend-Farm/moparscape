package io.github.ffakira.rsps.client.desktop.core;

public record WorldSceneProjection(
    int tileHalfWidth,
    int tileHalfHeight,
    int originPixelX,
    int originPixelY
) {

  public int projectPixelX(int localTileX, int localTileY) {
    return originPixelX + (localTileX - localTileY) * tileHalfWidth;
  }

  public int projectPixelY(int localTileX, int localTileY, int elevation) {
    return originPixelY + (localTileX + localTileY) * tileHalfHeight - elevation;
  }
}
