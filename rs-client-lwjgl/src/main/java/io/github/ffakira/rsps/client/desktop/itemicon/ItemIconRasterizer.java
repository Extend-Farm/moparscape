package io.github.ffakira.rsps.client.desktop.itemicon;

import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ItemIconRasterizer {

  private static final int ICON_SIZE = 32;
  private static final float ICON_CENTER = ICON_SIZE * 0.5f;
  private static final float INVENTORY_PROJECTION_SCALE = 512.0f;
  private static final float NEAR_PLANE = 1.0f;
  private static final int OUTLINE_RGB = 0x302020;

  private ItemIconRasterizer() {
  }

  static List<ClippedVertex> clipTriangleToNearPlane(ClippedVertex vertexA, ClippedVertex vertexB, ClippedVertex vertexC) {
    ArrayList<ClippedVertex> inputVertices = new ArrayList<>(List.of(vertexA, vertexB, vertexC));
    ArrayList<ClippedVertex> clippedVertices = new ArrayList<>(4);
    ClippedVertex previousVertex = inputVertices.get(inputVertices.size() - 1);
    boolean previousInside = previousVertex.depth() > NEAR_PLANE;
    for (ClippedVertex currentVertex : inputVertices) {
      boolean currentInside = currentVertex.depth() > NEAR_PLANE;
      if (currentInside != previousInside) {
        clippedVertices.add(interpolateNearPlane(previousVertex, currentVertex));
      }
      if (currentInside) {
        clippedVertices.add(currentVertex);
      }
      previousVertex = currentVertex;
      previousInside = currentInside;
    }
    return clippedVertices;
  }

  static ProjectedFace projectedFace(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC,
      int alpha,
      ArgbImage texture
  ) {
    if (vertexA.depth() <= NEAR_PLANE || vertexB.depth() <= NEAR_PLANE || vertexC.depth() <= NEAR_PLANE) {
      return null;
    }
    float ax = projectScreenX(vertexA.viewX(), vertexA.depth());
    float ay = projectScreenY(vertexA.viewY(), vertexA.depth());
    float bx = projectScreenX(vertexB.viewX(), vertexB.depth());
    float by = projectScreenY(vertexB.viewY(), vertexB.depth());
    float cx = projectScreenX(vertexC.viewX(), vertexC.depth());
    float cy = projectScreenY(vertexC.viewY(), vertexC.depth());
    float faceArea = signedArea(ax, ay, bx, by, cx, cy);
    if (Math.abs(faceArea) < 0.01f) {
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
        alpha,
        texture,
        vertexA.textureU(),
        vertexA.textureV(),
        vertexB.textureU(),
        vertexB.textureV(),
        vertexC.textureU(),
        vertexC.textureV()
    );
  }

  static ArgbImage rasterize(List<ProjectedFace> projectedFaces) {
    int[] pixels = new int[ICON_SIZE * ICON_SIZE];
    float[] depthBuffer = new float[ICON_SIZE * ICON_SIZE];
    Arrays.fill(depthBuffer, Float.POSITIVE_INFINITY);
    for (ProjectedFace face : projectedFaces) {
      rasterizeFace(face, pixels, depthBuffer);
    }
    applyOutline(pixels);
    return new ArgbImage(ICON_SIZE, ICON_SIZE, pixels);
  }

  private static ClippedVertex interpolateNearPlane(ClippedVertex fromVertex, ClippedVertex toVertex) {
    float depthDelta = toVertex.depth() - fromVertex.depth();
    if (Math.abs(depthDelta) < 0.00001f) {
      return new ClippedVertex(
          fromVertex.viewX(),
          fromVertex.viewY(),
          NEAR_PLANE,
          fromVertex.color(),
          fromVertex.textureU(),
          fromVertex.textureV()
      );
    }
    float interpolation = (NEAR_PLANE - fromVertex.depth()) / depthDelta;
    return new ClippedVertex(
        lerp(fromVertex.viewX(), toVertex.viewX(), interpolation),
        lerp(fromVertex.viewY(), toVertex.viewY(), interpolation),
        NEAR_PLANE,
        interpolateColor(fromVertex.color(), toVertex.color(), interpolation),
        lerp(fromVertex.textureU(), toVertex.textureU(), interpolation),
        lerp(fromVertex.textureV(), toVertex.textureV(), interpolation)
    );
  }

  private static void rasterizeFace(ProjectedFace face, int[] pixels, float[] depthBuffer) {
    int minX = clamp((int) Math.floor(Math.min(face.ax(), Math.min(face.bx(), face.cx()))), 0, ICON_SIZE - 1);
    int maxX = clamp((int) Math.ceil(Math.max(face.ax(), Math.max(face.bx(), face.cx()))), 0, ICON_SIZE - 1);
    int minY = clamp((int) Math.floor(Math.min(face.ay(), Math.min(face.by(), face.cy()))), 0, ICON_SIZE - 1);
    int maxY = clamp((int) Math.ceil(Math.max(face.ay(), Math.max(face.by(), face.cy()))), 0, ICON_SIZE - 1);
    float area = signedArea(face.ax(), face.ay(), face.bx(), face.by(), face.cx(), face.cy());
    if (Math.abs(area) < 0.0001f) {
      return;
    }
    for (int pixelY = minY; pixelY <= maxY; pixelY++) {
      for (int pixelX = minX; pixelX <= maxX; pixelX++) {
        float sampleX = pixelX + 0.5f;
        float sampleY = pixelY + 0.5f;
        float weightA = signedArea(sampleX, sampleY, face.bx(), face.by(), face.cx(), face.cy()) / area;
        float weightB = signedArea(face.ax(), face.ay(), sampleX, sampleY, face.cx(), face.cy()) / area;
        float weightC = 1.0f - weightA - weightB;
        if (weightA < -0.001f || weightB < -0.001f || weightC < -0.001f) {
          continue;
        }
        float depth = face.az() * weightA + face.bz() * weightB + face.cz() * weightC;
        int pixelIndex = pixelY * ICON_SIZE + pixelX;
        if (depth >= depthBuffer[pixelIndex]) {
          continue;
        }
        depthBuffer[pixelIndex] = depth;
        float denominator = perspectiveInterpolationDenominator(face, weightA, weightB, weightC);
        float shadedRed = interpolateColorChannel(face, 16, weightA, weightB, weightC);
        float shadedGreen = interpolateColorChannel(face, 8, weightA, weightB, weightC);
        float shadedBlue = interpolateColorChannel(face, 0, weightA, weightB, weightC);
        int sourceAlpha = face.alpha();
        int red;
        int green;
        int blue;
        if (face.texture() != null) {
          float textureU = perspectiveCorrectInterpolate(
              face.textureUa(),
              face.textureUb(),
              face.textureUc(),
              face,
              weightA,
              weightB,
              weightC,
              denominator
          );
          float textureV = perspectiveCorrectInterpolate(
              face.textureVa(),
              face.textureVb(),
              face.textureVc(),
              face,
              weightA,
              weightB,
              weightC,
              denominator
          );
          int textureArgb = sampleTexture(face.texture(), textureU, textureV);
          int textureAlpha = (textureArgb >>> 24) & 0xff;
          if (textureAlpha <= 0) {
            continue;
          }
          red = modulate((textureArgb >>> 16) & 0xff, shadedRed);
          green = modulate((textureArgb >>> 8) & 0xff, shadedGreen);
          blue = modulate(textureArgb & 0xff, shadedBlue);
          sourceAlpha = sourceAlpha * textureAlpha / 255;
        } else {
          red = clamp(Math.round(shadedRed), 0, 255);
          green = clamp(Math.round(shadedGreen), 0, 255);
          blue = clamp(Math.round(shadedBlue), 0, 255);
        }
        if (sourceAlpha <= 0) {
          continue;
        }
        int argb = 0xff000000 | (red << 16) | (green << 8) | blue;
        pixels[pixelIndex] = sourceAlpha >= 255
            ? argb
            : blend(pixels[pixelIndex], argb, sourceAlpha);
      }
    }
  }

  private static void applyOutline(int[] pixels) {
    int[] outlinedPixels = pixels.clone();
    for (int pixelY = 1; pixelY < ICON_SIZE - 1; pixelY++) {
      for (int pixelX = 1; pixelX < ICON_SIZE - 1; pixelX++) {
        int pixelIndex = pixelY * ICON_SIZE + pixelX;
        if (pixels[pixelIndex] != 0) {
          continue;
        }
        if (pixels[pixelIndex - 1] != 0
            || pixels[pixelIndex + 1] != 0
            || pixels[pixelIndex - ICON_SIZE] != 0
            || pixels[pixelIndex + ICON_SIZE] != 0) {
          outlinedPixels[pixelIndex] = 0xff000000 | OUTLINE_RGB;
        }
      }
    }
    System.arraycopy(outlinedPixels, 0, pixels, 0, pixels.length);
  }

  private static float projectScreenX(float viewX, float depth) {
    return ICON_CENTER + viewX * INVENTORY_PROJECTION_SCALE / depth;
  }

  private static float projectScreenY(float viewY, float depth) {
    return ICON_CENTER + viewY * INVENTORY_PROJECTION_SCALE / depth;
  }

  private static float perspectiveInterpolationDenominator(ProjectedFace face, float weightA, float weightB, float weightC) {
    return weightA / face.az() + weightB / face.bz() + weightC / face.cz();
  }

  private static float perspectiveCorrectInterpolate(
      float valueA,
      float valueB,
      float valueC,
      ProjectedFace face,
      float weightA,
      float weightB,
      float weightC,
      float denominator
  ) {
    if (Math.abs(denominator) < 0.00001f) {
      return valueA * weightA + valueB * weightB + valueC * weightC;
    }
    return ((valueA * weightA) / face.az() + (valueB * weightB) / face.bz() + (valueC * weightC) / face.cz())
        / denominator;
  }

  private static float interpolateColorChannel(ProjectedFace face, int shift, float weightA, float weightB, float weightC) {
    return ((face.colorA() >>> shift) & 0xff) * weightA
        + ((face.colorB() >>> shift) & 0xff) * weightB
        + ((face.colorC() >>> shift) & 0xff) * weightC;
  }

  private static int sampleTexture(ArgbImage texture, float u, float v) {
    float clampedU = clamp(u, 0.0f, 0.9999f);
    float clampedV = clamp(v, 0.0f, 0.9999f);
    int sampleX = clamp((int) (clampedU * texture.width()), 0, texture.width() - 1);
    int sampleY = clamp((int) (clampedV * texture.height()), 0, texture.height() - 1);
    return texture.pixels()[sampleY * texture.width() + sampleX];
  }

  private static int modulate(int textureChannel, float lightChannel) {
    return clamp(Math.round(textureChannel * clamp(lightChannel, 0.0f, 255.0f) / 255.0f), 0, 255);
  }

  private static int blend(int destinationArgb, int sourceArgb, int sourceAlpha) {
    int inverseAlpha = 255 - sourceAlpha;
    int destinationRed = (destinationArgb >>> 16) & 0xff;
    int destinationGreen = (destinationArgb >>> 8) & 0xff;
    int destinationBlue = destinationArgb & 0xff;
    int sourceRed = (sourceArgb >>> 16) & 0xff;
    int sourceGreen = (sourceArgb >>> 8) & 0xff;
    int sourceBlue = sourceArgb & 0xff;
    int red = (sourceRed * sourceAlpha + destinationRed * inverseAlpha) / 255;
    int green = (sourceGreen * sourceAlpha + destinationGreen * inverseAlpha) / 255;
    int blue = (sourceBlue * sourceAlpha + destinationBlue * inverseAlpha) / 255;
    return 0xff000000 | (red << 16) | (green << 8) | blue;
  }

  private static float signedArea(float ax, float ay, float bx, float by, float cx, float cy) {
    return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax);
  }

  private static float lerp(float from, float to, float interpolation) {
    return from + (to - from) * interpolation;
  }

  private static int interpolateColor(int fromColor, int toColor, float interpolation) {
    int red = Math.round(lerp((fromColor >>> 16) & 0xff, (toColor >>> 16) & 0xff, interpolation));
    int green = Math.round(lerp((fromColor >>> 8) & 0xff, (toColor >>> 8) & 0xff, interpolation));
    int blue = Math.round(lerp(fromColor & 0xff, toColor & 0xff, interpolation));
    return (red << 16) | (green << 8) | blue;
  }

  private static int clamp(int value, int minimum, int maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private static float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  static record ProjectedFace(
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
      int alpha,
      ArgbImage texture,
      float textureUa,
      float textureVa,
      float textureUb,
      float textureVb,
      float textureUc,
      float textureVc
  ) {
  }

  static record ClippedVertex(
      float viewX,
      float viewY,
      float depth,
      int color,
      float textureU,
      float textureV
  ) {
  }
}
