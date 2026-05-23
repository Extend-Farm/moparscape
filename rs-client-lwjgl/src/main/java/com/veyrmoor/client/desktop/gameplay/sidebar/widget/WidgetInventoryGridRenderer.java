package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentDefinition;

final class WidgetInventoryGridRenderer {

  private static final float SLOT_SIZE = 32.0f;

  private final ImmediateModeRenderer2d primitives;
  private final WidgetTextureCache textureCache;

  WidgetInventoryGridRenderer(ImmediateModeRenderer2d primitives, WidgetTextureCache textureCache) {
    this.primitives = primitives;
    this.textureCache = textureCache;
  }

  void drawInventoryGrid(
      InterfaceComponentDefinition component,
      float left,
      float top,
      SidebarWidgetRenderer.WidgetRenderContext context
  ) {
    InterfaceComponentDefinition.InventoryGrid inventoryGrid = component.inventoryGrid();
    int columnCount = Math.max(0, component.width());
    int rowCount = Math.max(0, component.height());
    if (columnCount == 0 || rowCount == 0) {
      return;
    }
    int slotCount = columnCount * rowCount;
    InterfaceComponentDefinition.SlotDecoration[] slotDecorations = inventoryGrid.slotDecorations();
    for (int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
      float slotLeft = left + slotColumn(slotIndex, columnCount) * slotStepX(inventoryGrid);
      float slotTop = top + slotRow(slotIndex, columnCount) * slotStepY(inventoryGrid);
      drawSlotDecoration(slotDecorations, slotIndex, slotLeft, slotTop);
      SidebarWidgetRenderer.WidgetGridItem item = context.gridResolver().itemAt(component.id(), slotIndex);
      if (item == null || item.itemId() < 0) {
        continue;
      }
      OpenGlTexture itemTexture = textureCache.itemTexture(item.itemId());
      if (itemTexture == null) {
        continue;
      }
      primitives.drawTexturedQuad(itemTexture, new ScreenRect(slotLeft, slotTop, SLOT_SIZE, SLOT_SIZE));
    }
  }

  private void drawSlotDecoration(
      InterfaceComponentDefinition.SlotDecoration[] slotDecorations,
      int slotIndex,
      float slotLeft,
      float slotTop
  ) {
    if (slotIndex < 0 || slotIndex >= slotDecorations.length) {
      return;
    }
    InterfaceComponentDefinition.SlotDecoration slotDecoration = slotDecorations[slotIndex];
    if (slotDecoration == null || slotDecoration.sprite() == null) {
      return;
    }
    OpenGlTexture slotTexture = textureCache.spriteTexture(slotDecoration.sprite());
    if (slotTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        slotTexture,
        new ScreenRect(
            slotLeft + slotDecoration.offsetX(),
            slotTop + slotDecoration.offsetY(),
            slotTexture.width(),
            slotTexture.height()
        )
    );
  }

  static float measuredWidth(InterfaceComponentDefinition component) {
    InterfaceComponentDefinition.InventoryGrid inventoryGrid = component.inventoryGrid();
    int columnCount = Math.max(0, component.width());
    if (columnCount == 0) {
      return 0.0f;
    }
    return columnCount * SLOT_SIZE + Math.max(0, columnCount - 1) * inventoryGrid.paddingX();
  }

  static float measuredHeight(InterfaceComponentDefinition component) {
    InterfaceComponentDefinition.InventoryGrid inventoryGrid = component.inventoryGrid();
    int rowCount = Math.max(0, component.height());
    if (rowCount == 0) {
      return 0.0f;
    }
    return rowCount * SLOT_SIZE + Math.max(0, rowCount - 1) * inventoryGrid.paddingY();
  }

  private static int slotColumn(int slotIndex, int columnCount) {
    return slotIndex % columnCount;
  }

  private static int slotRow(int slotIndex, int columnCount) {
    return slotIndex / columnCount;
  }

  private static float slotStepX(InterfaceComponentDefinition.InventoryGrid inventoryGrid) {
    return SLOT_SIZE + inventoryGrid.paddingX();
  }

  private static float slotStepY(InterfaceComponentDefinition.InventoryGrid inventoryGrid) {
    return SLOT_SIZE + inventoryGrid.paddingY();
  }
}
