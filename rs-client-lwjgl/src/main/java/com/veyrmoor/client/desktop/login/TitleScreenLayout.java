package com.veyrmoor.client.desktop.login;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

public record TitleScreenLayout(
    ScreenRect background,
    ScreenRect logo,
    ScreenRect titleBox,
    ScreenRect infoButton,
    ScreenRect playNowButton,
    ScreenRect enterButton,
    ScreenRect cancelButton,
    ScreenRect exitButton,
    ScreenRect usernameField,
    ScreenRect passwordField
) {

  public static TitleScreenLayout forViewport(int width, int height, TitleScreenAssets assets) {
    float designWidth = 765.0f;
    float designHeight = 503.0f;
    float scale = Math.min(1.0f, Math.min(width / designWidth, height / designHeight));
    float rootLeft = (width - designWidth * scale) / 2.0f;
    float rootTop = (height - designHeight * scale) / 2.0f;

    float titleBoxWidth = assetWidth(assets == null ? null : assets.titleBox(), 360.0f) * scale;
    float titleBoxHeight = assetHeight(assets == null ? null : assets.titleBox(), 200.0f) * scale;
    float titleBoxLeft = rootLeft + 202.0f * scale;
    float titleBoxTop = rootTop + 171.0f * scale;

    float titleButtonWidth = assetWidth(assets == null ? null : assets.titleButton(), 146.0f) * scale;
    float titleButtonHeight = assetHeight(assets == null ? null : assets.titleButton(), 40.0f) * scale;
    float leftButtonCenterX = titleBoxLeft + 100.0f * scale;
    float rightButtonCenterX = titleBoxLeft + 260.0f * scale;
    float centeredButtonX = titleBoxLeft + 180.0f * scale;
    float welcomeButtonTop = titleBoxTop + 100.0f * scale;
    float actionButtonTop = titleBoxTop + 130.0f * scale;
    float welcomeButtonCenterY = welcomeButtonTop + titleButtonHeight / 2.0f;
    float actionButtonCenterY = actionButtonTop + titleButtonHeight / 2.0f;

    float logoWidth = assetWidth(assets == null ? null : assets.logo(), 300.0f) * scale;
    float logoHeight = assetHeight(assets == null ? null : assets.logo(), 120.0f) * scale;
    float logoLeft = rootLeft + (382.0f - assetWidth(assets == null ? null : assets.logo(), 300.0f) / 2.0f) * scale;
    float logoTop = rootTop + 18.0f * scale;

    float fieldLeft = titleBoxLeft + 90.0f * scale;
    float fieldWidth = 190.0f * scale;
    float fieldHeight = 16.0f * scale;
    float usernameFieldTop = titleBoxTop + 78.0f * scale;
    float passwordFieldTop = titleBoxTop + 93.0f * scale;

    return new TitleScreenLayout(
        new ScreenRect(rootLeft, rootTop, designWidth * scale, designHeight * scale),
        new ScreenRect(logoLeft, logoTop, logoWidth, logoHeight),
        new ScreenRect(titleBoxLeft, titleBoxTop, titleBoxWidth, titleBoxHeight),
        centeredRect(leftButtonCenterX, welcomeButtonCenterY, titleButtonWidth, titleButtonHeight),
        centeredRect(rightButtonCenterX, welcomeButtonCenterY, titleButtonWidth, titleButtonHeight),
        centeredRect(leftButtonCenterX, actionButtonCenterY, titleButtonWidth, titleButtonHeight),
        centeredRect(rightButtonCenterX, actionButtonCenterY, titleButtonWidth, titleButtonHeight),
        centeredRect(centeredButtonX, actionButtonCenterY, titleButtonWidth, titleButtonHeight),
        new ScreenRect(fieldLeft, usernameFieldTop, fieldWidth, fieldHeight),
        new ScreenRect(fieldLeft, passwordFieldTop, fieldWidth, fieldHeight)
    );
  }

  private static ScreenRect centeredRect(float centerX, float centerY, float width, float height) {
    return new ScreenRect(centerX - width / 2.0f, centerY - height / 2.0f, width, height);
  }

  private static float assetWidth(ArgbImage image, float fallbackWidth) {
    return image == null ? fallbackWidth : image.width();
  }

  private static float assetHeight(ArgbImage image, float fallbackHeight) {
    return image == null ? fallbackHeight : image.height();
  }
}
