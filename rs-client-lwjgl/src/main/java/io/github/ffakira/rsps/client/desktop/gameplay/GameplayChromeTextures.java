package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.world.minimap.MapBackClipMasks;

final class GameplayChromeTextures implements AutoCloseable {

  private final boolean available;
  private final OpenGlTexture backLeft1Texture;
  private final OpenGlTexture backLeft2Texture;
  private final OpenGlTexture backRight1Texture;
  private final OpenGlTexture backRight2Texture;
  private final OpenGlTexture backTop1Texture;
  private final OpenGlTexture backVmid1Texture;
  private final OpenGlTexture backVmid2Texture;
  private final OpenGlTexture backVmid3Texture;
  private final OpenGlTexture backHmid2Texture;
  private final OpenGlTexture invBackTexture;
  private final OpenGlTexture chatBackTexture;
  private final OpenGlTexture mapBackTexture;
  private final OpenGlTexture backBase1Texture;
  private final OpenGlTexture backBase2Texture;
  private final OpenGlTexture backHMid1Texture;
  private final OpenGlTexture compassTexture;
  private final OpenGlTexture redstone1Texture;
  private final OpenGlTexture redstone2Texture;
  private final OpenGlTexture redstone3Texture;
  private final OpenGlTexture redstone1FlippedTexture;
  private final OpenGlTexture redstone2FlippedTexture;
  private final OpenGlTexture redstone1MirroredTexture;
  private final OpenGlTexture redstone2MirroredTexture;
  private final OpenGlTexture redstone3MirroredTexture;
  private final OpenGlTexture redstone1BothTransformsTexture;
  private final OpenGlTexture redstone2BothTransformsTexture;
  private final OpenGlTexture[] sideIconTextures;
  private final OpenGlTexture[] mapFunctionTextures;
  private final OpenGlTexture[] mapDotTextures;
  private final MapBackClipMasks mapBackClipMasks;

  GameplayChromeTextures(GameplayFrameAssets gameplayFrameAssets) {
    available = gameplayFrameAssets != null;
    backLeft1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backLeft1());
    backLeft2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backLeft2());
    backRight1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backRight1());
    backRight2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backRight2());
    backTop1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backTop1());
    backVmid1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backVmid1());
    backVmid2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backVmid2());
    backVmid3Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backVmid3());
    backHmid2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backHmid2());
    invBackTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.invBack());
    chatBackTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.chatBack());
    mapBackTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.mapBack());
    backBase1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backBase1());
    backBase2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backBase2());
    backHMid1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.backHMid1());
    compassTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.compass());
    redstone1Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone1());
    redstone2Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone2());
    redstone3Texture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone3());
    redstone1FlippedTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone1Flipped());
    redstone2FlippedTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone2Flipped());
    redstone1MirroredTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone1Mirrored());
    redstone2MirroredTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone2Mirrored());
    redstone3MirroredTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone3Mirrored());
    redstone1BothTransformsTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone1BothTransforms());
    redstone2BothTransformsTexture = texture(gameplayFrameAssets == null ? null : gameplayFrameAssets.redstone2BothTransforms());
    sideIconTextures = textures(gameplayFrameAssets == null ? null : gameplayFrameAssets.sideIcons());
    mapFunctionTextures = textures(gameplayFrameAssets == null ? null : gameplayFrameAssets.mapFunctionIcons());
    mapDotTextures = textures(gameplayFrameAssets == null ? null : gameplayFrameAssets.mapDotIcons());
    mapBackClipMasks = gameplayFrameAssets == null ? null : MapBackClipMasks.fromMapBack(gameplayFrameAssets.mapBack());
  }

  boolean isAvailable() {
    return available;
  }

  MapBackClipMasks mapBackClipMasks() {
    return mapBackClipMasks;
  }

  OpenGlTexture compassTexture() {
    return compassTexture;
  }

  OpenGlTexture[] mapFunctionTextures() {
    return mapFunctionTextures;
  }

  OpenGlTexture[] mapDotTextures() {
    return mapDotTextures;
  }

  void drawFrame(ImmediateModeRenderer2d primitives) {
    if (!available) {
      return;
    }
    // These are the real cache `media` frame pieces. The legacy client builds the shell from both
    // the central panel art and the separate outer back-buffer sprites; omitting the latter leaves
    // black gutters around the minimap, sidebar, and chatbox.
    primitives.drawTexturedQuad(backLeft1Texture, new ScreenRect(0.0f, 4.0f, backLeft1Texture.width(), backLeft1Texture.height()));
    primitives.drawTexturedQuad(backLeft2Texture, new ScreenRect(0.0f, 357.0f, backLeft2Texture.width(), backLeft2Texture.height()));
    primitives.drawTexturedQuad(backRight1Texture, new ScreenRect(722.0f, 4.0f, backRight1Texture.width(), backRight1Texture.height()));
    primitives.drawTexturedQuad(backRight2Texture, new ScreenRect(743.0f, 205.0f, backRight2Texture.width(), backRight2Texture.height()));
    primitives.drawTexturedQuad(backTop1Texture, new ScreenRect(0.0f, 0.0f, backTop1Texture.width(), backTop1Texture.height()));
    primitives.drawTexturedQuad(backVmid1Texture, new ScreenRect(516.0f, 4.0f, backVmid1Texture.width(), backVmid1Texture.height()));
    primitives.drawTexturedQuad(backVmid2Texture, new ScreenRect(516.0f, 205.0f, backVmid2Texture.width(), backVmid2Texture.height()));
    primitives.drawTexturedQuad(backVmid3Texture, new ScreenRect(496.0f, 357.0f, backVmid3Texture.width(), backVmid3Texture.height()));
    primitives.drawTexturedQuad(backHmid2Texture, new ScreenRect(0.0f, 338.0f, backHmid2Texture.width(), backHmid2Texture.height()));
    primitives.drawTexturedQuad(mapBackTexture, GameplayLayout.minimapPanelRect());
    primitives.drawTexturedQuad(backHMid1Texture, GameplayLayout.topTabRect());
    primitives.drawTexturedQuad(backBase2Texture, GameplayLayout.bottomTabRect());
    primitives.drawTexturedQuad(invBackTexture, GameplayLayout.sidebarPanelRect());
    primitives.drawTexturedQuad(chatBackTexture, GameplayLayout.chatboxPanelRect());
    primitives.drawTexturedQuad(backBase1Texture, new ScreenRect(0.0f, 453.0f, 496.0f, 50.0f));
  }

  void drawSideIcons(ImmediateModeRenderer2d primitives) {
    if (sideIconTextures.length < 13) {
      return;
    }
    float[][] positions = {
        {516.0f + 29.0f, 160.0f + 13.0f},
        {516.0f + 53.0f, 160.0f + 11.0f},
        {516.0f + 82.0f, 160.0f + 11.0f},
        {516.0f + 115.0f, 160.0f + 12.0f},
        {516.0f + 153.0f, 160.0f + 13.0f},
        {516.0f + 180.0f, 160.0f + 11.0f},
        {516.0f + 208.0f, 160.0f + 13.0f},
        {496.0f + 74.0f, 466.0f + 2.0f},
        {496.0f + 102.0f, 466.0f + 3.0f},
        {496.0f + 137.0f, 466.0f + 4.0f},
        {496.0f + 174.0f, 466.0f + 2.0f},
        {496.0f + 201.0f, 466.0f + 2.0f},
        {496.0f + 226.0f, 466.0f + 2.0f}
    };
    for (int index = 0; index < Math.min(sideIconTextures.length, positions.length); index++) {
      OpenGlTexture sideIconTexture = sideIconTextures[index];
      if (sideIconTexture == null) {
        continue;
      }
      primitives.drawTexturedQuad(
          sideIconTexture,
          new ScreenRect(positions[index][0], positions[index][1], sideIconTexture.width(), sideIconTexture.height())
      );
    }
  }

  void drawActiveGameplayTabHighlight(ImmediateModeRenderer2d primitives, GameplayTab activeGameplayTab) {
    OpenGlTexture highlightTexture = gameplayTabHighlightTexture(activeGameplayTab);
    if (highlightTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        highlightTexture,
        GameplayLayout.gameplayTabHighlightRect(activeGameplayTab, highlightTexture.width(), highlightTexture.height())
    );
  }

  private OpenGlTexture gameplayTabHighlightTexture(GameplayTab gameplayTab) {
    return switch (gameplayTab.index()) {
      case 0 -> redstone1Texture;
      case 1, 2 -> redstone2Texture;
      case 3 -> redstone3Texture;
      case 4, 5 -> redstone2FlippedTexture;
      case 6 -> redstone1FlippedTexture;
      case 7 -> redstone1MirroredTexture;
      case 8, 9 -> redstone2MirroredTexture;
      case 10 -> redstone3MirroredTexture;
      case 11, 12 -> redstone2BothTransformsTexture;
      case 13 -> redstone1BothTransformsTexture;
      default -> redstone3Texture;
    };
  }

  @Override
  public void close() {
    closeTexture(backLeft1Texture);
    closeTexture(backLeft2Texture);
    closeTexture(backRight1Texture);
    closeTexture(backRight2Texture);
    closeTexture(backTop1Texture);
    closeTexture(backVmid1Texture);
    closeTexture(backVmid2Texture);
    closeTexture(backVmid3Texture);
    closeTexture(backHmid2Texture);
    closeTexture(invBackTexture);
    closeTexture(chatBackTexture);
    closeTexture(mapBackTexture);
    closeTexture(backBase1Texture);
    closeTexture(backBase2Texture);
    closeTexture(backHMid1Texture);
    closeTexture(compassTexture);
    closeTexture(redstone1Texture);
    closeTexture(redstone2Texture);
    closeTexture(redstone3Texture);
    closeTexture(redstone1FlippedTexture);
    closeTexture(redstone2FlippedTexture);
    closeTexture(redstone1MirroredTexture);
    closeTexture(redstone2MirroredTexture);
    closeTexture(redstone3MirroredTexture);
    closeTexture(redstone1BothTransformsTexture);
    closeTexture(redstone2BothTransformsTexture);
    for (OpenGlTexture sideIconTexture : sideIconTextures) {
      closeTexture(sideIconTexture);
    }
    for (OpenGlTexture mapFunctionTexture : mapFunctionTextures) {
      closeTexture(mapFunctionTexture);
    }
    for (OpenGlTexture mapDotTexture : mapDotTextures) {
      closeTexture(mapDotTexture);
    }
  }

  private static OpenGlTexture texture(ArgbImage image) {
    return image == null ? null : OpenGlTexture.fromArgbImage(image);
  }

  private static OpenGlTexture[] textures(ArgbImage[] images) {
    if (images == null) {
      return new OpenGlTexture[0];
    }
    OpenGlTexture[] textures = new OpenGlTexture[images.length];
    for (int index = 0; index < images.length; index++) {
      textures[index] = texture(images[index]);
    }
    return textures;
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
