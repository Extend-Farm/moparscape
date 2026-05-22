package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayStatsTabLayoutTest {

  @Test
  void usesPerSkillHoverValueOffsetsFromLegacyLayout() {
    GameplayStatsTabLayout.StatsSlotLayout attack = slotForSkill(0);
    GameplayStatsTabLayout.StatsSlotLayout strength = slotForSkill(2);
    GameplayStatsTabLayout.StatsSlotLayout woodcutting = slotForSkill(8);

    assertThat(attack.hoverFirstValueX()).isEqualTo(68.0f);
    assertThat(strength.hoverFirstValueX()).isEqualTo(79.0f);
    assertThat(woodcutting.hoverFirstValueX()).isEqualTo(102.0f);
  }

  private static GameplayStatsTabLayout.StatsSlotLayout slotForSkill(int skillId) {
    return java.util.Arrays.stream(GameplayStatsTabLayout.slots())
        .filter(slot -> slot.skillId() == skillId)
        .findFirst()
        .orElseThrow();
  }
}
