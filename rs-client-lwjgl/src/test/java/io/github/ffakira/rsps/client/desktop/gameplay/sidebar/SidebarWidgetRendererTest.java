package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.login.TitleScreenAssetLoader;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenBitmapFont;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SidebarWidgetRendererTest {

  @Test
  void scrollbarThumbMathMatchesTheReferenceSpellbookContainer() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;

    int thumbHeight = SidebarWidgetRenderer.scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int thumbOffset = SidebarWidgetRenderer.scrollbarThumbOffset(0, viewportHeight, scrollContentHeight, thumbHeight);

    assertThat(thumbHeight).isEqualTo(104);
    assertThat(thumbOffset).isZero();
  }

  @Test
  void scrollbarTrackClicksMapToTheReferenceScrollRange() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;
    int thumbHeight = SidebarWidgetRenderer.scrollbarThumbHeight(viewportHeight, scrollContentHeight);

    int bottomScrollPosition =
        SidebarWidgetRenderer.trackScrollPosition(157.0, 0.0f, viewportHeight, scrollContentHeight, thumbHeight);

    assertThat(bottomScrollPosition).isEqualTo(48);
  }

  @Test
  void scrollbarThumbDraggingPreservesTheGrabOffsetInPixels() {
    int viewportHeight = 167;
    int scrollContentHeight = 215;
    int thumbHeight = SidebarWidgetRenderer.scrollbarThumbHeight(viewportHeight, scrollContentHeight);

    int scrollPosition =
        SidebarWidgetRenderer.dragScrollPosition(44.0, 0.0f, viewportHeight, scrollContentHeight, thumbHeight, 12.0);

    assertThat(scrollPosition).isEqualTo(24);
  }

  @Test
  void textTextureLayoutKeepsFullLogoutCopyInkBounds() {
    TitleScreenBitmapFont font = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of(".")).fonts().plain();

    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(font, "When you have finished playing", true);

    assertThat(layout.width()).isEqualTo(180);
    assertThat(layout.height()).isEqualTo(17);
    assertThat(layout.baselineY()).isEqualTo(font.maxGlyphHeight());
    assertThat(layout.canvasTop()).isZero();
    assertThat(layout.height()).isGreaterThan(font.maxGlyphHeight() + 1);
  }
}
