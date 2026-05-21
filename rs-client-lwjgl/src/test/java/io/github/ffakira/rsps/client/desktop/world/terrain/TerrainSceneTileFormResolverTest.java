package io.github.ffakira.rsps.client.desktop.world.terrain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TerrainSceneTileFormResolverTest {

  @Test
  void resolvesFlatPaintWhenTheTileIsRenderableAndUntextured() {
    TerrainLayerSource terrainLayerSource = tileSource(
        1,
        0,
        0x4a6a3c,
        0x4a6a3c,
        0,
        -1,
        -1,
        0
    );

    assertThat(TerrainSceneTileFormResolver.resolve(terrainLayerSource, 0, 0, true))
        .isEqualTo(TerrainSceneTileForm.FLAT_PAINT);
  }

  @Test
  void resolvesTexturedPaintForFullOverlayTextureTiles() {
    TerrainLayerSource terrainLayerSource = tileSource(
        1,
        4,
        0x4a6a3c,
        0x4a6a3c,
        0x5a7ea3,
        -1,
        1,
        0
    );

    assertThat(TerrainSceneTileFormResolver.resolve(terrainLayerSource, 0, 0, true))
        .isEqualTo(TerrainSceneTileForm.TEXTURED_PAINT);
  }

  @Test
  void resolvesSceneTileModelForSupportedShapedOverlayTiles() {
    TerrainLayerSource terrainLayerSource = tileSource(
        1,
        4,
        0x4a6a3c,
        0x4a6a3c,
        0x7d9150,
        -1,
        -1,
        1
    );

    assertThat(TerrainSceneTileFormResolver.resolve(terrainLayerSource, 0, 0, true))
        .isEqualTo(TerrainSceneTileForm.SCENE_TILE_MODEL);
  }

  @Test
  void resolvesNoneWhenARawOverlayBranchStaysHidden() {
    TerrainLayerSource terrainLayerSource = tileSource(
        1,
        4,
        0x4a6a3c,
        0x4a6a3c,
        0,
        -1,
        -1,
        0
    );

    assertThat(TerrainSceneTileFormResolver.resolve(terrainLayerSource, 0, 0, true))
        .isEqualTo(TerrainSceneTileForm.NONE);
  }

  private TerrainLayerSource tileSource(
      int underlayId,
      int overlayId,
      int tileColor,
      int underlayColor,
      int overlayColor,
      int underlayTextureId,
      int overlayTextureId,
      int overlayShape
  ) {
    return new TerrainLayerSource() {
      @Override
      public int tileWidth() {
        return 1;
      }

      @Override
      public int tileHeight() {
        return 1;
      }

      @Override
      public int elevationAt(int localX, int localY) {
        return 0;
      }

      @Override
      public int underlayIdAt(int localX, int localY) {
        return underlayId;
      }

      @Override
      public int overlayIdAt(int localX, int localY) {
        return overlayId;
      }

      @Override
      public int tileColorAt(int localX, int localY) {
        return tileColor;
      }

      @Override
      public int underlayColorAt(int localX, int localY) {
        return underlayColor;
      }

      @Override
      public int overlayColorAt(int localX, int localY) {
        return overlayColor;
      }

      @Override
      public int underlayTextureIdAt(int localX, int localY) {
        return underlayTextureId;
      }

      @Override
      public int overlayTextureIdAt(int localX, int localY) {
        return overlayTextureId;
      }

      @Override
      public int overlayShapeAt(int localX, int localY) {
        return overlayShape;
      }

      @Override
      public int overlayRotationAt(int localX, int localY) {
        return 0;
      }
    };
  }
}
