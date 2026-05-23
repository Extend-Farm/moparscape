package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class MusicSidebarPanelRenderer {

  static final int MUSIC_INTERFACE_ID = 962;

  private final GameplaySidebarRenderer owner;

  MusicSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawMusicSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(MUSIC_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, MUSIC_INTERFACE_ID, viewModel, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Music", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Music interface 962 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScrollbarClick(MUSIC_INTERFACE_ID, sidebarRect, x, y)
        || owner.sidebarWidgetRenderer().hasActionAt(MUSIC_INTERFACE_ID, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScroll(MUSIC_INTERFACE_ID, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(MUSIC_INTERFACE_ID, sidebarRect, x, y);
  }
}
