package com.veyrmoor.model;

public enum MovementMode {
  WALK(0),
  RUN(1);

  private final int protocolId;

  MovementMode(int protocolId) {
    this.protocolId = protocolId;
  }

  public int protocolId() {
    return protocolId;
  }

  public static MovementMode fromProtocolId(int protocolId) {
    return switch (protocolId) {
      case 0 -> WALK;
      case 1 -> RUN;
      default -> throw new IllegalArgumentException("Unknown movement mode protocol id: " + protocolId);
    };
  }
}
