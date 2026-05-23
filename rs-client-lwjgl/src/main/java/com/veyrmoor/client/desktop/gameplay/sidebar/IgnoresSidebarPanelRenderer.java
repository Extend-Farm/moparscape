package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class IgnoresSidebarPanelRenderer {

  static final int IGNORES_INTERFACE_ID = 5715;

  private final GameplaySidebarRenderer owner;

  IgnoresSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawIgnoresSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(IGNORES_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, IGNORES_INTERFACE_ID, viewModel, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Ignores", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Ignores interface 5715 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScrollbarClick(IGNORES_INTERFACE_ID, sidebarRect, x, y)
        || owner.sidebarWidgetRenderer().hasActionAt(IGNORES_INTERFACE_ID, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScroll(IGNORES_INTERFACE_ID, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(IGNORES_INTERFACE_ID, sidebarRect, x, y);
  }
}
