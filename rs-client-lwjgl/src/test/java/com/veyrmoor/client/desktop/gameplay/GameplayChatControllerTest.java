package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayChatControllerTest {

  @Test
  void backspaceAndSubmitMutateTheCurrentDraft() {
    GameplayChatController controller = new GameplayChatController();

    controller.activateTyping();
    controller.appendCodePoint('h');
    controller.appendCodePoint('i');
    controller.backspace();

    assertThat(controller.state().draftText()).isEqualTo("h");
    assertThat(controller.submitDraft()).isEqualTo("H");
    assertThat(controller.isTyping()).isFalse();
  }

  @Test
  void ignoresCharactersThatWouldExceedTheProtocolByteLimit() {
    GameplayChatController controller = new GameplayChatController();

    controller.activateTyping();
    for (int index = 0; index < 80; index++) {
      controller.appendCodePoint('a');
    }
    controller.appendCodePoint('b');

    assertThat(controller.state().draftText()).hasSize(80);
    assertThat(controller.state().draftText()).endsWith("a");
  }

  @Test
  void preservesDraftCaseButNormalizesSubmittedSentences() {
    GameplayChatController controller = new GameplayChatController();

    controller.activateTyping();
    controller.appendCodePoint('H');
    controller.appendCodePoint('i');
    controller.appendCodePoint(' ');
    controller.appendCodePoint(' ');
    controller.appendCodePoint('T');
    controller.appendCodePoint('H');
    controller.appendCodePoint('E');
    controller.appendCodePoint('R');
    controller.appendCodePoint('E');
    controller.appendCodePoint('.');
    controller.appendCodePoint(' ');
    controller.appendCodePoint('G');
    controller.appendCodePoint('E');
    controller.appendCodePoint('N');
    controller.appendCodePoint('E');
    controller.appendCodePoint('R');
    controller.appendCodePoint('A');
    controller.appendCodePoint('L');

    assertThat(controller.state().draftText()).isEqualTo("Hi THERE. GENERAL");
    assertThat(controller.submitDraft()).isEqualTo("Hi there. General");
  }

  @Test
  void ignoresLeadingAndRepeatedSpacesInTheDraftBuffer() {
    GameplayChatController controller = new GameplayChatController();

    controller.activateTyping();
    controller.appendCodePoint(' ');
    controller.appendCodePoint(' ');
    controller.appendCodePoint('a');
    controller.appendCodePoint(' ');
    controller.appendCodePoint(' ');
    controller.appendCodePoint('b');

    assertThat(controller.state().draftText()).isEqualTo("a b");
  }
}
