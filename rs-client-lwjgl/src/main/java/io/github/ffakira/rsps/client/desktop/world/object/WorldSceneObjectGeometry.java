package io.github.ffakira.rsps.client.desktop.world.object;

import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;

public record WorldSceneObjectGeometry(
    float[] vertexX,
    float[] vertexY,
    float[] vertexZ,
    int[] faceVertexA,
    int[] faceVertexB,
    int[] faceVertexC,
    int[] faceColorA,
    int[] faceColorB,
    int[] faceColorC,
    int[] faceAlpha,
    SceneRasterMode[] faceRasterModes,
    int[] faceTextureIds,
    int[] textureVertexA,
    int[] textureVertexB,
    int[] textureVertexC
) {
}
