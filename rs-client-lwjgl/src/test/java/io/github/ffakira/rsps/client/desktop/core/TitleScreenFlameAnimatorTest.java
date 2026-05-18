package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class TitleScreenFlameAnimatorTest {

  @Test
  void rendersTitleScreenFlamePanelsWithVisiblePixels() {
    TitleScreenRuneMask[] runeMasks = new TitleScreenRuneMask[12];
    for (int index = 0; index < runeMasks.length; index++) {
      runeMasks[index] = new TitleScreenRuneMask(new boolean[128 * 256]);
    }
    int[] leftBasePixels = new int[128 * 265];
    int[] rightBasePixels = new int[128 * 265];
    for (int index = 0; index < leftBasePixels.length; index++) {
      leftBasePixels[index] = 0xff222222;
      rightBasePixels[index] = 0xff222222;
    }

    TitleScreenFlameFrame flameFrame = new TitleScreenFlameAnimator(
        runeMasks,
        new ArgbImage(128, 265, leftBasePixels),
        new ArgbImage(128, 265, rightBasePixels)
    ).renderNextFrame();

    assertThat(flameFrame.leftPanel().width()).isEqualTo(128);
    assertThat(flameFrame.leftPanel().height()).isEqualTo(265);
    assertThat(flameFrame.rightPanel().width()).isEqualTo(128);
    assertThat(flameFrame.rightPanel().height()).isEqualTo(265);
    assertThat(Arrays.stream(flameFrame.leftPanel().pixels()).anyMatch(pixel -> pixel != 0)).isTrue();
    assertThat(Arrays.stream(flameFrame.rightPanel().pixels()).anyMatch(pixel -> pixel != 0)).isTrue();
  }
}
