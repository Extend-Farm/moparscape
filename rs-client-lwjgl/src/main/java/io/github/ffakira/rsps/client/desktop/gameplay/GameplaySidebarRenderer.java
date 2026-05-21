package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.core.BootstrapEquipmentItemPresentation;
import io.github.ffakira.rsps.client.core.BootstrapInventoryItemPresentation;
import io.github.ffakira.rsps.client.core.BootstrapSkillPresentation;
import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.itemicon.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenBitmapFont;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenFonts;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.BootstrapSkill;
import static org.lwjgl.opengl.GL11.glColor3f;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class GameplaySidebarRenderer implements AutoCloseable {

  private static final int INVENTORY_COLUMNS = 4;
  private static final int INVENTORY_ROWS = 7;
  private static final float INVENTORY_SLOT_STEP_X = 42.0f;
  private static final float INVENTORY_SLOT_STEP_Y = 36.0f;
  private static final float INVENTORY_SLOT_LEFT = 12.0f;
  private static final float INVENTORY_SLOT_TOP = 11.0f;
  private static final float INVENTORY_ICON_SIZE = 32.0f;
  private static final int INVENTORY_STACK_TEXT_RGB = 0xffff00;
  private static final int LEGACY_PANEL_OUTLINE_RGB = 0x726451;
  private static final int LEGACY_PANEL_INNER_OUTLINE_RGB = 0x2e2b23;
  private static final int LEGACY_PANEL_FILL_RGB = 0x000000;
  private static final int LEGACY_TEXT_RGB = 0xffff00;
  private static final float STATS_GRID_LEFT = 6.0f;
  private static final float STATS_GRID_TOP = 8.0f;
  private static final float STATS_CELL_WIDTH = 56.0f;
  private static final float STATS_CELL_HEIGHT = 30.0f;
  private static final float STATS_BADGE_SIZE = 14.0f;
  private static final int STATS_GRID_COLUMNS = 3;
  private static final int STATS_GRID_ROWS = 7;
  private static final int STATS_PANEL_RGB = 0x2b241c;
  private static final int STATS_PANEL_OUTLINE_RGB = 0x65563d;
  private static final int STATS_BADGE_OUTLINE_RGB = 0xd0c28a;
  private static final int STATS_LABEL_RGB = 0xd6bf6a;
  private static final int STATS_VALUE_RGB = 0xffff00;
  private static final int STATS_SECONDARY_VALUE_RGB = 0xff9040;
  private static final int STATS_BOOSTED_VALUE_RGB = 0x00ff00;
  private static final int STATS_LOWERED_VALUE_RGB = 0xff3030;
  private static final NumberFormat LEGACY_NUMBER_FORMAT = NumberFormat.getIntegerInstance();

  private static final String[] SKILL_NAMES = {
      "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic",
      "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
      "Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer", "Farming",
      "Runecraft", "Unused", "Unused", "Unused", "Unused"
  };
  private static final String[] FALLBACK_SKILL_SHORT_NAMES = {
      "Atk", "Def", "Str", "HP", "Rng", "Pray", "Mage",
      "Cook", "WC", "Fletch", "Fish", "FM", "Craft",
      "Smith", "Mine", "Herb", "Agi", "Thiev", "Slay", "Farm",
      "RC"
  };
  private static final int[] FALLBACK_SKILL_ACCENT_RGB = {
      0x9f3327, 0x6c7fa5, 0xb06122, 0xb52d32, 0x356c57, 0xc7c180, 0x6f4f9a,
      0x8e6b3d, 0x4a7a3b, 0x5b7447, 0x2c6f93, 0xba6d25, 0xa8644d,
      0x7a8087, 0x6b5e49, 0x5a8b57, 0x8eb053, 0x6b7e59, 0x74607d, 0xa18a46,
      0x6f6bd0
  };
  private static final String[] EQUIPMENT_SLOT_NAMES = {
      "Head", "Cape", "Neck", "Weapon", "Body", "Shield", "Arms",
      "Legs", "Hair", "Hands", "Feet", "Ring", "Ammo", "Aura"
  };

  private final ItemDefinitionCatalog itemDefinitionCatalog;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final TitleScreenBitmapFont statsSmallFont;
  private final TitleScreenBitmapFont statsLabelFont;
  private final ImmediateModeRenderer2d primitives;
  private final OpenGlTexture statsButtonLeftTexture;
  private final OpenGlTexture statsButtonRightTexture;
  private final OpenGlTexture[] statIconTexturesBySkillId;
  private final Map<Integer, OpenGlTexture> itemIconTextures = new HashMap<>();
  private final Map<String, OpenGlTexture> inventoryAmountTextures = new HashMap<>();
  private final Map<BitmapTextKey, OpenGlTexture> bitmapTextTextures = new HashMap<>();

  GameplaySidebarRenderer(
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      GameplayStatsTabAssets statsTabAssets,
      TitleScreenFonts titleScreenFonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.itemDefinitionCatalog = itemDefinitionCatalog;
    this.itemIconRenderer = itemIconRenderer;
    this.inventoryAmountFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsSmallFont = titleScreenFonts == null ? null : titleScreenFonts.plainSmall();
    this.statsLabelFont = titleScreenFonts == null ? null : titleScreenFonts.plain();
    this.primitives = primitives;
    this.statsButtonLeftTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonLeft());
    this.statsButtonRightTexture = statsTabAssets == null ? null : OpenGlTexture.fromArgbImage(statsTabAssets.buttonRight());
    this.statIconTexturesBySkillId = buildStatIconTextures(statsTabAssets);
  }

  boolean handleSidebarClick(GameplayTab activeGameplayTab, double x, double y) {
    if (activeGameplayTab != GameplayTab.STATS) {
      return false;
    }
    return handleStatsSidebarClick(GameplayLayout.sidebarPanelRect(), x, y);
  }

  void clearTransientState() {
  }

  void drawSidebar(ClientViewModel viewModel, GameplayTab activeGameplayTab, double pointerX, double pointerY) {
    // Sidebar content is intentionally honest about parity:
    // resolved bootstrap/item data is shown where native state exists, and explicit placeholders
    // remain where widget-driven interfaces have not been ported yet.
    ScreenRect rect = GameplayLayout.sidebarPanelRect();
    float left = rect.left() + 12.0f;
    float top = rect.top() + 22.0f;
    if (activeGameplayTab != GameplayTab.INVENTORY && activeGameplayTab != GameplayTab.STATS) {
      primitives.drawText(left, top, activeGameplayTab.label(), 0.92f, 0.86f, 0.46f);
    }
    switch (activeGameplayTab) {
      case INVENTORY -> drawInventorySidebar(viewModel, rect);
      case EQUIPMENT -> drawEquipmentSidebar(viewModel, left, top + 16.0f);
      case STATS -> drawStatsSidebar(viewModel, rect, pointerX, pointerY);
      case COMBAT -> drawCombatSidebar(viewModel, left, top + 16.0f);
      case QUESTS -> drawSidebarPlaceholder(left, top + 16.0f, "Quest journal not synced yet.", "Native widget rendering is next.");
      case PRAYER -> drawSidebarPlaceholder(left, top + 16.0f, "Prayer book not synced yet.", "Skill state is available through Stats.");
      case MAGIC -> drawSidebarPlaceholder(left, top + 16.0f, "Spellbook not decoded yet.", "Runes are available in inventory.");
      case FRIENDS -> drawSidebarPlaceholder(left, top + 16.0f, "Friends list not synced yet.", "Social state will move into protocol snapshots.");
      case IGNORES -> drawSidebarPlaceholder(left, top + 16.0f, "Ignore list not synced yet.", "Legacy file import already stores social links.");
      case LOGOUT -> drawSidebarPlaceholder(left, top + 16.0f, "Logout flow is not wired to a tab yet.", "Use Esc to close the client.");
      case SETTINGS -> drawSidebarPlaceholder(left, top + 16.0f, "Settings panel not implemented yet.", "Run energy and rights are in chat status.");
      case EMOTES -> drawSidebarPlaceholder(left, top + 16.0f, "Emote book not decoded yet.", "Runtime action sequence packets are ready; emote UI is still pending.");
      case MUSIC -> drawSidebarPlaceholder(left, top + 16.0f, "Music player not decoded yet.", "Audio is outside the current world slice.");
    }
  }

  void drawChatbox(ClientViewModel viewModel, GameplayTab activeGameplayTab) {
    ScreenRect rect = GameplayLayout.chatboxPanelRect();
    float left = rect.left() + 10.0f;
    float top = rect.top() + 22.0f;
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
  }

  private void drawInventorySidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    if (itemIconRenderer == null) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (inventory.isEmpty()) {
      drawInventoryTextFallback(viewModel, sidebarRect.left() + 12.0f, sidebarRect.top() + 38.0f);
      return;
    }
    for (BootstrapInventoryItemPresentation item : inventory) {
      if (item.slotIndex() < 0 || item.slotIndex() >= INVENTORY_COLUMNS * INVENTORY_ROWS) {
        continue;
      }
      ScreenRect slotRect = inventorySlotRect(sidebarRect, item.slotIndex());
      OpenGlTexture itemTexture = itemIconTexture(item.itemId(), item.quantity());
      if (itemTexture != null) {
        primitives.drawTexturedQuad(itemTexture, slotRect);
      }
      if (showsStackAmount(item)) {
        drawInventoryStackAmount(slotRect, formatQuantity(item.quantity()));
      }
    }
  }

  private void drawInventoryTextFallback(ClientViewModel viewModel, float left, float top) {
    primitives.drawText(left, top - 16.0f, GameplayTab.INVENTORY.label(), 0.92f, 0.86f, 0.46f);
    List<BootstrapInventoryItemPresentation> inventory = viewModel.inventoryPresentation();
    if (!inventory.isEmpty()) {
      int lineCount = Math.min(10, inventory.size());
      for (int index = 0; index < lineCount; index++) {
        BootstrapInventoryItemPresentation item = inventory.get(index);
        primitives.drawText(
            left,
            top + index * 12.0f,
            truncate(item.name(), 18) + " x" + formatQuantity(item.quantity()),
            0.95f,
            0.96f,
            0.98f
        );
      }
      if (inventory.size() > lineCount) {
        primitives.drawText(left, top + lineCount * 12.0f, "+" + (inventory.size() - lineCount) + " more items", 0.70f, 0.74f, 0.82f);
      }
      return;
    }
    drawResolvedItemList(left, top, viewModel.inventory(), 10);
  }

  private void drawEquipmentSidebar(ClientViewModel viewModel, float left, float top) {
    List<BootstrapEquipmentItemPresentation> equipment = viewModel.equipmentPresentation();
    if (!equipment.isEmpty()) {
      int row = 0;
      for (BootstrapEquipmentItemPresentation item : equipment) {
        int column = row % 2;
        int currentRow = row / 2;
        float columnLeft = left + column * 82.0f;
        float rowTop = top + currentRow * 26.0f;
        primitives.drawText(columnLeft, rowTop, truncate(item.slotName(), 8), 0.84f, 0.88f, 0.94f);
        primitives.drawText(columnLeft, rowTop + 12.0f, truncate(item.name(), 11), 0.95f, 0.96f, 0.98f);
        row++;
        if (row >= 10) {
          break;
        }
      }
      return;
    }
    drawEquipmentList(left, top, viewModel.equipment(), 2);
  }

  private void drawStatsSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    GameplayStatsSidebarModel statsModel = GameplayStatsSidebarModel.from(viewModel);
    if (!canDrawLegacyStatsTab()) {
      float left = sidebarRect.left() + 12.0f;
      float top = sidebarRect.top() + 38.0f;
      drawStatsSummary(left + STATS_GRID_LEFT, top - 2.0f, statsModel);
      drawStatsGrid(left + STATS_GRID_LEFT, top + STATS_GRID_TOP + 16.0f, statsModel);
      return;
    }

    for (GameplayStatsTabLayout.StatsSlotLayout slot : GameplayStatsTabLayout.slots()) {
      GameplayStatsSidebarModel.Entry entry = statsModel.entryForSkill(slot.skillId());
      if (entry != null) {
        drawLegacyStatsSlot(sidebarRect, slot, entry);
      }
    }
    drawLegacyStatsSummary(sidebarRect, statsModel);

    GameplayStatsTabLayout.StatsSlotLayout hoveredSlot = statsSlotAt(sidebarRect, pointerX, pointerY);
    if (hoveredSlot == null) {
      return;
    }
    GameplayStatsSidebarModel.Entry hoveredEntry = statsModel.entryForSkill(hoveredSlot.skillId());
    if (hoveredEntry != null) {
      drawLegacyStatsHover(sidebarRect, hoveredSlot, hoveredEntry);
    }
  }

  private void drawCombatSidebar(ClientViewModel viewModel, float left, float top) {
    drawSidebarPlaceholder(
        left,
        top,
        "Combat style UI is not decoded yet.",
        "Top skill: " + highestSkillLabel(viewModel),
        "Equipment stacks: " + viewModel.equipment().size(),
        "Inventory stacks: " + viewModel.inventory().size()
    );
  }

  private boolean canDrawLegacyStatsTab() {
    return statsButtonLeftTexture != null
        && statsButtonRightTexture != null
        && statsSmallFont != null
        && statsLabelFont != null;
  }

  private void drawLegacyStatsSlot(
      ScreenRect sidebarRect,
      GameplayStatsTabLayout.StatsSlotLayout slot,
      GameplayStatsSidebarModel.Entry entry
  ) {
    drawTextureAt(statsButtonLeftTexture, sidebarRect.left() + slot.buttonLeftX(), sidebarRect.top() + slot.buttonLeftY());
    drawTextureAt(statsButtonRightTexture, sidebarRect.left() + slot.buttonRightX(), sidebarRect.top() + slot.buttonRightY());

    OpenGlTexture iconTexture = statIconTexturesBySkillId[slot.skillId()];
    drawTextureAt(iconTexture, sidebarRect.left() + slot.iconX(), sidebarRect.top() + slot.iconY());

    drawLegacyStatsTextCentered(
        statsSmallFont,
        sidebarRect.left() + slot.currentTextX(),
        sidebarRect.top() + slot.currentTextY(),
        slot.currentTextWidth(),
        Integer.toString(entry.currentLevel()),
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsTextCentered(
        statsSmallFont,
        sidebarRect.left() + slot.baseTextX(),
        sidebarRect.top() + slot.baseTextY(),
        slot.baseTextWidth(),
        Integer.toString(entry.baseLevel()),
        LEGACY_TEXT_RGB
    );
  }

  private void drawLegacyStatsSummary(ScreenRect sidebarRect, GameplayStatsSidebarModel statsModel) {
    outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_OUTER_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_OUTER_Y,
        178.0f,
        36.0f,
        LEGACY_PANEL_OUTLINE_RGB
    );
    outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_INNER_ONE_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_INNER_ONE_Y,
        176.0f,
        34.0f,
        LEGACY_PANEL_OUTLINE_RGB
    );
    outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_INNER_TWO_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_INNER_TWO_Y,
        177.0f,
        35.0f,
        LEGACY_PANEL_INNER_OUTLINE_RGB
    );
    fillRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_FILL_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_FILL_Y,
        174.0f,
        32.0f,
        LEGACY_PANEL_FILL_RGB
    );

    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + 90.0f,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + 12.0f,
        "Combat Lvl: " + statsModel.combatLevel(),
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + 91.0f,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + 27.0f,
        "Total Lvl: " + statsModel.totalLevel(),
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + 18.0f,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + 20.0f,
        "QP: 0",
        LEGACY_TEXT_RGB
    );
  }

  private void drawLegacyStatsHover(
      ScreenRect sidebarRect,
      GameplayStatsTabLayout.StatsSlotLayout slot,
      GameplayStatsSidebarModel.Entry entry
  ) {
    fillRect(
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_FILL_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FILL_Y,
        174.0f,
        32.0f,
        LEGACY_PANEL_FILL_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_FIRST_LINE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FIRST_LINE_Y,
        entry.name() + " XP:",
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        // The legacy hover widgets shift the XP value per skill so long labels like
        // "Woodcutting XP:" or "Strength XP:" do not bleed into the number.
        sidebarRect.left() + slot.hoverFirstValueX(),
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FIRST_LINE_Y,
        formatLegacyNumber(entry.experience()),
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_SECOND_LINE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_SECOND_LINE_Y,
        "Next Level At:",
        LEGACY_TEXT_RGB
    );
    drawLegacyStatsText(
        statsLabelFont,
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_SECOND_VALUE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_SECOND_LINE_Y,
        formatLegacyNumber(GameplayStatsSidebarModel.experienceForLevel(entry.baseLevel())),
        LEGACY_TEXT_RGB
    );
  }

  private boolean handleStatsSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (!sidebarRect.contains(x, y)) {
      return false;
    }
    GameplayStatsTabLayout.StatsSlotLayout clickedSlot = statsSlotAt(sidebarRect, x, y);
    return clickedSlot != null;
  }

  private GameplayStatsTabLayout.StatsSlotLayout statsSlotAt(
      ScreenRect sidebarRect,
      double pointerX,
      double pointerY
  ) {
    if (Double.isNaN(pointerX) || Double.isNaN(pointerY) || !sidebarRect.contains(pointerX, pointerY)) {
      return null;
    }
    float localX = (float) (pointerX - sidebarRect.left());
    float localY = (float) (pointerY - sidebarRect.top());
    return GameplayStatsTabLayout.hoveredSlot(localX, localY);
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

  private void drawResolvedItemList(float left, float top, List<BootstrapItemSlot> itemSlots, int maxLines) {
    int lineCount = Math.min(maxLines, itemSlots.size());
    for (int index = 0; index < lineCount; index++) {
      BootstrapItemSlot slot = itemSlots.get(index);
      primitives.drawText(
          left,
          top + index * 12.0f,
          truncate(resolveItemName(slot.itemId()), 18) + " x" + formatQuantity(slot.quantity()),
          0.95f,
          0.96f,
          0.98f
      );
    }
    if (itemSlots.size() > lineCount) {
      primitives.drawText(left, top + lineCount * 12.0f, "+" + (itemSlots.size() - lineCount) + " more items", 0.70f, 0.74f, 0.82f);
    }
  }

  private void drawEquipmentList(float left, float top, List<BootstrapItemSlot> equipment, int columns) {
    int rowCount = 0;
    for (BootstrapItemSlot slot : equipment) {
      if (rowCount >= EQUIPMENT_SLOT_NAMES.length) {
        break;
      }
      int column = rowCount % columns;
      int row = rowCount / columns;
      float columnLeft = left + column * 82.0f;
      float rowTop = top + row * 24.0f;
      int slotIndex = Math.min(slot.slotIndex(), EQUIPMENT_SLOT_NAMES.length - 1);
      primitives.drawText(columnLeft, rowTop, EQUIPMENT_SLOT_NAMES[slotIndex], 0.84f, 0.88f, 0.94f);
      primitives.drawText(columnLeft, rowTop + 12.0f, truncate(resolveItemName(slot.itemId()), 11), 0.95f, 0.96f, 0.98f);
      rowCount++;
    }
  }

  private void drawStatsSummary(float left, float top, GameplayStatsSidebarModel statsModel) {
    drawShadowedText(left, top, "Combat", rgbUnit(STATS_LABEL_RGB, 16), rgbUnit(STATS_LABEL_RGB, 8), rgbUnit(STATS_LABEL_RGB, 0), 0.85f);
    drawShadowedText(
        left + 46.0f,
        top,
        Integer.toString(statsModel.combatLevel()),
        rgbUnit(STATS_VALUE_RGB, 16),
        rgbUnit(STATS_VALUE_RGB, 8),
        rgbUnit(STATS_VALUE_RGB, 0),
        0.85f
    );
    drawShadowedText(left + 86.0f, top, "Total", rgbUnit(STATS_LABEL_RGB, 16), rgbUnit(STATS_LABEL_RGB, 8), rgbUnit(STATS_LABEL_RGB, 0), 0.85f);
    drawShadowedText(
        left + 122.0f,
        top,
        Integer.toString(statsModel.totalLevel()),
        rgbUnit(STATS_VALUE_RGB, 16),
        rgbUnit(STATS_VALUE_RGB, 8),
        rgbUnit(STATS_VALUE_RGB, 0),
        0.85f
    );
  }

  private void drawStatsGrid(float left, float top, GameplayStatsSidebarModel statsModel) {
    List<GameplayStatsSidebarModel.Entry> entries = statsModel.entries();
    for (int index = 0; index < Math.min(entries.size(), STATS_GRID_COLUMNS * STATS_GRID_ROWS); index++) {
      GameplayStatsSidebarModel.Entry entry = entries.get(index);
      int column = index % STATS_GRID_COLUMNS;
      int row = index / STATS_GRID_COLUMNS;
      float cellLeft = left + column * STATS_CELL_WIDTH;
      float cellTop = top + row * STATS_CELL_HEIGHT;
      drawStatsCell(cellLeft, cellTop, entry);
    }
  }

  private void drawStatsCell(float left, float top, GameplayStatsSidebarModel.Entry entry) {
    fillRect(left, top, STATS_CELL_WIDTH - 3.0f, STATS_CELL_HEIGHT - 4.0f, STATS_PANEL_RGB);
    outlineRect(left, top, STATS_CELL_WIDTH - 3.0f, STATS_CELL_HEIGHT - 4.0f, STATS_PANEL_OUTLINE_RGB);

    float badgeLeft = left + 4.0f;
    float badgeTop = top + 4.0f;
    fillRect(badgeLeft, badgeTop, STATS_BADGE_SIZE, STATS_BADGE_SIZE, fallbackAccentRgb(entry.skillId()));
    outlineRect(badgeLeft, badgeTop, STATS_BADGE_SIZE, STATS_BADGE_SIZE, STATS_BADGE_OUTLINE_RGB);
    drawShadowedText(
        badgeLeft + 2.0f,
        badgeTop + 4.0f,
        fallbackShortSkillName(entry.skillId()),
        1.0f,
        1.0f,
        1.0f,
        0.55f
    );

    drawShadowedText(
        left + 21.0f,
        top + 5.0f,
        levelText(entry),
        rgbUnit(levelRgb(entry), 16),
        rgbUnit(levelRgb(entry), 8),
        rgbUnit(levelRgb(entry), 0),
        0.70f
    );
    drawShadowedText(
        left + 4.0f,
        top + 21.0f,
        truncate(entry.name(), 9),
        rgbUnit(STATS_LABEL_RGB, 16),
        rgbUnit(STATS_LABEL_RGB, 8),
        rgbUnit(STATS_LABEL_RGB, 0),
        0.65f
    );
  }

  private String highestSkillLabel(ClientViewModel viewModel) {
    return viewModel.skills().stream()
        .max(Comparator.comparingInt(BootstrapSkill::currentLevel))
        .map(skill -> skillName(skill.skillId()) + " " + skill.currentLevel())
        .orElse("none");
  }

  private String levelText(GameplayStatsSidebarModel.Entry entry) {
    if (entry.currentLevel() == entry.baseLevel()) {
      return Integer.toString(entry.currentLevel());
    }
    return entry.currentLevel() + "/" + entry.baseLevel();
  }

  private int levelRgb(GameplayStatsSidebarModel.Entry entry) {
    if (entry.currentLevel() > entry.baseLevel()) {
      return STATS_BOOSTED_VALUE_RGB;
    }
    if (entry.currentLevel() < entry.baseLevel()) {
      return STATS_LOWERED_VALUE_RGB;
    }
    return entry.baseLevel() >= 99 ? STATS_SECONDARY_VALUE_RGB : STATS_VALUE_RGB;
  }

  private String skillName(int skillId) {
    if (skillId < 0 || skillId >= SKILL_NAMES.length) {
      return "Skill " + skillId;
    }
    return SKILL_NAMES[skillId];
  }

  private String fallbackShortSkillName(int skillId) {
    if (skillId < 0 || skillId >= FALLBACK_SKILL_SHORT_NAMES.length) {
      return "S" + skillId;
    }
    return FALLBACK_SKILL_SHORT_NAMES[skillId];
  }

  private int fallbackAccentRgb(int skillId) {
    if (skillId < 0 || skillId >= FALLBACK_SKILL_ACCENT_RGB.length) {
      return 0x7f7f7f;
    }
    return FALLBACK_SKILL_ACCENT_RGB[skillId];
  }

  private String resolveItemName(int itemId) {
    if (itemDefinitionCatalog == null) {
      return "item-" + itemId;
    }
    return itemDefinitionCatalog.find(itemId)
        .map(ItemDefinition::name)
        .orElse("item-" + itemId);
  }

  private String formatQuantity(int quantity) {
    if (quantity >= 10_000_000) {
      return (quantity / 1_000_000) + "M";
    }
    if (quantity >= 100_000) {
      return (quantity / 1_000) + "K";
    }
    return Integer.toString(quantity);
  }

  private boolean showsStackAmount(BootstrapInventoryItemPresentation item) {
    return item.stackable() || item.noted() || item.quantity() != 1;
  }

  private String truncate(String value, int maxChars) {
    if (value.length() <= maxChars) {
      return value;
    }
    return value.substring(0, Math.max(0, maxChars - 1)) + ".";
  }

  private ScreenRect inventorySlotRect(ScreenRect sidebarRect, int slotIndex) {
    int column = slotIndex % INVENTORY_COLUMNS;
    int row = slotIndex / INVENTORY_COLUMNS;
    return new ScreenRect(
        sidebarRect.left() + INVENTORY_SLOT_LEFT + column * INVENTORY_SLOT_STEP_X,
        sidebarRect.top() + INVENTORY_SLOT_TOP + row * INVENTORY_SLOT_STEP_Y,
        INVENTORY_ICON_SIZE,
        INVENTORY_ICON_SIZE
    );
  }

  private OpenGlTexture itemIconTexture(int itemId, int quantity) {
    if (itemIconRenderer == null) {
      return null;
    }
    int iconKey = itemIconRenderer.iconKey(itemId, quantity);
    if (iconKey < 0) {
      return null;
    }
    OpenGlTexture cachedTexture = itemIconTextures.get(iconKey);
    if (cachedTexture != null) {
      return cachedTexture;
    }
    ArgbImage iconImage = itemIconRenderer.render(itemId, quantity);
    if (iconImage == null) {
      return null;
    }
    OpenGlTexture createdTexture = OpenGlTexture.fromArgbImage(iconImage);
    itemIconTextures.put(iconKey, createdTexture);
    return createdTexture;
  }

  private void drawShadowedText(float left, float top, String text, float red, float green, float blue) {
    drawShadowedText(left, top, text, red, green, blue, 1.0f);
  }

  private void drawShadowedText(float left, float top, String text, float red, float green, float blue, float scale) {
    float shadowOffset = scale < 0.8f ? 0.75f : 1.0f;
    primitives.drawText(left + shadowOffset, top + shadowOffset, text, 0.0f, 0.0f, 0.0f, scale);
    primitives.drawText(left, top, text, red, green, blue, scale);
  }

  private void drawInventoryStackAmount(ScreenRect slotRect, String text) {
    if (inventoryAmountFont == null) {
      primitives.drawText(slotRect.left() + 1.0f, slotRect.top() + 10.0f, text, 0.0f, 0.0f, 0.0f, 0.55f);
      primitives.drawText(slotRect.left(), slotRect.top() + 9.0f, text, 1.0f, 1.0f, 0.0f, 0.55f);
      return;
    }
    OpenGlTexture amountTexture = inventoryAmountTexture(text);
    if (amountTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        amountTexture,
        new ScreenRect(
            slotRect.left(),
            slotRect.top() + 9.0f - inventoryAmountFont.maxGlyphHeight(),
            amountTexture.width(),
            amountTexture.height()
        )
    );
  }

  private OpenGlTexture inventoryAmountTexture(String text) {
    if (inventoryAmountFont == null || text == null || text.isEmpty()) {
      return null;
    }
    return inventoryAmountTextures.computeIfAbsent(text, this::createInventoryAmountTexture);
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
        INVENTORY_STACK_TEXT_RGB,
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

  private void drawLegacyStatsText(
      TitleScreenBitmapFont font,
      float left,
      float top,
      String text,
      int rgb
  ) {
    OpenGlTexture textTexture = bitmapTextTexture(font, text, rgb, true);
    drawTextureAt(textTexture, left, top);
  }

  private void drawLegacyStatsTextCentered(
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

  private void drawTextureAt(OpenGlTexture texture, float left, float top) {
    if (texture == null) {
      return;
    }
    primitives.drawTexturedQuad(texture, new ScreenRect(left, top, texture.width(), texture.height()));
  }

  private String formatLegacyNumber(int value) {
    synchronized (LEGACY_NUMBER_FORMAT) {
      return LEGACY_NUMBER_FORMAT.format(value);
    }
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private void fillRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawQuad(left, top, width, height);
  }

  private void outlineRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawOutline(left, top, width, height);
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  private record BitmapTextKey(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
  }
}
