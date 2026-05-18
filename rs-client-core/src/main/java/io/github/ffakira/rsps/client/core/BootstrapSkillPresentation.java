package io.github.ffakira.rsps.client.core;

public record BootstrapSkillPresentation(
    int skillId,
    String name,
    int currentLevel,
    int experience
) {
}
