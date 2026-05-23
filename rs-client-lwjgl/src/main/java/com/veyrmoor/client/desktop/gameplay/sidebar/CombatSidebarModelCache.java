package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.core.EquipmentLoadout;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class CombatSidebarModelCache {

  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final Map<Integer, GameplayCombatSidebarModel> modelsByWeaponItemId = new HashMap<>();

  CombatSidebarModelCache(ItemDefinitionCatalog itemDefinitionCatalog) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
  }

  GameplayCombatSidebarModel combatModelFor(ClientViewModel viewModel) {
    int weaponItemId = weaponItemId(viewModel.equipment());
    return modelsByWeaponItemId.computeIfAbsent(
        weaponItemId,
        ignored -> GameplayCombatSidebarModel.from(viewModel.equipment(), itemDefinitionCatalog)
    );
  }

  private static int weaponItemId(List<BootstrapItemSlot> equipment) {
    for (BootstrapItemSlot itemSlot : equipment) {
      if (itemSlot != null && itemSlot.slotIndex() == EquipmentLoadout.WEAPON_SLOT) {
        return itemSlot.itemId();
      }
    }
    return -1;
  }
}
