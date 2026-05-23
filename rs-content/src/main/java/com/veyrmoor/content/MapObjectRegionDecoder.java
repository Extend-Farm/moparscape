package com.veyrmoor.content;

import java.util.ArrayList;
import java.util.List;

public final class MapObjectRegionDecoder {

  public MapObjectRegionData decode(int regionX, int regionY, byte[] bytes) {
    ContentDataReader reader = new ContentDataReader(bytes);
    List<MapObjectPlacement> placements = new ArrayList<>();
    int objectId = -1;
    while (true) {
      int objectIdDelta = reader.readUnsignedSmart();
      if (objectIdDelta == 0) {
        return new MapObjectRegionData(regionX, regionY, placements);
      }
      objectId += objectIdDelta;

      int packedLocation = 0;
      while (true) {
        int locationDelta = reader.readUnsignedSmart();
        if (locationDelta == 0) {
          break;
        }
        packedLocation += locationDelta - 1;
        int localY = packedLocation & 0x3f;
        int localX = packedLocation >> 6 & 0x3f;
        int plane = packedLocation >> 12;
        int info = reader.readUnsignedByte();
        int type = info >> 2;
        int orientation = info & 3;
        placements.add(
            new MapObjectPlacement(
                objectId,
                (regionX << 6) + localX,
                (regionY << 6) + localY,
                plane,
                type,
                orientation
            )
        );
      }
    }
  }
}
