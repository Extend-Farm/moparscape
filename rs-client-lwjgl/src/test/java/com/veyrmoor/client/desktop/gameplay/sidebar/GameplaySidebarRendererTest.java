package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
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

  @Test
  void centersReportAbuseModalInsideTheWorldViewportInsteadOfTheFullFrame() {
    ScreenRect modalRect = GameplaySidebarRenderer.centeredReportAbuseModalRect(
        GameplayLayout.worldViewportInnerRect(),
        488.0f,
        314.0f
    );

    assertThat(modalRect.left()).isEqualTo(16.0f);
    assertThat(modalRect.top()).isEqualTo(14.0f);
    assertThat(modalRect.width()).isEqualTo(488.0f);
    assertThat(modalRect.height()).isEqualTo(314.0f);
  }

  @Test
  void rebrandsTheLegacyLogoutRuneScapeLineToVeyrmoor() {
    SidebarWidgetRenderer.WidgetOverride widgetOverride =
        LogoutSidebarPanelRenderer.logoutWidgetOverride(LogoutSidebarPanelRenderer.LOGOUT_BRANDING_COMPONENT_ID);

    assertThat(widgetOverride).isNotNull();
    assertThat(widgetOverride.text()).isEqualTo("Veyrmoor, always use the");
    assertThat(LogoutSidebarPanelRenderer.logoutWidgetOverride(2450)).isNull();
  }
}
