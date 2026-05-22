package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;

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
import java.util.HashMap;
import java.util.Map;

final class CombatSidebarWidgetRenderer implements AutoCloseable {

  private static final int SEND_WEAPON_WIDGET_ZOOM_SCALE = 200;

  private final InterfaceComponentCatalog interfaceComponents;
  private final ArchiveSpriteResolver mediaSpriteResolver;
  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenFonts fonts;
  private final ImmediateModeRenderer2d primitives;
  private final Map<SpriteKey, OpenGlTexture> spriteTextures = new HashMap<>();
  private final Map<TextKey, BitmapTextTexture> textTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> itemTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> weaponPreviewTextures = new HashMap<>();

  CombatSidebarWidgetRenderer(
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

  boolean canRender(GameplayCombatSidebarModel combatModel) {
    return interfaceComponents != null
        && mediaSpriteResolver != null
        && combatModel != null
        && interfaceComponents.getOrNull(combatModel.interfaceId()) != null;
  }

  void draw(ScreenRect sidebarRect, GameplayCombatSidebarModel combatModel) {
    InterfaceComponentDefinition root = interfaceComponents.require(combatModel.interfaceId());
    renderComponent(root, sidebarRect.left(), sidebarRect.top(), combatModel);
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
      GameplayCombatSidebarModel combatModel
  ) {
    if (component == null) {
      return;
    }
    if (component.componentType() == 0 && component.container().hidden()) {
      return;
    }
    switch (component.componentType()) {
      case 0 -> renderContainer(component, left, top, combatModel);
      case 3 -> drawRectangle(component, left, top);
      case 4 -> drawText(component, left, top, combatModel);
      case 5 -> drawSprite(component, left, top);
      case 6 -> drawWeaponModel(component, left, top, combatModel);
      default -> {
        // Combat interfaces currently only need container, rectangle, text, sprite, and model
        // previews.
      }
    }
  }

  private void renderContainer(
      InterfaceComponentDefinition container,
      float left,
      float top,
      GameplayCombatSidebarModel combatModel
  ) {
    int[] childIds = container.container().childIds();
    int[] childX = container.container().childX();
    int[] childY = container.container().childY();
    for (int childIndex = 0; childIndex < childIds.length; childIndex++) {
      InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
      if (child == null) {
        continue;
      }
      renderComponent(child, left + childX[childIndex], top + childY[childIndex], combatModel);
    }
  }

  private void drawRectangle(InterfaceComponentDefinition component, float left, float top) {
    int rgb = component.colors().defaultColor();
    float alpha = component.alpha() == 0 ? 1.0f : Math.max(0.0f, Math.min(1.0f, (256.0f - component.alpha()) / 256.0f));
    glColor4f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0), alpha);
    primitives.drawQuad(left, top, component.width(), component.height());
  }

  private void drawText(
      InterfaceComponentDefinition component,
      float left,
      float top,
      GameplayCombatSidebarModel combatModel
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
      drawFallbackText(component, left, top, text);
      return;
    }
    int baselineY = Math.round(top + font.maxGlyphHeight());
    int rgb = component.colors().defaultColor();
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
                  lineY - font.maxGlyphHeight(),
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
      String text
  ) {
    int rgb = component.colors().defaultColor();
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

  private void drawWeaponModel(
      InterfaceComponentDefinition component,
      float left,
      float top,
      GameplayCombatSidebarModel combatModel
  ) {
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
          int visibleLeft = key.font().visibleLeftOffset(key.text());
          int visibleWidth = key.font().measureVisibleWidth(key.text());
          int advanceWidth = key.font().measureText(key.text());
          int visibleRight = visibleLeft + visibleWidth;
          int canvasLeft = Math.min(0, visibleLeft);
          int canvasRight = Math.max(advanceWidth, visibleRight);
          int shadowPadding = key.shadow() ? 1 : 0;
          int width = Math.max(1, canvasRight - canvasLeft + shadowPadding);
          int height = Math.max(1, key.font().maxGlyphHeight() + (key.shadow() ? 1 : 0));
          int[] pixels = new int[width * height];
          key.font().drawText(
              pixels,
              width,
              height,
              key.text(),
              -canvasLeft,
              key.font().maxGlyphHeight(),
              key.rgb(),
              key.shadow()
          );
          OpenGlTexture texture = OpenGlTexture.fromArgbImage(new ArgbImage(width, height, pixels));
          return texture == null ? null : new BitmapTextTexture(texture, canvasLeft);
        }
    );
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

  private record SpriteKey(String entryName, int frameIndex) {
  }

  private record TextKey(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
  }

  private record BitmapTextTexture(OpenGlTexture texture, int drawOffsetX) {
  }
}
