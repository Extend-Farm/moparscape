package com.veyrmoor.content;

public record ContentDefinitionSummary(
    int itemCount,
    int npcCount,
    int objectCount,
    int mapRegionCount
) {
}
