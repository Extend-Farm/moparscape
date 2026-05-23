package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class QuestSidebarPanelRenderer {

  static final int QUEST_INTERFACE_ID = 638;

  private final GameplaySidebarRenderer owner;

  QuestSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawQuestSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(QUEST_INTERFACE_ID)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, QUEST_INTERFACE_ID, viewModel, pointerX, pointerY);
      return;
    }
    float left = sidebarRect.left() + 12.0f;
    float top = sidebarRect.top() + 38.0f;
    owner.primitives().drawText(left, top, "Quests", 0.92f, 0.86f, 0.46f);
    owner.primitives().drawText(left, top + 18.0f, "Quest interface 638 is unavailable.", 0.84f, 0.88f, 0.94f);
  }

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScrollbarClick(QUEST_INTERFACE_ID, sidebarRect, x, y)
        || owner.sidebarWidgetRenderer().hasActionAt(QUEST_INTERFACE_ID, sidebarRect, x, y);
  }

  boolean handleSidebarScroll(ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handleScroll(QUEST_INTERFACE_ID, sidebarRect, x, y, yOffset);
  }

  boolean handleSidebarPointerMove(ScreenRect sidebarRect, double x, double y) {
    if (owner.sidebarWidgetRenderer() == null) {
      return false;
    }
    return owner.sidebarWidgetRenderer().handlePointerMove(QUEST_INTERFACE_ID, sidebarRect, x, y);
  }
}
