package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;

final class SidebarWidgetRenderer implements AutoCloseable {

  private static final int SEND_WEAPON_WIDGET_ZOOM_SCALE = 200;
  private static final int SCROLLBAR_TRACK_RGB = 0x23201b;
  private static final int SCROLLBAR_THUMB_FILL_RGB = 0x4d4233;
  private static final int SCROLLBAR_HIGHLIGHT_RGB = 0x766654;
  private static final int SCROLLBAR_DARK_RGB = 0x332d25;

  private final InterfaceComponentCatalog interfaceComponents;
  private final ArchiveSpriteResolver mediaSpriteResolver;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenFonts fonts;
  private final ImmediateModeRenderer2d primitives;
  private final SidebarWidgetScrollState scrollState;
  private final SidebarWidgetLocator locator;
  private final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4);
  private final Map<SpriteKey, OpenGlTexture> spriteTextures = new HashMap<>();
  private final Map<TextKey, BitmapTextTexture> textTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> itemTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> weaponPreviewTextures = new HashMap<>();

  SidebarWidgetRenderer(
      InterfaceComponentCatalog interfaceComponents,
      ArchiveSpriteResolver mediaSpriteResolver,
      ItemIconRenderer itemIconRenderer,
      TitleScreenFonts fonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.interfaceComponents = interfaceComponents;
    this.mediaSpriteResolver = mediaSpriteResolver;
    this.itemIconRenderer = itemIconRenderer;
    this.fonts = fonts;
    this.primitives = primitives;
    this.scrollState = new SidebarWidgetScrollState();
    this.locator = new SidebarWidgetLocator(interfaceComponents, scrollState);
  }

  boolean canRender(int interfaceId) {
    return interfaceComponents != null
        && mediaSpriteResolver != null
        && interfaceComponents.getOrNull(interfaceId) != null;
  }

  boolean canRender(GameplayCombatSidebarModel combatModel) {
    return combatModel != null && canRender(combatModel.interfaceId());
  }

  void draw(ScreenRect sidebarRect, int interfaceId, ClientViewModel viewModel) {
    draw(sidebarRect, interfaceId, viewModel, Double.NaN, Double.NaN);
  }

  void draw(ScreenRect sidebarRect, int interfaceId, ClientViewModel viewModel, double pointerX, double pointerY) {
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    int hoveredWidgetId = locator.hoveredWidgetId(root, sidebarRect.left(), sidebarRect.top(), null, pointerX, pointerY);
    renderComponent(
        root,
        sidebarRect.left(),
        sidebarRect.top(),
        new WidgetRenderContext(viewModel, null, viewModel == null ? null : GameplayStatsSidebarModel.from(viewModel), hoveredWidgetId),
        null
    );
  }

  void draw(ScreenRect sidebarRect, GameplayCombatSidebarModel combatModel, ClientViewModel viewModel) {
    InterfaceComponentDefinition root = interfaceComponents.require(combatModel.interfaceId());
    renderComponent(
        root,
        sidebarRect.left(),
        sidebarRect.top(),
        new WidgetRenderContext(viewModel, combatModel, viewModel == null ? null : GameplayStatsSidebarModel.from(viewModel), -1),
        null
    );
  }

  boolean handleScrollbarClick(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return false;
    }
    return scrollState.handleScrollbarClick(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        x,
        y,
        locator
    );
  }

  boolean handleScroll(int interfaceId, ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y) || yOffset == 0.0d) {
      return false;
    }
    return scrollState.handleScroll(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        x,
        y,
        yOffset,
        locator
    );
  }

  boolean handlePointerMove(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId)) {
      return false;
    }
    return scrollState.handlePointerMove(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        x,
        y,
        locator
    );
  }

  void endPointerDrag() {
    scrollState.endPointerDrag();
  }

  void clearTransientState() {
    scrollState.clearTransientState();
  }

  boolean hasActionAt(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return false;
    }
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    return locator.hasActionAt(root, sidebarRect.left(), sidebarRect.top(), x, y);
  }

  @Override
  public void close() {
    for (OpenGlTexture texture : spriteTextures.values()) {
      closeTexture(texture);
    }
    spriteTextures.clear();
    for (BitmapTextTexture texture : textTextures.values()) {
      closeTexture(texture.texture());
    }
    textTextures.clear();
    for (OpenGlTexture texture : itemTextures.values()) {
      closeTexture(texture);
    }
    itemTextures.clear();
    for (OpenGlTexture texture : weaponPreviewTextures.values()) {
      closeTexture(texture);
    }
    weaponPreviewTextures.clear();
  }

  private void renderComponent(
      InterfaceComponentDefinition component,
      float left,
      float top,
      WidgetRenderContext context,
      ScreenRect clipRect
  ) {
    if (component == null) {
      return;
    }
    if (component.componentType() == 0 && component.container().hidden() && component.id() != context.hoveredWidgetId()) {
      return;
    }
    switch (component.componentType()) {
      case 0 -> renderContainer(component, left, top, context, clipRect);
      case 3 -> drawRectangle(component, left, top, context.hoveredWidgetId());
      case 4 -> drawText(component, left, top, context);
      case 5 -> drawSprite(component, left, top);
      case 6 -> drawModelPreview(component, left, top, context.combatModel());
      default -> {
        // Current native sidebar widget rendering only needs containers, rectangles, text, sprites,
        // and optional model preview overrides.
      }
    }
  }

  private void renderContainer(
      InterfaceComponentDefinition container,
      float left,
      float top,
      WidgetRenderContext context,
      ScreenRect clipRect
  ) {
    int scrollPosition = scrollState.scrollPosition(container);
    ScreenRect containerRect = SidebarWidgetLocator.componentBounds(container, left, top);
    ScreenRect containerClipRect = SidebarWidgetLocator.intersectClipRect(clipRect, containerRect);
    ScreenRect previousClipRect = clipRect;
    applyClipRect(containerClipRect);
    int[] childIds = container.container().childIds();
    int[] childX = container.container().childX();
    int[] childY = container.container().childY();
    for (int childIndex = 0; childIndex < childIds.length; childIndex++) {
      InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
      if (child == null) {
        continue;
      }
      renderComponent(
          child,
          left + childX[childIndex],
          top + childY[childIndex] - scrollPosition,
          context,
          containerClipRect
      );
    }
    applyClipRect(previousClipRect);
    if (container.container().scrollContentHeight() > container.height()) {
      drawScrollbar(left + container.width(), top, container.height(), scrollPosition, container.container().scrollContentHeight());
    }
  }

  private void drawRectangle(InterfaceComponentDefinition component, float left, float top, int hoveredWidgetId) {
    int rgb = componentRgb(component, hoveredWidgetId);
    float alpha = component.alpha() == 0 ? 1.0f : Math.max(0.0f, Math.min(1.0f, (256.0f - component.alpha()) / 256.0f));
    glColor4f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0), alpha);
    primitives.drawQuad(left, top, component.width(), component.height());
  }

  private void drawText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      WidgetRenderContext context
  ) {
    String text = resolveText(component, context);
    if (text == null || text.isEmpty()) {
      return;
    }
    TitleScreenBitmapFont font = fontFor(component.textBlock().fontIndex());
    if (font == null) {
      drawFallbackText(component, left, top, text, context.hoveredWidgetId());
      return;
    }
    int baselineY = Math.round(top + font.maxGlyphHeight());
    int rgb = componentRgb(component, context.hoveredWidgetId());
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
          drawLeft = left + component.width() * 0.5f - font.measureText(line) * 0.5f;
        }
        BitmapTextTexture textTexture = textTexture(font, line, rgb, component.textBlock().shadow());
        if (textTexture != null) {
          primitives.drawTexturedQuad(
              textTexture.texture(),
              new ScreenRect(
                  drawLeft + textTexture.drawOffsetX(),
                  lineY - font.maxGlyphHeight() + textTexture.drawOffsetY(),
                  textTexture.texture().width(),
                  textTexture.texture().height()
              )
          );
        }
      }
      lineY += lineHeight;
      if (lineBreak < 0) {
        return;
      }
      lineStart = lineBreak + 2;
    }
  }

  private void drawFallbackText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      String text,
      int hoveredWidgetId
  ) {
    int rgb = componentRgb(component, hoveredWidgetId);
    float red = rgbUnit(rgb, 16);
    float green = rgbUnit(rgb, 8);
    float blue = rgbUnit(rgb, 0);
    glColor3f(red, green, blue);
    primitives.drawText(left, top, text, red, green, blue, 0.8f);
  }

  private void drawSprite(InterfaceComponentDefinition component, float left, float top) {
    InterfaceComponentDefinition.SpriteReference spriteReference = component.defaultSprite();
    if (spriteReference == null) {
      return;
    }
    OpenGlTexture spriteTexture = spriteTexture(spriteReference);
    if (spriteTexture == null) {
      return;
    }
    primitives.drawTexturedQuad(
        spriteTexture,
        new ScreenRect(left, top, spriteTexture.width(), spriteTexture.height())
    );
  }

  private void drawModelPreview(
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
    OpenGlTexture weaponTexture = weaponPreviewTexture(combatModel.weaponItemId());
    if (weaponTexture == null) {
      weaponTexture = itemTexture(combatModel.weaponItemId());
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

  private String overrideText(int componentId, GameplayCombatSidebarModel combatModel) {
    if (combatModel == null) {
      return null;
    }
    if (componentId == combatModel.weaponNameComponentId()) {
      return combatModel.weaponName();
    }
    return null;
  }

  private String resolveText(InterfaceComponentDefinition component, WidgetRenderContext context) {
    String text = overrideText(component.id(), context.combatModel());
    if (text == null || text.isEmpty()) {
      text = component.textBlock().defaultText();
    }
    return SidebarWidgetTextResolver.interpolate(component, text, context.viewModel(), context.statsModel());
  }

  private int componentRgb(InterfaceComponentDefinition component, int hoveredWidgetId) {
    return component.id() == hoveredWidgetId && component.colors().hoverColor() != 0
        ? component.colors().hoverColor()
        : component.colors().defaultColor();
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

  private OpenGlTexture spriteTexture(InterfaceComponentDefinition.SpriteReference spriteReference) {
    if (spriteReference == null) {
      return null;
    }
    SpriteKey key = new SpriteKey(spriteReference.entryName(), spriteReference.frameIndex());
    if (spriteTextures.containsKey(key)) {
      return spriteTextures.get(key);
    }
    ArgbImage image = mediaSpriteResolver.resolve(key.entryName(), key.frameIndex());
    OpenGlTexture texture = image == null ? null : OpenGlTexture.fromArgbImage(image);
    spriteTextures.put(key, texture);
    return texture;
  }

  private BitmapTextTexture textTexture(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
    if (font == null || text == null || text.isEmpty()) {
      return null;
    }
    return textTextures.computeIfAbsent(
        new TextKey(font, text, rgb, shadow),
        key -> {
          TextTextureLayout layout = textTextureLayout(key.font(), key.text(), key.shadow());
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
          OpenGlTexture texture = OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels));
          return texture == null ? null : new BitmapTextTexture(texture, layout.canvasLeft(), layout.canvasTop());
        }
    );
  }

  static TextTextureLayout textTextureLayout(TitleScreenBitmapFont font, String text, boolean shadow) {
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
    return new TextTextureLayout(canvasLeft, canvasTop, width, height, baselineY);
  }

  private OpenGlTexture weaponPreviewTexture(int itemId) {
    if (itemIconRenderer == null || itemId < 0) {
      return null;
    }
    if (weaponPreviewTextures.containsKey(itemId)) {
      return weaponPreviewTextures.get(itemId);
    }
    ArgbImage image = itemIconRenderer.renderWidgetPreview(itemId, SEND_WEAPON_WIDGET_ZOOM_SCALE);
    OpenGlTexture texture = image == null ? null : OpenGlTexture.fromArgbImage(image);
    weaponPreviewTextures.put(itemId, texture);
    return texture;
  }

  private OpenGlTexture itemTexture(int itemId) {
    if (itemIconRenderer == null || itemId < 0) {
      return null;
    }
    int iconKey = itemIconRenderer.iconKey(itemId, 1);
    if (iconKey < 0) {
      return null;
    }
    if (itemTextures.containsKey(iconKey)) {
      return itemTextures.get(iconKey);
    }
    ArgbImage image = itemIconRenderer.render(itemId, 1);
    OpenGlTexture texture = image == null ? null : OpenGlTexture.fromArgbImage(image);
    itemTextures.put(iconKey, texture);
    return texture;
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }

  private void drawScrollbar(float left, float top, int viewportHeight, int scrollPosition, int scrollContentHeight) {
    OpenGlTexture topArrowTexture = spriteTexture(new InterfaceComponentDefinition.SpriteReference("scrollbar", 0));
    OpenGlTexture bottomArrowTexture = spriteTexture(new InterfaceComponentDefinition.SpriteReference("scrollbar", 1));
    if (topArrowTexture != null) {
      primitives.drawTexturedQuad(topArrowTexture, new ScreenRect(left, top, topArrowTexture.width(), topArrowTexture.height()));
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
    int thumbOffset = SidebarWidgetScrollState.scrollbarThumbOffset(scrollPosition, viewportHeight, scrollContentHeight, thumbHeight);
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

  private void applyClipRect(ScreenRect clipRect) {
    if (clipRect == null) {
      glDisable(GL_SCISSOR_TEST);
      return;
    }
    int scissorWidth = Math.max(0, Math.round(clipRect.width()));
    int scissorHeight = Math.max(0, Math.round(clipRect.height()));
    if (scissorWidth <= 0 || scissorHeight <= 0) {
      glEnable(GL_SCISSOR_TEST);
      glScissor(0, 0, 0, 0);
      return;
    }
    viewportBuffer.clear();
    glGetIntegerv(GL_VIEWPORT, viewportBuffer);
    int viewportHeight = viewportBuffer.get(3);
    int scissorLeft = Math.round(clipRect.left());
    int scissorBottom = viewportHeight - Math.round(clipRect.top() + clipRect.height());
    glEnable(GL_SCISSOR_TEST);
    glScissor(scissorLeft, scissorBottom, scissorWidth, scissorHeight);
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

  private record SpriteKey(String entryName, int frameIndex) {
  }

  private record TextKey(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
  }

  private record BitmapTextTexture(OpenGlTexture texture, int drawOffsetX, int drawOffsetY) {
  }

  private record WidgetRenderContext(
      ClientViewModel viewModel,
      GameplayCombatSidebarModel combatModel,
      GameplayStatsSidebarModel statsModel,
      int hoveredWidgetId
  ) {
  }

  record TextTextureLayout(int canvasLeft, int canvasTop, int width, int height, int baselineY) {
  }
}
