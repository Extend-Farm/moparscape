package com.veyrmoor.client.desktop.login;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import org.junit.jupiter.api.Test;

class TitleScreenBackgroundComposerTest {

  @Test
  void composesTitleBackgroundToFullLoginCanvas() {
    int[] pixels = new int[383 * 503];
    for (int y = 0; y < 503; y++) {
      for (int x = 0; x < 383; x++) {
        pixels[x + y * 383] = 0xff000000 | ((x & 0xff) << 16) | ((y & 0xff) << 8) | (x ^ y) & 0xff;
      }
    }

    ArgbImage composed = TitleScreenBackgroundComposer.compose(new ArgbImage(383, 503, pixels));

    assertThat(composed.width()).isEqualTo(765);
    assertThat(composed.height()).isEqualTo(503);
    assertThat(composed.pixels()[0]).isNotZero();
    assertThat(composed.pixels()[128]).isNotZero();
    assertThat(composed.pixels()[637]).isNotZero();
  }
}
