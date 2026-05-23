package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentDefinition;
import java.util.HashMap;
import java.util.Map;

final class SidebarWidgetScrollState {

  static final int SCROLLBAR_ARROW_SCROLL_STEP = 4;
  static final int SCROLLBAR_WHEEL_SCROLL_STEP = 30;
  static final int SCROLLBAR_WIDTH = 16;
  static final int SCROLLBAR_ARROW_HEIGHT = 16;

  private final Map<Integer, Integer> scrollPositions = new HashMap<>();
  private ScrollbarDragState scrollbarDragState;

  boolean handleScrollbarClick(
      InterfaceComponentDefinition root,
      float left,
      float top,
      double x,
      double y,
      SidebarWidgetLocator locator
  ) {
    SidebarWidgetLocator.ScrollTarget scrollTarget = locator.findScrollTarget(root, left, top, null, x, y);
    if (scrollTarget == null || !scrollTarget.overScrollbar()) {
      return false;
    }
    InterfaceComponentDefinition container = scrollTarget.container();
    int viewportHeight = container.height();
    int scrollContentHeight = container.container().scrollContentHeight();
    int currentScrollPosition = scrollPosition(container);
    ScreenRect scrollbarRect = scrollbarRect(container, scrollTarget.left(), scrollTarget.top());
    if (topArrowRect(scrollbarRect).contains(x, y)) {
      scrollbarDragState = null;
      setScrollPosition(container, currentScrollPosition - SCROLLBAR_ARROW_SCROLL_STEP);
      return true;
    }
    if (bottomArrowRect(scrollbarRect).contains(x, y)) {
      scrollbarDragState = null;
      setScrollPosition(container, currentScrollPosition + SCROLLBAR_ARROW_SCROLL_STEP);
      return true;
    }
    int thumbHeight = scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    ScreenRect thumbRect = scrollbarThumbRect(container, scrollTarget.left(), scrollTarget.top(), currentScrollPosition);
    if (thumbRect.contains(x, y)) {
      scrollbarDragState = new ScrollbarDragState(container.id(), y - thumbRect.top());
      return true;
    }
    scrollbarDragState = null;
    int nextScrollPosition = trackScrollPosition(y, scrollbarRect.top(), viewportHeight, scrollContentHeight, thumbHeight);
    setScrollPosition(container, nextScrollPosition);
    return true;
  }

  boolean handleScroll(
      InterfaceComponentDefinition root,
      float left,
      float top,
      double x,
      double y,
      double yOffset,
      SidebarWidgetLocator locator
  ) {
    SidebarWidgetLocator.ScrollTarget scrollTarget = locator.findScrollTarget(root, left, top, null, x, y);
    if (scrollTarget == null) {
      return false;
    }
    int direction = yOffset > 0.0d ? -1 : 1;
    setScrollPosition(
        scrollTarget.container(),
        scrollPosition(scrollTarget.container()) + direction * SCROLLBAR_WHEEL_SCROLL_STEP
    );
    return true;
  }

  boolean handlePointerMove(
      InterfaceComponentDefinition root,
      float left,
      float top,
      double x,
      double y,
      SidebarWidgetLocator locator
  ) {
    if (scrollbarDragState == null) {
      return false;
    }
    SidebarWidgetLocator.ScrollTarget scrollTarget =
        locator.findScrollTargetByContainerId(root, left, top, null, scrollbarDragState.containerId());
    if (scrollTarget == null) {
      scrollbarDragState = null;
      return false;
    }
    InterfaceComponentDefinition container = scrollTarget.container();
    int viewportHeight = container.height();
    int scrollContentHeight = container.container().scrollContentHeight();
    int thumbHeight = scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int nextScrollPosition = dragScrollPosition(
        y,
        scrollTarget.top(),
        viewportHeight,
        scrollContentHeight,
        thumbHeight,
        scrollbarDragState.thumbGrabOffsetY()
    );
    setScrollPosition(container, nextScrollPosition);
    return true;
  }

  void endPointerDrag() {
    scrollbarDragState = null;
  }

  void clearTransientState() {
    scrollbarDragState = null;
  }

  int scrollPosition(InterfaceComponentDefinition container) {
    return clampScrollPosition(
        scrollPositions.getOrDefault(container.id(), 0),
        container.height(),
        container.container().scrollContentHeight()
    );
  }

  static int scrollbarThumbHeight(int viewportHeight, int scrollContentHeight) {
    int thumbHeight = ((viewportHeight - 32) * viewportHeight) / scrollContentHeight;
    return Math.max(8, thumbHeight);
  }

  static int scrollbarThumbOffset(int scrollPosition, int viewportHeight, int scrollContentHeight, int thumbHeight) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    return ((viewportHeight - 32 - thumbHeight) * scrollPosition) / (scrollContentHeight - viewportHeight);
  }

  static int trackScrollPosition(
      double mouseY,
      float scrollbarTop,
      int viewportHeight,
      int scrollContentHeight,
      int thumbHeight
  ) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    int trackHeight = viewportHeight - SCROLLBAR_ARROW_HEIGHT * 2 - thumbHeight;
    if (trackHeight <= 0) {
      return 0;
    }
    double trackMouseY = mouseY - scrollbarTop - SCROLLBAR_ARROW_HEIGHT - thumbHeight * 0.5d;
    int scrollPosition = (int) (((scrollContentHeight - viewportHeight) * trackMouseY) / trackHeight);
    return clampScrollPosition(scrollPosition, viewportHeight, scrollContentHeight);
  }

  static int dragScrollPosition(
      double mouseY,
      float containerTop,
      int viewportHeight,
      int scrollContentHeight,
      int thumbHeight,
      double thumbGrabOffsetY
  ) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    int trackHeight = viewportHeight - SCROLLBAR_ARROW_HEIGHT * 2 - thumbHeight;
    if (trackHeight <= 0) {
      return 0;
    }
    double thumbTop = mouseY - thumbGrabOffsetY;
    double trackMouseY = thumbTop - containerTop - SCROLLBAR_ARROW_HEIGHT;
    int scrollPosition = (int) (((scrollContentHeight - viewportHeight) * trackMouseY) / trackHeight);
    return clampScrollPosition(scrollPosition, viewportHeight, scrollContentHeight);
  }

  static ScreenRect scrollbarRect(InterfaceComponentDefinition component, float left, float top) {
    return new ScreenRect(left + component.width(), top, SCROLLBAR_WIDTH, component.height());
  }

  static ScreenRect topArrowRect(ScreenRect scrollbarRect) {
    return new ScreenRect(scrollbarRect.left(), scrollbarRect.top(), SCROLLBAR_WIDTH, SCROLLBAR_ARROW_HEIGHT);
  }

  static ScreenRect bottomArrowRect(ScreenRect scrollbarRect) {
    return new ScreenRect(
        scrollbarRect.left(),
        scrollbarRect.top() + scrollbarRect.height() - SCROLLBAR_ARROW_HEIGHT,
        SCROLLBAR_WIDTH,
        SCROLLBAR_ARROW_HEIGHT
    );
  }

  static ScreenRect scrollbarThumbRect(
      InterfaceComponentDefinition component,
      float left,
      float top,
      int scrollPosition
  ) {
    ScreenRect scrollbarRect = scrollbarRect(component, left, top);
    int thumbHeight = scrollbarThumbHeight(component.height(), component.container().scrollContentHeight());
    int thumbOffset = scrollbarThumbOffset(
        scrollPosition,
        component.height(),
        component.container().scrollContentHeight(),
        thumbHeight
    );
    return new ScreenRect(
        scrollbarRect.left(),
        scrollbarRect.top() + SCROLLBAR_ARROW_HEIGHT + thumbOffset,
        SCROLLBAR_WIDTH,
        thumbHeight
    );
  }

  private void setScrollPosition(InterfaceComponentDefinition container, int scrollPosition) {
    int clampedScrollPosition = clampScrollPosition(
        scrollPosition,
        container.height(),
        container.container().scrollContentHeight()
    );
    if (clampedScrollPosition == 0) {
      scrollPositions.remove(container.id());
      return;
    }
    scrollPositions.put(container.id(), clampedScrollPosition);
  }

  private static int clampScrollPosition(int scrollPosition, int viewportHeight, int scrollContentHeight) {
    int maxScrollPosition = Math.max(0, scrollContentHeight - viewportHeight);
    return Math.max(0, Math.min(maxScrollPosition, scrollPosition));
  }

  private record ScrollbarDragState(int containerId, double thumbGrabOffsetY) {
  }
}
