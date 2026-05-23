package com.veyrmoor.client.desktop.world.minimap;

/**
 * Per-scene-shape 4x4 minimap masks and per-rotation index remap tables. The legacy SceneGraph
 * minimap rasterizer keeps these as static state on the scene-graph class; isolating them here
 * keeps the rasterizer files focused on pixel logic rather than table definitions.
 */
final class MinimapTileShapes {

  /**
   * Shape masks: per-shape, a 4x4 boolean (1 = overlay, 0 = underlay) flattened row-major. Index
   * 0 is reserved for "no overlay"; indices 1..12 mirror the legacy curved/diagonal masks.
   */
  static final int[][] TILE_SHAPE_MASKS = {
      new int[16],
      {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1},
      {1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1},
      {0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
      {1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0},
      {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 0},
      {1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 0, 1, 1},
      {1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
      {0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
      {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1}
  };

  /**
   * Per-rotation index remap for the 4x4 mask grid. Rotation 0 is identity; 1/2/3 are 90/180/270
   * CW remappings that the legacy client also pre-bakes into a table for speed.
   */
  static final int[][] TILE_ROTATION_MAP = {
      {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
      {12, 8, 4, 0, 13, 9, 5, 1, 14, 10, 6, 2, 15, 11, 7, 3},
      {15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
      {3, 7, 11, 15, 2, 6, 10, 14, 1, 5, 9, 13, 0, 4, 8, 12}
  };

  private MinimapTileShapes() {
  }
}
