package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GameplayFrameAssetLoaderTest {

  @Test
  void loadsGameplayFrameAssetsFromMediaArchive() {
    GameplayFrameAssets assets = GameplayFrameAssetLoader.loadFromWorkingDirectory(Path.of("."));

    assertThat(assets.invBack().width()).isGreaterThan(0);
    assertThat(assets.chatBack().height()).isGreaterThan(0);
    assertThat(assets.mapBack().width()).isGreaterThan(0);
    assertThat(assets.backHMid1().width()).isGreaterThan(0);
    assertThat(assets.compass().width()).isGreaterThan(0);
    assertThat(assets.redstone1().width()).isGreaterThan(0);
    assertThat(assets.redstone2().width()).isGreaterThan(0);
    assertThat(assets.redstone3().width()).isGreaterThan(0);
    assertThat(assets.redstone1Flipped().width()).isEqualTo(assets.redstone1().width());
    assertThat(assets.redstone2BothTransforms().height()).isEqualTo(assets.redstone2().height());
    assertThat(assets.sideIcons()).hasSize(13);
  }
}
