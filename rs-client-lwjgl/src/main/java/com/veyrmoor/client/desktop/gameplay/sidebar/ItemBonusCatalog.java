package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ItemBonusCatalog {

  private static final Path MODERN_REFERENCE_ITEM_CFG = Path.of("moparscape-reference", "client", "item.cfg");
  private static final Path LEGACY_REFERENCE_ITEM_CFG = Path.of("client", "item.cfg");

  private final Map<Integer, EquipmentBonuses> bonusesByItemId;

  private ItemBonusCatalog(Map<Integer, EquipmentBonuses> bonusesByItemId) {
    this.bonusesByItemId = bonusesByItemId;
  }

  static ItemBonusCatalog loadFromWorkingDirectory() {
    Path itemConfigPath = Files.exists(MODERN_REFERENCE_ITEM_CFG) ? MODERN_REFERENCE_ITEM_CFG : LEGACY_REFERENCE_ITEM_CFG;
    if (!Files.exists(itemConfigPath)) {
      return empty();
    }
    try (BufferedReader reader = Files.newBufferedReader(itemConfigPath, StandardCharsets.ISO_8859_1)) {
      return parse(reader);
    } catch (IOException | RuntimeException ignored) {
      return empty();
    }
  }

  static ItemBonusCatalog parse(Reader reader) throws IOException {
    BufferedReader bufferedReader = reader instanceof BufferedReader buffered ? buffered : new BufferedReader(reader);
    Map<Integer, EquipmentBonuses> bonusesByItemId = new HashMap<>();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      String trimmedLine = line.trim();
      if (trimmedLine.isEmpty() || trimmedLine.charAt(0) == '/' || trimmedLine.charAt(0) == '[') {
        continue;
      }
      int delimiterIndex = trimmedLine.indexOf('=');
      if (delimiterIndex < 0 || !"item".equals(trimmedLine.substring(0, delimiterIndex).trim())) {
        continue;
      }
      String[] fields = trimmedLine.substring(delimiterIndex + 1).trim().split("\\t+");
      if (fields.length < 18) {
        continue;
      }
      int itemId = Integer.parseInt(fields[0]);
      int[] bonuses = new int[EquipmentBonuses.STAT_COUNT];
      for (int index = 0; index < bonuses.length; index++) {
        bonuses[index] = Integer.parseInt(fields[6 + index]);
      }
      bonusesByItemId.put(itemId, EquipmentBonuses.of(bonuses));
    }
    return new ItemBonusCatalog(bonusesByItemId);
  }

  static ItemBonusCatalog empty() {
    return new ItemBonusCatalog(Map.of());
  }

  EquipmentBonuses bonusesFor(int itemId) {
    return bonusesByItemId.getOrDefault(itemId, EquipmentBonuses.ZERO);
  }

  EquipmentBonuses totalBonuses(List<BootstrapItemSlot> equipment) {
    if (equipment == null || equipment.isEmpty()) {
      return EquipmentBonuses.ZERO;
    }
    EquipmentBonuses totalBonuses = EquipmentBonuses.ZERO;
    for (BootstrapItemSlot equippedItem : equipment) {
      if (equippedItem == null || equippedItem.itemId() < 0) {
        continue;
      }
      totalBonuses = totalBonuses.plus(bonusesFor(equippedItem.itemId()));
    }
    return totalBonuses;
  }

  record EquipmentBonuses(
      int attackStab,
      int attackSlash,
      int attackCrush,
      int attackMagic,
      int attackRange,
      int defenceStab,
      int defenceSlash,
      int defenceCrush,
      int defenceMagic,
      int defenceRange,
      int strength,
      int prayer
  ) {

    private static final int STAT_COUNT = 12;
    private static final EquipmentBonuses ZERO = new EquipmentBonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    static EquipmentBonuses of(int[] bonuses) {
      if (bonuses == null || bonuses.length < STAT_COUNT) {
        return ZERO;
      }
      return new EquipmentBonuses(
          bonuses[0],
          bonuses[1],
          bonuses[2],
          bonuses[3],
          bonuses[4],
          bonuses[5],
          bonuses[6],
          bonuses[7],
          bonuses[8],
          bonuses[9],
          bonuses[10],
          bonuses[11]
      );
    }

    EquipmentBonuses plus(EquipmentBonuses other) {
      if (other == null || other == ZERO) {
        return this;
      }
      return new EquipmentBonuses(
          attackStab + other.attackStab,
          attackSlash + other.attackSlash,
          attackCrush + other.attackCrush,
          attackMagic + other.attackMagic,
          attackRange + other.attackRange,
          defenceStab + other.defenceStab,
          defenceSlash + other.defenceSlash,
          defenceCrush + other.defenceCrush,
          defenceMagic + other.defenceMagic,
          defenceRange + other.defenceRange,
          strength + other.strength,
          prayer + other.prayer
      );
    }
  }
}
