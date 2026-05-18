package io.github.ffakira.rsps.client.lwjgl;

import java.util.Arrays;

final class SceneMeshFacePassPlanner {

  // Legacy raster submission does not treat every face as one blended stream. The native backend
  // now mirrors that more closely by splitting opaque and translucent faces before execution, so
  // depth-tested opaque geometry does not get needlessly composited through the translucent path.
  SceneMeshFacePassPlan plan(SceneTriangleMesh mesh) {
    if (mesh == null || mesh.isEmpty()) {
      return new SceneMeshFacePassPlan(new int[0], new int[0]);
    }
    int[] opaqueFaces = new int[mesh.faceVertexA().length];
    int[] translucentFaces = new int[mesh.faceVertexA().length];
    int opaqueCount = 0;
    int translucentCount = 0;
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      if (mesh.faceAlpha()[faceIndex] >= 255) {
        opaqueFaces[opaqueCount++] = faceIndex;
      } else {
        translucentFaces[translucentCount++] = faceIndex;
      }
    }
    return new SceneMeshFacePassPlan(
        Arrays.copyOf(opaqueFaces, opaqueCount),
        Arrays.copyOf(translucentFaces, translucentCount)
    );
  }
}
