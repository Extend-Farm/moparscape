package io.github.ffakira.rsps.client.desktop.world;

import io.github.ffakira.rsps.client.desktop.character.NpcModelAssembler;
import io.github.ffakira.rsps.client.desktop.world.object.WorldSceneObjectGeometry;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRasterMode;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneRenderQueueBuilder;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneSubmissionKind;
import io.github.ffakira.rsps.client.desktop.world.raster.SceneTriangleMeshBuilder;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionContext;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneOcclusionPlanner;
import io.github.ffakira.rsps.client.desktop.world.visibility.WorldSceneVisibilityWindow;

final class WorldSceneNpcBatchBuilder {

  private static final int DEBUG_GOBLIN_NPC_ID = 100;
  private static final int DEBUG_GOBLIN_TILE_OFFSET_X = 1;
  private static final int DEBUG_GOBLIN_TILE_OFFSET_Y = 0;
  private static final float DEBUG_GOBLIN_YAW_DEGREES = 180.0f;
  private static final boolean DEBUG_GOBLIN_ENABLED =
      Boolean.parseBoolean(System.getProperty("rsps.debugGoblinEnabled", "true"));

  private final NpcModelAssembler npcModelAssembler;

  WorldSceneNpcBatchBuilder(NpcModelAssembler npcModelAssembler) {
    this.npcModelAssembler = npcModelAssembler;
  }

  void addDebugGoblinBatch(
      SceneRenderQueueBuilder renderQueueBuilder,
      WorldScene worldScene,
      WorldSceneVisibilityWindow visibilityWindow,
      WorldSceneOcclusionContext occlusionContext,
      WorldCameraState cameraState,
      int playerLocalTileX,
      int playerLocalTileY
  ) {
    if (!DEBUG_GOBLIN_ENABLED || npcModelAssembler == null) {
      return;
    }
    int goblinLocalTileX = playerLocalTileX + DEBUG_GOBLIN_TILE_OFFSET_X;
    int goblinLocalTileY = playerLocalTileY + DEBUG_GOBLIN_TILE_OFFSET_Y;
    if (goblinLocalTileX < 0 || goblinLocalTileY < 0
        || goblinLocalTileX >= worldScene.tileWidth() || goblinLocalTileY >= worldScene.tileHeight()) {
      return;
    }
    if (!visibilityWindow.containsTile(goblinLocalTileX, goblinLocalTileY)) {
      return;
    }
    float goblinLocalX = WorldSceneActorBatchBuilder.renderedLocalAxis(goblinLocalTileX, 0.0f, worldScene.tileWidth());
    float goblinLocalY = WorldSceneActorBatchBuilder.renderedLocalAxis(goblinLocalTileY, 0.0f, worldScene.tileHeight());
    float baseHeight = WorldSceneActorBatchBuilder.sampleTerrainHeight(worldScene, goblinLocalX, goblinLocalY);
    if (WorldSceneOcclusionPlanner.isOccluded(
        occlusionContext,
        goblinLocalX,
        baseHeight + 1.1f,
        goblinLocalY
    )) {
      return;
    }
    WorldSceneObjectGeometry geometry = npcModelAssembler.assemble(DEBUG_GOBLIN_NPC_ID);
    if (geometry == null) {
      return;
    }
    SceneTriangleMeshBuilder builder = new SceneTriangleMeshBuilder(
        geometry.vertexX().length,
        geometry.faceVertexA().length
    );
    builder.addGeometry(geometry, goblinLocalX, baseHeight, goblinLocalY, DEBUG_GOBLIN_YAW_DEGREES, null);
    renderQueueBuilder.add(
        SceneSubmissionKind.ACTOR,
        preferredRasterMode(geometry),
        WorldSceneActorBatchBuilder.sortActorMeshForSubmission(builder.buildDetached(), cameraState)
    );
  }

  private SceneRasterMode preferredRasterMode(WorldSceneObjectGeometry geometry) {
    for (int faceTextureId : geometry.faceTextureIds()) {
      if (faceTextureId >= 0) {
        return SceneRasterMode.TEXTURED;
      }
    }
    return SceneRasterMode.GOURAUD;
  }
}
