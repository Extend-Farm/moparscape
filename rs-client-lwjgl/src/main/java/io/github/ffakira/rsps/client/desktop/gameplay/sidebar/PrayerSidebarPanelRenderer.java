package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import io.github.ffakira.rsps.client.desktop.core.ScreenRect;

final class PrayerSidebarPanelRenderer {

  static final int PRAYER_INTERFACE_ID = 5608;

  private final GameplaySidebarRenderer owner;

  PrayerSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawPrayerSidebar(ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(PRAYER_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, PRAYER_INTERFACE_ID, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Prayer", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Prayer interface 5608 is unavailable.", 0.84f, 0.88f, 0.94f);
  }
}
