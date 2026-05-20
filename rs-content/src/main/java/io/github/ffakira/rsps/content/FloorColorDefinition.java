package io.github.ffakira.rsps.content;

public record FloorColorDefinition(
    int rgb,
    int textureId,
    boolean occludes,
    int secondaryRgb,
    int hue,
    int saturation,
    int luminance,
    int blendHue,
    int blendHueMultiplier,
    int hsl16,
    int secondaryHsl16
) {
}
