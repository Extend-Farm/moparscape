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
}
