# GameClientCore Chunk D (lines 6527–7919) — legacy moparscape

Source: [GameClientCore.java](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java)

## Chunk overview

This chunk groups three loosely related concerns inside the legacy obfuscated
client:

1. **Boot / loader error painter and applet plumbing** — `method94`
   (file:6527), `getCodeBase` (file:6588).
2. **Actor (Player/NPC) per-tick movement/animation pipeline** — `method95`
   (file:6607) drives all NPCs; `method96`–`method101` (file:6621–6946) form a
   per-actor walk/run/interpolation/orientation/animation step; `method108`
   (file:7305) does the camera follow for the local player; `method114`
   (file:7583) does the same per-tick pass for all visible players.
4. **Frame-rendering glue / minimap-area chrome** — `method102` (file:6948) is
   the master "redraw chrome surfaces" routine that repaints the chat,
   tab-bar, side-panel sprite chrome and the in-game settings tab using
   `GameFrameHandler.*` helpers; `method112` (file:7470) layers in-world
   overlays each frame (mouse, FPS/MEM debug, system-update timer, scroll
   widgets).
5. **Widget tree animation/render and context menu plumbing** — `method103`
   (file:7129) builds friend/ignore right-click menu entries; `method105`
   (file:7171) renders an entire `Widget` subtree via `WidgetTreeRenderer`;
   `method116` (file:7640) measures and positions the right-click context
   menu against the three legal anchor areas (main viewport / inventory /
   chatbox); `method119` (file:7794) walks a `Widget` tree and advances its
   `SequenceDefinition` frame counters.
6. **Misc utilities** — `method109` (file:7423) is-friend predicate;
   `method110` (file:7436) combat-level diff colour code; `method111`
   (file:7461) sets `SignLink.wavevol`; `method113` (file:7542) ignore-list
   add; `method118` (file:7765) tears down sprite/array references when
   shutting the game loop down; `method120` (file:7838) ray-tests
   `aByteArrayArrayArray1258` between camera and player to pick a render
   plane.
7. **Helpers extracted to handler classes** — `method106` (file:7214) bakes a
   smoke/water-style cloudy backdrop into `anIntArray1190/1191`; `method107`
   (file:7300) forwards to `applyPlayerUpdateMasks`; `method115` (file:7601)
   ticks the queued `SceneObjectSpawnRequest` list; `method117` (file:7716)
   reads the local-player movement update block; `method104` (file:7166)
   forwards to `SceneEffectHandler.updateGraphicsObjects`; `method9`
   (file:7406) is the top-level main-loop draw entry which decides between
   error painter / loading screen / live frame; new helpers
   `setWidgetDragOffsetY` (file:7209), `applyPlayerPublicChatUpdate`
   (file:7264), `applyPlayerMaskStateGuard` (file:7269) and
   `applyPlayerUpdateMasks` (file:7275) appear as the partially-refactored
   bridges to extracted handler classes.

## Static/instance fields touched (only fields first introduced in your range)

This chunk reads/writes pre-existing fields; no new fields are declared in the
range. Notable first-time use of locally-scoped names:

- `dragPointerY` (file:7211) — written by `setWidgetDragOffsetY`; this is the
  package-visible storage backing `getDragPointerY()`; the chunk is the first
  place to mutate it via a dedicated setter.

All other state is shared with earlier chunks (`aBoolean926`, `aBoolean1252`,
`aBoolean1176`, `aBoolean1255`, `aBoolean1153/1223/1103/1233`, `aBoolean885`,
`aBoolean1157`, `aBoolean991`, `aBoolean831`, `aBoolean962`, `aBoolean1156`,
`aBoolean1043`, `aBoolean1149`, `aBoolean1224`, `aBoolean919`, `aBoolean960`,
the `aClass15_*` chrome render targets, `aClass30_Sub2_Sub1_Sub2*` sprites,
`aClass30_Sub2_Sub1_Sub4_1271/1272` typeface helpers, `aClass30_Sub2_Sub2_1192`
out-going packet buffer, `anInt1023` (login state), `anInt1054/1221`
(tab-bar), `anInt1018/857/1189/1276/1042/1116` (active widget ids),
`anInt1014/1015/1184/1185/1186/1187/984` (camera tracking),
`anInt939/1287/845/1248/1104/849`, `friendCount/friendNameHashes/
friendDisplayNames/ignoreCount/ignoredNameHashes`, `aByteArrayArrayArray1258`
collision-flag grid, `anIntArrayArrayArray1214` heightmap, `anInt918`
plane, `anInt945` (elapsed-ticks accumulator), `anInt836/anIntArray837/
aClass30_Sub2_Sub4_Sub1_Sub1Array835` NPC slots, `anInt891/anIntArray892/
aClass30_Sub2_Sub4_Sub1_Sub2Array890` player slots, `anInt889`/`anInt884`
local-player index, `anInt893/anIntArray894` movement-update queue,
`aClass19_1056` (graphics-object list), `aClass19_1179`
(SceneObjectSpawnRequest list)).

## Methods

### method94(int i) — file:6527

- **Signature**: `public final void method94(int i)`
- **Purpose**: Paints the boot-time error screens. Renders one of three
  mutually-exclusive messages: generic load-failure (`aBoolean926`), wrong
  host (`aBoolean1176`), or "client already loaded" (`aBoolean1252`). Draws
  directly on the buffer at `method11(0)`.
- **Parameters**: `i` — obfuscator dummy; if `!= -13873` enters an
  intentional infinite loop (`for(int j = 1; j > 0; j++)`) as a tamper
  check.
- **Returns**: void.
- **Called by**: `method9` (file:7410) when any of the three boot-error
  flags is set.
- **Calls**: `method11(0)` (offscreen buffer accessor in chunk A);
  `method4(false, 1)` (push-buffer-to-canvas in chunk A);
  `Graphics.fillRect`/`drawString`/`setFont`/`setColor`.
- **Notes**: Also clears `aBoolean831` (game-running flag) on each path.
  Hard-coded resolution 765x503 confirms the legacy fixed-resolution
  viewport.

### getCodeBase() — file:6588

- **Signature**: `public final URL getCodeBase()`
- **Purpose**: Overrides `Applet.getCodeBase` to support three deployment
  modes: embedded host (returns the configured server endpoint's code-base
  URL), `SignLink.mainapp` (delegates), or running inside a frame
  (`super.aFrame_Sub1_15 != null`).
- **Returns**: a `URL` for relative resource lookup; falls back to
  `super.getCodeBase()` if no special mode applies.
- **Called by**: `getAppletContext().showDocument(new URL(getCodeBase(),
  ...))` at file:860 — the load-error redirect path.
- **Calls**: `getConfiguredServerEndpoint().toCodeBaseUrl()` (modern bridge
  helper); `SignLink.mainapp.getCodeBase()`.
- **Notes**: Swallows exceptions silently — typical legacy applet-host
  behaviour.

### method95(int i) — file:6607

- **Signature**: `public final void method95(int i)`
- **Purpose**: Per-tick NPC advance — iterates over the `anInt836` active
  NPC indices in `anIntArray837` and runs `method96` against each, passing
  the NPC's `aClass5_1696.aByte68` movement-size byte.
- **Parameters**: `i` — obfuscator dummy (also opportunistically writes
  `anInt1218` when not the magic value).
- **Returns**: void.
- **Called by**: `method19` at file:2966 (the per-tick world-update entry
  point in chunk B).
- **Calls**: `method96`.
- **Notes**: NPCs that are `null` (in-slot but garbage-collected) are
  skipped.

### method96(int i, int j, Actor actor) — file:6621

- **Signature**: `public final void method96(int i, int j, Actor actor)`
- **Purpose**: Master per-actor movement/animation tick. Validates that the
  actor's `anInt1550/1551` (world-pixel x/y) sit inside the legal regions
  (with a tighter clamp for the local player at `aClass30_Sub2_Sub4_Sub1_
  Sub2_1126`), then dispatches to one of `method97` (interpolated server-
  pushed movement), `method98` (mid-server-step interpolation) or
  `method99` (queued step movement) depending on `anInt1547` vs current
  tick `anInt1161`. Finally runs `method100` (head turn) and `method101`
  (frame advance).
- **Parameters**: `i` magic-guard (`46988`); `j` unused-after-guard; `actor`
  the target.
- **Returns**: void.
- **Called by**: `method95` (file:6614) for NPCs; `method114` (file:7596)
  for players.
- **Calls**: `Actor.method446`, `method97`, `method98`, `method99`,
  `method100`, `method101`.
- **Notes**: When the actor is out of bounds, the queued path is cleared
  (`anInt1547/1548 = 0`) and the actor is teleported to its base
  coordinate.

### method97(Actor actor, boolean flag) — file:6656

- **Signature**: `public final void method97(Actor, boolean)`
- **Purpose**: "Force movement" interpolation — moves an actor toward the
  cached destination cell using a per-tick fraction `(dest - cur) / i`
  where `i = anInt1547 - anInt1161`. When `flag` is true (the only call
  site), also updates the Y component and snaps `anInt1510` (facing) to one
  of four cardinal angles based on `anInt1549` (force-movement direction
  code 0-3 → NORTH/EAST/SOUTH/WEST as 1024/1536/0/512).
- **Parameters**: `actor` target; `flag` whether to also update Y.
- **Returns**: void.
- **Called by**: `method96` (file:6646).
- **Calls**: none (pure field math).
- **Notes**: Resets `anInt1503` (idle-orientation counter) and writes
  `anInt1510` only (does not touch `anInt1552`).

### method98(Actor actor, byte byte0) — file:6676

- **Signature**: `public final void method98(Actor, byte)`
- **Purpose**: Mid-tick interpolation between two server-supplied force-
  movement waypoints (1547→1548 range). If the actor currently has an
  animation that has frames left, both X and Y are interpolated linearly
  using the proportion `(anInt1161 - anInt1547) / (anInt1548 - anInt1547)`
  across `(1543,1545)` → `(1544,1546)`. Then snaps both `anInt1510` and
  `anInt1552` to one of the four cardinal angles via `anInt1549`.
- **Parameters**: `actor` target; `byte0` obfuscator dummy compared to
  `aByte1012`.
- **Returns**: void.
- **Called by**: `method96` (file:6649).
- **Calls**: `SequenceDefinition.method258`.
- **Notes**: Unlike `method97`, this commits the final facing
  (`anInt1552 = anInt1510`).

### method99(byte byte0, Actor actor) — file:6703

- **Signature**: `public final void method99(byte, Actor)`
- **Purpose**: Per-tick queued-step walking/running. Pops the next waypoint
  from `anIntArray1500/1501` (capped by `anInt1525`), picks one of eight
  facing angles from the dx/dy sign, then computes a walk speed `k1` that
  varies with the queue depth (2 for facing-only adjust, 6 for `>2`-step,
  8 for `>3`-step, doubled if the slot's run flag in `aBooleanArray1553`
  is set) and the active animation (`anInt1554` walk, `anInt1556` left-
  strafe, `anInt1557` right-strafe, `anInt1555` reverse, `anInt1505` run).
  Advances X/Y by `k1`, snapping when reaching the waypoint; decrements
  `anInt1525` and `anInt1542` (run-energy ticker).
- **Parameters**: `byte0` obfuscator dummy (must be `34` to skip an
  `anInt1096 = 285` write); `actor` target.
- **Returns**: void.
- **Called by**: `method96` (file:6651).
- **Calls**: `SequenceDefinition.aClass20Array351[...]` lookup only.
- **Notes**: If the queued waypoint is `> 256` px away the actor is
  teleported (anti-desync); if the actor is mid-rotation an alternate stand
  animation (`anInt1505`) is chosen.

### method100(Actor actor, int i) — file:6824

- **Signature**: `public final void method100(Actor, int)`
- **Purpose**: Head-turn / "face entity" handler. If the actor is following
  a face-target (`anInt1502`) — either an NPC (`<32768`) or a player
  (`-32768` then resolved through `anInt884`/`anInt889` local-player swap)
  — compute the bearing with `Math.atan2`. Same logic applies when a face-
  coordinate is set in `anInt1538/anInt1539` (subtracted by `anInt1034 -
  anInt1034`-style scene offsets). Then rotates `anInt1552` toward
  `anInt1510` by at most `anInt1504` radians-per-tick (turn speed), wrapping
  modulo `0x7ff`, and switches the actor to the `anInt1512` (turn) or
  `anInt1554` (walk) animation if it isn't already stepping.
- **Parameters**: `actor` target; `i` obfuscator dummy (must be `< 0`).
- **Returns**: void.
- **Called by**: `method96` (file:6652).
- **Calls**: `Math.atan2`.

### method101(Actor actor, int i) — file:6887

- **Signature**: `public final void method101(Actor, int)`
- **Purpose**: Per-tick animation frame advance. Drives three concurrent
  sequences: (a) the current passive/idle sequence `anInt1517` advancing
  `anInt1518/anInt1519` against `SequenceDefinition.method258` durations;
  (b) the spot-animation/graphic `anInt1520` advancing
  `anInt1521/anInt1522` against `SpotAnimationDefinition.aClass23Array403
  [...].aClass20_407`; (c) the priority/queued sequence `anInt1526`
  advancing `anInt1527/anInt1528`, including loop-count tracking
  (`anInt1530`/`anInt362`) and rewind (`anInt356`). Also clears
  `anInt1529` (animation-delay counter) by one each call and copies the
  current frame's `aBoolean358` (movement-blocking) to `aBoolean1541`.
- **Parameters**: `actor`; `i` obfuscator dummy (toggles `aBoolean919` if
  `>= 0`).
- **Returns**: void.
- **Called by**: `method96` (file:6653).
- **Calls**: `SequenceDefinition.method258`.

### method102(boolean flag) — file:6948

- **Signature**: `public final void method102(boolean)`
- **Purpose**: Per-frame "chrome" repaint. After a one-shot full-frame
  prelude (`GameFrameHandler.applyFullFrameRedrawPrelude` when
  `aBoolean1255` requests it), this method:
  - if `anInt1023 == 2` invokes `method146` (the in-world overlay);
  - advances animation on the active full-screen interface
    (`anInt1189`);
  - decides via `GameFrameHandler.applyMainViewportRedrawTriggers` whether
    the main viewport needs `method36` (the main-viewport redraw);
  - drives the scroll-handle drag for the side panel
    (`aClass9_1059.anInt224`/`method65`) and re-renders the side panel
    if needed (`method18(6)`);
  - via `GameFrameHandler.shouldRedrawChatbox` decides if chat needs a
    redraw;
  - regenerates the tab-bar selection sprites
    (`aClass15_1125`/`aClass15_1124`) — the two sprite atlases for the top
    and bottom tab strips — drawing the active-tab and hover-blink
    highlights into them;
  - regenerates the chat-mode setting strip (`aClass15_1123`) with
    coloured "Public chat / Private chat / Trade-compete / Report abuse"
    labels reflecting `anInt1287`/`anInt845`/`anInt1248`;
  - finally resets `anInt945` (the per-frame elapsed-tick accumulator).
- **Parameters**: `flag` true on normal frames; when false reads
  `aClass17_1000.method246()` into `anInt939` (typeface height).
- **Returns**: void.
- **Called by**: `method9` (file:7419).
- **Calls**: `GameFrameHandler.*` (extracted helpers), `method146`,
  `method119`, `method36`, `method65`, `method18`, `method126`,
  `Class15.method237/method238`, `IndexedSprite.method361`,
  `BitmapFont.method382`.
- **Notes**: This is one of the most state-heavy methods in the file; the
  modern code has been partially refactored to push the boolean
  combination logic into `GameFrameHandler`.

### method103(Widget widget, boolean flag) — file:7129

- **Signature**: `public final boolean method103(Widget, boolean)`
- **Purpose**: Right-click context-menu builder for friend-list and
  ignore-list widgets. Inspects `widget.anInt214` (widget interaction id):
  `1..200` and `701..900` are friend rows (with 200 / 700 / 600 offset
  arithmetic to recover the index), emitting "Remove" and "Message" menu
  options against `friendDisplayNames[i]` with menu codes 792 and 639;
  `401..500` are ignore rows, emitting "Remove" with menu code 322 and
  `widget.aString248` as the label.
- **Parameters**: `widget` the hovered widget; `flag` obfuscator dummy
  (calls `method6` when true).
- **Returns**: `true` if entries were appended (writes into
  `aStringArray1199`/`anIntArray1093` at `anInt1133`).
- **Called by**: file:907 (the widget-tree hover-test loop in chunk A).
- **Calls**: `method6` (chunk A — RNG re-key).

### method104(boolean flag) — file:7166

- **Signature**: `public final void method104(boolean)`
- **Purpose**: Per-tick advance of all queued spot animations / projectiles
  in the scene-effect list. Pure forward to `SceneEffectHandler.
  updateGraphicsObjects(aClass19_1056, anInt918, anInt1161, anInt945,
  aClass25_946, flag, aBoolean1157)` and assigns the result back to
  `aBoolean1157` (scene-needs-repaint flag).
- **Called by**: file:10136.

### method105(int drawModeGuard, int scrollY, int baseX, Widget parentWidget, int baseY) — file:7171

- **Signature**: `public final void method105(int, int, int, Widget, int)`
- **Purpose**: Recursively renders an entire `Widget` subtree. After
  optional flag-toggle and visibility tests via `WidgetInteractionHandler`
  it delegates the actual draw to `WidgetTreeRenderer.renderWidgetTree`,
  passing every piece of input/drag state the renderer needs
  (`getDragState`, `getDragSlot`, `getDragWidgetId`, `getDragStartMouseX`,
  `getDragPointerY`, `getSelectedDrag*`, `getItemUse*`, super.anInt20/21
  mouse, `anInt1039/1048/1026/989/939` widget metrics, `aBoolean1043/
  1149` flags and the `aClass30_Sub2_Sub1_Sub4_1270` typeface).
- **Called by**: file:156, 160, 1616, 1619, 7490, 7495 — for every active
  widget root.
- **Calls**: `WidgetInteractionHandler.shouldFlipMenuStateGuard`,
  `WidgetInteractionHandler.canRenderWidgetTree`,
  `WidgetTreeRenderer.renderWidgetTree`.
- **Notes**: A leaf of the partial refactor — the original recursive draw
  has been extracted to `WidgetTreeRenderer` but `GameClientCore` still
  owns the state and the per-frame entry call.

### setWidgetDragOffsetY(int dragOffsetY) — file:7209

- **Signature**: `void setWidgetDragOffsetY(int)` (package-private)
- **Purpose**: Setter for `dragPointerY` used by `WidgetTreeRenderer` and
  `WidgetInteractionHandler` to update the drag handle as the user moves
  the mouse.
- **Called by**: extracted helpers in `WidgetTreeRenderer`/
  `WidgetInteractionHandler` (no direct caller in `GameClientCore`).

### method106(IndexedSprite sprite, int i) — file:7214

- **Signature**: `public final void method106(IndexedSprite, int)`
- **Purpose**: Generates the title-screen / login-background "smoke" or
  "fog" backdrop. Seeds `anIntArray1190` with 5000 random pixels, then
  runs 20 box-blur passes (`anIntArray1190` ↔ `anIntArray1191` ping-pong)
  over a 128x256 buffer. If a sprite mask is passed, every non-zero pixel
  of the sprite is punched to `0` at offset `(sprite.anInt1454+16,
  sprite.anInt1455+16)` so text can be overlaid sharply.
- **Parameters**: `sprite` optional mask (`null` for blank backdrop); `i`
  obfuscator dummy (writes `aClass30_Sub2_Sub2_1192.method398(126)` when
  `>= 0`).
- **Called by**: file:2474 (`method106(null, -135)`) and file:2850
  (`method106(aClass30_Sub2_Sub1_Sub2Array1152[i2], -135)`) — title screen
  setup.

### applyPlayerPublicChatUpdate(Player, PacketBuffer) — file:7264

- **Signature**: `private void applyPlayerPublicChatUpdate(Player,
  PacketBuffer)`
- **Purpose**: Thin bridge invoking
  `PlayerUpdateMaskHandler.applyPlayerPublicChatUpdate` with the shared
  `aClass30_Sub2_Sub2_834` scratch buffer plus the local ignore-list and
  `anInt1251`/`aBoolean991` flags.
- **Called by**: `applyPlayerUpdateMasks` (file:7287).

### applyPlayerMaskStateGuard(byte) — file:7269

- **Signature**: `private void applyPlayerMaskStateGuard(byte)`
- **Purpose**: Obfuscator-guard helper extracted from the legacy method —
  if `stateGuard != 25` it nulls out the scene's animation-event arrays
  (`aClass19ArrayArrayArray827`). Called once at the head of
  `applyPlayerUpdateMasks`.

### applyPlayerUpdateMasks(int updateMask, int playerIndex, PacketBuffer, byte stateGuard, Player) — file:7275

- **Signature**: `private void applyPlayerUpdateMasks(int, int,
  PacketBuffer, byte, Player)`
- **Purpose**: Demultiplexes a player update mask into one of nine handler
  calls on `PlayerUpdateMaskHandler` (force-movement, graphic, animation,
  forced-chat, public chat, interacting-entity, appearance, face-coords,
  hit primary, hit secondary). Each branch is gated by an
  `EntityUpdateMasks.Player.*` bit.
- **Called by**: `method107` (file:7302) and direct call at file:2237.

### method107(int i, int j, PacketBuffer, byte, Player) — file:7300

- **Signature**: `private final void method107(int, int, PacketBuffer,
  byte, Player)`
- **Purpose**: One-line forward to `applyPlayerUpdateMasks`. Retained for
  backwards-compatible call sites referencing the legacy name.
- **Called by**: the player-update reader loop (this delegation is the
  external surface used at file:9332 via `method117` style chain).

### method108(int i) — file:7305

- **Signature**: `public final void method108(int)`
- **Purpose**: Per-tick camera follow for the local player. Smoothly
  interpolates `anInt1014/anInt1015` toward `((Actor)
  aClass30_Sub2_Sub4_Sub1_Sub2_1126).anInt1550/1551 + anInt1278/anInt1131`.
  Reads camera-control keys from `super.anIntArray30[1..4]` to spin
  `anInt1186`/`anInt1187` (camera yaw/pitch velocity) and apply them to
  `anInt1185`/`anInt1184` (clamped to [128, 383] pitch). Picks the
  highest occupied height under the player from
  `anIntArrayArrayArray1214[plane][x±4][y±4]` to compute the desired
  camera-zoom `j2` (capped to `[32768, 0x17f00]`) and eases `anInt984`
  toward it (`/24` ascend, `/80` descend).
- **Side-effect**: every 1512 ticks (`anInt1005`) writes a periodic
  obfuscation-/anti-cheat packet to `aClass30_Sub2_Sub2_1192` (12 bytes of
  randomness + the player's pixel position).
- **Called by**: file:3082 (the main per-frame run-loop scene update).
- **Throws**: wraps any exception into `RuntimeException("eek")` after
  reporting to `SignLink.reporterror`.

### method9(int i) — file:7406

- **Signature**: `public final void method9(int)`
- **Purpose**: Top-level paint dispatcher. If any boot-error flag is set,
  calls `method94`. Else, increments `anInt1061` (idle counter) and either
  draws the load progress (`method135` when `!aBoolean1157`) or the live
  frame (`method102`).
- **Called by**: the applet's paint loop (registered in chunk A).

### method109(boolean flag, String s) — file:7423

- **Signature**: `public final boolean method109(boolean, String)`
- **Purpose**: True if `s` matches any name in `friendDisplayNames` (case-
  insensitive) or the local player's own name. Used everywhere to gate
  "friends-only" chat filtering.
- **Called by**: file:189, 210, 233, 254, 1390, 5117, 5136, 5155, 5167,
  5507, 8312.

### method110(int i, int j, boolean flag) — file:7436

- **Signature**: `public static final String method110(int, int, boolean)`
- **Purpose**: Colour-tag picker for combat-level differences — given
  `i - j` returns `@red@`/`@or1..3@`/`@yel@`/`@gr1..3@`/`@gre@`. Used by
  the right-click "Attack" menu in the world rendering code.
- **Parameters**: `flag` must be true (else throws `NullPointerException`
  — obfuscator dummy).

### method111(byte byte0, int i) — file:7461

- **Signature**: `public final void method111(byte, int)`
- **Purpose**: Sets `SignLink.wavevol` (global audio volume). When
  `byte0 != 2` also re-keys via `method6`.
- **Called by**: file:1294, 1299, 1304, 1309 — the four sound-volume tab
  buttons (0 / -400 / -800 / -1200 dB).

### method112(int i) — file:7470

- **Signature**: `public final void method112(int)`
- **Purpose**: Per-frame in-world overlays. Draws the mouse-click marker
  sprites (`aClass30_Sub2_Sub1_Sub1Array1150[anInt916/100]`) animating
  through 4 frames; reissues any active full-screen interface
  (`anInt1018`, `anInt857`) plus its widget animation tick via
  `method119`; draws the active-camera "compass spinner" pointer; if
  `aBoolean1156` (debug overlay enabled) draws "Fps:" and "Mem:" in the
  top right (red >32 MB & `aBoolean960`); if `anInt1104 != 0` (system
  update timer) renders "System update in: mm:ss" and beeps every 75
  ticks.
- **Called by**: file:10195 (the per-frame main loop).
- **Calls**: `method76`, `method119`, `method105`, `method70`, `method82`,
  `method125`, `method40`, `IndexedSprite.method348`,
  `BitmapFont.method380/method385`.

### method113(long l, int i) — file:7542

- **Signature**: `public final void method113(long, int)`
- **Purpose**: Add a name (encoded as the 38-bit hash `l`) to the local
  ignore list. Validates against the 100-entry cap, dupes within the
  ignore list, and "must not be on friend list" rule. On success writes
  the name to `ignoredNameHashes`, sets `aBoolean1153` (chrome redraw),
  and sends packet 133 with a 5-byte payload (the hash).
- **Parameters**: `i` obfuscator dummy (must be `4`).
- **Called by**: file:4860 (the ignore-list "Add" button handler).

### method114(byte byte0) — file:7583

- **Signature**: `public final void method114(byte)`
- **Purpose**: Per-tick advance for every visible player. Iterates over
  `anIntArray892` (with `-1` index → local player at `anInt889`) and
  runs `method96(46988, 1, player)` per slot. Guarded by
  `byte0 == aByte973`.
- **Called by**: file:2965 (per-tick world update).

### method115(byte byte0) — file:7601

- **Signature**: `private final void method115(byte)`
- **Purpose**: Per-tick processor of the queued `SceneObjectSpawnRequest`
  list (`aClass19_1179`). Walks the list backwards: each request
  decrements its `anInt1294` "delay" timer; when zero the spawn is
  committed via `method142` (place scene object) and unlinked. Separately,
  for requests with a `anInt1302` "transient-revert" timer, when that
  hits zero the original tile is re-shown.
- **Parameters**: `byte0` obfuscator dummy (must be `8`).
- **Called by**: file:2960 (per-tick world update).

### method116(boolean flag) — file:7640

- **Signature**: `public final void method116(boolean)`
- **Purpose**: Open the right-click context menu. Measures the widest
  menu string via `aClass30_Sub2_Sub1_Sub4_1272.method383` against
  "Choose Option" and `aStringArray1199`; computes the menu's
  width (`i + 8`) and height (`15*anInt1133 + 21`). Then anchors the menu
  to one of three regions depending on where the mouse was last released:
  the main viewport (`4..516 x 4..338`), the inventory panel
  (`553..743 x 205..466`), or the chatbox (`17..496 x 357..453`); sets
  `aBoolean885` (menu open), `anInt948` (anchor region 0/1/2), and
  `anInt949/950/951/952` (menu rectangle).
- **Parameters**: `flag` — ANDed into `aBoolean1157` (force scene
  repaint).
- **Called by**: file:456, 3047 — the right-mouse-press handlers.

### method117(PacketBuffer buf, int i, byte byte0) — file:7716

- **Signature**: `private final void method117(PacketBuffer, int, byte)`
- **Purpose**: Reads the local-player movement update block from `buf`
  (initialised to `anInt1118`). The block is variable-encoded: `j=0`
  means "no update"; `k` selects one of four update kinds: `0` queue
  movement, `1` teleport (3-bit direction), `2` double-step (two 3-bit
  directions), `3` full reset (plane + run-flag + 7-bit x + 7-bit y).
  Each kind also reads a 1-bit "update mask present" trailer flag.
- **Parameters**: `i` legacy unused; `byte0` obfuscator dummy (must be
  `5`).
- **Called by**: file:9332 (the world-update packet handler).

### method118(int i) — file:7765

- **Signature**: `public final void method118(int)`
- **Purpose**: Shut-down / unload — busy-waits while `aBoolean962` is set,
  then nulls out the large sprite/colour/heightmap arrays so they become
  garbage-collected (`aClass30_Sub2_Sub1_Sub2_966/967/Array1152`,
  `anIntArray850..853`, `anIntArray1190/1191/828/829`,
  `aClass30_Sub2_Sub1_Sub1_1201/1202`, `aClass19ArrayArrayArray827`).
- **Parameters**: `i` obfuscator dummy (must be `3`).
- **Called by**: file:4742, 5783 — the two engine-reset call sites.

### method119(int i, boolean flag, int j) — file:7794

- **Signature**: `public final boolean method119(int, boolean, int)`
- **Purpose**: Recursive widget animation tick. Walks the children of
  `Widget.aClass9Array210[j]` via `anIntArray240`: type-1 (nested
  interface) recurses into `method119(i, false, child.anInt250)`; type-6
  (model with cycle animation) picks between `anInt257` and `anInt258`
  via `method131` (state predicate), advances `anInt208` by `i`, rolls
  over to the next frame when a frame's duration is exceeded, and wraps
  around `anInt356`. Returns true if at least one widget frame advanced
  (used by the caller to force a redraw).
- **Parameters**: `flag` obfuscator dummy (must be false, else throws
  `NullPointerException`).
- **Called by**: `method102` (file:6965, 6993), `method112` (file:7489,
  7494), and indirectly through itself.

### method120(int i) — file:7838

- **Signature**: `public final int method120(int)`
- **Purpose**: Picks the floor `plane` index used for camera ray-casting.
  Walks a Bresenham line from `(anInt858, anInt860)` (camera target) to
  the player's pixel position. Along the line, if any tile flag
  `aByteArrayArrayArray1258[plane][x][y] & 4` is set (bridge / multi-
  level marker), promotes `j` to the current `anInt918` plane. Returns
  the final plane.
- **Parameters**: `i` obfuscator dummy (toggles `aBoolean1224` if `<= 0`).
- **Called by**: file:10149.

## Cross-chunk dependencies

The chunk depends on many earlier-defined helpers in `GameClientCore`:

- Buffer / paint: `method11(0)` (chunk A) and `method4` for screen flush.
- World-render entry points: `method36`, `method18`, `method70`, `method82`,
  `method125`, `method40`, `method76`, `method126`, `method146` (all
  defined earlier).
- Loading screen: `method135` (called from `method9`).
- Widget hit-testing: `method131` (state predicate); `method65` (scrollbar
  drag); `method142` (scene-object spawn commit).
- World-coord helpers: `method42` (height lookup).
- RNG re-keying: `method6`.
- Outgoing packet writes are layered onto the shared
  `aClass30_Sub2_Sub2_1192` buffer initialised in chunk B.
- Field state owned by earlier chunks (see list above) — none of the chunk
  D code declares fresh instance fields.

The chunk is called back into from later chunks at:

- file:9332 (`method117`) — packet handler chain.
- file:9743, 9508 — `InterfacePacketHandler` callbacks that operate on the
  same chrome state `method102` repaints.
- file:10136, 10149, 10195 — top-level frame loop driving `method104`,
  `method120` and `method112`.

## Cross-class call graph

External classes invoked from this chunk:

- `GameFrameHandler.applyFullFrameRedrawPrelude` /
  `applyMainViewportRedrawTriggers` / `shouldRedrawChatbox`
  (file:6955, 6969, 6997) — extracted frame-state helpers; see
  `GameFrameHandler.java`.
- `WidgetInteractionHandler.shouldFlipMenuStateGuard` /
  `canRenderWidgetTree` (file:7173, 7175).
- `WidgetTreeRenderer.renderWidgetTree` (file:7177).
- `SceneEffectHandler.updateGraphicsObjects` (file:7168).
- `PlayerUpdateMaskHandler.*` (file:7266, 7279, 7281, 7283, 7285, 7289,
  7291, 7293, 7295, 7297).
- `SequenceDefinition.aClass20Array351[...].method258` (animation frame
  duration lookup, file:6713, 6896, 6912, 6920, 6929, 7818, 7820).
- `SpotAnimationDefinition.aClass23Array403[...].aClass20_407` (file:6911).
- `Widget.aClass9Array210[...]` (file:7490, 7495, 7799, 7804) — the global
  widget table; child traversal via `anIntArray240/250/257/258/261/262`.
- `IndexedSprite.method348` (file:7477, 7486, 7506), `method361`
  (file:7021, 7027 etc. — within `method102`).
- `BitmapFont.method380/382/383/385` — text drawing for chrome.
- `MapRegion.method178` (file:7615, 7624) — region-load check inside
  `method115`.
- `TextUtils.method587/method584` — name-hash to display-name (file:7553).
- `SignLink.wavevol` / `SignLink.reporterror` / `SignLink.mainapp`.
- `Actor.method446` (snap-to-tile) (file:6633, 6643);
  `Actor.method445/method448` from `method117`.
- `aClass30_Sub2_Sub2_1192.method397/398/399/404/407` — outgoing packet
  ops.
- `aClass30_Sub2_Sub1_Sub4_1271/1272` — the BitmapFont chrome typeface.
- `aClass15_*` — sprite-atlas render targets (`method237` activate,
  `method238` flush-to-graphics).
