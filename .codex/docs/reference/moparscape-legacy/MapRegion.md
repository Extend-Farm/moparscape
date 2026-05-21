# MapRegion (legacy moparscape)

## Overview

- **Path:** `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java`
- **Line count:** 1346 lines (1345 source lines + trailing newline)
- **Class:** `final class MapRegion` (package-private), reconstructed from `MapRegion.class` in `moparclient.jar` via CFR 0.152 (header comment, `MapRegion.java:7`).

### Purpose

`MapRegion` is the legacy 317-era terrain assembly + per-tile lighting pipeline. It owns the parsed map state for a 104x104 (`anInt146` x `anInt147`) scene across 4 build planes: per-tile heights, underlay floor IDs, overlay floor IDs, overlay shape/rotation, tile flag bits, and per-corner shading scratch. Its main responsibilities are:

1. **Parse cache map blocks** (`method181`) — decode the per-tile opcode stream from `m{X}_{Y}` map files into height/underlay/overlay/flag arrays, generating random heights via Perlin-like noise (`method170`, `method172`, `method176`, `method184`, `method186`) when no explicit height is provided.
2. **Parse cache loc/object blocks** (`method183`, `method190`, `method179`, `method175`, `method188`, `method189`) — decode object placement streams and push wall / wall-decoration / ground-decoration / interactive-object instances into a `SceneGraph`.
3. **Assemble lit terrain into the scene graph** (`method171`) — compute per-corner slope normals against a fixed directional light, blend underlay HSL across an 11x11 sliding window (the famous "underlay blur" producing soft floor-colour transitions), pick overlay textures/colours, write `SceneTilePaint` / `SceneTileModel` via `SceneGraph.method279`, and emit large flat occluders via `SceneGraph.method277`.

### Key concepts

- **HSL16 packing** (`method177` at `MapRegion.java:844`): `(hue/4 << 10) | (sat/32 << 7) | (lum/2)`. Hue is 8-bit input -> 6-bit field; saturation 8-bit -> 3-bit field; luminance 8-bit -> 7-bit field. High-saturation entries are desaturated as luminance climbs (`>179`, `>192`, `>217`, `>243` each halve saturation), mimicking how the renderer fades to white. Identical implementation to `FloorDefinition.method263` (`FloorDefinition.java:155`).
- **Brightness modulation** (`method185`/`method187`): take a packed HSL16 colour and replace its 7-bit luminance field with `lightness * (hsl & 0x7F) / 128` clamped to `[2,126]`. `method187` (`MapRegion.java:1089`) is the static variant for runtime underlay shading; `method185` (`MapRegion.java:1061`) adds two sentinel returns — `-2` -> `12345678` ("hidden tile" sentinel for the `0xFF00FF` overlay magic colour), `-1` -> a *pure-lightness* mono ramp (`127 - clamp(lightness, 0, 127)`). The `12345678` sentinel is recognised downstream by `SceneTilePaint` to skip drawing.
- **11x11 sliding-window underlay blend** (inside `method171`): each tile averages the surrounding 11x11 neighbourhood (5 in each direction inclusive) of underlay HSL contributions weighted by `FloorDefinition.anInt395..398`. The blend uses a two-pass running-sum trick: outer Y loop maintains 5 per-column running sums (`anIntArray124..128`), inner X loop maintains row totals (`n7,n6,n33,n34,n3`).
- **Per-corner slope-light grid** (`anIntArrayArray139`): `(anInt146+1) x (anInt147+1)` ints. Filled from height gradients dotted with a fixed light vector `(-50, -10, -50)` plus a per-tile detail subtract from `aByteArrayArrayArray134` (the per-corner extra-shadow byte set by ground-decoration code in `method175`). Used at each corner of every painted tile so adjacent tiles share lighting at shared corners.
- **Bridge plane semantics**: bit `2` of `aByteArrayArrayArray149[1][x][y]` means "this tile is a bridge — render plane 1 as plane 0 for the player on top". See `method171:98-103`, `method175:560-562`, `method182:1014-1016`, `method183:1044`. Anywhere code drops a level (`--n18`, `--n16`, `--n20`) is honouring this flag.
- **Overlay branch sentinels** (`method171:280-290`): `FloorDefinition.anInt391 >= 0` -> the overlay is a *textured* tile (texture ID submitted to `SceneGraph.method279` as `n55`, light multiplier comes from `Rasterizer3D.method369(textureId, 12660)`). `FloorDefinition.anInt390 == 0xFF00FF` -> magic "invisible" overlay (no underlay blend, no texture, `n53 = -2` => `12345678` sentinel passed to corners). Otherwise the overlay is a flat HSL colour built from `anInt394/395/396` with brightness from `anInt399`.
- **Occlusion bits** (`anIntArrayArrayArray135`): per-corner bit flags marking surfaces that should later become large flat occluders. Bits used: floor occluder `0x924` (set by `method171:264` when all four corners share height) plus per-wall `0x249`/`0x492` flags set in `method175` per wall orientation. Final pass of `method171` (`MapRegion.java:323-479`) merges contiguous runs of these flags and emits an `Occluder` via `SceneGraph.method277` for each rectangle of >=4 or >=8 tiles.

## Static fields

| Legacy name | Proposed name | Type | Purpose | Read/written |
|---|---|---|---|---|
| `anInt123` | `underlayHueJitter` | `int` (`MapRegion.java:9`) | Per-region jitter applied to averaged underlay hue. Seeded with `random*17 - 8`, drifts by `random*5 - 2` per `method171` call, clamped to `[-8, 8]`. | written `method171:114-119`, read `method171:245`. |
| `anInt131` | `currentRenderPlane` | `static int` (`:17`) | The "logical" plane the player currently occupies — used by `method182` / `method171:220` / `method175:543` as the visibility cutoff when `aBoolean151` (bridge-aware culling) is true. | Set externally by `GameClientCore` (e.g. `GameClientCore.java:2587, 2649`); read here. |
| `anInt133` | `underlayLightnessJitter` | `int` (`:19`) | Same scheme as `anInt123` but for lightness; range `[-16, 16]`. | `method171:120-125, 246`; init `static{}:1330`. |
| `anIntArray137` | `wallDecorationXOffsetByDir` | `int[4]` (`:23`, init `static{}:1331-1334`) | `{1, 0, -1, 0}` — per-rotation X offset for inset wall decorations. | `method175:811`, `method188:1239`. |
| `anInt138` | `unusedSentinel138` | `int` (`:24`) | Looks like a dead/debug sentinel — written with constant magic values (`329`, `323`) in `method171:112` and the static initializer; passed verbatim to `CollisionMap.method212`. Purpose unclear — based on the pattern of `anInt150/anInt153` it is one of the obfuscator's dummy parameters. | Many. |
| `anIntArrayArray139` | `cornerLightGrid` | `int[anInt146+1][anInt147+1]` (`:25`) | Per-corner light intensity used when emitting tile paints. Rebuilt at the top of each `method171` plane pass. | written `method171:148`; read `method171:234-237, 272, 291`. |
| `anIntArray140` | `wallDiagonalBitByDir` | `int[]{16,32,64,128}` (`:26`, init `static{}:1336`) | Wall mask bits for diagonal (`type 1/3`) walls keyed by rotation. | `method175:682, 752`, `method188:1166, 1191`. |
| `aBoolean141` | `unusedFlag141` | `boolean` (`:27`) | Toggled in `method173` when `by != -107` — opaque obfuscator parameter check. Purpose unclear — appears unused elsewhere. | `method173:496`. |
| `anIntArray144` | `wallDecorationYOffsetByDir` | `int[]{0,-1,0,1}` (`:30`, init `static{}:1337-1340`) | Companion of `anIntArray137` — Y offset per rotation. | `method175:811`, `method188:1239`. |
| `anInt145` | `lowestRenderedPlane` | `static int` (`:31`) | Minimum plane index that ended up with painted tiles, used by `SceneGraph.method275` (`GameClientCore.java:619`) to skip empty top planes. Reset to `99` each call; lowered to `n19` whenever a tile is emitted (`method171:221-222`, `method175:548`). | Read by client. |
| `aBoolean151` | `bridgeAwareCulling` | `static boolean` (`:37`) | Master switch for "hide tiles above the player's plane unless bridge" logic. Default `true` (static init); set false during cutscenes (`GameClientCore.java:2491`) and back to true (`:9043`). | Read `method171:220`, `method175:539, 565`, `method189:1287`. |
| `anIntArray152` | `wallStraightBitByDir` | `int[]{1,2,4,8}` (`:38`, init `static{}:1343`) | Bit mask for straight/corner walls (`type 0/2/4/5`) keyed by rotation. | `method175:630, 710, 801, 811`, `method188:1158, 1183, 1229, 1239`. |
| `anInt153` | `unusedSentinel153` | `static int` (`:39`) | Dummy obfuscator counter written in dead branches (`method171:46`, `method174:533`, `method188:1108`, init `static{}:1344`). Purpose unclear — never read. | n/a. |

## Instance fields

Heights and colour layers are indexed as `[plane][tileX 0..103][tileY 0..103]`. Constructor at `MapRegion.java:41-62` shows shapes.

| Legacy name | Proposed name | Type/shape | Purpose | Read/written |
|---|---|---|---|---|
| `anInt146` | `tileWidth` | `int` (`:32`) — set from ctor `n3`, used as 104 | X tile count. | constructor `:43`, used throughout. |
| `anInt147` | `tileLength` | `int` (`:33`) — set from ctor `n2`, used as 104 | Z/Y tile count. | constructor `:44`. |
| `anInt150` | `occluderOrientationSentinel` | `int = -53` (`:36`) | Magic value forwarded to `SceneGraph.method277` as the seventh arg (purpose unknown — `Occluder` reconstruction would reveal). Purpose unclear. | `method171:371, 416, 459`. |
| `aBoolean132` | `unusedFlag132` | `boolean = true` (`:18`) | Purpose unclear — declared but no readers in this file. | None observed. |
| `aBoolean143` | `unusedFlag143` | `boolean = false` (`:29`) | Toggled by `method180:953` and `method183:1025` based on opaque obfuscator parameter checks. Purpose unclear. | n/a real. |
| `anIntArrayArrayArray129` | `tileHeight` | `int[4][anInt146+1][anInt147+1]` (`:15`) — provided by caller, shared with `GameClientCore.anIntArrayArrayArray1214` | Per-corner heights in render-units (negative values, multiples of 8). Sampled at corners hence `+1` dimension. | written by `method174`, `method181`; read everywhere. |
| `aByteArrayArrayArray149` | `tileFlags` / `renderRules` | `byte[4][anInt146][anInt147]` (`:35`) — provided by caller | Bit-packed render rules per tile: bit `1` = "blocked", bit `2` = "bridge" (only used on plane 1), bit `8` = "force hidden", bit `0x10` = "force show". See `method171:96-103, 220`, `method175:539-544`, `method182:1007-1018`, `method183:1044`. | written `method181:961, 991`; read across. |
| `aByteArrayArrayArray142` | `underlayFloorId` | `byte[4][anInt146][anInt147]` (`:28`) | 1-based index into `FloorDefinition.aClass22Array388` for the underlay floor; 0 = none. | written `method181:994`; read `method171:168, 181, 224`. |
| `aByteArrayArrayArray130` | `overlayFloorId` | `byte[4][anInt146][anInt147]` (`:16`) | 1-based index into `FloorDefinition.aClass22Array388` for the overlay; 0 = none. | written `method181:985`; read `method171:225, 271, 278`. |
| `aByteArrayArrayArray136` | `overlayShape` | `byte[4][anInt146][anInt147]` (`:22`) | Tile shape index 0..12 (overlay shape, e.g. corner/diagonal cuts); stored as `(opcode - 2) / 4`. | written `method181:986`; read `method171:255, 276`. |
| `aByteArrayArrayArray148` | `overlayRotation` | `byte[4][anInt146][anInt147]` (`:34`) | Overlay rotation 0..3, stored as `(opcode - 2 + n5) & 3`. | written `method181:987`; read `method171:277`. |
| `anIntArrayArrayArray135` | `occluderFlags` | `int[4][anInt146+1][anInt147+1]` (`:21`) | Per-corner bit flags marking surfaces that should become large flat occluders. | written `method171:264`, `method175:621, 639, 649, 659, 669, 715, 718, 722, 725, 729, 732, 736, 739`; consumed `method171:323-479`. |
| `aByteArrayArrayArray134` | `cornerExtraShadow` | `byte[4][anInt146+1][anInt147+1]` (`:20`) | Additional shadow attenuation per corner (added by trees / walls — see `method175:601-602`). Sampled in the slope-light blur in `method171:147`. | `method174:514`, `method175`, `method171`. |
| `anIntArray124..128` | `underlayWindowCol{Hue,Sat,Lum,Weight,Count}` | `int[anInt147]` each (`:10-14`) | Per-column running totals used by the 11x11 underlay blend in `method171`. | `method171`. |

## Methods

In source order. Most `method17x/18x/19x` carry vestigial obfuscator "guard" parameters (`int n` checked against a magic number, `byte by` checked, etc.) — these are noted as guards.

### `method170(int x, int z)` — proposed name: `noiseHash`

- **Signature:** `private static final int method170(int n, int n2)` (`MapRegion.java:64`).
- **Purpose:** Classic Bracewell-Buse integer noise hash; returns an 8-bit pseudo-random value seeded by `(x, z)`. Used as the base lattice for Perlin-style smoothing.
- **Parameters:** `n` = lattice X; `n2` = lattice Z. Combined via `n + n2*57`.
- **Returns:** Pseudo-random `0..255`.
- **Called by:** `method186`.
- **Calls:** none.

### `method171(CollisionMap[] colMaps, SceneGraph scene, int guard)` — proposed name: `buildTerrainScene`

- **Signature:** `public final void method171(CollisionMap[] class11Array, SceneGraph class25, int n)` (`:71`).
- **Purpose:** The full per-region terrain build. Applies hard collision blockers from tile flags, advances jitter counters, then for each of the 4 planes: computes the slope-light grid, performs the 11x11 sliding-window underlay HSL blend, emits a `SceneTilePaint`/`SceneTileModel` per visible tile, marks bridge tiles, and finally coalesces occluder flags into large flat occluders.
- **Parameters:** `class11Array` — collision maps per plane; `class25` — destination scene graph; `n` — guard parameter (must not be `2`, else writes `anInt138 = 329`, see `:111`). Tile coords inside the method are `0..103` in both axes.
- **Returns:** void.
- **Called by:** `GameClientCore.java:610` (`class7.method171(aClass11Array1230, aClass25_946, 2)`).
- **Calls:** `CollisionMap.method213`, `MapRegion.method177`, `MapRegion.method185`, `MapRegion.method187`, `MapRegion.method182`, `Rasterizer3D.anIntArray1482`, `Rasterizer3D.method369`, `SceneGraph.method279`, `SceneGraph.method278`, `SceneGraph.method305`, `SceneGraph.method276`, `SceneGraph.method277`, `FloorDefinition.aClass22Array388`.

#### Block-by-block walk of `method171`

**Block A — apply blocked-tile collision (`:90-110`).** Quadruple loop over plane `n20 = 0..3`, tiles `n19 = 0..103`, `n21 = 0..103`. If `aByteArrayArrayArray149[plane][x][y] & 1 != 0` the tile is "blocked"; the call goes into `collisionMaps[plane - bridgeShift].method213(x, 0, y)` to mark a fully blocked tile. The bridge shift (`--n18`) is only applied when `aByteArrayArrayArray149[1][x][y] & 2 != 0` — i.e. tiles above a bridge "borrow" the collision of the layer below.

**Block B — dead guard (`:111-113`).** `if (n < 2 || n > 2) anInt138 = 329;` — opaque check on the guard arg, only matters because the caller passes `2`. Not load-bearing.

**Block C — random colour jitter drift (`:114-125`).** Wobble `anInt123` (hue) and `anInt133` (lightness) by `random*5 - 2`, clamp.

**Block D — per-plane loop (`n19 = 0..3`, `:126-310`).** For each plane:

- **D.1 corner light grid (`:127-152`).** Cache `aByteArrayArrayArray134[plane]` into `byArray`. Light constants: ambient `n18=96`, light strength `n17=768`, direction `(n16, n15, n14) = (-50, -10, -50)`. Compute `n12 = n17 * |light| >> 8`. For every interior corner `(n10, n11)` with `1 <= * < dim-1`:
  - Tangent vectors from neighbour heights: `n9 = dh/dx`, `n8 = dh/dz`.
  - Normalised normal `(n6, n5, n4)` is `(n9, 256, n8) / |.|` (`Math.sqrt(n9*n9 + 65536 + n8*n8)` -> the `65536` is a constant `256^2` for the Y component).
  - `n3 = n18 + (light dot normal) / n12` -> diffuse value in `0..192`-ish.
  - `n2 = weighted sum of neighbouring `cornerExtraShadow` bytes (centre `>>1`, axial neighbours `>>2`/`>>3`) -> the per-corner extra-shadow contribution from objects.
  - Write `cornerLightGrid[n10][n11] = n3 - n2`.

- **D.2 reset column running-sum arrays (`:153-161`).** Zero `anIntArray124..128[0..tileLength-1]`.

- **D.3 outer Y loop `n9 = -5..tileWidth+4` (`:162-299`).** This is the underlay sliding-window blend. For each row `n9`, the algorithm:

  - **Column update (`:164-195`).** For each `n8 = 0..tileLength-1`, add `(n9+5)` row's underlay contributions and subtract `(n9-5)` row's contributions from the per-column accumulators. The contribution is `FloorDefinition.aClass22Array388[underlayId-1].anInt395..398` plus a `+1` count — pre-weighted HSL components and a tile-count weight; see `FloorDefinition.method262` for how those fields are derived from the RGB underlay colour.
  - **Inner X loop (`:196-297`).** Only runs when `n9 >= 1 && n9 < tileWidth-1`. Inside: for each `n2 = -5..tileLength+4` advance row totals `n7,n6,n33,n34,n3` (add column at `n2+5`, subtract column at `n2-5`). Then if `n2 >= 1 && n2 < tileLength-1`, process tile `(n9, n2)`:
    - **Visibility gate (`:220`).** Skip tile if bridge-aware culling enabled AND not a bridge tile AND (bit `0x10` is set OR `method182(...)` != `anInt131`). I.e. only render tiles on the current plane (with bridge promotion) unless a force-show flag overrides.
    - **Lowest-plane tracking (`:221-223`).** `if (n19 < anInt145) anInt145 = n19;`.
    - **Skip if no underlay AND no overlay (`:226`).**
    - **Corner heights** `n42..n45` and **corner lights** `n46..n49`.
    - **Underlay colour (`:240-252`).** If there is an underlay, compute the blended HSL: `n41 = n7*256/n34` (hue weighted by `anInt398`-derived total), `n40 = n6/n3`, `n39 = n33/n3`. `n50 = method177(...)` is the unjittered packed HSL (base for *underlay* per-corner shading via `method187`). Apply hue/lightness jitter, clamp, and pack again into `n51` — the *light-source* base used for the texture light pre-multiplier.
    - **Floor occluder flag (`:253-266`).** Only on planes >0: if the four corner heights are equal AND no overlay shape AND the underlay is opaque (no transparent floor flag, see `FloorDefinition.aBoolean393`) AND this isn't a "no overlay shape" floor either, OR (the overlay exists), set `occluderFlags[plane][x][y] |= 0x924` so later the floor becomes an occluder.
    - **Light multiplier (`:267-270`).** If we have an underlay colour, `n41 = Rasterizer3D.anIntArray1482[method187(n51, 96)]` — pre-table-lookup brightness factor for texture multiply.
    - **Pure underlay tile (`:271-272`).** `SceneGraph.method279(plane, x, y, shape=0, rotation=0, textureId=-1, h0..h3, underlay corner colours, 0, 0, 0, 0, lightMul, 0)`. Shape `0` = `SceneTilePaint` (see `SceneGraph.java:163-171`).
    - **Mixed underlay/overlay tile (`:274-292`).** Compute `n40 = shape+1`, `n39 = rotation`. Resolve overlay colour with three branches:
      - `class22.anInt391 >= 0` (textured) -> texture id = `n55`, base brightness = `Rasterizer3D.method369(textureId, 12660)`, `n53 = -1` so `method185` returns the mono "shaded white" ramp at each corner.
      - `class22.anInt390 == 0xFF00FF` (invisible) -> `n55 = -1`, `n54 = 0`, `n53 = -2` so `method185` returns sentinel `12345678` -> tile drawn as transparent / cut-out.
      - Otherwise (flat HSL) -> `n53 = method177(class22.anInt394, anInt395, anInt396)`, `n54 = Rasterizer3D.anIntArray1482[method185(class22.anInt399, 96)]`.
      
      Then submit via `SceneGraph.method279(plane, x, y, shape, rotation, texId, h0..h3, underlay corners, overlay corners, lightMul, overlayLightMul)`. The corners are `method185/method187(n50/n53, n46..n49)` — i.e. modulate the colour's 7-bit luminance by the per-corner light values.

- **D.4 tile-mode resolution pass (`:300-308`).** For each interior tile (`n7,n8` 1..dim-2) call `SceneGraph.method278(plane, x, y, method182(...))` — stamps the "logical plane" the tile actually belongs to on the tile.

**Block E — emit a global occluder for ambient light (`:311`).** `class25.method305(-10, (byte)3, 64, -50, 768, -50)` — passes the same light direction used above plus ambient/strength constants. Sets up scene-graph lighting state.

**Block F — bridge tile marker pass (`:312-322`).** For each `(n56, n18)` where `aByteArrayArrayArray149[1][x][y] & 2 == 2`, call `SceneGraph.method276(y, x, -438)`. `-438` is a guard arg; the call notifies the scene that this tile is a bridge.

**Block G — occluder coalescing (`:323-479`).** Walks bits `n18=1`, `n17=2`, `n16=4` (later shifted left by 3 each iteration of `n15`). For each of 4 planes (`n15`), iterates corners and tries to extend runs:

- **Bit `n18`** (Z-aligned wall occluder, `:339-383`): grow X interval `n11..n10` along the column then plane-shrink `n9..n8` upward; if rectangle area >=8, emit `SceneGraph.method277(plane, x*128, h, x*128, (y+1)*128, h-240, anInt150, y*128, 1)` and clear the flag.
- **Bit `n17`** (X-aligned wall occluder, `:384-429`): mirror with X grow.
- **Bit `n16`** (floor occluder, `:430-471`): grow both X and Y, require area >=4, emit with face id `4`.

The `>>3` shift each outer iteration switches the bit triple to `(0x8, 0x10, 0x20)`, `(0x40, 0x80, 0x100)`, `(0x200, 0x400, 0x800)` — i.e. per-plane occluder bits packed inside the same 32-bit corner flag. Each `0x924`/`0x249`/`0x492` constant is the three bits set across all four iterations for the corresponding orientation.

### `method172(int x, int z)` — proposed name: `randomTileHeightOffset`

- **Signature:** `private static final int method172(int n, int n2)` (`:482`).
- **Purpose:** Generates the per-tile pseudo-random height delta used when the map block leaves a height implicit (`method181:966`). Three octaves of `method176` summed and biased into `[10, 60]`.
- **Returns:** int height in render-units (10..60).
- **Called by:** `method181:966`.
- **Calls:** `method176`.

### `method173(byte guard, PacketBuffer stream, OnDemandFetcher demand)` — proposed name: `preloadObjectModelsFromStream`

- **Signature:** `public static final void method173(byte by, PacketBuffer class30_Sub2_Sub2, OnDemandFetcher class42_Sub1)` (`:492`).
- **Purpose:** Reads a "scene chunk" packet from `stream` (sequence of delta-encoded object IDs, each followed by their per-tile placements which are *discarded* here) and asks the on-demand fetcher to preload each `ObjectDefinition`'s models. This is the model-prefetch path during region loading; the actual placement runs in `method183/method190/method175`.
- **Parameters:** `by` is a guard parameter (expected `-107`). `class30_Sub2_Sub2` = packet buffer over the loc file. `class42_Sub1` = the on-demand fetcher.
- **Returns:** void.
- **Called by:** `GameClientCore.java:2811`.
- **Calls:** `PacketBuffer.method422`, `PacketBuffer.method408`, `ObjectDefinition.method572`, `ObjectDefinition.method574`.

### `method174(int z, int dz, int guard, int dx, int x)` — proposed name: `clearTerrainPatch`

- **Signature:** `public final void method174(int n, int n2, int n3, int n4, int n5)` (`:508`).
- **Purpose:** Marks an `(n2+1) x (n4+1)` rectangle on plane 0 as "fully extra-shadowed" (`aByteArrayArrayArray134[0][x][z] = 127`) and propagates edge heights inward (left/right/top/bottom edges of the patch copy heights from neighbour outside the patch). Used to flatten the local edges of a freshly-loaded sub-region into the surrounding terrain so seams disappear.
- **Parameters:** `n` = start Z (tile), `n2` = Z extent, `n3` = guard (non-zero path writes `anInt153`), `n4` = X extent, `n5` = start X (tile).
- **Returns:** void.
- **Called by:** `GameClientCore.java:513` (whole-map patch), `:573` (per-region 8x8 patch).
- **Calls:** none.

### `method175(int y, SceneGraph scene, CollisionMap col, int type, int plane, int x, int objId, boolean skip, int rot)` — proposed name: `placeObject`

- **Signature:** `private final void method175(int n, SceneGraph class25, CollisionMap class11, int n2, int n3, int n4, int n5, boolean bl, int n6)` (`:537`).
- **Purpose:** Pushes a single object instance into the scene graph based on its loc `type`:
  - `0` — straight wall (`SceneGraph.method282`, mask `anIntArray152[rot]`).
  - `1` — diagonal wall (`anIntArray140[rot]`).
  - `2` — corner wall (two halves, masks `anIntArray152[rot]` + `anIntArray152[(rot+1)&3]`).
  - `3` — diagonal corner wall.
  - `4..8` — wall decorations (`SceneGraph.method283` with various inset offsets).
  - `9` — diagonal interactive object (`SceneGraph.method284`).
  - `10/11` — interactive object footprint (handles rotated footprints, computes per-tile shadow box, calls `CollisionMap.method212`).
  - `12..21` — roof / variant interactive (single-tile `method284`, plus occluder bits on planes >0).
  - `22` — ground decoration (`SceneGraph.method280`).
- **Parameters:** `n` tile Y (0..103), `class25` scene graph, `class11` collision map for the *effective* plane (already bridge-adjusted by callers), `n2` object type 0..22, `n3` plane 0..3, `n4` tile X, `n5` object id, `bl` early-return flag (caller's `false` means "place"; `true` means "skip placement"), `n6` rotation 0..3.
- **Returns:** void.
- **Called by:** `method183:1051`, `method190:1324`.
- **Calls:** Lots — see source. Notably `ObjectDefinition.method572`, `ObjectDefinition.method578`, `new DynamicObject(...)`, `SceneGraph.method282/283/284/280/290`, `CollisionMap.method211/212/213`.
- **Notes:** Mirrors `method188`. The only differences are (a) `method175` requires `aBoolean779` for the shadow box on type 10/11; (b) `method175` writes `aByteArrayArrayArray134` and `anIntArrayArrayArray135` for ground-shadows/occluders; (c) `method188` works on user-supplied height array `nArray` while `method175` reads `this.anIntArrayArrayArray129`. `method188` is the path used by `GameClientCore.java:9321` for instanced-map placement.

### `method176(int x, int z, int scale)` — proposed name: `perlinOctave`

- **Signature:** `private static final int method176(int n, int n2, int n3)` (`:830`).
- **Purpose:** One octave of bicubic-interpolated noise at lattice scale `n3` (must be power of 2). Returns smoothed `0..255`.
- **Parameters:** `n,n2` world tile coords, `n3` lattice step.
- **Called by:** `method172`.
- **Calls:** `method184`, `method186`.

### `method177(int hue, int sat, int lum)` — proposed name: `packHsl16`

- **Signature:** `private final int method177(int n, int n2, int n3)` (`:844`).
- **Purpose:** Pack 8-bit HSL into the renderer's 16-bit HSL format, applying the high-lightness desaturation curve. Identical to `FloorDefinition.method263`.
- **Returns:** `(hue/4 << 10) | (sat/32 << 7) | (lum/2)`.
- **Called by:** `method171:244, 251, 288`.

### `method178(int objId, int type, int guard)` — proposed name: `isObjectMeshReady`

- **Signature:** `public static final boolean method178(int n, int n2, int n3)` (`:861`).
- **Purpose:** Wrapper around `ObjectDefinition.method577` that normalises `type 11 -> 10` and `5..8 -> 4`, so the on-demand check is per "loc category", not per rotation.
- **Parameters:** `n` object id, `n2` loc type, `n3` guard (must be `8`).
- **Returns:** `true` iff all of the locs' models are ready.
- **Called by:** `GameClientCore.java:7615, 7624`.

### `method179(int regionPlane, int rotation, CollisionMap[] colMaps, int guard, int dstX, int srcSubX, byte[] payload, int loadedPlane, int srcSubY, int dstY)` — proposed name: `loadInstancedTerrainBlock`

- **Signature:** `public final void method179(int n, int n2, CollisionMap[] class11Array, int n3, int n4, int n5, byte[] byArray, int n6, int n7, int n8)` (`:878`).
- **Purpose:** Decodes a 64x64 terrain chunk from `byArray` into the local scene at offset `(n4, n8)`, but only writes one specific 8x8 sub-chunk (when the loop indices match `n5..n5+8, n6..n6+8` AND `n12 == n`), applying rotation `n2`. Used to assemble the "instanced map" view: the legacy client passes 4 plane-buffers and remaps them with `TileRotationUtils.method155/156`.
- **Parameters:** All tile-space. `n3` is a guard (must be `9`). `n` is the source plane to copy from.
- **Returns:** void.
- **Called by:** `GameClientCore.java:556`.
- **Calls:** `method181`, `TileRotationUtils.method155/156`.

### `method180(byte[] payload, int dstX, int dstZ, int guard, int rotation, byte guard2, CollisionMap[] colMaps)` — proposed name: `loadFullTerrainBlock`

- **Signature:** `public final void method180(byte[] byArray, int n, int n2, int n3, int n4, byte by, CollisionMap[] class11Array)` (`:919`).
- **Purpose:** Decodes a full 4x64x64 terrain map ("m{X}_{Y}" file) into the scene at offset `(n, n2)`. Unlike `method179` no rotation/sub-chunk filter — copy everything verbatim. First pass also clears bit `0x01000000` (custom collision marker) in `CollisionMap.anIntArrayArray294`.
- **Parameters:** `by` guard (`4`).
- **Called by:** `GameClientCore.java:504`.
- **Calls:** `method181`.

### `method181(int x, int rotation, PacketBuffer stream, int z, int plane, int rotShift, int guard, int seedSalt)` — proposed name: `decodeTileOpcodes`

- **Signature:** `private final void method181(int n, int n2, PacketBuffer class30_Sub2_Sub2, int n3, int n4, int n5, int n6, int n7)` (`:957`).
- **Purpose:** Consumes the variable-length opcode stream for one tile. Opcodes (`PacketBuffer.method408()` = unsigned byte):
  - `0` — terminator. If on plane 0, set height to `-method172(z + 932731 + seed, x + 556238 + rot) * 8`; otherwise inherit from plane below minus 240 (one storey).
  - `1` — explicit height: read byte, treat `1` as `0`, then `height = -value*8` (or chained from below).
  - `2..49` — overlay: read colour byte into `overlayFloorId`, shape `(op-2)/4` into `overlayShape`, rotation `(op-2+rotShift)&3` into `overlayRotation`. **Continues looping** for more opcodes (overlays don't terminate the tile).
  - `50..81` — render-rules byte: `tileFlags[plane][z][x] = op - 49`.
  - `>=82` — underlay floor id `op - 81`. **Continues looping**.
  
  When `(x, z)` is outside `[0,104)`, drains the same stream length without writing (the `while` after the if).
- **Parameters:** `n, n3` dest tile X/Z; `n2` rotation guard for chained recursion (`method179` uses `n4`); `n4` plane; `n5` rotation adjustment for overlay; `n6` guard (must give `36/n6 == 1` -> `n6 = 36`); `n7` extra seed for height noise.
- **Called by:** `method179`, `method180`.

### `method182(int y, int plane, int x, int guard)` — proposed name: `effectivePlaneAt`

- **Signature:** `public int method182(int n, int n2, int n3, int n4)` (`:1007`).
- **Purpose:** Returns the *logical* plane of a tile honouring bridge/hidden semantics. `guard != 0` short-circuits to `2`. Otherwise: bit `0x08` (force-hidden) returns 0; if plane>0 AND bit `0x02` of plane-1 tile is set (bridge), return `plane - 1`; else return `plane`.
- **Called by:** `method171:220, 304`, `method175:543`.

### `method183(CollisionMap[] colMaps, SceneGraph scene, int regionPlane, int dstX, int srcSubX, boolean skipFlag, int loadedPlane, byte[] payload, int srcSubY, int rotation, int dstY)` — proposed name: `loadInstancedObjects`

- **Signature:** `public final void method183(CollisionMap[] class11Array, SceneGraph class25, int n, int n2, int n3, boolean bl, int n4, byte[] byArray, int n5, int n6, int n7)` (`:1020`).
- **Purpose:** Decodes a locs file (`l{X}_{Y}`) and places its objects, but filters to the requested 8x8 sub-chunk + plane and applies rotation `n6`. Each loc record has a delta-encoded object id and a delta-encoded packed coord `(plane<<12 | y<<6 | x)`. Coordinates inside the chunk are rotated via `TileRotationUtils.method157/158` before placement.
- **Called by:** `GameClientCore.java:597`.
- **Calls:** `PacketBuffer.method422/408`, `TileRotationUtils.method157/158`, `ObjectDefinition.method572`, `method175`.

### `method184(int a, int b, int t, int scale)` — proposed name: `cosineInterpolate`

- **Signature:** `private static final int method184(int n, int n2, int n3, int n4)` (`:1056`).
- **Purpose:** Cosine interpolation between `a` and `b` at parameter `t/scale` using the precomputed cosine LUT `Rasterizer3D.anIntArray1471`. Used by `method176` for smooth Perlin octaves.

### `method185(int colour, int lightness)` — proposed name: `applyLightnessToColourWithSentinels`

- **Signature:** `private final int method185(int n, int n2)` (`:1061`).
- **Purpose:** Compute the per-corner shaded HSL16 for an overlay colour. Special cases:
  - `n == -2` -> `12345678` (the "invisible tile" sentinel propagated to the scene graph).
  - `n == -1` -> mono pure-lightness ramp `127 - clamp(lightness, 0, 127)`, used when overlay is a *texture* (so corners shade by lightness alone).
  - Otherwise modulate the colour's 7-bit luminance by `lightness * (n & 0x7F) / 128`, clamped to `[2,126]`, repacked into the 16-bit HSL.
- **Called by:** `method171:289, 291`.

### `method186(int x, int z)` — proposed name: `smoothNoiseLattice`

- **Signature:** `private static final int method186(int n, int n2)` (`:1082`).
- **Purpose:** Filter the raw noise of `method170` over a 3x3 neighbourhood with weights `1/16` (diagonals), `1/8` (axials), `1/4` (centre) — i.e. a smoothing kernel applied at lattice points before bicubic interpolation.
- **Called by:** `method176`.

### `method187(int colour, int lightness)` — proposed name: `applyLightnessToHsl`

- **Signature:** `private static final int method187(int n, int n2)` (`:1089`).
- **Purpose:** Static variant of `method185` for underlay colours. Same `-1 -> 12345678` sentinel (underlay invisible) and same luminance modulation.
- **Called by:** `method171:269, 272, 291`.

### `method188(SceneGraph scene, int rotation, int x, int type, int plane, CollisionMap col, int[][][] heights, int objId, int y, int regionPlane, byte guard)` — proposed name: `placeObjectWithHeights`

- **Signature:** `public static final void method188(SceneGraph class25, int n, int n2, int n3, int n4, CollisionMap class11, int[][][] nArray, int n5, int n6, int n7, byte by)` (`:1101`).
- **Purpose:** Static placement variant of `method175` used for *instanced* maps where the caller supplies its own height array. Same big type switch (0..22). Differs from `method175` in that it never writes back to `aByteArrayArrayArray134` / `anIntArrayArrayArray135` (no shadow box, no occluder flag) because those scratch arrays are per-instance.
- **Parameters:** `by` is a guard (must be `93`).
- **Called by:** `GameClientCore.java:9321`.

### `method189(int dstX, byte[] payload, int dstY, int guard)` — proposed name: `areLocsReadyForChunk`

- **Signature:** `public static final boolean method189(int n, byte[] byArray, int n2, int n3)` (`:1258`).
- **Purpose:** Walk a locs stream and check that every `ObjectDefinition` referenced has its models loaded (`ObjectDefinition.method579`). At most one definition per stream record is checked (the `bl2` flag short-circuits the inner per-coord loop after the first valid placement). Returns `false` if any required model is still missing.
- **Parameters:** `n3` guard (must be `6`, else throws NPE).
- **Called by:** `GameClientCore.java:2637`.

### `method190(int dstX, CollisionMap[] colMaps, int dstY, int guard, SceneGraph scene, byte[] payload)` — proposed name: `loadFullLocs`

- **Signature:** `public final void method190(int n, CollisionMap[] class11Array, int n2, int n3, SceneGraph class25, byte[] byArray)` (`:1295`).
- **Purpose:** Decode a full locs file and place every object (no rotation/sub-chunk filter, unlike `method183`). Applies bridge-plane shift for collision selection.
- **Parameters:** `n3` guard (must be `7`).
- **Called by:** `GameClientCore.java:531`.
- **Calls:** `method175`.

### `static {}` — initializer

- **At:** `:1329-1344`.
- Sets `anInt133`, `anIntArray137 = {1,0,-1,0}`, `anInt138 = 323`, `anIntArray140 = {16,32,64,128}`, `anIntArray144 = {0,-1,0,1}`, `anInt145 = 99`, `aBoolean151 = true`, `anIntArray152 = {1,2,4,8}`, `anInt153 = -388`.

## Cross-class call graph

```
GameClientCore.<region load loop>
 ├─ new MapRegion(...)                           MapRegion.java:41 (called from GameClientCore.java:493)
 ├─ MapRegion.method180  (full terrain chunk)    MapRegion.java:919 (caller :504)
 │    └─ MapRegion.method181                     MapRegion.java:957
 │         ├─ MapRegion.method172                MapRegion.java:482
 │         │    └─ MapRegion.method176           MapRegion.java:830
 │         │         ├─ MapRegion.method184      MapRegion.java:1056
 │         │         │    └─ Rasterizer3D.anIntArray1471
 │         │         └─ MapRegion.method186      MapRegion.java:1082
 │         │              └─ MapRegion.method170 MapRegion.java:64
 │         └─ PacketBuffer.method408/409
 ├─ MapRegion.method174  (edge clear)            MapRegion.java:508 (callers :513, :573)
 ├─ MapRegion.method190  (full locs)             MapRegion.java:1295 (caller :531)
 │    └─ MapRegion.method175                     MapRegion.java:537
 │         ├─ ObjectDefinition.method572/method578
 │         ├─ new DynamicObject
 │         ├─ SceneGraph.method280 (ground deco)
 │         ├─ SceneGraph.method282 (wall)
 │         ├─ SceneGraph.method283 (wall deco)
 │         ├─ SceneGraph.method284 (interactive)
 │         ├─ SceneGraph.method290 (ambient sound?)
 │         └─ CollisionMap.method211/212/213
 ├─ MapRegion.method179  (instanced terrain)     MapRegion.java:878  (caller :556)
 │    └─ MapRegion.method181, TileRotationUtils.method155/156
 ├─ MapRegion.method183  (instanced locs)        MapRegion.java:1020 (caller :597)
 │    └─ MapRegion.method175, TileRotationUtils.method157/158
 ├─ MapRegion.method171  (assemble lit scene)    MapRegion.java:71   (caller :610)
 │    ├─ CollisionMap.method213
 │    ├─ MapRegion.method182                     MapRegion.java:1007
 │    ├─ MapRegion.method177                     MapRegion.java:844
 │    ├─ MapRegion.method185                     MapRegion.java:1061
 │    ├─ MapRegion.method187                     MapRegion.java:1089
 │    ├─ FloorDefinition.aClass22Array388[]      FloorDefinition.java:180
 │    ├─ Rasterizer3D.anIntArray1482, method369
 │    ├─ SceneGraph.method279 (tile paint/model) SceneGraph.java:159
 │    ├─ SceneGraph.method278 (tile plane)       SceneGraph.java:146
 │    ├─ SceneGraph.method305 (ambient light)    SceneGraph.java:670
 │    ├─ SceneGraph.method276 (bridge marker)    SceneGraph.java:97
 │    └─ SceneGraph.method277 (occluder)         SceneGraph.java:126
 └─ MapRegion.anInt145 (lowest rendered plane consumed at GameClientCore.java:613-619)

GameClientCore.<other call sites>
 ├─ MapRegion.method173 (preload locs models)    MapRegion.java:492 (caller :2811)
 ├─ MapRegion.method178 (is loc loaded?)         MapRegion.java:861 (callers :7615, :7624)
 ├─ MapRegion.method188 (place w/ heights)       MapRegion.java:1101 (caller :9321)
 ├─ MapRegion.method189 (locs ready check)       MapRegion.java:1258 (caller :2637)
 ├─ writes MapRegion.aBoolean151                  GameClientCore.java:2491, :9043
 └─ writes MapRegion.anInt131                     GameClientCore.java:2649

FloorDefinition (referenced from MapRegion)
 ├─ FloorDefinition.aClass22Array388[id]           — global floor table
 ├─ .anInt394..399 (HSL components + light)        — used in overlay path of method171
 ├─ .anInt395/396/397/398 (weighted underlay)      — used in 11x11 underlay blend
 ├─ .anInt390 (RGB 0xFF00FF == invisible)          — overlay invisible sentinel
 ├─ .anInt391 (texture id, -1 = none)              — overlay texture branch
 └─ .aBoolean393 (opaque floor flag)               — gates floor occluder
```

### Caveats / unclear items

- `anInt138` / `anInt150` / `anInt153` / `aBoolean132` / `aBoolean141` / `aBoolean143` look like dummy obfuscator state — purpose unclear; preserved for byte-parity with the original class. Treat as no-ops in any reimplementation.
- The `by`/`int guard` "magic number" parameters on most `method17x/18x/19x` are anti-tamper checks the obfuscator added; the only correct call sites in this codebase pass the expected values (`-107`, `9`, `4`, `942`, `93`, `6`, `7`, `8`, `110`, `891`, `-235`, ...). For a reimplementation drop them.
- The "occluder orientation sentinel" `anInt150 = -53` is forwarded to `SceneGraph.method277` as `k1`; without the `Occluder` source it is hard to say whether `-53` is meaningful or another dummy. Purpose unclear — based on the `Occluder` field assignment at `SceneGraph.java:136-142` it sets `class47.anInt791 = orientationCode` and `class47.anInt792..797` to bounding box coords, so `-53` likely is a sentinel.
- `method173`'s inner `while ((n3 = ...method422()) != 0) { method408(); }` discards per-tile placements — model loading only. The actual placements come through `method190`/`method183` later.
