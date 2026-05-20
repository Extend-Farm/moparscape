package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.AnimationFrameCatalog;
import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AnimationSequenceCatalog {

  private static final String SEQUENCE_ENTRY = "seq.dat";
  private static final int INTERLEAVE_SENTINEL = 0x98967f;

  private final Map<Integer, AnimationSequenceDefinition> sequencesById;

  private AnimationSequenceCatalog(Map<Integer, AnimationSequenceDefinition> sequencesById) {
    this.sequencesById = Map.copyOf(sequencesById);
  }

  public static AnimationSequenceCatalog load(ContentManifest manifest, AnimationFrameCatalog animationFrames) {
    CacheArchiveContainer configArchive = new ContentArchiveCatalog().load(manifest).configArchive();
    SequenceDataReader reader = new SequenceDataReader(configArchive.readEntry(SEQUENCE_ENTRY));
    int sequenceCount = reader.readUnsignedShort();
    HashMap<Integer, AnimationSequenceDefinition> sequencesById = new HashMap<>(sequenceCount * 2);
    for (int sequenceId = 0; sequenceId < sequenceCount; sequenceId++) {
      sequencesById.put(sequenceId, decodeSequence(reader, animationFrames));
    }
    return new AnimationSequenceCatalog(sequencesById);
  }

  public Optional<AnimationSequenceDefinition> find(int sequenceId) {
    return Optional.ofNullable(sequencesById.get(sequenceId));
  }

  public AnimationSequenceDefinition require(int sequenceId) {
    return find(sequenceId).orElseThrow(() -> new IllegalArgumentException("Unknown animation sequence " + sequenceId));
  }

  private static AnimationSequenceDefinition decodeSequence(
      SequenceDataReader reader,
      AnimationFrameCatalog animationFrames
  ) {
    int frameCount = 0;
    int[] primaryFrameIds = new int[]{-1};
    int[] secondaryFrameIds = new int[]{-1};
    int[] frameDurations = new int[]{1};
    int loopOffset = -1;
    int[] interleaveOrder = null;
    boolean stretches = false;
    int forcedPriority = 5;
    int playerOffhandAppearanceId = -1;
    int playerMainhandAppearanceId = -1;
    int maximumLoops = 99;
    int animatingPrecedence = -1;
    int priority = -1;
    int replayMode = 2;
    while (true) {
      int opcode = reader.readUnsignedByte();
      if (opcode == 0) {
        break;
      }
      switch (opcode) {
        case 1 -> {
          frameCount = reader.readUnsignedByte();
          primaryFrameIds = new int[frameCount];
          secondaryFrameIds = new int[frameCount];
          frameDurations = new int[frameCount];
          for (int index = 0; index < frameCount; index++) {
            primaryFrameIds[index] = reader.readUnsignedShort();
            int secondaryFrameId = reader.readUnsignedShort();
            secondaryFrameIds[index] = secondaryFrameId == 65535 ? -1 : secondaryFrameId;
            int decodedDuration = reader.readUnsignedShort();
            frameDurations[index] = resolveDuration(decodedDuration, primaryFrameIds[index], animationFrames);
          }
        }
        case 2 -> loopOffset = reader.readUnsignedShort();
        case 3 -> {
          int count = reader.readUnsignedByte();
          interleaveOrder = new int[count + 1];
          for (int index = 0; index < count; index++) {
            interleaveOrder[index] = reader.readUnsignedByte();
          }
          interleaveOrder[count] = INTERLEAVE_SENTINEL;
        }
        case 4 -> stretches = true;
        case 5 -> forcedPriority = reader.readUnsignedByte();
        case 6 -> playerOffhandAppearanceId = reader.readUnsignedShort();
        case 7 -> playerMainhandAppearanceId = reader.readUnsignedShort();
        case 8 -> maximumLoops = reader.readUnsignedByte();
        case 9 -> animatingPrecedence = reader.readUnsignedByte();
        case 10 -> priority = reader.readUnsignedByte();
        case 11 -> replayMode = reader.readUnsignedByte();
        case 12 -> reader.readUnsignedInt();
        default -> throw new IllegalStateException("Unsupported sequence opcode " + opcode);
      }
    }
    if (frameCount == 0) {
      frameCount = 1;
      primaryFrameIds = new int[]{-1};
      secondaryFrameIds = new int[]{-1};
      frameDurations = new int[]{1};
    }
    if (animatingPrecedence == -1) {
      animatingPrecedence = interleaveOrder == null ? 0 : 2;
    }
    if (priority == -1) {
      priority = interleaveOrder == null ? 0 : 2;
    }
    return new AnimationSequenceDefinition(
        frameCount,
        primaryFrameIds,
        secondaryFrameIds,
        frameDurations,
        loopOffset,
        interleaveOrder,
        stretches,
        forcedPriority,
        playerOffhandAppearanceId,
        playerMainhandAppearanceId,
        maximumLoops,
        animatingPrecedence,
        priority,
        replayMode
    );
  }

  private static int resolveDuration(int decodedDuration, int frameId, AnimationFrameCatalog animationFrames) {
    if (decodedDuration > 0) {
      return decodedDuration;
    }
    if (animationFrames == null || frameId < 0) {
      return 1;
    }
    return animationFrames.find(frameId)
        .map(frame -> Math.max(1, frame.duration()))
        .orElse(1);
  }

  private static final class SequenceDataReader {

    private final byte[] bytes;
    private int position;

    private SequenceDataReader(byte[] bytes) {
      this.bytes = bytes;
    }

    private int readUnsignedByte() {
      return bytes[position++] & 0xff;
    }

    private int readUnsignedShort() {
      int value = ((bytes[position] & 0xff) << 8) | (bytes[position + 1] & 0xff);
      position += 2;
      return value;
    }

    private int readUnsignedInt() {
      int value = readUnsignedByte() << 24;
      value |= readUnsignedByte() << 16;
      value |= readUnsignedByte() << 8;
      value |= readUnsignedByte();
      return value;
    }
  }
}
