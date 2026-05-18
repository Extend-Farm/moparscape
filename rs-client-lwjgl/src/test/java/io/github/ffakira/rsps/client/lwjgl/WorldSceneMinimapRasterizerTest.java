package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneMinimapRasterizerTest {

  @Test
  void rasterizesWallsAndAreaObjectsFromSceneMetadata() {
    int[] tileColors = new int[16];
    java.util.Arrays.fill(tileColors, 0x2f3946);
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        4,
        4,
        tileColors,
        tileColors,
        new int[16],
        new byte[16],
        new byte[16],
        List.of(
            new WorldSceneObject(1, "Stone wall", 1, 1, 0, 0, 0, 1, 1, List.of(), null),
            new WorldSceneObject(2, "Oak tree", 2, 2, 0, 10, 0, 1, 1, List.of(), null)
        )
    );

    assertThat(image.width()).isEqualTo(16);
    assertThat(image.height()).isEqualTo(16);
    assertThat(image.pixels()[pixelIndex(16, 4, 9)]).isEqualTo(0xff4c3f35);
    assertThat(image.pixels()[pixelIndex(16, 9, 5)]).isEqualTo(0xff436c33);
  }

  @Test
  void rasterizesShapedTilesUsingLegacyFourByFourMasks() {
    int[] tileColors = new int[]{0x2f3946};
    int[] underlayColors = new int[]{0x654d24};
    int[] overlayColors = new int[]{0x97a34a};
    byte[] overlayShapes = new byte[]{2};
    byte[] overlayRotations = new byte[]{0};
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        tileColors,
        underlayColors,
        overlayColors,
        overlayShapes,
        overlayRotations,
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isEqualTo(0xff8a9644);
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isEqualTo(0xff5d4721);
  }

  private int pixelIndex(int width, int pixelX, int pixelY) {
    return pixelY * width + pixelX;
  }
}
