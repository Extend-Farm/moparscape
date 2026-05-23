package com.veyrmoor.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SceneTextureCoordinatePlannerTest {

  @Test
  void usesExplicitTextureAnchorsWhenTheyAreAvailable() {
    SceneTriangleMesh mesh = new SceneTriangleMesh(
        new float[]{0.0f, 1.0f, 0.0f},
        new float[]{0.0f, 0.0f, 1.0f},
        new float[]{0.0f, 0.0f, 0.0f},
        new int[]{0},
        new int[]{1},
        new int[]{2},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{255},
        new int[]{5},
        new int[]{0},
        new int[]{1},
        new int[]{2}
    );

    float[] uv = new SceneTextureCoordinatePlanner().plan(mesh, 0);

    assertThat(uv).containsExactly(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f);
  }

  @Test
  void fallsBackToPlanarCoordinatesWhenTextureAnchorsAreMissing() {
    SceneTriangleMesh mesh = new SceneTriangleMesh(
        new float[]{0.0f, 1.0f, 0.0f},
        new float[]{0.0f, 0.0f, 1.0f},
        new float[]{0.0f, 0.0f, 0.0f},
        new int[]{0},
        new int[]{1},
        new int[]{2},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{0xffffff},
        new int[]{255},
        new int[]{5},
        new int[]{-1},
        new int[]{-1},
        new int[]{-1}
    );

    float[] uv = new SceneTextureCoordinatePlanner().plan(mesh, 0);

    assertThat(uv).containsExactly(0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f);
  }
}
