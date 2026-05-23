package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameplayStatsSidebarModelTest {

  @Test
  void computesLegacyCombatAndTotalLevelsFromSyncedSkills() {
    CharacterBootstrapPayload bootstrap = new CharacterBootstrapPayload(
        1L,
        1L,
        "akira",
        "region",
        null,
        new BootstrapProfile((short) 0, false, 100),
        null,
        List.of(),
        List.of(),
        allSkillsAt(99, 13_034_431)
    );
    ClientViewModel viewModel = new ClientViewModel("status", true, null, 1L, 1L, "region", bootstrap, null);

    GameplayStatsSidebarModel statsModel = GameplayStatsSidebarModel.from(viewModel);

    assertThat(statsModel.entries()).hasSize(21);
    assertThat(statsModel.totalLevel()).isEqualTo(2079);
    assertThat(statsModel.combatLevel()).isEqualTo(126);
  }

  @Test
  void derivesBaseLevelsFromExperienceForBoostedSkills() {
    CharacterBootstrapPayload bootstrap = new CharacterBootstrapPayload(
        1L,
        1L,
        "akira",
        "region",
        null,
        new BootstrapProfile((short) 0, false, 100),
        null,
        List.of(),
        List.of(),
        List.of(
            new BootstrapSkill(0, 102, 13_034_431),
            new BootstrapSkill(1, 99, 13_034_431),
            new BootstrapSkill(2, 99, 13_034_431),
            new BootstrapSkill(3, 99, 13_034_431),
            new BootstrapSkill(4, 99, 13_034_431),
            new BootstrapSkill(5, 99, 13_034_431),
            new BootstrapSkill(6, 99, 13_034_431)
        )
    );
    ClientViewModel viewModel = new ClientViewModel("status", true, null, 1L, 1L, "region", bootstrap, null);

    GameplayStatsSidebarModel.Entry attack = GameplayStatsSidebarModel.from(viewModel).entries().getFirst();

    assertThat(attack.skillId()).isEqualTo(0);
    assertThat(attack.currentLevel()).isEqualTo(102);
    assertThat(attack.baseLevel()).isEqualTo(99);
  }

  @Test
  void exposesLegacyExperienceThresholdsForDisplayedLevels() {
    assertThat(GameplayStatsSidebarModel.experienceForLevel(1)).isZero();
    assertThat(GameplayStatsSidebarModel.experienceForLevel(99)).isEqualTo(13_034_431);
  }

  private static List<BootstrapSkill> allSkillsAt(int level, int experience) {
    return java.util.stream.IntStream.rangeClosed(0, 20)
        .mapToObj(skillId -> new BootstrapSkill(skillId, level, experience))
        .toList();
  }
}
