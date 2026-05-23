package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.GameplayTab;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class SidebarPanelCoordinator {

  private static final float SIDEBAR_CONTENT_LEFT_INSET = 12.0f;
  private static final float SIDEBAR_CONTENT_TOP_INSET = 22.0f;

  private final GameplaySidebarRenderer owner;
  private final InventoryEquipmentSidebarPanelRenderer inventoryEquipmentPanelRenderer;
  private final StatsSidebarPanelRenderer statsPanelRenderer;
  private final CombatSidebarPanelRenderer combatPanelRenderer;
  private final PrayerSidebarPanelRenderer prayerPanelRenderer;
  private final MagicSidebarPanelRenderer magicPanelRenderer;
  private final QuestSidebarPanelRenderer questPanelRenderer;
  private final FriendsSidebarPanelRenderer friendsPanelRenderer;
  private final IgnoresSidebarPanelRenderer ignoresPanelRenderer;
  private final MusicSidebarPanelRenderer musicPanelRenderer;
  private final SettingsSidebarPanelRenderer settingsPanelRenderer;
  private final EmotesSidebarPanelRenderer emotesPanelRenderer;
  private final LogoutSidebarPanelRenderer logoutPanelRenderer;

  SidebarPanelCoordinator(GameplaySidebarRenderer owner) {
    this.owner = owner;
    this.inventoryEquipmentPanelRenderer = new InventoryEquipmentSidebarPanelRenderer(owner);
    this.statsPanelRenderer = new StatsSidebarPanelRenderer(owner);
    this.combatPanelRenderer = new CombatSidebarPanelRenderer(owner);
    this.prayerPanelRenderer = new PrayerSidebarPanelRenderer(owner);
    this.magicPanelRenderer = new MagicSidebarPanelRenderer(owner);
    this.questPanelRenderer = new QuestSidebarPanelRenderer(owner);
    this.friendsPanelRenderer = new FriendsSidebarPanelRenderer(owner);
    this.ignoresPanelRenderer = new IgnoresSidebarPanelRenderer(owner);
    this.musicPanelRenderer = new MusicSidebarPanelRenderer(owner);
    this.settingsPanelRenderer = new SettingsSidebarPanelRenderer(owner);
    this.emotesPanelRenderer = new EmotesSidebarPanelRenderer(owner);
    this.logoutPanelRenderer = new LogoutSidebarPanelRenderer(owner);
  }

  GameplayClickResult handleSidebarClick(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case STATS -> handled(statsPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case QUESTS -> handled(questPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case MAGIC -> handled(magicPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case FRIENDS -> handled(friendsPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case IGNORES -> handled(ignoresPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case SETTINGS -> handled(settingsPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case EMOTES -> handled(emotesPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case MUSIC -> handled(musicPanelRenderer.handleSidebarClick(sidebarRect, x, y));
      case LOGOUT -> logoutPanelRenderer.handleSidebarClick(sidebarRect, x, y);
      default -> GameplayClickResult.ignored();
    };
  }

  boolean handleSidebarScroll(GameplayTab activeGameplayTab, double x, double y, double yOffset) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case QUESTS -> questPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case MAGIC -> magicPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case FRIENDS -> friendsPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case IGNORES -> ignoresPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case SETTINGS -> settingsPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case EMOTES -> emotesPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case MUSIC -> musicPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      default -> false;
    };
  }

  boolean handleSidebarPointerMove(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case QUESTS -> questPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case MAGIC -> magicPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case FRIENDS -> friendsPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case IGNORES -> ignoresPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case SETTINGS -> settingsPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case EMOTES -> emotesPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case MUSIC -> musicPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      default -> false;
    };
  }

  void endSidebarPointerDrag() {
    if (owner.sidebarWidgetRenderer() != null) {
      owner.sidebarWidgetRenderer().endPointerDrag();
    }
  }

  void clearTransientState() {
    if (owner.sidebarWidgetRenderer() != null) {
      owner.sidebarWidgetRenderer().clearTransientState();
    }
  }

  void drawSidebar(ClientViewModel viewModel, GameplayTab activeGameplayTab, double pointerX, double pointerY) {
    ScreenRect rect = GameplayLayout.sidebarPanelRect();
    drawSidebarHeading(activeGameplayTab, rect);
    switch (activeGameplayTab) {
      case INVENTORY -> inventoryEquipmentPanelRenderer.drawInventorySidebar(viewModel, rect);
      case EQUIPMENT -> inventoryEquipmentPanelRenderer.drawEquipmentSidebar(viewModel, rect);
      case STATS -> statsPanelRenderer.drawStatsSidebar(viewModel, rect, pointerX, pointerY);
      case COMBAT -> combatPanelRenderer.drawCombatSidebar(viewModel, rect);
      case QUESTS -> questPanelRenderer.drawQuestSidebar(viewModel, rect, pointerX, pointerY);
      case PRAYER -> prayerPanelRenderer.drawPrayerSidebar(viewModel, rect, pointerX, pointerY);
      case MAGIC -> magicPanelRenderer.drawMagicSidebar(viewModel, rect);
      case FRIENDS -> friendsPanelRenderer.drawFriendsSidebar(viewModel, rect, pointerX, pointerY);
      case IGNORES -> ignoresPanelRenderer.drawIgnoresSidebar(viewModel, rect, pointerX, pointerY);
      case LOGOUT -> logoutPanelRenderer.drawLogoutSidebar(viewModel, rect);
      case SETTINGS -> settingsPanelRenderer.drawSettingsSidebar(viewModel, rect, pointerX, pointerY);
      case EMOTES -> emotesPanelRenderer.drawEmotesSidebar(viewModel, rect, pointerX, pointerY);
      case MUSIC -> musicPanelRenderer.drawMusicSidebar(viewModel, rect, pointerX, pointerY);
    }
  }

  private void drawSidebarHeading(GameplayTab activeGameplayTab, ScreenRect sidebarRect) {
    if (!showsSidebarHeading(activeGameplayTab)) {
      return;
    }
    float left = sidebarRect.left() + SIDEBAR_CONTENT_LEFT_INSET;
    float top = sidebarRect.top() + SIDEBAR_CONTENT_TOP_INSET;
    owner.primitives().drawText(left, top, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
  }

  private static boolean showsSidebarHeading(GameplayTab activeGameplayTab) {
    return activeGameplayTab != GameplayTab.INVENTORY
        && activeGameplayTab != GameplayTab.STATS
        && activeGameplayTab != GameplayTab.COMBAT
        && activeGameplayTab != GameplayTab.EQUIPMENT
        && activeGameplayTab != GameplayTab.QUESTS
        && activeGameplayTab != GameplayTab.PRAYER
        && activeGameplayTab != GameplayTab.MAGIC
        && activeGameplayTab != GameplayTab.FRIENDS
        && activeGameplayTab != GameplayTab.IGNORES
        && activeGameplayTab != GameplayTab.MUSIC
        && activeGameplayTab != GameplayTab.SETTINGS
        && activeGameplayTab != GameplayTab.EMOTES
        && activeGameplayTab != GameplayTab.LOGOUT;
  }

  private static GameplayClickResult handled(boolean handled) {
    return handled ? GameplayClickResult.handledClick() : GameplayClickResult.ignored();
  }
}
