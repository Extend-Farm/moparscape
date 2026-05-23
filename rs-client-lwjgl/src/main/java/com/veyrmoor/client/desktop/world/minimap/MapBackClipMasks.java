package com.veyrmoor.client.desktop.world.minimap;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;

public final class MapBackClipMasks {

  public static final int COMPASS_WIDTH = 33;
  public static final int COMPASS_HEIGHT = 33;
  public static final int MINIMAP_WIDTH = 146;
  public static final int MINIMAP_HEIGHT = 151;

  private final int[] compassRowStarts;
  private final int[] compassRowWidths;
  private final int[] minimapRowStarts;
  private final int[] minimapRowWidths;

  private MapBackClipMasks(
      int[] compassRowStarts,
      int[] compassRowWidths,
      int[] minimapRowStarts,
      int[] minimapRowWidths
  ) {
    this.compassRowStarts = compassRowStarts;
    this.compassRowWidths = compassRowWidths;
    this.minimapRowStarts = minimapRowStarts;
    this.minimapRowWidths = minimapRowWidths;
  }

  public static MapBackClipMasks fromMapBack(ArgbImage mapBack) {
    // The original frame shell uses the transparent compass cutout from `mapback` as a per-row
    // clip mask. Preserving those scanlines keeps the native compass inside the same silhouette
    // instead of treating it like a generic circular sprite.
    int[] compassRowStarts = new int[COMPASS_HEIGHT];
    int[] compassRowWidths = new int[COMPASS_HEIGHT];
    for (int row = 0; row < COMPASS_HEIGHT; row++) {
      int start = -1;
      int end = COMPASS_WIDTH + 1;
      for (int column = 0; column <= COMPASS_WIDTH; column++) {
        if (isTransparent(mapBack, column, row)) {
          if (start == -1) {
            start = column;
          }
          continue;
        }
        if (start != -1) {
          end = column;
          break;
        }
      }
      compassRowStarts[row] = Math.max(0, start);
      compassRowWidths[row] = start == -1 ? 0 : Math.max(0, end - start);
    }
    int[] minimapRowStarts = new int[MINIMAP_HEIGHT];
    int[] minimapRowWidths = new int[MINIMAP_HEIGHT];
    for (int row = 5; row < 156; row++) {
      int start = -1;
      int end = 172;
      for (int column = 25; column < 172; column++) {
        if (isTransparent(mapBack, column, row) && (column > 34 || row > 34)) {
          if (start == -1) {
            start = column;
          }
          continue;
        }
        if (start == -1) {
          continue;
        }
        end = column;
        break;
      }
      minimapRowStarts[row - 5] = start == -1 ? 0 : Math.max(0, start - 25);
      minimapRowWidths[row - 5] = start == -1 ? 0 : Math.max(0, end - start);
    }
    return new MapBackClipMasks(compassRowStarts, compassRowWidths, minimapRowStarts, minimapRowWidths);
  }

  public int[] compassRowStarts() {
    return compassRowStarts;
  }

  public int[] compassRowWidths() {
    return compassRowWidths;
  }

  public int[] minimapRowStarts() {
    return minimapRowStarts;
  }

  public int[] minimapRowWidths() {
    return minimapRowWidths;
  }

  private static boolean isTransparent(ArgbImage image, int x, int y) {
    if (x < 0 || y < 0 || x >= image.width() || y >= image.height()) {
      return false;
    }
    return ((image.pixels()[x + y * image.width()] >>> 24) & 0xff) == 0;
  }
}
