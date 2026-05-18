package io.github.ffakira.rsps.client.desktop.core;

public final class TitleScreenTextRenderer {

  private static final int CANVAS_WIDTH = 765;
  private static final int CANVAS_HEIGHT = 503;
  private static final int TITLE_BOX_LEFT = 202;
  private static final int TITLE_BOX_TOP = 171;
  private static final int TITLE_BUTTON_HEIGHT = 40;
  private static final int TITLE_BOX_CENTER_X = TITLE_BOX_LEFT + 180;
  private static final int INFO_BUTTON_CENTER_X = TITLE_BOX_LEFT + 100;
  private static final int PLAY_NOW_BUTTON_CENTER_X = TITLE_BOX_LEFT + 260;
  private static final int WELCOME_BUTTON_CENTER_Y = TITLE_BOX_TOP + 100 + TITLE_BUTTON_HEIGHT / 2;
  private static final int ACTION_BUTTON_CENTER_Y = TITLE_BOX_TOP + 130 + TITLE_BUTTON_HEIGHT / 2;
  private static final int DEFAULT_CREDENTIALS_BASELINE_Y = TITLE_BOX_TOP + 60;
  private static final int USERNAME_BASELINE_Y = TITLE_BOX_TOP + 93;
  private static final int PASSWORD_BASELINE_Y = TITLE_BOX_TOP + 108;
  private static final int PRIVATE_INFO_BASELINE_Y = TITLE_BOX_TOP + 45;
  private static final String DEFAULT_CREDENTIALS_PROMPT = "Enter your username and password.";
  private static final int TITLE_YELLOW = 0xffff00;
  private static final int WHITE = 0xffffff;

  private final TitleScreenBitmapFont plainSmallFont;
  private final TitleScreenBitmapFont plainFont;
  private final TitleScreenBitmapFont boldFont;

  public TitleScreenTextRenderer(TitleScreenFonts fonts) {
    this.plainSmallFont = fonts.plainSmall();
    this.plainFont = fonts.plain();
    this.boldFont = fonts.bold();
  }

  public ArgbImage render(LoginScreenState loginScreenState, String statusText, long clockMillis) {
    int[] pixels = new int[CANVAS_WIDTH * CANVAS_HEIGHT];
    switch (loginScreenState.stage()) {
      case WELCOME -> renderWelcome(pixels);
      case CREDENTIALS -> renderCredentials(pixels, loginScreenState, statusText, clockMillis);
      case PRIVATE_SERVER_INFO -> renderPrivateServerInfo(pixels);
    }
    return new ArgbImage(CANVAS_WIDTH, CANVAS_HEIGHT, pixels);
  }

  private void renderWelcome(int[] pixels) {
    drawCenteredBold(pixels, "Welcome to MoparScape", TITLE_BOX_CENTER_X, TITLE_BOX_TOP + 57, TITLE_YELLOW);
    drawCenteredBoldInButton(pixels, "Info", INFO_BUTTON_CENTER_X, WELCOME_BUTTON_CENTER_Y, WHITE);
    drawCenteredBoldInButton(pixels, "Play Now", PLAY_NOW_BUTTON_CENTER_X, WELCOME_BUTTON_CENTER_Y, WHITE);
  }

  private void renderCredentials(
      int[] pixels,
      LoginScreenState loginScreenState,
      String statusText,
      long clockMillis
  ) {
    boolean showCaret = (clockMillis / 500L) % 2L == 0L;
    String usernameLine = "Username: " + loginScreenState.username();
    String passwordLine = "Password: " + loginScreenState.maskedPassword();

    drawCenteredBold(
        pixels,
        normalizeCredentialsStatus(statusText),
        TITLE_BOX_CENTER_X,
        DEFAULT_CREDENTIALS_BASELINE_Y,
        TITLE_YELLOW
    );
    drawBold(pixels, usernameLine, TITLE_BOX_LEFT + 90, USERNAME_BASELINE_Y, WHITE);
    drawBold(pixels, passwordLine, TITLE_BOX_LEFT + 92, PASSWORD_BASELINE_Y, WHITE);

    if (showCaret) {
      if (loginScreenState.focusedField() == LoginField.USERNAME) {
        drawBold(
            pixels,
            "|",
            TITLE_BOX_LEFT + 90 + boldFont.measureText(usernameLine),
            USERNAME_BASELINE_Y,
            TITLE_YELLOW
        );
      } else {
        drawBold(
            pixels,
            "|",
            TITLE_BOX_LEFT + 92 + boldFont.measureText(passwordLine),
            PASSWORD_BASELINE_Y,
            TITLE_YELLOW
        );
      }
    }

    drawCenteredBoldInButton(pixels, "Enter", INFO_BUTTON_CENTER_X, ACTION_BUTTON_CENTER_Y, WHITE);
    drawCenteredBoldInButton(pixels, "Cancel", PLAY_NOW_BUTTON_CENTER_X, ACTION_BUTTON_CENTER_Y, WHITE);
  }

  private void renderPrivateServerInfo(int[] pixels) {
    drawCenteredBold(pixels, "To log into a private server", TITLE_BOX_CENTER_X, PRIVATE_INFO_BASELINE_Y, TITLE_YELLOW);
    plainFont.drawText(
        pixels,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        "click the Exit button below",
        TITLE_BOX_CENTER_X - plainFont.measureText("click the Exit button below") / 2,
        PRIVATE_INFO_BASELINE_Y + 25,
        WHITE,
        true
    );
    plainFont.drawText(
        pixels,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        "then click the 'Play Now' button",
        TITLE_BOX_CENTER_X - plainFont.measureText("then click the 'Play Now' button") / 2,
        PRIVATE_INFO_BASELINE_Y + 40,
        WHITE,
        true
    );
    plainFont.drawText(
        pixels,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        "fill in the required fields",
        TITLE_BOX_CENTER_X - plainFont.measureText("fill in the required fields") / 2,
        PRIVATE_INFO_BASELINE_Y + 55,
        WHITE,
        true
    );
    plainFont.drawText(
        pixels,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        "for more help go to 'www.kaitnieks.com/scr'.",
        TITLE_BOX_CENTER_X - plainFont.measureText("for more help go to 'www.kaitnieks.com/scr'.") / 2,
        PRIVATE_INFO_BASELINE_Y + 70,
        WHITE,
        true
    );
    plainFont.drawText(
        pixels,
        CANVAS_WIDTH,
        CANVAS_HEIGHT,
        "Have Fun ~Moparisthebest",
        TITLE_BOX_CENTER_X - plainFont.measureText("Have Fun ~Moparisthebest") / 2,
        PRIVATE_INFO_BASELINE_Y + 85,
        WHITE,
        true
    );
    drawCenteredBoldInButton(pixels, "Exit", TITLE_BOX_CENTER_X, ACTION_BUTTON_CENTER_Y, WHITE);
  }

  private String normalizeCredentialsStatus(String statusText) {
    if (statusText == null || statusText.isBlank()) {
      return DEFAULT_CREDENTIALS_PROMPT;
    }
    return switch (statusText) {
      case "Booting", "Assets ready", "Welcome to MoparScape" -> DEFAULT_CREDENTIALS_PROMPT;
      default -> statusText;
    };
  }

  private void drawCenteredBold(int[] pixels, String text, int centerX, int baselineY, int rgb) {
    drawBold(
        pixels,
        text,
        centerX - boldFont.measureVisibleWidth(text) / 2 - boldFont.visibleLeftOffset(text),
        baselineY,
        rgb
    );
  }

  private void drawBold(int[] pixels, String text, int left, int baselineY, int rgb) {
    boldFont.drawText(pixels, CANVAS_WIDTH, CANVAS_HEIGHT, text, left, baselineY, rgb, true);
  }

  private void drawCenteredBoldInButton(int[] pixels, String text, int centerX, int centerY, int rgb) {
    drawBold(
        pixels,
        text,
        centerX - boldFont.measureVisibleWidth(text) / 2 - boldFont.visibleLeftOffset(text),
        boldFont.baselineForVerticalCenter(text, centerY),
        rgb
    );
  }
}
