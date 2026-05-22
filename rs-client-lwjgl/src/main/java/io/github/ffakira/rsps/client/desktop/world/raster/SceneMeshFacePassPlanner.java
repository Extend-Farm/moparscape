package io.github.ffakira.rsps.client.desktop.world.raster;

final class SceneMeshFacePassPlanner {

  private final SceneMeshFacePassPlan reusablePlan = new SceneMeshFacePassPlan();

  // Legacy raster submission does not treat every face as one blended stream. The native backend
  // now mirrors that more closely by splitting opaque and translucent faces before execution, so
  // depth-tested opaque geometry does not get needlessly composited through the translucent path.
  SceneMeshFacePassPlan plan(SceneTriangleMesh mesh) {
    reusablePlan.clear();
    if (mesh == null || mesh.isEmpty()) {
      return reusablePlan;
    }
    reusablePlan.ensureCapacity(mesh.faceVertexA().length);
    for (int faceIndex = 0; faceIndex < mesh.faceVertexA().length; faceIndex++) {
      if (mesh.faceAlpha()[faceIndex] >= 255) {
        reusablePlan.appendOpaqueFace(faceIndex);
      } else {
        reusablePlan.appendTranslucentFace(faceIndex);
      }
    }
    return reusablePlan;
  }
}
