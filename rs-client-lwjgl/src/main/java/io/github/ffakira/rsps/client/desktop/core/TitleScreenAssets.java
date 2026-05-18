package io.github.ffakira.rsps.client.desktop.core;

public record TitleScreenAssets(
    ArgbImage background,
    ArgbImage logo,
    ArgbImage titleBox,
    ArgbImage titleButton,
    TitleScreenFonts fonts,
    TitleScreenRuneMask[] runeMasks
) {
}
