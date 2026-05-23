package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import java.util.List;

final class StatsSidebarPanelRenderer {

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

  private final GameplaySidebarRenderer owner;

  StatsSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawStatsSidebar(ClientViewModel viewModel, ScreenRect sidebarRect, double pointerX, double pointerY) {
    GameplayStatsSidebarModel statsModel = GameplayStatsSidebarModel.from(viewModel);
    if (!owner.canDrawLegacyStatsTab()) {
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

  boolean handleSidebarClick(ScreenRect sidebarRect, double x, double y) {
    if (!sidebarRect.contains(x, y)) {
      return false;
    }
    GameplayStatsTabLayout.StatsSlotLayout clickedSlot = statsSlotAt(sidebarRect, x, y);
    return clickedSlot != null;
  }

  private void drawLegacyStatsSlot(
      ScreenRect sidebarRect,
      GameplayStatsTabLayout.StatsSlotLayout slot,
      GameplayStatsSidebarModel.Entry entry
  ) {
    owner.primitives().drawTextureAt(
        owner.statsButtonLeftTexture(),
        sidebarRect.left() + slot.buttonLeftX(),
        sidebarRect.top() + slot.buttonLeftY()
    );
    owner.primitives().drawTextureAt(
        owner.statsButtonRightTexture(),
        sidebarRect.left() + slot.buttonRightX(),
        sidebarRect.top() + slot.buttonRightY()
    );
    owner.primitives().drawTextureAt(
        owner.statIconTexture(slot.skillId()),
        sidebarRect.left() + slot.iconX(),
        sidebarRect.top() + slot.iconY()
    );

    owner.primitives().drawLegacyStatsTextCentered(
        owner.statsSmallFont(),
        sidebarRect.left() + slot.currentTextX(),
        sidebarRect.top() + slot.currentTextY(),
        slot.currentTextWidth(),
        Integer.toString(entry.currentLevel()),
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsTextCentered(
        owner.statsSmallFont(),
        sidebarRect.left() + slot.baseTextX(),
        sidebarRect.top() + slot.baseTextY(),
        slot.baseTextWidth(),
        Integer.toString(entry.baseLevel()),
        LEGACY_TEXT_RGB
    );
  }

  private void drawLegacyStatsSummary(ScreenRect sidebarRect, GameplayStatsSidebarModel statsModel) {
    owner.primitives().outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_OUTER_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_OUTER_Y,
        178.0f,
        36.0f,
        LEGACY_PANEL_OUTLINE_RGB
    );
    owner.primitives().outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_INNER_ONE_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_INNER_ONE_Y,
        176.0f,
        34.0f,
        LEGACY_PANEL_OUTLINE_RGB
    );
    owner.primitives().outlineRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_INNER_TWO_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_INNER_TWO_Y,
        177.0f,
        35.0f,
        LEGACY_PANEL_INNER_OUTLINE_RGB
    );
    owner.primitives().fillRect(
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + GameplayStatsTabLayout.SUMMARY_FILL_X,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + GameplayStatsTabLayout.SUMMARY_FILL_Y,
        174.0f,
        32.0f,
        LEGACY_PANEL_FILL_RGB
    );

    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + 90.0f,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + 12.0f,
        "Combat Lvl: " + statsModel.combatLevel(),
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + GameplayStatsTabLayout.SUMMARY_RECT_X + 91.0f,
        sidebarRect.top() + GameplayStatsTabLayout.SUMMARY_RECT_Y + 27.0f,
        "Total Lvl: " + statsModel.totalLevel(),
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
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
    owner.primitives().fillRect(
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_FILL_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FILL_Y,
        174.0f,
        32.0f,
        LEGACY_PANEL_FILL_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_FIRST_LINE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FIRST_LINE_Y,
        entry.name() + " XP:",
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + slot.hoverFirstValueX(),
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_FIRST_LINE_Y,
        owner.textFormatter().formatLegacyNumber(entry.experience()),
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_SECOND_LINE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_SECOND_LINE_Y,
        "Next Level At:",
        LEGACY_TEXT_RGB
    );
    owner.primitives().drawLegacyStatsText(
        owner.statsLabelFont(),
        sidebarRect.left() + GameplayStatsTabLayout.HOVER_SECOND_VALUE_X,
        sidebarRect.top() + GameplayStatsTabLayout.HOVER_SECOND_LINE_Y,
        owner.textFormatter().formatLegacyNumber(GameplayStatsSidebarModel.experienceForLevel(entry.baseLevel())),
        LEGACY_TEXT_RGB
    );
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

  private void drawStatsSummary(float left, float top, GameplayStatsSidebarModel statsModel) {
    owner.primitives().drawShadowedText(
        left,
        top,
        "Combat",
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 16),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 8),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 0),
        0.85f
    );
    owner.primitives().drawShadowedText(
        left + 46.0f,
        top,
        Integer.toString(statsModel.combatLevel()),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 16),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 8),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 0),
        0.85f
    );
    owner.primitives().drawShadowedText(
        left + 86.0f,
        top,
        "Total",
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 16),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 8),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 0),
        0.85f
    );
    owner.primitives().drawShadowedText(
        left + 122.0f,
        top,
        Integer.toString(statsModel.totalLevel()),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 16),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 8),
        GameplaySidebarRenderer.rgbUnit(STATS_VALUE_RGB, 0),
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
    owner.primitives().fillRect(left, top, STATS_CELL_WIDTH - 3.0f, STATS_CELL_HEIGHT - 4.0f, STATS_PANEL_RGB);
    owner.primitives().outlineRect(left, top, STATS_CELL_WIDTH - 3.0f, STATS_CELL_HEIGHT - 4.0f, STATS_PANEL_OUTLINE_RGB);

    float badgeLeft = left + 4.0f;
    float badgeTop = top + 4.0f;
    owner.primitives().fillRect(badgeLeft, badgeTop, STATS_BADGE_SIZE, STATS_BADGE_SIZE, fallbackAccentRgb(entry.skillId()));
    owner.primitives().outlineRect(badgeLeft, badgeTop, STATS_BADGE_SIZE, STATS_BADGE_SIZE, STATS_BADGE_OUTLINE_RGB);
    owner.primitives().drawShadowedText(
        badgeLeft + 2.0f,
        badgeTop + 4.0f,
        fallbackShortSkillName(entry.skillId()),
        1.0f,
        1.0f,
        1.0f,
        0.55f
    );

    int levelRgb = levelRgb(entry);
    owner.primitives().drawShadowedText(
        left + 21.0f,
        top + 5.0f,
        levelText(entry),
        GameplaySidebarRenderer.rgbUnit(levelRgb, 16),
        GameplaySidebarRenderer.rgbUnit(levelRgb, 8),
        GameplaySidebarRenderer.rgbUnit(levelRgb, 0),
        0.70f
    );
    owner.primitives().drawShadowedText(
        left + 4.0f,
        top + 21.0f,
        truncate(entry.name(), 9),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 16),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 8),
        GameplaySidebarRenderer.rgbUnit(STATS_LABEL_RGB, 0),
        0.65f
    );
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

  private String truncate(String value, int maxChars) {
    if (value.length() <= maxChars) {
      return value;
    }
    return value.substring(0, Math.max(0, maxChars - 1)) + ".";
  }
}
