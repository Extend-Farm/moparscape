package io.github.ffakira.rsps.client.desktop.gameplay;

final class GameplayStatsTabLayout {

  static final float SUMMARY_RECT_X = 1.0f;
  static final float SUMMARY_RECT_Y = 212.0f;
  static final float SUMMARY_OUTER_X = 3.0f;
  static final float SUMMARY_OUTER_Y = 9.0f;
  static final float SUMMARY_INNER_ONE_X = 4.0f;
  static final float SUMMARY_INNER_ONE_Y = 10.0f;
  static final float SUMMARY_INNER_TWO_X = 4.0f;
  static final float SUMMARY_INNER_TWO_Y = 10.0f;
  static final float SUMMARY_FILL_X = 5.0f;
  static final float SUMMARY_FILL_Y = 11.0f;

  static final float HOVER_PANEL_X = 2.0f;
  static final float HOVER_PANEL_Y = 216.0f;
  static final float HOVER_FILL_X = 6.0f;
  static final float HOVER_FILL_Y = 223.0f;
  static final float HOVER_FIRST_LINE_X = 8.0f;
  static final float HOVER_FIRST_LINE_Y = 223.0f;
  static final float HOVER_SECOND_LINE_X = 8.0f;
  static final float HOVER_SECOND_LINE_Y = 239.0f;
  static final float HOVER_SECOND_VALUE_X = 86.0f;

  private static final StatsSlotLayout[] SLOTS = {
      new StatsSlotLayout(0, 0.0f, 2.0f, 68.0f, -1.0f, 0.0f, 29.0f, 0.0f, 4.0f, 5.0f, 32.0f, 5.0f, 15, 44.0f, 17.0f, 15),
      new StatsSlotLayout(3, 64.0f, 2.0f, 82.0f, 62.0f, 0.0f, 92.0f, 0.0f, 68.0f, 5.0f, 96.0f, 5.0f, 15, 108.0f, 17.0f, 15),
      new StatsSlotLayout(14, 128.0f, 2.0f, 66.0f, 125.0f, 0.0f, 155.0f, 0.0f, 128.0f, 5.0f, 160.0f, 5.0f, 15, 172.0f, 17.0f, 15),
      new StatsSlotLayout(2, 0.0f, 33.0f, 79.0f, -1.0f, 31.0f, 29.0f, 31.0f, 4.0f, 36.0f, 32.0f, 36.0f, 15, 44.0f, 48.0f, 15),
      new StatsSlotLayout(16, 64.0f, 33.0f, 66.0f, 62.0f, 31.0f, 92.0f, 31.0f, 68.0f, 36.0f, 96.0f, 36.0f, 15, 108.0f, 48.0f, 15),
      new StatsSlotLayout(13, 128.0f, 33.0f, 82.0f, 125.0f, 31.0f, 155.0f, 31.0f, 128.0f, 36.0f, 160.0f, 36.0f, 15, 172.0f, 48.0f, 15),
      new StatsSlotLayout(1, 0.0f, 64.0f, 78.0f, -1.0f, 62.0f, 29.0f, 62.0f, 4.0f, 67.0f, 32.0f, 67.0f, 15, 44.0f, 79.0f, 15),
      new StatsSlotLayout(15, 64.0f, 64.0f, 78.0f, 62.0f, 62.0f, 92.0f, 62.0f, 68.0f, 73.0f, 96.0f, 67.0f, 15, 108.0f, 79.0f, 15),
      new StatsSlotLayout(10, 128.0f, 64.0f, 70.0f, 125.0f, 62.0f, 155.0f, 62.0f, 128.0f, 67.0f, 160.0f, 67.0f, 15, 172.0f, 79.0f, 15),
      new StatsSlotLayout(4, 0.0f, 95.0f, 75.0f, -1.0f, 93.0f, 29.0f, 93.0f, 5.0f, 98.0f, 33.0f, 98.0f, 15, 45.0f, 110.0f, 15),
      new StatsSlotLayout(17, 64.0f, 95.0f, 78.0f, 62.0f, 93.0f, 92.0f, 93.0f, 69.0f, 98.0f, 97.0f, 98.0f, 15, 109.0f, 110.0f, 15),
      new StatsSlotLayout(7, 128.0f, 95.0f, 78.0f, 125.0f, 93.0f, 155.0f, 93.0f, 129.0f, 98.0f, 160.0f, 98.0f, 15, 172.0f, 110.0f, 15),
      new StatsSlotLayout(5, 0.0f, 126.0f, 66.0f, -1.0f, 124.0f, 29.0f, 124.0f, 5.0f, 129.0f, 33.0f, 129.0f, 15, 45.0f, 141.0f, 15),
      new StatsSlotLayout(12, 64.0f, 126.0f, 74.0f, 62.0f, 124.0f, 92.0f, 124.0f, 69.0f, 129.0f, 96.0f, 130.0f, 15, 109.0f, 141.0f, 15),
      new StatsSlotLayout(11, 128.0f, 126.0f, 94.0f, 125.0f, 124.0f, 155.0f, 124.0f, 129.0f, 129.0f, 161.0f, 129.0f, 15, 172.0f, 141.0f, 15),
      new StatsSlotLayout(6, 0.0f, 157.0f, 62.0f, -1.0f, 155.0f, 29.0f, 155.0f, 5.0f, 160.0f, 33.0f, 160.0f, 15, 45.0f, 172.0f, 15),
      new StatsSlotLayout(9, 64.0f, 157.0f, 82.0f, 62.0f, 155.0f, 92.0f, 155.0f, 69.0f, 160.0f, 97.0f, 160.0f, 15, 109.0f, 172.0f, 15),
      new StatsSlotLayout(8, 128.0f, 157.0f, 102.0f, 125.0f, 155.0f, 155.0f, 155.0f, 129.0f, 160.0f, 161.0f, 160.0f, 15, 172.0f, 172.0f, 15),
      new StatsSlotLayout(20, 0.0f, 188.0f, 87.0f, -1.0f, 186.0f, 29.0f, 186.0f, 5.0f, 191.0f, 33.0f, 191.0f, 15, 45.0f, 203.0f, 15),
      new StatsSlotLayout(18, 64.0f, 188.0f, 66.0f, 62.0f, 186.0f, 92.0f, 186.0f, 66.0f, 191.0f, 97.0f, 191.0f, 15, 109.0f, 202.0f, 15),
      new StatsSlotLayout(19, 128.0f, 188.0f, 77.0f, 125.0f, 186.0f, 155.0f, 186.0f, 131.0f, 191.0f, 161.0f, 192.0f, 15, 172.0f, 204.0f, 15)
  };

  private GameplayStatsTabLayout() {
  }

  static StatsSlotLayout[] slots() {
    return SLOTS.clone();
  }

  static StatsSlotLayout hoveredSlot(float localX, float localY) {
    for (StatsSlotLayout slot : SLOTS) {
      if (slot.contains(localX, localY)) {
        return slot;
      }
    }
    return null;
  }

  record StatsSlotLayout(
      int skillId,
      float hoverX,
      float hoverY,
      float hoverFirstValueX,
      float buttonLeftX,
      float buttonLeftY,
      float buttonRightX,
      float buttonRightY,
      float iconX,
      float iconY,
      float currentTextX,
      float currentTextY,
      int currentTextWidth,
      float baseTextX,
      float baseTextY,
      int baseTextWidth
  ) {

    private boolean contains(float localX, float localY) {
      return localX >= hoverX && localX < hoverX + 64.0f
          && localY >= hoverY && localY < hoverY + 31.0f;
    }
  }
}
