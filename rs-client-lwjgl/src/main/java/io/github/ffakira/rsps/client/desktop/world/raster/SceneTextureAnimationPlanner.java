package io.github.ffakira.rsps.client.desktop.world.raster;

final class SceneTextureAnimationPlanner {

  private static final float SCROLL_PIXELS_PER_SECOND = 96.0f;

  float[] plan(int textureId, int textureHeight, float[] uv, long elapsedNanos) {
    if (uv == null || textureHeight <= 0 || !isAnimatedTexture(textureId)) {
      return uv;
    }
    // The reference client scrolls these scene textures by rotating sprite rows.
    // In the native renderer the same motion is cheaper and easier to tune as a V-axis UV offset.
    float offset = scrollOffset(textureHeight, elapsedNanos);
    return new float[]{
        uv[0], uv[1] - offset,
        uv[2], uv[3] - offset,
        uv[4], uv[5] - offset
    };
  }

  private boolean isAnimatedTexture(int textureId) {
    // Legacy moparscape scrolls these textures by rotating sprite rows each frame. Texture id 1 is
    // water — without animation it appears as flat blue, which is the symptom the user reports
    // when comparing the native client side-by-side with the legacy reference at Lumbridge bridge.
    return switch (textureId) {
      case 1, 17, 24, 34 -> true;
      default -> false;
    };
  }

  private float scrollOffset(int textureHeight, long elapsedNanos) {
    float cycles = (elapsedNanos / 1_000_000_000.0f) * SCROLL_PIXELS_PER_SECOND / textureHeight;
    return cycles - (float) Math.floor(cycles);
  }
}
