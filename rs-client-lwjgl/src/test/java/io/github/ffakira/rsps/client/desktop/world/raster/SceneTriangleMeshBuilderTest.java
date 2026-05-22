package io.github.ffakira.rsps.client.desktop.world.raster;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import org.junit.jupiter.api.Test;

class SceneTriangleMeshBuilderTest {

  @Test
  void filteredGeometryCopiesOnlyVerticesUsedByMatchingFacesAndTextureAnchors() {
    WorldSceneObjectGeometry geometry = new WorldSceneObjectGeometry(
        new float[]{0.0f, 1.0f, 2.0f, 3.0f, 4.0f},
        new float[]{10.0f, 11.0f, 12.0f, 13.0f, 14.0f},
        new float[]{20.0f, 21.0f, 22.0f, 23.0f, 24.0f},
        new int[]{0, 2},
        new int[]{1, 3},
        new int[]{2, 4},
        new int[]{0x111111, 0x222222},
        new int[]{0x111111, 0x222222},
        new int[]{0x111111, 0x222222},
        new int[]{255, 192},
        new SceneRasterMode[]{SceneRasterMode.GOURAUD, SceneRasterMode.TEXTURED},
        new int[]{-1, 7},
        new int[]{-1, 1},
        new int[]{-1, 4},
        new int[]{-1, 3},
        new int[]{0, 5}
    );

    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    builder.addGeometry(geometry, 100.0f, 200.0f, 300.0f, SceneRasterMode.TEXTURED);

    SceneTriangleMesh mesh = builder.build();

    assertThat(mesh.vertexX()).containsExactly(102.0f, 103.0f, 104.0f, 101.0f);
    assertThat(mesh.vertexY()).containsExactly(212.0f, 213.0f, 214.0f, 211.0f);
    assertThat(mesh.vertexZ()).containsExactly(322.0f, 323.0f, 324.0f, 321.0f);
    assertThat(mesh.faceVertexA()).containsExactly(0);
    assertThat(mesh.faceVertexB()).containsExactly(1);
    assertThat(mesh.faceVertexC()).containsExactly(2);
    assertThat(mesh.faceTextureIds()).containsExactly(7);
    assertThat(mesh.textureVertexA()).containsExactly(3);
    assertThat(mesh.textureVertexB()).containsExactly(2);
    assertThat(mesh.textureVertexC()).containsExactly(1);
    assertThat(mesh.facePriorities()).containsExactly(5);
  }

  @Test
  void rotatedFilteredGeometryAppliesYawOnlyToMatchingFaces() {
    WorldSceneObjectGeometry geometry = new WorldSceneObjectGeometry(
        new float[]{0.0f, 1.0f, 0.0f, 5.0f},
        new float[]{0.0f, 0.0f, 0.0f, 9.0f},
        new float[]{0.0f, 0.0f, 1.0f, 5.0f},
        new int[]{0, 1},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0x111111, 0x222222},
        new int[]{0x111111, 0x222222},
        new int[]{0x111111, 0x222222},
        new int[]{255, 255},
        new SceneRasterMode[]{SceneRasterMode.FLAT, SceneRasterMode.GOURAUD},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{0, 0}
    );

    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder();
    builder.addGeometry(geometry, 10.0f, 20.0f, 30.0f, 90.0f, SceneRasterMode.GOURAUD);

    SceneTriangleMesh mesh = builder.build();

    assertThat(mesh.vertexX()).containsExactly(10.0f, 11.0f, 15.0f);
    assertThat(mesh.vertexY()).containsExactly(20.0f, 20.0f, 29.0f);
    assertThat(mesh.vertexZ()).containsExactly(29.0f, 30.0f, 25.0f);
    assertThat(mesh.faceVertexA()).containsExactly(0);
    assertThat(mesh.faceVertexB()).containsExactly(1);
    assertThat(mesh.faceVertexC()).containsExactly(2);
  }

  @Test
  void buildPreservesBuilderReuseWithoutMutatingPreviouslyBuiltMeshes() {
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder(3, 1);
    int a = builder.addVertex(0.0f, 0.0f, 0.0f);
    int b = builder.addVertex(1.0f, 0.0f, 0.0f);
    int c = builder.addVertex(0.0f, 1.0f, 0.0f);
    builder.addTriangle(a, b, c, 0xffffff);

    SceneTriangleMesh firstMesh = builder.build();

    builder.reset();
    int d = builder.addVertex(5.0f, 5.0f, 5.0f);
    int e = builder.addVertex(6.0f, 5.0f, 5.0f);
    int f = builder.addVertex(5.0f, 6.0f, 5.0f);
    builder.addTriangle(d, e, f, 0x123456);

    SceneTriangleMesh secondMesh = builder.build();

    assertThat(firstMesh.vertexX()).containsExactly(0.0f, 1.0f, 0.0f);
    assertThat(firstMesh.faceColorA()).containsExactly(0xffffff);
    assertThat(secondMesh.vertexX()).containsExactly(5.0f, 6.0f, 5.0f);
    assertThat(secondMesh.faceColorA()).containsExactly(0x123456);
  }

  @Test
  void buildDetachedTransfersMeshDataAndStillLeavesTheBuilderReusable() {
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder(3, 1);
    int a = builder.addVertex(0.0f, 0.0f, 0.0f);
    int b = builder.addVertex(1.0f, 0.0f, 0.0f);
    int c = builder.addVertex(0.0f, 1.0f, 0.0f);
    builder.addTriangle(a, b, c, 0xffffff);

    SceneTriangleMesh firstMesh = builder.buildDetached();

    builder.reset();
    int d = builder.addVertex(5.0f, 5.0f, 5.0f);
    int e = builder.addVertex(6.0f, 5.0f, 5.0f);
    int f = builder.addVertex(5.0f, 6.0f, 5.0f);
    builder.addTriangle(d, e, f, 0x123456);

    SceneTriangleMesh secondMesh = builder.buildDetached();

    assertThat(firstMesh.vertexX()).containsExactly(0.0f, 1.0f, 0.0f);
    assertThat(firstMesh.faceColorA()).containsExactly(0xffffff);
    assertThat(secondMesh.vertexX()).containsExactly(5.0f, 6.0f, 5.0f);
    assertThat(secondMesh.faceColorA()).containsExactly(0x123456);
  }
}
