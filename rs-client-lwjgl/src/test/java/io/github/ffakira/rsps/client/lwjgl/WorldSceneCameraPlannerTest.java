package io.github.ffakira.rsps.client.lwjgl;

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

    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(scene, 8, 8, 0.18f, 496, 318);

    assertThat(cameraState.pitchDegrees()).isBetween(24.0f, 31.0f);
    assertThat(cameraState.distance()).isBetween(16.0f, 27.0f);
    assertThat(cameraState.focusX()).isGreaterThan(8.5f);
    assertThat(cameraState.focusY()).isGreaterThan(8.5f);
  }
}
