package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.lwjgl.opengl.GL11.glColor3f;

import com.veyrmoor.client.desktop.gameplay.sidebar.GameplayCombatSidebarModel;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentDefinition;
import java.util.ArrayList;
import java.util.List;

final class WidgetTextRenderer {

  private final TitleScreenFonts fonts;
  private final ImmediateModeRenderer2d primitives;
  private final WidgetTextureCache textureCache;

  WidgetTextRenderer(
      TitleScreenFonts fonts,
      ImmediateModeRenderer2d primitives,
      WidgetTextureCache textureCache
  ) {
    this.fonts = fonts;
    this.primitives = primitives;
    this.textureCache = textureCache;
  }

  void drawText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      SidebarWidgetRenderer.WidgetRenderContext context
  ) {
    String text = resolveText(component, context);
    if (text == null || text.isEmpty()) {
      return;
    }
    TitleScreenBitmapFont font = fontFor(component.textBlock().fontIndex());
    if (font == null) {
      drawFallbackText(component, left, top, text, context.hoveredWidgetId(), context.overrideResolver());
      return;
    }
    int baselineY = Math.round(top + font.maxGlyphHeight());
    int rgb = componentRgb(component, context.hoveredWidgetId(), context.overrideResolver());
    int lineHeight = font.maxGlyphHeight();
    int lineY = baselineY;
    int lineStart = 0;
    while (lineStart <= text.length()) {
      int lineBreak = text.indexOf("\\n", lineStart);
      int lineEnd = lineBreak >= 0 ? lineBreak : text.length();
      String line = text.substring(lineStart, lineEnd);
      if (!line.isEmpty()) {
        float drawLeft = left;
        if (component.textBlock().centered()) {
          drawLeft = left + component.width() * 0.5f - measureTaggedText(font, line) * 0.5f;
        }
        drawTextLine(font, drawLeft, lineY, line, rgb, component.textBlock().shadow());
      }
      lineY += lineHeight;
      if (lineBreak < 0) {
        return;
      }
      lineStart = lineBreak + 2;
    }
  }

  int componentRgb(
      InterfaceComponentDefinition component,
      int hoveredWidgetId,
      SidebarWidgetRenderer.WidgetOverrideResolver overrideResolver
  ) {
    if (component.id() == hoveredWidgetId && component.colors().hoverColor() != 0) {
      return component.colors().hoverColor();
    }
    SidebarWidgetRenderer.WidgetOverride widgetOverride = overrideResolver.resolve(component.id());
    if (widgetOverride != null && widgetOverride.defaultColor() != null) {
      return widgetOverride.defaultColor();
    }
    return component.colors().defaultColor();
  }

  static SidebarWidgetRenderer.TextTextureLayout textTextureLayout(
      TitleScreenBitmapFont font,
      String text,
      boolean shadow
  ) {
    int visibleLeft = font.visibleLeftOffset(text);
    int visibleWidth = font.measureVisibleWidth(text);
    int advanceWidth = font.measureText(text);
    int visibleRight = visibleLeft + visibleWidth;
    int visibleTop = font.visibleTopOffset(text);
    int visibleHeight = font.measureVisibleHeight(text);
    int visibleBottom = visibleTop + visibleHeight;
    int canvasLeft = Math.min(0, visibleLeft);
    int canvasTop = Math.min(0, visibleTop);
    int canvasRight = Math.max(advanceWidth, visibleRight);
    int canvasBottom = Math.max(font.maxGlyphHeight(), visibleBottom);
    int shadowPadding = shadow ? 1 : 0;
    int width = Math.max(1, canvasRight - canvasLeft + shadowPadding);
    int height = Math.max(1, canvasBottom - canvasTop + shadowPadding);
    int baselineY = font.maxGlyphHeight() - canvasTop;
    return new SidebarWidgetRenderer.TextTextureLayout(canvasLeft, canvasTop, width, height, baselineY);
  }

  static int measureTaggedText(TitleScreenBitmapFont font, String text) {
    if (font == null || text == null || text.isEmpty()) {
      return 0;
    }
    int width = 0;
    for (TextRun run : textRuns(text, 0)) {
      width += font.measureText(run.text());
    }
    return width;
  }

  static String stripColorTags(String text) {
    if (text == null || text.isEmpty()) {
      return "";
    }
    StringBuilder strippedText = new StringBuilder(text.length());
    for (int index = 0; index < text.length(); index++) {
      if (isColorTagAt(text, index)) {
        index += 4;
        continue;
      }
      strippedText.append(text.charAt(index));
    }
    return strippedText.toString();
  }

  private void drawFallbackText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      String text,
      int hoveredWidgetId,
      SidebarWidgetRenderer.WidgetOverrideResolver overrideResolver
  ) {
    int rgb = componentRgb(component, hoveredWidgetId, overrideResolver);
    float red = rgbUnit(rgb, 16);
    float green = rgbUnit(rgb, 8);
    float blue = rgbUnit(rgb, 0);
    glColor3f(red, green, blue);
    primitives.drawText(left, top, stripColorTags(text), red, green, blue, 0.8f);
  }

  private void drawTextLine(
      TitleScreenBitmapFont font,
      float left,
      int baselineY,
      String text,
      int defaultRgb,
      boolean shadow
  ) {
    if (!containsColorTags(text)) {
      SidebarWidgetRenderer.BitmapTextTexture textTexture =
          textureCache.bitmapTextTexture(font, text, defaultRgb, shadow);
      if (textTexture == null) {
        return;
      }
      primitives.drawTexturedQuad(
          textTexture.texture(),
          new ScreenRect(
              left + textTexture.drawOffsetX(),
              baselineY - font.maxGlyphHeight() + textTexture.drawOffsetY(),
              textTexture.texture().width(),
              textTexture.texture().height()
          )
      );
      return;
    }
    float cursorLeft = left;
    for (TextRun textRun : textRuns(text, defaultRgb)) {
      SidebarWidgetRenderer.BitmapTextTexture textTexture =
          textureCache.bitmapTextTexture(font, textRun.text(), textRun.rgb(), shadow);
      if (textTexture != null) {
        primitives.drawTexturedQuad(
            textTexture.texture(),
            new ScreenRect(
                cursorLeft + textTexture.drawOffsetX(),
                baselineY - font.maxGlyphHeight() + textTexture.drawOffsetY(),
                textTexture.texture().width(),
                textTexture.texture().height()
            )
        );
      }
      cursorLeft += font.measureText(textRun.text());
    }
  }

  private String resolveText(
      InterfaceComponentDefinition component,
      SidebarWidgetRenderer.WidgetRenderContext context
  ) {
    SidebarWidgetRenderer.WidgetOverride widgetOverride = context.overrideResolver().resolve(component.id());
    String text = widgetOverride != null && widgetOverride.text() != null ? widgetOverride.text() : null;
    if (text == null) {
      text = overrideText(component.id(), context.combatModel());
    }
    if (text == null) {
      text = component.textBlock().defaultText();
    }
    return SidebarWidgetTextResolver.interpolate(component, text, context.viewModel(), context.statsModel());
  }

  private String overrideText(int componentId, GameplayCombatSidebarModel combatModel) {
    if (combatModel == null) {
      return null;
    }
    if (componentId == combatModel.weaponNameComponentId()) {
      return combatModel.weaponName();
    }
    return null;
  }

  private TitleScreenBitmapFont fontFor(int fontIndex) {
    if (fonts == null) {
      return null;
    }
    return switch (fontIndex) {
      case 0 -> fonts.plainSmall();
      case 1 -> fonts.plain();
      case 2 -> fonts.bold();
      default -> null;
    };
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  private static boolean containsColorTags(String text) {
    if (text == null || text.isEmpty()) {
      return false;
    }
    for (int index = 0; index < text.length(); index++) {
      if (isColorTagAt(text, index)) {
        return true;
      }
    }
    return false;
  }

  private static List<TextRun> textRuns(String text, int defaultRgb) {
    List<TextRun> runs = new ArrayList<>();
    if (text == null || text.isEmpty()) {
      return runs;
    }
    int currentRgb = defaultRgb;
    StringBuilder currentText = new StringBuilder(text.length());
    for (int index = 0; index < text.length(); index++) {
      if (isColorTagAt(text, index)) {
        if (!currentText.isEmpty()) {
          runs.add(new TextRun(currentText.toString(), currentRgb));
          currentText.setLength(0);
        }
        currentRgb = legacyColorTagRgb(text.substring(index + 1, index + 4), currentRgb);
        index += 4;
        continue;
      }
      currentText.append(text.charAt(index));
    }
    if (!currentText.isEmpty()) {
      runs.add(new TextRun(currentText.toString(), currentRgb));
    }
    return runs;
  }

  private static boolean isColorTagAt(String text, int index) {
    return text != null
        && index >= 0
        && index + 4 < text.length()
        && text.charAt(index) == '@'
        && text.charAt(index + 4) == '@'
        && legacyColorTagRgb(text.substring(index + 1, index + 4), -1) >= 0;
  }

  private static int legacyColorTagRgb(String tag, int defaultRgb) {
    return switch (tag) {
      case "red" -> 0xff0000;
      case "gre" -> 0x00ff00;
      case "blu" -> 0x0000ff;
      case "yel" -> 0xffff00;
      case "cya" -> 0x00ffff;
      case "mag" -> 0xff00ff;
      case "whi" -> 0xffffff;
      case "bla" -> 0x000000;
      case "lre" -> 0xff9040;
      case "dre" -> 0x800000;
      case "dbl" -> 0x000080;
      case "or1" -> 0xffb000;
      case "or2" -> 0xff7000;
      case "or3" -> 0xff3000;
      case "gr1" -> 0xc0ff00;
      case "gr2" -> 0x80ff00;
      case "gr3" -> 0x40ff00;
      default -> defaultRgb;
    };
  }

  private record TextRun(String text, int rgb) {
  }
}
