package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class WidgetTextRendererTest {

  @Test
  void stripsLegacyColorTagsFromWidgetText() {
    assertThat(WidgetTextRenderer.stripColorTags("Moderator option: @gre@<ON>"))
        .isEqualTo("Moderator option: <ON>");
    assertThat(WidgetTextRenderer.stripColorTags("@red@Offline"))
        .isEqualTo("Offline");
  }

  @Test
  void measuresTaggedWidgetTextByVisibleGlyphsInsteadOfTagBytes() {
    TitleScreenBitmapFont font = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of(".")).fonts().plain();

    int measuredWidth = WidgetTextRenderer.measureTaggedText(font, "Moderator option: @gre@<ON>");

    assertThat(measuredWidth).isEqualTo(font.measureText("Moderator option: <ON>"));
  }
}
