package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class WorldViewportProjectionTest {

  @Test
  void derivesTheGameplayFrustumFromTheLegacySoftwareFocalLength() {
    assertThat(WorldViewportProjection.halfVerticalFovDegrees(318.0f)).isCloseTo(17.252018f, within(0.0001f));
    assertThat(WorldViewportProjection.frustumTop(318.0f)).isCloseTo(0.37265626f, within(0.0001f));
    assertThat(WorldViewportProjection.frustumRight(496.0f)).isCloseTo(0.58125f, within(0.0001f));
  }
}
