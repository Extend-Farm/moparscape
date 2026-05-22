package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.core.ArchiveSpriteResolver;
import io.github.ffakira.rsps.client.desktop.gameplay.sidebar.GameplayStatsTabAssets;
import io.github.ffakira.rsps.content.InterfaceComponentCatalog;

public record GameplayFrameAssets(
    ArgbImage backLeft1,
    ArgbImage backLeft2,
    ArgbImage backRight1,
    ArgbImage backRight2,
    ArgbImage backTop1,
    ArgbImage backVmid1,
    ArgbImage backVmid2,
    ArgbImage backVmid3,
    ArgbImage backHmid2,
    ArgbImage invBack,
    ArgbImage chatBack,
    ArgbImage mapBack,
    ArgbImage backBase1,
    ArgbImage backBase2,
    ArgbImage backHMid1,
    ArgbImage compass,
    ArgbImage redstone1,
    ArgbImage redstone2,
    ArgbImage redstone3,
    ArgbImage redstone1Flipped,
    ArgbImage redstone2Flipped,
    ArgbImage redstone1Mirrored,
    ArgbImage redstone2Mirrored,
    ArgbImage redstone3Mirrored,
    ArgbImage redstone1BothTransforms,
    ArgbImage redstone2BothTransforms,
    ArgbImage[] sideIcons,
    ArgbImage[] mapFunctionIcons,
    ArgbImage[] mapDotIcons,
    ArgbImage[] clickCrosses,
    GameplayStatsTabAssets statsTabAssets,
    InterfaceComponentCatalog interfaceComponents,
    ArchiveSpriteResolver mediaSpriteResolver
) {
}
