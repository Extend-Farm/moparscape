package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;

import com.veyrmoor.client.core.ClientChatMessage;
import com.veyrmoor.client.core.ClientChatMessageKind;
import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.core.EquipmentLoadout;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayChatState;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssets;
import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.ReportAbuseController;
import com.veyrmoor.client.desktop.gameplay.GameplayTab;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetScrollState;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.content.ItemDefinition;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.model.StaffRole;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GameplaySidebarRenderer implements AutoCloseable {

  private static final float SIDEBAR_CONTENT_LEFT_INSET = 12.0f;
  private static final float SIDEBAR_CONTENT_TOP_INSET = 22.0f;
  private static final float CHAT_LINE_HEIGHT = 14.0f;
  private static final float CHAT_HISTORY_BOTTOM_BASELINE_OFFSET = 70.0f;
  private static final float CHAT_PROMPT_LEFT_OFFSET = 4.0f;
  private static final float CHAT_RIGHT_PADDING = 4.0f;
  private static final int CHAT_HISTORY_LINES = 5;
  private static final int CHAT_LABEL_RGB = 0x000000;
  private static final int CHAT_PUBLIC_TEXT_RGB = 0x0000ff;
  private static final int CHAT_PROMPT_TEXT_RGB = 0x0000ff;
  private static final int CHAT_SYSTEM_RGB = 0x000000;
  private static final int CHAT_SEPARATOR_RGB = 0x000000;
  private static final float CHAT_SEPARATOR_TOP_OFFSET = 77.0f;
  private static final int CHAT_SCROLLBAR_TRACK_RGB = 0x23201b;
  private static final int CHAT_SCROLLBAR_THUMB_FILL_RGB = 0x4d4233;
  private static final int CHAT_SCROLLBAR_HIGHLIGHT_RGB = 0x766654;
  private static final int CHAT_SCROLLBAR_DARK_RGB = 0x332d25;
  private static final int CHAT_SCROLLBAR_WIDTH = 16;
  private static final int CHAT_SCROLLBAR_ARROW_HEIGHT = 16;
  private static final float CHAT_CROWN_BASELINE_OFFSET_Y = 12.0f;
  private static final float CHAT_CROWN_TEXT_GAP = 2.0f;
  private static final String CHAT_ELLIPSIS = "...";
  private static final String REPORT_ABUSE_LABEL = "Report abuse";
  private static final float REPORT_ABUSE_CENTER_X = 458.0f;
  private static final float REPORT_ABUSE_BASELINE_Y = 486.0f;
  private static final NumberFormat LEGACY_NUMBER_FORMAT = NumberFormat.getIntegerInstance();
  private static final ClientChatMessage DEFAULT_WELCOME_MESSAGE =
      ClientChatMessage.system("Welcome to Veyrmoor! Have fun.");

  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final ItemIconRenderer itemIconRenderer;
  private final GameplayChatController chatController;
  private final ReportAbuseController reportAbuseController;
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final TitleScreenBitmapFont chatFont;
  private final TitleScreenBitmapFont statsSmallFont;
  private final TitleScreenBitmapFont statsLabelFont;
  private final ImmediateModeRenderer2d primitives;
  private final OpenGlTexture statsButtonLeftTexture;
  private final OpenGlTexture statsButtonRightTexture;
  private final OpenGlTexture chatScrollbarTopArrowTexture;
  private final OpenGlTexture chatScrollbarBottomArrowTexture;
  private final OpenGlTexture[] modIconTextures;
  private final OpenGlTexture[] statIconTexturesBySkillId;
  private final SidebarWidgetRenderer sidebarWidgetRenderer;
  private final Map<Integer, OpenGlTexture> itemIconTextures = new HashMap<>();
  private final Map<Integer, GameplayCombatSidebarModel> combatModelsByWeaponItemId = new HashMap<>();
  private final Map<String, OpenGlTexture> inventoryAmountTextures = new HashMap<>();
  private final Map<BitmapTextKey, OpenGlTexture> bitmapTextTextures = new HashMap<>();
  private final InventoryEquipmentSidebarPanelRenderer inventoryEquipmentPanelRenderer;
  private final StatsSidebarPanelRenderer statsPanelRenderer;
  private final CombatSidebarPanelRenderer combatPanelRenderer;
  private final PrayerSidebarPanelRenderer prayerPanelRenderer;
  private final MagicSidebarPanelRenderer magicPanelRenderer;
  private final QuestSidebarPanelRenderer questPanelRenderer;
  private final FriendsSidebarPanelRenderer friendsPanelRenderer;
  private final IgnoresSidebarPanelRenderer ignoresPanelRenderer;
  private final MusicSidebarPanelRenderer musicPanelRenderer;
  private final SettingsSidebarPanelRenderer settingsPanelRenderer;
  private final EmotesSidebarPanelRenderer emotesPanelRenderer;
  private final LogoutSidebarPanelRenderer logoutPanelRenderer;
  private final Map<BitmapGlyphKey, BitmapGlyphTexture> bitmapGlyphTextures = new HashMap<>();
  private boolean promptGlyphsPrewarmed;

  public GameplaySidebarRenderer(
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayChatController chatController,
      ReportAbuseController reportAbuseController,
      GameplayFrameAssets gameplayFrameAssets,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
    this.itemIconRenderer = itemIconRenderer;
    this.chatController = chatController;
    this.reportAbuseController = reportAbuseController;
    this.inventoryAmountFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.chatFont = titleScreenFonts == null ? null : titleScreenFonts.plain();
    this.statsSmallFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsLabelFont = titleScreenFonts == null ? null : titleScreenFonts.plain();
    this.primitives = primitives;
    GameplayStatsTabAssets statsTabAssets = gameplayFrameAssets == null ? null : gameplayFrameAssets.statsTabAssets();
    this.statsButtonLeftTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonLeft());
    this.statsButtonRightTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonRight());
    ArchiveSpriteResolver mediaSpriteResolver = gameplayFrameAssets == null ? null : gameplayFrameAssets.mediaSpriteResolver();
    this.chatScrollbarTopArrowTexture = spriteTexture(mediaSpriteResolver, "scrollbar", 0);
    this.chatScrollbarBottomArrowTexture = spriteTexture(mediaSpriteResolver, "scrollbar", 1);
    this.modIconTextures = new OpenGlTexture[]{
        spriteTexture(mediaSpriteResolver, "mod_icons", 0),
        spriteTexture(mediaSpriteResolver, "mod_icons", 1)
    };
    this.statIconTexturesBySkillId = buildStatIconTextures(statsTabAssets);
    this.sidebarWidgetRenderer = gameplayFrameAssets == null ? null : new SidebarWidgetRenderer(
        gameplayFrameAssets.interfaceComponents(),
        gameplayFrameAssets.mediaSpriteResolver(),
        itemIconRenderer,
        titleScreenFonts,
        primitives
    );
    this.inventoryEquipmentPanelRenderer = new InventoryEquipmentSidebarPanelRenderer(this);
    this.statsPanelRenderer = new StatsSidebarPanelRenderer(this);
    this.combatPanelRenderer = new CombatSidebarPanelRenderer(this);
    this.prayerPanelRenderer = new PrayerSidebarPanelRenderer(this);
    this.magicPanelRenderer = new MagicSidebarPanelRenderer(this);
    this.questPanelRenderer = new QuestSidebarPanelRenderer(this);
    this.friendsPanelRenderer = new FriendsSidebarPanelRenderer(this);
    this.ignoresPanelRenderer = new IgnoresSidebarPanelRenderer(this);
    this.musicPanelRenderer = new MusicSidebarPanelRenderer(this);
    this.settingsPanelRenderer = new SettingsSidebarPanelRenderer(this);
    this.emotesPanelRenderer = new EmotesSidebarPanelRenderer(this);
    this.logoutPanelRenderer = new LogoutSidebarPanelRenderer(this);
    prewarmChatPromptGlyphTextures();
  }

  public GameplayClickResult handleSidebarClick(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case STATS -> statsPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case QUESTS -> questPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case MAGIC -> magicPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case FRIENDS -> friendsPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case IGNORES -> ignoresPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case SETTINGS -> settingsPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case EMOTES -> emotesPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case MUSIC -> musicPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case LOGOUT -> logoutPanelRenderer.handleSidebarClick(sidebarRect, x, y);
      default -> GameplayClickResult.ignored();
    };
  }

  public GameplayClickResult handleChatboxClick(double x, double y) {
    if (reportAbuseLabelRect().contains(x, y)) {
      if (reportAbuseController != null) {
        reportAbuseController.open();
      }
      return GameplayClickResult.handledClick();
    }
    if (!GameplayLayout.chatboxPanelRect().contains(x, y)) {
      return GameplayClickResult.ignored();
    }
    chatController.activateTyping();
    return GameplayClickResult.handledClick();
  }

  public boolean handleSidebarScroll(GameplayTab activeGameplayTab, double x, double y, double yOffset) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case QUESTS -> questPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case MAGIC -> magicPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case FRIENDS -> friendsPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case IGNORES -> ignoresPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case SETTINGS -> settingsPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case EMOTES -> emotesPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      case MUSIC -> musicPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      default -> false;
    };
  }

  public boolean handleSidebarPointerMove(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case QUESTS -> questPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case MAGIC -> magicPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case FRIENDS -> friendsPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case IGNORES -> ignoresPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case SETTINGS -> settingsPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case EMOTES -> emotesPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      case MUSIC -> musicPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      default -> false;
    };
  }

  public void endSidebarPointerDrag() {
    if (sidebarWidgetRenderer != null) {
      sidebarWidgetRenderer.endPointerDrag();
    }
  }

  public void clearTransientState() {
    if (sidebarWidgetRenderer != null) {
      sidebarWidgetRenderer.clearTransientState();
    }
  }

  public void drawSidebar(ClientViewModel viewModel, GameplayTab activeGameplayTab, double pointerX, double pointerY) {
    ScreenRect rect = GameplayLayout.sidebarPanelRect();
    float left = rect.left() + SIDEBAR_CONTENT_LEFT_INSET;
    float top = rect.top() + SIDEBAR_CONTENT_TOP_INSET;
    if (activeGameplayTab != GameplayTab.INVENTORY
        && activeGameplayTab != GameplayTab.STATS
        && activeGameplayTab != GameplayTab.COMBAT
        && activeGameplayTab != GameplayTab.EQUIPMENT
        && activeGameplayTab != GameplayTab.QUESTS
        && activeGameplayTab != GameplayTab.PRAYER
        && activeGameplayTab != GameplayTab.MAGIC
        && activeGameplayTab != GameplayTab.FRIENDS
        && activeGameplayTab != GameplayTab.IGNORES
        && activeGameplayTab != GameplayTab.MUSIC
        && activeGameplayTab != GameplayTab.SETTINGS
        && activeGameplayTab != GameplayTab.EMOTES
        && activeGameplayTab != GameplayTab.LOGOUT) {
      primitives.drawText(left, top, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
    }
    switch (activeGameplayTab) {
      case INVENTORY -> inventoryEquipmentPanelRenderer.drawInventorySidebar(viewModel, rect);
      case EQUIPMENT -> inventoryEquipmentPanelRenderer.drawEquipmentSidebar(viewModel, rect);
      case STATS -> statsPanelRenderer.drawStatsSidebar(viewModel, rect, pointerX, pointerY);
      case COMBAT -> combatPanelRenderer.drawCombatSidebar(viewModel, rect);
      case QUESTS -> questPanelRenderer.drawQuestSidebar(viewModel, rect, pointerX, pointerY);
      case PRAYER -> prayerPanelRenderer.drawPrayerSidebar(viewModel, rect, pointerX, pointerY);
      case MAGIC -> magicPanelRenderer.drawMagicSidebar(viewModel, rect);
      case FRIENDS -> friendsPanelRenderer.drawFriendsSidebar(viewModel, rect, pointerX, pointerY);
      case IGNORES -> ignoresPanelRenderer.drawIgnoresSidebar(viewModel, rect, pointerX, pointerY);
      case LOGOUT -> logoutPanelRenderer.drawLogoutSidebar(viewModel, rect);
      case SETTINGS -> settingsPanelRenderer.drawSettingsSidebar(viewModel, rect, pointerX, pointerY);
      case EMOTES -> emotesPanelRenderer.drawEmotesSidebar(viewModel, rect, pointerX, pointerY);
      case MUSIC -> musicPanelRenderer.drawMusicSidebar(viewModel, rect, pointerX, pointerY);
    }
  }

  public void drawChatbox(ClientViewModel viewModel) {
    ScreenRect chatboxRect = GameplayLayout.chatboxPanelRect();
    ScreenRect historyRect = GameplayLayout.chatHistoryRect();
    ScreenRect inputRect = GameplayLayout.chatInputRect();
    drawChatHistory(viewModel, historyRect);
    drawChatPromptSeparator(chatboxRect);
    drawChatPrompt(viewModel, inputRect);
    drawChatOptionsBar();
  }

  public boolean canDrawReportAbuseModal() {
    return reportAbuseController != null
        && reportAbuseController.isAvailable()
        && sidebarWidgetRenderer != null
        && sidebarWidgetRenderer.canRender(reportAbuseController.interfaceId());
  }

  public boolean containsReportAbuseModal(double x, double y) {
    return canDrawReportAbuseModal() && reportAbuseModalRect().contains(x, y);
  }

  public int reportAbuseActionWidgetIdAt(double x, double y) {
    if (!canDrawReportAbuseModal()) {
      return -1;
    }
    return sidebarWidgetRenderer.actionWidgetIdAt(reportAbuseController.interfaceId(), reportAbuseModalRect(), x, y);
  }

  public void drawReportAbuseModal(ClientViewModel viewModel, double pointerX, double pointerY) {
    if (!canDrawReportAbuseModal()) {
      return;
    }
    // The reference client mutates these component strings live instead of rebuilding the
    // interface tree, so the native widget renderer needs the same narrow runtime override hook.
    sidebarWidgetRenderer.draw(
        reportAbuseModalRect(),
        reportAbuseController.interfaceId(),
        viewModel,
        componentId -> {
          if (componentId == reportAbuseController.nameFieldComponentId()) {
            return new SidebarWidgetRenderer.WidgetOverride(reportAbuseController.nameFieldText(), null);
          }
          if (componentId == reportAbuseController.muteToggleComponentId()) {
            return new SidebarWidgetRenderer.WidgetOverride(
                reportAbuseController.muteToggleText(viewModel),
                reportAbuseController.muteToggleRgb()
            );
          }
          return null;
        },
        SidebarWidgetRenderer.WidgetInventoryGridResolver.NONE,
        pointerX,
        pointerY
    );
  }

  @Override
  public void close() {
    for (OpenGlTexture itemIconTexture : itemIconTextures.values()) {
      closeTexture(itemIconTexture);
    }
    for (OpenGlTexture inventoryAmountTexture : inventoryAmountTextures.values()) {
      closeTexture(inventoryAmountTexture);
    }
    for (OpenGlTexture bitmapTextTexture : bitmapTextTextures.values()) {
      closeTexture(bitmapTextTexture);
    }
    for (BitmapGlyphTexture bitmapGlyphTexture : bitmapGlyphTextures.values()) {
      closeTexture(bitmapGlyphTexture.texture());
    }
    closeTexture(statsButtonLeftTexture);
    closeTexture(statsButtonRightTexture);
    closeTexture(chatScrollbarTopArrowTexture);
    closeTexture(chatScrollbarBottomArrowTexture);
    for (OpenGlTexture modIconTexture : modIconTextures) {
      closeTexture(modIconTexture);
    }
    for (OpenGlTexture iconTexture : statIconTexturesBySkillId) {
      closeTexture(iconTexture);
    }
    if (sidebarWidgetRenderer != null) {
      sidebarWidgetRenderer.close();
    }
  }

  ImmediateModeRenderer2d primitives() {
    return primitives;
  }

  TitleScreenBitmapFont inventoryAmountFont() {
    return inventoryAmountFont;
  }

  boolean canRenderItemIcons() {
    return itemIconRenderer != null;
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

  String resolveItemName(int itemId) {
    if (itemDefinitionCatalog == null) {
      return "item-" + itemId;
    }
    return itemDefinitionCatalog.find(itemId)
        .map(ItemDefinition::name)
        .orElse("item-" + itemId);
  }

  String formatQuantity(int quantity) {
    if (quantity >= InventoryEquipmentSidebarPanelRenderer.MILLION_STACK_THRESHOLD) {
      return (quantity / InventoryEquipmentSidebarPanelRenderer.MILLION_STACK_DIVISOR) + "M";
    }
    if (quantity >= InventoryEquipmentSidebarPanelRenderer.THOUSAND_STACK_THRESHOLD) {
      return (quantity / InventoryEquipmentSidebarPanelRenderer.THOUSAND_STACK_DIVISOR) + "K";
    }
    return Integer.toString(quantity);
  }

  OpenGlTexture itemIconTexture(int itemId, int quantity) {
    if (itemIconRenderer == null) {
      return null;
    }
    int iconKey = itemIconRenderer.iconKey(itemId, quantity);
    if (iconKey < 0) {
      return null;
    }
    if (itemIconTextures.containsKey(iconKey)) {
      return itemIconTextures.get(iconKey);
    }
    ArgbImage iconImage = itemIconRenderer.render(itemId, quantity);
    OpenGlTexture createdTexture = iconImage == null ? null : OpenGlTexture.fromArgbImage(iconImage);
    itemIconTextures.put(iconKey, createdTexture);
    return createdTexture;
  }

  void drawShadowedText(float left, float top, String text, float red, float green, float blue) {
    drawShadowedText(left, top, text, red, green, blue, 1.0f);
  }

  void drawShadowedText(float left, float top, String text, float red, float green, float blue, float scale) {
    float shadowOffset = scale < 0.8f ? 0.75f : 1.0f;
    primitives.drawText(left + shadowOffset, top + shadowOffset, text, 0.0f, 0.0f, 0.0f, scale);
    primitives.drawText(left, top, text, red, green, blue, scale);
  }

  void drawLegacyStatsText(TitleScreenBitmapFont font, float left, float top, String text, int rgb) {
    OpenGlTexture textTexture = bitmapTextTexture(font, text, rgb, true);
    drawTextureAt(textTexture, left, top);
  }

  void drawLegacyStatsTextCentered(
      TitleScreenBitmapFont font,
      float left,
      float top,
      int width,
      String text,
      int rgb
  ) {
    if (font == null || text == null) {
      return;
    }
    float centeredLeft = left + width * 0.5f - font.measureText(text) * 0.5f;
    drawLegacyStatsText(font, centeredLeft, top, text, rgb);
  }

  void drawTextureAt(OpenGlTexture texture, float left, float top) {
    if (texture == null) {
      return;
    }
    primitives.drawTexturedQuad(texture, new ScreenRect(left, top, texture.width(), texture.height()));
  }

  OpenGlTexture inventoryAmountTexture(String text) {
    if (inventoryAmountFont == null || text == null || text.isEmpty()) {
      return null;
    }
    return inventoryAmountTextures.computeIfAbsent(text, this::createInventoryAmountTexture);
  }

  String formatLegacyNumber(int value) {
    synchronized (LEGACY_NUMBER_FORMAT) {
      return LEGACY_NUMBER_FORMAT.format(value);
    }
  }

  void fillRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawQuad(left, top, width, height);
  }

  void outlineRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawOutline(left, top, width, height);
  }

  void drawGlyphLine(float startX, float startY, float endX, float endY, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawLine(startX, startY, endX, endY);
  }

  GameplayCombatSidebarModel combatModelFor(ClientViewModel viewModel) {
    int weaponItemId = weaponItemId(viewModel.equipment());
    return combatModelsByWeaponItemId.computeIfAbsent(
        weaponItemId,
        ignored -> GameplayCombatSidebarModel.from(viewModel.equipment(), itemDefinitionCatalog)
    );
  }

  static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  private void drawSidebarPlaceholder(float left, float top, String... lines) {
    for (int index = 0; index < lines.length; index++) {
      primitives.drawText(left, top + index * 14.0f, lines[index], 0.84f, 0.88f, 0.94f);
    }
  }

  private void drawChatHistory(ClientViewModel viewModel, ScreenRect historyRect) {
    List<ClientChatMessage> renderedMessages = renderedChatMessages(viewModel);
    int visibleCount = Math.min(renderedMessages.size(), CHAT_HISTORY_LINES);
    int startIndex = renderedMessages.size() - visibleCount;
    float historyRight = historyRect.left() + historyRect.width() - CHAT_RIGHT_PADDING;
    for (int index = 0; index < visibleCount; index++) {
      ClientChatMessage message = renderedMessages.get(startIndex + index);
      float baselineY = historyRect.top()
          + CHAT_HISTORY_BOTTOM_BASELINE_OFFSET
          - (visibleCount - 1 - index) * CHAT_LINE_HEIGHT;
      drawChatMessageLine(viewModel, historyRect.left(), historyRight, baselineY, message);
    }
    drawChatScrollbar(historyRect, Math.max(1, renderedMessages.size()));
  }

  private void drawChatPrompt(ClientViewModel viewModel, ScreenRect inputRect) {
    GameplayChatState chatState = chatController.state();
    String displayName = viewModel.bootstrap() == null ? "You" : viewModel.bootstrap().displayName();
    float baselineY = GameplayLayout.chatPromptBaselineY();
    drawBitmapTextAtBaseline(chatFont, inputRect.left() + CHAT_PROMPT_LEFT_OFFSET, baselineY, displayName + ":", CHAT_LABEL_RGB, false);
    float promptLeft = inputRect.left()
        + CHAT_PROMPT_LEFT_OFFSET
        + measureBitmapText(chatFont, displayName + ": ")
        + 2.0f;
    String promptText = fitChatPromptText(chatFont, chatState.draftText() + "*", inputRect.left() + inputRect.width() - CHAT_RIGHT_PADDING - promptLeft);
    drawBitmapTextGlyphsAtBaseline(
        chatFont,
        promptLeft,
        baselineY,
        promptText,
        CHAT_PROMPT_TEXT_RGB,
        false
    );
  }

  private void drawChatPromptSeparator(ScreenRect chatboxRect) {
    fillRect(
        chatboxRect.left(),
        chatboxRect.top() + CHAT_SEPARATOR_TOP_OFFSET,
        chatboxRect.width(),
        1.0f,
        CHAT_SEPARATOR_RGB
    );
  }

  private void drawChatOptionsBar() {
    drawBitmapTextCentered(chatFont, 55.0f, 481.0f, "Public chat", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 55.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, 184.0f, 481.0f, "Private chat", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 184.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, 324.0f, 481.0f, "Trade/compete", 0xffffff, true);
    drawBitmapTextCentered(chatFont, 324.0f, 494.0f, "On", 0x00ff00, true);
    drawBitmapTextCentered(chatFont, REPORT_ABUSE_CENTER_X, REPORT_ABUSE_BASELINE_Y, REPORT_ABUSE_LABEL, 0xffffff, true);
  }

  private OpenGlTexture createInventoryAmountTexture(String text) {
    int width = Math.max(1, inventoryAmountFont.measureText(text) + 1);
    int height = Math.max(1, inventoryAmountFont.maxGlyphHeight() + 1);
    int[] pixels = new int[width * height];
    inventoryAmountFont.drawText(
        pixels,
        width,
        height,
        text,
        0,
        inventoryAmountFont.maxGlyphHeight(),
        InventoryEquipmentSidebarPanelRenderer.INVENTORY_STACK_TEXT_RGB,
        true
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(width, height, pixels));
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

  private OpenGlTexture bitmapTextTexture(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return null;
    }
    return bitmapTextTextures.computeIfAbsent(
        new BitmapTextKey(font, text, rgb, shadow),
        this::createBitmapTextTexture
    );
  }

  private OpenGlTexture createBitmapTextTexture(BitmapTextKey key) {
    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(key.font(), key.text(), key.shadow());
    int[] pixels = new int[layout.width() * layout.height()];
    key.font().drawText(
        pixels,
        layout.width(),
        layout.height(),
        key.text(),
        -layout.canvasLeft(),
        layout.baselineY(),
        key.rgb(),
        key.shadow()
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels));
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private static int weaponItemId(List<BootstrapItemSlot> equipment) {
    for (BootstrapItemSlot itemSlot : equipment) {
      if (itemSlot != null && itemSlot.slotIndex() == EquipmentLoadout.WEAPON_SLOT) {
        return itemSlot.itemId();
      }
    }
    return -1;
  }

  private void drawChatMessageLine(ClientViewModel viewModel, float left, float right, float baselineY, ClientChatMessage message) {
    if (message.kind() == ClientChatMessageKind.SYSTEM || message.speakerDisplayName() == null) {
      drawBitmapTextAtBaseline(
          chatFont,
          left,
          baselineY,
          fitChatHistoryText(chatFont, message.text(), right - left),
          CHAT_SYSTEM_RGB,
          false
      );
      return;
    }
    float currentLeft = left;
    OpenGlTexture crownTexture = speakerCrownTexture(viewModel, message);
    if (crownTexture != null) {
      drawTextureAt(crownTexture, currentLeft, baselineY - CHAT_CROWN_BASELINE_OFFSET_Y);
      currentLeft += crownTexture.width() + CHAT_CROWN_TEXT_GAP;
    }
    String speaker = message.speakerDisplayName() + ":";
    drawBitmapTextAtBaseline(chatFont, currentLeft, baselineY, speaker, CHAT_LABEL_RGB, false);
    float messageLeft = currentLeft + measureBitmapText(chatFont, speaker + " ");
    drawBitmapTextAtBaseline(
        chatFont,
        messageLeft,
        baselineY,
        fitChatHistoryText(chatFont, message.text(), right - messageLeft),
        CHAT_PUBLIC_TEXT_RGB,
        false
    );
  }

  private void drawChatScrollbar(ScreenRect historyRect, int totalMessages) {
    ScreenRect scrollbarRect = new ScreenRect(
        historyRect.left() + historyRect.width(),
        historyRect.top(),
        CHAT_SCROLLBAR_WIDTH,
        historyRect.height()
    );
    drawTextureAt(chatScrollbarTopArrowTexture, scrollbarRect.left(), scrollbarRect.top());
    drawTextureAt(
        chatScrollbarBottomArrowTexture,
        scrollbarRect.left(),
        scrollbarRect.top() + scrollbarRect.height() - CHAT_SCROLLBAR_ARROW_HEIGHT
    );
    float trackTop = scrollbarRect.top() + CHAT_SCROLLBAR_ARROW_HEIGHT;
    float trackHeight = scrollbarRect.height() - CHAT_SCROLLBAR_ARROW_HEIGHT * 2.0f;
    fillRect(scrollbarRect.left(), trackTop, scrollbarRect.width(), trackHeight, CHAT_SCROLLBAR_TRACK_RGB);
    outlineRect(scrollbarRect.left(), trackTop, scrollbarRect.width(), trackHeight, CHAT_SCROLLBAR_DARK_RGB);
    int viewportHeight = Math.round(historyRect.height());
    int scrollContentHeight = Math.max(viewportHeight, totalMessages * Math.round(CHAT_LINE_HEIGHT));
    int thumbHeight = SidebarWidgetScrollState.scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int thumbOffset = SidebarWidgetScrollState.scrollbarThumbOffset(
        Math.max(0, scrollContentHeight - viewportHeight),
        viewportHeight,
        scrollContentHeight,
        thumbHeight
    );
    float thumbTop = scrollbarRect.top() + CHAT_SCROLLBAR_ARROW_HEIGHT + thumbOffset;
    fillRect(scrollbarRect.left() + 1.0f, thumbTop, scrollbarRect.width() - 2.0f, thumbHeight, CHAT_SCROLLBAR_THUMB_FILL_RGB);
    drawGlyphLine(scrollbarRect.left(), thumbTop, scrollbarRect.left() + scrollbarRect.width() - 1.0f, thumbTop, CHAT_SCROLLBAR_HIGHLIGHT_RGB);
    drawGlyphLine(scrollbarRect.left(), thumbTop, scrollbarRect.left(), thumbTop + thumbHeight - 1.0f, CHAT_SCROLLBAR_HIGHLIGHT_RGB);
    drawGlyphLine(
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop,
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop + thumbHeight - 1.0f,
        CHAT_SCROLLBAR_DARK_RGB
    );
    drawGlyphLine(
        scrollbarRect.left(),
        thumbTop + thumbHeight - 1.0f,
        scrollbarRect.left() + scrollbarRect.width() - 1.0f,
        thumbTop + thumbHeight - 1.0f,
        CHAT_SCROLLBAR_DARK_RGB
    );
  }

  private void drawBitmapTextAtBaseline(
      TitleScreenBitmapFont font,
      float left,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    SidebarWidgetRenderer.TextTextureLayout layout = SidebarWidgetRenderer.textTextureLayout(font, text, shadow);
    OpenGlTexture texture = bitmapTextTexture(font, text, rgb, shadow);
    if (texture == null) {
      return;
    }
    drawTextureAt(
        texture,
        left + layout.canvasLeft(),
        baselineY - font.maxGlyphHeight() + layout.canvasTop()
    );
  }

  private void drawBitmapTextGlyphsAtBaseline(
      TitleScreenBitmapFont font,
      float left,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    float currentLeft = left;
    for (int index = 0; index < text.length(); index++) {
      BitmapGlyphTexture glyphTexture = bitmapGlyphTexture(font, text.charAt(index), rgb, shadow);
      if (glyphTexture == null) {
        continue;
      }
      drawTextureAt(
          glyphTexture.texture(),
          currentLeft + glyphTexture.layout().canvasLeft(),
          baselineY - font.maxGlyphHeight() + glyphTexture.layout().canvasTop()
      );
      currentLeft += glyphTexture.advance();
    }
  }

  private void drawBitmapTextCentered(
      TitleScreenBitmapFont font,
      float centerX,
      float baselineY,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return;
    }
    float left = centerX - measureBitmapText(font, text) * 0.5f;
    drawBitmapTextAtBaseline(font, left, baselineY, text, rgb, shadow);
  }

  private static float measureBitmapText(TitleScreenBitmapFont font, String text) {
    if (font == null || text == null) {
      return 0.0f;
    }
    return font.measureText(text);
  }

  private ScreenRect reportAbuseLabelRect() {
    float labelWidth = Math.max(80.0f, measureBitmapText(chatFont, REPORT_ABUSE_LABEL));
    float labelLeft = REPORT_ABUSE_CENTER_X - labelWidth * 0.5f;
    float labelTop = REPORT_ABUSE_BASELINE_Y - (chatFont == null ? 10.0f : chatFont.maxGlyphHeight());
    float labelHeight = chatFont == null ? 12.0f : chatFont.maxGlyphHeight() + 2.0f;
    return new ScreenRect(labelLeft, labelTop, labelWidth, labelHeight);
  }

  private ScreenRect reportAbuseModalRect() {
    return centeredReportAbuseModalRect(
        GameplayLayout.worldViewportInnerRect(),
        reportAbuseController.interfaceWidth(),
        reportAbuseController.interfaceHeight()
    );
  }

  static ScreenRect centeredReportAbuseModalRect(ScreenRect worldViewportRect, float width, float height) {
    return new ScreenRect(
        worldViewportRect.left() + (worldViewportRect.width() - width) * 0.5f,
        worldViewportRect.top() + (worldViewportRect.height() - height) * 0.5f,
        width,
        height
    );
  }

  private static OpenGlTexture spriteTexture(
      ArchiveSpriteResolver spriteResolver,
      String entryName,
      int frameIndex
  ) {
    if (spriteResolver == null) {
      return null;
    }
    ArgbImage image = spriteResolver.resolve(entryName, frameIndex);
    return image == null ? null : OpenGlTexture.fromArgbImage(image);
  }

  private List<ClientChatMessage> renderedChatMessages(ClientViewModel viewModel) {
    if (!viewModel.chatMessages().isEmpty()) {
      return viewModel.chatMessages();
    }
    return List.of(DEFAULT_WELCOME_MESSAGE);
  }

  private OpenGlTexture speakerCrownTexture(ClientViewModel viewModel, ClientChatMessage message) {
    if (message == null || message.speakerDisplayName() == null || modIconTextures.length < 2) {
      return null;
    }
    StaffRole staffRole = speakerStaffRole(viewModel, message.speakerDisplayName());
    if (!staffRole.hasCrown()) {
      return null;
    }
    int crownSpriteIndex = staffRole.crownSpriteIndex();
    return crownSpriteIndex >= 0 && crownSpriteIndex < modIconTextures.length
        ? modIconTextures[crownSpriteIndex]
        : null;
  }

  private StaffRole speakerStaffRole(ClientViewModel viewModel, String speakerDisplayName) {
    if (speakerDisplayName == null || viewModel == null || viewModel.bootstrap() == null) {
      return StaffRole.NONE;
    }
    if (!speakerDisplayName.equals(viewModel.bootstrap().displayName())) {
      return StaffRole.NONE;
    }
    return viewModel.bootstrap().profile() == null ? StaffRole.NONE : viewModel.bootstrap().profile().staffRole();
  }

  static String fitChatHistoryText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return fitChatTextFromStart(font, text, maxWidth);
  }

  static String fitChatPromptText(TitleScreenBitmapFont font, String text, float maxWidth) {
    return fitChatTextFromEnd(font, text, maxWidth);
  }

  private static String fitChatTextFromStart(TitleScreenBitmapFont font, String text, float maxWidth) {
    String safeText = Objects.toString(text, "");
    if (font == null || safeText.isEmpty() || maxWidth <= 0.0f) {
      return "";
    }
    if (measureBitmapText(font, safeText) <= maxWidth) {
      return safeText;
    }
    float ellipsisWidth = measureBitmapText(font, CHAT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = CHAT_ELLIPSIS;
    for (int endIndex = 1; endIndex <= safeText.length(); endIndex++) {
      String candidate = safeText.substring(0, endIndex) + CHAT_ELLIPSIS;
      if (measureBitmapText(font, candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  private static String fitChatTextFromEnd(TitleScreenBitmapFont font, String text, float maxWidth) {
    String safeText = Objects.toString(text, "");
    if (font == null || safeText.isEmpty() || maxWidth <= 0.0f) {
      return "";
    }
    if (measureBitmapText(font, safeText) <= maxWidth) {
      return safeText;
    }
    float ellipsisWidth = measureBitmapText(font, CHAT_ELLIPSIS);
    if (ellipsisWidth > maxWidth) {
      return "";
    }
    String bestFit = CHAT_ELLIPSIS;
    for (int startIndex = safeText.length() - 1; startIndex >= 0; startIndex--) {
      String candidate = CHAT_ELLIPSIS + safeText.substring(startIndex);
      if (measureBitmapText(font, candidate) <= maxWidth) {
        bestFit = candidate;
        continue;
      }
      break;
    }
    return bestFit;
  }

  private BitmapGlyphTexture bitmapGlyphTexture(
      TitleScreenBitmapFont font,
      char glyph,
      int rgb,
      boolean shadow
  ) {
    return bitmapGlyphTextures.computeIfAbsent(
        new BitmapGlyphKey(font, glyph, rgb, shadow),
        this::createBitmapGlyphTexture
    );
  }

  private BitmapGlyphTexture createBitmapGlyphTexture(BitmapGlyphKey key) {
    String glyphText = String.valueOf(key.glyph());
    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(key.font(), glyphText, key.shadow());
    int[] pixels = new int[layout.width() * layout.height()];
    key.font().drawText(
        pixels,
        layout.width(),
        layout.height(),
        glyphText,
        -layout.canvasLeft(),
        layout.baselineY(),
        key.rgb(),
        key.shadow()
    );
    return new BitmapGlyphTexture(
        OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels)),
        layout,
        key.font().measureGlyph(key.glyph())
    );
  }

  // Keep live chat typing hitch-free by warming the printable prompt glyph set once instead of
  // uploading new GL textures the first time Shift introduces an uppercase character.
  private void prewarmChatPromptGlyphTextures() {
    if (chatFont == null || promptGlyphsPrewarmed) {
      return;
    }
    for (int glyph = 32; glyph <= 126; glyph++) {
      bitmapGlyphTexture(chatFont, (char) glyph, CHAT_PROMPT_TEXT_RGB, false);
    }
    promptGlyphsPrewarmed = true;
  }

  private record BitmapTextKey(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
  }

  private record BitmapGlyphKey(
      TitleScreenBitmapFont font,
      char glyph,
      int rgb,
      boolean shadow
  ) {
  }

  private record BitmapGlyphTexture(
      OpenGlTexture texture,
      SidebarWidgetRenderer.TextTextureLayout layout,
      int advance
  ) {
  }
}
