package com.veyrmoor.client.desktop.world;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.veyrmoor.client.desktop.render.common.ScreenRect;
import org.junit.jupiter.api.Test;

class WorldViewportProjectionTest {

  @Test
  void derivesTheGameplayFrustumFromTheLegacySoftwareFocalLength() {
    assertThat(WorldViewportProjection.halfVerticalFovDegrees(318.0f)).isCloseTo(17.252018f, within(0.0001f));
    assertThat(WorldViewportProjection.frustumTop(318.0f)).isCloseTo(0.37265626f, within(0.0001f));
    assertThat(WorldViewportProjection.frustumRight(496.0f)).isCloseTo(0.58125f, within(0.0001f));
  }

  @Test
  void projectsTheCameraFocusPointToTheViewportCenter() {
    ScreenRect viewportRect = new ScreenRect(4.0f, 4.0f, 512.0f, 334.0f);
    WorldCameraState cameraState = new WorldCameraState(0.0f, 0.0f, 20.0f, 0.0f, 10.5f, 11.5f, 0.0f);

    WorldViewportScreenProjector.ScreenProjection projection = WorldViewportScreenProjector.project(
        viewportRect,
        cameraState,
        10.5f,
        0.0f,
        11.5f
    );

    assertThat(projection).isNotNull();
    assertThat(projection.screenX()).isCloseTo(viewportRect.left() + viewportRect.width() * 0.5f, within(0.0001f));
    assertThat(projection.screenY()).isCloseTo(viewportRect.top() + viewportRect.height() * 0.5f, within(0.0001f));
  }

  @Test
  void projectsHigherWorldPointsAboveTheViewportCenter() {
    ScreenRect viewportRect = new ScreenRect(4.0f, 4.0f, 512.0f, 334.0f);
    WorldCameraState cameraState = new WorldCameraState(0.0f, 0.0f, 20.0f, 0.0f, 10.5f, 11.5f, 0.0f);

    WorldViewportScreenProjector.ScreenProjection focusProjection = WorldViewportScreenProjector.project(
        viewportRect,
        cameraState,
        10.5f,
        0.0f,
        11.5f
    );
    WorldViewportScreenProjector.ScreenProjection overheadProjection = WorldViewportScreenProjector.project(
        viewportRect,
        cameraState,
        10.5f,
        2.0f,
        11.5f
    );

    assertThat(focusProjection).isNotNull();
    assertThat(overheadProjection).isNotNull();
    assertThat(overheadProjection.screenY()).isLessThan(focusProjection.screenY());
  }
}
