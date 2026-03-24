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

    private static void sleepBriefly()
    {
        try
        {
            Thread.sleep(100L);
        }
        catch(Exception _ex) { }
    }
}
