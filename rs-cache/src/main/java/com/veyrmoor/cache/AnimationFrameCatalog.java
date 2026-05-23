package com.veyrmoor.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AnimationFrameCatalog {

  public static final int ANIMATION_FRAME_STORE_INDEX = 2;

  private final Map<Integer, AnimationFrameDefinition> framesById;

  private AnimationFrameCatalog(Map<Integer, AnimationFrameDefinition> framesById) {
    this.framesById = Map.copyOf(framesById);
  }

  public static AnimationFrameCatalog load(CacheStoreLayout cacheStoreLayout) {
    AnimationFrameDecoder decoder = new AnimationFrameDecoder();
    HashMap<Integer, AnimationFrameDefinition> framesById = new HashMap<>();
    try (CacheStoreReader reader = new CacheStoreReader(cacheStoreLayout)) {
      int archiveCount = reader.archiveCount(ANIMATION_FRAME_STORE_INDEX);
      for (int archiveId = 0; archiveId < archiveCount; archiveId++) {
        CacheIndexEntry indexEntry = reader.readIndexEntry(ANIMATION_FRAME_STORE_INDEX, archiveId);
        if (!indexEntry.exists()) {
          continue;
        }
        try {
          framesById.putAll(
              decoder.decode(
                  CacheCompression.decompressGzipIfNeeded(
                      reader.readArchive(ANIMATION_FRAME_STORE_INDEX, archiveId).bytes(),
                      "animation frame archive " + archiveId
                  )
              )
          );
        } catch (RuntimeException runtimeException) {
          throw new IllegalStateException("Failed to decode animation frame archive " + archiveId, runtimeException);
        }
      }
    } catch (Exception exception) {
      throw new IllegalStateException("Failed to load animation frames from cache store", exception);
    }
    return new AnimationFrameCatalog(framesById);
  }

  public Optional<AnimationFrameDefinition> find(int frameId) {
    return Optional.ofNullable(framesById.get(frameId));
  }

  public AnimationFrameDefinition require(int frameId) {
    return find(frameId).orElseThrow(() -> new IllegalArgumentException("Unknown animation frame " + frameId));
  }
}
