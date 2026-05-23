package com.veyrmoor.client.desktop.world.minimap;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.world.object.WorldSceneObject;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneMapFunctionMarkerCollectorTest {

  @Test
  void collectsOnlyGroundDecorationMapFunctionMarkers() {
    WorldSceneMapFunctionMarkerCollector collector = new WorldSceneMapFunctionMarkerCollector();
    List<WorldSceneObject> sceneObjects = List.of(
        new WorldSceneObject(100, "altar", 12, 34, 0, 22, 0, 1, 1, false, -1, 29, List.of(), false, null),
        new WorldSceneObject(101, "wall", 13, 34, 0, 0, 0, 1, 1, false, -1, 55, List.of(), false, null),
        new WorldSceneObject(102, "plain-ground", 14, 34, 0, 22, 0, 1, 1, false, -1, -1, List.of(), false, null)
    );

    assertThat(collector.collect(sceneObjects, 64, 64))
        .containsExactly(new WorldSceneMapFunctionMarker(12, 34, 29));
  }

  @Test
  void preservesTileCoordinatesForStationaryMarkerIdsInsteadOfObjectFootprintCenter() {
    WorldSceneMapFunctionMarkerCollector collector = new WorldSceneMapFunctionMarkerCollector();
    List<WorldSceneObject> sceneObjects = List.of(
        new WorldSceneObject(103, "large-decoration", 20, 40, 0, 22, 0, 3, 2, false, -1, 29, List.of(), false, null)
    );

    assertThat(collector.collect(sceneObjects, 64, 64))
        .containsExactly(new WorldSceneMapFunctionMarker(20, 40, 29));
  }

  @Test
  void driftsWaterMarkersWithinTheLegacyThreeTileBox() {
    WorldSceneMapFunctionMarkerCollector collector = new WorldSceneMapFunctionMarkerCollector();
    List<WorldSceneObject> sceneObjects = List.of(
        new WorldSceneObject(2771, "well", 20, 40, 0, 22, 0, 1, 1, false, -1, 39, List.of(), false, true, true, false, null)
    );

    assertThat(collector.collect(sceneObjects, 64, 64))
        .containsExactly(new WorldSceneMapFunctionMarker(21, 39, 39));
  }

  @Test
  void keepsMarkerDriftInsideCollisionBoundaries() {
    WorldSceneMapFunctionMarkerCollector collector = new WorldSceneMapFunctionMarkerCollector();
    List<WorldSceneObject> sceneObjects = List.of(
        new WorldSceneObject(2771, "well", 20, 40, 0, 22, 0, 1, 1, false, -1, 39, List.of(), false, true, true, false, null),
        new WorldSceneObject(900, "blocker", 21, 39, 0, 10, 0, 1, 1, false, -1, -1, List.of(), false, true, true, false, null)
    );

    assertThat(collector.collect(sceneObjects, 64, 64))
        .containsExactly(new WorldSceneMapFunctionMarker(18, 39, 39));
  }
}
