package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapSkill;
import io.github.ffakira.rsps.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

final class BootstrapSidebarPresenter {

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
      "Runecraft"
  };

  BootstrapSidebarModel present(ClientViewModel viewModel) {
    CharacterBootstrapPayload bootstrap = viewModel.bootstrap();
    if (bootstrap == null) {
      return null;
    }
    List<String> headerLines = List.of(
        viewModel.statusText(),
        describeWorldState(bootstrap.regionKey(), bootstrap.worldPoint())
    );
    return new BootstrapSidebarModel(
        bootstrap.displayName(),
        headerLines,
        List.of(
            new Section("Profile", profileLines(bootstrap)),
            new Section("Equipment", itemSectionLines(bootstrap.equipment(), 14)),
            new Section("Inventory", itemSectionLines(bootstrap.inventory(), 28)),
            new Section("Skills", skillLines(bootstrap.skills()))
        )
    );
  }

  private List<String> profileLines(CharacterBootstrapPayload bootstrap) {
    return List.of(
        "Account " + bootstrap.accountId() + "  Character " + bootstrap.characterId(),
        "Rights " + bootstrap.profile().rights() + "  " + (bootstrap.profile().member() ? "Member" : "Free"),
        "Run energy " + bootstrap.profile().runEnergy() + "%",
        bootstrap.worldPoint() == null ? "Coords pending" : formatWorldPoint(bootstrap.worldPoint())
    );
  }

  private List<String> itemSectionLines(List<BootstrapItemSlot> itemSlots, int capacity) {
    if (itemSlots.isEmpty()) {
      return List.of("0/" + capacity + " occupied", "No synced items");
    }
    List<String> lines = new ArrayList<>();
    lines.add(itemSlots.size() + "/" + capacity + " occupied");
    int visibleLines = Math.min(2, itemSlots.size());
    for (int index = 0; index < visibleLines; index++) {
      lines.add(formatItemSlot(itemSlots.get(index)));
    }
    if (itemSlots.size() > visibleLines) {
      lines.add("+" + (itemSlots.size() - visibleLines) + " more stacks");
    } else {
      int totalQuantity = itemSlots.stream().mapToInt(BootstrapItemSlot::quantity).sum();
      lines.add("Total qty " + formatQuantity(totalQuantity));
    }
    return List.copyOf(lines);
  }

  private List<String> skillLines(List<BootstrapSkill> skills) {
    if (skills.isEmpty()) {
      return List.of("No skill data synced");
    }
    return skills.stream()
        .sorted(
            Comparator.comparingInt(BootstrapSkill::currentLevel).reversed()
                .thenComparingInt(BootstrapSkill::experience).reversed()
                .thenComparingInt(BootstrapSkill::skillId)
        )
        .limit(4)
        .map(skill -> skillName(skill.skillId()) + " " + skill.currentLevel())
        .toList();
  }

  private String formatItemSlot(BootstrapItemSlot itemSlot) {
    return "Slot " + itemSlot.slotIndex() + ": " + itemSlot.itemId() + " x" + formatQuantity(itemSlot.quantity());
  }

  private String describeWorldState(String regionKey, WorldPoint worldPoint) {
    if (worldPoint == null) {
      return friendlyRegion(regionKey) + " | Awaiting position";
    }
    return friendlyRegion(regionKey) + " | " + formatWorldPoint(worldPoint);
  }

  private String skillName(int skillId) {
    if (skillId >= 0 && skillId < SKILL_NAMES.length) {
      return SKILL_NAMES[skillId];
    }
    return "Skill " + skillId;
  }

  private String friendlyRegion(String regionKey) {
    if (regionKey == null || regionKey.isBlank()) {
      return "Unknown region";
    }
    String[] parts = regionKey.replace('-', ' ').replace('_', ' ').split("\\s+");
    StringBuilder builder = new StringBuilder();
    for (String part : parts) {
      if (part.isEmpty()) {
        continue;
      }
      if (!builder.isEmpty()) {
        builder.append(' ');
      }
      builder.append(Character.toUpperCase(part.charAt(0)));
      if (part.length() > 1) {
        builder.append(part.substring(1).toLowerCase(Locale.ROOT));
      }
    }
    return builder.toString();
  }

  private String formatWorldPoint(WorldPoint worldPoint) {
    return worldPoint.x() + ", " + worldPoint.y() + ", " + worldPoint.plane();
  }

  private String formatQuantity(int quantity) {
    return String.format(Locale.ROOT, "%,d", quantity);
  }

  record BootstrapSidebarModel(String title, List<String> headerLines, List<Section> sections) {

    BootstrapSidebarModel {
      headerLines = List.copyOf(headerLines);
      sections = List.copyOf(sections);
    }
  }

  record Section(String title, List<String> lines) {

    Section {
      lines = List.copyOf(lines);
    }
  }
}
