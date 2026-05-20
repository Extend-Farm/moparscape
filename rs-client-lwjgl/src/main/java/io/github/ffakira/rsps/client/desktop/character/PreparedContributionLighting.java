package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;

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
