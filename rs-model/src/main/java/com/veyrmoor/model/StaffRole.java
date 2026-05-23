package com.veyrmoor.model;

public enum StaffRole {
  NONE(0, "None", -1),
  MODERATOR(1, "Moderator", 0),
  ADMIN(2, "Admin", 1);

  private final int id;
  private final String displayName;
  private final int crownSpriteIndex;

  StaffRole(int id, String displayName, int crownSpriteIndex) {
    this.id = id;
    this.displayName = displayName;
    this.crownSpriteIndex = crownSpriteIndex;
  }

  public int id() {
    return id;
  }

  public String displayName() {
    return displayName;
  }

  public boolean hasCrown() {
    return crownSpriteIndex >= 0;
  }

  public int crownSpriteIndex() {
    return crownSpriteIndex;
  }

  public static StaffRole fromId(int id) {
    return switch (id) {
      case 1 -> MODERATOR;
      case 2 -> ADMIN;
      default -> NONE;
    };
  }
}
