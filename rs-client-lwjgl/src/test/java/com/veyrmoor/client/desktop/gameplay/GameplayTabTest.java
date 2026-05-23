package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayTabTest {

  @Test
  void matchesFixed317BottomRowTabIndices() {
    assertThat(GameplayTab.values()).hasSize(13);
    assertThat(GameplayTab.FRIENDS.index()).isEqualTo(8);
    assertThat(GameplayTab.IGNORES.index()).isEqualTo(9);
    assertThat(GameplayTab.LOGOUT.index()).isEqualTo(10);
    assertThat(GameplayTab.SETTINGS.index()).isEqualTo(11);
    assertThat(GameplayTab.EMOTES.index()).isEqualTo(12);
    assertThat(GameplayTab.MUSIC.index()).isEqualTo(13);
    assertThat(GameplayTab.fromIndex(7)).isEqualTo(GameplayTab.INVENTORY);
  }
}
