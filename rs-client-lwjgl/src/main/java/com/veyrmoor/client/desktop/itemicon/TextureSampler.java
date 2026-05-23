package com.veyrmoor.client.desktop.itemicon;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;

final class TextureSampler {

  private TextureSampler() {
  }

  static int sample(ArgbImage texture, float u, float v) {
    float clampedU = MathUtil.clamp(u, 0.0f, 0.9999f);
    float clampedV = MathUtil.clamp(v, 0.0f, 0.9999f);
    int sampleX = MathUtil.clamp((int) (clampedU * texture.width()), 0, texture.width() - 1);
    int sampleY = MathUtil.clamp((int) (clampedV * texture.height()), 0, texture.height() - 1);
    return texture.pixels()[sampleY * texture.width() + sampleX];
  }

  static int modulate(int textureChannel, float lightChannel) {
    return MathUtil.clamp(
        Math.round(textureChannel * MathUtil.clamp(lightChannel, 0.0f, 255.0f) / 255.0f),
        0,
        255
    );
  }
}
