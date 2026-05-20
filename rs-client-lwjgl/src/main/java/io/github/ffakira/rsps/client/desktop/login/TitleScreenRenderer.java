package io.github.ffakira.rsps.client.desktop.login;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import java.util.Objects;

public final class TitleScreenRenderer implements AutoCloseable {

  private static final float TITLE_SCREEN_WIDTH = 765.0f;
  private static final float TITLE_SCREEN_HEIGHT = 503.0f;
  private static final long FLAME_FRAME_INTERVAL_NANOS = 40_000_000L;
  private static final float LEFT_FLAME_X = -20.0f;
  private static final float LEFT_FLAME_Y = 0.0f;
  private static final float RIGHT_FLAME_X = 637.0f;
  private static final float RIGHT_FLAME_Y = 0.0f;
  private static final float LOADING_HEADER_Y = 228.0f;
  private static final float LOADING_BAR_LEFT = 232.0f;
  private static final float LOADING_BAR_TOP = 253.0f;
  private static final float LOADING_BAR_WIDTH = 301.0f;
  private static final float LOADING_BAR_HEIGHT = 33.0f;
  private static final String LOADING_HEADING = "MoparScape is loading - Hold onto your butts...";

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
  private TitleScreenFlameFrame cachedFlameFrame;
  private long lastFlameFrameNanos;
  private LoginScreenState loginScreenState =
      new LoginScreenState(TitleScreenStage.WELCOME, "", "", LoginField.USERNAME);

  public TitleScreenRenderer(TitleScreenAssets titleScreenAssets, ImmediateModeRenderer2d primitives) {
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
        : new TitleScreenFlameAnimator(titleScreenAssets.runeMasks());
    this.leftFlameTexture = titleScreenAssets == null ? null : OpenGlTexture.create(128, 265);
    this.rightFlameTexture = titleScreenAssets == null ? null : OpenGlTexture.create(128, 265);
  }

  public void setLoginScreenState(LoginScreenState loginScreenState) {
    this.loginScreenState = Objects.requireNonNull(loginScreenState, "loginScreenState");
  }

  public TitleScreenLayout currentLayout(int width, int height) {
    return TitleScreenLayout.forViewport(width, height, titleScreenAssets);
  }

  public void render(ClientViewModel viewModel, int width, int height) {
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
    if (loginScreenState.stage() != TitleScreenStage.LOADING) {
      primitives.drawTexturedQuad(titleBoxTexture, layout.titleBox());
    }
    drawTitleScreenButtons(layout);
    drawTitleScreenTextOverlay(viewModel, layout);
  }

  private void drawTitleScreenFlames(TitleScreenLayout layout) {
    if (titleScreenFlameAnimator == null || leftFlameTexture == null || rightFlameTexture == null) {
      return;
    }
    TitleScreenFlameFrame flameFrame = currentFlameFrame();
    leftFlameTexture.update(flameFrame.leftPanel());
    rightFlameTexture.update(flameFrame.rightPanel());

    float scale = layout.background().width() / TITLE_SCREEN_WIDTH;
    float rootLeft = layout.background().left();
    float rootTop = layout.background().top();
    // The overlay still needs to sit on the original fixed side-panel anchors so it lines up with
    // the braziers in the composed title background.
    primitives.drawTexturedQuad(leftFlameTexture, scaledRect(rootLeft, rootTop, LEFT_FLAME_X, LEFT_FLAME_Y, 128.0f, 265.0f, scale));
    primitives.drawTexturedQuad(rightFlameTexture, scaledRect(rootLeft, rootTop, RIGHT_FLAME_X, RIGHT_FLAME_Y, 128.0f, 265.0f, scale));
  }

  private TitleScreenFlameFrame currentFlameFrame() {
    long now = System.nanoTime();
    if (cachedFlameFrame == null || now - lastFlameFrameNanos >= FLAME_FRAME_INTERVAL_NANOS) {
      cachedFlameFrame = titleScreenFlameAnimator.renderNextFrame();
      lastFlameFrameNanos = now;
    }
    return cachedFlameFrame;
  }

  private void drawTitleScreenButtons(TitleScreenLayout layout) {
    switch (loginScreenState.stage()) {
      case LOADING -> {
      }
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
      case LOADING -> drawFallbackLoadingScreen(viewModel.statusText(), layout);
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

  private void drawFallbackLoadingScreen(String statusText, TitleScreenLayout layout) {
    float scale = layout.background().width() / TITLE_SCREEN_WIDTH;
    float rootLeft = layout.background().left();
    float rootTop = layout.background().top();
    ScreenRect loadingBar = scaledRect(rootLeft, rootTop, LOADING_BAR_LEFT, LOADING_BAR_TOP, LOADING_BAR_WIDTH, LOADING_BAR_HEIGHT, scale);
    float centeredTextX = rootLeft + TITLE_SCREEN_WIDTH * scale / 2.0f;

    primitives.drawCenteredText(
        centeredTextX,
        rootTop + LOADING_HEADER_Y * scale,
        LOADING_HEADING,
        0.92f,
        0.94f,
        0.98f
    );

    org.lwjgl.opengl.GL11.glColor3f(0.0f, 0.0f, 0.0f);
    primitives.drawQuad(loadingBar.left(), loadingBar.top(), loadingBar.width(), loadingBar.height());
    org.lwjgl.opengl.GL11.glColor3f(0.55f, 0.07f, 0.07f);
    primitives.drawOutline(loadingBar.left(), loadingBar.top(), loadingBar.width(), loadingBar.height());

    float filledWidth = Math.max(0.0f, (loadingBar.width() - 2.0f) * loginScreenState.loadingPercent() / 100.0f);
    if (filledWidth > 0.0f) {
      org.lwjgl.opengl.GL11.glColor3f(0.49f, 0.0f, 0.0f);
      primitives.drawQuad(loadingBar.left() + 1.0f, loadingBar.top() + 1.0f, filledWidth, loadingBar.height() - 2.0f);
    }

    primitives.drawCenteredText(
        loadingBar.left() + loadingBar.width() / 2.0f,
        loadingBar.top() + loadingBar.height() / 2.0f + 5.0f,
        statusText == null || statusText.isBlank() ? "Preparing native client..." : statusText,
        0.95f,
        0.95f,
        0.95f
    );
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
