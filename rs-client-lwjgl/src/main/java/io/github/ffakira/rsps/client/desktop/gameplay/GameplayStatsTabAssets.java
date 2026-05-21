package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

record GameplayStatsTabAssets(
    ArgbImage buttonLeft,
    ArgbImage buttonRight,
    ArgbImage[] skillIconsBySkillId
) {

  GameplayStatsTabAssets {
    skillIconsBySkillId = skillIconsBySkillId.clone();
  }

  ArgbImage iconForSkill(int skillId) {
    if (skillId < 0 || skillId >= skillIconsBySkillId.length) {
      return null;
    }
    return skillIconsBySkillId[skillId];
  }
}
