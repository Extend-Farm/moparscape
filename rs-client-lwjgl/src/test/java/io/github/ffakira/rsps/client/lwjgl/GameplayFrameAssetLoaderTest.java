package io.github.ffakira.rsps.client.lwjgl;

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
    assertThat(assets.sideIcons()).hasSize(13);
  }
}
