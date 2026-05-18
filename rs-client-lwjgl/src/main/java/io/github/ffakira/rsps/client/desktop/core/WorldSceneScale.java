package io.github.ffakira.rsps.client.desktop.core;

public final class WorldSceneScale {

  // The earlier native height exaggeration made the viewport read like a stitched terrain preview
  // instead of RuneScape's flatter play scene. Keep one shared scale so terrain, clicks, and
  // occlusion all agree on the same world surface.
  public static final float HEIGHT_SCALE = 0.12f;

  private WorldSceneScale() {
  }
}
