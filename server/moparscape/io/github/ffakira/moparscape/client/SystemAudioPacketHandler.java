package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;

final class SystemAudioPacketHandler {

    private SystemAudioPacketHandler() {
    }

    static boolean shouldQueueSong(int songId, int currentSongId, boolean musicEnabled, boolean muted, int currentSongDelay)
    {
        return songId != currentSongId && musicEnabled && !muted && currentSongDelay == 0;
    }

    static boolean shouldQueueDelayedSong(boolean musicEnabled, boolean muted)
    {
        return musicEnabled && !muted;
    }

    static int[] readAreaSoundEffect(PacketBuffer packetBuffer)
    {
        int effectId = packetBuffer.method410();
        int effectLoops = packetBuffer.method408();
        int effectDelay = packetBuffer.method410();
        return new int[] {
            effectId, effectLoops, effectDelay
        };
    }
}
