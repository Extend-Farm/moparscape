package io.github.ffakira.rsps.client.desktop.world.minimap;

import io.github.ffakira.rsps.client.desktop.core.WorldScene;
import io.github.ffakira.rsps.model.WorldPoint;

public final class MinimapViewportPlanner {

  // Legacy minimap behavior is local-area oriented, not an atlas view of the whole stitched
  // scene. The native client keeps the scene-state raster image, but this planner crops a stable
  // player-centered window out of that image before it is drawn into `mapback`.
  public MinimapViewport plan(WorldScene worldScene, WorldPoint worldPoint, int visibleTiles) {
    if (worldScene == null || worldPoint == null || !worldScene.contains(worldPoint)) {
      return null;
    }
    int cropWidth = Math.max(1, Math.min(visibleTiles * worldScene.minimapPixelWidthPerTile(), worldScene.minimapImage().width()));
    int cropHeight = Math.max(1, Math.min(visibleTiles * worldScene.minimapPixelHeightPerTile(), worldScene.minimapImage().height()));
    int playerPixelX = worldScene.projectMinimapX(worldPoint);
    int playerPixelY = worldScene.projectMinimapY(worldPoint);
    int sourceX = clamp(playerPixelX - cropWidth / 2, 0, worldScene.minimapImage().width() - cropWidth);
    int sourceY = clamp(playerPixelY - cropHeight / 2, 0, worldScene.minimapImage().height() - cropHeight);
    return new MinimapViewport(
        sourceX,
        sourceY,
        cropWidth,
        cropHeight,
        playerPixelX - sourceX,
        playerPixelY - sourceY
    );
  }

  private int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }
}
