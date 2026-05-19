package io.github.ffakira.rsps.client.desktop.login;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import java.util.Random;

public final class TitleScreenFlameAnimator {

  private static final int PANEL_WIDTH = 128;
  private static final int PANEL_HEIGHT = 265;
  private static final int FLAME_WIDTH = 128;
  private static final int FLAME_HEIGHT = 256;

  private final Random random = new Random();
  private final TitleScreenRuneMask[] runeMasks;
  private final int[] leftBasePixels;
  private final int[] rightBasePixels;
  private final int[] primaryPalette = new int[256];
  private final int[] secondaryPalette = new int[256];
  private final int[] tertiaryPalette = new int[256];
  private final int[] activePalette = new int[256];
  private final int[] noise = new int[32768];
  private final int[] noiseBuffer = new int[32768];
  private final int[] flame = new int[32768];
  private final int[] flameBuffer = new int[32768];
  private final int[] lineOffsets = new int[256];

  private int paletteBlendToSecondary;
  private int paletteBlendToTertiary;
  private int noiseOffset;
  private int tick;

  public TitleScreenFlameAnimator(TitleScreenRuneMask[] runeMasks, ArgbImage leftBase, ArgbImage rightBase) {
    this.runeMasks = runeMasks;
    this.leftBasePixels = leftBase.pixels().clone();
    this.rightBasePixels = rightBase.pixels().clone();
    initializePalettes();
    seedNoise(null);
  }

  public TitleScreenFlameFrame renderNextFrame() {
    step();
    step();
    updatePalette();
    return new TitleScreenFlameFrame(
        new ArgbImage(PANEL_WIDTH, PANEL_HEIGHT, renderLeftPanel()),
        new ArgbImage(PANEL_WIDTH, PANEL_HEIGHT, renderRightPanel())
    );
  }

  private void initializePalettes() {
    for (int index = 0; index < 64; index++) {
      primaryPalette[index] = index * 0x40000;
      primaryPalette[index + 64] = 0xff0000 + 1024 * index;
      primaryPalette[index + 128] = 0xffff00 + 4 * index;
      primaryPalette[index + 192] = 0xffffff;

      secondaryPalette[index] = index * 1024;
      secondaryPalette[index + 64] = 0x00ff00 + 4 * index;
      secondaryPalette[index + 128] = 0x00ffff + 0x40000 * index;
      secondaryPalette[index + 192] = 0xffffff;

      tertiaryPalette[index] = index * 4;
      tertiaryPalette[index + 64] = 0x0000ff + 0x40000 * index;
      tertiaryPalette[index + 128] = 0xff00ff + 1024 * index;
      tertiaryPalette[index + 192] = 0xffffff;
    }
  }

  private void step() {
    for (int column = 10; column < 117; column++) {
      if (random.nextInt(100) < 50) {
        flame[column + ((FLAME_HEIGHT - 2) << 7)] = 255;
      }
    }

    for (int sample = 0; sample < 100; sample++) {
      int x = random.nextInt(124) + 2;
      int y = random.nextInt(128) + 128;
      flame[x + (y << 7)] = 192;
    }

    for (int y = 1; y < FLAME_HEIGHT - 1; y++) {
      for (int x = 1; x < FLAME_WIDTH - 1; x++) {
        int index = x + (y << 7);
        flameBuffer[index] =
            (flame[index - 1] + flame[index + 1] + flame[index - 128] + flame[index + 128]) / 4;
      }
    }

    noiseOffset += 128;
    if (noiseOffset > noise.length) {
      noiseOffset -= noise.length;
      seedNoise(runeMasks[random.nextInt(runeMasks.length)]);
    }

    for (int y = 1; y < FLAME_HEIGHT - 1; y++) {
      for (int x = 1; x < FLAME_WIDTH - 1; x++) {
        int index = x + (y << 7);
        int value = flameBuffer[index + 128] - noise[(index + noiseOffset) & (noise.length - 1)] / 5;
        flame[index] = Math.max(value, 0);
      }
    }

    System.arraycopy(lineOffsets, 1, lineOffsets, 0, lineOffsets.length - 1);
    lineOffsets[lineOffsets.length - 1] =
        (int) (
            Math.sin(tick / 14.0D) * 16.0D
                + Math.sin(tick / 15.0D) * 14.0D
                + Math.sin(tick / 16.0D) * 12.0D
        );

    if (paletteBlendToSecondary > 0) {
      paletteBlendToSecondary -= 4;
    }
    if (paletteBlendToTertiary > 0) {
      paletteBlendToTertiary -= 4;
    }
    if (paletteBlendToSecondary == 0 && paletteBlendToTertiary == 0) {
      int chance = random.nextInt(2000);
      if (chance == 0) {
        paletteBlendToSecondary = 1024;
      }
      if (chance == 1) {
        paletteBlendToTertiary = 1024;
      }
    }
    tick++;
  }

  private void updatePalette() {
    if (paletteBlendToSecondary > 0) {
      for (int index = 0; index < activePalette.length; index++) {
        if (paletteBlendToSecondary > 768) {
          activePalette[index] = blend(primaryPalette[index], secondaryPalette[index], 1024 - paletteBlendToSecondary);
        } else if (paletteBlendToSecondary > 256) {
          activePalette[index] = secondaryPalette[index];
        } else {
          activePalette[index] = blend(secondaryPalette[index], primaryPalette[index], 256 - paletteBlendToSecondary);
        }
      }
      return;
    }

    if (paletteBlendToTertiary > 0) {
      for (int index = 0; index < activePalette.length; index++) {
        if (paletteBlendToTertiary > 768) {
          activePalette[index] = blend(primaryPalette[index], tertiaryPalette[index], 1024 - paletteBlendToTertiary);
        } else if (paletteBlendToTertiary > 256) {
          activePalette[index] = tertiaryPalette[index];
        } else {
          activePalette[index] = blend(tertiaryPalette[index], primaryPalette[index], 256 - paletteBlendToTertiary);
        }
      }
      return;
    }

    System.arraycopy(primaryPalette, 0, activePalette, 0, activePalette.length);
  }

  private int[] renderLeftPanel() {
    int[] pixels = leftBasePixels.clone();
    int sourceIndex = 0;
    for (int sourceRow = 1; sourceRow < FLAME_HEIGHT - 1; sourceRow++) {
      int horizontalOffset = (lineOffsets[sourceRow] * (FLAME_HEIGHT - sourceRow)) / FLAME_HEIGHT;
      int startX = Math.max(22 + horizontalOffset, 0);
      int destinationRow = sourceRow + 8;
      sourceIndex += startX;
      for (int destinationX = startX; destinationX < PANEL_WIDTH; destinationX++) {
        int intensity = flame[sourceIndex++];
        if (intensity != 0 && destinationRow < PANEL_HEIGHT) {
          int destinationIndex = destinationX + destinationRow * PANEL_WIDTH;
          pixels[destinationIndex] = blendOverBase(activePalette[intensity], pixels[destinationIndex], intensity);
        }
      }
    }
    return pixels;
  }

  private int[] renderRightPanel() {
    int[] pixels = rightBasePixels.clone();
    int sourceIndex = 0;
    for (int sourceRow = 1; sourceRow < FLAME_HEIGHT - 1; sourceRow++) {
      int horizontalOffset = (lineOffsets[sourceRow] * (FLAME_HEIGHT - sourceRow)) / FLAME_HEIGHT;
      int visibleWidth = 103 - horizontalOffset;
      int destinationRow = sourceRow + 8;
      int destinationX = 24 + horizontalOffset;
      for (int column = 0; column < visibleWidth; column++) {
        int intensity = flame[sourceIndex++];
        if (intensity != 0 && destinationRow < PANEL_HEIGHT && destinationX >= 0 && destinationX < PANEL_WIDTH) {
          int destinationIndex = destinationX + destinationRow * PANEL_WIDTH;
          pixels[destinationIndex] = blendOverBase(activePalette[intensity], pixels[destinationIndex], intensity);
        }
        destinationX++;
      }
      sourceIndex += 128 - visibleWidth;
    }
    return pixels;
  }

  private void seedNoise(TitleScreenRuneMask runeMask) {
    for (int index = 0; index < noise.length; index++) {
      noise[index] = 0;
    }

    for (int index = 0; index < 5000; index++) {
      int sampleIndex = random.nextInt(FLAME_WIDTH * FLAME_HEIGHT);
      noise[sampleIndex] = random.nextInt(256);
    }

    for (int iteration = 0; iteration < 20; iteration++) {
      for (int y = 1; y < FLAME_HEIGHT - 1; y++) {
        for (int x = 1; x < FLAME_WIDTH - 1; x++) {
          int index = x + (y << 7);
          noiseBuffer[index] =
              (noise[index - 1] + noise[index + 1] + noise[index - 128] + noise[index + 128]) / 4;
        }
      }
      System.arraycopy(noiseBuffer, 0, noise, 0, noise.length);
    }

    if (runeMask == null) {
      return;
    }
    boolean[] maskPixels = runeMask.maskPixels();
    for (int index = 0; index < Math.min(maskPixels.length, noise.length); index++) {
      if (maskPixels[index]) {
        noise[index] = 0;
      }
    }
  }

  private int blend(int leftColor, int rightColor, int alpha) {
    int inverseAlpha = 256 - alpha;
    return (
        ((leftColor & 0xff00ff) * inverseAlpha + (rightColor & 0xff00ff) * alpha & 0xff00ff00)
            + ((leftColor & 0x00ff00) * inverseAlpha + (rightColor & 0x00ff00) * alpha & 0x00ff0000)
    ) >> 8;
  }

  private int blendOverBase(int flameColor, int basePixel, int intensity) {
    int baseColor = basePixel & 0x00ffffff;
    int inverseIntensity = 256 - intensity;
    return 0xff000000 | (
        ((flameColor & 0xff00ff) * intensity + (baseColor & 0xff00ff) * inverseIntensity & 0xff00ff00)
            + ((flameColor & 0x00ff00) * intensity + (baseColor & 0x00ff00) * inverseIntensity & 0x00ff0000)
    ) >> 8;
  }
}
