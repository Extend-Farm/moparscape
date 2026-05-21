package io.github.ffakira.rsps.client.desktop.world.minimap;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder.IndexedArgbSprite;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
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
  void rasterizesOrientationOneWallsOnTheTopEdge() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(new WorldSceneObject(1, "North wall", 0, 0, 0, 0, 1, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 3, 0)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 0, 3)]).isZero();
  }

  @Test
  void rasterizesOrientationThreeWallsOnTheBottomEdge() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(new WorldSceneObject(1, "South wall", 0, 0, 0, 0, 3, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 3)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isZero();
  }

  @Test
  void rasterizesCornerWallsAsTwoUndimmedLegs() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(new WorldSceneObject(1, "Corner wall", 0, 0, 0, 2, 0, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 0, 1)]).isEqualTo(0xffeeeeee);
  }

  @Test
  void rasterizesCornerCapsAsSinglePixels() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(new WorldSceneObject(1, "Corner cap", 0, 0, 0, 3, 2, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isEqualTo(0xffeeeeee);
    assertThat(java.util.Arrays.stream(image.pixels()).filter(pixel -> pixel != 0).toArray())
        .containsExactly(0xffeeeeee);
  }

  @Test
  void skipsTypeOneBoundaryWallsWithoutMapScene() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(new WorldSceneObject(1, "Diagonal boundary", 0, 0, 0, 1, 0, 1, 1, false, -1, -1, List.of(), true, null))
    );

    assertThat(image.pixels()).containsOnly(0);
  }

  @Test
  void rasterizesInteractiveDoorsAndDiagonalsInLegacyRed() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        2,
        1,
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{0, 0},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new byte[]{0, 0},
        new byte[]{0, 0},
        new byte[]{0, 0},
        List.of(
            new WorldSceneObject(1, "Door", 0, 0, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, true, true, true, null),
            new WorldSceneObject(2, "Diagonal gate", 1, 0, 0, 9, 1, 1, 1, false, -1, -1, List.of(), true, true, true, true, null)
        )
    );

    assertThat(image.pixels()[pixelIndex(8, 0, 0)]).isEqualTo(0xffee0000);
    assertThat(image.pixels()[pixelIndex(8, 4, 0)]).isEqualTo(0xffee0000);
    assertThat(image.pixels()[pixelIndex(8, 7, 3)]).isEqualTo(0xffee0000);
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
  void leavesUnderlayHalfUntouchedWhenShapedTileHasUnderlayIdButZeroBlendedColor() {
    // Mirrors legacy SceneTileModel.anInt686 == 0: tile has a non-zero underlay id, but the HSL
    // underlay blend produced no color (so the underlay surface is hidden). Legacy method309 then
    // only writes overlay-mask pixels; the underlay-mask pixels keep whatever was already there.
    int[] underlayIds = new int[]{1};
    int[] overlayIds = new int[]{0};
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
        underlayIds,
        overlayIds,
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

    // Scene shape id = overlayShape + 1 = 2. Mask row 0 = {1,0,0,0}; row 3 = {1,1,1,1}. Underlay
    // pixels (mask == 0) must stay zero; overlay pixels (mask == 1) must carry the overlay color.
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 2, 0)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 3, 0)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isNotZero();
    assertThat(image.pixels()[pixelIndex(4, 0, 3)]).isNotZero();
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isNotZero();
  }

  @Test
  void leavesFlatTilesUntouchedWhenARawOverlayBranchResolvesHidden() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{1},
        new int[]{4},
        new int[]{0x2f3946},
        new int[]{0x654d24},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()).containsOnly(0);
  }

  @Test
  void usesTileBaseColorForFlatOverlayPaintInsteadOfSurfaceOverlayColor() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{1},
        new int[]{4},
        new int[]{0x4f82c8},
        new int[]{0x654d24},
        new int[]{0x8a5b2f},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()).containsOnly(0xff4f82c8);
  }

  @Test
  void leavesHiddenOverlayMaskPixelsUntouchedForShapedTiles() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{1},
        new int[]{4},
        new int[]{0x2f3946},
        new int[]{0x654d24},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{1},
        new byte[]{0},
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isZero();
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isNotZero();
  }

  @Test
  void usesTileBaseColorForShapedOverlayPixelsInsteadOfSurfaceOverlayColor() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{1},
        new int[]{4},
        new int[]{0x4f82c8},
        new int[]{0x654d24},
        new int[]{0x8a5b2f},
        new int[]{-1},
        new int[]{-1},
        new byte[]{1},
        new byte[]{0},
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isEqualTo(0xff4f82c8);
    assertThat(image.pixels()[pixelIndex(4, 1, 0)]).isEqualTo(0xff654d24);
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
  void centersRotatedMapSceneSpritesUsingDefinitionFootprintInsteadOfSwappedFootprint() {
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(
            8,
            4,
            filledPixels(8 * 4, 0xff8f5a32)
        )
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        4,
        4,
        new int[16],
        new int[16],
        new int[16],
        new int[16],
        filledInts(16, -1),
        filledInts(16, -1),
        new byte[16],
        new byte[16],
        new byte[16],
        List.of(
            // sizeX/sizeY here are the oriented footprint (1x2). Legacy method50 centers the
            // sprite using the raw definition footprint (2x1), so the sprite must sit one tile
            // wider and one tile lower than the swapped-footprint placement would.
            new WorldSceneObject(100, "Rotated house", 1, 1, 0, 10, 1, 1, 2, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()[pixelIndex(16, 4, 8)]).isEqualTo(0xff8f5a32);
    assertThat(image.pixels()[pixelIndex(16, 11, 11)]).isEqualTo(0xff8f5a32);
    assertThat(image.pixels()[pixelIndex(16, 3, 7)]).isZero();
  }

  @Test
  void placesMapSceneSpritesIndependentlyOfObjectOrientation() {
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(
            8,
            4,
            filledPixels(8 * 4, 0xff8f5a32)
        )
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage orientationZero = rasterizer.rasterize(
        4,
        4,
        new int[16],
        new int[16],
        new int[16],
        new int[16],
        filledInts(16, -1),
        filledInts(16, -1),
        new byte[16],
        new byte[16],
        new byte[16],
        List.of(
            new WorldSceneObject(100, "House", 1, 1, 0, 10, 0, 2, 1, false, 0, -1, List.of(), true, null)
        )
    );

    ArgbImage orientationOne = rasterizer.rasterize(
        4,
        4,
        new int[16],
        new int[16],
        new int[16],
        new int[16],
        filledInts(16, -1),
        filledInts(16, -1),
        new byte[16],
        new byte[16],
        new byte[16],
        List.of(
            new WorldSceneObject(100, "House", 1, 1, 0, 10, 1, 1, 2, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(orientationOne.pixels()).containsExactly(orientationZero.pixels());
  }

  @Test
  void centersMapSceneSpritesUsingTrimmedBoundsAndDrawOffsets() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(
        new IndexedArgbSprite[]{
            new IndexedArgbSprite(
                1,
                1,
                2,
                2,
                new int[]{
                    0xff8f5a32, 0xff8f5a32,
                    0xff8f5a32, 0xff8f5a32
                }
            )
        }
    );

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(100, "Offset icon", 0, 0, 0, 10, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()[pixelIndex(4, 2, 2)]).isEqualTo(0xff8f5a32);
    assertThat(image.pixels()[pixelIndex(4, 3, 3)]).isEqualTo(0xff8f5a32);
    assertThat(image.pixels()[pixelIndex(4, 1, 1)]).isZero();
  }

  @Test
  void drawsInteractiveMapSceneAfterBoundaryWallsRegardlessOfInputOrder() {
    int[] tileColors = new int[]{0x2f3946};
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(
            4,
            4,
            new int[]{
                0xff7c673e, 0xff7c673e, 0xff7c673e, 0xff7c673e,
                0xff7c673e, 0xff7c673e, 0xff7c673e, 0xff7c673e,
                0xff7c673e, 0xff7c673e, 0xff7c673e, 0xff7c673e,
                0xff7c673e, 0xff7c673e, 0xff7c673e, 0xff7c673e
            }
        )
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        tileColors,
        tileColors,
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(100, "House", 0, 0, 0, 10, 0, 1, 1, false, 0, -1, List.of(), true, null),
            new WorldSceneObject(1, "Stone wall", 0, 0, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()).containsOnly(0xff7c673e);
  }

  @Test
  void drawsGroundDecorationMapSceneAfterInteractiveDiagonalRegardlessOfInputOrder() {
    int[] tileColors = new int[]{0x2f3946};
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(
            4,
            4,
            new int[]{
                0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0,
                0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0,
                0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0,
                0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0, 0xfff7f6f0
            }
        )
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        tileColors,
        tileColors,
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(100, "Statue", 0, 0, 0, 22, 0, 1, 1, false, 0, -1, List.of(), true, null),
            new WorldSceneObject(1, "Diagonal wall", 0, 0, 0, 9, 0, 1, 1, false, -1, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()).containsOnly(0xfff7f6f0);
  }

  @Test
  void ignoresWallDecorationMapSceneSprites() {
    int baseRgb = 0x2f3946;
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(
            4,
            4,
            new int[]{
                0xff8f5a32, 0xff8f5a32, 0xff8f5a32, 0xff8f5a32,
                0xff8f5a32, 0xff8f5a32, 0xff8f5a32, 0xff8f5a32,
                0xff8f5a32, 0xff8f5a32, 0xff8f5a32, 0xff8f5a32,
                0xff8f5a32, 0xff8f5a32, 0xff8f5a32, 0xff8f5a32
            }
        )
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{baseRgb},
        new int[]{baseRgb},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(1, "Banner", 0, 0, 0, 4, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()).containsOnly(0xff2f3946);
  }

  @Test
  void drawsInteractiveMapSceneOverBoundaryMapSceneRegardlessOfInputOrder() {
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(4, 4, filledPixels(16, 0xff8f5a32)),
        new ArgbImage(4, 4, filledPixels(16, 0xfff7f6f0))
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(200, "House", 0, 0, 0, 10, 0, 1, 1, false, 1, -1, List.of(), true, null),
            new WorldSceneObject(100, "Gate", 0, 0, 0, 0, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()).containsOnly(0xfff7f6f0);
  }

  @Test
  void drawsGroundDecorationMapSceneOverInteractiveMapSceneRegardlessOfInputOrder() {
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(4, 4, filledPixels(16, 0xff8f5a32)),
        new ArgbImage(4, 4, filledPixels(16, 0xfff7f6f0))
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(100, "Statue", 0, 0, 0, 22, 0, 1, 1, false, 1, -1, List.of(), true, null),
            new WorldSceneObject(200, "House", 0, 0, 0, 10, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()).containsOnly(0xfff7f6f0);
  }

  @Test
  void drawsInteractiveDiagonalOverBoundaryMapSceneRegardlessOfInputOrder() {
    ArgbImage[] mapSceneSprites = new ArgbImage[]{
        new ArgbImage(4, 4, filledPixels(16, 0xff8f5a32))
    };
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer(mapSceneSprites);

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        List.of(
            new WorldSceneObject(100, "Diagonal wall", 0, 0, 0, 9, 0, 1, 1, false, -1, -1, List.of(), true, null),
            new WorldSceneObject(200, "Gate", 0, 0, 0, 0, 0, 1, 1, false, 0, -1, List.of(), true, null)
        )
    );

    assertThat(image.pixels()[pixelIndex(4, 3, 0)]).isEqualTo(0xffeeeeee);
    assertThat(image.pixels()[pixelIndex(4, 0, 0)]).isEqualTo(0xff8f5a32);
  }

  @Test
  void usesWaterTintForLegacyWaterTextureTilesWithoutOverlayRgb() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0x5a7ea3},
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
    assertThat(waterPixel & 0x00ffffff).isEqualTo(0x5a7ea3);
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

  @Test
  void skipsBasePlanePaintWhenLegacyTileFlagsBlockTheCurrentPlane() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{0x385f24},
        new int[]{0x385f24},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0x18},
        new byte[]{0},
        BridgeTerrainLayer.empty(1, 1),
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()).containsOnly(0);
  }

  @Test
  void paintsBridgeLoweredTilesFromTheVisibleSurfacePlane() {
    WorldSceneMinimapRasterizer rasterizer = new WorldSceneMinimapRasterizer();
    int bridgeDeckRgb = 0x7c673e;
    int underBridgeWaterRgb = 0x576a92;
    BridgeTerrainLayer bridgeLowerLayer = new BridgeTerrainLayer(
        1,
        1,
        new int[]{0},
        new int[]{0},
        new int[]{1},
        new int[]{underBridgeWaterRgb},
        new int[]{underBridgeWaterRgb},
        new int[]{underBridgeWaterRgb},
        new int[]{-1},
        new int[]{1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{1}
    );

    ArgbImage image = rasterizer.rasterize(
        1,
        1,
        new int[]{0},
        new int[]{bridgeDeckRgb},
        new int[]{bridgeDeckRgb},
        new int[]{0},
        new int[]{-1},
        new int[]{-1},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        new byte[]{0},
        bridgeLowerLayer,
        new byte[]{0},
        List.of()
    );

    assertThat(image.pixels()).containsOnly(0xff000000 | bridgeDeckRgb);
  }

  private int pixelIndex(int width, int pixelX, int pixelY) {
    return pixelY * width + pixelX;
  }

  private static int[] filledPixels(int count, int argb) {
    int[] pixels = new int[count];
    java.util.Arrays.fill(pixels, argb);
    return pixels;
  }

  private static int[] filledInts(int count, int value) {
    int[] values = new int[count];
    java.util.Arrays.fill(values, value);
    return values;
  }
}
