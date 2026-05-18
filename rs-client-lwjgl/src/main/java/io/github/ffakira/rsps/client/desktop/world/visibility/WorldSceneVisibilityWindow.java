package io.github.ffakira.rsps.client.desktop.world.visibility;

public record WorldSceneVisibilityWindow(
    int minLocalX,
    int maxLocalX,
    int minLocalY,
    int maxLocalY
) {

  public boolean containsTile(int localX, int localY) {
    return localX >= minLocalX && localX <= maxLocalX && localY >= minLocalY && localY <= maxLocalY;
  }

  public boolean intersectsArea(float minX, float minY, float maxX, float maxY) {
    return maxX >= minLocalX
        && minX <= maxLocalX + 1.0f
        && maxY >= minLocalY
        && minY <= maxLocalY + 1.0f;
  }
}
