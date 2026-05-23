package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.content.InterfaceComponentDefinition;
import java.util.HashMap;
import java.util.Map;

final class WidgetTextureCache implements AutoCloseable {

  private final ArchiveSpriteResolver mediaSpriteResolver;
  private final ItemIconRenderer itemIconRenderer;
  private final Map<SpriteKey, OpenGlTexture> spriteTextures = new HashMap<>();
  private final Map<TextKey, SidebarWidgetRenderer.BitmapTextTexture> textTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> itemTextures = new HashMap<>();
  private final Map<Integer, OpenGlTexture> weaponPreviewTextures = new HashMap<>();

  WidgetTextureCache(ArchiveSpriteResolver mediaSpriteResolver, ItemIconRenderer itemIconRenderer) {
    this.mediaSpriteResolver = mediaSpriteResolver;
    this.itemIconRenderer = itemIconRenderer;
  }

  boolean hasSpriteResolver() {
    return mediaSpriteResolver != null;
  }

  OpenGlTexture spriteTexture(InterfaceComponentDefinition.SpriteReference spriteReference) {
    if (spriteReference == null) {
      return null;
    }
    return spriteTexture(spriteReference.entryName(), spriteReference.frameIndex());
  }

  OpenGlTexture spriteTexture(String entryName, int frameIndex) {
    if (mediaSpriteResolver == null) {
      return null;
    }
    SpriteKey key = new SpriteKey(entryName, frameIndex);
    if (spriteTextures.containsKey(key)) {
      return spriteTextures.get(key);
    }
    ArgbImage image = mediaSpriteResolver.resolve(entryName, frameIndex);
    OpenGlTexture texture = image == null ? null : OpenGlTexture.fromArgbImage(image);
    spriteTextures.put(key, texture);
    return texture;
  }

  SidebarWidgetRenderer.BitmapTextTexture bitmapTextTexture(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
    if (font == null || text == null || text.isEmpty()) {
      return null;
    }
    return textTextures.computeIfAbsent(
        new TextKey(font, text, rgb, shadow),
        key -> {
          SidebarWidgetRenderer.TextTextureLayout layout =
              WidgetTextRenderer.textTextureLayout(key.font(), key.text(), key.shadow());
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
          return texture == null ? null : new SidebarWidgetRenderer.BitmapTextTexture(
              texture,
              layout.canvasLeft(),
              layout.canvasTop()
          );
        }
    );
  }

  OpenGlTexture weaponPreviewTexture(int itemId, int widgetZoomScale) {
    if (itemIconRenderer == null || itemId < 0) {
      return null;
    }
    if (weaponPreviewTextures.containsKey(itemId)) {
      return weaponPreviewTextures.get(itemId);
    }
    ArgbImage image = itemIconRenderer.renderWidgetPreview(itemId, widgetZoomScale);
    OpenGlTexture texture = image == null ? null : OpenGlTexture.fromArgbImage(image);
    weaponPreviewTextures.put(itemId, texture);
    return texture;
  }

  OpenGlTexture itemTexture(int itemId) {
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

  @Override
  public void close() {
    for (OpenGlTexture texture : spriteTextures.values()) {
      closeTexture(texture);
    }
    spriteTextures.clear();
    for (SidebarWidgetRenderer.BitmapTextTexture texture : textTextures.values()) {
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

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private record SpriteKey(String entryName, int frameIndex) {
  }

  private record TextKey(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
  }
}
