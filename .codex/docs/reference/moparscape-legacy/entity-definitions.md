# Moparscape legacy entity definitions reference

This document reverse-engineers the three obfuscated entity definition classes shipped with the legacy Moparscape (Jad-decompiled, "317"-era) client:

- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ObjectDefinition.java` (557 lines)
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/NpcDefinition.java` (372 lines)
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ItemDefinition.java` (669 lines)

Each class parses a single cache index (`loc.dat`/`loc.idx`, `npc.dat`/`npc.idx`, `obj.dat`/`obj.idx`) into per-entity definition objects and exposes a small set of "build a `Model`" methods that callers (MapRegion, SceneGraph, Player, Npc, Widget, GameClientCore) invoke at render time. They are used as the source of truth by the legacy decoder; the modern `rs-content` decoders should reproduce their opcode tables and field semantics byte-for-byte.

All method/field "inferred name" labels below are best-guess interpretations derived from caller context, opcode layout, and well-known RuneScape 317 cache schemas. Where a meaning is not certain, the entry is marked **purpose unclear**.

---

## 1. `ObjectDefinition`

### Overview

A definition of a scenery/locatable ("loc") object — walls, decorations, floor-deco, ground-deco, etc. The class is `final` with package-private visibility (file:10).

- **Cache files:** `loc.dat` (definitions) + `loc.idx` (per-id length table) loaded by `method576` from the config `Archive` (file:90-107). `loc.idx` is read entirely into `anIntArray755` as a prefix-sum of byte offsets into the shared `PacketBuffer` `aClass30_Sub2_Sub2_753`.
- **Instantiation/caching:** A 20-slot ring of pre-allocated `ObjectDefinition` instances (`aClass46Array782`, file:552) is recycled by `method572(int id)` (file:13-26). On miss, the next slot is overwritten via `method573` reset + `method582` parse. The slot's `anInt754` field stores the id, used as a cache key. Two `NodeCache`s sit on top: `aClass12_785` (sub-model cache, capacity 500) and `aClass12_780` (composed/recoloured/scaled model cache, capacity 30).
- **External callers:** `BootstrapConfigLoader` (init), `MapRegion` (~30 sites, builds scenery models for the world), `SceneGraph`/`GameClientCore` (lookups during render/menu/interaction), `DynamicObject.method49`.

### Static fields

| Legacy name | Inferred name | Shape / init | Purpose | Callers |
|---|---|---|---|---|
| `aClass30_Sub2_Sub4_Sub6Array741` | `compositeBuffer` | `Model[4]` (file:511) | Scratch array reused inside `method581` when stitching multi-mesh objects. | `method581` |
| `aBoolean752` | `loadingMembersOnlyCache` (purpose unclear — likely a re-parse flag toggled while reloading definitions; checked at file:319 and 344 to allow re-overwriting model arrays) | `boolean` (file:522, public static) | When `true`, opcode 1/5 always overwrites `anIntArray773`/`anIntArray776`; when `false`, the second occurrence is skipped. Toggled by `GameClientCore` lines 2494 and 9044 around region rebuild. | `GameClientCore.java:2494,9044`, `method582` |
| `aClass30_Sub2_Sub2_753` | `definitionBuffer` | `PacketBuffer` (file:523) | Shared cache buffer for `loc.dat`. | `method572`, `method576`, `method575` |
| `anIntArray755` | `definitionOffsets` | `int[anInt756]` | Prefix-sum offsets into `definitionBuffer` for each loc id. | `method572`, `method576`, `method575` |
| `anInt756` | `definitionCount` | `int` (file:526) | Total number of object definitions in the cache. | `method576` |
| `aClient765` | `clientRef` | `GameClient` (file:535, public static) | Back-reference to the running client; used by `method580` to look up varp/varbit state. | `method580` |
| `anInt771` | `ringCursor` | `int` (file:541) | Round-robin index into `aClass46Array782`. | `method572` |
| `aClass12_780` | `composedModelCache` | `NodeCache(false, 30)` (file:550, public) | LRU cache keyed by `((id<<6) + variant + orientation, plane+1)` long key — caches the fully-built `Model` returned from `method581`. | `method581`, `GameClientCore.java:638-690` (flush) |
| `aClass46Array782` | `definitionRing` | `ObjectDefinition[20]` (file:552) | Ring of pre-parsed definition slots. | `method572`, `method576`, `method575` |
| `aClass12_785` | `rawModelCache` | `NodeCache(false, 500)` (file:555, public) | LRU cache keyed by raw model id (with bit 16 set if mirrored) for `Model.method462` results. | `method581`, `GameClientCore.java:638,689` (flush) |

### Instance fields

Grouped by role.

**Identity / parse housekeeping** (file:524, 533, 540, 549)
- `anInt754` — definition id (cache key, init `-1`).
- `anInt743` — init `9`, written to `422` by `method581` on the i!=0 branch (file:297); **purpose unclear**, looks like a leftover scaling/light value used by `method478`. Set to `9` in ctor.
- `aBoolean763` — toggled to obfuscate dead branches (`method574`, `method577`); no semantic value.
- `aBoolean779` — "is rendered" / "blocks projectiles" flag; defaults `true`, cleared by opcode 64 (file:425). Read by `MapRegion:592` to decide if a secondary masking model is needed.
- `anInt770` — model file index / pool id used when calling `Model.method462(anInt770, modelId)` (file:217, 254). Defaults `9` (= the standard model pool).

**Model & animation** (file:543-554)
- `anIntArray773` — sub-model id list ("models" opcode 1/5).
- `anIntArray776` — per-sub-model placement-type filter (opcode 1 only). If `null`, sub-models apply to all placement types but only type `10` is rendered (file:117, 199).
- `anInt781` — animation/sequence id (opcode 24). `-1` means static. Used by `MapRegion` to construct `DynamicObject` instead of static `Model`.
- `anIntArray784` / `anIntArray747` — recolour source / destination palette indices (opcode 40).
- `aBoolean751` — mirrored ("flipped") flag (opcode 62); XOR'd with `(orientation > 3)` in `method581` to decide whether to call `Model.method477(0)` (mirror).

**Sizing / footprint** (file:514, 531)
- `anInt744` — x-size in tiles (opcode 14, default `1`).
- `anInt761` — y-size in tiles (opcode 15, default `1`).

**Scaling / translation** (file:510, 518, 542, 553-554)
- `anInt748`, `anInt772`, `anInt740` — model scale on X/Z/Y axes (opcodes 65/66/67, default `128` = identity).
- `anInt738`, `anInt745`, `anInt783` — model translation on X/Y/Z (opcodes 70/71/72, default `0`). Note `anInt745` is read via `method411` (signed 16-bit).

**Lighting / orientation tweaks** (file:507, 512)
- `aByte737` — diffuse-light delta (opcode 29). Added to `64` in `method479` (file:292).
- `aByte742` — specular/contrast delta (opcode 39). Multiplied by 5 and added to `768`.

**Interaction / menu** (file:509, 527, 547, 556)
- `aString739` — display name (opcode 2).
- `aByteArray777` — description bytes (opcode 3).
- `aBoolean757` — `walkable`/blocks-line-of-sight (opcode 18 → `false`, default `true`). Read by `MapRegion` collision logic.
- `aStringArray786` — 5 right-click action strings (opcodes 30-38, "hidden" → `null`).
- `aBoolean767` — solid/clipping flag (opcode 17 → `false`, default `true`).
- `aBoolean778` — adjust-render flag (opcode 19 sets to `true` when value `==1`; also defaulted post-parse if any model + null-or-`10` placement type or any actions). **Purpose:** "render as model on minimap and in chat icons"-style decoration flag.
- `aBoolean762` — heightmap-deform flag (opcode 21). Triggers per-vertex height interpolation in `method578` (file:140-153).
- `aBoolean764` — `clipping_unknown` (opcode 23) — **purpose unclear**; never read elsewhere in this class but exposed publicly.
- `aBoolean769` — "share-vertices/shadowed?" flag (opcode 22). Influences `Model` constructor at file:138 and disables backface culling at file:292 (`!aBoolean769`).
- `aBoolean736` — opcode 73 marker (**purpose unclear**, exposed publicly).
- `aBoolean766` — "inverted clip" (opcode 74); when true, forces both `aBoolean767` and `aBoolean757` to `false`. Acts as a "no clipping at all" override.
- `anInt760` — minimap function (opcode 75). After parse: `-1` → derived from `aBoolean767` (1 if solid, else 0). Triggers a special "fixed depth" branch at file:294.

**Misc** (file:516, 519, 528-538, 544-551)
- `anInt746` — varp/icon id (opcode 60). Read by `GameClientCore:747` as `class46.anInt746` (perhaps minimap icon id).
- `anInt758` — opcode 68 (**purpose unclear**, exposed publicly).
- `anInt768` — opcode 69 (**purpose unclear**, exposed publicly).
- `anInt775` — ambient-light delta (opcode 28, default `16`). Used inside engine `Model.method479` chain implicitly via `aByte737`/`aByte742`.
- `anInt749` — varp id for child-id resolution (opcode 77, second short).
- `anInt774` — varbit id for child-id resolution (opcode 77, first short).
- `anIntArray759` — child-id array (opcode 77 tail). Used by `method580` to forward to a sub-definition based on varp/varbit value.
- `anInt750` — set to `217` when `method582(false, ...)` is invoked (file:304); else uninitialised. **Purpose unclear** — dead obfuscation parameter.

### Methods

#### `public static final ObjectDefinition method572(int id)` (file:13)
**Inferred name:** `forId`. Returns a cached `ObjectDefinition` for the given loc id. Linear-scans the 20-slot ring; on miss, advances `anInt771`, seeks the shared buffer to `anIntArray755[id]`, resets the slot with `method573`, and parses via `method582(true, ...)`.
- **Params:** `id` — loc definition id.
- **Returns:** the (cached or freshly-parsed) `ObjectDefinition` (always non-null, but mutating state).
- **Callers:** `GameClientCore` (lines 747, 2258, 2349, 2386, 3316, 4183, 4451, 8902, 9293, 9302, 9311), `MapRegion`, `DynamicObject` (indirectly via held ref), `method580` (self-recursive for child id).
- **Calls:** `method573`, `method582`.
- **Notes:** Not thread-safe; ring entries are mutated in place — long-held references will silently change identity when evicted.

#### `private final void method573()` (file:28)
**Inferred name:** `resetToDefaults`. Re-initialises every parsed field to the default value before re-using a ring slot.
- **Params/Returns:** none.
- **Callers:** `method572`.
- **Notes:** Defaults critical for opcode-absent fields: scales `128`, sizes `1`, all flags neutral, ambient `16`, child-id table `null`.

#### `public final void method574(OnDemandFetcher fetcher, int unused)` (file:68)
**Inferred name:** `requestModels`. Walks `anIntArray773` and asks the on-demand fetcher to download each sub-model (priority `0`, not-urgent).
- **Params:** `class42_sub1` — the `OnDemandFetcher`. `i` — obfuscation-only; loop at file:75-76 toggles `aBoolean763` to consume it.
- **Returns:** void.
- **Callers:** `MapRegion.java:501` (preloads region scenery models).
- **Calls:** `OnDemandFetcher.method560`.

#### `public static final void method575(int i)` (file:79)
**Inferred name:** `unload`. Clears all static caches and ring (called on client logout/shutdown).
- **Params:** `i` — obfuscation gate (loop at file:85 swallows it).
- **Callers:** `GameClientCore.java:4743`.
- **Notes:** Drops `aClass12_785`, `aClass12_780`, `anIntArray755`, `aClass46Array782`, `aClass30_Sub2_Sub2_753`.

#### `public static final void method576(Archive class44)` (file:90)
**Inferred name:** `load`. Initial loader. Pulls `loc.dat` / `loc.idx` from the title `Archive`, builds the offset table, allocates the 20-slot ring.
- **Callers:** `BootstrapConfigLoader.java:19`.
- **Calls:** `Archive.method571`, `PacketBuffer.method410`.

#### `public final boolean method577(int placementType, boolean flag)` (file:109)
**Inferred name:** `areModelsAvailable`. Returns whether the sub-models needed for the given placement type have been downloaded.
- **Params:** `i` — placement type to test. `flag` — obfuscation guard (`!flag` throws NPE — always called with `true`).
- **Returns:** `true` if models are ready (or no models needed).
- **Callers:** `MapRegion.java:875` (via wrapper).
- **Calls:** `Model.method463`.
- **Notes:** Two paths: filtered (`anIntArray776` non-null → only the model that matches) vs unfiltered (only placement type `10` is rendered when `anIntArray776 == null`).

#### `public final Model method578(int placementType, int orientation, int swCornerY, int seCornerY, int neCornerY, int nwCornerY, int unused)` (file:132)
**Inferred name:** `getModelAtTile`. Builds the `Model` for an object at a specific tile, optionally height-deformed to match the four tile corners.
- **Params:** `i` placement type, `j` orientation (0-3, 4-7 for mirrored), `k`/`l`/`i1`/`j1` four corner y heights (SW/SE/NE/NW), `k1` unused.
- **Returns:** `Model` (already wrapped/recoloured/animated) or `null` if not yet loaded.
- **Callers:** `MapRegion` (~22 call sites for every placement type), `DynamicObject.java:49`, `GameClientCore.java:8907`.
- **Calls:** `method581`, `Model` ctor, `Model.method467`.
- **Notes:** When `aBoolean762` (height-warp) is true, walks every vertex and bilinearly interpolates a tile-corner height delta into its Y coord, then re-runs lighting via `method467(false)`.

#### `public final boolean method579(boolean flag)` (file:157)
**Inferred name:** `areAllModelsAvailable`. Lighter version of `method577` that ignores placement type.
- **Params:** `flag` — obfuscation NPE-guard.
- **Returns:** all sub-models downloaded.
- **Callers:** `MapRegion.java:1288`.

#### `public final ObjectDefinition method580(boolean flag)` (file:171)
**Inferred name:** `resolveChild`. If the definition has a child table (`anIntArray759`), reads the controlling varbit (`anInt774`) or varp (`anInt749`) and returns `method572(anIntArray759[value])`.
- **Params:** `flag` — obfuscation NPE-guard.
- **Returns:** child `ObjectDefinition` or `null` if the resolved index is out of range / `-1`.
- **Callers:** `GameClientCore.java:4453`.
- **Calls:** `method572`, `VarBitDefinition.aClass37Array646`, `GameClient.anIntArray1232` (mask table).

#### `private final Model method581(int variant, int placementType, int orientation, int unused)` (file:193)
**Inferred name:** `buildModel`. Core mesh builder. Resolves sub-models, applies mirroring, then composes scaling / translation / rotation / lighting on top, caches the result.
- **Params:** `i` first cache-control flag (sets `anInt743 = 422` if non-zero — obfuscation leftover); `j` placement type to render; `k` orientation (sequence frame); `l` mirror count (also 0-3 rotations).
- **Returns:** `Model` (cached in `aClass12_780`) or `null`.
- **Notes:** Cache key encodes `(id<<6 | (filterIdx<<3) | l)` in low 32 bits and `(k+1)` in high 32 bits. Raw sub-models cached separately in `aClass12_785` with key = modelId, with bit 16 toggled for mirrored variants. When multiple sub-models, builds composite via `new Model(count, array, -38)`. The wrapper `Model` ctor at file:272 chooses whether to share vertex tables based on whether further mutation is needed.

#### `private final void method582(boolean flag, PacketBuffer buffer)` (file:301) — **opcode parser**
**Inferred name:** `decode`. Reads opcode bytes until terminator `0`. Each opcode reads a typed payload and writes a single field.
- **Params:** `flag` — obfuscation gate; `class30_sub2_sub2` — the seeked cache buffer.

**Opcode table:**

| Op | Bytes read | Field(s) | Notes |
|----|-----------|----------|-------|
| `0` | — | (terminator) | Exits loop; runs post-parse fix-ups. |
| `1` | u8 N, then N×(u16 modelId + u8 placementType) | `anIntArray773`, `anIntArray776` | Skipped if models already loaded and `aBoolean752==false`. |
| `2` | RuneString | `aString739` | Display name. |
| `3` | RuneString-as-bytes | `aByteArray777` | Description. |
| `5` | u8 N, then N×u16 | `anIntArray773` (only) | Models with no placement-type filter (`anIntArray776=null`). |
| `14` | u8 | `anInt744` | Size X tiles. |
| `15` | u8 | `anInt761` | Size Y tiles. |
| `17` | — | `aBoolean767=false` | Walkable/non-solid. |
| `18` | — | `aBoolean757=false` | Blocks projectiles cleared. |
| `19` | u8 (`i`) | `aBoolean778=true` iff value 1 | Has actions hint. |
| `21` | — | `aBoolean762=true` | Height-deform. |
| `22` | — | `aBoolean769=true` | Shared vertices / no-cull. |
| `23` | — | `aBoolean764=true` | Unknown flag. |
| `24` | u16 | `anInt781` (`65535→-1`) | Sequence/animation id. |
| `28` | u8 | `anInt775` | Ambient delta. |
| `29` | i8 | `aByte737` | Diffuse delta. |
| `30-38` | RuneString (× up to 9 actions, first 5 stored) | `aStringArray786[i-30]` | "hidden" → `null`. |
| `39` | i8 | `aByte742` | Specular/contrast delta. |
| `40` | u8 N, then N×(u16 src + u16 dst) | `anIntArray784`, `anIntArray747` | Recolour palette. |
| `60` | u16 | `anInt746` | Minimap icon id (`MapRegion` reads). |
| `62` | — | `aBoolean751=true` | Mirrored. |
| `64` | — | `aBoolean779=false` | Skip secondary occluder model. |
| `65` | u16 | `anInt748` | Scale X. |
| `66` | u16 | `anInt772` | Scale Z (legacy "height scale"). |
| `67` | u16 | `anInt740` | Scale Y. |
| `68` | u16 | `anInt758` | Unknown. |
| `69` | u8 | `anInt768` | Unknown. |
| `70` | i16 (`method411`) | `anInt738` | Translate X. |
| `71` | i16 | `anInt745` | Translate Y. |
| `72` | i16 | `anInt783` | Translate Z. |
| `73` | — | `aBoolean736=true` | Unknown. |
| `74` | — | `aBoolean766=true` | Force non-clipping. |
| `75` | u8 | `anInt760` | Minimap rendering function. |
| `77` | u16 varbit + u16 varp + u8 N + (N+1)×u16 child ids | `anInt774`, `anInt749`, `anIntArray759` | Multi-state child table. |

**Post-parse fix-ups:** if opcode 19 was absent, `aBoolean778` is set to true if any model exists with placement type `10` or actions are defined (file:482-487). If opcode 74 was hit (`aBoolean766`), `aBoolean767` and `aBoolean757` are forced `false`. If `anInt760==-1`, it defaults to `1` when `aBoolean767`, else `0`.

#### `ObjectDefinition()` (file:498) — ctor
Initialises minimal sentinel fields (`anInt743=9`, `anInt754=-1`, `aBoolean763=true`, `anInt770=9`). All other defaults are applied by `method573`.

---

## 2. `NpcDefinition`

### Overview

A definition of an NPC type — name, combat level, models, mounted animations, child-id table. `final`, package-private (file:10).

- **Cache files:** `npc.dat` + `npc.idx`, loaded by `method162` (file:88) from the config `Archive`.
- **Instantiation/caching:** 20-slot ring `aClass5Array80` (file:355), round-robin via `anInt56`. On every miss `method159` **allocates a fresh `NpcDefinition`** (unlike `ObjectDefinition`, which reuses) — see file:20. The id is stored as `aLong78` (long, presumably to widen later). One `NodeCache` `aClass12_95` (capacity 30) caches the pre-animation composed `Model` produced by `method164`.
- **External callers:** `Player.java:114,190,314,370` (player NPC-morph), `Npc.java:25,30` (npc render), `NpcUpdateMaskHandler.java:123` (transform mask), `Widget.java:263` (interface NPC preview), `GameClientCore` (multiple), `EntityMenuBuilder.java:15` (right-click).

### Static fields

| Legacy | Inferred | Init | Purpose | Callers |
|---|---|---|---|---|
| `anInt56` | `ringCursor` | `int` (file:331) | Ring round-robin index. | `method159` |
| `aClass30_Sub2_Sub2_60` | `definitionBuffer` | `PacketBuffer` (file:335) | Shared `npc.dat` buffer. | `method159`, `method162`, `method163` |
| `anInt62` | `definitionCount` | public static (file:337) | Number of npc definitions. | `method162` |
| `anIntArray72` | `definitionOffsets` | `int[anInt62]` (file:347) | Prefix-sum offsets. | `method159`, `method162` |
| `anInt74` | `loadingFlag` | `int = 748` (file:349) | Set to `60` in `method163` if `i>=0`. **Purpose unclear** (likely dead obfuscation parameter). | `method163` |
| `aClass5Array80` | `definitionRing` | `NpcDefinition[20]` (file:355) | Ring. | `method159`, `method162`, `method163` |
| `aClient82` | `clientRef` | `GameClient` (file:357, public static) | Provides varp/varbit lookup in `method161`. | `method161` |
| `aClass12_95` | `composedModelCache` | `NodeCache(false, 30)` (file:370, public) | LRU cache keyed on `aLong78` for the composed (non-animated) `Model` produced inside `method164`. | `method164`, `GameClientCore.java:691` (flush) |

### Instance fields

**Identity** (file:330-353)
- `aLong78` — definition id (cached as `long`, init `-1L`).

**Child-id table** (file:332, 334, 363)
- `anInt57` — varbit id (opcode 106 first short, `65535→-1`).
- `anInt59` — varp id (opcode 106 second short).
- `anIntArray88` — child-id array (opcode 106 tail).
- `anInt64` — local-player combat level used when no varbit/varp set; defaults `1834` in ctor. **Purpose unclear** in spec but used as `method161` argument by `Player.aClass5_1698.method164(0, -1, ...)` — really it is just the parameter passed by `Player`/`Npc`/`GameClientCore` to identify which child to pick.

**Models & animation** (file:344-369)
- `anIntArray94` — body model ids (opcode 1). Used by the "in-world" `method164` path.
- `anIntArray73` — alternative model ids (opcode 60). Used by the "interface preview" `method160` path.
- `anIntArray76` / `anIntArray70` — recolour src/dst (opcode 40).
- `anInt69` — model pool id (defaults `9`); never written by an opcode — fixed.
- `anInt75` — opcode 102 (**purpose unclear**; likely "render priority" or "scenery override").
- `anInt77` — opcode 13 (**purpose unclear**; an animation id?).
- `anInt67` — opcode 14 and opcode 17 first short (idle/stand animation).
- `anInt58`, `anInt83`, `anInt55` — opcode 17 remaining shorts (**purpose unclear**; likely turn / walk-side animations on a transformed NPC).
- `anInt63` — init `9` (ctor), no opcode writes — passed as a `Model.method478` parameter at file:167. **Purpose unclear** ("scale flag for axis swap").

**Sizing / scale** (file:361, 365-367)
- `anInt86` — size scale X (opcode 98, default `128`).
- `anInt91` — size scale Y (opcode 97, default `128`).
- `anInt90` — opcode 92 (**purpose unclear** — possibly head icon overlay).
- `anInt96` — opcode 90 (**purpose unclear** — possibly drawing-priority).
- `anInt71` — opcode 91 (**purpose unclear** — likely another sprite/icon id).
- `anInt61` — opcode 95 (**purpose unclear** — possibly combat-level or hit-icon).

**Lighting** (file:360, 367)
- `anInt85` — diffuse delta (opcode 100, signed). Added to `64`.
- `anInt92` — specular delta (opcode 101, signed × 5). Added to `850`.

**Flags / interaction** (file:343, 358, 362, 359, 368)
- `aByte68` — head-icon / priority byte (opcode 12, default `1`). When `1`, sets `aBoolean1659=true` on the produced model at file:171.
- `aBoolean84` — "visible on minimap" (opcode 107 → `false`, default `true`).
- `aBoolean87` — "clickable" (opcode 93 → `false`, default `true`).
- `aBoolean93` — opcode 99 marker (**purpose unclear**).
- `aBoolean81` — pure obfuscation toggle.

**Strings** (file:340-341, 364)
- `aString65` — display name (opcode 2).
- `aStringArray66` — 5 action strings (opcodes 30-39; "hidden" → null).
- `aByteArray89` — description (opcode 3).
- `anInt79` — opcode never sets it; default `32` (ctor) — **purpose unclear** (likely "menu width" or "size in tiles", commonly `1` in stock cache). Note: no opcode writes to it; stays at `32` regardless of cache.

### Methods

#### `public static final NpcDefinition method159(int id)` (file:13)
**Inferred name:** `forId`. Linear-scans the ring for `aLong78 == id`; on miss, allocates a new instance into the next ring slot, seeks the buffer, calls `method165(true, buffer)`.
- **Returns:** the cached/freshly-parsed `NpcDefinition`.
- **Callers:** `Player.java:114`, `NpcUpdateMaskHandler.java:123`, `Widget.java:263`, `GameClientCore.java:2025`, self-call from `method161`.
- **Calls:** `method165`.
- **Notes:** Always allocates on miss → produces garbage; in contrast to `ObjectDefinition.method572` (which reuses slots).

#### `public final Model method160(boolean flag)` (file:27)
**Inferred name:** `buildInterfaceModel`. Builds the "preview / morphed" model used by widgets and player transforms. Resolves child-id first, then composes models from `anIntArray73` (note: not `anIntArray94`), applies recolour.
- **Params:** `flag` — obfuscation; if `!flag`, writes `anInt64=303` (dead).
- **Returns:** `Model` or `null` (if a model is still being streamed in).
- **Callers:** `Player.java:314`, `Widget.java:263`.
- **Calls:** `method161`, `Model.method462`, `Model.method463`, `Model.method476`.

#### `public final NpcDefinition method161(int combatLevel)` (file:66)
**Inferred name:** `resolveChild`. Reads varbit (`anInt57`) or varp (`anInt59`) and indexes into `anIntArray88` to find a child id.
- **Params:** `i` — semantically the "current local player combat level" or whatever the caller decides (`Npc` passes `anInt877`).
- **Returns:** child `NpcDefinition` or `null`.
- **Callers:** `GameClientCore.java:1346,3902,8175`, `EntityMenuBuilder.java:15`, self-call from `method160`, `method164`.
- **Calls:** `method159`, `VarBitDefinition.aClass37Array646`, `GameClient.anIntArray1232`.

#### `public static final void method162(Archive class44)` (file:88)
**Inferred name:** `load`. Loads `npc.dat`/`npc.idx` and allocates the 20-slot ring.
- **Callers:** `BootstrapConfigLoader.java:22`.

#### `public static final void method163(int i)` (file:107)
**Inferred name:** `unload`. Clears all static caches and ring.
- **Callers:** `GameClientCore.java:4744`.

#### `public final Model method164(int unused, int sequenceFrame, int idleFrame, int[] sequenceTransforms)` (file:117)
**Inferred name:** `buildAnimatedModel`. The in-world animated builder. Resolves child id first; if not cached, builds composite from `anIntArray94`, recolours, runs `method469` + `method479` lighting and caches into `aClass12_95`. Then takes the shared scratch model `Model.aClass30_Sub2_Sub4_Sub6_1621`, applies either a paired-frame transform (when both `j`/`k` valid) or a single-frame transform, plus size scaling, normal recompute, and the `aByte68==1` "scenery priority" flag.
- **Params:** `i` — obfuscation gate; `j` (sequence frame, can be `-1`), `k` (idle frame), `ai` (per-vertex weights / sequence transforms for `Model.method471`).
- **Returns:** `Model` ready to draw; **shared mutable instance** — caller must re-call before next frame.
- **Callers:** `Player.java:190`, `Npc.java:25,30`.
- **Calls:** `method161`, `Model.method462/463/464/466/469/470/471/476/478/479`, `AnimationFrame.method532`.

#### `private final void method165(boolean flag, PacketBuffer buffer)` (file:176) — **opcode parser**
**Inferred name:** `decode`.

| Op | Bytes | Field(s) | Notes |
|----|------|----------|-------|
| `0` | — | terminator | — |
| `1` | u8 N + N×u16 | `anIntArray94` | Primary model list. |
| `2` | RuneString | `aString65` | Name. |
| `3` | bytes | `aByteArray89` | Description. |
| `12` | i8 | `aByte68` | Head-icon / scenery flag. |
| `13` | u16 | `anInt77` | Unknown. |
| `14` | u16 | `anInt67` | Stand/idle animation id. |
| `17` | 4×u16 | `anInt67`, `anInt58`, `anInt83`, `anInt55` | Compound (idle + 3 turn anims). |
| `30-39` | RuneString | `aStringArray66[i-30]` | Actions; "hidden" → null. |
| `40` | u8 N + N×(u16 src + u16 dst) | `anIntArray76`, `anIntArray70` | Recolour palette. |
| `60` | u8 N + N×u16 | `anIntArray73` | Secondary model list (used by `method160`). |
| `90` | u16 | `anInt96` | Unknown. |
| `91` | u16 | `anInt71` | Unknown. |
| `92` | u16 | `anInt90` | Unknown. |
| `93` | — | `aBoolean87=false` | Not clickable. |
| `95` | u16 | `anInt61` | Combat level / hit-icon. |
| `97` | u16 | `anInt91` | Scale Y (default `128`). |
| `98` | u16 | `anInt86` | Scale X (default `128`). |
| `99` | — | `aBoolean93=true` | Unknown. |
| `100` | i8 | `anInt85` | Diffuse delta. |
| `101` | i8 ×5 | `anInt92` | Specular delta. |
| `102` | u16 | `anInt75` | Unknown. |
| `103` | u16 | `anInt79` | Note: writes default-`32` field. Unknown. |
| `106` | u16 varbit + u16 varp + u8 N + (N+1)×u16 child ids (`65535→-1`) | `anInt57`, `anInt59`, `anIntArray88` | Multi-state child table. |
| `107` | — | `aBoolean84=false` | Hide from minimap. |

#### `NpcDefinition()` (file:302) — ctor
Initialises every nullable sentinel to `-1` / default constant (`anInt63=9`, `anInt64=1834`, scales `128`, `aByte68=1`, `aBoolean84/87=true`, etc.). No call to `method197`-style reset since each instance is freshly allocated by `method159`.

---

## 3. `ItemDefinition`

### Overview

A definition of an inventory/world item (sometimes called `obj`). `final`, package-private (file:10).

- **Cache files:** `obj.dat` + `obj.idx`, loaded by `method193`.
- **Instantiation/caching:** A **10-slot** ring `aClass8Array172` (file:634), recycled via round-robin `anInt180`. Two `NodeCache`s: `aClass12_159` (composed `Model`, capacity 50) and `aClass12_158` (icon `Sprite`, capacity 100).
- **Note-pair behaviour:** Items use opcode 97 / 98 to define "note" linkage. `method199(byte 61)` patches a note-item from its target (`anInt179`/`anInt163`) and rewrites name/description.
- **External callers:** `Player.java` (worn-equipment rendering), `Widget.java:267` (inventory icons), `GroundItem.java:15-19` (ground pile mesh), `InterfacePacketHandler.java:33`, `NpcUpdateMaskHandler` (not direct), `WidgetRenderHandler.java:18,248` (icon sprites), many `GameClientCore` lookups.

### Static fields

| Legacy | Inferred | Init | Purpose | Callers |
|---|---|---|---|---|
| `aClass12_158` | `iconSpriteCache` | `NodeCache(false, 100)` (file:620) | LRU cache of 32×32 item icons. | `method200`, flushed by `GameClientCore.java:693,1247` and `method191`. |
| `aClass12_159` | `composedModelCache` | `NodeCache(false, 50)` (file:621, public) | LRU cache for in-world (ground / pile) models. | `method201`, flushed by `GameClientCore.java:692`, `method191`. |
| `aClass8Array172` | `definitionRing` | `ItemDefinition[10]` (file:634) | Ring. | `method198`, `method193`, `method191`. |
| `anInt180` | `ringCursor` | (file:642) | Round-robin. | `method198` |
| `aBoolean182` | `membersWorldFlag` | `boolean = true` (file:644, public static) | When `false`, members-only items have their description swapped to "Members Object" + "Login to a members' server…" placeholder. | `method198` |
| `aClass30_Sub2_Sub2_183` | `definitionBuffer` | `PacketBuffer` (file:645) | Shared `obj.dat` buffer. | `method193`, `method198`, `method191`. |
| `aBoolean187` | `unloadToggle` | `boolean` (file:649) | Obfuscation toggle in `method191`. | `method191` |
| `anIntArray195` | `definitionOffsets` | `int[anInt203]` (file:657) | Prefix-sum offsets. | `method193`, `method198`. |
| `anInt203` | `definitionCount` | public static (file:665) | Total item count. | `method193`, world clamps in `GameClientCore.java:8008,8035`. |

### Instance fields

**Identity / ground render** (file:619, 633)
- `anInt157` — id (cache key).
- `anInt171` — model pool id (default `9`).
- `anInt174` — primary model id (opcode 1).

**Ground-pile lighting / orientation** (file:629, 646, 658-664)
- `anInt167`, `anInt192`, `anInt191` — scale X/Z/Y (opcodes 110/111/112, default `128`).
- `anInt177` — init `9`, **purpose unclear** (passed into `Model.method478` at file:418, looks like axis-flag).
- `anInt196` — diffuse delta (opcode 113).
- `anInt184` — specular delta (opcode 114, ×5).
- `anInt202` — opcode 115 (**purpose unclear**; read by `Player.java:119` to bias something — likely "team / cape colour" or "ge tradable flag").

**Icon render** (file:617, 626-639, 643, 660-664)
- `anInt181` — icon zoom (opcode 4, default `2000`).
- `anInt190` — icon Y-rotation (opcode 5).
- `anInt198` — icon X-rotation (opcode 6).
- `anInt169` — icon X-translate (opcode 7; sign-extended from u16).
- `anInt194` — icon Y-translate (opcode 8; sign-extended).
- `anInt199` — opcode 10 (**purpose unclear**).
- `anInt204` — icon Z-rotation (opcode 95).
- `aBoolean176` — "stackable" (opcode 11 → `true`, also forced by `method199` for noted items). Also makes the rendered sprite width `33` (file:392).
- `anInt155` — item base value / shop price (opcode 12).
- `aBoolean161` — "members only" (opcode 16). Controls description replacement in `method198`.
- `aStringArray168` — 5 ground actions (opcodes 30-34; "hidden" → null).
- `aStringArray189` — 5 inventory actions (opcodes 35-39; no "hidden" replacement).

**Equipment models (worn by `Player`)** (file:624-668)
- `anInt165` / `anInt188` / `anInt185` — male equipment models (head/upper/lower? actually "primary/secondary/tertiary"). Opcodes 23, 24, 78.
- `anInt200` / `anInt164` / `anInt162` — female equipment models. Opcodes 25, 26, 79.
- `aByte205` — male equipment vertical offset (opcode 23 trailing byte).
- `aByte154` — female equipment vertical offset (opcode 25 trailing byte).
- `anInt175` / `anInt166` — male alternate-pose model + secondary (opcodes 90, 92).
- `anInt197` / `anInt173` — female alternate-pose model + secondary (opcodes 91, 93).
- `aBoolean186`, `aBoolean206` — obfuscation toggles.

**Recolouring** (file:618, 622)
- `anIntArray156` — recolour src palette (opcode 40 first short).
- `anIntArray160` — recolour dst palette (opcode 40 second short).

**Stack / count rendering** (file:655, 663)
- `anIntArray193` — stack-count → alternate-id table (opcodes 100-109, even slots).
- `anIntArray201` — stack-count thresholds (opcodes 100-109, odd slots).

**Note linkage** (file:625, 641)
- `anInt179` — base item id this note represents (opcode 97).
- `anInt163` — note template id (opcode 98). Triggers `method199` on definition load.

### Methods

#### `public static final void method191(int i)` (file:13)
**Inferred name:** `unload`. Drops both `NodeCache`s, the offset table, the ring, the buffer.
- **Callers:** `GameClientCore.java:4745`.

#### `public final boolean method192(int unused, int gender)` (file:24)
**Inferred name:** `isWieldModelAReady`. Returns whether the primary equipment models for the given gender (`0`=male using `anInt175`+`anInt166`, `1`=female using `anInt197`+`anInt173`) have been streamed in. Note: tests **alternate-pose** models (opcodes 90-93), not the base ones.
- **Callers:** `Player.java:321`.
- **Calls:** `Model.method463`.

#### `public static final void method193(Archive class44)` (file:45)
**Inferred name:** `load`. Initial loader for `obj.dat` / `obj.idx`. Allocates the 10-slot ring.
- **Callers:** `BootstrapConfigLoader.java:21`.

#### `public final Model method194(int unused, int gender)` (file:64)
**Inferred name:** `getWieldModelA`. Builds the **alternate-pose** equipment model (opcodes 90-93). For gender 0 uses `anInt175` + optional `anInt166`; gender 1 uses `anInt197` + `anInt173`. Combines them via the 2-model composite ctor and recolours.
- **Callers:** `Player.java:340`.

#### `public final boolean method195(int unused, int gender)` (file:95)
**Inferred name:** `isWieldModelBReady`. Same as `method192` but for the **primary** equipment models (opcodes 23-26, 78-79). Tests up to 3 model ids per gender.
- **Callers:** `Player.java:234`.

#### `public final Model method196(boolean flag, int gender)` (file:120)
**Inferred name:** `getWieldModelB`. Builds the **primary** equipment composite (opcodes 23-26, 78-79). Applies the gendered vertical offset (`aByte205` male, `aByte154` female) via `Model.method475(0, offset, 16384, 0)`. Recolours.
- **Callers:** `Player.java:265`.

#### `public final void method197()` (file:166)
**Inferred name:** `resetToDefaults`. Resets every parsed field to defaults; called before reusing a ring slot. Notably sets scales to `128`, action arrays to `null`, equipment ids to `-1`, `anInt181=2000`, `anInt155=1`.
- **Callers:** `method198`.

#### `public static final ItemDefinition method198(int id)` (file:209)
**Inferred name:** `forId`. Linear-scan ring; on miss, advances `anInt180`, seeks the buffer, calls `method197` + `method203`. If the parsed definition has `anInt163 != -1` (is a note), calls `method199` to derive name/description from the noted item. If `aBoolean182==false` and the item is members, swaps the metadata to the "Members Object" placeholder.
- **Callers:** ubiquitous — `Player`, `Widget`, `GroundItem`, `InterfacePacketHandler`, `WidgetRenderHandler`, `GameClientCore` (many sites).

#### `public void method199(byte byte0)` (file:234)
**Inferred name:** `applyNoteTemplate`. Copies the note **template** definition's render fields (`anInt174`, `anInt181`, `anInt190`, `anInt198`, `anInt204`, `anInt169`, `anInt194`, recolour arrays) onto this definition, then loads the **target** item (`anInt179`) to obtain its display name + members flag + value, and constructs the description `"Swap this note at any bank for {a/an} {name}."`. Forces `aBoolean176=true` (stackable).
- **Params:** `byte0` — obfuscation gate (`!=61` toggles `aBoolean186`).
- **Calls:** `method198` twice (template + target).

#### `public static final Sprite method200(int id, int stackCount, int outlineColor, int unused)` (file:260)
**Inferred name:** `renderIcon`. The icon renderer — produces a 32×32 `Sprite` for the inventory grid. Looks up the sprite cache; on miss, picks the stack-count alternate via `anIntArray193`/`anIntArray201`, retrieves the wielded-icon model via `method201(1)`, sets up the 3D rasterizer, projects the model via `method482`, then post-processes the pixel buffer to (a) draw a 1-pixel outline ("1" sentinel) around the silhouette, (b) recolour the outline to `outlineColor` (`k`) if `>0`, (c) draw a dark-grey drop-shadow (`0x302020`) when `k==0`. If the item is a note (`anInt163 != -1`), recursively renders the **note template** icon and stamps it on top via `Sprite.method348`. Caches only when `k==0` (i.e., no custom outline). The final sprite has its `anInt1445 = stackCount` (used by `WidgetRenderHandler` to invalidate cached icons when stack changes).
- **Params:** `i` item id; `j` stack count; `k` outline colour (or `0`/`-1` sentinel); `l` arbitrary obfuscation gate.
- **Callers:** `WidgetRenderHandler.java:248`, recursively in note branch.

#### `public final Model method201(int stackCount)` (file:399)
**Inferred name:** `getGroundModel`. Builds the in-world ground-pile model. If `stackCount > 1`, picks the highest-threshold alternate from `anIntArray193`/`anIntArray201` and recurses with `i=1`. Otherwise looks in `aClass12_159`; on miss loads `Model.method462(anInt171, anInt174)`, applies scaling, recolour, lighting (`method479` with `64+anInt196`, `768+anInt184`). Forces `aBoolean1659=true` (scenery-priority drawing).
- **Callers:** `GroundItem.java:19`, `method200`.

#### `public final Model method202(int stackCount, boolean flag)` (file:431)
**Inferred name:** `getInterfaceModel`. Lighter variant of `method201` used by interfaces: loads the model and applies recolour but does **not** apply scaling, lighting, or caching. Stack-count alternation works the same as `method201`.
- **Params:** `i` stack count, `flag` obfuscation gate.
- **Callers:** `Widget.java:267`.

#### `public final void method203(boolean flag, PacketBuffer buffer)` (file:457) — **opcode parser**

| Op | Bytes | Field(s) | Notes |
|----|------|----------|-------|
| `0` | — | terminator | — |
| `1` | u16 | `anInt174` | Primary model id. |
| `2` | RuneString | `aString170` | Name. |
| `3` | bytes | `aByteArray178` | Examine text. |
| `4` | u16 | `anInt181` | Icon zoom. |
| `5` | u16 | `anInt190` | Icon Y rot. |
| `6` | u16 | `anInt198` | Icon X rot. |
| `7` | u16 sign-extended | `anInt169` | Icon X translate (signed). |
| `8` | u16 sign-extended | `anInt194` | Icon Y translate (signed). |
| `10` | u16 | `anInt199` | Unknown. |
| `11` | — | `aBoolean176=true` | Stackable. |
| `12` | i32 (`method413`) | `anInt155` | Shop price. |
| `16` | — | `aBoolean161=true` | Members-only. |
| `23` | u16 + i8 | `anInt165`, `aByte205` | Male wield model A + vertical offset. |
| `24` | u16 | `anInt188` | Male wield model B. |
| `25` | u16 + i8 | `anInt200`, `aByte154` | Female wield model A + vertical offset. |
| `26` | u16 | `anInt164` | Female wield model B. |
| `30-34` | RuneString | `aStringArray168[i-30]` | Ground action ("hidden"→null). |
| `35-39` | RuneString | `aStringArray189[i-35]` | Inventory action (no "hidden" handling). |
| `40` | u8 N + N×(u16 src + u16 dst) | `anIntArray156`, `anIntArray160` | Recolour. |
| `78` | u16 | `anInt185` | Male wield model C. |
| `79` | u16 | `anInt162` | Female wield model C. |
| `90` | u16 | `anInt175` | Male alternate-pose model A. |
| `91` | u16 | `anInt197` | Female alternate-pose model A. |
| `92` | u16 | `anInt166` | Male alternate-pose model B. |
| `93` | u16 | `anInt173` | Female alternate-pose model B. |
| `95` | u16 | `anInt204` | Icon Z rotation. |
| `97` | u16 | `anInt179` | Note: target item id. |
| `98` | u16 | `anInt163` | Note: template id (triggers `method199`). |
| `100-109` | u16 + u16 | `anIntArray193[i-100]`, `anIntArray201[i-100]` | Stack-count → alternate-id pairs (allocated `int[10]` lazily). |
| `110` | u16 | `anInt167` | Scale X. |
| `111` | u16 | `anInt192` | Scale Z. |
| `112` | u16 | `anInt191` | Scale Y. |
| `113` | i8 | `anInt196` | Diffuse delta. |
| `114` | i8 × 5 | `anInt184` | Specular delta. |
| `115` | u8 | `anInt202` | Unknown (read by `Player.java:119`). |

#### `ItemDefinition()` (file:607) — ctor
Sets sentinel `anInt157=-1`, `anInt171=9`, `anInt177=9`, `aBoolean186=false`, `aBoolean206=false`. All other defaults applied by `method197`.

---

## Cross-class call graph

Lists every external (non-self) caller invocation observed in the legacy tree, grouped by class. Line numbers are in the **caller** file.

### `ObjectDefinition` external callers

| Caller (file:line) | Invocation | Purpose |
|---|---|---|
| `BootstrapConfigLoader.java:19` | `ObjectDefinition.method576(configArchive)` | Cache load on boot. |
| `GameClientCore.java:638,689` | `ObjectDefinition.aClass12_785.method224()` | Flush raw-model cache. |
| `GameClientCore.java:690` | `ObjectDefinition.aClass12_780.method224()` | Flush composed-model cache. |
| `GameClientCore.java:747,2258,2349,2386,3316,4183,4451,8902,9293,9302,9311` | `ObjectDefinition.method572(id)` | Various definition lookups (minimap, menu, click, render). |
| `GameClientCore.java:2494,9044` | `ObjectDefinition.aBoolean752 = ...` | Toggle re-parse mode during region rebuild. |
| `GameClientCore.java:4453` | `class46.method580(true)` | Resolve child id via varbit/varp. |
| `GameClientCore.java:4743` | `ObjectDefinition.method575(-501)` | Unload. |
| `GameClientCore.java:8907` | `class46.method578(j19, i20, i22, j22, k22, l22, -1)` | Build object model at world tile. |
| `MapRegion.java:501` | `class46.method574(class42_Sub1, -235)` | Request models for region prefetch. |
| `MapRegion.java:568,576,592,616,629,681,704,705,751,770,800,810,815,820,825,1118,1126,1149,1157,1165,1177,1178,1190,1198,1228,1238,1243,1248,1253` | `class46.method578(...)` | Build scenery models for every placement type (walls, decoration, etc.). |
| `MapRegion.java:875` | `class46.method577(n2, true)` | Models-ready check. |
| `MapRegion.java:1288` | `class46.method579(true)` | Models-ready check (any placement type). |
| `MapRegion.java:568-1253` (multiple) | reads `class46.anInt781`, `class46.anIntArray759` | Decide static vs `DynamicObject` instantiation. |
| `DynamicObject.java:49` | `class46.method578(anInt1611, anInt1612, anInt1603, anInt1604, anInt1605, anInt1606, j)` | Per-frame animated object model. |

### `NpcDefinition` external callers

| Caller (file:line) | Invocation | Purpose |
|---|---|---|
| `BootstrapConfigLoader.java:22` | `NpcDefinition.method162(configArchive)` | Cache load on boot. |
| `GameClientCore.java:691` | `NpcDefinition.aClass12_95.method224()` | Flush model cache. |
| `GameClientCore.java:1346,3902,8175` | `class5.method161(anInt877)` | Resolve child by combat level. |
| `GameClientCore.java:1344,1376,3900,8173` | `((Npc)obj).aClass5_1696` | Read NPC's definition pointer. |
| `GameClientCore.java:2025` | `NpcDefinition.method159(...)` | Bind definition to a freshly-spawned NPC. |
| `GameClientCore.java:4744` | `NpcDefinition.method163(-501)` | Unload. |
| `GameClientCore.java:6189` | `method87(NpcDefinition class5, ...)` | Compose NPC menu entry. |
| `EntityMenuBuilder.java:15` | `npcDefinition = npcDefinition.method161(localPlayerCombatLevel)` | Resolve child for menu. |
| `NpcUpdateMaskHandler.java:123` | `npc.aClass5_1696 = NpcDefinition.method159(...)` | Apply transform mask. |
| `Npc.java:25` | `aClass5_1696.method164(0, i1, k, SequenceDefinition.aClass20Array351[…].anIntArray357)` | Build animated model with sequence. |
| `Npc.java:30` | `aClass5_1696.method164(0, -1, l, null)` | Build animated model idle-only. |
| `Player.java:114` | `aClass5_1698 = NpcDefinition.method159(...)` | Player morph to NPC. |
| `Player.java:190` | `aClass5_1698.method164(0, -1, j, null)` | Render morphed player. |
| `Player.java:314` | `aClass5_1698.method160(true)` | Build "preview" model for morphed player. |
| `Widget.java:263` | `NpcDefinition.method159(j).method160(true)` | Interface NPC preview. |

### `ItemDefinition` external callers

| Caller (file:line) | Invocation | Purpose |
|---|---|---|
| `BootstrapConfigLoader.java:21` | `ItemDefinition.method193(configArchive)` | Cache load on boot. |
| `GameClientCore.java:692` | `ItemDefinition.aClass12_159.method224()` | Flush model cache. |
| `GameClientCore.java:693,1247` | `ItemDefinition.aClass12_158.method224()` | Flush icon cache. |
| `GameClientCore.java:795,974,4144,4175,4201,4560,8008,8035` | `ItemDefinition.method198(id)` | Various inventory/widget/world lookups (name, members flag, examine). |
| `GameClientCore.java:4745` | `ItemDefinition.method191(-501)` | Unload. |
| `Player.java:117-119` | `ItemDefinition.anInt203` bound check + `method198(...).anInt202` | Equipment slot iteration. |
| `Player.java:234` | `ItemDefinition.method198(k2-512).method195(40903, anInt1702)` | Equipment wield-model-B ready check. |
| `Player.java:265` | `ItemDefinition.method198(i3-512).method196(false, anInt1702)` | Build primary wield model. |
| `Player.java:321` | `ItemDefinition.method198(j-512).method192(-2836, anInt1702)` | Alternate wield-model-A ready check. |
| `Player.java:340` | `ItemDefinition.method198(i1-512).method194(-705, anInt1702)` | Build alternate wield model. |
| `GroundItem.java:15-19` | `ItemDefinition.method198(anInt1558).method201(anInt1559)` | Ground pile mesh. |
| `Widget.java:267` | `ItemDefinition.method198(j).method202(50, true)` | Interface item preview. |
| `WidgetRenderHandler.java:18` | `ItemDefinition.method198(...)` | Examine + members flag for inventory grid. |
| `WidgetRenderHandler.java:248` | `ItemDefinition.method200(itemId, anIntArray252[slot], outlineColor, 9)` | Render inventory icon sprite. |
| `InterfacePacketHandler.java:33` | `ItemDefinition.method198(itemId)` | Process interface item swap packet. |
| `NpcUpdateMaskHandler` | (none direct) | — |

---

## Appendix: PacketBuffer accessor cheat-sheet

The opcode parsers above use the following obfuscated `PacketBuffer` accessors (see `cache/PacketBuffer.java`):

- `method408()` → `readUnsignedByte()` (u8)
- `method409()` → `readByte()` (i8)
- `method410()` → `readUnsignedShort()` (u16)
- `method411()` → `readShort()` (i16, sign-extended)
- `method413()` → `readInt()` (i32)
- `method415()` → `readJagString()` (terminated)
- `method416(byte)` → read until terminator into byte[]
- `method419(...)` → `readBits` / smart-int variant
- `anInt1406` is the read cursor.

These signatures are consistent across all three definitions and should be matched 1:1 by the modern `rs-content` decoders.
