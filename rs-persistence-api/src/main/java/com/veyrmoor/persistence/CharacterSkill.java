package com.veyrmoor.persistence;

public record CharacterSkill(int skillId, int currentLevel, int experience) {

  public CharacterSkill {
    if (skillId < 0) {
      throw new IllegalArgumentException("Skill id cannot be negative");
    }
    if (currentLevel < 0) {
      throw new IllegalArgumentException("Current level cannot be negative");
    }
    if (experience < 0) {
      throw new IllegalArgumentException("Experience cannot be negative");
    }
  }
}
