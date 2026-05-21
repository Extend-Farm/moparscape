package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneCameraPlannerTest {

  @Test
  void plansLegacyStyleOrbitDistanceAndDefaultAngles() {
    WorldScene scene = sceneWithSlope(16, 16, (x, y) -> x * 2 + y * 3);
    WorldSceneCameraPlanner planner = new WorldSceneCameraPlanner();

    WorldCameraState cameraState = planner.plan(
        scene,
        8.5f,
        8.5f,
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318
    );

    assertThat(cameraState.pitchDegrees()).isEqualTo(22.5f);
    assertThat(cameraState.distance()).isEqualTo(9.0f);
    assertThat(cameraState.focusX()).isEqualTo(8.5f);
    assertThat(cameraState.focusY()).isEqualTo(8.5f);
    assertThat(cameraState.focusHeight()).isEqualTo(3.59375f);
    assertThat(cameraState.screenOffsetY()).isZero();
    assertThat(cameraState.yawDegrees()).isEqualTo(0.0f);
  }

  @Test
  void smoothsOrbitFocusTowardTheRenderedActorPosition() {
    WorldScene scene = sceneWithSlope(16, 16, (x, y) -> 0);
    WorldSceneCameraPlanner planner = new WorldSceneCameraPlanner();

    planner.plan(scene, 8.5f, 8.5f, WorldSceneScale.HEIGHT_SCALE, 496, 318);
    WorldCameraState smoothedCamera = planner.plan(
        scene,
        10.5f,
        6.5f,
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318
    );

    assertThat(smoothedCamera.focusX()).isEqualTo(8.625f);
    assertThat(smoothedCamera.focusY()).isEqualTo(8.375f);
  }

  @Test
  void raisesPitchWhenNearbyTerrainRequiresMoreClearance() {
    WorldScene scene = sceneWithSlope(16, 16, (x, y) -> x >= 8 && y >= 8 ? 0 : -240);
    WorldSceneCameraPlanner planner = new WorldSceneCameraPlanner();

    WorldCameraState cameraState = planner.plan(
        scene,
        8.5f,
        8.5f,
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318
    );

    assertThat(cameraState.pitchDegrees()).isGreaterThan(22.5f);
    assertThat(cameraState.distance()).isGreaterThan(9.0f);
  }

  @Test
  void appliesManualOrbitAnglesOnTopOfTheLegacyPitchClamp() {
    WorldScene scene = sceneWithSlope(16, 16, (x, y) -> 0);
    WorldSceneCameraPlanner planner = new WorldSceneCameraPlanner();

    WorldCameraState cameraState = planner.plan(
        scene,
        8.5f,
        8.5f,
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318,
        135.0f,
        30.0f
    );

    assertThat(cameraState.yawDegrees()).isEqualTo(135.0f);
    assertThat(cameraState.pitchDegrees()).isEqualTo(30.058594f);
  }

  private static WorldScene sceneWithSlope(int tileWidth, int tileHeight, ElevationSupplier elevationSupplier) {
    int[] elevations = new int[tileWidth * tileHeight];
    int[] colors = new int[tileWidth * tileHeight];
    for (int y = 0; y < tileHeight; y++) {
      for (int x = 0; x < tileWidth; x++) {
        elevations[y * tileWidth + x] = elevationSupplier.elevationAt(x, y);
        colors[y * tileWidth + x] = 0x2f3946;
      }
    }
    return new WorldScene(
        "test",
        3200,
        3200,
        0,
        tileWidth,
        tileHeight,
        elevations,
        colors,
        colors,
        colors,
        new int[tileWidth * tileHeight],
        new int[tileWidth * tileHeight],
        new byte[tileWidth * tileHeight],
        new byte[tileWidth * tileHeight],
        new byte[tileWidth * tileHeight],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }

  @FunctionalInterface
  private interface ElevationSupplier {
    int elevationAt(int x, int y);
  }
}
