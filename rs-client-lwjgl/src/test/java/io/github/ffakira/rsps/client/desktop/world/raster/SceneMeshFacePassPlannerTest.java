package io.github.ffakira.rsps.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SceneMeshFacePassPlannerTest {

  @Test
  void splitsOpaqueAndTranslucentFacesIntoSeparatePasses() {
    SceneTriangleMesh mesh = new SceneTriangleMesh(
        new float[]{0.0f, 1.0f, 0.0f, 2.0f},
        new float[]{0.0f, 0.0f, 0.0f, 0.0f},
        new float[]{0.0f, 0.0f, 1.0f, 0.0f},
        new int[]{0, 1},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{255, 160},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1}
    );

    SceneMeshFacePassPlan passPlan = new SceneMeshFacePassPlanner().plan(mesh);

    assertThat(passPlan.opaqueFaces()).containsExactly(0);
    assertThat(passPlan.translucentFaces()).containsExactly(1);
  }
}
