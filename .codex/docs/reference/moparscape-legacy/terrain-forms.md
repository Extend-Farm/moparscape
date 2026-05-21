# Legacy terrain forms reference (SceneTile, SceneTilePaint, SceneTileModel, FloorDefinition)

Reverse-engineered notes on the four obfuscated classes that together describe
floor tiles in the legacy Moparscape client. All names of the form `anIntNNN`,
`methodNNN`, `aClassNN_NNN` are JAD obfuscation artefacts; the inferred names
and roles below are derived from cross-referencing call-sites (notably
[SceneGraph.method279](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L159),
[SceneGraph.method309](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L862),
[SceneGraph.method315](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1689),
[SceneGraph.method316](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1802) and
[MapRegion.method171](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L71)).
Source paths in this document are absolute. Anything that could not be
unambiguously identified is explicitly labelled "purpose unclear".

The conventions used by the legacy client are:
- A "tile" is a single 128-unit-wide cell of the floor grid.
- Tile XYZ is per-plane `[plane][tileX][tileY]` indexed as `[i][j][k]`.
- Each tile has four corners labelled SW, SE, NE, NW in the order
  `(j,k)`, `(j+1,k)`, `(j+1,k+1)`, `(j,k+1)` (`method315` uses that exact
  ordering when reading `anIntArrayArrayArray440`).
- Heightmap and per-corner colour come from `MapRegion` and the floor tables
  in `FloorDefinition.aClass22Array388`.

---

## SceneTile

File: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java`
Length: 48 lines.

### Overview

`SceneTile` (legacy `class30_sub3`) is the per-tile slot held in
`SceneGraph.aClass30_Sub3ArrayArrayArray441[plane][tileX][tileY]`
([SceneGraph.java:31](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L31)).
It is a `Node` (linked-list entry) so it can be queued during traversal in
`method314`. Each slot aggregates the renderables that live on that tile: the
flat paint, the shaped model, walls/wall decorations, ground decoration, item
pile, and up to five `InteractiveObject`s.

### Static fields

None.

### Instance fields

| Legacy | Inferred name | Shape | Purpose | Set by | Read by |
|---|---|---|---|---|---|
| `anInt1306` | `padding` (init `-589`) | `int` | Purpose unclear: only ever initialised in the ctor, never read elsewhere in the four files inspected. | ctor [SceneTile.java:17](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L17) | — |
| `anInt1307` | `plane` | `int` | Plane index `i` passed at construction. | ctor [SceneTile.java:20](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L20) | SceneGraph traversal |
| `anInt1308` | `tileX` | `int` | Tile X (`j`). | ctor [SceneTile.java:21](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L21) | SceneGraph traversal |
| `anInt1309` | `tileY` | `int` | Tile Y (`k`). | ctor [SceneTile.java:22](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L22) | SceneGraph traversal |
| `anInt1310` | `originPlane` | `int` | Initialised to the same value as `plane`; used to remember the original plane after a tile has been raised by `method275` (see [SceneGraph.java:99-123](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L99)). |
| `aClass43_1311` | `tilePaint` | `SceneTilePaint` | Flat tile paint (set when `method279` is called with `shape == 0 \|\| shape == 1`). | [SceneGraph.java:170,180](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L170) | [SceneGraph.method309](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L867), [SceneGraph.method315](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1689) |
| `aClass40_1312` | `tileModel` | `SceneTileModel` | Shaped tile model (set when `shape >= 2`). | [SceneGraph.java:188](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L188) | [SceneGraph.method309](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L884), [SceneGraph.method316](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1802) |
| `aClass10_1313` | `wallObject` | `WallObject` | Wall on this tile. | [SceneGraph.java:263](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L263) | scene traversal |
| `aClass26_1314` | `wallDecoration` | `WallDecoration` | Wall-mounted decoration. | [SceneGraph.java:286](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L286) | scene traversal |
| `aClass49_1315` | `groundDecoration` | `GroundDecoration` | Ground decoration. | [SceneGraph.java:207](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L207) | scene traversal |
| `aClass3_1316` | `itemPile` | `GroundItemPile` | Stack of ground items. | [SceneGraph.java:239](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L239) | scene traversal |
| `anInt1317` | `decorationFlags` | `int` | Purpose unclear: read by SceneGraph occlusion code; not directly involved in floor pipeline. | — | — |
| `aClass28Array1318` | `interactiveObjects` | `InteractiveObject[5]` | Up to five game objects on the tile. | ctor [SceneTile.java:18](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L18) | scene traversal |
| `anIntArray1319` | `interactiveObjectFlags` | `int[5]` | Per-object flag (size 5, parallel array). | ctor [SceneTile.java:19](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L19) | scene traversal |
| `anInt1320` | `interactiveObjectCount` | `int` | Number of valid entries in the two arrays above. Purpose unclear in detail. | scene mutation methods | — |
| `anInt1321` | `logicHeight` | `int` | Logic/render height for the tile. Set by [SceneGraph.java:154](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L154). | — | — |
| `aBoolean1322` | `drawn` | `boolean` | Render-time visit marker. | — | — |
| `aBoolean1323` | `visible` | `boolean` | Render-time visibility flag. | — | — |
| `aBoolean1324` | `culled` | `boolean` | Render-time culling flag (purpose inferred). | — | — |
| `anInt1325`-`anInt1328` | `cullState[0..3]` | `int` | Purpose unclear: traversal bookkeeping consumed by `method314`. | — | — |
| `aClass30_Sub3_1329` | `bridgeBelow` | `SceneTile` | Used when a bridge tile is raised: the original plane-0 tile is stashed here while `method275` moves the bridge slot up. See [SceneGraph.java:122](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L122). | `method275` | `method314` |

### Methods

Single constructor `SceneTile(int i, int j, int k)`
([SceneTile.java:15](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java#L15)):

- Parameters: `i = plane`, `j = tileX`, `k = tileY`.
- Initialises `anInt1306 = -589` (unused sentinel), allocates the
  `InteractiveObject[5]` and `int[5]` arrays, and sets
  `anInt1310 = anInt1307 = i`, `anInt1308 = j`, `anInt1309 = k`.
- Called from many sites in `SceneGraph` immediately before assigning a
  paint/model/decoration to the slot (see the `new SceneTile(...)` grep
  results, e.g. [SceneGraph.java:91](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L91),
  [121](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L121),
  [168](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L168),
  [206](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L206),
  [238](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L238),
  [261](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L261),
  [284](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L284),
  [393](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L393)).

### How this fits into the floor pipeline

`SceneTile` is the *container*. When the parser walks the heightmap in
`MapRegion.method171`, it eventually invokes
`SceneGraph.method279(plane, x, y, shape, rotation, textureId, …)` per tile;
that method allocates (if necessary) a `SceneTile` for every plane from 0..i
and parks either a `SceneTilePaint` in `aClass43_1311` (flat tile) or a
`SceneTileModel` in `aClass40_1312` (shaped tile). The minimap (`method309`)
and the world render (`method315`/`method316`) read those two slots back off
the `SceneTile`.

---

## SceneTilePaint

File: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTilePaint.java`
Length: 33 lines.

### Overview

Flat-paint (non-shaped) tile. Holds the four corner colours (HSL anchors),
optional texture id, the minimap base colour, and a flag noting that all four
corners share the same shade.

### Static fields

None.

### Instance fields

Order of arguments at the ctor matches the assignment order
([SceneTilePaint.java:17-25](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTilePaint.java#L17)).
Cross-referencing call-site
[SceneGraph.method279](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L165)
gives:
- For underlay (`shape == 0`): `new SceneTilePaint(k2, l2, i3, j3, -1, k4, false)`
  i.e. `(swColour, seColour, neColour, nwColour, textureId=-1, minimapColour, flat=false)`.
- For overlay (`shape == 1`): `new SceneTilePaint(k3, l3, i4, j4, j1, l4, k1==l1 && k1==i2 && k1==j2)`
  i.e. the texture id `j1` is the floor's `anInt391`, `flat` flag set iff all
  four overlay corners share a hue.

| Legacy | Inferred name | Shape | Purpose | Notes |
|---|---|---|---|---|
| `anInt716` | `colourSW` | `int` | South-west corner HSL colour. | Read by [method315:1786,1793](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1786). |
| `anInt717` | `colourSE` | `int` | South-east corner HSL colour. | Read by method315 (multiple). |
| `anInt718` | `colourNE` | `int` | North-east corner HSL colour. | Used as the primary colour also tested against `0xbc614e` sentinel ([SceneGraph.java:1757](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1757)) — that magic value (12345678) means "hidden/no draw". |
| `anInt719` | `colourNW` | `int` | North-west corner HSL colour. | — |
| `anInt720` | `textureId` | `int` | Floor texture id; `-1` means "no texture, use untextured shaded triangle". | Read by [SceneGraph.java:1755,1763,1768,1782,1793,1796](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1755). |
| `aBoolean721` | `uniformShade` | `boolean` | True iff all four corners share the same overlay hue; selects the alternative UV mapping in `method315` (see [SceneGraph.java:1762-1765](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1762)). Defaults to `true` then overwritten by the ctor argument `flag` — that re-assignment makes the initial value irrelevant. |
| `anInt722` | `minimapColour` | `int` | Per-tile flat base colour drawn into the minimap pixel block. `0` is the "do not draw" sentinel per [SceneGraph.method309](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L870-L872). The underlay variant uses `k4` from MapRegion (a Rasterizer3D-mapped colour); the overlay variant uses `l4` derived from `FloorDefinition.anInt399` (see [MapRegion.java:289](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L289)). |

### Methods

Single constructor (only behaviour is field assignment as listed above).

### How this fits into the floor pipeline

A `SceneTilePaint` represents one quad of the heightmap-shaded floor with no
geometric sub-division. It is created in `SceneGraph.method279` when the
caller's `shape` parameter is 0 (no overlay; underlay-only) or 1 (full-tile
overlay), and stored on `SceneTile.aClass43_1311`. At render-time
`SceneGraph.method315` projects the four corners using
`anIntArrayArrayArray440` (the heightmap) and emits one or two textured /
flat-shaded triangles depending on `uniformShade`. The minimap renderer reads
`anInt722` to colour the 4x4 pixel block representing this tile.

---

## SceneTileModel

File: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java`
Length: 351 lines (constructor + static lookup tables only — no instance
methods; rendering is performed by `SceneGraph.method316`).

### Overview

Shaped tile model: tiles whose floor is split into triangles because an
overlay only covers part of the tile (e.g. a diagonal corner, a centre dot,
an L-shape). The class stores:

- Per-vertex world-space coordinates `(x,y,z)` (with `y` the projected height
  used by the rasteriser).
- Per-triangle vertex indices.
- Per-triangle RGB-triplet colours for both underlay and overlay shades.
- Per-triangle texture id (`null` array if there is no texture).
- Shape id, rotation, and a precomputed "flat" flag.
- Static scratch arrays reused by `SceneGraph.method316` during projection.

### Static fields

All lookup tables are immutable for `anIntArrayArray696` /
`anIntArrayArray697` and the small permutation tables; the `anIntArray688..692`
arrays are render-time scratch.

| Legacy | Inferred name | Shape | Purpose |
|---|---|---|---|
| `anIntArray688[6]` | `screenX` | scratch `int[6]` | Per-vertex projected screen X used by [SceneGraph.method316:1827](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1827). |
| `anIntArray689[6]` | `screenY` | scratch `int[6]` | Per-vertex projected screen Y ([1828](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1828)). |
| `anIntArray690[6]` | `viewSpaceX` | scratch `int[6]` | View-space X used for texture mapping when `anIntArray682 != null` ([1823](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1823)). |
| `anIntArray691[6]` | `viewSpaceY` | scratch `int[6]` | View-space Y for texture mapping ([1824](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1824)). |
| `anIntArray692[6]` | `viewSpaceZ` | scratch `int[6]` | View-space Z for texture mapping ([1825](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1825)). |
| `anIntArray693` | unclear, `{1,0}` | `int[2]` | Purpose unclear: read nowhere in the four files inspected. |
| `anIntArray694` | unclear, `{2,1}` | `int[2]` | Purpose unclear. |
| `anIntArray695` | unclear, `{3,3}` | `int[2]` | Purpose unclear. |
| `anIntArrayArray696[13][]` | `shapeVertexLookup` | `int[shape][]` | Per-shape vertex layout: each element is an "octant index" (1..16) describing which of the 16 standard sub-positions on a tile a given vertex sits in. See the inline 1..16 dispatch in the ctor ([SceneTileModel.java:37-177](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L37)). Octants 1,3,5,7 are the four corners (SW,NE-via-S boundary,SE,NW); 2,4,6,8 are the four edge midpoints; 9..16 are sub-quadrant centres. |
| `anIntArrayArray697[13][]` | `shapeTriangleLookup` | `int[shape][n*4]` | Per-shape triangle list, packed as `(overlayFlag, vertexA, vertexB, vertexC)` quadruplets (see the `l7 += 4` walk at [SceneTileModel.java:202](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L202)). `overlayFlag == 0` means "use the underlay colour set"; non-zero means "use the overlay colour set and bind the texture id". |

### Instance fields

| Legacy | Inferred name | Shape | Purpose |
|---|---|---|---|
| `anIntArray673` | `vertexX` | `int[n]` | World X per vertex (n = `shapeVertexLookup[shape].length`). |
| `anIntArray674` | `vertexY` | `int[n]` | World Y (height) per vertex. |
| `anIntArray675` | `vertexZ` | `int[n]` | World Z per vertex. |
| `anIntArray676` | `triUnderlayColourA` | `int[m]` | Per-triangle colour for vertex A (m = `shapeTriangleLookup[shape].length / 4`). When the triangle's overlay flag is 0 the colours come from the underlay corner shades (`ai1` array), otherwise from the overlay corner shades (`ai2`). |
| `anIntArray677` | `triColourB` | `int[m]` | Per-triangle colour for vertex B. |
| `anIntArray678` | `triColourC` | `int[m]` | Per-triangle colour for vertex C. |
| `anIntArray679` | `triVertexA` | `int[m]` | Triangle vertex index A (also rotated by `k1` for indices < 4 — i.e. the four corner indices honour the tile rotation). |
| `anIntArray680` | `triVertexB` | `int[m]` | Triangle vertex index B. |
| `anIntArray681` | `triVertexC` | `int[m]` | Triangle vertex index C. |
| `anIntArray682` | `triTextureId` | `int[m]` or `null` | Per-triangle texture id; only allocated if the constructor's `i1` (texture id) is not `-1`. Entries are `-1` for underlay triangles and the floor's texture id for overlay triangles. |
| `aBoolean683` | `uniformOverlay` | `boolean` | True iff the four overlay corner shades (`i3, l2, l, k2`) are equal; selects the alternative texture-coordinate path in `method316` ([1861-1865](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1861)). Determined at construction by comparing `i3 != l2 \|\| i3 != l \|\| i3 != k2`. |
| `anInt684` | `minimapUnderlay` | `int` | Stores ctor arg `j3` (the underlay's minimap colour). Read by `method309` as the minimap index into `anIntArrayArray489` (mask table). |
| `anInt685` | `rotation` | `int` | Stores ctor arg `k1` (the tile's rotation in quarters). Used by `method309` to look up the minimap permutation `anIntArrayArray490[rotation]`. |
| `anInt686` | `minimapOverlay` | `int` | Stores ctor arg `i2`. The non-zero/zero branch in [method309:894](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L894) treats this as the underlay-side minimap colour: when non-zero the minimap pixels are painted with either it or the overlay colour depending on the mask; when zero only the overlay colour is painted in masked pixels. |
| `anInt687` | `minimapOverlayColour` | `int` | Stores ctor arg `l4`. The overlay-side minimap colour. |

### Methods

Single constructor only (no instance methods; rendering is `SceneGraph.method316`).

`SceneTileModel(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4, int l4)`
([SceneTileModel.java:13](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L13)).

Parameter meanings (recovered by following `method279` and `MapRegion.method171`):

| Ctor arg | Inferred role | Source at call-site ([SceneGraph.java:183](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L183)) |
|---|---|---|
| `i` | tileX (world tile X, used to compute `j6 = i * 128`). | `k` from `method279` |
| `j` | overlay corner-height SW (vertical Y for vertex at SW). | `k3` |
| `k` | overlay corner-height SE. | `j3` |
| `l` | overlay corner-height NE. | `i2` |
| `i1` | texture id (or `-1` for untextured). | `j1` |
| `j1` | overlay corner-shade NE (UV / colour for NE vertex). | `i4` |
| `k1` | tile rotation (0..3 quarters). | `i1` |
| `l1` | underlay corner-shade NE. | `k2` |
| `i2` | overlay corner-height NW. | `k4` |
| `j2` | underlay corner-shade NW. | `i3` |
| `k2` | overlay corner-shade NW. | `j2` |
| `l2` | overlay corner-height SE. | `l1` |
| `i3` | underlay corner-shade SW. | `k1` |
| `j3` | shape id (index into `anIntArrayArray696` / `anIntArrayArray697`). | `l` |
| `k3` | underlay corner-shade SE. | `j4` |
| `l3` | overlay corner-shade SW. | `l3` |
| `i4` | underlay corner-shade NE-z (`l3` from caller). | `l2` |
| `j4` | unused-debug marker (caller passes literal `3`; constructor uses it only in the no-op `if (j4 < 3 \|\| j4 > 3)` block at lines 245-248). | literal `3` |
| `k4` | tileY (used to compute `i6 = k4 * 128`). | `j` |
| `l4` | overlay corner-shade SE (alongside the minimap-overlay colour stored as `anInt687`). | `l4` |

Note: because the obfuscated parameter list re-orders many values, the
column above lists the *only* mapping that produces consistent rendering
output when read against `method279`. The pairing of "height" vs "shade"
values is inferred from the way the inner loop consumes them: for each
vertex octant (1..16) the loop fetches *one* height (`k8`) and *one*
overlay shade (`j9`), then later (`ai1`/`ai2`) reuses `k8` as the underlay
shade and `j9` as the overlay shade depending on the triangle's overlay
flag. Where two interpretations are equally plausible we have chosen the
one that lines up with the OSRS-spec shape table.

Loop-by-loop walk-through:

1. **Header / static state** ([13-36](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L13)):
   `aBoolean683` is set true then cleared if the four overlay corner
   shades `(i3, l2, l, k2)` are not all identical. Ctor args `j3, k1, i2,
   l4` are stashed on the instance (`anInt684`..`anInt687`). Constants
   `c=128`, `i5=64`, `j5=32`, `k5=96` are pre-computed octant offsets.
   `ai = anIntArrayArray696[j3]` selects the vertex layout for the shape;
   `l5 = ai.length` is the per-shape vertex count. Five working arrays of
   length `l5` are allocated: `anIntArray673` (vertexX), `anIntArray674`
   (vertexY), `anIntArray675` (vertexZ), plus locals `ai1` (per-vertex
   underlay shade) and `ai2` (per-vertex overlay shade).

2. **Vertex layout loop** ([37-183](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L37)):
   For each of the `l5` vertices the loop reads its octant `l6`.

   - Octants in the 1..8 range (the eight cardinal positions on the tile
     perimeter) are rotated by the tile rotation `k1` using the mask
     `(l6 - k1 - k1 - 1 & 7) + 1` for the corner-only cases (`(l6 & 1) ==
     0` is misleading — the check is `(l6 & 1) == 0 && l6 <= 8` and only
     fires for even octants 2,4,6,8).
   - Octants 9..12 (intermediate radial positions for L-shape tiles) are
     rotated with `((l6 - 9 - k1) & 3) + 9`.
   - Octants 13..16 (inner sub-quadrant centres) are rotated with
     `((l6 - 13 - k1) & 3) + 13`.

   Then a 16-way dispatch sets `(i7, k7)` to the vertex's world X/Z
   relative to the tile origin `(i6, j6)` in 128-unit steps with 32/64/96
   midpoints, picks `i8 = vertexY` (a corner or interpolated mid-edge
   height — `>> 1` averages two corner heights for edges) and stores
   `ai1[k6] = k8` (underlay shade) and `ai2[k6] = j9` (overlay shade) for
   the matching corner. Octants 9..12 reuse the four edge midpoint values
   (so the inner ring uses the same shading as the matching edge),
   octants 13..16 reuse the four corner values (inner corner sub-points
   share the matching corner's shade/height). For each vertex the loop
   then writes `anIntArray673[k6] = i7`, `anIntArray674[k6] = i8`,
   `anIntArray675[k6] = k7`.

3. **Triangle build loop** ([185-227](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L185)):
   `ai3 = anIntArrayArray697[j3]` and the triangle count is `j7 =
   ai3.length / 4`. Six per-triangle arrays of length `j7` are allocated;
   `anIntArray682` (textures) is allocated only when `i1 != -1`. The loop
   walks `ai3` four entries at a time: `l8` (overlay flag, 0 or 1), `k9`
   (vertex A), `i10` (vertex B), `k10` (vertex C). For each vertex index
   under 4 (i.e. the four canonical corners) it is rotated in-place by the
   tile rotation (`(idx - k1) & 3`). The indices are stored in
   `anIntArray679/680/681`. When `l8 == 0` the triangle's three colours
   are pulled from `ai1` (underlay shades) and the texture entry is `-1`;
   otherwise from `ai2` (overlay shades) and the texture id `i1`.

4. **Min/max scan + JAD dead loop** ([229-248](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java#L229)):
   `i9` and `l9` accumulate the min/max overlay-height values; results are
   divided by 14 but never used — JAD has lost the load. The trailing
   `if (j4 < 3 \|\| j4 > 3) for(int j10 = 1; j10 > 0; j10++);` is a
   classic JAD-emitted infinite-loop artefact guarded by a comparison
   that, given the caller always passes literal `3`, is dead. Treat the
   block as a no-op.

Calls out: none. Read by: `SceneGraph.method316` (render),
`SceneGraph.method309` (minimap).

### How this fits into the floor pipeline

A `SceneTileModel` represents a tile whose underlay/overlay split requires
two or more triangles. It is created by `SceneGraph.method279` (caller
`MapRegion.method171`) when the floor shape id is `>= 2` and stored on
`SceneTile.aClass40_1312`. The shape id picks vertex and triangle templates
from the static tables, the rotation byte rotates corner indices into the
template, and the per-corner height/shade tuples come from the same
`MapRegion` arrays used for the simpler `SceneTilePaint` case. At render time
`method316` projects each vertex using `anIntArray688..692` scratch and
issues one `Rasterizer3D.method374`/`method378` call per triangle, picking
between texture mapping using `aBoolean683` exactly as the simpler
`SceneTilePaint` flag does. The minimap reads the shape id (`anInt684`) and
rotation (`anInt685`) to mask 16 pixels via `anIntArrayArray489` /
`anIntArrayArray490`, painting either `anInt686` or `anInt687` per pixel.

---

## FloorDefinition

File: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java`
Length: 191 lines.

### Overview

Parsed entry from `flo.dat` — one record per underlay/overlay floor type.
Stores the RGB swatch, derived HSL, optional texture id, a sentinel flag, and
flags that control whether the floor occludes the underlay or projects onto
the minimap.

### Static fields

| Legacy | Inferred name | Shape | Purpose |
|---|---|---|---|
| `anInt386` | `loadGuard` | `int` | Sentinel toggled to `115` when `method260` is invoked with `i != 0` ([FloorDefinition.java:17-18](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L17)). Purpose unclear beyond a guard against JIT inlining; only ever written. |
| `anInt387` | `count` | `int` | Number of floor records read from `flo.dat` ([FloorDefinition.java:21](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L21)). |
| `aClass22Array388` | `floors` | `FloorDefinition[]` | Indexed by floor id (`anInt387` long). Read from `MapRegion.method171` at e.g. [MapRegion.java:169,182,278](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L169) and cleared by `GameClientCore` shutdown at [GameClientCore.java:4746](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L4746). |

### Instance fields

| Legacy | Inferred name | Shape | Purpose |
|---|---|---|---|
| `aBoolean385` | `loaded` | `boolean` (`true` default) | Toggled if `method261` is called with `flag == false`. Purpose unclear in the legacy parser; the call-site always passes `true`. |
| `aString389` | `name` | `String` | Floor display name (opcode 6 in `flo.dat`). |
| `anInt390` | `rgbColour` | `int` (RGB 0xRRGGBB) | Raw RGB swatch for the floor. The magic value `0x00FF00FF` (per [MapRegion.java:283](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L283)) is the "hide this floor entirely" sentinel — when seen the caller skips HSL computation and forces texture-less rendering. |
| `anInt391` | `textureId` | `int` (`-1` default) | Texture id (opcode 2). `-1` means no texture. Consumed by `MapRegion.method171` at [MapRegion.java:279-282](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L279) where it is normalised by `Rasterizer3D.method369` and stored as the overlay's `class22.anInt391`. |
| `aBoolean392` | `requiresAdjust` | `boolean` | Set true by opcode 3. Purpose unclear in the four files inspected. |
| `aBoolean393` | `occludesUnderlay` | `boolean` (`true` default) | Set false by opcode 5. Read by [MapRegion.java:258](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L258) — when false the overlay does *not* hide the underlay seam, so the seam-blending bit pattern `0x924` is *not* OR'd into `anIntArrayArrayArray135`. |
| `anInt394` | `hue` | `int` (0..255) | HSL hue derived from `anInt390` by `method262`. |
| `anInt395` | `saturation` | `int` (0..255) | HSL saturation. |
| `anInt396` | `lightness` | `int` (0..255) | HSL lightness. |
| `anInt397` | `hueWeightedByChroma` | `int` | `hue * chromaWeight` precomputed for blend averaging in `MapRegion.method177`. |
| `anInt398` | `chromaWeight` | `int` | Lightness-weighted chroma used as the denominator when averaging hues across adjacent tiles. |
| `anInt399` | `minimapRgb` | `int` | Pre-quantised minimap colour, derived from a jittered (`±8` hue, `±24` sat/lum) copy of the HSL triple via `method263`. Read by [MapRegion.java:289](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L289). |

### Methods

| Signature | Purpose |
|---|---|
| `static void method260(int i, Archive class44)` ([15](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L15)) | Parse `flo.dat` from the archive. Reads `count` (1 short), allocates `aClass22Array388[count]`, then loops calling `method261(true, …)` per entry. Called by [BootstrapConfigLoader.java:20](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L20). |
| `void method261(boolean flag, PacketBuffer pb)` ([32](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L32)) | Per-entry parser. Reads opcodes in a loop until opcode `0` (terminator). Opcodes: `1` rgb (calls `method262(rgb, 9)`), `2` textureId, `3` aBoolean392 = true, `5` aBoolean393 = false, `6` name, `7` re-runs `method262` on a fresh rgb but then restores the previous HSL/anInt397 and only overwrites `anInt398`. Logs unknown opcodes to stdout. |
| `private void method262(int i, int j)` ([78](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L78)) | RGB → HSL conversion. `i` is the packed RGB; `j` is a magic dispatch value (caller always passes `9`, and the method early-returns when `j != 9` at [139](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L139) — purpose unclear, looks like an obfuscation guard). Computes hue/sat/lum into `anInt394/395/396`, derives `anInt398` from lightness-weighted chroma, sets `anInt397 = hue * anInt398`, then re-jitters HSL by `±8/±24/±24` and stores the quantised RGB-15 result in `anInt399`. |
| `private final int method263(int i, int j, int k)` ([155](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L155)) | Quantises HSL `(i, j, k)` to a 5-7-... packed minimap palette index, halving saturation as lightness rises past 179/192/217/243. Returns `((i/4) << 10) \| ((j/32) << 7) \| (k/2)` — the standard 5-bit hue / 3-bit sat / 7-bit lum packing used by `Rasterizer3D.anIntArray1482`. |
| `FloorDefinition()` ([169](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java#L169)) | Defaults: `aBoolean385 = true`, `anInt391 = -1`, `aBoolean392 = false`, `aBoolean393 = true`. |

### How this fits into the floor pipeline

`FloorDefinition` is the *configuration* layer. `MapRegion.method171`
indexes `aClass22Array388[overlayId - 1]` to read the floor's texture id and
HSL/minimap colours and from those derives the per-corner shades that are
ultimately handed to `SceneGraph.method279`. `method279` then either wraps
those values in a `SceneTilePaint` (`shape 0/1`) or a `SceneTileModel`
(`shape >= 2`) attached to a `SceneTile` slot. `aBoolean393` (occludes
underlay) is the only field whose value escapes outside the colour pipeline:
it controls whether the underlay's seam mask bit `0x924` is set on the tile.

---

## Cross-class call graph

```
flo.dat (Archive)
   |
   v
FloorDefinition.method260 ---- (per entry) ---> FloorDefinition.method261
       |                                              |
       v                                              v
FloorDefinition.aClass22Array388[]              FloorDefinition.method262 --> method263
       ^                                              |
       |                                              v
       |                                      anInt394/395/396 (HSL),
       |                                      anInt397/398 (chroma),
       |                                      anInt399 (minimap colour)
       |
       |  (referenced by overlayId)
       |
MapRegion.method171
       |
       +--> reads class22.anInt390 (rgb)            } overlay colour /
       +--> reads class22.anInt391 (textureId)      } texture
       +--> reads class22.anInt393 (occludesUnderlay) -> writes 0x924 mask
       +--> reads class22.anInt394/395/396 (HSL)    } per-corner shading
       +--> reads class22.anInt399 (minimap colour) } per-corner minimap shade
       |
       v
SceneGraph.method279(plane, x, y, shape, rotation, textureId,
                    underlaySW, underlaySE, underlayNE, underlayNW,
                    overlaySW, overlaySE, overlayNE, overlayNW,
                    underlayShade*4, minimapUnderlay, minimapOverlay)
       |
       +-- shape == 0  --> new SceneTilePaint(... textureId=-1 ...)  --> SceneTile.aClass43_1311
       +-- shape == 1  --> new SceneTilePaint(... textureId>=0  ...)  --> SceneTile.aClass43_1311
       +-- shape >= 2  --> new SceneTileModel(shape, rotation, ...)   --> SceneTile.aClass40_1312
       |
       v
SceneTile slot in SceneGraph.aClass30_Sub3ArrayArrayArray441[plane][x][y]

Render path:
  SceneGraph.method315(SceneTilePaint, ...)
       -> projects 4 corners using anIntArrayArrayArray440 (heightmap)
       -> Rasterizer3D.method374 / method378 (textured)
       -> uses SceneTilePaint.aBoolean721 to pick UV mapping

  SceneGraph.method316(SceneTileModel, ...)
       -> projects each vertex into anIntArray688/689 (screen),
          anIntArray690/691/692 (view space)
       -> per triangle: Rasterizer3D.method374 / method378
       -> uses SceneTileModel.aBoolean683 to pick UV mapping

Minimap path:
  SceneGraph.method309(int[], i, j, plane, x, y)
       -> SceneTilePaint: paint 4x4 pixel block with anInt722, skip when == 0
       -> SceneTileModel:
            shape = anInt684, rotation = anInt685
            mask  = SceneGraph.anIntArrayArray489[shape]
            perm  = SceneGraph.anIntArrayArray490[rotation]
            per pixel: paint anInt687 (overlay) where masked,
                       paint anInt686 (underlay) elsewhere when non-zero
```

Files referenced:
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTile.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTilePaint.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneTileModel.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FloorDefinition.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
