package io.github.ffakira.rsps.client.desktop.world.minimap;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneMinimapRasterizerTest {

  @Test
  void rasterizesWallsFromSceneMetadata() {
    int[] tileColors = new int[16];
    java.util.Arrays.fill(tileColors, 0x2f3946);
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        4,
        4,
        new int[16],
        tileColors,
        tileColors,
        new int[16],
        new int[16],
        new int[16],
        new byte[16],
        new byte[16],
        new byte[16],
        List.of(
            new WorldSceneObject(1, "Stone wall", 1, 1, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null)
        )
    );

    assertThat(image.width()).isEqualTo(16);
    assertThat(image.height()).isEqualTo(16);
    assertThat(image.pixels()[pixelIndex(16, 4, 9)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(16, 9, 5)]).isNotEqualTo(0xff5c4a34);
  }

  @Test
  void rasterizesShapedTilesUsingLegacyFourByFourMasks() {
    int[] tileColors = new int[]{0x2f3946};
    int[] underlayColors = new int[]{0x654d24};
    int[] overlayColors = new int[]{0x97a34a};
    int[] underlayTextureIds = new int[]{-1};
    int[] overlayTextureIds = new int[]{-1};
    byte[] overlayShapes = new byte[]{2};
    byte[] overlayRotations = new byte[]{0};
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        new byte[1],
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isNotEqualTo(image.pixels()[pixelIndex(4, 2, 0)]);
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isNotEqualTo(0);
    assertThat(image.pixels()[pixelIndex(4, 2, 0)]).isNotEqualTo(0);
  }

  @Test
  void promotesRawOverlayTypeOneIntoTheFirstCurvedMinimapMask() {
    int[] tileColors = new int[]{0x2f3946};
    int[] underlayColors = new int[]{0x654d24};
    int[] overlayColors = new int[]{0x97a34a};
    int[] underlayTextureIds = new int[]{-1};
    int[] overlayTextureIds = new int[]{-1};
    byte[] overlayShapes = new byte[]{1};
    byte[] overlayRotations = new byte[]{0};
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        new byte[1],
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isNotEqualTo(image.pixels()[pixelIndex(4, 1, 0)]);
    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isNotEqualTo(0);
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isNotEqualTo(0);
  }

  @Test
  void leavesNonOverlayPixelsUntouchedForOverlayOnlyShapedTiles() {
    int[] tileColors = new int[]{0x2f3946};
    int[] underlayColors = new int[]{0};
    int[] overlayColors = new int[]{0x97a34a};
    int[] underlayTextureIds = new int[]{-1};
    int[] overlayTextureIds = new int[]{-1};
    byte[] overlayShapes = new byte[]{1};
    byte[] overlayRotations = new byte[]{0};
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        tileColors,
        underlayColors,
        overlayColors,
        underlayTextureIds,
        overlayTextureIds,
        overlayShapes,
        overlayRotations,
        new byte[1],
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isNotZero();
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isNotZero();
  }

  @Test
  void stampsMapSceneSpritesInsteadOfGenericAreaObjectFootprints() {
    int[] tileColors = new int[16];
    java.util.Arrays.fill(tileColors, 0x2f3946);
    ArgbImage[] mapSceneSprites = new ArgbImage[1];
    mapSceneSprites[0] = new ArgbImage(
        4,
        4,
        new int[]{
            0, 0xff8f5a32, 0xff8f5a32, 0,
            0xfff7f6f0, 0xff8f5a32, 0xff8f5a32, 0xfff7f6f0,
            0xfff7f6f0, 0xff8f5a32, 0xff8f5a32, 0xfff7f6f0,
            0, 0xfff7f6f0, 0xfff7f6f0, 0
        }
    );
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        4,
        4,
        new int[16],
        tileColors,
        tileColors,
        new int[16],
        new int[16],
        new int[16],
        new byte[16],
        new byte[16],
        new byte[16],
        List.of(
            new WorldSceneObject(100, "Small house", 1, 1, 0, 10, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()[pixelIndex(16, 5, 8)]).isEqualTo(0xff8f5a32);
    assertThat(image.pixels()[pixelIndex(16, 4, 9)]).isEqualTo(0xfff7f6f0);
  }

  @Test
  void usesWaterTintForLegacyWaterTextureTilesWithoutOverlayRgb() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0x2f3946},
        new int[]{0x644e1e},
        new int[]{0},
        new int[]{-1},
        new int[]{1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of()
    );

    int waterPixel = image.pixels()[pixelIndex(4, 1, 1)];
    assertThat((waterPixel >>> 24) & 0xff).isEqualTo(0xff);
    assertThat(waterPixel & 0x00ffffff).isNotEqualTo(0x2f3946);
    assertThat(waterPixel & 0xff).isGreaterThan(((waterPixel >>> 16) & 0xff));
  }

  @Test
  void fillsPaintTilesAsSolidFourByFourBlocks() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        2,
        2,
        new int[4],
        new int[]{
            0x385f24, 0x7fa34d,
            0x6c4f1f, 0x9dbb61
        },
        new int[]{
            0x385f24, 0x7fa34d,
            0x6c4f1f, 0x9dbb61
        },
        new int[4],
        new int[]{-1, -1, -1, -1},
        new int[]{-1, -1, -1, -1},
        new byte[4],
        new byte[4],
        new byte[4],
        List.of()
    );

    int tileOneTopLeft = image.pixels()[pixelIndex(8, 0, 0)];
    int tileOneBottomRight = image.pixels()[pixelIndex(8, 3, 3)];
    int tileTwoTopLeft = image.pixels()[pixelIndex(8, 4, 0)];
    assertThat(tileOneTopLeft).isEqualTo(tileOneBottomRight);
    assertThat(tileOneTopLeft).isNotEqualTo(tileTwoTopLeft);
  }

  private int pixelIndex(int width, int pixelX, int pixelY) {
    return pixelY * width + pixelX;
  }
}
