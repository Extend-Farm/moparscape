package com.veyrmoor.client.desktop.login;

import com.veyrmoor.cache.CacheArchiveContainer;
import com.veyrmoor.cache.CacheArchiveRepository;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.TitleArchiveSpriteDecoder;
import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.TopLevelArchiveId;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public final class TitleScreenAssetLoader {

  private static final int TITLE_ARCHIVE_STORE = 0;

  private TitleScreenAssetLoader() {
  }

  public static TitleScreenAssets loadFromWorkingDirectory(Path workingDirectory) {
    try {
      ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
      CacheArchiveContainer titleArchive = new CacheArchiveRepository(manifest.cacheStore())
          .loadArchive(TITLE_ARCHIVE_STORE, TopLevelArchiveId.TITLE.archiveId());
      TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(titleArchive);
      TitleArchiveFontDecoder fontDecoder = new TitleArchiveFontDecoder();

      TitleScreenRuneMask[] runeMasks = new TitleScreenRuneMask[12];
      for (int runeIndex = 0; runeIndex < runeMasks.length; runeIndex++) {
        runeMasks[runeIndex] = spriteDecoder.decodeRuneMask(titleArchive, "runes", runeIndex);
      }

      return new TitleScreenAssets(
          decodeBackground(titleArchive.readEntry("title.dat")),
          spriteDecoder.decodeSprite(titleArchive, "logo", 0, true),
          spriteDecoder.decodeSprite(titleArchive, "titlebox", 0, false),
          spriteDecoder.decodeSprite(titleArchive, "titlebutton", 0, false),
          new TitleScreenFonts(
              fontDecoder.decodeFont(titleArchive, "p11_full", false),
              fontDecoder.decodeFont(titleArchive, "p12_full", false),
              fontDecoder.decodeFont(titleArchive, "b12_full", false)
          ),
          runeMasks
      );
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load native title screen assets", exception);
    }
  }

  private static ArgbImage decodeBackground(byte[] backgroundBytes) throws Exception {
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(backgroundBytes));
    if (image == null) {
      throw new IllegalStateException("Could not decode title.dat as an image");
    }
    int width = image.getWidth();
    int height = image.getHeight();
    int[] pixels = new int[width * height];
    image.getRGB(0, 0, width, height, pixels, 0, width);
    return new ArgbImage(width, height, pixels);
  }
}
