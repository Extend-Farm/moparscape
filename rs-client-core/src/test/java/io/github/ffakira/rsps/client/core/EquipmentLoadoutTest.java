package io.github.ffakira.rsps.client.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.List;
import org.junit.jupiter.api.Test;

class EquipmentLoadoutTest {

  @Test
  void indexesEquippedItemsByCanonicalSlot() {
    EquipmentLoadout loadout = EquipmentLoadout.from(List.of(
        new BootstrapItemSlot(EquipmentLoadout.AMULET_SLOT, 1704, 1),
        new BootstrapItemSlot(EquipmentLoadout.WEAPON_SLOT, 1265, 1),
        new BootstrapItemSlot(EquipmentLoadout.BEARD_SLOT, 9999, 1),
        new BootstrapItemSlot(99, 4151, 1)
    ));

    assertThat(loadout.itemIdAt(EquipmentLoadout.AMULET_SLOT)).isEqualTo(1704);
    assertThat(loadout.itemIdAt(EquipmentLoadout.WEAPON_SLOT)).isEqualTo(1265);
    assertThat(loadout.itemIdAt(EquipmentLoadout.BEARD_SLOT)).isEqualTo(9999);
    assertThat(loadout.itemIdAt(99)).isEqualTo(-1);
    assertThat(loadout.hasItemInSlot(EquipmentLoadout.WEAPON_SLOT)).isTrue();
  }

  @Test
  void exposesCanonicalSlotNames() {
    assertThat(EquipmentLoadout.slotName(EquipmentLoadout.AMULET_SLOT)).isEqualTo("Neck");
    assertThat(EquipmentLoadout.slotName(EquipmentLoadout.BEARD_SLOT)).isEqualTo("Beard");
    assertThat(EquipmentLoadout.slotName(EquipmentLoadout.AMMO_SLOT)).isEqualTo("Ammo");
    assertThat(EquipmentLoadout.slotName(42)).isEqualTo("Slot 42");
  }
}
