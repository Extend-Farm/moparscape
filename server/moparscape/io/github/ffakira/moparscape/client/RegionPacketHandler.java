package io.github.ffakira.moparscape.client;

final class RegionPacketHandler {

    private RegionPacketHandler() {
    }

    static int collectDynamicRegionIds(int[][][] chunkTemplates, int[] regionIdsOut)
    {
        int uniqueCount = 0;
        for(int plane = 0; plane < 4; plane++)
        {
            for(int chunkX = 0; chunkX < 13; chunkX++)
            {
                for(int chunkY = 0; chunkY < 13; chunkY++)
                {
                    int template = chunkTemplates[plane][chunkX][chunkY];
                    if(template == -1)
                        continue;
                    int templateX = template >> 14 & 0x3ff;
                    int templateY = template >> 3 & 0x7ff;
                    int regionId = (templateX / 8 << 8) + templateY / 8;
                    boolean duplicate = false;
                    for(int index = 0; index < uniqueCount; index++)
                    {
                        if(regionIdsOut[index] != regionId)
                            continue;
                        duplicate = true;
                        break;
                    }

                    if(!duplicate)
                        regionIdsOut[uniqueCount++] = regionId;
                }
            }
        }

        return uniqueCount;
    }
}
