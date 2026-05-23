package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class SettingsSidebarPanelRenderer {

  static final int SETTINGS_INTERFACE_ID = 4445;

  private final GameplaySidebarRenderer owner;

  SettingsSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawSettingsSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(SETTINGS_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, SETTINGS_INTERFACE_ID, viewModel, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Settings", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Settings interface 4445 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScrollbarClick(SETTINGS_INTERFACE_ID, sidebarRect, x, y)
        || owner.sidebarWidgetRenderer().hasActionAt(SETTINGS_INTERFACE_ID, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScroll(SETTINGS_INTERFACE_ID, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(SETTINGS_INTERFACE_ID, sidebarRect, x, y);
  }
}
