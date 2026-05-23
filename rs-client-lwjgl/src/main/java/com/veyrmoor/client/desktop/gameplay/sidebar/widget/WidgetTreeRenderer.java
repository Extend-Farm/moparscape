package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.lwjgl.opengl.GL11.glColor4f;

import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;

final class WidgetTreeRenderer {

  private final InterfaceComponentCatalog interfaceComponents;
  private final SidebarWidgetScrollState scrollState;
  private final ImmediateModeRenderer2d primitives;
  private final WidgetTextRenderer textRenderer;
  private final WidgetSpriteRenderer spriteRenderer;
  private final WidgetInventoryGridRenderer inventoryGridRenderer;
  private final WidgetScrollbarRenderer scrollbarRenderer;
  private final WidgetClipStack clipStack;

  WidgetTreeRenderer(
      InterfaceComponentCatalog interfaceComponents,
      SidebarWidgetScrollState scrollState,
      ImmediateModeRenderer2d primitives,
      WidgetTextRenderer textRenderer,
      WidgetSpriteRenderer spriteRenderer,
      WidgetInventoryGridRenderer inventoryGridRenderer,
      WidgetScrollbarRenderer scrollbarRenderer,
      WidgetClipStack clipStack
  ) {
    this.interfaceComponents = interfaceComponents;
    this.scrollState = scrollState;
    this.primitives = primitives;
    this.textRenderer = textRenderer;
    this.spriteRenderer = spriteRenderer;
    this.inventoryGridRenderer = inventoryGridRenderer;
    this.scrollbarRenderer = scrollbarRenderer;
    this.clipStack = clipStack;
  }

  void render(
      InterfaceComponentDefinition root,
      float left,
      float top,
      SidebarWidgetRenderer.WidgetRenderContext context
  ) {
    renderComponent(root, left, top, context, null);
  }

  private void renderComponent(
      InterfaceComponentDefinition component,
      float left,
      float top,
      SidebarWidgetRenderer.WidgetRenderContext context,
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
      case 2 -> inventoryGridRenderer.drawInventoryGrid(component, left, top, context);
      case 3 -> drawRectangle(component, left, top, context.hoveredWidgetId());
      case 4 -> textRenderer.drawText(component, left, top, context);
      case 5 -> spriteRenderer.drawSprite(component, left, top);
      case 6 -> spriteRenderer.drawModelPreview(component, left, top, context.combatModel());
      default -> {
        // Native widget rendering currently only needs containers, rectangles, text, sprites,
        // model previews, and inventory grids for the sidebar/report-abuse surfaces we decode
        // from cache.
      }
    }
  }

  private void renderContainer(
      InterfaceComponentDefinition container,
      float left,
      float top,
      SidebarWidgetRenderer.WidgetRenderContext context,
      ScreenRect clipRect
  ) {
    int scrollPosition = scrollState.scrollPosition(container);
    ScreenRect containerRect = SidebarWidgetLocator.componentBounds(container, left, top);
    ScreenRect containerClipRect = SidebarWidgetLocator.intersectClipRect(clipRect, containerRect);
    clipStack.push(containerClipRect);
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
    clipStack.pop();
    if (container.container().scrollContentHeight() > container.height()) {
      scrollbarRenderer.drawScrollbar(
          left + container.width(),
          top,
          container.height(),
          scrollPosition,
          container.container().scrollContentHeight()
      );
    }
  }

  private void drawRectangle(InterfaceComponentDefinition component, float left, float top, int hoveredWidgetId) {
    int rgb = textRenderer.componentRgb(component, hoveredWidgetId, SidebarWidgetRenderer.WidgetOverrideResolver.NONE);
    float alpha = component.alpha() == 0 ? 1.0f : Math.max(0.0f, Math.min(1.0f, (256.0f - component.alpha()) / 256.0f));
    glColor4f(rgbUnit(rgb, 16), rgbUnit(rgb, 8), rgbUnit(rgb, 0), alpha);
    primitives.drawQuad(left, top, component.width(), component.height());
  }

  private static float rgbUnit(int rgb, int shift) {
    return ((rgb >>> shift) & 0xff) / 255.0f;
  }
}
