package io.github.ffakira.rsps.client.desktop.world.minimap;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.WorldSceneProjection;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.List;
import org.junit.jupiter.api.Test;

class MinimapViewportPlannerTest {

  @Test
  void centersTheVisibleCropAroundThePlayerWhenRoomExists() {
    WorldScene worldScene = testWorldScene(48, 48);

    MinimapViewport viewport = new MinimapViewportPlanner().plan(worldScene, new WorldPoint(3224, 3224, 0), 25);

    assertThat(viewport.sourceWidth()).isEqualTo(100);
    assertThat(viewport.sourceHeight()).isEqualTo(100);
    assertThat(viewport.sourceX()).isEqualTo(48);
    assertThat(viewport.sourceY()).isEqualTo(44);
    assertThat(viewport.markerSourceX()).isEqualTo(50);
    assertThat(viewport.markerSourceY()).isEqualTo(50);
  }

  @Test
  void clampsTheVisibleCropAtSceneEdges() {
    WorldScene worldScene = testWorldScene(48, 48);

    MinimapViewport viewport = new MinimapViewportPlanner().plan(worldScene, new WorldPoint(3201, 3201, 0), 25);

    assertThat(viewport.sourceX()).isZero();
    assertThat(viewport.sourceY()).isEqualTo(92);
    assertThat(viewport.markerSourceX()).isEqualTo(6);
    assertThat(viewport.markerSourceY()).isEqualTo(94);
  }

  private static WorldScene testWorldScene(int width, int height) {
    int[] zeros = new int[width * height];
    int[] pixels = new int[width * height * 16];
    java.util.Arrays.fill(pixels, 0xff334455);
    return new WorldScene(
        "minimap-test",
        3200,
        3200,
        0,
        width,
        height,
        zeros,
        zeros,
        zeros,
        zeros,
        filledInts(width * height, -1),
        filledInts(width * height, -1),
        new byte[width * height],
        new byte[width * height],
        new byte[width * height],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(width * 4, height * 4, pixels),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }

  private static int[] filledInts(int size, int value) {
    int[] values = new int[size];
    java.util.Arrays.fill(values, value);
    return values;
  }
}
