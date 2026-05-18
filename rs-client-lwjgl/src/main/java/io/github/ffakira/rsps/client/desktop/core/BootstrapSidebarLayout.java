package io.github.ffakira.rsps.client.desktop.core;

public record BootstrapSidebarLayout(
    ScreenRect playArea,
    ScreenRect sidebar,
    ScreenRect sidebarHeader,
    ScreenRect sidebarContent
) {

  private static final float MIN_MARGIN = 12.0f;
  private static final float MAX_MARGIN = 20.0f;
  private static final float GUTTER = 14.0f;
  private static final float MIN_SIDEBAR_WIDTH = 180.0f;
  private static final float MAX_SIDEBAR_WIDTH = 230.0f;
  private static final float MIN_PLAY_AREA_WIDTH = 280.0f;
  private static final float HEADER_HEIGHT = 82.0f;
  private static final float INNER_PADDING = 12.0f;

  public static BootstrapSidebarLayout forViewport(int width, int height, boolean showSidebar) {
    float viewportWidth = Math.max(1.0f, width);
    float viewportHeight = Math.max(1.0f, height);
    float margin = clamp(viewportWidth * 0.025f, MIN_MARGIN, MAX_MARGIN);
    float sidebarWidth = 0.0f;
    if (showSidebar) {
      sidebarWidth = clamp(viewportWidth * 0.31f, MIN_SIDEBAR_WIDTH, MAX_SIDEBAR_WIDTH);
      float maxSidebarWidth = Math.max(MIN_SIDEBAR_WIDTH, viewportWidth - margin * 2.0f - GUTTER - MIN_PLAY_AREA_WIDTH);
      sidebarWidth = Math.min(sidebarWidth, maxSidebarWidth);
    }

    float playAreaWidth = viewportWidth - margin * 2.0f - (showSidebar ? sidebarWidth + GUTTER : 0.0f);
    float playAreaHeight = viewportHeight - margin * 2.0f;
    ScreenRect playArea = new ScreenRect(margin, margin, Math.max(1.0f, playAreaWidth), Math.max(1.0f, playAreaHeight));

    if (!showSidebar) {
      return new BootstrapSidebarLayout(
          playArea,
          new ScreenRect(viewportWidth - margin, margin, 0.0f, playAreaHeight),
          new ScreenRect(viewportWidth - margin, margin, 0.0f, 0.0f),
          new ScreenRect(viewportWidth - margin, margin, 0.0f, 0.0f)
      );
    }

    float sidebarLeft = playArea.left() + playArea.width() + GUTTER;
    ScreenRect sidebar = new ScreenRect(sidebarLeft, margin, sidebarWidth, playAreaHeight);
    ScreenRect sidebarHeader = new ScreenRect(
        sidebar.left() + INNER_PADDING,
        sidebar.top() + INNER_PADDING,
        sidebar.width() - INNER_PADDING * 2.0f,
        HEADER_HEIGHT
    );
    ScreenRect sidebarContent = new ScreenRect(
        sidebarHeader.left(),
        sidebarHeader.top() + sidebarHeader.height() + INNER_PADDING,
        sidebarHeader.width(),
        Math.max(1.0f, sidebar.height() - HEADER_HEIGHT - INNER_PADDING * 3.0f)
    );
    return new BootstrapSidebarLayout(playArea, sidebar, sidebarHeader, sidebarContent);
  }

  private static float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
