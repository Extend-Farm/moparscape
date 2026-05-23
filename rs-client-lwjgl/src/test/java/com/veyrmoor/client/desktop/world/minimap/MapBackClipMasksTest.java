package com.veyrmoor.client.desktop.world.minimap;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import org.junit.jupiter.api.Test;

class MapBackClipMasksTest {

  @Test
  void extractsCompassClipSpansFromTransparentMapBackRows() {
    int width = 200;
    int height = 200;
    int[] pixels = new int[width * height];
    for (int index = 0; index < pixels.length; index++) {
      pixels[index] = 0xff221c16;
    }
    paintTransparentSpan(pixels, width, 0, 3, 7);
    paintTransparentSpan(pixels, width, 1, 1, 5);
    paintTransparentSpan(pixels, width, 2, 10, 20);
    paintTransparentSpan(pixels, width, 35, 40, 90);
    paintTransparentSpan(pixels, width, 80, 55, 145);

    MapBackClipMasks clipMasks = MapBackClipMasks.fromMapBack(new ArgbImage(width, height, pixels));

    assertThat(clipMasks.compassRowStarts()[0]).isEqualTo(3);
    assertThat(clipMasks.compassRowWidths()[0]).isEqualTo(4);
    assertThat(clipMasks.compassRowStarts()[1]).isEqualTo(1);
    assertThat(clipMasks.compassRowWidths()[1]).isEqualTo(4);
    assertThat(clipMasks.compassRowStarts()[2]).isEqualTo(10);
    assertThat(clipMasks.compassRowWidths()[2]).isEqualTo(10);
    assertThat(clipMasks.compassRowWidths()[3]).isZero();
    assertThat(clipMasks.minimapRowStarts()[30]).isEqualTo(15);
    assertThat(clipMasks.minimapRowWidths()[30]).isEqualTo(50);
    assertThat(clipMasks.minimapRowStarts()[75]).isEqualTo(30);
    assertThat(clipMasks.minimapRowWidths()[75]).isEqualTo(90);
  }

  private static void paintTransparentSpan(int[] pixels, int width, int row, int startColumn, int endColumnExclusive) {
    for (int column = startColumn; column < endColumnExclusive; column++) {
      pixels[column + row * width] = 0;
    }
  }
}
