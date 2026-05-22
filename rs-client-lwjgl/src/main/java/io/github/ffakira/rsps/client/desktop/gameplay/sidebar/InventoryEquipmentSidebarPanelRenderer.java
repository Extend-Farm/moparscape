package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.core.BootstrapEquipmentItemPresentation;
import io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation;
import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.core.EquipmentLoadout;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayTab;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
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

  private final GameplaySidebarRenderer owner;

  InventoryEquipmentSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawInventorySidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (!owner.canRenderItemIcons()) {
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
      OpenGlTexture itemTexture = owner.itemIconTexture(item.itemId(), item.quantity());
      if (itemTexture != null) {
        owner.primitives().drawTexturedQuad(itemTexture, slotRect);
      }
      if (showsStackAmount(item)) {
        drawInventoryStackAmount(slotRect, owner.formatQuantity(item.quantity()));
      }
    }
  }

  void drawEquipmentSidebar(ClientViewModel viewModel, float left, float top) {
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
            truncate(item.name(), 18) + " x" + owner.formatQuantity(item.quantity()),
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
          truncate(owner.resolveItemName(slot.itemId()), 18) + " x" + owner.formatQuantity(slot.quantity()),
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
      owner.primitives().drawText(columnLeft, rowTop + 12.0f, truncate(owner.resolveItemName(slot.itemId()), 11), 0.95f, 0.96f, 0.98f);
      rowCount++;
    }
  }

  private void drawInventoryStackAmount(ScreenRect slotRect, String text) {
    if (owner.inventoryAmountFont() == null) {
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
    OpenGlTexture amountTexture = owner.inventoryAmountTexture(text);
    if (amountTexture == null) {
      return;
    }
    owner.primitives().drawTexturedQuad(
        amountTexture,
        new ScreenRect(
            slotRect.left(),
            slotRect.top() + INVENTORY_STACK_TEXT_BASELINE_Y - owner.inventoryAmountFont().maxGlyphHeight(),
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
