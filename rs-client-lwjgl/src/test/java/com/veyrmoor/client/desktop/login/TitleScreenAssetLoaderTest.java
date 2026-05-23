package com.veyrmoor.client.desktop.login;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class TitleScreenAssetLoaderTest {

  @Test
  void loadsNativeTitleScreenAssetsFromRepositoryCache() {
    TitleScreenAssets assets = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of("."));

    assertThat(assets.background().width()).isEqualTo(383);
    assertThat(assets.background().height()).isEqualTo(503);
    assertThat(assets.logo().width()).isGreaterThan(0);
    assertThat(assets.logo().height()).isGreaterThan(0);
    assertThat(assets.titleBox().width()).isGreaterThan(0);
    assertThat(assets.titleBox().height()).isGreaterThan(0);
    assertThat(assets.titleButton().width()).isGreaterThan(0);
    assertThat(assets.titleButton().height()).isGreaterThan(0);
    assertThat(assets.fonts().plainSmall().measureText("Welcome")).isGreaterThan(0);
    assertThat(assets.fonts().bold().measureText("Play Now")).isGreaterThan(0);
    assertThat(assets.runeMasks()).hasSize(12);
    assertThat(assets.runeMasks()[0].maskPixels()).hasSize(128 * 256);
  }
}
