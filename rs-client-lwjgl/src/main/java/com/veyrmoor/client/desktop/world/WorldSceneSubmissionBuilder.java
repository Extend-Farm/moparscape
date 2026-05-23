package com.veyrmoor.client.desktop.world;

import com.veyrmoor.client.desktop.character.ActorAnimationState;
import com.veyrmoor.client.desktop.character.CharacterModelAssembler;
import com.veyrmoor.client.desktop.character.NpcModelAssembler;
import com.veyrmoor.client.desktop.world.raster.SceneRasterMode;
import com.veyrmoor.client.desktop.world.raster.SceneRenderQueue;
import com.veyrmoor.client.desktop.world.raster.SceneRenderQueueBuilder;
import com.veyrmoor.client.desktop.world.raster.SceneSubmissionKind;
import com.veyrmoor.client.desktop.world.raster.SceneTriangleMesh;
import com.veyrmoor.client.desktop.world.terrain.TerrainSceneMeshBuilder;
import com.veyrmoor.client.desktop.world.visibility.WorldSceneOcclusionContext;
import com.veyrmoor.client.desktop.world.visibility.WorldSceneOcclusionPlanner;
import com.veyrmoor.client.desktop.world.visibility.WorldScenePlaneRules;
import com.veyrmoor.client.desktop.world.visibility.WorldSceneVisibilityPlanner;
import com.veyrmoor.client.desktop.world.visibility.WorldSceneVisibilityWindow;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.util.List;

public final class WorldSceneSubmissionBuilder {

  private final TerrainSceneMeshBuilder terrainSceneMeshBuilder;
  private final WorldSceneCameraPlanner worldSceneCameraPlanner;
  private final WorldSceneObjectBatchBuilder objectBatchBuilder;
  private final WorldSceneActorBatchBuilder actorBatchBuilder;
  private final WorldSceneNpcBatchBuilder npcBatchBuilder;
  private WorldScene cachedTerrainScene;
  private SceneRenderQueue cachedTerrainQueue;

  public WorldSceneSubmissionBuilder(CharacterModelAssembler characterModelAssembler) {
    this(characterModelAssembler, null);
  }

  public WorldSceneSubmissionBuilder(
      CharacterModelAssembler characterModelAssembler,
      NpcModelAssembler npcModelAssembler
  ) {
    this.terrainSceneMeshBuilder = new TerrainSceneMeshBuilder();
    this.worldSceneCameraPlanner = new WorldSceneCameraPlanner();
    this.objectBatchBuilder = new WorldSceneObjectBatchBuilder();
    this.actorBatchBuilder = new WorldSceneActorBatchBuilder(characterModelAssembler);
    this.npcBatchBuilder = new WorldSceneNpcBatchBuilder(npcModelAssembler);
  }

  // This is the first native scene-submission boundary for the rewrite. It does not attempt full
  // parity yet, but it does turn terrain, static objects, and the local player into one coherent
  // queued submission payload instead of rendering them through disconnected helper passes.
  public WorldSceneRenderSubmission build(
      WorldScene worldScene,
      WorldPoint worldPoint,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState actorAnimationState,
      int viewportWidth,
      int viewportHeight
  ) {
    return build(
        worldScene,
        worldPoint,
        appearance,
        equipment,
        actorAnimationState,
        viewportWidth,
        viewportHeight,
        0.0f,
        0.0f
    );
  }

  public WorldSceneRenderSubmission build(
      WorldScene worldScene,
      WorldPoint worldPoint,
      BootstrapAppearance appearance,
      List<BootstrapItemSlot> equipment,
      ActorAnimationState actorAnimationState,
      int viewportWidth,
      int viewportHeight,
      float cameraYawOffsetDegrees,
      float cameraPitchOffsetDegrees
  ) {
    int playerLocalX = worldPoint.x() - worldScene.originWorldX();
    int playerLocalY = worldPoint.y() - worldScene.originWorldY();
    float actorLocalX = WorldSceneActorBatchBuilder.renderedLocalAxis(
        playerLocalX,
        actorAnimationState == null ? 0.0f : actorAnimationState.positionOffsetX(),
        worldScene.tileWidth()
    );
    float actorLocalY = WorldSceneActorBatchBuilder.renderedLocalAxis(
        playerLocalY,
        actorAnimationState == null ? 0.0f : actorAnimationState.positionOffsetY(),
        worldScene.tileHeight()
    );
    int focusTileX = clampTile((int) Math.floor(actorLocalX), worldScene.tileWidth());
    int focusTileY = clampTile((int) Math.floor(actorLocalY), worldScene.tileHeight());
    WorldCameraState cameraState = worldSceneCameraPlanner.plan(
        worldScene,
        actorLocalX,
        actorLocalY,
        WorldSceneScale.HEIGHT_SCALE,
        viewportWidth,
        viewportHeight,
        cameraYawOffsetDegrees,
        cameraPitchOffsetDegrees
    );
    int renderPlane = WorldScenePlaneRules.renderPlane(worldScene, cameraState, focusTileX, focusTileY);
    WorldSceneVisibilityWindow visibilityWindow = WorldSceneVisibilityPlanner.plan(
        worldScene,
        cameraState,
        focusTileX,
        focusTileY,
        viewportWidth,
        viewportHeight
    );
    WorldSceneOcclusionContext occlusionContext = WorldSceneOcclusionPlanner.plan(
        worldScene,
        visibilityWindow,
        cameraState
    );
    SceneRenderQueueBuilder renderQueueBuilder = new SceneRenderQueueBuilder();
    renderQueueBuilder.addAll(terrainQueue(worldScene));
    objectBatchBuilder.addBatches(renderQueueBuilder, worldScene, visibilityWindow, occlusionContext);
    WorldSceneRenderSubmission.ActorOverheadAnchor localPlayerOverheadAnchor = actorBatchBuilder.addBatches(
        renderQueueBuilder,
        worldScene,
        visibilityWindow,
        occlusionContext,
        cameraState,
        actorLocalX,
        actorLocalY,
        appearance,
        equipment,
        actorAnimationState
    );
    npcBatchBuilder.addDebugGoblinBatch(
        renderQueueBuilder,
        worldScene,
        visibilityWindow,
        occlusionContext,
        cameraState,
        playerLocalX,
        playerLocalY
    );
    return new WorldSceneRenderSubmission(
        renderPlane,
        cameraState,
        renderQueueBuilder.build(),
        localPlayerOverheadAnchor
    );
  }

  static float actorYawDegrees(ActorAnimationState actorAnimationState) {
    return WorldSceneActorBatchBuilder.actorYawDegrees(actorAnimationState);
  }

  static SceneTriangleMesh sortActorMeshForSubmission(SceneTriangleMesh mesh, WorldCameraState cameraState) {
    return WorldSceneActorBatchBuilder.sortActorMeshForSubmission(mesh, cameraState);
  }

  private void addTerrainBatches(SceneRenderQueueBuilder renderQueueBuilder, WorldScene worldScene) {
    // Terrain stays on the full stitched scene window for correctness. The client still lacks the
    // scene graph's visible-tile traversal, so a narrower terrain cull would reopen black voids
    // at the viewport edge while objects and actors remain bounded by the visibility window.
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_PAINT,
        SceneRasterMode.GOURAUD,
        terrainSceneMeshBuilder.buildTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_PAINT,
        SceneRasterMode.TEXTURED,
        terrainSceneMeshBuilder.buildTexturedTilePaintMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.GOURAUD,
        terrainSceneMeshBuilder.buildTileModelMesh(worldScene)
    );
    renderQueueBuilder.add(
        SceneSubmissionKind.TILE_MODEL,
        SceneRasterMode.TEXTURED,
        terrainSceneMeshBuilder.buildTexturedTileModelMesh(worldScene)
    );
  }

  private SceneRenderQueue terrainQueue(WorldScene worldScene) {
    if (worldScene == cachedTerrainScene && cachedTerrainQueue != null) {
      return cachedTerrainQueue;
    }
    SceneRenderQueueBuilder terrainQueueBuilder = new SceneRenderQueueBuilder();
    addTerrainBatches(terrainQueueBuilder, worldScene);
    cachedTerrainScene = worldScene;
    cachedTerrainQueue = terrainQueueBuilder.build();
    return cachedTerrainQueue;
  }

  private int clampTile(int value, int tileBound) {
    return Math.max(0, Math.min(tileBound - 1, value));
  }
}
