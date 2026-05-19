package io.github.ffakira.rsps.client.desktop.character;

final class EquipmentVisibilityRules {

  private static final int[] PLATE_BODIES = {
      1035, 540, 5553, 4757, 1833, 1835, 6388, 6384, 2503, 2501, 2499, 1355, 4111, 4101,
      4091, 6186, 6184, 6180, 3058, 4509, 4504, 4069, 4728, 4736, 4712, 6107, 2661, 3140,
      1115, 1117, 1119, 1121, 1123, 1125, 1127, 2583, 2591, 2599, 2607, 2615, 2623, 2653,
      2669, 3481, 4720, 4728, 4749, 2661
  };

  private static final int[] FULL_HELMS = {
      4732, 4753, 6188, 4511, 4056, 4071, 4724, 6109, 2665, 1153, 1155, 1157, 1159, 1161,
      1163, 1165, 2587, 2595, 2605, 2613, 2619, 2627, 2657, 2673, 3486, 6402, 6394
  };

  private static final int[] FULL_MASKS = {
      4732, 5554, 4753, 4611, 6188, 3507, 4511, 4056, 4071, 4724, 2665, 6109, 1053, 1055,
      1057
  };

  private EquipmentVisibilityRules() {
  }

  static boolean isPlateBody(int itemId) {
    return contains(PLATE_BODIES, itemId);
  }

  static boolean isFullHelm(int itemId) {
    return contains(FULL_HELMS, itemId);
  }

  static boolean isFullMask(int itemId) {
    return contains(FULL_MASKS, itemId);
  }

  private static boolean contains(int[] values, int needle) {
    for (int value : values) {
      if (value == needle) {
        return true;
      }
    }
    return false;
  }
}
