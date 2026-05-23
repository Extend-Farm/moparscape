package com.veyrmoor.client.desktop.gameplay.sidebar;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.ReportAbuseController;
import com.veyrmoor.client.desktop.gameplay.sidebar.widget.SidebarWidgetRenderer;
import com.veyrmoor.client.desktop.render.common.ScreenRect;

final class ReportAbuseModalRenderer {

  private final ReportAbuseController reportAbuseController;
  private final SidebarWidgetRenderer sidebarWidgetRenderer;

  ReportAbuseModalRenderer(
      ReportAbuseController reportAbuseController,
      SidebarWidgetRenderer sidebarWidgetRenderer
  ) {
    this.reportAbuseController = reportAbuseController;
    this.sidebarWidgetRenderer = sidebarWidgetRenderer;
  }

  boolean canDraw() {
    return reportAbuseController != null
        && reportAbuseController.isAvailable()
        && sidebarWidgetRenderer != null
        && sidebarWidgetRenderer.canRender(reportAbuseController.interfaceId());
  }

  boolean contains(double x, double y) {
    return canDraw() && modalRect().contains(x, y);
  }

  int actionWidgetIdAt(double x, double y) {
    if (!canDraw()) {
      return -1;
    }
    return sidebarWidgetRenderer.actionWidgetIdAt(reportAbuseController.interfaceId(), modalRect(), x, y);
  }

  void draw(ClientViewModel viewModel, double pointerX, double pointerY) {
    if (!canDraw()) {
      return;
    }
    // The reference client mutates these component strings live instead of rebuilding the
    // interface tree, so the native widget renderer needs the same narrow runtime override hook.
    sidebarWidgetRenderer.draw(
        modalRect(),
        reportAbuseController.interfaceId(),
        viewModel,
        componentId -> {
          if (componentId == reportAbuseController.nameFieldComponentId()) {
            return new SidebarWidgetRenderer.WidgetOverride(reportAbuseController.nameFieldText(), null);
          }
          if (componentId == reportAbuseController.muteToggleComponentId()) {
            return new SidebarWidgetRenderer.WidgetOverride(
                reportAbuseController.muteToggleText(viewModel),
                reportAbuseController.muteToggleRgb()
            );
          }
          return null;
        },
        SidebarWidgetRenderer.WidgetInventoryGridResolver.NONE,
        pointerX,
        pointerY
    );
  }

  static ScreenRect centeredModalRect(ScreenRect worldViewportRect, float width, float height) {
    return new ScreenRect(
        worldViewportRect.left() + (worldViewportRect.width() - width) * 0.5f,
        worldViewportRect.top() + (worldViewportRect.height() - height) * 0.5f,
        width,
        height
    );
  }

  private ScreenRect modalRect() {
    return centeredModalRect(
        GameplayLayout.worldViewportInnerRect(),
        reportAbuseController.interfaceWidth(),
        reportAbuseController.interfaceHeight()
    );
  }
}
