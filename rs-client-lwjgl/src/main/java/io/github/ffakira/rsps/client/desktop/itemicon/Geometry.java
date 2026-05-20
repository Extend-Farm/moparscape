package io.github.ffakira.rsps.client.desktop.itemicon;

final class Geometry {

  private Geometry() {
  }

  static float signedArea(float ax, float ay, float bx, float by, float cx, float cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }

  static boolean coversSample(float edgeValue, boolean topLeftEdge) {
    return edgeValue > IconRenderConstants.EDGE_EPSILON
        || Math.abs(edgeValue) <= IconRenderConstants.EDGE_EPSILON && topLeftEdge;
  }

  static boolean isTopLeftEdge(float startX, float startY, float endX, float endY) {
    return startY < endY || startY == endY && startX > endX;
  }
}
