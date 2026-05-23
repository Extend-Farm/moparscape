package com.veyrmoor.client.core;

import java.util.List;

public record BootstrapCharacterPresentation(
    List<BootstrapInventoryItemPresentation> inventory,
    List<BootstrapEquipmentItemPresentation> equipment,
    List<BootstrapSkillPresentation> skills
) {

  public BootstrapCharacterPresentation {
    inventory = List.copyOf(inventory);
    equipment = List.copyOf(equipment);
    skills = List.copyOf(skills);
  }
}
