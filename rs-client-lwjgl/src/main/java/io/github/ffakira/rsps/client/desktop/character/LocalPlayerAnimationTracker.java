package io.github.ffakira.rsps.client.desktop.character;

import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.BootstrapAnimationProfile;
import io.github.ffakira.rsps.content.AnimationSequenceCatalog;
import io.github.ffakira.rsps.content.AnimationSequenceDefinition;
import java.util.Objects;
import java.util.function.LongSupplier;

public final class LocalPlayerAnimationTracker {

  private static final long MOVEMENT_POSE_TAIL_NANOS = 220_000_000L;
  private static final float BASE_WALK_CYCLES_PER_SECOND = 1.8f;
  private static final float EXTRA_WALK_CYCLES_PER_SECOND = 1.1f;
  private static final float RUN_CYCLES_PER_SECOND = 3.65f;
  private static final float STRIDE_BLEND_PER_SECOND = 8.0f;
  private static final float HEADING_TURN_DEGREES_PER_SECOND = 320.0f;
  private static final float MOVEMENT_TILES_PER_SECOND = 8.0f;
  private static final float MAX_STEP_DISTANCE = 1.5f;
  private static final float MAX_RENDER_LAG = 2.0f;
  private static final float POSITION_EPSILON = 0.01f;
  private static final float FORWARD_LOCOMOTION_ANGLE = 45.0f;
  private static final float SIDE_LOCOMOTION_ANGLE = 135.0f;
  private static final float RUN_STEP_DISTANCE = 1.35f;
  private static final float TAU = (float) (Math.PI * 2.0);
  private static final long CLIENT_CYCLE_NANOS = 20_000_000L;

  private final LongSupplier nanoClock;
  private final AnimationSequenceCatalog animationSequenceCatalog;
  private WorldPoint lastWorldPoint;
  private long lastUpdateNanos = Long.MIN_VALUE;
  private long lastMovementNanos = Long.MIN_VALUE;
  private float displayedWorldX;
  private float displayedWorldY;
  private float targetHeadingDegrees;
  private float displayedHeadingDegrees;
  private float travelHeadingDegrees;
  private float lastStepDistance;
  private float walkCycleRadians;
  private float strideWeight;
  private final SequenceTimeline movementTimeline = new SequenceTimeline();
  private final SequenceTimeline actionTimeline = new SequenceTimeline();

  public LocalPlayerAnimationTracker() {
    this(System::nanoTime, null);
  }

  public LocalPlayerAnimationTracker(AnimationSequenceCatalog animationSequenceCatalog) {
    this(System::nanoTime, animationSequenceCatalog);
  }

  public LocalPlayerAnimationTracker(LongSupplier nanoClock) {
    this(nanoClock, null);
  }

  public LocalPlayerAnimationTracker(LongSupplier nanoClock, AnimationSequenceCatalog animationSequenceCatalog) {
    this.nanoClock = Objects.requireNonNull(nanoClock, "nanoClock");
    this.animationSequenceCatalog = animationSequenceCatalog;
  }

  public ActorAnimationState update(WorldPoint worldPoint) {
    return update(worldPoint, null, -1);
  }

  public ActorAnimationState update(WorldPoint worldPoint, BootstrapAnimationProfile animationProfile) {
    return update(worldPoint, animationProfile, -1);
  }

  public ActorAnimationState update(
      WorldPoint worldPoint,
      BootstrapAnimationProfile animationProfile,
      int actionSequenceId
  ) {
    long now = nanoClock.getAsLong();
    if (worldPoint == null) {
      reset();
      return ActorAnimationState.idle();
    }
    if (lastWorldPoint == null) {
      snapDisplayedPosition(worldPoint);
      lastWorldPoint = worldPoint;
      lastUpdateNanos = now;
      return buildAnimationState(0.0f, animationProfile, 0L, actionSequenceId, ActorAnimationState.LocomotionMode.IDLE, 0.0f, 0.0f);
    }
    long updateElapsedNanos = lastUpdateNanos == Long.MIN_VALUE ? 0L : Math.max(0L, now - lastUpdateNanos);
    float elapsedSeconds = elapsedSeconds(now);
    if (hasMoved(lastWorldPoint, worldPoint)) {
      int deltaX = worldPoint.x() - lastWorldPoint.x();
      int deltaY = worldPoint.y() - lastWorldPoint.y();
      if (requiresPositionSnap(lastWorldPoint, worldPoint)) {
        snapDisplayedPosition(lastWorldPoint);
      }
      targetHeadingDegrees = headingDegrees(deltaX, deltaY);
      travelHeadingDegrees = targetHeadingDegrees;
      if (lastMovementNanos == Long.MIN_VALUE) {
        displayedHeadingDegrees = targetHeadingDegrees;
      }
      lastStepDistance = Math.min(MAX_STEP_DISTANCE, (float) Math.hypot(deltaX, deltaY));
      lastMovementNanos = now;
    }
    displayedHeadingDegrees = approachAngle(
        displayedHeadingDegrees,
        targetHeadingDegrees,
        HEADING_TURN_DEGREES_PER_SECOND * elapsedSeconds
    );
    moveDisplayedPositionToward(worldPoint, elapsedSeconds);
    lastWorldPoint = worldPoint;
    lastUpdateNanos = now;
    if (lastMovementNanos == Long.MIN_VALUE) {
      return buildAnimationState(
          displayedHeadingDegrees,
          animationProfile,
          updateElapsedNanos,
          actionSequenceId,
          ActorAnimationState.LocomotionMode.IDLE,
          0.0f,
          0.0f
      );
    }
    float distanceRemaining = (float) Math.hypot(worldPoint.x() - displayedWorldX, worldPoint.y() - displayedWorldY);
    boolean stillInterpolating = distanceRemaining > POSITION_EPSILON;
    long movementAgeNanos = now - lastMovementNanos;
    float normalizedTail = movementAgeNanos >= MOVEMENT_POSE_TAIL_NANOS
        ? 0.0f
        : 1.0f - movementAgeNanos / (float) MOVEMENT_POSE_TAIL_NANOS;
    float stepWeight = clamp(lastStepDistance / MAX_STEP_DISTANCE, 0.48f, 1.0f);
    ActorAnimationState.LocomotionMode locomotionMode = resolveLocomotionMode(
        displayedHeadingDegrees,
        travelHeadingDegrees,
        lastStepDistance,
        stillInterpolating || normalizedTail > 0.0f
    );
    float targetStrideWeight = (stillInterpolating ? 1.0f : smoothstep(normalizedTail)) * stepWeight;
    strideWeight = approach(strideWeight, targetStrideWeight, STRIDE_BLEND_PER_SECOND * elapsedSeconds);
    if (strideWeight <= 0.001f && normalizedTail <= 0.0f) {
      strideWeight = 0.0f;
      return buildAnimationState(
          displayedHeadingDegrees,
          animationProfile,
          updateElapsedNanos,
          actionSequenceId,
          ActorAnimationState.LocomotionMode.IDLE,
          0.0f,
          0.0f
      );
    }
    float cadence = locomotionMode == ActorAnimationState.LocomotionMode.RUN_FORWARD
        ? RUN_CYCLES_PER_SECOND
        : BASE_WALK_CYCLES_PER_SECOND + EXTRA_WALK_CYCLES_PER_SECOND * stepWeight;
    walkCycleRadians = wrapRadians(walkCycleRadians + elapsedSeconds * cadence * TAU * Math.max(0.20f, strideWeight));
    return buildAnimationState(
        displayedHeadingDegrees,
        animationProfile,
        updateElapsedNanos,
        actionSequenceId,
        locomotionMode,
        displayedWorldX - worldPoint.x(),
        displayedWorldY - worldPoint.y()
    );
  }

  public void reset() {
    lastWorldPoint = null;
    lastUpdateNanos = Long.MIN_VALUE;
    lastMovementNanos = Long.MIN_VALUE;
    displayedWorldX = 0.0f;
    displayedWorldY = 0.0f;
    targetHeadingDegrees = 0.0f;
    displayedHeadingDegrees = 0.0f;
    travelHeadingDegrees = 0.0f;
    lastStepDistance = 0.0f;
    walkCycleRadians = 0.0f;
    strideWeight = 0.0f;
    movementTimeline.reset();
    actionTimeline.reset();
  }

  private boolean hasMoved(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint) {
    return previousWorldPoint.x() != currentWorldPoint.x()
        || previousWorldPoint.y() != currentWorldPoint.y()
        || previousWorldPoint.plane() != currentWorldPoint.plane();
  }

  private boolean requiresPositionSnap(WorldPoint previousWorldPoint, WorldPoint currentWorldPoint) {
    if (previousWorldPoint.plane() != currentWorldPoint.plane()) {
      return true;
    }
    return Math.hypot(currentWorldPoint.x() - previousWorldPoint.x(), currentWorldPoint.y() - previousWorldPoint.y()) > MAX_RENDER_LAG;
  }

  private void moveDisplayedPositionToward(WorldPoint worldPoint, float elapsedSeconds) {
    if (elapsedSeconds <= 0.0f) {
      return;
    }
    float targetWorldX = worldPoint.x();
    float targetWorldY = worldPoint.y();
    float remainingX = targetWorldX - displayedWorldX;
    float remainingY = targetWorldY - displayedWorldY;
    float remainingDistance = (float) Math.hypot(remainingX, remainingY);
    if (remainingDistance <= POSITION_EPSILON) {
      displayedWorldX = targetWorldX;
      displayedWorldY = targetWorldY;
      return;
    }
    float stepRateScale = clamp(lastStepDistance / MAX_STEP_DISTANCE, 0.85f, 1.0f);
    float maximumTravel = MOVEMENT_TILES_PER_SECOND * stepRateScale * elapsedSeconds;
    if (remainingDistance <= maximumTravel) {
      displayedWorldX = targetWorldX;
      displayedWorldY = targetWorldY;
      return;
    }
    float travelScale = maximumTravel / remainingDistance;
    displayedWorldX += remainingX * travelScale;
    displayedWorldY += remainingY * travelScale;
  }

  private void snapDisplayedPosition(WorldPoint worldPoint) {
    displayedWorldX = worldPoint.x();
    displayedWorldY = worldPoint.y();
  }

  private float smoothstep(float value) {
    float clamped = clamp(value, 0.0f, 1.0f);
    return clamped * clamped * (3.0f - 2.0f * clamped);
  }

  private float elapsedSeconds(long now) {
    if (lastUpdateNanos == Long.MIN_VALUE) {
      return 0.0f;
    }
    return clamp((now - lastUpdateNanos) / 1_000_000_000.0f, 0.0f, 0.25f);
  }

  private float approach(float current, float target, float maximumDelta) {
    if (maximumDelta <= 0.0f) {
      return current;
    }
    if (current < target) {
      return Math.min(target, current + maximumDelta);
    }
    return Math.max(target, current - maximumDelta);
  }

  private float approachAngle(float current, float target, float maximumDeltaDegrees) {
    float delta = normalizeDegrees(target - current);
    if (Math.abs(delta) <= maximumDeltaDegrees) {
      return normalizeDegrees(target);
    }
    return normalizeDegrees(current + Math.copySign(maximumDeltaDegrees, delta));
  }

  private float headingDegrees(int deltaX, int deltaY) {
    return (float) Math.toDegrees(Math.atan2(deltaX, deltaY));
  }

  private ActorAnimationState.LocomotionMode resolveLocomotionMode(
      float facingHeadingDegrees,
      float movementHeadingDegrees,
      float stepDistance,
      boolean locomotionActive
  ) {
    if (!locomotionActive) {
      return ActorAnimationState.LocomotionMode.IDLE;
    }
    float relativeMovementDegrees = normalizeDegrees(movementHeadingDegrees - facingHeadingDegrees);
    if (Math.abs(relativeMovementDegrees) <= FORWARD_LOCOMOTION_ANGLE) {
      return stepDistance >= RUN_STEP_DISTANCE
          ? ActorAnimationState.LocomotionMode.RUN_FORWARD
          : ActorAnimationState.LocomotionMode.WALK_FORWARD;
    }
    if (relativeMovementDegrees > FORWARD_LOCOMOTION_ANGLE && relativeMovementDegrees < SIDE_LOCOMOTION_ANGLE) {
      return ActorAnimationState.LocomotionMode.STRAFE_RIGHT;
    }
    if (relativeMovementDegrees < -FORWARD_LOCOMOTION_ANGLE && relativeMovementDegrees > -SIDE_LOCOMOTION_ANGLE) {
      return ActorAnimationState.LocomotionMode.STRAFE_LEFT;
    }
    return ActorAnimationState.LocomotionMode.WALK_BACKWARD;
  }

  private int resolveActiveSequenceId(
      BootstrapAnimationProfile animationProfile,
      ActorAnimationState.LocomotionMode locomotionMode
  ) {
    if (animationProfile == null) {
      return -1;
    }
    // The bootstrap packet exposes the same stand/walk/turn/run slots the reference client reads
    // from player appearance updates, so locomotion selection stays aligned even before native
    // frame application lands.
    return switch (locomotionMode) {
      case IDLE -> animationProfile.standSequenceId();
      case WALK_FORWARD -> animationProfile.walkSequenceId();
      case WALK_BACKWARD -> fallbackSequenceId(animationProfile.turnAroundSequenceId(), animationProfile.walkSequenceId());
      case STRAFE_LEFT -> fallbackSequenceId(animationProfile.turnLeftSequenceId(), animationProfile.walkSequenceId());
      case STRAFE_RIGHT -> fallbackSequenceId(animationProfile.turnRightSequenceId(), animationProfile.walkSequenceId());
      case RUN_FORWARD -> fallbackSequenceId(animationProfile.runSequenceId(), animationProfile.walkSequenceId());
    };
  }

  private int standSequenceId(BootstrapAnimationProfile animationProfile) {
    return animationProfile == null ? -1 : animationProfile.standSequenceId();
  }

  private int fallbackSequenceId(int preferredSequenceId, int fallbackSequenceId) {
    return preferredSequenceId >= 0 ? preferredSequenceId : fallbackSequenceId;
  }

  private ActorAnimationState buildAnimationState(
      float headingDegrees,
      BootstrapAnimationProfile animationProfile,
      long elapsedNanos,
      int actionSequenceId,
      ActorAnimationState.LocomotionMode locomotionMode,
      float positionOffsetX,
      float positionOffsetY
  ) {
    int movementSequenceId = locomotionMode == ActorAnimationState.LocomotionMode.IDLE
        ? standSequenceId(animationProfile)
        : resolveActiveSequenceId(animationProfile, locomotionMode);
    int movementFrameId = sequenceFrameId(movementTimeline, movementSequenceId, elapsedNanos, SequencePlaybackMode.MOVEMENT);
    int resolvedActionFrameId = sequenceFrameId(actionTimeline, actionSequenceId, elapsedNanos, SequencePlaybackMode.ACTION);
    int resolvedActionSequenceId = resolvedActionFrameId >= 0 ? actionSequenceId : -1;
    return new ActorAnimationState(
        strideWeight,
        locomotionMode == ActorAnimationState.LocomotionMode.IDLE ? 0.0f : walkCycleRadians,
        headingDegrees,
        locomotionMode == ActorAnimationState.LocomotionMode.IDLE ? headingDegrees : travelHeadingDegrees,
        locomotionMode,
        movementSequenceId,
        movementFrameId,
        resolvedActionSequenceId,
        resolvedActionFrameId,
        positionOffsetX,
        positionOffsetY
    );
  }

  private int sequenceFrameId(
      SequenceTimeline timeline,
      int sequenceId,
      long elapsedNanos,
      SequencePlaybackMode playbackMode
  ) {
    if (animationSequenceCatalog == null || sequenceId < 0) {
      timeline.reset();
      return -1;
    }
    AnimationSequenceDefinition sequence = animationSequenceCatalog.find(sequenceId).orElse(null);
    if (sequence == null || sequence.frameCount() <= 0) {
      timeline.reset();
      return -1;
    }
    if (sequenceId != timeline.sequenceId) {
      timeline.resetFor(sequenceId);
    }
    long totalNanos = timeline.remainderNanos + elapsedNanos;
    int cycleCount = (int) (totalNanos / CLIENT_CYCLE_NANOS);
    timeline.remainderNanos = totalNanos % CLIENT_CYCLE_NANOS;
    while (cycleCount-- > 0) {
      if (!advanceSequence(sequence, timeline, playbackMode)) {
        return -1;
      }
    }
    return sequence.frameIdAt(timeline.frameIndex);
  }

  private boolean advanceSequence(
      AnimationSequenceDefinition sequence,
      SequenceTimeline timeline,
      SequencePlaybackMode playbackMode
  ) {
    return switch (playbackMode) {
      case MOVEMENT -> advanceMovementSequence(sequence, timeline);
      case ACTION -> advanceActionSequence(sequence, timeline);
    };
  }

  private boolean advanceMovementSequence(AnimationSequenceDefinition sequence, SequenceTimeline timeline) {
    timeline.frameCycle++;
    if (timeline.frameIndex < sequence.frameCount() && timeline.frameCycle > sequence.frameDurationAt(timeline.frameIndex)) {
      timeline.frameCycle = 0;
      timeline.frameIndex++;
    }
    if (timeline.frameIndex >= sequence.frameCount()) {
      timeline.frameCycle = 0;
      timeline.frameIndex = 0;
    }
    return true;
  }

  private boolean advanceActionSequence(AnimationSequenceDefinition sequence, SequenceTimeline timeline) {
    timeline.frameCycle++;
    while (timeline.frameIndex < sequence.frameCount()
        && timeline.frameCycle > sequence.frameDurationAt(timeline.frameIndex)) {
      timeline.frameCycle -= sequence.frameDurationAt(timeline.frameIndex);
      timeline.frameIndex++;
    }
    if (timeline.frameIndex < sequence.frameCount()) {
      return true;
    }
    timeline.frameIndex -= sequence.loopOffset();
    timeline.completedLoops++;
    if (timeline.completedLoops >= sequence.maximumLoops()) {
      timeline.reset();
      return false;
    }
    if (timeline.frameIndex < 0 || timeline.frameIndex >= sequence.frameCount()) {
      timeline.reset();
      return false;
    }
    return true;
  }

  private float wrapRadians(float radians) {
    float wrapped = radians % TAU;
    return wrapped < 0.0f ? wrapped + TAU : wrapped;
  }

  private float normalizeDegrees(float degrees) {
    float normalized = degrees % 360.0f;
    if (normalized > 180.0f) {
      normalized -= 360.0f;
    } else if (normalized <= -180.0f) {
      normalized += 360.0f;
    }
    return normalized;
  }

  private float clamp(float value, float minimum, float maximum) {
    return Math.max(minimum, Math.min(maximum, value));
  }

  private enum SequencePlaybackMode {
    MOVEMENT,
    ACTION
  }

  private static final class SequenceTimeline {
    private long remainderNanos;
    private int sequenceId = -1;
    private int frameIndex;
    private int frameCycle;
    private int completedLoops;

    private void reset() {
      remainderNanos = 0L;
      sequenceId = -1;
      frameIndex = 0;
      frameCycle = 0;
      completedLoops = 0;
    }

    private void resetFor(int sequenceId) {
      reset();
      this.sequenceId = sequenceId;
    }
  }
}
