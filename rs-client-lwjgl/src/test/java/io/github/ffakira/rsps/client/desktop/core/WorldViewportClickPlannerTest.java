package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldViewportClickPlannerTest {

  @Test
  void resolvesDistinctTerrainTargetsAcrossTheViewport() {
    WorldScene worldScene = testWorldScene(24, 24);
    WorldCameraState cameraState = new WorldCameraState(20.0f, 0.0f, 14.0f, -1.2f, 12.5f, 15.5f, 0.0f);
    ScreenRect viewport = new ScreenRect(0.0f, 0.0f, 496.0f, 318.0f);
    WorldViewportClickPlanner planner = new WorldViewportClickPlanner();

    WorldViewportClickPlanner.WorldViewportClickTarget centerTarget = planner.pickTile(
        worldScene,
        cameraState,
        viewport,
        248.0,
        230.0
    );
    WorldViewportClickPlanner.WorldViewportClickTarget outsideTarget = planner.pickTile(
        worldScene,
        cameraState,
        viewport,
        -24.0,
        230.0
    );

    assertThat(centerTarget).isNotNull();
    assertThat(centerTarget.localX()).isBetween(0, 22);
    assertThat(centerTarget.localY()).isBetween(0, 22);
    assertThat(outsideTarget).isNull();
  }

  @Test
  void mapsNearAndFarViewportRowsToDifferentTerrainDepths() {
    WorldScene worldScene = testWorldScene(24, 24);
    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        worldScene,
        12,
        12,
        ActorAnimationState.idle(),
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318
    );
    ScreenRect viewport = new ScreenRect(0.0f, 0.0f, 496.0f, 318.0f);
    WorldViewportClickPlanner planner = new WorldViewportClickPlanner();

    WorldViewportClickPlanner.WorldViewportClickTarget farTarget = planner.pickTile(worldScene, cameraState, viewport, 248.0, 120.0);
    WorldViewportClickPlanner.WorldViewportClickTarget nearTarget = planner.pickTile(worldScene, cameraState, viewport, 248.0, 280.0);

    assertThat(farTarget).isNotNull();
    assertThat(nearTarget).isNotNull();
    assertThat(farTarget.localY()).isGreaterThan(nearTarget.localY());
  }

  @Test
  void mapsLeftAndRightViewportColumnsAcrossTerrainWidth() {
    WorldScene worldScene = testWorldScene(24, 24);
    WorldCameraState cameraState = WorldSceneCameraPlanner.plan(
        worldScene,
        12,
        12,
        ActorAnimationState.idle(),
        WorldSceneScale.HEIGHT_SCALE,
        496,
        318
    );
    ScreenRect viewport = new ScreenRect(0.0f, 0.0f, 496.0f, 318.0f);
    WorldViewportClickPlanner planner = new WorldViewportClickPlanner();

    WorldViewportClickPlanner.WorldViewportClickTarget leftTarget = planner.pickTile(worldScene, cameraState, viewport, 80.0, 230.0);
    WorldViewportClickPlanner.WorldViewportClickTarget rightTarget = planner.pickTile(worldScene, cameraState, viewport, 416.0, 230.0);

    assertThat(leftTarget).isNotNull();
    assertThat(rightTarget).isNotNull();
    assertThat(leftTarget.localX()).isGreaterThan(rightTarget.localX());
  }

  private static WorldScene testWorldScene(int width, int height) {
    int[] elevations = new int[width * height];
    int[] colors = new int[width * height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        elevations[y * width + x] = (x + y) * 2;
        colors[y * width + x] = 0x335533;
      }
    }
    int[] textureIds = new int[width * height];
    java.util.Arrays.fill(textureIds, -1);
    return new WorldScene(
        "click-test",
        3200,
        3200,
        0,
        width,
        height,
        elevations,
        colors,
        colors,
        colors,
        textureIds,
        textureIds.clone(),
        new byte[width * height],
        new byte[width * height],
        new byte[width * height],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }
}
