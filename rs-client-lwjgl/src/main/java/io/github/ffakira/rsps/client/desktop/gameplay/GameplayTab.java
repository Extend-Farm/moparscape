package io.github.ffakira.rsps.client.desktop.gameplay;

enum GameplayTab {
  COMBAT(0, "Combat"),
  STATS(1, "Stats"),
  QUESTS(2, "Quests"),
  INVENTORY(3, "Inventory"),
  EQUIPMENT(4, "Equipment"),
  PRAYER(5, "Prayer"),
  MAGIC(6, "Magic"),
  // Fixed 317 chrome leaves lower-left tab slot 7 empty and starts the bottom row at 8.
  FRIENDS(8, "Friends"),
  IGNORES(9, "Ignores"),
  LOGOUT(10, "Logout"),
  SETTINGS(11, "Settings"),
  EMOTES(12, "Emotes"),
  MUSIC(13, "Music");

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
