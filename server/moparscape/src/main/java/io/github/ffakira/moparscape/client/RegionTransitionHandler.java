package io.github.ffakira.moparscape.client;

import io.github.ffakira.moparscape.cache.Deque;
import io.github.ffakira.moparscape.net.OnDemandFetcher;

final class RegionTransitionHandler {

    private RegionTransitionHandler() {
    }

    static int buildStaticRegionRequests(int regionX, int regionY, boolean forceNullRegions, OnDemandFetcher onDemandFetcher, int[] regionIds, int[] terrainArchiveIds, int[] objectArchiveIds)
    {
        int regionCount = 0;
        for(int mapX = (regionX - 6) / 8; mapX <= (regionX + 6) / 8; mapX++)
        {
            for(int mapY = (regionY - 6) / 8; mapY <= (regionY + 6) / 8; mapY++)
            {
                regionIds[regionCount] = (mapX << 8) + mapY;
                if(forceNullRegions && (mapY == 49 || mapY == 149 || mapY == 147 || mapX == 50 || mapX == 49 && mapY == 47))
                {
                    terrainArchiveIds[regionCount] = -1;
                    objectArchiveIds[regionCount] = -1;
                    regionCount++;
                } else
                {
                    int terrainArchiveId = terrainArchiveIds[regionCount] = onDemandFetcher.method562(0, 0, mapY, mapX);
                    if(terrainArchiveId != -1)
                        onDemandFetcher.method558(3, terrainArchiveId);
                    int objectArchiveId = objectArchiveIds[regionCount] = onDemandFetcher.method562(1, 0, mapY, mapX);
                    if(objectArchiveId != -1)
                        onDemandFetcher.method558(3, objectArchiveId);
                    regionCount++;
                }
            }
        }
        return regionCount;
    }

    static int collectDynamicRegionIds(int[][][] regionChunkTemplates, int[] regionIds)
    {
        int regionCount = 0;
        for(int plane = 0; plane < 4; plane++)
        {
            for(int chunkX = 0; chunkX < 13; chunkX++)
            {
                for(int chunkY = 0; chunkY < 13; chunkY++)
                {
                    int packedChunk = regionChunkTemplates[plane][chunkX][chunkY];
                    if(packedChunk == -1)
                        continue;
                    int mapRegionX = packedChunk >> 14 & 0x3ff;
                    int mapRegionY = packedChunk >> 3 & 0x7ff;
                    int regionId = (mapRegionX / 8 << 8) + mapRegionY / 8;
                    for(int index = 0; index < regionCount; index++)
                    {
                        if(regionIds[index] != regionId)
                            continue;
                        regionId = -1;
                        break;
                    }
                    if(regionId != -1)
                        regionIds[regionCount++] = regionId;
                }
            }
        }
        return regionCount;
    }

    static void buildDynamicRegionRequests(int[] regionIds, int regionCount, OnDemandFetcher onDemandFetcher, int[] terrainArchiveIds, int[] objectArchiveIds)
    {
        for(int index = 0; index < regionCount; index++)
        {
            int regionId = regionIds[index];
            int regionX = regionId >> 8 & 0xff;
            int regionY = regionId & 0xff;
            int terrainArchiveId = terrainArchiveIds[index] = onDemandFetcher.method562(0, 0, regionY, regionX);
            if(terrainArchiveId != -1)
                onDemandFetcher.method558(3, terrainArchiveId);
            int objectArchiveId = objectArchiveIds[index] = onDemandFetcher.method562(1, 0, regionY, regionX);
            if(objectArchiveId != -1)
                onDemandFetcher.method558(3, objectArchiveId);
        }
    }

    static void applyRegionShift(int deltaX, int deltaY, Npc[] npcs, Player[] players, int playerCount, Deque[][][] groundItems, Deque pendingSpawns)
    {
        for(int index = 0; index < 16384; index++)
        {
            Npc npc = npcs[index];
            if(npc != null)
                shiftActor(npc, deltaX, deltaY);
        }

        for(int index = 0; index < playerCount; index++)
        {
            Player player = players[index];
            if(player != null)
                shiftActor(player, deltaX, deltaY);
        }

        byte startX = 0;
        byte stopX = 104;
        byte stepX = 1;
        if(deltaX < 0)
        {
            startX = 103;
            stopX = -1;
            stepX = -1;
        }
        byte startY = 0;
        byte stopY = 104;
        byte stepY = 1;
        if(deltaY < 0)
        {
            startY = 103;
            stopY = -1;
            stepY = -1;
        }
        for(int x = startX; x != stopX; x += stepX)
        {
            for(int y = startY; y != stopY; y += stepY)
            {
                int sourceX = x + deltaX;
                int sourceY = y + deltaY;
                for(int plane = 0; plane < 4; plane++)
                    if(sourceX >= 0 && sourceY >= 0 && sourceX < 104 && sourceY < 104)
                        groundItems[plane][x][y] = groundItems[plane][sourceX][sourceY];
                    else
                        groundItems[plane][x][y] = null;
            }
        }

        for(SceneObjectSpawnRequest pendingSpawn = (SceneObjectSpawnRequest)pendingSpawns.last(); pendingSpawn != null; pendingSpawn = (SceneObjectSpawnRequest)pendingSpawns.previous(false))
        {
            pendingSpawn.anInt1297 -= deltaX;
            pendingSpawn.anInt1298 -= deltaY;
            if(pendingSpawn.anInt1297 < 0 || pendingSpawn.anInt1298 < 0 || pendingSpawn.anInt1297 >= 104 || pendingSpawn.anInt1298 >= 104)
                pendingSpawn.unlink();
        }
    }

    private static void shiftActor(Actor actor, int deltaX, int deltaY)
    {
        for(int index = 0; index < 10; index++)
        {
            actor.anIntArray1500[index] -= deltaX;
            actor.anIntArray1501[index] -= deltaY;
        }
        actor.anInt1550 -= deltaX * 128;
        actor.anInt1551 -= deltaY * 128;
    }
}
