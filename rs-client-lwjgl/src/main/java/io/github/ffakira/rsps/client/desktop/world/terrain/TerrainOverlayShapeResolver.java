package io.github.ffakira.rsps.client.desktop.world.terrain;

public final class TerrainOverlayShapeResolver {

  private TerrainOverlayShapeResolver() {
  }

  public static int sceneShapeId(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    if (!hasVisibleOverlay(terrainLayerSource, tileX, tileY)) {
      return 0;
    }
    // Cache terrain stores the raw overlay type from the landscape opcode. Legacy SceneGraph
    // promotes that type into scene shape ids where 0 means "no overlay", 1 is the full simple
    // overlay tile, and 2..12 are the curved or diagonal shaped masks.
    return terrainLayerSource.overlayShapeAt(tileX, tileY) + 1;
  }

  public static boolean hasVisibleOverlay(TerrainLayerSource terrainLayerSource, int tileX, int tileY) {
    // Legacy scene assembly first checks whether an overlay exists on the tile, then chooses how
    // that overlay resolves. The native scene needs the same rule so colorless overlays still
    // keep their shaped mask instead of disappearing.
    if (terrainLayerSource.overlayIdAt(tileX, tileY) > 0) {
      return true;
    }
    return terrainLayerSource.overlayTextureIdAt(tileX, tileY) >= 0
        || terrainLayerSource.overlayColorAt(tileX, tileY) != 0;
  }
}
