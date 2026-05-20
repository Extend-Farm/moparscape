package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.ArrayList;
import java.util.List;

final class GameplayContextMenu {

  static final String TITLE = "Choose Option";
  static final int OUTER_RGB = 0x5d5447;
  static final int TITLE_RGB = 0x5d5447;
  static final int ACTION_RGB = 0xffffff;
  static final int ACTION_HOVER_RGB = 0xffff00;
  static final int PLAYER_SUBJECT_RGB = 0xffffff;
  static final int NPC_SUBJECT_RGB = 0xffff00;
  static final int SCENERY_SUBJECT_RGB = 0x00ffff;
  static final int ITEM_SUBJECT_RGB = 0xff9040;
  static final int SPELL_SUBJECT_RGB = 0x00ff00;
  static final int WORLD_ENTITY_SUBJECT_RGB = 0xeda6ed;

  private static final float HORIZONTAL_PADDING = 8.0f;
  private static final float TOTAL_VERTICAL_PADDING = 22.0f;
  private static final float HEADER_BLACK_HEIGHT = 16.0f;
  private static final float BODY_TOP = 18.0f;
  private static final float ROW_HEIGHT = 15.0f;
  private static final float MIN_WIDTH = 65.0f;

  private final ScreenRect rect;
  private final List<Entry> entries;
  private final List<Entry> displayEntries;

  private GameplayContextMenu(ScreenRect rect, List<Entry> entries) {
    this.rect = rect;
    this.entries = List.copyOf(entries);
    this.displayEntries = reverseCopy(this.entries);
  }

  static GameplayContextMenu walkHereMenu(
      ScreenRect bounds,
      double preferredX,
      double preferredY,
      WorldPoint targetWorldPoint,
      int titleTextWidth,
      int walkHereTextWidth,
      int cancelTextWidth
  ) {
    Entry cancelEntry = plainEntry(GameplayMenuAction.cancel(), "Cancel");
    Entry walkHereEntry = plainEntry(GameplayMenuAction.walkTo(targetWorldPoint), "Walk here");
    float width = Math.max(MIN_WIDTH, Math.max(titleTextWidth, Math.max(walkHereTextWidth, cancelTextWidth)) + HORIZONTAL_PADDING);
    float height = entriesHeight(2);
    float left = clamp((float) preferredX - width * 0.5f, bounds.left(), bounds.left() + bounds.width() - width);
    float top = clamp((float) preferredY, bounds.top(), bounds.top() + bounds.height() - height);
    return new GameplayContextMenu(new ScreenRect(left, top, width, height), List.of(cancelEntry, walkHereEntry));
  }

  ScreenRect rect() {
    return rect;
  }

  String title() {
    return TITLE;
  }

  List<Entry> entries() {
    return displayEntries;
  }

  ScreenRect headerFillRect() {
    return new ScreenRect(rect.left() + 1.0f, rect.top() + 1.0f, rect.width() - 2.0f, HEADER_BLACK_HEIGHT);
  }

  ScreenRect bodyFillRect() {
    return new ScreenRect(rect.left() + 1.0f, rect.top() + BODY_TOP, rect.width() - 2.0f, rect.height() - 19.0f);
  }

  float titleLeft() {
    return rect.left() + 3.0f;
  }

  float titleBaselineY() {
    return rect.top() + 14.0f;
  }

  float entryLeft() {
    return rect.left() + 3.0f;
  }

  float entryBaselineY(int index) {
    return rect.top() + 31.0f + index * ROW_HEIGHT;
  }

  ScreenRect entryHoverRect(int index) {
    return new ScreenRect(rect.left(), rect.top() + BODY_TOP + index * ROW_HEIGHT, rect.width(), ROW_HEIGHT);
  }

  Entry entryAt(double x, double y) {
    int entryIndex = entryIndexAt(x, y);
    return entryIndex < 0 ? null : displayEntries.get(entryIndex);
  }

  int entryIndexAt(double x, double y) {
    for (int index = 0; index < entries.size(); index++) {
      if (entryHoverRect(index).contains(x, y)) {
        return index;
      }
    }
    return -1;
  }

  static Entry plainEntry(GameplayMenuAction action, String text) {
    return new Entry(List.of(TextRun.action(text)), action);
  }

  static Entry subjectEntry(GameplayMenuAction action, String actionText, String subjectText, int subjectRgb) {
    return new Entry(
        List.of(
            TextRun.action(actionText),
            TextRun.subject(subjectText, subjectRgb)
        ),
        action
    );
  }

  record Entry(List<TextRun> textRuns, GameplayMenuAction action) {
    String plainText() {
      StringBuilder builder = new StringBuilder();
      for (TextRun textRun : textRuns) {
        builder.append(textRun.text());
      }
      return builder.toString();
    }
  }

  record TextRun(String text, int rgb, boolean actionColorControlled) {
    static TextRun action(String text) {
      return new TextRun(text, ACTION_RGB, true);
    }

    static TextRun subject(String text, int rgb) {
      return new TextRun(text, rgb, false);
    }
  }

  private static float clamp(float value, float min, float max) {
    if (max < min) {
      return min;
    }
    return Math.max(min, Math.min(value, max));
  }

  private static float entriesHeight(int entryCount) {
    return TOTAL_VERTICAL_PADDING + entryCount * ROW_HEIGHT;
  }

  private static <T> List<T> reverseCopy(List<T> values) {
    ArrayList<T> reversed = new ArrayList<>(values.size());
    for (int index = values.size() - 1; index >= 0; index--) {
      reversed.add(values.get(index));
    }
    return List.copyOf(reversed);
  }
}
