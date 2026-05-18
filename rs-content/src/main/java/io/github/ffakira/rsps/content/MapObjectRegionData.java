package io.github.ffakira.rsps.content;

import java.util.List;

public record MapObjectRegionData(
    int regionX,
    int regionY,
    List<MapObjectPlacement> placements
) {

  public MapObjectRegionData {
    placements = List.copyOf(placements);
  }

  public static MapObjectRegionData blank(int regionX, int regionY) {
    return new MapObjectRegionData(regionX, regionY, List.of());
  }
}
