package com.veyrmoor.client.desktop.gameplay;

final class GameplayCursorMarker {

  static final int FRAME_COUNT = 4;
  private static final long FRAME_DURATION_NANOS = 100_000_000L;
  private static final long TOTAL_DURATION_NANOS = FRAME_COUNT * FRAME_DURATION_NANOS;

  private GameplayMouseButton button;
  private float centerX;
  private float centerY;
  private long activatedAtNanos = Long.MIN_VALUE;

  void activate(GameplayMouseButton button, double centerX, double centerY) {
    activate(button, centerX, centerY, System.nanoTime());
  }

  void activate(GameplayMouseButton button, double centerX, double centerY, long nowNanos) {
    this.button = button;
    this.centerX = (float) centerX;
    this.centerY = (float) centerY;
    activatedAtNanos = nowNanos;
  }

  Snapshot snapshot() {
    return snapshot(System.nanoTime());
  }

  Snapshot snapshot(long nowNanos) {
    if (button == null) {
      return null;
    }
    long elapsedNanos = Math.max(0L, nowNanos - activatedAtNanos);
    if (elapsedNanos >= TOTAL_DURATION_NANOS) {
      clear();
      return null;
    }
    return new Snapshot(button, centerX, centerY, (int) (elapsedNanos / FRAME_DURATION_NANOS));
  }

  private void clear() {
    button = null;
    activatedAtNanos = Long.MIN_VALUE;
  }

  record Snapshot(GameplayMouseButton button, float centerX, float centerY, int frameIndex) {
  }
}
