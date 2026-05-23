package com.veyrmoor.protocol.chat;

import com.veyrmoor.protocol.ServerMessage;

public record PublicChatMessage(String speakerDisplayName, String text) implements ServerMessage {

  public PublicChatMessage {
    if (speakerDisplayName == null) {
      throw new IllegalArgumentException("speakerDisplayName cannot be null");
    }
    if (text == null) {
      throw new IllegalArgumentException("text cannot be null");
    }
    speakerDisplayName = speakerDisplayName.trim();
    text = PublicChatTextNormalizer.normalize(text);
    if (speakerDisplayName.isEmpty()) {
      throw new IllegalArgumentException("speakerDisplayName cannot be blank");
    }
    if (text.isEmpty()) {
      throw new IllegalArgumentException("text cannot be blank");
    }
  }
}
