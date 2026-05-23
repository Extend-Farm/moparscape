package com.veyrmoor.client.core;

import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.util.List;
import java.util.Optional;

public final class EquipmentLoadout {

  public static final int SLOT_COUNT = 14;
  public static final int HEAD_SLOT = 0;
  public static final int CAPE_SLOT = 1;
  public static final int AMULET_SLOT = 2;
  public static final int WEAPON_SLOT = 3;
  public static final int BODY_SLOT = 4;
  public static final int SHIELD_SLOT = 5;
  public static final int ARMS_SLOT = 6;
  public static final int LEGS_SLOT = 7;
  public static final int HAIR_SLOT = 8;
  public static final int HANDS_SLOT = 9;
  public static final int FEET_SLOT = 10;
  public static final int BEARD_SLOT = 11;
  public static final int RING_SLOT = 12;
  public static final int AMMO_SLOT = 13;

  private static final String[] SLOT_NAMES = {
      "Head",
      "Cape",
      "Neck",
      "Weapon",
      "Body",
      "Shield",
      "Arms",
      "Legs",
      "Hair",
      "Hands",
      "Feet",
      "Beard",
      "Ring",
      "Ammo"
  };
  private static final EquipmentLoadout EMPTY = new EquipmentLoadout(new BootstrapItemSlot[SLOT_COUNT], List.of());

  private final BootstrapItemSlot[] slotsByIndex;
  private final List<BootstrapItemSlot> equippedItems;

  private EquipmentLoadout(BootstrapItemSlot[] slotsByIndex, List<BootstrapItemSlot> equippedItems) {
    this.slotsByIndex = slotsByIndex;
    this.equippedItems = equippedItems;
  }

  public static EquipmentLoadout empty() {
    return EMPTY;
  }

  public static EquipmentLoadout from(List<BootstrapItemSlot> equipment) {
    if (equipment == null || equipment.isEmpty()) {
      return empty();
    }
    BootstrapItemSlot[] slotsByIndex = new BootstrapItemSlot[SLOT_COUNT];
    for (BootstrapItemSlot itemSlot : equipment) {
      if (itemSlot == null) {
        continue;
      }
      int slotIndex = itemSlot.slotIndex();
      if (slotIndex < 0 || slotIndex >= SLOT_COUNT || itemSlot.itemId() < 0 || slotsByIndex[slotIndex] != null) {
        continue;
      }
      slotsByIndex[slotIndex] = itemSlot;
    }
    return new EquipmentLoadout(slotsByIndex, List.copyOf(equipment));
  }

  public List<BootstrapItemSlot> equippedItems() {
    return equippedItems;
  }

  public Optional<BootstrapItemSlot> itemInSlot(int slotIndex) {
    if (slotIndex < 0 || slotIndex >= slotsByIndex.length) {
      return Optional.empty();
    }
    return Optional.ofNullable(slotsByIndex[slotIndex]);
  }

  public boolean hasItemInSlot(int slotIndex) {
    return itemInSlot(slotIndex).isPresent();
  }

  public int itemIdAt(int slotIndex) {
    return itemInSlot(slotIndex).map(BootstrapItemSlot::itemId).orElse(-1);
  }

  public static String slotName(int slotIndex) {
    if (slotIndex < 0 || slotIndex >= SLOT_NAMES.length) {
      return "Slot " + slotIndex;
    }
    return SLOT_NAMES[slotIndex];
  }
}
