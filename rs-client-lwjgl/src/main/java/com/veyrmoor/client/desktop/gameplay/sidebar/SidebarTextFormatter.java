package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.content.ItemDefinition;
import com.veyrmoor.content.ItemDefinitionCatalog;
import java.text.NumberFormat;

final class SidebarTextFormatter {

  private static final NumberFormat LEGACY_NUMBER_FORMAT = NumberFormat.getIntegerInstance();

  private final ItemDefinitionCatalog itemDefinitionCatalog;

  SidebarTextFormatter(ItemDefinitionCatalog itemDefinitionCatalog) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
  }

  String resolveItemName(int itemId) {
    if (itemDefinitionCatalog == null) {
      return "item-" + itemId;
    }
    return itemDefinitionCatalog.find(itemId)
        .map(ItemDefinition::name)
        .orElse("item-" + itemId);
  }

  String formatQuantity(int quantity) {
    if (quantity >= InventoryEquipmentSidebarPanelRenderer.MILLION_STACK_THRESHOLD) {
      return (quantity / InventoryEquipmentSidebarPanelRenderer.MILLION_STACK_DIVISOR) + "M";
    }
    if (quantity >= InventoryEquipmentSidebarPanelRenderer.THOUSAND_STACK_THRESHOLD) {
      return (quantity / InventoryEquipmentSidebarPanelRenderer.THOUSAND_STACK_DIVISOR) + "K";
    }
    return Integer.toString(quantity);
  }

  String formatLegacyNumber(int value) {
    synchronized (LEGACY_NUMBER_FORMAT) {
      return LEGACY_NUMBER_FORMAT.format(value);
    }
  }
}
