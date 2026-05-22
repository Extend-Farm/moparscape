package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapSkill;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.List;

public final class BootstrapPresentationCatalog {

  private static final String[] SKILL_NAMES = {
      "Attack",
      "Defence",
      "Strength",
      "Hitpoints",
      "Ranged",
      "Prayer",
      "Magic",
      "Cooking",
      "Woodcutting",
      "Fletching",
      "Fishing",
      "Firemaking",
      "Crafting",
      "Smithing",
      "Mining",
      "Herblore",
      "Agility",
      "Thieving",
      "Slayer",
      "Farming",
      "Runecrafting"
  };

  private final ItemDefinitionCatalog itemDefinitions;

  private BootstrapPresentationCatalog(ItemDefinitionCatalog itemDefinitions) {
    this.itemDefinitions = itemDefinitions;
  }

  public static BootstrapPresentationCatalog empty() {
    return new BootstrapPresentationCatalog(ItemDefinitionCatalog.empty());
  }

  public static BootstrapPresentationCatalog from(ItemDefinitionCatalog itemDefinitions) {
    return new BootstrapPresentationCatalog(itemDefinitions);
  }

  public BootstrapCharacterPresentation present(CharacterBootstrapPayload bootstrap) {
    return new BootstrapCharacterPresentation(
        bootstrap.inventory().stream().map(this::presentInventoryItem).toList(),
        bootstrap.equipment().stream().map(this::presentEquipmentItem).toList(),
        bootstrap.skills().stream().map(this::presentSkill).toList()
    );
  }

  private BootstrapInventoryItemPresentation presentInventoryItem(BootstrapItemSlot itemSlot) {
    ItemDefinition itemDefinition = itemDefinitions.find(itemSlot.itemId()).orElse(null);
    return new BootstrapInventoryItemPresentation(
        itemSlot.slotIndex(),
        itemSlot.itemId(),
        itemDefinition == null ? "item-" + itemSlot.itemId() : itemDefinition.name(),
        itemSlot.quantity(),
        itemDefinition != null && itemDefinition.stackable(),
        itemDefinition != null && itemDefinition.membersOnly(),
        itemDefinition != null && itemDefinition.noted()
    );
  }

  private BootstrapEquipmentItemPresentation presentEquipmentItem(BootstrapItemSlot itemSlot) {
    ItemDefinition itemDefinition = itemDefinitions.find(itemSlot.itemId()).orElse(null);
    return new BootstrapEquipmentItemPresentation(
        itemSlot.slotIndex(),
        EquipmentLoadout.slotName(itemSlot.slotIndex()),
        itemSlot.itemId(),
        itemDefinition == null ? "item-" + itemSlot.itemId() : itemDefinition.name(),
        itemSlot.quantity(),
        itemDefinition != null && itemDefinition.stackable(),
        itemDefinition != null && itemDefinition.membersOnly(),
        itemDefinition != null && itemDefinition.noted()
    );
  }

  private BootstrapSkillPresentation presentSkill(BootstrapSkill skill) {
    return new BootstrapSkillPresentation(
        skill.skillId(),
        skillName(skill.skillId()),
        skill.currentLevel(),
        skill.experience()
    );
  }

  private String skillName(int skillId) {
    if (skillId < 0 || skillId >= SKILL_NAMES.length) {
      return "Skill " + skillId;
    }
    return SKILL_NAMES[skillId];
  }
}
