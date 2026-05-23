package com.veyrmoor.cache;

public record CacheIndexEntry(int archiveId, int length, int firstSector) {

  public boolean exists() {
    return length > 0 && firstSector > 0;
  }
}
