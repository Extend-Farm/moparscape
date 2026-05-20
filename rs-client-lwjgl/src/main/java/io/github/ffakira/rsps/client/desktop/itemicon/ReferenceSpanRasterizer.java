package io.github.ffakira.rsps.client.desktop.itemicon;

final class ReferenceSpanRasterizer {

  void drawShadedSpan(
      int[] pixels,
      int rowOffset,
      int startX,
      int endX,
      int startValue,
      int endValue,
      int referenceAlpha
  ) {
    int startShade = startValue >> 7;
    int endShade = endValue >> 7;
    boolean clipped = startX < 0 || endX > IconRenderConstants.MAX_PIXEL_INDEX;
    if (clipped) {
      if (endX > IconRenderConstants.MAX_PIXEL_INDEX) {
        endX = IconRenderConstants.MAX_PIXEL_INDEX;
      }
      if (startX < 0) {
        startShade -= startX * ((endX - startX) > 3 ? (endShade - startShade) / (endX - startX) : 0);
        startX = 0;
      }
      if (startX >= endX) {
        return;
      }
    } else if (startX >= endX) {
      return;
    }
    int shadeStep;
    int pixelCount;
    if (clipped) {
      pixelCount = endX - startX >> 2;
      shadeStep = (endX - startX) > 3 ? (endShade - startShade) / (endX - startX) : 0;
      shadeStep <<= 2;
    } else {
      pixelCount = endX - startX >> 2;
      shadeStep = pixelCount > 0
          ? (endShade - startShade) * IconRenderConstants.RECIPROCAL_512[pixelCount] >> 15
          : 0;
    }
    rowOffset += startX;
    if (referenceAlpha == 0) {
      while (--pixelCount >= 0) {
        int rgb = Palette.rgb(startShade >> 8);
        startShade += shadeStep;
        pixels[rowOffset++] = 0xff000000 | rgb;
        pixels[rowOffset++] = 0xff000000 | rgb;
        pixels[rowOffset++] = 0xff000000 | rgb;
        pixels[rowOffset++] = 0xff000000 | rgb;
      }
      pixelCount = endX - startX & 3;
      if (pixelCount > 0) {
        int rgb = Palette.rgb(startShade >> 8);
        do {
          pixels[rowOffset++] = 0xff000000 | rgb;
        } while (--pixelCount > 0);
      }
      return;
    }
    int inverseAlpha = 256 - referenceAlpha;
    while (--pixelCount >= 0) {
      int rgb = Palette.rgb(startShade >> 8);
      startShade += shadeStep;
      pixels[rowOffset] = PixelBlender.blendReferenceRgb(pixels[rowOffset], rgb, referenceAlpha, inverseAlpha);
      rowOffset++;
      pixels[rowOffset] = PixelBlender.blendReferenceRgb(pixels[rowOffset], rgb, referenceAlpha, inverseAlpha);
      rowOffset++;
      pixels[rowOffset] = PixelBlender.blendReferenceRgb(pixels[rowOffset], rgb, referenceAlpha, inverseAlpha);
      rowOffset++;
      pixels[rowOffset] = PixelBlender.blendReferenceRgb(pixels[rowOffset], rgb, referenceAlpha, inverseAlpha);
      rowOffset++;
    }
    pixelCount = endX - startX & 3;
    if (pixelCount > 0) {
      int rgb = Palette.rgb(startShade >> 8);
      do {
        pixels[rowOffset] = PixelBlender.blendReferenceRgb(pixels[rowOffset], rgb, referenceAlpha, inverseAlpha);
        rowOffset++;
      } while (--pixelCount > 0);
    }
  }

  void drawFlatSpan(
      int[] pixels,
      int rowOffset,
      int startX,
      int endX,
      int color,
      int ignored,
      int referenceAlpha
  ) {
    if (startX >= endX) {
      return;
    }
    if (startX < 0) {
      startX = 0;
    }
    if (endX > IconRenderConstants.MAX_PIXEL_INDEX) {
      endX = IconRenderConstants.MAX_PIXEL_INDEX;
    }
    if (startX >= endX) {
      return;
    }
    rowOffset += startX;
    int count = endX - startX;
    if (referenceAlpha == 0) {
      for (int index = 0; index < count; index++) {
        pixels[rowOffset + index] = 0xff000000 | color;
      }
      return;
    }
    int inverseAlpha = 256 - referenceAlpha;
    for (int index = 0; index < count; index++) {
      pixels[rowOffset + index] =
          PixelBlender.blendReferenceRgb(pixels[rowOffset + index], color, referenceAlpha, inverseAlpha);
    }
  }
}
