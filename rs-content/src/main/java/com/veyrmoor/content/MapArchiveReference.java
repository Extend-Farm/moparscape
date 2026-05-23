package com.veyrmoor.content;

public record MapArchiveReference(
    int regionId,
    int terrainArchiveId,
    int objectArchiveId,
    boolean membersOnly
) {

  public int regionX() {
    return regionId >> 8 & 0xff;
  }

  public int regionY() {
    return regionId & 0xff;
  }
}
