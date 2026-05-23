package com.veyrmoor.client.desktop.itemicon;

final class IconRenderConstants {

  static final int ICON_SIZE = 32;
  static final int PIXEL_COUNT = ICON_SIZE * ICON_SIZE;
  static final int MAX_PIXEL_INDEX = ICON_SIZE - 1;
  static final float ICON_CENTER_X = ICON_SIZE * 0.5f;
  static final float ICON_CENTER_Y = ICON_SIZE * 0.5f;
  static final float INVENTORY_PROJECTION_SCALE = 512.0f;
  static final float NEAR_PLANE = 1.0f;
  static final float EDGE_EPSILON = 0.0001f;
  static final int OUTLINE_RGB = 0x000001;
  static final int SHADOW_RGB = 0x302020;
  static final int[] RECIPROCAL_512 = buildReciprocalTable(512, 32768);
  static final int[] ROW_OFFSETS = buildRowOffsets();

  private IconRenderConstants() {
  }

  private static int[] buildReciprocalTable(int size, int numerator) {
    int[] table = new int[size];
    for (int index = 1; index < table.length; index++) {
      table[index] = numerator / index;
    }
    return table;
  }

  private static int[] buildRowOffsets() {
    int[] rowOffsets = new int[ICON_SIZE];
    for (int row = 0; row < ICON_SIZE; row++) {
      rowOffsets[row] = row * ICON_SIZE;
    }
    return rowOffsets;
  }
}
