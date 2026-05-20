package io.github.ffakira.rsps.protocol;

public record BootstrapAnimationProfile(
    int standSequenceId,
    int standTurnSequenceId,
    int walkSequenceId,
    int turnAroundSequenceId,
    int turnRightSequenceId,
    int turnLeftSequenceId,
    int runSequenceId
) {

  private static final int HUMAN_STAND_SEQUENCE_ID = 0x328;
  private static final int HUMAN_STAND_TURN_SEQUENCE_ID = 0x337;
  private static final int HUMAN_WALK_SEQUENCE_ID = 0x333;
  private static final int HUMAN_TURN_AROUND_SEQUENCE_ID = 0x334;
  private static final int HUMAN_TURN_RIGHT_SEQUENCE_ID = 0x335;
  private static final int HUMAN_TURN_LEFT_SEQUENCE_ID = 0x336;
  private static final int HUMAN_RUN_SEQUENCE_ID = 0x338;
  private static final BootstrapAnimationProfile REFERENCE_PLAYER = new BootstrapAnimationProfile(
      HUMAN_STAND_SEQUENCE_ID,
      HUMAN_STAND_TURN_SEQUENCE_ID,
      HUMAN_WALK_SEQUENCE_ID,
      HUMAN_TURN_AROUND_SEQUENCE_ID,
      HUMAN_TURN_RIGHT_SEQUENCE_ID,
      HUMAN_TURN_LEFT_SEQUENCE_ID,
      HUMAN_RUN_SEQUENCE_ID
  );

  // This mirrors the seven animation slots in the reference appearance block: stand, stand turn,
  // walk, turn around, turn right, turn left, then run.
  public static BootstrapAnimationProfile referencePlayer() {
    return REFERENCE_PLAYER;
  }

  public BootstrapAnimationProfile {
    standSequenceId = normalizeSequenceId(standSequenceId);
    standTurnSequenceId = normalizeSequenceId(standTurnSequenceId);
    walkSequenceId = normalizeSequenceId(walkSequenceId);
    turnAroundSequenceId = normalizeSequenceId(turnAroundSequenceId);
    turnRightSequenceId = normalizeSequenceId(turnRightSequenceId);
    turnLeftSequenceId = normalizeSequenceId(turnLeftSequenceId);
    runSequenceId = normalizeSequenceId(runSequenceId);
  }

  private static int normalizeSequenceId(int sequenceId) {
    return sequenceId < -1 ? -1 : sequenceId;
  }
}
