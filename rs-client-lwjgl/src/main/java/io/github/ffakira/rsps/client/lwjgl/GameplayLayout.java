package io.github.ffakira.rsps.client.lwjgl;

final class GameplayLayout {

  private static final float WORLD_VIEW_LEFT = 4.0f;
  private static final float WORLD_VIEW_TOP = 4.0f;
  private static final float WORLD_VIEW_WIDTH = 512.0f;
  private static final float WORLD_VIEW_HEIGHT = 334.0f;
  private static final float WORLD_PANEL_PADDING = 8.0f;
  private static final float MINIMAP_PANEL_LEFT = 550.0f;
  private static final float MINIMAP_PANEL_TOP = 4.0f;
  private static final float MINIMAP_PANEL_WIDTH = 172.0f;
  private static final float MINIMAP_PANEL_HEIGHT = 156.0f;
  private static final float SIDEBAR_PANEL_LEFT = 553.0f;
  private static final float SIDEBAR_PANEL_TOP = 205.0f;
  private static final float SIDEBAR_PANEL_WIDTH = 190.0f;
  private static final float SIDEBAR_PANEL_HEIGHT = 261.0f;
  private static final float CHATBOX_PANEL_LEFT = 17.0f;
  private static final float CHATBOX_PANEL_TOP = 357.0f;
  private static final float CHATBOX_PANEL_WIDTH = 479.0f;
  private static final float CHATBOX_PANEL_HEIGHT = 96.0f;
  private static final float TOP_TAB_LEFT = 516.0f;
  private static final float TOP_TAB_TOP = 160.0f;
  private static final float TOP_TAB_WIDTH = 249.0f;
  private static final float TOP_TAB_HEIGHT = 45.0f;
  private static final float BOTTOM_TAB_LEFT = 496.0f;
  private static final float BOTTOM_TAB_TOP = 466.0f;
  private static final float BOTTOM_TAB_WIDTH = 269.0f;
  private static final float BOTTOM_TAB_HEIGHT = 37.0f;

  private GameplayLayout() {
  }

  static ScreenRect worldViewportRect() {
    return new ScreenRect(WORLD_VIEW_LEFT, WORLD_VIEW_TOP, WORLD_VIEW_WIDTH, WORLD_VIEW_HEIGHT);
  }

  static ScreenRect worldViewportInnerRect() {
    return new ScreenRect(
        WORLD_VIEW_LEFT + WORLD_PANEL_PADDING,
        WORLD_VIEW_TOP + WORLD_PANEL_PADDING,
        WORLD_VIEW_WIDTH - WORLD_PANEL_PADDING * 2.0f,
        WORLD_VIEW_HEIGHT - WORLD_PANEL_PADDING * 2.0f
    );
  }

  static ScreenRect minimapPanelRect() {
    return new ScreenRect(MINIMAP_PANEL_LEFT, MINIMAP_PANEL_TOP, MINIMAP_PANEL_WIDTH, MINIMAP_PANEL_HEIGHT);
  }

  static ScreenRect minimapContentRect() {
    return new ScreenRect(MINIMAP_PANEL_LEFT + 25.0f, MINIMAP_PANEL_TOP + 5.0f, 146.0f, 151.0f);
  }

  static ScreenRect sidebarPanelRect() {
    return new ScreenRect(SIDEBAR_PANEL_LEFT, SIDEBAR_PANEL_TOP, SIDEBAR_PANEL_WIDTH, SIDEBAR_PANEL_HEIGHT);
  }

  static ScreenRect chatboxPanelRect() {
    return new ScreenRect(CHATBOX_PANEL_LEFT, CHATBOX_PANEL_TOP, CHATBOX_PANEL_WIDTH, CHATBOX_PANEL_HEIGHT);
  }

  static ScreenRect topTabRect() {
    return new ScreenRect(TOP_TAB_LEFT, TOP_TAB_TOP, TOP_TAB_WIDTH, TOP_TAB_HEIGHT);
  }

  static ScreenRect bottomTabRect() {
    return new ScreenRect(BOTTOM_TAB_LEFT, BOTTOM_TAB_TOP, BOTTOM_TAB_WIDTH, BOTTOM_TAB_HEIGHT);
  }

  static ScreenRect gameplayTabRect(GameplayTab gameplayTab) {
    return switch (gameplayTab.index()) {
      case 0 -> legacyRect(539.0f, 169.0f, 573.0f, 205.0f);
      case 1 -> legacyRect(569.0f, 168.0f, 599.0f, 205.0f);
      case 2 -> legacyRect(597.0f, 168.0f, 627.0f, 205.0f);
      case 3 -> legacyRect(625.0f, 168.0f, 669.0f, 203.0f);
      case 4 -> legacyRect(666.0f, 168.0f, 696.0f, 205.0f);
      case 5 -> legacyRect(694.0f, 168.0f, 724.0f, 205.0f);
      case 6 -> legacyRect(722.0f, 169.0f, 756.0f, 205.0f);
      case 7 -> legacyRect(540.0f, 466.0f, 574.0f, 502.0f);
      case 8 -> legacyRect(572.0f, 466.0f, 602.0f, 503.0f);
      case 9 -> legacyRect(599.0f, 466.0f, 629.0f, 503.0f);
      case 10 -> legacyRect(627.0f, 467.0f, 671.0f, 502.0f);
      case 11 -> legacyRect(669.0f, 466.0f, 699.0f, 503.0f);
      case 12 -> legacyRect(696.0f, 466.0f, 726.0f, 503.0f);
      case 13 -> legacyRect(724.0f, 466.0f, 758.0f, 502.0f);
      default -> legacyRect(625.0f, 168.0f, 669.0f, 203.0f);
    };
  }

  private static ScreenRect legacyRect(float left, float top, float right, float bottom) {
    return new ScreenRect(left, top, right - left, bottom - top);
  }
}
