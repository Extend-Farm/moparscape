package io.github.ffakira.rsps.client.desktop.world.raster;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

public record SceneTextureAssets(ArgbImage[] textures) {

  public SceneTextureAssets {
    textures = textures.clone();
  }

  public ArgbImage texture(int textureId) {
    if (textureId < 0 || textureId >= textures.length) {
      return null;
    }
    return textures[textureId];
  }
}
