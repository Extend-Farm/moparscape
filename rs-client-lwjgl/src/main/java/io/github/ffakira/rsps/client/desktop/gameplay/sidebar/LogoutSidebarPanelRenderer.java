package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayClickResult;

final class LogoutSidebarPanelRenderer {

  static final int LOGOUT_INTERFACE_ID = 2449;

  private final GameplaySidebarRenderer owner;

  LogoutSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawLogoutSidebar(ScreenRect sidebarRect) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(LOGOUT_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, LOGOUT_INTERFACE_ID);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Logout", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Logout interface 2449 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  GameplayClickResult handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return GameplayClickResult.ignored();
    }
    if (!owner.sidebarWidgetRenderer().hasActionAt(LOGOUT_INTERFACE_ID, sidebarRect, x, y)) {
      return GameplayClickResult.ignored();
    }
    return GameplayClickResult.logout();
  }
}
