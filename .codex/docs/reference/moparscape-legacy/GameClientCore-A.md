# GameClientCore Chunk A (lines 1–1923) — legacy moparscape

- **Path:** `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
- **Total file length:** 10,960 lines. Class `GameClientCore extends GameShell implements SocialOutputPort, WidgetConditionPort` (`GameClientCore.java:22`).
- **This chunk:** lines 1–1923 covering `init`, `method12`, `openGameSocket`, `getConfiguredServerEndpoint`, `getConfiguredGamePort`, `method15` through `method41` (28 methods total).

## Chunk overview

- **Method range covered:** `init` / `method12` / `openGameSocket` / `getConfiguredServerEndpoint` / `getConfiguredGamePort` / `method15`–`method41`.
- **Themes observed:**
  1. **Boot + socket setup** — `init` (`:285`), `method12` (`:297`), `openGameSocket` (`:317`), `method19` (`:338`). Selects between embedded host, signed-applet socket factory, and direct `Socket`.
  2. **Chatbox rendering** — `method18` (`:130`) draws the 8-channel public/private/clan/trade/game chat overlay with per-line filter checks (`anInt1287`, `anInt845`, `anInt1248`, `anInt1195`).
  3. **Scene rebuild / map streaming pipeline** — `method22` (`:468`) and its helpers `method23` (`:687` — cache flush), `method24` (`:700` — minimap underlay/object snapshot), `method25` (`:783` — per-tile ground-item submission), `method26` (`:821` — NPC submission). This is the "load new region" code path.
  4. **Click / menu dispatch** — `method17` (`:118` opcode predicate), `method20` (`:344` mouse-click dispatch + drag detection), `method29` (`:875` recursive widget hit-test), `method40` (`:1826` right-click menu draw).
  5. **Per-actor overhead rendering** — `method34` (`:1327`) overhead icons (skull/protection prayers), chat bubbles, hit-splat bars; `method38` (`:1698`) chat-bubble TTL countdown; `method31` (`:1119`) NPC update packet decode.
  6. **Camera smoothing + texture animation** — `method39` (`:1732`) camera lerp; `method37` (`:1627`) cycles three scrolling water/lava textures (slots 17/24/34) including a periodic anti-tamper noise packet.
  7. **Settings → behaviour fan-out** — `method33` (`:1229`) acts on `VarpDefinition.aClass41Array701[i].anInt709` to apply brightness, music volume, sound volume, chat-effect, mouse-button, split-private and accept-aid settings.
  8. **Friend list mutators** — `method35` (`:1574` remove friend), `method41` (`:1869` add friend), `method32` (`:1152`) handles chat-filter button clicks (cycle public/private/trade) + a periodic anti-tamper packet 165.
  9. **Misc** — `method15` music stop; `method16` legacy CRC checksum loop (now commented out); `method21` MIDI save; `method27` audio replay hook; `method28` fatal load-error redirect; `method30` chat scrollbar; `method36` minimap chrome.

- **Pointers to other chunks for callees in this range:**
  - `method2`, `method6`, `method13`, `method12` (super) live in **earlier ranges of `GameShell`** or chunk pre-A.
  - `method42` (height sampler at tile), `method46` (NPC walk decode), `applyNpcUpdateMasks`, `method50`, `method63`, `method65`, `method69`, `method77`, `method103`, `method105`, `method109`, `method111`, `method116`, `method123`, `method127`, `method132`, `method138`, `method139`, `method147` — all in **chunks B / C / D / E** (line >1923).

## Static fields touched

Only fields first read/written here are listed. Full catalog deferred to integration index.

| Legacy | Inferred name | Type | Touched in |
|---|---|---|---|
| `LOGIN_SCREEN_WELCOME` | screen id `0` | `int` const | declared `:24`; consumed in chunks > A. |
| `LOGIN_SCREEN_CREDENTIALS` | screen id `2` | `int` const | `:25`. |
| `LOGIN_SCREEN_PRIVATE_SERVER_INFO` | screen id `3` | `int` const | `:26`. |
| `LOGIN_INPUT_USERNAME` | input id `0` | `int` const | `:27`. |
| `LOGIN_INPUT_PASSWORD` | input id `1` | `int` const | `:28`. |
| `cameratoggle` | toggle-camera key flag | `public static int` | declared `:29`; not touched in this range. |
| `zoom` | zoom key state | `public static int` | `:30`. |
| `lftrit` | strafe (A/D) state | `public static int` | `:31`. |
| `fwdbwd` | forward/back (W/S) state | `public static int` | `:32`. |

## Instance fields touched (first read or first write in this range)

Only the ones touched in chunk A. Annotations are inferred from usage context — full catalog will be unified in the integration index.

- `aBoolean1206` — opaque anti-tamper toggle flipped in `method15` (`:39`).
- `anIntArray1090` — 9-int login/crc handshake buffer (`:47, 49, 60, 64, 70, 76, 81, 86`). Index 8 acts as "checksum-ok" gate.
- `aBoolean872` — opaque toggle flipped in `method16` retry loop (`:113`).
- `aBoolean1206`, `aBoolean991`, `aBoolean959`, `aBoolean960`, `aBoolean1224`, `aBoolean1141`, `aBoolean1153`, `aBoolean1157`, `aBoolean1158`, `aBoolean1159`, `aBoolean1223`, `aBoolean1228`, `aBoolean1233`, `aBoolean1242`, `aBoolean1255`, `aBoolean1256`, `aBoolean848`, `aBoolean885`, `aBoolean919`, `aBoolean1149`, `aBoolean1151` — UI/state flags toggled across `method18/22/24/29/32/33/36/39`.
- `anIntArray1093` (opcode), `anIntArray1091` (slot), `anIntArray1092` (widget id), `anIntArray1094` (item id), `aStringArray1199` (caption), `anInt1133` (menu entry count) — the right-click menu entry table; written by `method29` (`:910–1082`), read by `method17`/`method20`/`method40`.
- `anInt1276` (top-chat-widget id), `anInt1042` (top-side-widget), `anInt857` (open interface id), `anInt1178`, `anInt1189` (sidebar widget), `anInt1221` (active tab), `anIntArray1130` (tab→widget map) — interface stack pointers, read/cleared throughout `method18/29/32/36`.
- `anInt948` — 0=fixed/4-pixel chrome, 1=sidebar, 2=chatbox (positional offset switch repeated in `method20/32/40`).
- `aClass30_Sub2_Sub2_1083` — `PacketBuffer` used as a timer source via `method408` to drive `anInt1008` (anti-tamper write-poison).
- `aClass30_Sub2_Sub2_1192` — outgoing packet writer; many `method397/398/399/404/407` invocations (chat-filter packet 95, anti-tamper 150/165/210/226, friend-add 188, friend-del 215).
- `aClass15_1163`/`1165`/`1166` — `RsImageProducer` raster targets (sidebar/scene/chat).
- `aClass30_Sub2_Sub1_Sub2_1024`, `..._1025`, `..._1196`, `..._1198`, `aClass30_Sub2_Sub1_Sub2Array1219`, `aClass30_Sub2_Sub1_Sub1Array1095/987/1140/1033`, `aClass30_Sub2_Sub1_Sub4_1270/1271/1272` — sprites & fonts for chat scrollbar / minimap / chatbox / hitsplat / overheads.
- `chatMessages`, `chatTypes`, `chatSenders`, `anInt1089` (chat scroll offset), `anInt1211` (chat content height) — chat ring buffer.
- `loginUsername`, `aString881`, `aString887`, `aString844`, `aString1004`, `aString1121`, `aString1139`, `aString1212`, `aString1199` — credential/dialog text buffers.
- `friendCount`, `friendDisplayNames`, `friendNameHashes`, `friendWorlds`, `ignoreCount`, `ignoredNameHashes` — friend/ignore arrays mutated by `method35/41`.
- `aClass19ArrayArrayArray827` — per-plane ground-item deques (`method25`).
- `aClass19_1056`, `aClass19_1013` — region-load linked lists cleared by `method22`.
- `aByteArrayArrayArray1258` — `[plane][x][y]` tile-flag byte plane reset to 0 in `method22:486`, read in `method24:712`.
- `aByteArrayArray1183`, `aByteArrayArray1247` — raw map/object cache blobs keyed by `anIntArray1234` (region coord pairs) — fed to `MapRegion` in `method22:497–608`.
- `aClass11Array1230` — 4 `CollisionMap` instances (one per plane), reset `method22:480`, populated by `MapRegion`.
- `aClass25_946` — the `SceneGraph` ([Reference](SceneGraph.md)); cleared & repopulated each `method22`, fed per-tile in `method24/25/26`.
- `anIntArrayArrayArray1214`, `anIntArrayArrayArray1129` — instanced-map mapping table for `aBoolean1159` instances.
- `anInt1034`, `anInt1035`, `anInt1069`, `anInt1070` — region origin & player-region coords (read in `method22`).
- `anInt918` — player's current plane.
- `anInt1097`, `anInt1051`, `anInt854`, `anInt940` — anti-tamper counters incrementing each call, when overflow triggers a noise packet (`method22/24/32/37`).
- `aClass30_Sub2_Sub4_Sub1_Sub2_1126` — local player (`Player`); read in `method18:268`, `method34:1334`, `method41:1902`.
- `aClass30_Sub2_Sub4_Sub1_Sub2Array890` / `anIntArray892` / `anInt891` / `anInt889` — surrounding player array + index list + local-player slot.
- `aClass30_Sub2_Sub4_Sub1_Sub1Array835` / `anIntArray837` / `anInt836` / `anIntArray840` / `anInt839` — NPC arrays.
- `anIntArrayArray929`, `anInt1265` — "tile occupied this tick" stamp used by `method26`.
- `anInt949/950/951/952` — right-click menu rect; read by `method20/40`.
- `dragState`, `dragWidgetId`, `dragSlot`, `dragStartMouseX`, `dragPointerY`, `anInt989`, `aBoolean1242` — inventory drag state set up in `method20`.
- `anInt995/996`, `anInt997`, `anInt858/859/860/861/862`, `anInt1098/1099/1100/1101/1102`, `anInt998/999` — camera target/current + ease constants used by `method39`.
- `anInt1265`, `anInt1161` — tick counters.
- `anInt974/975`, `anIntArray976/977/978/979/980/981/982`, `aStringArray983` — chat-bubble bucket built by `method34`.
- `anIntArray965` — chat-color palette (clan rank colours).
- `anInt963/964` — projected screen XY for the actor currently being annotated (filled by `method127`).
- `aByteArray912`, `anInt945` — scratch ping-pong buffer + scroll offset for `method37`'s texture animator.
- `embeddedHostComponent`, `configuredServerAddress` — embedding/host configuration (`:301, :320, :330`).

## Methods

### `method15(int i)` — proposed name `stopMusicPlayback` ([GameClientCore.java:34](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L34))

- **Signature:** `public final void method15(int i)`
- **Purpose:** Stops the currently-playing MIDI by zeroing fade and writing `"stop"` to `SignLink.midi`. The `i` parameter is an obfuscator dummy: when `i <= 0` it flips `aBoolean1206` (a junk toggle used to defeat tamper-detection / inlining).
- **Parameters:** `i` — opaque guard; documented call sites pass `860` and `833` so the toggle never fires in practice.
- **Returns:** void.
- **Called by:** `method33` ([GameClientCore.java:1284](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1284)) when music volume is set to 0; the boot path at `:1976`; the logout path at `:4639`.
- **Calls:** `SignLink.midifade`/`SignLink.midi` writes only.
- **Notes:** Anti-tamper guard parameter — see "Themes" above.

### `method16(int i)` — proposed name `verifyClientArchiveCrcLoop` ([GameClientCore.java:42](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L42))

- **Signature:** `public final void method16(int i)`
- **Purpose:** Historically downloaded `crc<rand>-317` from the web server to verify cache archive checksums against `anIntArray1090[0..8]`, retrying with exponential backoff up to 10 attempts. In this build the entire body is commented out — the method is now a no-op stub kept so call-site offsets stay valid.
- **Parameters:** `i` — opaque guard; assigned `aClass17_1000.method246()` to `anInt1218` when `i <= 0` (dead code now).
- **Returns:** void.
- **Called by:** `method31`-vicinity boot stages at `:6333`.
- **Calls:** (commented out) `method13`, `method132`, `PacketBuffer.method413`, `Thread.sleep`.
- **Notes:** Body is entirely commented out — leaving just the method signature.

### `method17(int i, int j)` — proposed name `isExamineMenuEntry` ([GameClientCore.java:118](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L118))

- **Signature:** `public final boolean method17(int i, int j)`
- **Purpose:** Returns true when menu entry index `j` corresponds to opcode `337` (after stripping the `2000` "examine variant" offset used when the menu entry is the "examine" line). Used to decide whether the bottom menu entry deserves "second-option" treatment in `method20`.
- **Parameters:** `i` — opaque guard (when `i != 9`, also resets `anInt1008`); `j` — index into `anIntArray1093`.
- **Returns:** `boolean` — true if entry is opcode 337 (after de-offset).
- **Called by:** `method20` ([:451](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L451)); main tick at `:3046`.
- **Calls:** none.
- **Notes:** Opcode `337` is the cancel/examine sentinel.

### `method18(int i)` — proposed name `renderChatbox` ([GameClientCore.java:130](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L130))

- **Signature:** `public final void method18(int i)`
- **Purpose:** Draws the chatbox raster `aClass15_1166`: backdrop sprite, then either an input prompt (`aBoolean1256` price-checker; `anInt1225==1` amount; `anInt1225==2` name) or, by default, the 100-entry chat ring buffer with per-channel filter rules (public/private/clan/trade), then the username + asterisked entry input. Also overlays the right-click menu if `aBoolean885 && anInt948==2`.
- **Parameters:** `i` — opaque guard; when `i<6 || i>6` toggles `aBoolean991`.
- **Returns:** void.
- **Called by:** main render loop at `:7001`.
- **Calls:** `Rasterizer3D.anIntArray1472` assignment, `aClass30_Sub2_Sub1_Sub2_1198.method361`, `FontRenderer.method381/385/383/386`, `Rasterizer2D.method333/332/339`, `method105` (widget draw — chunks > A), `method109` (friend check — chunks > A), `method40` (this chunk), `method30` (this chunk), `TextUtils.method587`, `aClass15_1166.method237/238`.
- **Notes:** Chat type codes (used at `:183–257`): 0=game, 1=public, 2=public clan tagged, 3=private incoming, 4=trade request, 5=server msg, 6=private outgoing, 7=mod private, 8=clan / trade msg. `byte0` is `@cr1@` / `@cr2@` crown prefix decode.

### `init()` ([GameClientCore.java:285](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L285))

- **Signature:** `public final void init()`
- **Purpose:** Applet entry point. Clears `anInt957/958`, forces members+highmem (`method52(false)`), then calls `method2(503, false, 765)` to start the `GameShell` lifecycle.
- **Returns:** void.
- **Called by:** the applet container.
- **Calls:** `method52`, `method2` (both in `GameShell` / chunks > A).
- **Notes:** The commented `method138((byte)77)` (`lowmem`) and `aBoolean959=false` (`free`) preserved for reference — the live config is "members + high-detail".

### `method12(Runnable runnable, int i)` ([GameClientCore.java:297](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L297))

- **Signature:** `public final void method12(Runnable runnable, int i)`
- **Purpose:** Override of `GameShell.method12` (thread spawner). Caps priority at 10; in embedded mode forwards to `super`; otherwise prefers `SignLink.startthread` when running as a signed jar, falling back to `super`. Used for music / cache-loader background threads.
- **Parameters:** `runnable` — task; `i` — thread priority (1..10).
- **Returns:** void.
- **Called by:** super class lifecycle; cache loader.
- **Calls:** `super.method12`, `SignLink.startthread`.

### `openGameSocket(int port)` ([GameClientCore.java:317](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L317))

- **Signature:** `public final Socket openGameSocket(int port) throws IOException`
- **Purpose:** Opens a TCP connection to the configured server. In embedded mode uses `java.net.Socket` directly; in signed-jar mode delegates to `SignLink.opensocket`; otherwise plain socket again.
- **Parameters:** `port` — TCP port.
- **Returns:** open `Socket`.
- **Called by:** `method19` (`:341`), login pipeline (`:5935, :8424`).
- **Calls:** `getConfiguredServerEndpoint().host()`, `SignLink.opensocket`.

### `getConfiguredServerEndpoint()` ([GameClientCore.java:328](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L328))

- **Signature:** `private GameServerEndpoint getConfiguredServerEndpoint()`
- **Purpose:** Parses `configuredServerAddress` into a host/port pair.
- **Returns:** `GameServerEndpoint`.
- **Called by:** `openGameSocket`, `getConfiguredGamePort`, host-string getters at `:5812`, `:6593`, `:6601`.
- **Calls:** `GameServerEndpoint.fromConfiguredAddress`.

### `getConfiguredGamePort()` ([GameClientCore.java:333](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L333))

- **Signature:** `public final int getConfiguredGamePort()`
- **Purpose:** Returns the parsed game port (typically 43594).
- **Called by:** login bootstrap at `:5935`.

### `method19(int i)` ([GameClientCore.java:338](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L338))

- **Signature:** `public final Socket method19(int i) throws IOException`
- **Purpose:** Trivial delegating overload kept so legacy callers (e.g. `JagGrabConnection`) can pass the `int` port through the original signature.
- **Calls:** `openGameSocket`.

### `method20(int i)` — proposed name `processMouseClick` ([GameClientCore.java:344](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L344))

- **Signature:** `public final void method20(int i)`
- **Purpose:** Per-tick mouse-click dispatcher. (1) If `aBoolean885` (right-click menu open) and mouse leaves a 10-px buffer, dismiss menu; if left-click inside, resolve which entry was hit and call `method69`. (2) Otherwise: detect a long-press / drag on an inventory entry (`anIntArray1093` opcodes for "use/wear/wield…") and arm `dragState`. (3) Otherwise pick the bottom menu entry: `method69` for left-click, `method116(true)` for right-click → opens the menu.
- **Parameters:** `i` — opaque guard; when `i != 4` resets `anInt1008` from the packet-timer.
- **Called by:** main tick at `:3075`.
- **Calls:** `method17` (this chunk), `method69`, `method116` (chunks > A).
- **Notes:** Coordinate offsets for `anInt948` 0/1/2 correspond to fixed-mode (4,4), sidebar (553,205), chatbox (17,357) — same triple appears in `method32/40`. Opcodes 632/78/867/431/53/74/454/539/493/847/447/1125 are the "draggable inventory action" set.

### `method21(boolean flag, int i, byte[] abyte0)` — proposed name `persistMidi` ([GameClientCore.java:460](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L460))

- **Signature:** `public final void method21(boolean flag, int i, byte abyte0[])`
- **Purpose:** Saves a MIDI blob to disk via `SignLink.midisave` and sets the fade flag.
- **Parameters:** `flag` — whether to fade in (1) or not (0); `i` — opaque guard; `abyte0` — MIDI bytes.
- **Called by:** boot music-load at `:2789`.

### `method22(boolean flag)` — proposed name `rebuildSceneAfterRegionLoad` ([GameClientCore.java:468](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L468))

- **Signature:** `public final void method22(boolean flag)`
- **Purpose:** Full scene re-assembly after a region change. Resets follow target / region lists / Rasterizer brightness, drops every cache table (`method23`), zeroes the 4-plane `aByteArrayArrayArray1258` tile-flag grid, then re-feeds the new `MapRegion` from per-region terrain (`method180`) and loc (`method190`) blobs OR — when `aBoolean1159` — from the per-instance `anIntArrayArrayArray1129` chunk mapping table (`method179/183`). Finally calls [`MapRegion.method171`](MapRegion.md) to bake lighting, paints each tile's ground items (`method25`), and proactively unloads loc-models 9 tiles outside the visible 13x13.
- **Parameters:** `flag` — gets `&=`d into `aBoolean1157` (region-loaded latch).
- **Called by:** main tick at `:2650`.
- **Calls:** `method23/25/63` (this chunk + > A), `aClass11Array1230[].method210`, `new MapRegion`, `MapRegion.method180/174/190/179/183/171`, `SceneGraph.method274/275`, `ObjectDefinition.aClass12_785.method224`, `Rasterizer3D.method366/367`, `Model.method461`, `aClass42_Sub1_1068.method555/559/560/562/566` (model-cache GC).
- **Notes:** The five anti-tamper sentinels `anInt1097`, `anInt1051` here trigger occasional `aClass30_Sub2_Sub2_1192.method397((byte)6, 238/150)` packets, fingerprinting the client.

### `method23(boolean flag)` — proposed name `flushDefinitionCaches` ([GameClientCore.java:687](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L687))

- **Signature:** `public final void method23(boolean flag)`
- **Purpose:** Clears all definition LRU caches: `ObjectDefinition.aClass12_785/780`, `NpcDefinition.aClass12_95`, `ItemDefinition.aClass12_158/159`, `Player.aClass12_1704`, `SpotAnimationDefinition.aClass12_415`. Called both from `method22` (region change) and at logout / boot (`:1970`).
- **Parameters:** `flag` — opaque guard; when true resets `anInt1008`.

### `method24(boolean flag, int i)` — proposed name `rebuildMinimapPlane` ([GameClientCore.java:700](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L700))

- **Signature:** `public final void method24(boolean flag, int i)`
- **Purpose:** Rebuilds the minimap raster for plane `i`. Zeros the destination pixel buffer of `aClass30_Sub2_Sub1_Sub1_1263`, then walks the 103x103 inner tiles, asking `SceneGraph.method309` to draw underlay colour for each tile whose flag `& 0x18` is clear, plus the plane above for bridge tiles (flag bit `8`). After that it pastes per-object minimap icons by walking `SceneGraph.method303` and looking up `ObjectDefinition.anInt746` — for each object with a non-(-1) icon, picks an unoccupied neighbour tile via a 10-step random walk constrained by `CollisionMap.anIntArrayArray294`, and records it into `aClass30_Sub2_Sub1_Sub1Array1140/anIntArray1072/1073` for later minimap icon paint.
- **Parameters:** `flag` — `&=` into `aBoolean1157`; `i` — plane index (0..3).
- **Called by:** main tick at `:2608`.
- **Calls:** `SceneGraph.method309/303`, `ObjectDefinition.method572`, `method50` (chunks > A).
- **Notes:** Random colour jitter on minimap floor (`j1`, `l1`) gives the watery shimmer.

### `method25(int i, int j)` — proposed name `submitGroundItemsAtTile` ([GameClientCore.java:783](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L783))

- **Signature:** `public final void method25(int i, int j)`
- **Purpose:** Pushes up to three stacked ground items at tile `(i,j)` on the current plane to the scene graph. Picks the most-valuable stack first (`ItemDefinition.anInt155` × quantity if stackable) and rotates it to the top of the `Deque`, then picks the next two of differing item ids — these become the "top / middle / bottom" item models. Sends them to [`SceneGraph.method281`](SceneGraph.md) keyed by tile.
- **Parameters:** `i`,`j` — tile coords (0..103).
- **Called by:** `method22` (`:625`), main tick at `:8755, 8793, 8818, 8989, 9494`.
- **Calls:** `ItemDefinition.method198`, `Deque.last/previous/addLast`, `method42` (chunks > A), `SceneGraph.method281`.
- **Notes:** Tag `0x60000000 | i | (j<<7)` is the scene-graph object key used later for click-to-pick-up.

### `method26(boolean flag, int i)` — proposed name `submitNpcsForRender` ([GameClientCore.java:821](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L821))

- **Signature:** `public final void method26(boolean flag, int i)`
- **Purpose:** Iterates all currently-known NPCs; for each one whose `NpcDefinition.aBoolean93` matches `flag`, projects it onto its tile and submits it via [`SceneGraph.method285`](SceneGraph.md). Skips a tile if another NPC already claimed it this tick (`anIntArrayArray929 == anInt1265`). For "transparent / cannot click" NPCs sets the `0x80000000` "translucent" bit on the entity tag.
- **Parameters:** `flag` — pass selector (called twice per tick to do solid then transparent passes); `i` — anti-tamper sentinel (must be `-30815`).
- **Called by:** main tick at `:10132, :10134`.
- **Calls:** `Actor.method449`, `method42`, `SceneGraph.method285`.

### `method27(int i)` — proposed name `replayWave` ([GameClientCore.java:847](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L847))

- **Signature:** `public final boolean method27(int i)`
- **Purpose:** Anti-tamper guarded wrapper around `SignLink.wavereplay()`. Throws NPE if `i != 11456`.
- **Notes:** Pure guard parameter — caller must pass `11456`.

### `method28(String s)` — proposed name `fatalLoadError` ([GameClientCore.java:855](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L855))

- **Signature:** `private final void method28(String s)`
- **Purpose:** Logs `s` to stdout, navigates the applet to `loaderror_<s>.html`, and then loops forever sleeping 1 s — i.e. permanently halts the client thread after a fatal asset-load failure.
- **Called by:** boot path at `:6256` (`"ondemand"` failure).

### `method29(int i, int j, Widget class9, int k, int l, int i1, int j1)` — proposed name `collectMenuEntriesFromWidget` ([GameClientCore.java:875](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L875))

- **Signature:** `public final void method29(int i, int j, Widget class9, int k, int l, int i1, int j1)`
- **Purpose:** Recursive hit-test of an interface tree. Walks `class9.anIntArray240` children, offsets each by `anInt263/265/241/272`, recurses into containers, and when the mouse `(k,i1)` falls inside an actionable child (`anInt217 == 1..6`) or an inventory slot (`anInt262 == 2`) it appends one or many entries to the global right-click menu (`aStringArray1199`, `anIntArray1093` opcodes, `anIntArray1091/1092/1094` payloads, `anInt1133` counter). Per-item entries depend on whether item-use is armed (`itemUseState`), magic-spell is armed (`anInt1136 == 1`), or default menu (Examine + per-item `aStringArray189` + per-widget `aStringArray225`). Also tracks the hovered widget tooltip via `anInt886`.
- **Parameters:** `i`,`l` — parent absolute X/Y; `j` — anti-tamper (`13037`); `class9` — parent widget; `k`,`i1` — mouse X/Y; `j1` — scroll offset (passed `class9_1.anInt224` recursively).
- **Called by:** itself recursively, and from the menu-build pass at `:5858, :5866, :5869, :5878`.
- **Calls:** `method29` (self), `method65` (chunks > A — scroll-bar variant), `method103` (chunks > A — condition check), `ItemDefinition.method198`.
- **Notes:** Opcodes the entry inserts here are decoded in `method69`/`method79` (chunks > A): 169 button-click, 200 close interface, 315 reset action, 447 use-on-item, 493/847 Drop/Drop-X, 539/454/74 inventory option 0/1/2, 543 cast spell on item, 626 tooltip-link, 632/78/867/431/53 widget custom actions, 646 widget select, 679 friend-list button, 870 use-with, 1125 Examine.

### `method30(int i, int j, int k, int l, int i1, int j1)` — proposed name `drawChatScrollbar` ([GameClientCore.java:1097](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1097))

- **Signature:** `public final void method30(int i, int j, int k, int l, int i1, int j1)`
- **Purpose:** Draws the scrollbar at `(i1, l)` of width 16 and height `j` with content-size `j1` and current scroll `k`. Renders up arrow, down arrow, track and proportionally-sized thumb with edge highlights.
- **Parameters:** `i` — anti-tamper guard (when `<=0`, samples `aClass17_1000.method246()` into `anInt1105`); `j` — visible height; `k` — current scroll; `l` — top Y; `i1` — X; `j1` — total content height.
- **Called by:** `method18` chat at `:266`; widget rendering at `:5916` etc.

### `method31(PacketBuffer class30_sub2_sub2, int i, int j)` — proposed name `decodeNpcUpdateBlock` ([GameClientCore.java:1119](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1119))

- **Signature:** `private final void method31(PacketBuffer class30_sub2_sub2, int i, int j)`
- **Purpose:** Decode the NPC info packet. Calls `method139` (NPC walk-flags / queue), then `method46` (NPC index list / add-NPC opcodes), then `applyNpcUpdateMasks` (NpcInfoMaskUpdate). Removes NPCs marked stale (`anInt1537 != anInt1161`). Sanity-checks the bit-cursor matches packet length and that no slot in `anIntArray837` is null, calling `SignLink.reporterror` + `throw new RuntimeException("eek")` on mismatch.
- **Parameters:** `class30_sub2_sub2` — packet; `i` — expected packet length; `j` — opaque guard (resets `anInt877`).
- **Called by:** main packet loop at `:9989`.

### `method32(boolean flag)` — proposed name `handleChatButtonClicks` ([GameClientCore.java:1152](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1152))

- **Signature:** `public final void method32(boolean flag)`
- **Purpose:** Handles left-click on the four chat-filter buttons drawn under the chatbox: public (x≈6..106), private (135..235), trade (273..373), report-abuse (412..512). For each filter button, cycles state (public 0..3, private/trade 0..2), sets dirty flags, and emits packet `95(filter)` writing the three new states. Report-abuse opens widget id `600` if no interface is open, else shows an error via `method77`. Includes an `anInt940 > 1386` periodic anti-tamper packet (opcode 165) with random padding.
- **Parameters:** `flag` — `&=` into `aBoolean1157`.
- **Called by:** main tick at `:3078`.
- **Calls:** `method77`, `method147` (chunks > A).

### `method33(boolean flag, int i)` — proposed name `applyClientSetting` ([GameClientCore.java:1229](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1229))

- **Signature:** `public final void method33(boolean flag, int i)`
- **Purpose:** Reacts to a setting change indexed by varp `i`. Looks up `VarpDefinition.aClass41Array701[i].anInt709` to find the setting kind, reads the new value from `anIntArray971[i]`, and dispatches: `1` brightness (`Rasterizer3D.method372` with 0.9/0.8/0.7/0.6 gamma + item icon cache flush), `3` music volume (writes `method123` and re-arms streaming), `4` sound effect volume (`method111`), `5` mouse buttons (`anInt1253` 0=two/1=one), `6` chat effect speed (`anInt1249`), `8` split-private (`anInt1195`), `9` accept-aid (`anInt913`).
- **Parameters:** `flag` — opaque guard (sets `anInt961`); `i` — varp index.
- **Called by:** varp-update packet handler (`:3831, :4165, :9824, :10037, :10054`).
- **Calls:** `method15/111/123` (this chunk + > A), `Rasterizer3D.method372`, `ItemDefinition.aClass12_158.method224`.

### `method34(int i)` — proposed name `drawOverheads` ([GameClientCore.java:1327](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1327))

- **Signature:** `public final void method34(int i)`
- **Purpose:** Per-frame overhead drawer. Iterates local player + 2046 surrounding players + NPCs. For each: (a) if `Player.anInt1706` (overhead-prayer/skull bitmask), paint up to 8 icons from `aClass30_Sub2_Sub1_Sub1Array1095`; (b) for NPCs with `anInt75` head-icon, paint that icon; (c) chat bubble text via `method127` projection into a collision-resolved `anIntArray976/977/978/979` rect — coloured per `anIntArray980` channel (clan crowns + flash colour cycles in `method34:1477-1521`); (d) HP bar (`anInt1532 > anInt1161`) and up to 4 hit-splats (`anIntArray1516`). Two-pass: first the projection, then a layout pass that pushes overlapping bubbles upward (`l1 = anIntArray977[l2] - anIntArray978[l2]`).
- **Parameters:** `i` — when non-zero, calls `method6` (likely cursor reset).
- **Called by:** main render loop at `:10192`.
- **Calls:** `method127` (project to screen — chunks > A), `method6` (chunks > A), `FontRenderer.method381/385/386/387/388/389`, `Rasterizer2D.method333/336/332`.
- **Notes:** Local-player target highlighting (`anInt855 == 10`) uses icon slot 7; NPC target (`anInt855 == 1`) uses slot 2 and only blinks when `anInt1161 % 20 < 10`.

### `method35(boolean flag, long l)` — proposed name `removeFriend` ([GameClientCore.java:1574](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1574))

- **Signature:** `public final void method35(boolean flag, long l)`
- **Purpose:** Removes the friend whose 38-bit name-hash equals `l`. Shifts the arrays down, marks `aBoolean1153` dirty, and emits server packet `215` with the hash. Reports errors with caller context via `SignLink.reporterror`.
- **Parameters:** `flag` — opaque guard; `l` — name hash.
- **Called by:** menu handler at `:4834`.

### `method36(byte byte0)` — proposed name `renderSidebar` ([GameClientCore.java:1608](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1608))

- **Signature:** `public final void method36(byte byte0)`
- **Purpose:** Renders the 190-wide right sidebar raster (`aClass15_1163`): backdrop sprite (`aClass30_Sub2_Sub1_Sub2_1196`), then either an explicit overlay widget (`anInt1189`) or the active tab's widget (`anIntArray1130[anInt1221]`), with a right-click menu overlay when `aBoolean885 && anInt948==1`. Then flushes to the AWT canvas at (553, 205).
- **Parameters:** `byte0` — opaque guard (must be `-81`).
- **Called by:** main render at `:6972`.
- **Calls:** `method105`, `method40`.

### `method37(int i, int j)` — proposed name `animateScrollingTextures` ([GameClientCore.java:1627](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1627))

- **Signature:** `public final void method37(int i, int j)`
- **Purpose:** Scrolls texture slots 17, 24, 34 (water / lava / animated) by `anInt945 * 2` pixels by rotating their pixel buffer through scratch `aByteArray912` and re-uploading. Anti-tamper: every 1235 calls emits packet `226` with random payload.
- **Parameters:** `i` — opaque guard; `j` — minimum texture-usage count required (only animates textures whose `Rasterizer3D.anIntArray1480 >= j`).
- **Called by:** main render at `:10194`.

### `method38(byte byte0)` — proposed name `tickChatBubbleTtls` ([GameClientCore.java:1698](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1698))

- **Signature:** `public final void method38(byte byte0)`
- **Purpose:** Decrements `anInt1535` (chat-bubble TTL in ticks) on local + visible players + NPCs; clears `aString1506` when TTL hits 0.
- **Parameters:** `byte0` — opaque (`-92`).
- **Called by:** main tick at `:2967`.

### `method39(byte byte0)` — proposed name `interpolateCamera` ([GameClientCore.java:1732](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1732))

- **Signature:** `public final void method39(byte byte0)`
- **Purpose:** Smoothly eases the camera position (`anInt858/859/860`) toward focus tile `(anInt1098, anInt1099)` and the look-at (`anInt995/996`), using a fixed-step (`anInt1101/998`) + proportional (`anInt1102/999`) lerp. Computes desired pitch (`l1`) and yaw (`i2`) via `atan2`, clamping pitch to `[128, 383]` (~ 22.5° to 67.5°). Yaw `j2` wraps through ±1024 to take the short way.
- **Parameters:** `byte0` — opaque (must be `5`); otherwise toggles `aBoolean919`.
- **Called by:** main tick at `:3084`.
- **Calls:** `method42` (terrain height at tile — chunks > A).

### `method40(byte byte0)` — proposed name `drawRightClickMenu` ([GameClientCore.java:1826](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1826))

- **Signature:** `public final void method40(byte byte0)`
- **Purpose:** Draws the floating right-click menu box at `(anInt949, anInt950, anInt951, anInt952)`. Fills body, paints title "Choose Option", then each `aStringArray1199[l1]` entry from bottom-to-top (so last-pushed is on top), highlighting the entry under the mouse in yellow `0xffff00`.
- **Parameters:** `byte0` — opaque (must be `9`).
- **Called by:** `method18`/`method36` overlays + main render at `:7504`.

### `method41(byte byte0, long l)` — proposed name `addFriend` ([GameClientCore.java:1869](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1869))

- **Signature:** `public final void method41(byte byte0, long l)`
- **Purpose:** Adds a friend by 38-bit name hash. Enforces friend limits (100 for free, 200 for members based on `anInt1046`), checks duplicates, checks ignore list, refuses self-add, then appends to `friendDisplayNames/NameHashes/Worlds`, marks dirty, and emits server packet `188` with the hash.
- **Parameters:** `byte0` — opaque (must be `68`); `l` — name hash.
- **Called by:** menu handler at `:4829`.

## Cross-chunk dependencies

These methods are called by chunk A but defined elsewhere in `GameClientCore` (line > 1923):

| Callee | Used by (chunk A) | Likely chunk |
|---|---|---|
| `method2` | `init` | pre-A (`GameShell` lifecycle) |
| `method6` | `method34` | early B |
| `method13` | `method16` (commented) | B |
| `method42` | `method22/25/26/39` | B |
| `method46`, `applyNpcUpdateMasks`, `method139` | `method31` | B/C (packet decoding) |
| `method50` | `method24` | B |
| `method52` | `init` | B |
| `method63` | `method22` | B |
| `method65` | `method29` | B |
| `method69`, `method116` | `method20` | B/C (menu handler) |
| `method77` | `method32`, `method41` | B (dialog open) |
| `method103`, `method105`, `method109` | `method18`, `method29`, `method36` | B/C (widget condition / render / chat filter) |
| `method111`, `method123` | `method33` | C (sound/music volume) |
| `method127` | `method34` | C (project actor to screen) |
| `method132` | `method16` (commented) | B |
| `method138` | commented in `init` | C |
| `method147` | `method32` | C (open report-abuse) |

## Cross-class call graph

Non-`GameClientCore` methods invoked from chunk A:

- `SignLink.midifade`, `SignLink.midi`, `SignLink.midisave`, `SignLink.opensocket`, `SignLink.startthread`, `SignLink.reporterror`, `SignLink.wavereplay`, `SignLink.cache_dat`, `SignLink.mainapp`, `SignLink.storeid`, `SignLink.reporterror` (`method15/16/21/22/27/31/35/41`).
- `GameServerEndpoint.fromConfiguredAddress` (`getConfiguredServerEndpoint`).
- `Rasterizer3D.anIntArray1472`, `Rasterizer3D.method366/367/370/372`, `Rasterizer3D.aClass30_Sub2_Sub1_Sub2Array1474`, `Rasterizer3D.anIntArray1480` (`method18/22/24/33/37`). See [Rasterizer3D.md](Rasterizer3D.md).
- `Rasterizer2D.method332/333/336/337/339/341` — 2D clip/fill primitives, used throughout the UI methods.
- `MapRegion.method171/174/179/180/183/190` — terrain assembly invoked from `method22`. See [MapRegion.md](MapRegion.md).
- `MapRegion.anInt145` — minimum painted plane, sampled in `method22`.
- `SceneGraph.method274/275/281/285/295/303/309` — scene graph operations from `method22/24/25/26`. See [SceneGraph.md](SceneGraph.md).
- `CollisionMap.method210` and `aClass11Array1230[].anIntArrayArray294` (`method22/24`).
- `ObjectDefinition.aClass12_785/780.method224`, `ObjectDefinition.method572` (`method22/23/24`).
- `NpcDefinition.aClass12_95.method224`, `NpcDefinition.method161`, `NpcDefinition.anInt75/anInt88/aBoolean93` (`method23/26/34`).
- `ItemDefinition.aClass12_158/159.method224`, `ItemDefinition.method198`, `ItemDefinition.aBoolean176/aString170/anInt155/anInt157/aStringArray189` (`method23/25/29/33`).
- `Player.aClass12_1704.method224`, `Player.aString1703/anInt1706` (`method23/34`).
- `SpotAnimationDefinition.aClass12_415.method224` (`method23`).
- `Model.method461` — model-cache eviction during region rebuild (`method22`).
- `Widget.aClass9Array210` and many `Widget` fields (`anInt214/216/217/220/224/230/231/236/240/241/244/247/250/253/259/261/262/263/265/266/267/272`, `aString218/221/222`, `aStringArray225`, `anIntArray215/240/241/247/253/272`, `aBoolean235/242/249/259/266`) — read in `method18/20/29/32/36`.
- `VarpDefinition.aClass41Array701[i].anInt709` — varp metadata lookup in `method33`.
- `ObjectDefinition.anInt746` — minimap icon id (`method24`).
- `TextUtils.method584/587` — name hashing / display name decoding (`method18/41`).
- `Actor.method449`, `Actor.aString1506`, `Actor.anInt1507/1513/1531/1532/1533/1534/1535/1537/1540/1541/1550/1551/1552`, `Actor.anIntArray1514/1515/1516` — overhead drawer (`method26/34/38`).
- `Deque.last/previous/addLast` — ground-item linked-list (`method25`).
- `IndexedSprite` and `FontRenderer` (alias `aClass30_Sub2_Sub1_Sub2/Sub4`) — `.method348/361/381/383/384/385/386/387/388/389` calls scattered across `method18/30/34/40`.
- `BufferedConnection` / `RsImageProducer` (`aClass15_*.method237/238`).
- `aClass17_1000.method246()` — nanosecond / monotonic clock (`method16/30/33/37`).
- `aClass42_Sub1_1068.method555/558/559/560/562/566` — model/loc on-demand cache GC (`method22/33`).
- `aClass30_Sub2_Sub2_1192.method397/398/399/402/404/407` and `.anInt1406` — outgoing `PacketBuffer` (`method22/32/35/37/41`). Opcodes seen: `95` chat filter, `150/165/210/226/238` anti-tamper noise, `188` add-friend, `215` remove-friend.
- `aClass30_Sub2_Sub2_1083.method408()` — packet timer (`method20`).

---

Generated for chunk A; integration with chunks B–E will reconcile field types and resolve the deferred `method42/method69/method77/method105/method127` etc. signatures.
