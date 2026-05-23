package com.veyrmoor.client.desktop.itemicon;

import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import java.util.List;

final class ItemIconRasterizer {

  private static final FaceSorter FACE_SORTER = new FaceSorter();
  private static final TriangleRasterizer REFERENCE_TRIANGLE_RASTERIZER = new ReferenceTriangleRasterizer();
  private static final TriangleRasterizer APPROXIMATE_TRIANGLE_RASTERIZER = new ApproximateTriangleRasterizer();
  private static final IconPostProcessor POST_PROCESSOR = new IconPostProcessor();

  private ItemIconRasterizer() {
  }

  static List<ClippedVertex> clipTriangleToNearPlane(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC
  ) {
    return NearPlaneClipper.clipTriangleToNearPlane(vertexA, vertexB, vertexC);
  }

  static ProjectedFace projectedFace(
      ClippedVertex vertexA,
      ClippedVertex vertexB,
      ClippedVertex vertexC,
      int alpha,
      ArgbImage texture,
      int priority,
      float averageDepth,
      int faceIndex
  ) {
    return FaceProjector.projectFace(vertexA, vertexB, vertexC, alpha, texture, priority, averageDepth, faceIndex);
  }

  static ArgbImage rasterize(List<ProjectedFace> projectedFaces) {
    int[] pixels = new int[IconRenderConstants.PIXEL_COUNT];
    for (ProjectedFace face : FACE_SORTER.sort(projectedFaces)) {
      selectRasterizer(face).rasterize(face, pixels);
    }
    POST_PROCESSOR.apply(pixels);
    return new ArgbImage(IconRenderConstants.ICON_SIZE, IconRenderConstants.ICON_SIZE, pixels);
  }

  static void applyInventoryPostProcess(int[] pixels) {
    POST_PROCESSOR.apply(pixels);
  }

  private static TriangleRasterizer selectRasterizer(ProjectedFace face) {
    // Untextured palette faces follow the classic scanline sprite path; textured faces still use
    // the native approximate path until the cache-era textured spans are ported.
    return face.texture() == null && face.paletteShaded()
        ? REFERENCE_TRIANGLE_RASTERIZER
        : APPROXIMATE_TRIANGLE_RASTERIZER;
  }
}
