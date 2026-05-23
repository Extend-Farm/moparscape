package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class SidebarWidgetRendererTest {

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
