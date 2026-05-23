package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;

final class LogoutSidebarPanelRenderer {

  static final int LOGOUT_INTERFACE_ID = 2449;
  static final int LOGOUT_BRANDING_COMPONENT_ID = 2451;
  private static final String LOGOUT_BRANDING_TEXT = "Veyrmoor, always use the";

  private final GameplaySidebarRenderer owner;

  LogoutSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawLogoutSidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(LOGOUT_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(
          sidebarRect,
          LOGOUT_INTERFACE_ID,
          viewModel,
          LogoutSidebarPanelRenderer::logoutWidgetOverride,
          SidebarWidgetRenderer.WidgetInventoryGridResolver.NONE
      );
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

  static SidebarWidgetRenderer.WidgetOverride logoutWidgetOverride(int componentId) {
    if (componentId != LOGOUT_BRANDING_COMPONENT_ID) {
      return null;
    }
    return new SidebarWidgetRenderer.WidgetOverride(LOGOUT_BRANDING_TEXT, null);
  }
}
