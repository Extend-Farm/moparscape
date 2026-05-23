package com.veyrmoor.client.desktop.world.terrain;

public enum TerrainSceneTileForm {
  NONE,
  FLAT_PAINT,
  TEXTURED_PAINT,
  SCENE_TILE_MODEL;

  public boolean usesPaintTile() {
    return this == FLAT_PAINT || this == TEXTURED_PAINT;
  }

  public boolean usesSceneTileModel() {
    return this == SCENE_TILE_MODEL;
  }
}
