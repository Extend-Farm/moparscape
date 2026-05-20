package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import io.github.ffakira.rsps.cache.CacheArchiveRepository;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ArgbImageTransforms;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder;
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
      ArgbImage[] mapFunctionIcons = spriteDecoder.decodeSprites(mediaArchive, "mapfunction", false);
      ArgbImage[] mapDotIcons = spriteDecoder.decodeSprites(mediaArchive, "mapdots", false);
      ArgbImage[] clickCrosses = optionalSprites(spriteDecoder, mediaArchive, "cross");
      ArgbImage redstone1 = spriteDecoder.decodeSprite(mediaArchive, "redstone1", 0, false);
      ArgbImage redstone2 = spriteDecoder.decodeSprite(mediaArchive, "redstone2", 0, false);
      ArgbImage redstone3 = spriteDecoder.decodeSprite(mediaArchive, "redstone3", 0, false);
      // Match the 317 client transform contract:
      // - method358 = horizontal flip
      // - method359 = vertical flip
      ArgbImage redstone1Flipped = ArgbImageTransforms.flipHorizontally(redstone1);
      ArgbImage redstone2Flipped = ArgbImageTransforms.flipHorizontally(redstone2);
      ArgbImage redstone1Mirrored = ArgbImageTransforms.flipVertically(redstone1);
      ArgbImage redstone2Mirrored = ArgbImageTransforms.flipVertically(redstone2);
      ArgbImage redstone3Mirrored = ArgbImageTransforms.flipVertically(redstone3);

      return new GameplayFrameAssets(
          spriteDecoder.decodeSprite(mediaArchive, "backleft1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backleft2", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backright1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backright2", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backtop1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backvmid1", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backvmid2", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backvmid3", 0, false),
          spriteDecoder.decodeSprite(mediaArchive, "backhmid2", 0, false),
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
          redstone1Flipped,
          redstone2Flipped,
          redstone1Mirrored,
          redstone2Mirrored,
          redstone3Mirrored,
          ArgbImageTransforms.flipVertically(redstone1Flipped),
          ArgbImageTransforms.flipVertically(redstone2Flipped),
          sideIcons,
          mapFunctionIcons,
          mapDotIcons,
          clickCrosses
      );
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load gameplay frame assets from media archive", exception);
    }
  }

  private static ArgbImage[] optionalSprites(
      TitleArchiveSpriteDecoder spriteDecoder,
      CacheArchiveContainer archive,
      String entryName
  ) {
    try {
      return spriteDecoder.decodeSprites(archive, entryName, false);
    } catch (Exception ignored) {
      return new ArgbImage[0];
    }
  }
}
