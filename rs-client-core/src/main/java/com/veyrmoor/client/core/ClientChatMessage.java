package com.veyrmoor.client.core;

public record ClientChatMessage(
    ClientChatMessageKind kind,
    String speakerDisplayName,
    String text
) {

  public ClientChatMessage {
    if (kind == null) {
      throw new IllegalArgumentException("kind cannot be null");
    }
    if (text == null) {
      throw new IllegalArgumentException("text cannot be null");
    }
    if (speakerDisplayName != null) {
      speakerDisplayName = speakerDisplayName.trim();
      if (speakerDisplayName.isEmpty()) {
        speakerDisplayName = null;
      }
    }
    text = text.trim();
    if (text.isEmpty()) {
      throw new IllegalArgumentException("text cannot be blank");
    }
    if (kind == ClientChatMessageKind.PUBLIC && speakerDisplayName == null) {
      throw new IllegalArgumentException("public chat requires a speakerDisplayName");
    }
  }

  public static ClientChatMessage publicChat(String speakerDisplayName, String text) {
    return new ClientChatMessage(ClientChatMessageKind.PUBLIC, speakerDisplayName, text);
  }

  public static ClientChatMessage system(String text) {
    return new ClientChatMessage(ClientChatMessageKind.SYSTEM, null, text);
  }

  public String formattedText() {
    if (speakerDisplayName == null) {
      return text;
    }
    return speakerDisplayName + ": " + text;
  }
}
