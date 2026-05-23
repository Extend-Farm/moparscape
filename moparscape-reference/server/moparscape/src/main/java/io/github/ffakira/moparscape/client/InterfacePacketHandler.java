package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class InterfacePacketHandler {

    private InterfacePacketHandler() {
    }

    static void applyPlayerIdentityWidget(PacketBuffer packetBuffer, Player localPlayer)
    {
        int widgetId = packetBuffer.method436((byte)-74);
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.anInt233 = 3;
        if(localPlayer.aClass5_1698 == null)
            widget.anInt234 = (localPlayer.anIntArray1700[0] << 25) + (localPlayer.anIntArray1700[4] << 20) + (localPlayer.anIntArray1717[0] << 15) + (localPlayer.anIntArray1717[8] << 10) + (localPlayer.anIntArray1717[11] << 5) + localPlayer.anIntArray1717[1];
        else
            widget.anInt234 = (int)(0x12345678L + localPlayer.aClass5_1698.aLong78);
    }

    static void applyWidgetItemModel(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method434((byte)108);
        int zoomScale = packetBuffer.method410();
        int itemId = packetBuffer.method410();
        Widget widget = Widget.aClass9Array210[widgetId];
        if(itemId == 65535)
        {
            widget.anInt233 = 0;
            return;
        } else
        {
            ItemDefinition itemDefinition = ItemDefinition.method198(itemId);
            widget.anInt233 = 4;
            widget.anInt234 = itemId;
            widget.anInt270 = itemDefinition.anInt190;
            widget.anInt271 = itemDefinition.anInt198;
            widget.anInt269 = (itemDefinition.anInt181 * 100) / zoomScale;
            return;
        }
    }

    static void applyWidgetVisibility(PacketBuffer packetBuffer)
    {
        boolean hidden = packetBuffer.method408() == 1;
        int widgetId = packetBuffer.method410();
        Widget.aClass9Array210[widgetId].aBoolean266 = hidden;
    }

    static boolean applyWidgetText(PacketBuffer packetBuffer, int[] tabInterfaceIds, int currentTabId)
    {
        String text = packetBuffer.method415();
        int widgetId = packetBuffer.method435(true);
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.aString248 = text;
        return widget.anInt236 == tabInterfaceIds[currentTabId];
    }

    static void applyWidgetNpcHeadModel(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method436((byte)-74);
        int modelId = packetBuffer.method410();
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.anInt233 = 1;
        widget.anInt234 = modelId;
    }

    static void applyWidgetTextColor(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method436((byte)-74);
        int packedColor = packetBuffer.method436((byte)-74);
        int red = packedColor >> 10 & 0x1f;
        int green = packedColor >> 5 & 0x1f;
        int blue = packedColor & 0x1f;
        Widget.aClass9Array210[widgetId].anInt232 = (red << 19) + (green << 11) + (blue << 3);
    }

    static void applyWidgetItemGridSnapshot(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method410();
        Widget widget = Widget.aClass9Array210[widgetId];
        int itemCount = packetBuffer.method410();
        for(int index = 0; index < itemCount; index++)
        {
            int amount = packetBuffer.method408();
            if(amount == 255)
                amount = packetBuffer.method440(true);
            widget.anIntArray253[index] = packetBuffer.method436((byte)-74);
            widget.anIntArray252[index] = amount;
        }

        for(int index = itemCount; index < widget.anIntArray253.length; index++)
        {
            widget.anIntArray253[index] = 0;
            widget.anIntArray252[index] = 0;
        }
    }

    static void applyWidgetModelTransform(PacketBuffer packetBuffer)
    {
        int zoom = packetBuffer.method435(true);
        int widgetId = packetBuffer.method410();
        int rotationX = packetBuffer.method410();
        int rotationY = packetBuffer.method436((byte)-74);
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.anInt270 = rotationX;
        widget.anInt271 = rotationY;
        widget.anInt269 = zoom;
    }

    static void applyWidgetAnimation(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method410();
        int animationId = packetBuffer.method411();
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.anInt257 = animationId;
        if(animationId == -1)
        {
            widget.anInt246 = 0;
            widget.anInt208 = 0;
        }
    }

    static void applyWidgetItemSlotUpdates(PacketBuffer packetBuffer, int packetSize)
    {
        int widgetId = packetBuffer.method410();
        Widget widget = Widget.aClass9Array210[widgetId];
        while(packetBuffer.anInt1406 < packetSize) 
        {
            int slot = packetBuffer.method422();
            int itemId = packetBuffer.method410();
            int amount = packetBuffer.method408();
            if(amount == 255)
                amount = packetBuffer.method413();
            if(slot >= 0 && slot < widget.anIntArray253.length)
            {
                widget.anIntArray253[slot] = itemId;
                widget.anIntArray252[slot] = amount;
            }
        }
    }

    static int readOverlayWidgetId(PacketBuffer packetBuffer)
    {
        return packetBuffer.method437(-665);
    }

    static void applyPlayerMenuOption(PacketBuffer packetBuffer, String[] playerOptions, boolean[] playerOptionPriorities)
    {
        int optionIndex = packetBuffer.method427(false);
        int lowPriorityFlag = packetBuffer.method426(0);
        String optionText = packetBuffer.method415();
        if(optionIndex >= 1 && optionIndex <= 5)
        {
            if(optionText.equalsIgnoreCase("null"))
                optionText = null;
            playerOptions[optionIndex - 1] = optionText;
            playerOptionPriorities[optionIndex - 1] = lowPriorityFlag == 0;
        }
    }

    static int[] readRootAndSidebarInterfaces(PacketBuffer packetBuffer)
    {
        int rootInterfaceId = packetBuffer.method435(true);
        int sidebarInterfaceId = packetBuffer.method410();
        return new int[] {
            rootInterfaceId, sidebarInterfaceId
        };
    }

    static int readSidebarWidgetId(PacketBuffer packetBuffer)
    {
        return packetBuffer.method434((byte)108);
    }

    static int readPublicChatMode(PacketBuffer packetBuffer)
    {
        return packetBuffer.method411();
    }

    static int readTabId(PacketBuffer packetBuffer)
    {
        return packetBuffer.method427(false);
    }
}
