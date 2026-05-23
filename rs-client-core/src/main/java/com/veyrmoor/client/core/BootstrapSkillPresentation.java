package com.veyrmoor.client.core;

public record BootstrapSkillPresentation(
    int skillId,
    String name,
    int currentLevel,
    int experience
) {
}
