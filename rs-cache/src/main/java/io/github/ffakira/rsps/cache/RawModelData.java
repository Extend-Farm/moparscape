package io.github.ffakira.rsps.cache;

public record RawModelData(
    int vertexCount,
    int faceCount,
    int texturedFaceCount,
    int[] vertexX,
    int[] vertexY,
    int[] vertexZ,
    int[] faceVertexA,
    int[] faceVertexB,
    int[] faceVertexC,
    int[] faceColorHsl,
    int[] faceRenderTypes,
    int[] facePriorities,
    int[] faceAlpha,
    int[] texturedFaceVertexA,
    int[] texturedFaceVertexB,
    int[] texturedFaceVertexC
) {
}
