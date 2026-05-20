package io.github.ffakira.rsps.client.desktop.itemicon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ItemIconRasterizerTest {

  @Test
  void appliesClassicOutlineAndShadowPostProcess() {
    int[] pixels = new int[32 * 32];
    int centerIndex = (16 * 32) + 16;
    pixels[centerIndex] = 0xff884422;

    ItemIconRasterizer.applyInventoryPostProcess(pixels);

    assertThat(pixels[(16 * 32) + 15]).isEqualTo(0xff000001);
    assertThat(pixels[((16 + 1) * 32) + (16 + 1)]).isEqualTo(0xff302020);
  }

  @Test
  void appliesClassicOutlineOnCanvasEdges() {
    int[] pixels = new int[32 * 32];
    pixels[(1 * 32) + 0] = 0xff884422;

    ItemIconRasterizer.applyInventoryPostProcess(pixels);

    assertThat(pixels[0]).isEqualTo(0xff000001);
  }
}
