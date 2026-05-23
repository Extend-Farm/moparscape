package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;

import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class SidebarRenderPrimitives {

  private final ImmediateModeRenderer2d renderer;
  private final SidebarTextureCache textureCache;

  SidebarRenderPrimitives(ImmediateModeRenderer2d renderer, SidebarTextureCache textureCache) {
    this.renderer = renderer;
    this.textureCache = textureCache;
  }

  void drawText(float left, float top, String text, float red, float green, float blue) {
    renderer.drawText(left, top, text, red, green, blue);
  }

  void drawText(float left, float top, String text, float red, float green, float blue, float scale) {
    renderer.drawText(left, top, text, red, green, blue, scale);
  }

  void drawTexturedQuad(OpenGlTexture texture, ScreenRect rect) {
    renderer.drawTexturedQuad(texture, rect);
  }

  void drawShadowedText(float left, float top, String text, float red, float green, float blue) {
    drawShadowedText(left, top, text, red, green, blue, 1.0f);
  }

  void drawShadowedText(float left, float top, String text, float red, float green, float blue, float scale) {
    float shadowOffset = scale < 0.8f ? 0.75f : 1.0f;
    renderer.drawText(left + shadowOffset, top + shadowOffset, text, 0.0f, 0.0f, 0.0f, scale);
    renderer.drawText(left, top, text, red, green, blue, scale);
  }

  void drawLegacyStatsText(TitleScreenBitmapFont font, float left, float top, String text, int rgb) {
    OpenGlTexture textTexture = textureCache.bitmapTextTexture(font, text, rgb, true);
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
    drawTexturedQuad(texture, new ScreenRect(left, top, texture.width(), texture.height()));
  }

  void fillRect(float left, float top, float width, float height, int rgb) {
    glColor3f(GameplaySidebarRenderer.rgbUnit(rgb, 16), GameplaySidebarRenderer.rgbUnit(rgb, 8), GameplaySidebarRenderer.rgbUnit(rgb, 0));
    renderer.drawQuad(left, top, width, height);
  }

  void outlineRect(float left, float top, float width, float height, int rgb) {
    glColor3f(GameplaySidebarRenderer.rgbUnit(rgb, 16), GameplaySidebarRenderer.rgbUnit(rgb, 8), GameplaySidebarRenderer.rgbUnit(rgb, 0));
    renderer.drawOutline(left, top, width, height);
  }

  void drawGlyphLine(float startX, float startY, float endX, float endY, int rgb) {
    glColor3f(GameplaySidebarRenderer.rgbUnit(rgb, 16), GameplaySidebarRenderer.rgbUnit(rgb, 8), GameplaySidebarRenderer.rgbUnit(rgb, 0));
    renderer.drawLine(startX, startY, endX, endY);
  }
}
