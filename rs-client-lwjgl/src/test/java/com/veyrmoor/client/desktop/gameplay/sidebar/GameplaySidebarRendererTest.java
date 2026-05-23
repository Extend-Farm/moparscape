package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GameplaySidebarRendererTest {

  @Test
  void constrainsPromptTextByVisibleWidthAndKeepsTheTypingCursorVisible() {
    TitleScreenBitmapFont font = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of(".")).fonts().plain();
    float maxWidth = font.measureText("aaaaaaaaaaaa");

    String constrained = GameplaySidebarRenderer.fitChatPromptText(font, "aaaaaaaaaaaaaaaaaaaa*", maxWidth);

    assertThat(font.measureText(constrained)).isLessThanOrEqualTo(Math.round(maxWidth));
    assertThat(constrained).startsWith("...");
    assertThat(constrained).endsWith("*");
  }

  @Test
  void constrainsHistoryTextByVisibleWidthFromTheStartOfTheLine() {
    TitleScreenBitmapFont font = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of(".")).fonts().plain();
    float maxWidth = font.measureText("Welcome to");

    String constrained = GameplaySidebarRenderer.fitChatHistoryText(font, "Welcome to Veyrmoor and beyond", maxWidth);

    assertThat(font.measureText(constrained)).isLessThanOrEqualTo(Math.round(maxWidth));
    assertThat(constrained).endsWith("...");
  }
}
