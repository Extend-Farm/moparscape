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
}
