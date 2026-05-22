package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import io.github.ffakira.rsps.cache.CacheArchiveRepository;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ArgbImageTransforms;
import io.github.ffakira.rsps.client.desktop.core.ArchiveSpriteResolver;
import io.github.ffakira.rsps.client.desktop.core.TitleArchiveSpriteDecoder;
import io.github.ffakira.rsps.client.desktop.gameplay.sidebar.GameplayStatsTabAssets;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.InterfaceComponentCatalog;
import io.github.ffakira.rsps.content.TopLevelArchiveId;
import java.nio.file.Path;

public final class GameplayFrameAssetLoader {

  private static final int TOP_LEVEL_ARCHIVE_STORE = 0;

  private GameplayFrameAssetLoader() {
  }

  public static GameplayFrameAssets loadFromWorkingDirectory(Path workingDirectory) {
    try {
      ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      CacheArchiveRepository archiveRepository = new CacheArchiveRepository(manifest.cacheStore());
      CacheArchiveContainer mediaArchive = archiveRepository.loadArchive(TOP_LEVEL_ARCHIVE_STORE, TopLevelArchiveId.MEDIA.archiveId());
      TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(mediaArchive);
      ArchiveSpriteResolver mediaSpriteResolver = new ArchiveSpriteResolver(mediaArchive);
      InterfaceComponentCatalog interfaceComponents = InterfaceComponentCatalog.load(manifest);
      GameplayStatsTabAssets statsTabAssets = loadStatsTabAssets(spriteDecoder, mediaArchive);

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
          clickCrosses,
          statsTabAssets,
          interfaceComponents,
          mediaSpriteResolver
      );
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load gameplay frame assets from media archive", exception);
    }
  }

  private static GameplayStatsTabAssets loadStatsTabAssets(
      TitleArchiveSpriteDecoder spriteDecoder,
      CacheArchiveContainer mediaArchive
  ) {
    ArgbImage[] skillIconsBySkillId = new ArgbImage[21];
    ArgbImage[] primaryIcons = spriteDecoder.decodeSprites(mediaArchive, "staticons", false);
    ArgbImage[] extendedIcons = spriteDecoder.decodeSprites(mediaArchive, "staticons2", false);

    skillIconsBySkillId[0] = primaryIcons[0];
    skillIconsBySkillId[1] = primaryIcons[2];
    skillIconsBySkillId[2] = primaryIcons[1];
    skillIconsBySkillId[3] = primaryIcons[6];
    skillIconsBySkillId[4] = primaryIcons[3];
    skillIconsBySkillId[5] = primaryIcons[4];
    skillIconsBySkillId[6] = primaryIcons[5];
    skillIconsBySkillId[7] = primaryIcons[15];
    skillIconsBySkillId[8] = primaryIcons[17];
    skillIconsBySkillId[9] = primaryIcons[11];
    skillIconsBySkillId[10] = primaryIcons[14];
    skillIconsBySkillId[11] = primaryIcons[16];
    skillIconsBySkillId[12] = primaryIcons[10];
    skillIconsBySkillId[13] = primaryIcons[13];
    skillIconsBySkillId[14] = primaryIcons[12];
    skillIconsBySkillId[15] = primaryIcons[8];
    skillIconsBySkillId[16] = primaryIcons[7];
    skillIconsBySkillId[17] = primaryIcons[9];
    skillIconsBySkillId[18] = extendedIcons[1];
    skillIconsBySkillId[19] = extendedIcons[2];
    skillIconsBySkillId[20] = extendedIcons[0];

    return new GameplayStatsTabAssets(
        spriteDecoder.decodeSprite(mediaArchive, "miscgraphics", 4, false),
        spriteDecoder.decodeSprite(mediaArchive, "miscgraphics", 5, false),
        skillIconsBySkillId
    );
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
