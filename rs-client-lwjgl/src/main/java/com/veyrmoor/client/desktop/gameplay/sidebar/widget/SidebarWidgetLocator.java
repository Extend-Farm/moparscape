package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import com.veyrmoor.content.InterfaceComponentCatalog;
import com.veyrmoor.content.InterfaceComponentDefinition;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class SidebarWidgetLocator {

  private final InterfaceComponentCatalog interfaceComponents;
  private final SidebarWidgetScrollState scrollState;

  SidebarWidgetLocator(InterfaceComponentCatalog interfaceComponents, SidebarWidgetScrollState scrollState) {
    this.interfaceComponents = interfaceComponents;
    this.scrollState = scrollState;
  }

  int hoveredWidgetId(
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
    int scrollPosition = scrollState.scrollPosition(component);
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
      if (child.componentType() != 0 || child.container().hidden()) {
        continue;
      }
      int childHoveredWidgetId = hoveredWidgetId(child, childLeft, childTop, containerClipRect, x, y);
      if (childHoveredWidgetId >= 0) {
        hoveredWidgetId = childHoveredWidgetId;
      }
    }
    return hoveredWidgetId;
  }

  boolean hasActionAt(
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
      int scrollPosition = scrollState.scrollPosition(component);
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

  int actionWidgetIdAt(
      InterfaceComponentDefinition component,
      float left,
      float top,
      ScreenRect clipRect,
      double x,
      double y
  ) {
    if (component == null) {
      return -1;
    }
    if (component.componentType() == 0 && component.container().hidden()) {
      return -1;
    }
    ScreenRect componentRect = componentBounds(component, left, top);
    ScreenRect effectiveClipRect = component.componentType() == 0
        ? intersectClipRect(clipRect, componentRect)
        : clipRect == null ? componentRect : intersectClipRect(clipRect, componentRect);
    if (effectiveClipRect != null && effectiveClipRect.width() > 0.0f && effectiveClipRect.height() > 0.0f) {
      if (component.componentType() == 0) {
        int scrollPosition = scrollState.scrollPosition(component);
        int[] childIds = component.container().childIds();
        int[] childX = component.container().childX();
        int[] childY = component.container().childY();
        for (int childIndex = childIds.length - 1; childIndex >= 0; childIndex--) {
          InterfaceComponentDefinition child = interfaceComponents.getOrNull(childIds[childIndex]);
          if (child == null) {
            continue;
          }
          int childActionWidgetId = actionWidgetIdAt(
              child,
              left + childX[childIndex],
              top + childY[childIndex] - scrollPosition,
              effectiveClipRect,
              x,
              y
          );
          if (childActionWidgetId >= 0) {
            return childActionWidgetId;
          }
        }
      }
      if (isActionable(component) && effectiveClipRect.contains(x, y)) {
        return component.id();
      }
    }
    return -1;
  }

  ScrollTarget findScrollTarget(
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
    int scrollPosition = scrollState.scrollPosition(component);
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
    ScreenRect scrollbarRect = SidebarWidgetScrollState.scrollbarRect(component, left, top);
    if (scrollbarRect.contains(x, y)) {
      return new ScrollTarget(component, left, top, true);
    }
    if (containerClipRect != null && containerClipRect.contains(x, y)) {
      return new ScrollTarget(component, left, top, false);
    }
    return null;
  }

  ScrollTarget findScrollTargetByContainerId(
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
    int scrollPosition = scrollState.scrollPosition(component);
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

  static ScreenRect intersectClipRect(ScreenRect clipRect, ScreenRect bounds) {
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

  static ScreenRect componentBounds(InterfaceComponentDefinition component, float left, float top) {
    if (component.componentType() == 2 || component.componentType() == 7) {
      return new ScreenRect(
          left,
          top,
          WidgetInventoryGridRenderer.measuredWidth(component),
          WidgetInventoryGridRenderer.measuredHeight(component)
      );
    }
    return new ScreenRect(left, top, Math.max(0.0f, component.width()), Math.max(0.0f, component.height()));
  }

  private boolean isActionable(InterfaceComponentDefinition component) {
    return component.optionType() != 0 || !component.actionLabel().isEmpty();
  }

  record ScrollTarget(InterfaceComponentDefinition container, float left, float top, boolean overScrollbar) {
  }
}
