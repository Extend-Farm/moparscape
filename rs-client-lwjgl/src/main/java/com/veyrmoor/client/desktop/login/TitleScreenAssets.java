package com.veyrmoor.client.desktop.login;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;

public record TitleScreenAssets(
    ArgbImage background,
    ArgbImage logo,
    ArgbImage titleBox,
    ArgbImage titleButton,
    TitleScreenFonts fonts,
    TitleScreenRuneMask[] runeMasks
) {
}
