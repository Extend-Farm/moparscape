package io.github.ffakira.rsps.client.desktop.core;

final class ArgbImageTransforms {

  private ArgbImageTransforms() {
  }

  static ArgbImage flipHorizontally(ArgbImage image) {
    int[] transformedPixels = new int[image.width() * image.height()];
    for (int y = 0; y < image.height(); y++) {
      for (int x = 0; x < image.width(); x++) {
        transformedPixels[x + y * image.width()] = image.pixels()[(image.width() - x - 1) + y * image.width()];
      }
    }
    return new ArgbImage(image.width(), image.height(), transformedPixels);
  }

  static ArgbImage flipVertically(ArgbImage image) {
    int[] transformedPixels = new int[image.width() * image.height()];
    for (int y = 0; y < image.height(); y++) {
      int sourceRow = image.height() - y - 1;
      System.arraycopy(
          image.pixels(),
          sourceRow * image.width(),
          transformedPixels,
          y * image.width(),
          image.width()
      );
    }
    return new ArgbImage(image.width(), image.height(), transformedPixels);
  }
}
