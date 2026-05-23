package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.net.OnDemandFetcher;

final class BootstrapDemandLoader {

    private BootstrapDemandLoader() {
    }

    static boolean requestAnimations(GameClientCore core, OnDemandFetcher onDemandFetcher)
    {
        core.method13(65, (byte)4, "Requesting animations");
        int animationCount = onDemandFetcher.method555(79, 1);
        for(int index = 0; index < animationCount; index++)
            onDemandFetcher.method558(1, index);
        while(onDemandFetcher.method552() > 0)
        {
            int loaded = animationCount - onDemandFetcher.method552();
            if(loaded > 0)
                core.method13(65, (byte)4, "Loading animations - " + (loaded * 100) / animationCount + "%");
            core.method57(false);
            sleepBriefly();
            if(onDemandFetcher.anInt1349 > 3)
                return false;
        }
        return true;
    }

    static void requestModels(GameClientCore core, OnDemandFetcher onDemandFetcher)
    {
        core.method13(70, (byte)4, "Requesting models");
        int modelCount = onDemandFetcher.method555(79, 0);
        for(int modelId = 0; modelId < modelCount; modelId++)
        {
            int flags = onDemandFetcher.method559(modelId, -203);
            if((flags & 1) != 0)
                onDemandFetcher.method558(0, modelId);
        }

        int queuedModels = onDemandFetcher.method552();
        while(onDemandFetcher.method552() > 0)
        {
            int loaded = queuedModels - onDemandFetcher.method552();
            if(loaded > 0)
                core.method13(70, (byte)4, "Loading models - " + (loaded * 100) / queuedModels + "%");
            core.method57(false);
            sleepBriefly();
        }
    }

    static void requestStartupMaps(GameClientCore core, OnDemandFetcher onDemandFetcher)
    {
        core.method13(75, (byte)4, "Requesting maps");
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 48, 47));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 48, 47));
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 48, 48));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 48, 48));
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 48, 49));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 48, 49));
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 47, 47));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 47, 47));
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 47, 48));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 47, 48));
        onDemandFetcher.method558(3, onDemandFetcher.method562(0, 0, 148, 48));
        onDemandFetcher.method558(3, onDemandFetcher.method562(1, 0, 148, 48));
        int mapCount = onDemandFetcher.method552();
        while(onDemandFetcher.method552() > 0)
        {
            int loaded = mapCount - onDemandFetcher.method552();
            if(loaded > 0)
                core.method13(75, (byte)4, "Loading maps - " + (loaded * 100) / mapCount + "%");
            core.method57(false);
            sleepBriefly();
        }
    }

    static void queuePriorityModelRequests(OnDemandFetcher onDemandFetcher)
    {
        int modelCount = onDemandFetcher.method555(79, 0);
        for(int modelId = 0; modelId < modelCount; modelId++)
        {
            int flags = onDemandFetcher.method559(modelId, -203);
            byte priority = 0;
            if((flags & 8) != 0)
                priority = 10;
            else
            if((flags & 0x20) != 0)
                priority = 9;
            else
            if((flags & 0x10) != 0)
                priority = 8;
            else
            if((flags & 0x40) != 0)
                priority = 7;
            else
            if((flags & 0x80) != 0)
                priority = 6;
            else
            if((flags & 2) != 0)
                priority = 5;
            else
            if((flags & 4) != 0)
                priority = 4;
            if((flags & 1) != 0)
                priority = 3;
            if(priority != 0)
                onDemandFetcher.method563(priority, 0, modelId, (byte)8);
        }
    }

    static void queueMemberSongs(OnDemandFetcher onDemandFetcher)
    {
        int songCount = onDemandFetcher.method555(79, 2);
        for(int songId = 1; songId < songCount; songId++)
            if(onDemandFetcher.method569(songId, 5))
                onDemandFetcher.method563((byte)1, 2, songId, (byte)8);
    }

    static void applyRandomMapSpriteTint(Sprite[] mapFunctionSprites, IndexedSprite[] mapSceneSprites)
    {
        int i5 = (int)(Math.random() * 21D) - 10;
        int j5 = (int)(Math.random() * 21D) - 10;
        int k5 = (int)(Math.random() * 21D) - 10;
        int l5 = (int)(Math.random() * 41D) - 20;
        for(int i6 = 0; i6 < 100; i6++)
        {
            if(mapFunctionSprites[i6] != null)
                mapFunctionSprites[i6].method344(i5 + l5, j5 + l5, k5 + l5, 0);
            if(mapSceneSprites[i6] != null)
                mapSceneSprites[i6].method360(i5 + l5, j5 + l5, k5 + l5, 0);
        }
    }

    private static void sleepBriefly()
    {
        try
        {
            Thread.sleep(100L);
        }
        catch(Exception _ex) { }
    }
}
