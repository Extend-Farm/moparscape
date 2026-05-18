package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import io.github.ffakira.rsps.cache.CacheArchiveRepository;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.TopLevelArchiveId;
import java.nio.file.Path;

public final class GameplayFrameAssetLoader {

  private static final int TOP_LEVEL_ARCHIVE_STORE = 0;

  private GameplayFrameAssetLoader() {
  }

  public static GameplayFrameAssets loadFromWorkingDirectory(Path workingDirectory) {
    try {
      ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      CacheArchiveContainer mediaArchive = new CacheArchiveRepository(manifest.cacheStore())
          .loadArchive(TOP_LEVEL_ARCHIVE_STORE, TopLevelArchiveId.MEDIA.archiveId());
      TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(mediaArchive);

      ArgbImage[] sideIcons = new ArgbImage[13];
      for (int iconIndex = 0; iconIndex < sideIcons.length; iconIndex++) {
        sideIcons[iconIndex] = spriteDecoder.decodeSprite(mediaArchive, "sideicons", iconIndex, false);
      }
      ArgbImage redstone1 = spriteDecoder.decodeSprite(mediaArchive, "redstone1", 0, false);
      ArgbImage redstone2 = spriteDecoder.decodeSprite(mediaArchive, "redstone2", 0, false);
      ArgbImage redstone3 = spriteDecoder.decodeSprite(mediaArchive, "redstone3", 0, false);

      return new GameplayFrameAssets(
          spriteDecoder.decodeSprite(mediaArchive, "invback", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "chatback", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "mapback", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backbase1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backbase2", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backhmid1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "compass", 0, false),
          redstone1,
          redstone2,
          redstone3,
          ArgbImageTransforms.flipVertically(redstone1),
          ArgbImageTransforms.flipVertically(redstone2),
          ArgbImageTransforms.flipHorizontally(redstone1),
          ArgbImageTransforms.flipHorizontally(redstone2),
          ArgbImageTransforms.flipHorizontally(redstone3),
          ArgbImageTransforms.flipHorizontally(ArgbImageTransforms.flipVertically(redstone1)),
          ArgbImageTransforms.flipHorizontally(ArgbImageTransforms.flipVertically(redstone2)),
          sideIcons
      );
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load gameplay frame assets from media archive", exception);
    }
  }
}
