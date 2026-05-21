## Objective

Establish a single canonical world coordinate frame for the native client and reconcile every transform site against the legacy `SceneGraph` / `MapRegion` conventions. The current pipeline mixes several incompatible conventions, which manifests as east/west reversal, landmark mis-placement, and minimap/3D heading disagreement — and which cannot be fixed by individual sign flips because each site is internally consistent with the *wrong* convention of the site that calls it.

This plan is **read-only background and a refactor outline**. Implementation is left to dedicated workstreams.

## Update — default framing calibration

The native fixed-frame viewport still needs a modest pullback on top of the restored `22.5°` pitch floor. The current baseline keeps the calibrated `768 + pitch*3` orbit arm while terrain height stays in the same `128`-units-per-tile world scale that models already use.

## Update — mirrored object parity

The courtyard statue rotation mismatch turned out not to be a global quarter-turn bug. `ObjectSceneGeometryBuilder` already matched the legacy clockwise `(x, z) -> (z, -x)` step from `Model.method473`. The remaining parity gap was the mirrored-definition contract: legacy object prep mirrors `Z`, swaps face winding `A/C`, and applies `translateX/Z` after rotation. The native builder had been mirroring `X` and translating before rotation, which only shows up on asymmetric props such as the mirrored statue variant.

## Update — concrete diagnosis from `WorldFrameParityProbeTest`

A diagnostic harness ([`WorldFrameParityProbeTest`](../../rs-client-lwjgl/src/test/java/io/github/ffakira/rsps/client/desktop/world/WorldFrameParityProbeTest.java)) traces a probe vertex through the exact `glRotatef` / `glTranslatef` sequence the renderer applies. The table below captures the earlier pre-calibration baseline (`yaw=0, pitch=31°, distance≈10.1`) so the historical world-frame diagnosis remains readable against the numbers that originally exposed it:

| Probe (OSRS-local) | Current camera-space `(x, y, z)` | Proposed-fix camera-space `(x, y, z)` |
| --- | --- | --- |
| focus (player) | `(+0.000, +0.000, -10.125)` | `(+0.000, +0.000, -10.125)` |
| north of player `(+Y+1)` | `(+0.000, -0.515, -9.268)` ❌ below & closer | `(+0.000, +0.515, -10.982)` ✓ above & further |
| south of player `(+Y-1)` | `(+0.000, +0.515, -10.982)` ❌ above & further | `(+0.000, -0.515, -9.268)` ✓ below & closer |
| east of player `(+X+1)` | `(+1.000, +0.000, -10.125)` ✓ | `(+1.000, +0.000, -10.125)` ✓ |
| west of player `(+X-1)` | `(-1.000, +0.000, -10.125)` ✓ | `(-1.000, +0.000, -10.125)` ✓ |

**The east/west axis is already correct.** Only the **north/south (Z) axis is inverted.**

The previous batch-flip recommendation (B+C+D+E on yaw rotation) was therefore the wrong fix — yaw direction wasn't the bug. The real cause is the Z-axis convention seam between the mesh and the view transform.

### Revised root cause

- `WorldSceneObjectAssembler` / `CacheBackedWorldSceneLoader` map OSRS `+Y = north` → scene-local `+localY = north`. ✓
- `TerrainSceneMeshBuilder` emits vertex at mesh `(localX, height, localY)` — so `+localY` ends up in mesh `+Z`. ✓ if mesh `+Z = north`.
- `WorldViewportRenderer` calls `glTranslatef(-focusX, -focusH, -focusY)` and then `glTranslatef(0, screenOffsetY, -distance)` — this places the **camera at `+Z` direction from focus** (with OpenGL `-Z = forward`, camera is at the focus's `+Z` side looking toward `-Z = focus`). With mesh `+Z = north`, **camera is north of player looking south**. Tiles north of the player end up between camera and focus (closer), and pitch tilts them downward on screen.

The legacy software camera looks `+Z = north` at `yaw=0` (the camera sits at `-Z = south` of focus). The native client has the **camera on the wrong physical side of the player along the north/south axis**.

### Revised fix — two coordinated edits

1. **Mesh emission**: emit `+localY` as mesh `-Z`, not mesh `+Z`. Negate Z at the mesh seam.
   - [`TerrainSceneMeshBuilder.java`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/terrain/TerrainSceneMeshBuilder.java) — every `addVertex(tileX, h, tileY)` call becomes `addVertex(tileX, h, -tileY)` (and `tileY + 1f` becomes `-(tileY + 1f)`).
   - [`SceneTriangleMeshBuilder.addGeometry`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/raster/SceneTriangleMeshBuilder.java#L160) — `offsetZ` is callers' scene-local Y, so negate before storing or have callers pass negated.
2. **View transform Z**: flip the focus Z sign in [`WorldViewportRenderer.java:63`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/WorldViewportRenderer.java#L63) — `glTranslatef(-focusX, -focusH, +focusY)` instead of `-focusY`.

After these two edits, mesh `+Z = south`, view transform places camera at `-Z`-side looking toward `+Z`, and everything aligns with legacy.

**Yaw and click planner stay untouched.** The east/west axis confirmed correct in both current and proposed-fix probes.

### Cascading sites that also consume the seam

Every other site that converts between scene-local Y and mesh Z needs the same negation:

- [`WorldViewportClickPlanner`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/WorldViewportClickPlanner.java) — the ray-vs-ground intersection currently computes `worldZ` matching the mesh `+Z`. After the fix, the intersection result's Z must be negated to recover OSRS-local Y.
- [`WorldSceneOcclusionPlanner.cameraPosition`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/visibility/WorldSceneOcclusionPlanner.java#L176) and [`WorldScenePlaneRules.cameraPosition`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/visibility/WorldScenePlaneRules.java#L139) — same: `cameraState.focusY()` enters as world Z but is currently treated as positive scene-local Y.
- [`WorldSceneObjectAssembler`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/object/WorldSceneObjectAssembler.java) — passes `localY` as a vertex offset for object placement; needs negation.
- Minimap rasterizer's `tileTopY` flip already produces north-up images regardless of mesh sign, so [`WorldSceneMinimapRasterizer`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/minimap/WorldSceneMinimapRasterizer.java) likely needs no change — verify with a probe.
- [`GameplayMinimapRenderer.minimapMarkerOffset`](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/gameplay/GameplayMinimapRenderer.java#L103) operates on `(east, north)` deltas before any mesh-Z conversion, so it should be untouched.

The change set is therefore **smaller and more localized** than the previous "flip yaw everywhere" plan, but still needs to land together because mesh-Z and view-Z disagreement causes the camera to clip everything.

## Symptom inventory

1. At the same player world position, the native 3D view and the legacy client show different layouts of nearby walls, statues, and ground items.
2. The compass / minimap heading and the 3D camera heading visually agree with each other but both disagree with the legacy reference.
3. Orbiting the camera with the same yaw delta produces opposite-direction motion vs. legacy.
4. A single isolated sign flip on `glRotatef(...)` in [WorldViewportRenderer](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/WorldViewportRenderer.java) mirrors the scene worse rather than fixing it, because click planning and visibility planning compensate for the *current* wrong direction.

## Legacy reference frame

From `server/moparscape/.../SceneGraph.method315` and `MapRegion`:

- `+X = east` (anInt455 is cameraX, world points use `tileX << 7`)
- `+Y = down in stored heights` (heights are stored as positive numbers that grow with depth, with `-anInt456` translating "lower stored Y" toward camera). Visually +Y still appears "up" on screen because the legacy software projection inverts before raster.
- `+Z = north` (anInt457 is cameraZ, world points use `tileY << 7`; default camera at `yaw=0` looks toward `+Z`).
- Yaw rotation matrix (legacy `method315` lines 1704–1706, with `l = sin(yaw)`, `i1 = cos(yaw)`):
  - `x' = x·cos(yaw) + z·sin(yaw)`
  - `z' = −x·sin(yaw) + z·cos(yaw)`
  - This is a **clockwise** rotation when viewed from `+Y` looking down.
- Default `yaw = 0` → camera sits at `+Z` relative to focus and looks toward `−Z`-camera-relative = `+Z`-world = **north**. At `yaw = 90°` (CW) the camera turns to face east.

## Current native frame (as found in audit)

| Site | File | Frame / rotation it actually applies |
| --- | --- | --- |
| Region → scene ingestion | [CacheBackedWorldSceneLoader.java](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/CacheBackedWorldSceneLoader.java) | Stores tileX/tileY identically to cache. **No explicit Y-axis decision recorded.** |
| Terrain mesh emission | [TerrainSceneMeshBuilder.java:305-308](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/terrain/TerrainSceneMeshBuilder.java#L305-L308) | Labels `NW = (tileX, tileY)`, `SW = (tileX, tileY+1)`. Implies "higher tileY = south", i.e. tileY is **inverted relative to OSRS where +Y = north**. |
| Vertex height | [TerrainSceneMeshBuilder.java:498-501](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/terrain/TerrainSceneMeshBuilder.java#L498-L501) | `elevationAt(...) * HEIGHT_SCALE` used directly as OpenGL `+Y`. Sign depends on whether `elevationAt` returns legacy-stored (positive = down) or pre-flipped (positive = up) — **not documented either way**. |
| Object yaw rotation | [SceneTriangleMeshBuilder.java:180-182](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/raster/SceneTriangleMeshBuilder.java#L180-L182) | `x' = x·cos + z·sin; z' = z·cos − x·sin` → **matches legacy CW**. |
| Object orientation 0..3 | [ObjectSceneGeometryBuilder.java](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/object/ObjectSceneGeometryBuilder.java) | Per-step `(x, z) → (z, −x)`. Matches legacy `method473` clockwise quarter-turns. |
| View transform | [WorldViewportRenderer.java:55-63](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/WorldViewportRenderer.java#L55-L63) | `glTranslatef(0, screenOffsetY, −distance)` then `glRotatef(−yaw, 0, 1, 0)`. With OpenGL `−Z = forward`, that means the camera sits at `+Z` relative to focus looking `−Z`; the yaw rotation is **CCW** (opposite of legacy CW). |
| Click planner inverse | [WorldViewportClickPlanner.java:135-143](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/WorldViewportClickPlanner.java#L135-L143) | `rotateAroundY` applies `x' = x·cos + z·sin; z' = −x·sin + z·cos` with `+yaw` — i.e. the legacy CW matrix, used to invert the renderer's wrong-direction CCW rotation. Consistent only with the renderer's current bug. |
| Visibility / occlusion | [WorldScenePlaneRules.java:141-147](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/visibility/WorldScenePlaneRules.java#L141-L147), [WorldSceneOcclusionPlanner.java:181-188](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/visibility/WorldSceneOcclusionPlanner.java#L181-L188) | Same pattern as click planner. |
| Minimap base raster | [WorldSceneMinimapRasterizer.java:472-474](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/minimap/WorldSceneMinimapRasterizer.java#L472-L474) | `tileTopY = (tileHeight − 1 − sceneY) * 4` — **flips Y for image coords**, so larger tileY = higher pixel. If tileY = north, that puts north at top (correct). If tileY = south (as terrain mesh implies), that puts north at bottom (wrong). |
| Minimap UI yaw | [GameplayMinimapRenderer.java:89-110](../../rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/gameplay/GameplayMinimapRenderer.java#L89-L110) | Returns `−yaw`. Marker rotation matrix has a built-in reflection: `[cos sin; sin −cos]`. Composed effect agrees with the 3D viewport's CCW rotation, not with legacy CW. |

### Agreements and disagreements

- **X-east**: every site agrees `+X = east`. ✓
- **Y-up**: every site agrees `+Y = up in mesh / OpenGL`. ✓ (but the sign convention of `elevationAt` is undocumented)
- **Z = north or south?**: terrain mesh labels imply `+Z = south`. Minimap rasterizer flips Y as if `+Z = south` (so the flip restores north-up image). Click/visibility/occlusion treat `+Z` consistently with the mesh. **No site disagrees here, but the choice is opposite legacy** (legacy `+Z = north`).
- **Yaw rotation direction**: renderer + minimap UI use **CCW**. Object yaw + click planner inverse use **CW**. Object meshes are still placed correctly because the renderer's CCW view rotation cancels the CW object yaw to produce the legacy visual at `yaw = 0` only. As soon as `yaw ≠ 0`, objects rotate the wrong way relative to terrain.

This is the core defect: **the renderer and the rest of the pipeline disagree on yaw direction**, and that disagreement is invisible at `yaw = 0` because both produce identity. Every observed symptom (mis-placed landmarks, wrong wall sides, mirrored compass) is a downstream effect.

## Recommended canonical frame

Adopt **legacy-faithful**:

- `+X = east`
- `+Y = up` (negate stored cache heights at scene ingestion if they are legacy-down)
- `+Z = north` (negate tileY at the mesh seam, or flip the Z axis sign once at the scene-load boundary)
- Yaw: **CW from above**, matching legacy `method315`

The negation can be applied either:
- **At ingestion** — translate cache `worldY` into native `+Z = north` at one place, then the rest of the pipeline never needs to think about it.
- **At the mesh seam** — keep `tileY` legacy-relative but negate `z` when emitting vertices to OpenGL.

The ingestion approach is cleaner. It lets the mesh builder, click planner, visibility, minimap, and object yaw all operate on `(X = east, Z = north)` consistently. Only the OpenGL camera transform has to deal with the OpenGL `−Z = forward` convention, and it does so in exactly one place (the renderer).

## Refactor workstreams

### Workstream A — canonical frame contract

- Add a `WorldFrame` value type / constants file documenting `+X = east, +Y = up, +Z = north, yaw CW from above`.
- Decide ingestion vs mesh-seam negation. Document the decision and the single conversion site.
- Add a unit test that nails: "ingesting OSRS world `(X=3200, Y=3200)` produces a native scene point where `+X = east` and `+Z = north`".

### Workstream B — renderer + view transform

- Rewrite `WorldViewportRenderer`'s `glRotatef` to `glRotatef(+yaw, 0, 1, 0)` so the view matrix is the inverse of the canonical CW yaw.
- Verify camera focus translation interprets `focusY` as `+Z = north`. If the canonical frame uses `+Z = north`, then `glTranslatef(−focusX, −focusHeight, −focusZ)` is correct; if the seam-mesh approach is used, the renderer needs to negate `focusZ` before passing it in.
- Update the orbit camera default yaw so it points the same direction as legacy at startup.

### Workstream C — click planner / visibility / occlusion

- Flip every "inverse rotation" site to undo the new (legacy-matching) view rotation. With CW view rotation, the inverse is CCW.
- Add a single shared helper (`WorldFrame.inverseViewRotation(camera, vector)`) so future tweaks happen in one place.
- Add a unit test: "click at the screen center at all four cardinal yaws maps to the tile directly in front of the player".

### Workstream D — minimap UI rotation

- Replace the ad hoc `[cos sin; sin −cos]` matrix with the canonical inverse-view rotation applied to `(east, north)` deltas, then a known image-Y flip.
- Update the minimap base rasterizer's Y flip to derive from the same `+Z = north` decision (currently it flips unconditionally and happens to be right by accident).
- Add a unit test: "marker at +1 tile east of player draws to the right of center at yaw = 0, and rotates CW as yaw increases".

### Workstream E — object orientation compatibility

- Confirm `SceneTriangleMeshBuilder.addGeometry` is using legacy CW per-vertex rotation (audit says yes — leave alone).
- Keep `ObjectSceneGeometryBuilder` quarter-turn rotation aligned with legacy clockwise `method473`.
- Lock down mirrored-definition parity: mirror `Z`, swap face winding `A/C`, and apply `translateX/Z` after rotation.
- Add per-orientation tests for boundary objects (walls), interactable objects, and ground decorations.

### Workstream F — parity diagnostics

- Add a `WorldFrameParityHarness` that, given a `(worldX, worldY, plane, yaw)` tuple, captures:
  - 3D viewport bounds of the tile directly in front of the player
  - Minimap pixel position of that same tile
  - Click target for the screen-center pixel
- Pin a small set of OSRS landmarks (Lumbridge fountain, Lumbridge stairs, Varrock west bank) and assert the four numbers above stay constant after each workstream.
- This is the only thing that will keep future scene work from re-introducing this exact class of bug.

## Ordering

Workstreams must land together as a batch — there is no safe intermediate state where only some sites use the new convention. The recommended landing order inside one batch:

1. Workstream A (contract, no behavior change).
2. Workstream F (parity harness, captures the current wrong baseline).
3. Workstreams B + C + D + E in a single commit, with the parity harness re-baselined to the legacy reference values.

Trying to land B alone (as was attempted) will appear to fix the 3D view at `yaw = 0` and immediately break click hit-testing and object orientation everywhere else.

## Out of scope

- Texture U/V orientation parity (drag-on-path artifacts) — separate Workstream, depends on this frame settling first.
- Floor color HSL parity (already covered by `.codex/plan/terrain-minimap-parity.md`).
- Object model parity (orientation flag handling distinct from yaw rotation).

## Risks and gotchas

- The OpenGL `−Z = forward` convention means even after the frame is canonicalized, the renderer is the one place where the seam happens. Mistakes here will look like "everything is mirrored" but actually only the render output is wrong.
- The `elevationAt(...)` sign is currently undocumented. If the cache stores heights as legacy-down and the mesh uses them directly as `+Y = up`, the world is upside-down in Y. Visually it looks "right" today only because nothing else exercises Y aggressively. This needs to be verified explicitly in Workstream A.
- Many existing unit tests pin yaw values like `90.0f` and assume the *current* convention. Workstream B/C/D will flip the meaning of every such pinned value. The parity harness from Workstream F should be in place first so we can tell rendering correctness apart from test-value correctness.

## Verification gate

Each workstream commit must run:
- `./gradlew :rs-client-lwjgl:test`
- Manual screenshot at the harness landmarks, compared against the legacy client reference image checked in alongside the harness.
