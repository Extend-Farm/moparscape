package io.github.ffakira.rsps.client.desktop.world.terrain;

public final class BridgeTerrainLayer implements TerrainLayerSource {

  private final int tileWidth;
  private final int tileHeight;
  private final int[] elevations;
  private final int[] underlayIds;
  private final int[] overlayIds;
  private final int[] tileColors;
  private final int[] underlayColors;
  private final int[] overlayColors;
  private final int[] underlayTextureIds;
  private final int[] overlayTextureIds;
  private final byte[] overlayShapes;
  private final byte[] overlayRotations;
  private final byte[] activeTiles;
  private final boolean empty;

  public BridgeTerrainLayer(
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
      byte[] activeTiles
  ) {
    this.tileWidth = tileWidth;
    this.tileHeight = tileHeight;
    this.elevations = elevations;
    this.underlayIds = underlayIds;
    this.overlayIds = overlayIds;
    this.tileColors = tileColors;
    this.underlayColors = underlayColors;
    this.overlayColors = overlayColors;
    this.underlayTextureIds = underlayTextureIds;
    this.overlayTextureIds = overlayTextureIds;
    this.overlayShapes = overlayShapes;
    this.overlayRotations = overlayRotations;
    this.activeTiles = activeTiles;
    boolean hasActiveTiles = false;
    for (byte activeTile : activeTiles) {
      if (activeTile != 0) {
        hasActiveTiles = true;
        break;
      }
    }
    this.empty = !hasActiveTiles;
  }

  public BridgeTerrainLayer(
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
      byte[] activeTiles
  ) {
    this(
        tileWidth,
        tileHeight,
        elevations,
        new int[tileWidth * tileHeight],
        new int[tileWidth * tileHeight],
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        activeTiles
    );
  }

  public static BridgeTerrainLayer empty(int tileWidth, int tileHeight) {
    int size = tileWidth * tileHeight;
    int[] underlayIds = new int[size];
    int[] overlayIds = new int[size];
    int[] underlayTextureIds = new int[size];
    int[] overlayTextureIds = new int[size];
    java.util.Arrays.fill(underlayTextureIds, -1);
    java.util.Arrays.fill(overlayTextureIds, -1);
    return new BridgeTerrainLayer(
        tileWidth,
        tileHeight,
        new int[size],
        underlayIds,
        overlayIds,
        new int[size],
        new int[size],
        new int[size],
        underlayTextureIds,
        overlayTextureIds,
        new byte[size],
        new byte[size],
        new byte[size]
    );
  }

  public boolean isEmpty() {
    return empty;
  }

  public boolean activeAt(int localX, int localY) {
    return activeTiles[localY * tileWidth + localX] != 0;
  }

  @Override
  public int tileWidth() {
    return tileWidth;
  }

  @Override
  public int tileHeight() {
    return tileHeight;
  }

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
}
