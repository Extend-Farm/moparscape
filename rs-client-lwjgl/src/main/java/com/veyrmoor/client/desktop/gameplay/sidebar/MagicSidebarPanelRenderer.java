package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class MagicSidebarPanelRenderer {

  static final int MODERN_MAGIC_INTERFACE_ID = 1151;
  static final int ANCIENT_MAGIC_INTERFACE_ID = 12855;

  private final GameplaySidebarRenderer owner;

  MagicSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawMagicSidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (owner.sidebarWidgetRenderer() != null) {
      int interfaceId = currentMagicInterfaceId();
      if (owner.sidebarWidgetRenderer().canRender(interfaceId)) {
        owner.sidebarWidgetRenderer().draw(sidebarRect, interfaceId, viewModel);
        return;
      }
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Magic", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Spellbook interface is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    int interfaceId = currentMagicInterfaceId();
    return owner.sidebarWidgetRenderer().handleScrollbarClick(interfaceId, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    int interfaceId = currentMagicInterfaceId();
    return owner.sidebarWidgetRenderer().handleScroll(interfaceId, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(currentMagicInterfaceId(), sidebarRect, x, y);
  }

  void endSidebarPointerDrag() {
    if (owner.sidebarWidgetRenderer() != null) {
      owner.sidebarWidgetRenderer().endPointerDrag();
    }
  }

  private int currentMagicInterfaceId() {
    return MODERN_MAGIC_INTERFACE_ID;
  }
}
