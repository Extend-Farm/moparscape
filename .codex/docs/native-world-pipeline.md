# Native World Pipeline

This document defines the current `rs-*` world-render boundary and the correct migration path to full RuneScape scene parity.

For the dedicated render-submission and raster track, see
[native-rasterizer-parity.md](/home/akira/projects/moparscape/docs/native-rasterizer-parity.md:1).

## Rule

- legacy client/server code is reference-only
- `rs-*` runtime code must stay native
- parity work means matching behavior and asset usage, not embedding legacy classes

## Current Native Pipeline

Today the native client does **not** render the full RuneScape scene. It renders cache-backed
terrain, static object models, and a native cache-backed character assembly inside cache-backed
gameplay chrome.

### Runtime flow

1. `:rs-persistence-*` and `:rs-server-runtime` load live character state.
2. `:rs-client-core` exposes bootstrap/player state to the client view model.
3. `CacheBackedWorldSceneLoader` loads native cache inputs:
   - `map_index`
   - terrain archives from cache store index `4`
   - object archives from cache store index `4`
   - raw model archives from cache store index `1`
   - texture sprites from the top-level `textures` archive
   - `flo.dat` color data
   - `loc.dat` / `loc.idx` object definitions
4. `TerrainRegionDecoder` produces terrain heights, underlays, and overlays.
5. `MapObjectRegionDecoder` produces placed object records with plane, type, orientation, and world position.
6. `RawModelRepository` and `RawModelDecoder` decode static object model archives into native vertex/face data.
   - face render types
   - face priorities
   - face alpha
   - textured-face vertex anchors
7. The loader builds a native `WorldScene` containing:
   - tile elevations
   - tile colors
   - current-plane tile flags for native roof-aware visibility decisions
   - overlay/underlay texture ids needed by the terrain submission layer
   - placed object metadata
   - static object geometry derived from cache model bytes through `ObjectSceneGeometryBuilder`
   - a scene-state minimap image derived from terrain plus legacy-style wall/mapscene semantics
   - temporary projection metadata
8. `OpenGlTileRenderSystem` renders:
   - a camera-centered submission-time visibility window around the local player instead of emitting the whole stitched region into every world queue build
   - a first roof-aware visibility pass driven by the same `& 4` tile flag signal used by the legacy client
   - a queued terrain path split into `TILE_PAINT` and `TILE_MODEL` submissions
   - overlay-only terrain texture candidates where the cache floor contract resolves a texture id
   - Gouraud terrain triangles for the remaining floor path
   - cache-object model geometry in the main world viewport
   - textured static-object faces sampled from the native `textures` archive where face metadata resolves cleanly
   - a planar UV fallback for textured static/object actor faces when cache texture anchors are missing or degenerate, so foliage-like faces do not collapse back to flat dark shading
   - a shared world-up transform convention for static objects and actors, so cache model Y is not inverted between object and character assembly
   - a native character mesh assembled from identity kits, wearable item body models, and palette swaps
   - one shared assembled-body transform for actor geometry, so identity-kit and equipment models stay in one coherent body space instead of being normalized part-by-part
   - actor batches that preserve cache face raster modes, alpha, and textured-face anchors when native actor geometry is available
   - a scene-state minimap inside `mapback`
   - a player-centered local minimap crop masked into the `mapback` radar, now rasterized at a legacy-style multi-pixel tile scale instead of one pixel per tile
   - a legacy-style minimap base contract that paints 4x4 tile blocks from terrain masks and overlays wall marks plus `mapscene` sprites instead of synthetic object-footprint fills
   - cache-backed gameplay frame art from the `media` archive
   - a native inventory tab that renders cache-model item icons plus live stack counts from bootstrap inventory state, while true note-paper overlays and full widget decoding remain pending
   - an explicit depth-buffered, multisampled world viewport instead of relying on platform-default 3D surface state

### Gameplay input contract

Gameplay input is now split the same way as the visible shell:

- sidebar clicks stay inside `GameplayChromeRenderer`, which now lives under `io.github.ffakira.rsps.client.desktop.gameplay` and now delegates frame-art ownership, minimap rendering, and sidebar/chatbox rendering to helper classes in that package
- world-viewport clicks are resolved through `WorldViewportClickPlanner`
- the current runtime still only exposes direct move deltas, so viewport click-to-walk currently sends one direct delta intent to the clicked tile rather than a legacy path queue
- terrain, queue submission, click-hit tests, and occluder sampling now share one lowered native height scale so the world surface no longer exaggerates slopes differently per subsystem

### Character bootstrap contract

The persisted bootstrap appearance payload does not directly store body-kit ids.

`BootstrapAppearance.lookValues()` currently maps to the legacy server contract:

- index `0`: sex
- index `1`: hair color
- index `2`: torso color
- index `3`: leg color
- index `4`: feet color
- index `5`: skin color

The visible body is assembled natively from:

- default identity kits selected for the active sex
- wearable item body models from item definitions
- legacy-equivalent visibility rules such as plate bodies hiding arms and full helms hiding head hair
- legacy-equivalent palette swaps for body colors
- preserved face raster semantics so actor geometry can enter the same flat/Gouraud/textured render queue as static models
- a shared assembled-body transform so the final actor scale comes from the whole combined body instead of each contribution expanding to full actor size on its own

That distinction matters: persistence supplies appearance controls, while the cache supplies the
body parts and wearable geometry.

### What this means in practice

The current client is using real cache terrain, real placed-object data, real static object
model bytes, and preserved raw-model face metadata, but it is **not** using the full legacy world
scene pipeline yet.

That is why the result does not match the legacy RuneScape viewport.

## What Is Missing

These pieces are still absent from the native world path:

- native scene graph assembly
- full legacy-equivalent scene traversal, ordering, clipping, and visibility behavior
- the legacy 51x51 tile visibility window, full roof-plane selection, richer occluder activation, and deeper object-level occlusion checks
- legacy-accurate textured floor UV/brightness behavior
- deeper native face-sorted model submission equivalent to `Model.method483(...)` and its rasterizer calls
- native depth/priority/clipping behavior equivalent to the legacy raster path
- full legacy-equivalent walls, decorations, interactives, and ground objects
- true NPC model composition
- full scene-driven minimap rasterization with mapfunction parity and final icon/rotation behavior
- legacy-equivalent camera, visibility, and occlusion behavior

The minimap and camera are now planned from native scene state, but they are still below legacy
parity because they do not yet consume the eventual scene graph, mapfunction icons, or NPC/player
overlay set. The current camera is intentionally a tighter player-follow camera rather than a
scene-wide overview, and the viewport now uses explicit depth/cull/multisample state, but this is
still a transitional approximation of the legacy viewport rules rather than full scene parity. A
camera-centered submission-time visibility window now exists, but it is still a coarse native
approximation rather than the legacy `SceneGraph.method313(...)` traversal and occlusion system. It
now reacts to roof-path tile flags and `method182`-style effective plane lowering, and the native
scene now carries first-pass wall/structure occluders that feed a plane-local object/actor
rejection pass during queue submission, but it still does not implement full render-plane
selection or legacy-equivalent occluder activation.

Until those exist, the world viewport remains a partial native scene rather than real RuneScape world parity.

## Next Integration Boundary

The next active boundary is no longer "load more cache data" or "tune the camera". It is:

- native raster parity
- textured terrain/object/actor fidelity
- deeper ordering/clipping parity on top of the existing unified render queue

That boundary sits between scene assembly and the LWJGL viewport. The native client already has the
first unified render queue and the first textured terrain/object/actor paths. What it still lacks
is the richer raster and scene-ordering contract that makes those inputs read like the legacy play
view instead of an approximate native scene.

The static-object path is now split more cleanly as well:

- `CacheBackedWorldSceneLoader` owns region assembly and scene capture
- `WorldScenePlaneRules` owns the first native bridge/roof plane semantics derived from legacy `method182`
- `WorldSceneObjectAssembler` owns placement-to-scene-object translation and preserves colocated tile entries
- `WorldSceneOccluderBuilder` owns the first native wall/structure occluder extraction step from assembled scene objects
- `ObjectSceneGeometryBuilder` owns raw-model transforms, recolors, normals, alpha, and raster-mode metadata for static objects
  - static object geometry now preserves cache-model Y origin instead of forcing every model's minimum Y back to zero, because bridges and other large structures legitimately extend below the tile surface
- `WorldSceneSubmissionBuilder` owns render-queue assembly
- `WorldSceneVisibilityPlanner` owns the current coarse pre-submit tile/object culling window
- `WorldSceneOcclusionPlanner` owns first-pass plane-local active-occluder selection and object/actor rejection before queue emission
  - wall occluders are active today
  - horizontal roof-plane occluders are intentionally withheld until the native client has a legacy-like render-plane chooser, because the simplified planner otherwise cuts open interiors and hides floor surfaces incorrectly
- `TerrainSceneMeshBuilder` now lives in `io.github.ffakira.rsps.client.desktop.world.terrain` and
  follows the legacy floor contract more closely:
  - underlay floors stay on the lit-color path
  - overlay floors are the only terrain texture candidates
  - the live viewport now submits overlay-textured terrain directly, while the underlay floor path stays on Gouraud/lit color
  - flat paint tiles now split across the legacy north-east/south-west diagonal instead of the earlier wrong diagonal
  - shaped terrain triangles now normalize to one stable tile-space winding before queue submission, so the viewport does not depend on inconsistent face order while culling stays disabled
- `TerrainSurfaceElevationResolver` now preserves per-plane height samples through scene capture
  and resolves each shared terrain corner from the highest adjacent visible surface
  - this is the first native fix for bridge/water seam collapse before raster parity is complete
- `OpenGlSceneRasterBackend` owns the current raster execution split, including the first native textured static-object path
  - it now also owns the first compatibility-profile GLSL programs for colored and textured scene batches, with runtime fallback to the earlier fixed-function path if shader compilation fails

That split is intentional. It keeps object-model growth out of the loader and avoids recreating a
new god class while the native renderer gets more complex.

The colocated-placement blocker is now fixed on the native path: walls, decorations, interactives,
and ground objects that share a tile are preserved as separate `WorldSceneObject` entries. The
next upstream scene-input gap is no longer tile flattening; it is deeper render-plane selection,
legacy-like occluder activation, and stronger ordering/clipping behavior on top of those fuller
scene inputs.

## Package Direction

The native world stack is large enough that package structure now matters. The intended package
split is:

- `client.desktop.world.minimap`
  - minimap rasterization
  - local radar crop
  - `mapback` clip masks
- `client.desktop.world.terrain`
  - terrain tile paints/models
  - floor textures
  - gradients and height-mesh submission
- `client.desktop.world.object`
  - extracted
  - object placement translation
  - static-object geometry assembly
- `client.desktop.world.raster`
  - extracted
  - queue execution
  - triangle modes
  - UV/composition/clipping
- `client.desktop.world.visibility`
  - extracted
  - bridge/roof plane rules
  - visibility windows
  - occluder selection/rejection

That split keeps future texture, meshing, gradient, and visibility work out of one flat LWJGL
namespace and makes ownership explicit in source.

### Required native split

To avoid collapsing back into god classes, the next slice should keep these responsibilities
separate:

- scene assembly:
  - owns world/region semantics
  - produces tile paints, tile models, placed scene objects, and actor submissions
- scene submission:
  - owns the world render queue
  - translates scene state into a stable set of submitted primitives
- raster layer:
  - owns triangle modes, clipping, ordering, and final screen-space composition
- viewport orchestration:
  - owns camera selection, render-target setup, and calling the submission/raster layers
  - must not directly become the place where terrain, objects, actors, and triangle rules all accumulate again

In practical terms, `OpenGlTileRenderSystem` should stay an orchestrator. It should not keep
absorbing:

- scene-tile traversal
- queue construction
- face sorting
- clipping logic
- triangle shading mode selection

Those belong behind a dedicated scene-submission and raster boundary.

## Legacy Reference Contract

The legacy client establishes the correct target architecture.

### Scene bootstrap

`GameClientCore.method54(...)` does not enter the live world after terrain alone. It waits for:

- terrain region archives
- object region archives
- object dependency validation

Implication:

- native world loading must treat terrain and object archives as one bootstrap unit

### Scene assembly

Legacy `MapRegion` is not just a renderer helper. It:

- computes tile colors and lighting
- emits tile paint/tile model data
- places walls, decorations, and interactives
- updates collision maps
- resolves object definitions into renderable scene entries

Implication:

- native parity requires a scene builder layer, not just a viewport renderer

### Model resolution

Legacy `ObjectDefinition`, item definitions, NPC definitions, and appearance systems all resolve model ids through native decode/registry steps before rendering.

Implication:

- native `rs-*` needs model-definition and model-byte decode support before object or actor parity is possible

### Rendering

Legacy `SceneGraph` renders with:

- camera position
- pitch and yaw
- visible tile selection
- scene object traversal
- actor rendering
- minimap raster support

Legacy rendering is not a single step. The important render contract is:

1. `GameClientCore.method146(...)` computes camera state, resets raster targets, and enters scene render.
2. `SceneGraph.method313(...)` selects visible tiles and traverses the scene graph in camera order.
3. `SceneGraph.method315(...)` submits `SceneTilePaint` triangles.
4. `SceneGraph.method316(...)` submits `SceneTileModel` triangles.
5. `Model` submits actor/object faces through `Rasterizer3D.method374(...)`, `method376(...)`, and `method378(...)`.
6. `Rasterizer3D` applies flat, Gouraud, or textured triangle rasterization with clipping and depth-like ordering assumptions.

Implication:

- a 3D height mesh alone is not the target renderer
- raw OpenGL model drawing is still missing a large part of the legacy scene contract
- parity work now needs a dedicated raster/submission track, not just better camera tuning

## Correct Migration Path

### Slice 1. Native object and model foundations

Deliver:

- `ObjectDefinitionCatalog` for `loc.dat` / `loc.idx`
- `MapObjectRegionDecoder` for placed-object archives
- native model archive decode support
- tests proving object ids resolve to expected model ids

Exit criteria:

- the new stack can resolve object definitions and decode referenced model bytes without any legacy runtime help

### Slice 2. Native region object placement

Deliver:

- placed object records with world position, plane, type, and orientation
- stable placement tests for the region around `akira`

Exit criteria:

- the new stack can enumerate the objects that belong in the player scene

### Slice 3. Native scene assembly

Deliver:

- scene tile records
- placed walls/decorations/interactives using the decoded object model layer
- collision/culling metadata needed by rendering and movement

Exit criteria:

- the world is represented as a native scene graph input, not just terrain arrays

### Slice 4. Native 3D scene renderer

Deliver:

- perspective camera
- static object model rendering
- terrain + objects in one scene pass
- temporary actor proxies removed from the main viewport

Exit criteria:

- the main viewport shows terrain and placed cache objects in the correct world positions

### Slice 4A. Native scene submission and raster contract

Deliver:

- native scene submission layer for tile paints, tile models, and object/model faces
- a unified render queue that both the viewport and minimap can consume
- native handling for face ordering, clipping, and screen-space triangle emission
- native render targets that support both the main viewport and scene-driven minimap work

Current status:

- the native client now has an explicit world render queue boundary through
  `WorldSceneSubmissionBuilder`, `SceneRenderQueue`, and `WorldViewportRenderer`
- queue execution is delegated to `OpenGlSceneRasterBackend`
- cache terrain decode now carries overlay shape/rotation into the native scene path
- terrain submission is split into `TILE_PAINT` and `TILE_MODEL` batches instead of one quad per
  tile
- terrain batches now use a native Gouraud-style raster path
- static object and actor batches still rasterize as flat-colored triangles
- textured triangles and tighter ordering/clipping rules are still pending

Exit criteria:

- the native client no longer renders the world as independent terrain/object helpers
- the main viewport is driven by a single scene submission contract comparable to the legacy `SceneGraph` + `Model` flow
- render-queue ownership is explicit enough that viewport/chrome classes do not grow back into submission or raster god classes

### Slice 4B. Raster parity

Deliver:

- flat-shaded and Gouraud-shaded triangle paths
- textured triangle path for scene/object faces
- visibility/culling behavior close enough that the player no longer sees stitched-region "overview" artifacts
- queue-to-raster integration stable enough that camera tuning stops masking structural render defects

Exit criteria:

- the world no longer reads like an eagle-eye mesh preview
- scene depth, overlap, and tile composition behave like a play camera rather than a region visualizer

### Slice 5. NPC and minimap parity

Deliver:

- NPC model composition
- scene-backed minimap generation
- mapscene / mapfunction style overlays where applicable

Exit criteria:

- the world viewport and minimap both depend on the same native scene state

## Current Working Assumption

If the native world looks wrong today, assume the pipeline is incomplete before assuming the decoded models are incorrect.

Right now the usual failure mode is:

- terrain and static object model data exist
- actor composition and scene-graph parity do not

That distinction should drive implementation order and code review.
