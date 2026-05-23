package com.veyrmoor.client.desktop.gameplay;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.client.desktop.world.minimap.MapBackClipMasks;

final class GameplayChromeTextures implements AutoCloseable {

  private static final float[][] SIDE_ICON_POSITIONS = {
      {545.0f, 173.0f},
      {569.0f, 171.0f},
      {598.0f, 171.0f},
      {631.0f, 172.0f},
      {669.0f, 173.0f},
      {696.0f, 171.0f},
      {724.0f, 173.0f},
      {570.0f, 468.0f},
      {598.0f, 469.0f},
      {633.0f, 470.0f},
      {670.0f, 468.0f},
      {697.0f, 468.0f},
      {722.0f, 468.0f}
  };

  private final boolean available;
  private final FrameTextures frameTextures;
  private final OpenGlTexture compassTexture;
  private final TabHighlightTextures tabHighlightTextures;
  private final OpenGlTexture[] tabHighlightTexturesByIndex;
  private final OpenGlTexture[] sideIconTextures;
  private final OpenGlTexture[] mapFunctionTextures;
  private final OpenGlTexture[] mapDotTextures;
  private final OpenGlTexture[] clickCrossTextures;
  private final MapBackClipMasks mapBackClipMasks;

  GameplayChromeTextures(GameplayFrameAssets gameplayFrameAssets) {
    available = gameplayFrameAssets != null;
    if (gameplayFrameAssets == null) {
      frameTextures = FrameTextures.empty();
      compassTexture = null;
      tabHighlightTextures = TabHighlightTextures.empty();
      tabHighlightTexturesByIndex = new OpenGlTexture[0];
      sideIconTextures = new OpenGlTexture[0];
      mapFunctionTextures = new OpenGlTexture[0];
      mapDotTextures = new OpenGlTexture[0];
      clickCrossTextures = new OpenGlTexture[0];
      mapBackClipMasks = null;
      return;
    }

    frameTextures = FrameTextures.from(gameplayFrameAssets);
    compassTexture = requireTexture("compass", gameplayFrameAssets.compass());
    tabHighlightTextures = TabHighlightTextures.from(gameplayFrameAssets);
    tabHighlightTexturesByIndex = tabHighlightTextures.byTabIndex();
    sideIconTextures = textures(gameplayFrameAssets.sideIcons());
    mapFunctionTextures = textures(gameplayFrameAssets.mapFunctionIcons());
    mapDotTextures = textures(gameplayFrameAssets.mapDotIcons());
    clickCrossTextures = textures(gameplayFrameAssets.clickCrosses());
    mapBackClipMasks = MapBackClipMasks.fromMapBack(requireImage("mapBack", gameplayFrameAssets.mapBack()));
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

  OpenGlTexture clickCrossTexture(GameplayMouseButton button, int frameIndex) {
    if (frameIndex < 0 || frameIndex >= GameplayCursorMarker.FRAME_COUNT) {
      return null;
    }
    int textureIndex = (button == GameplayMouseButton.LEFT ? 0 : GameplayCursorMarker.FRAME_COUNT) + frameIndex;
    if (textureIndex < 0 || textureIndex >= clickCrossTextures.length) {
      return null;
    }
    return clickCrossTextures[textureIndex];
  }

  void drawFrame(ImmediateModeRenderer2d primitives) {
    if (!available) {
      return;
    }
    // These are the real cache `media` frame pieces. The legacy client builds the shell from both
    // the central panel art and the separate outer back-buffer sprites; omitting the latter leaves
    // black gutters around the minimap, sidebar, and chatbox.
    drawAt(primitives, frameTextures.backLeft1(), 0.0f, 4.0f);
    drawAt(primitives, frameTextures.backLeft2(), 0.0f, 357.0f);
    drawAt(primitives, frameTextures.backRight1(), 722.0f, 4.0f);
    drawAt(primitives, frameTextures.backRight2(), 743.0f, 205.0f);
    drawAt(primitives, frameTextures.backTop1(), 0.0f, 0.0f);
    drawAt(primitives, frameTextures.backVmid1(), 516.0f, 4.0f);
    drawAt(primitives, frameTextures.backVmid2(), 516.0f, 205.0f);
    drawAt(primitives, frameTextures.backVmid3(), 496.0f, 357.0f);
    drawAt(primitives, frameTextures.backHmid2(), 0.0f, 338.0f);
    draw(primitives, frameTextures.mapBack(), GameplayLayout.minimapPanelRect());
    draw(primitives, frameTextures.backHMid1(), GameplayLayout.topTabRect());
    draw(primitives, frameTextures.backBase2(), GameplayLayout.bottomTabRect());
    draw(primitives, frameTextures.invBack(), GameplayLayout.sidebarPanelRect());
    draw(primitives, frameTextures.chatBack(), GameplayLayout.chatboxPanelRect());
    draw(primitives, frameTextures.backBase1(), new ScreenRect(0.0f, 453.0f, 496.0f, 50.0f));
  }

  void drawSideIcons(ImmediateModeRenderer2d primitives) {
    if (sideIconTextures.length < SIDE_ICON_POSITIONS.length) {
      return;
    }
    for (int index = 0; index < Math.min(sideIconTextures.length, SIDE_ICON_POSITIONS.length); index++) {
      OpenGlTexture sideIconTexture = sideIconTextures[index];
      drawAt(primitives, sideIconTexture, SIDE_ICON_POSITIONS[index][0], SIDE_ICON_POSITIONS[index][1]);
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
    int tabIndex = gameplayTab.index();
    if (tabIndex < 0 || tabIndex >= tabHighlightTexturesByIndex.length) {
      return tabHighlightTextures.redstone3();
    }
    return tabHighlightTexturesByIndex[tabIndex];
  }

  @Override
  public void close() {
    frameTextures.close();
    closeTexture(compassTexture);
    tabHighlightTextures.close();
    closeTextures(sideIconTextures);
    closeTextures(mapFunctionTextures);
    closeTextures(mapDotTextures);
    closeTextures(clickCrossTextures);
  }

  private static OpenGlTexture texture(ArgbImage image) {
    return image == null ? null : OpenGlTexture.fromArgbImage(image);
  }

  private static OpenGlTexture requireTexture(String name, ArgbImage image) {
    return OpenGlTexture.fromArgbImage(requireImage(name, image));
  }

  private static ArgbImage requireImage(String name, ArgbImage image) {
    if (image == null) {
      throw new IllegalStateException("Missing gameplay chrome asset: " + name);
    }
    return image;
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

  private static void drawAt(ImmediateModeRenderer2d primitives, OpenGlTexture texture, float x, float y) {
    if (texture == null) {
      return;
    }
    primitives.drawTexturedQuad(texture, new ScreenRect(x, y, texture.width(), texture.height()));
  }

  private static void draw(ImmediateModeRenderer2d primitives, OpenGlTexture texture, ScreenRect rect) {
    if (texture == null) {
      return;
    }
    primitives.drawTexturedQuad(texture, rect);
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private static void closeTextures(OpenGlTexture[] textures) {
    for (OpenGlTexture texture : textures) {
      closeTexture(texture);
    }
  }

  private record FrameTextures(
      OpenGlTexture backLeft1,
      OpenGlTexture backLeft2,
      OpenGlTexture backRight1,
      OpenGlTexture backRight2,
      OpenGlTexture backTop1,
      OpenGlTexture backVmid1,
      OpenGlTexture backVmid2,
      OpenGlTexture backVmid3,
      OpenGlTexture backHmid2,
      OpenGlTexture invBack,
      OpenGlTexture chatBack,
      OpenGlTexture mapBack,
      OpenGlTexture backBase1,
      OpenGlTexture backBase2,
      OpenGlTexture backHMid1
  ) {

    private static final FrameTextures EMPTY = new FrameTextures(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    private static FrameTextures empty() {
      return EMPTY;
    }

    private static FrameTextures from(GameplayFrameAssets gameplayFrameAssets) {
      return new FrameTextures(
          requireTexture("backLeft1", gameplayFrameAssets.backLeft1()),
          requireTexture("backLeft2", gameplayFrameAssets.backLeft2()),
          requireTexture("backRight1", gameplayFrameAssets.backRight1()),
          requireTexture("backRight2", gameplayFrameAssets.backRight2()),
          requireTexture("backTop1", gameplayFrameAssets.backTop1()),
          requireTexture("backVmid1", gameplayFrameAssets.backVmid1()),
          requireTexture("backVmid2", gameplayFrameAssets.backVmid2()),
          requireTexture("backVmid3", gameplayFrameAssets.backVmid3()),
          requireTexture("backHmid2", gameplayFrameAssets.backHmid2()),
          requireTexture("invBack", gameplayFrameAssets.invBack()),
          requireTexture("chatBack", gameplayFrameAssets.chatBack()),
          requireTexture("mapBack", gameplayFrameAssets.mapBack()),
          requireTexture("backBase1", gameplayFrameAssets.backBase1()),
          requireTexture("backBase2", gameplayFrameAssets.backBase2()),
          requireTexture("backHMid1", gameplayFrameAssets.backHMid1())
      );
    }

    private void close() {
      closeTexture(backLeft1);
      closeTexture(backLeft2);
      closeTexture(backRight1);
      closeTexture(backRight2);
      closeTexture(backTop1);
      closeTexture(backVmid1);
      closeTexture(backVmid2);
      closeTexture(backVmid3);
      closeTexture(backHmid2);
      closeTexture(invBack);
      closeTexture(chatBack);
      closeTexture(mapBack);
      closeTexture(backBase1);
      closeTexture(backBase2);
      closeTexture(backHMid1);
    }
  }

  private record TabHighlightTextures(
      OpenGlTexture redstone1,
      OpenGlTexture redstone2,
      OpenGlTexture redstone3,
      OpenGlTexture redstone1Flipped,
      OpenGlTexture redstone2Flipped,
      OpenGlTexture redstone1Mirrored,
      OpenGlTexture redstone2Mirrored,
      OpenGlTexture redstone3Mirrored,
      OpenGlTexture redstone1BothTransforms,
      OpenGlTexture redstone2BothTransforms
  ) {

    private static final TabHighlightTextures EMPTY = new TabHighlightTextures(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    );

    private static TabHighlightTextures empty() {
      return EMPTY;
    }

    private static TabHighlightTextures from(GameplayFrameAssets gameplayFrameAssets) {
      return new TabHighlightTextures(
          requireTexture("redstone1", gameplayFrameAssets.redstone1()),
          requireTexture("redstone2", gameplayFrameAssets.redstone2()),
          requireTexture("redstone3", gameplayFrameAssets.redstone3()),
          requireTexture("redstone1Flipped", gameplayFrameAssets.redstone1Flipped()),
          requireTexture("redstone2Flipped", gameplayFrameAssets.redstone2Flipped()),
          requireTexture("redstone1Mirrored", gameplayFrameAssets.redstone1Mirrored()),
          requireTexture("redstone2Mirrored", gameplayFrameAssets.redstone2Mirrored()),
          requireTexture("redstone3Mirrored", gameplayFrameAssets.redstone3Mirrored()),
          requireTexture("redstone1BothTransforms", gameplayFrameAssets.redstone1BothTransforms()),
          requireTexture("redstone2BothTransforms", gameplayFrameAssets.redstone2BothTransforms())
      );
    }

    private OpenGlTexture[] byTabIndex() {
      return new OpenGlTexture[] {
          redstone1,
          redstone2,
          redstone2,
          redstone3,
          redstone2Flipped,
          redstone2Flipped,
          redstone1Flipped,
          redstone1Mirrored,
          redstone2Mirrored,
          redstone2Mirrored,
          redstone3Mirrored,
          redstone2BothTransforms,
          redstone2BothTransforms,
          redstone1BothTransforms
      };
    }

    private void close() {
      closeTexture(redstone1);
      closeTexture(redstone2);
      closeTexture(redstone3);
      closeTexture(redstone1Flipped);
      closeTexture(redstone2Flipped);
      closeTexture(redstone1Mirrored);
      closeTexture(redstone2Mirrored);
      closeTexture(redstone3Mirrored);
      closeTexture(redstone1BothTransforms);
      closeTexture(redstone2BothTransforms);
    }
  }
}
