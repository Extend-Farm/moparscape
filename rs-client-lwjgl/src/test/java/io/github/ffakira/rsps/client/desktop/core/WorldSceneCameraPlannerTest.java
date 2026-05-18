package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneCameraPlannerTest {

  @Test
  void plansAPlayerFollowCameraInsteadOfSceneOverview() {
    int tileWidth = 16;
    int tileHeight = 16;
    int[] elevations = new int[tileWidth * tileHeight];
    int[] colors = new int[tileWidth * tileHeight];
    for (int y = 0; y < tileHeight; y++) {
      for (int x = 0; x < tileWidth; x++) {
        elevations[y * tileWidth + x] = x * 2 + y * 3;
        colors[y * tileWidth + x] = 0x2f3946;
      }
    }
    WorldScene scene = new WorldScene(
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

    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        scene,
        8,
        8,
        ActorAnimationState.idle(),
        0.18f,
        496,
        318
    );

    assertThat(cameraState.pitchDegrees()).isBetween(26.0f, 32.5f);
    assertThat(cameraState.distance()).isBetween(12.1f, 16.3f);
    assertThat(cameraState.focusX()).isBetween(8.4f, 8.6f);
    assertThat(cameraState.focusY()).isBetween(8.6f, 9.4f);
    assertThat(cameraState.screenOffsetY()).isBetween(-0.95f, -0.40f);
    assertThat(cameraState.yawDegrees()).isEqualTo(180.0f);
  }

  @Test
  void nudgesTheCameraTowardMovementHeadingWithoutAbandoningThePlayLane() {
    int tileWidth = 16;
    int tileHeight = 16;
    int[] elevations = new int[tileWidth * tileHeight];
    int[] colors = new int[tileWidth * tileHeight];
    WorldScene scene = new WorldScene(
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

    WorldCameraState idleCamera = WorldSceneCameraPlanner.plan(scene, 8, 8, ActorAnimationState.idle(), 0.18f, 496, 318);
    WorldCameraState eastwardCamera = WorldSceneCameraPlanner.plan(
        scene,
        8,
        8,
        new ActorAnimationState(1.0f, 0.0f, 90.0f),
        0.18f,
        496,
        318
    );

    assertThat(eastwardCamera.focusX()).isGreaterThan(idleCamera.focusX());
    assertThat(eastwardCamera.focusY()).isGreaterThanOrEqualTo(idleCamera.focusY() - 0.3f);
  }

  @Test
  void appliesManualCameraYawAndPitchOffsetsInsideSafeBounds() {
    WorldScene scene = new WorldScene(
        "test",
        3200,
        3200,
        0,
        16,
        16,
        new int[16 * 16],
        new int[16 * 16],
        new int[16 * 16],
        new int[16 * 16],
        new int[16 * 16],
        new int[16 * 16],
        new byte[16 * 16],
        new byte[16 * 16],
        new byte[16 * 16],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        scene,
        8,
        8,
        ActorAnimationState.idle(),
        0.18f,
        496,
        318,
        -45.0f,
        4.0f
    );

    assertThat(cameraState.yawDegrees()).isEqualTo(135.0f);
    assertThat(cameraState.pitchDegrees()).isBetween(30.0f, 36.5f);
  }
}
