package io.github.ffakira.moparscape.client;

final class WidgetTreeRenderer {

  private WidgetTreeRenderer() {
  }

  // Exact extraction of legacy method105 widget tree traversal/orchestration.
  static void renderWidgetTree(
    GameClientCore gameClient,
    int scrollY,
    int baseX,
    Widget parentWidget,
    int baseY,
    int gameTickDelta,
    int dragState,
    int dragSlot,
    int dragWidgetId,
    int dragStartMouseX,
    int dragStartMouseY,
    int dragThreshold,
    boolean dragAlphaEnabled,
    int selectedDragState,
    int selectedDragSlot,
    int selectedDragWidgetId,
    int itemUseState,
    int itemUseSlot,
    int itemUseWidgetId,
    int mouseX,
    int mouseY,
    int dragOffsetY,
    int focusedWidgetIdPrimary,
    int focusedWidgetIdSecondary,
    int focusedWidgetIdTertiary,
    int textColorSeed,
    boolean isDialogueOpen,
    FontRenderer itemAmountFont
  ) {
    int clipLeft = Rasterizer2D.anInt1383;
    int clipTop = Rasterizer2D.anInt1381;
    int clipRight = Rasterizer2D.anInt1384;
    int clipBottom = Rasterizer2D.anInt1382;
    Rasterizer2D.method333(baseY + parentWidget.anInt267, baseX, false, baseX + parentWidget.anInt220, baseY);
    int childCount = parentWidget.anIntArray240.length;
    for(int childIndex = 0; childIndex < childCount; childIndex++)
    {
      int childX = parentWidget.anIntArray241[childIndex] + baseX;
      int childY = (parentWidget.anIntArray272[childIndex] + baseY) - scrollY;
      Widget childWidget = Widget.aClass9Array210[parentWidget.anIntArray240[childIndex]];
      childX += childWidget.anInt263;
      childY += childWidget.anInt265;
      if(childWidget.anInt262 == 0)
      {
        WidgetContainerHandler.renderType0Container(gameClient, childWidget, childX, childY);
      } else
      if(childWidget.anInt262 != 1)
        if(childWidget.anInt262 == 2)
        {
          dragOffsetY = WidgetRenderHandler.renderType2InventoryGrid(
            parentWidget,
            childWidget,
            childX,
            childY,
            gameTickDelta,
            dragState,
            dragSlot,
            dragWidgetId,
            dragStartMouseX,
            dragStartMouseY,
            dragThreshold,
            dragAlphaEnabled,
            selectedDragState,
            selectedDragSlot,
            selectedDragWidgetId,
            itemUseState,
            itemUseSlot,
            itemUseWidgetId,
            mouseX,
            mouseY,
            dragOffsetY,
            itemAmountFont
          );
        } else
        if(childWidget.anInt262 == 3)
        {
          boolean isFocusedWidget = false;
          if(focusedWidgetIdPrimary == childWidget.anInt250 || focusedWidgetIdSecondary == childWidget.anInt250 || focusedWidgetIdTertiary == childWidget.anInt250)
            isFocusedWidget = true;
          WidgetRenderHandler.renderType3Rectangle(childWidget, childX, childY, isFocusedWidget, gameClient.method131(childWidget, false));
        } else
        if(childWidget.anInt262 == 4)
        {
          boolean isFocusedWidget = false;
          if(focusedWidgetIdPrimary == childWidget.anInt250 || focusedWidgetIdSecondary == childWidget.anInt250 || focusedWidgetIdTertiary == childWidget.anInt250)
            isFocusedWidget = true;
          WidgetRenderHandler.renderType4Text(gameClient, childWidget, childX, childY, textColorSeed, isFocusedWidget, gameClient.method131(childWidget, false), isDialogueOpen);
        } else
        if(childWidget.anInt262 == 5)
        {
          WidgetRenderHandler.renderType5Sprite(childWidget, childX, childY, gameClient.method131(childWidget, false));
        } else
        if(childWidget.anInt262 == 6)
        {
          WidgetRenderHandler.renderType6Model(childWidget, childX, childY, gameClient.method131(childWidget, false));
        } else
        if(childWidget.anInt262 == 7)
        {
          WidgetRenderHandler.renderType7ItemTextGrid(childWidget, childWidget.aClass30_Sub2_Sub1_Sub4_243, childX, childY, textColorSeed);
        }
    }

    Rasterizer2D.method333(clipBottom, clipLeft, false, clipRight, clipTop);
    gameClient.setWidgetDragOffsetY(dragOffsetY);
  }
}
