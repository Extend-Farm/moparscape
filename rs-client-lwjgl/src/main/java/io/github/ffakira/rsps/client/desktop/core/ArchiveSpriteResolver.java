package io.github.ffakira.rsps.client.desktop.core;

import io.github.ffakira.rsps.cache.CacheArchiveContainer;
import java.util.HashMap;
import java.util.Map;

public final class ArchiveSpriteResolver {

  private final SpriteLookup spriteLookup;
  private final Map<SpriteKey, ArgbImage> decodedSprites = new HashMap<>();

  public ArchiveSpriteResolver(CacheArchiveContainer archive) {
    this(createSpriteLookup(archive));
  }

  ArchiveSpriteResolver(SpriteLookup spriteLookup) {
    this.spriteLookup = spriteLookup;
  }

  public ArgbImage resolve(String entryName, int frameIndex) {
    if (entryName == null || entryName.isBlank() || frameIndex < 0) {
      return null;
    }
    SpriteKey key = new SpriteKey(entryName, frameIndex);
    if (decodedSprites.containsKey(key)) {
      return decodedSprites.get(key);
    }
    ArgbImage image;
    try {
      image = spriteLookup.decode(key.entryName(), key.frameIndex());
    } catch (RuntimeException exception) {
      image = null;
    }
    decodedSprites.put(key, image);
    return image;
  }

  private static SpriteLookup createSpriteLookup(CacheArchiveContainer archive) {
    TitleArchiveSpriteDecoder spriteDecoder = new TitleArchiveSpriteDecoder(archive);
    return (entryName, frameIndex) -> spriteDecoder.decodeSprite(archive, entryName, frameIndex, false);
  }

  private record SpriteKey(String entryName, int frameIndex) {
  }

  @FunctionalInterface
  interface SpriteLookup {
    ArgbImage decode(String entryName, int frameIndex);
  }
}
