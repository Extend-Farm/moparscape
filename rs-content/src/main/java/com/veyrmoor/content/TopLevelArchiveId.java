package com.veyrmoor.content;

public enum TopLevelArchiveId {
  TITLE(1, "title"),
  CONFIG(2, "config"),
  INTERFACE(3, "interface"),
  MEDIA(4, "media"),
  UPDATE_LIST(5, "versionlist"),
  TEXTURES(6, "textures"),
  WORDENC(7, "wordenc"),
  SOUNDS(8, "sounds");

  private final int archiveId;
  private final String legacyName;

  TopLevelArchiveId(int archiveId, String legacyName) {
    this.archiveId = archiveId;
    this.legacyName = legacyName;
  }

  public int archiveId() {
    return archiveId;
  }

  public String legacyName() {
    return legacyName;
  }
}
