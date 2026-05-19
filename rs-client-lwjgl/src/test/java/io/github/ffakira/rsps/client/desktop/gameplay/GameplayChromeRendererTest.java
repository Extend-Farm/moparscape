package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.desktop.world.WorldCameraState;
import org.junit.jupiter.api.Test;

class GameplayChromeRendererTest {

  @Test
  void minimapUiYawUsesOppositeSignFromWorldCameraYaw() {
    WorldCameraState cameraState = new WorldCameraState(26.0f, 90.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f);

    assertThat(GameplayChromeRenderer.minimapUiYawDegrees(cameraState)).isEqualTo(-90.0f);
  }

  @Test
  void minimapUiYawDefaultsToZeroWithoutCameraState() {
    assertThat(GameplayChromeRenderer.minimapUiYawDegrees(null)).isZero();
  }

  @Test
  void minimapMarkerOffsetMatchesNorthUpRadarWithoutCameraRotation() {
    WorldCameraState cameraState = new WorldCameraState(26.0f, 0.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f);

    float[] eastMarker = GameplayChromeRenderer.minimapMarkerOffset(10.0f, 0.0f, cameraState);
    float[] northMarker = GameplayChromeRenderer.minimapMarkerOffset(0.0f, 10.0f, cameraState);

    assertThat(eastMarker[0]).isEqualTo(10.0f);
    assertThat(eastMarker[1]).isZero();
    assertThat(northMarker[0]).isZero();
    assertThat(northMarker[1]).isEqualTo(-10.0f);
  }

  @Test
  void remapsLegacyMapFunctionIdsBeforeSpriteLookup() {
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(14)).isEqualTo(14);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(15)).isEqualTo(13);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(67)).isEqualTo(65);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(68)).isEqualTo(67);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(84)).isEqualTo(83);
  }
}
