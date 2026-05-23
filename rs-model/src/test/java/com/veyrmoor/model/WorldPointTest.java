package com.veyrmoor.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class WorldPointTest {

  @Test
  void translatesWithinSamePlane() {
    WorldPoint start = new WorldPoint(3200, 3200, 0);

    WorldPoint translated = start.translate(1, -2);

    assertThat(translated).isEqualTo(new WorldPoint(3201, 3198, 0));
  }
}
