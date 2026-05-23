package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import java.util.LinkedHashMap;
import java.util.Map;

final class SidebarTextureCache implements AutoCloseable {

  private static final int ITEM_ICON_CACHE_CAPACITY = 512;
  private static final int INVENTORY_AMOUNT_CACHE_CAPACITY = 128;
  private static final int BITMAP_TEXT_CACHE_CAPACITY = 256;

  private final ItemIconRenderer itemIconRenderer;
  private final TitleScreenBitmapFont inventoryAmountFont;
  private final Map<Integer, OpenGlTexture> itemIconTextures = newLruCache(ITEM_ICON_CACHE_CAPACITY);
  // These strings can vary at runtime, so keep the caches bounded instead of growing forever.
  private final Map<String, OpenGlTexture> inventoryAmountTextures = newLruCache(INVENTORY_AMOUNT_CACHE_CAPACITY);
  private final Map<BitmapTextKey, OpenGlTexture> bitmapTextTextures = newLruCache(BITMAP_TEXT_CACHE_CAPACITY);

  SidebarTextureCache(ItemIconRenderer itemIconRenderer, TitleScreenBitmapFont inventoryAmountFont) {
    this.itemIconRenderer = itemIconRenderer;
    this.inventoryAmountFont = inventoryAmountFont;
  }

  boolean canRenderItemIcons() {
    return itemIconRenderer != null;
  }

  TitleScreenBitmapFont inventoryAmountFont() {
    return inventoryAmountFont;
  }

  OpenGlTexture itemIconTexture(int itemId, int quantity) {
    if (itemIconRenderer == null) {
      return null;
    }
    int iconKey = itemIconRenderer.iconKey(itemId, quantity);
    if (iconKey < 0) {
      return null;
    }
    if (itemIconTextures.containsKey(iconKey)) {
      return itemIconTextures.get(iconKey);
    }
    ArgbImage iconImage = itemIconRenderer.render(itemId, quantity);
    OpenGlTexture createdTexture = iconImage == null ? null : OpenGlTexture.fromArgbImage(iconImage);
    itemIconTextures.put(iconKey, createdTexture);
    return createdTexture;
  }

  OpenGlTexture inventoryAmountTexture(String text) {
    if (inventoryAmountFont == null || text == null || text.isEmpty()) {
      return null;
    }
    return inventoryAmountTextures.computeIfAbsent(text, this::createInventoryAmountTexture);
  }

  OpenGlTexture bitmapTextTexture(TitleScreenBitmapFont font, String text, int rgb, boolean shadow) {
    if (font == null || text == null || text.isEmpty()) {
      return null;
    }
    return bitmapTextTextures.computeIfAbsent(
        new BitmapTextKey(font, text, rgb, shadow),
        this::createBitmapTextTexture
    );
  }

  @Override
  public void close() {
    closeTextures(itemIconTextures);
    closeTextures(inventoryAmountTextures);
    closeTextures(bitmapTextTextures);
  }

  private OpenGlTexture createInventoryAmountTexture(String text) {
    int width = Math.max(1, inventoryAmountFont.measureText(text) + 1);
    int height = Math.max(1, inventoryAmountFont.maxGlyphHeight() + 1);
    int[] pixels = new int[width * height];
    inventoryAmountFont.drawText(
        pixels,
        width,
        height,
        text,
        0,
        inventoryAmountFont.maxGlyphHeight(),
        InventoryEquipmentSidebarPanelRenderer.INVENTORY_STACK_TEXT_RGB,
        true
    );
    return OpenGlTexture.fromArgbImage(new ArgbImage(width, height, pixels));
  }

  private OpenGlTexture createBitmapTextTexture(BitmapTextKey key) {
    SidebarWidgetRenderer.TextTextureLayout layout =
        SidebarWidgetRenderer.textTextureLayout(key.font(), key.text(), key.shadow());
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
    return OpenGlTexture.fromArgbImage(new ArgbImage(layout.width(), layout.height(), pixels));
  }

  private static <K> Map<K, OpenGlTexture> newLruCache(int capacity) {
    return new LinkedHashMap<>(capacity, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<K, OpenGlTexture> eldest) {
        if (size() <= capacity) {
          return false;
        }
        closeTexture(eldest.getValue());
        return true;
      }
    };
  }

  private static void closeTextures(Map<?, OpenGlTexture> textures) {
    for (OpenGlTexture texture : textures.values()) {
      closeTexture(texture);
    }
    textures.clear();
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }

  private record BitmapTextKey(
      TitleScreenBitmapFont font,
      String text,
      int rgb,
      boolean shadow
  ) {
  }
}
