# CollisionMap + RoutePlanner (legacy moparscape)

This reference covers the two cooperating helpers that own per-tile passability for a single scene plane and the breadth-first search that turns that grid into a server-bound walk packet. Both files are decompiler output from `moparclient.jar`; field/method names are the original obfuscator names. Inferred purposes are flagged.

- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/CollisionMap.java` (641 lines, Jad header at `CollisionMap.java:7-9`)
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/RoutePlanner.java` (235 lines, partly reconstructed: marked `final class`, all state hoisted to parameters)

Companion docs that touch the same data:
- [`MapRegion.md`](MapRegion.md) for how walls/objects are decoded out of the cache and stamped via `method211/212/215/216/218`.
- [`GameClientCore-A..E.md`](GameClientCore-A.md) for the per-tick caller `method85`.

## CollisionMap.java

### Overview

- **Class:** `public class CollisionMap` (`CollisionMap.java:10`).
- **Line count:** 641 source lines.
- **Purpose:** Owns the per-tile blocking-flag grid for one scene plane of size `anInt292 x anInt293` (instantiated as 104x104 by `GameClientCore.java:6356`). Each grid cell is a packed `int` of bit flags describing:
  - Per-direction wall presence (8 cardinal/diagonal bits in the low byte).
  - Solid object presence (`0x100`).
  - Plane-shift / bridge-deck blocking (`0x20000`).
  - Hard "blocked tile" (`0x200000`) toggled by `method213` / cleared by `method218`.
  - Out-of-bounds sentinel (`0xffffff` set on border tiles by `method210`; uninitialised body tiles default to `0x1000000`).
- **Co-owners:** 4 instances live in `GameClientCore.aClass11Array1230[plane]` (allocated at `GameClientCore.java:10411` and `:6356`), reset each scene rebuild via `method210` (`GameClientCore.java:480, 1973`), populated by `MapRegion.method171/175/183/188` (see [`MapRegion.md`](MapRegion.md) and `MapRegion.java:102, 571, 611, 624, 673, 695, 743, 765, 773, 1121, 1144, 1152, 1160, 1168, 1185, 1193, 1201`), and consumed by `RoutePlanner.route` plus the ground-item drift loop at `GameClientCore.java:756-768`.

### Bit-flag dictionary

The cell type is `int` and bit layout is inferred from the masks used by `method211/215/219/220/221` and `RoutePlanner.route`. Bits group into four logical layers:

**Wall presence — low byte (per-direction, set by `method211`/`method215`):**

| Bit  | Mask     | Direction / wall kind | Set at |
|------|----------|----------------------|--------|
| 0    | `0x01`   | NE diagonal corner (`type 1/3`, rot 0)     | `method211:88`, `method215:284` |
| 1    | `0x02`   | South wall (`type 0`, rot 3)               | `method211:81/125/130`, `method215:277/321/326` |
| 2    | `0x04`   | SE diagonal corner (`type 1/3`, rot 1)     | `method211:93`, `method215:289` |
| 3    | `0x08`   | West wall (`type 0`, rot 0 / `type 2`)     | `method211:65/112/131`, `method215:261/308/327` |
| 4    | `0x10`   | SW diagonal corner (`type 1/3`, rot 2)     | `method211:98`, `method215:294` |
| 5    | `0x20`   | North wall (`type 0`, rot 1)               | `method211:71/113/118`, `method215:267/309/314` |
| 6    | `0x40`   | NW diagonal corner (`type 1/3`, rot 3)     | `method211:103`, `method215:299` |
| 7    | `0x80`   | East wall (`type 0`, rot 2 / `type 2`)     | `method211:76/119/124`, `method215:272/315/320` |

Composites in `method211/215` such as `130 = 0x82` (S+E), `10 = 0x0A` (S+W), `40 = 0x28` (N+W), `160 = 0xA0` (N+E) stamp two cardinal walls at once for the four rotations of `type 2` (L-shaped corner) walls.

**Solid object — bit 8:**

| Bit  | Mask     | Meaning | Set at |
|------|----------|---------|--------|
| 8    | `0x100`  | Tile contains a solid interactive object (`type 9/10/22` from MapRegion). Base of the `256` literal in `method212/216`. | `method212:214`, `method216:410` |

**"Projectile/sight blocks" shadow layer — bits 9..16 (set when `method211/215` are called with `flag=true`):**

| Bit  | Mask      | Mirror of                |
|------|-----------|--------------------------|
| 9    | `0x200`   | bit 0 (`0x01`)           |
| 10   | `0x400`   | bit 1 (`0x02`)           |
| 11   | `0x800`   | bit 2 (`0x04`)           |
| 12   | `0x1000`  | bit 3 (`0x08`)           |
| 13   | `0x2000`  | bit 4 (`0x10`)           |
| 14   | `0x4000`  | bit 5 (`0x20`)           |
| 15   | `0x8000`  | bit 6 (`0x40`)           |
| 16   | `0x10000` | bit 7 (`0x80`)           |

These are the literals `512, 1024, 2048, 4096, 8192, 16384, 32768, 0x10000` (and composites `0x10400 = 0x10000|0x400`, `5120 = 0x1000|0x400`, `20480 = 0x4000|0x1000`, `0x14000 = 0x10000|0x4000`) used inside the `if(flag)` arms of `method211:134-209` / `method215:330-405`. They duplicate the wall bit layout shifted by 9. The pathfinder masks never reference these — they appear to be a sight/projectile/cast-attack layer.

**Bridge-deck / plane-shift — bit 17:**

| Bit  | Mask       | Meaning | Set at |
|------|------------|---------|--------|
| 17   | `0x20000`  | Plane-shadow object — added to the solid-object stamp when the `flag` argument of `method212/216` is true. Same parameter that MapRegion derives from `class46.aBoolean757` (`MapRegion.java:611/624/773/1144/1152/1201`), i.e. the ObjectDefinition "blocks projectiles / plane shadow" toggle for bridge-deck tiles. | `method212:215-216`, `method216:411-412` |

**Hard-blocked tile — bit 21:**

| Bit  | Mask       | Meaning | Set at |
|------|------------|---------|--------|
| 21   | `0x200000` | "This tile is fully blocked" (set per ground decoration in MapRegion `method171:102`). Cleared individually by `method218`. | `method213:243`, `method218:445` (clear via `& 0xdfffff`) |

**Sentinel — bit 24:**

| Bit  | Mask       | Meaning |
|------|------------|---------|
| 24   | `0x1000000` | "Empty / uninitialised" — written to every interior tile by `method210:47`. Border tiles get `0xffffff` (`method210:45`) to forbid any wall/object touching the edge. The pathfinder's composite mask `0x1280000 = 0x1000000 \| 0x200000 \| 0x80000` includes this bit, so any tile still at `0x1000000` is treated as impassable. |

**Pathfinder composite masks** (used by `RoutePlanner.route` and `method219/220/221`):

| Composite mask | Decomposition (bits)                                                          | Step direction tested |
|----------------|--------------------------------------------------------------------------------|-----------------------|
| `0x1280102`    | `0x1000000` empty \| `0x200000` blocked \| `0x80000` (bit 19, "blocked-S") \| `0x100` object \| `0x02` south wall | Move N (step `+y`) into tile to the north — check south face of that tile |
| `0x1280108`    | `0x1000000 \| 0x200000 \| 0x80000 \| 0x100 \| 0x08`                            | Move W (`-x`) — check west face |
| `0x1280120`    | `0x1000000 \| 0x200000 \| 0x80000 \| 0x100 \| 0x20`                            | Move S (`-y`) — check north face |
| `0x1280180`    | `0x1000000 \| 0x200000 \| 0x80000 \| 0x100 \| 0x80`                            | Move E (`+x`) — check east face |
| `0x128010e`    | `…\|0x100\|0x0E` (`0x08\|0x04\|0x02`)                                          | NW diagonal: blocked if west wall, south wall, or SE-diagonal corner is present |
| `0x1280183`    | `…\|0x100\|0x83` (`0x80\|0x02\|0x01`)                                          | NE diagonal |
| `0x1280138`    | `…\|0x100\|0x38` (`0x20\|0x10\|0x08`)                                          | SW diagonal |
| `0x12801e0`    | `…\|0x100\|0xE0` (`0x80\|0x40\|0x20`)                                          | SE diagonal |

The repeated `0x80000` (bit 19) in every pathing mask is unaccounted for by any setter in this file; it appears to be another "blocked" sentinel set elsewhere (likely by `aByteArrayArrayArray149` translation in `GameClientCore` — purpose unclear from this file alone).

### Static fields

None — `CollisionMap` declares no `static` state. (All `static` magic is via composite literal bit masks inlined in `method211/215/219` callers.)

### Instance fields (declarations at `CollisionMap.java:629-641`)

| Legacy name | Type | Purpose | Read/written |
|---|---|---|---|
| `aBoolean282` | `boolean` (`:629`) | Obfuscator dummy. Initialised `true` (`:15`); toggled in `method213`, `method216`, `method219` whenever an opaque parameter check fails. Purpose unclear — no observed readers. | toggled `:241, 416, 451`. |
| `anInt283` | `int` (`:630`) | Obfuscator dummy. Set to `-32357` in ctor, overwritten to `-496` when `!flag` (`:31`). No readers. Purpose unclear. | `:16, 31`. |
| `aBoolean284` | `boolean` (`:631`) | Declared, never used. Purpose unclear (likely dead). | none. |
| `anInt285` | `int` = 7 (`:632`) | Dead. Purpose unclear. | none. |
| `aBoolean286` | `boolean` = true (`:633`) | Dead. Purpose unclear. | none. |
| `aBoolean287` | `boolean` = true (`:634`) | Dead. Purpose unclear. | none. |
| `aByte288` | `byte` = 2 (`:635`) | Dead. Purpose unclear. | none. |
| `aByte289` | `byte` = -101 (`:636`) | Dead. Purpose unclear. | none. |
| `anInt290` | `public int` (`:637`) | World-tile X offset of this region's local-tile origin (the "base X" passed in subtraction `k -= anInt290` to convert absolute tile X -> local `[0, 103]` index). Initialised to `0` and never written by this class — `GameClientCore` mutates it directly. | read every public stamper (`:55, 217, 239, 253, 413, 442, 454, 456, 560, 562, 620, 622, 624, 626`). |
| `anInt291` | `public int` (`:638`) | World-tile Y origin offset, twin of `anInt290`. | read same call sites. |
| `anInt292` | `public int` (`:639`) | Grid width in tiles (== 104 in practice). | ctor `:25`, bounds `:41, 44, 227`. |
| `anInt293` | `public int` (`:640`) | Grid height in tiles (== 104). | ctor `:26`, bounds `:43, 44, 230`. |
| `anIntArrayArray294` | `public int[anInt292][anInt293]` (`:641`) | The blocking-flag grid itself. Allocated in ctor (`:27`); reset by `method210`. Direct access via `aClass11Array1230[plane].anIntArrayArray294` is taken by `RoutePlanner.route:36` and `GameClientCore.java:756`. | written by all setters; read by `method219/220/221` and external code. |

### Methods

All bit literals below decode against the dictionary above. Many parameters with names like `byte0`, `i`, `l` exist purely as obfuscator anti-tamper checks (the first thing the method does is `if (byte0 != 1) return;` or `throw new NullPointerException()`); those are flagged "obfuscator parameter".

#### `CollisionMap(int i, int j, boolean flag)` — `:13`

- **Signature:** ctor.
- **Purpose:** Build a `i x j` collision grid, initialise the dead-field defaults, allocate the `int[i][j]` flag array, and call `method210` to write borders + empty sentinels.
- **Params:** `i` = width (`anInt292`), `j` = height (`anInt293`), `flag` = obfuscator parameter — selects which sentinel (`-32357` vs `-496`) gets written to dead field `anInt283`.
- **Called by:** `GameClientCore.java:6356` (`new CollisionMap(104, 104, true)`).

#### `void method210()` — `:39`

- **Purpose:** Reset the grid. Border tiles (any `i==0 || j==0 || i==anInt292-1 || j==anInt293-1`) get `0xffffff` (every wall + object + projectile bit set => completely impassable); interior tiles get `0x1000000` (the "empty" sentinel).
- **Called by:** ctor `:28`; `GameClientCore.java:480, 1973` (per scene rebuild and per region load).

#### `void method211(int i, int j, int k, int l, byte byte0, boolean flag)` — `:53`

- **Purpose:** Stamp a wall into the grid based on its shape, rotation and add-projectile-flag. This is the "wall blocker" entry point.
- **Params:**
  - `i` = world tile Y; `k` = world tile X (subtracted by `anInt291/anInt290` -> local `(k, i)`).
  - `j` = rotation 0..3 (N/E/S/W facing).
  - `l` = wall type / shape selector: `0` = straight wall (1 bit on this tile + mirrored bit on neighbour); `1` or `3` = diagonal corner; `2` = L-shaped corner (2 bits on this tile, 2 on neighbours).
  - `byte0` = obfuscator parameter; method early-returns unless `byte0 == 1` (`:57-60`).
  - `flag` = if true, also stamp the bit-9-shifted "projectile shadow" layer (`:134-209`).
- **Calls:** `method214` repeatedly.
- **Called by:** `MapRegion.method175:673/695/743/765`, `MapRegion.method188:1160/1168/1185/1193` (i.e. every wall MapRegion decodes).
- **Notes:** Each branch sets *two* bits — one on the wall tile, one mirrored on the neighbouring tile (e.g. rot 0, `l==0`: `0x80` (E) on `(k,i)` and `0x08` (W) on `(k-1,i)`). This is what lets the pathfinder check passability by inspecting only the destination tile.

#### `void method212(boolean flag, int i, int j, int k, int l, int i1, int j1)` — `:212`

- **Purpose:** Stamp a solid-object rectangle. Marks every tile inside the object's `j x k` footprint (swapped to `k x j` for rotations 1/3) with bit `0x100` (object), and if `flag` is true also bit `0x20000` (bridge/plane-shift).
- **Params:**
  - `flag` = "blocks projectiles / bridge-deck" toggle (from `ObjectDefinition.aBoolean757`).
  - `i` = obfuscator parameter; method does `i = 14 / i;` (`:218`) — throws if zero, otherwise discards.
  - `j, k` = footprint width/length; `j1` = rotation (1/3 swap axes).
  - `l, i1` = world X / Y of the object's NW corner.
- **Calls:** `method214` for each tile.
- **Called by:** `MapRegion.method175:611/624/773`, `MapRegion.method188:1144/1152/1201`.

#### `void method213(int i, int j, int k)` — `:237`

- **Purpose:** Mark a single tile as "hard blocked" by OR-ing bit `0x200000`. Used for ground-decoration objects whose model is solid (e.g. a chest occupying the tile).
- **Params:** `k` = world X, `i` = world Y, `j` = obfuscator parameter (flips dead `aBoolean282` if nonzero).
- **Called by:** `MapRegion.method171:102`, `MapRegion.method175:571`, `MapRegion.method188:1121`.

#### `private void method214(int i, int j, int k)` — `:246`

- **Purpose:** Raw OR helper — `anIntArrayArray294[i][j] |= k;`. Internal building block for `method211/212/213`.

#### `void method215(int i, int j, boolean flag, boolean flag1, int k, int l)` — `:251`

- **Purpose:** *Remove* a wall — mirror of `method211`. Uses `method217` (AND with the inverted mask) at every cell that `method211` would have OR'd. Called when a temporary scene object is despawned. Early-returns unless `flag1` is true (`:255`).
- **Params:** `i` = rotation, `j` = shape, `k` = world X, `l` = world Y, `flag` = also clear projectile layer.
- **Called by:** `GameClientCore.java:9295` (during spawn-stream playback for temporary wall objects).

#### `void method216(int i, int j, int k, int l, byte byte0, int i1, boolean flag)` — `:408`

- **Purpose:** *Remove* a solid-object rectangle — mirror of `method212`. ANDs out bits `0x100` (and `0x20000` if `flag`).
- **Params:** `i` = rotation (1/3 swap), `j, i1` = footprint, `k, l` = world X/Y, `byte0` = obfuscator parameter (flips `aBoolean282` if `!= 6`), `flag` = bridge-deck object.
- **Calls:** `method217`.
- **Called by:** `GameClientCore.java:9306`.

#### `private void method217(int i, int j, int k, int l)` — `:434`

- **Purpose:** Raw AND-clear helper — `anIntArrayArray294[j][k] &= 0xffffff - i;`. `l` is obfuscator-only (`l = 36 / l`).

#### `void method218(int i, int j, int k)` — `:440`

- **Purpose:** Clear the hard-blocked bit `0x200000` from a single tile (`& 0xdfffff`). Companion of `method213`.
- **Params:** `k, j` = world X/Y, `i` = obfuscator parameter (`i = 6 / i`).
- **Called by:** `GameClientCore.java:9313`.

#### `boolean method219(int i, int j, int k, int l, int i1, int j1, int k1)` — `:448`

- **Purpose:** "Can entity at `(i, k1)` step onto the **target rectangle** at `(j, k)` given that the target's shape is `j1` and rotation is `i1`?" Used by `RoutePlanner.route` for early-out when the BFS frontier lands beside an interactable object (door, gate, picnic bench). Returns `true` for tiles immediately adjacent to the target on whichever side the rotation says is the "approach face".
- **Params:**
  - `i` = target world X, `k1` = target world Y (turned into local `i, k1`).
  - `j, k` = current frontier tile world coords (turned into local).
  - `l` = obfuscator parameter; flips `aBoolean282` if nonzero.
  - `j1` = target object **shape category**: `0` = wall-style (per-rotation single approach face), `2` = corner wall (two adjacent approach faces), `9` = generic clickable (any of four cardinal neighbours).
  - `i1` = target rotation 0..3.
- **Returns:** `true` if `(j, k)` is the same as or directly adjacent to `(i, k1)` and the wall bit on the intervening face is clear.
- **Notes:** The masks used (`0x1280120`, `0x1280102`, `0x1280108`, `0x1280180`, and bare `0x20`, `2`, `8`, `0x80` for the `j1 == 9` case) are the cardinal step masks. The `j1 == 9` branch only checks the wall bit on the target tile itself (not the empty/blocked sentinels) — i.e. you can approach a generic clickable through any non-walled face even from an "empty" tile.

#### `boolean method220(int i, int j, int k, int l, int i1, int j1, int k1)` — `:554`

- **Purpose:** Same idea as `method219` but for object **shapes 6, 7 and 8** — diagonal-wall decorations and inset wall items. Shape 6 = "wall item on a straight wall"; shape 7 = same but rotated 180° (`i1 = (i1 + 2) & 3`); shape 8 = "inside corner" — passable from any of the four cardinal neighbours that lack the appropriate wall bit.
- **Params:**
  - `i` = target world X, `j` = target world Y; `j1, k` = frontier world X/Y (turned into local).
  - `l` = target shape (6, 7, or 8 — others return false).
  - `i1` = rotation.
  - `k1` = obfuscator parameter; throws `NullPointerException` unless `k1 == 0` (`:556`).
- **Returns:** `true` if the frontier tile can reach the wall-decoration via an unwalled face.

#### `boolean method221(byte byte0, int i, int j, int k, int l, int i1, int j1, int k1)` — `:611`

- **Purpose:** "Is `(k, k1)` either *inside* or *one step outside, in a non-walled direction* a rectangle of size `j1 x l` whose NW corner is at world `(j, i)`?" Used by `RoutePlanner.route` for the **rectangle-target** early-out (e.g. walking to a generic "use" target tile range rather than a wall-attached object). Mirrors how server-side path validation works for "approach a rectangle".
- **Params:**
  - `byte0` = obfuscator parameter; throws `NullPointerException` unless `1` (`:614`).
  - `j, i` = rectangle NW corner world X/Y; `j1, l` = rectangle size in X/Y.
  - `k, k1` = frontier world X/Y.
  - `i1` = "approach-side mask" passed through from the caller (`RoutePlanner.route` param `k1`) — bit 1 = approach from south allowed, 2 = from west, 4 = from north, 8 = from east. The method allows the step when the corresponding bit is *clear*.
- **Returns:** `true` if inside or legally adjacent. Uses bits `0x08, 0x80, 0x02, 0x20` on the *frontier* tile to check the relevant wall face.

## RoutePlanner.java

### Overview

- **Class:** `final class RoutePlanner` (package-private, single static `route` method).
- **Line count:** 235 lines.
- **Purpose:** Run a wraparound-queue BFS on a single `CollisionMap` plane to find a step path from `(j2, j1)` (start tile, local coords) to either `(k2, i2)` (point target) or an object/rectangle target described by the shape/rotation/size params, then write the resulting `<= 25`-step path into the outbound `PacketBuffer` as a server walk packet. Treats the `int[104][104]` blocking grid as a passability source; never mutates it.
- **State:** All state hoisted to parameters (this was likely an instance method in the original — `GameClientCore.method85` is the thin caller that holds the queue/cost arrays and routeState in instance fields).

### The BFS queue + visited arrays

Allocated and owned by `GameClientCore` (see `:10234/10268/10430/10431`), passed in as parameters:

| Param | GameClientCore source | Shape | Purpose |
|---|---|---|---|
| `pathDirection` | `anIntArrayArray901 = new int[104][104]` (`:10268`) | `int[104][104]` | "Parent-step direction": for each tile, the bit-encoded direction taken to arrive (`1 = +Y`, `2 = +X`, `4 = -Y`, `8 = -X`, `3 = +X+Y`, `6 = -X+Y`, `9 = +X-Y`, `12 = -X-Y`, sentinel `99` at start). Doubles as the visited flag — non-zero means visited. Reset to `0` at the top of every `route` call (`:20`). |
| `pathCost` | `anIntArrayArray825 = new int[104][104]` (`:10234`) | `int[104][104]` | BFS distance (Manhattan-like since diagonals count as 1 step). Reset to `0x5f5e0ff` (= 99,999,999) per call (`:21`). |
| `pathXQueue` / `pathYQueue` | `anIntArray1280 / anIntArray1281 = new int[4000]` (`:10430/10431`) | `int[4000]` | Circular queue of frontier tiles. Head pointer `l3`, tail pointer `i4`, modulo `j4 = pathXQueue.length` (`:35, 41, 70…`). 4000 entries comfortably exceed `104*104 = 10816`/3 simultaneous frontiers in practice. |

The BFS visits each tile at most once (`pathDirection[..] == 0` check before enqueue at `:66, 74, 82, 90, 98, 106, 114, 122`).

### The "next step" output buffer

After the search, the `route` method walks the parent-direction array backward from the matched destination tile, collapsing consecutive identical steps into one "waypoint" by writing only **direction-change** tiles back into `pathXQueue/pathYQueue` (`:161-187`). The truncated waypoint list (up to 25 entries; `k4 = min(i4, 25)` at `:191-193`) is then serialised into the outbound `PacketBuffer` (`outboundBuffer`) as a server walk packet:

- Optional pre-packet `(opcode 36, len 0)` — "clear queued action" idle, emitted when the running count `routeState[1]` exceeds 92 walks (`:198-203`).
- Walk opcode by request type `i`: `i==0` -> opcode `164` (player walk-to), `i==1` -> opcode `248` (walk-to with running ack), `i==2` -> opcode `98` (interact-after-walk). Sizes are `2*k4 + 3` and `2*k4 + 3 + 14` respectively (`:204-218`).
- Body: anchor world tile `(k6 + anInt1034, i7 + anInt1035)` (`anInt1034/1035` are the region base offsets, set at `GameClientCore.java:9628/9629`), then `(dx, dy)` deltas per waypoint relative to the anchor (`:222-227`), then trailing byte = `1` if `inputState[5] == 1` else `0` (`:230`, used to flag run vs walk).

The `routeState` `int[4]` (`anInt1264, anInt1288, anInt1261, anInt1262` in `GameClientCore` — written back via `method85:6172-6175`) carries:
- `[0]` = "approximate path used" flag (set when the exact target was unreachable but a tile within distance 1 was reached, `:147`);
- `[1]` = rolling walk counter modulo 92 (`:198-203`);
- `[2]` / `[3]` = anchor tile of the most recently emitted walk (the head of `pathXQueue/pathYQueue`, `:220-221`) — used by `GameClientCore.java:2057` to suppress re-emission when the player already reached that anchor.

### Methods

#### `static boolean route(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, boolean flag, int k2, int anInt918, CollisionMap[] collisionMaps, int[][] pathDirection, int[][] pathCost, int[] pathXQueue, int[] pathYQueue, int anInt1034, int anInt1035, PacketBuffer outboundBuffer, int[] inputState, int[] routeState)` — `:10`

- **Purpose:** Run BFS + emit walk packet (described above).
- **Parameters (inferred from caller `GameClientCore.method85:6171` and the original obfuscated signature):**
  - `i` = walk-request type: `0` = ground walk (`method85` call at `:3059`), `1` = walk to NPC/item (`:6476`), `2` = walk to target actor/object (most callers).
  - `j` = "interaction op" — `0` for plain walk; forwarded to `method220` (op subtype). Purpose unclear at this layer; comes from caller arg.
  - `k` = rectangle target width (for `method221`); 0 if not a rectangle target.
  - `l` = obfuscator parameter; the dead `if(l != -11308) { for(int j6 = 1; j6 > 0; j6++); }` infinite loop at `:164-167` aborts the JVM if `l` is anything other than `-11308`. All callers pass `-11308` literally (`GameClientCore.java:3059, 3331, 3334, …`).
  - `i1` = `method219`/`method220` "shape category" + 1 (the code subtracts 1 before passing). Values `0` = point target only; `1..4` -> shapes 0..3 for `method219` (straight wall, corner, etc.); `5..9` -> shapes 5..8 for `method220` (wall decoration kinds); `10` -> a special wall-style approach (same as `1` via `(i1 < 5 || i1 == 10)`). When 0, no object early-out — only the literal tile match at `:42` succeeds.
  - `j1` = player size (1 for normal player; matters for rectangle entry — passed to `method219/220/221` as `j`/size).
  - `k1` = rectangle "approach-side mask" forwarded to `method221` as `i1`.
  - `l1` = target rotation 0..3 (forwarded to `method219/220`).
  - `i2` = target world Y; `k2` = target world X.
  - `j2, j1` = start tile local X / Y (in the BFS grid space already — the caller passes player local tile, not world).
  - `flag` = "accept approximate path" — when true and the exact target was unreachable, the method scans a 1-tile halo around the target for the cheapest reached tile (`:134-156`) and emits that path instead, flagging `routeState[0] = 1`.
  - `k2`, `anInt918` = collision map plane index (player's current plane).
  - `anInt1034, anInt1035` = region base offsets (added to local tile when emitting the walk packet).
  - `inputState` = `GameClientCore.super.anIntArray30` — keyboard / mouse state; only `inputState[5]` (CTRL for run) is read.
  - `routeState` = `[anInt1264, anInt1288, anInt1261, anInt1262]` round-tripped through `method85`.
- **Returns:** `true` if a walk packet was emitted, `false` otherwise. Special case at `:233`: when `i4 == 0` after backtracking (i.e. start tile *is* the destination), returns `i != 1` — i.e. type-1 walks return false ("no step emitted"), other types return true ("nothing to do — already there, considered satisfied").
- **Called by:** `GameClientCore.method85:6171` only.

### Route reconstruction

After the BFS exits (`flag1 == true`), `(j3, k3)` is the matched tile. The reconstruction loop at `:169-187` walks `pathDirection[j3][k3]` backward to `(j2, j1)`:

```
for j5 = l5 = pathDirection[j3][k3]; (j3,k3) != (j2,j1); j5 = pathDirection[j3][k3]:
    if j5 != l5:                  // direction changed -> record a waypoint
        l5 = j5
        pathXQueue[i4]   = j3
        pathYQueue[i4++] = k3
    if (j5 & 2) != 0: j3++        // came from +X -> step back +X (we walk *back* to start)
    elif (j5 & 8) != 0: j3--
    if (j5 & 1) != 0: k3++
    elif (j5 & 4) != 0: k3--
```

The four direction bits `1 = +Y, 2 = +X, 4 = -Y, 8 = -X` are independent so the cardinal codes (`1, 2, 4, 8`) test one each, and diagonal codes (`3 = 1|2`, `6 = 2|4`, `9 = 1|8`, `12 = 4|8`) trigger both axes naturally. Only direction-change tiles are buffered, so the resulting waypoint list is a *segment-endpoint* list rather than every intermediate tile — exactly what the 317 walk packet expects (head anchor + delta endpoints).

`pathXQueue[0]` ends up holding the **destination** (set at `:162`); the *last* slot written (`pathXQueue[i4-1]` after decrement at `:194`) holds the **first step from start** — used as the packet anchor `k6, i7`.

## How they interplay

`MapRegion.method171` (terrain + heights), `method175` (interior loc parsing), `method183` (loc-list parsing), and the static `method188` decode every wall, ground decoration, and interactive object from the `m{x}_{y}` / `l{x}_{y}` cache files and stamp them into the appropriate `CollisionMap[plane]` via the OR-style setters: walls -> `method211`, solid objects -> `method212`, hard-block tiles -> `method213`. Temporary scene mutations (a door opening, a furniture object spawning) call the AND-style `method215/216/218` removers.

At runtime, `GameClientCore.method85` (line `6165-6177`) is the universal "request a walk to X" entry point. It allocates a one-shot `routeState` int[4] from its instance fields and calls `RoutePlanner.route` with the current player's local tile (`j2, j1`), the destination world tile (`k2, i2`) and a description of the target's shape/rotation/size. `RoutePlanner.route` does its BFS over `aClass11Array1230[anInt918].anIntArrayArray294` (the **current plane**'s collision grid — read directly, not via methods), backtracks via the `pathDirection` parent array to produce <=25 segment endpoints, and writes those to the outbound `PacketBuffer` (`aClass30_Sub2_Sub2_1192`) as the server walk packet. The player walk queue on the server consumes it on the next tick.

Bridge tiles are honoured indirectly: when MapRegion stamps a bridge-deck object it sets the `0x20000` bit via `method212(flag=true, …)`; the pathfinder's per-step masks all include that bit, so a tile carrying a bridge object on the *current* plane is impassable — but `MapRegion` simultaneously demotes the bridge's plane-1 tiles to plane 0 (`MapRegion.java:98-103, 560-562, 1014-1016, 1044`), so the player's effective plane index `anInt918` already points to the right grid.

## Cross-class call graph

```
MapRegion.method171  ──┐  (terrain + ground decorations: hard-block)
MapRegion.method175  ──┼──► CollisionMap.method213     (set 0x200000)
MapRegion.method183  ──┘
MapRegion.method175  ──┐
MapRegion.method188  ──┼──► CollisionMap.method211     (stamp walls)
                       │    CollisionMap.method212     (stamp solid objects)
                       └──► CollisionMap.method213

GameClientCore.<scene rebuild> ────► CollisionMap.method210  (reset grid)
  GameClientCore.java:480, 1973

GameClientCore.<scene-object spawn stream playback> (line 9295/9306/9313)
   ├──► CollisionMap.method215  (clear wall)
   ├──► CollisionMap.method216  (clear solid object)
   └──► CollisionMap.method218  (clear hard-block)

GameClientCore.<random ground-item drift> (line 756-768)
   └──► direct read of aClass11Array1230[plane].anIntArrayArray294

GameClientCore.method85  ────► RoutePlanner.route
                                  ├──► CollisionMap.method219  (point/wall target adjacency)
                                  ├──► CollisionMap.method220  (decoration target adjacency)
                                  ├──► CollisionMap.method221  (rectangle-target adjacency)
                                  └──► direct mask reads on anIntArrayArray294
                                       (cardinal & diagonal step tests)
                              └──► PacketBuffer.method397/398/431/433/424
                                       (emit walk packet to outbound stream)

Callers of GameClientCore.method85 (incomplete, see grep):
  :3059   ground walk request (i=0)
  :3331/:3334 walk-to-tile w/ object click (i=2)
  :3539   walk to other player (i=2)
  :3571/:3573 attack-step retries
  :4235/:4278/:4280 NPC interact / right-click walk
  :6476   "examine"-style walk (i=1)
  many more for follow / trade / interact actions
```

---

**Unresolved / inferred** (call out for the consumer of this doc):

- `0x80000` (bit 19) appears in every pathfinder composite mask but no setter in `CollisionMap.java` writes it. It is plausibly written elsewhere (likely from `MapRegion`'s `aByteArrayArrayArray149` "force-hidden" flag projected into the collision plane) — the writer was not located in this pass.
- Eight instance fields (`aBoolean282/284/286/287`, `anInt283/285`, `aByte288/289`) are pure obfuscator dummies — declared/initialised but never meaningfully read.
- `RoutePlanner.route` parameter `j` ("interaction op subtype") and `i1` ("shape+1") encoding above is inferred from the masks used; the exact enum on the caller side would need a deeper audit of `method85`'s 19 call sites.
- `routeState[0..3]` mapping to `anInt1264 / anInt1288 / anInt1261 / anInt1262` is verbatim from `method85` (`:6168-6175`), but the broader meaning of `anInt1288` (walk-counter modulo 92) is inferred from how the BFS uses it.
