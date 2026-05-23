package com.veyrmoor.content;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerrainRegionDecoderTest {

  @Test
  void decodesUnderlayAssignmentsForTerrainTiles() {
    byte[] bytes = new byte[16385];
    bytes[0] = 82;
    bytes[1] = 0;

    TerrainRegionData regionData = new TerrainRegionDecoder().decode(50, 50, bytes);

    assertThat(regionData.underlayIdAt(0, 0, 0)).isEqualTo(1);
    assertThat(regionData.overlayIdAt(0, 0, 0)).isEqualTo(0);
  }

  @Test
  void decodesOverlayShapeAndRotationForShapedTiles() {
    byte[] bytes = new byte[16387];
    bytes[0] = 10;
    bytes[1] = 5;
    bytes[2] = 0;

    TerrainRegionData regionData = new TerrainRegionDecoder().decode(50, 50, bytes);

    assertThat(regionData.overlayIdAt(0, 0, 0)).isEqualTo(5);
    assertThat(regionData.overlayShapeAt(0, 0, 0)).isEqualTo(2);
    assertThat(regionData.overlayRotationAt(0, 0, 0)).isEqualTo(0);
  }
}
