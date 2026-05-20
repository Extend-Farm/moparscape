package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import io.github.ffakira.rsps.cache.CacheArchiveRepository;
import io.github.ffakira.rsps.cache.CacheStoreReader;
import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder;
import io.github.ffakira.rsps.content.ContentArchiveCatalog;
import io.github.ffakira.rsps.content.ContentArchiveSnapshot;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.FloorColorCatalog;
import io.github.ffakira.rsps.content.MapArchiveIndex;
import io.github.ffakira.rsps.content.MapObjectPlacement;
import io.github.ffakira.rsps.content.MapObjectRegionData;
import io.github.ffakira.rsps.content.MapObjectRegionDecoder;
import io.github.ffakira.rsps.content.MapArchiveReference;
import io.github.ffakira.rsps.content.ObjectDefinitionCatalog;
import io.github.ffakira.rsps.content.TerrainRegionData;
import io.github.ffakira.rsps.content.TerrainRegionDecoder;
import io.github.ffakira.rsps.content.TopLevelArchiveId;
import io.github.ffakira.rsps.client.desktop.world.minimap.WorldSceneMinimapRasterizer;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectAssembler;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
import io.github.ffakira.rsps.client.desktop.world.terrain.FloorSurfaceColorResolver;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainShadowResolver;
import io.github.ffakira.rsps.client.desktop.world.terrain.TerrainSurfaceElevationResolver;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOccluder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOccluderBuilder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldScenePlaneRules;
import io.github.ffakira.rsps.model.WorldPoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class CacheBackedWorldSceneLoader {

  private static final int SCENE_REGION_SPAN = 2;
  private static final int REGION_SIZE = 64;
  private static final int SCENE_TILE_SIZE = SCENE_REGION_SPAN * REGION_SIZE;
  private static final int TOP_LEVEL_ARCHIVE_STORE = 0;
  private static final int MAP_STORE_INDEX = 4;
  private static final int TILE_HALF_WIDTH = 5;
  private static final int TILE_HALF_HEIGHT = 3;
  private static final int IMAGE_PADDING = 12;
  private static final int PLANE_COUNT = 4;
  private final ContentManifest manifest;
  private final FloorColorCatalog floorColors;
  private final FloorSurfaceColorResolver floorSurfaceColorResolver;
  private final MapArchiveIndex mapArchiveIndex;
  private final WorldSceneObjectAssembler objectAssembler;
  private final WorldSceneOccluderBuilder occluderBuilder = new WorldSceneOccluderBuilder();
  private final WorldSceneMinimapRasterizer minimapRasterizer;
  private final TerrainSurfaceElevationResolver terrainSurfaceElevationResolver = new TerrainSurfaceElevationResolver();
  private final TerrainShadowResolver terrainShadowResolver = new TerrainShadowResolver();
  private final TerrainRegionDecoder terrainRegionDecoder = new TerrainRegionDecoder();
  private final MapObjectRegionDecoder mapObjectRegionDecoder = new MapObjectRegionDecoder();
  private final Map<Integer, TerrainRegionData> terrainRegionsById = new HashMap<>();
  private final Map<Integer, MapObjectRegionData> objectRegionsById = new HashMap<>();
  private final Map<String, WorldScene> scenesByKey = new HashMap<>();

  /**
   * Builds native world scenes directly from the checked-in cache layout used by the rewrite.
   *
   * <p>This loader currently assembles terrain plus placed-object scenes: it reads floor colors and
   * object definitions from config archives, resolves terrain and object region archives from
   * {@code map_index}, and converts the selected plane into:
   *
   * <p>1. a projected scene image used by the current world viewport
   * <p>2. a minimap image for the {@code mapback} panel
   * <p>3. raw height/color arrays and placed object metadata that later slices can feed into a
   *     fuller scene-graph/raster parity pipeline
   */
  public CacheBackedWorldSceneLoader(Path workingDirectory) {
    this.manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    this.floorColors = FloorColorCatalog.parse(archiveSnapshot.readConfigEntry("flo.dat"));
    this.floorSurfaceColorResolver = new FloorSurfaceColorResolver(floorColors);
    this.mapArchiveIndex = MapArchiveIndex.parse(archiveSnapshot.readUpdateListEntry("map_index"));
    ObjectDefinitionCatalog objectDefinitions = ObjectDefinitionCatalog.parse(
        archiveSnapshot.readConfigEntry("loc.idx"),
        archiveSnapshot.readConfigEntry("loc.dat")
    );
    this.objectAssembler = new WorldSceneObjectAssembler(
        objectDefinitions,
        new RawModelRepository(manifest.cacheStore())
    );
    this.minimapRasterizer = new WorldSceneMinimapRasterizer(loadMapSceneSprites(manifest));
  }

  public WorldScene load(WorldPoint worldPoint) {
    // Coordinate spaces in this loader:
    // - world coordinates: absolute RS tile positions from the runtime
    // - region coordinates: 64x64 cache regions referenced by map_index
    // - scene coordinates: a stitched 2x2-region local tile grid around the player
    // - pixel coordinates: temporary projected viewport/minimap outputs derived from that grid
    String sceneKey = sceneKeyFor(worldPoint);
    WorldScene cachedScene = scenesByKey.get(sceneKey);
    if (cachedScene != null) {
      return cachedScene;
    }

    int startRegionX = sceneStartRegion(worldPoint.x());
    int startRegionY = sceneStartRegion(worldPoint.y());
    int originWorldX = startRegionX << 6;
    int originWorldY = startRegionY << 6;
    int[] underlayIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] overlayIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] tileColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] underlayColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] overlayColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] underlayTextureIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] overlayTextureIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    java.util.Arrays.fill(underlayTextureIds, -1);
    java.util.Arrays.fill(overlayTextureIds, -1);
    byte[] overlayShapes = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    byte[] overlayRotations = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    byte[] tileFlags = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerUnderlayIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerOverlayIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerTileColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerUnderlayColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerOverlayColors = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerUnderlayTextureIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[] bridgeLowerOverlayTextureIds = new int[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    java.util.Arrays.fill(bridgeLowerUnderlayTextureIds, -1);
    java.util.Arrays.fill(bridgeLowerOverlayTextureIds, -1);
    byte[] bridgeLowerOverlayShapes = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    byte[] bridgeLowerOverlayRotations = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    byte[] bridgeLowerActiveTiles = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    int[][] heightSamplesByPlane = new int[PLANE_COUNT][SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    byte[] surfacePlanes = new byte[SCENE_TILE_SIZE * SCENE_TILE_SIZE];
    List<WorldSceneObject> sceneObjects = new ArrayList<>();

    // The viewport is assembled from a small 2x2 region window around the player rather than a
    // single 64x64 region so short-range movement does not force a cache reload every time the
    // player crosses a region edge.
    for (int regionOffsetX = 0; regionOffsetX < SCENE_REGION_SPAN; regionOffsetX++) {
      for (int regionOffsetY = 0; regionOffsetY < SCENE_REGION_SPAN; regionOffsetY++) {
        TerrainRegionData regionData = loadTerrainRegion(startRegionX + regionOffsetX, startRegionY + regionOffsetY);
        captureRegion(
            regionData,
            worldPoint.plane(),
            underlayIds,
            overlayIds,
            overlayShapes,
            overlayRotations,
            tileFlags,
            bridgeLowerUnderlayIds,
            bridgeLowerOverlayIds,
            bridgeLowerOverlayShapes,
            bridgeLowerOverlayRotations,
            bridgeLowerActiveTiles,
            heightSamplesByPlane,
            surfacePlanes,
            originWorldX,
            originWorldY
        );
        MapObjectRegionData objectRegionData = loadObjectRegion(startRegionX + regionOffsetX, startRegionY + regionOffsetY);
        captureObjects(regionData, objectRegionData, worldPoint.plane(), originWorldX, originWorldY, sceneObjects);
      }
    }
    floorSurfaceColorResolver.resolveSceneColors(
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        underlayIds,
        overlayIds,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        tileColors
    );
    floorSurfaceColorResolver.resolveSceneColors(
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        bridgeLowerUnderlayIds,
        bridgeLowerOverlayIds,
        bridgeLowerUnderlayColors,
        bridgeLowerOverlayColors,
        bridgeLowerUnderlayTextureIds,
        bridgeLowerOverlayTextureIds,
        bridgeLowerTileColors
    );
    byte[] terrainShadows = terrainShadowResolver.resolve(SCENE_TILE_SIZE, SCENE_TILE_SIZE, sceneObjects);
    // Visible bridge/water seams are shared-corner problems. The loader now keeps per-plane
    // height samples through region capture and resolves the final scene-local corner height from
    // the adjacent visible surfaces afterwards instead of flattening each point to one tile plane.
    int[] tileElevations = terrainSurfaceElevationResolver.resolve(
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        surfacePlanes,
        heightSamplesByPlane
    );
    BridgeTerrainLayer bridgeTerrainLayer = new BridgeTerrainLayer(
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        java.util.Arrays.copyOf(heightSamplesByPlane[0], heightSamplesByPlane[0].length),
        bridgeLowerTileColors,
        bridgeLowerUnderlayColors,
        bridgeLowerOverlayColors,
        bridgeLowerUnderlayTextureIds,
        bridgeLowerOverlayTextureIds,
        bridgeLowerOverlayShapes,
        bridgeLowerOverlayRotations,
        bridgeLowerActiveTiles
    );

    WorldSceneProjection projection = buildProjection(tileElevations);
    List<WorldSceneOccluder> occluders = occluderBuilder.build(
        worldPoint.plane(),
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        tileElevations,
        sceneObjects
    );
    ArgbImage image = renderProjectedScene(tileColors, tileElevations, projection);
    ArgbImage minimapImage = renderMinimap(
        tileElevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        terrainShadows,
        sceneObjects
    );

    WorldScene worldScene = new WorldScene(
        sceneKey,
        originWorldX,
        originWorldY,
        worldPoint.plane(),
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        tileElevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        tileFlags,
        terrainShadows,
        sceneObjects,
        occluders,
        image,
        minimapImage,
        projection,
        bridgeTerrainLayer
    );
    scenesByKey.put(sceneKey, worldScene);
    return worldScene;
  }

  public void clearSceneCache() {
    scenesByKey.clear();
  }

  private TerrainRegionData loadTerrainRegion(int regionX, int regionY) {
    int regionId = MapArchiveIndex.regionId(regionX, regionY);
    TerrainRegionData cachedRegion = terrainRegionsById.get(regionId);
    if (cachedRegion != null) {
      return cachedRegion;
    }

    TerrainRegionData regionData = mapArchiveIndex.find(regionX, regionY)
        .filter(reference -> reference.terrainArchiveId() >= 0)
        .map(reference -> readTerrainRegion(reference, regionX, regionY))
        .orElseGet(() -> TerrainRegionData.blank(regionX, regionY));
    terrainRegionsById.put(regionId, regionData);
    return regionData;
  }

  private MapObjectRegionData loadObjectRegion(int regionX, int regionY) {
    int regionId = MapArchiveIndex.regionId(regionX, regionY);
    MapObjectRegionData cachedRegion = objectRegionsById.get(regionId);
    if (cachedRegion != null) {
      return cachedRegion;
    }

    MapObjectRegionData regionData = mapArchiveIndex.find(regionX, regionY)
        .filter(reference -> reference.objectArchiveId() >= 0)
        .map(reference -> readObjectRegion(reference, regionX, regionY))
        .orElseGet(() -> MapObjectRegionData.blank(regionX, regionY));
    objectRegionsById.put(regionId, regionData);
    return regionData;
  }

  private TerrainRegionData readTerrainRegion(MapArchiveReference reference, int regionX, int regionY) {
    try (CacheStoreReader cacheStoreReader = new CacheStoreReader(manifest.cacheStore())) {
      // Update list store 4 contains the terrain/object region payloads referenced by map_index.
      // Terrain and object archives are now loaded together, even though the current renderer
      // still turns objects into simple proxy geometry instead of true decoded RS models.
      byte[] compressedBytes = cacheStoreReader.readArchive(MAP_STORE_INDEX, reference.terrainArchiveId()).bytes();
      return terrainRegionDecoder.decode(regionX, regionY, decompressGzip(compressedBytes));
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to close cache reader for region " + reference.regionId(), ioException);
    } catch (RuntimeException runtimeException) {
      throw new IllegalStateException("Failed to load terrain region " + reference.regionId(), runtimeException);
    }
  }

  private MapObjectRegionData readObjectRegion(MapArchiveReference reference, int regionX, int regionY) {
    try (CacheStoreReader cacheStoreReader = new CacheStoreReader(manifest.cacheStore())) {
      byte[] compressedBytes = cacheStoreReader.readArchive(MAP_STORE_INDEX, reference.objectArchiveId()).bytes();
      return mapObjectRegionDecoder.decode(regionX, regionY, decompressGzip(compressedBytes));
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to close cache reader for object region " + reference.regionId(), ioException);
    } catch (RuntimeException runtimeException) {
      throw new IllegalStateException("Failed to load object region " + reference.regionId(), runtimeException);
    }
  }

  private void captureRegion(
      TerrainRegionData regionData,
      int plane,
      int[] underlayIds,
      int[] overlayIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] tileFlags,
      int[] bridgeLowerUnderlayIds,
      int[] bridgeLowerOverlayIds,
      byte[] bridgeLowerOverlayShapes,
      byte[] bridgeLowerOverlayRotations,
      byte[] bridgeLowerActiveTiles,
      int[][] heightSamplesByPlane,
      byte[] surfacePlanes,
      int originWorldX,
      int originWorldY
  ) {
    // Region tiles are copied into a stitched scene-local buffer. The origin values anchor that
    // buffer back to absolute world space so later render and minimap steps can translate the
    // local player position correctly.
    for (int tileX = 0; tileX < REGION_SIZE; tileX++) {
      for (int tileY = 0; tileY < REGION_SIZE; tileY++) {
        int worldX = regionData.baseWorldX() + tileX;
        int worldY = regionData.baseWorldY() + tileY;
        int sceneX = worldX - originWorldX;
        int sceneY = worldY - originWorldY;
        if (sceneX < 0 || sceneY < 0 || sceneX >= SCENE_TILE_SIZE || sceneY >= SCENE_TILE_SIZE) {
          continue;
        }
        int surfacePlane = WorldScenePlaneRules.surfacePlane(regionData, plane, tileX, tileY);
        int sceneIndex = sceneY * SCENE_TILE_SIZE + sceneX;
        for (int samplePlane = 0; samplePlane < PLANE_COUNT; samplePlane++) {
          heightSamplesByPlane[samplePlane][sceneIndex] = tileElevation(regionData, samplePlane, tileX, tileY);
        }
        surfacePlanes[sceneIndex] = (byte) surfacePlane;
        underlayIds[sceneIndex] = regionData.underlayIdAt(surfacePlane, tileX, tileY);
        overlayIds[sceneIndex] = regionData.overlayIdAt(surfacePlane, tileX, tileY);
        overlayShapes[sceneIndex] = (byte) regionData.overlayShapeAt(surfacePlane, tileX, tileY);
        overlayRotations[sceneIndex] = (byte) regionData.overlayRotationAt(surfacePlane, tileX, tileY);
        tileFlags[sceneIndex] = (byte) regionData.tileFlagAt(plane, tileX, tileY);
        captureBridgeLowerSurface(
            regionData,
            plane,
            tileX,
            tileY,
            sceneIndex,
            bridgeLowerUnderlayIds,
            bridgeLowerOverlayIds,
            bridgeLowerOverlayShapes,
            bridgeLowerOverlayRotations,
            bridgeLowerActiveTiles
        );
      }
    }
  }

  private void captureObjects(
      TerrainRegionData terrainRegionData,
      MapObjectRegionData objectRegionData,
      int plane,
      int originWorldX,
      int originWorldY,
      List<WorldSceneObject> sceneObjects
  ) {
    for (MapObjectPlacement placement : objectRegionData.placements()) {
      int regionTileX = placement.worldX() - terrainRegionData.baseWorldX();
      int regionTileY = placement.worldY() - terrainRegionData.baseWorldY();
      if (regionTileX < 0 || regionTileY < 0 || regionTileX >= REGION_SIZE || regionTileY >= REGION_SIZE) {
        continue;
      }
      int scenePlane = WorldScenePlaneRules.objectScenePlane(
          terrainRegionData,
          plane,
          placement.plane(),
          regionTileX,
          regionTileY
      );
      if (scenePlane < 0) {
        continue;
      }
      int localX = placement.worldX() - originWorldX;
      int localY = placement.worldY() - originWorldY;
      if (localX < 0 || localY < 0 || localX >= SCENE_TILE_SIZE || localY >= SCENE_TILE_SIZE) {
        continue;
      }
      MapObjectPlacement visiblePlacement = scenePlane == placement.plane()
          ? placement
          : new MapObjectPlacement(
              placement.objectId(),
              placement.worldX(),
              placement.worldY(),
              scenePlane,
              placement.type(),
              placement.orientation()
          );
      sceneObjects.add(objectAssembler.assemble(visiblePlacement, originWorldX, originWorldY));
    }
  }

  private int tileElevation(TerrainRegionData regionData, int plane, int tileX, int tileY) {
    return Math.max(0, -regionData.heightAt(plane, tileX, tileY) / 12);
  }

  private void captureBridgeLowerSurface(
      TerrainRegionData regionData,
      int requestedPlane,
      int tileX,
      int tileY,
      int sceneIndex,
      int[] bridgeLowerUnderlayIds,
      int[] bridgeLowerOverlayIds,
      byte[] bridgeLowerOverlayShapes,
      byte[] bridgeLowerOverlayRotations,
      byte[] bridgeLowerActiveTiles
  ) {
    if (requestedPlane != 0
        || (regionData.tileFlagAt(1, tileX, tileY) & 2) == 0
        || !hasSurfaceData(regionData, 1, tileX, tileY)
        || !hasSurfaceData(regionData, 0, tileX, tileY)) {
      return;
    }
    bridgeLowerUnderlayIds[sceneIndex] = regionData.underlayIdAt(0, tileX, tileY);
    bridgeLowerOverlayIds[sceneIndex] = regionData.overlayIdAt(0, tileX, tileY);
    bridgeLowerOverlayShapes[sceneIndex] = (byte) regionData.overlayShapeAt(0, tileX, tileY);
    bridgeLowerOverlayRotations[sceneIndex] = (byte) regionData.overlayRotationAt(0, tileX, tileY);
    bridgeLowerActiveTiles[sceneIndex] = 1;
  }

  private boolean hasSurfaceData(TerrainRegionData regionData, int plane, int tileX, int tileY) {
    return regionData.underlayIdAt(plane, tileX, tileY) > 0
        || regionData.overlayIdAt(plane, tileX, tileY) > 0;
  }

  private WorldSceneProjection buildProjection(int[] tileElevations) {
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int sceneX = 0; sceneX < SCENE_TILE_SIZE; sceneX++) {
      for (int sceneY = 0; sceneY < SCENE_TILE_SIZE; sceneY++) {
        int sceneIndex = sceneY * SCENE_TILE_SIZE + sceneX;
        int centerX = (sceneX - sceneY) * TILE_HALF_WIDTH;
        int topY = (sceneX + sceneY) * TILE_HALF_HEIGHT - tileElevations[sceneIndex];
        minX = Math.min(minX, centerX - TILE_HALF_WIDTH);
        maxX = Math.max(maxX, centerX + TILE_HALF_WIDTH);
        minY = Math.min(minY, topY);
        maxY = Math.max(maxY, topY + TILE_HALF_HEIGHT * 2);
      }
    }
    return new WorldSceneProjection(
        TILE_HALF_WIDTH,
        TILE_HALF_HEIGHT,
        IMAGE_PADDING - minX,
        IMAGE_PADDING - minY
    );
  }

  private ArgbImage renderProjectedScene(
      int[] tileColors,
      int[] tileElevations,
      WorldSceneProjection projection
  ) {
    // This image is a transitional artifact for tests and parity debugging. The live viewport is
    // already using the raw height/color arrays for the temporary 3D terrain path, while the
    // projected image remains useful as a stable cache-scene snapshot during migration.
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int sceneX = 0; sceneX < SCENE_TILE_SIZE; sceneX++) {
      for (int sceneY = 0; sceneY < SCENE_TILE_SIZE; sceneY++) {
        int sceneIndex = sceneY * SCENE_TILE_SIZE + sceneX;
        int centerX = projection.projectPixelX(sceneX, sceneY);
        int topY = projection.projectPixelY(sceneX, sceneY, tileElevations[sceneIndex]);
        maxX = Math.max(maxX, centerX + TILE_HALF_WIDTH);
        maxY = Math.max(maxY, topY + TILE_HALF_HEIGHT * 2);
      }
    }
    int imageWidth = maxX + IMAGE_PADDING + 1;
    int imageHeight = maxY + IMAGE_PADDING + 1;
    int[] pixels = new int[imageWidth * imageHeight];

    for (int diagonal = 0; diagonal <= (SCENE_TILE_SIZE - 1) * 2; diagonal++) {
      int startX = Math.max(0, diagonal - (SCENE_TILE_SIZE - 1));
      int endX = Math.min(SCENE_TILE_SIZE - 1, diagonal);
      for (int sceneX = startX; sceneX <= endX; sceneX++) {
        int sceneY = diagonal - sceneX;
        int sceneIndex = sceneY * SCENE_TILE_SIZE + sceneX;
        int centerX = projection.projectPixelX(sceneX, sceneY);
        int topY = projection.projectPixelY(sceneX, sceneY, tileElevations[sceneIndex]);
        fillDiamond(
            pixels,
            imageWidth,
            imageHeight,
            centerX,
            topY,
            TILE_HALF_WIDTH,
            TILE_HALF_HEIGHT,
            tileColors[sceneIndex]
        );
      }
    }

    return new ArgbImage(imageWidth, imageHeight, pixels);
  }

  private ArgbImage renderMinimap(
      int[] tileElevations,
      int[] tileColors,
      int[] underlayColors,
      int[] overlayColors,
      int[] underlayTextureIds,
      int[] overlayTextureIds,
      byte[] overlayShapes,
      byte[] overlayRotations,
      byte[] shadowSamples,
      List<WorldSceneObject> sceneObjects
  ) {
    // Legacy minimap rendering is tile-first: it paints 4x4 tile blocks from floor data, then
    // overlays wall marks, diagonals, and selected mapscene sprites. We keep that ordering here
    // instead of filling arbitrary object footprints into the radar.
    return minimapRasterizer.rasterize(
        SCENE_TILE_SIZE,
        SCENE_TILE_SIZE,
        tileElevations,
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        shadowSamples,
        sceneObjects
    );
  }

  private ArgbImage[] loadMapSceneSprites(ContentManifest manifest) {
    try {
      CacheArchiveContainer mediaArchive = new CacheArchiveRepository(manifest.cacheStore())
          .loadArchive(TOP_LEVEL_ARCHIVE_STORE, TopLevelArchiveId.MEDIA.archiveId());
      TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(mediaArchive);
      return spriteDecoder.decodeSprites(mediaArchive, "mapscene", false);
    } catch (RuntimeException runtimeException) {
      return new ArgbImage[0];
    }
  }

  private void fillDiamond(
      int[] pixels,
      int imageWidth,
      int imageHeight,
      int centerX,
      int topY,
      int halfWidth,
      int halfHeight,
      int rgb
  ) {
    int topColor = applyBrightness(rgb, 142);
    int bottomColor = applyBrightness(rgb, 110);
    int outlineColor = applyBrightness(rgb, 84);
    int totalRows = halfHeight * 2;
    for (int row = 0; row <= totalRows; row++) {
      int dy = row - halfHeight;
      int horizontalInset = Math.abs(dy) * halfWidth / Math.max(1, halfHeight);
      int rowHalfWidth = Math.max(1, halfWidth - horizontalInset);
      int pixelY = topY + row;
      if (pixelY < 0 || pixelY >= imageHeight) {
        continue;
      }
      int fillColor = row <= halfHeight ? topColor : bottomColor;
      int startX = centerX - rowHalfWidth;
      int endX = centerX + rowHalfWidth;
      for (int pixelX = startX; pixelX <= endX; pixelX++) {
        if (pixelX < 0 || pixelX >= imageWidth) {
          continue;
        }
        int color = (pixelX == startX || pixelX == endX || row == 0 || row == totalRows) ? outlineColor : fillColor;
        pixels[pixelY * imageWidth + pixelX] = 0xff000000 | color;
      }
    }
  }

  private byte[] decompressGzip(byte[] bytes) {
    if (bytes.length < 2 || (bytes[0] & 0xff) != 0x1f || (bytes[1] & 0xff) != 0x8b) {
      return bytes;
    }
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
      return gzipInputStream.readAllBytes();
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to decompress terrain archive", ioException);
    }
  }

  private static int applyBrightness(int rgb, int brightness) {
    int red = (rgb >> 16) & 0xff;
    int green = (rgb >> 8) & 0xff;
    int blue = rgb & 0xff;
    red = red * brightness / 128;
    green = green * brightness / 128;
    blue = blue * brightness / 128;
    return (clamp(red, 0, 255) << 16) | (clamp(green, 0, 255) << 8) | clamp(blue, 0, 255);
  }

  private static int sceneStartRegion(int worldCoordinate) {
    return Math.max(0, (worldCoordinate - 32) >> 6);
  }

  public static String sceneKeyFor(WorldPoint worldPoint) {
    // The 32-tile offset centers the player inside the 2x2-region window long enough to reuse the
    // assembled scene across ordinary movement instead of thrashing the cache on every region seam.
    return sceneStartRegion(worldPoint.x()) + "_" + sceneStartRegion(worldPoint.y()) + "_" + worldPoint.plane();
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
