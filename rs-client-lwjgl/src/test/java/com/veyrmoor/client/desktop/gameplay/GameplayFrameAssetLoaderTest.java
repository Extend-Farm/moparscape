package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.assets.image.ArgbImageTransforms;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GameplayFrameAssetLoaderTest {

  @Test
  void loadsGameplayFrameAssetsFromMediaArchive() {
    GameplayFrameAssets assets = GameplayFrameAssetLoader.loadFromWorkingDirectory(Path.of("."));

    assertThat(assets.backLeft1().width()).isGreaterThan(0);
    assertThat(assets.backLeft2().height()).isGreaterThan(0);
    assertThat(assets.backRight1().width()).isGreaterThan(0);
    assertThat(assets.backRight2().height()).isGreaterThan(0);
    assertThat(assets.backTop1().width()).isGreaterThan(0);
    assertThat(assets.backVmid1().height()).isGreaterThan(0);
    assertThat(assets.backVmid2().height()).isGreaterThan(0);
    assertThat(assets.backVmid3().height()).isGreaterThan(0);
    assertThat(assets.backHmid2().width()).isGreaterThan(0);
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
    assertThat(assets.mapFunctionIcons()).isNotEmpty();
    assertThat(assets.mapFunctionIcons()).anySatisfy(icon -> assertThat(icon).isNotNull());
    assertThat(assets.mapDotIcons()).isNotEmpty();
    assertThat(assets.mapDotIcons()).hasSizeGreaterThanOrEqualTo(4);
    assertThat(assets.mapDotIcons()).anySatisfy(icon -> assertThat(icon).isNotNull());
    assertThat(assets.statsTabAssets()).isNotNull();
    assertThat(assets.statsTabAssets().buttonLeft().width()).isGreaterThan(0);
    assertThat(assets.statsTabAssets().buttonRight().height()).isGreaterThan(0);
    for (int skillId = 0; skillId <= 20; skillId++) {
      assertThat(assets.statsTabAssets().iconForSkill(skillId)).isNotNull();
    }
    assertThat(assets.redstone1Flipped().pixels())
        .containsExactly(ArgbImageTransforms.flipHorizontally(assets.redstone1()).pixels());
    assertThat(assets.redstone2Flipped().pixels())
        .containsExactly(ArgbImageTransforms.flipHorizontally(assets.redstone2()).pixels());
    assertThat(assets.redstone1Mirrored().pixels())
        .containsExactly(ArgbImageTransforms.flipVertically(assets.redstone1()).pixels());
    assertThat(assets.redstone3Mirrored().pixels())
        .containsExactly(ArgbImageTransforms.flipVertically(assets.redstone3()).pixels());
    assertThat(assets.redstone1BothTransforms().pixels())
        .containsExactly(ArgbImageTransforms.flipVertically(assets.redstone1Flipped()).pixels());
  }
}
