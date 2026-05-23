package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.BootstrapEquipmentItemPresentation;
import com.veyrmoor.client.core.BootstrapInventoryItemPresentation;
import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.core.EquipmentLoadout;
import com.veyrmoor.client.desktop.gameplay.GameplayTab;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.util.List;

final class InventoryEquipmentSidebarPanelRenderer {

  static final int INVENTORY_STACK_TEXT_RGB = 0xffff00;
  static final int MILLION_STACK_THRESHOLD = 10_000_000;
  static final int THOUSAND_STACK_THRESHOLD = 100_000;
  static final int MILLION_STACK_DIVISOR = 1_000_000;
  static final int THOUSAND_STACK_DIVISOR = 1_000;

  private static final int INVENTORY_COLUMNS = 4;
  private static final int INVENTORY_ROWS = 7;
  private static final float INVENTORY_SLOT_STEP_X = 42.0f;
  private static final float INVENTORY_SLOT_STEP_Y = 36.0f;
  private static final float INVENTORY_SLOT_LEFT = 12.0f;
  private static final float INVENTORY_SLOT_TOP = 11.0f;
  private static final float INVENTORY_ICON_SIZE = 32.0f;
  private static final float INVENTORY_STACK_TEXT_LEFT_OFFSET = 1.0f;
  private static final float INVENTORY_STACK_TEXT_BASELINE_Y = 9.0f;
  private static final int EQUIPMENT_WIDGET_INTERFACE_ID = 1644;
  private static final int EQUIPMENT_WIDGET_GRID_COMPONENT_ID = 1688;
  private static final int ATTACK_STAB_COMPONENT_ID = 1675;
  private static final int ATTACK_SLASH_COMPONENT_ID = 1676;
  private static final int ATTACK_CRUSH_COMPONENT_ID = 1677;
  private static final int ATTACK_MAGIC_COMPONENT_ID = 1678;
  private static final int ATTACK_RANGE_COMPONENT_ID = 1679;
  private static final int DEFENCE_STAB_COMPONENT_ID = 1680;
  private static final int DEFENCE_SLASH_COMPONENT_ID = 1681;
  private static final int DEFENCE_CRUSH_COMPONENT_ID = 1682;
  private static final int DEFENCE_MAGIC_COMPONENT_ID = 1683;
  private static final int DEFENCE_RANGE_COMPONENT_ID = 1684;
  private static final int STRENGTH_COMPONENT_ID = 1686;
  private static final int PRAYER_COMPONENT_ID = 1687;

  private final GameplaySidebarRenderer owner;
  private final ItemBonusCatalog itemBonusCatalog;

  InventoryEquipmentSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
    this.itemBonusCatalog = ItemBonusCatalog.loadFromWorkingDirectory();
  }

  void drawInventorySidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (!owner.textures().canRenderItemIcons()) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (inventory.isEmpty()) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    for (BootstrapInventoryItemPresentation item : inventory) {
      if (item.slotIndex() < 0 || item.slotIndex() >= INVENTORY_COLUMNS * INVENTORY_ROWS) {
        continue;
      }
      ScreenRect slotRect = inventorySlotRect(sidebarRect, item.slotIndex());
      OpenGlTexture itemTexture = owner.textures().itemIconTexture(item.itemId(), item.quantity());
      if (itemTexture != null) {
        owner.primitives().drawTexturedQuad(itemTexture, slotRect);
      }
      if (showsStackAmount(item)) {
        drawInventoryStackAmount(slotRect, owner.textFormatter().formatQuantity(item.quantity()));
      }
    }
  }

  void drawEquipmentSidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(EQUIPMENT_WIDGET_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(
          sidebarRect,
          EQUIPMENT_WIDGET_INTERFACE_ID,
          viewModel,
          equipmentWidgetOverrides(viewModel),
          equipmentGridResolver(viewModel)
      );
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    List<BootstrapEquipmentItemPresentation> equipment = viewModel.equipmentPresentation();
    if (!equipment.isEmpty()) {
      int row = 0;
      for (BootstrapEquipmentItemPresentation item : equipment) {
        int column = row % 2;
        int currentRow = row / 2;
        float columnLeft = left + column * 82.0f;
        float rowTop = top + currentRow * 26.0f;
        owner.primitives().drawText(columnLeft, rowTop, truncate(item.slotName(), 8), 0.84f, 0.88f, 0.94f);
        owner.primitives().drawText(columnLeft, rowTop + 12.0f, truncate(item.name(), 11), 0.95f, 0.96f, 0.98f);
        row++;
        if (row >= 10) {
          break;
        }
      }
      return;
    }
    drawEquipmentList(left, top, viewModel.equipment(), 2);
  }

  private SidebarWidgetRenderer.WidgetOverrideResolver equipmentWidgetOverrides(ClientViewModel viewModel) {
    ItemBonusCatalog.EquipmentBonuses bonuses = itemBonusCatalog.totalBonuses(viewModel.equipment());
    // The cache only ships placeholder bonus strings for 1644; the reference client rewrites them
    // from item.cfg at runtime, so the native decoded widget needs the same compatibility override.
    return componentId -> switch (componentId) {
      case ATTACK_STAB_COMPONENT_ID -> widgetTextOverride("Stab", bonuses.attackStab());
      case ATTACK_SLASH_COMPONENT_ID -> widgetTextOverride("Slash", bonuses.attackSlash());
      case ATTACK_CRUSH_COMPONENT_ID -> widgetTextOverride("Crush", bonuses.attackCrush());
      case ATTACK_MAGIC_COMPONENT_ID -> widgetTextOverride("Magic", bonuses.attackMagic());
      case ATTACK_RANGE_COMPONENT_ID -> widgetTextOverride("Range", bonuses.attackRange());
      case DEFENCE_STAB_COMPONENT_ID -> widgetTextOverride("Stab", bonuses.defenceStab());
      case DEFENCE_SLASH_COMPONENT_ID -> widgetTextOverride("Slash", bonuses.defenceSlash());
      case DEFENCE_CRUSH_COMPONENT_ID -> widgetTextOverride("Crush", bonuses.defenceCrush());
      case DEFENCE_MAGIC_COMPONENT_ID -> widgetTextOverride("Magic", bonuses.defenceMagic());
      case DEFENCE_RANGE_COMPONENT_ID -> widgetTextOverride("Range", bonuses.defenceRange());
      case STRENGTH_COMPONENT_ID -> widgetTextOverride("Strength", bonuses.strength());
      case PRAYER_COMPONENT_ID -> widgetTextOverride("Prayer", bonuses.prayer());
      default -> null;
    };
  }

  private SidebarWidgetRenderer.WidgetInventoryGridResolver equipmentGridResolver(ClientViewModel viewModel) {
    EquipmentLoadout equipmentLoadout = viewModel.equipmentLoadout();
    return (componentId, slotIndex) -> {
      if (componentId != EQUIPMENT_WIDGET_GRID_COMPONENT_ID) {
        return null;
      }
      return equipmentLoadout.itemInSlot(slotIndex)
          .map(slot -> new SidebarWidgetRenderer.WidgetGridItem(slot.itemId(), slot.quantity()))
          .orElse(null);
    };
  }

  private SidebarWidgetRenderer.WidgetOverride widgetTextOverride(String label, int value) {
    return new SidebarWidgetRenderer.WidgetOverride(label + ": " + signedValue(value), null);
  }

  private static String signedValue(int value) {
    return value >= 0 ? "+" + value : Integer.toString(value);
  }

  private void drawInventoryTextFallback(ClientViewModel viewModel, float left, float top) {
    owner.primitives().drawText(left, top - 16.0f, GameplayTab.INVENTORY.label(), 0.92f, 0.86f, 0.46f);
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (!inventory.isEmpty()) {
      int lineCount = Math.min(10, inventory.size());
      for (int index = 0; index < lineCount; index++) {
        BootstrapInventoryItemPresentation item = inventory.get(index);
        owner.primitives().drawText(
            left,
            top + index * 12.0f,
            truncate(item.name(), 18) + " x" + owner.textFormatter().formatQuantity(item.quantity()),
            0.95f,
            0.96f,
            0.98f
        );
      }
      if (inventory.size() > lineCount) {
        owner.primitives().drawText(
            left,
            top + lineCount * 12.0f,
            "+" + (inventory.size() - lineCount) + " more items",
            0.70f,
            0.74f,
            0.82f
        );
      }
      return;
    }
    drawResolvedItemList(left, top, viewModel.inventory(), 10);
  }

  private void drawResolvedItemList(float left, float top, List<BootstrapItemSlot> itemSlots, int maxLines) {
    int lineCount = Math.min(maxLines, itemSlots.size());
    for (int index = 0; index < lineCount; index++) {
      BootstrapItemSlot slot = itemSlots.get(index);
      owner.primitives().drawText(
          left,
          top + index * 12.0f,
          truncate(owner.textFormatter().resolveItemName(slot.itemId()), 18)
              + " x"
              + owner.textFormatter().formatQuantity(slot.quantity()),
          0.95f,
          0.96f,
          0.98f
      );
    }
    if (itemSlots.size() > lineCount) {
      owner.primitives().drawText(
          left,
          top + lineCount * 12.0f,
          "+" + (itemSlots.size() - lineCount) + " more items",
          0.70f,
          0.74f,
          0.82f
      );
    }
  }

  private void drawEquipmentList(float left, float top, List<BootstrapItemSlot> equipment, int columns) {
    int rowCount = 0;
    for (BootstrapItemSlot slot : equipment) {
      if (rowCount >= EquipmentLoadout.SLOT_COUNT) {
        break;
      }
      int column = rowCount % columns;
      int row = rowCount / columns;
      float columnLeft = left + column * 82.0f;
      float rowTop = top + row * 24.0f;
      owner.primitives().drawText(columnLeft, rowTop, EquipmentLoadout.slotName(slot.slotIndex()), 0.84f, 0.88f, 0.94f);
      owner.primitives().drawText(
          columnLeft,
          rowTop + 12.0f,
          truncate(owner.textFormatter().resolveItemName(slot.itemId()), 11),
          0.95f,
          0.96f,
          0.98f
      );
      rowCount++;
    }
  }

  private void drawInventoryStackAmount(ScreenRect slotRect, String text) {
    if (owner.textures().inventoryAmountFont() == null) {
      owner.primitives().drawText(
          slotRect.left() + INVENTORY_STACK_TEXT_LEFT_OFFSET,
          slotRect.top() + INVENTORY_STACK_TEXT_BASELINE_Y + 1.0f,
          text,
          0.0f,
          0.0f,
          0.0f,
          0.55f
      );
      owner.primitives().drawText(
          slotRect.left(),
          slotRect.top() + INVENTORY_STACK_TEXT_BASELINE_Y,
          text,
          1.0f,
          1.0f,
          0.0f,
          0.55f
      );
      return;
    }
    OpenGlTexture amountTexture = owner.textures().inventoryAmountTexture(text);
    if (amountTexture == null) {
      return;
    }
    owner.primitives().drawTexturedQuad(
        amountTexture,
        new ScreenRect(
            slotRect.left(),
            slotRect.top() + INVENTORY_STACK_TEXT_BASELINE_Y - owner.textures().inventoryAmountFont().maxGlyphHeight(),
            amountTexture.width(),
            amountTexture.height()
        )
    );
  }

  private boolean showsStackAmount(BootstrapInventoryItemPresentation item) {
    return item.stackable() || item.noted() || item.quantity() != 1;
  }

  private String truncate(String value, int maxChars) {
    if (value.length() <= maxChars) {
      return value;
    }
    return value.substring(0, Math.max(0, maxChars - 1)) + ".";
  }

  private ScreenRect inventorySlotRect(ScreenRect sidebarRect, int slotIndex) {
    int column = slotIndex % INVENTORY_COLUMNS;
    int row = slotIndex / INVENTORY_COLUMNS;
    return new ScreenRect(
        sidebarRect.left() + INVENTORY_SLOT_LEFT + column * INVENTORY_SLOT_STEP_X,
        sidebarRect.top() + INVENTORY_SLOT_TOP + row * INVENTORY_SLOT_STEP_Y,
        INVENTORY_ICON_SIZE,
        INVENTORY_ICON_SIZE
    );
  }
}
