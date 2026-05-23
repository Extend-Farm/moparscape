package com.veyrmoor.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
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

    assertThat(passPlan.opaqueFaceCount()).isEqualTo(1);
    assertThat(passPlan.translucentFaceCount()).isEqualTo(1);
    assertThat(Arrays.copyOf(passPlan.opaqueFaces(), passPlan.opaqueFaceCount())).containsExactly(0);
    assertThat(Arrays.copyOf(passPlan.translucentFaces(), passPlan.translucentFaceCount())).containsExactly(1);
  }

  @Test
  void reusesPlannerBuffersAcrossCalls() {
    SceneTriangleMesh firstMesh = new SceneTriangleMesh(
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
    SceneTriangleMesh secondMesh = new SceneTriangleMesh(
        new float[]{0.0f, 1.0f, 0.0f},
        new float[]{0.0f, 0.0f, 0.0f},
        new float[]{0.0f, 0.0f, 1.0f},
        new int[]{0},
        new int[]{1},
        new int[]{2},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{255},
        new int[]{-1},
        new int[]{-1},
        new int[]{-1},
        new int[]{-1}
    );

    SceneMeshFacePassPlanner planner = new SceneMeshFacePassPlanner();
    SceneMeshFacePassPlan firstPlan = planner.plan(firstMesh);
    int[] reusedOpaqueFaces = firstPlan.opaqueFaces();
    int[] reusedTranslucentFaces = firstPlan.translucentFaces();

    SceneMeshFacePassPlan secondPlan = planner.plan(secondMesh);

    assertThat(secondPlan).isSameAs(firstPlan);
    assertThat(secondPlan.opaqueFaces()).isSameAs(reusedOpaqueFaces);
    assertThat(secondPlan.translucentFaces()).isSameAs(reusedTranslucentFaces);
    assertThat(secondPlan.opaqueFaceCount()).isEqualTo(1);
    assertThat(secondPlan.translucentFaceCount()).isZero();
    assertThat(Arrays.copyOf(secondPlan.opaqueFaces(), secondPlan.opaqueFaceCount())).containsExactly(0);
  }
}
