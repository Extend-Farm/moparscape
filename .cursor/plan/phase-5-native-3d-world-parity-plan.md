# Phase 5: Native 3D World Parity Plan

## Goal

Replace the current `rs-client-lwjgl` proxy world viewport with a native RuneScape scene pipeline that matches the legacy client behavior:

- real region terrain
- real scene objects, walls, decorations, and ground details
- real player and NPC models
- real camera and visibility behavior
- real minimap generation from the scene, not just from raw terrain color data

Legacy remains reference-only. No `rs-*` production runtime may import or execute legacy classes.

This plan is the implementation companion to [docs/native-world-pipeline.md](/home/akira/projects/moparscape/docs/native-world-pipeline.md:1). The doc captures the stable boundary; this file keeps the delivery slices.

## Shared Execution Rules

- Legacy remains reference-only. No `rs-*` production runtime may import or execute legacy classes.
- Do not revert or reshape another contributor's unrelated in-flight work. Coordinate by ownership, not by cleanup.
- Every implementation slice must ship with both code changes and documentation updates. Documentation is part of done, not a follow-up task.
- Every slice must keep the current runtime boundary honest. Temporary scaffolds must be named as scaffolds in code comments and task-state docs.
- If a slice changes the current native-client truth, update both this plan and the relevant repo/runtime docs in the same change window.

## Current Problem

The current `rs-client-lwjgl` world path is still structurally incomplete.

It only uses:

- terrain heights
- floor colors
- native placed-object metadata from object region archives
- native object definitions from `loc.dat` / `loc.idx`
- native raw model decode from cache store `1`
- preserved raw-model face metadata for static objects
- a native OpenGL height mesh
- overlay-only terrain texture candidates derived from native floor texture ids
- Gouraud fallback for terrain in the live viewport until floor-texture parity is credible
- static object geometry derived from real cache model bytes
- a native player mesh assembled from bootstrap appearance controls, identity kits, and wearable item body models
- actor geometry that now preserves raw face raster modes, alpha, and texture anchors
- a scene-state minimap raster derived from terrain and object semantics
- a scene-aware camera planner instead of fixed viewport transforms
- a unified native render queue split into terrain/object/actor submission batches
- a broader camera-centered submission-time visibility window that now stays closer to the legacy
  51x51 working area instead of the earlier clipped preview window
- a first roof-aware visibility pass plus `method182`-style effective plane lowering for terrain
  and object capture
- first-pass native wall/structure occluders carried on `WorldScene`
- plane-local object/actor occlusion rejection during queue submission
  - currently limited to wall occluders until the native render-plane chooser exists

It does **not** yet implement the actual legacy scene pipeline:

- no native scene graph
- no native NPC model composition
- no legacy-equivalent 51x51 visibility window, full roof-plane chooser, or full occluder activation
- no full legacy-equivalent face-sorted raster contract for models and tiles
- no native camera visibility system equivalent to the legacy scene renderer
- no full scene-ordering/clipping/priority parity

So the issue is no longer "there is no native model layer" or "there is no native submission path".
The issue is that the native client still lacks deeper raster parity, scene-ordering parity, NPC
composition, and the rest of the legacy rendering contract.

## Active Integration Boundary

The next active implementation boundary is:

- comparison-driven raster and submission parity
- terrain/tile correctness before more camera polish
- scene-ordering, clipping, and render-plane parity on top of the existing queue
- explicit package ownership so minimap, terrain, raster, object, and visibility work do not keep
  accumulating in one flat `client.desktop` package

The package root for this refactor is now `io.github.ffakira.rsps.client.desktop`.
The first extracted world slice is `io.github.ffakira.rsps.client.desktop.world.minimap`.
The second extracted world slice is `io.github.ffakira.rsps.client.desktop.world.terrain`.

This is now the priority over more viewport-only tuning. Terrain, object placement, static object
geometry, local-player assembly, minimap scaffolding, textured floor/object/actor inputs, and the
first unified queue already exist. The visible mismatch is now primarily caused by the incomplete
raster and scene-ordering contract between scene state and the viewport. The latest world-pass
slice also made the 3D surface contract explicit with a requested depth buffer, multisampling, a
tighter player camera/frustum, a first camera-centered visibility window, and
tile-flag/effective-plane-aware submission inputs, but that remains a transitional runtime step
rather than final viewport parity. Backface culling is currently intentionally disabled in the
world pass because native terrain/object winding parity is not yet strong enough to rely on it.
The colocated scene-entry correctness blocker is now closed on the native path: scene assembly
preserves multiple placements on the same tile instead of collapsing them by tile key. The next
upstream correctness target is deeper roof/plane selection plus stronger occluder activation and
ordering/clipping semantics on top of those preserved inputs.

## Package Map

The world stack should now converge on these package boundaries:

- `io.github.ffakira.rsps.client.desktop.world.minimap`
  - first extracted slice
  - local radar crop
  - `mapback` clip masks
  - minimap rasterization and icon work
- `io.github.ffakira.rsps.client.desktop.world.terrain`
  - extracted
  - tile paints
  - tile models
  - floor texture, gradient, and height-mesh submission
- `io.github.ffakira.rsps.client.desktop.world.object`
  - extracted
  - object placement translation
  - raw-model transforms
  - object geometry assembly
- `io.github.ffakira.rsps.client.desktop.world.raster`
  - extracted
  - queue execution
  - flat/Gouraud/textured behavior
  - UV handling
  - ordering/clipping/composition
- `io.github.ffakira.rsps.client.desktop.world.visibility`
  - extracted
  - bridge/roof plane rules
  - visibility windows
  - occluder activation/rejection

`OpenGlTileRenderSystem`, `GameplayChromeRenderer`, and `CacheBackedWorldSceneLoader` remain the
top-level orchestrators for now, but new world work should move into those packages rather than
staying in the root LWJGL package.

## Delivery Rule For The Next Slices

Do not judge progress by whether one local shader, texture, or camera tweak compiles. Judge progress
by whether a side-by-side native-vs-legacy capture materially improves.

That means the next slices must ship as end-to-end parity gates:

1. comparison harness stability
2. terrain submission parity
3. raster backend parity
4. render-plane and occluder parity
5. actor/NPC presentation parity

Any slice that only tweaks one local renderer knob without moving one of those gates should be
treated as non-progress.

## Gate Plan

### Gate 0: Stable Comparison Harness

Objective:

- keep repeatable native-vs-legacy capture on the same account, area, and sidebar state

Must prove:

- screenshots are comparable and not drifted by tab state or login/bootstrap state

Exit condition:

- every world-render slice can be evaluated against a stable reference pair

### Gate 1: Terrain Submission Parity

Objective:

- make terrain submission follow the legacy floor and shaped-tile contract closely enough that the
  world stops reading like a torn mesh

Work:

- verify overlay-only texturing against legacy `MapRegion`
- verify tile-paint vs tile-model split for shaped tiles
- verify terrain triangle winding consistency
- verify terrain mesh height scaling and bridge/river surface selection
- verify floor gradients and brightness interpolation against the legacy floor contract
- make floor texture candidates flow through one explicit terrain-texture path instead of scattered
  local fallbacks
- avoid relying on OpenGL backface culling until winding parity is proven

Exit condition:

- native terrain no longer shows large black holes, inverted faces, or obviously broken floor continuity

### Gate 2: Raster Backend Parity

Objective:

- make the native queue render more like a coherent scene than a stitched preview

Work:

- tighten flat/Gouraud/textured face behavior
- tighten UV and texture composition behavior
- make object and terrain textured faces use one coherent texture-loading contract
- tighten alpha/cutout handling for foliage, bridge rails, and water-adjacent props
- tighten ordering/clipping semantics inside the raster backend
  - current native step in progress: opaque-first plus translucent-second execution, so the backend stops compositing every face through one blended stream

Exit condition:

- terrain and static objects stop collapsing into dark, broken, or visually inverted patches

### Gate 3: Render-Plane And Occluder Parity

Objective:

- stop cut-open interiors and overexposed structures

Work:

- native render-plane chooser derived from legacy `method120` / `method121`
- stronger occluder activation rules
- deeper object-level occlusion checks before submission

Exit condition:

- roofed/interior scenes present like play scenes instead of exposed map previews

### Gate 4: Actor And NPC Presentation Parity

Objective:

- make the player and later NPCs participate in the same credible scene contract

Work:

- actor grounding, facing, orientation, and pose pipeline
- NPC runtime/protocol/client state
- NPC composition on the native actor path

Exit condition:

- actors read as real scene actors instead of separate overlay meshes

### Code-quality guardrails for this slice

To avoid rebuilding god classes while parity work gets more complex:

- `OpenGlTileRenderSystem` should remain viewport orchestration, not the home for queue construction or triangle-mode logic
- `CacheBackedWorldSceneLoader` should remain scene assembly, not the home for raw-model transform and shading rules
- scene submission should have its own data structures and entrypoints
- raster parity should have its own ownership boundary rather than accumulating inside viewport helpers
- task-state and source documentation must name any temporary shortcut explicitly if submission or raster work lands partially

Current ownership after the latest static-object slice:

- `ObjectSceneGeometryBuilder` owns static-object raw-model transforms, normals, recolors, alpha, and raster-mode metadata
- `WorldScenePlaneRules` owns first-pass bridge/roof plane semantics derived from legacy `method182`
- `WorldSceneObjectAssembler` owns placement-to-scene-object translation and preserves colocated tile entries
- `WorldSceneOccluderBuilder` owns first-pass wall/structure occluder extraction from assembled scene objects
- `WorldSceneSubmissionBuilder` owns queue assembly for terrain, objects, and the local actor
- `WorldSceneOcclusionPlanner` owns plane-local active-occluder selection and pre-submit object/actor rejection
- `OpenGlSceneRasterBackend` owns flat/Gouraud plus the first real textured terrain/static-object/actor execution paths backed by the native `textures` archive
- `TerrainSceneMeshBuilder` now owns the overlay-only textured floor split and the shape `1..12` modeled-tile boundary
  - the live viewport currently keeps terrain on the Gouraud path even when overlay texture ids exist
  - flat paint tiles now use the legacy north-east/south-west split
  - shaped terrain triangles now normalize to one stable tile-space winding before queue submission
  - shared bridge/water corner heights now resolve from adjacent visible surfaces instead of one flattened tile plane

## Appearance Contract Correction

The native client must preserve the legacy appearance bootstrap semantics:

- persisted `lookValues` are sex plus color controls
- they are not direct identity-kit ids
- default body kits are chosen per sex and then recolored
- wearable item body models layer on top of those kits

That means character appearance is a two-source assembly problem:

- persistence supplies control values and equipped item ids
- the cache supplies identity kits, wearable models, and recolor targets

Future work on NPCs and scene actors should follow the same rule: bootstrap state chooses what to
render, but the cache still owns the renderable parts.

## Current Hard Blocker

NPC composition is still blocked on missing native runtime/protocol state.

Today the `rs-*` runtime does not publish native NPC bootstrap/update messages into the client.
That means the client can render terrain, static objects, the local player, and a scene-derived
minimap, but it still cannot honestly render NPCs without inventing state that does not exist.

The next NPC slice therefore has to start in:

- `rs-server-runtime`
- `rs-protocol`
- `rs-client-core`

before `rs-client-lwjgl` can compose native NPC models.

## Legacy Reference Findings

### 1. World loading waits for both terrain and object region archives

Reference:

- `GameClientCore.method54(...)`
- [GameClientCore.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java:2612)

What it does:

- waits for terrain region bytes in `aByteArrayArray1183`
- waits for object region bytes in `aByteArrayArray1247`
- validates object dependencies with `MapRegion.method189(...)`
- only enters live world state after both are ready

Implication:

- native world loading must treat terrain and object archives as one scene bootstrap unit
- terrain-only loading is not sufficient

### 2. Model data is streamed and registered before scene use

Reference:

- `GameClientCore.bootstrapConnectUpdateServer(...)`
- `GameClientCore.method57(...)`
- `Model.method459(...)`
- `Model.method460(...)`
- [GameClientCore.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java:6368)
- [GameClientCore.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java:2778)
- [Model.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java:35)

What it does:

- initializes a model header registry against the resource provider
- receives raw model bytes from the on-demand system
- decodes those bytes into the legacy model format
- later object, item, NPC, and player appearance systems resolve models through that registry

Implication:

- `rs-*` needs a native model-definition pipeline, not just terrain decode
- native object and entity rendering cannot be correct until model decoding exists

### 3. Region scene assembly is split between terrain build and object placement

Reference:

- `MapRegion.method171(...)`
- `MapRegion.method190(...)`
- `MapRegion.method188(...)`
- [MapRegion.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java:69)
- [MapRegion.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java:1295)
- [MapRegion.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java:1101)

What it does:

- computes underlay/overlay colors and lighting
- emits tile paints and tile models into `SceneGraph`
- places scene objects by reading object region archives
- resolves `ObjectDefinition`
- builds static models with `ObjectDefinition.method578(...)` or animated `DynamicObject`
- updates collision maps during placement

Implication:

- native world parity needs a scene builder, not just a renderer
- collision, walls, decorations, and interactives are part of scene assembly

### 4. Object definitions are model factories, not just metadata

Reference:

- `ObjectDefinition.method576(...)`
- `ObjectDefinition.method578(...)`
- `ObjectDefinition.method581(...)`
- [ObjectDefinition.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ObjectDefinition.java:78)
- [ObjectDefinition.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ObjectDefinition.java:113)
- [ObjectDefinition.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ObjectDefinition.java:177)

What it does:

- decodes `loc.dat` / `loc.idx`
- resolves one or more model ids per object
- applies rotation, scaling, offsets, recolors, contouring-to-ground, and animation hooks

Implication:

- native `ObjectDefinitionCatalog` must expose render-ready placement info
- native model rendering must support object transforms and basic recolor/contour behavior

### 5. The scene is rendered through a real scene graph and camera system

Reference:

- `SceneGraph.method313(...)`
- `GameClientCore.method146(...)`
- [SceneGraph.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java:1040)
- [GameClientCore.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java:10135)

What it does:

- consumes camera position, pitch, yaw, and plane
- selects visible scene tiles
- renders walls, interactives, decorations, projectiles, and actors through the scene graph
- blends object normals/lighting in `SceneGraph.method305(...)`

Implication:

- native parity requires a real camera-driven renderer
- a viewport textured with a prebuilt image, or a static mesh without scene objects, is not enough

### 5A. Legacy world rendering is also a rasterizer contract

Reference:

- `SceneGraph.method315(...)`
- `SceneGraph.method316(...)`
- `Model.method483(...)`
- `Rasterizer3D.method374(...)`
- `Rasterizer3D.method376(...)`
- `Rasterizer3D.method378(...)`
- [SceneGraph.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java:1689)
- [SceneGraph.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java:1802)
- [Model.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java:1830)

What it does:

- submits `SceneTilePaint` triangles into the rasterizer
- submits `SceneTileModel` triangles into the rasterizer
- submits object and actor model faces into the rasterizer
- switches between flat, Gouraud, and textured triangle paths
- relies on clipping, face ordering, and priority behavior in the raster layer

Implication:

- the native client is still bypassing the heart of the legacy world render path
- this is why the viewport still reads like a region preview even after model decode and camera tuning
- the next real track is native scene submission plus raster parity, not more viewport-only polish

### 6. Minimap is rendered from the scene, not from raw terrain only

Reference:

- `GameClientCore.method24(...)`
- `SceneGraph.method309(...)`
- [GameClientCore.java](/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java:700)

What it does:

- rasterizes scene tiles into the minimap buffer
- overlays scene/object sprites such as `mapscene`
- tracks dynamic markers separately

Implication:

- the native minimap should eventually come from the scene builder + scene decorations
- the current terrain-only minimap is an acceptable temporary scaffold, not the target

## Required Native Architecture

### 1. `rs-content` additions

Add native catalogs for:

- object definitions from `loc.dat` / `loc.idx`
- identity kits
- NPC definitions
- spot animations
- animation sequences / frames
- mapscene / mapfunction sprite definitions
- model metadata references used by objects and actors

### 2. `rs-cache` additions

Add native decode support for:

- model archive bytes
- model header tables / indices
- any typed archive/container helpers needed for model and animation formats

### 3. New native scene assembly layer

Create a dedicated scene package or module, for example:

- `rs-scene`

It should own:

- scene tiles
- tile paint/tile model data
- wall objects
- ground decorations
- interactive scene objects
- ground item piles
- scene visibility metadata
- collision/culling inputs

This is the native equivalent of the role currently spread across `MapRegion` and `SceneGraph`.

### 4. New native rendering backend

Keep legacy as reference only. For the new client:

- use native cache definitions and scene graph data
- render through LWJGL/OpenGL
- do not port the exact software rasterizer as runtime architecture unless needed for temporary parity validation

The renderer needs:

- perspective camera
- tile mesh rendering
- static object model rendering
- player/NPC model rendering
- scene sprite overlays where appropriate
- minimap scene raster path

It also needs a formal scene submission contract:

- tile-paint submission
- tile-model submission
- model-face submission
- face ordering / clipping / shading rules

Without that, the client will keep rendering valid assets through the wrong scene abstraction.

## Recommended Delivery Order

### Slice 1. Native object-definition and model foundation

Deliver:

- native `ObjectDefinitionCatalog`
- native `ModelDefinitionCatalog`
- tests against known object ids and model ids from the checked-in cache

Acceptance:

- resolve a sample object id from `loc.dat`
- enumerate its model ids
- decode the referenced model bytes natively

Workstream owner:

- Cache and content foundation

Documentation deliverables:

- update `docs/native-world-pipeline.md` with the new native object/model boundary
- add or update inline comments on model/object decode entry points where the cache format is non-obvious
- record known gaps and sample object ids in task-state docs if the slice lands partial parity only

Verification expectations:

- cache-backed unit tests for `loc.dat` / `loc.idx` decoding
- cache-backed unit tests for model id resolution
- compile verification for `:rs-cache`, `:rs-content`, and any consumer module touched by the slice

### Slice 2. Native region object placement

Deliver:

- native decoder for object region archives
- native `RegionSceneBuilder`
- object placements in world coordinates with type/orientation/plane

Acceptance:

- for a sample region around `akira`, produce a stable list of placed scene objects
- sample counts roughly track legacy expectations for walls/decorations/interactives

Workstream owner:

- Scene assembly foundation

Documentation deliverables:

- update `docs/native-world-pipeline.md` with the terrain-plus-object bootstrap contract
- add task-state notes describing the chosen region samples, coordinate conventions, and any placement assumptions still unresolved
- document collision and orientation semantics inline where the builder consumes packed region data

Verification expectations:

- cache-backed tests for object region archive decode
- deterministic tests for stable scene-object placement around the chosen sample region
- compile verification for `:rs-content` and any new scene module/package

### Slice 3. Native static scene graph

Deliver:

- tile paints / tile models
- walls, decorations, interactives
- collision attachments

Acceptance:

- native scene graph can answer what occupies a given tile
- object placements and terrain exist in the same scene data structure

Workstream owner:

- Scene graph and occupancy layer

Documentation deliverables:

- update architecture docs if a new scene package or module is introduced
- add inline documentation for scene-node ownership, tile occupancy semantics, and collision attachments
- extend task-state docs with the scene-graph contract that downstream rendering and minimap work must rely on

Verification expectations:

- tests for occupancy queries and tile/object co-location
- compile verification for scene assembly consumers
- if a new module is added, module-specific compile and test coverage must be listed in handoff notes

### Slice 4. Native OpenGL object rendering

Deliver:

- render decoded object models into the world viewport
- apply object orientation, scale, translation, and recolor support

Acceptance:

- fences, trees, rocks, house walls, and bridges appear in the world viewport
- viewport no longer looks like terrain-only geometry

Workstream owner:

- Client viewport rendering

Documentation deliverables:

- update `docs/native-world-pipeline.md` to replace "terrain mesh preview" wording once real scene objects render
- document renderer boundaries inline so `OpenGlTileRenderSystem` does not become a god class again
- record which object categories are visually validated and which remain missing

Verification expectations:

- renderer compile and tests for `:rs-client-lwjgl`
- smoke verification that the viewport renders object-bearing regions without immediate startup failure
- note any remaining temporary renderer shortcuts in task-state docs

### Slice 4A. Native scene submission layer

Deliver:

- a unified world render queue for terrain, tile paints, tile models, and static object faces
- removal of the current disconnected terrain/object helper split in the viewport
- explicit scene submission data structures that downstream rasterization can consume
- queue entrypoints narrow enough that future actor/NPC integration does not expand viewport classes into god classes again

Acceptance:

- the viewport render entrypoint consumes one coherent scene submission contract instead of drawing the world in disconnected passes
- the same submission contract is viable for both world viewport and scene-driven minimap work

Workstream owner:

- Scene submission and client render orchestration

Current status:

- landed an explicit native queue boundary:
  - `WorldSceneSubmissionBuilder`
  - `SceneRenderQueue`
  - `SceneRenderBatch`
  - `WorldViewportRenderer`
  - `OpenGlSceneRasterBackend`
- terrain, static objects, and the local actor now enter one coherent queue instead of being drawn
  by separate viewport-owned helper passes
- cache terrain decode now preserves overlay shape/rotation and emits distinct `TILE_PAINT` and
  `TILE_MODEL` batches on the native path
- terrain queue entries now execute through a Gouraud-style raster path
- static objects and actors still execute as flat-colored triangles, so Slice 4B remains the active
  visual-parity blocker

Documentation deliverables:

- update `docs/native-world-pipeline.md` with the scene submission contract
- add inline notes where tile paints, tile models, and model faces enter the render queue
- record any temporary submission shortcuts still present after the slice lands
- update multi-agent execution notes if ownership boundaries or write scopes change during the integration

Verification expectations:

- compile verification for `:rs-client-lwjgl` and scene consumers
- targeted tests around scene submission counts or invariants where possible
- review that queue construction is not re-embedded into viewport/chrome classes

### Slice 4B. Native raster parity layer

Deliver:

- flat-shaded triangle path
- Gouraud-shaded triangle path
- textured triangle path
- clipping and ordering rules close enough for in-world composition
- a dedicated raster ownership boundary that can be reviewed independently from camera/chrome work

Acceptance:

- world depth and overlap stop reading like an eagle-eye stitched-region render
- tile and object composition behave like a play camera rather than a region visualizer
- remaining visual defects can be classified as specific raster gaps rather than generic viewport mismatch

Workstream owner:

- Client raster and screen-space rendering

Documentation deliverables:

- document which parts of the legacy raster contract are implemented natively
- add inline notes around clipping, triangle modes, and any temporary deviations from legacy behavior
- record known parity gaps such as texture filtering, priority handling, or shading differences
- keep task-state notes explicit about whether a reported artifact is queue/submission related or raster related

Verification expectations:

- renderer compile and tests for `:rs-client-lwjgl`
- visual smoke verification against known legacy comparison scenes
- task-state notes for any remaining artifacts that are still attributable to raster parity gaps

### Slice 5. Native player and NPC model composition

Deliver:

- player appearance model composition from identity kits and equipment
- NPC model composition from NPC definitions
- minimal animation pose support

Acceptance:

- `akira` appears as a real character model in-world
- at least one NPC definition can be rendered correctly

Workstream owner:

- Actor presentation and appearance composition

Documentation deliverables:

- update docs describing the boundary between persisted character state, protocol bootstrap, and cache-backed appearance/model resolution
- add inline notes where equipment, identity kits, or NPC definitions feed model composition
- record any animation limitations still present after the slice lands

Verification expectations:

- tests for appearance model composition inputs
- compile verification for `:rs-client-core`, `:rs-client-lwjgl`, `:rs-content`, and any protocol/runtime modules touched
- smoke verification that `akira` and at least one NPC render in the native viewport path

### Slice 6. Native camera + minimap parity

Deliver:

- camera behavior aligned with the legacy viewport semantics
- minimap raster based on scene + mapscene overlays

Acceptance:

- scene framing feels RuneScape-like rather than debug-like
- minimap contains real scene overlays, not just terrain colors

Workstream owner:

- Camera, minimap, and final parity pass

Documentation deliverables:

- update repo-facing docs to describe the new native viewport/minimap status honestly
- add inline notes for camera semantics, minimap overlay sources, and any remaining non-parity shortcuts
- close or revise prior task-state warnings that described the viewport as terrain-only once that is no longer true

Verification expectations:

- renderer tests for minimap scene raster inputs where practical
- smoke verification that the native client boots, logs in, and presents the updated camera/minimap path
- final slice handoff must enumerate what still differs from legacy if full parity is not yet achieved

## Immediate Correction To Current Runtime

Do not treat the current world viewport as "3D world parity".

The current native path should be described internally as:

- cache-backed scene preview with transitional viewport rendering

not:

- RuneScape world renderer

That keeps the engineering boundary honest while the real scene pipeline is being built.

## Next Implementation Slice

Implement Slice 4A first:

- native scene-submission data structures for tile paints, tile models, and model faces
- a unified world render queue for existing terrain/object/local-player scene inputs
- tests or invariants around queue construction and submission ownership

Only after Slice 4A lands should Slice 4B raster parity work proceed. Until then, more camera or
viewport polish should be treated as secondary work rather than parity progress.

## Documentation Discipline Per Slice

Each slice must update the following when relevant:

- task-state docs under `.cursor/plan/`
- `docs/native-world-pipeline.md` for stable architecture or boundary changes
- `README.md` only when user-visible run/build/runtime behavior changes
- inline code comments only where format, ownership, or temporary scaffolding would otherwise be rediscovered

Minimum documentation output for a completed slice:

- what changed
- what stayed provisional
- what references in legacy were consulted
- what verification actually ran
- what the next slice can safely assume

## Verification Expectations

Every slice handoff must explicitly separate:

- unit or cache-backed tests that passed
- compile targets that passed
- smoke or manual checks that were attempted
- checks that were not run

If a slice touches multiple modules, the handoff must name the exact Gradle targets used rather than saying "build passed".

## Handoff Expectations

Any worker handing off a slice must leave a task-state update that includes:

- owned files touched
- interfaces or contracts changed
- legacy reference points consulted
- verification completed
- known gaps, TODOs, or blockers
- what the next worker may change safely
- what files or boundaries are currently under active ownership and should not be rewritten opportunistically
