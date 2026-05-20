package io.github.ffakira.rsps.client.desktop.itemicon;

final class PixelBlender {

  private PixelBlender() {
  }

  static int blend(int destinationArgb, int sourceArgb, int sourceAlpha) {
    int inverseAlpha = 255 - sourceAlpha;
    int destinationRed = (destinationArgb >>> 16) & 0xff;
    int destinationGreen = (destinationArgb >>> 8) & 0xff;
    int destinationBlue = destinationArgb & 0xff;
    int sourceRed = (sourceArgb >>> 16) & 0xff;
    int sourceGreen = (sourceArgb >>> 8) & 0xff;
    int sourceBlue = sourceArgb & 0xff;
    int red = (sourceRed * sourceAlpha + destinationRed * inverseAlpha) / 255;
    int green = (sourceGreen * sourceAlpha + destinationGreen * inverseAlpha) / 255;
    int blue = (sourceBlue * sourceAlpha + destinationBlue * inverseAlpha) / 255;
    return 0xff000000 | (red << 16) | (green << 8) | blue;
  }

  static int blendReferenceRgb(int destinationArgb, int sourceRgb, int alpha, int inverseAlpha) {
    int destinationRgb = destinationArgb & 0x00ffffff;
    int blended = ((sourceRgb & 0xff00ff) * inverseAlpha >> 8 & 0xff00ff)
        + ((sourceRgb & 0x00ff00) * inverseAlpha >> 8 & 0x00ff00);
    blended += ((destinationRgb & 0xff00ff) * alpha >> 8 & 0xff00ff)
        + ((destinationRgb & 0x00ff00) * alpha >> 8 & 0x00ff00);
    return 0xff000000 | blended;
  }
}
