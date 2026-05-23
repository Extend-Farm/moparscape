package com.veyrmoor.client.desktop.render.opengl;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.core.RenderSystem;
import com.veyrmoor.client.desktop.assets.image.ArgbImage;
import com.veyrmoor.client.desktop.character.ActorAnimationState;
import com.veyrmoor.client.desktop.character.CharacterModelAssembler;
import com.veyrmoor.client.desktop.character.LocalPlayerAnimationTracker;
import com.veyrmoor.client.desktop.character.NpcModelAssembler;
import com.veyrmoor.client.desktop.gameplay.GameplayCameraController;
import com.veyrmoor.client.desktop.gameplay.GameplayChatController;
import com.veyrmoor.client.desktop.gameplay.GameplayChromeRenderer;
import com.veyrmoor.client.desktop.gameplay.GameplayClickResult;
import com.veyrmoor.client.desktop.gameplay.GameplayFrameAssets;
import com.veyrmoor.client.desktop.gameplay.GameplayLayout;
import com.veyrmoor.client.desktop.gameplay.GameplayMenuAction;
import com.veyrmoor.client.desktop.gameplay.GameplayMouseButton;
import com.veyrmoor.client.desktop.gameplay.ReportAbuseController;
import com.veyrmoor.client.desktop.itemicon.ItemIconRenderer;
import com.veyrmoor.client.desktop.login.LoginScreenState;
import com.veyrmoor.client.desktop.login.TitleScreenAssets;
import com.veyrmoor.client.desktop.login.TitleScreenLayout;
import com.veyrmoor.client.desktop.login.TitleScreenRenderer;
import com.veyrmoor.client.desktop.render.common.ImmediateModeRenderer2d;
import com.veyrmoor.client.desktop.render.common.OpenGlTexture;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.client.desktop.world.WorldScene;
import com.veyrmoor.client.desktop.world.WorldSceneRenderSubmission;
import com.veyrmoor.client.desktop.world.WorldSceneSubmissionBuilder;
import com.veyrmoor.client.desktop.world.WorldViewportClickPlanner;
import com.veyrmoor.client.desktop.world.WorldViewportRenderer;
import com.veyrmoor.client.desktop.world.raster.SceneTextureAssets;
import com.veyrmoor.content.ItemDefinitionCatalog;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glColor3f;

public final class OpenGlTileRenderSystem implements RenderSystem, AutoCloseable {

  private static final int TILE_SIZE = 40;
  private static final int HALF_GRID_WIDTH = 8;
  private static final int HALF_GRID_HEIGHT = 5;

  private int width;
  private int height;
  private WorldPoint anchorPoint;
  private WorldScene worldScene;
  private OpenGlTexture worldMinimapTexture;
  private WorldPoint lastLocalPlayerPosition;
  private WorldSceneRenderSubmission lastWorldSceneSubmission;
  private double pointerX = Double.NaN;
  private double pointerY = Double.NaN;
  private final ImmediateModeRenderer2d primitives;
  private final TitleScreenRenderer titleScreenRenderer;
  private final GameplayChromeRenderer gameplayChromeRenderer;
  private final GameplayChatController gameplayChatController;
  private final GameplayCameraController gameplayCameraController;
  private final LocalPlayerAnimationTracker localPlayerAnimationTracker;
  private final WorldSceneSubmissionBuilder worldSceneSubmissionBuilder;
  private final WorldViewportRenderer worldViewportRenderer;
  private final WorldViewportClickPlanner worldViewportClickPlanner;

  public OpenGlTileRenderSystem(
      int width,
      int height,
      TitleScreenAssets titleScreenAssets,
      GameplayFrameAssets gameplayFrameAssets,
      SceneTextureAssets sceneTextureAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      ItemIconRenderer itemIconRenderer,
      CharacterModelAssembler characterModelAssembler,
      NpcModelAssembler npcModelAssembler
  ) {
    this.primitives = new ImmediateModeRenderer2d();
    this.titleScreenRenderer = new TitleScreenRenderer(titleScreenAssets, primitives);
    this.gameplayChatController = new GameplayChatController();
    this.gameplayChromeRenderer = new GameplayChromeRenderer(
        gameplayFrameAssets,
        itemDefinitionCatalog,
        itemIconRenderer,
        gameplayChatController,
        titleScreenAssets == null ? null : titleScreenAssets.fonts(),
        primitives
    );
    this.gameplayCameraController = new GameplayCameraController();
    this.localPlayerAnimationTracker = new LocalPlayerAnimationTracker(
        characterModelAssembler == null ? null : characterModelAssembler.animationSequenceCatalog()
    );
    this.worldSceneSubmissionBuilder = new WorldSceneSubmissionBuilder(characterModelAssembler, npcModelAssembler);
    this.worldViewportRenderer = new WorldViewportRenderer(sceneTextureAssets);
    this.worldViewportClickPlanner = new WorldViewportClickPlanner();
    resize(width, height);
  }

  public void setLoginScreenState(LoginScreenState loginScreenState) {
    titleScreenRenderer.setLoginScreenState(loginScreenState);
  }

  public void setWorldScene(WorldScene worldScene) {
    this.worldScene = Objects.requireNonNull(worldScene, "worldScene");
    closeTexture(worldMinimapTexture);
    worldMinimapTexture = OpenGlTexture.fromArgbImage(
        worldScene.minimapImage(),
        OpenGlTexture.FilterMode.LINEAR
    );
  }

  public void clearWorldScene() {
    worldScene = null;
    lastWorldSceneSubmission = null;
    gameplayChatController.reset();
    gameplayChromeRenderer.clearTransientState();
    closeTexture(worldMinimapTexture);
    worldMinimapTexture = null;
  }

  public void resize(int width, int height) {
    this.width = Math.max(1, width);
    this.height = Math.max(1, height);
    glViewport(0, 0, this.width, this.height);
  }

  @Override
  public void render(ClientViewModel viewModel) {
    lastLocalPlayerPosition = viewModel.localPlayerPosition();
    configureProjection();
    glClearColor(0.08f, 0.09f, 0.12f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    if (viewModel.localPlayerPosition() == null) {
      anchorPoint = null;
      lastWorldSceneSubmission = null;
      localPlayerAnimationTracker.reset();
      gameplayChatController.reset();
      gameplayCameraController.clearInputs();
      gameplayChromeRenderer.clearTransientState();
      titleScreenRenderer.render(viewModel, width, height);
      return;
    }
    renderWorld(viewModel);
  }

  public TitleScreenLayout currentTitleScreenLayout() {
    return titleScreenRenderer.currentLayout(width, height);
  }

  public void setPointerPosition(double x, double y) {
    pointerX = x;
    pointerY = y;
    gameplayChromeRenderer.setPointerPosition(x, y);
  }

  public GameplayClickResult handleGameplayClick(double x, double y, GameplayMouseButton button) {
    if (button == GameplayMouseButton.LEFT && gameplayChromeRenderer.hasOpenContextMenu()) {
      GameplayMenuAction selectedAction = gameplayChromeRenderer.consumeContextMenuSelection(x, y);
      if (selectedAction == null) {
        return GameplayClickResult.handledClick();
      }
      return executeMenuAction(selectedAction, GameplayMouseButton.RIGHT, x, y);
    }
    if (button == GameplayMouseButton.LEFT) {
      GameplayClickResult chromeClickResult = gameplayChromeRenderer.handleClick(x, y);
      if (chromeClickResult.handled()) {
        gameplayChromeRenderer.closeContextMenu();
        return chromeClickResult;
      }
    }
    ScreenRect worldViewport = GameplayLayout.worldViewportInnerRect();
    if (!worldViewport.contains(x, y)) {
      gameplayChromeRenderer.closeContextMenu();
      return GameplayClickResult.ignored();
    }
    if (button == GameplayMouseButton.RIGHT) {
      gameplayChromeRenderer.closeContextMenu();
      WorldPoint targetWorldPoint = pickWorldTarget(x, y, worldViewport);
      if (targetWorldPoint == null) {
        return GameplayClickResult.ignored();
      }
      gameplayChromeRenderer.openWalkHereContextMenu(x, y, targetWorldPoint);
      return GameplayClickResult.handledClick();
    }
    gameplayChromeRenderer.closeContextMenu();
    return executeMenuAction(GameplayMenuAction.walkTo(pickWorldTarget(x, y, worldViewport)), GameplayMouseButton.LEFT, x, y);
  }

  public boolean handleGameplayScroll(double x, double y, double yOffset) {
    return gameplayChromeRenderer.handleScroll(x, y, yOffset);
  }

  public boolean handleGameplayPointerMove(double x, double y) {
    return gameplayChromeRenderer.handlePointerMove(x, y);
  }

  public void endGameplayPointerDrag() {
    gameplayChromeRenderer.endPointerDrag();
  }

  public void resetGameplayTabForBootstrap() {
    gameplayChromeRenderer.resetToInventoryTab();
  }

  public GameplayChatController gameplayChatController() {
    return gameplayChatController;
  }

  public ReportAbuseController reportAbuseController() {
    return gameplayChromeRenderer.reportAbuseController();
  }

  public void setGameplayCameraInputs(
      boolean rotateLeftPressed,
      boolean rotateRightPressed,
      boolean pitchUpPressed,
      boolean pitchDownPressed
  ) {
    gameplayCameraController.setHeldInputs(rotateLeftPressed, rotateRightPressed, pitchUpPressed, pitchDownPressed);
  }

  private void configureProjection() {
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
  }

  private void renderWorld(ClientViewModel viewModel) {
    // The world shell now has an explicit submission boundary:
    // - WorldSceneSubmissionBuilder turns terrain, scene objects, and the local player into one
    //   coherent payload.
    // - WorldViewportRenderer consumes that payload.
    // This is still not full legacy raster parity, but it removes the old disconnected helper
    // passes from the main viewport path and keeps OpenGlTileRenderSystem from becoming a render
    // god class again.
    if (worldScene != null) {
      renderWorldScene(viewModel);
    } else {
      renderFallbackWorld(viewModel);
    }
    updateSceneActionHint();
    configureProjection();
    gameplayChromeRenderer.render(
        viewModel,
        worldScene,
        worldMinimapTexture,
        lastWorldSceneSubmission
    );
  }

  private void renderWorldScene(ClientViewModel viewModel) {
    GameplayCameraController.CameraOrbitAngles cameraOrbitAngles = gameplayCameraController.update();
    BootstrapAppearance appearance = viewModel.bootstrap() == null ? null : viewModel.bootstrap().appearance();
    ActorAnimationState actorAnimationState = localPlayerAnimationTracker.update(
        viewModel.localPlayerPosition(),
        appearance == null ? null : appearance.animationProfile(),
        viewModel.localPlayerActionSequenceId()
    );
    lastWorldSceneSubmission = worldSceneSubmissionBuilder.build(
        worldScene,
        viewModel.localPlayerPosition(),
        appearance,
        viewModel.equipment(),
        actorAnimationState,
        Math.max(1, Math.round(GameplayLayout.worldViewportInnerRect().width())),
        Math.max(1, Math.round(GameplayLayout.worldViewportInnerRect().height())),
        cameraOrbitAngles.yawDegrees(),
        cameraOrbitAngles.pitchDegrees()
    );
    worldViewportRenderer.render(GameplayLayout.worldViewportInnerRect(), width, height, lastWorldSceneSubmission);
  }

  private void renderFallbackWorld(ClientViewModel viewModel) {
    lastWorldSceneSubmission = null;
    if (anchorPoint == null) {
      anchorPoint = viewModel.localPlayerPosition();
    }
    drawPlayfield();
    drawAnchorMarker();
    drawPlayer(viewModel.localPlayerPosition());
  }

  private void drawPlayfield() {
    ScreenRect viewport = GameplayLayout.worldViewportRect();
    float originX = viewport.left() + viewport.width() / 2.0f - (TILE_SIZE / 2.0f);
    float originY = viewport.top() + viewport.height() / 2.0f - (TILE_SIZE / 2.0f);

    glColor3f(0.11f, 0.13f, 0.16f);
    primitives.drawQuad(
        originX - HALF_GRID_WIDTH * TILE_SIZE,
        originY - HALF_GRID_HEIGHT * TILE_SIZE,
        (HALF_GRID_WIDTH * 2 + 1) * TILE_SIZE,
        (HALF_GRID_HEIGHT * 2 + 1) * TILE_SIZE
    );

    for (int deltaY = -HALF_GRID_HEIGHT; deltaY <= HALF_GRID_HEIGHT; deltaY++) {
      for (int deltaX = -HALF_GRID_WIDTH; deltaX <= HALF_GRID_WIDTH; deltaX++) {
        float tileLeft = originX + deltaX * TILE_SIZE;
        float tileBottom = originY + deltaY * TILE_SIZE;
        glColor3f(0.17f, 0.20f, 0.24f);
        primitives.drawQuad(tileLeft + 1.0f, tileBottom + 1.0f, TILE_SIZE - 2.0f, TILE_SIZE - 2.0f);
        glColor3f(0.24f, 0.29f, 0.34f);
        primitives.drawOutline(tileLeft, tileBottom, TILE_SIZE, TILE_SIZE);
      }
    }
  }

  private void drawAnchorMarker() {
    ScreenRect viewport = GameplayLayout.worldViewportRect();
    float anchorLeft = viewport.left() + viewport.width() / 2.0f - (TILE_SIZE / 2.0f);
    float anchorBottom = viewport.top() + viewport.height() / 2.0f - (TILE_SIZE / 2.0f);
    glColor3f(0.68f, 0.58f, 0.22f);
    primitives.drawOutline(anchorLeft + 4.0f, anchorBottom + 4.0f, TILE_SIZE - 8.0f, TILE_SIZE - 8.0f);
  }

  private void drawPlayer(WorldPoint worldPoint) {
    int worldDeltaX = worldPoint.x() - anchorPoint.x();
    int worldDeltaY = anchorPoint.y() - worldPoint.y();
    ScreenRect viewport = GameplayLayout.worldViewportRect();
    float playerLeft = viewport.left() + viewport.width() / 2.0f - (TILE_SIZE / 2.0f) + worldDeltaX * TILE_SIZE + 6.0f;
    float playerBottom = viewport.top() + viewport.height() / 2.0f - (TILE_SIZE / 2.0f) + worldDeltaY * TILE_SIZE + 6.0f;

    glColor3f(0.24f, 0.78f, 0.51f);
    primitives.drawQuad(playerLeft, playerBottom, TILE_SIZE - 12.0f, TILE_SIZE - 12.0f);
    glColor3f(0.75f, 0.93f, 0.84f);
    primitives.drawOutline(playerLeft, playerBottom, TILE_SIZE - 12.0f, TILE_SIZE - 12.0f);
  }

  private WorldPoint pickWorldTarget(double x, double y, ScreenRect worldViewport) {
    if (worldScene == null || lastWorldSceneSubmission == null) {
      return null;
    }
    WorldViewportClickPlanner.WorldViewportClickTarget clickTarget = worldViewportClickPlanner.pickTile(
        worldScene,
        lastWorldSceneSubmission.cameraState(),
        worldViewport,
        x,
        y
    );
    if (clickTarget == null) {
      return null;
    }
    return new WorldPoint(
        worldScene.originWorldX() + clickTarget.localX(),
        worldScene.originWorldY() + clickTarget.localY(),
        worldScene.plane()
    );
  }

  private void updateSceneActionHint() {
    if (gameplayChromeRenderer.hasOpenContextMenu()) {
      gameplayChromeRenderer.clearSceneActionHint();
      return;
    }
    ScreenRect worldViewport = GameplayLayout.worldViewportInnerRect();
    if (Double.isNaN(pointerX) || Double.isNaN(pointerY) || !worldViewport.contains(pointerX, pointerY)) {
      gameplayChromeRenderer.clearSceneActionHint();
      return;
    }
    WorldPoint hoveredWorldPoint = pickWorldTarget(pointerX, pointerY, worldViewport);
    if (hoveredWorldPoint == null) {
      gameplayChromeRenderer.clearSceneActionHint();
      return;
    }
    gameplayChromeRenderer.showSceneActionHint("Walk here", 2);
  }

  private GameplayClickResult executeMenuAction(
      GameplayMenuAction action,
      GameplayMouseButton markerButton,
      double clickX,
      double clickY
  ) {
    if (action == null) {
      return GameplayClickResult.handledClick();
    }
    return switch (action.kind()) {
      case CANCEL -> GameplayClickResult.handledClick();
      case WALK_TO_WORLD_POINT -> executeWorldWalk(action.targetWorldPoint(), markerButton, clickX, clickY);
    };
  }

  private GameplayClickResult executeWorldWalk(
      WorldPoint targetWorldPoint,
      GameplayMouseButton markerButton,
      double clickX,
      double clickY
  ) {
    if (targetWorldPoint == null || lastLocalPlayerPosition == null) {
      return GameplayClickResult.ignored();
    }
    if (targetWorldPoint.plane() != lastLocalPlayerPosition.plane()) {
      return GameplayClickResult.handledClick();
    }
    gameplayChromeRenderer.activateCursorMarker(markerButton, clickX, clickY);
    int deltaX = targetWorldPoint.x() - lastLocalPlayerPosition.x();
    int deltaY = targetWorldPoint.y() - lastLocalPlayerPosition.y();
    if (deltaX == 0 && deltaY == 0) {
      return GameplayClickResult.handledClick();
    }
    return GameplayClickResult.move(deltaX, deltaY);
  }

  @Override
  public void close() {
    titleScreenRenderer.close();
    gameplayChromeRenderer.close();
    worldViewportRenderer.close();
    closeTexture(worldMinimapTexture);
  }

  private static void closeTexture(OpenGlTexture texture) {
    if (texture != null) {
      texture.close();
    }
  }
}
