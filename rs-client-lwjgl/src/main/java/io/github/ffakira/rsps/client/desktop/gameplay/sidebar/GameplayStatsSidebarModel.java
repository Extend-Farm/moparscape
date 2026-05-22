package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.core.BootstrapSkillPresentation;
import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class GameplayStatsSidebarModel {

  private static final int[] DISPLAY_ORDER = {
      0, 3, 14,
      2, 16, 13,
      1, 15, 10,
      4, 17, 7,
      5, 12, 11,
      6, 9, 8,
      20, 18, 19
  };
  private static final String[] SKILL_NAMES = {
      "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic",
      "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
      "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming",
      "Runecraft"
  };
  private static final int MAX_LEVEL = 99;
  private static final int[] EXPERIENCE_FOR_LEVEL = buildExperienceTable();

  private final List<Entry> entries;
  private final int totalLevel;
  private final int combatLevel;

  private GameplayStatsSidebarModel(List<Entry> entries, int totalLevel, int combatLevel) {
    this.entries = List.copyOf(entries);
    this.totalLevel = totalLevel;
    this.combatLevel = combatLevel;
  }

  static GameplayStatsSidebarModel from(ClientViewModel viewModel) {
    Map<Integer, SkillSnapshot> skillById = snapshots(viewModel);
    ArrayList<Entry> entries = new ArrayList<>(DISPLAY_ORDER.length);
    int totalLevel = 0;
    for (int skillId : DISPLAY_ORDER) {
      SkillSnapshot snapshot = skillById.getOrDefault(skillId, SkillSnapshot.empty(skillId));
      entries.add(new Entry(
          skillId,
          skillName(skillId),
          snapshot.currentLevel(),
          snapshot.baseLevel(),
          snapshot.experience()
      ));
      totalLevel += Math.max(snapshot.baseLevel(), 0);
    }
    return new GameplayStatsSidebarModel(entries, totalLevel, combatLevel(skillById));
  }

  List<Entry> entries() {
    return entries;
  }

  int totalLevel() {
    return totalLevel;
  }

  int combatLevel() {
    return combatLevel;
  }

  Entry entryForSkill(int skillId) {
    for (Entry entry : entries) {
      if (entry.skillId() == skillId) {
        return entry;
      }
    }
    return null;
  }

  static int experienceForLevel(int level) {
    int clampedLevel = Math.max(1, Math.min(level, EXPERIENCE_FOR_LEVEL.length - 1));
    return EXPERIENCE_FOR_LEVEL[clampedLevel];
  }

  private static Map<Integer, SkillSnapshot> snapshots(ClientViewModel viewModel) {
    HashMap<Integer, SkillSnapshot> skillsById = new HashMap<>();
    List<BootstrapSkillPresentation> presentedSkills = viewModel.skillPresentation();
    if (!presentedSkills.isEmpty()) {
      for (BootstrapSkillPresentation skill : presentedSkills) {
        skillsById.put(
            skill.skillId(),
            new SkillSnapshot(skill.skillId(), skill.currentLevel(), baseLevelForExperience(skill.experience()), skill.experience())
        );
      }
      return skillsById;
    }
    for (BootstrapSkill skill : viewModel.skills()) {
      skillsById.put(
          skill.skillId(),
          new SkillSnapshot(skill.skillId(), skill.currentLevel(), baseLevelForExperience(skill.experience()), skill.experience())
      );
    }
    return skillsById;
  }

  private static int combatLevel(Map<Integer, SkillSnapshot> skillsById) {
    int attack = baseLevel(skillsById, 0);
    int defence = baseLevel(skillsById, 1);
    int strength = baseLevel(skillsById, 2);
    int hitpoints = Math.max(baseLevel(skillsById, 3), 10);
    int ranged = baseLevel(skillsById, 4);
    int prayer = baseLevel(skillsById, 5);
    int magic = baseLevel(skillsById, 6);

    double base = 0.25d * (defence + hitpoints + Math.floor(prayer / 2.0d));
    double melee = 0.325d * (attack + strength);
    double ranger = 0.325d * Math.floor(ranged * 1.5d);
    double mage = 0.325d * Math.floor(magic * 1.5d);
    return (int) Math.floor(base + Math.max(melee, Math.max(ranger, mage)));
  }

  private static int baseLevel(Map<Integer, SkillSnapshot> skillsById, int skillId) {
    return skillsById.getOrDefault(skillId, SkillSnapshot.empty(skillId)).baseLevel();
  }

  private static int baseLevelForExperience(int experience) {
    int clampedExperience = Math.max(0, experience);
    int level = 1;
    for (int currentLevel = 2; currentLevel < EXPERIENCE_FOR_LEVEL.length; currentLevel++) {
      if (clampedExperience < EXPERIENCE_FOR_LEVEL[currentLevel]) {
        break;
      }
      level = currentLevel;
    }
    return level;
  }

  private static int[] buildExperienceTable() {
    int[] table = new int[MAX_LEVEL + 1];

    int accumualted = 0;

    for (int level = 2; level <= MAX_LEVEL; level++) {
      int previousLevel = level - 1;

      int points = (int) Math.floor(
        previousLevel + 300.0 * Math.pow(2.0, previousLevel / 7.0)
      );

      accumualted += points;
      table[level] = accumualted / 4;
    }

    return table;
  }

  private static String skillName(int skillId) {
    if (skillId < 0 || skillId >= SKILL_NAMES.length) {
      return "Skill " + skillId;
    }
    return SKILL_NAMES[skillId];
  }

  record Entry(
      int skillId,
      String name,
      int currentLevel,
      int baseLevel,
      int experience
  ) {
  }

  private record SkillSnapshot(int skillId, int currentLevel, int baseLevel, int experience) {

    private static SkillSnapshot empty(int skillId) {
      return new SkillSnapshot(skillId, 0, 0, 0);
    }
  }
}
