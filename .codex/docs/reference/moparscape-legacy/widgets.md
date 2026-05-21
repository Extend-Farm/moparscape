# Widget + WidgetRenderHandler (legacy moparscape)

Tier-1 1:1-parity reference for the legacy obfuscated client's interface (RuneScape "widget") subsystem. All field/method names below are the obfuscated Jad output preserved verbatim — inferred semantic names are given in parentheses. Line references are absolute file:line into the legacy sources.

Companion files (rendered tree walk + per-component dispatch live there; not in scope but cross-referenced):
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/WidgetTreeRenderer.java`
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/WidgetInteractionHandler.java`
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/WidgetConditionPort.java`

---

## Widget.java

### Overview
- **Path**: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Widget.java`
- **Line count**: 401
- **Class kind**: Plain data class plus three static helpers and one instance helper. Models a single RuneScape `RSInterface`/"widget" component (group container, model preview, inventory grid, rectangle fill, text, sprite, model-list/animation preview, or text-item grid). A flat global table `aClass9Array210[]` indexes every decoded component across every interface — group children reference siblings by integer id.
- **Construction**: `new Widget()` initialises `anInt213 = 9` (model-lighting magic constant used by `method209` → `Model` constructor) and `anInt229 = 891` (an obfuscation guard echoed throughout the legacy client). All other state is filled in by the static decoder `method205` (see [How widgets are decoded from cache](#how-widgets-are-decoded-from-cache)).
- **Caches**:
  - `aClass12_238` (line 366) — sprite cache, scoped to a single `method205` decode pass (allocated at the top, nulled at the bottom: `Widget.java:29`, `:251`).
  - `aClass12_264` (line 392) — global model cache (capacity 30), used by `method206`/`method208` to memoise model-id → `Model` lookups across all widgets and reset by `method208`.
- **Global table**: `public static Widget aClass9Array210[]` (line 338) — every decoded component lives here, slot index = component id; group children list child ids in `anIntArray240`.

### Type discriminator (`anInt262` — "component type")
Inferred from the per-type decode branches in `method205` and from caller dispatch in `WidgetTreeRenderer` + `GameClientCore.method29`:

| `anInt262` | Inferred meaning | Decode branch | Renderer branch |
|---|---|---|---|
| 0 | Group / container (holds child ids + per-child child-x/child-y; `anInt261` = scroll-content height, `aBoolean266` = hidden, optional scrollbar when `anInt261 > anInt267`) | `Widget.java:82-97` | `WidgetTreeRenderer.java:50-?` (recurses) |
| 1 | Model preview (single static model; `anInt211` = model id, `aBoolean251` = ?) | `Widget.java:98-102` | (model branch, dispatched via `method29`/`method105`) |
| 2 | Inventory grid (`anInt220 × anInt267` slots; per-slot `anIntArray253` = itemId+1, `anIntArray252` = stack; `anIntArray215`/`anIntArray247` = first-20 slot pixel offsets; `aClass30_Sub2_Sub1_Sub1Array209` = first-20 fallback sprites; `aBoolean259/249/242/235` = drag/swap/use/depth flags; `anInt231/244` = slot x/y padding; `aStringArray225[5]` = right-click options) | `Widget.java:103-140` | `WidgetRenderHandler.renderType2InventoryGrid` (line 202-310) |
| 3 | Filled rectangle (solid or alpha-blended; `aBoolean227` = horizontal-fill flag) | `Widget.java:141-142, :158-163` | `WidgetRenderHandler.renderType3Rectangle` (line 77-102) |
| 4 | Text / label (`aString248` = default text, `aString228` = active text, font in `aClass30_Sub2_Sub1_Sub4_243`; `aBoolean223` = centred, `aBoolean268` = shadow) | `Widget.java:143-150, :151-155, :156-163` | `WidgetRenderHandler.renderType4Text` (line 105-199) |
| 5 | Sprite (`aClass30_Sub2_Sub1_Sub1_207` = default, `aClass30_Sub2_Sub1_Sub1_260` = active/hover) | `Widget.java:164-178` | `WidgetRenderHandler.renderType5Sprite` (line 36-45) |
| 6 | Model with animation (`anInt233/anInt234` = default model source+id; `anInt255/anInt256` = active; `anInt257/anInt258` = default/active sequence id; `anInt269` = zoom, `anInt270` = pitch index, `anInt271` = roll) — used for character/NPC/item preview panels | `Widget.java:179-206` | `WidgetRenderHandler.renderType6Model` (line 48-74) |
| 7 | Inventory-text grid (text-only inventory; same `anIntArray253/anIntArray252` layout as type 2, but renders item name + " x" + stack via font; `anInt231/anInt244` = x/y padding, `anInt232` = colour, font in `aClass30_Sub2_Sub1_Sub4_243`) | `Widget.java:207-228` | `WidgetRenderHandler.renderType7ItemTextGrid` (line 9-33) |

### Interaction discriminator (`anInt217` — "option type")
Independent of `anInt262`. Drives right-click menu entries built by `GameClientCore.method29` (lines 903-952):

| `anInt217` | Inferred meaning | Default `aString221` | Notes |
|---|---|---|---|
| 1 | Standard "Ok"-style click target | "Ok" | Decoded at `Widget.java:235-249` |
| 2 | Hover-tooltip target ("`aString222` @gre@ `aString218`"); decoded at `Widget.java:229-234` along with `anInt237` (tooltip target id?) | — | Used at `GameClientCore.java:916-923` |
| 3 | Reset / clear action (no caption decoded; menu uses hard-coded text in `method29`) | — | `GameClientCore.java:926-930` |
| 4 | "Select"-style click target | "Select" | `Widget.java:243`, `GameClientCore.java:933-937` |
| 5 | Secondary "Select" | "Select" | `Widget.java:245`, `GameClientCore.java:940-944` |
| 6 | Continue-dialogue button | "Continue" | `Widget.java:247`; suppressed while `aBoolean1149` (dialogue-in-flight); text becomes "Please wait..." in `renderType4Text` (line 131-135) |

### Fields

Grouped by inferred role. All declared `public` (unless noted) at `Widget.java:335-400`.

#### Identity + hierarchy
- `int anInt250` (line 378) — component id (= index into `aClass9Array210`). Set at `method205:43`. Read by every packet handler in `InterfacePacketHandler` and by `GameClientCore.method29` (lines 912, 923, 930, 937, 944, 951, 971, 4286).
- `int anInt236` (line 364) — parent interface (root) id; assigned from the `65535` header sentinel in the decoder loop (`method205:38-44`). Used to test "is this widget part of the open interface" — e.g. `InterfacePacketHandler.applyWidgetText:56`, `GameClientCore:443-445, 1197, 4034, 4322-4324, 5858+`.
- `int anInt262` (line 390) — component type (see table above). Read by every render / hit-test branch.
- `int anInt217` (line 345) — option type (see table above).
- `int anInt214` (line 342) — script trigger id / hover-cs1-trigger; only test seen is `== 600` (chat-tab autoselect) at `GameClientCore.java:1195, 4032`.
- `byte aByte254` (line 382) — opacity / alpha channel for rectangle fills (0 = opaque solid). Sampled at `WidgetRenderHandler.renderType3Rectangle:91, :99, :101` to pick between `Rasterizer2D.method336/337` (opaque) and `method335/338` (alpha).
- `int anInt230` (line 358) — hover/secondary-component override id ("when this is hovered, render component-id N instead"); decoded with a 2-byte big-endian "+1" sentinel (`method205:51-55`); used at `GameClientCore.java:891-895` to redirect tooltip and option targets.

#### Layout (geometry, scroll, runtime offsets)
- `int anInt220` (line 348) — pixel width (or column count for type 2/7).
- `int anInt267` (line 395) — pixel height (or row count for type 2/7).
- `int anInt263` (line 391) — runtime scroll-x offset added to the child's base position in `GameClientCore.method29:889` and `WidgetTreeRenderer.java:50`. Written by `IncomingPacketDispatcher.java:54` (server-pushed scroll).
- `int anInt265` (line 393) — runtime scroll-y offset; mirror of `anInt263`. Written by `IncomingPacketDispatcher.java:55`; consumed at `GameClientCore.method29:890`, `WidgetTreeRenderer.java:51`.
- `int anInt224` (line 352) — current vertical scroll position inside a group container. Adjusted by mouse-wheel/scrollbar in `GameClientCore` (lines 3274, 3283, 3297) and during inventory drag in `renderType2InventoryGrid` (lines 272, 282). Auto-reset via `aClass9_1059.anInt224 = …` at `GameClientCore:6977-6980`.
- `int anInt261` (line 389) — total content height inside a group container (drives the scrollbar). Decoded for type 0 at `method205:84`. Recomputed dynamically for friends/ignore lists at `GameClientCore:5255-5293`. Compared against `anInt267` to decide whether to draw a scrollbar (`GameClientCore:899-900`).
- `int[] anIntArray240` (line 368) — child component ids inside a group (length = number of children). Decoded at `method205:87-95`.
- `int[] anIntArray241` (line 369) — per-child relative x. Decoded as signed short (`method411`) at `method205:93`.
- `int[] anIntArray272` (line 400) — per-child relative y. Decoded as signed short at `method205:94`.

#### Conditional scripts (used by `WidgetConditionPort.formatWidgetValue` + visibility evaluator `GameClientCore.method131`)
- `int[] anIntArray245` (line 373) — per-condition opcode (`1` = "less than", `2` = "greater than", `3` = "equal", `4` = "not equal" per usual RS323 semantics; verify in `method131`). Decoded at `method205:63`.
- `int[] anIntArray212` (line 340) — per-condition operand (the value being compared). Decoded at `method205:64`.
- `int[][] anIntArrayArray226` (line 354) — interpreted client-side scripts (cs1): outer array = scripts, inner array = opcode stream (terminator-bounded). Decoded at `method205:71-79`. Read by `WidgetConditionPort.formatWidgetValue` (called from `renderType4Text:152-180` for `%1`–`%5` substitution) and `GameClientCore.method131`.

#### Type-2 / type-7 inventory state
- `int[] anIntArray253` (line 381) — per-slot itemId+1 (0 = empty slot). Length = `anInt220 * anInt267`. Allocated for type 2 (`method205:105`) and type 7 (`method205:209`). Written by `InterfacePacketHandler.applyWidgetItemGridSnapshot` (line 88) and `applyWidgetItemSlotUpdates` (line 137). Consumed by `renderType2InventoryGrid:238, 242` and `renderType7ItemTextGrid:16, 18`.
- `int[] anIntArray252` (line 380) — per-slot stack count. Mirrors `anIntArray253`. Same producers/consumers.
- `boolean aBoolean259` (line 387) — "swappable" flag (drag-to-swap enabled). Decoded `method205:107`; tested in `GameClientCore:434`.
- `boolean aBoolean249` (line 377) — "item-usable" flag (left-click→use selection enabled). Decoded `method205:108` (type 2), `method205:219` (type 7); tested at `GameClientCore:975`, `renderType7ItemTextGrid` indirectly via menu logic.
- `boolean aBoolean242` (line 370) — "use-on" target flag (can be the target of item-use). Decoded `method205:109`.
- `boolean aBoolean235` (line 363) — "depth-flag" / "deletable" flag. Decoded `method205:110`; tested in `GameClientCore:434`.
- `int anInt231` (line 359) — extra horizontal gap (px) added between adjacent slot columns beyond the 32-px (type 2) / 115-px (type 7) cell width. Decoded `method205:111` / `method205:217`. Consumed `renderType2InventoryGrid:231`, `renderType7ItemTextGrid:22`.
- `int anInt244` (line 372) — extra vertical gap (px). Decoded `method205:112` / `method205:218`. Consumed `renderType2InventoryGrid:232`, `renderType7ItemTextGrid:23`.
- `int[] anIntArray215` (line 343) — first-20 per-slot pixel-x offset (decoded only if marker byte = 1: `method205:121`). Consumed `renderType2InventoryGrid:235`.
- `int[] anIntArray247` (line 375) — first-20 per-slot pixel-y offset. Consumed `renderType2InventoryGrid:236`.
- `Sprite[] aClass30_Sub2_Sub1_Sub1Array209` (line 337) — first-20 fallback sprites drawn in empty slots (used for "wear-this-here" silhouettes in equipment screens). Loaded via `method207`. Consumed `renderType2InventoryGrid:299-303`.
- `String[] aStringArray225` (line 353) — five right-click options for the inventory cell. Decoded `method205:132-138` (type 2), `:220-226` (type 7).

#### Type-3 (rectangle)
- `boolean aBoolean227` (line 355) — orientation flag selecting `Rasterizer2D.method336/335` (horizontal-fill?) vs `method337/338` (vertical or full-rect). Decoded `method205:142`; consumed `renderType3Rectangle:93, :98`.

#### Type-4 (text) — also shared with types 1, 3
- `boolean aBoolean223` (line 351) — "centred" flag. Also used by type 7. Decoded `method205:145, :211`. Consumed `renderType4Text:194`, `renderType7ItemTextGrid:24`.
- `boolean aBoolean268` (line 396) — "shadow" flag. Decoded `method205:149, :215`. Consumed by both text renderers as final arg to `FontRenderer.method382/389`.
- `FontRenderer aClass30_Sub2_Sub1_Sub4_243` (line 371) — font lookup. Index decoded at `method205:146-148, :213-214`; the actual font table is passed into `method205` as `aclass30_sub2_sub1_sub4[]`.
- `String aString248` (line 376) — default text. Decoded `method205:153`. Mutated at runtime by `InterfacePacketHandler.applyWidgetText:55`.
- `String aString228` (line 356) — active/alternate text (used when `method131` returns true). Decoded `method205:154`.
- `int anInt232` (line 360) — default colour (24-bit RGB). Decoded as 4-byte word (`method413`) at `method205:157`. Mutated by `InterfacePacketHandler.applyWidgetTextColor:75`. Also used for type 3 fill colour.
- `int anInt219` (line 347) — active colour (when `method131` true). Decoded `method205:160`. Consumed `renderType3Rectangle:82`, `renderType4Text:120`.
- `int anInt216` (line 344) — hover colour (when focused, default state). Decoded `method205:161`. Consumed `renderType3Rectangle:88`, `renderType4Text:128`.
- `int anInt239` (line 367) — hover colour (when focused + active). Decoded `method205:162`. Consumed `renderType3Rectangle:83`, `renderType4Text:121`.

#### Type-5 (sprite)
- `Sprite aClass30_Sub2_Sub1_Sub1_207` (line 335) — default sprite (id+archive name decoded then resolved via `method207`). `method205:166-171`.
- `Sprite aClass30_Sub2_Sub1_Sub1_260` (line 388) — active/hover sprite. `method205:172-177`.

#### Type-6 (model+animation)
- `int anInt233` (line 361) — default model **source** kind: 1 = base model from `Model.method462(anInt213, j)`, 2 = NPC head (`NpcDefinition.method159(j).method160`), 3 = local-player model (`GameClient.aClass30_Sub2_Sub4_Sub1_Sub2_1126.method453`), 4 = item model (`ItemDefinition.method198(j).method202`), 5 = no model (`null`). See `method206:260-269`. Decoded as 1-byte+1-byte BE id at `method205:181-185`. Overwritten by `InterfacePacketHandler` packets: `applyPlayerIdentityWidget:14` (→ 3), `applyWidgetItemModel:34` (→ 4 or 0=cleared), `applyWidgetNpcHeadModel:64` (→ 1).
- `int anInt234` (line 362) — default model id (interpretation depends on `anInt233`). Also doubles as a packed appearance/equipment hash for player kit (`applyPlayerIdentityWidget:16-18`).
- `int anInt255` (line 383) — active model source kind. Decoded `method205:187-191`. Consumed `method209:308` when `flag==true`.
- `int anInt256` (line 384) — active model id.
- `int anInt257` (line 385) — default animation sequence id (`-1` = none). Decoded `method205:193-197`. Mutated by `InterfacePacketHandler.applyWidgetAnimation:116`. Consumed `renderType6Model:60`.
- `int anInt258` (line 386) — active animation sequence id. Decoded `method205:198-202`.
- `int anInt269` (line 397) — model zoom / scale. Decoded `method205:203`. Mutated by `applyWidgetItemModel:38`, `applyWidgetModelTransform:108`. Consumed `renderType6Model:54-55` (multiplied with sin/cos tables).
- `int anInt270` (line 398) — yaw / pitch index (0–2047, indexes `Rasterizer3D.anIntArray1470/1471`). Decoded `method205:204`. Mutated by item/model packets. Consumed `renderType6Model:54-55, :71`.
- `int anInt271` (line 399) — roll angle. Decoded `method205:205`. Consumed `renderType6Model:71`.

#### Animation playback state (mutated by `GameClientCore` frame-advance at lines 7818-7826)
- `int anInt246` (line 374) — current sequence-frame index. Reset to 0 by `applyWidgetAnimation:119` (when sequence cleared) and `GameClientCore:2903`.
- `int anInt208` (line 336) — accumulator (ms or ticks) toward next frame. Reset alongside `anInt246`.

#### Hover-tooltip / option-label state
- `String aString222` (line 350) — tooltip prefix (decoded only when `anInt217 == 2` or `anInt262 == 2`, `method205:231`).
- `String aString218` (line 346) — tooltip suffix (rendered as "`aString222` @gre@ `aString218`" at `GameClientCore:921`).
- `int anInt237` (line 365) — tooltip target id (`method205:233`). Use unclear; likely the option's action target ("use X with …") packed id.
- `String aString221` (line 349) — right-click menu label for `anInt217` ∈ {1,4,5,6}. `method205:237-249` falls back to "Ok"/"Select"/"Select"/"Continue".

#### Decoder pre-amble (always present)
- `int anInt262` — see type table. Decoded `method205:45`.
- `int anInt217` — see option-type table. Decoded `method205:46`.
- `int anInt214` — script-trigger id. Decoded `method205:47`.
- `int anInt220` — width. Decoded `method205:48`.
- `int anInt267` — height. Decoded `method205:49`.
- `byte aByte254` — alpha. Decoded `method205:50`.
- `int anInt230` — hover redirect. Decoded `method205:51-55`.

#### Header guards / private knobs
- `int anInt213` (line 341, **private**) — model-lighting magic constant (init 9 in ctor). Passed verbatim to `Model.method462(anInt213, j)` in `method206:261`. Never re-written.
- `int anInt229` (line 357, **private**) — obfuscation guard (init 891). Touched only by `method204:20` (no-op write `anInt229 = -76`) and by the `PacketBuffer` constructor in `method205:30` (which is passed the constant 891 directly). Effectively a dead canary.
- `boolean aBoolean266` (line 394) — hidden flag (skip render). Decoded `method205:85`. Mutated by `InterfacePacketHandler.applyWidgetVisibility:47`. Tested at `GameClientCore:877`.
- `int anInt211` (line 339) — type-1 model id (decoded `method205:100`). Equivalent of `anInt234` but for the static-model branch.
- `boolean aBoolean251` (line 379) — type-1 flag (decoded `method205:101`; semantics unclear — likely "use active appearance" toggle).

### Methods

#### `public void method204(int i, byte byte0, int j)` — swap inventory slots
- **Lines**: 13–25.
- **Purpose**: Swap the contents of inventory slot `i` and slot `j` (both the itemId+1 in `anIntArray253` and the stack in `anIntArray252`). Used during drag-and-drop reordering of the player inventory.
- **Parameters**: `i` = source slot, `j` = destination slot, `byte0` = obfuscation guard — branch at line 17-20 either resets the guard (`byte0 == 9` → set to 0) or writes the canary `anInt229 = -76`. Functionally pure.
- **Returns**: void.
- **Called by**: `GameClientCore.java:3026, 3031, 3037` — the three drag-resolved swap sites (left-shift, right-shift, drop-on-other-slot) inside the inventory drag handler.
- **Calls**: none.
- **Notes**: The `byte0` parameter has no observed effect on the swap — it only toggles the canary. All real callers pass `(byte)9`, which means `anInt229` is left untouched (set to 0).

#### `public static void method205(Archive class44, FontRenderer[] aclass30_sub2_sub1_sub4, byte byte0, Archive class44_1)` — decode all widgets from cache
- **Lines**: 27–253.
- **Purpose**: Parse the cache file `data` inside the interfaces archive and populate the global table `aClass9Array210` with every interface component. Single shared sprite cache (`aClass12_238`) is allocated for the duration of the parse and then released.
- **Parameters**:
  - `class44` — the interfaces archive (`interface.jag` / index `3`) — supplies the `data` entry parsed as a `PacketBuffer`.
  - `aclass30_sub2_sub1_sub4` — the font table indexed by the per-widget font byte (`method205:148, :214`).
  - `byte0` — obfuscation guard (caller passes `-84` from `BootstrapConfigLoader.java:43`; check at `:252` is a no-op).
  - `class44_1` — the media/sprites archive used by `method207` to load per-component sprites (may be `null` to skip sprite resolution, e.g. on a headless decode).
- **Returns**: void (side-effect: writes `aClass9Array210`).
- **Called by**: `BootstrapConfigLoader.java:43` — exactly once during client bootstrap.
- **Calls**: `PacketBuffer.method408/410/411/413/415`, `method207` (sprite loader).
- **Wire format**: header is `numWidgets` (short). The body is a stream of components; component `k`'s id is the running short, and a leading `0xFFFF` sentinel pushes a new parent-interface id `i` (stored as `anInt236` on every following component). Common pre-amble = `type, optionType, scriptTrigger, width, height, alpha, hoverId`. Conditional blocks then follow based on `anInt262` and `anInt217`. See [How widgets are decoded from cache](#how-widgets-are-decoded-from-cache).
- **Notes**: The trailing `if(byte0 == -84);` (line 252) is a dead branch left by the obfuscator and is a useful fingerprint for matching the decompile output to the original bytecode.

#### `private Model method206(int i, int j)` — resolve model handle by (source kind, id)
- **Lines**: 255–273.
- **Purpose**: Look up a `Model` for the type-6 / type-1 widget renderer by `(kind, id)`. Memoised in `aClass12_264` keyed by `(i << 16) + j`.
- **Parameters**: `i` = source-kind dispatch (1=base model with lighting `anInt213`, 2=NPC head, 3=local player kit, 4=item ground model, 5=null/none); `j` = id within the dispatched space.
- **Returns**: `Model` (may be `null` if `i == 5` or the lookup fails).
- **Called by**: `method209:308, :310` (instance, both branches of the active/default selector).
- **Calls**: `Model.method462`, `NpcDefinition.method159 → method160`, `GameClient.aClass30_Sub2_Sub4_Sub1_Sub2_1126.method453` (the local-player builder), `ItemDefinition.method198 → method202`, plus the `aClass12_264` cache primitives.
- **Notes**: When `i == 4` the result is cached under `(4<<16)+j` here, but the symmetric writer `method208` deliberately **skips** caching item models (line 300 condition `j != 4`). Net effect: item-model entries only land in the cache via this read path, never via the external invalidation entry point.

#### `private static Sprite method207(int i, boolean flag, Archive class44, String s)` — load a per-widget sprite
- **Lines**: 275–293.
- **Purpose**: Load (and cache in `aClass12_238`) a `Sprite` from `class44` keyed by archive-entry `s` and frame id `i`. The cache key is `(textHash(s) << 8) + i` via `TextUtils.method585`.
- **Parameters**: `i` = sprite frame index, `flag` = obfuscation guard (must be `false` — `true` throws `NullPointerException` at line 279), `class44` = the media archive, `s` = the entry name within the archive (everything before the last `,` of the legacy `"name,index"` string).
- **Returns**: `Sprite` or `null` if construction throws.
- **Called by**: `Widget.method205:127` (type-2 fallback-slot sprites), `:170` (type-5 default sprite), `:176` (type-5 active sprite).
- **Calls**: `TextUtils.method585`, `NodeCache.method222`/`method223`, `new Sprite(Archive, String, int)`.
- **Notes**: The shared cache `aClass12_238` is created at the very top of `method205` and nulled at the bottom, so this helper is only usable from within that decode pass.

#### `public static void method208(int i, boolean flag, int j, Model class30_sub2_sub4_sub6)` — clear and seed the per-frame model cache
- **Lines**: 295–302.
- **Purpose**: Wipe the global model cache `aClass12_264` and optionally re-seed it with a single `(j, i)` → `model` entry. Used after a widget-driven render that constructed a one-off transformed model to evict stale entries.
- **Parameters**: `i` = id (low 16 bits of the cache key), `flag` = obfuscation guard (early-return when `true` — the legacy client always passes `aBoolean994`), `j` = source kind (cached only when `j != 4`, i.e. item models are never re-seeded), `class30_sub2_sub4_sub6` = model to seed (skipped when `null`).
- **Returns**: void.
- **Called by**: `GameClientCore.java:5333` — once per render pass after the inventory-item preview model is built.
- **Calls**: `NodeCache.method224` (clear), `NodeCache.method223` (insert).
- **Notes**: The `aBoolean994` guard means the call is a no-op while certain UI states are active (an obfuscation-time gate, not a semantic flag).

#### `public Model method209(int i, int j, int k, boolean flag)` — build the per-frame transformed model
- **Lines**: 304–327.
- **Purpose**: Build the `Model` actually fed to the rasterizer for a type-6 widget: looks up the base model via `method206` according to `flag`, then layers a primary animation frame `k` and an interleaved frame `j` (used for stand+walk blends), bakes pivots, and applies the canonical preview-lighting tweak.
- **Parameters**: `i` = obfuscation guard (must be `0` — non-zero throws NPE at line 324); `j` = secondary animation frame id (`-1` = none); `k` = primary animation frame id (`-1` = none); `flag` = `true` ⇒ use active source `(anInt255, anInt256)`, `false` ⇒ default `(anInt233, anInt234)`.
- **Returns**: `Model` (or `null` if the base lookup failed).
- **Called by**: `WidgetRenderHandler.renderType6Model:64` (no-sequence path: `(0, -1, -1, isActive)`) and `:68` (sequenced path: `(0, sequence.anIntArray354[anInt246], sequence.anIntArray353[anInt246], isActive)`).
- **Calls**: `method206`, `AnimationFrame.method532` (frame skin-mask), `new Model(int, boolean, int, boolean, Model)` (skinned copy ctor), `Model.method469` (bake pivots), `Model.method470` (apply animation frame), `Model.method479` (apply preview lighting `64, 768, -50, -10, -50`).
- **Notes**: When both frames are `-1` and the base model has no skin (`anIntArray1640 == null`) the base is returned directly — a fast path that skips the skinned copy allocation.

#### `public Widget()` — constructor
- **Lines**: 329–333.
- **Purpose**: Initialise `anInt213 = 9` (model-lighting magic constant) and `anInt229 = 891` (obfuscation canary).
- **Called by**: `method205:42` only.

---

## WidgetRenderHandler.java

### Overview
- **Path**: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/WidgetRenderHandler.java`
- **Line count**: 321
- **Class kind**: `final` utility class (private ctor at line 5). Holds the **extracted, byte-for-byte** per-type rendering branches that originally lived inside the legacy mega-method `method105` on `GameClientCore`. Each `renderTypeN…` method here corresponds to exactly one `if(class9.anInt262 == N)` arm.
- **Tree walk**: The actual recursion over children, hit-test, scroll-offset application, and scrollbar drawing still live in `GameClientCore.method105` (lines 7171-…) and its delegate `WidgetTreeRenderer.renderWidgetTree`. This handler is only the leaf dispatch.
- **State coupling**: Mutates `Rasterizer3D.anInt1466`/`anInt1467` (camera centre) inside `renderType6Model` and restores it on exit. Reads (does not write) `Rasterizer2D.anInt1379`/`anInt1381`-`1384` for the visible-rect clip used by the inventory drag autoscroll.

### Methods

#### `static void renderType7ItemTextGrid(Widget widget, FontRenderer fontRenderer, int baseX, int baseY, int textColor)`
- **Lines**: 9–33.
- **Purpose**: Render an `anInt220 × anInt267` grid of item-name text lines. For each non-empty slot, looks up the `ItemDefinition`, optionally appends " x" + formatted stack (when stackable or stack != 1), and draws via the centred (`method382`) or left-aligned (`method389`) font path.
- **Parameters**: `widget` = the type-7 component; `fontRenderer` = font from `widget.aClass30_Sub2_Sub1_Sub4_243`; `baseX`/`baseY` = pixel origin of the grid; `textColor` = colour-seed argument forwarded to `FontRenderer.method382/389`.
- **Called by**: `WidgetTreeRenderer.java:108`.
- **Calls**: `ItemDefinition.method198`, `StackAmountFormatter.formatStackAmount`, `FontRenderer.method382/389`.
- **Notes**: Cell stride is hardcoded `115 + anInt231` horizontally and `12 + anInt244` vertically (matches the 317 reference). Slot index runs row-major from 0 to `anInt220*anInt267-1`.

#### `static void renderType5Sprite(Widget widget, int drawX, int drawY, boolean isWidgetActive)`
- **Lines**: 36–45.
- **Purpose**: Draw the type-5 sprite at `(drawX, drawY)`. Selects the active sprite (`aClass30_Sub2_Sub1_Sub1_260`) when `isWidgetActive` else the default (`aClass30_Sub2_Sub1_Sub1_207`). Skips when the selected sprite is `null`.
- **Called by**: `WidgetTreeRenderer.java:100`.
- **Calls**: `Sprite.method348` (opaque blit).
- **Notes**: The `16083` literal forwarded to `Sprite.method348` is the obfuscation guard for that draw helper.

#### `static void renderType6Model(Widget widget, int drawX, int drawY, boolean isWidgetActive)`
- **Lines**: 48–74.
- **Purpose**: Render the type-6 model preview. Temporarily relocates the 3D rasterizer centre `(Rasterizer3D.anInt1466, anInt1467)` to the centre of the widget rect, computes `yawSin/yawCos` from `anInt269/anInt270` against the global sin/cos tables, picks the default vs active sequence (`anInt257` vs `anInt258`), builds the per-frame model via `widget.method209`, and invokes `Model.method482(0, anInt271, 0, anInt270, 0, yawSin, yawCos)`. Restores the centre on exit.
- **Called by**: `WidgetTreeRenderer.java:104`.
- **Calls**: `widget.method209`, `Model.method482`, `SequenceDefinition.aClass20Array351[…]` lookup.
- **Notes**: Animation **frame advance** (`widget.anInt208`/`anInt246`) is done elsewhere (`GameClientCore:7818-7826`) — this method only samples the current frame.

#### `static void renderType3Rectangle(Widget widget, int drawX, int drawY, boolean isFocusedWidget, boolean isWidgetActive)`
- **Lines**: 77–102.
- **Purpose**: Render the type-3 rectangle fill. Picks the colour from `anInt232` / `anInt216` / `anInt219` / `anInt239` according to active+focused state (with the focused colour only overriding when non-zero), and routes through one of four `Rasterizer2D` primitives based on `aByte254` (opaque vs alpha) × `aBoolean227` (orientation).
- **Called by**: `WidgetTreeRenderer.java:89`.
- **Calls**: `Rasterizer2D.method335/336/337/338` — see `Rasterizer2D.java:71, :110, :140, :149`. Alpha is forwarded as `256 - (aByte254 & 0xff)`.

#### `static void renderType4Text(WidgetConditionPort widgetConditionPort, Widget widget, int drawX, int drawY, int textColorSeed, boolean isFocusedWidget, boolean isWidgetActive, boolean isDialogueOpen)`
- **Lines**: 105–199.
- **Purpose**: Render the type-4 text component with per-line word-wrap on `"\n"`, colour selection identical to `renderType3Rectangle` (`anInt232`/`anInt216`/`anInt219`/`anInt239`), `"Please wait..."` substitution for in-flight dialogue continue-buttons (`anInt217 == 6 && isDialogueOpen`), iterative `%1`..`%5` token substitution from the widget's conditional scripts via `WidgetConditionPort.formatWidgetValue`, an 8-bit display compatibility hack (`Rasterizer2D.anInt1379 == 479` → remap yellow/dark-blue), and finally a per-line draw via `FontRenderer.method382` (centred) or `method389` (left-aligned).
- **Parameters**: `widgetConditionPort` = the implementation that supplies dynamic numeric values for `%N` tokens (the `GameClientCore` itself implements this interface — see `GameClientCore.java:22, :5577`); `textColorSeed` = colour-randomiser seed forwarded to the font.
- **Called by**: `WidgetTreeRenderer.java:96`.
- **Calls**: `WidgetConditionPort.formatWidgetValue`, `FontRenderer.method382/389`.
- **Notes**: Line stride is `fontRenderer.anInt1497` (font line height). The `%N` substitution is performed before the newline split, so a substituted value may legally contain `"\n"` and produce multiple physical lines.

#### `static int renderType2InventoryGrid(Widget parentWidget, Widget widget, int drawX, int drawY, int gameTickDelta, int dragState, int dragSlot, int dragWidgetId, int dragStartMouseX, int dragStartMouseY, int dragThreshold, boolean dragAlphaEnabled, int selectedDragState, int selectedDragSlot, int selectedDragWidgetId, int itemUseState, int itemUseSlot, int itemUseWidgetId, int mouseX, int mouseY, int dragOffsetY, FontRenderer itemAmountFont)`
- **Lines**: 202–310.
- **Purpose**: Render the type-2 inventory grid. For each non-empty slot inside the clip rect (`Rasterizer2D.anInt1381-1384`), renders the item sprite via `ItemDefinition.method200` (with optional white outline when this slot is the item-use highlight target), handles three sprite-draw modes (normal, drag-offset alpha-blit when this slot is being dragged, selected-drag alpha-blit), draws the stack amount in yellow with a black shadow when stackable or stack != 1, and crucially scrolls the parent group container (`parentWidget.anInt224`) up/down when the dragged sprite crosses the clip-rect edges. For empty slots, falls back to drawing `aClass30_Sub2_Sub1_Sub1Array209[slot]` (the equipment-slot silhouette).
- **Parameters**: returns the (possibly adjusted) `dragOffsetY` so the caller can keep the running mouse-Y offset coherent across frames.
- **Called by**: `WidgetTreeRenderer.java:59`.
- **Calls**: `ItemDefinition.method198/method200`, `Sprite.method348/method350`, `FontRenderer.method385`, `formatLegacyItemAmount`, reads `Rasterizer2D.anInt1381/1382/1383/1384`.
- **Notes**: Cell stride is hardcoded `32 + anInt231` × `32 + anInt244` (32-px cell). Slot index < 20 reads the optional per-slot offsets (`anIntArray215/247`) and the fallback sprite array. Drag auto-scroll cap is `gameTickDelta * 10` pixels per call.

#### `private static String formatLegacyItemAmount(int amount)`
- **Lines**: 312–320.
- **Purpose**: Format the small stack-amount overlay drawn on top of inventory item icons. `<100 000` → raw decimal; `<10 000 000` → "`amount/1000` + K"; else → "`amount/1 000 000` + M".
- **Called by**: `renderType2InventoryGrid:293, :294` only.
- **Notes**: Distinct from `StackAmountFormatter.formatStackAmount` used in `renderType7ItemTextGrid:21` — the latter is the long-form "55K / 10M / 2147M" used in text grids.

---

## How widgets are decoded from cache

The decoder is `Widget.method205` and runs **once at bootstrap** from `BootstrapConfigLoader.java:43` (`Widget.method205(interfaceArchive, fonts, (byte)-84, mediaArchive);`).

Inputs (see [`Widget.method205` signature](#public-static-void-method205archive-class44-fontrenderer-aclass30_sub2_sub1_sub4-byte-byte0-archive-class44_1--decode-all-widgets-from-cache)):
- `interfaceArchive` — the legacy "interfaces" archive (RuneScape's `interface.jag` / cache index 3 historically). Only its `data` entry is consumed (`method205:30`).
- `mediaArchive` — the legacy "media" archive (sprites). Looked up per-component via `method207`; may be `null`.
- `fonts` — the pre-loaded font table indexed by the per-component font byte.

Wire format (a single concatenated stream, no per-component length prefix):
1. **Header**: `short numWidgets` → allocates `aClass9Array210 = new Widget[numWidgets]` (`method205:32-33`).
2. **Body loop** (`while anInt1406 < length`):
   - `short k` — the next component's id (slot in `aClass9Array210`).
   - If `k == 0xFFFF` (sentinel), read `short i` and another `short k`. `i` becomes the running **parent interface id** that is then stamped onto every following component's `anInt236` until the next sentinel.
   - Allocate `new Widget()`, store `anInt250 = k`, `anInt236 = i`.
   - **Common pre-amble** (always present, `method205:45-51`): `type` (`anInt262`), `optionType` (`anInt217`), `width` (`anInt220`), `height` (`anInt267`), `alpha` (`aByte254`), `hoverComponentId` (`anInt230`, big-endian 2-byte with "+1" sentinel).
   - **Conditional script block**: `byte numConditions`; if > 0, allocate `anIntArray245`/`anIntArray212` and read `(byte op, short operand)` per condition (`method205:56-67`).
   - **Cs1 script block**: `byte numScripts`; if > 0, allocate `anIntArrayArray226` and per-script read `short len` + `len` shorts of bytecode (`method205:68-81`).
   - **Type-specific block**: dispatch on `anInt262 ∈ {0..7}` (see decode line ranges in the type table above).
   - **Option-type block**: dispatch on `anInt217` for the menu-label and tooltip strings (`method205:229-249`).
3. Cleanup: `aClass12_238 = null` releases the per-decode sprite cache (`method205:251`).

Storage: every decoded widget is placed into the global flat array `Widget.aClass9Array210` at index = `anInt250`. Group containers reference their children by id (`anIntArray240`), making the runtime "tree" walk just an index-deref against this array (see `WidgetTreeRenderer.recurse` and `GameClientCore.method29:888`, `:7799-7804`).

---

## Cross-class call graph

```text
BootstrapConfigLoader.java:43
        └─▶ Widget.method205 (decode all)
                ├─▶ Widget.method207  ──▶ new Sprite(class44, name, idx) via aClass12_238 cache
                └─▶ FontRenderer[] (passed through; stored on per-widget `aClass30_Sub2_Sub1_Sub4_243`)

InterfacePacketHandler  (server-driven runtime mutation)
        ├─▶ applyPlayerIdentityWidget    → widget.anInt233/234         (line 13-18)
        ├─▶ applyWidgetItemModel         → widget.anInt233/234/269/270/271 (line 26-40)
        ├─▶ applyWidgetVisibility        → widget.aBoolean266          (line 47)
        ├─▶ applyWidgetText              → widget.aString248           (line 54-56)
        ├─▶ applyWidgetNpcHeadModel      → widget.anInt233/234         (line 63-65)
        ├─▶ applyWidgetTextColor         → widget.anInt232             (line 75)
        ├─▶ applyWidgetItemGridSnapshot  → widget.anIntArray253/252    (line 81-96)
        ├─▶ applyWidgetModelTransform    → widget.anInt269/270/271     (line 105-108)
        ├─▶ applyWidgetAnimation         → widget.anInt257 (+ resets 246/208) (line 115-121)
        └─▶ applyWidgetItemSlotUpdates   → widget.anIntArray253/252    (line 127-140)

IncomingPacketDispatcher.java:54-55  → widget.anInt263/265 (scroll offsets)

GameClientCore  (per-frame)
        ├─▶ method131(Widget)        ─ evaluate conditional scripts (anIntArray245/212 + anIntArrayArray226)
        ├─▶ formatWidgetValue(Widget,int) implements WidgetConditionPort
        ├─▶ method29(…)              ─ hit-test + option-menu builder (reads anInt217, anIntArray240/241/272, anInt263/265/220/267/250/236)
        ├─▶ method105(…)             ─ tree-render entry → WidgetTreeRenderer.renderWidgetTree
        │       └─▶ WidgetTreeRenderer
        │               ├─▶ WidgetRenderHandler.renderType2InventoryGrid
        │               ├─▶ WidgetRenderHandler.renderType3Rectangle
        │               ├─▶ WidgetRenderHandler.renderType4Text (passes `this` as WidgetConditionPort)
        │               ├─▶ WidgetRenderHandler.renderType5Sprite
        │               ├─▶ WidgetRenderHandler.renderType6Model
        │               │       └─▶ Widget.method209 → Widget.method206 → Model.method462/NpcDefinition…/ItemDefinition…
        │               └─▶ WidgetRenderHandler.renderType7ItemTextGrid
        ├─▶ animation tick (lines 7818-7826) advances widget.anInt208/anInt246
        ├─▶ scroll handlers (3274, 3283, 3297) update widget.anInt224
        ├─▶ drag handlers (3026, 3031, 3037) call widget.method204 to swap slots
        └─▶ method208(0, aBoolean994, 5, model) (line 5333) flushes/seeds the model cache

Rasterizer2D dependencies
        ├─▶ renderType3Rectangle  → method335 (line 71) / 336 (110) / 337 (140) / 338 (149)
        └─▶ renderType2InventoryGrid reads clip rect anInt1381/1382/1383/1384
            renderType4Text reads display mode anInt1379
```

---

## Unverified / open questions

- `anInt217 == 3` decodes nothing in `method205` beyond the option-type itself; the menu code at `GameClientCore:926-930` reads no caption string. Inferred as a "reset/clear" action but the exact semantics are not directly attested in `method205`.
- `aBoolean251` (line 379) is decoded for type-1 widgets (`method205:101`) but has no reader visible in the files surveyed — likely consumed in a `method105` branch outside the extracted handler. Marked as semantics-unclear.
- `anInt237` (line 365) is decoded as a short for tooltip/use-target widgets (`method205:233`) but its consumer site isn't in the surveyed files.
- The `byte0` arguments to `method204`, `method205`, `method207`, `method208` are obfuscation guards with no semantic effect on the visible code paths; the canary `anInt229` (init 891) is similarly dead.
- `Widget.method206` caches item models under `(4<<16)+j` but the external invalidator `method208` deliberately skips them (`j != 4` at line 300). Suspect intentional (item models also live in `ItemDefinition`'s own cache) but unconfirmed.
