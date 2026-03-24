package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;

final class BootstrapMediaLoader {

    static final class CoreMediaState {
        IndexedSprite invBack;
        IndexedSprite chatBack;
        IndexedSprite mapBack;
        IndexedSprite backBase1;
        IndexedSprite backBase2;
        IndexedSprite backHMid1;
        IndexedSprite[] sideIcons;
        Sprite compass;
        Sprite mapEdge;
    }

    private BootstrapMediaLoader() {
    }

    static CoreMediaState loadCoreMedia(Archive mediaArchive)
    {
        CoreMediaState state = new CoreMediaState();
        state.invBack = new IndexedSprite(mediaArchive, "invback", 0);
        state.chatBack = new IndexedSprite(mediaArchive, "chatback", 0);
        state.mapBack = new IndexedSprite(mediaArchive, "mapback", 0);
        state.backBase1 = new IndexedSprite(mediaArchive, "backbase1", 0);
        state.backBase2 = new IndexedSprite(mediaArchive, "backbase2", 0);
        state.backHMid1 = new IndexedSprite(mediaArchive, "backhmid1", 0);
        state.sideIcons = new IndexedSprite[13];
        for(int index = 0; index < 13; index++)
            state.sideIcons[index] = new IndexedSprite(mediaArchive, "sideicons", index);
        state.compass = new Sprite(mediaArchive, "compass", 0);
        state.mapEdge = new Sprite(mediaArchive, "mapedge", 0);
        state.mapEdge.method345(5059);
        return state;
    }

    static void loadMapSceneSprites(Archive mediaArchive, IndexedSprite[] mapSceneSprites)
    {
        try
        {
            for(int index = 0; index < 100; index++)
                mapSceneSprites[index] = new IndexedSprite(mediaArchive, "mapscene", index);

        }
        catch(Exception _ex) { }
    }

    static void loadMapFunctionSprites(Archive mediaArchive, Sprite[] mapFunctionSprites)
    {
        try
        {
            for(int index = 0; index < 100; index++)
                mapFunctionSprites[index] = new Sprite(mediaArchive, "mapfunction", index);

        }
        catch(Exception _ex) { }
    }

    static void loadHitmarkSprites(Archive mediaArchive, Sprite[] hitmarkSprites)
    {
        try
        {
            for(int index = 0; index < 20; index++)
                hitmarkSprites[index] = new Sprite(mediaArchive, "hitmarks", index);

        }
        catch(Exception _ex) { }
    }

    static void loadHeadIconSprites(Archive mediaArchive, Sprite[] headIconSprites)
    {
        try
        {
            for(int index = 0; index < 20; index++)
                headIconSprites[index] = new Sprite(mediaArchive, "headicons", index);

        }
        catch(Exception _ex) { }
    }
}
