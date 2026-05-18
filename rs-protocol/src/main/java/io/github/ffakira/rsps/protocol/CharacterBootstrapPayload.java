package io.github.ffakira.rsps.protocol;

import io.github.ffakira.rsps.model.WorldPoint;
import java.util.List;

public record CharacterBootstrapPayload(
    long accountId,
    long characterId,
    String displayName,
    String regionKey,
    WorldPoint worldPoint,
    BootstrapProfile profile,
    BootstrapAppearance appearance,
    List<BootstrapItemSlot> inventory,
    List<BootstrapItemSlot> equipment,
    List<BootstrapSkill> skills
) {

  public CharacterBootstrapPayload {
    inventory = List.copyOf(inventory);
    equipment = List.copyOf(equipment);
    skills = List.copyOf(skills);
  }
}
