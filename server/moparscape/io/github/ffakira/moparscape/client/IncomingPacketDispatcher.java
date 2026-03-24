package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class IncomingPacketDispatcher {

    private IncomingPacketDispatcher() {
    }

    static int applyIgnoreListSnapshot(PacketBuffer packetBuffer, int packetSize, long[] ignoredNameHashes)
    {
        int ignoreCount = packetSize / 8;
        for(int index = 0; index < ignoreCount; index++)
            ignoredNameHashes[index] = packetBuffer.method414(-35089);
        return ignoreCount;
    }

    static void resetCameraEffects(boolean[] enabledCameraEffects)
    {
        for(int effectIndex = 0; effectIndex < 5; effectIndex++)
            enabledCameraEffects[effectIndex] = false;
    }

    static void applyCameraEffectUpdate(PacketBuffer packetBuffer, boolean[] enabledCameraEffects, int[] effectTargets, int[] effectAmplitudes, int[] effectFrequencies, int[] effectPhases)
    {
        int effectSlot = packetBuffer.method408();
        int effectTarget = packetBuffer.method408();
        int effectAmplitude = packetBuffer.method408();
        int effectFrequency = packetBuffer.method408();
        enabledCameraEffects[effectSlot] = true;
        effectTargets[effectSlot] = effectTarget;
        effectAmplitudes[effectSlot] = effectAmplitude;
        effectFrequencies[effectSlot] = effectFrequency;
        effectPhases[effectSlot] = 0;
    }

    static void clearWidgetItemContainer(PacketBuffer packetBuffer)
    {
        int widgetId = packetBuffer.method434((byte)108);
        Widget widget = Widget.aClass9Array210[widgetId];
        for(int slot = 0; slot < widget.anIntArray253.length; slot++)
        {
            widget.anIntArray253[slot] = -1;
            widget.anIntArray253[slot] = 0;
        }
    }

    static void applyWidgetScrollPosition(PacketBuffer packetBuffer)
    {
        int scrollX = packetBuffer.method411();
        int scrollY = packetBuffer.method437(-665);
        int widgetId = packetBuffer.method434((byte)108);
        Widget widget = Widget.aClass9Array210[widgetId];
        widget.anInt263 = scrollX;
        widget.anInt265 = scrollY;
    }

    static void applyWidgetModelId(PacketBuffer packetBuffer)
    {
        int modelId = packetBuffer.method436((byte)-74);
        int widgetId = packetBuffer.method436((byte)-74);
        Widget.aClass9Array210[widgetId].anInt233 = 2;
        Widget.aClass9Array210[widgetId].anInt234 = modelId;
    }

    static int readMinimapState(PacketBuffer packetBuffer)
    {
        return packetBuffer.method408();
    }

    static int readSystemUpdateTimer(PacketBuffer packetBuffer)
    {
        return packetBuffer.method434((byte)108) * 30;
    }
}
