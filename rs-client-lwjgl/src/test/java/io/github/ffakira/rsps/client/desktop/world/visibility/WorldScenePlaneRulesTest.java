package io.github.ffakira.rsps.client.desktop.world.visibility;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneProjection;
import org.junit.jupiter.api.Test;

class WorldScenePlaneRulesTest {

  @Test
  void selectsTheScenePlaneWhenRoofFlagsExistOnTheCameraToPlayerPath() {
    byte[] tileFlags = new byte[128 * 128];
    tileFlags[32 * 128 + 32] = 4;
    WorldScene worldScene = testWorldScene(tileFlags);

    int renderPlane = WorldScenePlaneRules.renderPlane(
        worldScene,
        new WorldCameraState(26.0f, -45.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f),
        32,
        32
    );

    assertThat(renderPlane).isEqualTo(worldScene.plane());
  }

  @Test
  void fallsBackToOpenPlaneWhenTheCameraPitchIsAboveTheRoofThreshold() {
    byte[] tileFlags = new byte[128 * 128];
    tileFlags[32 * 128 + 32] = 4;
    WorldScene worldScene = testWorldScene(tileFlags);

    int renderPlane = WorldScenePlaneRules.renderPlane(
        worldScene,
        new WorldCameraState(60.0f, -45.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f),
        32,
        32
    );

    assertThat(renderPlane).isEqualTo(3);
  }

  private WorldScene testWorldScene(byte[] tileFlags) {
    return new WorldScene(
        "plane-rules",
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
  }
}
