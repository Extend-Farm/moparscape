package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.lwjgl.opengl.GL11.glColor3f;

import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class WidgetScrollbarRenderer {

  private static final int SCROLLBAR_TRACK_RGB = 0x23201b;
  private static final int SCROLLBAR_THUMB_FILL_RGB = 0x4d4233;
  private static final int SCROLLBAR_HIGHLIGHT_RGB = 0x766654;
  private static final int SCROLLBAR_DARK_RGB = 0x332d25;

  private final ImmediateModeRenderer2d primitives;
  private final WidgetTextureCache textureCache;

  WidgetScrollbarRenderer(ImmediateModeRenderer2d primitives, WidgetTextureCache textureCache) {
    this.primitives = primitives;
    this.textureCache = textureCache;
  }

  void drawScrollbar(float left, float top, int viewportHeight, int scrollPosition, int scrollContentHeight) {
    OpenGlTexture topArrowTexture = textureCache.spriteTexture("scrollbar", 0);
    OpenGlTexture bottomArrowTexture = textureCache.spriteTexture("scrollbar", 1);
    if (topArrowTexture != null) {
      primitives.drawTexturedQuad(
          topArrowTexture,
          new ScreenRect(left, top, topArrowTexture.width(), topArrowTexture.height())
      );
    }
    if (bottomArrowTexture != null) {
      primitives.drawTexturedQuad(
          bottomArrowTexture,
          new ScreenRect(left, top + viewportHeight - bottomArrowTexture.height(), bottomArrowTexture.width(), bottomArrowTexture.height())
      );
    }
    fillRect(
        left,
        top + SidebarWidgetScrollState.SCROLLBAR_ARROW_HEIGHT,
        SidebarWidgetScrollState.SCROLLBAR_WIDTH,
        viewportHeight - SidebarWidgetScrollState.SCROLLBAR_ARROW_HEIGHT * 2.0f,
        SCROLLBAR_TRACK_RGB
    );
    int thumbHeight = SidebarWidgetScrollState.scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int thumbOffset = SidebarWidgetScrollState.scrollbarThumbOffset(
        scrollPosition,
        viewportHeight,
        scrollContentHeight,
        thumbHeight
    );
    float thumbTop = top + SidebarWidgetScrollState.SCROLLBAR_ARROW_HEIGHT + thumbOffset;
    fillRect(left, thumbTop, SidebarWidgetScrollState.SCROLLBAR_WIDTH, thumbHeight, SCROLLBAR_THUMB_FILL_RGB);
    drawVerticalLine(left, thumbTop, thumbHeight, SCROLLBAR_HIGHLIGHT_RGB);
    drawVerticalLine(left + 1.0f, thumbTop, thumbHeight, SCROLLBAR_HIGHLIGHT_RGB);
    drawHorizontalLine(left, thumbTop, SidebarWidgetScrollState.SCROLLBAR_WIDTH, SCROLLBAR_HIGHLIGHT_RGB);
    drawHorizontalLine(left, thumbTop + 1.0f, SidebarWidgetScrollState.SCROLLBAR_WIDTH, SCROLLBAR_HIGHLIGHT_RGB);
    drawVerticalLine(left + SidebarWidgetScrollState.SCROLLBAR_WIDTH - 1.0f, thumbTop, thumbHeight, SCROLLBAR_DARK_RGB);
    drawVerticalLine(
        left + SidebarWidgetScrollState.SCROLLBAR_WIDTH - 2.0f,
        thumbTop + 1.0f,
        thumbHeight - 1.0f,
        SCROLLBAR_DARK_RGB
    );
    drawHorizontalLine(left, thumbTop + thumbHeight - 1.0f, SidebarWidgetScrollState.SCROLLBAR_WIDTH, SCROLLBAR_DARK_RGB);
    drawHorizontalLine(
        left + 1.0f,
        thumbTop + thumbHeight - 2.0f,
        SidebarWidgetScrollState.SCROLLBAR_WIDTH - 1.0f,
        SCROLLBAR_DARK_RGB
    );
  }

  private void fillRect(float left, float top, float width, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawQuad(left, top, width, height);
  }

  private void drawHorizontalLine(float left, float top, float width, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawLine(left, top, left + width - 1.0f, top);
  }

  private void drawVerticalLine(float left, float top, float height, int rgb) {
    glColor3f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0));
    primitives.drawLine(left, top, left, top + height - 1.0f);
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }
}
