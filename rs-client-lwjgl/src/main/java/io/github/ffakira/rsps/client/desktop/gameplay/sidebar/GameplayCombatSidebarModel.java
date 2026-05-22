package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.core.EquipmentLoadout;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import java.util.List;
import java.util.Locale;

final class GameplayCombatSidebarModel {

  private static final String UNARMED_NAME = "Unarmed";
  private static final String[] KNOWN_MATERIAL_PREFIXES = {
      "bronze", "iron", "steel", "black", "mithril", "adamant", "rune", "granite", "dragon", "crystal"
  };
  private static final WeaponSidebarLayout UNARMED_LAYOUT = new WeaponSidebarLayout(5855, -1, 5857);
  private static final WeaponSidebarLayout STAFF_LAYOUT = new WeaponSidebarLayout(328, 329, 331);
  private static final WeaponSidebarLayout BOW_LAYOUT = new WeaponSidebarLayout(1764, 1765, 1767);
  private static final WeaponSidebarLayout DEFAULT_LAYOUT = new WeaponSidebarLayout(2423, 2424, 2426);
  private static final WeaponSidebarLayout DAGGER_LAYOUT = new WeaponSidebarLayout(2276, 2277, 2279);
  private static final WeaponSidebarLayout DART_LAYOUT = new WeaponSidebarLayout(4446, 4447, 4449);
  private static final WeaponSidebarLayout SPEAR_LAYOUT = new WeaponSidebarLayout(4679, 4680, 4682);
  private static final WeaponSidebarLayout PICKAXE_LAYOUT = new WeaponSidebarLayout(5570, 5571, 5573);
  private static final WeaponSidebarLayout HALBERD_LAYOUT = new WeaponSidebarLayout(8460, 8461, 8463);
  private static final WeaponSidebarLayout WHIP_LAYOUT = new WeaponSidebarLayout(12290, 12291, 12293);
  private static final WeaponSidebarLayout AXE_LAYOUT = new WeaponSidebarLayout(1698, 1699, 1701);
  private static final WeaponProfile UNARMED = new WeaponProfile(
      List.of(
          new AttackStyle("Punch", "Accurate", "Crush"),
          new AttackStyle("Kick", "Aggressive", "Crush"),
          new AttackStyle("Block", "Defensive", "Crush")
      )
  );
  private static final WeaponProfile PICKAXE = new WeaponProfile(
      List.of(
          new AttackStyle("Spike", "Accurate", "Stab"),
          new AttackStyle("Impale", "Aggressive", "Stab"),
          new AttackStyle("Smash", "Aggressive", "Crush"),
          new AttackStyle("Block", "Defensive", "Stab")
      )
  );
  private static final WeaponProfile STAFF = new WeaponProfile(
      List.of(
          new AttackStyle("Bash", "Accurate", "Crush"),
          new AttackStyle("Pound", "Aggressive", "Crush"),
          new AttackStyle("Focus", "Defensive", "Crush")
      )
  );
  private static final WeaponProfile SCIMITAR = new WeaponProfile(
      List.of(
          new AttackStyle("Chop", "Accurate", "Slash"),
          new AttackStyle("Slash", "Aggressive", "Slash"),
          new AttackStyle("Lunge", "Controlled", "Stab"),
          new AttackStyle("Block", "Defensive", "Slash")
      )
  );
  private static final WeaponProfile SWORD = new WeaponProfile(
      List.of(
          new AttackStyle("Stab", "Accurate", "Stab"),
          new AttackStyle("Lunge", "Aggressive", "Stab"),
          new AttackStyle("Slash", "Aggressive", "Slash"),
          new AttackStyle("Block", "Defensive", "Slash")
      )
  );
  private static final WeaponProfile DAGGER = new WeaponProfile(
      List.of(
          new AttackStyle("Stab", "Accurate", "Stab"),
          new AttackStyle("Lunge", "Aggressive", "Stab"),
          new AttackStyle("Slash", "Aggressive", "Slash"),
          new AttackStyle("Block", "Defensive", "Stab")
      )
  );
  private static final WeaponProfile MACE = new WeaponProfile(
      List.of(
          new AttackStyle("Pound", "Accurate", "Crush"),
          new AttackStyle("Pummel", "Aggressive", "Crush"),
          new AttackStyle("Spike", "Controlled", "Stab"),
          new AttackStyle("Block", "Defensive", "Crush")
      )
  );
  private static final WeaponProfile AXE = new WeaponProfile(
      List.of(
          new AttackStyle("Chop", "Accurate", "Slash"),
          new AttackStyle("Hack", "Aggressive", "Slash"),
          new AttackStyle("Smash", "Aggressive", "Crush"),
          new AttackStyle("Block", "Defensive", "Slash")
      )
  );
  private static final WeaponProfile BOW = new WeaponProfile(
      List.of(
          new AttackStyle("Accurate", "Accurate", "Ranged"),
          new AttackStyle("Rapid", "Rapid", "Ranged"),
          new AttackStyle("Longrange", "Defensive", "Ranged")
      )
  );
  private static final WeaponProfile WHIP = new WeaponProfile(
      List.of(
          new AttackStyle("Flick", "Accurate", "Slash"),
          new AttackStyle("Lash", "Controlled", "Slash"),
          new AttackStyle("Deflect", "Defensive", "Slash")
      )
  );

  private final int weaponItemId;
  private final String weaponName;
  private final int interfaceId;
  private final int itemComponentId;
  private final int weaponNameComponentId;
  private final List<AttackStyle> attackStyles;

  private GameplayCombatSidebarModel(
      int weaponItemId,
      String weaponName,
      int interfaceId,
      int itemComponentId,
      int weaponNameComponentId,
      List<AttackStyle> attackStyles
  ) {
    this.weaponItemId = weaponItemId;
    this.weaponName = weaponName;
    this.interfaceId = interfaceId;
    this.itemComponentId = itemComponentId;
    this.weaponNameComponentId = weaponNameComponentId;
    this.attackStyles = attackStyles;
  }

  static GameplayCombatSidebarModel from(EquipmentLoadout equipmentLoadout, ItemDefinitionCatalog itemDefinitions) {
    return fromWeaponItemId(equipmentLoadout.itemIdAt(EquipmentLoadout.WEAPON_SLOT), itemDefinitions);
  }

  static GameplayCombatSidebarModel from(List<BootstrapItemSlot> equipment, ItemDefinitionCatalog itemDefinitions) {
    return fromWeaponItemId(weaponItemId(equipment), itemDefinitions);
  }

  int weaponItemId() {
    return weaponItemId;
  }

  String weaponName() {
    return weaponName;
  }

  int interfaceId() {
    return interfaceId;
  }

  int itemComponentId() {
    return itemComponentId;
  }

  int weaponNameComponentId() {
    return weaponNameComponentId;
  }

  List<AttackStyle> attackStyles() {
    return attackStyles;
  }

  private static WeaponPresentation weaponPresentationFor(String weaponName) {
    if (UNARMED_NAME.equals(weaponName)) {
      return new WeaponPresentation(UNARMED_LAYOUT, UNARMED);
    }
    String normalized = weaponName.toLowerCase(Locale.ROOT);
    String stripped = stripKnownMaterialPrefixes(normalized);
    return new WeaponPresentation(sidebarLayoutFor(normalized, stripped), profileFor(normalized));
  }

  private static WeaponProfile profileFor(String normalized) {
    if (normalized.contains("pickaxe")) {
      return PICKAXE;
    }
    if (normalized.contains("whip")) {
      return WHIP;
    }
    if (normalized.contains("crossbow") || normalized.contains("shortbow") || normalized.contains("longbow")
        || (normalized.contains("bow") && !normalized.contains("rainbow"))) {
      return BOW;
    }
    if (normalized.contains("staff")) {
      return STAFF;
    }
    if (normalized.contains("scimitar")) {
      return SCIMITAR;
    }
    if (normalized.contains("dagger")) {
      return DAGGER;
    }
    if (normalized.contains("mace") || normalized.contains("flail")) {
      return MACE;
    }
    if (normalized.contains("battleaxe") || normalized.contains("greataxe")
        || normalized.contains("hatchet") || (normalized.contains("axe") && !normalized.contains("pickaxe"))) {
      return AXE;
    }
    if (normalized.contains("sword") || normalized.contains("rapier")) {
      return SWORD;
    }
    return UNARMED;
  }

  private static String resolveItemName(ItemDefinitionCatalog itemDefinitions, int itemId) {
    if (itemDefinitions == null) {
      return "item-" + itemId;
    }
    return itemDefinitions.find(itemId)
        .map(ItemDefinition::name)
        .orElse("item-" + itemId);
  }

  private static WeaponSidebarLayout sidebarLayoutFor(String normalized, String stripped) {
    if (normalized.endsWith("whip")) {
      return WHIP_LAYOUT;
    }
    if (normalized.endsWith("bow")) {
      return BOW_LAYOUT;
    }
    if (normalized.startsWith("staff") || normalized.endsWith("staff")) {
      return STAFF_LAYOUT;
    }
    if (stripped.startsWith("dart")) {
      return DART_LAYOUT;
    }
    if (stripped.startsWith("dagger")) {
      return DAGGER_LAYOUT;
    }
    if (stripped.startsWith("pickaxe")) {
      return PICKAXE_LAYOUT;
    }
    if (stripped.startsWith("axe") || stripped.startsWith("battleaxe")) {
      return AXE_LAYOUT;
    }
    if (stripped.startsWith("halberd")) {
      return HALBERD_LAYOUT;
    }
    if (stripped.startsWith("spear")) {
      return SPEAR_LAYOUT;
    }
    return DEFAULT_LAYOUT;
  }

  private static String stripKnownMaterialPrefixes(String normalizedName) {
    String stripped = normalizedName;
    for (String material : KNOWN_MATERIAL_PREFIXES) {
      stripped = stripped.replace(material, "");
    }
    return stripped.trim();
  }

  private static GameplayCombatSidebarModel fromWeaponItemId(int weaponItemId, ItemDefinitionCatalog itemDefinitions) {
    if (weaponItemId < 0) {
      return new GameplayCombatSidebarModel(
          -1,
          UNARMED_NAME,
          UNARMED_LAYOUT.interfaceId(),
          UNARMED_LAYOUT.itemComponentId(),
          UNARMED_LAYOUT.weaponNameComponentId(),
          UNARMED.attackStyles()
      );
    }
    String weaponName = resolveItemName(itemDefinitions, weaponItemId);
    WeaponPresentation presentation = weaponPresentationFor(weaponName);
    return new GameplayCombatSidebarModel(
        weaponItemId,
        weaponName,
        presentation.layout().interfaceId(),
        presentation.layout().itemComponentId(),
        presentation.layout().weaponNameComponentId(),
        presentation.profile().attackStyles()
    );
  }

  private static int weaponItemId(List<BootstrapItemSlot> equipment) {
    if (equipment == null || equipment.isEmpty()) {
      return -1;
    }
    for (BootstrapItemSlot itemSlot : equipment) {
      if (itemSlot != null && itemSlot.slotIndex() == EquipmentLoadout.WEAPON_SLOT) {
        return itemSlot.itemId();
      }
    }
    return -1;
  }

  record AttackStyle(String name, String stance, String combatType) {
  }

  private record WeaponProfile(List<AttackStyle> attackStyles) {
  }

  private record WeaponSidebarLayout(int interfaceId, int itemComponentId, int weaponNameComponentId) {
  }

  private record WeaponPresentation(WeaponSidebarLayout layout, WeaponProfile profile) {
  }
}
