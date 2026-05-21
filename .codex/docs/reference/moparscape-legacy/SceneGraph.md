# SceneGraph (legacy moparscape)

## Overview

- **Source**: [server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java)
- **Line count**: 2444 lines (single Jad-decompiled class)
- **Purpose**: The central scene-graph assembly, visibility, occlusion-culling and rendering coordinator for the OSRS-317-era client. It owns the three-dimensional grid of `SceneTile` cells (`[plane][localX][localZ]`), accepts terrain (paint / blended-corner / triangle), wall, decoration, ground-decoration, ground-item, and 3D "interactive" (loc) submissions from `MapRegion` during world load, then each frame performs camera-space transform setup, frustum/occluder/plane culling and back-to-front rasterization via `Rasterizer3D`.
- **Key concepts**:
  - **Tile grid**: `aClass30_Sub3ArrayArrayArray441[plane][x][z]` — 4×104×104 for the active region.
  - **Heightmap**: `anIntArrayArrayArray440[plane][x][z]` shared with `MapRegion`, indexed at tile corners (size `[planes][x+1][z+1]`).
  - **Camera transform**: pre-computed sin/cos tables in `Model.anIntArray1689/1690` (16-bit fixed point). `anInt458/459` = pitch sin/cos; `anInt460/461` = yaw sin/cos; `anInt455/456/457` = camera X / Y / Z.
  - **Plane culling**: `method313` decides which tiles are within the 50×50 view square around the camera tile (`anInt453/454`) and visible at the current pitch via the static visibility mask `aBooleanArrayArrayArrayArray491` (pre-baked by `method310`).
  - **Occluders**: `Occluder` axis-aligned boxes registered via `method277` (one per plane in `aClass47ArrayArray474`). Active ones for the current view get projected each frame into `aClass47Array476` by `method319`; `method320/321/322/323` test tile/wall/decoration/loc corner points against them via `method324`.
  - **Wall occlusion masks (anInt1320/anInt1325/1326/1327)**: bitfield `1=west 2=north 4=east 8=south` describing which neighbouring tiles a loc straddles, used by the recursive flood traversal in `method314`.
  - **SceneTilePaint vs SceneTileModel**: `SceneTilePaint` (legacy `Class43`) is a flat textured/coloured tile (overlay covers underlay). `SceneTileModel` (legacy `Class40`) is the 1×1 sub-triangulated "shape" tile used wherever underlay and overlay coexist or where the overlay is non-rectangular. `method279` chooses between them based on shape index `l`.
  - **Render order**: `method313` enqueues visible tiles into `aClass19_477` (a Deque). `method314` is the BFS that, for each tile, walks bridge → paint/model → walls → decoration → ground decoration → ground items → interactive locs, deferring locs that straddle still-uncovered neighbours.

## Static fields

| Legacy name | Inferred modern name | Type | Purpose | Read/written |
|---|---|---|---|---|
| `anInt431` | `obfuscationGuardA` | `int` | Anti-tamper sink (assigned in dead-code guards). | Written by guards in many methods. |
| `anInt432` | `obfuscationGuardB` | `int` | Same. | Idem. |
| `anInt433` | `obfuscationGuardC` | `int` | Same. | Idem. |
| `aBoolean436` | `texturedTilesEnabled` (low-detail flag) | `boolean` | When `true`, paint/model tiles use full texturing; when `false`, fallback to the `anIntArray485`-indexed average colour and `method317` shade. Cleared by `GameClientCore` at low-detail or for minimap. | [GameClientCore.java:2488](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2488), [:9036](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9036). |
| `anInt446` | `visibleTileCount` | `int` | Number of tiles still flagged `aBoolean1322`/`aBoolean1323`; the cull/draw loops exit early when this hits zero. | `method313`, `method314`. |
| `anInt447` | `activeOccluderPlane` | `int` | Plane index used by `method319` to pick an occluder bucket. | Set by `method313`. |
| `anInt448` | `frameCounter` | `int` | Incremented each `method313` call. Used as cache key in `anIntArrayArrayArray445` (positive = below-camera result, negative = above) and as the per-frame "already drawn" tag on `InteractiveObject.anInt528`. | `method313` writes; `method320/321/322/323`, `method314` read. |
| `anInt449..452` | `cullMinX, cullMaxX, cullMinZ, cullMaxZ` | `int` | 50×50 cull rectangle around the camera tile, clipped to scene bounds. | Written by `method313`; read by `method314`. |
| `anInt453, anInt454` | `cameraTileX, cameraTileZ` | `int` | Camera world position quantized to tiles (`anInt455>>7`, `anInt457>>7`). | `method313`; read everywhere. |
| `anInt455, anInt456, anInt457` | `cameraSceneX, cameraSceneY, cameraSceneZ` | `int` | Camera world position in sub-tile (128) units. | `method313`; read by `method315/316`, `method443` calls. |
| `anInt458, anInt459` | `pitchSin, pitchCos` | `int` | 16-bit fixed-point. Indexed from `Model.anIntArray1689/1690[pitch]`. | `method310, 313`; read by transforms. |
| `anInt460, anInt461` | `yawSin, yawCos` | `int` | 16-bit fixed-point. | Idem. |
| `aClass28Array462` | `pendingLocsScratch` | `InteractiveObject[100]` | Working buffer in `method314` for locs that still need a paint-ordering pass. | `method314`; nulled in `method273/274`. |
| `anIntArray463/464` | `wallDecoXOffsetA/ZOffsetA` | `int[4]` `{53,-53,-53,53}` / `{-53,-53,53,53}` | Offsets per direction for diagonal "outer" wall-decoration sub-position (anInt502 & 0x100). | `method314`. |
| `anIntArray465/466` | `wallDecoXOffsetB/ZOffsetB` | `int[4]` `{-45,45,45,-45}` / `{45,45,-45,-45}` | Same for the "inner" diagonal sub-position (0x200). | `method314`. |
| `aBoolean467` | `mouseHoverTileRequested` | `boolean` | True while `method313`+`method314` should test each rasterized triangle against `(anInt468, anInt469)` to determine the hovered tile (`anInt470/471`). | `method312` sets, `method315/316`/`method318` read. |
| `anInt468, anInt469` | `hoverMouseX, hoverMouseY` | `int` | Screen-space mouse coordinates set by `method312`. | `method312`, `method315/316`. |
| `anInt470, anInt471` | `hoveredTileX, hoveredTileZ` | `int` (public) | Out: world tile under the cursor; reset to `-1` after caller consumes it. | Set in `method315/316`; consumed at [GameClientCore.java:3055](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L3055). |
| `anInt472` | `occluderPlaneCount` | `int` (=4) | Number of planes/buckets in `aClass47ArrayArray474`. | Static init. |
| `anIntArray473` | `occluderCountPerPlane` | `int[4]` | Per-plane count of registered occluders. | `method274`, `method277`, `method319`. |
| `aClass47ArrayArray474` | `occludersPerPlane` | `Occluder[4][500]` | Registered axis-aligned occluder boxes per plane. | `method277` adds; `method319` projects. |
| `anInt475` | `activeOccluderCount` | `int` (public) | Number of occluders projected into `aClass47Array476` for the current view. | `method319` writes; `method324` reads. |
| `aClass47Array476` | `activeOccluders` | `Occluder[500]` | View-space-relevant subset for the current camera. | `method319/324`. |
| `aClass19_477` | `tileTraversalDeque` | `Deque(169)` | FIFO/LIFO used as the traversal frontier by `method314`. | Idem. |
| `anIntArray478` | `wallNeighbourMaskTable` | `int[9]` (per camera-relative quadrant) | Lookup of which wall-bit set is visible from each of the 9 (camera vs tile) quadrants. | `method314`. |
| `anIntArray479` | `wallAdjacentMaskTable` | `int[9]` | Per-quadrant mask of the wall pieces that are "facing the camera". | `method314`. |
| `anIntArray480` | `wallDecoMaskTable` | `int[9]` | Per-quadrant mask for decoration rendering (`anInt1328`). | `method314`. |
| `anIntArray481..484` | `wallOcclusionShiftTable16/32/64/Diag` | `int[9]` | Lookup for the `anInt1326/1327` bits given `class10.anInt276` orientation 16/32/64/diag. | `method314`. |
| `anIntArray485` | `tileTypeAverageHsl` | `int[50]` | Per overlay-type packed HSL fallback colour used when `aBoolean436==false` (low detail). | `method315/316`. |
| `anIntArrayArray489` | `tileShapeMask` | `int[13][16]` | Per shape index, 4×4 grid marking which texels are "underlay" (0) vs "overlay" (1). Used only for minimap rasterization. | `method309`. |
| `anIntArrayArray490` | `tileShapeRotation` | `int[4][16]` | Per rotation 0..3, 4×4 permutation lookup into `anIntArrayArray489`. | `method309`. |
| `aBooleanArrayArrayArrayArray491` | `tileVisibilityMask` | `boolean[8][32][51][51]` | Pre-baked frustum-coverage mask, indexed `[pitchBucket][yawBucket][dx+25][dz+25]`. Computed once by `method310`. | `method310` writes; `method313` slices via `aBooleanArrayArray492`. |
| `aBooleanArrayArray492` | `currentVisibilityMask` | `boolean[51][51]` | The slice of `aBooleanArrayArrayArrayArray491` for the current pitch+yaw. | `method313`, `method319`. |
| `anInt493, anInt494` | `viewCentreX, viewCentreY` | `int` | Half-viewport for `method311` projection check. | `method310/311`. |
| `anInt495..498` | `viewMinX, viewMinY, viewMaxX, viewMaxY` | `int` | Viewport rectangle for `method311`. | `method310/311`. |

## Instance fields

Grouped:

### Construction parameters
- `anInt437` → `planeCount` (e.g. 4).
- `anInt438` → `localSizeX` (e.g. 104).
- `anInt439` → `localSizeZ` (e.g. 104).
- `anIntArrayArrayArray440` → `heightMap` (`int[plane][x+1][z+1]`, shared with `MapRegion`).

### Tile grid / loc tracking
- `aClass30_Sub3ArrayArrayArray441` → `tiles` (`SceneTile[plane][x][z]`, lazily allocated).
- `anInt442` → `minDrawPlane` (set by `method275`, default 0; raised by client to hide upper floors).
- `aClass28Array444` → `activeLocs` (`InteractiveObject[5000]`, the locs added with `flag=true` in `method287`).
- `anInt443` → `activeLocCount`.
- `anIntArrayArrayArray445` → `cornerCullCache` (`int[plane][x+1][z+1]`, sign-magnitude-tagged with `anInt448` to cache `method320` per-tile occluder verdicts).

### Per-loc dirty-rebuild scratch (used by `method305`/`method308`)
- `anIntArray486, anIntArray487` → `vertexMergeMarkerA/B` (`int[10000]`).
- `anInt488` → `mergeCounter` (incremented per `method308` invocation; used like `anInt448` but for vertex-merge tagging).

### Flow-control booleans
- `aBoolean429, aBoolean434, aBoolean435` → unused debug toggles (only flipped inside obfuscation guards).
- `anInt430` → obfuscation-guard return value (returned from `method301`/`method317` on guard branches that should never run).

## Methods

### Constructor `SceneGraph(int i, byte byte0, int j, int ai[][][], int k)` — proposed name: `new SceneGraph(localSizeX, marker, localSizeZ, heightMap, planeCount)`
- **Signature**: `public SceneGraph(int localX, byte marker43, int localZ, int[][][] heights, int planes)`.
- **Purpose**: Allocates the tile grid, the corner-cull cache, and resets the global occluder/loc state via `method274(619)`. The `byte0!=43` branch is a Jad-paranoia guard that always passes in practice.
- **Parameters**: `i = localSizeX` (104), `byte0 = sentinel 43`, `j = localSizeZ` (104), `ai = heightMap shared with MapRegion`, `k = planeCount` (4).
- **Returns**: none.
- **Called by**: [GameClientCore.java:6354](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L6354).
- **Calls**: `method274`.
- **Notes**: `aClass30_Sub3ArrayArrayArray441 = new SceneTile[plane][x][z]` and `anIntArrayArrayArray445 = new int[plane][x+1][z+1]` (the `+1` because cull cache is indexed by tile corners).

### `method273(int i)` — proposed name: `unloadStatics`
- **Signature**: `public static void method273(int marker)`.
- **Purpose**: Drops the static visibility mask and per-plane occluder pool to make them eligible for GC.
- **Parameters**: `i` must be negative; the `while(i>=0)` is an infinite-loop guard executed if a wrong marker is passed (caller always passes `-501`).
- **Returns**: none.
- **Called by**: [GameClientCore.java:4757](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L4757) (`SceneGraph.method273(-501)`).
- **Calls**: none.
- **Notes**: Nulls `aClass28Array462`, `anIntArray473`, `aClass47ArrayArray474`, `aClass19_477`, `aBooleanArrayArrayArrayArray491`, `aBooleanArrayArray492`.

### `method274(int i)` — proposed name: `clearScene`
- **Signature**: `public void method274(int marker)`.
- **Purpose**: Wipes every tile slot, clears all occluder lists, and clears the active-locs array. Called after `MapRegion` finishes streaming a new region and before a re-load.
- **Parameters**: `i` must be non-zero (`37/i` triggers `ArithmeticException` if zero) — a Jad guard. Callers pass `619`.
- **Returns**: none.
- **Called by**: constructor; [GameClientCore.java:477](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L477), [:1971](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1971).
- **Calls**: none.
- **Notes**: Resets `anInt443` and `anIntArray473[*]` to 0.

### `method275(int i, int j)` — proposed name: `ensurePlaneAllocated`
- **Signature**: `public void method275(int plane, int marker)`.
- **Purpose**: Pre-creates an empty `SceneTile` for every (x,z) on a given plane and stores `plane` as the new `anInt442` (minimum draw plane). Used by the client to set the active "lowest visible" floor.
- **Parameters**: `i = plane`, `j` must equal `-34686`.
- **Returns**: none.
- **Called by**: [GameClientCore.java:619, :621](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L619).
- **Calls**: none.
- **Notes**: Sets `anInt442 = plane`; the rendering loop in `method313` starts at `anInt442`, effectively hiding planes below the player.

### `method276(int i, int j, int k)` — proposed name: `collapsePlanesForBridge`
- **Signature**: `public void method276(int localX, int localZ, int marker)`.
- **Purpose**: Special handling for bridge tiles: shifts planes [1..3] down by one at `(localX, localZ)`, decrements each loc's `anInt517` plane reference, and stores the original plane-0 tile in `aClass30_Sub3_1329` of the new plane-0 (so the bridge under-tile can still be rendered before the deck — see `method314`'s `aClass30_Sub3_1329` block).
- **Parameters**: `i = localX`, `j = localZ`, `k = guard (must be <0)`.
- **Returns**: none.
- **Called by**: [MapRegion.java:317](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L317).
- **Calls**: none.
- **Notes**: After the shift, plane 3 is nulled. The `(class28.anInt529 >> 29 & 3) == 2` filter is an OSRS loc-type check: only "interactable" locs whose anchor matches the tile coords get their plane decremented (avoids double-decrement for multi-tile locs).

### `method277(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2)` — proposed name: `addOccluder`
- **Signature**: `public static void method277(int plane, int minX, int minY, int maxX, int maxY, int minZ, int maxZ, int marker, int axis)`.
- **Purpose**: Constructs an `Occluder` and appends it to the per-plane bucket. Parameter ordering is convoluted; from MapRegion call-sites:
- **Parameters** (mapped from [MapRegion.java:371,416,459](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L371)): `i = plane`, `j = minX` (in world units), `k = minY`, `l = maxX`, `i1 = maxZ`, `j1 = maxY`, `k1 = guard`, `l1 = minZ`, `i2 = axis (1=X-plane, 2=Z-plane, 4=Y-plane)`.
- **Returns**: none.
- **Called by**: [MapRegion.java:371](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L371), [:416](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L416), [:459](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L459).
- **Calls**: none.
- **Notes**: Stores both world-unit bounds (`anInt792..797`) and tile-unit bounds (`anInt787..790 = bound/128`). `anInt791` = axis. These are consumed by `method319` and `method324`.

### `method278(int i, int j, int k, int l)` — proposed name: `setTileLogicHeight`
- **Signature**: `public void method278(int plane, int localX, int localZ, int logicHeight)`.
- **Purpose**: Stores per-tile logic height (`SceneTile.anInt1321`) used by `method313` to early-cull tiles whose logic exceeds the supplied threshold `i1` — typically "roof above player" suppression.
- **Parameters**: `i = plane`, `j = localX`, `k = localZ`, `l = logicHeight`.
- **Returns**: none.
- **Called by**: [MapRegion.java:304](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L304).
- **Calls**: none.

### `method279(...20 args...)` — proposed name: `submitTile`
- **Signature**: `public void method279(int plane, int localX, int localZ, int shape, int rotation, int underlayTextureId, int swColorA, int seColorA, int neColorA, int nwColorA, int swColorB, int seColorB, int neColorB, int nwColorB, int swHsl, int seHsl, int neHsl, int nwHsl, int overlayTextureId, int overlayRgb)`. (See [MapRegion.java:272, :291](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L272) for arg shape.)
- **Purpose**: Build the appropriate terrain primitive for one tile and attach it to `tiles[plane][x][z]`, lazily allocating all lower planes' tiles too (so an upper-plane paint also creates the plane-0 cell — used by occluder corner lookup).
  - `shape == 0`: underlay-only paint → `SceneTilePaint(swColorA, seColorA, neColorA, nwColorA, -1, overlayRgb, false)`.
  - `shape == 1`: overlay-only paint with a single `underlayTextureId` and the `B` colour set; sets `monochrome` flag if all four B colours match.
  - `shape > 1`: blended `SceneTileModel` (sub-triangulated, both layers).
- **Parameters**: see signature.
- **Returns**: none.
- **Called by**: [MapRegion.java:272](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L272), [:291](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L291).
- **Calls**: `new SceneTilePaint` or `new SceneTileModel`, `new SceneTile`.
- **Notes**: The "lazy-allocate every lower plane" pattern (`for(i5=i;i5>=0;i5--)`) appears in every submit method — see `method280, 282, 283, 287` etc. This guarantees occluder corner lookups never trip a null.

### `method280(int i, int j, int k, int l, Renderable model, byte type, int i1, int j1)` — proposed name: `submitGroundDecoration`
- **Signature**: `(plane, sceneY, localZ, marker, model, hashType, configBits, localX)`.
- **Purpose**: Place a `GroundDecoration` on the tile (the third loc layer, e.g. flowers/stones laid flat on terrain). Computes world centre `anInt812 = localX*128+64`, `anInt813 = localZ*128+64`, stores `sceneY` and a hash.
- **Parameters**: `i=plane`, `j=sceneY`, `k=localZ`, `l=guard (must be >0)`, `class30_sub2_sub4=Renderable`, `byte0=loc-hash type byte`, `i1=configBits`, `j1=localX`.
- **Returns**: none.
- **Called by**: [MapRegion.java:569](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L569), [:1119](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1119).
- **Calls**: lazy `new SceneTile`.

### `method281(byte byte0, int i, int j, Renderable a, int k, Renderable b, Renderable c, int l, int i1)` — proposed name: `submitGroundItemPile`
- **Signature**: `(marker7, localX, sceneY, top, hash, middle, bottom, plane, localZ)`.
- **Purpose**: Place the ground-item pile (up to three stacked `Renderable`s) on a tile. Computes `anInt52` = max `Model.anInt1654` of any interactive loc already standing on this tile, so the items render above the loc when `anInt52 != 0`.
- **Parameters**: `byte0` must equal 7 (guard), `i=localX`, `j=sceneY`, `class30_sub2_sub4=topModel`, `k=hash`, `class30_sub2_sub4_1=middleModel`, `class30_sub2_sub4_2=bottomModel`, `l=plane`, `i1=localZ`.
- **Returns**: none.
- **Called by**: [GameClientCore.java:818](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L818).
- **Calls**: lazy `new SceneTile`.

### `method282(int i, Renderable a, boolean flag, int j, int k, byte type, int l, Renderable b, int i1, int j1, int k1)` — proposed name: `submitWallObject`
- **Signature**: `(hash, primaryModel, marker, sceneY, localZ, hashType, localX, secondaryModel, anInt273, anInt277, plane)`.
- **Purpose**: Attach a `WallObject` (one or two model halves for L/corner walls) to a single tile. Lazily allocates lower-plane tiles up to and including `plane`.
- **Parameters**: From [MapRegion.java:630](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L630): `i=hash (`anInt276`)`, `class30_sub2_sub4=primary`, `flag=guard (must be true)`, `j=sceneY`, `k=localZ`, `byte0=hashType`, `l=localX`, `class30_sub2_sub4_1=secondary`, `i1=anInt273 (extraConfig)`, `j1=anInt277 (secondary orientation bitfield)`, `k1=plane`.
- **Returns**: none.
- **Called by**: [MapRegion.java:630, :682, :710, :752, :1158, :1166, :1183, :1191](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L630).

### `method283(int i, int j, int k, int l, int i1, int j1, int k1, Renderable a, int l1, byte type, int i2, int j2)` — proposed name: `submitWallDecoration`
- **Signature**: `(hash, localZ, anInt505_or_dir, guard, plane, xOffset, anInt499, model, localX, hashType, zOffset, anInt502)`.
- **Purpose**: Attach a `WallDecoration` (e.g. wall-mounted torch, painting) to a tile. Stores world-space centre + a small offset (`+j1`, `+i2`) so the deco sits slightly off the wall plane.
- **Parameters**: From [MapRegion.java:801](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L801): `i=anInt505`, `j=localZ`, `k=anInt503 (direction 0..3)`, `l=guard (must be <0)`, `i1=plane`, `j1=xOffset`, `k1=anInt499 (sceneY)`, `class30_sub2_sub4=model`, `l1=localX`, `byte0=hashType`, `i2=zOffset`, `j2=anInt502 (orientation/diagonal mask)`.
- **Returns**: none.
- **Called by**: [MapRegion.java:801, :811, :816, :821, :826, :1229, :1239, :1244, :1249, :1254](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L801).

### `method284(...12 args...)` — proposed name: `submitLoc1x1`
- **Signature**: `public boolean method284(int hash, byte hashType, int localZ, int sizeMarker, Renderable model, int sizeMarker2, int plane, int j1, byte guard110, int sceneY, int localX)`.
- **Purpose**: Submit a single-tile interactive loc by computing its scene-centre and delegating to `method287`.
- **Parameters**: From [MapRegion.java:592](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L592): `i=hash (anInt529)`, `byte0=hashType`, `j=localZ`, `k=tileSizeX (always 1 here)`, `model`, `l=tileSizeZ`, `i1=plane`, `j1=anInt522 (orientation)`, `byte1=guard`, `k1=sceneY`, `l1=localX`.
- **Returns**: `true` on success (model null returns true as a no-op).
- **Called by**: [MapRegion.java:592, :617, :771, :1141, :1150, :1199](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L592).
- **Calls**: `method287`.

### `method285(int i, int j, byte byte0, int k, int l, int i1, int j1, int k1, Renderable a, boolean flag)` — proposed name: `submitProjectileOrActor`
- **Signature**: `(plane, anInt524-or-rot, marker6, anInt522, hash, sceneZ, sceneY, sceneX, model, isMoving)`.
- **Purpose**: Submit a moving entity (NPC / player / projectile) into the scene. When `flag==true` and the rotation `j` falls in one of four ranges (`(640,1408)`, `(1152,1920)`, `(1664,2048)|(0,384)`, `(128,896)`) the bounding rectangle is extended by 128 in that axial direction to account for partial-tile crossing.
- **Parameters**: From [GameClientCore.java:841](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L841): `i=plane`, `j=anInt524 (rotation)`, `byte0=guard 6`, `k=anInt522 (orientation)`, `l=hash`, `i1=sceneZ`, `j1=sceneY (height offset, e.g. 60)`, `k1=sceneX`, `class30_sub2_sub4=actor`, `flag=isWalking`.
- **Returns**: `true` if accepted.
- **Called by**: [GameClientCore.java:841, :2102, :2684](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L841).
- **Calls**: `method287`.
- **Notes**: The `byte0==6 else throw NullPointerException` is a Jad guard.

### `method286(...14 args...)` — proposed name: `submitMultiTileEntity`
- **Signature**: `(marker35, plane, anInt524, model, anInt522, hash, sceneY, sceneZ, l, minLocalX, maxLocalX, j2, sceneX, byte35)`.
- **Purpose**: Variant of `method285` for actors that explicitly span a multi-tile rectangle (used by some big NPCs in cycle scene assembly).
- **Parameters**: From [GameClientCore.java:2092](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2092): `i=guard 60`, `j=plane`, `k=anInt524`, `model`, `l=anInt522`, `i1=sceneY`, `j1=sceneZ`, `k1=hash`, `l1=minLocalX`, `i2=maxLocalX`, `j2=anInt527 (priority?)`, `k2=anInt525 (minLocalZ)`, `byte0=guard 35`.
- **Returns**: `true` on accept.
- **Called by**: [GameClientCore.java:2092](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2092).
- **Calls**: `method287`.

### `method287(...) private` — proposed name: `addInteractiveObject`
- **Signature**: `private boolean method287(int plane, int minLocalX, int minLocalZ, int sizeX, int sizeZ, int sceneZ, int sceneX, int hash, Renderable model, int orientation, boolean registerForRebuild, int hashFull, byte hashType)`.
- **Purpose**: The shared loc-insertion routine. Verifies the rectangle has free slots (each tile can hold ≤5 locs), allocates an `InteractiveObject`, fills its bounds, and for every covered (x,z) computes the edge bitmask (`k3` bits 1=west,2=south,4=east,8=north relative to the loc rectangle) and ORs it into `SceneTile.anInt1320`. Also pushes the loc into `aClass28Array444` when `registerForRebuild==true`.
- **Returns**: `false` if any covered tile is out-of-bounds or already saturated; `true` on success.
- **Called by**: `method284, 285, 286` (internal only).
- **Calls**: lazy `new SceneTile`, `new InteractiveObject`.
- **Notes**: The 5-loc-per-tile limit, `anInt529 >> 29 & 3` loc-type field and edge bitmask exactly match the 317-era reference deob. `anInt522` = orientation, `anInt527` = rendering priority computed lazily in `method314`.

### `method288(byte byte0)` — proposed name: `clearTransientLocs`
- **Signature**: `public void method288(byte marker104)`.
- **Purpose**: Removes every loc that was added via `flag=true` (`aClass28Array444[0..anInt443-1]`) from the tiles they cover by calling `method289` on each, then clears the active-locs array. Called once per frame after rendering.
- **Called by**: [GameClientCore.java:10191](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L10191).
- **Calls**: `method289`.

### `method289(int i, InteractiveObject class28) private` — proposed name: `removeLocFromTiles`
- **Signature**: `private void method289(int guard, InteractiveObject loc)`.
- **Purpose**: Walk the loc's covered rectangle and splice it out of each tile's `aClass28Array1318`, re-OR the remaining edge masks into `anInt1320`. `guard` must be negative.
- **Called by**: `method288`, `method293`.

### `method290(int i, int j, int k, int l, int i1)` — proposed name: `interpolateWallDecorationPosition`
- **Signature**: `(localZ, guard, t16, localX, plane)`.
- **Purpose**: Linearly interpolate a `WallDecoration`'s world position from its current spot toward the tile centre by factor `k/16` (used for animating doors swinging open relative to their hinge).
- **Called by**: [MapRegion.java:676, :746](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L676).

### `method291(int i, int j, int k, byte byte0)` — proposed name: `removeWallObject`
- **Signature**: `(localX, plane, localZ, marker-119)`.
- **Purpose**: Null out `tiles[plane][x][z].aClass10_1313`.
- **Called by**: [GameClientCore.java:9292](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9292).

### `method292(int i, int j, int k, int l)` — proposed name: `removeWallDecoration`
- **Signature**: `(guard0, localZ, localX, plane)`.
- **Purpose**: Null `aClass26_1314`.
- **Called by**: [GameClientCore.java:9298](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9298).

### `method293(int i, int j, int k, int l)` — proposed name: `removeLocAtTile`
- **Signature**: `(plane, guard, localX, localZ)`.
- **Purpose**: Find an "interactive" (`anInt529>>29 & 3 == 2`) loc whose anchor matches `(localX, localZ)` on the given plane, delegate to `method289`.
- **Called by**: [GameClientCore.java:9301](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9301).

### `method294(byte byte0, int i, int j, int k)` — proposed name: `removeGroundDecoration`
- **Signature**: `(marker9, plane, localZ, localX)`.
- **Called by**: [GameClientCore.java:9310](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9310).

### `method295(int i, int j, int k)` — proposed name: `removeGroundItemPile`
- **Signature**: `(plane, localX, localZ)`.
- **Called by**: [GameClientCore.java:788](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L788).

### `method296(int i, int j, int k, boolean flag)` — proposed name: `getWallObject`
- **Signature**: `(plane, localX, localZ, guard)`.
- **Returns**: `WallObject` at tile, or null.
- **Called by**: [GameClientCore.java:8841](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L8841).

### `method297(int i, int j, int k, int l)` — proposed name: `getWallDecoration`
- **Signature**: `(localZ, guard>0, localX, plane)`.
- **Returns**: `WallDecoration` at tile.
- **Called by**: [GameClientCore.java:8857](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L8857).

### `method298(int i, int j, byte byte0, int k)` — proposed name: `getInteractiveLoc`
- **Signature**: `(localX, localZ, marker4, plane)`.
- **Returns**: First "interactive" loc anchored at the tile, or null.
- **Called by**: [GameClientCore.java:8863](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L8863).

### `method299(int i, int j, int k, int l)` — proposed name: `getGroundDecoration`
- **Signature**: `(localZ, localX, plane, guard0)`.
- **Returns**: `GroundDecoration` or null.
- **Called by**: [GameClientCore.java:8871](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L8871).

### `method300..303` — proposed names: `getWallObjectHash`, `getWallDecorationHash`, `getInteractiveLocHash`, `getGroundDecorationHash`
- **Signature**: `(plane, localX, localZ[, guard])`.
- **Purpose**: Same as the four getters above but returning `anInt280` / `anInt505` / `anInt529` / `anInt815` (the loc hash). Used by the right-click menu builder.
- **Called by**: [GameClientCore.java:743, :2244, :2342, :2382, :9277..9283](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L743) and [MapRegion.java:806, :1234](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L806).

### `method304(int i, int j, int k, int l)` — proposed name: `getLocHashTypeForHash`
- **Signature**: `(plane, localX, localZ, hash)`.
- **Purpose**: Search every layer for a loc matching `hash` and return its packed `aByte5..._anInt5..._&0xff` "hash type byte" (used for click-context dispatch); `-1` if none.
- **Called by**: [GameClientCore.java:2249, :2345, :3307, :4449, :9286](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2249).

### `method305(int i, byte byte0, int j, int k, int l, int i1)` — proposed name: `applyLightingToAllModels`
- **Signature**: `(lightDirZ, marker3, lightDiffuse, lightDirX, lightAmbient, lightDirY)`.
- **Purpose**: Sweep every tile, every layer; for any `Model` whose normals (`aClass33Array1425`) are still un-baked, run vertex-normal merging with adjacent neighbours (`method306/307/308`) and then bake the per-vertex shaded HSL via `Model.method480(ambient, attenuated, normalX, normalY, normalZ)`.
- **Parameters**: From [MapRegion.java:311](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L311) (`class25.method305(-10, (byte)3, 64, -50, 768, -50)`): `i=lightDirZ`, `byte0=guard 3`, `j=lightAmbient (64)`, `k=lightDirX (-50)`, `l=lightContrast (768)`, `i1=lightDirY (-50)`.
- **Returns**: none.
- **Called by**: [MapRegion.java:311](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L311).
- **Calls**: `method306, 307, 308`, `Model.method480`.
- **Notes**: `k1 = lightContrast * sqrt(dx²+dy²+dz²) >> 8` — pre-attenuated diffuse magnitude. Walls run `method307` against their 3×3 neighbourhood; ground decorations run `method306` against four cardinal neighbours; locs run `method307` over a rectangle the size of their footprint.

### `method306(int i, int j, Model m, byte byte0, int k) private` — proposed name: `mergeNormalsForGroundDecoration`
- Walk the four cardinal ground-decoration neighbours and merge shared vertices.

### `method307(int i, int j, int k, int l, byte byte0, int i1, Model m) private` — proposed name: `mergeNormalsAcrossLocFootprint`
- For a loc of footprint `j×k` anchored at `(l,i1)` on plane `i`, walk every cell in (plane, plane+1) within the rectangle and merge normals across walls/locs whose models also expose `aClass33Array1425`. Uses the heightmap delta `i3` between current and neighbour tile as the Y offset before merging.

### `method308(Model a, Model b, int dx, int dy, int dz, boolean flag) private` — proposed name: `mergeVertexNormals`
- For every vertex in `a` whose offset-by-(dx,dy,dz) matches a vertex in `b`, accumulate normals on both sides and flag the vertex with `anInt488`. When `flag==true` and ≥3 vertices merged, also blanks face colour (`anIntArray1637[face]=-1`) on faces whose three vertices all merged (so the seam between two merged models doesn't render twice).

### `method309(int ai[], int i, int j, int k, int l, int i1)` — proposed name: `rasterizeMinimapTile`
- **Signature**: `public void method309(int[] dest, int destOffset, int destStride, int plane, int localX, int localZ)`.
- **Purpose**: Write a 4×4 block of pixels into the minimap pixel buffer for one tile.
  - If the tile holds a `SceneTilePaint`, fill with the paint's `anInt722` mini-colour.
  - If it holds a `SceneTileModel`, look up `anIntArrayArray489[shape]` and `anIntArrayArray490[rotation]` to decide for each of 16 sub-texels whether to plot the overlay colour `k2` or underlay colour `j2`. When `j2==0` (no underlay) the underlay pixels are left untouched (transparent minimap).
- **Parameters**: `ai=destBuffer`, `i=destOffset`, `j=destStride`, `k=plane`, `l=localX`, `i1=localZ`.
- **Returns**: none.
- **Called by**: [GameClientCore.java:713, :715](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L713) (plane and plane+1, so a bridge appears on minimap).

### `method310(int i, int j, int k, int l, int ai[], boolean flag)` — proposed name: `bakeVisibilityMask`
- **Signature**: `public static void method310(int minYNeg, int maxY, int viewportW, int viewportH, int[] zoomTable, boolean guard)`.
- **Purpose**: Pre-bake the static `aBooleanArrayArrayArrayArray491[8 pitch buckets][32 yaw buckets][51 dx+25][51 dz+25]` mask. For each pitch index 128..384 step 32 and each yaw 0..2048 step 64, projects ±26 tiles in 128-unit steps over the Y range [-i..j] using `method311`; sets the per-(pitch,yaw,dx,dz) cell true if any sample is inside the viewport. Then dilates the result by 1 cell in both pitch and yaw to produce the runtime mask used by `method313`.
- **Parameters**: `i=worldMinY (~500)`, `j=worldMaxY (~800)`, `k=viewportW (512)`, `l=viewportH (334)`, `ai=zoomTable per-pitch-bucket Z scale (size 9)`, `flag=guard (must be false)`.
- **Returns**: none.
- **Called by**: [BootstrapGraphicsSetup.java:79](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L79).
- **Calls**: `method311`.

### `method311(byte byte0, int i, int j, int k)` — proposed name: `projectAndClip`
- **Signature**: `public static boolean method311(byte marker9, int zoom, int sceneX, int sceneZ)`.
- **Purpose**: Project `(sceneX, ai[bucket]=zoom, sceneZ)` through the current pitch/yaw matrix in `anInt458..461` and return whether the resulting screen point falls inside `(anInt495..498)`. Rejects samples with view-space Z `<50` or `>3500`.
- **Returns**: `true` if visible.
- **Called by**: `method310`.

### `method312(boolean flag, int i, int j)` — proposed name: `requestHoveredTileQuery`
- **Signature**: `(guard, mouseY, mouseX)`.
- **Purpose**: Arm `method313`/`method315`/`method316` to record the world tile under the cursor for this frame.
- **Called by**: [GameClientCore.java:3656, :3658](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L3656).

### `method313(int i, int j, int k, int l, int i1, int j1, boolean flag)` — proposed name: `renderScene`
- **Signature**: `public void method313(int cameraSceneX, int cameraSceneZ, int yaw, int cameraSceneY, int logicHeightCutoff, int pitch, boolean guard)`.
- **Purpose**: Top-level per-frame entry point.
  1. Clamps camera into scene bounds and re-derives `anInt453/454`, `anInt455..461`.
  2. Picks the corresponding `aBooleanArrayArray492` slice from the pre-baked visibility mask.
  3. `method319(0)` projects occluders into `aClass47Array476` for this view.
  4. Iterates plane `[anInt442..anInt437)` and the 50×50 cull rectangle, marking each tile `aBoolean1322=aBoolean1323=true` (or false if hidden by mask / logic-height cutoff / under-camera). Counts visible tiles in `anInt446`.
  5. Two outer painter-algorithm sweeps (`method314(...,true)` then `method314(...,false)`) walk the tiles in concentric L-shape order centred on the camera (`(anInt453+dx, anInt454+dz)` and the three mirror points) — closest-to-camera first.
- **Parameters**: From [GameClientCore.java:10190](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L10190): `i = anInt858 (cameraSceneX)`, `j = anInt860 (cameraSceneZ)`, `k = anInt862 (yaw)`, `l = anInt859 (cameraSceneY)`, `i1 = j (logic-height cutoff = `anInt1321` ceiling)`, `j1 = anInt861 (pitch)`, `flag=guard (must be false)`.
- **Returns**: none.
- **Called by**: [GameClientCore.java:10190](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L10190).
- **Calls**: `method319`, `method314`.
- **Notes**: `aBoolean1322` = "active in current pass" (cleared as the tile is drained); `aBoolean1323` = "still in this frame's visible set" (cleared when fully drained); `aBoolean1324` = "has locs whose paint order still needs to be resolved against neighbours".

### `method314(SceneTile tile, boolean flag)` — proposed name: `traverseAndDrawTile`
- **Signature**: `public void method314(SceneTile seed, boolean recurseNeighbours)`.
- **Purpose**: The depth-first traversal/back-to-front painter. Started from a seed tile, it pulls from `aClass19_477` (LIFO) and, for each tile:
  - If not yet "drained" (`aBoolean1322`), check that all four cardinal-neighbour tiles closer to the camera have been drained or are blocked by a wall in the tile's `anInt1320` edge mask; if any closer neighbour is undrained, skip and keep the tile for later.
  - Render the bridge sub-tile from `aClass30_Sub3_1329` first (paint or model + its locs).
  - Render the tile's `SceneTilePaint`/`SceneTileModel` via `method315/316` (skipping when `method320` reports the four corners hidden by occluders).
  - Render the `WallObject` halves via `Model.method443` (skipping when `method321` says hidden); decoration via `method322`; ground decoration and ground-item pile.
  - Then iteratively pick interactive locs whose camera-distance priority `anInt527` is largest, draw them via `method443` (skipping when `method323` is true), and re-enqueue tiles those locs straddle so their walls/decoration can finalise. Locs whose footprint still overlaps undrained tiles defer.
  - Finally, when `aBoolean1323` flips off, recurse into the four cardinal neighbours that are further from the camera.
- **Notes**: This is the heart of the OSRS painter algorithm — its correctness depends entirely on the `anIntArray478..484` lookup tables for which wall faces / decoration sub-positions are camera-facing.

### `method315(SceneTilePaint tile, int plane, int pitchSin, int pitchCos, int yawSin, int yawCos, int localX, int localZ)` — proposed name: `drawTilePaint`
- **Purpose**: Transform the four tile-corner positions (XZ from `localX*128`/`localZ*128`, Y from `anIntArrayArrayArray440`) through the camera matrix, project to screen, perform CCW back-face test, optionally hit-test against the mouse via `method318`, and dispatch:
  - Untextured + colour set → `Rasterizer3D.method374` (gouraud triangle).
  - Textured high-detail → `Rasterizer3D.method378` (textured gouraud).
  - Textured low-detail (`aBoolean436==false`) → `Rasterizer3D.method374` with each corner colour replaced by `method317(_, anIntArray485[textureId], cornerHsl)`.
- **Notes**: Issues two triangles. The first (`(i6,j6),(k6,l6),(k5,l5)`) uses corners `(swColorB, seColorB, nwColorB)`; the second uses `(swColorA, neColorA, seColorA)`. `class43.aBoolean721` decides whether to pass the "A" or "B" world-space corner triplet to `Rasterizer3D.method378` (this is the "monochrome" optimisation — all-equal corners can share texture coords).

### `method316(int localX, byte marker99, int pitchSin, int yawSin, SceneTileModel tile, int pitchCos, int localZ, int yawCos)` — proposed name: `drawTileModel`
- **Purpose**: Same as `method315` but for the sub-triangulated `SceneTileModel`. Transforms each of the (typically 6 or 9) sub-vertices into `SceneTileModel.anIntArray690/691/692` (scene XYZ) and `anIntArray688/689` (screen XY), then walks the `anIntArray679/680/681` face index lists; for each face does back-face test, optional mouse hit-test, and emits the same three rasterizer dispatch paths as `method315`.

### `method317(int marker, int textureLightHsl, int cornerHsl)` — proposed name: `applyTextureAverageShade`
- **Purpose**: Compose the low-detail fallback colour. `cornerHsl` carries the 7-bit luminance; this multiplies it by the texture-average's luminance `(textureLightHsl & 0x7f)`, clamps 2..126, then re-packs with `(textureLightHsl & 0xff80)`.
- **Returns**: Packed HSL value (`int`).
- **Called by**: `method315, method316`.

### `method318(int mouseX, int mouseY, int x0, int y0, int x1, int x2, int y1, int y2)` — proposed name: `pointInProjectedTriangle`
- **Purpose**: Tests whether the screen-space point `(mouseY, mouseX)` lies inside the triangle defined by three screen points. Used during rasterization to set `anInt470/471` to the hovered tile.
- **Returns**: `true` if inside.
- **Notes**: Argument order is convoluted: callers pass `(anInt468, anInt469, y0, y1, y2, x0, x1, x2)` — i.e. mouseX is parameter `i`, mouseY is parameter `j`, and the next six are the three (x, y) projected corners interleaved as (yA, yB, yC, xA, xB, xC).

### `method319(int i) private` — proposed name: `projectActiveOccluders`
- **Signature**: `(guard0)`.
- **Purpose**: For the active plane (`anInt447`), walk the registered occluders. Per-axis (`anInt791 == 1|2|4`):
  - Check the occluder's tile-space bounding rectangle intersects the visibility-mask slice `aBooleanArrayArray492`.
  - Compute the distance from the camera to the occluder plane in scene units (must exceed 32 to be relevant; negative = on the opposite side, mode `2`/`4`).
  - Pre-compute the four `anInt799..804` slopes (`(facePoint - cameraScene) << 8 / distance`) used as ramp factors by `method324`.
  - Add the occluder to `aClass47Array476`.

### `method320(int plane, int localX, int localZ) private` — proposed name: `isTileFullyOccluded`
- **Purpose**: Quick four-corner occluder test on the tile (plane,X,Z). Caches its verdict in `anIntArrayArrayArray445[plane][x][z]` keyed by sign of `anInt448`.
- **Returns**: `true` if all four tile corners are occluded.
- **Calls**: `method324` four times.

### `method321(int plane, int localX, int localZ, int wallFlag) private` — proposed name: `isWallOccluded`
- **Purpose**: Test the visible wall segment for occlusion. For axis-aligned wall flags `1/2/4/8` (S/E/W/N), checks two corner endpoints at three heights (terrain, terrain-120, terrain-230). For diagonal flags `16/32/64/128`, tests the wall midpoint at terrain-238 plus one corner.
- **Returns**: `true` if occluded.
- **Calls**: `method320`, `method324`.

### `method322(int plane, int localX, int localZ, int modelHeight) private` — proposed name: `isDecorationOccluded`
- **Purpose**: Four-corner occluder test using `terrain - modelHeight` instead of terrain itself.

### `method323(int plane, int minX, int maxX, int minZ, int maxZ, int modelHeight) private` — proposed name: `isLocFootprintOccluded`
- **Purpose**: 1×1 locs reuse the four-corner test; multi-tile locs first do a fast scan of `anIntArrayArrayArray445` (any cell already marked "not occluded" short-circuits to false), then test the four outer corners.

### `method324(int sceneX, int sceneY, int sceneZ) private` — proposed name: `isPointOccluded`
- **Purpose**: The actual point-in-frustum-occluder test. Loops over `aClass47Array476`; depending on the occluder's `anInt798 ∈ {1..5}` (which face of the box faces the camera), interpolates the relevant face rectangle proportional to the test point's distance from the camera-side plane (`>>8` to undo the `<<8` in `method319`) and checks whether the test point lies inside.
- **Returns**: `true` if any occluder hides the point.

## Cross-class call graph

### From `MapRegion.java`
- `class25.method274(619)` — clear scene before reload: implicit via `GameClientCore` callers but `MapRegion` does not call it directly; mentioned here for completeness.
- `class25.method275(plane, -34686)` — done by `GameClientCore`, not `MapRegion`.
- `class25.method276(plane, ..., -438)` at [MapRegion.java:317](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L317) — bridge collapse.
- `class25.method278(...)` at [MapRegion.java:304](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L304) — logic height.
- `class25.method279(...)` at [MapRegion.java:272](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L272), [:291](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L291) — terrain paint/blend submit.
- `class25.method280(...)` at [MapRegion.java:569](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L569), [:1119](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1119) — ground decoration.
- `class25.method282(...)` at [MapRegion.java:630](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L630), [:682](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L682), [:710](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L710), [:752](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L752), [:1158](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1158), [:1166](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1166), [:1183](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1183), [:1191](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1191) — walls.
- `class25.method283(...)` at lines 801, 811, 816, 821, 826, 1229, 1239, 1244, 1249, 1254 — wall decorations.
- `class25.method284(...)` at lines 592, 617, 771, 1141, 1150, 1199 — 1×1 locs.
- `class25.method290(...)` at lines 676, 746 — wall-decoration interpolation (door animation).
- `class25.method305(-10,(byte)3,64,-50,768,-50)` at [MapRegion.java:311](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L311) — bake lighting.
- `SceneGraph.method277(...)` at lines 371, 416, 459 — register occluder rectangles (axis 1, 2 and 4 respectively).
- `class25.method300(...)` at line 806, 1234 — query existing wall-hash before replacing.

### From `GameClientCore.java`
- Lifecycle:
  - `new SceneGraph(...)` at [:6354](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L6354).
  - `method274(619)` at [:477](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L477), [:1971](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1971).
  - `SceneGraph.method273(-501)` at [:4757](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L4757).
- Visibility flags: `SceneGraph.aBoolean436 = ...` at [:2488, :9036](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2488).
- Hovered tile pickup: `SceneGraph.anInt470/471` at [:3055..3060](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L3055).
- Hovered tile request: `method312(false, mouseY, mouseX)` at [:3656, :3658](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L3656).
- Plane min: `method275(plane, -34686)` at [:619, :621](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L619).
- Minimap: `method309(...)` at [:713, :715](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L713).
- Ground items: `method281(...)` at [:818](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L818); `method295(...)` at [:788](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L788).
- Actors: `method285(...)` at [:841, :2102, :2684](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L841); `method286(...)` at [:2092](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2092).
- Queries: `method296..304` at [:743, :2244, :2249, :2342, :2345, :2382, :3307, :4449, :8841, :8857, :8863, :8871, :9277..9286](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L743).
- Removals: `method291..295` at [:9292..9310, :788](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L9292).
- Per-frame: `method313(...)` at [:10190](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L10190) followed by `method288((byte)104)` at [:10191](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L10191).

### From `BootstrapGraphicsSetup.java`
- `SceneGraph.method310(500, 800, 512, 334, visibilityMap, softwareVisibility)` at [BootstrapGraphicsSetup.java:79](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L79) — bake the pitch/yaw visibility mask at startup.

### Into `Rasterizer3D.java`
- `Rasterizer3D.anInt1466/1467` — screen-centre in pixels, set when the rasterizer is bound to a `Rasterizer2D` target ([Rasterizer3D.java:47-48, :59-60](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Rasterizer3D.java#L47)); read by `method315/316` when computing `(x<<9)/z + anInt1466`.
- `Rasterizer3D.aBoolean1462` — "screen-edge clip" flag set by `method315/316` whenever any of the three projected corners is outside the framebuffer; consulted by `method374/378` ([Rasterizer3D.java:769, :847, :1228, :1837](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Rasterizer3D.java#L769)).
- `Rasterizer3D.anInt1465` — alpha mod; explicitly reset to `0` by `method315/316` before each tile (opaque terrain).
- `Rasterizer3D.method374(yA,yB,yC,xA,xB,xC,hslA,hslB,hslC)` — gouraud triangle.
- `Rasterizer3D.method378(yA,yB,yC,xA,xB,xC,hslA,hslB,hslC, vx0,vx1,vx2, vy0,vy1,vy2, vz0,vz1,vz2, textureId)` — textured gouraud triangle, called with the three "texture-space" world-vertices to interpolate UVs.

## Methods whose purpose could not be confidently inferred

None — every method has either an unambiguous caller-site mapping (MapRegion / GameClientCore) or is a single-purpose private helper with self-evident semantics. The lookup tables `anIntArray478..484` are described from their use-sites but the exact derivation (which of the 9 (camera-quadrant) cells corresponds to which numeric entry) would require running the algorithm or comparing with an OSRS reference deob — the role within `method314` is nonetheless certain.
