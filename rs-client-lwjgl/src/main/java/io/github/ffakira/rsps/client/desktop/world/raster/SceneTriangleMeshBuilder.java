package io.github.ffakira.rsps.client.desktop.world.raster;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import java.util.Arrays;

public final class SceneTriangleMeshBuilder {

  private static final int DEFAULT_VERTEX_CAPACITY = 256;
  private static final int DEFAULT_FACE_CAPACITY = 256;
  private static final int NO_VERTEX_REMAP = -1;
  private static final int MAX_REFERENCED_VERTICES_PER_FACE = 6;
  private static final boolean VALIDATE_VERTEX_INDICES =
      Boolean.getBoolean("rsps.validateSceneTriangleMeshBuilderIndices");

  private float[] vertexX;
  private float[] vertexY;
  private float[] vertexZ;
  private int vertexCount;

  private int[] faceVertexA;
  private int[] faceVertexB;
  private int[] faceVertexC;
  private int[] faceColorA;
  private int[] faceColorB;
  private int[] faceColorC;
  private int[] faceAlpha;
  private int[] faceTextureIds;
  private int[] textureVertexA;
  private int[] textureVertexB;
  private int[] textureVertexC;
  private int[] facePriorities;
  private int faceCount;

  private int[] filteredVertexRemap = new int[0];
  private int[] filteredVertexMarks = new int[0];
  private int filteredVertexMark = 1;

  public SceneTriangleMeshBuilder() {
    this(DEFAULT_VERTEX_CAPACITY, DEFAULT_FACE_CAPACITY);
  }

  public SceneTriangleMeshBuilder(int initialVertexCapacity, int initialFaceCapacity) {
    reinitializeStorage(
        Math.max(0, initialVertexCapacity),
        Math.max(0, initialFaceCapacity)
    );
  }

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
    // Terrain shape points use the 317 tile-model layout:
    //
    //   1 --- 2 --- 3
    //   | \  9  10 / |
    //   8  13  | 14  4
    //   | 12 \ | /11 |
    //   7  16  | 15  5
    //   | /    6    \|
    //
    // The default path uses the shared midpoint for irregular overlay fallbacks.
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
    validateVertexIndices(a, b, c);
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
      appendMeshFace(
          mesh,
          index,
          baseVertexIndex + mesh.faceVertexA()[index],
          baseVertexIndex + mesh.faceVertexB()[index],
          baseVertexIndex + mesh.faceVertexC()[index],
          remapTextureVertex(mesh.textureVertexA()[index], baseVertexIndex),
          remapTextureVertex(mesh.textureVertexB()[index], baseVertexIndex),
          remapTextureVertex(mesh.textureVertexC()[index], baseVertexIndex)
      );
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
    if (rasterModeFilter != null) {
      addFilteredRotatedGeometry(geometry, offsetX, offsetY, offsetZ, yawDegrees, rasterModeFilter);
      return;
    }
    appendRotatedGeometry(geometry, offsetX, offsetY, offsetZ, yawDegrees);
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
    if (rasterModeFilter != null) {
      addFilteredGeometry(geometry, offsetX, offsetY, offsetZ, rasterModeFilter);
      return;
    }
    appendGeometry(geometry, offsetX, offsetY, offsetZ);
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

  public SceneTriangleMesh buildDetached() {
    if (faceCount == 0) {
      return SceneTriangleMesh.EMPTY;
    }
    int nextVertexCapacity = Math.max(DEFAULT_VERTEX_CAPACITY, vertexX.length);
    int nextFaceCapacity = Math.max(DEFAULT_FACE_CAPACITY, faceVertexA.length);
    SceneTriangleMesh mesh = new SceneTriangleMesh(
        detachFloats(vertexX, vertexCount),
        detachFloats(vertexY, vertexCount),
        detachFloats(vertexZ, vertexCount),
        detachInts(faceVertexA, faceCount),
        detachInts(faceVertexB, faceCount),
        detachInts(faceVertexC, faceCount),
        detachInts(faceColorA, faceCount),
        detachInts(faceColorB, faceCount),
        detachInts(faceColorC, faceCount),
        detachInts(faceAlpha, faceCount),
        detachInts(faceTextureIds, faceCount),
        detachInts(textureVertexA, faceCount),
        detachInts(textureVertexB, faceCount),
        detachInts(textureVertexC, faceCount),
        detachInts(facePriorities, faceCount)
    );
    reinitializeStorage(nextVertexCapacity, nextFaceCapacity);
    return mesh;
  }

  public void reset() {
    vertexCount = 0;
    faceCount = 0;
  }

  private void appendRotatedGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ,
      float yawDegrees
  ) {
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
      appendGeometryFace(
          geometry,
          index,
          baseVertexIndex + geometry.faceVertexA()[index],
          baseVertexIndex + geometry.faceVertexB()[index],
          baseVertexIndex + geometry.faceVertexC()[index],
          remapTextureVertex(geometry.textureVertexA()[index], baseVertexIndex),
          remapTextureVertex(geometry.textureVertexB()[index], baseVertexIndex),
          remapTextureVertex(geometry.textureVertexC()[index], baseVertexIndex)
      );
    }
  }

  private void addFilteredRotatedGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ,
      float yawDegrees,
      SceneRasterMode rasterModeFilter
  ) {
    int matchingFaceCount = countMatchingFaces(geometry.faceRasterModes(), rasterModeFilter);
    if (matchingFaceCount == 0) {
      return;
    }
    ensureVertexCapacity(vertexCount + initialFilteredVertexCapacity(geometry, matchingFaceCount));
    ensureFaceCapacity(faceCount + matchingFaceCount);
    int mark = prepareFilteredVertexScratch(geometry.vertexX().length);
    float radians = (float) Math.toRadians(yawDegrees);
    float sine = (float) Math.sin(radians);
    float cosine = (float) Math.cos(radians);
    for (int index = 0; index < geometry.faceVertexA().length; index++) {
      if (geometry.faceRasterModes()[index] != rasterModeFilter) {
        continue;
      }
      appendGeometryFace(
          geometry,
          index,
          copyRotatedFilteredVertex(geometry, geometry.faceVertexA()[index], offsetX, offsetY, offsetZ, sine, cosine, mark),
          copyRotatedFilteredVertex(geometry, geometry.faceVertexB()[index], offsetX, offsetY, offsetZ, sine, cosine, mark),
          copyRotatedFilteredVertex(geometry, geometry.faceVertexC()[index], offsetX, offsetY, offsetZ, sine, cosine, mark),
          copyRotatedFilteredTextureVertex(geometry, geometry.textureVertexA()[index], offsetX, offsetY, offsetZ, sine, cosine, mark),
          copyRotatedFilteredTextureVertex(geometry, geometry.textureVertexB()[index], offsetX, offsetY, offsetZ, sine, cosine, mark),
          copyRotatedFilteredTextureVertex(geometry, geometry.textureVertexC()[index], offsetX, offsetY, offsetZ, sine, cosine, mark)
      );
    }
  }

  private void appendGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ
  ) {
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
      appendGeometryFace(
          geometry,
          index,
          baseVertexIndex + geometry.faceVertexA()[index],
          baseVertexIndex + geometry.faceVertexB()[index],
          baseVertexIndex + geometry.faceVertexC()[index],
          remapTextureVertex(geometry.textureVertexA()[index], baseVertexIndex),
          remapTextureVertex(geometry.textureVertexB()[index], baseVertexIndex),
          remapTextureVertex(geometry.textureVertexC()[index], baseVertexIndex)
      );
    }
  }

  private void addFilteredGeometry(
      WorldSceneObjectGeometry geometry,
      float offsetX,
      float offsetY,
      float offsetZ,
      SceneRasterMode rasterModeFilter
  ) {
    int matchingFaceCount = countMatchingFaces(geometry.faceRasterModes(), rasterModeFilter);
    if (matchingFaceCount == 0) {
      return;
    }
    ensureVertexCapacity(vertexCount + initialFilteredVertexCapacity(geometry, matchingFaceCount));
    ensureFaceCapacity(faceCount + matchingFaceCount);
    int mark = prepareFilteredVertexScratch(geometry.vertexX().length);
    for (int index = 0; index < geometry.faceVertexA().length; index++) {
      if (geometry.faceRasterModes()[index] != rasterModeFilter) {
        continue;
      }
      appendGeometryFace(
          geometry,
          index,
          copyFilteredVertex(geometry, geometry.faceVertexA()[index], offsetX, offsetY, offsetZ, mark),
          copyFilteredVertex(geometry, geometry.faceVertexB()[index], offsetX, offsetY, offsetZ, mark),
          copyFilteredVertex(geometry, geometry.faceVertexC()[index], offsetX, offsetY, offsetZ, mark),
          copyFilteredTextureVertex(geometry, geometry.textureVertexA()[index], offsetX, offsetY, offsetZ, mark),
          copyFilteredTextureVertex(geometry, geometry.textureVertexB()[index], offsetX, offsetY, offsetZ, mark),
          copyFilteredTextureVertex(geometry, geometry.textureVertexC()[index], offsetX, offsetY, offsetZ, mark)
      );
    }
  }

  private void ensureVertexCapacity(int requiredCapacity) {
    if (requiredCapacity <= vertexX.length) {
      return;
    }
    int newCapacity = Math.max(requiredCapacity, Math.max(1, vertexX.length) * 2);
    vertexX = Arrays.copyOf(vertexX, newCapacity);
    vertexY = Arrays.copyOf(vertexY, newCapacity);
    vertexZ = Arrays.copyOf(vertexZ, newCapacity);
  }

  private void ensureFaceCapacity(int requiredCapacity) {
    if (requiredCapacity <= faceVertexA.length) {
      return;
    }
    int newCapacity = Math.max(requiredCapacity, Math.max(1, faceVertexA.length) * 2);
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

  private int countMatchingFaces(SceneRasterMode[] faceRasterModes, SceneRasterMode rasterModeFilter) {
    int matchingFaceCount = 0;
    for (SceneRasterMode faceRasterMode : faceRasterModes) {
      if (faceRasterMode == rasterModeFilter) {
        matchingFaceCount++;
      }
    }
    return matchingFaceCount;
  }

  private int initialFilteredVertexCapacity(WorldSceneObjectGeometry geometry, int matchingFaceCount) {
    return Math.min(geometry.vertexX().length, matchingFaceCount * MAX_REFERENCED_VERTICES_PER_FACE);
  }

  private int prepareFilteredVertexScratch(int requiredCapacity) {
    if (filteredVertexRemap.length < requiredCapacity) {
      filteredVertexRemap = new int[requiredCapacity];
      filteredVertexMarks = new int[requiredCapacity];
      filteredVertexMark = 1;
    } else if (filteredVertexMark == Integer.MAX_VALUE) {
      Arrays.fill(filteredVertexMarks, 0);
      filteredVertexMark = 1;
    }
    return filteredVertexMark++;
  }

  private int copyFilteredTextureVertex(
      WorldSceneObjectGeometry geometry,
      int vertexIndex,
      float offsetX,
      float offsetY,
      float offsetZ,
      int mark
  ) {
    if (vertexIndex < 0) {
      return NO_VERTEX_REMAP;
    }
    return copyFilteredVertex(geometry, vertexIndex, offsetX, offsetY, offsetZ, mark);
  }

  private int copyRotatedFilteredTextureVertex(
      WorldSceneObjectGeometry geometry,
      int vertexIndex,
      float offsetX,
      float offsetY,
      float offsetZ,
      float sine,
      float cosine,
      int mark
  ) {
    if (vertexIndex < 0) {
      return NO_VERTEX_REMAP;
    }
    return copyRotatedFilteredVertex(geometry, vertexIndex, offsetX, offsetY, offsetZ, sine, cosine, mark);
  }

  private int copyFilteredVertex(
      WorldSceneObjectGeometry geometry,
      int vertexIndex,
      float offsetX,
      float offsetY,
      float offsetZ,
      int mark
  ) {
    if (filteredVertexMarks[vertexIndex] == mark) {
      return filteredVertexRemap[vertexIndex];
    }
    ensureVertexCapacity(vertexCount + 1);
    vertexX[vertexCount] = geometry.vertexX()[vertexIndex] + offsetX;
    vertexY[vertexCount] = geometry.vertexY()[vertexIndex] + offsetY;
    vertexZ[vertexCount] = geometry.vertexZ()[vertexIndex] + offsetZ;
    filteredVertexMarks[vertexIndex] = mark;
    filteredVertexRemap[vertexIndex] = vertexCount;
    return vertexCount++;
  }

  private int copyRotatedFilteredVertex(
      WorldSceneObjectGeometry geometry,
      int vertexIndex,
      float offsetX,
      float offsetY,
      float offsetZ,
      float sine,
      float cosine,
      int mark
  ) {
    if (filteredVertexMarks[vertexIndex] == mark) {
      return filteredVertexRemap[vertexIndex];
    }
    ensureVertexCapacity(vertexCount + 1);
    float localX = geometry.vertexX()[vertexIndex];
    float localZ = geometry.vertexZ()[vertexIndex];
    vertexX[vertexCount] = localX * cosine + localZ * sine + offsetX;
    vertexY[vertexCount] = geometry.vertexY()[vertexIndex] + offsetY;
    vertexZ[vertexCount] = localZ * cosine - localX * sine + offsetZ;
    filteredVertexMarks[vertexIndex] = mark;
    filteredVertexRemap[vertexIndex] = vertexCount;
    return vertexCount++;
  }

  private int remapTextureVertex(int textureVertexIndex, int baseVertexIndex) {
    return textureVertexIndex < 0 ? NO_VERTEX_REMAP : baseVertexIndex + textureVertexIndex;
  }

  private void appendMeshFace(
      SceneTriangleMesh mesh,
      int faceIndex,
      int vertexA,
      int vertexB,
      int vertexC,
      int textureA,
      int textureB,
      int textureC
  ) {
    faceVertexA[faceCount] = vertexA;
    faceVertexB[faceCount] = vertexB;
    faceVertexC[faceCount] = vertexC;
    faceColorA[faceCount] = mesh.faceColorA()[faceIndex];
    faceColorB[faceCount] = mesh.faceColorB()[faceIndex];
    faceColorC[faceCount] = mesh.faceColorC()[faceIndex];
    faceAlpha[faceCount] = mesh.faceAlpha()[faceIndex];
    faceTextureIds[faceCount] = mesh.faceTextureIds()[faceIndex];
    textureVertexA[faceCount] = textureA;
    textureVertexB[faceCount] = textureB;
    textureVertexC[faceCount] = textureC;
    facePriorities[faceCount] = mesh.facePriorities()[faceIndex];
    faceCount++;
  }

  private void appendGeometryFace(
      WorldSceneObjectGeometry geometry,
      int faceIndex,
      int vertexA,
      int vertexB,
      int vertexC,
      int textureA,
      int textureB,
      int textureC
  ) {
    faceVertexA[faceCount] = vertexA;
    faceVertexB[faceCount] = vertexB;
    faceVertexC[faceCount] = vertexC;
    faceColorA[faceCount] = geometry.faceColorA()[faceIndex];
    faceColorB[faceCount] = geometry.faceColorB()[faceIndex];
    faceColorC[faceCount] = geometry.faceColorC()[faceIndex];
    faceAlpha[faceCount] = geometry.faceAlpha()[faceIndex];
    faceTextureIds[faceCount] = geometry.faceTextureIds()[faceIndex];
    textureVertexA[faceCount] = textureA;
    textureVertexB[faceCount] = textureB;
    textureVertexC[faceCount] = textureC;
    facePriorities[faceCount] = geometry.facePriorities()[faceIndex];
    faceCount++;
  }

  private float[] detachFloats(float[] values, int usedLength) {
    return values.length == usedLength ? values : Arrays.copyOf(values, usedLength);
  }

  private int[] detachInts(int[] values, int usedLength) {
    return values.length == usedLength ? values : Arrays.copyOf(values, usedLength);
  }

  private void reinitializeStorage(int vertexCapacity, int faceCapacity) {
    vertexX = new float[vertexCapacity];
    vertexY = new float[vertexCapacity];
    vertexZ = new float[vertexCapacity];
    faceVertexA = new int[faceCapacity];
    faceVertexB = new int[faceCapacity];
    faceVertexC = new int[faceCapacity];
    faceColorA = new int[faceCapacity];
    faceColorB = new int[faceCapacity];
    faceColorC = new int[faceCapacity];
    faceAlpha = new int[faceCapacity];
    faceTextureIds = new int[faceCapacity];
    textureVertexA = new int[faceCapacity];
    textureVertexB = new int[faceCapacity];
    textureVertexC = new int[faceCapacity];
    facePriorities = new int[faceCapacity];
    reset();
  }

  private void validateVertexIndices(int a, int b, int c) {
    if (!VALIDATE_VERTEX_INDICES) {
      return;
    }
    validateVertexIndex(a, "a");
    validateVertexIndex(b, "b");
    validateVertexIndex(c, "c");
  }

  private void validateVertexIndex(int vertexIndex, String label) {
    if (vertexIndex >= 0 && vertexIndex < vertexCount) {
      return;
    }
    throw new IllegalArgumentException(
        "Triangle vertex " + label + " index " + vertexIndex + " is outside 0.." + (vertexCount - 1)
    );
  }

  private float midpoint(float first, float second) {
    return (first + second) * 0.5f;
  }
}
