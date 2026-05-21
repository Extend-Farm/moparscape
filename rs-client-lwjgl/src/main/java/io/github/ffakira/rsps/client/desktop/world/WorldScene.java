package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.minimap.WorldSceneMapFunctionMarker;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainLayerSource;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOccluder;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.List;

public record WorldScene(
    String sceneKey,
    int originWorldX,
    int originWorldY,
    int plane,
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
    byte[] tileFlags,
    byte[] shadowSamples,
    List<WorldSceneObject> objects,
    List<WorldSceneMapFunctionMarker> mapFunctionMarkers,
    List<WorldSceneOccluder> occluders,
    ArgbImage image,
    ArgbImage minimapImage,
    WorldSceneProjection projection,
    BridgeTerrainLayer bridgeTerrainLayer,
    byte[] surfacePlanes,
    int[] terrainOcclusionFlags,
    int[][] heightSamplesByPlane
) implements TerrainLayerSource {

  public WorldScene(
      String sceneKey,
      int originWorldX,
      int originWorldY,
      int plane,
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
      byte[] tileFlags,
      List<WorldSceneObject> objects,
      List<WorldSceneOccluder> occluders,
      ArgbImage image,
      ArgbImage minimapImage,
      WorldSceneProjection projection,
      BridgeTerrainLayer bridgeTerrainLayer
  ) {
    this(
        sceneKey,
        originWorldX,
        originWorldY,
        plane,
        tileWidth,
        tileHeight,
        elevations,
        emptyFloorIds(tileWidth, tileHeight),
        emptyFloorIds(tileWidth, tileHeight),
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        emptyShadowSamples(tileWidth, tileHeight),
        objects,
        List.of(),
        occluders,
        image,
        minimapImage,
        projection,
        bridgeTerrainLayer,
        emptySurfacePlanes(tileWidth, tileHeight),
        emptyTerrainOcclusionFlags(tileWidth, tileHeight),
        null
    );
  }

  public WorldScene(
      String sceneKey,
      int originWorldX,
      int originWorldY,
      int plane,
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
      byte[] tileFlags,
      List<WorldSceneObject> objects,
      List<WorldSceneOccluder> occluders,
      ArgbImage image,
      ArgbImage minimapImage,
      WorldSceneProjection projection,
      BridgeTerrainLayer bridgeTerrainLayer
  ) {
    this(
        sceneKey,
        originWorldX,
        originWorldY,
        plane,
        tileWidth,
        tileHeight,
        elevations,
        underlayIds,
        overlayIds,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        emptyShadowSamples(tileWidth, tileHeight),
        objects,
        List.of(),
        occluders,
        image,
        minimapImage,
        projection,
        bridgeTerrainLayer,
        emptySurfacePlanes(tileWidth, tileHeight),
        emptyTerrainOcclusionFlags(tileWidth, tileHeight),
        null
    );
  }

  public WorldScene(
      String sceneKey,
      int originWorldX,
      int originWorldY,
      int plane,
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
      byte[] tileFlags,
      List<WorldSceneObject> objects,
      List<WorldSceneOccluder> occluders,
      ArgbImage image,
      ArgbImage minimapImage,
      WorldSceneProjection projection
  ) {
    this(
        sceneKey,
        originWorldX,
        originWorldY,
        plane,
        tileWidth,
        tileHeight,
        elevations,
        emptyFloorIds(tileWidth, tileHeight),
        emptyFloorIds(tileWidth, tileHeight),
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        emptyShadowSamples(tileWidth, tileHeight),
        objects,
        List.of(),
        occluders,
        image,
        minimapImage,
        projection,
        BridgeTerrainLayer.empty(tileWidth, tileHeight),
        emptySurfacePlanes(tileWidth, tileHeight),
        emptyTerrainOcclusionFlags(tileWidth, tileHeight),
        null
    );
  }

  public WorldScene(
      String sceneKey,
      int originWorldX,
      int originWorldY,
      int plane,
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
      byte[] tileFlags,
      byte[] shadowSamples,
      List<WorldSceneObject> objects,
      List<WorldSceneOccluder> occluders,
      ArgbImage image,
      ArgbImage minimapImage,
      WorldSceneProjection projection,
      BridgeTerrainLayer bridgeTerrainLayer
  ) {
    this(
        sceneKey,
        originWorldX,
        originWorldY,
        plane,
        tileWidth,
        tileHeight,
        elevations,
        emptyFloorIds(tileWidth, tileHeight),
        emptyFloorIds(tileWidth, tileHeight),
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        shadowSamples,
        objects,
        List.of(),
        occluders,
        image,
        minimapImage,
        projection,
        bridgeTerrainLayer,
        emptySurfacePlanes(tileWidth, tileHeight),
        emptyTerrainOcclusionFlags(tileWidth, tileHeight),
        null
    );
  }

  public WorldScene {
    objects = List.copyOf(objects);
    mapFunctionMarkers = mapFunctionMarkers == null ? List.of() : List.copyOf(mapFunctionMarkers);
    occluders = List.copyOf(occluders);
    underlayIds = underlayIds == null ? emptyFloorIds(tileWidth, tileHeight) : underlayIds;
    overlayIds = overlayIds == null ? emptyFloorIds(tileWidth, tileHeight) : overlayIds;
    shadowSamples = shadowSamples == null ? emptyShadowSamples(tileWidth, tileHeight) : shadowSamples;
    bridgeTerrainLayer = bridgeTerrainLayer == null
        ? BridgeTerrainLayer.empty(tileWidth, tileHeight)
        : bridgeTerrainLayer;
    surfacePlanes = surfacePlanes == null ? emptySurfacePlanes(tileWidth, tileHeight) : surfacePlanes;
    terrainOcclusionFlags = terrainOcclusionFlags == null
        ? emptyTerrainOcclusionFlags(tileWidth, tileHeight)
        : terrainOcclusionFlags;
    // Default to one-plane samples derived from the flattened elevations array. Production scenes
    // populate per-plane heights to support per-tile-per-corner mesh elevations (bridges); tests
    // and legacy callers fall through to a single plane = current elevations, which preserves
    // their existing shared-corner behaviour.
    if (heightSamplesByPlane == null) {
      heightSamplesByPlane = new int[][]{elevations};
    }
  }

  public boolean contains(WorldPoint worldPoint) {
    if (worldPoint.plane() != plane) {
      return false;
    }
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return localX >= 0 && localY >= 0 && localX < tileWidth && localY < tileHeight;
  }

  public int projectPixelX(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return projection.projectPixelX(localX, localY);
  }

  public int projectPixelY(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int localY = worldPoint.y() - originWorldY;
    return projection.projectPixelY(localX, localY, elevationAt(localX, localY));
  }

  public int projectMinimapX(WorldPoint worldPoint) {
    int localX = worldPoint.x() - originWorldX;
    int pixelScale = minimapPixelWidthPerTile();
    return localX * pixelScale + pixelScale / 2;
  }

  public int projectMinimapY(WorldPoint worldPoint) {
    int localY = worldPoint.y() - originWorldY;
    int pixelScale = minimapPixelHeightPerTile();
    return ((tileHeight - 1) - localY) * pixelScale + pixelScale / 2;
  }

  public int minimapPixelWidthPerTile() {
    return Math.max(1, minimapImage.width() / tileWidth);
  }

  public int minimapPixelHeightPerTile() {
    return Math.max(1, minimapImage.height() / tileHeight);
  }

  public int elevationAt(int localX, int localY) {
    return elevations[localY * tileWidth + localX];
  }

  public int surfacePlaneAt(int localX, int localY) {
    return surfacePlanes[localY * tileWidth + localX] & 0xff;
  }

  /**
   * Returns the elevation sample on a given plane at scene-local tile coordinates. Used by the
   * terrain mesh builder to produce per-tile-per-corner heights so a bridge deck and the water
   * tile beside it can render with different vertex positions at the shared corner — matching the
   * legacy `MapRegion` behaviour where each tile uses its own plane's height.
   */
  @Override
  public int tileCornerElevationAt(int tileX, int tileY, int cornerOffsetX, int cornerOffsetY) {
    int clampedTileX = Math.max(0, Math.min(tileWidth - 1, tileX));
    int clampedTileY = Math.max(0, Math.min(tileHeight - 1, tileY));
    int surfacePlane = surfacePlanes[clampedTileY * tileWidth + clampedTileX] & 0xff;
    return planeElevationAt(surfacePlane, tileX + cornerOffsetX, tileY + cornerOffsetY);
  }

  public int planeElevationAt(int plane, int localX, int localY) {
    int clampedX = Math.max(0, Math.min(tileWidth - 1, localX));
    int clampedY = Math.max(0, Math.min(tileHeight - 1, localY));
    int safePlane = plane >= 0 && plane < heightSamplesByPlane.length ? plane : 0;
    int[] samples = heightSamplesByPlane[safePlane];
    if (samples == null) {
      samples = elevations;
    }
    return samples[clampedY * tileWidth + clampedX];
  }

  @Override
  public int underlayIdAt(int localX, int localY) {
    return underlayIds[localY * tileWidth + localX];
  }

  @Override
  public int overlayIdAt(int localX, int localY) {
    return overlayIds[localY * tileWidth + localX];
  }

  public int tileColorAt(int localX, int localY) {
    return tileColors[localY * tileWidth + localX];
  }

  public int underlayColorAt(int localX, int localY) {
    return underlayColors[localY * tileWidth + localX];
  }

  public int overlayColorAt(int localX, int localY) {
    return overlayColors[localY * tileWidth + localX];
  }

  public int underlayTextureIdAt(int localX, int localY) {
    return underlayTextureIds[localY * tileWidth + localX];
  }

  public int overlayTextureIdAt(int localX, int localY) {
    return overlayTextureIds[localY * tileWidth + localX];
  }

  public int overlayShapeAt(int localX, int localY) {
    return overlayShapes[localY * tileWidth + localX] & 0xff;
  }

  public int overlayRotationAt(int localX, int localY) {
    return overlayRotations[localY * tileWidth + localX] & 0xff;
  }

  @Override
  public int shadowAt(int localX, int localY) {
    return shadowSamples[localY * tileWidth + localX] & 0xff;
  }

  public int tileFlagAt(int localX, int localY) {
    return tileFlags[localY * tileWidth + localX] & 0xff;
  }

  public int terrainOcclusionFlagAt(int localX, int localY) {
    return terrainOcclusionFlags[localY * tileWidth + localX];
  }

  private static byte[] emptyShadowSamples(int tileWidth, int tileHeight) {
    return new byte[tileWidth * tileHeight];
  }

  private static int[] emptyFloorIds(int tileWidth, int tileHeight) {
    return new int[tileWidth * tileHeight];
  }

  private static byte[] emptySurfacePlanes(int tileWidth, int tileHeight) {
    return new byte[tileWidth * tileHeight];
  }

  private static int[] emptyTerrainOcclusionFlags(int tileWidth, int tileHeight) {
    return new int[tileWidth * tileHeight];
  }
}
