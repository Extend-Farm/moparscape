package io.github.ffakira.rsps.client.desktop.login;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

public final class TitleScreenBackgroundComposer {

  private static final int COMPOSED_WIDTH = 765;
  private static final int COMPOSED_HEIGHT = 503;

  private TitleScreenBackgroundComposer() {
  }

  public static ArgbImage compose(ArgbImage titleBackground) {
    ArgbImage flippedBackground = flipHorizontally(titleBackground);

    ArgbImage leftFlameBase = composeBuffer(128, 265, titleBackground, 0, 0, flippedBackground, 382, 0);
    ArgbImage rightFlameBase = composeBuffer(128, 265, titleBackground, -637, 0, flippedBackground, -255, 0);
    ArgbImage topCenter = composeBuffer(509, 171, titleBackground, -128, 0, flippedBackground, 254, 0);
    ArgbImage titleBoxBackground = composeBuffer(360, 200, titleBackground, -202, -171, flippedBackground, 180, -171);
    ArgbImage bottomCenter = composeBuffer(360, 132, titleBackground, -202, -371, flippedBackground, 180, -371);
    ArgbImage bottomLeft = composeBuffer(202, 238, titleBackground, 0, -265, flippedBackground, 382, -265);
    ArgbImage bottomRight = composeBuffer(203, 238, titleBackground, -562, -265, flippedBackground, -180, -265);
    ArgbImage middleLeft = composeBuffer(74, 94, titleBackground, -128, -171, flippedBackground, 254, -171);
    ArgbImage middleRight = composeBuffer(75, 94, titleBackground, -562, -171, flippedBackground, -180, -171);

    int[] composedPixels = new int[COMPOSED_WIDTH * COMPOSED_HEIGHT];
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, leftFlameBase, 0, 0);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, rightFlameBase, 637, 0);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, topCenter, 128, 0);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, titleBoxBackground, 202, 171);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, bottomCenter, 202, 371);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, bottomLeft, 0, 265);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, bottomRight, 562, 265);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, middleLeft, 128, 171);
    blit(composedPixels, COMPOSED_WIDTH, COMPOSED_HEIGHT, middleRight, 562, 171);
    return new ArgbImage(COMPOSED_WIDTH, COMPOSED_HEIGHT, composedPixels);
  }

  private static ArgbImage composeBuffer(
      int width,
      int height,
      ArgbImage original,
      int originalX,
      int originalY,
      ArgbImage flipped,
      int flippedX,
      int flippedY
  ) {
    int[] bufferPixels = new int[width * height];
    blit(bufferPixels, width, height, original, originalX, originalY);
    blit(bufferPixels, width, height, flipped, flippedX, flippedY);
    return new ArgbImage(width, height, bufferPixels);
  }

  private static void blit(int[] destinationPixels, int destinationWidth, int destinationHeight, ArgbImage source, int destinationX, int destinationY) {
    for (int sourceY = 0; sourceY < source.height(); sourceY++) {
      int targetY = destinationY + sourceY;
      if (targetY < 0 || targetY >= destinationHeight) {
        continue;
      }
      for (int sourceX = 0; sourceX < source.width(); sourceX++) {
        int targetX = destinationX + sourceX;
        if (targetX < 0 || targetX >= destinationWidth) {
          continue;
        }
        int pixel = source.pixels()[sourceX + sourceY * source.width()];
        if (pixel != 0) {
          destinationPixels[targetX + targetY * destinationWidth] = pixel;
        }
      }
    }
  }

  private static ArgbImage flipHorizontally(ArgbImage image) {
    int[] flippedPixels = new int[image.width() * image.height()];
    for (int y = 0; y < image.height(); y++) {
      for (int x = 0; x < image.width(); x++) {
        flippedPixels[x + y * image.width()] = image.pixels()[(image.width() - x - 1) + y * image.width()];
      }
    }
    return new ArgbImage(image.width(), image.height(), flippedPixels);
  }
}
