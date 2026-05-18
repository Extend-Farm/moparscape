package io.github.ffakira.rsps.client.lwjgl;

record WorldSceneVisibilityWindow(
    int minLocalX,
    int maxLocalX,
    int minLocalY,
    int maxLocalY
) {

  boolean containsTile(int localX, int localY) {
    return localX >= minLocalX && localX <= maxLocalX && localY >= minLocalY && localY <= maxLocalY;
  }

  boolean intersectsArea(float minX, float minY, float maxX, float maxY) {
    return maxX >= minLocalX
        && minX <= maxLocalX + 1.0f
        && maxY >= minLocalY
        && minY <= maxLocalY + 1.0f;
  }
}
