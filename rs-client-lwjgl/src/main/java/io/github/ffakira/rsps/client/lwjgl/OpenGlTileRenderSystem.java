package io.github.ffakira.rsps.client.lwjgl;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.core.RenderSystem;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.model.WorldPoint;
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
  private final ImmediateModeRenderer2d primitives;
  private final TitleScreenRenderer titleScreenRenderer;
  private final GameplayChromeRenderer gameplayChromeRenderer;
  private final WorldSceneSubmissionBuilder worldSceneSubmissionBuilder;
  private final WorldViewportRenderer worldViewportRenderer;

  public OpenGlTileRenderSystem(
      int width,
      int height,
      TitleScreenAssets titleScreenAssets,
      GameplayFrameAssets gameplayFrameAssets,
      SceneTextureAssets sceneTextureAssets,
      ItemDefinitionCatalog itemDefinitionCatalog,
      CharacterModelAssembler characterModelAssembler
  ) {
    this.primitives = new ImmediateModeRenderer2d();
    this.titleScreenRenderer = new TitleScreenRenderer(titleScreenAssets, primitives);
    this.gameplayChromeRenderer = new GameplayChromeRenderer(gameplayFrameAssets, itemDefinitionCatalog, primitives);
    this.worldSceneSubmissionBuilder = new WorldSceneSubmissionBuilder(characterModelAssembler);
    this.worldViewportRenderer = new WorldViewportRenderer(sceneTextureAssets);
    resize(width, height);
  }

  public void setLoginScreenState(LoginScreenState loginScreenState) {
    titleScreenRenderer.setLoginScreenState(loginScreenState);
  }

  public void setWorldScene(WorldScene worldScene) {
    this.worldScene = Objects.requireNonNull(worldScene, "worldScene");
    closeTexture(worldMinimapTexture);
    worldMinimapTexture = OpenGlTexture.fromArgbImage(worldScene.minimapImage());
  }

  public void clearWorldScene() {
    worldScene = null;
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
    configureProjection();
    glClearColor(0.08f, 0.09f, 0.12f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    if (viewModel.localPlayerPosition() == null) {
      anchorPoint = null;
      titleScreenRenderer.render(viewModel, width, height);
      return;
    }
    renderWorld(viewModel);
  }

  public TitleScreenLayout currentTitleScreenLayout() {
    return titleScreenRenderer.currentLayout(width, height);
  }

  public boolean handleGameplayClick(double x, double y) {
    return gameplayChromeRenderer.handleClick(x, y);
  }

  public void resetGameplayTabForBootstrap() {
    gameplayChromeRenderer.resetToInventoryTab();
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
    configureProjection();
    gameplayChromeRenderer.render(viewModel, worldScene, worldMinimapTexture);
  }

  private void renderWorldScene(ClientViewModel viewModel) {
    WorldSceneRenderSubmission submission = worldSceneSubmissionBuilder.build(
        worldScene,
        viewModel.localPlayerPosition(),
        viewModel.bootstrap() == null ? null : viewModel.bootstrap().appearance(),
        viewModel.equipment(),
        Math.max(1, Math.round(GameplayLayout.worldViewportInnerRect().width())),
        Math.max(1, Math.round(GameplayLayout.worldViewportInnerRect().height()))
    );
    worldViewportRenderer.render(GameplayLayout.worldViewportInnerRect(), width, height, submission);
  }

  private void renderFallbackWorld(ClientViewModel viewModel) {
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
