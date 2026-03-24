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
}
