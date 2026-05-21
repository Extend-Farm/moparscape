package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import org.junit.jupiter.api.Test;

class GameplayMinimapRendererTest {

  @Test
  void projectsNearbyMarkersAsDirectBlits() {
    GameplayMinimapRenderer.MarkerProjection projection = GameplayMinimapRenderer.projectMarker(
        10.0f,
        0.0f,
        8.0f,
        4.0f,
        GameplayLayout.minimapContentRect(),
        null
    );

    assertThat(projection).isNotNull();
    assertThat(projection.masked()).isFalse();
    assertThat(projection.rect().left()).isCloseTo(654.1992f, within(0.0001f));
    assertThat(projection.rect().top()).isEqualTo(81.0f);
    assertThat(projection.rect().width()).isEqualTo(8.0f);
    assertThat(projection.rect().height()).isEqualTo(4.0f);
  }

  @Test
  void projectsFarMarkersAsMaskedBlitsUntilLegacyCutoff() {
    GameplayMinimapRenderer.MarkerProjection maskedProjection = GameplayMinimapRenderer.projectMarker(
        60.0f,
        0.0f,
        8.0f,
        4.0f,
        GameplayLayout.minimapContentRect(),
        null
    );
    GameplayMinimapRenderer.MarkerProjection hiddenProjection = GameplayMinimapRenderer.projectMarker(
        81.0f,
        0.0f,
        8.0f,
        4.0f,
        GameplayLayout.minimapContentRect(),
        null
    );

    assertThat(maskedProjection).isNotNull();
    assertThat(maskedProjection.masked()).isTrue();
    assertThat(hiddenProjection).isNull();
  }

  @Test
  void rotatesProjectedMarkerOffsetsWithTheCameraYaw() {
    WorldCameraState cameraState = new WorldCameraState(31.0f, 90.0f, 14.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    GameplayMinimapRenderer.MarkerProjection projection = GameplayMinimapRenderer.projectMarker(
        20.0f,
        0.0f,
        8.0f,
        4.0f,
        GameplayLayout.minimapContentRect(),
        cameraState
    );

    assertThat(projection).isNotNull();
    assertThat(projection.masked()).isFalse();
    assertThat(projection.rect().left()).isCloseTo(644.0f, within(0.0001f));
    assertThat(projection.rect().top()).isCloseTo(101.39841f, within(0.0001f));
    assertThat(projection.rect().width()).isEqualTo(8.0f);
    assertThat(projection.rect().height()).isEqualTo(4.0f);
  }
}
