package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayLayoutTest {

  @Test
  void worldViewportUsesFullLegacySurface() {
    assertThat(GameplayLayout.worldViewportInnerRect()).isEqualTo(GameplayLayout.worldViewportRect());
  }
}
