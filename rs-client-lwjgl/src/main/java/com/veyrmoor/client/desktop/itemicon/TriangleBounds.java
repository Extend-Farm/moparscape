package com.veyrmoor.client.desktop.itemicon;

record TriangleBounds(int minX, int maxX, int minY, int maxY) {

  static TriangleBounds from(ScreenVertex a, ScreenVertex b, ScreenVertex c) {
    float minFaceX = Math.min(a.x(), Math.min(b.x(), c.x()));
    float maxFaceX = Math.max(a.x(), Math.max(b.x(), c.x()));
    float minFaceY = Math.min(a.y(), Math.min(b.y(), c.y()));
    float maxFaceY = Math.max(a.y(), Math.max(b.y(), c.y()));
    int minX = MathUtil.clamp((int) Math.ceil(minFaceX), 0, IconRenderConstants.MAX_PIXEL_INDEX);
    int maxX = MathUtil.clamp((int) Math.floor(maxFaceX), 0, IconRenderConstants.MAX_PIXEL_INDEX);
    int minY = MathUtil.clamp((int) Math.ceil(minFaceY), 0, IconRenderConstants.MAX_PIXEL_INDEX);
    int maxY = MathUtil.clamp((int) Math.floor(maxFaceY), 0, IconRenderConstants.MAX_PIXEL_INDEX);
    // The classic sprite rasterizer treats an exact bottom edge as exclusive, which prevents the
    // 1px tail that shows up on runes when the native loop includes it.
    if (Math.abs(maxFaceY - Math.rint(maxFaceY)) <= IconRenderConstants.EDGE_EPSILON) {
      maxY--;
    }
    return new TriangleBounds(minX, maxX, minY, maxY);
  }

  boolean isEmpty() {
    return minX > maxX || minY > maxY;
  }
}
