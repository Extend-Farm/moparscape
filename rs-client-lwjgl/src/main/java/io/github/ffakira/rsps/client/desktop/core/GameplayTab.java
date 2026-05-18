package io.github.ffakira.rsps.client.desktop.core;

enum GameplayTab {
  COMBAT(0, "Combat"),
  STATS(1, "Stats"),
  QUESTS(2, "Quests"),
  INVENTORY(3, "Inventory"),
  EQUIPMENT(4, "Equipment"),
  PRAYER(5, "Prayer"),
  MAGIC(6, "Magic"),
  FRIENDS(7, "Friends"),
  IGNORES(8, "Ignores"),
  LOGOUT(9, "Logout"),
  SETTINGS(10, "Settings"),
  EMOTES(11, "Emotes"),
  MUSIC(12, "Music"),
  EXTRA(13, "Extra");

  private final int index;
  private final String label;

  GameplayTab(int index, String label) {
    this.index = index;
    this.label = label;
  }

  int index() {
    return index;
  }

  String label() {
    return label;
  }

  static GameplayTab fromIndex(int index) {
    for (GameplayTab value : values()) {
      if (value.index == index) {
        return value;
      }
    }
    return INVENTORY;
  }
}
