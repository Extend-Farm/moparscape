package io.github.ffakira.rsps.client.lwjgl;

public record TitleScreenAssets(
    ArgbImage background,
    ArgbImage logo,
    ArgbImage titleBox,
    ArgbImage titleButton,
    TitleScreenFonts fonts,
    TitleScreenRuneMask[] runeMasks
) {
}
