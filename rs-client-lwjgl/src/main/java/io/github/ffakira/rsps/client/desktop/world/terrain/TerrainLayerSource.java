package io.github.ffakira.rsps.client.desktop.world.terrain;

public interface TerrainLayerSource {

  int tileWidth();

  int tileHeight();

  int elevationAt(int localX, int localY);

  default int underlayIdAt(int localX, int localY) {
    return 0;
  }

  default int overlayIdAt(int localX, int localY) {
    return 0;
  }

  int tileColorAt(int localX, int localY);

  int underlayColorAt(int localX, int localY);

  int overlayColorAt(int localX, int localY);

  int underlayTextureIdAt(int localX, int localY);

  int overlayTextureIdAt(int localX, int localY);

  int overlayShapeAt(int localX, int localY);

  int overlayRotationAt(int localX, int localY);

  default int shadowAt(int localX, int localY) {
    return 0;
  }
}
