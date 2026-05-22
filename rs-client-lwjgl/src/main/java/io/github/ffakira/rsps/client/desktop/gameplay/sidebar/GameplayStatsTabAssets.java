package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

public record GameplayStatsTabAssets(
    ArgbImage buttonLeft,
    ArgbImage buttonRight,
    ArgbImage[] skillIconsBySkillId
) {

  public GameplayStatsTabAssets {
    skillIconsBySkillId = skillIconsBySkillId.clone();
  }

  public ArgbImage iconForSkill(int skillId) {
    if (skillId < 0 || skillId >= skillIconsBySkillId.length) {
      return null;
    }
    return skillIconsBySkillId[skillId];
  }
}
