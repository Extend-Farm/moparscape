package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.minimap.MapBackClipMasks;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewport;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewportPlanner;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.glColor3f;

final class GameplayMinimapRenderer {

  private static final int MINIMAP_VISIBLE_TILES = 37;
  private static final float MINIMAP_SELF_MARKER_SIZE = 3.0f;

  private final ImmediateModeRenderer2d primitives;
  private final OpenGlTexture compassTexture;
  private final OpenGlTexture[] mapFunctionTextures;
  private final MapBackClipMasks mapBackClipMasks;
  private final MinimapViewportPlanner minimapViewportPlanner = new MinimapViewportPlanner();

  GameplayMinimapRenderer(
      ImmediateModeRenderer2d primitives,
      MapBackClipMasks mapBackClipMasks,
      OpenGlTexture compassTexture,
      OpenGlTexture[] mapFunctionTextures
  ) {
    this.primitives = primitives;
    this.mapBackClipMasks = mapBackClipMasks;
    this.compassTexture = compassTexture;
    this.mapFunctionTextures = mapFunctionTextures == null ? new OpenGlTexture[0] : mapFunctionTextures;
  }

  void drawMinimap(
      ClientViewModel viewModel,
      WorldScene worldScene,
      OpenGlTexture worldMinimapTexture,
      WorldCameraState cameraState
  ) {
    ScreenRect minimapRect = GameplayLayout.minimapContentRect();
    MinimapViewport minimapViewport = minimapViewportPlanner.plan(
        worldScene,
        viewModel.localPlayerPosition(),
        MINIMAP_VISIBLE_TILES
    );
    if (minimapViewport != null && worldMinimapTexture != null) {
      if (mapBackClipMasks != null) {
        drawRotatedMinimap(worldMinimapTexture, minimapRect, minimapViewport, cameraState);
        drawMinimapFunctionMarkers(worldScene, viewModel.localPlayerPosition(), minimapRect, cameraState);
        drawCenteredMinimapPlayer(minimapRect);
      } else {
        primitives.drawCroppedTexturedOval(
            worldMinimapTexture,
            minimapViewport.sourceX(),
            minimapViewport.sourceY(),
            minimapViewport.sourceWidth(),
            minimapViewport.sourceHeight(),
            minimapRect
        );
        drawMinimapFunctionMarkers(worldScene, viewModel.localPlayerPosition(), minimapRect, cameraState);
        drawMinimapPlayer(minimapRect, minimapViewport);
      }
      drawCompass(cameraState);
      return;
    }
    glColor3f(0.03f, 0.05f, 0.07f);
    primitives.drawQuad(minimapRect.left(), minimapRect.top(), minimapRect.width(), minimapRect.height());
    glColor3f(0.20f, 0.24f, 0.30f);
    primitives.drawOutline(minimapRect.left(), minimapRect.top(), minimapRect.width(), minimapRect.height());
    primitives.drawCenteredText(
        minimapRect.left() + minimapRect.width() / 2.0f,
        minimapRect.top() + minimapRect.height() / 2.0f,
        "Loading map",
        0.92f,
        0.86f,
        0.46f
    );
    drawCompass(cameraState);
  }

  static float minimapUiYawDegrees(WorldCameraState cameraState) {
    return cameraState == null ? 0.0f : -cameraState.yawDegrees();
  }

  static int legacyMapFunctionSpriteIndex(int rawMapFunctionId) {
    if (rawMapFunctionId >= 15 && rawMapFunctionId <= 67) {
      return rawMapFunctionId - 2;
    }
    if (rawMapFunctionId == 13 || (rawMapFunctionId >= 68 && rawMapFunctionId <= 84)) {
      return rawMapFunctionId - 1;
    }
    return rawMapFunctionId;
  }

  static float[] minimapMarkerOffset(float eastDelta, float northDelta, WorldCameraState cameraState) {
    float radians = (float) Math.toRadians(minimapUiYawDegrees(cameraState));
    float sine = (float) Math.sin(radians);
    float cosine = (float) Math.cos(radians);
    return new float[]{
        eastDelta * cosine + northDelta * sine,
        eastDelta * sine - northDelta * cosine
    };
  }

  private void drawMinimapPlayer(ScreenRect minimapRect, MinimapViewport minimapViewport) {
    float scaleX = minimapRect.width() / minimapViewport.sourceWidth();
    float scaleY = minimapRect.height() / minimapViewport.sourceHeight();
    float markerLeft = minimapRect.left() + minimapViewport.markerSourceX() * scaleX - MINIMAP_SELF_MARKER_SIZE * 0.5f;
    float markerTop = minimapRect.top() + minimapViewport.markerSourceY() * scaleY - MINIMAP_SELF_MARKER_SIZE * 0.5f;
    glColor3f(0.96f, 0.96f, 0.96f);
    primitives.drawQuad(markerLeft, markerTop, MINIMAP_SELF_MARKER_SIZE, MINIMAP_SELF_MARKER_SIZE);
  }

  private void drawRotatedMinimap(
      OpenGlTexture worldMinimapTexture,
      ScreenRect minimapRect,
      MinimapViewport minimapViewport,
      WorldCameraState cameraState
  ) {
    primitives.drawMaskedRotatedTexturedRows(
        worldMinimapTexture,
        minimapRect,
        MapBackClipMasks.MINIMAP_WIDTH,
        mapBackClipMasks.minimapRowStarts(),
        mapBackClipMasks.minimapRowWidths(),
        minimapUiYawDegrees(cameraState),
        minimapViewport.sourceX() + minimapViewport.markerSourceX(),
        minimapViewport.sourceY() + minimapViewport.markerSourceY()
    );
  }

  private void drawCenteredMinimapPlayer(ScreenRect minimapRect) {
    float markerLeft = minimapRect.left() + minimapRect.width() * 0.5f - MINIMAP_SELF_MARKER_SIZE * 0.5f;
    float markerTop = minimapRect.top() + minimapRect.height() * 0.5f - MINIMAP_SELF_MARKER_SIZE * 0.5f;
    glColor3f(0.96f, 0.96f, 0.96f);
    primitives.drawQuad(markerLeft, markerTop, MINIMAP_SELF_MARKER_SIZE, MINIMAP_SELF_MARKER_SIZE);
  }

  private void drawMinimapFunctionMarkers(
      WorldScene worldScene,
      WorldPoint localPlayerPosition,
      ScreenRect minimapRect,
      WorldCameraState cameraState
  ) {
    if (worldScene == null
        || localPlayerPosition == null
        || !worldScene.contains(localPlayerPosition)
        || mapFunctionTextures.length == 0) {
      return;
    }
    int pixelScaleX = worldScene.minimapPixelWidthPerTile();
    int pixelScaleY = worldScene.minimapPixelHeightPerTile();
    int playerPixelX = worldScene.projectMinimapX(localPlayerPosition);
    int playerPixelY = worldScene.projectMinimapY(localPlayerPosition);
    float centerX = minimapRect.left() + minimapRect.width() * 0.5f;
    float centerY = minimapRect.top() + minimapRect.height() * 0.5f;
    Set<Long> drawnMarkers = new HashSet<>();
    for (var sceneObject : worldScene.objects()) {
      if (sceneObject.type() != 22) {
        continue;
      }
      int markerSpriteIndex = legacyMapFunctionSpriteIndex(sceneObject.mapFunctionId());
      if (markerSpriteIndex < 0 || markerSpriteIndex >= mapFunctionTextures.length) {
        continue;
      }
      OpenGlTexture markerTexture = mapFunctionTextures[markerSpriteIndex];
      if (markerTexture == null) {
        continue;
      }
      long markerKey = (((long) markerSpriteIndex) << 32)
          | ((long) sceneObject.localX() << 16)
          | (sceneObject.localY() & 0xffffL);
      if (!drawnMarkers.add(markerKey)) {
        continue;
      }
      float objectPixelX = sceneObject.centerX() * pixelScaleX;
      float objectPixelY = (worldScene.tileHeight() - sceneObject.centerY()) * pixelScaleY;
      float eastDelta = objectPixelX - playerPixelX;
      float northDelta = playerPixelY - objectPixelY;
      if (eastDelta * eastDelta + northDelta * northDelta > 6400.0f) {
        continue;
      }
      float[] markerOffset = minimapMarkerOffset(eastDelta, northDelta, cameraState);
      float markerLeft = centerX + markerOffset[0] - markerTexture.width() * 0.5f;
      float markerTop = centerY + markerOffset[1] - markerTexture.height() * 0.5f;
      if (markerLeft + markerTexture.width() < minimapRect.left()
          || markerLeft > minimapRect.left() + minimapRect.width()
          || markerTop + markerTexture.height() < minimapRect.top()
          || markerTop > minimapRect.top() + minimapRect.height()) {
        continue;
      }
      primitives.drawTexturedQuad(
          markerTexture,
          new ScreenRect(markerLeft, markerTop, markerTexture.width(), markerTexture.height())
      );
    }
  }

  private void drawCompass(WorldCameraState cameraState) {
    if (compassTexture == null || mapBackClipMasks == null) {
      return;
    }
    primitives.drawMaskedRotatedTexturedRows(
        compassTexture,
        GameplayLayout.compassRect(),
        MapBackClipMasks.COMPASS_WIDTH,
        mapBackClipMasks.compassRowStarts(),
        mapBackClipMasks.compassRowWidths(),
        minimapUiYawDegrees(cameraState),
        compassTexture.width() / 2.0f,
        compassTexture.height() / 2.0f
    );
  }
}
