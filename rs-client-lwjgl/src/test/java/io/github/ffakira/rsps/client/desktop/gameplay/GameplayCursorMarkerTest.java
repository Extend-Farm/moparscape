package io.github.ffakira.rsps.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class GameplayCursorMarkerTest {

  @Test
  void advancesOneFrameEveryHundredMilliseconds() {
    GameplayCursorMarker marker = new GameplayCursorMarker();

    marker.activate(GameplayMouseButton.LEFT, 320.0, 240.0, 1_000_000_000L);

    assertThat(marker.snapshot(1_000_000_000L)).extracting(GameplayCursorMarker.Snapshot::frameIndex).isEqualTo(0);
    assertThat(marker.snapshot(1_100_000_000L)).extracting(GameplayCursorMarker.Snapshot::frameIndex).isEqualTo(1);
    assertThat(marker.snapshot(1_200_000_000L)).extracting(GameplayCursorMarker.Snapshot::frameIndex).isEqualTo(2);
    assertThat(marker.snapshot(1_300_000_000L)).extracting(GameplayCursorMarker.Snapshot::frameIndex).isEqualTo(3);
  }

  @Test
  void expiresAfterFourFrames() {
    GameplayCursorMarker marker = new GameplayCursorMarker();

    marker.activate(GameplayMouseButton.RIGHT, 320.0, 240.0, 2_000_000_000L);

    assertThat(marker.snapshot(2_399_999_999L)).isNotNull();
    assertThat(marker.snapshot(2_400_000_000L)).isNull();
    assertThat(marker.snapshot(2_450_000_000L)).isNull();
  }

  @Test
  void latestActivationReplacesOlderMarkerState() {
    GameplayCursorMarker marker = new GameplayCursorMarker();

    marker.activate(GameplayMouseButton.LEFT, 120.0, 140.0, 1_000_000_000L);
    marker.activate(GameplayMouseButton.RIGHT, 220.0, 260.0, 1_250_000_000L);

    GameplayCursorMarker.Snapshot snapshot = marker.snapshot(1_250_000_000L);

    assertThat(snapshot.button()).isEqualTo(GameplayMouseButton.RIGHT);
    assertThat(snapshot.centerX()).isEqualTo(220.0f);
    assertThat(snapshot.centerY()).isEqualTo(260.0f);
    assertThat(snapshot.frameIndex()).isZero();
  }
}
