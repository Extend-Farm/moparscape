package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class CombatSidebarPanelRenderer {

  private static final float COMBAT_FALLBACK_LEFT_INSET = 12.0f;
  private static final float COMBAT_FALLBACK_TOP_INSET = 28.0f;
  private static final float COMBAT_WEAPON_NAME_X_OFFSET = 40.0f;
  private static final float COMBAT_STYLE_HEADING_Y_OFFSET = 18.0f;
  private static final float COMBAT_WEAPON_ICON_LEFT_OFFSET = 145.0f;
  private static final float COMBAT_WEAPON_ICON_TOP_OFFSET = 26.0f;
  private static final float COMBAT_WEAPON_ICON_SIZE = 32.0f;
  private static final float COMBAT_STYLE_BUTTON_LEFT_OFFSET = 11.0f;
  private static final float COMBAT_STYLE_BUTTON_TOP_OFFSET = 78.0f;
  private static final float COMBAT_STYLE_BUTTON_VERTICAL_STEP = 38.0f;
  private static final float COMBAT_STYLE_BUTTON_WIDTH = 146.0f;
  private static final float COMBAT_STYLE_BUTTON_HEIGHT = 32.0f;
  private static final float COMBAT_STYLE_BUTTON_OUTLINE_INSET = 1.0f;
  private static final float COMBAT_STYLE_BUTTON_FILL_INSET = 2.0f;
  private static final float COMBAT_STYLE_TEXT_LEFT_OFFSET = 38.0f;
  private static final float COMBAT_STYLE_TEXT_WIDTH_REDUCTION = 44.0f;
  private static final float COMBAT_STYLE_GLYPH_LEFT_OFFSET = 8.0f;
  private static final float COMBAT_STYLE_GLYPH_TOP_OFFSET = 6.0f;
  private static final int COMBAT_BUTTON_FILL_RGB = 0x3f3a32;
  private static final int COMBAT_BUTTON_ACTIVE_FILL_RGB = 0x6b2c1d;
  private static final int COMBAT_BUTTON_OUTLINE_RGB = 0x8a7a59;
  private static final int COMBAT_BUTTON_INNER_OUTLINE_RGB = 0x1c1b18;
  private static final int COMBAT_HEADING_RGB = 0xffff00;
  private static final int COMBAT_SUBHEADING_RGB = 0x00ff00;
  private static final int COMBAT_VALUE_RGB = 0xffff00;

  private final GameplaySidebarRenderer owner;

  CombatSidebarPanelRenderer(GameplaySidebarRenderer owner) {
    this.owner = owner;
  }

  void drawCombatSidebar(ClientViewModel viewModel, ScreenRect sidebarRect) {
    GameplayCombatSidebarModel combatModel = owner.combatModels().combatModelFor(viewModel);
    if (owner.sidebarWidgetRenderer() != null && owner.sidebarWidgetRenderer().canRender(combatModel)) {
      owner.sidebarWidgetRenderer().draw(sidebarRect, combatModel, viewModel);
      return;
    }
    float left = sidebarRect.left() + COMBAT_FALLBACK_LEFT_INSET;
    float top = sidebarRect.top() + COMBAT_FALLBACK_TOP_INSET;
    drawCombatText(left, top, "Weapon:", COMBAT_HEADING_RGB, false);
    drawCombatText(left + COMBAT_WEAPON_NAME_X_OFFSET, top, combatModel.weaponName(), COMBAT_VALUE_RGB, false);
    drawCombatText(left, top + COMBAT_STYLE_HEADING_Y_OFFSET, "Choose Attack Style", COMBAT_SUBHEADING_RGB, false);
    drawCombatWeaponIcon(sidebarRect, combatModel.weaponItemId());
    for (int index = 0; index < combatModel.attackStyles().size(); index++) {
      drawCombatStyleButton(sidebarRect, index, combatModel.attackStyles().get(index), index == 0);
    }
  }

  private void drawCombatWeaponIcon(ScreenRect sidebarRect, int weaponItemId) {
    if (weaponItemId < 0) {
      return;
    }
    OpenGlTexture weaponTexture = owner.textures().itemIconTexture(weaponItemId, 1);
    if (weaponTexture == null) {
      return;
    }
    owner.primitives().drawTexturedQuad(
        weaponTexture,
        new ScreenRect(
            sidebarRect.left() + COMBAT_WEAPON_ICON_LEFT_OFFSET,
            sidebarRect.top() + COMBAT_WEAPON_ICON_TOP_OFFSET,
            COMBAT_WEAPON_ICON_SIZE,
            COMBAT_WEAPON_ICON_SIZE
        )
    );
  }

  private void drawCombatStyleButton(
      ScreenRect sidebarRect,
      int styleIndex,
      GameplayCombatSidebarModel.AttackStyle attackStyle,
      boolean selected
  ) {
    float left = sidebarRect.left() + COMBAT_STYLE_BUTTON_LEFT_OFFSET;
    float top = sidebarRect.top() + COMBAT_STYLE_BUTTON_TOP_OFFSET + styleIndex * COMBAT_STYLE_BUTTON_VERTICAL_STEP;
    float width = COMBAT_STYLE_BUTTON_WIDTH;
    float height = COMBAT_STYLE_BUTTON_HEIGHT;
    owner.primitives().outlineRect(left, top, width, height, COMBAT_BUTTON_OUTLINE_RGB);
    owner.primitives().outlineRect(
        left + COMBAT_STYLE_BUTTON_OUTLINE_INSET,
        top + COMBAT_STYLE_BUTTON_OUTLINE_INSET,
        width - COMBAT_STYLE_BUTTON_OUTLINE_INSET * 2.0f,
        height - COMBAT_STYLE_BUTTON_OUTLINE_INSET * 2.0f,
        COMBAT_BUTTON_INNER_OUTLINE_RGB
    );
    owner.primitives().fillRect(
        left + COMBAT_STYLE_BUTTON_FILL_INSET,
        top + COMBAT_STYLE_BUTTON_FILL_INSET,
        width - COMBAT_STYLE_BUTTON_FILL_INSET * 2.0f,
        height - COMBAT_STYLE_BUTTON_FILL_INSET * 2.0f,
        selected ? COMBAT_BUTTON_ACTIVE_FILL_RGB : COMBAT_BUTTON_FILL_RGB
    );
    drawCombatStyleGlyph(left + COMBAT_STYLE_GLYPH_LEFT_OFFSET, top + COMBAT_STYLE_GLYPH_TOP_OFFSET, attackStyle.name());
    drawCombatTextCentered(
        left + COMBAT_STYLE_TEXT_LEFT_OFFSET,
        top + 5.0f,
        width - COMBAT_STYLE_TEXT_WIDTH_REDUCTION,
        attackStyle.name(),
        COMBAT_HEADING_RGB,
        true
    );
    drawCombatTextCentered(
        left + COMBAT_STYLE_TEXT_LEFT_OFFSET,
        top + 14.0f,
        width - COMBAT_STYLE_TEXT_WIDTH_REDUCTION,
        "(" + attackStyle.stance() + ")",
        COMBAT_HEADING_RGB,
        true
    );
    drawCombatTextCentered(
        left + COMBAT_STYLE_TEXT_LEFT_OFFSET,
        top + 23.0f,
        width - COMBAT_STYLE_TEXT_WIDTH_REDUCTION,
        "(" + attackStyle.combatType() + ")",
        COMBAT_HEADING_RGB,
        true
    );
  }

  private void drawCombatStyleGlyph(float left, float top, String styleName) {
    int rgb = 0x000000;
    float right = left + 24.0f;
    float bottom = top + 18.0f;
    switch (styleName) {
      case "Spike" -> {
        owner.primitives().drawGlyphLine(left + 2.0f, bottom, left + 12.0f, top + 3.0f, rgb);
        owner.primitives().drawGlyphLine(left + 12.0f, top + 3.0f, right - 2.0f, top + 9.0f, rgb);
        owner.primitives().drawGlyphLine(left + 10.0f, top + 5.0f, left + 17.0f, bottom - 1.0f, rgb);
      }
      case "Impale" -> {
        owner.primitives().drawGlyphLine(left + 3.0f, bottom - 1.0f, right - 5.0f, top + 3.0f, rgb);
        owner.primitives().drawGlyphLine(right - 5.0f, top + 3.0f, right - 2.0f, top + 8.0f, rgb);
        owner.primitives().drawGlyphLine(right - 5.0f, top + 3.0f, right - 10.0f, top + 7.0f, rgb);
      }
      case "Smash" -> {
        owner.primitives().outlineRect(left + 2.0f, top + 2.0f, 9.0f, 7.0f, rgb);
        owner.primitives().drawGlyphLine(left + 10.0f, top + 8.0f, right - 3.0f, bottom - 1.0f, rgb);
      }
      case "Block" -> {
        owner.primitives().outlineRect(left + 4.0f, top + 2.0f, 14.0f, 14.0f, rgb);
        owner.primitives().drawGlyphLine(left + 4.0f, top + 2.0f, left + 11.0f, top - 1.0f, rgb);
        owner.primitives().drawGlyphLine(left + 18.0f, top + 2.0f, left + 11.0f, top - 1.0f, rgb);
      }
      default -> owner.primitives().drawGlyphLine(left + 3.0f, bottom - 1.0f, right - 3.0f, top + 2.0f, rgb);
    }
  }

  private void drawCombatText(float left, float top, String text, int rgb, boolean small) {
    TitleScreenBitmapFont font = small ? owner.statsSmallFont() : owner.statsLabelFont();
    if (font != null) {
      owner.primitives().drawLegacyStatsText(font, left, top, text, rgb);
      return;
    }
    owner.primitives().drawShadowedText(
        left,
        top,
        text,
        GameplaySidebarRenderer.rgbUnit(rgb, 16),
        GameplaySidebarRenderer.rgbUnit(rgb, 8),
        GameplaySidebarRenderer.rgbUnit(rgb, 0),
        small ? 0.72f : 0.82f
    );
  }

  private void drawCombatTextCentered(float left, float top, float width, String text, int rgb, boolean small) {
    TitleScreenBitmapFont font = small ? owner.statsSmallFont() : owner.statsLabelFont();
    if (font != null) {
      float centeredLeft = left + width * 0.5f - font.measureText(text) * 0.5f;
      owner.primitives().drawLegacyStatsText(font, centeredLeft, top, text, rgb);
      return;
    }
    owner.primitives().drawShadowedText(
        left + width * 0.5f - text.length() * (small ? 2.2f : 3.0f),
        top,
        text,
        GameplaySidebarRenderer.rgbUnit(rgb, 16),
        GameplaySidebarRenderer.rgbUnit(rgb, 8),
        GameplaySidebarRenderer.rgbUnit(rgb, 0),
        small ? 0.72f : 0.82f
    );
  }
}
