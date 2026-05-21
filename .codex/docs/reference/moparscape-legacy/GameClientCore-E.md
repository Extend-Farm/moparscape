# GameClientCore Chunk E (lines 7920–10960) — legacy moparscape

Source: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`

This chunk closes out the 10 960-line obfuscated client class. It contains the last 28 named `method*` members, the no-arg constructor, six package-private constructor-init helpers (extracted previously into `GameClientCoreConstructorInit`), the field declarations, and the static initialiser that builds the XP and bit-mask lookup tables.

## Chunk overview

- Late game-loop helpers and rendering glue: `method121`–`method136`, plus the giant world-event sub-dispatcher `method137` and frame-step entry `method146`.
- World update payload helpers (`method134` player-mask scan, `method139` npc-mask scan, `method143` "getplayer" composite, `method142` static-object replace, `method144` camera-vector math, `method147` close-interface request).
- The networking pump and opcode dispatch loop `method145` (lines 9400–10126).
- The constructor (`GameClientCore()` line 10227) and its six init helpers (lines 10232–10434) that allocate every field block.
- ~520 field declarations (lines 10436–10937) and the static initialiser (line 10939) computing `anIntArray1019` (the level→XP table) and `anIntArray1232` (`2^k − 1` mask table).

## Final static + instance field roundup (any fields not covered by A–D)

The block at `GameClientCore.java:10436-10937` is the canonical field list. Only fields that first appear (or are first used in a non-trivial way) inside this chunk are called out; anything else is a re-declaration of state owned by earlier chunks.

- Socket / jaggrab: `aSocket832` (line 10446) opened in `method132`.
- Login-screen state machine: `loginScreenState` (10447), `loginInputField` (10860), `loginUsername`/`loginPassword`/`configuredServerAddress` (10808-10810), `loginAutoSubmitPending` (10811), `loginStatusPrimary`/`loginStatusSecondary` (10910-10911), `aBoolean1255` (10899, redraw-login flag), `aBoolean1157` (10792, login-suspend flag), `anInt1038`/`anInt1105` (login timers, used in `method140`).
- Camera lock packet 166: `anInt1098`-`anInt1102` (10730-10734), `aBoolean1160` (10795).
- Camera oscillation packet 35: `aBooleanArray876`, `anIntArray873`, `anIntArray1203`, `anIntArray928`, `anIntArray1030` (10487-10661, all sized 5; consumed by `method146` lines 10157-10177).
- Camera "look-at" packet 177: `anInt995`-`anInt999` (10611-10615), trig in `method145` lines 9961-9976.
- Region transition packets 73/241: `anInt1069`, `anInt1070`, `anInt1034`-`anInt1037`, `aBoolean1141`, `aBoolean1159`, `aBoolean1080`, `aBoolean1160`, `anIntArrayArrayArray1129` (dynamic-region chunk template), `aByteArrayArray1183`/`aByteArrayArray1247` (region map/landscape blobs), `anIntArray1234`/`anIntArray1235`/`anIntArray1236` (region id + on-demand request indices).
- Scene-object spawn deque: `aClass19_1179` (10820) populated by `method130` and `method137`.
- Friends/ignores: `ignoreCount` (10436), `ignoredNameHashes` (10539), `friendCount` (10513), `friendNameHashes` (10569), `friendDisplayNames` (10713), `friendWorlds` (10440).
- Chat scrollback: `chatTypes`/`chatSenders`/`chatMessages` (10556-10558), `recentChatIds` (10884), `recentChatWriteIndex` (10804).
- Music: `anInt956` (current song), `anInt1227` (queued song), `anInt1259` (queue delay), `aBoolean1228`, `aBoolean1151` (10786, music-enabled).
- Area sounds queue: `anIntArray1207`, `anIntArray1241`, `anIntArray1250`, `anInt1062` (all consumed by `method145` opcodes 105/174).
- Hint icon state: `anInt855`, `anInt1222`, `anInt933`-`anInt938` (10547-10552, packet 254).
- Skill data: `anIntArray864` (current level), `anIntArray922` (XP), `anIntArray1044` (boosted level) — packet 134 plus level-up helpers.
- Varp/setting storage: `anIntArray971` (live varps), `anIntArray1045` (server varps), `anIntArray1130` (interface settings, default `-1`).
- Drag state: `dragWidgetId`/`dragSlot`/`dragState`/`dragStartMouseX`/`dragPointerY` (10715-10720) plus the matching `selectedDrag*` set (10888-10890).
- Item-use flow: `itemUseState`/`itemUseSlot`/`itemUseWidgetId`/`itemUseItemId`/`itemUseItemName` (10926-10932).
- Region/chunk-coord cursors used by `method137`: `anInt1268`, `anInt1269`.
- Static tables (line 10939-10959):
  - `anIntArray1019` — 99-entry experience threshold table, formula `i += (int)(l + 300 * 2^(l/7))` then `/4`, matching the canonical RuneScape XP curve.
  - `anIntArray1232` — `2^k − 1` for `k ∈ [1,32]`, used by `method124` op 14 (VarBit extraction) and elsewhere.
- Static encryption constants: `aBigInteger856` (RSA modulus, line 10470) and `aBigInteger1032` (login RSA exponent, 10663).
- Class-level singleton `aClass30_Sub2_Sub4_Sub1_Sub2_1126` (line 10758) — the local `Player`.
- AWT host: `embeddedHostComponent` (10937).

## Methods

### `method121(int i)` (lines 7920–7929)
Re-evaluates the camera/scene plane after a non-zero argument forces a no-op write loop (`method398(21)`) that is effectively unreachable. Calls `method42` to project local player at `(anInt858, anInt860)`; if the projected `j − anInt859 < 800` and the tile mask has bit `4` set, returns `anInt918` (current plane), else returns `3` (sky/highest plane). Used by `method146` line 10151 when the camera is locked.

### `method122(int i, long l)` (lines 7931–7959)
Removes a name hash from the ignore list. Walks `ignoredNameHashes`, on match shifts the tail down, decrements `ignoreCount`, marks UI dirty (`aBoolean1153`) and queues outgoing packet 74 with a 5-byte name hash (`method404(5, l)`). Param `i` if not `3` triggers `method6()`. Wrapped in catch+`SignLink.reporterror("47229,…")`.

### `getParameter(String s)` (lines 7961–7967)
Applet-parameter shim that delegates to `SignLink.mainapp.getParameter` when an embedded host is configured, else `super.getParameter`.

### `method123(byte byte0, boolean flag, int i)` (lines 7969–7978)
Sets MIDI volume. `i` is stored on `SignLink.midivol`. When `byte0 != 0` it nulls `aClass19ArrayArrayArray827` (the ground-item grid), an obfuscation guard that should never fire. If `flag`, requests the SignLink mixer to perform a `voladjust`.

### `method124(int i, Widget class9, int j)` (lines 7980–8100)
Evaluates a single CS1-style script (`class9.anIntArrayArray226[j]`) used for widget hide/show calculations. The opening `i = 91/i` is the obfuscation guard. Walks an opcode list, accumulating with `+ − * /` and a comparator override (ops 15-17 set a pending comparator). Operand ops mirror the canonical CS1 set:

| op | source |
|----|--------|
| 0 | terminate (`return k`) |
| 1 | `anIntArray922[arg]` (current XP) |
| 2 | `anIntArray1044[arg]` (boosted level) |
| 3 | `anIntArray864[arg]` (base level) |
| 4 | inventory-count of item-id `arg2` in widget `arg1` |
| 5 | `anIntArray971[arg]` (varp) |
| 6 | XP threshold table indexed by `boostedLevel-1` |
| 7 | `(varp[arg]*100)/46875` (combat-style %?) |
| 8 | `localPlayer.anInt1705` |
| 9 | sum of boosted levels for enabled skills (`Skills.aBooleanArray735`) |
| 10 | inventory contains item-id `arg2+1` → `0x3b9ac9ff` |
| 11 | `anInt1148` (chat status) |
| 12 | `anInt878` (public chat mode) |
| 13 | bit `arg2` of varp `arg1` |
| 14 | VarBit (`VarBitDefinition.aClass37Array646[arg]`) — uses `anIntArray1232` mask table |
| 15-17 | comparator override (signed/abs/etc.) |
| 18,19 | local player tile X/Y absolute |
| 20 | literal constant |

Returns `-2` if `anIntArrayArray226` missing or index out of range, `-1` on any exception.

### `method125(int i)` (lines 8102–8121)
Renders the context-menu tooltip at the mouse. Picks a caption from `aStringArray1199[anInt1133-1]`, prepends "Use X with..." or "<verb>..." based on `itemUseState`/`anInt1136`, appends "@whi@ / N more options" when more than two options exist, and draws via `aClass30_Sub2_Sub1_Sub4_1272.method390`. Trailing `for(int j=1;j>0;j++)` is dead obfuscation guard.

### `method126(boolean flag)` (lines 8123–8252)
Composes the minimap. `flag==true` short-circuits. Clears `aClass15_1164`, in `anInt1021 == 2` state draws only the alpha-masked sprite mask. Otherwise:
1. Rotates the minimap underlay (`aClass30_Sub2_Sub1_Sub1_1263.method352`) by `anInt1185+anInt1209`, scaled by `anInt1170` zoom, centred on local player.
2. Overlays the alpha mask sprite `aClass30_Sub2_Sub1_Sub1_1122`.
3. Plots map dot sprites for: registered map markers (`aClass30_Sub2_Sub1_Sub1Array1140`, `method141`), ground-item piles (yellow dot `_1074`), every visible NPC (`_1075` if def flags clickable and visible), every visible player (`_1077` friend, `_1078` clan-mate, `_1076` regular), and a flashing hint icon — `anInt855==1` (NPC), `==2` (tile), `==10` (player) — using the larger `_871` sprite via `method81`.
4. Renders the destination flag (`_870`) at `(anInt1261, anInt1262)`.
5. Draws compass needle border via `Rasterizer2D.method336` and finally pushes to the minimap buffer `aClass15_1165`.

### `method127(boolean flag, Actor actor, int i)` (lines 8254–8259)
Computes screen-space anchor for an actor’s health-bar/chat-icon. Reads `anInt1008` from the net buffer when `!flag` (the obfuscation guard) and delegates to `method128` with `(actor.anInt1550, i, anInt875, actor.anInt1551)`.

### `method128(int i, int j, int k, int l)` (lines 8261–8296)
World→screen projection. Sanity-clamps world coordinates to `[128, 13056]`, computes Y from `method42(anInt918, l, true, i) - j`, applies camera translation (`anInt858/859/860`) and the two-axis rotation (`anInt861` pitch, `anInt862` yaw) using `Model.anIntArray1689/1690` sin/cos LUTs. When the projected depth `l >= 50` writes screen coords to `anInt963/964`, otherwise `(-1,-1)`.

### `method129(boolean flag)` (lines 8298–8343)
Hover-on-chat context menu builder for private messages. Walks `chatMessages[]`, when message type is `3` (PM in) or `7` (PM out) and the chat tab filter (`anInt845`) allows it, tests the mouse row. If hovered, queues "Report abuse @whi@<name>" (id 2606, requires `anInt863 >= 1`), "Add ignore @whi@<name>" (id 2042), and "Add friend @whi@<name>" (id 2337) into the context menu (`aStringArray1199`/`anIntArray1093`/`anInt1133`). Stops after 5 visible rows.

### `method130(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2)` (lines 8345–8373)
Inserts/updates a `SceneObjectSpawnRequest` keyed by `(plane=l1, type=i1, tileX=i2, tileY=j1)` into `aClass19_1179`. Updates `anInt1291…anInt1302` slots (anim id, rotation, object id, ticks). Called by `method137` for opcodes 147, 151, 160, 101 and by `method142`.

### `method131(Widget class9, boolean flag)` (lines 8375–8405)
Evaluates the widget visibility predicate set (`anIntArray245`/`anIntArray212`) by calling `method124` for each row and applying comparator op (1 `!=`, 2 `<`, 3 `>`, 4 `==`). Returns false on first failing row, else true. `flag` only sets `anInt883` obfuscation guard.

### `method132(String s)` (lines 8407–8430)
JAGGRAB downloader. Closes any open `aSocket832`, opens a fresh socket to port `43595` via `openGameSocket`, writes `"JAGGRAB /<s>\n\n"` and returns the wrapped `DataInputStream`. The commented-out branch shows the original applet-mode `URL` fall-back. Used by `OnDemandFetcher` to stream cache index data.

### `method133(byte byte0)` (lines 8432–8529)
Animates the login flame background. Three modes:
- `anInt1040>0`: cross-fade `anIntArray852` (off-palette) into the live `anIntArray850`.
- `anInt1041>0`: cross-fade `anIntArray853` (alt palette).
- otherwise: copy `anIntArray851` straight through.

Then copies the backing flame texture `aClass30_Sub2_Sub1_Sub1_1201.anIntArray1439` into `aClass15_1110.anIntArray315` and runs the left flame turbulence kernel (`anIntArray969` per-row warp, `anIntArray828` flame heatmap, alpha-blend onto destination). Same procedure for the right flame into `aClass15_1111` from `_1202`. Both buffers are blitted via `method238` at screen X 0 and 637. `byte0 != 9` triggers the obfuscation read of `anInt1008`.

### `method134(byte byte0, int i, PacketBuffer class30_sub2_sub2)` (lines 8531–8593)
Bit-stream parser for the **local player** update mask block. Reads an 8-bit count of players that already existed and walks each existing slot: 1 bit "moved?", 2 bits "move type" (0=mask-only-but-needs-mask, 1=walk dir, 2=run dir, 3=remove). Records updates into `anIntArray894`/`anInt893` for follow-up mask read in `method91`. Out-of-range count throws `RuntimeException("eek")` after `SignLink.reporterror`.

### `method135(boolean flag, boolean flag1)` (lines 8595–8677)
Renders the entire login screen frame. Calls `method64(0)` (font setup), clears `aClass15_1109`, draws background plate `_966` at (0, 16083). Three sub-screens by `loginScreenState`:
- `LOGIN_SCREEN_WELCOME`: title "Welcome to MoparScape" + version `aClass42_Sub1_1068.aString1333`, two buttons "Info" / "Play Now".
- `LOGIN_SCREEN_CREDENTIALS`: status text, username/password fields (password obfuscated via `TextUtils.method588`), caret blinking via `anInt1161 % 40 < 20`, Enter/Cancel buttons.
- `LOGIN_SCREEN_PRIVATE_SERVER_INFO`: instructional copy and an Exit button.

Pushes `aClass15_1109` to the live `Graphics`. When `aBoolean1255` is set, also re-blits the six static frame panes (`_1107`/`_1108`/`_1112`/`_1113`/`_1114`/`_1115`) and clears the flag.

### `method136(byte byte0)` (lines 8679–8714)
Background "draw thread" used while the I/O thread is loading. Sets `aBoolean962 = true`, loops while `aBoolean831`, each tick: `anInt1208++`, two `method58(25106)` polls (incoming queue + outgoing flush), then `method133((byte)9)` to animate flames; sleeps `j` ms recalibrated every 10 frames to target 40 ms total. Aborts on any exception, clearing `aBoolean962`.

### `method10(byte byte0)` (lines 8716–8728)
Sets `aBoolean1255 = true` (login redraw). When `byte0 == 1` returns immediately; else copies `aClass17_1000.method246()` (next ISAAC byte) into `anInt1218`. The chunk overlaps a previous chunk’s declaration of `method10` — this is the second body emitted by the obfuscator.

### `method137(int i, PacketBuffer class30_sub2_sub2, int j)` (lines 8730–9032)
Sub-dispatcher for chunk-payload region events. The opening `while(i>=0) j=-1;` is the obfuscation guard. Dispatches by inner opcode `j`:

| inner op | event | payload | effect |
|----|----|----|----|
| 84 | Ground-item count change | u8 packedXY, u16 itemId, u16 oldCount, u16 newCount | finds matching `GroundItem` in `aClass19ArrayArrayArray827[plane][x][y]`, replaces `.anInt1559` count, calls `method25(x,y)` to repaint. |
| 105 | Player area sound | u8 packedXY, u16 soundId, u8 packed(delay4|radius4) | if within `radius` tiles of local player and audio enabled, appends to `anIntArray1207/1241/1250` (sound, packed-radius, delay+default). |
| 215 | Add ground item (with priority) | u16le itemId, packedXY, u16le previousCount, u16 newCount | spawns new `GroundItem` head-of-list and triggers tile repaint. |
| 156 | Remove ground item | packedXY (`method426`), u16 itemId | unlinks matching deque entry, nulls deque if empty. |
| 160 | Add `DynamicObject` (animated scenery) | packedXY, packed(type<<2|orient), u16 animation id | resolves group via `anIntArray1177[type]` and replaces the matching `WallObject`/`WallDecoration`/`InteractiveObject`/`GroundDecoration` with a new `DynamicObject`. Type 2 (double wall) installs two synced animators. |
| 147 | Set player as object-occupier | many fields (see body) | computes a `Model` from the target `ObjectDefinition.method578`, calls `method130` to register the spawn request and updates the actor's hold-object slot (`anInt1707…anInt1722`). |
| 151 | Replace scene object (object→object) | packedXY, u16 newId, packed(type<<2|orient) | delegates to `method130` (anim=-1, ticks=0). |
| 4 | Tile-anchored projectile graphic | packedXY, u16 gfxId, u8 height, u16 delay | spawns a `GraphicsObject` (one-shot SFX entity) in `aClass19_1056`. |
| 44 | Add ground item | u16le itemId, u16 quantity, packedXY | inserts new `GroundItem` head-of-deque. |
| 101 | Remove scene object | packed(type<<2|orient), packedXY | calls `method130(404,-1,-1,…)` to mark for removal. |
| 117 | Projectile | packedXY, signed offsets to target tile, height/start-Z, gfx id, start tick, end tick, slope, start height, target index | constructs `Projectile`, calls `class30_sub2_sub4_sub4.method455(...)` to set end point, registers in `aClass19_1013`. |

Tiles are addressed with `(anInt1268 + (packedXY>>4 & 7), anInt1269 + (packedXY & 7))` where `(anInt1268, anInt1269)` were anchored by opcodes 60/85.

### `method138(byte byte0)` (static, lines 9034–9045)
Global "viewport dirty" flag set. Triggered when graphics need a full repaint: `SceneGraph.aBoolean436`, `Rasterizer3D.aBoolean1461`, `aBoolean960` (scene reload), `MapRegion.aBoolean151`, `ObjectDefinition.aBoolean752` all set true. Obfuscation guard compares `byte0` to `aByte823 (77)` and on mismatch enters a tight no-op loop.

### `method139(PacketBuffer class30_sub2_sub2, int i, int j)` (lines 9047–9110)
Bit-stream parser for the **already-present NPC** list. Calls `method418(anInt1118)` to switch to bit access mode, reads u8 count, iterates existing slots with 1-bit "moved?" + 2-bit move type (0 mask-only, 1 walk-dir-3bits, 2 run-two-dir-3bits, 3 remove). Walking/running directions feed `Npc.method448`. Throws `RuntimeException("eek")` if the server count exceeds local `anInt836`.

### `method140(boolean flag)` (lines 9112–9236)
Login-screen mouse + keyboard handler. Three sub-states:
- Welcome: "Info" button → switch to `LOGIN_SCREEN_PRIVATE_SERVER_INFO`; "Play Now" → switch to credentials screen, reset status, `configuredServerAddress = DEFAULT_ADDRESS`.
- Credentials: handles auto-submit (`loginAutoSubmitPending`), field hit-tests username/password rows, Enter/Cancel buttons. Then drains key queue (`method5(-796)`): tab/enter switch field, backspace edit, ASCII (filtered through `aString1162`) appended; truncates to 12/20 chars. Calls `method84(loginUsername, loginPassword, false)` when submitted; aborts if `aBoolean1157` set.
- Private-server info: Exit button → credentials screen.

### `method141(Sprite sprite, int i, int j, boolean flag)` (lines 9238–9261)
Plots a single minimap dot. Reuses compass orientation `(anInt1185+anInt1209)&0x7ff` and zoom `anInt1170` to compute rotated offset; bail-out when `i²+j² > 6400` (off-minimap radius). Inner radius `≤ 2500` uses the unfiltered draw `method348`, outer band uses alpha blend `method354` into the minimap mask `aClass30_Sub2_Sub1_Sub2_1197`. `flag` short-circuits.

### `method142(int i, int j, int k, int l, int i1, int j1, int k1, int l1)` (lines 9263–9324)
Force-rebuild of one scene-object tile when an authoritative update arrives. Args: `(tileY=i, plane=j, ?, ?, tileX=i1, group=j1, … animId=k1, guard=l1)`. Skips tiles within 1 of region edge or when in dynamic-region mode and plane mismatches. For each of the four object groups (`j1 ∈ {0,1,2,3}` ⇒ wall / wall-deco / interactive / ground-deco) reads the existing object id and orientation, removes from `aClass25_946`, clears `aClass11Array1230[plane]` collision flags via `method215/216/218`, then if `k1 >= 0` re-adds via `MapRegion.method188`.

### `method143(int i, PacketBuffer class30_sub2_sub2, int j)` (lines 9326–9355)
Composite "PLAYER_UPDATE" payload parser invoked from opcode 81. Resets `anInt839`/`anInt893`, then runs in order: `method117` (local-player movement bit-stream), `method134` (other-players list), `method91` (mask block decode), `method49` (npc mask block). Removes any player whose `anInt1537` did not get refreshed this tick. Throws if the buffer position doesn’t match `anInt1007`. Throws if a non-null entry is missing.

### `method144(int i, int j, int k, int l, int i1, int j1, int k1)` (lines 9357–9398)
Camera-pose helper. Inputs include yaw `k`, pitch `j1`, target `(l, i1, k1)` and arm length components. Performs two rotations (pitch then yaw) on the focus vector using `Model.anIntArray1689/1690`. Honours `cameratoggle == 1` debug freeze, caching `zoom/lftrit/fwdbwd`. Writes `anInt858/859/860` (camera position) and `anInt861/862` (orientation).

### `method145(boolean flag)` (lines 9400–10126) — **incoming packet dispatch**
The main networking pump. Procedure:
1. Read available bytes from `aClass24_1168` (`BufferedConnection`).
2. If `anInt1008 == -1`, read 1 byte → opcode; subtract `aClass17_1000.method246()` (server ISAAC) mod 256.
3. Look up size in `PacketSizeTable.anIntArray553[anInt1008]`. If `-1` read 1-byte size, if `-2` read 2-byte size.
4. Wait until `i >= anInt1007`, then `method270` reads the payload into `aClass30_Sub2_Sub2_1083`, resets `anInt1009 = 0`.
5. Shift the recent-opcode trace `anInt843 ← anInt842 ← anInt841 ← anInt1008` (for crash reporting).
6. Giant `if/else` cascade by `anInt1008` (see table below).
7. On unhandled opcode: `SignLink.reporterror("T1 - ...")` and `method44(true)` (disconnect).
8. `IOException` calls `method68`. Other `Exception`s dump the first 50 payload bytes via `SignLink.reporterror("T2 - ...")` and disconnect.

### Opcode dispatch table

Coordinates: `u8` = `method408`/`method434`; `u16` = `method410`/`method436`; `u16le` = `method426/427`; `i16le` = `method435`; `u24` = `method438`; `u32` = `method440`. "le" suffix means little-endian; signed/unsigned variants reflect the legacy `PacketBuffer` aliases.

| Opcode | Short description | Payload | Calls / writes |
|-------:|---|---|---|
| 1 | "Clear region anim/interaction ids" | (none) | nulls every `Player.anInt1526` and `Npc.anInt1526` to `-1`. |
| 8 | Widget NPC head | per `InterfacePacketHandler.applyWidgetNpcHeadModel` | sets widget's `anInt233`/model. |
| 24 | Sidebar tab toggle | `i16le tabId` | toggles `anInt1054/anInt1221` (3 ↔ 1); marks UI dirty. |
| 27 | Cancel chat input | (none) | clears `aBoolean1256`, sets `anInt1225=1`, blanks `aString1004`. |
| 34 | Widget item-slot delta | inline list to `i==anInt1007` | `InterfacePacketHandler.applyWidgetItemSlotUpdates`. |
| 35 | Camera oscillator add | `u8 slot,u8 amp,u8 jitter,u8 freq` | `IncomingPacketDispatcher.applyCameraEffectUpdate`. |
| 36 | Varp byte update | `u16 index,u8 value` | `InventoryPacketHandler.readVarpByteUpdate` then varp/cs1 cascade. |
| 44 | Open inventory container | `u16 widgetId,u16 count,u8 first?` | `InterfacePacketHandler` (?) delegated via fall-through to `method137`. |
| 50 | Friend status update | per `SocialPacketHandler.handleFriendStatusUpdate` | mutates friend arrays; sets `aBoolean1153`. |
| 53 | Widget item-grid snapshot | size = `anInt1007` | `InterfacePacketHandler.applyWidgetItemGridSnapshot`. |
| 60 | Chunk delta header | `u8 chunkY,u8 chunkX` then nested mini-opcodes | sets `anInt1269/1268`; loops `method137(anInt1119, …, op=method408)` over the rest of the packet. |
| 61 | Multiway combat flag | `u8` | `anInt1055`. |
| 64 | Region wipe 8×8 | `u16le chunkX,u16le chunkY` | nulls `aClass19ArrayArrayArray827` over an 8×8 block and zeroes spawn requests in `aClass19_1179`. |
| 65 | Friends-list page write | size = `anInt1007` | `method31` (friends snapshot). |
| 68 | Reset all varps | (none) | replaces `anIntArray971` from `anIntArray1045`, recomputes via `method33`. |
| 70 | Widget scroll position | `i16le x,i16le y,u8 widgetId` | `IncomingPacketDispatcher.applyWidgetScrollPosition`. |
| 71 | Interface setting | `u16 value,u16le index` | sets `anIntArray1130[index]`; dirty flags set. |
| 72 | Clear widget item container | `u8 widgetId` | `IncomingPacketDispatcher.clearWidgetItemContainer`. |
| 73 | Static region load | `i16le regionX,u16 regionY` | rebuilds region+landscape request list, requests JAGGRAB blobs, fires `RegionTransitionHandler.applyRegionShift`. |
| 74 | Now-playing song | `u8 songId(65535=-1)` | `aClass42_Sub1_1068.method558(2, songId)`. |
| 75 | Widget model id | `u16 modelId,u16 widgetId` | `IncomingPacketDispatcher.applyWidgetModelId`. |
| 78 | Clear destination flag | (none) | `anInt1261 = 0`. |
| 79 | Widget scroll clamp | `u8 widgetId,i16le scroll` | `IncomingPacketDispatcher.applyWidgetScrollClamp`. |
| 81 | Player update composite | variable | `method143` → `method117/134/91/49`. |
| 84 | (ground item count change) | falls through to `method137` op 84 | see `method137` table. |
| 85 | Scene base position | `u16le baseX,u16le baseY` | `anInt1269/1268`. |
| 87 | Varp int update | `u16 index,u16 value` | `InventoryPacketHandler.readVarpIntUpdate`. |
| 97 | Open main interface | `u16 widgetId` | clears overlays, sets `anInt857`. |
| 99 | Minimap state | `u8` | `anInt1021` (0 normal / 1 hide minimap / 2 mask-only). |
| 101 | Replace scene object | passed through `method137`. |
| 104 | Player context menu option | per `InterfacePacketHandler.applyPlayerMenuOption` | mutates `aStringArray1127`. |
| 105 | Player area sound | through `method137`. |
| 106 | Active tab | `u16le` | `anInt1221`. |
| 107 | Clear camera lock | (none) | `aBoolean1160=false`, resets `aBooleanArray876`. |
| 109 | Logout | (none) | `method44(true)`. |
| 110 | Energy update | `u8` | `anInt1148` (run energy / chatbox status). |
| 114 | System update timer | `u8 minutes` | multiplied by 30, stored in `anInt1104`. |
| 117 | Projectile add | via `method137`. |
| 121 | Queued song with delay | `u16 songId,i16le delay` | `aClass42_Sub1_1068.method558(2, …)`, `anInt1259`. |
| 122 | Widget text colour | per `InterfacePacketHandler.applyWidgetTextColor`. |
| 126 | Widget text | per `InterfacePacketHandler.applyWidgetText` | sets `aBoolean1153` if accepted. |
| 134 | Skill update | `u8 skillId,u32(little) xp,u8 level` | `IncomingPacketDispatcher.applySkillUpdate`. |
| 142 | Open chat-overlay interface | `u8 widgetId` | `method60`, `clearDialogAndInputOverlayState`, sets `anInt1189`. |
| 147 | Player carry object | via `method137`. |
| 151 | Replace scene object | via `method137`. |
| 156 | Remove ground item | via `method137`. |
| 160 | Spawn dynamic object | via `method137`. |
| 164 | Open chatbox widget | `u8 widgetId` | sets tab interface. |
| 166 | Camera lock | `u8 x,u8 y,u16 height,u8 speed,u8 acceleration` | populates `anInt1098..1102`; if `acc>=100` snaps camera. |
| 171 | Widget visibility | per `InterfacePacketHandler.applyWidgetVisibility`. |
| 174 | Generic area sound | per `SystemAudioPacketHandler.readAreaSoundEffect`. |
| 176 | Login welcome screen data | `u16le days,i16le bonus,u8 recovery,u32(le) ip,u16 unread` | populates last-login statistics and switches to widget id 670/671 (`c=0x28A`/`0x28F`) found via `Widget.anInt214` lookup. |
| 177 | Camera look-at | `u8 x,u8 y,u16 height,u8 speed,u8 acceleration` | re-computes yaw/pitch via `atan2`; clamps pitch to `[128,383]`. |
| 185 | Player identity widget | per `InterfacePacketHandler.applyPlayerIdentityWidget`. |
| 187 | Force chat-input keyboard | (none) | starts numeric? input mode (`anInt1225 = 2`). |
| 196 | Public chat | per `SocialPacketHandler.handlePublicChatPacket`. |
| 200 | Widget animation | per `InterfacePacketHandler.applyWidgetAnimation`. |
| 206 | Privacy options | `u8 publicChatMode,u8 privateChatMode,u8 tradeMode` | stored in `anInt1287/845/1248`. |
| 208 | Walkable overlay | `u16le widgetId` | sets `anInt1018`. |
| 214 | Ignore list snapshot | size = `anInt1007` | `IncomingPacketDispatcher.applyIgnoreListSnapshot`. |
| 215 | Add ground item (with old count) | via `method137`. |
| 218 | Reset varbit | `u24` | `anInt1042`. |
| 219 | Close all interfaces | (none) | `clearAllOpenInterfaces`. |
| 221 | Multi-icon flag | `u8` | `anInt900`. |
| 230 | Widget model transform | per `InterfacePacketHandler.applyWidgetModelTransform`. |
| 240 | Public chat mode | `u8` | `anInt878` via `InterfacePacketHandler.readPublicChatMode`. |
| 241 | Dynamic region load | bit stream (4×13×13 chunks) | parallel to op 73 but uses `anIntArrayArrayArray1129`, `aBoolean1159 = true`. |
| 246 | Widget item-on-model | per `InterfacePacketHandler.applyWidgetItemModel`. |
| 248 | Main + side interface pair | `i16le main,u16 side` | `setMainAndSidebarInterfaces`. |
| 249 | Mark local-player slot | `u16le isMember(?),u16 playerIndex` | sets `anInt1046,anInt884`. |
| 253 | Social request message | per `SocialPacketHandler.handleSocialRequestMessage`. |
| 254 | Hint icon | per `CameraPacketHandler.readHintIconState` | sets `anInt855/1222/937/938/934/935/936`; if `==10` mirrors into `anInt933`. |
| 105/84/147/215/4/117/156/44/160/101/151 | Composite world-event packet | inner opcode list | delegates to `method137(anInt1119, buf, anInt1008)`. |

### `method146(byte byte0)` (lines 10128–10204)
Per-frame world update + draw entry. Increments `anInt1265`, runs the four-pass actor scheduler (`method47(0,true)`, `method26(true,anInt882)`, mirror passes with `false`), then `method55`, `method104(true)`. When camera is not locked computes a smoothed `i` (zoom) and calls `method144` with yaw=`anInt1185 + anInt896` to update camera pose. Saves camera state, applies oscillation jitters from `aBooleanArray876` (axes 0-4: pos X, pos Y, pos Z, yaw, pitch with clamp). Sets `Model.aBoolean1684=true`, then renders (only when `byte0==1`): clears the depth/colour via `Rasterizer2D.method334`, calls `aClass25_946.method313` (scene draw) + `method288`, `method34(anInt898)` (entities), `method61(-252)` (UI), `method37(854,k2)` (overlay text), `method112(8)` (sprites). Pushes `aClass15_1165` to live `Graphics`. Restores camera state.

### `method147(int i)` (lines 10206–10225)
Sends opcode 130 (close-all-interfaces request). Resets `anInt1189`, `anInt1276`, `anInt857`. If `i <= 0`, the obfuscation guard, also appends a single trailing byte (`method398(13)`).

### `GameClientCore()` (line 10227)
Delegates to `GameClientCoreConstructorInit.initialize(this)`, the extracted helper that calls the six init methods in sequence.

### `constructorInitWorldEntityAndBuffers()` (lines 10232–10281)
Allocates ground-item deque grid, friends/ignores arrays, primary packet buffers (`aClass30_Sub2_Sub2_834` 5000-byte, `_847` length-9 fixed), NPC array (16 384 slots), player array sized `anInt888=2048`, skills arrays, RSA-related fields, camera oscillation arrays (size 5), tile-region scratch arrays (104×104), `CRC32` instance.

### `constructorInitRuntimeCachesAndChat()` (lines 10283–10312)
Allocates chat arrays (100-row scrollback), 13 sidebar-icon `IndexedSprite[]`, 200-row friends arrays, varp array (`anIntArray971` size 2000), camera-shake counter arrays (`anInt975=50`), area-sound queues.

### `constructorInitUiWidgetsAndDeques()` (lines 10314–10350)
Allocates `aClass30_Sub2_Sub1_Sub1Array987` (20 sprites), the projectile/graphic deques (`aClass19_1013`, `aClass19_1056`), the minimap sprite table (100 entries), `aClass9_1059`, and the receive `PacketBuffer aClass30_Sub2_Sub2_1083`.

### `constructorInitNetworkingAndContextMenus()` (lines 10352–10392)
Allocates context-menu arrays (`anIntArray1090..1094`, size 500), dynamic-region template grid `anIntArrayArrayArray1129` (4×13×13), player-action text arrays (5 slots), minimap dot sprites (`aClass30_Sub2_Sub1_Sub1Array1140`, size 1000), spawn-request deque `aClass19_1179`, outgoing send buffer `aClass30_Sub2_Sub2_1192`, login fields (default username/password empty, default server address).

### `constructorInitLateUiAndCollision()` (lines 10394–10414)
Allocates `aStringArray1199` (500-row tooltip cache), camera oscillation amplitude array, area sound delays (size 50), `aClass11Array1230` (per-plane `CollisionMap[]` of size 4), recent-chat ids (size 100).

### `constructorInitScratchArraysAndDefaults()` (lines 10416–10434)
Allocates `anIntArray1280/1281` (size 4000, used during projection clipping), default login state strings, initial login screen state = credentials.

### Static initialiser (lines 10939–10959)
Computes the canonical RuneScape XP curve into `anIntArray1019[99]`:

```
for(int j=0; j<99; j++) {
    int l = j+1;
    int i1 = (int)(l + 300D * Math.pow(2D, l/7D));
    i += i1;
    anIntArray1019[j] = i/4;
}
```
Then `anIntArray1232[k] = 2^(k+1) − 1` for `k ∈ [0,31]` — the bitmask LUT used by `method124` op 14 and the VarBit decoder.

## Cross-chunk dependencies

Fields and helpers consumed by this chunk but declared/owned elsewhere:

- `PacketSizeTable.anIntArray553` — packet length lookup (uncovered chunk).
- `aClass17_1000` (`IsaacCipher`) — server-side stream cipher used to deobfuscate opcode bytes.
- `aClass24_1168` (`BufferedConnection`) — TCP read wrapper.
- `aClass30_Sub2_Sub2_1083` (incoming) and `aClass30_Sub2_Sub2_1192` (outgoing) — both initialised here in the constructor helpers but used heavily by earlier chunks (chat send, click packets, etc.).
- `aClass30_Sub2_Sub4_Sub1_Sub2_1126` (local `Player`) and the player/npc arrays — populated by `method134/139/91/49` in earlier chunks but cleared here in `method145` op 64.
- `aClass25_946` (`SceneGraph`), `aClass11Array1230` (`CollisionMap`), `MapRegion.method188` — region rebuild helpers from chunks B/C.
- `method42` (height-at-tile), `method33` (varp cascade), `method44` (disconnect), `method58` (network poll), `method60` (open-widget tree walker), `method68` (login error), `method81` / `method84` (login submit), `method91/49/117` (mask block decoders), `method25` (tile dirty), `method31` (friends-list write) — all live in earlier chunks.
- Outer-class subsystems: `OnDemandFetcher aClass42_Sub1_1068`, `Skills`, `ObjectDefinition`, `NpcDefinition`, `ItemDefinition`, `VarBitDefinition`, `Widget`, `Rasterizer2D`, `Rasterizer3D`, `Model`, `SoundEffect`, `SignLink`, `TextUtils`, `ChatNameTagParser` — referenced extensively.
- The `LOGIN_SCREEN_*` and `LOGIN_INPUT_*` constants used in `method135/140` are declared in an earlier chunk (alongside `LOGIN_SCREEN_WELCOME = 14`, `_CREDENTIALS = 3`, `_PRIVATE_SERVER_INFO = 1` in the matching constants file).
- Camera debug freeze fields `cameratoggle`, `zoom`, `lftrit`, `fwdbwd` (`method144`) are static fields declared in the input handler chunk.

## Cross-class call graph

Refactored handlers split out from the original Jad output. Calls from this chunk:

- `IncomingPacketDispatcher` (`/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/IncomingPacketDispatcher.java`):
  - `applyIgnoreListSnapshot` ← op 214 (`GameClientCore.java:9527`)
  - `resetCameraEffects` ← op 107 (`9515`)
  - `applyCameraEffectUpdate` ← op 35 (`9721`)
  - `clearWidgetItemContainer` ← op 72 (`9521`)
  - `applyWidgetScrollPosition` ← op 70 (`9603`)
  - `applyWidgetModelId` ← op 75 (`9696`)
  - `readMinimapState` ← op 99 (`9690`)
  - `readSystemUpdateTimer` ← op 114 (`9702`)
  - `applySkillUpdate` ← op 134 (`9552`)
  - `readInterfaceSettingUpdate` ← op 71 (`9558`)
  - `readSongId`/`readSongDelayUpdate` ← op 74/121 (`9569`,`9582`)
  - `readMapFlagUpdate` ← op 60 (`9708`)
  - `readRegionPacketCoordinates`, `readDynamicRegionY/X`, `readDynamicRegionChunks` ← op 73/241 (`9609`,`9616`,`9617`,`9618`)
  - `readSceneBasePosition` ← op 85 (`9851`)
  - `readChatboxStatus` ← op 110 (`9785`)
  - `applyWidgetScrollClamp` ← op 79 (`9814`)
- `InterfacePacketHandler`: `applyPlayerIdentityWidget` (op 185), `readOverlayWidgetId` (208), `applyPlayerMenuOption` (104), `applyWidgetItemModel` (246), `applyWidgetVisibility` (171), `applyWidgetText` (126), `readPublicChatMode` (240), `applyWidgetNpcHeadModel` (8), `applyWidgetTextColor` (122), `applyWidgetItemGridSnapshot` (53), `applyWidgetModelTransform` (230), `applyWidgetAnimation` (200), `applyWidgetItemSlotUpdates` (34).
- `SocialPacketHandler`: `handleSocialRequestMessage` (253), `handleFriendStatusUpdate` (50), `handlePublicChatPacket` (196).
- `InventoryPacketHandler`: `readVarpIntUpdate` (87), `readVarpByteUpdate` (36).
- `CameraPacketHandler`: `readCameraLock` (166), `readHintIconState` (254).
- `SystemAudioPacketHandler`: `shouldQueueSong` (74), `shouldQueueDelayedSong` (121), `readAreaSoundEffect` (174).
- `RegionTransitionHandler`: `buildStaticRegionRequests`, `collectDynamicRegionIds`, `buildDynamicRegionRequests`, `applyRegionShift` (op 73/241).
- `GameServerEndpoint.DEFAULT_ADDRESS` ← login flow.
- `GameClientCoreConstructorInit.initialize` ← constructor.
- `ChatNameTagParser.parse` ← `method129` chat hover.
- `TextUtils.method583/586/588` ← name hashing, IP formatting, password masking.
- `ChatMessageCodec` (`/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ChatMessageCodec.java`) — invoked indirectly through `SocialPacketHandler` for public-chat decode (`method525/526/527`). Holds the static Huffman-style alphabet `aCharArray633` (62 chars) used for the compressed chat payload of opcode 196.
