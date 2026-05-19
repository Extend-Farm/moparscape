package io.github.ffakira.rsps.client.desktop.login;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

public record TitleScreenAssets(
    ArgbImage background,
    ArgbImage logo,
    ArgbImage titleBox,
    ArgbImage titleButton,
    TitleScreenFonts fonts,
    TitleScreenRuneMask[] runeMasks
) {
}
