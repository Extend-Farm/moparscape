package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Archive;

final class BootstrapMediaLayoutLoader {

    static final class State {
        Sprite mapMarker0;
        Sprite mapMarker1;
        Sprite[] crossSprites;
        Sprite[] mapDotSprites;
        IndexedSprite scrollbar0;
        IndexedSprite scrollbar1;
        IndexedSprite redstone1;
        IndexedSprite redstone2;
        IndexedSprite redstone3;
        IndexedSprite redstone1Flipped;
        IndexedSprite redstone2Flipped;
        IndexedSprite redstone1Mirrored;
        IndexedSprite redstone2Mirrored;
        IndexedSprite redstone3Mirrored;
        IndexedSprite redstone1BothTransforms;
        IndexedSprite redstone2BothTransforms;
        IndexedSprite[] modIcons;
    }

    private BootstrapMediaLayoutLoader() {
    }

    static State load(Archive mediaArchive)
    {
        State state = new State();
        state.mapMarker0 = new Sprite(mediaArchive, "mapmarker", 0);
        state.mapMarker1 = new Sprite(mediaArchive, "mapmarker", 1);
        state.crossSprites = new Sprite[8];
        for(int index = 0; index < 8; index++)
            state.crossSprites[index] = new Sprite(mediaArchive, "cross", index);
        state.mapDotSprites = new Sprite[] {
            new Sprite(mediaArchive, "mapdots", 0),
            new Sprite(mediaArchive, "mapdots", 1),
            new Sprite(mediaArchive, "mapdots", 2),
            new Sprite(mediaArchive, "mapdots", 3),
            new Sprite(mediaArchive, "mapdots", 4)
        };
        state.scrollbar0 = new IndexedSprite(mediaArchive, "scrollbar", 0);
        state.scrollbar1 = new IndexedSprite(mediaArchive, "scrollbar", 1);
        state.redstone1 = new IndexedSprite(mediaArchive, "redstone1", 0);
        state.redstone2 = new IndexedSprite(mediaArchive, "redstone2", 0);
        state.redstone3 = new IndexedSprite(mediaArchive, "redstone3", 0);
        state.redstone1Flipped = new IndexedSprite(mediaArchive, "redstone1", 0);
        state.redstone1Flipped.method358(0);
        state.redstone2Flipped = new IndexedSprite(mediaArchive, "redstone2", 0);
        state.redstone2Flipped.method358(0);
        state.redstone1Mirrored = new IndexedSprite(mediaArchive, "redstone1", 0);
        state.redstone1Mirrored.method359(true);
        state.redstone2Mirrored = new IndexedSprite(mediaArchive, "redstone2", 0);
        state.redstone2Mirrored.method359(true);
        state.redstone3Mirrored = new IndexedSprite(mediaArchive, "redstone3", 0);
        state.redstone3Mirrored.method359(true);
        state.redstone1BothTransforms = new IndexedSprite(mediaArchive, "redstone1", 0);
        state.redstone1BothTransforms.method358(0);
        state.redstone1BothTransforms.method359(true);
        state.redstone2BothTransforms = new IndexedSprite(mediaArchive, "redstone2", 0);
        state.redstone2BothTransforms.method358(0);
        state.redstone2BothTransforms.method359(true);
        state.modIcons = new IndexedSprite[2];
        for(int index = 0; index < 2; index++)
            state.modIcons[index] = new IndexedSprite(mediaArchive, "mod_icons", index);
        return state;
    }
}
