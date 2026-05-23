package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class FriendsSidebarPanelRenderer {

  static final int FRIENDS_INTERFACE_ID = 5065;

  private final GameplaySidebarRenderer owner;

  FriendsSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawFriendsSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(FRIENDS_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, FRIENDS_INTERFACE_ID, viewModel, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Friends", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Friends interface 5065 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScrollbarClick(FRIENDS_INTERFACE_ID, sidebarRect, x, y)
        || owner.sidebarWidgetRenderer().hasActionAt(FRIENDS_INTERFACE_ID, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScroll(FRIENDS_INTERFACE_ID, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(FRIENDS_INTERFACE_ID, sidebarRect, x, y);
  }
}
