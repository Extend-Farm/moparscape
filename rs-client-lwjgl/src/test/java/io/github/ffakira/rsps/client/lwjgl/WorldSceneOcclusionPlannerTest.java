package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneOcclusionPlannerTest {

  @Test
  void marksTargetsBehindAnActiveWallOccluderAsHidden() {
    WorldSceneOcclusionContext occlusionContext = new WorldSceneOcclusionContext(
        20.0f,
        1.2f,
        12.0f,
        List.of(new WorldSceneOccluder(
            WorldSceneOccluderType.X_WALL,
            0,
            16.0f,
            15.88f,
            16.12f,
            10.0f,
            20.0f,
            0.0f,
            4.0f
        ))
    );

    assertThat(WorldSceneOcclusionPlanner.isOccluded(occlusionContext, 12.0f, 1.2f, 12.0f)).isTrue();
    assertThat(WorldSceneOcclusionPlanner.isOccluded(occlusionContext, 20.0f, 1.2f, 12.0f)).isFalse();
  }

  @Test
  void ignoresHorizontalPlaneOccludersUntilRenderPlaneParityExists() {
    WorldScene worldScene = new WorldScene(
        "roofed",
        3200,
        3200,
        0,
        32,
        32,
        new int[32 * 32],
        new int[32 * 32],
        new int[32 * 32],
        new int[32 * 32],
        new int[32 * 32],
        new int[32 * 32],
        new byte[32 * 32],
        new byte[32 * 32],
        new byte[32 * 32],
        List.of(),
        List.of(new WorldSceneOccluder(
            WorldSceneOccluderType.HORIZONTAL_PLANE,
            0,
            3.0f,
            8.0f,
            16.0f,
            8.0f,
            16.0f,
            2.8f,
            3.2f
        )),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneOcclusionContext occlusionContext = WorldSceneOcclusionPlanner.plan(
        worldScene,
        new WorldSceneVisibilityWindow(0, 30, 0, 30),
        new WorldCameraState(20.0f, -45.0f, 20.0f, -1.0f, 12.0f, 12.0f, 0.0f)
    );

    assertThat(occlusionContext.activeOccluders()).isEmpty();
  }
}
