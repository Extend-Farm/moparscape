package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;

final class ApproximateTriangleRasterizer implements TriangleRasterizer {

  @Override
  public void rasterize(ProjectedFace face, int[] pixels) {
    TriangleSetup triangle = TriangleSetup.from(face);
    if (triangle == null) {
      return;
    }
    TriangleBounds bounds = TriangleBounds.from(triangle.a(), triangle.b(), triangle.c());
    if (bounds.isEmpty()) {
      return;
    }
    PixelShader shader = PixelShader.forFace(face);
    for (int pixelY = bounds.minY(); pixelY <= bounds.maxY(); pixelY++) {
      for (int pixelX = bounds.minX(); pixelX <= bounds.maxX(); pixelX++) {
        float sampleX = pixelX;
        float sampleY = pixelY;
        float edgeA = Geometry.signedArea(triangle.b().x(), triangle.b().y(), triangle.c().x(), triangle.c().y(), sampleX, sampleY);
        float edgeB = Geometry.signedArea(triangle.c().x(), triangle.c().y(), triangle.a().x(), triangle.a().y(), sampleX, sampleY);
        float edgeC = Geometry.signedArea(triangle.a().x(), triangle.a().y(), triangle.b().x(), triangle.b().y(), sampleX, sampleY);
        if (!Geometry.coversSample(edgeA, triangle.topLeftBC())
            || !Geometry.coversSample(edgeB, triangle.topLeftCA())
            || !Geometry.coversSample(edgeC, triangle.topLeftAB())) {
          continue;
        }
        float weightA = edgeA / triangle.area();
        float weightB = edgeB / triangle.area();
        float weightC = edgeC / triangle.area();
        ShadedPixel shadedPixel = shader.shade(weightA, weightB, weightC);
        if (shadedPixel.alpha() <= 0) {
          continue;
        }
        int pixelIndex = pixelY * IconRenderConstants.ICON_SIZE + pixelX;
        pixels[pixelIndex] = shadedPixel.alpha() >= 255
            ? shadedPixel.argb()
            : PixelBlender.blend(pixels[pixelIndex], shadedPixel.argb(), shadedPixel.alpha());
      }
    }
  }

  private static float perspectiveInterpolationDenominator(
      ScreenVertex a,
      ScreenVertex b,
      ScreenVertex c,
      float weightA,
      float weightB,
      float weightC
  ) {
    return weightA / a.z() + weightB / b.z() + weightC / c.z();
  }

  private static float perspectiveCorrectInterpolate(
      float valueA,
      float valueB,
      float valueC,
      ScreenVertex a,
      ScreenVertex b,
      ScreenVertex c,
      float weightA,
      float weightB,
      float weightC,
      float denominator
  ) {
    if (Math.abs(denominator) < 0.00001f) {
      return valueA * weightA + valueB * weightB + valueC * weightC;
    }
    return ((valueA * weightA) / a.z() + (valueB * weightB) / b.z() + (valueC * weightC) / c.z())
        / denominator;
  }

  private static float interpolateColorChannel(
      ScreenVertex a,
      ScreenVertex b,
      ScreenVertex c,
      int shift,
      float weightA,
      float weightB,
      float weightC
  ) {
    return ((a.color() >>> shift) & 0xff) * weightA
        + ((b.color() >>> shift) & 0xff) * weightB
        + ((c.color() >>> shift) & 0xff) * weightC;
  }

  private record TriangleSetup(
      ScreenVertex a,
      ScreenVertex b,
      ScreenVertex c,
      float area,
      boolean topLeftAB,
      boolean topLeftBC,
      boolean topLeftCA
  ) {

    private static TriangleSetup from(ProjectedFace face) {
      ScreenVertex a = face.a();
      ScreenVertex b = face.b();
      ScreenVertex c = face.c();
      float area = Geometry.signedArea(a.x(), a.y(), b.x(), b.y(), c.x(), c.y());
      if (Math.abs(area) < IconRenderConstants.EDGE_EPSILON) {
        return null;
      }
      if (area < 0.0f) {
        return fromVertices(a, c, b, -area);
      }
      return fromVertices(a, b, c, area);
    }

    private static TriangleSetup fromVertices(ScreenVertex a, ScreenVertex b, ScreenVertex c, float area) {
      return new TriangleSetup(
          a,
          b,
          c,
          area,
          Geometry.isTopLeftEdge(a.x(), a.y(), b.x(), b.y()),
          Geometry.isTopLeftEdge(b.x(), b.y(), c.x(), c.y()),
          Geometry.isTopLeftEdge(c.x(), c.y(), a.x(), a.y())
      );
    }
  }

  private record ShadedPixel(int argb, int alpha) {
  }

  private interface PixelShader {

    ShadedPixel shade(float weightA, float weightB, float weightC);

    static PixelShader forFace(ProjectedFace face) {
      return face.texture() != null
          ? new TexturedPixelShader(face)
          : face.paletteShaded()
          ? new PalettePixelShader(face)
          : new RgbPixelShader(face);
    }
  }

  private static final class TexturedPixelShader implements PixelShader {

    private final ProjectedFace face;
    private final ScreenVertex a;
    private final ScreenVertex b;
    private final ScreenVertex c;
    private final ArgbImage texture;

    private TexturedPixelShader(ProjectedFace face) {
      this.face = face;
      a = face.a();
      b = face.b();
      c = face.c();
      texture = face.texture();
    }

    @Override
    public ShadedPixel shade(float weightA, float weightB, float weightC) {
      float denominator = perspectiveInterpolationDenominator(a, b, c, weightA, weightB, weightC);
      float shadedRed = interpolateColorChannel(a, b, c, 16, weightA, weightB, weightC);
      float shadedGreen = interpolateColorChannel(a, b, c, 8, weightA, weightB, weightC);
      float shadedBlue = interpolateColorChannel(a, b, c, 0, weightA, weightB, weightC);
      float textureU = perspectiveCorrectInterpolate(
          a.textureU(),
          b.textureU(),
          c.textureU(),
          a,
          b,
          c,
          weightA,
          weightB,
          weightC,
          denominator
      );
      float textureV = perspectiveCorrectInterpolate(
          a.textureV(),
          b.textureV(),
          c.textureV(),
          a,
          b,
          c,
          weightA,
          weightB,
          weightC,
          denominator
      );
      int textureArgb = TextureSampler.sample(texture, textureU, textureV);
      int textureAlpha = (textureArgb >>> 24) & 0xff;
      if (textureAlpha <= 0) {
        return new ShadedPixel(0, 0);
      }
      int red = TextureSampler.modulate((textureArgb >>> 16) & 0xff, shadedRed);
      int green = TextureSampler.modulate((textureArgb >>> 8) & 0xff, shadedGreen);
      int blue = TextureSampler.modulate(textureArgb & 0xff, shadedBlue);
      int alpha = face.alpha() * textureAlpha / 255;
      return new ShadedPixel(0xff000000 | (red << 16) | (green << 8) | blue, alpha);
    }
  }

  private static final class PalettePixelShader implements PixelShader {

    private final ScreenVertex a;
    private final ScreenVertex b;
    private final ScreenVertex c;

    private PalettePixelShader(ProjectedFace face) {
      a = face.a();
      b = face.b();
      c = face.c();
    }

    @Override
    public ShadedPixel shade(float weightA, float weightB, float weightC) {
      int shadedHsl = MathUtil.clamp(
          Math.round(a.color() * weightA + b.color() * weightB + c.color() * weightC),
          0,
          0xffff
      );
      return new ShadedPixel(0xff000000 | Palette.rgb(shadedHsl), 255);
    }
  }

  private static final class RgbPixelShader implements PixelShader {

    private final ProjectedFace face;
    private final ScreenVertex a;
    private final ScreenVertex b;
    private final ScreenVertex c;

    private RgbPixelShader(ProjectedFace face) {
      this.face = face;
      a = face.a();
      b = face.b();
      c = face.c();
    }

    @Override
    public ShadedPixel shade(float weightA, float weightB, float weightC) {
      int red = MathUtil.clamp(Math.round(interpolateColorChannel(a, b, c, 16, weightA, weightB, weightC)), 0, 255);
      int green = MathUtil.clamp(Math.round(interpolateColorChannel(a, b, c, 8, weightA, weightB, weightC)), 0, 255);
      int blue = MathUtil.clamp(Math.round(interpolateColorChannel(a, b, c, 0, weightA, weightB, weightC)), 0, 255);
      return new ShadedPixel(0xff000000 | (red << 16) | (green << 8) | blue, face.alpha());
    }
  }
}
