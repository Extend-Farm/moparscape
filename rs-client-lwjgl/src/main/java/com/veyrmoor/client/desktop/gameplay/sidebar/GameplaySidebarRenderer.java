package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssets;
import com.veyrmoor.client.desktop.gameplay.GameplayTab;
import com.veyrmoor.client.desktop.gameplay.ReportAbuseController;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.ItemDefinitionCatalog;

public final class GameplaySidebarRenderer implements AutoCloseable {

  private final SidebarTextFormatter textFormatter;
  private final SidebarTextureCache textureCache;
  private final SidebarRenderPrimitives primitives;
  private final CombatSidebarModelCache combatModels;
  private final ReportAbuseController reportAbuseController;
  private final ReportAbuseModalRenderer reportAbuseModalRenderer;
  private final TitleScreenBitmapFont statsSmallFont;
  private final TitleScreenBitmapFont statsLabelFont;
  private final OpenGlTexture statsButtonLeftTexture;
  private final OpenGlTexture statsButtonRightTexture;
  private final OpenGlTexture[] statIconTexturesBySkillId;
  private final SidebarWidgetRenderer sidebarWidgetRenderer;
  private final SidebarPanelCoordinator sidebarPanels;
  private final SidebarChatboxRenderer chatboxRenderer;

  public GameplaySidebarRenderer(
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayChatController chatController,
      ReportAbuseController reportAbuseController,
      GameplayFrameAssets gameplayFrameAssets,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d renderer
  ) {
    this.textFormatter = new SidebarTextFormatter(itemDefinitionCatalog);
    this.textureCache = new SidebarTextureCache(
        itemIconRenderer,
        titleScreenFonts == null ? null : titleScreenFonts.plainSmall()
    );
    this.primitives = new SidebarRenderPrimitives(renderer, textureCache);
    this.combatModels = new CombatSidebarModelCache(itemDefinitionCatalog);
    this.reportAbuseController = reportAbuseController;
    this.statsSmallFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsLabelFont = titleScreenFonts == null ? null : titleScreenFonts.plain();
    GameplayStatsTabAssets statsTabAssets = gameplayFrameAssets == null ? null : gameplayFrameAssets.statsTabAssets();
    this.statsButtonLeftTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonLeft());
    this.statsButtonRightTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonRight());
    ArchiveSpriteResolver mediaSpriteResolver = gameplayFrameAssets == null ? null : gameplayFrameAssets.mediaSpriteResolver();
    this.statIconTexturesBySkillId = buildStatIconTextures(statsTabAssets);
    this.sidebarWidgetRenderer = gameplayFrameAssets == null ? null : new SidebarWidgetRenderer(
        gameplayFrameAssets.interfaceComponents(),
        gameplayFrameAssets.mediaSpriteResolver(),
        itemIconRenderer,
        titleScreenFonts,
        renderer
    );
    this.reportAbuseModalRenderer = new ReportAbuseModalRenderer(reportAbuseController, sidebarWidgetRenderer);
    this.sidebarPanels = new SidebarPanelCoordinator(this);
    this.chatboxRenderer = new SidebarChatboxRenderer(
        chatController,
        titleScreenFonts == null ? null : titleScreenFonts.plain(),
        mediaSpriteResolver,
        renderer
    );
  }

  public GameplayClickResult handleSidebarClick(GameplayTab activeGameplayTab, double x, double y) {
    return sidebarPanels.handleSidebarClick(activeGameplayTab, x, y);
  }

  public GameplayClickResult handleChatboxClick(double x, double y) {
    return chatboxRenderer.handleChatboxClick(x, y, reportAbuseController);
  }

  public boolean handleSidebarScroll(GameplayTab activeGameplayTab, double x, double y, double yOffset) {
    return sidebarPanels.handleSidebarScroll(activeGameplayTab, x, y, yOffset);
  }

  public boolean handleSidebarPointerMove(GameplayTab activeGameplayTab, double x, double y) {
    return sidebarPanels.handleSidebarPointerMove(activeGameplayTab, x, y);
  }

  public void endSidebarPointerDrag() {
    sidebarPanels.endSidebarPointerDrag();
  }

  public void clearTransientState() {
    sidebarPanels.clearTransientState();
  }

  public void drawSidebar(ClientViewModel viewModel, GameplayTab activeGameplayTab, double pointerX, double pointerY) {
    sidebarPanels.drawSidebar(viewModel, activeGameplayTab, pointerX, pointerY);
  }

  public void drawChatbox(ClientViewModel viewModel) {
    chatboxRenderer.drawChatbox(viewModel);
  }

  public boolean canDrawReportAbuseModal() {
    return reportAbuseModalRenderer.canDraw();
  }

  public boolean containsReportAbuseModal(double x, double y) {
    return reportAbuseModalRenderer.contains(x, y);
  }

  public int reportAbuseActionWidgetIdAt(double x, double y) {
    return reportAbuseModalRenderer.actionWidgetIdAt(x, y);
  }

  public void drawReportAbuseModal(ClientViewModel viewModel, double pointerX, double pointerY) {
    reportAbuseModalRenderer.draw(viewModel, pointerX, pointerY);
  }

  @Override
  public void close() {
    textureCache.close();
    chatboxRenderer.close();
    closeTexture(statsButtonLeftTexture);
    closeTexture(statsButtonRightTexture);
    for (OpenGlTexture iconTexture : statIconTexturesBySkillId) {
      closeTexture(iconTexture);
    }
    if (sidebarWidgetRenderer != null) {
      sidebarWidgetRenderer.close();
    }
  }

  SidebarRenderPrimitives primitives() {
    return primitives;
  }

  SidebarTextureCache textures() {
    return textureCache;
  }

  SidebarTextFormatter textFormatter() {
    return textFormatter;
  }

  CombatSidebarModelCache combatModels() {
    return combatModels;
  }

  TitleScreenBitmapFont statsSmallFont() {
    return statsSmallFont;
  }

  TitleScreenBitmapFont statsLabelFont() {
    return statsLabelFont;
  }

  OpenGlTexture statsButtonLeftTexture() {
    return statsButtonLeftTexture;
  }

  OpenGlTexture statsButtonRightTexture() {
    return statsButtonRightTexture;
  }

  OpenGlTexture statIconTexture(int skillId) {
    if (skillId < 0 || skillId >= statIconTexturesBySkillId.length) {
      return null;
    }
    return statIconTexturesBySkillId[skillId];
  }

  SidebarWidgetRenderer sidebarWidgetRenderer() {
    return sidebarWidgetRenderer;
  }

  boolean canDrawLegacyStatsTab() {
    return statsButtonLeftTexture != null
        && statsButtonRightTexture != null
        && statsSmallFont != null
        && statsLabelFont != null;
  }

  static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  static ScreenRect centeredReportAbuseModalRect(ScreenRect worldViewportRect, float width, float height) {
    return ReportAbuseModalRenderer.centeredModalRect(worldViewportRect, width, height);
  }

  static String fitChatHistoryText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return SidebarChatboxRenderer.fitChatHistoryText(font, text, maxWidth);
  }

  static String fitChatPromptText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return SidebarChatboxRenderer.fitChatPromptText(font, text, maxWidth);
  }

  private OpenGlTexture[] buildStatIconTextures(GameplayStatsTabAssets statsTabAssets) {
    OpenGlTexture[] textures = new OpenGlTexture[21];
    if (statsTabAssets == null) {
      return textures;
    }
    for (int skillId = 0; skillId < textures.length; skillId++) {
      ArgbImage icon = statsTabAssets.iconForSkill(skillId);
      if (icon != null) {
        textures[skillId] = OpenGlTexture.fromArgbImage(icon);
      }
    }
    return textures;
  }
  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
