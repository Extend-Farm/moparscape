package io.github.ffakira.rsps.client.desktop.world.visibility;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.content.TerrainRegionData;
import org.junit.jupiter.api.Test;

class WorldScenePlaneRulesTest {

  @Test
  void lowersPlaneWhenBridgeFlagIsSetOnPlaneOne() {
    int[][] heights = new int[4][64 * 64];
    byte[][] underlays = new byte[4][64 * 64];
    byte[][] overlays = new byte[4][64 * 64];
    byte[][] overlayShapes = new byte[4][64 * 64];
    byte[][] overlayRotations = new byte[4][64 * 64];
    byte[][] tileFlags = new byte[4][64 * 64];
    tileFlags[1][5 * 64 + 5] = 2;
    TerrainRegionData regionData = new TerrainRegionData(
        50,
        50,
        heights,
        underlays,
        overlays,
        overlayShapes,
        overlayRotations,
        tileFlags
    );

    assertThat(WorldScenePlaneRules.effectivePlane(regionData, 2, 5, 5)).isEqualTo(1);
  }

  @Test
  void forcesPlaneZeroWhenRoofFlagEightIsSet() {
    int[][] heights = new int[4][64 * 64];
    byte[][] underlays = new byte[4][64 * 64];
    byte[][] overlays = new byte[4][64 * 64];
    byte[][] overlayShapes = new byte[4][64 * 64];
    byte[][] overlayRotations = new byte[4][64 * 64];
    byte[][] tileFlags = new byte[4][64 * 64];
    tileFlags[2][7 * 64 + 7] = 8;
    TerrainRegionData regionData = new TerrainRegionData(
        50,
        50,
        heights,
        underlays,
        overlays,
        overlayShapes,
        overlayRotations,
        tileFlags
    );

    assertThat(WorldScenePlaneRules.effectivePlane(regionData, 2, 7, 7)).isEqualTo(0);
  }

  @Test
  void usesBridgeSurfacePlaneForGroundScenesWhenPlaneOneCarriesTheDeck() {
    int[][] heights = new int[4][64 * 64];
    byte[][] underlays = new byte[4][64 * 64];
    byte[][] overlays = new byte[4][64 * 64];
    byte[][] overlayShapes = new byte[4][64 * 64];
    byte[][] overlayRotations = new byte[4][64 * 64];
    byte[][] tileFlags = new byte[4][64 * 64];
    tileFlags[1][9 * 64 + 9] = 2;
    overlays[1][9 * 64 + 9] = 10;
    TerrainRegionData regionData = new TerrainRegionData(
        50,
        50,
        heights,
        underlays,
        overlays,
        overlayShapes,
        overlayRotations,
        tileFlags
    );

    assertThat(WorldScenePlaneRules.surfacePlane(regionData, 0, 9, 9)).isEqualTo(1);
  }

  @Test
  void projectsPlaneOneBridgeObjectsIntoGroundScenes() {
    int[][] heights = new int[4][64 * 64];
    byte[][] underlays = new byte[4][64 * 64];
    byte[][] overlays = new byte[4][64 * 64];
    byte[][] overlayShapes = new byte[4][64 * 64];
    byte[][] overlayRotations = new byte[4][64 * 64];
    byte[][] tileFlags = new byte[4][64 * 64];
    tileFlags[1][11 * 64 + 11] = 2;
    TerrainRegionData regionData = new TerrainRegionData(
        50,
        50,
        heights,
        underlays,
        overlays,
        overlayShapes,
        overlayRotations,
        tileFlags
    );

    assertThat(WorldScenePlaneRules.objectScenePlane(regionData, 0, 1, 11, 11)).isEqualTo(0);
    assertThat(WorldScenePlaneRules.objectScenePlane(regionData, 0, 1, 10, 10)).isEqualTo(-1);
  }
}
