package io.github.ffakira.rsps.content;

public final class TerrainRegionData {

  private static final int PLANE_COUNT = 4;
  private static final int REGION_WIDTH = 64;
  private static final int REGION_HEIGHT = 64;
  private static final int TILE_COUNT = REGION_WIDTH * REGION_HEIGHT;

  private final int regionX;
  private final int regionY;
  private final int[][] heightsByPlane;
  private final byte[][] underlaysByPlane;
  private final byte[][] overlaysByPlane;
  private final byte[][] overlayShapesByPlane;
  private final byte[][] overlayRotationsByPlane;
  private final byte[][] tileFlagsByPlane;

  public TerrainRegionData(
      int regionX,
      int regionY,
      int[][] heightsByPlane,
      byte[][] underlaysByPlane,
      byte[][] overlaysByPlane,
      byte[][] overlayShapesByPlane,
      byte[][] overlayRotationsByPlane,
      byte[][] tileFlagsByPlane
  ) {
    this.regionX = regionX;
    this.regionY = regionY;
    this.heightsByPlane = heightsByPlane;
    this.underlaysByPlane = underlaysByPlane;
    this.overlaysByPlane = overlaysByPlane;
    this.overlayShapesByPlane = overlayShapesByPlane;
    this.overlayRotationsByPlane = overlayRotationsByPlane;
    this.tileFlagsByPlane = tileFlagsByPlane;
  }

  public static TerrainRegionData blank(int regionX, int regionY) {
    return new TerrainRegionData(
        regionX,
        regionY,
        new int[PLANE_COUNT][TILE_COUNT],
        new byte[PLANE_COUNT][TILE_COUNT],
        new byte[PLANE_COUNT][TILE_COUNT],
        new byte[PLANE_COUNT][TILE_COUNT],
        new byte[PLANE_COUNT][TILE_COUNT],
        new byte[PLANE_COUNT][TILE_COUNT]
    );
  }

  public int regionX() {
    return regionX;
  }

  public int regionY() {
    return regionY;
  }

  public int baseWorldX() {
    return regionX << 6;
  }

  public int baseWorldY() {
    return regionY << 6;
  }

  public int heightAt(int plane, int tileX, int tileY) {
    return heightsByPlane[plane][tileIndex(tileX, tileY)];
  }

  public int underlayIdAt(int plane, int tileX, int tileY) {
    return underlaysByPlane[plane][tileIndex(tileX, tileY)] & 0xff;
  }

  public int overlayIdAt(int plane, int tileX, int tileY) {
    return overlaysByPlane[plane][tileIndex(tileX, tileY)] & 0xff;
  }

  public int overlayShapeAt(int plane, int tileX, int tileY) {
    return overlayShapesByPlane[plane][tileIndex(tileX, tileY)] & 0xff;
  }

  public int overlayRotationAt(int plane, int tileX, int tileY) {
    return overlayRotationsByPlane[plane][tileIndex(tileX, tileY)] & 0xff;
  }

  public int tileFlagAt(int plane, int tileX, int tileY) {
    return tileFlagsByPlane[plane][tileIndex(tileX, tileY)] & 0xff;
  }

  private int tileIndex(int tileX, int tileY) {
    return tileY * REGION_WIDTH + tileX;
  }
}
