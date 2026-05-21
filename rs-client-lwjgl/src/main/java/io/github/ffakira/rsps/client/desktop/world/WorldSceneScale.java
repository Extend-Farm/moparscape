package io.github.ffakira.rsps.client.desktop.world;

public final class WorldSceneScale {

  // Cache-backed terrain elevations are stored in WorldScene as `legacyHeight / 12`. Convert them
  // back into the same tile-relative unit system the models use (`128` legacy units per tile) so
  // terrain, objects, clicks, and camera math all share one faithful world-space scale.
  public static final float HEIGHT_SCALE = 12.0f / 128.0f;

  private WorldSceneScale() {
  }
}
