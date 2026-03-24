package io.github.ffakira.moparscape.client.net.packet;

import io.github.ffakira.moparscape.net.PacketBuffer;

public final class SystemAudioPacketHandler {

    private SystemAudioPacketHandler() {
    }

    public static boolean shouldQueueSong(int songId, int currentSongId, boolean musicEnabled, boolean muted, int currentSongDelay)
    {
        return songId != currentSongId && musicEnabled && !muted && currentSongDelay == 0;
    }

    public static boolean shouldQueueDelayedSong(boolean musicEnabled, boolean muted)
    {
        return musicEnabled && !muted;
    }

    public static int[] readAreaSoundEffect(PacketBuffer packetBuffer)
    {
        int effectId = packetBuffer.method410();
        int effectLoops = packetBuffer.method408();
        int effectDelay = packetBuffer.method410();
        return new int[] {
            effectId, effectLoops, effectDelay
        };
    }
}
