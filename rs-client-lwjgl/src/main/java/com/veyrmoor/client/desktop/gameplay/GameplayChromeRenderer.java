package com.veyrmoor.client.desktop.gameplay;

import com.veyrmoor.client.core.ClientChatMessage;
import com.veyrmoor.client.core.ClientChatMessageKind;
import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.client.desktop.gameplay.sidebar.GameplaySidebarRenderer;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.world.WorldCameraState;
import com.veyrmoor.client.desktop.world.WorldScene;
import com.veyrmoor.client.desktop.world.WorldSceneRenderSubmission;
import com.veyrmoor.client.desktop.world.WorldViewportScreenProjector;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.model.WorldPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongSupplier;

import static org.lwjgl.opengl.GL11.glColor3f;

public final class GameplayChromeRenderer implements AutoCloseable {

  private static final float SCENE_ACTION_HINT_LEFT_PADDING = 0.0f;
  private static final float SCENE_ACTION_HINT_BASELINE_OFFSET = 11.0f;
  private static final long OVERHEAD_CHAT_DURATION_NANOS = 3_000_000_000L;
  private static final int OVERHEAD_CHAT_RGB = 0xffff00;
  private static final float OVERHEAD_CHAT_BASELINE_OFFSET = 4.0f;
  private static final float OVERHEAD_CHAT_HORIZONTAL_PADDING = 4.0f;
  private static final String TEXT_ELLIPSIS = "...";

  private final ImmediateModeRenderer2d primitives;
  private final GameplayChromeTextures chromeTextures;
  private final GameplayMinimapRenderer minimapRenderer;
  private final GameplaySidebarRenderer sidebarRenderer;
  private final TitleScreenBitmapFont menuFont;
  private final LongSupplier nanoClock;
  private final GameplayCursorMarker cursorMarker = new GameplayCursorMarker();
  private final Map<String, OpenGlTexture> menuTextTextures = new HashMap<>();
  private long renderFrameCounter;
  private GameplayContextMenu contextMenu;
  private SceneActionHint sceneActionHint;
  private OverheadChatState overheadChatState;
  private ClientChatMessage lastSeenLocalPublicChatMessage;
  private GameplayTab activeGameplayTab = GameplayTab.INVENTORY;
  private double pointerX = Double.NaN;
  private double pointerY = Double.NaN;

  public GameplayChromeRenderer(
      GameplayFrameAssets gameplayFrameAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayChatController chatController,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this(
        gameplayFrameAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        chatController,
        titleScreenFonts,
        primitives,
        System::nanoTime
    );
  }

  GameplayChromeRenderer(
      GameplayFrameAssets gameplayFrameAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayChatController chatController,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives,
      LongSupplier nanoClock
  ) {
    this.primitives = primitives;
    this.menuFont = titleScreenFonts == null ? null : titleScreenFonts.bold();
    this.nanoClock = nanoClock == null ? System::nanoTime : nanoClock;
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
        chatController,
        gameplayFrameAssets,
        titleScreenFonts,
        primitives
    );
  }

  public GameplayClickResult handleClick(double x, double y) {
    for (GameplayTab gameplayTab : GameplayTab.values()) {
      if (GameplayLayout.gameplayTabRect(gameplayTab).contains(x, y)) {
        activeGameplayTab = gameplayTab;
        if (gameplayTab != GameplayTab.STATS) {
          sidebarRenderer.clearTransientState();
        }
        return GameplayClickResult.handledClick();
      }
    }
    GameplayClickResult chatboxClickResult = sidebarRenderer.handleChatboxClick(x, y);
    if (chatboxClickResult.handled()) {
      return chatboxClickResult;
    }
    return sidebarRenderer.handleSidebarClick(activeGameplayTab, x, y);
  }

  public boolean handleScroll(double x, double y, double yOffset) {
    return sidebarRenderer.handleSidebarScroll(activeGameplayTab, x, y, yOffset);
  }

  public boolean handlePointerMove(double x, double y) {
    return sidebarRenderer.handleSidebarPointerMove(activeGameplayTab, x, y);
  }

  public void endPointerDrag() {
    sidebarRenderer.endSidebarPointerDrag();
  }

  public void resetToInventoryTab() {
    activeGameplayTab = GameplayTab.INVENTORY;
    sidebarRenderer.clearTransientState();
  }

  public void activateCursorMarker(GameplayMouseButton button, double x, double y) {
    cursorMarker.activate(button, x, y);
  }

  public void openWalkHereContextMenu(double x, double y, WorldPoint targetWorldPoint) {
    contextMenu = GameplayContextMenu.walkHereMenu(
        GameplayLayout.worldViewportInnerRect(),
        x,
        y,
        targetWorldPoint,
        measureMenuTextWidth(GameplayContextMenu.TITLE),
        measureMenuTextWidth("Walk here"),
        measureMenuTextWidth("Cancel")
    );
  }

  public GameplayMenuAction consumeContextMenuSelection(double x, double y) {
    if (contextMenu == null) {
      return null;
    }
    GameplayContextMenu.Entry selectedEntry = contextMenu.entryAt(x, y);
    contextMenu = null;
    return selectedEntry == null ? null : selectedEntry.action();
  }

  public boolean hasOpenContextMenu() {
    return contextMenu != null;
  }

  public void closeContextMenu() {
    contextMenu = null;
  }

  public void clearTransientState() {
    contextMenu = null;
    sceneActionHint = null;
    overheadChatState = null;
    lastSeenLocalPublicChatMessage = null;
    sidebarRenderer.clearTransientState();
  }

  public void setPointerPosition(double x, double y) {
    pointerX = x;
    pointerY = y;
  }

  public void showSceneActionHint(String primaryText, int optionCount) {
    if (primaryText == null || primaryText.isBlank()) {
      sceneActionHint = null;
      return;
    }
    sceneActionHint = new SceneActionHint(primaryText, Math.max(0, optionCount));
  }

  public void clearSceneActionHint() {
    sceneActionHint = null;
  }

  public void render(
      ClientViewModel viewModel,
      WorldScene worldScene,
      OpenGlTexture worldMinimapTexture,
      WorldSceneRenderSubmission worldSceneSubmission
  ) {
    renderFrameCounter++;
    WorldCameraState cameraState = worldSceneSubmission == null ? null : worldSceneSubmission.cameraState();
    updateOverheadChat(viewModel);
    // Chrome is a deliberate mix today:
    // - authentic cache-backed frame art when media assets are available
    // - a native scene-state minimap inside that frame
    // - a mixed sidebar where combat can now render from decoded interface widgets while the
    //   remaining tabs still fall back to native/bootstrap surfaces
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
    drawOverheadChat(worldSceneSubmission);
    sidebarRenderer.drawSidebar(viewModel, activeGameplayTab, pointerX, pointerY);
    sidebarRenderer.drawChatbox(viewModel);
    drawCursorMarker();
    drawSceneActionHint();
    drawContextMenu();
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

  static String formatSceneActionHint(String primaryText, int optionCount) {
    if (primaryText == null || primaryText.isBlank() || optionCount < 2) {
      return primaryText;
    }
    if (optionCount == 2) {
      return primaryText;
    }
    return primaryText + " / " + (optionCount - 2) + " more options";
  }

  static float sceneActionHintLeft(ScreenRect worldViewport) {
    return worldViewport.left() + SCENE_ACTION_HINT_LEFT_PADDING;
  }

  static float sceneActionHintBaselineY(ScreenRect worldViewport) {
    return worldViewport.top() + SCENE_ACTION_HINT_BASELINE_OFFSET;
  }

  static ClientChatMessage latestLocalPublicChatMessage(ClientViewModel viewModel) {
    if (viewModel == null || viewModel.bootstrap() == null) {
      return null;
    }
    String localDisplayName = viewModel.bootstrap().displayName();
    if (localDisplayName == null || localDisplayName.isBlank()) {
      return null;
    }
    List<ClientChatMessage> chatMessages = viewModel.chatMessages();
    for (int index = chatMessages.size() - 1; index >= 0; index--) {
      ClientChatMessage message = chatMessages.get(index);
      if (message.kind() == ClientChatMessageKind.PUBLIC
          && localDisplayName.equals(message.speakerDisplayName())) {
        return message;
      }
    }
    return null;
  }

  private void drawCursorMarker() {
    GameplayCursorMarker.Snapshot snapshot = cursorMarker.snapshot();
    if (snapshot == null) {
      return;
    }
    OpenGlTexture clickCrossTexture = chromeTextures.clickCrossTexture(snapshot.button(), snapshot.frameIndex());
    if (clickCrossTexture != null) {
      primitives.drawTexturedQuad(
          clickCrossTexture,
          new ScreenRect(
              snapshot.centerX() - clickCrossTexture.width() * 0.5f,
              snapshot.centerY() - clickCrossTexture.height() * 0.5f,
              clickCrossTexture.width(),
              clickCrossTexture.height()
          )
      );
      return;
    }
    drawSyntheticCursorMarker(snapshot);
  }

  private void drawSyntheticCursorMarker(GameplayCursorMarker.Snapshot snapshot) {
    float left = snapshot.centerX() - 8.0f;
    float top = snapshot.centerY() - 8.0f;
    if (snapshot.button() == GameplayMouseButton.LEFT) {
      glColor3f(0.92f, 0.84f, 0.28f);
    } else {
      glColor3f(0.88f, 0.27f, 0.24f);
    }
    primitives.drawLine(left, top, left + 16.0f, top + 16.0f);
    primitives.drawLine(left + 16.0f, top, left, top + 16.0f);
    primitives.drawLine(left + 1.0f, top, left + 16.0f, top + 15.0f);
    primitives.drawLine(left + 15.0f, top, left, top + 15.0f);
  }

  private void drawContextMenu() {
    if (contextMenu == null) {
      return;
    }
    ScreenRect rect = contextMenu.rect();
    glColor3f(rgbUnit(GameplayContextMenu.OUTER_RGB, 16), rgbUnit(GameplayContextMenu.OUTER_RGB, 8), rgbUnit(GameplayContextMenu.OUTER_RGB, 0));
    primitives.drawQuad(rect.left(), rect.top(), rect.width(), rect.height());
    glColor3f(0.0f, 0.0f, 0.0f);
    ScreenRect headerFillRect = contextMenu.headerFillRect();
    ScreenRect bodyFillRect = contextMenu.bodyFillRect();
    primitives.drawQuad(headerFillRect.left(), headerFillRect.top(), headerFillRect.width(), headerFillRect.height());
    primitives.drawQuad(bodyFillRect.left(), bodyFillRect.top(), bodyFillRect.width(), bodyFillRect.height());
    drawMenuText(
        contextMenu.title(),
        GameplayContextMenu.TITLE_RGB,
        false,
        contextMenu.titleLeft(),
        contextMenu.titleBaselineY()
    );
    int hoveredEntryIndex = contextMenu.entryIndexAt(pointerX, pointerY);
    for (int index = 0; index < contextMenu.entries().size(); index++) {
      GameplayContextMenu.Entry entry = contextMenu.entries().get(index);
      drawMenuEntry(entry, contextMenu.entryLeft(), contextMenu.entryBaselineY(index), hoveredEntryIndex == index);
    }
  }

  private void drawSceneActionHint() {
    if (contextMenu != null || sceneActionHint == null) {
      return;
    }
    String hintText = formatSceneActionHint(sceneActionHint.primaryText(), sceneActionHint.optionCount());
    if (hintText == null || hintText.isBlank()) {
      return;
    }
    ScreenRect worldViewport = GameplayLayout.worldViewportInnerRect();
    drawAnimatedMenuText(
        hintText,
        GameplayContextMenu.ACTION_RGB,
        true,
        sceneActionHintLeft(worldViewport),
        sceneActionHintBaselineY(worldViewport)
    );
  }

  private void updateOverheadChat(ClientViewModel viewModel) {
    long now = nanoClock.getAsLong();
    ClientChatMessage latestLocalPublicChatMessage = latestLocalPublicChatMessage(viewModel);
    if (latestLocalPublicChatMessage != null && latestLocalPublicChatMessage != lastSeenLocalPublicChatMessage) {
      overheadChatState = new OverheadChatState(latestLocalPublicChatMessage, now + OVERHEAD_CHAT_DURATION_NANOS);
      lastSeenLocalPublicChatMessage = latestLocalPublicChatMessage;
    }
    if (overheadChatState != null && now >= overheadChatState.expiresAtNanos()) {
      overheadChatState = null;
    }
  }

  private void drawOverheadChat(WorldSceneRenderSubmission worldSceneSubmission) {
    if (overheadChatState == null || worldSceneSubmission == null || worldSceneSubmission.localPlayerOverheadAnchor() == null) {
      return;
    }
    WorldSceneRenderSubmission.ActorOverheadAnchor overheadAnchor = worldSceneSubmission.localPlayerOverheadAnchor();
    ScreenRect worldViewport = GameplayLayout.worldViewportInnerRect();
    WorldViewportScreenProjector.ScreenProjection screenProjection = WorldViewportScreenProjector.project(
        worldViewport,
        worldSceneSubmission.cameraState(),
        overheadAnchor.localX(),
        overheadAnchor.worldHeight(),
        overheadAnchor.localY()
    );
    if (screenProjection == null) {
      return;
    }
    String overheadText = overheadChatState.sourceMessage().text();
    if (overheadText == null || overheadText.isBlank()) {
      return;
    }
    String fittedOverheadText = fitOverheadChatText(
        overheadText,
        worldViewport.width() - OVERHEAD_CHAT_HORIZONTAL_PADDING * 2.0f
    );
    if (fittedOverheadText.isBlank()) {
      return;
    }
    int textWidth = measureMenuTextWidth(fittedOverheadText);
    float minLeft = worldViewport.left() + OVERHEAD_CHAT_HORIZONTAL_PADDING;
    float maxLeft = worldViewport.left() + worldViewport.width() - textWidth;
    float drawLeft = Math.max(minLeft, Math.min(maxLeft, screenProjection.screenX() - textWidth * 0.5f));
    float baselineY = Math.max(
        worldViewport.top() + (menuFont == null ? 10.0f : menuFont.maxGlyphHeight()),
        screenProjection.screenY() - OVERHEAD_CHAT_BASELINE_OFFSET
    );
    drawMenuText(fittedOverheadText, OVERHEAD_CHAT_RGB, true, drawLeft, baselineY);
  }

  GameplayTab activeGameplayTab() {
    return activeGameplayTab;
  }

  @Override
  public void close() {
    sidebarRenderer.close();
    chromeTextures.close();
    for (OpenGlTexture menuTextTexture : menuTextTextures.values()) {
      closeTexture(menuTextTexture);
    }
    menuTextTextures.clear();
  }

  private int measureMenuTextWidth(String text) {
    if (menuFont != null) {
      return menuFont.measureText(text);
    }
    return Math.round(primitives.measureTextWidth(text));
  }

  String fitOverheadChatText(String text, float maxWidth) {
    if (menuFont != null) {
      return fitOverheadChatText(menuFont, text, maxWidth);
    }
    if (text == null || text.isBlank() || maxWidth <= 0.0f) {
      return "";
    }
    if (measureMenuTextWidth(text) <= maxWidth) {
      return text;
    }
    int ellipsisWidth = measureMenuTextWidth(TEXT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = TEXT_ELLIPSIS;
    for (int endIndex = 1; endIndex <= text.length(); endIndex++) {
      String candidate = text.substring(0, endIndex) + TEXT_ELLIPSIS;
      if (measureMenuTextWidth(candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  static String fitOverheadChatText(TitleScreenBitmapFont font, String text, float maxWidth) {
    if (font == null || text == null || text.isBlank() || maxWidth <= 0.0f) {
      return "";
    }
    if (font.measureText(text) <= maxWidth) {
      return text;
    }
    int ellipsisWidth = font.measureText(TEXT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = TEXT_ELLIPSIS;
    for (int endIndex = 1; endIndex <= text.length(); endIndex++) {
      String candidate = text.substring(0, endIndex) + TEXT_ELLIPSIS;
      if (font.measureText(candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  private void drawMenuText(String text, int rgb, boolean shadow, float left, float baselineY) {
    if (menuFont == null) {
      float red = rgbUnit(rgb, 16);
      float green = rgbUnit(rgb, 8);
      float blue = rgbUnit(rgb, 0);
      if (shadow) {
        primitives.drawText(left + 1.0f, baselineY - 10.0f + 1.0f, text, 0.0f, 0.0f, 0.0f);
      }
      primitives.drawText(left, baselineY - 10.0f, text, red, green, blue);
      return;
    }
    OpenGlTexture menuTextTexture = menuTextTexture(text, rgb, shadow);
    if (menuTextTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        menuTextTexture,
        new ScreenRect(left, baselineY - menuFont.maxGlyphHeight(), menuTextTexture.width(), menuTextTexture.height())
    );
  }

  private void drawAnimatedMenuText(String text, int rgb, boolean shadow, float left, float baselineY) {
    if (menuFont == null) {
      drawMenuText(text, rgb, shadow, left, baselineY);
      return;
    }
    OpenGlTexture menuTextTexture = animatedMenuTextTexture(text, rgb, shadow, sceneActionHintSeed());
    if (menuTextTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        menuTextTexture,
        new ScreenRect(left, baselineY - menuFont.maxGlyphHeight(), menuTextTexture.width(), menuTextTexture.height())
    );
  }

  private void drawMenuEntry(GameplayContextMenu.Entry entry, float left, float baselineY, boolean hovered) {
    float cursorX = left;
    for (GameplayContextMenu.TextRun textRun : entry.textRuns()) {
      int rgb = textRun.actionColorControlled() && hovered ? GameplayContextMenu.ACTION_HOVER_RGB : textRun.rgb();
      drawMenuText(textRun.text(), rgb, true, cursorX, baselineY);
      cursorX += measureMenuTextWidth(textRun.text());
    }
  }

  private OpenGlTexture menuTextTexture(String text, int rgb, boolean shadow) {
    if (menuFont == null || text == null || text.isEmpty()) {
      return null;
    }
    String cacheKey = rgb + "|" + shadow + "|" + text;
    return menuTextTextures.computeIfAbsent(cacheKey, ignored -> createMenuTextTexture(text, rgb, shadow));
  }

  private OpenGlTexture animatedMenuTextTexture(String text, int rgb, boolean shadow, int seed) {
    if (menuFont == null || text == null || text.isEmpty()) {
      return null;
    }
    String cacheKey = "animated|" + seed + "|" + rgb + "|" + shadow + "|" + text;
    return menuTextTextures.computeIfAbsent(cacheKey, ignored -> createAnimatedMenuTextTexture(text, rgb, shadow, seed));
  }

  private OpenGlTexture createMenuTextTexture(String text, int rgb, boolean shadow) {
    int visibleWidth = menuFont.measureVisibleWidth(text);
    int advanceWidth = menuFont.measureText(text);
    int canvasRight = Math.max(advanceWidth, visibleWidth);
    int shadowPadding = shadow ? 1 : 0;
    int width = Math.max(1, canvasRight + shadowPadding);
    int height = Math.max(1, menuFont.maxGlyphHeight() + (shadow ? 1 : 0));
    int[] pixels = new int[width * height];
    menuFont.drawText(pixels, width, height, text, 0, menuFont.maxGlyphHeight(), rgb, shadow);
    return OpenGlTexture.fromArgbImage(new com.veyrmoor.client.desktop.assets.image.ArgbImage(width, height, pixels));
  }

  private OpenGlTexture createAnimatedMenuTextTexture(String text, int rgb, boolean shadow, int seed) {
    int width = Math.max(1, menuFont.measureAnimatedMenuText(text, seed) + 2);
    int height = Math.max(1, menuFont.maxGlyphHeight() + (shadow ? 1 : 0));
    int[] pixels = new int[width * height];
    menuFont.drawAnimatedMenuText(pixels, width, height, text, 0, menuFont.maxGlyphHeight(), rgb, seed, shadow);
    return OpenGlTexture.fromArgbImage(new com.veyrmoor.client.desktop.assets.image.ArgbImage(width, height, pixels));
  }

  private int sceneActionHintSeed() {
    return (int) (renderFrameCounter / 1000L);
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >> shift) & 0xff) / 255.0f;
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private record SceneActionHint(String primaryText, int optionCount) {
  }

  private record OverheadChatState(ClientChatMessage sourceMessage, long expiresAtNanos) {
  }
}
