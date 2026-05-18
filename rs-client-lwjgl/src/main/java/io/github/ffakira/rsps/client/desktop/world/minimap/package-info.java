/**
 * Minimap-only world rendering support.
 *
 * <p>This package owns the local radar crop, the `mapback`-derived clip masks, and the scene-state
 * minimap rasterization path. It is intentionally separate from the main viewport terrain/object
 * pipeline so minimap parity can evolve without pushing more world-map logic into the flat LWJGL
 * root package.
 */
package io.github.ffakira.rsps.client.desktop.world.minimap;

