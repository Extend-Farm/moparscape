package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TitleScreenLayoutTest {

  @Test
  void doesNotUpscaleTitleScreenCompositionInLargerWindows() {
    TitleScreenLayout layout = TitleScreenLayout.forViewport(1027, 760, null);

    assertThat(layout.background().width()).isEqualTo(765.0f);
    assertThat(layout.background().height()).isEqualTo(503.0f);
    assertThat(layout.titleBox().left()).isEqualTo(layout.background().left() + 202.0f);
    assertThat(layout.titleBox().top()).isEqualTo(layout.background().top() + 171.0f);
    assertThat(layout.infoButton().top()).isEqualTo(layout.titleBox().top() + 100.0f);
    assertThat(layout.playNowButton().top()).isEqualTo(layout.titleBox().top() + 100.0f);
    assertThat(layout.enterButton().top()).isEqualTo(layout.titleBox().top() + 130.0f);
    assertThat(layout.cancelButton().top()).isEqualTo(layout.titleBox().top() + 130.0f);
    assertThat(layout.exitButton().top()).isEqualTo(layout.titleBox().top() + 130.0f);
    assertThat(layout.usernameField().left()).isEqualTo(layout.passwordField().left());
    assertThat(layout.usernameField().top()).isEqualTo(layout.titleBox().top() + 78.0f);
    assertThat(layout.passwordField().top()).isEqualTo(layout.titleBox().top() + 93.0f);
  }

  @Test
  void downscalesTitleScreenCompositionWhenViewportIsSmallerThanDesignSize() {
    TitleScreenLayout layout = TitleScreenLayout.forViewport(640, 420, null);

    assertThat(layout.background().width()).isLessThan(765.0f);
    assertThat(layout.background().height()).isLessThan(503.0f);
  }
}
