package io.github.ffakira.rsps.client.desktop.itemicon;

import java.util.ArrayList;
import java.util.List;

final class NearPlaneClipper {

  private NearPlaneClipper() {
  }

  static List<ClippedVertex> clipTriangleToNearPlane(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC
  ) {
    ArrayList<ClippedVertex> inputVertices = new ArrayList<>(List.of(vertexA, vertexB, vertexC));
    ArrayList<ClippedVertex> clippedVertices = new ArrayList<>(4);
    ClippedVertex previousVertex = inputVertices.get(inputVertices.size() - 1);
    boolean previousInside = previousVertex.depth() > IconRenderConstants.NEAR_PLANE;
    for (ClippedVertex currentVertex : inputVertices) {
      boolean currentInside = currentVertex.depth() > IconRenderConstants.NEAR_PLANE;
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

  private static ClippedVertex interpolateNearPlane(ClippedVertex fromVertex, ClippedVertex toVertex) {
    float depthDelta = toVertex.depth() - fromVertex.depth();
    if (Math.abs(depthDelta) < 0.00001f) {
      return new ClippedVertex(
          fromVertex.viewX(),
          fromVertex.viewY(),
          IconRenderConstants.NEAR_PLANE,
          fromVertex.color(),
          fromVertex.textureU(),
          fromVertex.textureV(),
          fromVertex.paletteShaded()
      );
    }
    float interpolation = (IconRenderConstants.NEAR_PLANE - fromVertex.depth()) / depthDelta;
    return new ClippedVertex(
        MathUtil.lerp(fromVertex.viewX(), toVertex.viewX(), interpolation),
        MathUtil.lerp(fromVertex.viewY(), toVertex.viewY(), interpolation),
        IconRenderConstants.NEAR_PLANE,
        interpolateFaceValue(fromVertex.color(), toVertex.color(), interpolation, fromVertex.paletteShaded()),
        MathUtil.lerp(fromVertex.textureU(), toVertex.textureU(), interpolation),
        MathUtil.lerp(fromVertex.textureV(), toVertex.textureV(), interpolation),
        fromVertex.paletteShaded()
    );
  }

  private static int interpolateFaceValue(int fromColor, int toColor, float interpolation, boolean paletteShaded) {
    if (paletteShaded) {
      return Math.round(MathUtil.lerp(fromColor, toColor, interpolation));
    }
    return interpolateColor(fromColor, toColor, interpolation);
  }

  private static int interpolateColor(int fromColor, int toColor, float interpolation) {
    int red = Math.round(MathUtil.lerp((fromColor >>> 16) & 0xff, (toColor >>> 16) & 0xff, interpolation));
    int green = Math.round(MathUtil.lerp((fromColor >>> 8) & 0xff, (toColor >>> 8) & 0xff, interpolation));
    int blue = Math.round(MathUtil.lerp(fromColor & 0xff, toColor & 0xff, interpolation));
    return (red << 16) | (green << 8) | blue;
  }
}
