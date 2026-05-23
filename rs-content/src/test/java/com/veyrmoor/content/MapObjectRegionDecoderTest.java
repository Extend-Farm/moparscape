package com.veyrmoor.content;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.cache.CacheStoreReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import org.junit.jupiter.api.Test;

class MapObjectRegionDecoderTest {

  private static final int MAP_STORE_INDEX = 4;

  @Test
  void decodesPlacedObjectsFromAkiraRegion() throws IOException {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    ContentArchiveSnapshot archiveSnapshot = new ContentArchiveCatalog().load(manifest);
    MapArchiveIndex mapArchiveIndex = MapArchiveIndex.parse(archiveSnapshot.readUpdateListEntry("map_index"));
    MapArchiveReference akiraRegion = mapArchiveIndex.find(50, 50).orElseThrow();

    byte[] compressedBytes;
    try (CacheStoreReader cacheStoreReader = new CacheStoreReader(manifest.cacheStore())) {
      compressedBytes = cacheStoreReader.readArchive(MAP_STORE_INDEX, akiraRegion.objectArchiveId()).bytes();
    }

    MapObjectRegionData regionData = new MapObjectRegionDecoder().decode(50, 50, decompressGzip(compressedBytes));

    assertThat(regionData.regionX()).isEqualTo(50);
    assertThat(regionData.regionY()).isEqualTo(50);
    assertThat(regionData.placements()).isNotEmpty();
    assertThat(regionData.placements()).allSatisfy(placement -> {
      assertThat(placement.worldX()).isBetween(50 << 6, (50 << 6) + 63);
      assertThat(placement.worldY()).isBetween(50 << 6, (50 << 6) + 63);
      assertThat(placement.plane()).isBetween(0, 3);
      assertThat(placement.type()).isBetween(0, 22);
      assertThat(placement.orientation()).isBetween(0, 3);
    });
    Map<String, Long> placementsByTile = regionData.placements().stream()
        .collect(Collectors.groupingBy(
            placement -> placement.plane() + ":" + placement.worldX() + ":" + placement.worldY(),
            Collectors.counting()
        ));
    assertThat(placementsByTile.values().stream().anyMatch(count -> count > 1)).isTrue();
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
