package io.github.ffakira.rsps.cache;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RawModelRepository {

  public static final int MODEL_STORE_INDEX = 1;

  private final CacheStoreLayout cacheStoreLayout;
  private final RawModelDecoder decoder = new RawModelDecoder();
  private final Map<Integer, RawModelData> modelsById = new ConcurrentHashMap<>();

  public RawModelRepository(CacheStoreLayout cacheStoreLayout) {
    this.cacheStoreLayout = cacheStoreLayout;
  }

  public RawModelData loadModel(int modelId) {
    return modelsById.computeIfAbsent(modelId, this::readAndDecodeModel);
  }

  private RawModelData readAndDecodeModel(int modelId) {
    try (CacheStoreReader reader = new CacheStoreReader(cacheStoreLayout)) {
      byte[] bytes = reader.readArchive(MODEL_STORE_INDEX, modelId).bytes();
      return decoder.decode(CacheCompression.decompressGzipIfNeeded(bytes, "model archive"));
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to close cache reader for model " + modelId, ioException);
    } catch (RuntimeException runtimeException) {
      throw new IllegalStateException("Failed to load model " + modelId, runtimeException);
    }
  }
}
