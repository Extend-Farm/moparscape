## Objective

Bring the native `rs-client-lwjgl` terrain and minimap stack to legacy-equivalent behavior without importing or executing legacy runtime code.

This note is the active task plan for:

- terrain scene assembly and emission parity
- terrain lighting, texturing, and shaped-tile behavior
- minimap base raster parity
- minimap radar overlay and rotation parity

## Current status

The native client is past the "load some cache data and draw a rough world" stage.

Terrain already has meaningful native groundwork:

- raw underlay and overlay ids now survive scene capture
- bridge-lowered surface-plane semantics exist
- terrain keeps overlay shape and rotation
- tile paint vs tile model split exists
- paint tiles now use the legacy north-east/south-west diagonal
- overlay floor textures are split from underlay Gouraud terrain
- flat paint tiles no longer invent `tileColor` fallback surfaces when the overlay branch resolves hidden
- palette-derived floor corner lighting now reuses the reference HSL `checkedLight(..., brightness)` path instead of RGB-only relight approximation
- shared-corner floor colors now stay in reference HSL space when every contributing tile color came from the floor palette
- bridge-lower terrain is emitted
- terrain occlusion flags and horizontal roof-plane occluders now come from terrain data instead of object heuristics
- the visibility planner now uses a first roof-plane rule instead of treating roof flags as a blanket visibility hint

Minimap already has meaningful native groundwork:

- 4x4 tile block raster exists
- shaped overlay masks exist
- flat and shaped hidden-overlay minimap tiles now preserve the legacy "leave untouched" behavior instead of inventing fallback RGB
- textured overlay floors now use legacy-style texture average colors for tile/minimap base output instead of only the water special-case
- explicit bridge-above minimap overpaint from `(flags[plane + 1] & 8)` now exists
- bridge-lowered minimap tiles now paint from the promoted visible surface plane instead of the stored under-bridge water layer, so Lumbridge-style wooden and stone bridge decks no longer collapse back to river blue
- wall and diagonal wall marks exist
- `mapscene` sprite stamping exists
- minimap object marks now follow legacy `method50(...)` query order: boundary, interactive, then ground decoration
- rotated multi-tile `mapscene` sprites now center from raw definition footprint dimensions instead of swapped placement footprint dimensions
- `mapscene` raster now preserves trimmed sprite bounds plus draw offsets instead of centering on full-canvas decoded images
- same-tile boundary/interactable/ground-decoration `mapscene` precedence is now regression-covered
- `mapfunction` markers are now cached scene state from ground-decoration placements instead of being rediscovered from generic object iteration during every radar draw
- cached `mapfunction` markers now apply the legacy post-collection drift contract for non-excluded ids, using native scene wall/object blockers so water-style icons do not stay pinned to the raw object tile
- wall / door / diagonal minimap marks now follow legacy `method50(...)` shape rules: type `1` stays invisible, type `3` is a one-pixel corner cap, type `2` keeps both legs at the same color, horizontal wall orientations are no longer flipped, and interactive doors / diagonals render in legacy red
- compass and minimap both use `mapback`-derived clip masks
- the minimap image is rotated into the radar instead of shown as a static crop
- radar and compass rotation now use the legacy yaw sign instead of the earlier viewport-compensation sign
- `mapfunction` radar markers now mirror legacy `method141(...)` distance bands: direct blit when near, masked `mapback`-clipped blit when farther out, and hidden past the legacy radius cutoff
- rotated minimap sampling now uses a fixed default zoom baseline near the midpoint of legacy `anInt1170` drift instead of a hardcoded 1:1 source step, and marker projection uses the matching inverse scale so icons stay registered to the terrain
- minimap and terrain builders now share one scene-tile form contract for flat paint vs textured paint vs shaped tile-model selection

That means the next work is no longer broad cache/bootstrap work. It is parity work against the legacy scene and minimap contracts.

## Legacy reference map

Use these legacy methods as the behavior target.

Terrain:

- `server/moparscape/.../MapRegion.method171(...)`
  - floor color blending
  - terrain light and shadow values
  - underlay/overlay tile submission
  - `SceneTilePaint` vs `SceneTileModel` split
  - terrain occlusion bit generation
- `server/moparscape/.../SceneGraph.method279(...)`
  - actual tile-paint/tile-model scene object construction
- `server/moparscape/.../SceneTilePaint`
  - paint-triangle texture anchor behavior
- `server/moparscape/.../SceneTileModel`
  - shaped tile vertex, color, and texture-triangle contract
- `server/moparscape/.../SceneGraph.method315(...)`
  - tile-paint render contract
- `server/moparscape/.../SceneGraph.method316(...)`
  - tile-model render contract

Minimap:

- `server/moparscape/.../GameClientCore.method24(...)`
  - minimap base generation from scene state
  - mapscene discovery
  - mapfunction collection
- `server/moparscape/.../GameClientCore.method50(...)`
  - wall, diagonal wall, and object edge marks
- `server/moparscape/.../SceneGraph.method309(...)`
  - terrain contribution into minimap pixels

## Reverse-engineered legacy pipeline

### Terrain scene construction

`MapRegion.method171(...)` is the real source of truth for the floor contract.

It does all of this in one pass:

- computes per-corner slope light into `anIntArrayArray139`
- keeps a sliding 5-tile floor-definition window for underlay hue/saturation/luminance blending
- resolves one blended underlay HSL and one resolved overlay color/texture contract per tile
- feeds `SceneGraph.method279(...)` with either:
  - plain paint tile
  - textured paint tile
  - shaped tile model
- sets terrain occlusion bits in `anIntArrayArrayArray135`

Important implications:

- the legacy terrain contract is HSL-first, not RGB-first
- underlay blending is neighborhood-based, not only per-tile
- overlay floors can produce:
  - texture-backed terrain
  - color-backed terrain
  - hidden / transparent terrain
- the minimap and the viewport are both downstream of this same scene-tile decision

More exact legacy rules:

- emission only starts when `underlayId > 0 || overlayId > 0`
- `overlayId == 0` means pure underlay paint
- `overlayId > 0 && overlayShape + 1 == 1` means full overlay paint
- `overlayId > 0 && overlayShape + 1 > 1` means shaped tile model
- overlay texture candidacy is overlay-only; underlays are never terrain textures in the legacy floor contract
- transparent overlay sentinel `rgb == 0xFF00FF` is not “fallback to underlay”; it keeps the overlay branch but resolves to hidden output

### Scene tile forms

`SceneGraph.method279(...)` creates three distinct scene tile forms:

- `l == 0`
  - plain `SceneTilePaint`
  - no texture id
- `l == 1`
  - textured `SceneTilePaint`
  - carries texture id and the flat-quad equality flag
- `l >= 2`
  - `SceneTileModel`
  - carries shape id, rotation, vertex heights, color triplets, and optional texture ids

Important implication:

- minimap parity should not invent a separate terrain interpretation if the scene tile form is already known

### Minimap base generation

The base minimap image is assembled in `GameClientCore.method24(...)`.

That method:

- clears the 512x512 minimap raster
- iterates tiles on the current plane
- also draws the bridge-above tile if `plane < 3` and `(tileFlags[plane + 1] & 8) != 0`
- asks `SceneGraph.method309(...)` to paint each tile into the minimap buffer
- runs `method50(...)` afterwards to stamp wall marks and `mapscene` sprites
- collects `mapfunction` icons from `method303(...)` object lookups, not from generic object iteration
- applies the bridge-above rule to both the terrain paint pass and the `method50(...)` mark pass

Important implications:

- the minimap base is scene-driven, not a second independent floor/object rasterizer
- bridge-above minimap painting is explicit in the legacy path
- wall marks and `mapscene` stamping happen after terrain fill, not inline with it
- `mapfunction` icons are cached output from the base rebuild, not discovered fresh during every radar draw

### Minimap tile raster rules

`SceneGraph.method309(...)` explains the exact minimap tile semantics:

- `SceneTilePaint`
  - fills a solid 4x4 block from `anInt722`
- `SceneTileModel`
  - uses shape-pattern lookup arrays
  - chooses between two colors:
    - `anInt686`
    - `anInt687`
  - if `anInt686 == 0`, only overlay pixels are written and the rest of the tile is left untouched

Important implication:

- shaped minimap masks are not just "overlay vs underlay color"
- there is an explicit "leave previous pixel intact" path that the native minimap still approximates too aggressively

### Minimap overlay and radar composition

`GameClientCore.method126(...)` is the radar composition stage.

It:

- rotates the prebuilt minimap buffer around the player
- masks it through the `mapback` cutout
- draws collected `mapfunction` icons
- draws ground-item dots
- draws NPC dots
- draws player dots with relationship-specific icons
- draws hint / destination markers
- draws the self marker
- draws the compass separately through its own masked rotation path

Important implication:

- "minimap parity" is two separate problems:
  - base scene raster
  - final radar overlay composition

### Mapfunction collection contract

Legacy `mapfunction` icons are not collected from generic visible scene objects.

`GameClientCore.method24(...)`:

- scans all tiles
- queries `SceneGraph.method303(...)`, which is the ground-decoration lookup
- resolves `ObjectDefinition.anInt746`
- stores the chosen sprite and tile coordinates into cached arrays
- randomly nudges most icons within a `3-tile` box, except several excluded ids

Important implication:

- native minimap parity should not derive `mapfunction` markers only from the current `WorldSceneObject` list or mainly from one object type; it needs a dedicated collection contract

### Radar overlay draw order

Legacy `method126(...)` draw order is:

1. rotated minimap base
2. rotated compass
3. cached `mapfunction` icons
4. ground-item dots
5. NPC dots
6. player dots
7. blinking hint markers
8. destination marker
9. local-player center square

Important implication:

- minimap overlay parity is not just "draw some icons on top". The draw order affects precedence and visibility at the minimap edge.

### Marker clipping contract

Legacy overlay markers do not all clip the same way.

- near markers use the plain `method141(...)` path
- farther markers use a masked blit path against the `mapback` scanline mask
- hint markers are special:
  - nearby: normal hint sprite
  - mid-range: edge arrow through `method81(...)`
  - far: nothing

Important implication:

- native minimap parity should not edge-clamp or uniformly mask every marker type

## Native ownership

The current native ownership split is correct and should be preserved:

- `CacheBackedWorldSceneLoader`
  - scene capture
- `world.terrain.*`
  - terrain data interpretation
  - terrain mesh/minimap emission
- `world.visibility.*`
  - plane, roof, and occluder behavior
- `world.minimap.*`
  - minimap base image and local radar crop
- `gameplay/GameplayMinimapRenderer`
  - final radar composition inside gameplay chrome

Do not collapse this back into `OpenGlTileRenderSystem` or `GameplayChromeRenderer`.

## Completed groundwork

Terrain:

- preserved raw underlay/overlay ids in scene state
- promoted raw overlay type `1` into the first curved mask shape
- kept shaped overlay masks alive even when only raw overlay identity survives
- stopped flat paint tiles from falling back to visible RGB when the legacy overlay branch should stay hidden
- split shaped textured overlay triangles away from Gouraud underlay triangles
- stopped synthesizing missing shaped-tile halves when only one terrain surface is actually renderable
- extracted terrain occlusion flags from the legacy flat-upper-tile rule
- moved horizontal occluders to terrain-driven extraction

Minimap:

- rasterizes 4x4 tile blocks
- preserves shaped tile masks and rotations
- keeps raw floor ids through minimap rasterization so transparent overlay branches can stay hidden
- uses texture-derived floor colors for textured overlay minimap and tile-base output
- paints the current plane first and then explicitly overpaints bridge-above tiles from the next plane flag rule
- stamps `mapscene` sprites from scene metadata
- draws wall marks from scene object metadata
- uses `mapback` scanline masks for both compass and radar
- rotates the radar around the player instead of drawing a static atlas crop
- resolves `SceneTilePaint` vs `SceneTileModel` form once and reuses that decision across viewport terrain and minimap base paths

## Remaining parity gaps

### Terrain data and lighting

1. Floor color resolution is still RGB-first in several native seams.
   Legacy `MapRegion.method171(...)` operates in HSL space and only converts through the legacy palette late.

   More exact target:
   - viewport terrain stores per-corner or per-triangle HSL-derived values
   - minimap colors are a separate RGB side-channel
   - native currently conflates those two outputs too early

2. Hidden or transparent terrain cases are still approximated.
   Important legacy sentinels:
   - `-1`
   - `-2`
   - `0xbc614e`
   Native code still treats some of those as ordinary fallback RGB instead of explicit "do not render this surface" behavior.

  Specific current hole:
   - viewport-side textured/color fallbacks still need exact sentinel checking beyond the flat-paint and minimap hidden-overlay fixes

3. Shared-corner terrain color and brightness behavior still needs byte-for-byte checking against `anIntArrayArray139`-driven reference shading.
   Palette-derived stored floor RGB now goes back through the reference HSL lightness path during corner relight, and palette-backed shared corners now average in HSL before lighting, but mixed or non-palette colors still use approximation fallback.

### Terrain submission

1. Tile paint parity is still incomplete.
   The native paint path does not yet mirror the full `SceneTilePaint` contract for:
   - hidden overlay cases
   - exact texture-anchor selection on flat vs sloped tiles
   - texture-vs-color fallbacks when floor definitions resolve special sentinels

2. Tile model parity is still incomplete.
   The native shaped-tile builder has the right general structure, but still needs exact checking against `SceneTileModel` for:
   - vertex point ordering
   - rotated triangle indices
   - triangle color source selection
   - per-triangle texture anchor selection
   - missing-surface omission rules

3. Terrain raster parity is still below the legacy renderer.
   Even when submission is right, final viewport differences can still come from raster behavior in:
   - textured triangle interpolation
   - Gouraud interpolation
   - clipping and face ordering

### Minimap base raster

1. The minimap base is still rasterized from simplified native terrain/object state, not the legacy scene graph contract.
   It should be verified against the combined effect of:
   - `SceneGraph.method309(...)`
   - `GameClientCore.method50(...)`

   Progress:
   - tile-form selection is now shared with the terrain builders instead of being re-derived separately inside the minimap rasterizer

2. Transparent and special overlay cases on the minimap still need parity checks.
   The current minimap now preserves hidden flat/shaped overlay paths and uses texture-derived colors for textured overlays, but it may still disagree on some special floor-definition branches and exact scene-tile form reuse.

   Specific missing rule:
   - when the legacy `SceneTileModel` has `anInt686 == 0`, only the overlay-mask pixels are written and the rest of the 4x4 tile stays unchanged
   - remaining special overlay floor definitions still need direct parity checks against the legacy scene-tile output

3. `mapscene` behavior is now covered for:
   - category query order
   - rotated raw-footprint centering
   - trimmed sprite bounds and draw offsets
   - same-tile overlap precedence across boundary/interactable/ground-decoration passes

### Minimap radar overlays

1. `mapfunction` support is still partial.
   The native path now caches markers from the ground-decoration contract, but it does not yet prove parity with the full legacy icon collection rules.

   Specific mismatch:
   - legacy still applies special-case exclusions and random position drift for some icons after collection
   - native still does not preserve the exact collision-aware drift rules from the legacy scene rebuild

2. Entity and item radar dots are still outside the native parity plan.
   The full legacy radar also overlays:
   - players
   - NPCs
   - hint markers
   - ground-item dots

3. Final minimap transform parity still needs checking.
   The current radar rotates the scene crop around the player, but legacy parity still requires checking:
   - rotation sign and anchor
   - icon rotation coupling
   - fixed visible-radius rules

## Ordered workstreams

### Workstream 1. Terrain floor contract

Goal:
- make the native floor color and texture decision match `MapRegion.method171(...)`

Files:
- `world/terrain/FloorSurfaceColorResolver.java`
- `world/terrain/TerrainTileColorResolver.java`
- `world/terrain/TerrainSceneMeshBuilder.java`

Tasks:
- audit every use of fallback RGB against legacy `method185(...)` and `method187(...)`
- introduce explicit non-render / hidden-surface handling instead of color fallbacks where the legacy client would suppress a surface
- verify underlay-vs-overlay texture candidacy rules on both paint tiles and shaped tiles
- remove underlay-texture thinking from terrain-floor parity work; legacy terrain texturing is overlay-only

Exit condition:
- no shaped or flat terrain tile invents a surface the legacy client would keep hidden

### Workstream 2. Terrain submission parity

Goal:
- make native paint/model emission structurally match `SceneTilePaint` and `SceneTileModel`

Files:
- `world/terrain/TerrainSceneMeshBuilder.java`
- `world/terrain/TerrainShapeDefinitions.java`
- related tests under `world/terrain/`

Tasks:
- verify every supported shape against `SceneTileModel`
- pin triangle count, color source, and texture-anchor behavior per shape/rotation
- add slope and bridge cases, not only flat-tile tests

Exit condition:
- shape-by-shape tests demonstrate the same emitted triangles, anchors, and surface omissions as the legacy contract

### Workstream 3. Terrain-to-viewport parity

Goal:
- separate submission mistakes from raster mistakes

Files:
- `world/WorldSceneSubmissionBuilder.java`
- `world/raster/*`
- `docs/native-rasterizer-parity.md`

Tasks:
- compare terrain-only captures between legacy and native at the same tile/camera
- classify remaining defects as scene-input vs submission vs raster
- avoid papering over raster defects inside terrain builders

Exit condition:
- remaining terrain mismatches are explicitly classified and no longer mixed together

### Workstream 4. Minimap base parity

Goal:
- make the minimap background behave like legacy `method24(...)` plus `method50(...)`

Files:
- `world/minimap/WorldSceneMinimapRasterizer.java`
- `world/CacheBackedWorldSceneLoader.java`
- minimap tests

Tasks:
- decide whether the native minimap should continue rasterizing from `WorldScene` directly or whether it should preserve a scene-tile intermediate equivalent to `SceneTilePaint` / `SceneTileModel`
- verify 4x4 tile fill, shaped masks, water tint, and transparent overlay behavior
- add the explicit "leave pixel untouched" shaped-mask path where the legacy tile model uses `anInt686 == 0`
- verify wall and diagonal-wall edge rules against `method50(...)`
- verify any remaining `mapscene` precedence edge cases beyond placement/centering/bounds
- add tests for multi-tile objects and overlapping wall/mapscene combinations

Exit condition:
- base minimap raster matches legacy tile, wall, and `mapscene` behavior for controlled cases

### Workstream 5. Radar overlay parity

Goal:
- make the final minimap panel behave like the legacy rotating radar instead of only a rotated crop

Files:
- `gameplay/GameplayMinimapRenderer.java`
- `world/minimap/MinimapViewportPlanner.java`
- `gameplay/GameplayFrameAssetLoader.java`

Tasks:
- keep `mapfunction` sprite lookup pinned to the real media-archive contract; in this cache layout raw ids map directly to frames `0..60`
- add the legacy exclusion and collision-aware drift rules for cached `mapfunction` markers
- add player/NPC/item/hint overlay plan once scene/runtime state is available
- verify radar clipping and rotation around the masked minimap aperture

Exit condition:
- minimap panel behavior is planned as a separate overlay/radar problem, not hidden inside base-raster work

### Workstream 6. Verification harness

Goal:
- keep terrain and minimap work measurable

Tasks:
- keep cache-backed unit tests for shaped tiles, bridge tiles, minimap masks, wall marks, and `mapscene`
- add a repeatable screenshot capture checklist for:
  - flat terrain
  - shaped overlay terrain
  - bridge/water edges
  - minimap base
  - minimap radar

Exit condition:
- each parity slice has a stable regression target and does not depend on memory or screenshots alone

## Verification performed

Relevant verified groundwork already in tree:

- terrain scene builder tests
- terrain occlusion flag tests
- minimap rasterizer tests
- minimap viewport planner tests
- world submission and visibility tests

Most recent terrain verification:

- `./gradlew :rs-client-lwjgl:compileJava :rs-client-lwjgl:compileTestJava`
- `./gradlew :rs-client-lwjgl:test --tests 'io.github.ffakira.rsps.client.desktop.world.terrain.*'`

## Known blockers

1. The repo worktree is intentionally dirty across character, itemicon, cache, and world slices.
   Terrain/minimap work must stay narrow and must not revert unrelated files.

2. Full viewport parity still depends on raster-layer work.
   Not every terrain mismatch is actually a terrain-data bug anymore.

3. Full radar parity depends on richer live scene/runtime overlays that the native client does not yet surface completely.

## Next recommended step

Start with Workstream 1, specifically:

- audit the flat paint path against legacy `SceneTilePaint`
- make transparent or hidden overlay cases explicit instead of relying on RGB fallback
- add matching minimap tests for the same overlay cases so terrain and minimap do not diverge again
