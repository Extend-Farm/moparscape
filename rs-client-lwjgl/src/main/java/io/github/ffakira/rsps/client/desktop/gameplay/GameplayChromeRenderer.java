package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenFonts;
import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;

import static org.lwjgl.opengl.GL11.glColor3f;

public final class GameplayChromeRenderer implements AutoCloseable {

  private final ImmediateModeRenderer2d primitives;
  private final GameplayChromeTextures chromeTextures;
  private final GameplayMinimapRenderer minimapRenderer;
  private final GameplaySidebarRenderer sidebarRenderer;
  private GameplayTab activeGameplayTab = GameplayTab.INVENTORY;

  public GameplayChromeRenderer(
      GameplayFrameAssets gameplayFrameAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.primitives = primitives;
    chromeTextures = new GameplayChromeTextures(gameplayFrameAssets);
    minimapRenderer = new GameplayMinimapRenderer(
        primitives,
        chromeTextures.mapBackClipMasks(),
        chromeTextures.compassTexture(),
        chromeTextures.mapFunctionTextures()
    );
    sidebarRenderer = new GameplaySidebarRenderer(
        itemDefinitionCatalog,
        itemIconRenderer,
        titleScreenFonts == null ? null : titleScreenFonts.plainSmall(),
        primitives
    );
  }

  public boolean handleClick(double x, double y) {
    for (GameplayTab gameplayTab : GameplayTab.values()) {
      if (GameplayLayout.gameplayTabRect(gameplayTab).contains(x, y)) {
        activeGameplayTab = gameplayTab;
        return true;
      }
    }
    return false;
  }

  public void resetToInventoryTab() {
    activeGameplayTab = GameplayTab.INVENTORY;
  }

  public void render(
      ClientViewModel viewModel,
      WorldScene worldScene,
      OpenGlTexture worldMinimapTexture,
      WorldCameraState cameraState
  ) {
    // Chrome is a deliberate mix today:
    // - authentic cache-backed frame art when media assets are available
    // - a native scene-state minimap inside that frame
    // - native/synthetic sidebar and chat contents until widget decoding is ported
    if (chromeTextures.isAvailable()) {
      chromeTextures.drawFrame(primitives);
    } else {
      drawPanel(GameplayLayout.worldViewportRect(), 0.07f, 0.08f, 0.10f);
      drawPanel(GameplayLayout.minimapPanelRect(), 0.09f, 0.11f, 0.15f);
      drawPanel(GameplayLayout.sidebarPanelRect(), 0.09f, 0.11f, 0.15f);
      drawPanel(GameplayLayout.chatboxPanelRect(), 0.09f, 0.11f, 0.15f);
    }
    chromeTextures.drawActiveGameplayTabHighlight(primitives, activeGameplayTab);
    chromeTextures.drawSideIcons(primitives);
    minimapRenderer.drawMinimap(viewModel, worldScene, worldMinimapTexture, cameraState);
    sidebarRenderer.drawSidebar(viewModel, activeGameplayTab);
    sidebarRenderer.drawChatbox(viewModel, activeGameplayTab);
  }

  private void drawPanel(ScreenRect rect, float red, float green, float blue) {
    glColor3f(red, green, blue);
    primitives.drawQuad(rect.left(), rect.top(), rect.width(), rect.height());
    glColor3f(0.33f, 0.40f, 0.52f);
    primitives.drawOutline(rect.left(), rect.top(), rect.width(), rect.height());
  }

  static float minimapUiYawDegrees(WorldCameraState cameraState) {
    return GameplayMinimapRenderer.minimapUiYawDegrees(cameraState);
  }

  static int legacyMapFunctionSpriteIndex(int rawMapFunctionId) {
    return GameplayMinimapRenderer.legacyMapFunctionSpriteIndex(rawMapFunctionId);
  }

  static float[] minimapMarkerOffset(float eastDelta, float northDelta, WorldCameraState cameraState) {
    return GameplayMinimapRenderer.minimapMarkerOffset(eastDelta, northDelta, cameraState);
  }

  @Override
  public void close() {
    sidebarRenderer.close();
    chromeTextures.close();
  }
}
