package io.github.ffakira.rsps.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.client.desktop.character.ActorAnimationState;
import io.github.ffakira.rsps.client.desktop.character.CharacterModelAssembler;
import io.github.ffakira.rsps.client.desktop.core.ArgbImage;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObject;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderBatch;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneSubmissionKind;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMesh;
import io.github.ffakira.rsps.client.desktop.world.terrain.BridgeTerrainLayer;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class WorldSceneSubmissionBuilderTest {

  @Test
  void appliesAForwardAxisCorrectionToActorHeading() {
    assertThat(WorldSceneSubmissionBuilder.actorYawDegrees(null)).isEqualTo(180.0f);
    assertThat(WorldSceneSubmissionBuilder.actorYawDegrees(ActorAnimationState.idle(0.0f))).isEqualTo(180.0f);
    assertThat(WorldSceneSubmissionBuilder.actorYawDegrees(ActorAnimationState.idle(90.0f))).isEqualTo(-90.0f);
    assertThat(WorldSceneSubmissionBuilder.actorYawDegrees(ActorAnimationState.idle(-90.0f))).isEqualTo(90.0f);
  }

  @Test
  void buildsAnExplicitRenderQueueInsteadOfDisconnectedViewportPasses() {
    WorldScene worldScene = testWorldScene();

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    assertThat(submission.renderQueue().batches())
        .extracting(batch -> batch.kind() + ":" + batch.rasterMode())
        .containsExactly(
            "TILE_PAINT:TEXTURED",
            "TILE_MODEL:GOURAUD",
            "TILE_MODEL:TEXTURED",
            "ACTOR:FLAT"
        );

    SceneRenderBatch tilePaintBatch = batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.TEXTURED);
    SceneRenderBatch tileModelGouraudBatch = batchOf(submission, SceneSubmissionKind.TILE_MODEL, SceneRasterMode.GOURAUD);
    SceneRenderBatch tileModelTexturedBatch = batchOf(submission, SceneSubmissionKind.TILE_MODEL, SceneRasterMode.TEXTURED);
    SceneRenderBatch actorBatch = batchOf(submission, SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT);

    assertThat(tilePaintBatch.rasterMode()).isEqualTo(SceneRasterMode.TEXTURED);
    assertThat(actorBatch.rasterMode()).isEqualTo(SceneRasterMode.FLAT);
    assertThat(tilePaintBatch.isEmpty()).isFalse();
    assertThat(tileModelGouraudBatch.isEmpty()).isFalse();
    assertThat(tileModelTexturedBatch.isEmpty()).isFalse();
    assertThat(actorBatch.isEmpty()).isFalse();
    assertThat(tilePaintBatch.mesh().faceColorA()).hasSameSizeAs(tilePaintBatch.mesh().faceVertexA());
    assertThat(tilePaintBatch.mesh().faceTextureIds()).containsOnly(12);
    assertThat(tileModelTexturedBatch.mesh().faceTextureIds()).containsOnly(12);
  }

  @Test
  void submitsNativeActorGeometryUsingActorFaceRasterModes() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(assembler).build(
        testWorldScene(),
        new WorldPoint(3204, 3204, 0),
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    List<SceneRenderBatch> actorBatches = submission.renderQueue().batches().stream()
        .filter(batch -> batch.kind() == SceneSubmissionKind.ACTOR)
        .toList();

    assertThat(actorBatches).isNotEmpty();
    assertThat(actorBatches).extracting(SceneRenderBatch::rasterMode).contains(SceneRasterMode.GOURAUD);
    assertThat(actorBatches).allMatch(batch -> !batch.isEmpty());
  }

  @Test
  void rotatesSubmittedActorGeometryToMatchItsHeading() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    CharacterModelAssembler assembler = new CharacterModelAssembler(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
    WorldScene worldScene = testWorldScene();

    WorldSceneRenderSubmission northFacingSubmission = new WorldSceneSubmissionBuilder(assembler).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );
    WorldSceneRenderSubmission eastFacingSubmission = new WorldSceneSubmissionBuilder(assembler).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        ActorAnimationState.idle(90.0f),
        496,
        318
    );

    SceneTriangleMesh northMesh = batchOf(northFacingSubmission, SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD).mesh();
    SceneTriangleMesh eastMesh = batchOf(eastFacingSubmission, SceneSubmissionKind.ACTOR, SceneRasterMode.GOURAUD).mesh();

    assertThat(northMesh.faceVertexA()).containsExactly(eastMesh.faceVertexA());
    assertThat(northMesh.faceVertexB()).containsExactly(eastMesh.faceVertexB());
    assertThat(northMesh.faceVertexC()).containsExactly(eastMesh.faceVertexC());
    assertThat(Arrays.equals(northMesh.vertexX(), eastMesh.vertexX())).isFalse();
    assertThat(Arrays.equals(northMesh.vertexZ(), eastMesh.vertexZ())).isFalse();
  }

  @Test
  void placesTheActorMeshAtItsSmoothedInBetweenTilePosition() {
    WorldScene worldScene = testWorldScene();

    WorldSceneRenderSubmission centeredSubmission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );
    WorldSceneRenderSubmission offsetSubmission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        new ActorAnimationState(1.0f, 0.0f, 90.0f, -0.40f, 0.25f),
        496,
        318
    );

    SceneTriangleMesh centeredMesh = batchOf(centeredSubmission, SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT).mesh();
    SceneTriangleMesh offsetMesh = batchOf(offsetSubmission, SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT).mesh();

    assertThat(offsetMesh.vertexX()[0]).isLessThan(centeredMesh.vertexX()[0]);
    assertThat(offsetMesh.vertexZ()[0]).isGreaterThan(centeredMesh.vertexZ()[0]);
  }

  @Test
  void keepsFullTerrainWhileCullingFarObjectsBeforeQueueSubmission() {
    WorldScene worldScene = new WorldScene(
        "wide",
        3200,
        3200,
        0,
        48,
        48,
        gradientHeights(48, 48),
        filledInts(48 * 48, 0x4a6a3c),
        filledInts(48 * 48, 0x4a6a3c),
        filledInts(48 * 48, 0),
        filledInts(48 * 48, -1),
        filledInts(48 * 48, -1),
        new byte[48 * 48],
        new byte[48 * 48],
        new byte[48 * 48],
        List.of(
            new WorldSceneObject(1, "Near fence", 6, 6, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null),
            new WorldSceneObject(2, "Far fence", 40, 40, 0, 0, 0, 1, 1, false, -1, -1, List.of(), true, null)
        ),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(48, 48, filledInts(48 * 48, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3206, 3206, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneRenderBatch tilePaintBatch = batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD);
    SceneRenderBatch staticObjectBatch = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT);

    assertThat(tilePaintBatch.mesh().faceVertexA().length).isEqualTo((48 - 1) * (48 - 1) * 2);
    assertThat(staticObjectBatch.mesh().faceVertexA()).hasSize(10);
  }

  @Test
  void rendersFallbackWallSegmentsForStubBackedWorldObjects() {
    WorldScene worldScene = new WorldScene(
        "stub-backed-object",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, -1),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(new WorldSceneObject(85, "Bridge wall stub", 3, 3, 0, 0, 0, 1, 1, false, 22, -1, List.of(2214, 2215), true, null)),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneTriangleMesh staticObjectMesh = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT).mesh();

    assertThat(staticObjectMesh.faceVertexA()).hasSize(10);
    assertThat(min(staticObjectMesh.vertexX())).isCloseTo(3.0f, offset(0.0001f));
    assertThat(max(staticObjectMesh.vertexX())).isCloseTo(3.18f, offset(0.0001f));
    assertThat(min(staticObjectMesh.vertexZ())).isCloseTo(3.0f, offset(0.0001f));
    assertThat(max(staticObjectMesh.vertexZ())).isCloseTo(4.0f, offset(0.0001f));
  }

  @Test
  void rendersPerimeterFallbacksForLargeMissingStructures() {
    WorldScene worldScene = new WorldScene(
        "missing-building",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, -1),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(new WorldSceneObject(1200, "Stone building", 2, 3, 0, 10, 0, 3, 2, false, -1, -1, List.of(9999), true, null)),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneTriangleMesh staticObjectMesh = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT).mesh();

    assertThat(staticObjectMesh.faceVertexA()).hasSize(40);
    assertThat(min(staticObjectMesh.vertexX())).isCloseTo(2.0f, offset(0.0001f));
    assertThat(max(staticObjectMesh.vertexX())).isCloseTo(5.0f, offset(0.0001f));
    assertThat(min(staticObjectMesh.vertexZ())).isCloseTo(3.0f, offset(0.0001f));
    assertThat(max(staticObjectMesh.vertexZ())).isCloseTo(5.0f, offset(0.0001f));
    assertThat(containsApprox(staticObjectMesh.vertexX(), 2.18f)).isTrue();
    assertThat(containsApprox(staticObjectMesh.vertexX(), 4.82f)).isTrue();
    assertThat(containsApprox(staticObjectMesh.vertexZ(), 3.18f)).isTrue();
    assertThat(containsApprox(staticObjectMesh.vertexZ(), 4.82f)).isTrue();
  }

  @Test
  void doesNotTexturePlainUnderlayPaintTiles() {
    WorldScene worldScene = new WorldScene(
        "underlay-only",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, 12),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    assertThat(submission.renderQueue().batches())
        .noneMatch(batch -> batch.kind() == SceneSubmissionKind.TILE_PAINT && batch.rasterMode() == SceneRasterMode.TEXTURED);
    assertThat(batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD).isEmpty()).isFalse();
  }

  @Test
  void centersStaticObjectGeometryWithinItsFootprint() {
    WorldSceneObjectGeometry geometry = new WorldSceneObjectGeometry(
        new float[]{-0.5f, 0.5f, 0.5f, -0.5f},
        new float[]{0.0f, 0.0f, 1.0f, 1.0f},
        new float[]{-0.5f, -0.5f, 0.5f, 0.5f},
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{255, 255},
        new SceneRasterMode[]{SceneRasterMode.GOURAUD, SceneRasterMode.GOURAUD},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1}
    );
    WorldScene worldScene = new WorldScene(
        "centered-object",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, -1),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(new WorldSceneObject(1, "Tree", 2, 3, 0, 10, 0, 1, 1, false, -1, -1, List.of(), true, geometry)),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneTriangleMesh staticObjectMesh = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD).mesh();

    assertThat(staticObjectMesh.vertexX()).containsExactly(2.0f, 3.0f, 3.0f, 2.0f);
    assertThat(staticObjectMesh.vertexZ()).containsExactly(3.0f, 3.0f, 4.0f, 4.0f);
  }

  @Test
  void contoursFenceLikeObjectGeometryAgainstTileCornerHeights() {
    WorldSceneObjectGeometry geometry = new WorldSceneObjectGeometry(
        new float[]{-0.5f, 0.5f, 0.5f, -0.5f},
        new float[]{0.0f, 0.0f, 0.0f, 0.0f},
        new float[]{-0.5f, -0.5f, 0.5f, 0.5f},
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{255, 255},
        new SceneRasterMode[]{SceneRasterMode.GOURAUD, SceneRasterMode.GOURAUD},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1}
    );
    WorldScene worldScene = new WorldScene(
        "contoured-fence",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, -1),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(new WorldSceneObject(980, "Fence", 2, 3, 0, 0, 0, 1, 1, true, -1, -1, List.of(), true, geometry)),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneTriangleMesh staticObjectMesh = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD).mesh();

    float southWest = worldScene.elevationAt(2, 3) * WorldSceneScale.HEIGHT_SCALE;
    float southEast = worldScene.elevationAt(3, 3) * WorldSceneScale.HEIGHT_SCALE;
    float northEast = worldScene.elevationAt(3, 4) * WorldSceneScale.HEIGHT_SCALE;
    float northWest = worldScene.elevationAt(2, 4) * WorldSceneScale.HEIGHT_SCALE;

    assertThat(staticObjectMesh.vertexY()).containsExactly(southWest, southEast, northEast, northWest);
  }

  @Test
  void anchorsBridgeSupportObjectsToTheLowerBridgeSurface() {
    WorldSceneObjectGeometry geometry = new WorldSceneObjectGeometry(
        new float[]{-0.5f, 0.5f, 0.5f, -0.5f},
        new float[]{0.0f, 0.0f, 1.0f, 1.0f},
        new float[]{-0.5f, -0.5f, 0.5f, 0.5f},
        new int[]{0, 0},
        new int[]{1, 2},
        new int[]{2, 3},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{0xffffff, 0xffffff},
        new int[]{255, 255},
        new SceneRasterMode[]{SceneRasterMode.GOURAUD, SceneRasterMode.GOURAUD},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1},
        new int[]{-1, -1}
    );
    int[] surfaceHeights = filledInts(64, 20);
    int[] lowerHeights = filledInts(64, 0);
    byte[] bridgeActiveTiles = new byte[64];
    bridgeActiveTiles[3 * 8 + 2] = 1;
    WorldScene worldScene = new WorldScene(
        "bridge-support",
        3200,
        3200,
        0,
        8,
        8,
        surfaceHeights,
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0),
        filledInts(64, -1),
        filledInts(64, -1),
        new byte[64],
        new byte[64],
        new byte[64],
        List.of(new WorldSceneObject(3009, "Bridge support", 2, 3, 0, 10, 0, 1, 1, false, -1, -1, List.of(), true, geometry)),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0),
        new BridgeTerrainLayer(
            8,
            8,
            lowerHeights,
            filledInts(64, 0x4a6a3c),
            filledInts(64, 0x4a6a3c),
            filledInts(64, 0),
            filledInts(64, -1),
            filledInts(64, -1),
            new byte[64],
            new byte[64],
            bridgeActiveTiles
        )
    );

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        ActorAnimationState.idle(),
        496,
        318
    );

    SceneTriangleMesh staticObjectMesh = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.GOURAUD).mesh();

    assertThat(staticObjectMesh.vertexY()).containsExactly(0.0f, 0.0f, 1.0f, 1.0f);
  }

  private static WorldScene testWorldScene() {
    return new WorldScene(
        "test",
        3200,
        3200,
        0,
        8,
        8,
        gradientHeights(8, 8),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x4a6a3c),
        filledInts(64, 0x7d9150),
        filledInts(64, -1),
        filledInts(64, 12),
        mixedTileShapeArray(8, 8),
        new byte[64],
        new byte[64],
        List.of(),
        List.of(),
        new ArgbImage(1, 1, new int[]{0xff000000}),
        new ArgbImage(8, 8, filledInts(64, 0xff334455)),
        new WorldSceneProjection(5, 3, 0, 0)
    );
  }

  private static SceneRenderBatch batchOf(
      WorldSceneRenderSubmission submission,
      SceneSubmissionKind kind,
      SceneRasterMode rasterMode
  ) {
    return submission.renderQueue().batches().stream()
        .filter(batch -> batch.kind() == kind && batch.rasterMode() == rasterMode)
        .findFirst()
        .orElseThrow();
  }

  private static int[] filledInts(int size, int value) {
    int[] values = new int[size];
    java.util.Arrays.fill(values, value);
    return values;
  }

  private static int[] gradientHeights(int width, int height) {
    int[] heights = new int[width * height];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        heights[y * width + x] = x * 6 + y * 11;
      }
    }
    return heights;
  }

  private static byte[] mixedTileShapeArray(int width, int height) {
    byte[] values = new byte[width * height];
    values[3 * width + 3] = 2;
    return values;
  }

  private static float min(float[] values) {
    float minimum = Float.POSITIVE_INFINITY;
    for (float value : values) {
      minimum = Math.min(minimum, value);
    }
    return minimum;
  }

  private static float max(float[] values) {
    float maximum = Float.NEGATIVE_INFINITY;
    for (float value : values) {
      maximum = Math.max(maximum, value);
    }
    return maximum;
  }

  private static boolean containsApprox(float[] values, float expected) {
    for (float value : values) {
      if (Math.abs(value - expected) <= 0.0001f) {
        return true;
      }
    }
    return false;
  }
}
