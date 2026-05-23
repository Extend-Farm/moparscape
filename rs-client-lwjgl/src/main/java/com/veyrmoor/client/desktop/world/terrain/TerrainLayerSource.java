package com.veyrmoor.client.desktop.world.terrain;

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

  /**
   * Returns the elevation a tile would render with at one of its corners. The default
   * implementation shares one elevation across the shared sample point — fine for sources that
   * only have one plane (bridge layer, test fixtures). Sources with multiple terrain planes (the
   * production WorldScene) override this so a bridge deck and the water tile beside it can pick
   * different heights at the same corner. {@code tileX, tileY} is the tile that's rendering;
   * {@code cornerOffsetX, cornerOffsetY} are 0 or 1 to pick NW/NE/SW/SE.
   */
  default int tileCornerElevationAt(int tileX, int tileY, int cornerOffsetX, int cornerOffsetY) {
    return elevationAt(tileX + cornerOffsetX, tileY + cornerOffsetY);
  }
}
