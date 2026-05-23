package com.veyrmoor.protocol.input;

import com.veyrmoor.protocol.ClientMessage;
import com.veyrmoor.protocol.chat.PublicChatTextNormalizer;

public record PublicChatIntentMessage(String text) implements ClientMessage {

  public PublicChatIntentMessage {
    if (text == null) {
      throw new IllegalArgumentException("text cannot be null");
    }
    text = PublicChatTextNormalizer.normalize(text);
    if (text.isEmpty()) {
      throw new IllegalArgumentException("text cannot be blank");
    }
  }
}
