package io.github.ffakira.rsps.client.desktop.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.model.WorldPoint;
import org.junit.jupiter.api.Test;

class LocalPlayerAnimationTrackerTest {

  @Test
  void smoothsHeadingChangesInsteadOfSnappingToEachTileDelta() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState eastwardState = tracker.update(new WorldPoint(3201, 3200, 0));
    clock.advanceNanos(30_000_000L);
    ActorAnimationState northwardState = tracker.update(new WorldPoint(3201, 3201, 0));

    assertThat(eastwardState.isIdle()).isFalse();
    assertThat(eastwardState.headingDegrees()).isBetween(45.0f, 60.0f);
    assertThat(northwardState.isIdle()).isFalse();
    assertThat(northwardState.headingDegrees()).isBetween(30.0f, 45.0f);
  }

  @Test
  void fallsBackToIdleAfterTheMovementPoseTailExpires() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState movingState = tracker.update(new WorldPoint(3201, 3200, 0));
    clock.advanceNanos(500_000_000L);
    ActorAnimationState idleState = tracker.update(new WorldPoint(3201, 3200, 0));

    assertThat(movingState.isIdle()).isFalse();
    assertThat(idleState.isIdle()).isTrue();
    assertThat(idleState.headingDegrees()).isGreaterThan(0.0f);
  }

  private static final class TestNanoClock {

    private long now;

    long now() {
      return now;
    }

    void advanceNanos(long nanos) {
      now += nanos;
    }
  }
}
