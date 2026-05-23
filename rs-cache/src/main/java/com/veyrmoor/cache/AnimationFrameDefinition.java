package com.veyrmoor.cache;

public record AnimationFrameDefinition(
    int duration,
    AnimationFrameBase base,
    int transformationCount,
    int[] transformationIndices,
    int[] transformX,
    int[] transformY,
    int[] transformZ
) {
}
