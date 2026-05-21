package io.github.ffakira.rsps.client.desktop.world.terrain;

public final class TerrainSceneTileFormResolver {

  private TerrainSceneTileFormResolver() {
  }

  // Mirrors the legacy SceneGraph.method279 split:
  // flat paint, textured paint, or shaped tile model. The minimap and viewport terrain builders
  // should both key off this same decision so the base radar cannot drift from the scene form.
  public static TerrainSceneTileForm resolve(
      TerrainLayerSource terrainLayerSource,
      int tileX,
      int tileY,
      boolean texturedTerrainEnabled
  ) {
    int sceneShapeId = TerrainOverlayShapeResolver.sceneShapeId(terrainLayerSource, tileX, tileY);
    if (sceneShapeId > 1) {
      return TerrainShapeDefinitions.isSupportedShape(sceneShapeId)
          ? TerrainSceneTileForm.SCENE_TILE_MODEL
          : TerrainSceneTileForm.NONE;
    }
    if (!TerrainTileColorResolver.shouldRenderPaintTile(terrainLayerSource, tileX, tileY)) {
      return TerrainSceneTileForm.NONE;
    }
    return TerrainTileColorResolver.activePaintTextureId(terrainLayerSource, tileX, tileY, texturedTerrainEnabled) >= 0
        ? TerrainSceneTileForm.TEXTURED_PAINT
        : TerrainSceneTileForm.FLAT_PAINT;
  }
}
