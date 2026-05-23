package com.veyrmoor.client.desktop.world.visibility;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.world.WorldCameraState;
import com.veyrmoor.client.desktop.world.WorldScene;
import com.veyrmoor.client.desktop.world.WorldSceneProjection;
import org.junit.jupiter.api.Test;

class WorldSceneVisibilityPlannerTest {

  @Test
  void clampsAPlayerCentricVisibilityWindowInsideTheSceneBounds() {
    WorldScene worldScene = new WorldScene(
        "wide",
        3200,
        3200,
        0,
        128,
        128,
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new byte[128 * 128],
        new byte[128 * 128],
        new byte[128 * 128],
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneVisibilityWindow visibilityWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        new WorldCameraState(26.0f, -45.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f),
        32,
        32,
        496,
        318
    );

    assertThat(visibilityWindow.containsTile(32, 32)).isTrue();
    assertThat(visibilityWindow.minLocalX()).isBetween(0, 126);
    assertThat(visibilityWindow.maxLocalX()).isBetween(0, 126);
    assertThat(visibilityWindow.minLocalY()).isBetween(0, 126);
    assertThat(visibilityWindow.maxLocalY()).isBetween(0, 126);
    assertThat(visibilityWindow.maxLocalX() - visibilityWindow.minLocalX()).isLessThanOrEqualTo(50);
    assertThat(visibilityWindow.maxLocalY() - visibilityWindow.minLocalY()).isLessThanOrEqualTo(50);
  }

  @Test
  void tightensTheWindowWhenRoofFlagsExistOnTheCameraToPlayerPath() {
    byte[] tileFlags = new byte[128 * 128];
    tileFlags[32 * 128 + 32] = 4;
    WorldScene worldScene = new WorldScene(
        "roofed",
        3200,
        3200,
        0,
        128,
        128,
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new byte[128 * 128],
        new byte[128 * 128],
        tileFlags,
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneVisibilityWindow unroofedWindow = WorldSceneVisibilityPlanner.plan(
        new WorldScene(
            "open",
            3200,
            3200,
            0,
            128,
            128,
            new int[128 * 128],
            new int[128 * 128],
            new int[128 * 128],
            new int[128 * 128],
            new int[128 * 128],
            new int[128 * 128],
            new byte[128 * 128],
            new byte[128 * 128],
            new byte[128 * 128],
            java.util.List.of(),
            java.util.List.of(),
            new ArgbImage(1, 1, new int[]{0xff000000}),
            new ArgbImage(1, 1, new int[]{0xff000000}),
            new WorldSceneProjection(5, 3, 0, 0)
        ),
        new WorldCameraState(26.0f, -45.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f),
        32,
        32,
        496,
        318
    );

    WorldSceneVisibilityWindow roofedWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        new WorldCameraState(26.0f, -45.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f),
        32,
        32,
        496,
        318
    );

    assertThat(roofedWindow.containsTile(32, 32)).isTrue();
    assertThat(roofedWindow.maxLocalX() - roofedWindow.minLocalX())
        .isLessThan(unroofedWindow.maxLocalX() - unroofedWindow.minLocalX());
    assertThat(roofedWindow.maxLocalY() - roofedWindow.minLocalY())
        .isLessThan(unroofedWindow.maxLocalY() - unroofedWindow.minLocalY());
  }

  @Test
  void keepsTheFullLegacyWindowWhenTheCameraPitchIsAboveTheRoofThreshold() {
    byte[] tileFlags = new byte[128 * 128];
    tileFlags[64 * 128 + 64] = 4;
    WorldScene worldScene = new WorldScene(
        "high-pitch-roofed",
        3200,
        3200,
        0,
        128,
        128,
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new int[128 * 128],
        new byte[128 * 128],
        new byte[128 * 128],
        tileFlags,
        java.util.List.of(),
        java.util.List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneVisibilityWindow visibilityWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        new WorldCameraState(60.0f, -45.0f, 20.0f, -1.0f, 64.0f, 64.0f, 0.0f),
        64,
        64,
        496,
        318
    );

    assertThat(visibilityWindow.containsTile(64, 64)).isTrue();
    assertThat(visibilityWindow.maxLocalX() - visibilityWindow.minLocalX()).isEqualTo(50);
    assertThat(visibilityWindow.maxLocalY() - visibilityWindow.minLocalY()).isEqualTo(50);
  }
}
