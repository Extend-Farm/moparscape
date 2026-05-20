package io.github.ffakira.rsps.client.desktop.character;

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
    assertThat(eastwardState.headingDegrees()).isBetween(85.0f, 95.0f);
    assertThat(northwardState.isIdle()).isFalse();
    assertThat(northwardState.headingDegrees()).isBetween(75.0f, 85.0f);
  }

  @Test
  void keepsAnInBetweenTilePositionWhileWalkingTowardTheLatestServerTile() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    tracker.update(new WorldPoint(3201, 3200, 0));
    clock.advanceNanos(16_000_000L);
    ActorAnimationState inBetweenState = tracker.update(new WorldPoint(3201, 3200, 0));

    assertThat(inBetweenState.positionOffsetX()).isLessThan(0.0f);
    assertThat(inBetweenState.positionOffsetX()).isGreaterThan(-1.0f);
    assertThat(inBetweenState.positionOffsetY()).isEqualTo(0.0f);
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

  @Test
  void advancesTheWalkFrameLifecycleWhileMovementIsActive() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState firstWalkingState = tracker.update(new WorldPoint(3201, 3200, 0));
    clock.advanceNanos(60_000_000L);
    ActorAnimationState secondWalkingState = tracker.update(new WorldPoint(3201, 3200, 0));

    assertThat(firstWalkingState.isIdle()).isFalse();
    assertThat(secondWalkingState.isIdle()).isFalse();
    assertThat(secondWalkingState.walkFrameIndex()).isNotEqualTo(firstWalkingState.walkFrameIndex());
    assertThat(secondWalkingState.walkFrameProgress()).isBetween(0.0f, 1.0f);
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
