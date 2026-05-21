package io.github.ffakira.rsps.client.desktop.gameplay;

import io.github.ffakira.rsps.client.core.ClientViewModel;
import io.github.ffakira.rsps.client.desktop.core.ImmediateModeRenderer2d;
import io.github.ffakira.rsps.client.desktop.core.OpenGlTexture;
import io.github.ffakira.rsps.client.desktop.core.ScreenRect;
import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import io.github.ffakira.rsps.client.desktop.world.WorldScene;
import io.github.ffakira.rsps.client.desktop.world.minimap.WorldSceneMapFunctionMarker;
import io.github.ffakira.rsps.client.desktop.world.minimap.MapBackClipMasks;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewport;
import io.github.ffakira.rsps.client.desktop.world.minimap.MinimapViewportPlanner;
import io.github.ffakira.rsps.model.WorldPoint;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.opengl.GL11.glColor3f;

final class GameplayMinimapRenderer {

  private static final int MINIMAP_VISIBLE_TILES = 37;
  private static final int LEGACY_DEFAULT_MINIMAP_SCALE = 251;
  private static final float MINIMAP_SELF_MARKER_SIZE = 3.0f;
  private static final float MINIMAP_DIRECT_MARKER_RADIUS_SQUARED = 2500.0f;
  private static final float MINIMAP_VISIBLE_MARKER_RADIUS_SQUARED = 6400.0f;
  private static final float LEGACY_MINIMAP_MARKER_CENTER_X = 73.0f;
  private static final float LEGACY_MINIMAP_MARKER_CENTER_Y = 74.0f;
  private static final float LEGACY_MINIMAP_SELF_MARKER_LEFT = 72.0f;
  private static final float LEGACY_MINIMAP_SELF_MARKER_TOP = 73.0f;

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
    return cameraState == null ? 0.0f : cameraState.yawDegrees();
  }

  static int legacyMapFunctionSpriteIndex(int rawMapFunctionId) {
    // In this cache layout the media archive exposes direct mapfunction frames 0..60, so the
    // ObjectDefinition raw id is already the correct sprite index.
    return rawMapFunctionId;
  }

  static float legacyMinimapSourceScale() {
    return LEGACY_DEFAULT_MINIMAP_SCALE / 256.0f;
  }

  static float legacyMinimapMarkerDistanceScale() {
    return 256.0f / LEGACY_DEFAULT_MINIMAP_SCALE;
  }

  static float[] minimapMarkerOffset(float eastDelta, float northDelta, WorldCameraState cameraState) {
    float scaledEastDelta = eastDelta * legacyMinimapMarkerDistanceScale();
    float scaledNorthDelta = northDelta * legacyMinimapMarkerDistanceScale();
    float radians = (float) Math.toRadians(minimapUiYawDegrees(cameraState));
    float sine = (float) Math.sin(radians);
    float cosine = (float) Math.cos(radians);
    return new float[]{
        scaledEastDelta * cosine + scaledNorthDelta * sine,
        scaledEastDelta * sine - scaledNorthDelta * cosine
    };
  }

  static MarkerProjection projectMarker(
      float eastDelta,
      float northDelta,
      float markerWidth,
      float markerHeight,
      ScreenRect minimapRect,
      WorldCameraState cameraState
  ) {
    float distanceSquared = eastDelta * eastDelta + northDelta * northDelta;
    if (distanceSquared > MINIMAP_VISIBLE_MARKER_RADIUS_SQUARED) {
      return null;
    }
    float[] markerOffset = minimapMarkerOffset(eastDelta, northDelta, cameraState);
    float centerX = minimapRect.left() + LEGACY_MINIMAP_MARKER_CENTER_X;
    float centerY = minimapRect.top() + LEGACY_MINIMAP_MARKER_CENTER_Y;
    return new MarkerProjection(
        new ScreenRect(
            centerX + markerOffset[0] - markerWidth * 0.5f,
            centerY + markerOffset[1] - markerHeight * 0.5f,
            markerWidth,
            markerHeight
        ),
        distanceSquared > MINIMAP_DIRECT_MARKER_RADIUS_SQUARED
    );
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
        minimapViewport.sourceY() + minimapViewport.markerSourceY(),
        legacyMinimapSourceScale()
    );
  }

  private void drawCenteredMinimapPlayer(ScreenRect minimapRect) {
    float markerLeft = minimapRect.left() + LEGACY_MINIMAP_SELF_MARKER_LEFT;
    float markerTop = minimapRect.top() + LEGACY_MINIMAP_SELF_MARKER_TOP;
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
    Set<Long> drawnMarkers = new HashSet<>();
    for (WorldSceneMapFunctionMarker marker : worldScene.mapFunctionMarkers()) {
      int markerSpriteIndex = legacyMapFunctionSpriteIndex(marker.mapFunctionId());
      if (markerSpriteIndex < 0 || markerSpriteIndex >= mapFunctionTextures.length) {
        continue;
      }
      OpenGlTexture markerTexture = mapFunctionTextures[markerSpriteIndex];
      if (markerTexture == null) {
        continue;
      }
      long markerKey = (((long) markerSpriteIndex) << 32)
          | ((long) marker.localX() << 16)
          | (marker.localY() & 0xffffL);
      if (!drawnMarkers.add(markerKey)) {
        continue;
      }
      int markerPixelX = minimapMarkerPixelX(marker, pixelScaleX);
      int markerPixelY = minimapMarkerPixelY(worldScene, marker, pixelScaleY);
      float eastDelta = markerPixelX - playerPixelX;
      float northDelta = playerPixelY - markerPixelY;
      MarkerProjection markerProjection = projectMarker(
          eastDelta,
          northDelta,
          markerTexture.width(),
          markerTexture.height(),
          minimapRect,
          cameraState
      );
      if (markerProjection == null) {
        continue;
      }
      if (markerProjection.masked() && mapBackClipMasks != null) {
        primitives.drawMaskedTexturedQuad(
            markerTexture,
            markerProjection.rect(),
            minimapRect,
            mapBackClipMasks.minimapRowStarts(),
            mapBackClipMasks.minimapRowWidths()
        );
        continue;
      }
      ScreenRect markerRect = markerProjection.rect();
      if (markerRect.left() + markerRect.width() < minimapRect.left()
          || markerRect.left() > minimapRect.left() + minimapRect.width()
          || markerRect.top() + markerRect.height() < minimapRect.top()
          || markerRect.top() > minimapRect.top() + minimapRect.height()) {
        continue;
      }
      primitives.drawTexturedQuad(markerTexture, markerRect);
    }
  }

  private static int minimapMarkerPixelX(WorldSceneMapFunctionMarker marker, int pixelScaleX) {
    return marker.localX() * pixelScaleX + pixelScaleX / 2;
  }

  private static int minimapMarkerPixelY(
      WorldScene worldScene,
      WorldSceneMapFunctionMarker marker,
      int pixelScaleY
  ) {
    return ((worldScene.tileHeight() - 1) - marker.localY()) * pixelScaleY + pixelScaleY / 2;
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
        compassTexture.height() / 2.0f,
        1.0f
    );
  }

  record MarkerProjection(ScreenRect rect, boolean masked) {
  }
}
