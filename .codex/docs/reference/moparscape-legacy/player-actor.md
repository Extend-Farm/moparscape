# Actor + Player (legacy moparscape)

Tier-1 reference for the legacy obfuscated `Actor` (base mob/avatar) and `Player`
(extends `Actor` with identity-kit + worn equipment + appearance-Model assembly).
Inferred names are best-effort, marked when uncertain. Field/method numbers and
visibility are preserved exactly. Callers were located by grep over
`GameClientCore.java`, `Model.java`, `SceneGraph.java`, `Widget.java`,
`PlayerUpdateMaskHandler.java`, `NpcUpdateMaskHandler.java`.

Companion classes referenced throughout:
- `Renderable` — superclass that exposes `anInt1426` (model bounds-related, copied
  into `Actor.anInt1507`).
- `Model` — `method464` (combine with anim mask), `method466`, `method469`,
  `method470` (apply per-frame), `method471` (interpolate two frames),
  `method473` (rotate 90°), `method475` (translate xyz), `method476`
  (recolour), `method478` (scale), `method479` (light), `aBoolean1659`
  (visible/registered flag), static `aClass30_Sub2_Sub4_Sub6_1621`
  (scratch/template model). See `Model.md`.
- `SequenceDefinition` (`Class20`) — `aClass20Array351[]` is the global anim
  table; `anIntArray353` = frame ids; `anIntArray357` = interpolation frame
  ids; `anInt352` = frame count; `anInt356` = loop offset; `anInt360`/`anInt361`
  = identity-kit overrides while animating; `anInt362` = loop count;
  `anInt363`/`anInt364` = walk/idle gating flags; `aBoolean358` = locks facing.
- `SpotAnimationDefinition` (`Class23`) — gfx overlay; `aClass23Array403[]`,
  `method266()` builds source model; `aClass20_407` = its sequence;
  `anInt410`/`anInt411` = xy scale; `anInt413`/`anInt414` = light tweak.
- `AnimationFrame.method532(frameId, false)` — returns the bitmask of skeleton
  groups affected by a given frame.
- `IdentityKitDefinition.aClass38Array656[]` — body-kit slots; `method537`
  (cached?), `method538` (body model), `method539` (head ready check),
  `method540` (head model).
- `ItemDefinition.method198(id)` — definition lookup; `method192`/`method194`
  (head-icon model), `method195`/`method196` (equip body model), `anInt202` =
  weapon stance group, gender-aware via `Player.anInt1702`.
- `NpcDefinition` (`Class5`) — `method159(id)` lookup; `method160(true)` head
  model; `method164(0,-1,frame,null)` full body model; `aBoolean93`,
  `aByte68` (size), `anInt55/58/67/77/79/83` (idle/walk/turn anim ids).
- `NodeCache` — `method222(key)` get, `method223(value, key, byte)` put.
- `GameClient.anInt1161` = current game tick. `GameClient.anIntArrayArray1003`
  = palette swap tables for `anIntArray1700`. `GameClient.anIntArray1204` =
  secondary palette for body slot index 1.
- `TextUtils.method584/method587` — username decode/format.
- `PacketBuffer` (`Class30_Sub2_Sub2`) — `method408()` u8, `method410()` u16,
  `method414()` chat-string, `anInt1406` byte offset.

---

## Actor.java

### Overview
File: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Actor.java`.
Total 225 lines, package-private class extending `Renderable`. Holds shared
mob state: walk-queue, tween position, facing/orientation, current sequence
animation (anim-cycle progress), spot-gfx overlay, queued forced movement,
hit-splat ring buffer, overhead chat, and combat target. Owns no rendering
itself — `Player` (and `Npc`, both `Renderable` subclasses) override the
final `method444(int)` from `Renderable` to assemble the per-frame Model.
`GameClientCore.method96-method101` drive an Actor's per-tick update.

### Fields

Notes use these aliases for the codified domain meaning derived from
GameClientCore usage. `tile = 1 unit`, world position uses fixed-point
`anInt1550/anInt1551 = tileX*128 + size*64`.

| legacy | type | inferred name | purpose / callers |
| --- | --- | --- | --- |
| `anIntArray1500[10]` | `int[]` | walkQueueTileX | Walk-queue tile X, index 0 = next/current target, grows backwards. Read everywhere in GameClientCore for routing (`method85` calls), pathing snapshots, projectile targets. Written by `method445/method448`. |
| `anIntArray1501[10]` | `int[]` | walkQueueTileY | Walk-queue tile Y, parallel to 1500. |
| `aBooleanArray1553[10]` | `boolean[]` | walkQueueRun | Per-step run flag — when true the per-tick walk speed `k1` is doubled in `method99`. |
| `anInt1525` | `int` | walkQueueSize | Steps remaining 0..9. Incremented by `method445/method448`; decremented in `method99` when a step is reached; cleared by `method446`. |
| `anInt1503` | `int` | walkPauseTicks | Throttle counter — when >0 holds movement while animation completes (`method99`). |
| `anInt1550` | `int` | renderWorldX | Sub-tile interpolated world X (128 per tile, +size*64 centre). Tweened by `method97/98/99`. |
| `anInt1551` | `int` | renderWorldY | Sub-tile interpolated world Y. |
| `anInt1540` | `int` | size | Tile footprint (1 for player, NPC size copied from `aClass5_1696.aByte68`). Used in every world-coord conversion. |
| `anInt1510` | `int` | facingAngle | Target render orientation 0..2047 (`& 0x7ff`). Set from movement deltas (`method99`) or face-target atan2 (`method100`). |
| `anInt1552` | `int` | currentAngle | Smoothed orientation that lags `anInt1510` by `anInt1504` per tick. |
| `anInt1504` | `int` | turnSpeed | Max rotation step per tick (`method100`). Inferred — NPC seeds from `aClass5_1696.anInt79`. 32 default. |
| `anInt1507` | `int` | modelHeight | Mob height in model units. Default 200; overwritten with `Renderable.anInt1426` of the assembled appearance model in `Player.method444`. Consumed by `method127` overhead drawing. |
| `anInt1511` | `int` | idleAnim | Standing/idle sequence id. Player gets it from appearance packet; NPC from `aClass5_1696.anInt77`. -1 = none. |
| `anInt1512` | `int` | idleTurnAnim | Sequence used while standing and rotating (`method100`). Inferred "turn-on-spot". |
| `anInt1554` | `int` | walkAnim | Forward walk sequence id. NPC from `anInt67`. |
| `anInt1555` | `int` | walkBackAnim | Walking backward / facing-away anim. |
| `anInt1556` | `int` | walkLeftAnim | Strafe-left / left-relative anim. |
| `anInt1557` | `int` | walkRightAnim | Strafe-right / right-relative anim. |
| `anInt1505` | `int` | runAnim | Anim used when run flag pushes step rate above 8 (`method99` line 6790). |
| `anInt1517` | `int` | currentAnim | Currently playing sequence id (idle/walk/run chosen by `method99/100`). |
| `anInt1518` | `int` | currentAnimFrameIndex | Current frame index inside the SequenceDefinition. |
| `anInt1519` | `int` | currentAnimFrameTicks | Per-frame tick accumulator advanced in `method101`. |
| `anInt1520` | `int` | spotAnim | Active SpotAnimation id overlay (e.g. spell graphics). -1 none. |
| `anInt1521` | `int` | spotAnimFrameIndex | Frame index inside spot-anim sequence. |
| `anInt1522` | `int` | spotAnimFrameTicks | Spot-anim tick accumulator. |
| `anInt1523` | `int` | spotAnimStartTick | Tick at which spot-anim begins playing. |
| `anInt1524` | `int` | spotAnimHeight | Z-offset (negative-Y in `method444`) applied to spot-anim model. |
| `anInt1526` | `int` | forcedAnim | Forced animation id (priority animation, e.g. emote/hit). Cleared after one play. Suppressed when `SequenceDefinition.anInt364 == 1` and not idle (see `method445/448`). |
| `anInt1527` | `int` | forcedAnimFrameIndex | Frame index in forced anim. |
| `anInt1528` | `int` | forcedAnimFrameTicks | Tick accumulator (`method101`). |
| `anInt1529` | `int` | forcedAnimDelay | Pending delay countdown for forced anim transition (`method101`). |
| `anInt1530` | `int` | forcedAnimLoopCount | Loop counter — capped by `class20.anInt362` (`method101`). |
| `anInt1531` | `int` | overheadIconType | Skull / prayer overhead icon class (0 chat, 1..3 skull tiers, 4 PK, 5 etc — values inferred from method127 branches at GameClientCore:1400-1410). |
| `anInt1532` | `int` | hitMarkerEndTick | Tick after which legacy hit splat hides (`method127`). |
| `anInt1533` | `int` | currentHp | HP for above-head health bar (used as `currentHp*30/maxHp` in GameClientCore:1419). |
| `anInt1534` | `int` | maxHp | HP denominator for the bar. |
| `anInt1535` | `int` | chatTimer | Ticks remaining for overhead chat (`aString1506`). Decremented in main update; clears `aString1506` at 0. |
| `aString1506` | `String` | overheadChat | Public chat string to render above head. |
| `aString1703` (Player) | n/a | n/a | (lives on Player) — display name. |
| `anIntArray1514[4]` | `int[]` | hitSplatDamage | Hit-splat ring values (damage). `method447` writes oldest empty slot. |
| `anIntArray1515[4]` | `int[]` | hitSplatType | Hit-splat ring sprite/colour ids. |
| `anIntArray1516[4]` | `int[]` | hitSplatExpiryTick | Tick at which slot expires (set to `currentTick + 70`). |
| `aBoolean1508` | `boolean` | hitParityFlip | XOR'd in `method447` when an unknown control byte changes — purpose unclear, appears to be a stale-flag toggle used as a tie-breaker. |
| `anInt1509` | `int` | hitControlSentinel | Constant `-35698`; the `i` param to `method447` is checked against it. Appears to be an obfuscation guard / wrong-arg trap (`-35698` is the value also passed by all real callers). |
| `anInt1543` | `int` | forcedMoveSrcTileX | First tile of in-progress server-pushed forced move (tween source). Used in `method97/98`. |
| `anInt1545` | `int` | forcedMoveSrcTileY | Source tile Y. |
| `anInt1544` | `int` | forcedMoveDstTileX | Destination tile X (`method98` only). |
| `anInt1546` | `int` | forcedMoveDstTileY | Destination tile Y. |
| `anInt1547` | `int` | forcedMoveStartTick | Tween start tick. `method96` branches: if `>currentTick` snap to source via `method97`. |
| `anInt1548` | `int` | forcedMoveEndTick | Tween end tick. If `>= currentTick` interpolate via `method98`; else normal walk. |
| `anInt1549` | `int` | forcedMoveFacing | 0=W(1024), 1=S(1536), 2=E(0), 3=N(512) — facing snap applied after forced move (`method97/98`). |
| `aBoolean1541` | `boolean` | animLocksFacing | Mirrors `SequenceDefinition.aBoolean358` of the current forced anim — when true, GameClientCore skips face/turn updates. Read at `method285` calls. |
| `anInt1542` | `int` | queuedAnimCycles | "Steps before forced anim plays" counter — when >0 holds anim and decrements with each step (`method99`/`method101`). Cleared by `method446`. |
| `anInt1502` | `int` | combatTargetIndex | Faced/combat target: -1 none, `<32768` NPC index into `aClass30_Sub2_Sub4_Sub1_Sub1Array835`, `>=32768` `(idx-32768)` is Player index into `aClass30_Sub2_Sub4_Sub1_Sub2Array890`. `method100` uses atan2 toward target. |
| `anInt1513` | `int` | chatColor | Overhead chat colour (set at GameClientCore:1399). |
| `anInt1536` | `int` | walkAnimResetMark | Private. Set to 42 by `method445` when `flag1` true. Purpose unclear — appears to flag "teleport snap, reset interpolation"; value never read inside Actor itself in this file but the sentinel `-895` initial value suggests legacy/dead code path. |
| `anInt1537` | `int` | lastUpdateTick | "Last tick this actor was rebuilt from server" — set to `anInt1161` in PlayerUpdateMaskHandler and GameClientCore:1131/2017/6438/8554+; read at GameClientCore:9339 to skip removal of recently-updated actors. |
| `anInt1538` | `int` | faceTileX | Forced-facing tile X (region-relative, offset by `anInt1034`). Cleared by `method100` after use. |
| `anInt1539` | `int` | faceTileY | Forced-facing tile Y. |

### Methods

#### `Actor()` (constructor, lines 138–166)
- Signature: package-private no-arg.
- Purpose: zero-init defaults. Allocates the 10-deep walk queue arrays
  (`anIntArray1500/1501/aBooleanArray1553`) and the 4-slot hit-splat arrays
  (`anIntArray1514/1515/1516`). Sentinels: `-1` for "no anim" slots
  (`anInt1502/1505/1511/1512/1517/1520/1526/1554..1557`), `-1000` for
  `anInt1532` (hit-marker end-tick, ensures hidden initially), `-895` for
  `anInt1536`, `-35698` for `anInt1509`, `200` for `anInt1507` (default
  model height), `32` for `anInt1504` (default turn speed), `100` for
  `anInt1535`, `1` for `anInt1540` (size).
- Called-by: `Player()` (via `super()`) and `Npc` constructors.

#### `public final void method445(int i, int j, boolean flag, boolean flag1)` (13–47)
- Purpose: enqueue a new walk-queue head at tile (`i`,`j`). This is the
  "server told us to walk to tile" handler.
- Parameters:
  - `i`, `j`: new tile X/Y.
  - `flag`: true => unconditional snap (skip the local-step optimisation).
  - `flag1`: true => set `anInt1536 = 42` (teleport/reset marker).
- Behaviour:
  - Cancels the current forced anim if its `SequenceDefinition.anInt364 == 1`
    (animation is configured to not survive a movement event).
  - If `!flag` and target is within ±8 tiles of current head, shift queue
    forward (push the new tile onto front of queue) preserving up-to-9
    queued positions and `aBooleanArray1553`. This is the normal one-step
    push for local routing.
  - Otherwise (far jump / teleport): clear queue (`anInt1525=0`,
    `anInt1542=0`, `anInt1503=0`), seed slot 0 with target, snap
    `anInt1550/anInt1551` to the world centre of that tile.
- Returns: void.
- Called-by: `GameClientCore.method79` style flows at lines 2036, 6449, 7761
  (local-player movement updates); also indirectly via 7735/7744/7746/8569
  paired with `method448`.
- Calls: none external.
- Notes: deep-copy loop runs `i1 = anInt1525 .. 1` because `anInt1525` was
  already incremented; correct queue-shift idiom.

#### `public final void method446(boolean flag)` (49–57)
- Purpose: reset walk queue ("stop moving / clear path").
- Parameters: `flag` — JAD opaque-predicate guard. When `false` triggers an
  infinite loop (`for(int i = 1; i > 0; i++);`) — classic deobfuscation
  trap. All real callers pass `true`.
- Behaviour: `anInt1525 = 0`, `anInt1542 = 0`. Walk-queue contents not
  cleared (only the count), so stale slots remain but unreachable.
- Called-by: `PlayerUpdateMaskHandler.java:121` (mask-clear), and
  `GameClientCore.java:6633/6643` (out-of-bounds emergency reset in
  `method96`).
- Notes: `anInt1503` (walk-pause) is NOT cleared here, only by `method445`'s
  snap branch.

#### `public final void method447(int i, int j, int k, int l)` (59–72)
- Purpose: register a hit-splat. Inferred from the 4-slot ring + tick-expiry
  pattern.
- Parameters:
  - `i`: guard value, must be `-35698` (`anInt1509`). Different value flips
    `aBoolean1508` and the splat is NOT recorded (parity-flip side effect).
  - `j`: hit type / colour index (stored in `anIntArray1515`).
  - `k`: damage value (stored in `anIntArray1514`).
  - `l`: current game tick — splat expires at `l + 70`.
- Behaviour: finds first slot `i1` whose stored expiry `<= l` (i.e. free or
  expired) and overwrites it with new values + `l+70` expiry. If no free
  slot is found, only the parity-flip occurs.
- Called-by: `NpcUpdateMaskHandler.java:78/115` and
  `PlayerUpdateMaskHandler.java:155/165` — damage masks. Always pass
  `-35698` for `i`.

#### `public final void method448(boolean flag, byte byte0, int i)` (74–127)
- Purpose: push one walk-step relative to the head of the walk queue, given
  a movement direction code 0..7.
- Parameters:
  - `flag`: run flag for this step (stored in `aBooleanArray1553[0]`).
  - `byte0`: opaque-arg guard. Must be `20`; otherwise the method returns
    after the queue-shift without committing the new tile (effectively
    no-ops; another deobfuscation guard).
  - `i`: direction 0..7 — diagonals included
    (0=NW, 1=N, 2=NE, 3=W, 4=E, 5=SW, 6=S, 7=SE), inferred from the
    `j--/k++` ladders.
- Behaviour:
  - Cancels expired forced anim (same `anInt364 == 1` check as `method445`).
  - Pushes queue forward by one (same shift-up loop as `method445`).
  - If guard passes, writes new head at (j,k) and the run flag.
- Called-by: `GameClientCore.java:7735/7744/7746` (local prediction during
  movement) and `:8569/8579/8581/9088/9098/9100` (server step packets for
  other players/NPCs).

#### `public boolean method449(boolean flag)` (129–136)
- Purpose: "is this actor live/visible" predicate. Base impl always returns
  `false`; `Player` overrides to return `aBoolean1710` (appearance loaded).
  Inferred function: tells consumers a renderable+ready check before
  trying to draw or interact.
- Parameters: `flag` — JAD opaque guard. False causes infinite loop.
- Returns: `false` always at the Actor level.
- Called-by: `GameClientCore.java:827, 1340, 2079, 8171, 8188` — gating
  scene-graph submission, menu hit-tests, and entity-tick iteration.
  `aBoolean1224` is passed as the guard.
- Notes: declared `public boolean` (no `final`) precisely so `Player`
  can override.

---

## Player.java

### Overview
File: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Player.java`,
396 lines, package-private `final`. Extends `Actor`. Adds: identity-kit
(head/body/leg/etc. body-part style ids 256..511), worn-equipment ids
(512+), recolouring palette swap indices, gender flag, display name + chat
prefix, NPC-morph definition (for NPC-shaped players), forced-overlay
model with start/end ticks, and the model assembly pipeline that combines
all of the above with the current Actor animation into a per-frame `Model`.

The class caches the composed `Model` in a static `NodeCache aClass12_1704`
keyed by an appearance-and-anim hash (`aLong1718` plus packed sequence
overrides), so identical avatars in identical poses share GPU work.

### Fields

| legacy | type | inferred name | purpose / callers |
| --- | --- | --- | --- |
| `aLong1697` | `long` | lastAppearanceKey | Cache key of most recently built appearance, used as fallback when missing-asset flag forces a stale-but-valid model fetch (`method452:240-243`). |
| `aClass5_1698` | `NpcDefinition` | npcMorph | Non-null when player is morphed into an NPC (appearance byte 0 sentinel `65535` triggers this — see `method451:112-116`). When set, `method452`/`method453` short-circuit to NPC model. |
| `aBoolean1699` | `boolean` | skipAnimation | When true, return the assembled body model without applying the current animation (raw rest pose). Set externally — purpose appears to be "thumbnail/title-screen" or design-room view. |
| `anIntArray1700[5]` | `int[]` | recolourPaletteIndices | Five palette-swap indices into `GameClient.anIntArrayArray1003` (hair, torso, legs, feet, skin — order inferred). 0 means no recolour. |
| `anInt1701` | `int` | weaponStanceGroup | Equipped weapon's `anInt202`; non-zero overrides default stance. |
| `anInt1702` | `int` | gender | 0/1 from appearance packet, drives gender-aware `ItemDefinition.method192/194/195/196` and is folded into cache key. |
| `aString1703` | `String` | displayName | Decoded username after `TextUtils.method584/method587`. Read by GameClientCore:5078/5081/5083 when emitting chat messages and at :1390 to filter overhead chat by friend-list. |
| `aClass12_1704` (static) | `NodeCache` | appearanceModelCache | Shared cache for composed appearance Models. Capacity 260. Filled by `method452:282`. |
| `anInt1705` | `int` | combatLevel | Player combat level from appearance packet (`method408()` at :155). Purpose inferred from packet layout. |
| `anInt1706` | `int` | flags | Misc appearance flags byte from packet (`method408()` at :97). Purpose unclear — appears to be skull/PK status. |
| `anInt1707` | `int` | forcedModelStartTick | Start tick for `aClass30_Sub2_Sub4_Sub6_1714` overlay. |
| `anInt1708` | `int` | forcedModelEndTick | End tick; overlay cleared past this. |
| `anInt1709` | `int` | renderHeight | World-Z (height above terrain) of overlay anchor. Written by GameClientCore:2091/2101 via `method42` (terrain elevation lookup). |
| `aBoolean1710` | `boolean` | appearanceReady | True once `method451` has populated kit/equipment. Returned by overridden `method449`. |
| `anInt1711` | `int` | forcedModelStartWorldX | Source world X (matching `Actor.anInt1550` scale) for the overlay tween. |
| `anInt1712` | `int` | forcedModelStartHeight | Source world Z for overlay tween. |
| `anInt1713` | `int` | forcedModelStartWorldY | Source world Y. |
| `aClass30_Sub2_Sub4_Sub6_1714` | `Model` | forcedOverlayModel | Optional extra model attached to the player (e.g. tornado, soul shroud) that translates from start pos to current pos over [1707,1708]. |
| `anInt1715` | `int` | spotAnimScaleFallback | Default value 9, overwritten to 132 inside `method453` as side-effect when `byte0 != -41` (opaque-arg guard). Purpose unclear — appears to be a guard sentinel, never functionally consumed. |
| `aBoolean1716` | `boolean` | drawFlagParity | Toggled by `method444` when `i != 4016` (opaque-arg guard). Purpose unclear — flip-flag side effect of the trap. |
| `anIntArray1717[12]` | `int[]` | appearanceSlots | The 12-slot per-player appearance vector. Values: `0` empty, `1..255` reserved, `256..511` IdentityKit body-part id `(v-256)`, `512+` worn ItemDefinition id `(v-512)`. Slot order (legacy 525 layout): 0 head, 1 cape, 2 amulet, 3 weapon, 4 torso, 5 shield, 6 (unused/legs?), 7 legs, 8 (unused/hair?), 9 hands, 10 feet, 11 beard. (Order inferred from RuneScape 525 conventions; the only explicit branches in this file are `i2==3` and `i2==5` getting overridden by sequence's `anInt360/anInt361`, consistent with weapon/shield SequenceDefinition overrides.) |
| `aLong1718` | `long` | appearanceHash | 64-bit hash composed of the 12 appearance slot ids + 5 recolours + gender; used as cache key in `method452`. |
| `anInt1719` | `int` | speakOverheadOffset | Read at GameClientCore:2092 in chat-bubble layout. Purpose inferred. |
| `anInt1720` | `int` | renderWidth1 | Width arg into `Class25.method286`. Purpose unclear — appears to be model bound. |
| `anInt1721` | `int` | renderWidth2 | Second width arg into `method286`. |
| `anInt1722` | `int` | renderDepth | Depth arg into `method286`. |
| `anInt1723` | `int` | misc / team | u16 trailing the appearance packet (`method410()` at :156). Purpose unclear — appears to be team/clan id. |

### Methods

#### `Player()` (constructor, 358–367)
- Initialises caches: `aLong1697 = -1L`, `aBoolean1699 = false`,
  `aBoolean1710 = false`, `aBoolean1716 = true`, `anInt1715 = 9`. Allocates
  `anIntArray1700[5]` and `anIntArray1717[12]`. Calls `super()` implicitly.
- Called-by: `GameClientCore` player-array population (6434 area).

#### `public final Model method444(int i)` (13–91) — overrides `Renderable.method444`
- Purpose: assemble and return the per-frame appearance Model that the
  scene graph submits this tick. This is the rendering hot-path.
- Parameters:
  - `i`: opaque-arg guard. Must be `4016`. Other values flip
    `aBoolean1716` (no functional effect) and proceed normally — JAD-style
    obfuscation marker rather than real input.
- Behaviour:
  1. Returns `null` if appearance not yet loaded (`!aBoolean1710`).
  2. Calls private `method452(0)` to obtain (or build+cache) the animated
     body model. Returns null on failure.
  3. Copies `Renderable.anInt1426` of the result into
     `super.anInt1507` (mob height) so overhead UI knows current model bounds.
  4. Sets `Model.aBoolean1659 = true` to mark "ready".
  5. If `aBoolean1699` (skip-animation flag) is true, returns immediately.
  6. **Spot-animation overlay**: if `super.anInt1520 != -1` and `anInt1521
     != -1` (active spot anim), grabs `SpotAnimationDefinition`, builds its
     model via `method266`, clones-with-anim into a new `Model(9, true,
     AnimationFrame.method532(frameId), false, base)`, translates by
     (0, -spotAnimHeight, 16384, 0), normalises (`method469((byte)-71)`),
     applies the active frame (`method470`), scales using `anInt410/411`,
     applies lighting (`method479`), then merges with body Model via
     `new Model(2, -819, true, {body, spot})`.
  7. **Forced overlay model**: if `aClass30_Sub2_Sub4_Sub6_1714 != null`:
     - Clear it when past `anInt1708`.
     - Otherwise within [anInt1707, anInt1708] window:
       - Translate the overlay to its (start - current) delta in world
         space so it stays anchored to its start tile while the player
         walks; (anInt1711/1712/1713 is the start, super.anInt1550/anInt1709/
         anInt1551 is current).
       - Apply 1, 2, 3, or 0 calls to `method473` (90° rotation steps)
         to align with `super.anInt1510 / 512` cardinal direction.
       - Merge with body via `new Model(2, -819, true, {body, overlay})`.
       - Then reverse the rotations and translation so the overlay model
         object itself is unmodified (idempotent transform pattern).
  8. Re-flags `aBoolean1659 = true` and returns final composite Model.
- Returns: composite per-frame `Model`, or `null` if not ready.
- Called-by: `Renderable` polymorphic dispatch from `SceneGraph` /
  `GameClientCore` rendering loop (no direct grep hits because polymorphic
  — invoked through `Renderable.method444` virtual call).
- Calls: `method452`, `SpotAnimationDefinition.method266`, `AnimationFrame.method532`,
  `Model.method469/470/473/475/478/479`.

#### `public final void method451(int i, PacketBuffer class30_sub2_sub2)` (93–178)
- Purpose: decode the player-appearance sub-packet (variable-length payload
  inside the player-update mask). Populates kit/equipment/recolour/gender/
  name/combat-level + the four animation override slots (idle/walk-back/
  walk-left/walk-right/turn) + runs default-fight anim.
- Parameters:
  - `i`: opaque guard. Real caller passes `0`. Other values early-return.
  - `class30_sub2_sub2`: source `PacketBuffer`. Reset to offset 0
    (`anInt1406 = 0`) on entry.
- Behaviour:
  1. Read `anInt1702` (gender) u8, `anInt1706` (flags/skull) u8.
  2. Clear morph (`aClass5_1698 = null`), reset weapon stance.
  3. Loop 12 slots: each entry is u8; if `0`, slot empty; otherwise u8 high
     + u8 low packed into u16 stored in `anIntArray1717`. First slot value
     of `65535` means "morph into NPC" — read NPC id u16 and break.
  4. For each non-empty equipment slot (>=512): if it carries a custom
     stance group (`ItemDefinition.anInt202`), set `anInt1701`.
  5. Loop 5 recolour palettes: each entry u8, clamped to
     `GameClient.anIntArrayArray1003[l].length`, stored in `anIntArray1700`.
  6. Read 5 SequenceDefinition u16 ids into super fields with `65535 -> -1`
     normalisation: `anInt1511` (idle), `anInt1512` (idle-turn),
     `anInt1554` (walk-fwd), `anInt1555` (walk-back), `anInt1556`
     (walk-left), `anInt1557` (walk-right), then `anInt1505` (run).
  7. Read display name (chat-string) and decode via `TextUtils`.
  8. Read `anInt1705` (combat level) u8, `anInt1723` u16.
  9. Set `aBoolean1710 = true` (appearance now valid).
  10. Compute `aLong1718` cache hash by packing the 12 slot ids (low nibbles
      via `<<4`), plus high-bit overflow for first two slots, plus the 5
      palette indices (3 bits each), plus gender (1 bit). Resulting hash
      uniquely identifies the avatar's body composition for the model cache.
- Returns: void.
- Called-by: `PlayerUpdateMaskHandler.java:79` (appearance mask handler);
  `GameClientCore.java:6434` (initial player slot population).
- Notes: order of reads is brittle — exactly matches the packet emitted by
  the legacy server. The 12-slot vector mixes kit and equipment because
  equipment supersedes kit per slot (head, body, legs share slot indices
  with hair, torso, legs in the source emitter).

#### `private final Model method452(int i)` (180–298)
- Purpose: build the body Model with the current animation applied. The
  workhorse that:
  - Branches to NPC morph if `aClass5_1698 != null`.
  - Computes the active frame id `k` from forced-anim
    (`super.anInt1526`/`anInt1527`) when not delayed, else from
    `super.anInt1517`/`anInt1518` (idle/walk).
  - Computes interpolation frame id `i1` when both forced + secondary anim
    are active and differ (for `Model.method471` blend).
  - Extracts kit overrides `j1`/`k1` from `SequenceDefinition.anInt360/361`
    — sequence definitions can replace slots 3 and 5 of the appearance
    vector while the anim plays (e.g. weapon visible only while attack
    anim is active). These overrides are folded into the cache key by
    XOR-shifting them into the upper bits of `l` (the cache key copy of
    `aLong1718`).
  - Looks up the composed model in `aClass12_1704`. On miss:
    - Walks the 12 slots, swapping in `j1`/`k1` for slots 3 and 5 when
      override present.
    - For each slot: 256..511 -> `IdentityKitDefinition.method538` body
      model; 512+ -> `ItemDefinition.method196` equip model (gender-aware
      via `anInt1702`).
    - If any source asset isn't loaded
      (`IdentityKitDefinition.method537` false or
      `ItemDefinition.method195` false), set `flag = true` and try to
      reuse last cached model (`aLong1697`); if still null, return null
      (will retry next frame after caches load).
    - Combine collected `aclass30_sub2_sub4_sub6[]` into
      `new Model(j2, parts, -38)`.
    - Apply each non-zero recolour palette (`method476`); slot 1
      (torso) gets a secondary swap from `GameClient.anIntArray1204` too.
    - Normalise (`method469`) + light (`method479(64, 850, -30, -50, -30,
      true)`).
    - Insert into cache under key `l`, set `aLong1697 = l`.
  - If `aBoolean1699`, return body without anim.
  - Otherwise stamp the animation onto a shared scratch
    `Model.aClass30_Sub2_Sub4_Sub6_1621` using `method464(7, body,
    mask)` where mask = AND of the two frames' affected groups.
    - If both `k` and `i1` valid: `method471(-20491, classifierGroups,
      i1, k)` (blend interpolation).
    - Else if `k` valid: `method470(k, 40542)` (single-frame apply).
    - Call `method466(false)` (purpose unclear — appears to finalise
      transformed vertices/normals).
    - Null out `anIntArrayArray1657/1658` (drops per-vertex animation
      side tables to avoid leaking state across actors sharing the scratch).
- Parameters:
  - `i`: opaque guard. Must be `0` for the early-exit infinite loop
    trap not to fire (`if(i != 0) for(int l1=1; l1>0; l1++);`). Real
    caller passes `0`.
- Returns: the per-frame Model, or null if assets missing on first build.
- Called-by: `method444` only.
- Calls: virtually every `Model.method46x/47x` and the kit/item/sequence
  asset loaders. This method is the bridge between the cache subsystem
  and the renderer.

#### `public final boolean method449(boolean flag)` (300–305) — overrides `Actor.method449`
- Purpose: "is this Player live and renderable?" predicate.
- Parameters: `flag` — opaque guard. False throws NPE; all real callers
  pass `aBoolean1224`.
- Returns: `aBoolean1710` (appearance has been decoded at least once).
- Called-by: same `GameClientCore` sites listed for `Actor.method449` —
  e.g. lines 827, 1340, 2079, 8171, 8188, and `GameClientCore:827` reads
  it specifically through a `Player` reference for additional fields.

#### `public final Model method453(byte byte0)` (307–356)
- Purpose: build the head-only model used by the chat-message portrait
  ("PM heads", overhead chat heads in the chatbox). Same composition as
  `method452` but using `IdentityKitDefinition.method540(0)` /
  `ItemDefinition.method194(-705, gender)` (the "head" model variants),
  WITHOUT animation and WITHOUT caching.
- Parameters: `byte0` — opaque guard. Must be `-41`; otherwise overwrites
  `anInt1715 = 132` as side effect. No functional impact, no return change.
- Returns: head Model, or `null` if appearance not ready or any required
  head model not yet loaded; or NPC head via `NpcDefinition.method160(true)`
  when morphed.
- Called-by: `Widget.java:265` — `GameClient.aClass30_Sub2_Sub4_Sub1_Sub2_1126.method453((byte)-41)`
  — i.e. the local player's head for chatbox/PM rendering.

---

## How Player extends Actor

`Actor` carries the per-tick state every mob needs (position tween, walk
queue, animation cursors, hit splats, forced overlays, target). `Player`
adds the per-avatar *identity* (kit + worn equipment + colours +
display name + gender + optional NPC morph) and overrides exactly two
inherited virtuals:

- `method444(int)` — `Renderable`'s "produce my drawable Model this frame"
  hook. `Player` returns a composite of the cached body model (built via
  `method452` using the inherited animation cursors `anInt1517`/`anInt1518`
  /`anInt1526`/`anInt1527`) plus optional spot-anim and forced-overlay
  layers, then writes back `super.anInt1507` so the parent's overhead-UI
  math stays in sync with the actual model height.
- `method449(boolean)` — base returns `false`; Player returns
  `aBoolean1710` so the scene graph and menu logic gate on whether the
  appearance packet has arrived.

Tick driver (`GameClientCore.method96 -> method97/98/99/100/101`) treats
both Players and Npcs uniformly through the `Actor` base type — only at
render time does the virtual `method444` dispatch reach Player's
identity-aware composition pipeline.

## Cross-class call graph

```
PlayerUpdateMaskHandler ─┬─> Player.method451 (appearance packet -> kit/equip/recolour/name)
                         ├─> Player.method446 (clear walk queue)
                         └─> Player.method447 (hit splat ring)

NpcUpdateMaskHandler ────> Npc/Actor.method447 (hit splat)

Widget.draw ─────────────> Player.method453 (chatbox head model)

SceneGraph render loop ──> Renderable.method444 (virtual) ─> Player.method444
                                                              │
                                                              ├─> Player.method452 ─┬─> NodeCache.method222/223 (cache)
                                                              │                     ├─> IdentityKitDefinition.method537/538 (body parts)
                                                              │                     ├─> ItemDefinition.method195/196 (equip body)
                                                              │                     ├─> SequenceDefinition.aClass20Array351 (anim ids)
                                                              │                     ├─> AnimationFrame.method532 (group mask)
                                                              │                     └─> Model.method464/466/469/470/471/476/479
                                                              │                         (combine, finalise, normalise, apply,
                                                              │                          interpolate, recolour, light)
                                                              ├─> SpotAnimationDefinition.method266 (overlay model)
                                                              └─> Model.method475/478/479/473
                                                                  (translate/scale/light/rotate for overlays)

GameClientCore.method96 ─┬─> Actor.method446 (out-of-bounds reset)
                         ├─> Actor.method97  (forced-move pre-window)
                         ├─> Actor.method98  (forced-move window)
                         ├─> Actor.method99  (consume walk queue, pick anim)
                         ├─> Actor.method100 (face target, smooth turn)
                         └─> Actor.method101 (advance anim cursors)

GameClientCore.method79 ─┬─> Player.method445 (snap/push walk target)
                         └─> Player.method448 (direction-coded step)

GameClientCore menu/scene gating ─> Player.method449 (renderable?)
```
