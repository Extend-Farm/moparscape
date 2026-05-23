package com.veyrmoor.protocol.chat;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PublicChatTextNormalizerTest {

  @Test
  void normalizesToSentenceCaseLikeTheReferenceClient() {
    assertThat(PublicChatTextNormalizer.normalize("hI")).isEqualTo("Hi");
    assertThat(PublicChatTextNormalizer.normalize("tEST")).isEqualTo("Test");
    assertThat(PublicChatTextNormalizer.normalize("a jfjkf. sDjkds")).isEqualTo("A jfjkf. Sdjkds");
    assertThat(PublicChatTextNormalizer.normalize("aB")).isEqualTo("Ab");
    assertThat(PublicChatTextNormalizer.normalize("a. b")).isEqualTo("A. B");
  }

  @Test
  void collapsesWhitespaceRunsAndTrimsOuterPadding() {
    assertThat(PublicChatTextNormalizer.normalize("   hello\t\tthere \n general   kenobi   "))
        .isEqualTo("Hello there general kenobi");
  }
}
