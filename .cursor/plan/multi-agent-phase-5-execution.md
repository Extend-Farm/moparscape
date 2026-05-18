# Multi-Agent Execution: Phase 5 Native World Parity

## Goal

Coordinate concurrent implementation of native world parity without letting the
task sprawl into undocumented or conflicting work.

This note complements:

- [phase-5-native-3d-world-parity-plan.md](/home/akira/projects/moparscape/.cursor/plan/phase-5-native-3d-world-parity-plan.md:1)
- [multi-agent-phase-5-documentation.md](/home/akira/projects/moparscape/.cursor/plan/multi-agent-phase-5-documentation.md:1)

## Shared Rules

- Legacy is reference-only. No `rs-*` production runtime may import or execute
  legacy classes.
- Do not revert unrelated work. If another contributor changed a nearby file,
  work around it or narrow ownership instead of "cleaning up" their edits.
- Documentation is mandatory on every slice. A code-only handoff is incomplete.
- Every worker must describe temporary scaffolding honestly. Do not rename a
  preview path or partial renderer as parity-complete.
- If a slice changes a public or architectural contract, the worker must update
  both code comments and plan/docs in the same handoff.

## Workstream Ownership

### Workstream 1: Cache and Definition Foundation

Owns:

- `rs-cache/src/main/java/io/github/ffakira/rsps/cache/*`
- `rs-content/src/main/java/io/github/ffakira/rsps/content/*`

Focus:

- object definitions from `loc.dat` / `loc.idx`
- model archive decode and model-definition catalog work
- cache-backed tests for model and object resolution

Primary outputs:

- native `ObjectDefinitionCatalog`
- native model decode pipeline
- cache-backed tests proving object ids resolve to model ids and native model data

Documentation deliverables:

- update the phase-5 plan if slice scope or order changes
- update `docs/native-world-pipeline.md` if the cache/model boundary becomes more concrete
- leave inline documentation where packed cache formats or transform semantics are non-obvious

### Workstream 2: Scene Assembly and Placement

Owns:

- `rs-content/src/main/java/io/github/ffakira/rsps/content/*`
- any new native scene package or module

Focus:

- object region archive decode
- region scene builder
- tile/object placement, occupancy, and collision attachments

Primary outputs:

- native object placement pipeline
- native static scene graph data structures
- deterministic sample-region placement tests

Documentation deliverables:

- update plan docs with sample regions and placement assumptions
- document coordinate systems, orientation rules, and scene ownership inline
- record any unresolved collision or occupancy shortcuts in task-state docs

### Workstream 3: Client Viewport, Minimap, and Actor Presentation

Owns:

- `rs-client-core/src/main/java/io/github/ffakira/rsps/client/core/*`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/*`

Focus:

- rendering the native scene graph in the LWJGL viewport
- minimap scene raster parity
- player and NPC model composition and presentation
- scene submission/render-queue integration on the client side
- keeping viewport orchestration separate from raster logic

Primary outputs:

- native object rendering in the 3D viewport
- actor model composition for player and NPC presentation
- camera and minimap integration against native scene data
- a client-facing scene submission layer that does not collapse back into `OpenGlTileRenderSystem`

Documentation deliverables:

- document renderer boundaries so viewport/chrome/input classes do not collapse back into god classes
- update repo-facing docs when visible runtime behavior changes
- note remaining visual shortcuts explicitly if parity is still incomplete
- leave explicit handoff notes when queue construction and raster logic are split across new classes or packages

### Workstream 4: Coordination, Documentation, and Verification Gate

Owns:

- `.cursor/plan/*` files related to phase 5
- `docs/native-world-pipeline.md`
- repo-level accuracy updates where needed

Focus:

- keep slice ordering and ownership current
- enforce documentation discipline
- keep verification evidence explicit and honest

Primary outputs:

- updated phase plan
- execution notes and handoff records
- verification checklists per slice

Documentation deliverables:

- maintain current workstream ownership
- record who owns which boundary during active work
- keep the current native-client truth aligned with the codebase

## Ordering Constraints

- Workstream 1 must establish object/model decode before viewport parity work claims world-scene progress.
- Workstream 2 depends on Workstream 1 outputs for object placement and scene structure.
- Workstream 3 may continue renderer refactors in parallel, but scene-submission/raster work must treat Workstreams 1 and 2 outputs as upstream scene inputs rather than rebuilding scene semantics inside the viewport layer.
- Workstream 4 runs continuously and must be updated whenever scope, ownership, or slice status changes.

## Verification Expectations

Every workstream handoff must name exact checks, for example:

- `./gradlew :rs-cache:test`
- `./gradlew :rs-content:test`
- `./gradlew :rs-client-lwjgl:compileJava`
- `timeout 8s ./gradlew :rs-client-lwjgl:run`

Each handoff must classify verification as:

- passed
- attempted but inconclusive
- not run

No worker should write "verified" without naming the exact commands or checks.

## Handoff Contract

Every worker handoff must leave a short task-state update containing:

- summary of the slice completed
- exact files touched
- interfaces or contracts changed
- legacy reference points consulted
- verification run
- remaining gaps or blockers
- the next safe starting point for another worker

If a worker partially lands a slice, the handoff must say what remains unsafe to assume.

## Active Starting Point

Current priority has moved past the first queue-integration slice.

Recently landed:

- the desktop-client world refactor now uses `io.github.ffakira.rsps.client.desktop` as its package root
- `io.github.ffakira.rsps.client.desktop.world.minimap` is the first extracted world slice
- unified world render queue for terrain, static objects, and the local actor
- overlay-only textured terrain submissions that respect the legacy floor contract more closely
- textured static-object execution from the native `textures` archive
- actor geometry that now preserves raw face raster modes, alpha, and texture anchors
- a camera-centered submission-time visibility window through `WorldSceneVisibilityPlanner`, so
  the native client no longer emits the whole stitched region into every terrain/object batch
- first-pass bridge/roof semantics through `WorldScenePlaneRules`, so terrain/object capture now
  honors `method182`-style effective plane lowering and roof-path flags reach the visibility layer
- gameplay bootstrap now resets the native sidebar to the Inventory tab for comparison captures

Current priority is the next raster-parity slice:

- keep `io.github.ffakira.rsps.client.desktop.world.terrain` as the terrain-owned package slice and
  avoid pushing submission/raster code back into it
- stabilize the native-vs-legacy comparison harness so slices can be judged on real side-by-side movement
- finish terrain submission parity before more viewport-only tuning
- improve textured triangle fidelity and UV behavior
- improve scene ordering, clipping, and composition parity
- deepen the new roof/plane + scene-occluder path toward legacy-like render-plane and occluder
  activation behavior
- keep NPC/runtime state work separate until terrain/object/raster parity is substantially more credible

Current anti-sprawl rule:

- viewport/chrome classes may orchestrate submission, but they should not own queue construction plus raster logic plus camera tuning in one place

Until the raster-ordering slice lands, viewport polish or scene-camera tuning should be treated as
secondary work, not world-parity progress.

## Recommended Multi-Agent Execution Order

When parallel execution resumes, split by these boundaries:

### Worker 1: Terrain Submission Contract

Own:

- `TerrainSceneMeshBuilder`
- terrain-facing tests

Focus:

- overlay-only texturing
- tile-paint vs tile-model parity
- terrain winding consistency

### Worker 2: Raster Backend Parity

Own:

- `OpenGlSceneRasterBackend`
- render-queue/raster tests

Focus:

- textured face behavior
- Gouraud/flat parity
- ordering/clipping behavior

### Worker 3: Render-Plane And Occluder Semantics

Own:

- `WorldScenePlaneRules`
- `WorldSceneVisibilityPlanner`
- `WorldSceneOcclusionPlanner`

Focus:

- native `method120` / `method121` equivalent
- occluder activation
- submission-time scene visibility correctness

### Worker 4: Verification And Comparison Harness

Own:

- runtime capture helpers
- comparison/task-state docs

Focus:

- keep like-for-like native-vs-legacy captures
- mark whether a slice materially moved the side-by-side comparison
