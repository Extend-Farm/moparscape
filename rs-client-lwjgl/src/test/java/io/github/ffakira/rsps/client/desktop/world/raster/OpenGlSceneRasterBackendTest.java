package io.github.ffakira.rsps.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OpenGlSceneRasterBackendTest {

  @Test
  void appliesTerrainShadeOnlyToTerrainBatches() {
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD)))
        .isEqualTo(0.0f);
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.TILE_MODEL, SceneRasterMode.TEXTURED)))
        .isEqualTo(0.0f);
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD)))
        .isEqualTo(0.0f);
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.ACTOR, SceneRasterMode.TEXTURED)))
        .isEqualTo(0.0f);
  }

  @Test
  void keepsTerrainShadeRoutingIndependentFromActorBatches() {
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD)))
        .isEqualTo(0.0f);
    assertThat(OpenGlSceneRasterBackend.terrainShadeStrength(batch(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD)))
        .isEqualTo(0.0f);
  }

  @Test
  void usesSceneDepthButNotActorSelfDepthForActorBatches() {
    assertThat(OpenGlSceneRasterBackend.usesSceneDepthOnly(batch(SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD)))
        .isTrue();
    assertThat(OpenGlSceneRasterBackend.usesSceneDepthOnly(batch(SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD)))
        .isFalse();
  }

  private static SceneRenderBatch batch(SceneSubmissionKind kind, SceneRasterMode rasterMode) {
    return new SceneRenderBatch(kind, rasterMode, SceneTriangleMesh.EMPTY);
  }
}
