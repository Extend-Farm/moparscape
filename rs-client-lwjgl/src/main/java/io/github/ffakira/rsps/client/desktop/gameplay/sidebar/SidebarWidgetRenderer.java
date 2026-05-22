package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetIntegerv;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ArchiveSpriteResolver;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.itemicon.ItemIconRenderer;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenBitmapFont;
import io.github.ffakira.rsps.client.desktop.login.TitleScreenFonts;
import io.github.ffakira.rsps.content.InterfaceComponentCatalog;
import io.github.ffakira.rsps.content.InterfaceComponentDefinition;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;

final class SidebarWidgetRenderer implements AutoCloseable {

  private static final int SEND_WEAPON_WIDGET_ZOOM_SCALE = 200;
  private static final int SCROLLBAR_ARROW_SCROLL_STEP = 4;
  private static final int SCROLLBAR_WHEEL_SCROLL_STEP = 30;
  private static final int SCROLLBAR_TRACK_RGB = 0x23201b;
  private static final int SCROLLBAR_THUMB_FILL_RGB = 0x4d4233;
  private static final int SCROLLBAR_HIGHLIGHT_RGB = 0x766654;
  private static final int SCROLLBAR_DARK_RGB = 0x332d25;
  private static final int SCROLLBAR_WIDTH = 16;
  private static final int SCROLLBAR_ARROW_HEIGHT = 16;

  private final InterfaceComponentCatalog interfaceComponents;
  private final ArchiveSpriteResolver mediaSpriteResolver;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenFonts fonts;
  private final ImmediateModeRenderer2d primitives;
  private final IntBuffer viewportBuffer = BufferUtils.createIntBuffer(4);
  private final Map<Integer, Integer> scrollPositions = new HashMap<>();
  private final Map<SpriteKey, OpenGlTexture> spriteTextures = new HashMap<>();
  private final Map<TextKey, BitmapTextTexture> textTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> itemTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> weaponPreviewTextures = new HashMap<>();
  private ScrollbarDragState scrollbarDragState;

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
  }

  boolean canRender(int interfaceId) {
    return interfaceComponents != null
        && mediaSpriteResolver != null
        && interfaceComponents.getOrNull(interfaceId) != null;
  }

  boolean canRender(GameplayCombatSidebarModel combatModel) {
    return combatModel != null && canRender(combatModel.interfaceId());
  }

  void draw(ScreenRect sidebarRect, int interfaceId) {
    draw(sidebarRect, interfaceId, Double.NaN, Double.NaN);
  }

  void draw(ScreenRect sidebarRect, int interfaceId, double pointerX, double pointerY) {
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    int hoveredWidgetId = hoveredWidgetId(root, sidebarRect.left(), sidebarRect.top(), null, pointerX, pointerY);
    renderComponent(root, sidebarRect.left(), sidebarRect.top(), null, null, hoveredWidgetId);
  }

  void draw(ScreenRect sidebarRect, GameplayCombatSidebarModel combatModel) {
    InterfaceComponentDefinition root = interfaceComponents.require(combatModel.interfaceId());
    renderComponent(root, sidebarRect.left(), sidebarRect.top(), combatModel, null, -1);
  }

  boolean handleScrollbarClick(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return false;
    }
    ScrollTarget scrollTarget = findScrollTarget(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        null,
        x,
        y
    );
    if (scrollTarget == null || !scrollTarget.overScrollbar()) {
      return false;
    }
    int viewportHeight = scrollTarget.container().height();
    int scrollContentHeight = scrollTarget.container().container().scrollContentHeight();
    int currentScrollPosition = scrollPosition(scrollTarget.container());
    ScreenRect scrollbarRect = scrollbarRect(scrollTarget.container(), scrollTarget.left(), scrollTarget.top());
    if (topArrowRect(scrollbarRect).contains(x, y)) {
      scrollbarDragState = null;
      setScrollPosition(scrollTarget.container(), currentScrollPosition - SCROLLBAR_ARROW_SCROLL_STEP);
      return true;
    }
    if (bottomArrowRect(scrollbarRect).contains(x, y)) {
      scrollbarDragState = null;
      setScrollPosition(scrollTarget.container(), currentScrollPosition + SCROLLBAR_ARROW_SCROLL_STEP);
      return true;
    }
    int thumbHeight = scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    ScreenRect thumbRect = scrollbarThumbRect(scrollTarget.container(), scrollTarget.left(), scrollTarget.top(), currentScrollPosition);
    if (thumbRect.contains(x, y)) {
      scrollbarDragState = new ScrollbarDragState(scrollTarget.container().id(), y - thumbRect.top());
      return true;
    }
    scrollbarDragState = null;
    int nextScrollPosition = trackScrollPosition(
        y,
        scrollbarRect.top(),
        viewportHeight,
        scrollContentHeight,
        thumbHeight
    );
    setScrollPosition(scrollTarget.container(), nextScrollPosition);
    return true;
  }

  boolean handleScroll(int interfaceId, ScreenRect sidebarRect, double x, double y, double yOffset) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y) || yOffset == 0.0d) {
      return false;
    }
    ScrollTarget scrollTarget = findScrollTarget(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        null,
        x,
        y
    );
    if (scrollTarget == null) {
      return false;
    }
    int direction = yOffset > 0.0d ? -1 : 1;
    setScrollPosition(scrollTarget.container(), scrollPosition(scrollTarget.container()) + direction * SCROLLBAR_WHEEL_SCROLL_STEP);
    return true;
  }

  boolean handlePointerMove(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (scrollbarDragState == null || !canRender(interfaceId)) {
      return false;
    }
    ScrollTarget scrollTarget = findScrollTargetByContainerId(
        interfaceComponents.require(interfaceId),
        sidebarRect.left(),
        sidebarRect.top(),
        null,
        scrollbarDragState.containerId()
    );
    if (scrollTarget == null) {
      scrollbarDragState = null;
      return false;
    }
    InterfaceComponentDefinition container = scrollTarget.container();
    int viewportHeight = container.height();
    int scrollContentHeight = container.container().scrollContentHeight();
    int thumbHeight = scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int nextScrollPosition = dragScrollPosition(
        y,
        scrollTarget.top(),
        viewportHeight,
        scrollContentHeight,
        thumbHeight,
        scrollbarDragState.thumbGrabOffsetY()
    );
    setScrollPosition(container, nextScrollPosition);
    return true;
  }

  void endPointerDrag() {
    scrollbarDragState = null;
  }

  void clearTransientState() {
    scrollbarDragState = null;
  }

  boolean hasActionAt(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return false;
    }
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    return hasActionAt(root, sidebarRect.left(), sidebarRect.top(), x, y);
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
      GameplayCombatSidebarModel combatModel,
      ScreenRect clipRect,
      int hoveredWidgetId
  ) {
    if (component == null) {
      return;
    }
    if (component.componentType() == 0 && component.container().hidden() && component.id() != hoveredWidgetId) {
      return;
    }
    switch (component.componentType()) {
      case 0 -> renderContainer(component, left, top, combatModel, clipRect, hoveredWidgetId);
      case 3 -> drawRectangle(component, left, top, hoveredWidgetId);
      case 4 -> drawText(component, left, top, combatModel, hoveredWidgetId);
      case 5 -> drawSprite(component, left, top);
      case 6 -> drawModelPreview(component, left, top, combatModel);
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
      GameplayCombatSidebarModel combatModel,
      ScreenRect clipRect,
      int hoveredWidgetId
  ) {
    int scrollPosition = scrollPosition(container);
    ScreenRect containerRect = componentBounds(container, left, top);
    ScreenRect containerClipRect = intersectClipRect(clipRect, containerRect);
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
          combatModel,
          containerClipRect,
          hoveredWidgetId
      );
    }
    applyClipRect(previousClipRect);
    if (container.container().scrollContentHeight() > container.height()) {
      drawScrollbar(left + container.width(), top, container.height(), scrollPosition, container.container().scrollContentHeight());
    }
  }

  private boolean hasActionAt(
      InterfaceComponentDefinition component,
      float left,
      float top,
      double x,
      double y
  ) {
    if (component == null) {
      return false;
    }
    if (component.componentType() == 0 && component.container().hidden()) {
      return false;
    }
    if (component.componentType() == 0) {
      int scrollPosition = scrollPosition(component);
      int[] childIds = component.container().childIds();
      int[] childX = component.container().childX();
      int[] childY = component.container().childY();
      for (int childIndex = childIds.length - 1; childIndex >= 0; childIndex--) {
        InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
        if (child != null && hasActionAt(child, left + childX[childIndex], top + childY[childIndex] - scrollPosition, x, y)) {
          return true;
        }
      }
    }
    return isActionable(component) && componentBounds(component, left, top).contains(x, y);
  }

  private void drawRectangle(InterfaceComponentDefinition component, float left, float top, int hoveredWidgetId) {
    int rgb = component.id() == hoveredWidgetId && component.colors().hoverColor() != 0
        ? component.colors().hoverColor()
        : component.colors().defaultColor();
    float alpha = component.alpha() == 0 ? 1.0f : Math.max(0.0f, Math.min(1.0f, (256.0f - component.alpha()) / 256.0f));
    glColor4f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0), alpha);
    primitives.drawQuad(left, top, component.width(), component.height());
  }

  private void drawText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      GameplayCombatSidebarModel combatModel,
      int hoveredWidgetId
  ) {
    String text = overrideText(component.id(), combatModel);
    if (text == null || text.isEmpty()) {
      text = component.textBlock().defaultText();
    }
    if (text == null || text.isEmpty()) {
      return;
    }
    TitleScreenBitmapFont font = fontFor(component.textBlock().fontIndex());
    if (font == null) {
      drawFallbackText(component, left, top, text, hoveredWidgetId);
      return;
    }
    int baselineY = Math.round(top + font.maxGlyphHeight());
    int rgb = component.id() == hoveredWidgetId && component.colors().hoverColor() != 0
        ? component.colors().hoverColor()
        : component.colors().defaultColor();
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
    int rgb = component.id() == hoveredWidgetId && component.colors().hoverColor() != 0
        ? component.colors().hoverColor()
        : component.colors().defaultColor();
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

  private int hoveredWidgetId(
      InterfaceComponentDefinition component,
      float left,
      float top,
      ScreenRect clipRect,
      double x,
      double y
  ) {
    if (component == null || component.componentType() != 0 || component.container().hidden()) {
      return -1;
    }
    ScreenRect containerRect = componentBounds(component, left, top);
    ScreenRect containerClipRect = intersectClipRect(clipRect, containerRect);
    if (containerClipRect == null || !containerClipRect.contains(x, y)) {
      return -1;
    }
    int hoveredWidgetId = -1;
    int scrollPosition = scrollPosition(component);
    int[] childIds = component.container().childIds();
    int[] childX = component.container().childX();
    int[] childY = component.container().childY();
    for (int childIndex = 0; childIndex < childIds.length; childIndex++) {
      InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
      if (child == null) {
        continue;
      }
      float childLeft = left + childX[childIndex];
      float childTop = top + childY[childIndex] - scrollPosition;
      if ((child.hoverTargetId() >= 0 || child.colors().hoverColor() != 0)
          && componentBounds(child, childLeft, childTop).contains(x, y)) {
        hoveredWidgetId = child.hoverTargetId() >= 0 ? child.hoverTargetId() : child.id();
      }
      if (child.componentType() == 0 && !child.container().hidden()) {
        int childHoveredWidgetId = hoveredWidgetId(child, childLeft, childTop, containerClipRect, x, y);
        if (childHoveredWidgetId >= 0) {
          hoveredWidgetId = childHoveredWidgetId;
        }
      }
    }
    return hoveredWidgetId;
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

  static int scrollbarThumbHeight(int viewportHeight, int scrollContentHeight) {
    int thumbHeight = ((viewportHeight - 32) * viewportHeight) / scrollContentHeight;
    return Math.max(8, thumbHeight);
  }

  static int scrollbarThumbOffset(int scrollPosition, int viewportHeight, int scrollContentHeight, int thumbHeight) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    return ((viewportHeight - 32 - thumbHeight) * scrollPosition) / (scrollContentHeight - viewportHeight);
  }

  static int trackScrollPosition(
      double mouseY,
      float scrollbarTop,
      int viewportHeight,
      int scrollContentHeight,
      int thumbHeight
  ) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    int trackHeight = viewportHeight - SCROLLBAR_ARROW_HEIGHT * 2 - thumbHeight;
    if (trackHeight <= 0) {
      return 0;
    }
    double trackMouseY = mouseY - scrollbarTop - SCROLLBAR_ARROW_HEIGHT - thumbHeight * 0.5d;
    int scrollPosition = (int) (((scrollContentHeight - viewportHeight) * trackMouseY) / trackHeight);
    return clampScrollPosition(scrollPosition, viewportHeight, scrollContentHeight);
  }

  static int dragScrollPosition(
      double mouseY,
      float containerTop,
      int viewportHeight,
      int scrollContentHeight,
      int thumbHeight,
      double thumbGrabOffsetY
  ) {
    if (scrollContentHeight <= viewportHeight) {
      return 0;
    }
    int trackHeight = viewportHeight - SCROLLBAR_ARROW_HEIGHT * 2 - thumbHeight;
    if (trackHeight <= 0) {
      return 0;
    }
    double thumbTop = mouseY - thumbGrabOffsetY;
    double trackMouseY = thumbTop - containerTop - SCROLLBAR_ARROW_HEIGHT;
    int scrollPosition = (int) (((scrollContentHeight - viewportHeight) * trackMouseY) / trackHeight);
    return clampScrollPosition(scrollPosition, viewportHeight, scrollContentHeight);
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
    fillRect(left, top + SCROLLBAR_ARROW_HEIGHT, SCROLLBAR_WIDTH, viewportHeight - SCROLLBAR_ARROW_HEIGHT * 2.0f, SCROLLBAR_TRACK_RGB);
    int thumbHeight = scrollbarThumbHeight(viewportHeight, scrollContentHeight);
    int thumbOffset = scrollbarThumbOffset(scrollPosition, viewportHeight, scrollContentHeight, thumbHeight);
    float thumbTop = top + SCROLLBAR_ARROW_HEIGHT + thumbOffset;
    fillRect(left, thumbTop, SCROLLBAR_WIDTH, thumbHeight, SCROLLBAR_THUMB_FILL_RGB);
    drawVerticalLine(left, thumbTop, thumbHeight, SCROLLBAR_HIGHLIGHT_RGB);
    drawVerticalLine(left + 1.0f, thumbTop, thumbHeight, SCROLLBAR_HIGHLIGHT_RGB);
    drawHorizontalLine(left, thumbTop, SCROLLBAR_WIDTH, SCROLLBAR_HIGHLIGHT_RGB);
    drawHorizontalLine(left, thumbTop + 1.0f, SCROLLBAR_WIDTH, SCROLLBAR_HIGHLIGHT_RGB);
    drawVerticalLine(left + SCROLLBAR_WIDTH - 1.0f, thumbTop, thumbHeight, SCROLLBAR_DARK_RGB);
    drawVerticalLine(left + SCROLLBAR_WIDTH - 2.0f, thumbTop + 1.0f, thumbHeight - 1.0f, SCROLLBAR_DARK_RGB);
    drawHorizontalLine(left, thumbTop + thumbHeight - 1.0f, SCROLLBAR_WIDTH, SCROLLBAR_DARK_RGB);
    drawHorizontalLine(left + 1.0f, thumbTop + thumbHeight - 2.0f, SCROLLBAR_WIDTH - 1.0f, SCROLLBAR_DARK_RGB);
  }

  private int scrollPosition(InterfaceComponentDefinition container) {
    return clampScrollPosition(
        scrollPositions.getOrDefault(container.id(), 0),
        container.height(),
        container.container().scrollContentHeight()
    );
  }

  private void setScrollPosition(InterfaceComponentDefinition container, int scrollPosition) {
    int clampedScrollPosition = clampScrollPosition(
        scrollPosition,
        container.height(),
        container.container().scrollContentHeight()
    );
    if (clampedScrollPosition == 0) {
      scrollPositions.remove(container.id());
      return;
    }
    scrollPositions.put(container.id(), clampedScrollPosition);
  }

  private ScreenRect intersectClipRect(ScreenRect clipRect, ScreenRect bounds) {
    if (clipRect == null) {
      return bounds;
    }
    float left = Math.max(clipRect.left(), bounds.left());
    float top = Math.max(clipRect.top(), bounds.top());
    float right = Math.min(clipRect.left() + clipRect.width(), bounds.left() + bounds.width());
    float bottom = Math.min(clipRect.top() + clipRect.height(), bounds.top() + bounds.height());
    if (right <= left || bottom <= top) {
      return new ScreenRect(left, top, 0.0f, 0.0f);
    }
    return new ScreenRect(left, top, right - left, bottom - top);
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

  private ScrollTarget findScrollTarget(
      InterfaceComponentDefinition component,
      float left,
      float top,
      ScreenRect clipRect,
      double x,
      double y
  ) {
    if (component == null || component.componentType() != 0 || component.container().hidden()) {
      return null;
    }
    ScreenRect containerRect = componentBounds(component, left, top);
    ScreenRect containerClipRect = intersectClipRect(clipRect, containerRect);
    int scrollPosition = scrollPosition(component);
    int[] childIds = component.container().childIds();
    int[] childX = component.container().childX();
    int[] childY = component.container().childY();
    for (int childIndex = childIds.length - 1; childIndex >= 0; childIndex--) {
      InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
      if (child == null) {
        continue;
      }
      ScrollTarget childTarget = findScrollTarget(
          child,
          left + childX[childIndex],
          top + childY[childIndex] - scrollPosition,
          containerClipRect,
          x,
          y
      );
      if (childTarget != null) {
        return childTarget;
      }
    }
    if (component.container().scrollContentHeight() <= component.height()) {
      return null;
    }
    ScreenRect scrollbarRect = scrollbarRect(component, left, top);
    if (scrollbarRect.contains(x, y)) {
      return new ScrollTarget(component, left, top, true);
    }
    if (containerClipRect != null && containerClipRect.contains(x, y)) {
      return new ScrollTarget(component, left, top, false);
    }
    return null;
  }

  private ScrollTarget findScrollTargetByContainerId(
      InterfaceComponentDefinition component,
      float left,
      float top,
      ScreenRect clipRect,
      int containerId
  ) {
    if (component == null || component.componentType() != 0 || component.container().hidden()) {
      return null;
    }
    if (component.id() == containerId) {
      return new ScrollTarget(component, left, top, false);
    }
    ScreenRect containerRect = componentBounds(component, left, top);
    ScreenRect containerClipRect = intersectClipRect(clipRect, containerRect);
    int scrollPosition = scrollPosition(component);
    int[] childIds = component.container().childIds();
    int[] childX = component.container().childX();
    int[] childY = component.container().childY();
    for (int childIndex = 0; childIndex < childIds.length; childIndex++) {
      InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
      if (child == null) {
        continue;
      }
      ScrollTarget childTarget = findScrollTargetByContainerId(
          child,
          left + childX[childIndex],
          top + childY[childIndex] - scrollPosition,
          containerClipRect,
          containerId
      );
      if (childTarget != null) {
        return childTarget;
      }
    }
    return null;
  }

  private ScreenRect scrollbarRect(InterfaceComponentDefinition component, float left, float top) {
    return new ScreenRect(left + component.width(), top, SCROLLBAR_WIDTH, component.height());
  }

  private ScreenRect topArrowRect(ScreenRect scrollbarRect) {
    return new ScreenRect(scrollbarRect.left(), scrollbarRect.top(), SCROLLBAR_WIDTH, SCROLLBAR_ARROW_HEIGHT);
  }

  private ScreenRect bottomArrowRect(ScreenRect scrollbarRect) {
    return new ScreenRect(
        scrollbarRect.left(),
        scrollbarRect.top() + scrollbarRect.height() - SCROLLBAR_ARROW_HEIGHT,
        SCROLLBAR_WIDTH,
        SCROLLBAR_ARROW_HEIGHT
    );
  }

  private ScreenRect scrollbarThumbRect(
      InterfaceComponentDefinition component,
      float left,
      float top,
      int scrollPosition
  ) {
    ScreenRect scrollbarRect = scrollbarRect(component, left, top);
    int thumbHeight = scrollbarThumbHeight(component.height(), component.container().scrollContentHeight());
    int thumbOffset = scrollbarThumbOffset(
        scrollPosition,
        component.height(),
        component.container().scrollContentHeight(),
        thumbHeight
    );
    return new ScreenRect(
        scrollbarRect.left(),
        scrollbarRect.top() + SCROLLBAR_ARROW_HEIGHT + thumbOffset,
        SCROLLBAR_WIDTH,
        thumbHeight
    );
  }

  private static int clampScrollPosition(int scrollPosition, int viewportHeight, int scrollContentHeight) {
    int maxScrollPosition = Math.max(0, scrollContentHeight - viewportHeight);
    return Math.max(0, Math.min(maxScrollPosition, scrollPosition));
  }

  private boolean isActionable(InterfaceComponentDefinition component) {
    return component.optionType() != 0 || !component.actionLabel().isEmpty();
  }

  private ScreenRect componentBounds(InterfaceComponentDefinition component, float left, float top) {
    return new ScreenRect(left, top, Math.max(0.0f, component.width()), Math.max(0.0f, component.height()));
  }

  private record SpriteKey(String entryName, int frameIndex) {
  }

  private record TextKey(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
  }

  private record BitmapTextTexture(OpenGlTexture texture, int drawOffsetX, int drawOffsetY) {
  }

  private record ScrollTarget(InterfaceComponentDefinition container, float left, float top, boolean overScrollbar) {
  }

  private record ScrollbarDragState(int containerId, double thumbGrabOffsetY) {
  }

  record TextTextureLayout(int canvasLeft, int canvasTop, int width, int height, int baselineY) {
  }
}
