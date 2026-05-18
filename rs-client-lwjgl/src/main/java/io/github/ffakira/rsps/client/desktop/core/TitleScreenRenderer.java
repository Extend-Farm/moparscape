package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import java.util.Objects;

final class TitleScreenRenderer implements AutoCloseable {

  private static final float TITLE_SCREEN_WIDTH = 765.0f;
  private static final float TITLE_SCREEN_HEIGHT = 503.0f;
  private static final float LEFT_FLAME_X = 0.0f;
  private static final float LEFT_FLAME_Y = 0.0f;
  private static final float RIGHT_FLAME_X = 637.0f;
  private static final float RIGHT_FLAME_Y = 0.0f;

  private final ImmediateModeRenderer2d primitives;
  private final TitleScreenAssets titleScreenAssets;
  private final OpenGlTexture backgroundTexture;
  private final OpenGlTexture logoTexture;
  private final OpenGlTexture titleBoxTexture;
  private final OpenGlTexture titleButtonTexture;
  private final TitleScreenTextRenderer titleScreenTextRenderer;
  private final OpenGlTexture titleTextOverlayTexture;
  private final TitleScreenFlameAnimator titleScreenFlameAnimator;
  private final OpenGlTexture leftFlameTexture;
  private final OpenGlTexture rightFlameTexture;
  private LoginScreenState loginScreenState =
      new LoginScreenState(TitleScreenStage.WELCOME, "", "", LoginField.USERNAME);

  TitleScreenRenderer(TitleScreenAssets titleScreenAssets, ImmediateModeRenderer2d primitives) {
    this.titleScreenAssets = titleScreenAssets;
    this.primitives = primitives;
    ArgbImage composedBackground = titleScreenAssets == null
        ? null
        : TitleScreenBackgroundComposer.compose(titleScreenAssets.background());
    this.backgroundTexture = composedBackground == null ? null : OpenGlTexture.fromArgbImage(composedBackground);
    this.logoTexture = titleScreenAssets == null ? null : OpenGlTexture.fromArgbImage(titleScreenAssets.logo());
    this.titleBoxTexture = titleScreenAssets == null ? null : OpenGlTexture.fromArgbImage(titleScreenAssets.titleBox());
    this.titleButtonTexture = titleScreenAssets == null ? null : OpenGlTexture.fromArgbImage(titleScreenAssets.titleButton());
    this.titleScreenTextRenderer = titleScreenAssets == null ? null : new TitleScreenTextRenderer(titleScreenAssets.fonts());
    this.titleTextOverlayTexture = titleScreenAssets == null ? null : OpenGlTexture.create((int) TITLE_SCREEN_WIDTH, (int) TITLE_SCREEN_HEIGHT);
    this.titleScreenFlameAnimator = titleScreenAssets == null
        ? null
        : new TitleScreenFlameAnimator(
            titleScreenAssets.runeMasks(),
            crop(composedBackground, 0, 0, 128, 265),
            crop(composedBackground, 637, 0, 128, 265)
        );
    this.leftFlameTexture = titleScreenAssets == null ? null : OpenGlTexture.create(128, 265);
    this.rightFlameTexture = titleScreenAssets == null ? null : OpenGlTexture.create(128, 265);
  }

  void setLoginScreenState(LoginScreenState loginScreenState) {
    this.loginScreenState = Objects.requireNonNull(loginScreenState, "loginScreenState");
  }

  TitleScreenLayout currentLayout(int width, int height) {
    return TitleScreenLayout.forViewport(width, height, titleScreenAssets);
  }

  void render(ClientViewModel viewModel, int width, int height) {
    if (viewModel.loggedIn()) {
      drawPostLoginStatusScreen(viewModel, width, height);
      return;
    }
    if (hasTitleScreenAssets()) {
      drawTitleScreen(viewModel, width, height);
    } else {
      drawFallbackTitleScreen(viewModel, width, height);
    }
  }

  private void drawTitleScreen(ClientViewModel viewModel, int width, int height) {
    TitleScreenLayout layout = currentLayout(width, height);
    primitives.drawTexturedQuad(backgroundTexture, layout.background());
    drawTitleScreenFlames(layout);
    primitives.drawTexturedQuad(logoTexture, layout.logo());
    primitives.drawTexturedQuad(titleBoxTexture, layout.titleBox());
    drawTitleScreenButtons(layout);
    drawTitleScreenTextOverlay(viewModel, layout);
  }

  private void drawTitleScreenFlames(TitleScreenLayout layout) {
    if (titleScreenFlameAnimator == null || leftFlameTexture == null || rightFlameTexture == null) {
      return;
    }
    TitleScreenFlameFrame flameFrame = titleScreenFlameAnimator.renderNextFrame();
    leftFlameTexture.update(flameFrame.leftPanel());
    rightFlameTexture.update(flameFrame.rightPanel());

    float scale = layout.background().width() / TITLE_SCREEN_WIDTH;
    float rootLeft = layout.background().left();
    float rootTop = layout.background().top();
    primitives.drawTexturedQuad(leftFlameTexture, scaledRect(rootLeft, rootTop, LEFT_FLAME_X, LEFT_FLAME_Y, 128.0f, 265.0f, scale));
    primitives.drawTexturedQuad(rightFlameTexture, scaledRect(rootLeft, rootTop, RIGHT_FLAME_X, RIGHT_FLAME_Y, 128.0f, 265.0f, scale));
  }

  private void drawTitleScreenButtons(TitleScreenLayout layout) {
    switch (loginScreenState.stage()) {
      case WELCOME, CREDENTIALS -> {
        if (loginScreenState.stage() == TitleScreenStage.WELCOME) {
          primitives.drawTexturedQuad(titleButtonTexture, layout.infoButton());
          primitives.drawTexturedQuad(titleButtonTexture, layout.playNowButton());
          return;
        }
        primitives.drawTexturedQuad(titleButtonTexture, layout.enterButton());
        primitives.drawTexturedQuad(titleButtonTexture, layout.cancelButton());
      }
      case PRIVATE_SERVER_INFO -> primitives.drawTexturedQuad(titleButtonTexture, layout.exitButton());
    }
  }

  private void drawFallbackTitleScreen(ClientViewModel viewModel, int width, int height) {
    TitleScreenLayout layout = currentLayout(width, height);

    org.lwjgl.opengl.GL11.glColor3f(0.11f, 0.13f, 0.16f);
    primitives.drawQuad(layout.background().left(), layout.background().top(), layout.background().width(), layout.background().height());
    org.lwjgl.opengl.GL11.glColor3f(0.24f, 0.29f, 0.34f);
    primitives.drawOutline(layout.background().left(), layout.background().top(), layout.background().width(), layout.background().height());

    org.lwjgl.opengl.GL11.glColor3f(0.18f, 0.20f, 0.25f);
    primitives.drawQuad(layout.titleBox().left(), layout.titleBox().top(), layout.titleBox().width(), layout.titleBox().height());
    org.lwjgl.opengl.GL11.glColor3f(0.34f, 0.39f, 0.47f);
    primitives.drawOutline(layout.titleBox().left(), layout.titleBox().top(), layout.titleBox().width(), layout.titleBox().height());

    primitives.drawCenteredText(
        layout.background().left() + layout.background().width() / 2.0f,
        layout.background().top() + 54.0f,
        "MOPARSCAPE",
        0.92f,
        0.86f,
        0.46f
    );

    switch (loginScreenState.stage()) {
      case WELCOME -> {
        primitives.drawCenteredText(
            layout.titleBox().left() + layout.titleBox().width() / 2.0f,
            layout.titleBox().top() + 72.0f,
            "Welcome to MoparScape",
            0.95f,
            0.92f,
            0.55f
        );
        drawFallbackButton(layout.infoButton(), "INFO", false);
        drawFallbackButton(layout.playNowButton(), "PLAY NOW", true);
      }
      case CREDENTIALS -> {
        primitives.drawCenteredText(
            layout.titleBox().left() + layout.titleBox().width() / 2.0f,
            layout.titleBox().top() + 48.0f,
            viewModel.statusText(),
            0.95f,
            0.92f,
            0.55f
        );
        drawFallbackField(layout.usernameField(), "Username:", loginScreenState.username(), loginScreenState.focusedField() == LoginField.USERNAME);
        drawFallbackField(layout.passwordField(), "Password:", loginScreenState.maskedPassword(), loginScreenState.focusedField() == LoginField.PASSWORD);
        drawFallbackButton(layout.enterButton(), "ENTER", loginScreenState.canSubmit());
        drawFallbackButton(layout.cancelButton(), "CANCEL", false);
      }
      case PRIVATE_SERVER_INFO -> {
        primitives.drawCenteredText(
            layout.titleBox().left() + layout.titleBox().width() / 2.0f,
            layout.titleBox().top() + 50.0f,
            "Native title archive decoding is not ready yet.",
            0.95f,
            0.92f,
            0.55f
        );
        primitives.drawCenteredText(
            layout.titleBox().left() + layout.titleBox().width() / 2.0f,
            layout.titleBox().top() + 74.0f,
            "Use Play Now to log in through the new runtime.",
            0.84f,
            0.88f,
            0.94f
        );
        drawFallbackButton(layout.exitButton(), "EXIT", false);
      }
    }
  }

  private void drawTitleScreenTextOverlay(ClientViewModel viewModel, TitleScreenLayout layout) {
    if (titleScreenTextRenderer == null || titleTextOverlayTexture == null) {
      return;
    }
    titleTextOverlayTexture.update(
        titleScreenTextRenderer.render(loginScreenState, viewModel.statusText(), System.currentTimeMillis())
    );
    primitives.drawTexturedQuad(titleTextOverlayTexture, layout.background());
  }

  private void drawPostLoginStatusScreen(ClientViewModel viewModel, int width, int height) {
    float panelWidth = 360.0f;
    float panelHeight = 120.0f;
    float left = (width - panelWidth) / 2.0f;
    float top = (height - panelHeight) / 2.0f;

    org.lwjgl.opengl.GL11.glColor3f(0.13f, 0.16f, 0.22f);
    primitives.drawQuad(left, top, panelWidth, panelHeight);
    org.lwjgl.opengl.GL11.glColor3f(0.33f, 0.40f, 0.52f);
    primitives.drawOutline(left, top, panelWidth, panelHeight);

    primitives.drawCenteredText(left + panelWidth / 2.0f, top + 42.0f, "Native Runtime", 0.92f, 0.86f, 0.46f);
    primitives.drawCenteredText(left + panelWidth / 2.0f, top + 72.0f, viewModel.statusText(), 0.95f, 0.96f, 0.98f);
  }

  private void drawFallbackField(ScreenRect rect, String label, String value, boolean focused) {
    primitives.drawText(rect.left() - 72.0f, rect.top() + rect.height() - 2.0f, label, 0.86f, 0.90f, 0.96f);
    org.lwjgl.opengl.GL11.glColor3f(0.09f, 0.11f, 0.15f);
    primitives.drawQuad(rect.left(), rect.top() - 4.0f, rect.width(), rect.height() + 8.0f);
    if (focused) {
      org.lwjgl.opengl.GL11.glColor3f(0.92f, 0.86f, 0.46f);
    } else {
      org.lwjgl.opengl.GL11.glColor3f(0.33f, 0.40f, 0.52f);
    }
    primitives.drawOutline(rect.left(), rect.top() - 4.0f, rect.width(), rect.height() + 8.0f);
    primitives.drawText(rect.left() + 8.0f, rect.top() + rect.height() + 6.0f, value, 0.95f, 0.96f, 0.98f);
  }

  private void drawFallbackButton(ScreenRect rect, String label, boolean emphasized) {
    if (emphasized) {
      org.lwjgl.opengl.GL11.glColor3f(0.27f, 0.31f, 0.18f);
    } else {
      org.lwjgl.opengl.GL11.glColor3f(0.18f, 0.18f, 0.18f);
    }
    primitives.drawQuad(rect.left(), rect.top(), rect.width(), rect.height());
    if (emphasized) {
      org.lwjgl.opengl.GL11.glColor3f(0.92f, 0.86f, 0.46f);
    } else {
      org.lwjgl.opengl.GL11.glColor3f(0.45f, 0.45f, 0.45f);
    }
    primitives.drawOutline(rect.left(), rect.top(), rect.width(), rect.height());
    primitives.drawCenteredText(rect.left() + rect.width() / 2.0f, rect.top() + rect.height() / 2.0f + 5.0f, label, 0.95f, 0.95f, 0.95f);
  }

  private boolean hasTitleScreenAssets() {
    return backgroundTexture != null
        && logoTexture != null
        && titleBoxTexture != null
        && titleButtonTexture != null
        && titleScreenTextRenderer != null
        && titleTextOverlayTexture != null;
  }

  private ScreenRect scaledRect(
      float rootLeft,
      float rootTop,
      float designLeft,
      float designTop,
      float designWidth,
      float designHeight,
      float scale
  ) {
    return new ScreenRect(
        rootLeft + designLeft * scale,
        rootTop + designTop * scale,
        designWidth * scale,
        designHeight * scale
    );
  }

  private static ArgbImage crop(ArgbImage image, int left, int top, int cropWidth, int cropHeight) {
    int[] pixels = new int[cropWidth * cropHeight];
    for (int y = 0; y < cropHeight; y++) {
      int sourceOffset = (top + y) * image.width() + left;
      System.arraycopy(image.pixels(), sourceOffset, pixels, y * cropWidth, cropWidth);
    }
    return new ArgbImage(cropWidth, cropHeight, pixels);
  }

  @Override
  public void close() {
    closeTexture(backgroundTexture);
    closeTexture(logoTexture);
    closeTexture(titleBoxTexture);
    closeTexture(titleButtonTexture);
    closeTexture(titleTextOverlayTexture);
    closeTexture(leftFlameTexture);
    closeTexture(rightFlameTexture);
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
