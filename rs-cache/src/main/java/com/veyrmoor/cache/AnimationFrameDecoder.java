package com.veyrmoor.cache;

import java.util.HashMap;
import java.util.Map;

final class AnimationFrameDecoder {

  private static final int FOOTER_LENGTH = 8;
  private static final int MAX_TRANSFORMATIONS = 500;

  Map<Integer, AnimationFrameDefinition> decode(byte[] bytes) {
    if (bytes.length < FOOTER_LENGTH) {
      throw new IllegalArgumentException("Animation frame archive is too short: " + bytes.length);
    }

    // Reference-client frame archives are split into index/flags/transform/duration/base streams
    // and stitched back together through the footer lengths here.
    CacheDataReader footerReader = new CacheDataReader(bytes);
    footerReader.position(bytes.length - FOOTER_LENGTH);
    int indexSectionLength = footerReader.readUnsignedShort();
    int flagsSectionLength = footerReader.readUnsignedShort();
    int transformSectionLength = footerReader.readUnsignedShort();
    int durationSectionLength = footerReader.readUnsignedShort();

    int offset = 0;
    CacheDataReader indexReader = new CacheDataReader(bytes);
    indexReader.position(offset);
    offset += indexSectionLength + 2;

    CacheDataReader flagsReader = new CacheDataReader(bytes);
    flagsReader.position(offset);
    offset += flagsSectionLength;

    CacheDataReader transformReader = new CacheDataReader(bytes);
    transformReader.position(offset);
    offset += transformSectionLength;

    CacheDataReader durationReader = new CacheDataReader(bytes);
    durationReader.position(offset);
    offset += durationSectionLength;

    AnimationFrameBase base = readBase(bytes, offset);
    int frameCount = indexReader.readUnsignedShort();
    HashMap<Integer, AnimationFrameDefinition> framesById = new HashMap<>(frameCount * 2);
    int[] indices = new int[MAX_TRANSFORMATIONS];
    int[] transformX = new int[MAX_TRANSFORMATIONS];
    int[] transformY = new int[MAX_TRANSFORMATIONS];
    int[] transformZ = new int[MAX_TRANSFORMATIONS];
    for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {
      int frameId = indexReader.readUnsignedShort();
      int duration = durationReader.readUnsignedByte();
      int transformationSlots = indexReader.readUnsignedByte();
      int transformationCount = 0;
      int lastNonZeroType = -1;
      for (int slotIndex = 0; slotIndex < transformationSlots; slotIndex++) {
        int flags = flagsReader.readUnsignedByte();
        if (flags <= 0) {
          continue;
        }
        if (base.transformationTypes()[slotIndex] != 0) {
          for (int missingIndex = slotIndex - 1; missingIndex > lastNonZeroType; missingIndex--) {
            if (base.transformationTypes()[missingIndex] != 0) {
              continue;
            }
            indices[transformationCount] = missingIndex;
            transformX[transformationCount] = 0;
            transformY[transformationCount] = 0;
            transformZ[transformationCount] = 0;
            transformationCount++;
            break;
          }
        }
        indices[transformationCount] = slotIndex;
        int defaultValue = base.transformationTypes()[slotIndex] == 3 ? 128 : 0;
        transformX[transformationCount] = (flags & 1) != 0 ? transformReader.readSignedSmart() : defaultValue;
        transformY[transformationCount] = (flags & 2) != 0 ? transformReader.readSignedSmart() : defaultValue;
        transformZ[transformationCount] = (flags & 4) != 0 ? transformReader.readSignedSmart() : defaultValue;
        lastNonZeroType = slotIndex;
        transformationCount++;
      }
      framesById.put(
          frameId,
          new AnimationFrameDefinition(
              duration,
              base,
              transformationCount,
              copyOf(indices, transformationCount),
              copyOf(transformX, transformationCount),
              copyOf(transformY, transformationCount),
              copyOf(transformZ, transformationCount)
          )
      );
    }
    return framesById;
  }

  private AnimationFrameBase readBase(byte[] bytes, int offset) {
    try {
      CacheDataReader separatedReader = new CacheDataReader(bytes);
      separatedReader.position(offset);
      return readSeparatedBase(separatedReader);
    } catch (RuntimeException separatedFailure) {
      CacheDataReader interleavedReader = new CacheDataReader(bytes);
      interleavedReader.position(offset);
      try {
        return readInterleavedBase(interleavedReader);
      } catch (RuntimeException interleavedFailure) {
        interleavedFailure.addSuppressed(separatedFailure);
        throw interleavedFailure;
      }
    }
  }

  private AnimationFrameBase readSeparatedBase(CacheDataReader reader) {
    int labelCount = reader.readUnsignedByte();
    int[] transformationTypes = new int[labelCount];
    int[][] skinList = new int[labelCount][];
    for (int index = 0; index < labelCount; index++) {
      transformationTypes[index] = reader.readUnsignedByte();
    }
    for (int labelIndex = 0; labelIndex < labelCount; labelIndex++) {
      skinList[labelIndex] = new int[reader.readUnsignedByte()];
    }
    for (int labelIndex = 0; labelIndex < labelCount; labelIndex++) {
      for (int index = 0; index < skinList[labelIndex].length; index++) {
        skinList[labelIndex][index] = reader.readUnsignedByte();
      }
    }
    return new AnimationFrameBase(transformationTypes, skinList);
  }

  private AnimationFrameBase readInterleavedBase(CacheDataReader reader) {
    int labelCount = reader.readUnsignedByte();
    int[] transformationTypes = new int[labelCount];
    int[][] skinList = new int[labelCount][];
    for (int index = 0; index < labelCount; index++) {
      transformationTypes[index] = reader.readUnsignedByte();
    }
    for (int labelIndex = 0; labelIndex < labelCount; labelIndex++) {
      int labelSize = reader.readUnsignedByte();
      skinList[labelIndex] = new int[labelSize];
      for (int index = 0; index < labelSize; index++) {
        skinList[labelIndex][index] = reader.readUnsignedByte();
      }
    }
    return new AnimationFrameBase(transformationTypes, skinList);
  }

  private int[] copyOf(int[] values, int length) {
    int[] copy = new int[length];
    System.arraycopy(values, 0, copy, 0, length);
    return copy;
  }
}
