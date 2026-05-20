package io.github.ffakira.rsps.client.desktop.itemicon;

final class ReferenceTriangleRasterizer implements TriangleRasterizer {

  private final ReferenceTriangleWalker triangleWalker = new ReferenceTriangleWalker();
  private final ReferenceSpanRasterizer spanRasterizer = new ReferenceSpanRasterizer();

  @Override
  public void rasterize(ProjectedFace face, int[] pixels) {
    int yA = Math.round(face.ay());
    int yB = Math.round(face.by());
    int yC = Math.round(face.cy());
    int xA = Math.round(face.ax());
    int xB = Math.round(face.bx());
    int xC = Math.round(face.cx());
    int referenceAlpha = 255 - face.alpha();
    if (face.colorA() == face.colorB() && face.colorA() == face.colorC()) {
      int color = Palette.rgb(face.colorA());
      triangleWalker.drawTriangle(
          yA,
          yB,
          yC,
          xA,
          xB,
          xC,
          color,
          color,
          color,
          pixels,
          referenceAlpha,
          spanRasterizer::drawFlatSpan
      );
      return;
    }
    triangleWalker.drawTriangle(
        yA,
        yB,
        yC,
        xA,
        xB,
        xC,
        face.colorA() << 15,
        face.colorB() << 15,
        face.colorC() << 15,
        pixels,
        referenceAlpha,
        spanRasterizer::drawShadedSpan
    );
  }
}
