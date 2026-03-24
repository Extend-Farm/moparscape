package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;

final class BootstrapMediaLoader {

    private BootstrapMediaLoader() {
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
