package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.core.EquipmentLoadout;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayClickResult;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayFrameAssets;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayLayout;
import io.github.ffakira.rsps.client.desktop.gameplay.GameplayTab;
import io.github.ffakira.rsps.client.desktop.itemicon.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenBitmapFont;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenFonts;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.bootstrap.BootstrapItemSlot;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class GameplaySidebarRenderer implements AutoCloseable {

  private static final float SIDEBAR_CONTENT_LEFT_INSET = 12.0f;
  private static final float SIDEBAR_CONTENT_TOP_INSET = 22.0f;
  private static final float CHATBOX_CONTENT_LEFT_INSET = 10.0f;
  private static final float CHATBOX_CONTENT_TOP_INSET = 22.0f;
  private static final NumberFormat LEGACY_NUMBER_FORMAT = NumberFormat.getIntegerInstance();

  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final TitleScreenBitmapFont statsSmallFont;
  private final TitleScreenBitmapFont statsLabelFont;
  private final ImmediateModeRenderer2d primitives;
  private final OpenGlTexture statsButtonLeftTexture;
  private final OpenGlTexture statsButtonRightTexture;
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
  private final LogoutSidebarPanelRenderer logoutPanelRenderer;

  public GameplaySidebarRenderer(
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayFrameAssets gameplayFrameAssets,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
    this.itemIconRenderer = itemIconRenderer;
    this.inventoryAmountFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsSmallFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsLabelFont = titleScreenFonts == null ? null : titleScreenFonts.plain();
    this.primitives = primitives;
    GameplayStatsTabAssets statsTabAssets = gameplayFrameAssets == null ? null : gameplayFrameAssets.statsTabAssets();
    this.statsButtonLeftTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonLeft());
    this.statsButtonRightTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonRight());
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
    this.logoutPanelRenderer = new LogoutSidebarPanelRenderer(this);
  }

  public GameplayClickResult handleSidebarClick(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case STATS -> statsPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case MAGIC -> magicPanelRenderer.handleSidebarClick(sidebarRect, x, y)
          ? GameplayClickResult.handledClick()
          : GameplayClickResult.ignored();
      case LOGOUT -> logoutPanelRenderer.handleSidebarClick(sidebarRect, x, y);
      default -> GameplayClickResult.ignored();
    };
  }

  public boolean handleSidebarScroll(GameplayTab activeGameplayTab, double x, double y, double yOffset) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case MAGIC -> magicPanelRenderer.handleSidebarScroll(sidebarRect, x, y, yOffset);
      default -> false;
    };
  }

  public boolean handleSidebarPointerMove(GameplayTab activeGameplayTab, double x, double y) {
    ScreenRect sidebarRect = GameplayLayout.sidebarPanelRect();
    return switch (activeGameplayTab) {
      case MAGIC -> magicPanelRenderer.handleSidebarPointerMove(sidebarRect, x, y);
      default -> false;
    };
  }

  public void endSidebarPointerDrag() {
    magicPanelRenderer.endSidebarPointerDrag();
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
        && activeGameplayTab != GameplayTab.PRAYER
        && activeGameplayTab != GameplayTab.MAGIC
        && activeGameplayTab != GameplayTab.LOGOUT) {
      primitives.drawText(left, top, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
    }
    switch (activeGameplayTab) {
      case INVENTORY -> inventoryEquipmentPanelRenderer.drawInventorySidebar(viewModel, rect);
      case EQUIPMENT -> inventoryEquipmentPanelRenderer.drawEquipmentSidebar(viewModel, left, top + 16.0f);
      case STATS -> statsPanelRenderer.drawStatsSidebar(viewModel, rect, pointerX, pointerY);
      case COMBAT -> combatPanelRenderer.drawCombatSidebar(viewModel, rect);
      case QUESTS -> drawSidebarPlaceholder(left, top + 16.0f, "Quest journal not synced yet.", "Native widget rendering is next.");
      case PRAYER -> prayerPanelRenderer.drawPrayerSidebar(rect, pointerX, pointerY);
      case MAGIC -> magicPanelRenderer.drawMagicSidebar(rect);
      case FRIENDS -> drawSidebarPlaceholder(left, top + 16.0f, "Friends list not synced yet.", "Social state will move into protocol snapshots.");
      case IGNORES -> drawSidebarPlaceholder(left, top + 16.0f, "Ignore list not synced yet.", "Legacy file import already stores social links.");
      case LOGOUT -> logoutPanelRenderer.drawLogoutSidebar(rect);
      case SETTINGS -> drawSidebarPlaceholder(left, top + 16.0f, "Settings panel not implemented yet.", "Run energy and rights are in chat status.");
      case EMOTES -> drawSidebarPlaceholder(left, top + 16.0f, "Emote book not decoded yet.", "Runtime action sequence packets are ready; emote UI is still pending.");
      case MUSIC -> drawSidebarPlaceholder(left, top + 16.0f, "Music player not decoded yet.", "Audio is outside the current world slice.");
    }
  }

  public void drawChatbox(ClientViewModel viewModel, GameplayTab activeGameplayTab) {
    ScreenRect rect = GameplayLayout.chatboxPanelRect();
    float left = rect.left() + CHATBOX_CONTENT_LEFT_INSET;
    float top = rect.top() + CHATBOX_CONTENT_TOP_INSET;
    primitives.drawText(left, top, "Welcome to MoparScape.", 0.92f, 0.86f, 0.46f);
    primitives.drawText(left, top + 16.0f, viewModel.statusText(), 0.30f, 0.85f, 0.35f);
    if (viewModel.bootstrap() != null && viewModel.localPlayerPosition() != null) {
      primitives.drawText(
          left,
          top + 32.0f,
          viewModel.bootstrap().displayName() + ": region "
              + Objects.toString(viewModel.regionKey(), "unknown")
              + "  x:" + viewModel.localPlayerPosition().x()
              + " y:" + viewModel.localPlayerPosition().y()
              + " z:" + viewModel.localPlayerPosition().plane(),
          0.95f,
          0.96f,
          0.98f
      );
      primitives.drawText(
          left,
          top + 48.0f,
          "Rights " + viewModel.bootstrap().profile().rights()
              + "  Energy " + viewModel.bootstrap().profile().runEnergy()
              + "  Active tab " + activeGameplayTab.label(),
          0.84f,
          0.88f,
          0.94f
      );
    }
    primitives.drawText(left, top + 64.0f, "Click to walk. Arrow keys rotate and tilt the camera.", 0.84f, 0.88f, 0.94f);
    drawChatOptionsBar();
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
    closeTexture(statsButtonLeftTexture);
    closeTexture(statsButtonRightTexture);
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

  private void drawChatOptionsBar() {
    primitives.drawText(10.0f, 479.0f, "Public chat", 0.95f, 0.96f, 0.98f);
    primitives.drawText(78.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(137.0f, 479.0f, "Private chat", 0.95f, 0.96f, 0.98f);
    primitives.drawText(209.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(266.0f, 479.0f, "Trade/compete", 0.95f, 0.96f, 0.98f);
    primitives.drawText(343.0f, 490.0f, "On", 0.30f, 0.85f, 0.35f);
    primitives.drawText(402.0f, 479.0f, "Report abuse", 0.95f, 0.96f, 0.98f);
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
    int width = Math.max(1, key.font().measureText(key.text()) + (key.shadow() ? 1 : 0));
    int height = Math.max(1, key.font().maxGlyphHeight() + (key.shadow() ? 1 : 0));
    int[] pixels = new int[width * height];
    key.font().drawText(
        pixels,
        width,
        height,
        key.text(),
        0,
        key.font().maxGlyphHeight(),
        key.rgb(),
        key.shadow()
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(width, height, pixels));
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

  private record BitmapTextKey(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
  }
}
