package io.github.ffakira.rsps.client.lwjgl;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
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
  void buildsAnExplicitRenderQueueInsteadOfDisconnectedViewportPasses() {
    WorldScene worldScene = testWorldScene();

    WorldSceneRenderSubmission submission = new WorldSceneSubmissionBuilder(null).build(
        worldScene,
        new WorldPoint(3204, 3204, 0),
        null,
        List.of(),
        496,
        318
    );

    assertThat(submission.renderQueue().batches())
        .extracting(batch -> batch.kind() + ":" + batch.rasterMode())
        .containsExactly(
            "TILE_PAINT:GOURAUD",
            "TILE_MODEL:GOURAUD",
            "ACTOR:FLAT"
        );

    SceneRenderBatch tilePaintBatch = batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD);
    SceneRenderBatch tileModelGouraudBatch = batchOf(submission, SceneSubmissionKind.TILE_MODEL, SceneRasterMode.GOURAUD);
    SceneRenderBatch actorBatch = batchOf(submission, SceneSubmissionKind.ACTOR, SceneRasterMode.FLAT);

    assertThat(tilePaintBatch.rasterMode()).isEqualTo(SceneRasterMode.GOURAUD);
    assertThat(actorBatch.rasterMode()).isEqualTo(SceneRasterMode.FLAT);
    assertThat(tilePaintBatch.isEmpty()).isFalse();
    assertThat(tileModelGouraudBatch.isEmpty()).isFalse();
    assertThat(actorBatch.isEmpty()).isFalse();
    assertThat(tilePaintBatch.mesh().faceColorA()).hasSameSizeAs(tilePaintBatch.mesh().faceVertexA());
    assertThat(tilePaintBatch.mesh().faceTextureIds()).containsOnly(-1);
    assertThat(submission.renderQueue().batches())
        .noneMatch(batch -> batch.kind().name().startsWith("TILE_") && batch.rasterMode() == SceneRasterMode.TEXTURED);
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
            new WorldSceneObject(1, "Near fence", 6, 6, 0, 0, 0, 1, 1, List.of(), null),
            new WorldSceneObject(2, "Far fence", 40, 40, 0, 0, 0, 1, 1, List.of(), null)
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
        496,
        318
    );

    SceneRenderBatch tilePaintBatch = batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD);
    SceneRenderBatch staticObjectBatch = batchOf(submission, SceneSubmissionKind.STATIC_OBJECT, SceneRasterMode.FLAT);

    assertThat(tilePaintBatch.mesh().faceVertexA().length).isEqualTo((48 - 1) * (48 - 1) * 2);
    assertThat(staticObjectBatch.mesh().faceVertexA()).hasSize(10);
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
        496,
        318
    );

    assertThat(submission.renderQueue().batches())
        .noneMatch(batch -> batch.kind() == SceneSubmissionKind.TILE_PAINT && batch.rasterMode() == SceneRasterMode.TEXTURED);
    assertThat(batchOf(submission, SceneSubmissionKind.TILE_PAINT, SceneRasterMode.GOURAUD).isEmpty()).isFalse();
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
}
