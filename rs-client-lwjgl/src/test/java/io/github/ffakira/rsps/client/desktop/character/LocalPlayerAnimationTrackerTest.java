package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.protocol.BootstrapAnimationProfile;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class LocalPlayerAnimationTrackerTest {

  private static final BootstrapAnimationProfile REFERENCE_PROFILE = BootstrapAnimationProfile.referencePlayer();

  @Test
  void smoothsHeadingChangesInsteadOfSnappingToEachTileDelta() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState eastwardState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(30_000_000L);
    ActorAnimationState northwardState = tracker.update(new WorldPoint(3201, 3201, 0), REFERENCE_PROFILE);

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
    tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(16_000_000L);
    ActorAnimationState inBetweenState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);

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
    ActorAnimationState movingState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(500_000_000L);
    ActorAnimationState idleState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);

    assertThat(movingState.isIdle()).isFalse();
    assertThat(idleState.isIdle()).isTrue();
    assertThat(idleState.headingDegrees()).isGreaterThan(0.0f);
    assertThat(idleState.activeSequenceId()).isEqualTo(REFERENCE_PROFILE.standSequenceId());
  }

  @Test
  void advancesTheWalkFrameLifecycleWhileMovementIsActive() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState firstWalkingState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(60_000_000L);
    ActorAnimationState secondWalkingState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);

    assertThat(firstWalkingState.isIdle()).isFalse();
    assertThat(secondWalkingState.isIdle()).isFalse();
    assertThat(secondWalkingState.walkFrameIndex()).isNotEqualTo(firstWalkingState.walkFrameIndex());
    assertThat(secondWalkingState.walkFrameProgress()).isBetween(0.0f, 1.0f);
  }

  @Test
  void usesASidestepLocomotionModeWhenTravelDirectionLeadsFacingDirection() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(30_000_000L);
    ActorAnimationState sidestepState = tracker.update(new WorldPoint(3201, 3201, 0), REFERENCE_PROFILE);

    assertThat(sidestepState.isIdle()).isFalse();
    assertThat(sidestepState.locomotionMode()).isEqualTo(ActorAnimationState.LocomotionMode.STRAFE_LEFT);
    assertThat(sidestepState.relativeTravelDegrees()).isLessThan(-45.0f);
    assertThat(sidestepState.activeSequenceId()).isEqualTo(REFERENCE_PROFILE.turnLeftSequenceId());
  }

  @Test
  void usesRunForwardForLargeServerCatchupSteps() {
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState runState = tracker.update(new WorldPoint(3202, 3200, 0), REFERENCE_PROFILE);

    assertThat(runState.isIdle()).isFalse();
    assertThat(runState.locomotionMode()).isEqualTo(ActorAnimationState.LocomotionMode.RUN_FORWARD);
    assertThat(runState.activeSequenceId()).isEqualTo(REFERENCE_PROFILE.runSequenceId());
  }

  @Test
  void advancesDecodedSequenceFramesOnClientCycleTiming() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now, animationSequences);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState firstWalkingState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);
    clock.advanceNanos(240_000_000L);
    ActorAnimationState secondWalkingState = tracker.update(new WorldPoint(3201, 3200, 0), REFERENCE_PROFILE);

    assertThat(firstWalkingState.activeSequenceId()).isEqualTo(REFERENCE_PROFILE.walkSequenceId());
    assertThat(firstWalkingState.activeFrameId()).isGreaterThanOrEqualTo(0);
    assertThat(secondWalkingState.activeFrameId()).isGreaterThanOrEqualTo(0);
    assertThat(secondWalkingState.activeFrameId()).isNotEqualTo(firstWalkingState.activeFrameId());
  }

  @Test
  void keepsConcurrentMovementAndRuntimeActionFramesWhenActionSequenceIsSupplied() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    AnimationFrameCatalog animationFrames = AnimationFrameCatalog.load(manifest.cacheStore());
    AnimationSequenceCatalog animationSequences = AnimationSequenceCatalog.load(manifest, animationFrames);
    TestNanoClock clock = new TestNanoClock();
    LocalPlayerAnimationTracker tracker = new LocalPlayerAnimationTracker(clock::now, animationSequences);

    tracker.update(new WorldPoint(3200, 3200, 0));
    clock.advanceNanos(100_000_000L);
    ActorAnimationState concurrentState = tracker.update(
        new WorldPoint(3201, 3200, 0),
        REFERENCE_PROFILE,
        REFERENCE_PROFILE.standSequenceId()
    );

    assertThat(concurrentState.hasMovementFrame()).isTrue();
    assertThat(concurrentState.hasActionFrame()).isTrue();
    assertThat(concurrentState.actionSequenceId()).isEqualTo(REFERENCE_PROFILE.standSequenceId());
    assertThat(concurrentState.activeSequenceId()).isEqualTo(REFERENCE_PROFILE.standSequenceId());
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
