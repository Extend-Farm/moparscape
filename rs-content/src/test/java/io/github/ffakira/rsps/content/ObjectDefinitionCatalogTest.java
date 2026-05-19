package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.CacheStoreReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import org.junit.jupiter.api.Test;

class ObjectDefinitionCatalogTest {

  private static final int MAP_STORE_INDEX = 4;

  @Test
  void loadsLiveCacheObjectDefinitionsWithModelBackedEntries() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);

    assertThat(catalog.size()).isGreaterThan(1_000);
    assertThat(catalog.find(0)).isPresent();
    assertThat(catalog.find(0).orElseThrow().name()).isNotBlank();
    long modelBackedDefinitions = countModelBackedDefinitions(catalog);
    assertThat(modelBackedDefinitions).isGreaterThan(1_000);
  }

  @Test
  void resolvesDefinitionsForPlacedObjectsAroundAkiraSpawn() throws IOException {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    MapArchiveIndex mapArchiveIndex = MapArchiveIndex.parse(archiveSnapshot.readUpdateListEntry("map_index"));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);
    MapObjectRegionDecoder decoder = new MapObjectRegionDecoder();

    MapArchiveReference akiraRegion = mapArchiveIndex.find(50, 50).orElseThrow();

    MapObjectRegionData regionData;
    try (CacheStoreReader cacheStoreReader = new CacheStoreReader(manifest.cacheStore())) {
      byte[] compressedBytes = cacheStoreReader.readArchive(MAP_STORE_INDEX, akiraRegion.objectArchiveId()).bytes();
      regionData = decoder.decode(50, 50, decompressGzip(compressedBytes));
    }

    assertThat(regionData.placements()).isNotEmpty();
    long resolvedDefinitions = regionData.placements().stream()
        .map(MapObjectPlacement::objectId)
        .distinct()
        .map(catalog::find)
        .filter(java.util.Optional::isPresent)
        .count();
    assertThat(resolvedDefinitions).isGreaterThan(20);
    assertThat(
        regionData.placements().stream()
            .map(MapObjectPlacement::objectId)
            .map(catalog::require)
            .anyMatch(definition -> !definition.modelIds().isEmpty())
    ).isTrue();
  }

  @Test
  void resolvesWallDecorationVariantPlacementsThroughModelTypeFour() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);

    assertThat(catalog.require(196).modelIdsForType(7)).isNotEmpty();
    assertThat(catalog.require(840).modelIdsForType(5)).isNotEmpty();
    assertThat(catalog.require(1646).modelIdsForType(5)).isNotEmpty();
  }

  @Test
  void preservesObjectAmbientAndContrastLightingMetadata() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);

    assertThat(catalog.require(1308).name()).isEqualTo("Willow");
    assertThat(catalog.require(1308).ambient()).isEqualTo(25);
    assertThat(catalog.require(1308).contrast()).isZero();
  }

  @Test
  void preservesMapFunctionMarkerMetadata() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);

    long definitionsWithMapFunction = 0;
    for (int objectId = 0; objectId < catalog.size(); objectId++) {
      if (catalog.require(objectId).mapFunctionId() >= 0) {
        definitionsWithMapFunction++;
      }
    }
    assertThat(definitionsWithMapFunction).isGreaterThan(20);
  }

  @Test
  void preservesContouredGroundMetadataForBridgeSideFenceObjects() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ObjectDefinitionCatalog catalog = ObjectDefinitionCatalog.load(manifest);

    assertThat(catalog.require(980).contouredGround()).isTrue();
    assertThat(catalog.require(981).contouredGround()).isTrue();
    assertThat(catalog.require(3007).contouredGround()).isTrue();
  }

  private static long countModelBackedDefinitions(ObjectDefinitionCatalog catalog) {
    long count = 0;
    for (int objectId = 0; objectId < catalog.size(); objectId++) {
      if (!catalog.require(objectId).modelIds().isEmpty()) {
        count++;
      }
    }
    return count;
  }

  private static byte[] decompressGzip(byte[] bytes) {
    if (bytes.length < 2 || (bytes[0] & 0xff) != 0x1f || (bytes[1] & 0xff) != 0x8b) {
      return bytes;
    }
    try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes))) {
      return gzipInputStream.readAllBytes();
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to decompress object archive", ioException);
    }
  }
}
