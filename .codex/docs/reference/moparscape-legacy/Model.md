# Model (legacy moparscape)

## Overview
- **Path**: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java`
- **Line count**: 2139
- **Extends**: `Renderable` (which provides `aClass33Array1425` and `anInt1426` for vertex normals / "min Y" extents)
- **Purpose**: Combined 3D model storage and software rasterizer driver. Model
  owns vertex arrays (xyz), face (triangle) arrays with their colors/types/
  priorities/alphas/skins, texture-anchor triangles (defining UV basis for
  textured faces), bone-skin groupings, optional per-vertex normals, and a
  bounding volume. It also exposes:
  - cache loaders (`method459`–`method463`, `method460`) that parse the
    cache byte format into a `ModelHeader`,
  - constructors that materialise a `Model` from a header, copy a model,
    or merge several base models (used for player kit & object stacking),
  - in-place transforms (translate `method475`, scale `method478`,
    rotate-90 `method473`, mirror `method477`, recolour `method476`,
    skeletal-frame application `method470`/`method471`/`method472`,
    pose-pivot bake `method469`),
  - lighting / vertex-normal computation (`method479` and its companion
    `method480`, helper `method481`),
  - the render entry point `method443` (project verts + submit faces) and
    the alternate `method482` used for inventory/preview rendering, plus
    the rasterizer feeder `method483`/`method484`/`method485`.

- **Key encoding concepts**
  - **Face render-type word** (`anIntArray1637`, "type" byte from the cache):
    `bits 0..1` choose draw mode (0 = flat HSL, 1 = wire-frame using
    `Rasterizer3D.anIntArray1482`, 2 = textured Gouraud-shaded, 3 = textured
    flat-shaded with face HSL re-used). `bits 2..` hold the texture-anchor
    triangle index when bit 1 is set (see merge constructors adjusting the
    high bits with `<< 2`). A value of `-1` skips the face entirely
    (see `method483`).
  - **`anIntArray1640` "face colour" overload**: for untextured faces this
    is the HSL palette index; for textured faces (`type & 1 == 0` and
    bit 1 set) the same array carries the **texture id** passed to
    `Rasterizer3D.method378`.
  - **Skeletal anim model**: vertex bone group ids live in
    `anIntArray1655` (per-vertex) and `anIntArray1656` (per-face alpha
    group). `method469` bakes those linear arrays into the bucketed
    `anIntArrayArray1657` / `anIntArrayArray1658` so frame transforms
    can iterate vertices by transform group efficiently. Per-frame
    transforms read from `AnimationFrame`/`AnimationSkeleton` and
    delegate to `method472`.
  - **Texture-anchor triangles** (`anIntArray1643/1644/1645`): three
    vertex indices defining a (P, M, N) basis. When a face declares a
    textured render type, `Rasterizer3D.method378` reads the (camera-space
    x/y/z of those three vertices, cached in `anIntArray1668/1669/1670`)
    to compute the texture-projection plane.

## Static fields

| Legacy name | Inferred name | Type | Purpose / notes |
| --- | --- | --- | --- |
| `anInt1619` | `dummyStaticCounter` | `int` | Pure obfuscation noise. Touched conditionally in `method461`, `method466`, `method477`. Never read. |
| `anInt1620` | `instanceCount` | `int` | Incremented in every public ctor — instance counter (debug/leak telemetry). |
| `aClass30_Sub2_Sub4_Sub6_1621` | `EMPTY_MODEL` | `Model` | Sentinel empty model (built with private `Model(true)` ctor). Used by `Player.method?` and `NpcDefinition` as the base "into-which-parts-merge" target before `method464` copies a real merged model in. |
| `anIntArray1622/1623/1624` | `scratchVertX/Y/Z` | `int[2000]` | Shared scratch xyz buffers reused by `method464` to avoid allocations. Grown +100 when too small. |
| `anIntArray1625` | `scratchFaceAlpha` | `int[2000]` | Shared scratch alpha buffer reused by `method464`. |
| `aClass21Array1661` | `MODEL_HEADERS` | `ModelHeader[]` | Per-id header cache built by `method459`, populated by `method460`, cleared by `method461`/`method458`. |
| `aClass42_1662` | `MODEL_PROVIDER` | `ResourceProvider` | Backing async cache (`.method548(id)` issues a fetch when a header is missing). |
| `aBooleanArray1663` | `faceClipNeeded` | `boolean[4096]` | Per-face flag set in `method483` when projected verts fall outside the raster window; passed to `Rasterizer3D.aBoolean1462` to enable line-clipped span fill. |
| `aBooleanArray1664` | `faceNearPlaneClipped` | `boolean[4096]` | Per-face flag: a vertex is behind/near the near plane; `method484` defers to the heavy clipper `method485`. |
| `anIntArray1665` | `screenX` | `int[4096]` | Per-vertex projected screen-X (in 16.16 over depth). |
| `anIntArray1666` | `screenY` | `int[4096]` | Per-vertex projected screen-Y. |
| `anIntArray1667` | `depthMinusModelDepth` | `int[4096]` | Per-vertex `cameraZ - midZ` used as Z-bucket key for the painter. |
| `anIntArray1668/1669/1670` | `cameraX/Y/Z` | `int[4096]` | Per-vertex post-transform camera-space coords. Cached for texture mapping and near-plane clipping. |
| `anIntArray1671` | `facesPerDepthBucket` | `int[1500]` | Counts of faces in each depth bucket (the painter). |
| `anIntArrayArray1672` | `facesByDepthBucket` | `int[1500][512]` | Buckets of face indices keyed by mean depth. |
| `anIntArray1673` | `facesPerPriorityBucket` | `int[12]` | Priority-bucket face counts. |
| `anIntArrayArray1674` | `facesByPriorityBucket` | `int[12][2000]` | Faces grouped by `anIntArray1638` priority (0-11). |
| `anIntArray1675/1676` | `priority10Depths` / `priority11Depths` | `int[2000]` | Per-entry depth for priority-10/11 lists used by the multi-pass painter. |
| `anIntArray1677` | `prioritySumDepths` | `int[12]` | Sum of depths per priority bucket, used to compute the means used to interleave priority-10/11 faces. |
| `anIntArray1678/1679/1680` | `clippedScreenX/Y/Color` | `int[10]` | Output of the near-plane clipper `method485`. |
| `anInt1681/1682/1683` | `pivotX/Y/Z` | `int` | Pivot point computed at the start of every skeletal transform (`method472` cmd 0); reused for "transform around origin" cmds 2 and 3. |
| `aBoolean1684` | `pickingEnabled` | `boolean` | Static flag enabling triangle-pick-test in `method443`. |
| `anInt1685/1686` | `pickMouseX` / `pickMouseY` | `int` | Mouse coordinates picked against. |
| `anInt1687` | `pickResultCount` | `int` | Number of writes to `anIntArray1688`. |
| `anIntArray1688` | `pickResults` | `int[1000]` | Output IDs (the `i2` "object tag" arg of `method443`) that the model under cursor reported. |
| `anIntArray1689/1690` | `SIN_TABLE` / `COS_TABLE` | `int[]` | Aliases of `Rasterizer3D.anIntArray1470/1471` for 16.16 sin/cos. |
| `anIntArray1691` | `WIRE_COLORS` | `int[]` | Alias of `Rasterizer3D.anIntArray1482`, palette used for type-1 wireframe fill. |
| `anIntArray1692` | `INV_DEPTH_TABLE` | `int[]` | Alias of `Rasterizer3D.anIntArray1469`, reciprocal table used by `method485` for near-plane interpolation `(50 - z) / dz`. |

## Instance fields

Grouped:

### Inherited from `Renderable`
- `super.anInt1426` — model min-Y extent (negative of lowest Y).
- `super.aClass33Array1425` — per-vertex `VertexNormal` accumulators
  produced during lighting (`method479`).

### Misc / obfuscation
- `anInt1614 = 9` — `animationType` (selector passed to
  `AnimationFrame.method531(anInt1614, ...)`). Always 9 in this build.
- `aBoolean1615`, `anInt1616 = 360`, `anInt1617 = 1`, `aBoolean1618`,
  `anInt1654`, `aBoolean1659` — dead/obfuscation fields. Only ever
  read/written defensively to confuse decompilers; `aBoolean1659` does
  gate the picking-output style in `method443` but is never set true.

### Geometry — vertices
- `anInt1626` — vertex count.
- `anIntArray1627/1628/1629` — vertex X/Y/Z.
- `anIntArray1655` — vertex bone-skin id (raw from cache; consumed by
  `method469` into `anIntArrayArray1657`).

### Geometry — faces (triangles)
- `anInt1630` — face count.
- `anIntArray1631/1632/1633` — face vertex indices A/B/C.
- `anIntArray1634/1635/1636` — final per-vertex face HSL after baking
  (output of `method479`/`method480`). Only allocated when the face has a
  baked colour (i.e. after lighting).
- `anIntArray1637` — face **render-type word** (see encoding above).
- `anIntArray1638` — face render priority (0..11); when `null` falls back
  to `anInt1641`.
- `anIntArray1639` — face alpha (0 = opaque, 255 = fully transparent).
- `anIntArray1640` — face base colour or texture id (overloaded).
- `anInt1641` — model-wide render priority (used when per-face priority
  is absent). `-1` means "no model-wide priority".
- `anIntArray1656` — per-face skin/alpha-group id (cache-raw, consumed
  by `method469` into `anIntArrayArray1658`).

### Geometry — texture anchors
- `anInt1642` — texture-anchor triangle count.
- `anIntArray1643/1644/1645` — anchor triangle vertex indices P/M/N.

### Bounding volume
- `anInt1646/1647` — min/max model-space X.
- `anInt1649/1648` — min/max model-space Z.
- `anInt1651` — max Y (paired with inherited `anInt1426` = max -Y).
- `anInt1650` — XZ-plane bounding radius (`sqrt(maxX² + maxZ²)`).
- `anInt1652` — depth-bucket count for the painter
  (`anInt1653 + sqrt(radius² + maxY²)`).
- `anInt1653` — bound: `sqrt(radius² + minHeight²)`.

### Skeletal/groups
- `anIntArrayArray1657` — vertex-id buckets keyed by transform group
  (built by `method469`).
- `anIntArrayArray1658` — face-id buckets keyed by alpha group (built by
  `method469`).

### Normals (Phong-style lighting intermediate)
- `aClass33Array1660` — saved snapshot of vertex normals
  (`VertexNormal[]`), kept by `method479` when called with `flag=false`
  (the unflagged case for objects that re-light each frame).

## Methods

### `method458(int i)` — proposed name: `disposeStatics`
- **Signature**: `public static void method458(int i)`
- **Purpose**: Clears every static array/table the class owns. Looks like a
  `static` cleanup invoked when the client is being torn down. The body
  uses a degenerate `for(... ; i >= 0;) return;` (obfuscation guard) so
  that for non-default `i` execution returns early, leaving the rest of
  the nulls unexecuted — i.e. a "real" call must pass `i < 0`.
- **Parameters**: `i` — obfuscation/guard flag.
- **Returns**: nothing.
- **Called by**: no callers in the listed files (likely from a shutdown
  path elsewhere; unverified).
- **Calls**: nothing.

### `method459(int count, ResourceProvider provider)` — `initHeaderCache`
- Allocates `MODEL_HEADERS` (`aClass21Array1661`) to size `count` and
  stores the cache fetcher in `MODEL_PROVIDER` (`aClass42_1662`).
- **Called by**: client bootstrap (not in the listed files).
- **Calls**: none.

### `method460(byte[] data, int magic, int id)` — `decodeHeaderBytes`
- **Signature**: `public static void method460(byte abyte0[], int i, int j)`
- **Purpose**: Parses the trailing 18-byte footer of a cache-model blob to
  populate a `ModelHeader` entry (`MODEL_HEADERS[j]`). Reads the section
  sizes (verts/faces/anchors), the optional-section flags, and the bone-
  data length, then walks them to compute the section offsets stored in
  `anInt372..anInt384`.
- **Parameters**: `data` — raw model bytes (footer is the last 18B);
  `magic` — must equal `-4036` (obfuscation guard), else the method
  aborts after reading three fields, leaving partial state; `id` — slot
  in `MODEL_HEADERS`.
- **Returns**: nothing.
- **Called by**: `ResourceProvider` finish-load callback (unverified).
- **Calls**: `PacketBuffer.method408/method410`.
- **Notes**: When `data == null`, writes a tombstone header (counts 0).

### `method461(int guard, int id)` — `releaseHeader`
- Frees `MODEL_HEADERS[id]`. The `guard` argument only toggles `anInt1619`
  (dead). Used to evict a header once the model has been hydrated.
- **Called by**: cache eviction path (unverified).

### `method462(int magic, int id)` — `loadModel`
- **Signature**: `public static Model method462(int i, int j)`
- **Purpose**: Look up `MODEL_HEADERS[j]`; if missing, ask the
  `ResourceProvider` to fetch it asynchronously and return `null`. If
  present, construct a `Model(id, -870)` (the bytes→model constructor)
  and return it.
- **Parameters**: `magic` — obfuscation guard (must be 9); `id` — model
  cache id.
- **Returns**: `Model` or `null` if not yet loaded.
- **Called by**:
  - `ObjectDefinition.java:217`, `ObjectDefinition.java:254`
  - `NpcDefinition.java:50`, `NpcDefinition.java:143`
- **Calls**: `aClass42_1662.method548`, `Model(int, int)` private ctor.

### `method463(int id)` — `isModelReady`
- Returns `true` if `MODEL_HEADERS[id]` is hydrated; otherwise schedules
  a fetch via `MODEL_PROVIDER.method548(id)` and returns `false`.
- **Called by**:
  - `ObjectDefinition.java:121, 127, 163`
  - `NpcDefinition.java:43, 136`

### `Model(boolean flag)` — `Model(sentinelMarker)` (private)
- **Purpose**: Builds the static `EMPTY_MODEL` sentinel. The `flag` arg
  only toggles `aBoolean1618`. No geometry allocated.
- **Called by**: static initialiser of `aClass30_Sub2_Sub4_Sub6_1621`.

### `Model(int id, int magic)` — `decodeFromBytes` (private)
- **Signature**: `private Model(int i, int j)`
- **Purpose**: Hydrate a Model from `MODEL_HEADERS[id]`'s raw bytes.
  Allocates the vertex/face/anchor arrays per the header's optional-
  section bitmask, then opens five concurrent `PacketBuffer` views into
  the same cache blob — one per section offset (`anInt372..anInt384`) —
  and reads:
  1. **Vertex stream**: flag byte selects which of dX/dY/dZ are delta-
     encoded (`method421` is the var-int reader). Each axis is cumulative.
     The fifth view reads `anIntArray1655` (skin/bone id) per vertex.
  2. **Face attribute streams**: render type (`anIntArray1637`), priority
     (`anIntArray1638`), alpha (`anIntArray1639`), skin id
     (`anIntArray1656`) and base colour (`anIntArray1640`).
  3. **Face vertex indices** with a 4-mode delta scheme keyed by a
     "type" byte `i4 ∈ {1,2,3,4}` (1 = full reset, 2 = re-use B/old, 3 =
     re-use C/old, 4 = swap A/B; all incrementally produce A, B, C).
  4. **Texture anchor triangles** (`anIntArray1643/1644/1645`).
- **Parameters**: `id`, `magic` — `magic` is a dead obfuscation arg
  triggering an infinite loop when ≥ 0 (never called with that value).
- **Returns**: constructed model.
- **Called by**: `method462`.
- **Calls**: `PacketBuffer.method408/method410/method421`.
- **Notes**: This is the cache decoder for OSRS 317-era "old-format"
  models — the canonical reference for what the bytes mean.

### `Model(int count, Model[] parts, int magic)` — `mergeKitModels(deduped)`
- **Signature**: `public Model(int i, Model aclass30_sub2_sub4_sub6[], int j)`
- **Purpose**: Build a single model out of `count` part-models by
  re-indexing duplicate vertices via `method465` (i.e. shared vertices
  are coalesced). Inherits per-face attributes (type, priority, alpha,
  skin) from whichever parts have them. Adjusts texture-anchor indices
  per part using `(anInt1637 & 2) == 2 ? += textureOffset << 2`.
- **Called by**: `NpcDefinition.java:56,148`,
  `Player.java:271, 346` (player kit merge).

### `Model(int count, int magic, boolean flag, Model[] parts)` — `mergeAnimatedKit`
- **Signature**: `public Model(int i, int j, boolean flag, Model[] parts)`
- **Purpose**: Like the merge constructor above but does **no** vertex
  dedupe (faster, used when the model is going to be re-baked anyway).
  Concatenates vertices, then face attributes incl. baked colour
  (`anIntArray1634/1635/1636`). Finally calls `method466(false)` to
  recompute bounds.
- **Parameters**: `count`, `magic` (dead), `flag` (dead), `parts`.
- **Called by**: `ObjectDefinition.java:229`, `Player.java:44, 71`.
- **Calls**: `method466`.

### `Model(int magic, boolean shareVerts, boolean shareAlpha, boolean shareVertsAgain, Model src)` — `copyForRecolour`
- **Signature**: `public Model(int i, boolean flag, boolean flag1, boolean flag2, Model class30_sub2_sub4_sub6)`
- **Purpose**: Shallow/deep copy of `src` for a recolour-or-retex variant.
  The three boolean flags control whether each of (vertices, face-alpha,
  texture-coord-y) are shared by reference vs duplicated. All other
  arrays are shared.
- **Called by**: `ObjectDefinition.java:138`.

### `Model(boolean copyY, int magic, boolean copyLighting, Model src)` — `copyForReanimation`
- **Signature**: `public Model(boolean flag, int i, boolean flag1, Model class30_sub2_sub4_sub6)`
- **Purpose**: Copy ctor used when re-applying animation/lighting to a
  shared base model. `copyY` duplicates `anIntArray1628` (Y verts).
  `copyLighting` duplicates the per-face baked colours and vertex normals
  (`aClass33Array1425/1660`) so the source's lighting isn't overwritten.
- **Called by**: `Player.java:32`, `ObjectDefinition.java:272`.

### `method464(int magic, Model src, boolean shareAlpha)` — `populateForFrame`
- **Signature**: `public void method464(int i, Model class30_sub2_sub4_sub6, boolean flag)`
- **Purpose**: Re-initialise `this` (the `EMPTY_MODEL` sentinel) from
  `src` using the static scratch buffers (`anIntArray1622..1625`) for
  vertex storage so that per-frame skeletal animation costs no
  allocation. `shareAlpha=true` reuses src's alpha array; otherwise it
  fills the scratch alpha buffer.
- **Called by**: `Player.java:288`, `NpcDefinition.java:160`.

### `method465(Model src, int srcVertIdx)` — `internOrAppendVertex` (private)
- Returns the index in `this` matching the (x,y,z) of `src.vertex[i]`,
  appending a new vertex if no match. Used by the dedup merge ctor.
- **Called by**: merge ctor (`Model(int, Model[], int)`).

### `method466(boolean dead)` — `computeBounds`
- Recomputes `anInt1426` (min-Y), `anInt1651` (max-Y), `anInt1650`
  (XZ radius), `anInt1653`, `anInt1652` (depth bucket count) from
  current verts. `dead` arg toggles `anInt1619` only.
- **Called by**: merge ctor, `Player.java:294`,
  `NpcDefinition.java:168`, end of `method479`.

### `method467(boolean dead)` — `recomputeYBounds`
- Lighter variant: re-derives min/max Y only (assumes XZ radius
  unchanged). Used by `ObjectDefinition.java:152` after slight Y
  squash/stretch in object generation.

### `method468(int magic)` — `computeFullExtents`
- Like `method466` but also fills the explicit min/max X/Z values
  (`anInt1646/1647/1648/1649`). Initial bounds are sentinel ints
  (e.g. `0xf423f` = +1_000_000). `magic` (21073) gates the final two
  assignments (obfuscation early-return).
- **Called by**: `method479` (when called without bake-flag).

### `method469(byte magic)` — `bakeBoneGroups`
- **Signature**: `public void method469(byte byte0)`
- **Purpose**: Converts the per-vertex bone-id array `anIntArray1655`
  into bucketed `anIntArrayArray1657[group] = vertexIds[]`, and the
  per-face alpha-group `anIntArray1656` into `anIntArrayArray1658`.
  Releases the raw linear arrays afterward.
- **Parameters**: `byte0` — guard (must equal `-71`).
- **Called by**: `Player.java:34, 280`, `NpcDefinition.java:155`,
  `ObjectDefinition.java:275`.

### `method470(int frameId, int magic)` — `applyFrame`
- Apply a single `AnimationFrame` to the model: for each transform
  command in the frame, call `method472` with the matching skeleton
  transform type and target groups.
- **Parameters**: `frameId` (frame cache id), `magic` (40542 guard).
- **Called by**: `Player.java:35, 293`, `NpcDefinition.java:165`,
  `ObjectDefinition.java:276`.

### `method471(int magic, int[] mask, int prevFrameId, int frameId)` — `applyFrameWithMask`
- Apply `frameId` while interleaving a "mask" frame (`prevFrameId`),
  used for upper/lower-body split animations. The `mask` array is a
  sorted list of skeleton-node ids the secondary frame may override.
- **Called by**: `Player.java:290`, `NpcDefinition.java:162`.

### `method472(int op, int[] groups, int dx, int dy, int dz)` (private) — `applyTransformCommand`
- **Purpose**: Skeletal transform dispatcher. `op` selects:
  - `0`: compute and store the pivot point as the centroid of the
    affected vertices (plus the `dx/dy/dz` offset); stored in
    `anInt1681/1682/1683`.
  - `1`: translate the listed vertex groups by `(dx,dy,dz)`.
  - `2`: rotate vertices around the pivot — Y rotation first
    (`dz*8`), then X (`dx*8`), then Z (`dy*8`); uses
    `SIN_TABLE/COS_TABLE` (`anIntArray1689/1690`).
  - `3`: scale around the pivot by `(dx,dy,dz)/128`.
  - `5`: bump face alpha (`anIntArray1639`) by `dx*8` and clamp to
    `[0,255]` for the listed face groups.
- **Called by**: `method470`, `method471`.

### `method473(int magic)` — `rotate90Y` (clockwise)
- Rotates all verts 90° around Y axis: `(x,z) → (z, -x)`. Magic = 360.
- **Called by**: `Player.java:57..84` (multiple calls), `ObjectDefinition.java:281`.

### `method474(int angle, int magic)` — `rotateAroundX`
- Rotate all verts around X by `angle` (index into the 16.16 sin/cos
  tables). `magic` is dead. Not seen called in the inspected files but
  available for use.

### `method475(int dx, int dy, int magic, int dz)` — `translate`
- Adds `(dx, dy, dz)` to every vertex. `magic` must be 16384 (guard).
- **Called by**: `Player.java:33, 54, 86`, `ObjectDefinition.java:291`.

### `method476(int srcColor, int dstColor)` — `recolour`
- Replace every occurrence of `srcColor` in `anIntArray1640` with
  `dstColor`. Used to apply NPC/player colour kits to a base model.
- **Called by**: `Player.java:275, 277, 350, 352`,
  `NpcDefinition.java:60, 152`, `ObjectDefinition.java:285`.

### `method477(int magic)` — `mirrorZ`
- Mirrors the model on the Z axis (`z → -z`) and flips winding on every
  face (swap A/C). Used so doorways/objects can present a "right-handed"
  variant. `magic` must be 0.
- **Called by**: `ObjectDefinition.java:221, 258`.

### `method478(int sx, int sz, int magic, int sy)` — `scale`
- Non-uniform scale of all vertices: `x*=sx/128`, `y*=sy/128`,
  `z*=sz/128`. `magic` must be 9.
- **Called by**: `Player.java:39`, `NpcDefinition.java:167`,
  `ObjectDefinition.java:289`.

### `method479(int ambient, int contrast, int lx, int ly, int lz, boolean bake)` — `applyLighting`
- **Signature**: `public final void method479(int i, int j, int k, int l, int i1, boolean flag)`
- **Purpose**: Per-face Gouraud lighting bake. For every triangle:
  1. Compute the face normal `n = (B-A) × (C-A)` (auto-rescaled by
     right-shifts until each component fits in `[-8192, 8192]`),
     normalise to length 256.
  2. If the face's render-type bit 0 is unset (the smooth-shading case),
     accumulate `n` into each of the three `VertexNormal` slots so a
     vertex used by several smooth faces ends up with the mean normal.
  3. If the face is flat-shaded (bit 0 set), bake `anIntArray1634[i]`
     immediately from `dot(n, L)` plus the ambient.
  Then either: (a) `bake=true` → call `method480` to bake the smooth
  vertex colours and discard normals/skin data, then `method466`; or
  (b) `bake=false` → preserve normals in both `aClass33Array1425` and a
  copy in `aClass33Array1660` (so the model can be re-lit per frame),
  then `method468`.
- **Parameters**: `ambient`, `contrast` (light scale ÷ direction
  magnitude), `lx/ly/lz` (light direction), `bake` (whether to discard
  intermediates).
- **Called by**: `Player.java:40, 281`, `NpcDefinition.java:156`,
  `ObjectDefinition.java:292`.
- **Calls**: `method481`, `method480`, `method466`, `method468`.

### `method480(int ambient, int contrast, int lx, int ly, int lz)` — `bakeVertexLighting`
- Finishes lighting started by `method479`: for each face, computes
  per-vertex colours via `method481(faceCol, dotL, type)` using the
  averaged `VertexNormal`s, then releases all intermediate arrays
  (`aClass33Array1425`, `aClass33Array1660`, `anIntArray1655/1656`).
  Keeps `anIntArray1640` only if any face has the texture-id bit set.
- **Called by**: `method479` (when `bake=true`), and externally from
  `SceneGraph.java:693, 695, 703, 711` — meaning scene tiles call
  `method480` directly when a region is lit lazily.

### `method481(int faceCol, int dot, int type)` (static) — `combineHsl`
- HSL combine used by `method480`. If `(type & 2) == 2` the face is a
  textured-flat type — return `127 - clamp(dot, 0, 127)` (a brightness
  byte). Otherwise modulate the lower 7 bits (lightness) of `faceCol`
  by `dot/128`, clamped to `[2, 126]`, and OR the upper 9 bits (HS)
  back in.

### `method482(int rotX, int rotY, int rotZ, int unused, int tx, int ty, int tz)` — `renderInventory`
- **Signature**: `public final void method482(int i, int j, int k, int l, int i1, int j1, int k1)`
- **Purpose**: Camera-independent projection-and-submit pipeline, used
  for inventory/preview rendering. Applies three rotations around X/Y/Z
  (in that order) plus a translate, then projects into screen space and
  pre-multiplies the per-vertex (x,y) by 512 / z; finally calls
  `method483(false, false, 0)`. No frustum culling or picking — just
  rasterise.
- **Called by**: external preview/UI code (not in the listed files).

### `method443(int yaw, int sinPitch, int cosPitch, int sinYawCam, int cosYawCam, int tx, int ty, int tz, int pickTag)` — `renderInWorld`
- **Signature**: `public final void method443(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2)`
- **Purpose**: The world rendering entry point.
  1. Compute the bounding sphere's camera-space position
     (using `cosPitch/sinPitch`, `cosYawCam/sinYawCam` plus translate
     `tx/ty/tz`). Cull against near/far and the 2D frustum derived from
     `Rasterizer2D.anInt1386/1387`.
  2. Check whether the bounding rect contains the mouse cursor
     (`anInt1685/1686`) — if so, mark for picking (`flag1`) so the
     per-triangle hit test can run inside `method483`.
  3. Per vertex: rotate by `yaw`, translate by `(tx,ty,tz)`, rotate by
     yaw-cam, then by pitch-cam, projecting `(x,y)/z` into screen space
     with `Rasterizer3D.anInt1466/1467` as the centre. Vertices behind
     the near plane (`z < 50`) get `screenX = -5000` and set `flag` to
     enable per-face near-plane clip later.
  4. Hand off to `method483(flag, flag1, pickTag)`.
- **Parameters**: `yaw` — model's local yaw (index into sin/cos);
  `sinPitch`/`cosPitch` — camera pitch (16.16); `sinYawCam`/`cosYawCam`
  — camera yaw; `tx`/`ty`/`tz` — model centre in world-relative camera
  space; `pickTag` — opaque ID stored in `pickResults` if the mouse
  hits any face of this model.
- **Called by**: ~17 sites in `SceneGraph.java` (lines 1291, 1296,
  1365, 1367, 1371, 1393, 1399, 1406, 1411, 1413, 1415, 1462, 1550,
  1605, 1607, 1609, 1616, 1638, 1644, 1651, 1653).
- **Calls**: `method483`.

### `method483(boolean nearClip, boolean pickHit, int pickTag)` (private) — `submitFaces`
- **Purpose**: The painter. Bins each face into one of `anInt1652`
  depth buckets by mean Z (back-to-front). If `anIntArray1638` is set,
  each face is also bucketed by priority (0..11), with the
  priority-10/11 buckets interleaved into 10 by mean depth. Backface
  culling is done by 2D screen cross-product. For each visible face:
  picking-test via `method486`, then `method484` to actually rasterise.
- **Parameters**: `nearClip` — at least one vertex was clipped; force
  `method485` slow-path; `pickHit` — bounding-rect hit; `pickTag` — id
  to store if the cursor truly hits a triangle.
- **Called by**: `method443`, `method482`.
- **Calls**: `method484`, `method485` (via `method484`), `method486`.

### `method484(int faceIdx)` (private) — `rasteriseFace`
- Dispatch one face to the right `Rasterizer3D` overload based on the
  low two bits of `anIntArray1637[i]`: 0 → smooth-shaded HSL
  (`method374`), 1 → wireframe (`method376`), 2 → textured Gouraud
  (`method378`), 3 → textured but with face HSL re-used as flat shade.
  When `aBooleanArray1664[i]` is set, delegates to `method485` for
  near-plane clipping.
- **Called by**: `method483`.

### `method485(int faceIdx)` (private) — `rasteriseFaceClipped`
- **Purpose**: Slow path for near-plane-clipped triangles. Walks each
  edge of the triangle and computes new clipped vertex positions where
  `z < 50`, producing 3 or 4 output vertices (a quad if both edges of
  an interior vert were clipped). Then issues 1–2 `Rasterizer3D`
  draw calls per render-type (0/1/2/3) on the clipped polygon.
- **Called by**: `method484`.

### `method486(int mouseX, int mouseY, int sx0, int sx1, int sx2, int sy0, int sy1, int sy2)` (private) — `pointInTriangleAABB`
- AABB-based bounding-box test for the mouse cursor against a single
  triangle (the function is missing the actual edge-inside check; the
  cheap rectangle test is enough because `method443` already verified
  the mouse is over the model's projected sphere). Returns true if the
  cursor's x is between min/max screen Y and y is ≤ at least one
  screen X — used as a quick "this triangle could contain the cursor"
  hit pre-filter.
- **Called by**: `method483`.
- **Notes**: Purpose unclear in detail — based on its arguments and
  call site, it acts as a cheap mouse-pick reject test rather than a
  precise inside-triangle predicate.

## Cross-class call graph

### Inbound (external callers)

- **`SceneGraph.java`** — the world renderer
  - Reads static tables `Model.anIntArray1689/1690` (sin/cos) at lines
    937–940, 1053–1058.
  - `method443` — invoked from ~20 sites (1291, 1296, 1365, 1367, 1371,
    1393, 1399, 1406, 1411, 1413, 1415, 1462, 1550, 1605, 1607, 1609,
    1616, 1638, 1644, 1651, 1653) to draw object, wall and ground-item
    models per frame.
  - `method480` — invoked at lines 693, 695, 703, 711 to lazily bake
    smooth-vertex lighting on scene tiles after `method479` has
    accumulated normals.

- **`ObjectDefinition.java`** — object-cache hydration
  - `method463` (readiness check) at lines 121, 127, 163.
  - `method462` (load) at lines 217, 254.
  - Constructor `Model(int, Model[], int)` at line 229
    (mergeAnimatedKit) and `Model(int, boolean, boolean, boolean, Model)`
    at line 138 (copyForRecolour); `Model(9, ..., Model)` at line 272
    (copyForReanimation).
  - `method467` (line 152), `method477` (lines 221, 258),
    `method469` (275), `method470` (276), `method473` (281),
    `method476` (285), `method478` (289), `method475` (291),
    `method479` (292).

- **`NpcDefinition.java`** — NPC model assembly
  - `method463`/`method462` for loading parts (43, 50, 136, 143).
  - Constructor merge `Model(int, Model[], int)` at lines 56, 148.
  - `method476` (60, 152), `method469` (155), `method479` (156).
  - The "sentinel + populate-for-frame" pattern: grabs
    `Model.aClass30_Sub2_Sub4_Sub6_1621` (159), calls `method464`
    (160), then `method471`/`method470` (162, 165), `method478` (167),
    `method466` (168).

- **`Player.java`** — local-player model assembly + animation
  - Constructs head/body part-merges and recolour copies (32, 44, 71,
    271, 346) using the various ctors.
  - Per-frame pipeline: `method475`/`method469`/`method470`/`method478`/
    `method479` (lines 33–40), kit copy/dedup/mirror (`method473`,
    54–86), recolour (`method476` at 275, 277, 350, 352), final lighting
    (`method469` 280, `method479` 281), then sentinel populate
    `aClass30_Sub2_Sub4_Sub6_1621 + method464` (287–288), masked frame
    `method471` (290) or `method470` (293), and finally `method466`
    (294).

- **`Actor.java`** — does **not** call into `Model` directly (only the
  `Renderable` superclass API).

### Outbound (Model calls into other classes)
- `PacketBuffer.method408/method410/method421` — cache byte reader
  (used in `method460` and the bytes constructor).
- `AnimationFrame.method531` — fetches a frame; `AnimationSkeleton.anIntArray342/anIntArrayArray343/...` — skeleton transform tables.
- `Rasterizer2D.anInt1385/1386/1387` — screen extents for culling and
  off-screen detection.
- `Rasterizer3D.anInt1466/1467` (screen centre),
  `Rasterizer3D.anInt1465` (alpha), `Rasterizer3D.aBoolean1462`
  (clip flag), `Rasterizer3D.method374/376/378` (raster entry points).
- `VertexNormal` — accumulator used during lighting.
- `ResourceProvider.method548` — issues async fetch for missing model.
- `ModelHeader` — per-id metadata POJO.
