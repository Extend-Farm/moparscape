package io.github.ffakira.rsps.content;

public record MapObjectPlacement(
    int objectId,
    int worldX,
    int worldY,
    int plane,
    int type,
    int orientation
) {
}
