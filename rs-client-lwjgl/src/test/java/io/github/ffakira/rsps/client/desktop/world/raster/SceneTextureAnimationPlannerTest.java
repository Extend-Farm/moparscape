package io.github.ffakira.rsps.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SceneTextureAnimationPlannerTest {

  private final SceneTextureAnimationPlanner planner = new SceneTextureAnimationPlanner();

  @Test
  void leavesNonAnimatedTexturesUntouched() {
    float[] uv = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f};

    float[] animatedUv = planner.plan(5, 64, uv, 500_000_000L);

    assertThat(animatedUv).isSameAs(uv);
  }

  @Test
  void scrollsAnimatedTexturesAlongTheVAxis() {
    float[] uv = new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f};

    float[] animatedUv = planner.plan(17, 64, uv, 500_000_000L);

    assertThat(animatedUv)
        .usingComparatorWithPrecision(0.0001f)
        .containsExactly(0.0f, -0.75f, 1.0f, -0.75f, 0.0f, 0.25f);
  }

  @Test
  void wrapsWholeTextureCyclesInsteadOfGrowingOffsetsForever() {
    float[] uv = new float[]{0.0f, 0.2f, 1.0f, 0.2f, 0.0f, 0.7f};

    float[] animatedUv = planner.plan(24, 64, uv, 1_000_000_000L);

    assertThat(animatedUv)
        .usingComparatorWithPrecision(0.0001f)
        .containsExactly(0.0f, -0.3f, 1.0f, -0.3f, 0.0f, 0.2f);
  }
}
