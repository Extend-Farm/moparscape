package com.veyrmoor.client.desktop.world.raster;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;

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
