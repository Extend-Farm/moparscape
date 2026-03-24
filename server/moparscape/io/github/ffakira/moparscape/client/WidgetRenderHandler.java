package io.github.ffakira.moparscape.client;

final class WidgetRenderHandler {

    private WidgetRenderHandler() {
    }

    // Exact extraction of legacy method105 branch for widget type 7 text-item grids.
    static void renderType7ItemTextGrid(Widget widget, FontRenderer fontRenderer, int baseX, int baseY, int textColor)
    {
        int slot = 0;
        for(int row = 0; row < widget.anInt267; row++)
        {
            for(int column = 0; column < widget.anInt220; column++)
            {
                if(widget.anIntArray253[slot] > 0)
                {
                    ItemDefinition itemDefinition = ItemDefinition.method198(widget.anIntArray253[slot] - 1);
                    String itemText = itemDefinition.aString170;
                    if(itemDefinition.aBoolean176 || widget.anIntArray252[slot] != 1)
                        itemText = itemText + " x" + StackAmountFormatter.formatStackAmount(widget.anIntArray252[slot]);
                    int drawX = baseX + column * (115 + widget.anInt231);
                    int drawY = baseY + row * (12 + widget.anInt244);
                    if(widget.aBoolean223)
                        fontRenderer.method382(widget.anInt232, drawX + widget.anInt220 / 2, textColor, itemText, drawY, widget.aBoolean268);
                    else
                        fontRenderer.method389(false, widget.aBoolean268, drawX, widget.anInt232, itemText, drawY);
                }
                slot++;
            }

        }
    }

    // Exact extraction of legacy method105 branch for widget type 5 sprite draw.
    static void renderType5Sprite(Widget widget, int drawX, int drawY, boolean isWidgetActive)
    {
        Sprite sprite;
        if(isWidgetActive)
            sprite = widget.aClass30_Sub2_Sub1_Sub1_260;
        else
            sprite = widget.aClass30_Sub2_Sub1_Sub1_207;
        if(sprite != null)
            sprite.method348(drawX, 16083, drawY);
    }

    // Exact extraction of legacy method105 branch for widget type 6 model preview.
    static void renderType6Model(Widget widget, int drawX, int drawY, boolean isWidgetActive)
    {
        int previousCenterX = Rasterizer3D.anInt1466;
        int previousCenterY = Rasterizer3D.anInt1467;
        Rasterizer3D.anInt1466 = drawX + widget.anInt220 / 2;
        Rasterizer3D.anInt1467 = drawY + widget.anInt267 / 2;
        int yawSin = Rasterizer3D.anIntArray1470[widget.anInt270] * widget.anInt269 >> 16;
        int yawCos = Rasterizer3D.anIntArray1471[widget.anInt270] * widget.anInt269 >> 16;
        int sequenceId;
        if(isWidgetActive)
            sequenceId = widget.anInt258;
        else
            sequenceId = widget.anInt257;
        Model model;
        if(sequenceId == -1)
        {
            model = widget.method209(0, -1, -1, isWidgetActive);
        } else
        {
            SequenceDefinition sequence = SequenceDefinition.aClass20Array351[sequenceId];
            model = widget.method209(0, sequence.anIntArray354[widget.anInt246], sequence.anIntArray353[widget.anInt246], isWidgetActive);
        }
        if(model != null)
            model.method482(0, widget.anInt271, 0, widget.anInt270, 0, yawSin, yawCos);
        Rasterizer3D.anInt1466 = previousCenterX;
        Rasterizer3D.anInt1467 = previousCenterY;
    }

    // Exact extraction of legacy method105 branch for widget type 3 rectangle fills.
    static void renderType3Rectangle(Widget widget, int drawX, int drawY, boolean isFocusedWidget, boolean isWidgetActive)
    {
        int color;
        if(isWidgetActive)
        {
            color = widget.anInt219;
            if(isFocusedWidget && widget.anInt239 != 0)
                color = widget.anInt239;
        } else
        {
            color = widget.anInt232;
            if(isFocusedWidget && widget.anInt216 != 0)
                color = widget.anInt216;
        }
        if(widget.aByte254 == 0)
        {
            if(widget.aBoolean227)
                Rasterizer2D.method336(widget.anInt267, drawY, drawX, color, widget.anInt220, 0);
            else
                Rasterizer2D.method337(drawX, widget.anInt220, widget.anInt267, color, drawY, true);
        } else
        if(widget.aBoolean227)
            Rasterizer2D.method335(color, drawY, widget.anInt220, widget.anInt267, 256 - (widget.aByte254 & 0xff), 0, drawX);
        else
            Rasterizer2D.method338(drawY, widget.anInt267, 256 - (widget.aByte254 & 0xff), color, widget.anInt220, drawX, -17319);
    }

    // Exact extraction of legacy method105 branch for widget type 4 text rendering.
    static void renderType4Text(
        WidgetConditionPort widgetConditionPort,
        Widget widget,
        int drawX,
        int drawY,
        int textColorSeed,
        boolean isFocusedWidget,
        boolean isWidgetActive,
        boolean isDialogueOpen
    ) {
        FontRenderer fontRenderer = widget.aClass30_Sub2_Sub1_Sub4_243;
        String text = widget.aString248;
        int color;
        if(isWidgetActive)
        {
            color = widget.anInt219;
            if(isFocusedWidget && widget.anInt239 != 0)
                color = widget.anInt239;
            if(widget.aString228.length() > 0)
                text = widget.aString228;
        } else
        {
            color = widget.anInt232;
            if(isFocusedWidget && widget.anInt216 != 0)
                color = widget.anInt216;
        }
        if(widget.anInt217 == 6 && isDialogueOpen)
        {
            text = "Please wait...";
            color = widget.anInt232;
        }
        if(Rasterizer2D.anInt1379 == 479)
        {
            if(color == 0xffff00)
                color = 255;
            if(color == 49152)
                color = 0xffffff;
        }
        for(int lineY = drawY + fontRenderer.anInt1497; text.length() > 0; lineY += fontRenderer.anInt1497)
        {
            if(text.indexOf("%") != -1)
            {
                do
                {
                    int token = text.indexOf("%1");
                    if(token == -1)
                        break;
                    text = text.substring(0, token) + widgetConditionPort.formatWidgetValue(widget, 0) + text.substring(token + 2);
                } while(true);
                do
                {
                    int token = text.indexOf("%2");
                    if(token == -1)
                        break;
                    text = text.substring(0, token) + widgetConditionPort.formatWidgetValue(widget, 1) + text.substring(token + 2);
                } while(true);
                do
                {
                    int token = text.indexOf("%3");
                    if(token == -1)
                        break;
                    text = text.substring(0, token) + widgetConditionPort.formatWidgetValue(widget, 2) + text.substring(token + 2);
                } while(true);
                do
                {
                    int token = text.indexOf("%4");
                    if(token == -1)
                        break;
                    text = text.substring(0, token) + widgetConditionPort.formatWidgetValue(widget, 3) + text.substring(token + 2);
                } while(true);
                do
                {
                    int token = text.indexOf("%5");
                    if(token == -1)
                        break;
                    text = text.substring(0, token) + widgetConditionPort.formatWidgetValue(widget, 4) + text.substring(token + 2);
                } while(true);
            }
            int newlineToken = text.indexOf("\\n");
            String lineText;
            if(newlineToken != -1)
            {
                lineText = text.substring(0, newlineToken);
                text = text.substring(newlineToken + 2);
            } else
            {
                lineText = text;
                text = "";
            }
            if(widget.aBoolean223)
                fontRenderer.method382(color, drawX + widget.anInt220 / 2, textColorSeed, lineText, lineY, widget.aBoolean268);
            else
                fontRenderer.method389(false, widget.aBoolean268, drawX, color, lineText, lineY);
        }
    }

    // Exact extraction of legacy method105 branch for widget type 2 inventory grids.
    static int renderType2InventoryGrid(
        Widget parentWidget,
        Widget widget,
        int drawX,
        int drawY,
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
        FontRenderer itemAmountFont
    ) {
        int slot = 0;
        for(int row = 0; row < widget.anInt267; row++)
        {
            for(int column = 0; column < widget.anInt220; column++)
            {
                int itemX = drawX + column * (32 + widget.anInt231);
                int itemY = drawY + row * (32 + widget.anInt244);
                if(slot < 20)
                {
                    itemX += widget.anIntArray215[slot];
                    itemY += widget.anIntArray247[slot];
                }
                if(widget.anIntArray253[slot] > 0)
                {
                    int dragX = 0;
                    int dragY = 0;
                    int itemId = widget.anIntArray253[slot] - 1;
                    if(itemX > Rasterizer2D.anInt1383 - 32 && itemX < Rasterizer2D.anInt1384 && itemY > Rasterizer2D.anInt1381 - 32 && itemY < Rasterizer2D.anInt1382 || dragState != 0 && dragSlot == slot)
                    {
                        int outlineColor = 0;
                        if(itemUseState == 1 && itemUseSlot == slot && itemUseWidgetId == widget.anInt250)
                            outlineColor = 0xffffff;
                        Sprite itemSprite = ItemDefinition.method200(itemId, widget.anIntArray252[slot], outlineColor, 9);
                        if(itemSprite != null)
                        {
                            if(dragState != 0 && dragSlot == slot && dragWidgetId == widget.anInt250)
                            {
                                dragX = mouseX - dragStartMouseX;
                                dragY = mouseY - dragOffsetY;
                                if(dragX < 5 && dragX > -5)
                                    dragX = 0;
                                if(dragY < 5 && dragY > -5)
                                    dragY = 0;
                                if(dragThreshold < 5)
                                {
                                    dragX = 0;
                                    dragY = 0;
                                }
                                itemSprite.method350(itemX + dragX, itemY + dragY, 128, dragAlphaEnabled);
                                if(itemY + dragY < Rasterizer2D.anInt1381 && parentWidget.anInt224 > 0)
                                {
                                    int scrollDelta = (gameTickDelta * (Rasterizer2D.anInt1381 - itemY - dragY)) / 3;
                                    if(scrollDelta > gameTickDelta * 10)
                                        scrollDelta = gameTickDelta * 10;
                                    if(scrollDelta > parentWidget.anInt224)
                                        scrollDelta = parentWidget.anInt224;
                                    parentWidget.anInt224 -= scrollDelta;
                                    dragOffsetY += scrollDelta;
                                }
                                if(itemY + dragY + 32 > Rasterizer2D.anInt1382 && parentWidget.anInt224 < parentWidget.anInt261 - parentWidget.anInt267)
                                {
                                    int scrollDelta = (gameTickDelta * ((itemY + dragY + 32) - Rasterizer2D.anInt1382)) / 3;
                                    if(scrollDelta > gameTickDelta * 10)
                                        scrollDelta = gameTickDelta * 10;
                                    if(scrollDelta > parentWidget.anInt261 - parentWidget.anInt267 - parentWidget.anInt224)
                                        scrollDelta = parentWidget.anInt261 - parentWidget.anInt267 - parentWidget.anInt224;
                                    parentWidget.anInt224 += scrollDelta;
                                    dragOffsetY -= scrollDelta;
                                }
                            } else
                            if(selectedDragState != 0 && selectedDragSlot == slot && selectedDragWidgetId == widget.anInt250)
                                itemSprite.method350(itemX, itemY, 128, dragAlphaEnabled);
                            else
                                itemSprite.method348(itemX, 16083, itemY);
                            if(itemSprite.anInt1444 == 33 || widget.anIntArray252[slot] != 1)
                            {
                                int itemAmount = widget.anIntArray252[slot];
                                itemAmountFont.method385(0, formatLegacyItemAmount(itemAmount), itemY + 10 + dragY, 822, itemX + 1 + dragX);
                                itemAmountFont.method385(0xffff00, formatLegacyItemAmount(itemAmount), itemY + 9 + dragY, 822, itemX + dragX);
                            }
                        }
                    }
                } else
                if(widget.aClass30_Sub2_Sub1_Sub1Array209 != null && slot < 20)
                {
                    Sprite fallbackSprite = widget.aClass30_Sub2_Sub1_Sub1Array209[slot];
                    if(fallbackSprite != null)
                        fallbackSprite.method348(itemX, 16083, itemY);
                }
                slot++;
            }

        }
        return dragOffsetY;
    }

    private static String formatLegacyItemAmount(int amount)
    {
        if(amount < 0x186a0)
            return String.valueOf(amount);
        if(amount < 0x989680)
            return amount / 1000 + "K";
        else
            return amount / 0xf4240 + "M";
    }
}
