package com.veyrmoor.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MapArchiveIndex {

  private static final int RECORD_LENGTH = 7;

  private final Map<Integer, MapArchiveReference> referencesByRegionId;

  private MapArchiveIndex(Map<Integer, MapArchiveReference> referencesByRegionId) {
    this.referencesByRegionId = Map.copyOf(referencesByRegionId);
  }

  public static MapArchiveIndex parse(byte[] bytes) {
    if (bytes.length % RECORD_LENGTH != 0) {
      throw new IllegalArgumentException("map_index length must be a multiple of " + RECORD_LENGTH);
    }

    Map<Integer, MapArchiveReference> references = new HashMap<>(bytes.length / RECORD_LENGTH);
    for (int offset = 0; offset < bytes.length; offset += RECORD_LENGTH) {
      int regionId = readUnsignedShort(bytes, offset);
      references.put(
          regionId,
          new MapArchiveReference(
              regionId,
              readUnsignedShort(bytes, offset + 2),
              readUnsignedShort(bytes, offset + 4),
              (bytes[offset + 6] & 0xff) != 0
          )
      );
    }
    return new MapArchiveIndex(references);
  }

  public Optional<MapArchiveReference> find(int regionX, int regionY) {
    return Optional.ofNullable(referencesByRegionId.get(regionId(regionX, regionY)));
  }

  public static int regionId(int regionX, int regionY) {
    return ((regionX & 0xff) << 8) | (regionY & 0xff);
  }

  private static int readUnsignedShort(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 8) | (bytes[offset + 1] & 0xff);
  }
}
