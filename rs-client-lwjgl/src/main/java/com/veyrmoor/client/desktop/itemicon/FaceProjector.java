package com.veyrmoor.client.desktop.itemicon;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;

final class FaceProjector {

  private FaceProjector() {
  }

  static ProjectedFace projectFace(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC,
      int alpha,
      ArgbImage texture,
      int priority,
      float averageDepth,
      int faceIndex
  ) {
    if (vertexA.depth() <= IconRenderConstants.NEAR_PLANE
        || vertexB.depth() <= IconRenderConstants.NEAR_PLANE
        || vertexC.depth() <= IconRenderConstants.NEAR_PLANE) {
      return null;
    }
    float ax = projectScreenX(vertexA.viewX(), vertexA.depth());
    float ay = projectScreenY(vertexA.viewY(), vertexA.depth());
    float bx = projectScreenX(vertexB.viewX(), vertexB.depth());
    float by = projectScreenY(vertexB.viewY(), vertexB.depth());
    float cx = projectScreenX(vertexC.viewX(), vertexC.depth());
    float cy = projectScreenY(vertexC.viewY(), vertexC.depth());
    float faceArea = Geometry.signedArea(ax, ay, bx, by, cx, cy);
    if (faceArea >= -0.01f) {
      return null;
    }
    return new ProjectedFace(
        ax,
        ay,
        vertexA.depth(),
        bx,
        by,
        vertexB.depth(),
        cx,
        cy,
        vertexC.depth(),
        vertexA.color(),
        vertexB.color(),
        vertexC.color(),
        vertexA.paletteShaded(),
        alpha,
        texture,
        priority,
        averageDepth,
        faceIndex,
        vertexA.textureU(),
        vertexA.textureV(),
        vertexB.textureU(),
        vertexB.textureV(),
        vertexC.textureU(),
        vertexC.textureV()
    );
  }

  private static float projectScreenX(float viewX, float depth) {
    return IconRenderConstants.ICON_CENTER_X
        + (int) (viewX * IconRenderConstants.INVENTORY_PROJECTION_SCALE / depth);
  }

  private static float projectScreenY(float viewY, float depth) {
    return IconRenderConstants.ICON_CENTER_Y
        + (int) (viewY * IconRenderConstants.INVENTORY_PROJECTION_SCALE / depth);
  }
}
