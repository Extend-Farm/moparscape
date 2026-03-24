package io.github.ffakira.moparscape.client;

final class WidgetInteractionHandler {

    private WidgetInteractionHandler() {
    }

    static boolean canRenderWidgetTree(Widget parentWidget, int hoverWidgetId, int dragTargetWidgetId, int selectedWidgetId)
    {
        if(parentWidget.anInt262 != 0 || parentWidget.anIntArray240 == null)
            return false;
        return !parentWidget.aBoolean266 || hoverWidgetId == parentWidget.anInt250 || dragTargetWidgetId == parentWidget.anInt250 || selectedWidgetId == parentWidget.anInt250;
    }

    static boolean shouldFlipMenuStateGuard(int drawModeGuard)
    {
        return drawModeGuard != 8;
    }
}
