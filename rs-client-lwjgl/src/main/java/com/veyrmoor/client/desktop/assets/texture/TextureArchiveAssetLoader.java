package com.veyrmoor.client.desktop.assets.texture;

import com.veyrmoor.cache.CacheArchiveContainer;
import com.veyrmoor.cache.CacheArchiveRepository;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.TitleArchiveSpriteDecoder;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.TopLevelArchiveId;
import java.nio.file.Path;

public final class TextureArchiveAssetLoader {

  private static final int TOP_LEVEL_ARCHIVE_STORE = 0;
  private static final int TEXTURE_COUNT = 50;

  private TextureArchiveAssetLoader() {
  }

  public static SceneTextureAssets loadFromWorkingDirectory(Path workingDirectory) {
    try {
      ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      CacheArchiveContainer textureArchive = new CacheArchiveRepository(manifest.cacheStore())
          .loadArchive(TOP_LEVEL_ARCHIVE_STORE, TopLevelArchiveId.TEXTURES.archiveId());
      TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(textureArchive);
      ArgbImage[] textures = new ArgbImage[TEXTURE_COUNT];
      for (int textureId = 0; textureId < TEXTURE_COUNT; textureId++) {
        try {
          textures[textureId] = spriteDecoder.decodeSprite(textureArchive, String.valueOf(textureId), 0, false);
        } catch (RuntimeException ignored) {
          textures[textureId] = null;
        }
      }
      return new SceneTextureAssets(textures);
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load scene textures from textures archive", exception);
    }
  }
}
