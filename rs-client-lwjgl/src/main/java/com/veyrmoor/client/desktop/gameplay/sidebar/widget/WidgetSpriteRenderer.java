package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import com.veyrmoor.client.desktop.gameplay.sidebar.GameplayCombatSidebarModel;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentDefinition;

final class WidgetSpriteRenderer {

  private static final int SEND_WEAPON_WIDGET_ZOOM_SCALE = 200;

  private final ImmediateModeRenderer2d primitives;
  private final WidgetTextureCache textureCache;

  WidgetSpriteRenderer(ImmediateModeRenderer2d primitives, WidgetTextureCache textureCache) {
    this.primitives = primitives;
    this.textureCache = textureCache;
  }

  void drawSprite(InterfaceComponentDefinition component, float left, float top) {
    OpenGlTexture spriteTexture = textureCache.spriteTexture(component.defaultSprite());
    if (spriteTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        spriteTexture,
        new ScreenRect(left, top, spriteTexture.width(), spriteTexture.height())
    );
  }

  void drawModelPreview(
      InterfaceComponentDefinition component,
      float left,
      float top,
      GameplayCombatSidebarModel combatModel
  ) {
    if (combatModel == null) {
      return;
    }
    if (component.id() != combatModel.itemComponentId() || combatModel.weaponItemId() < 0) {
      return;
    }
    OpenGlTexture weaponTexture =
        textureCache.weaponPreviewTexture(combatModel.weaponItemId(), SEND_WEAPON_WIDGET_ZOOM_SCALE);
    if (weaponTexture == null) {
      weaponTexture = textureCache.itemTexture(combatModel.weaponItemId());
    }
    if (weaponTexture == null) {
      return;
    }
    float drawWidth = Math.min(component.width(), weaponTexture.width());
    float drawHeight = Math.min(component.height(), weaponTexture.height());
    float drawLeft = left + (component.width() - drawWidth) * 0.5f;
    float drawTop = top + (component.height() - drawHeight) * 0.5f;
    primitives.drawTexturedQuad(weaponTexture, new ScreenRect(drawLeft, drawTop, drawWidth, drawHeight));
  }
}
