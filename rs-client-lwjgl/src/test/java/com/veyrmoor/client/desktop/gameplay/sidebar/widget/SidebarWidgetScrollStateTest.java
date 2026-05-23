package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class SidebarWidgetScrollStateTest {

  @Test
  void scrollbarThumbMathMatchesTheReferenceSpellbookContainer() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;

    int thumbHeight = SidebarWidgetScrollState.scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int thumbOffset = SidebarWidgetScrollState.scrollbarThumbOffset(0, viewportHeight, scrollContentHeight, thumbHeight);

    assertThat(thumbHeight).isEqualTo(104);
    assertThat(thumbOffset).isZero();
  }

  @Test
  void scrollbarTrackClicksMapToTheReferenceScrollRange() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;
    int thumbHeight = SidebarWidgetScrollState.scrollbarThumbHeight(viewportHeight, scrollContentHeight);

    int bottomScrollPosition =
        SidebarWidgetScrollState.trackScrollPosition(157.0, 0.0f, viewportHeight, scrollContentHeight, thumbHeight);

    assertThat(bottomScrollPosition).isEqualTo(48);
  }

  @Test
  void scrollbarThumbDraggingPreservesTheGrabOffsetInPixels() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;
    int thumbHeight = SidebarWidgetScrollState.scrollbarThumbHeight(viewportHeight, scrollContentHeight);

    int scrollPosition =
        SidebarWidgetScrollState.dragScrollPosition(44.0, 0.0f, viewportHeight, scrollContentHeight, thumbHeight, 12.0);

    assertThat(scrollPosition).isEqualTo(24);
  }
}
