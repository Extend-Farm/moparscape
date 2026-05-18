# Native Rasterizer Parity

This document captures the specific render/raster contract the native client still lacks.

It exists because the current viewport problem is no longer "we need better models" or "the camera
is wrong". The remaining gap is that the native client still bypasses the legacy scene submission
and rasterization flow.

## Rule

- legacy classes remain reference-only
- native runtime code must not import or execute the legacy renderer
- parity means reproducing the render contract, not embedding the old software pipeline wholesale

## Legacy Render Contract

The legacy world viewport is produced by this chain:

1. `GameClientCore.method146(...)`
   - computes camera state
   - resets the active raster target
   - calls into scene rendering
2. `SceneGraph.method313(...)`
   - selects visible tiles from camera position/pitch/yaw
   - traverses the scene graph in render order
3. `SceneGraph.method315(...)`
   - submits `SceneTilePaint` triangles
4. `SceneGraph.method316(...)`
   - submits `SceneTileModel` triangles
5. `Model.method483(...)`
   - submits object and actor faces
6. `Rasterizer3D.method374(...)`, `method376(...)`, `method378(...)`
   - flat, Gouraud, and textured triangle rasterization
7. `Rasterizer2D`
   - owns the active target buffer, clipping bounds, and final 2D writes

That means the legacy client is not just "drawing models". It is:

- selecting visible scene tiles
- submitting multiple scene primitive types
- ordering and clipping those primitives
- shading or texturing them through one raster backend

## Why The Native View Still Looks Wrong

Today the native client already has:

- cache-backed terrain heights/colors
- cache-backed object placement
- cache-backed static object model bytes
- native local-player body assembly
- a tighter player-follow camera than before

But it still draws the world as disconnected helpers:

- terrain mesh pass
- object geometry pass
- character geometry pass

This is enough to prove the assets and placement are real, but not enough to reproduce RuneScape's
play view. The missing pieces are:

- tile-paint submission
- tile-model submission
- scene-wide depth/overlap behavior
- shading/texture mode selection per submitted primitive
- one coherent render queue

## Native Target

The native client should converge on this contract:

1. scene assembly produces tile paints, tile models, static scene objects, and actor submissions
2. one scene submission layer emits those primitives in a stable render order
3. one raster layer handles:
   - flat triangles
   - Gouraud triangles
   - textured triangles
   - clipping
   - screen-space ordering rules
4. the viewport and minimap both depend on the same native scene state

The implementation does not have to be a literal port of the old software rasterizer. It can stay
on LWJGL/OpenGL as long as the render contract is reproduced.

## Integration Boundary

Current native status:

- `WorldSceneSubmissionBuilder` now emits an explicit `SceneRenderQueue`
- `WorldViewportRenderer` delegates queue execution to `OpenGlSceneRasterBackend`
- cache terrain shape/rotation now reaches that queue as distinct `TILE_PAINT` and `TILE_MODEL`
  batches
- overlay floor texture ids now reach native scene state and the world viewport now submits those
  overlay floors through the textured terrain path while keeping underlays on the Gouraud/lit-color path
- underlays stay on the lit-color path to match the legacy floor contract more closely
- native terrain now treats overlay shapes `1..12` as modeled tiles instead of skipping shape `1`
- terrain batches now execute through a native Gouraud-style color-interpolation path
- raw model face metadata now survives decode in `RawModelDecoder` and is carried into native scene
  object geometry
- static object batches now split by raster intent (`FLAT`, `GOURAUD`, `TEXTURED`) instead of
  collapsing everything to one flat triangle pass
- the raster backend now loads the native `textures` archive and executes textured static-object
  faces through a real OpenGL texture path instead of a color-only fallback
- textured faces now have a native UV fallback when cache-provided texture anchors are missing or
  degenerate, so static-object foliage and similar props can stay on the textured path instead of
  dropping back to dark Gouraud fallback faces
- actor geometry now preserves raw face raster modes, face alpha, and texture anchors, and native
  actor submissions split into `FLAT`, `GOURAUD`, and `TEXTURED` batches when cache-backed actor
  geometry is available
- terrain texturing now follows the legacy floor split more closely:
  - underlays stay Gouraud/lit-color
  - overlay floor definitions remain the only terrain texture candidates
  - the current viewport now emits textured terrain batches for those overlay floors instead of collapsing them back to Gouraud terrain
- terrain, queue submission, click-hit tests, and occluder sampling now share one lowered native
  height scale so the viewport stops exaggerating slopes differently across subsystems
- queue construction now applies a camera-centered submission-time visibility window so terrain and
  object batches stop covering the whole stitched region on every frame
- queue construction now also sees current-plane tile flags and a first `method182`-style effective
  plane lowering path, so bridge/roof semantics affect submission inputs before rasterization
- assembled scene data now also carries first-pass native occluders, and queue construction runs a
  plane-local object/actor occlusion pass against active wall occluders before emission
  - horizontal roof-plane occluders are intentionally withheld until render-plane parity exists
- the world viewport now makes its 3D surface assumptions explicit:
  - depth buffer requested at window creation
  - backface culling intentionally withheld until terrain/object winding parity exists
  - multisampling enabled during the world pass
  - tighter play-camera/frustum values instead of the earlier scene-overview framing
- the raster backend now executes in two composition passes:
  - opaque faces first without the blended path
  - translucent faces second with blending and depth writes disabled
  - textured faces keep alpha-test cutouts on both passes instead of forcing the whole scene through one blended stream

That means the queue/raster ownership boundary now exists in source and the first textured terrain,
textured object, and textured actor paths are live, but raster-mode parity still does not. The next
work should extend that backend rather than pushing triangle-mode logic back into the viewport or
scene builder.

The visibility window plus first-pass occluder rejection are only a first-stage native guardrail.
They are not yet the legacy `SceneGraph.method313(...)` + occluder system, even though the native
path now reacts to roof-path flags, bridge-lowered effective planes, and prebuilt scene
occluders. Remaining world defects should still be classified carefully as:

- scene-input issues
- submission/visibility issues
- raster/ordering issues

The next implementation slice should introduce an explicit boundary between:

- scene data
- submitted render primitives
- raster execution

The package target for that split is now explicit:

- `client.desktop.world.terrain`
- `client.desktop.world.object`
  - extracted
- `client.desktop.world.raster`
  - extracted
- `client.desktop.world.visibility`
- `client.desktop.world.minimap`

That boundary should be visible in both code ownership and documentation.

Recommended native split:

- scene builder:
  - owns tile/object/actor semantics
- scene submission:
  - owns queue construction
  - emits submitted tile paints, tile models, and model faces
- raster backend:
  - owns triangle mode selection, clipping, ordering, and screen-space emission
- viewport/chrome shell:
  - owns camera selection and render-target orchestration only

The important texture rule is also explicit now: loading textures is not enough. Texture ids, UV
planning, brightness/gradient rules, alpha/cutout behavior, and the final raster-mode execution
all belong to one coherent raster contract, not scattered local fallbacks inside terrain/object
builders.

This is the code-quality guardrail for the next slice. If `OpenGlTileRenderSystem` grows new
terrain/object/face submission loops directly, the native client will recreate the same god-class
problem under a different name.

## Current Parity Strategy

The native world mismatch is now large enough that isolated visual tweaks are low-yield. The active
strategy must be an end-to-end parity pass over submission and raster behavior, not another run of
local terrain or camera adjustments.

The plan should be executed in these gates:

### Gate 0. Comparison harness

Before more renderer work:

- keep a repeatable native-vs-legacy capture flow for the same world point, tab state, and camera situation
- use those captures to decide whether a slice materially improves the viewport instead of relying on local reasoning alone

Exit condition:

- every renderer slice can be judged against a stable side-by-side baseline

### Gate 1. Terrain submission parity

Fix the terrain contract first:

- verify overlay-vs-underlay floor behavior against legacy `MapRegion`
- verify tile-paint vs tile-model split for shaped tiles
- verify triangle winding consistency for terrain batches
- verify terrain submission does not depend on OpenGL backface culling for correctness

Exit condition:

- terrain no longer shows large black holes or obviously inverted floor surfaces in side-by-side comparison

### Gate 2. Raster backend parity

Then deepen the native raster backend:

- make flat, Gouraud, and textured faces obey a more legacy-like brightness and composition contract
- tighten textured triangle behavior and UV handling
- tighten ordering/clipping behavior so scene faces do not collapse into dark or inverted patches

Exit condition:

- terrain and static objects stop reading like a broken stitched preview and start reading like one coherent scene

### Gate 3. Render-plane and occluder parity

Then fix scene visibility semantics:

- native render-plane chooser equivalent to legacy `method120` / `method121`
- stronger occluder activation rules
- deeper object-level occlusion checks before submission

Exit condition:

- interiors, roofs, and wall visibility stop looking cut open or overexposed

### Gate 4. Actor and scene presentation parity

Only after terrain/object/raster behavior is credible:

- actor grounding, orientation, and pose path
- NPC runtime state and composition
- actor participation in the same scene/raster contract as terrain and objects

Exit condition:

- player and NPCs read like real scene actors, not add-on meshes over a partial world renderer

## Execution Order

### 1. Scene submission first

Before more camera or viewport tweaks:

- introduce explicit tile-paint submission
- introduce explicit tile-model submission
- move terrain/object/actor drawing behind one render queue
- keep queue data structures independent from LWJGL draw calls so they can back both viewport and minimap work

Exit condition:

- the world is no longer drawn by unrelated helper passes
- queue ownership is separate from viewport orchestration

### 2. Raster parity second

After submission exists:

- implement flat-shaded scene triangles
- implement Gouraud-shaded scene triangles
- tighten textured terrain/object/actor triangles so UV fidelity and ordering match the legacy
  client more closely
- tighten clipping and composition behavior
- keep triangle-mode and clipping code out of UI/chrome render classes

Exit condition:

- the viewport stops reading like a stitched-region preview
- the raster layer is a dedicated boundary rather than ad hoc helper methods on the viewport renderer

### 3. Actor/NPC parity after runtime state exists

Only after the raster/submission path is credible:

- add native NPC protocol/runtime state
- compose NPC models
- add actor overlays and minimap markers

Exit condition:

- the viewport and minimap share one consistent scene state for terrain, objects, and actors

## Current Working Assumption

If the viewport still looks wrong, assume the missing layer is scene submission or raster parity
before assuming the cache decode is wrong.

## Active Working Rule

For the current integration slice:

- documentation and task-state updates must call out scene submission and raster parity as the active boundary
- code reviews should reject viewport-only fixes that bypass the queue/raster split
- any new source comments should explain ownership, especially where queue entries are created or rasterized
