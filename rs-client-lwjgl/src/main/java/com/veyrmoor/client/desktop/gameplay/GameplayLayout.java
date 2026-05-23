package com.veyrmoor.client.desktop.gameplay;

import com.veyrmoor.client.desktop.render.common.ScreenRect;

public final class GameplayLayout {

  private static final float WORLD_VIEW_LEFT = 4.0f;
  private static final float WORLD_VIEW_TOP = 4.0f;
  private static final float WORLD_VIEW_WIDTH = 512.0f;
  private static final float WORLD_VIEW_HEIGHT = 334.0f;
  private static final float MINIMAP_PANEL_LEFT = 550.0f;
  private static final float MINIMAP_PANEL_TOP = 4.0f;
  private static final float MINIMAP_PANEL_WIDTH = 172.0f;
  private static final float MINIMAP_PANEL_HEIGHT = 156.0f;
  private static final float COMPASS_LEFT = 553.0f;
  private static final float COMPASS_TOP = 6.0f;
  private static final float COMPASS_WIDTH = 33.0f;
  private static final float COMPASS_HEIGHT = 33.0f;
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
  private static final float FRAME_WIDTH = 765.0f;
  private static final float FRAME_HEIGHT = 503.0f;

  private GameplayLayout() {
  }

  public static ScreenRect worldViewportRect() {
    return new ScreenRect(WORLD_VIEW_LEFT, WORLD_VIEW_TOP, WORLD_VIEW_WIDTH, WORLD_VIEW_HEIGHT);
  }

  public static ScreenRect frameRect() {
    return new ScreenRect(0.0f, 0.0f, FRAME_WIDTH, FRAME_HEIGHT);
  }

  public static ScreenRect worldViewportInnerRect() {
    // The fixed 317 frame already exposes the exact 512x334 world surface at 4,4.
    // Applying an extra native inset shrinks the scene and leaves visible black gutters.
    return worldViewportRect();
  }

  static ScreenRect minimapPanelRect() {
    return new ScreenRect(MINIMAP_PANEL_LEFT, MINIMAP_PANEL_TOP, MINIMAP_PANEL_WIDTH, MINIMAP_PANEL_HEIGHT);
  }

  static ScreenRect minimapContentRect() {
    return new ScreenRect(MINIMAP_PANEL_LEFT + 25.0f, MINIMAP_PANEL_TOP + 5.0f, 146.0f, 151.0f);
  }

  static ScreenRect compassRect() {
    return new ScreenRect(COMPASS_LEFT, COMPASS_TOP, COMPASS_WIDTH, COMPASS_HEIGHT);
  }

  public static ScreenRect sidebarPanelRect() {
    return new ScreenRect(SIDEBAR_PANEL_LEFT, SIDEBAR_PANEL_TOP, SIDEBAR_PANEL_WIDTH, SIDEBAR_PANEL_HEIGHT);
  }

  public static ScreenRect chatboxPanelRect() {
    return new ScreenRect(CHATBOX_PANEL_LEFT, CHATBOX_PANEL_TOP, CHATBOX_PANEL_WIDTH, CHATBOX_PANEL_HEIGHT);
  }

  public static ScreenRect chatHistoryRect() {
    ScreenRect chatboxRect = chatboxPanelRect();
    return new ScreenRect(chatboxRect.left() + 4.0f, chatboxRect.top(), 459.0f, 77.0f);
  }

  public static ScreenRect chatInputRect() {
    ScreenRect chatboxRect = chatboxPanelRect();
    return new ScreenRect(chatboxRect.left(), chatboxRect.top() + 78.0f, chatboxRect.width(), 18.0f);
  }

  public static float chatPromptBaselineY() {
    return chatboxPanelRect().top() + 90.0f;
  }

  static ScreenRect topTabRect() {
    return new ScreenRect(TOP_TAB_LEFT, TOP_TAB_TOP, TOP_TAB_WIDTH, TOP_TAB_HEIGHT);
  }

  static ScreenRect bottomTabRect() {
    return new ScreenRect(BOTTOM_TAB_LEFT, BOTTOM_TAB_TOP, BOTTOM_TAB_WIDTH, BOTTOM_TAB_HEIGHT);
  }

  static ScreenRect gameplayTabRect(GameplayTab gameplayTab) {
    return switch (gameplayTab.index()) {
      case 0 -> rectFromBounds(539.0f, 169.0f, 573.0f, 205.0f);
      case 1 -> rectFromBounds(569.0f, 168.0f, 599.0f, 205.0f);
      case 2 -> rectFromBounds(597.0f, 168.0f, 627.0f, 205.0f);
      case 3 -> rectFromBounds(625.0f, 168.0f, 669.0f, 203.0f);
      case 4 -> rectFromBounds(666.0f, 168.0f, 696.0f, 205.0f);
      case 5 -> rectFromBounds(694.0f, 168.0f, 724.0f, 205.0f);
      case 6 -> rectFromBounds(722.0f, 169.0f, 756.0f, 205.0f);
      case 7 -> rectFromBounds(540.0f, 466.0f, 574.0f, 502.0f);
      case 8 -> rectFromBounds(572.0f, 466.0f, 602.0f, 503.0f);
      case 9 -> rectFromBounds(599.0f, 466.0f, 629.0f, 503.0f);
      case 10 -> rectFromBounds(627.0f, 467.0f, 671.0f, 502.0f);
      case 11 -> rectFromBounds(669.0f, 466.0f, 699.0f, 503.0f);
      case 12 -> rectFromBounds(696.0f, 466.0f, 726.0f, 503.0f);
      case 13 -> rectFromBounds(724.0f, 466.0f, 758.0f, 502.0f);
      default -> rectFromBounds(625.0f, 168.0f, 669.0f, 203.0f);
    };
  }

  static ScreenRect gameplayTabHighlightRect(GameplayTab gameplayTab, float width, float height) {
    return switch (gameplayTab.index()) {
      case 0 -> new ScreenRect(TOP_TAB_LEFT + 22.0f, TOP_TAB_TOP + 10.0f, width, height);
      case 1 -> new ScreenRect(TOP_TAB_LEFT + 54.0f, TOP_TAB_TOP + 8.0f, width, height);
      case 2 -> new ScreenRect(TOP_TAB_LEFT + 82.0f, TOP_TAB_TOP + 8.0f, width, height);
      case 3 -> new ScreenRect(TOP_TAB_LEFT + 110.0f, TOP_TAB_TOP + 8.0f, width, height);
      case 4 -> new ScreenRect(TOP_TAB_LEFT + 153.0f, TOP_TAB_TOP + 8.0f, width, height);
      case 5 -> new ScreenRect(TOP_TAB_LEFT + 181.0f, TOP_TAB_TOP + 8.0f, width, height);
      case 6 -> new ScreenRect(TOP_TAB_LEFT + 209.0f, TOP_TAB_TOP + 9.0f, width, height);
      case 7 -> new ScreenRect(BOTTOM_TAB_LEFT + 42.0f, BOTTOM_TAB_TOP, width, height);
      case 8 -> new ScreenRect(BOTTOM_TAB_LEFT + 74.0f, BOTTOM_TAB_TOP, width, height);
      case 9 -> new ScreenRect(BOTTOM_TAB_LEFT + 102.0f, BOTTOM_TAB_TOP, width, height);
      case 10 -> new ScreenRect(BOTTOM_TAB_LEFT + 130.0f, BOTTOM_TAB_TOP + 1.0f, width, height);
      case 11 -> new ScreenRect(BOTTOM_TAB_LEFT + 173.0f, BOTTOM_TAB_TOP, width, height);
      case 12 -> new ScreenRect(BOTTOM_TAB_LEFT + 201.0f, BOTTOM_TAB_TOP, width, height);
      case 13 -> new ScreenRect(BOTTOM_TAB_LEFT + 229.0f, BOTTOM_TAB_TOP, width, height);
      default -> new ScreenRect(TOP_TAB_LEFT + 110.0f, TOP_TAB_TOP + 8.0f, width, height);
    };
  }

  private static ScreenRect rectFromBounds(float left, float top, float right, float bottom) {
    return new ScreenRect(left, top, right - left, bottom - top);
  }
}
