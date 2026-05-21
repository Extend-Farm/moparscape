package io.github.ffakira.rsps.client.desktop.world.raster;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import java.util.Arrays;

public final class SceneTriangleMeshBuilder {

  private float[] vertexX = new float[256];
  private float[] vertexY = new float[256];
  private float[] vertexZ = new float[256];
  private int vertexCount;

  private int[] faceVertexA = new int[256];
  private int[] faceVertexB = new int[256];
  private int[] faceVertexC = new int[256];
  private int[] faceColorA = new int[256];
  private int[] faceColorB = new int[256];
  private int[] faceColorC = new int[256];
  private int[] faceAlpha = new int[256];
  private int[] faceTextureIds = new int[256];
  private int[] textureVertexA = new int[256];
  private int[] textureVertexB = new int[256];
  private int[] textureVertexC = new int[256];
  private int[] facePriorities = new int[256];
  private int faceCount;

  public int addVertex(float x, float y, float z) {
    ensureVertexCapacity(vertexCount + 1);
    vertexX[vertexCount] = x;
    vertexY[vertexCount] = y;
    vertexZ[vertexCount] = z;
    return vertexCount++;
  }

  public int addVertexPoint(
      float tileX,
      float tileY,
      int pointCode,
      float heightNorthWest,
      float heightNorthEast,
      float heightSouthEast,
      float heightSouthWest
  ) {
    return switch (pointCode) {
      case 1 -> addVertex(tileX, heightNorthWest, tileY);
      case 2 -> addVertex(tileX + 0.5f, midpoint(heightNorthWest, heightNorthEast), tileY);
      case 3 -> addVertex(tileX + 1.0f, heightNorthEast, tileY);
      case 4 -> addVertex(tileX + 1.0f, midpoint(heightNorthEast, heightSouthEast), tileY + 0.5f);
      case 5 -> addVertex(tileX + 1.0f, heightSouthEast, tileY + 1.0f);
      case 6 -> addVertex(tileX + 0.5f, midpoint(heightSouthEast, heightSouthWest), tileY + 1.0f);
      case 7 -> addVertex(tileX, heightSouthWest, tileY + 1.0f);
      case 8 -> addVertex(tileX, midpoint(heightSouthWest, heightNorthWest), tileY + 0.5f);
      case 9 -> addVertex(tileX + 0.5f, midpoint(heightNorthWest, heightNorthEast), tileY + 0.25f);
      case 10 -> addVertex(tileX + 0.75f, midpoint(heightNorthEast, heightSouthEast), tileY + 0.5f);
      case 11 -> addVertex(tileX + 0.5f, midpoint(heightSouthEast, heightSouthWest), tileY + 0.75f);
      case 12 -> addVertex(tileX + 0.25f, midpoint(heightSouthWest, heightNorthWest), tileY + 0.5f);
      case 13 -> addVertex(tileX + 0.25f, heightNorthWest, tileY + 0.25f);
      case 14 -> addVertex(tileX + 0.75f, heightNorthEast, tileY + 0.25f);
      case 15 -> addVertex(tileX + 0.75f, heightSouthEast, tileY + 0.75f);
      case 16 -> addVertex(tileX + 0.25f, heightSouthWest, tileY + 0.75f);
      default -> addVertex(tileX + 0.5f, midpoint(heightNorthWest, heightSouthEast), tileY + 0.5f);
    };
  }

  public void addTriangle(int a, int b, int c, int rgb) {
    addTriangle(a, b, c, rgb, rgb, rgb, 255, -1, -1, -1, -1, 0);
  }

  public void addTriangle(int a, int b, int c, int rgbA, int rgbB, int rgbC) {
    addTriangle(a, b, c, rgbA, rgbB, rgbC, 255, -1, -1, -1, -1, 0);
  }

  public void addTriangle(
      int a,
      int b,
      int c,
      int rgbA,
      int rgbB,
      int rgbC,
      int alpha,
      int textureId,
      int textureA,
      int textureB,
      int textureC
  ) {
    addTriangle(a, b, c, rgbA, rgbB, rgbC, alpha, textureId, textureA, textureB, textureC, 0);
  }

  public void addTriangle(
      int a,
      int b,
      int c,
      int rgbA,
      int rgbB,
      int rgbC,
      int alpha,
      int textureId,
      int textureA,
      int textureB,
      int textureC,
      int facePriority
  ) {
    ensureFaceCapacity(faceCount + 1);
    faceVertexA[faceCount] = a;
    faceVertexB[faceCount] = b;
    faceVertexC[faceCount] = c;
    faceColorA[faceCount] = rgbA;
    faceColorB[faceCount] = rgbB;
    faceColorC[faceCount] = rgbC;
    faceAlpha[faceCount] = alpha;
    faceTextureIds[faceCount] = textureId;
    textureVertexA[faceCount] = textureA;
    textureVertexB[faceCount] = textureB;
    textureVertexC[faceCount] = textureC;
    facePriorities[faceCount] = facePriority;
    faceCount++;
  }

  public void addQuad(
      float ax, float ay, float az,
      float bx, float by, float bz,
      float cx, float cy, float cz,
      float dx, float dy, float dz,
      int rgb
  ) {
    addQuad(ax, ay, az, bx, by, bz, cx, cy, cz, dx, dy, dz, rgb, rgb, rgb, rgb);
  }

  public void addQuad(
      float ax, float ay, float az,
      float bx, float by, float bz,
      float cx, float cy, float cz,
      float dx, float dy, float dz,
      int rgbA,
      int rgbB,
      int rgbC,
      int rgbD
  ) {
    int a = addVertex(ax, ay, az);
    int b = addVertex(bx, by, bz);
    int c = addVertex(cx, cy, cz);
    int d = addVertex(dx, dy, dz);
    addTriangle(a, b, c, rgbA, rgbB, rgbC);
    addTriangle(a, c, d, rgbA, rgbC, rgbD);
  }

  public void addMesh(SceneTriangleMesh mesh, float offsetX, float offsetY, float offsetZ) {
    if (mesh == null || mesh.isEmpty()) {
      return;
    }
    int baseVertexIndex = vertexCount;
    ensureVertexCapacity(vertexCount + mesh.vertexX().length);
    for (int index = 0; index < mesh.vertexX().length; index++) {
      vertexX[vertexCount] = mesh.vertexX()[index] + offsetX;
      vertexY[vertexCount] = mesh.vertexY()[index] + offsetY;
      vertexZ[vertexCount] = mesh.vertexZ()[index] + offsetZ;
      vertexCount++;
    }
    ensureFaceCapacity(faceCount + mesh.faceVertexA().length);
    for (int index = 0; index < mesh.faceVertexA().length; index++) {
      faceVertexA[faceCount] = baseVertexIndex + mesh.faceVertexA()[index];
      faceVertexB[faceCount] = baseVertexIndex + mesh.faceVertexB()[index];
      faceVertexC[faceCount] = baseVertexIndex + mesh.faceVertexC()[index];
      faceColorA[faceCount] = mesh.faceColorA()[index];
      faceColorB[faceCount] = mesh.faceColorB()[index];
      faceColorC[faceCount] = mesh.faceColorC()[index];
      faceAlpha[faceCount] = mesh.faceAlpha()[index];
      faceTextureIds[faceCount] = mesh.faceTextureIds()[index];
      textureVertexA[faceCount] = mesh.textureVertexA()[index] < 0 ? -1 : baseVertexIndex + mesh.textureVertexA()[index];
      textureVertexB[faceCount] = mesh.textureVertexB()[index] < 0 ? -1 : baseVertexIndex + mesh.textureVertexB()[index];
      textureVertexC[faceCount] = mesh.textureVertexC()[index] < 0 ? -1 : baseVertexIndex + mesh.textureVertexC()[index];
      facePriorities[faceCount] = mesh.facePriorities()[index];
      faceCount++;
    }
  }

  public void addGeometry(WorldSceneObjectGeometry geometry, float offsetX, float offsetY, float offsetZ) {
    addGeometry(geometry, offsetX, offsetY, offsetZ, null);
  }

  public void addGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ,
      float yawDegrees,
      SceneRasterMode rasterModeFilter
  ) {
    if (geometry == null || geometry.faceVertexA().length == 0) {
      return;
    }
    int baseVertexIndex = vertexCount;
    ensureVertexCapacity(vertexCount + geometry.vertexX().length);
    float radians = (float) Math.toRadians(yawDegrees);
    float sine = (float) Math.sin(radians);
    float cosine = (float) Math.cos(radians);
    for (int index = 0; index < geometry.vertexX().length; index++) {
      float localX = geometry.vertexX()[index];
      float localZ = geometry.vertexZ()[index];
      vertexX[vertexCount] = localX * cosine + localZ * sine + offsetX;
      vertexY[vertexCount] = geometry.vertexY()[index] + offsetY;
      vertexZ[vertexCount] = localZ * cosine - localX * sine + offsetZ;
      vertexCount++;
    }
    ensureFaceCapacity(faceCount + geometry.faceVertexA().length);
    for (int index = 0; index < geometry.faceVertexA().length; index++) {
      if (rasterModeFilter != null && geometry.faceRasterModes()[index] != rasterModeFilter) {
        continue;
      }
      faceVertexA[faceCount] = baseVertexIndex + geometry.faceVertexA()[index];
      faceVertexB[faceCount] = baseVertexIndex + geometry.faceVertexB()[index];
      faceVertexC[faceCount] = baseVertexIndex + geometry.faceVertexC()[index];
      faceColorA[faceCount] = geometry.faceColorA()[index];
      faceColorB[faceCount] = geometry.faceColorB()[index];
      faceColorC[faceCount] = geometry.faceColorC()[index];
      faceAlpha[faceCount] = geometry.faceAlpha()[index];
      faceTextureIds[faceCount] = geometry.faceTextureIds()[index];
      textureVertexA[faceCount] = geometry.textureVertexA()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexA()[index];
      textureVertexB[faceCount] = geometry.textureVertexB()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexB()[index];
      textureVertexC[faceCount] = geometry.textureVertexC()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexC()[index];
      facePriorities[faceCount] = geometry.facePriorities()[index];
      faceCount++;
    }
  }

  public void addGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ,
      SceneRasterMode rasterModeFilter
  ) {
    if (geometry == null || geometry.faceVertexA().length == 0) {
      return;
    }
    int baseVertexIndex = vertexCount;
    ensureVertexCapacity(vertexCount + geometry.vertexX().length);
    for (int index = 0; index < geometry.vertexX().length; index++) {
      vertexX[vertexCount] = geometry.vertexX()[index] + offsetX;
      vertexY[vertexCount] = geometry.vertexY()[index] + offsetY;
      vertexZ[vertexCount] = geometry.vertexZ()[index] + offsetZ;
      vertexCount++;
    }
    ensureFaceCapacity(faceCount + geometry.faceVertexA().length);
    for (int index = 0; index < geometry.faceVertexA().length; index++) {
      if (rasterModeFilter != null && geometry.faceRasterModes()[index] != rasterModeFilter) {
        continue;
      }
      faceVertexA[faceCount] = baseVertexIndex + geometry.faceVertexA()[index];
      faceVertexB[faceCount] = baseVertexIndex + geometry.faceVertexB()[index];
      faceVertexC[faceCount] = baseVertexIndex + geometry.faceVertexC()[index];
      faceColorA[faceCount] = geometry.faceColorA()[index];
      faceColorB[faceCount] = geometry.faceColorB()[index];
      faceColorC[faceCount] = geometry.faceColorC()[index];
      faceAlpha[faceCount] = geometry.faceAlpha()[index];
      faceTextureIds[faceCount] = geometry.faceTextureIds()[index];
      textureVertexA[faceCount] = geometry.textureVertexA()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexA()[index];
      textureVertexB[faceCount] = geometry.textureVertexB()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexB()[index];
      textureVertexC[faceCount] = geometry.textureVertexC()[index] < 0 ? -1 : baseVertexIndex + geometry.textureVertexC()[index];
      facePriorities[faceCount] = geometry.facePriorities()[index];
      faceCount++;
    }
  }

  public SceneTriangleMesh build() {
    if (faceCount == 0) {
      return SceneTriangleMesh.EMPTY;
    }
    return new SceneTriangleMesh(
        Arrays.copyOf(vertexX, vertexCount),
        Arrays.copyOf(vertexY, vertexCount),
        Arrays.copyOf(vertexZ, vertexCount),
        Arrays.copyOf(faceVertexA, faceCount),
        Arrays.copyOf(faceVertexB, faceCount),
        Arrays.copyOf(faceVertexC, faceCount),
        Arrays.copyOf(faceColorA, faceCount),
        Arrays.copyOf(faceColorB, faceCount),
        Arrays.copyOf(faceColorC, faceCount),
        Arrays.copyOf(faceAlpha, faceCount),
        Arrays.copyOf(faceTextureIds, faceCount),
        Arrays.copyOf(textureVertexA, faceCount),
        Arrays.copyOf(textureVertexB, faceCount),
        Arrays.copyOf(textureVertexC, faceCount),
        Arrays.copyOf(facePriorities, faceCount)
    );
  }

  private void ensureVertexCapacity(int requiredCapacity) {
    if (requiredCapacity <= vertexX.length) {
      return;
    }
    int newCapacity = Math.max(requiredCapacity, vertexX.length * 2);
    vertexX = Arrays.copyOf(vertexX, newCapacity);
    vertexY = Arrays.copyOf(vertexY, newCapacity);
    vertexZ = Arrays.copyOf(vertexZ, newCapacity);
  }

  private void ensureFaceCapacity(int requiredCapacity) {
    if (requiredCapacity <= faceVertexA.length) {
      return;
    }
    int newCapacity = Math.max(requiredCapacity, faceVertexA.length * 2);
    faceVertexA = Arrays.copyOf(faceVertexA, newCapacity);
    faceVertexB = Arrays.copyOf(faceVertexB, newCapacity);
    faceVertexC = Arrays.copyOf(faceVertexC, newCapacity);
    faceColorA = Arrays.copyOf(faceColorA, newCapacity);
    faceColorB = Arrays.copyOf(faceColorB, newCapacity);
    faceColorC = Arrays.copyOf(faceColorC, newCapacity);
    faceAlpha = Arrays.copyOf(faceAlpha, newCapacity);
    faceTextureIds = Arrays.copyOf(faceTextureIds, newCapacity);
    textureVertexA = Arrays.copyOf(textureVertexA, newCapacity);
    textureVertexB = Arrays.copyOf(textureVertexB, newCapacity);
    textureVertexC = Arrays.copyOf(textureVertexC, newCapacity);
    facePriorities = Arrays.copyOf(facePriorities, newCapacity);
  }

  private float midpoint(float first, float second) {
    return (first + second) * 0.5f;
  }
}
