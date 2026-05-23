package com.veyrmoor.content;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MapArchiveIndexTest {

  @Test
  void parsesRegionToTerrainAndObjectArchiveMappings() {
    byte[] bytes = {
        0x32, 0x31,
        0x01, 0x23,
        0x04, 0x56,
        0x01
    };

    MapArchiveReference reference = MapArchiveIndex.parse(bytes).find(0x32, 0x31).orElseThrow();

    assertThat(reference.regionId()).isEqualTo(0x3231);
    assertThat(reference.terrainArchiveId()).isEqualTo(0x0123);
    assertThat(reference.objectArchiveId()).isEqualTo(0x0456);
    assertThat(reference.membersOnly()).isTrue();
  }
}
