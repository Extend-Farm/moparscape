# GameClientCore Chunk C (lines 3485-6526) — legacy moparscape

## Chunk overview

- **File:** `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
- **Lines covered:** 3485-6526 (~3 kLoC, 28 obfuscated `methodNNN` plus ~25 helper extracts).
- **Theme:** input/menu pipeline + login handshake + bootstrap.

  Despite the prompt's hypothesis, **there is no ~882-line render method in this range**. The two outsized methods are:
  - `method69` (3519-4219, ~700 lines) — the **menu-action dispatcher** that decodes the menu opcode `l` selected by the player and emits the matching outbound packet / state mutation. It is a flat `if(l == NNN) { ... }` ladder over ~50 opcodes.
  - `method84` (5924-6163, ~240 lines) — the **login handshake** (ISAAC seeded, RSA-blocked credentials, server response codes).

  The rest of the chunk falls into five clusters:
  1. **Reconnect & menu plumbing** (`method68`, `method69`, helpers 4221-4399).
  2. **Camera / tile-context probes** (`method70`, `method71`).
  3. **Lifecycle teardown / debug** (`method8`, `method72`, `method11`).
  4. **Keyboard text-entry / chat / friend-list interface drivers** (`method73`, `method74`, `method75`, `method76`, `method77`).
  5. **Menu construction at frame end + tab-mouse handling + asset (re)allocation** (`method78`-`method82`).
  6. **Login flow + path planner / NPC packet shim + audio queue + bootstrap** (`method83`-`method93`).

- **Cross-class references in this chunk:** `SceneGraph` (`aClass25_946.method304`, `method312`), `Model.anIntArray1688/anInt1687` (object-under-cursor list), `Widget.aClass9Array210`, `ItemDefinition.method198`, `NpcDefinition.method161`, `ObjectDefinition.method572`/`method580`, `Rasterizer3D.anIntArray1470/1471`, `MapRegion` is **not** touched directly here (terrain is owned by `SceneGraph`).

## Static / instance fields first introduced in this range

These are the field names that this chunk **writes** for the first time within `methodNNN` numerals 68-93. (Fields like `aClass30_Sub2_Sub2_1192`, `anInt1133`, `aStringArray1199`, `anIntArray1091..1094`, `aClass30_Sub2_Sub4_Sub1_Sub2_1126` etc. are declared higher in the file and are merely consumed/mutated; they are inherited from chunks A/B.)

| Legacy name | Inferred role | First-write site |
|---|---|---|
| `anInt1188` | Walk/operate cooldown counter (90-tick gate for ping packet 136 inside opcode 561) | `GameClientCore.java:3616` |
| `anInt924` | Object-action cooldown counter (113-tick gate, opcode 1062) | `GameClientCore.java:3661` |
| `anInt1226` | NPC-action cooldown counter (85-tick gate, opcode 225 -> packets 230/239) | `GameClientCore.java:3846` |
| `anInt1175` | Item-action cooldown counter (59-tick gate, opcode 867 -> packet 200/25501) | `GameClientCore.java:3998` |
| `anInt1134` | NPC-action cooldown counter (96-tick gate, opcode 965 -> packets 152/88) | `GameClientCore.java:3867` |
| `anInt986`  | Player-action cooldown counter (54-tick gate, opcode 27 -> packets 189/234) | `GameClientCore.java:3768` |
| `anInt1155` | NPC-action cooldown counter (53-tick gate per 4 ticks, opcode 478 -> packets 85/66) | `GameClientCore.java:4107` |
| `anInt1136` | Pending widget-action state (1 = "use widget on X" mode) | `GameClientCore.java:3730` |
| `anInt1137` | Pending widget id for action selection | `GameClientCore.java:3731` |
| `anInt1138` | Pending widget action-flag bitmask (`anInt237`) | `GameClientCore.java:3732` |
| `aString1139` | Pending action prompt string ("Cast <spell> on …") | `GameClientCore.java:3741` |
| `anInt1064` | Text-entry purpose: 1=add-friend, 2=remove-friend, 3=PM, 4=add-ignore, 5=remove-ignore | `GameClientCore.java:4082` |
| `aLong953`  | Target name-hash for current PM prompt | `GameClientCore.java:4083` |
| `aString1121` | "Enter message to send to NNN" prompt label | `GameClientCore.java:4084` |
| `anInt1251` | Wilderness/PvP region flag (0/1) | `GameClientCore.java:4403` |
| `anInt1133` | Menu-entry count (written extensively) | `GameClientCore.java:4436` |
| `anInt1287` / `anInt845` / `anInt1248` | Public/private/trade chat-mode preference bytes, persisted via packet 95 | `GameClientCore.java:4847-4854`, `5077-5092` |
| `aString887` | Text-buffer for in-game chat composition | `GameClientCore.java:4925` |
| `aBoolean1158` | Mod toggle "mute for 48h" flag visible at widget id 613 | `GameClientCore.java:4029`, `5387` |
| `aString881` | Report-abuse target name buffer | `GameClientCore.java:4028` |
| `anInt1178` | Active report-abuse interface id | `GameClientCore.java:4034` |
| `anInt1042` | Active sticky chat overlay id (consumed by `method77`) | `GameClientCore.java:5553` |
| `aString844` | Sticky overlay text driven by `method77` | `GameClientCore.java:5555` |
| `aClass30_Sub2_Sub1_Sub1_931 / 932` | Cached pair of compass sprites for widget 324/325 (animated flip) | `GameClientCore.java:5341-5343` |
| `aBoolean1031` | "Avatar model needs rebuild" flag for widget 327 | `GameClientCore.java:5300`, `5309` |
| `aClass15_1163..1166`, `aClass15_1123..1125` | The seven `ProducingGraphicsBuffer` panels (chat-tabs, side, viewport, chatbox, top tabs, bottom tabs) (re)created in `method79` | `GameClientCore.java:5794-5805` |
| `aBoolean1255` | "Just (re)created the panels — re-render everything" flag | `GameClientCore.java:5806` |
| `anInt1264 / anInt1288 / anInt1261 / anInt1262` | Route-planner persistent state (rotation/throttle/path step counts) routed through `RoutePlanner.route` | `GameClientCore.java:6168-6175` |
| `loginStatusPrimary / loginStatusSecondary` | Two-line login status text drawn by `method135`/`method68` ("Connection lost" etc.) | `GameClientCore.java:5931-5932`, `6103-6104` |
| `aClass17_1000` | Inbound ISAAC cipher (initialised with seeds+50) | `GameClientCore.java:5984` |
| `aLong1215` | Server-supplied login session key | `GameClientCore.java:5951` |
| `anInt863` | Player rights / mod-tier flag returned in login response 2 | `GameClientCore.java:5996` |
| `aBoolean1205` | "Account flagged" boolean returned at login | `GameClientCore.java:5997` |
| `anInt1062 / anInt874 / anInt1289 / aLong1172 / anInt1257 / anInt1259 / anInt1227 / aBoolean1228` | Music/SFX queue state managed by `AudioQueueProcessor` and `method90` | `GameClientCore.java:6219-6229` |
| `anInt1117` | "Random anti-AFK ping" counter (>1151 triggers packet 246) | `GameClientCore.java:6492-6495` |

## Methods

### `method68(int i)` — connection-lost reconnect loop  
**Lines:** 3485-3517 · **Sig:** `public final void method68(int i)` · **Returns:** `void`.

If `anInt1011 > 0` (login throttle active), forks to `method44(true)` and returns. Otherwise draws the two-line "Connection lost / Please wait - attempting to reestablish" banner via the chatbox `ProducingGraphicsBuffer` (`aClass15_1165`), drains outbound buffer with `aClass30_Sub2_Sub2_1192.method398(164)` while `i >= 0`, blits the banner to screen (`method238`), resets `anInt1021`/`anInt1261`, swaps in the saved `BufferedConnection`, calls `method84(loginUsername, loginPassword, true)` to re-login, then `method267()`-closes the old connection.

- **Called by:** `GameClientCore.java:2964, 3146, 4938, 10114` (idle-timeout, system-update, `::clientdrop` cheat, packet `OP_FORCE_LOGOUT`).
- **Calls:** `method44`, `method237`, `method381`, `method398`, `method238`, `method84`, `method267`.
- **Notes:** Parameter `i` is an opaque countdown / dummy; the loop `while(i >= 0) aClass30_Sub2_Sub2_1192.method398(164);` is the dummy-byte filler typical of jagex obfuscation passes.

### `method69(int i, boolean flag)` — menu-action dispatcher  
**Lines:** 3519-4219 · **Sig:** `public final void method69(int i, boolean flag)` · **Returns:** `void`.

This is the single largest method in the chunk. It consumes one menu entry `i` from the parallel arrays `aStringArray1199[]` (label), `anIntArray1091[]/1092[]` (x/y or slot/widget), `anIntArray1093[]` (opcode `l`), `anIntArray1094[]` (target id `i1`), and emits the corresponding outbound packet to `aClass30_Sub2_Sub2_1192` (the `PacketBuffer`). After dispatch it resets `itemUseState = 0` and (if `flag` is `false`) collapses any open menu and forces a chrome redraw.

**Block-by-block walkthrough** (opcode `l = anIntArray1093[i]`; payload local names `j,k,l,i1` shadow the array reads at `:3528-3531`):

| Lines | Opcode `l` | Inferred action | Outbound packet id emitted |
|---|---|---|---|
| 3521-3527 | (preamble) | Closes the typed-int chat overlay if open (`anInt1225 != 0`). | — |
| 3528-3533 | (read & remap) | Reads payload; if `l >= 2000` subtract 2000 (Jagex's "skip examine" overlay convention). | — |
| 3534-3550 | **582** | Item-on-NPC: route to NPC and ping packet **57** with item slot/widget/id. | 57 |
| 3551-3558 | **234** | Pick-up ground item: route to tile then ping packet **236** with absolute tile coords + item id. | 236 |
| 3559-3568 | **62**  | Use-item-on-object (gated by `method66`): packet **192**, item id+slot+widget+absolute tile. | 192 |
| 3569-3585 | **511** | Use-item-on-player: both walk-here and walk-adjacent attempts via `method85`, then packet **25**. | 25 |
| 3586-3593 | **74**  | "Widget action 4" via packet **122**; refreshes the dragged-slot via `MenuActionExecutor.applyDragSelection`. | 122 |
| 3594-3605 | **315** | "Click button" path through `Widget.anInt214 > 0` confirm (`method48`); on accept, packet **185** (button click). | 185 |
| 3606-3625 | **561** | Player walk-towards + packet **128** (follow). Includes 90-tick random anti-AFK ping **136**. | 128 (+136) |
| 3626-3639 | **20**  | NPC "primary" action (e.g. Attack/Talk-to first option) -> packet **155**. | 155 |
| 3640-3653 | **779** | Player action -> packet **153**. | 153 |
| 3654-3658 | **516** | "Walk here" — dispatches to `SceneGraph.method312` either at the click point (`!aBoolean885`) or relative. | (no packet — route only) |
| 3659-3673 | **1062**| Object 4th action (e.g. spell-cast on object) -> packet **228** with 113-tick anti-AFK ping **183**. | 228 (+183) |
| 3674-3679 | **679** | "Cancel report"/escape — packet **40** once (`aBoolean1149` latch). | 40 |
| 3680-3687 | **431** | Widget action variant -> packet **129**. | 129 |
| 3688-3689 | **337/42/792/322** | Social-list ops dispatched through `MenuActionExecutor.applySocialListAction`. | (delegated) |
| 3690-3697 | **53**  | Widget action -> packet **135**. | 135 |
| 3698-3705 | **539** | Widget action -> packet **16**. | 16 |
| 3706-3707 | **484/6** | Trade/challenge accept dispatched through `MenuActionExecutor.handlePlayerNameAction`. | (delegated) |
| 3708-3718 | **870** | "Use-item on widget item" -> packet **53** with both source and target slot/widget/item ids. | 53 |
| 3719-3726 | **847** | Widget action -> packet **87**. | 87 |
| 3727-3749 | **626** | **Begin spell/widget selection** — sets `anInt1136 = 1` and `aString1139` to the spell prompt; *returns early* (no packet). Special-cases `anInt1138 == 16` (PvP target spell). | — |
| 3750-3757 | **78**  | Widget action -> packet **117**. | 117 |
| 3758-3778 | **27**  | Player action (e.g. trade-with) -> packet **73** with 54-tick anti-AFK ping **189**. | 73 (+189) |
| 3779-3786 | **213** | Route + ground-item action -> packet **79**. | 79 |
| 3787-3794 | **632** | Widget action -> packet **145**. | 145 |
| 3795-3802 | **493** | Widget action -> packet **75**. | 75 |
| 3803-3810 | **652** | Route + ground-item action -> packet **156**. | 156 |
| 3811-3819 | **94**  | Spell-on-ground-item: route + packet **181** with `anInt1137` (selected spell). | 181 |
| 3820-3835 | **646** | Button **185** plus client-side var toggle via `anIntArray971` / `method33` (settings toggle). | 185 (+local var) |
| 3836-3856 | **225** | NPC action -> packet **17** with 85-tick anti-AFK pings **230/239**. | 17 |
| 3857-3877 | **965** | NPC action -> packet **21** with 96-tick anti-AFK ping **152/88**. | 21 |
| 3878-3892 | **413** | NPC action with spell id (`anInt1137`) -> packet **131**. | 131 |
| 3893-3894 | **200** | Closes interface (`method147(537)`). | — |
| 3895-3913 | **1025**| **NPC examine** (text-only) — formats `class5.aByteArray89` or "It's a NAME." into `method77`. | — |
| 3914-3921 | **900** | Object action -> packet **252**. | 252 |
| 3922-3935 | **412** | NPC action -> packet **72**. | 72 |
| 3936-3950 | **365** | Spell-on-player -> packet **249** with spell `anInt1137`. | 249 |
| 3951-3964 | **729** | Player action -> packet **39**. | 39 |
| 3965-3978 | **577** | Player action -> packet **139**. | 139 |
| 3979-3986 | **956** | Spell-on-object (gated by `method66`) -> packet **35**. | 35 |
| 3987-3994 | **567** | Route + ground-item action -> packet **23**. | 23 |
| 3995-4010 | **867** | Widget action -> packet **43** with 59-tick anti-AFK ping **200**. | 43 |
| 4011-4019 | **543** | Widget action -> packet **237**. | 237 |
| 4020-4042 | **606** | **Report-abuse**: extracts name after `@whi@`, opens interface 600 (sets `anInt857 = anInt1178 = widget236`) or rejects if any interface already open. | — |
| 4043-4059 | **491** | Use-item-on-player -> packet **14**. | 14 |
| 4060-4087 | **639** | "Message" PM prompt — looks up `friendNameHashes`, opens text-entry overlay with `anInt1064 = 3`. | — |
| 4088-4095 | **454** | Widget action -> packet **41**. | 41 |
| 4096-4117 | **478** | NPC action -> packet **18** with 53-tick anti-AFK ping **85/66** (every 4th tick). | 18 |
| 4118-4125 | **113** | Object action -> packet **70**. | 70 |
| 4126-4133 | **872** | Object action -> packet **234**. | 234 |
| 4134-4141 | **502** | Object 1st action ("Take/Use") -> packet **132**. | 132 |
| 4142-4155 | **1125**| **Item examine** with count formatting (`anInt252[j] >= 0x186a0` -> "N x NAME"). | — |
| 4156-4168 | **169** | Button **185** plus client-side var toggle (binary). | 185 |
| 4169-4179 | **447** | **Begin item-on-X selection** — sets `itemUseState=1`, `itemUseSlot/itemUseWidgetId/itemUseItemId`. Early return. | — |
| 4180-4190 | **1226**| **Object examine** via `ObjectDefinition.method572(i1 >> 14 & 0x7fff).aByteArray777`. | — |
| 4191-4198 | **244** | Route + ground-item action -> packet **253**. | 253 |
| 4199-4208 | **1448**| **Ground-item examine**. | — |
| 4209-4218 | (postamble) | Always clears `itemUseState = 0`. If `flag == false`, also clears `anInt1136 = 0` and flags chrome redraw. | — |

Many of these arms duplicate a 4-line idiom (`method85(...) ; anInt914=anInt27 ; anInt915=anInt28 ; anInt917=2 ; anInt916=0;`) which is what the helpers `routeToActor` (`:4233`) and `markMenuInteractionCursor` (`:4238`) below extract.

- **Called by:** `GameClientCore.java:414` (left-click default action), `:454` (idle re-issue), `:3050` (chat-message-area click), and by `keyPressed`-driven re-issue inside the same chunk.
- **Calls:** `method85` (route planner), `method66` (visibility), `method48` (button-confirm), `method77` (chat), `method147` (close interface), `method33` (var update), `routeToTileAndMarkCursor`, `MenuActionExecutor.applyDragSelection / applySocialListAction / handlePlayerNameAction`, `Widget`, `ItemDefinition.method198`, `NpcDefinition.method161`, `ObjectDefinition.method572 / method580`.
- **Notes:** Many client commands are accompanied by a periodic "anti-AFK" extra packet whose counter (`anInt1188 / anInt924 / anInt1175 / anInt1134 / anInt986 / anInt1155 / anInt1226`) is reset to 0 when the threshold fires. Several opcodes (626, 447) *do not* send a packet — they only put the client into a selection state.

### Inlined refactor helpers between `method69` and `method70` (4221-4399)

These plain-named methods were extracted from `method69`/elsewhere by previous chunks; they are present in the range and document the same idioms used inside the dispatcher.

- `updateSelectedDragTarget(widgetId, slot)` (4221-4231) — sets `selectedDragState` based on whether the dragged widget matches the active main (`anInt857`) or tab (`anInt1276`) interface. Called from drag-end handler in chunk B.
- `routeToActor(targetActor)` (4233-4236) — 1-liner around `method85` for follow/attack routing.
- `markMenuInteractionCursor()` (4238-4244) — yellow-X click marker (`anInt914/915 = mouse`, `anInt917 = 2`).
- `getOutboundBuffer()` / `getSystemUpdateTimer()` / `setSystemUpdateTimer()` / `getLoginThrottleTicks()` / `setLoginThrottleTicks()` / `isGameReadyForTick()` (4246-4274) — accessor wrappers around `aClass30_Sub2_Sub2_1192`, `anInt1104`, `anInt1011`, `aBoolean1157`.
- `routeToTileAndMarkCursor(tileX, tileY)` (4276-4282) — first tries walk-exact (`route mode 0`), then walk-adjacent (`route mode 1`), then sets cursor.
- `beginWidgetSelection(widgetId)` (4284-4305) — extracted from opcode 626.
- `beginReportAbuseFromAction(actionText)` (4307-4327) — extracted from opcode 606.
- `beginPrivateMessagePrompt(targetNameHash)` (4329-4348) — extracted from opcode 639.
- `closeMainInterfacesIfOpen()` / `clearDialogAndInputOverlayState()` / `setMainAndSidebarInterfaces(...)` / `setTabInterface(...)` / `clearAllOpenInterfaces()` (4350-4399) — interface stack helpers used both by `method69` arms and by inbound packet handlers in chunk D.

### `method70(int i)` — wilderness/PvP region flag  
**Lines:** 4401-4413 · **Sig:** `public final void method70(int i)` · **Returns:** `void`.

Computes the player's absolute tile (`anInt1550>>7 + anInt1034`, `anInt1551>>7 + anInt1035`) and sets `anInt1251 = 1` if the player is inside the wilderness rectangle (`3053..3156 x 3056..3136`) or its underground equivalent (`3072..3118 x 9492..9535`), with a carve-out for the Edgeville bank corner (`3139..3199 x 3008..3062`). Otherwise `anInt1251 = 0`.

- **Called by:** `GameClientCore.java:7497` (per-frame chrome update).
- **Calls:** none.
- **Notes:** `i = 58 / i` at line 4406 is the standard Jagex dummy-divide that throws if `i == 0`, used to detect injection.

### `run()` override  
**Lines:** 4415-4426 · **Sig:** `public final void run()` · **Returns:** `void`.

If `aBoolean880` (server-list mini-app mode), drives `method136((byte)59)`; else delegates to `super.run()`. Standard `RsApplet.run()` shim.

### `method71(int i)` — build right-click menu from objects under cursor  
**Lines:** 4428-4627 · **Sig:** `public final void method71(int i)` · **Returns:** `void`.

The cursor-pick stage of menu construction. Walks `Model.anIntArray1688[0..anInt1687]` — the list of 32-bit "scene-graph hit IDs" that the rasteriser stamped this frame, each packed as `(rot<<29) | (typeMask<<14) | (y<<7) | x` — and converts each into menu entries.

- Inserts a default "Walk here" (opcode 516, `:4432-4436`) when no special selection state is active.
- For each pick (deduplicated by previous `j`):
  - **type 2 (object):** if `SceneGraph.method304(plane, x, y, hit) >= 0` (object still visible at this corner), pull `ObjectDefinition`, then either: item-use option (opcode 62), spell-on-object option (opcode 956 if `anInt1138 & 4`), or default 5 actions mapped to opcodes 502/900/113/872/1062 + Examine (1226). (`:4449-4508`).
  - **type 1 (NPC):** if the NPC is "stacked" centre-tile, also include any other NPCs / players at the same tile (`method87`/`method88`); always emit menu for the picked NPC. (`:4509-4530`).
  - **type 0 (player):** same stacking-resolution rule, then emit player menu via `method88`. (`:4531-4552`).
  - **type 3 (ground-item):** iterate the `Deque` of ground items at `aClass19ArrayArrayArray827[plane][x][y]`. For each, item-use (511), spell-on-ground-item (94 if `anInt1138 & 1`), or default 5 actions mapped 652/567/234/244/213 plus implicit "Take" (234) and "Examine" (1448). (`:4553-4622`).
- Tail `if(i != 33660) anInt1008 = aClass30_Sub2_Sub2_1083.method408();` — dummy assertion (`i` is always 33660 from the sole call site).

- **Called by:** `GameClientCore.java:5860` (`method82` viewport hover-test).
- **Calls:** `method85` indirectly through `method87/88`, `SceneGraph.method304`, `ObjectDefinition.method572/method580`, `ItemDefinition.method198`, `method87`, `method88`.

### `method8(int i)` — full client teardown  
**Lines:** 4629-4761 · **Sig:** `public final void method8(int i)` · **Returns:** `void`.

Override of `RsApplet.method8` (applet destroy). Closes the `BufferedConnection`, calls `method15(860)` to stop the title-screen subsystem, nulls out the `MidiPlayer` (`aClass48_879`), stops `OnDemandFetcher.method553()`, then nulls *every* major field group: packet buffers, archives, sprites (`aClass30_Sub2_Sub1_*`), graphics buffers (`aClass15_*`), font renderers, sub-sprite arrays, actor arrays, route planner state, friend list, dragging state, varbits. Finally invokes the static `method57x` resets on `ObjectDefinition`, `NpcDefinition`, `ItemDefinition`, `Rasterizer3D.method363`, `SceneGraph.method273`, `Model.method458`, `AnimationFrame.method530`, then `System.gc()`.

- **Called by:** `RsApplet.destroy` (parent class), and by the `::clientdrop` path.
- **Notes:** `i = 55 / i` at `:4717` is the dummy-divide. `method118(3)` (called at `:4742`) stops the secondary music thread.

### `method72(byte byte0)` — fatal-error dump / `::lag` debug  
**Lines:** 4763-4780 · **Sig:** `public void method72(byte byte0)` · **Returns:** `void`.

Prints to stdout: `flame-cycle`, `OnDemand cycle`, `loop-cycle`, `draw-cycle`, `ptype`, `psize` (incoming packet id / size), forwards `aClass24_1168.method272((byte)1)` (asks the connection to dump pending state), and sets `super.aBoolean9 = true` (triggers the "report an error" dialog in `RsApplet`). The `byte0 == 1` branch suppresses the side effect `anInt961 = 281` — dead branch but `anInt961` is used as a sentinel elsewhere.

- **Called by:** `GameClientCore.java:4940` (`::lag` cheat).

### `method11(int i)` — resolve `Component` for input listeners  
**Lines:** 4782-4793 · **Sig:** `public final Component method11(int i)` · **Returns:** `Component` (preferring `embeddedHostComponent`, then `SignLink.mainapp`, then the outer `Frame`'s game pane, else `this`). Counter `anInt1007 += i`.

### `method73(int i)` — keyboard text-input dispatcher  
**Lines:** 4795-5099 · **Sig:** `public final void method73(int i)` · **Returns:** `void`.

Per-tick keyboard pump. Polls keys with `method5(-796)` in a `do…while` loop until -1. Decides where the keystroke goes by checking, in order:

1. **Report-abuse name field** (`anInt857 == anInt1178`): only `[a-zA-Z0-9 ]`, max 12.
2. **Single-line input overlay** (`aBoolean1256`): printable 32-122, max 80; on Enter dispatches based on `anInt1064` — add-friend (`method41`), remove-friend (`method35`), send PM (build packet 126 with ISAAC-keyed XTEA-encoded body via `ChatMessageCodec.method526` + censor `method497`), add-ignore (`method113`), remove-ignore (`method122`). Includes a server-side trade/duel public-chat reset (packet **95**).
3. **Typed-integer overlay** (`anInt1225 == 1`): digit-only, max 10, on Enter sends packet **208** (e.g. trade-X amount).
4. **Typed-name overlay** (`anInt1225 == 2`): printable, max 12, on Enter sends packet **60** (e.g. challenge-by-name).
5. **Main chat composition** (else, with no interface open): max 80, on Enter:
   - Local cheats (only when `anInt863 == 2`): `::clientdrop` -> `method68(-670)`; `::lag` -> `method72((byte)1)`; `::prefetchmusic` -> request all tracks; `::fpson/off` -> toggle `aBoolean1156`; `::noclip` -> zero all `CollisionMap.anIntArrayArray294`.
   - Any `::cmd` prefix sends packet **103** as raw bytes.
   - Otherwise parses `yellow:/red:/green:/cyan:/purple:/white:/flash1-3:/glow1-3:` colour prefix into `j2` (0-11) and `wave:/wave2:/shake:/scroll:/slide:` effect prefix into `i3` (0-5); body is XTEA-compressed into `aClass30_Sub2_Sub2_834`, then sent as packet **4** with `[effect, colour, body…]`; local `Player` overhead-text fields (`aString1506`/`anInt1513`/`anInt1531`/`anInt1535`) are set so the bubble appears on the local avatar; promotes public-chat mode 2->3 (packet 95).

- **Called by:** `GameClientCore.java:3088` (frame tick).
- **Calls:** `method5`, `method41`, `method35`, `method113`, `method122`, `method68`, `method72`, `method77`, `ChatMessageCodec.method526/method527`, `ChatCensor.method497`, `TextUtils.method583/587/584`.

### `method74(int i, int j, int k)` — chat-area right-click menu builder  
**Lines:** 5101-5179 · **Sig:** `public final void method74(int i, int j, int k)` · **Returns:** `void`.

Iterates the 100-entry chat ring (`chatMessages/chatTypes/chatSenders`) and for the row under mouse `(i, j)` adds menu entries for: Report abuse (opcode 606 if `anInt863 >= 1`), Add ignore (42), Add friend (337). For trade requests (type 4) adds "Accept trade" (484); for duel requests (type 8) adds "Accept challenge" (6). Filters by public/private/trade mode (`anInt1287`, `anInt845`, `anInt1248`) and "friends only" check `method109`. Each visible chat row consumes 14 px starting at `y = 70 - row*14 + anInt1089 + 4`. Sender name is the post-tag part decoded by `ChatNameTagParser.parse(...)`.

- **Called by:** `GameClientCore.java:5881` (chat-area hover inside `method82`).
- **Calls:** `ChatNameTagParser.parse`, `method109`.

### `method75(int i, Widget class9)` — widget runtime data-binding hook  
**Lines:** 5181-5488 · **Sig:** `public final void method75(int i, Widget class9)` · **Returns:** `void`.

Called from widget render with each `Widget` whose `anInt214 > 0`; the value of `anInt214` selects which client variable to bind into the widget's `aString248` / `anInt217` / `anInt261` / sprite slots. The script tags handled in this range:

| `anInt214` range | Purpose |
|---|---|
| 1, 2, 3-100 / 701-800 | Friend list name rows (and the two "Loading…" headers). |
| 101-200 / 801-900 | Friend list world-status text ("@gre@World-N"). |
| 203 | Friend-list scrollbar `anInt261` height. |
| 401-500 | Ignore list name rows. |
| 503 | Ignore-list scrollbar height. |
| 327 | Player-design avatar 3D model preview (builds 7-part `Model` from `anIntArray1065` IDK ids, applies colour swaps from `anIntArrayArray1003[l][0..anIntArray990[l]]`, gives standing animation `SequenceDefinition[anInt1511].anIntArray353[0]`). |
| 324 / 325 | Compass / minimap toggle sprite pair (`aClass30_Sub2_Sub1_Sub1_931/932`). |
| 600 | Report-abuse text field (`aString881 + "|"` cursor blink every 20 ticks). |
| 613 | Moderator "Mute for 48 hours: <ON/OFF>" label. |
| 650 / 655 | "You last logged in N days ago from IP X" line. |
| 651 | "N unread messages" (with yellow→green colour). |
| 652-654 | Three text rows on the welcome screen: members-warning, recovery-question reminder, or "you changed your questions N days ago" (toggled by `anInt1167`, `anInt1120`). |

- **Called by:** `GameClientCore.java` interface render passes in chunk D.
- **Notes:** This is essentially a hand-written CS1 emulator — every magic widget tag id is a different RuneScape engine variable.

### `method76(byte byte0)` — chatbox PM/trade overlay renderer  
**Lines:** 5490-5547 · **Sig:** `public final void method76(byte byte0)` · **Returns:** `void`.

If `anInt1195 == 0` (chat overlay disabled), early-return. Iterates `chatMessages[0..99]` and renders **up to 5** entries in the chatbox area starting at `y = 329 - i*13`, two-pass shadow-then-text using `aClass30_Sub2_Sub1_Sub4_1271` (p12 font), via `method385`. Handles message types: 3/7 (incoming PM), 5 (own outgoing PM ack), 6 (own outgoing PM body). Filters via `method109` for "friends only" PM mode. Crown icons (`byte1 == 1/2`) are drawn from `aClass30_Sub2_Sub1_Sub2Array1219[0/1]` (mod/admin icons), inserting 14 px. `byte0 != aByteValue1274` -> toggles `aBoolean1231` (dummy parameter check).

- **Called by:** `GameClientCore.java:7474` (chrome render).

### `method77(String s, int i, String s1, boolean flag)` — push chat ring  
**Lines:** 5549-5570 · **Sig:** `public final void method77(String s, int i, String s1, boolean flag)` · **Returns:** `void`.

Inserts message `s` of type `i` from sender `s1` at the head of the 100-deep ring (`chatMessages`/`chatTypes`/`chatSenders`); shifts the rest down. If `flag` is `true`, silently no-ops (used to suppress in muted contexts). Also kicks `aString844 = s` and `anInt26 = 0` for type-0 sticky overlay when `anInt1042 != -1`. Toggles `aBoolean1223` to redraw chatbox.

`addChatMessage(message, chatType, sender, messageFilterFlag)` (5572-5575) is the plain-named wrapper.

### `formatWidgetValue(Widget, int)` (5577-5580)
1-liner: `method93(369, method124(341, widget, valueIndex))`. Stringifies a widget script value, or `"*"` if `>= 999_999_999`.

### Accessor cluster (5582-5685)
Plain `get/set` for `ignoreCount`, `ignoredNameHashes`, `friendCount`, `friendNameHashes/DisplayNames/Worlds`, `recentChatWriteIndex`, `recentChatIds`, `dragState/Slot/WidgetId`, `dragStartMouseX/dragPointerY`, `selectedDrag*`, `itemUseState/Slot/WidgetId`. Pure data exposure for extracted helpers (notably `MenuActionExecutor`, `ChatRing`, `DragGesture`).

### `method78(int i)` — sidebar tab-click hit-test  
**Lines:** 5687-5777 · **Sig:** `public final void method78(int i)` · **Returns:** `void`.

`i = 72 / i` dummy-divide. If `super.anInt26 == 1` (mouse just clicked), checks the 14 tab-button rectangles (7 top-row at y∈[168,205], 7 bottom-row at y∈[466,503]) — when the click lands on a slot and `anIntArray1130[slot] != -1`, sets `anInt1221 = slot`, raises `aBoolean1153`/`aBoolean1103` to mark side panel + chrome dirty.

- **Called by:** `GameClientCore.java:3077` (per-frame click handling).
- **Notes:** Pixel rectangles are hard-coded 317 native resolution; mirrored in `GameplayChromeRenderer` in the modern client.

### `method79(int i)` — allocate `ProducingGraphicsBuffer` chrome panels  
**Lines:** 5779-5807 · **Sig:** `public final void method79(int i)` · **Returns:** `void`.

If `aClass15_1166` already exists, no-op. Otherwise nulls all `aClass15_*`, calls `method118(3)` (stop music), then constructs the seven `ProducingGraphicsBuffer` panels with their pixel dimensions: 479×96 (`1166`, chatbox-bottom), 172×156 (`1164`, minimap), 190×261 (`1163`, sidebar), 512×334 (`1165`, viewport), 496×50 (`1123`, top-tab strip), 269×37 (`1124`, ?), 249×45 (`1125`, ?). Calls `method6()` (bootstrap) once when not invoked with `i == 1` (login-success path passes 1 to skip re-bootstrap). Sets `aBoolean1255 = true`.

- **Called by:** `GameClientCore.java:6097` (after a successful login response 2).

### `resolveServerHost()` (5809-5819)
Returns hostname for the current run target: embedded host -> applet `mainapp` -> `"MoparScape.com"` in frame mode -> applet `getDocumentBase().getHost()`. Used by `method84` and the world-list code.

### `method81(Sprite class30_sub2_sub1_sub1, int i, int j, int k)` — draw minimap arrow or dot  
**Lines:** 5821-5845 · **Sig:** `public final void method81(Sprite, int i, int j, int k)` · **Returns:** `void`.

Given player-relative `(j, k)` (Δx, Δy) of an off-/near-edge target: if `j²+k² ∈ (4225, 0x15f90)`, draws an *arrow* on the minimap rim using `aClass30_Sub2_Sub1_Sub1_1001.method353(...)` rotated by `atan2(rotated dx, rotated dy)` and scaled by `(anInt1170 + 256) / 256` zoom. Otherwise delegates to `method141(class30_sub2_sub1_sub1, k, j, false)` (plain dot inside minimap). The `i >= 0` branch calls `method6()` — Jagex dummy assertion (parameter is always negative at call sites; would re-enter bootstrap if violated).

- **Called by:** `GameClientCore.java:8224, 8231, 8240` (friend/team-mate map dots).

### `method82(int i)` — viewport / sidebar / chat right-click resolver  
**Lines:** 5847-5914 · **Sig:** `public final void method82(int i)` · **Returns:** `void`.

If a drag is in progress (`dragState != 0`), early-return. Initialises `aStringArray1199[0] = "Cancel"` with opcode 1107 and `anInt1133 = 1`. Calls `method129(false)` (background hover for mini-map/etc.). Then runs `anInt886 = 0`; `method29(...)` over each of three rectangles:
- Viewport (4,4)-(516,338): widget-29 hit-test or `method71(33660)` if no main interface.
- Side panel (553,205)-(743,466): widget-29 over `Widget.aClass9Array210[anInt1189]` (or current tab widget).
- Chatbox (17,357)-(496,453): widget-29 over `Widget.aClass9Array210[anInt1276]`, or chat-area hit via `method74` if below 434 / left of 426.

Each region tracks the last hover id via `anInt1026`, `anInt1048`, `anInt1039` for dirty-flag purposes. Finally bubble-sort entries to keep `anIntArray1093[j] < 1000` ones first (`Cancel`/general entries at top, `<1000`-opcodes considered higher priority).

- **Called by:** `GameClientCore.java:3002, 7500` (frame ticks).

### `method83(boolean flag, int i, int j, int k)` — RGB alpha blend  
**Lines:** 5916-5922 · **Sig:** `public final int method83(boolean flag, int i, int j, int k)` · **Returns:** `int` (24-bit RGB lerp of `i` and `j` by `k/256`). If `!flag`, also nulls `aClass19ArrayArrayArray827` — dummy side effect (parameter is always `true`).

- **Called by:** `GameClientCore.java:8439-8456` (minimap tile colour interp).

### `method84(String s, String s1, boolean flag)` — login handshake  
**Lines:** 5924-6163 · **Sig:** `public final void method84(String username, String password, boolean isReconnect)` · **Returns:** `void`.

The full RS2 login protocol:

1. Set `SignLink.errorname = username`; show the "Connecting to server..." chrome if `LoginProtocolHandler.shouldShowConnectingUi(flag)`.
2. Open `BufferedConnection` to `openGameSocket(getConfiguredGamePort())`.
3. Send hash-prefix request: byte `14`, then `(nameHash >> 16 & 31)`.
4. Skip 8 bytes (server reserved), read 1 status byte `k = k1`. Then on `k == 0` (proceed):
   - Read 8-byte server `aLong1215` session key.
   - Build XTEA seed array `ai = {rand, rand, key_hi, key_lo}`.
   - Build the inner login payload (`packetId = 10`) with seeds, `SignLink.uid`, username, password, then RSA-encrypt via `method423(aBigInteger1032, aBigInteger856)` (RSA modulus + public exponent).
   - Wrap inner payload in outer login packet `16` (or `18` if reconnect): length, 255, `317` revision, member flag, 9 archive CRCs, RSA-blob.
   - Initialise outbound `IsaacCipher(-436, ai)` and inbound `IsaacCipher(-436, ai+50)` for packet-opcode XORing.
   - Re-read the new response byte `k`.
5. On `k == 1`: 2-second sleep then recursive retry.
6. On `k == 2`: success — read `anInt863` (rights), `aBoolean1205` (flagged). Reset *all* per-session state: clear actor arrays, gauge counters (`anInt1175`, `anInt1134`, `anInt986`, `anInt1288`, `anInt924`, `anInt1188`, `anInt1155`, `anInt1226`, `anInt941`, `anInt1260`), randomise camera jitter (`anInt1278`, `anInt1131`, `anInt896`, `anInt1209`, `anInt1170`, `anInt1185`), construct the local `Player`, allocate `aClass19_1179`, call `method79(1)` to (re)allocate chrome.
7. On other codes: delegate to `LoginProtocolHandler.resolveLoginError(k)` for a 2-line user error message.
8. On `k == 15` (reconnect-OK): reset minimal in-game state and return.
9. On `k == 21` (server-transfer wait): countdown UI driven by `LoginProtocolHandler.resolveTransferCountdownMessage`, then recursive retry.
10. On `k == -1` (no response): retry with backoff via `shouldRetryAfterNoResponse(i1, anInt1038)` or display network error.
11. Catch `IOException` → "Error connecting to server."

- **Called by:** `method68`, `method84` (self, recursive), and the title-screen Login button handlers at `:9144, :9163, :9211`.
- **Notes:** `aBigInteger1032`/`aBigInteger856` are the RSA modulus/exponent built into the binary. The `255, 317, memberFlag, 9×CRC` envelope is the canonical 317 login.

### `method85(...)` — route planner shim  
**Lines:** 6165-6177 · **Sig:** `public final boolean method85(int routeType, int unused1, int adjacent, int sentinel, int unused2, int srcY, int allowApproach, int unused3, int destY, int srcX, boolean fastpath, int destX)` · **Returns:** `boolean` (true if route accepted).

Packs `{anInt1264, anInt1288, anInt1261, anInt1262}` into a local `routeState[]`, delegates to `RoutePlanner.route(...)` with the collision map (`aClass11Array1230`), tile heights (`anIntArrayArray901/825`), edge dx/dy (`anIntArray1280/1281`), region offset (`anInt1034/1035`) and outbound packet buffer (`aClass30_Sub2_Sub2_1192`), unpacks updated state. The 12-argument signature with `sentinel = -11308` and several unused ints preserves the original obfuscated entry point — `RoutePlanner` was extracted in a prior chunk but `method85` is left as the seam so existing callers don't change.

- **Called by:** ubiquitous — every walk/route in `method69`, `method71`, `method92`, plus `method82`, plus the parser packets in chunk D.

### `applyNpcUpdateMasks(...)` (6179-6182) and `method86(int i, PacketBuffer, boolean)` (6184-6187)
1-line wrappers around `NpcUpdateMaskHandler.applyNpcUpdateMasks(...)`. Updates `aBoolean1157` connection-health flag based on decode success.

- **Called by:** chunk D, packet `OP_NPC_UPDATE`.

### `method87(NpcDefinition class5, int i, boolean flag, int j, int k)` (6189-6194)
Builds NPC right-click entries via `EntityMenuBuilder.buildNpcMenu(...)`. `flag` is a dummy XOR on `aBoolean919`.

- **Called by:** `method71`.

### `method88(int i, int j, Player target, boolean flag, int k)` (6196-6201)
Builds Player right-click entries via `EntityMenuBuilder.buildPlayerMenu(...)`. Early-returns when `flag` is true.

- **Called by:** `method71`.

### `method89(boolean flag, SceneObjectSpawnRequest req)` (6203-6213)
Populates `req.anInt1299 / 1300 / 1301` (3 packed orientation/height attributes) by querying `SceneGraph` via `SceneObjectSpawnHandler.readSpawnAttributes(aClass25_946, req)`. The `flag` branch contains an empty infinite-loop sentinel (`for(int j1 = 1; j1 > 0; j1++);`) — Jagex obfuscation dummy.

- **Called by:** `GameClientCore.java:3172, 8364` (loc-update packets).

### `method90(boolean flag)` (6215-6231)
Single-tick audio pump. Delegates to `AudioQueueProcessor.process(...)` and writes back to `anInt1062 / anInt874 / anInt1289 / aLong1172 / anInt1257 / anInt1259`. If the processor signals `pendingSongRequest`, sets `anInt1227 = audioState.requestedSongId` and `aBoolean1228 = true` (music thread will pick it up). The `flag` dummy resets `anInt1008 = -1`.

- **Called by:** `GameClientCore.java:2961` (frame tick).

### `method6()` — bootstrap (game-startup pipeline)  
**Lines:** 6233-6329 · **Sig:** `public final void method6()` · **Returns:** `void`.

Override of `RsApplet.method6` (applet `init`). Calls `method13(20, (byte)4, "Starting up")` to draw progress. Then in a `try`:

1. `bootstrapLoadTitleResources()` — title screen archive, p11/p12/b12/q8 fonts. (6331-6341).
2. `bootstrapLoadArchivesAndScene()` — config, interface, media, textures, wordenc, sounds, versionlist; allocate `aByteArrayArrayArray1258`, `anIntArrayArrayArray1214`, the `SceneGraph(104,43,104,..,4)`, 4 `CollisionMap`s, 512×512 minimap sprite. (6343-6360).
3. `bootstrapConnectUpdateServer(updateListArchive)` — open `OnDemandFetcher`, register with version list, prime `AnimationFrame` and `Model` index counts. (6362-6369).
4. `bootstrapPrimeOnDemand()` — block until OnDemand queue drains or aborts. (6371-6396).
5. `bootstrapLoadDemandQueues()` — queue model/map/anim downloads. (6398-6407).
6. `BootstrapMediaLoader.loadCoreMedia(mediaArchive)` — invback/chatback/mapback, side icons, compass, mapedge.
7. `BootstrapMediaLoader.loadMapSceneSprites/loadMapFunctionSprites/loadHitmarkSprites/loadHeadIconSprites`.
8. `BootstrapMediaLayoutLoader.load` — map markers, cross sprites, mapdots, scrollbar, redstone variants, mod icons.
9. `BootstrapBackBufferLoader.createBackBuffers` — `aClass15_903..911` (login-screen panels).
10. `BootstrapDemandLoader.applyRandomMapSpriteTint` (per-instance tint randomisation).
11. `BootstrapConfigLoader.loadTexturesAndConfig`, `loadSoundsIfNeeded`, `loadInterfaces` (CS1 widget definitions).
12. `BootstrapGraphicsSetup.prepareMapbackClipMasks` (8 fixed clip rows for the round minimap), then `initializeRasterizerAndSceneVisibility(aBoolean1231)` returning scanline-offset tables for viewport, chatbox, fullscreen rendering.
13. `BootstrapEngineFinalizer.finalizeStartup` — starts the MIDI thread (`aClass48_879`), the chat-censor, and the input listeners.

On any `Exception`: `SignLink.reporterror("loaderror " + aString1049 + " " + anInt1079)` and `aBoolean926 = true` (load-failed flag).

- **Called by:** applet lifecycle, and from `method79` when first allocating chrome.
- **Notes:** This method was previously inline; only the local helpers (`bootstrap*` methods 6331-6407) plus the `BootstrapArchives` nested DTO class (6409-6417) live in this chunk.

### `method91(PacketBuffer, int packetLen, byte dummy)` (6419-6452)
Decodes the "local player list" bit-packed region from packets `OP_PLAYER_UPDATE` — for each 11-bit player slot (`j != 2047`), allocates a new `Player` (with last-known appearance bytes from `aClass30_Sub2_Sub2Array895[j]`), reads a 1-bit `update mask?` and 1-bit walking flag plus signed 5-bit dx/dy relative to local player, and calls `class30_sub2_sub4_sub1_sub2.method445(localX+dx, localY+dy, walking==1, false)`. Pads `byte0 == 8 ? 0 : anInt1119 = -50` as anti-tamper. Finishes with `method420(true)` to flush bit pointer.

- **Called by:** `GameClientCore.java:9334` (`OP_PLAYER_UPDATE` decode).

### `method92(boolean flag)` (6454-6515)
Minimap click handler + periodic random ping. `aBoolean1157 &= flag`; if a system update is active or no click this tick, return. Mouse rectangle `[(550..696) x (4..155)]` minus a 73×75 centring offset gives `(i, j)` relative to compass origin. Rotates by camera yaw (`anInt1185 + anInt1209`) scaled by zoom (`anInt1170+256>>8`), divides by `>> 11` for tile delta, applies to player tile to get target tile `(i2, j2)`, and submits `method85(1, …, true, i2)` (mode 1 = walk-to-minimap). On success, hand-encodes a 12-byte debug telemetry packet (mouse pos, rotation, anti-cheat camera angles).

Then increments `anInt1117`; when it crosses 1151, emits a randomised packet **246** (12 bytes of noise) — the legendary "client random ping" feature used by Jagex to fingerprint repos.

- **Called by:** `GameClientCore.java:3076` (frame tick).

### `method93(int i, int j)` (6517-6525)
`if(i <= 0) anInt1008 = aClass30_Sub2_Sub2_1083.method408();` (dummy assertion). Returns `String.valueOf(j)` if `j < 999_999_999`, else `"*"`. Used to format large item counts on widgets.

## Cross-chunk dependencies (methods *not* defined in this range)

Called by this range but defined elsewhere in `GameClientCore.java`:

| Method | Defined at | Used by (in this chunk) |
|---|---|---|
| `method5(-796)` (key poll) | chunk A | `method73:4800` |
| `method6()` (bootstrap entry) | also chunk C @6233, but called recursively from `method79:5803`, `method81:5825` |
| `method11(int)` (component lookup) | chunk C @4782 — called from `method79:5794+` |
| `method13(progress)` | chunk A | `method6` cluster |
| `method15(860)` (title-screen stop) | chunk A | `method8:4639` |
| `method16(533)` (title-screen load) | chunk A | `bootstrapLoadTitleResources:6333` |
| `method28(filename)` (cache load error) | chunk B | `method6:6256` |
| `method29(...)` (widget hit-test) | chunk B | `method82:5858/5866/5869/5878` |
| `method33(updateVar)` | chunk B | `method69:3831`, `method69:4165` |
| `method35(removeFriend)` | chunk B | `method73:4834` |
| `method41(addFriend)` | chunk B | `method73:4829` |
| `method44(loginDisplay)` | chunk B | `method68:3489/3507` |
| `method45(0)` (reset varbits) | chunk B | `method84:6077` |
| `method48(confirmWidget)` | chunk B | `method69:3599` |
| `method51(215)` (postFontLoad) | chunk B | `bootstrapLoadTitleResources:6340` |
| `method56(0)` (titleArchiveLoaded) | chunk B | `bootstrapLoadTitleResources:6339` |
| `method57(false)` (poll-OnDemand) | chunk B | `bootstrapPrimeOnDemand:6385` |
| `method66(visibility)` | chunk B | `method69:3559/3668/3916/3979/4120/4128/4136` |
| `method67(...)` (cache fetch) | chunk B | `bootstrap*` (6334, 6346-6358) |
| `method109(friendsOnlyFilter)` | chunk D | `method74:5117/5136/5155/5167`, `method76:5507` |
| `method113(addIgnore)` | chunk D | `method73:4860` |
| `method118(3)` (stop music thread) | chunk D | `method79:5783`, `method8:4742` |
| `method122(removeIgnore)` | chunk D | `method73:4865` |
| `method124(widget varbit eval)` | chunk D | `formatWidgetValue:5579` |
| `method129(false)` (chrome hover) | chunk D | `method82:5854` |
| `method135(true, false)` (login chrome draw) | chunk D | `method84:5933/6130` |
| `method136(byte)` (server-list run) | chunk D | `run:4419` |
| `method141(sprite, dx, dy, false)` (minimap dot blit) | chunk D | `method81:5842` |
| `method147(537)` (close interface) | chunk D | `method69:3894/4027/4317` |

Called from elsewhere into this range (forward callers):

| Method here | Caller chunk | Site |
|---|---|---|
| `method68` | A/B/D | `:2964, :3146, :4938, :10114` |
| `method69` | A | `:414, :454, :3050` (left-click defaults / pending menu re-issue) |
| `method70` | D | `:7497` |
| `method71` | this chunk @5860 (via `method82`) |
| `method72` | this chunk @4940 (`::lag`) |
| `method73` | A @3088 |
| `method74` | this chunk @5881 |
| `method75` | D (widget render) |
| `method76` | D @7474 |
| `method77` | A/B/D — every chat insertion |
| `method78` | A @3077 |
| `method79` | this chunk @6097 (login success) |
| `method81` | D @8224/8231/8240 (minimap friend dots) |
| `method82` | A @3002, D @7500 |
| `method83` | D @8439-8456 (minimap colour interp) |
| `method84` | this chunk + A/D (`:9144/9163/9211, :3505`) |
| `method85` | ubiquitous |
| `method86`/`method87`/`method88` | D (packet handlers + `method71`) |
| `method89` | A @3172, D @8364 |
| `method90` | A @2961 |
| `method6`  | RsApplet boot, plus `method79:5803` |
| `method91` | D @9334 |
| `method92` | A @3076 |
| `method93` | D @5579 (`formatWidgetValue`) |

## Cross-class call graph

- `SceneGraph` (`server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java`):
  - `aClass25_946.method304(plane, x, y, hitId)` — `GameClientCore.java:4449` (`method71`). Returns whether the picked-object id is still present at the corner; gates object menu insertion.
  - `aClass25_946.method312(false, screenX-4, screenY-4)` — `GameClientCore.java:3656/3658` (`method69` opcode 516, "walk here"). Submits a walk-to-screen-point route through the scene graph's screen-to-tile conversion.
  - `new SceneGraph(104, (byte)43, 104, anIntArrayArrayArray1214, 4)` — `bootstrapLoadArchivesAndScene:6354`.

- `Model` (model rasteriser, static cursor pick table):
  - `Model.anIntArray1688` / `Model.anInt1687` — `GameClientCore.java:4439/4441` (`method71`). The frame's "things under the cursor" list emitted by `Model.method469` and consumed here.
  - `new Model(part_count, parts[], -38)` — `method75:5319` (avatar preview).

- `MapRegion`: **no direct references** in this chunk. Map state is owned by `SceneGraph` and `CollisionMap`, both already populated.

- `Rasterizer3D`: `Rasterizer3D.anIntArray1470/1471` (`method92:6468/6469`) for cos/sin of camera yaw during minimap click routing. `Rasterizer3D.method363(-501)` in `method8:4756` (reset).

- `Widget`: `Widget.aClass9Array210` accessed throughout `method69`, `method71`, `method74`, `method75`, `method82`. `Widget.method208(0, aBoolean994, 5, model)` injects the rebuilt avatar model into widget id 327 (`method75:5333`).

- `RoutePlanner.route(...)` — sole call site `method85:6171`.
- `EntityMenuBuilder.buildNpcMenu / buildPlayerMenu` — `method87/88`.
- `MenuActionExecutor.applyDragSelection / applySocialListAction / handlePlayerNameAction` — used inside many arms of `method69`.
- `AudioQueueProcessor.process` — `method90:6219`.
- `NpcUpdateMaskHandler.applyNpcUpdateMasks` — `applyNpcUpdateMasks:6181`.
- `SceneObjectSpawnHandler.readSpawnAttributes` — `method89:6205`.
- `ChatMessageCodec.method526/method527`, `ChatCensor.method497`, `ChatNameTagParser.parse` — chat composition & display (`method73`, `method74`, `method76`).
- `LoginProtocolHandler.shouldShowConnectingUi / resolveLoginError / resolveNoResponseError / resolveTransferCountdownMessage / shouldRetryAfterNoResponse / sleepSilently` — `method84`.
- `BootstrapMediaLoader / BootstrapMediaLayoutLoader / BootstrapBackBufferLoader / BootstrapConfigLoader / BootstrapDemandLoader / BootstrapGraphicsSetup / BootstrapEngineFinalizer / ClientBootstrapLoader` — orchestrated by `method6` + the seven `bootstrap*` private helpers.
- `TextUtils.method583/584/587` — name-hash <-> string for friends/PM.
- `IsaacCipher` — `method84:5980/5984` (in/out ciphers).
- `OnDemandFetcher` — `method6` cluster + teardown.
