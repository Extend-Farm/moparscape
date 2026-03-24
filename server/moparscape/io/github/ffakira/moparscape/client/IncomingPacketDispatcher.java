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
}
