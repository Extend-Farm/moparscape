package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class InventoryPacketHandler {

    private InventoryPacketHandler() {
    }

    static int[] readVarpIntUpdate(PacketBuffer packetBuffer)
    {
        int index = packetBuffer.method434((byte)108);
        int value = packetBuffer.method439((byte)41);
        return new int[] {
            index, value
        };
    }

    static int[] readVarpByteUpdate(PacketBuffer packetBuffer)
    {
        int index = packetBuffer.method434((byte)108);
        int value = packetBuffer.method409();
        return new int[] {
            index, value
        };
    }
}
