package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.gameplay.sidebar.GameplayCombatSidebarModel;
import com.veyrmoor.client.desktop.gameplay.sidebar.GameplayStatsSidebarModel;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.TitleScreenBitmapFont;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;

public final class SidebarWidgetRenderer implements AutoCloseable {

  private final InterfaceComponentCatalog interfaceComponents;
  private final SidebarWidgetScrollState scrollState;
  private final SidebarWidgetLocator locator;
  private final WidgetTextureCache textureCache;
  private final WidgetTextRenderer textRenderer;
  private final WidgetSpriteRenderer spriteRenderer;
  private final WidgetInventoryGridRenderer inventoryGridRenderer;
  private final WidgetScrollbarRenderer scrollbarRenderer;
  private final WidgetClipStack clipStack;
  private final WidgetTreeRenderer treeRenderer;

  public SidebarWidgetRenderer(
      InterfaceComponentCatalog interfaceComponents,
      com.veyrmoor.client.desktop.assets.sprite.ArchiveSpriteResolver mediaSpriteResolver,
      ItemIconRenderer itemIconRenderer,
      TitleScreenFonts fonts,
      ImmediateModeRenderer2d primitives
  ) {
    this.interfaceComponents = interfaceComponents;
    this.scrollState = new SidebarWidgetScrollState();
    this.locator = new SidebarWidgetLocator(interfaceComponents, scrollState);
    this.textureCache = new WidgetTextureCache(mediaSpriteResolver, itemIconRenderer);
    this.textRenderer = new WidgetTextRenderer(fonts, primitives, textureCache);
    this.spriteRenderer = new WidgetSpriteRenderer(primitives, textureCache);
    this.inventoryGridRenderer = new WidgetInventoryGridRenderer(primitives, textureCache);
    this.scrollbarRenderer = new WidgetScrollbarRenderer(primitives, textureCache);
    this.clipStack = new WidgetClipStack();
    this.treeRenderer = new WidgetTreeRenderer(
        interfaceComponents,
        scrollState,
        primitives,
        textRenderer,
        spriteRenderer,
        inventoryGridRenderer,
        scrollbarRenderer,
        clipStack
    );
  }

  public boolean canRender(int interfaceId) {
    return interfaceComponents != null
        && textureCache.hasSpriteResolver()
        && interfaceComponents.getOrNull(interfaceId) != null;
  }

  public boolean canRender(GameplayCombatSidebarModel combatModel) {
    return combatModel != null && canRender(combatModel.interfaceId());
  }

  public void draw(ScreenRect sidebarRect, int interfaceId, ClientViewModel viewModel) {
    draw(
        sidebarRect,
        interfaceId,
        viewModel,
        WidgetOverrideResolver.NONE,
        WidgetInventoryGridResolver.NONE,
        Double.NaN,
        Double.NaN
    );
  }

  public void draw(ScreenRect sidebarRect, int interfaceId, ClientViewModel viewModel, double pointerX, double pointerY) {
    draw(
        sidebarRect,
        interfaceId,
        viewModel,
        WidgetOverrideResolver.NONE,
        WidgetInventoryGridResolver.NONE,
        pointerX,
        pointerY
    );
  }

  public void draw(
      ScreenRect sidebarRect,
      int interfaceId,
      ClientViewModel viewModel,
      WidgetOverrideResolver overrideResolver,
      WidgetInventoryGridResolver gridResolver
  ) {
    draw(sidebarRect, interfaceId, viewModel, overrideResolver, gridResolver, Double.NaN, Double.NaN);
  }

  public void draw(
      ScreenRect sidebarRect,
      int interfaceId,
      ClientViewModel viewModel,
      WidgetOverrideResolver overrideResolver,
      WidgetInventoryGridResolver gridResolver,
      double pointerX,
      double pointerY
  ) {
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    int hoveredWidgetId = locator.hoveredWidgetId(root, sidebarRect.left(), sidebarRect.top(), null, pointerX, pointerY);
    treeRenderer.render(
        root,
        sidebarRect.left(),
        sidebarRect.top(),
        new WidgetRenderContext(
            viewModel,
            null,
            viewModel == null ? null : GameplayStatsSidebarModel.from(viewModel),
            hoveredWidgetId,
            overrideResolver == null ? WidgetOverrideResolver.NONE : overrideResolver,
            gridResolver == null ? WidgetInventoryGridResolver.NONE : gridResolver
        )
    );
  }

  public void draw(ScreenRect sidebarRect, GameplayCombatSidebarModel combatModel, ClientViewModel viewModel) {
    InterfaceComponentDefinition root = interfaceComponents.require(combatModel.interfaceId());
    treeRenderer.render(
        root,
        sidebarRect.left(),
        sidebarRect.top(),
        new WidgetRenderContext(
            viewModel,
            combatModel,
            viewModel == null ? null : GameplayStatsSidebarModel.from(viewModel),
            -1,
            WidgetOverrideResolver.NONE,
            WidgetInventoryGridResolver.NONE
        )
    );
  }

  public boolean handleScrollbarClick(int interfaceId, ScreenRect sidebarRect, double x, double y) {
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

  public boolean handleScroll(int interfaceId, ScreenRect sidebarRect, double x, double y, double yOffset) {
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

  public boolean handlePointerMove(int interfaceId, ScreenRect sidebarRect, double x, double y) {
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

  public void endPointerDrag() {
    scrollState.endPointerDrag();
  }

  public void clearTransientState() {
    scrollState.clearTransientState();
  }

  public boolean hasActionAt(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return false;
    }
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    return locator.hasActionAt(root, sidebarRect.left(), sidebarRect.top(), x, y);
  }

  public int actionWidgetIdAt(int interfaceId, ScreenRect sidebarRect, double x, double y) {
    if (!canRender(interfaceId) || !sidebarRect.contains(x, y)) {
      return -1;
    }
    InterfaceComponentDefinition root = interfaceComponents.require(interfaceId);
    return locator.actionWidgetIdAt(root, sidebarRect.left(), sidebarRect.top(), null, x, y);
  }

  @Override
  public void close() {
    clipStack.clear();
    textureCache.close();
  }

  public static TextTextureLayout textTextureLayout(TitleScreenBitmapFont font, String text, boolean shadow) {
    return WidgetTextRenderer.textTextureLayout(font, text, shadow);
  }

  public interface WidgetOverrideResolver {

    WidgetOverrideResolver NONE = componentId -> null;

    WidgetOverride resolve(int componentId);
  }

  public record WidgetOverride(String text, Integer defaultColor) {
  }

  public interface WidgetInventoryGridResolver {

    WidgetInventoryGridResolver NONE = (componentId, slotIndex) -> null;

    WidgetGridItem itemAt(int componentId, int slotIndex);
  }

  public record WidgetGridItem(int itemId, int quantity) {
  }

  record BitmapTextTexture(com.veyrmoor.client.desktop.render.common.OpenGlTexture texture, int drawOffsetX, int drawOffsetY) {
  }

  record WidgetRenderContext(
      ClientViewModel viewModel,
      GameplayCombatSidebarModel combatModel,
      GameplayStatsSidebarModel statsModel,
      int hoveredWidgetId,
      WidgetOverrideResolver overrideResolver,
      WidgetInventoryGridResolver gridResolver
  ) {
  }

  public record TextTextureLayout(int canvasLeft, int canvasTop, int width, int height, int baselineY) {
  }
}
