package io.github.ffakira.rsps.client.desktop.itemicon;

final class IconPostProcessor {

  void apply(int[] pixels) {
    // The classic sprite pipeline outlines first, then stamps the diagonal shadow onto the new
    // silhouette. Reversing the order shifts pixels on rune edges.
    applyOutline(pixels);
    applyShadow(pixels);
  }

  private void applyOutline(int[] pixels) {
    int outlineArgb = 0xff000000 | IconRenderConstants.OUTLINE_RGB;
    int shadowArgb = 0xff000000 | IconRenderConstants.SHADOW_RGB;
    for (int pixelY = IconRenderConstants.ICON_SIZE - 1; pixelY >= 0; pixelY--) {
      for (int pixelX = IconRenderConstants.ICON_SIZE - 1; pixelX >= 0; pixelX--) {
        int pixelIndex = pixelY * IconRenderConstants.ICON_SIZE + pixelX;
        if (pixels[pixelIndex] != 0) {
          continue;
        }
        if (pixelX > 0 && isSourcePixel(pixels[pixelIndex - 1], outlineArgb, shadowArgb)
            || pixelY > 0 && isSourcePixel(pixels[pixelIndex - IconRenderConstants.ICON_SIZE], outlineArgb, shadowArgb)
            || pixelX < IconRenderConstants.ICON_SIZE - 1
            && isSourcePixel(pixels[pixelIndex + 1], outlineArgb, shadowArgb)
            || pixelY < IconRenderConstants.ICON_SIZE - 1
            && isSourcePixel(pixels[pixelIndex + IconRenderConstants.ICON_SIZE], outlineArgb, shadowArgb)) {
          pixels[pixelIndex] = outlineArgb;
        }
      }
    }
  }

  private void applyShadow(int[] pixels) {
    int shadowArgb = 0xff000000 | IconRenderConstants.SHADOW_RGB;
    for (int pixelY = IconRenderConstants.ICON_SIZE - 1; pixelY >= 0; pixelY--) {
      int rowStart = pixelY * IconRenderConstants.ICON_SIZE;
      for (int pixelX = IconRenderConstants.ICON_SIZE - 1; pixelX >= 0; pixelX--) {
        int pixelIndex = rowStart + pixelX;
        if (pixels[pixelIndex] == 0
            && pixelX > 0
            && pixelY > 0
            && pixels[pixelIndex - 1 - IconRenderConstants.ICON_SIZE] != 0) {
          pixels[pixelIndex] = shadowArgb;
        }
      }
    }
  }

  private boolean isSourcePixel(int pixel, int outlineArgb, int shadowArgb) {
    return pixel != 0 && pixel != outlineArgb && pixel != shadowArgb;
  }
}
