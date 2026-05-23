package com.veyrmoor.client.desktop.character;

import com.veyrmoor.client.desktop.world.raster.SceneRasterMode;

record PreparedContributionLighting(
    int[] faceColorA,
    int[] faceColorB,
    int[] faceColorC,
    SceneRasterMode[] faceRasterModes,
    int[] faceTextureIds,
    int[] textureVertexA,
    int[] textureVertexB,
    int[] textureVertexC
) {
}
