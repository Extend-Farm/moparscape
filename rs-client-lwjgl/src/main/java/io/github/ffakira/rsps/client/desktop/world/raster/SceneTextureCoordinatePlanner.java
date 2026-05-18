package io.github.ffakira.rsps.client.desktop.world.raster;

final class SceneTextureCoordinatePlanner {

  float[] plan(SceneTriangleMesh mesh, int faceIndex) {
    float[] explicitCoordinates = explicitCoordinates(mesh, faceIndex);
    if (explicitCoordinates != null) {
      return explicitCoordinates;
    }
    return planarFallback(mesh, faceIndex);
  }

  private float[] explicitCoordinates(SceneTriangleMesh mesh, int faceIndex) {
    int textureVertexA = mesh.textureVertexA()[faceIndex];
    int textureVertexB = mesh.textureVertexB()[faceIndex];
    int textureVertexC = mesh.textureVertexC()[faceIndex];
    if (textureVertexA < 0 || textureVertexB < 0 || textureVertexC < 0) {
      return null;
    }

    float[] anchorA = vertex(mesh, textureVertexA);
    float[] anchorB = vertex(mesh, textureVertexB);
    float[] anchorC = vertex(mesh, textureVertexC);
    float[] uvA = anchoredUv(anchorA, anchorB, anchorC, vertex(mesh, mesh.faceVertexA()[faceIndex]));
    float[] uvB = anchoredUv(anchorA, anchorB, anchorC, vertex(mesh, mesh.faceVertexB()[faceIndex]));
    float[] uvC = anchoredUv(anchorA, anchorB, anchorC, vertex(mesh, mesh.faceVertexC()[faceIndex]));
    if (uvA == null || uvB == null || uvC == null) {
      return null;
    }
    return new float[]{uvA[0], uvA[1], uvB[0], uvB[1], uvC[0], uvC[1]};
  }

  private float[] planarFallback(SceneTriangleMesh mesh, int faceIndex) {
    int vertexAIndex = mesh.faceVertexA()[faceIndex];
    int vertexBIndex = mesh.faceVertexB()[faceIndex];
    int vertexCIndex = mesh.faceVertexC()[faceIndex];
    float[] vertexA = vertex(mesh, vertexAIndex);
    float[] vertexB = vertex(mesh, vertexBIndex);
    float[] vertexC = vertex(mesh, vertexCIndex);

    float edgeOneX = vertexB[0] - vertexA[0];
    float edgeOneY = vertexB[1] - vertexA[1];
    float edgeOneZ = vertexB[2] - vertexA[2];
    float edgeTwoX = vertexC[0] - vertexA[0];
    float edgeTwoY = vertexC[1] - vertexA[1];
    float edgeTwoZ = vertexC[2] - vertexA[2];
    float normalX = edgeOneY * edgeTwoZ - edgeOneZ * edgeTwoY;
    float normalY = edgeOneZ * edgeTwoX - edgeOneX * edgeTwoZ;
    float normalZ = edgeOneX * edgeTwoY - edgeOneY * edgeTwoX;

    ProjectionAxis projectionAxis = dominantAxis(normalX, normalY, normalZ);
    float[] uvA = projectedUv(vertexA, projectionAxis, normalX, normalY, normalZ);
    float[] uvB = projectedUv(vertexB, projectionAxis, normalX, normalY, normalZ);
    float[] uvC = projectedUv(vertexC, projectionAxis, normalX, normalY, normalZ);
    return new float[]{uvA[0], uvA[1], uvB[0], uvB[1], uvC[0], uvC[1]};
  }

  private float[] anchoredUv(float[] anchorA, float[] anchorB, float[] anchorC, float[] point) {
    float abX = anchorB[0] - anchorA[0];
    float abY = anchorB[1] - anchorA[1];
    float abZ = anchorB[2] - anchorA[2];
    float acX = anchorC[0] - anchorA[0];
    float acY = anchorC[1] - anchorA[1];
    float acZ = anchorC[2] - anchorA[2];
    float apX = point[0] - anchorA[0];
    float apY = point[1] - anchorA[1];
    float apZ = point[2] - anchorA[2];

    float dotABAB = dot(abX, abY, abZ, abX, abY, abZ);
    float dotABAC = dot(abX, abY, abZ, acX, acY, acZ);
    float dotACAC = dot(acX, acY, acZ, acX, acY, acZ);
    float dotAPAB = dot(apX, apY, apZ, abX, abY, abZ);
    float dotAPAC = dot(apX, apY, apZ, acX, acY, acZ);
    float denominator = dotABAB * dotACAC - dotABAC * dotABAC;
    if (Math.abs(denominator) < 0.00001f) {
      return null;
    }
    float u = (dotACAC * dotAPAB - dotABAC * dotAPAC) / denominator;
    float v = (dotABAB * dotAPAC - dotABAC * dotAPAB) / denominator;
    return new float[]{u, v};
  }

  private float[] projectedUv(
      float[] vertex,
      ProjectionAxis projectionAxis,
      float normalX,
      float normalY,
      float normalZ
  ) {
    return switch (projectionAxis) {
      case X -> new float[]{
          normalX >= 0.0f ? vertex[2] : -vertex[2],
          vertex[1]
      };
      case Y -> new float[]{
          vertex[0],
          normalY >= 0.0f ? vertex[2] : -vertex[2]
      };
      case Z -> new float[]{
          normalZ >= 0.0f ? vertex[0] : -vertex[0],
          vertex[1]
      };
    };
  }

  private ProjectionAxis dominantAxis(float normalX, float normalY, float normalZ) {
    float absX = Math.abs(normalX);
    float absY = Math.abs(normalY);
    float absZ = Math.abs(normalZ);
    if (absY >= absX && absY >= absZ) {
      return ProjectionAxis.Y;
    }
    if (absX >= absZ) {
      return ProjectionAxis.X;
    }
    return ProjectionAxis.Z;
  }

  private float dot(float ax, float ay, float az, float bx, float by, float bz) {
    return ax * bx + ay * by + az * bz;
  }

  private float[] vertex(SceneTriangleMesh mesh, int vertexIndex) {
    return new float[]{
        mesh.vertexX()[vertexIndex],
        mesh.vertexY()[vertexIndex],
        mesh.vertexZ()[vertexIndex]
    };
  }

  private enum ProjectionAxis {
    X,
    Y,
    Z
  }
}
