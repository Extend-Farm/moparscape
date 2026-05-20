package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

record ProjectedFace(
    float ax,
    float ay,
    float az,
    float bx,
    float by,
    float bz,
    float cx,
    float cy,
    float cz,
    int colorA,
    int colorB,
    int colorC,
    boolean paletteShaded,
    int alpha,
    ArgbImage texture,
    int priority,
    float averageDepth,
    int faceIndex,
    float textureUa,
    float textureVa,
    float textureUb,
    float textureVb,
    float textureUc,
    float textureVc
) {

  ScreenVertex a() {
    return new ScreenVertex(ax, ay, az, colorA, textureUa, textureVa);
  }

  ScreenVertex b() {
    return new ScreenVertex(bx, by, bz, colorB, textureUb, textureVb);
  }

  ScreenVertex c() {
    return new ScreenVertex(cx, cy, cz, colorC, textureUc, textureVc);
  }
}
