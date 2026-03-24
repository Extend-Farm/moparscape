package io.github.ffakira.moparscape.client;

final class WidgetContainerHandler {

    private WidgetContainerHandler() {
    }

    // Exact extraction of legacy method105 branch for widget type 0 containers.
    static void renderType0Container(GameClientCore gameClient, Widget widget, int drawX, int drawY)
    {
        if(widget.anInt214 > 0)
            gameClient.method75(950, widget);
        if(widget.anInt224 > widget.anInt261 - widget.anInt267)
            widget.anInt224 = widget.anInt261 - widget.anInt267;
        if(widget.anInt224 < 0)
            widget.anInt224 = 0;
        gameClient.method105(8, widget.anInt224, drawX, widget, drawY);
        if(widget.anInt261 > widget.anInt267)
            gameClient.method30(519, widget.anInt267, widget.anInt224, drawY, drawX + widget.anInt220, widget.anInt261);
    }
}
