package com.veyrmoor.client.desktop.gameplay;

public record GameplayChatState(boolean typing, String draftText) {

  public GameplayChatState {
    draftText = draftText == null ? "" : draftText;
  }

  public static GameplayChatState idle() {
    return new GameplayChatState(false, "");
  }

  public static GameplayChatState typing(String draftText) {
    return new GameplayChatState(true, draftText);
  }
}
