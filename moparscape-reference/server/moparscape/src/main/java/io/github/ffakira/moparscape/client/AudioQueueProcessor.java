package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.PacketBuffer;
import io.github.ffakira.moparscape.net.OnDemandFetcher;

final class AudioQueueProcessor {

    static final class State {
        int queueCount;
        int currentTrackArchive;
        int currentTrackFile;
        int bufferedSoundLength;
        long bufferedSoundTimestamp;
        int musicCooldown;
        int requestedSongId;
        boolean pendingSongRequest;
    }

    private AudioQueueProcessor() {
    }

    static State process(GameClientCore core, int queueCount, int[] trackArchiveIds, int[] trackFileIds, int[] queueDelays,
                         int currentTrackArchive, int currentTrackFile, long bufferedSoundTimestamp, int bufferedSoundLength,
                         int musicCooldown, boolean musicEnabled, boolean muted, int requestedSongId, OnDemandFetcher onDemandFetcher)
    {
        State state = new State();
        state.queueCount = queueCount;
        state.currentTrackArchive = currentTrackArchive;
        state.currentTrackFile = currentTrackFile;
        state.bufferedSoundTimestamp = bufferedSoundTimestamp;
        state.bufferedSoundLength = bufferedSoundLength;
        state.musicCooldown = musicCooldown;
        state.requestedSongId = requestedSongId;
        state.pendingSongRequest = false;
        for(int index = 0; index < state.queueCount; index++)
            if(queueDelays[index] <= 0)
            {
                boolean shouldDrop = false;
                try
                {
                    if(trackArchiveIds[index] == state.currentTrackArchive && trackFileIds[index] == state.currentTrackFile)
                    {
                        if(!core.method27(11456))
                            shouldDrop = true;
                    } else
                    {
                        PacketBuffer soundData = SoundEffect.method241(trackFileIds[index], trackArchiveIds[index], false);
                        if(System.currentTimeMillis() + (long)(soundData.anInt1406 / 22) > state.bufferedSoundTimestamp + (long)(state.bufferedSoundLength / 22))
                        {
                            state.bufferedSoundLength = soundData.anInt1406;
                            state.bufferedSoundTimestamp = System.currentTimeMillis();
                            if(core.method59(soundData.aByteArray1405, (byte)116, soundData.anInt1406))
                            {
                                state.currentTrackArchive = trackArchiveIds[index];
                                state.currentTrackFile = trackFileIds[index];
                            } else
                            {
                                shouldDrop = true;
                            }
                        }
                    }
                }
                catch(Exception exception) { }
                if(!shouldDrop || queueDelays[index] == -5)
                {
                    state.queueCount--;
                    for(int offset = index; offset < state.queueCount; offset++)
                    {
                        trackArchiveIds[offset] = trackArchiveIds[offset + 1];
                        trackFileIds[offset] = trackFileIds[offset + 1];
                        queueDelays[offset] = queueDelays[offset + 1];
                    }

                    index--;
                } else
                {
                    queueDelays[index] = -5;
                }
            } else
            {
                queueDelays[index]--;
            }

        if(state.musicCooldown > 0)
        {
            state.musicCooldown -= 20;
            if(state.musicCooldown < 0)
                state.musicCooldown = 0;
            if(state.musicCooldown == 0 && musicEnabled && !muted)
            {
                state.pendingSongRequest = true;
                onDemandFetcher.method558(2, requestedSongId);
            }
        }
        return state;
    }
}
