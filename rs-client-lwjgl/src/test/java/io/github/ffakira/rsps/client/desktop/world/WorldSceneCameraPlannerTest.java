package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.character.ActorAnimationState;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
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

    assertThat(cameraState.pitchDegrees()).isBetween(28.0f, 36.0f);
    assertThat(cameraState.distance()).isBetween(12.4f, 16.8f);
    assertThat(cameraState.focusX()).isBetween(8.45f, 8.55f);
    assertThat(cameraState.focusY()).isBetween(8.45f, 8.55f);
    assertThat(cameraState.screenOffsetY()).isBetween(-0.95f, -0.35f);
    assertThat(cameraState.yawDegrees()).isEqualTo(180.0f);
  }

  @Test
  void followsTheSmoothedActorPositionInsteadOfBiasingAheadOfThePlayer() {
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

    WorldCameraState smoothedCamera = WorldSceneCameraPlanner.plan(
        scene,
        8.82f,
        8.18f,
        new ActorAnimationState(1.0f, 0.0f, 90.0f, 0.32f, -0.32f),
        0.18f,
        496,
        318
    );

    assertThat(smoothedCamera.focusX()).isBetween(8.80f, 8.84f);
    assertThat(smoothedCamera.focusY()).isBetween(8.16f, 8.20f);
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
    assertThat(cameraState.pitchDegrees()).isBetween(32.0f, 40.0f);
  }
}
