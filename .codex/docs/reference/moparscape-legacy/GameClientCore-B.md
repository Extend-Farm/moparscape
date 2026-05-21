# GameClientCore Chunk B (lines 1924–3484) — legacy moparscape

Source: `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
Lines covered: **1924–3484** (28 methods, `method42` through `method67`, plus three non-numbered helpers introduced inside the range: `main`, `initializeStandaloneClientRuntime`, `clearEmbeddedHost`, `configureBootstrapLogin`, `snapshotLegacyLoginStatus`, `clampLoginInput`, `normalizeConfiguredServerAddress`, plus an override of cross-chunk `method7`, `method13`).

## Chunk overview

This slice spans three loosely-coupled responsibilities of the legacy 317 client:

1. **Per-tick frame driving** (`method7`, `method53`, `method57`, `method62`, `method63`) — the *upper half* of `GameClient.processGameTick()`. `method62` is the master per-tick "process input → update simulation → flush outbound" routine; it consumes mouse/keyboard via `GameTickIoProcessor`, updates camera drift via `CameraDriftUpdater`, dispatches item-drag-drop, advances projectiles, polls the on-demand fetcher (`method57`), and ends by calling the outbound packet flush. `method53`/`method54` drive the loading screen ("Loading - please wait.") while map blocks from the on-demand fetcher are being inflated into the scene via `MapRegion.method189`.
2. **Packet/scene update handlers** (`method46`, `method47`, `method49`, `method55`) — these are call-sites of the legacy NPC-add / player-add / projectile-update streams. `method46` decodes the "added NPCs" stream from a bit-packed `PacketBuffer`. `method47` enqueues player render passes for every tracked `Player` (or just the local player). `method49` applies per-player update masks (delegating to the modern `applyPlayerUpdateMasks` helper). `method55` walks `aClass19_1013` (projectiles) and submits each to the `SceneGraph`.
3. **Title screen / asset bootstrap and minimap chrome** (`method51`, `method52`, `method56`, `method58`, `method64`, `method67`, `main`, `initializeStandaloneClientRuntime`, plus the new login-config helpers `configureBootstrapLogin`, `snapshotLegacyLoginStatus`, `clampLoginInput`, `normalizeConfiguredServerAddress`) — these set up the title-screen sprite buffers (`aClass15_1107…aClass15_1115`), the minimap edge sprites (`aClass30_Sub2_Sub1_Sub1_1201/1202`), the runic loading-strip palettes (`anIntArray850/851/852/853`), and the "smoke + animated flame" loading effect (`method58`).

Other small members in the range: `method42` (4-corner-bilinear ground-height lookup), `method43` (number → "12K"/"3M" abbreviation), `method44` (disconnect/teardown), `method45` (rebuild avatar identity-kit defaults), `method48` (`Widget.anInt214` button-id dispatcher for the avatar-customisation/options interfaces), `method50` (minimap-pixel painter for walls + ground decoration), `method59` (`SignLink.wavesave` wrapper), `method60` (recursive widget reset), `method61` (XP-drop / hit-splat overlay), `method65` (scrollbar click/drag), `method66` (right-click "use" pathing to a clicked object).

## Static / instance fields touched (only fields first introduced in your range)

This chunk uses many fields already documented in chunks above. The new ones first read/written in lines 1924–3484:

| Legacy name | Type | First seen | Purpose (inferred) |
|---|---|---|---|
| `aClass30_Sub2_Sub1_Sub2_966` | `IndexedSprite` | `method51:2402` | Title screen "titlebox" sprite (text-field background). |
| `aClass30_Sub2_Sub1_Sub2_967` | `IndexedSprite` | `method51:2405` | Title screen "titlebutton" sprite (centre login/cancel button). |
| `aClass30_Sub2_Sub1_Sub2Array1152` | `IndexedSprite[12]` | `method51:2406` | The 12 rotating rune glyphs animated across the title screen ("runes" archive entry). When `fl_icon` applet param is non-zero, falls back to a 4-rune cycle (`runes` entry `12 + (l & 3)`). |
| `aClass30_Sub2_Sub1_Sub1_1201`, `_1202` | `Sprite(128, 265)` | `method51:2424-2425` | Title-screen left/right edge sprites (the "wood frame" sides), seeded with `aClass15_1110/1111.anIntArray315[0..33919]`. |
| `anIntArray851/852/853/850` | `int[256]` | `method51:2432-2471` | Per-channel runic-loading palette ramps (red, green, blue, generic) used by `method58` to colour the animated flames. Each is a piecewise ramp: `[0..63]` ramps the channel from 0 to full, `[64..127]` adds the next colour bit, `[128..191]` adds the third, `[192..255]` saturates to white. |
| `anIntArray1190`, `anIntArray1191` | `int[32768]` | `method51:2472-2473` | Backing buffers for the animated rune glow (`anIntArray1190` is the active source, `anIntArray1191` is the alternate scratch — both filled by `method106`). |
| `anIntArray828`, `anIntArray829` | `int[32768]` | `method51:2475-2476`, `method58` | Double-buffered 128×256 cellular-automaton heat field that simulates the title-screen "flame" animation. `828` is sampled, `829` is the per-iteration neighbourhood average. |
| `anInt1275` | `int` | `method58:2845` | Scroll offset (in pixels) into `anIntArray1190` for the rune glow animation. When it wraps past the buffer length, `method106` is invoked with a freshly chosen rune sprite from `aClass30_Sub2_Sub1_Sub2Array1152[0..11]`. |
| `anIntArray969` | `int[256]` | `method58:2866-2868` | Per-row vertical "wobble" displacement for the title-screen text; new entry per tick is `sin(t/14)*16 + sin(t/15)*14 + sin(t/16)*12`. Older rows shift up by one each tick. |
| `anInt1040`, `anInt1041` | `int` | `method58:2869-2880` | Counters for the two title-screen "spark" effects (left and right). When both are 0, a 1-in-2000 roll spawns one of them with value 1024 (which then decays by 4 per tick). |
| `loginAutoSubmitPending`, `loginUsername`, `loginPassword`, `configuredServerAddress`, `loginScreenState`, `loginInputField`, `loginStatusPrimary`, `loginStatusSecondary` | `String`/`int`/`boolean` | `configureBootstrapLogin:2543` | New non-obfuscated login-bootstrap fields used to pre-fill credentials when launched from the modern desktop runtime. Read back via `snapshotLegacyLoginStatus`. |
| `embeddedHostComponent` | `Component` | `initializeStandaloneClientRuntime:2531` | Optional AWT host surface for the embedded runtime; `clearEmbeddedHost()` wipes it when the host shuts down. |

(All other fields touched — `aBoolean1157`, `anIntArrayArrayArray1214`, `aClass30_Sub2_Sub4_Sub1_Sub1Array835`, etc — are introduced in chunk A and documented there.)

## Methods

### `public final int method42(int i, int j, boolean flag, int k)` — `GameClientCore.java:1924`

- **Purpose:** Bilinear ground-height lookup at world coordinate `(k, j)` on build plane `i`. Returns the interpolated tile-corner Y value (in client world units). The `flag` parameter is OR-folded into `aBoolean1157` (this is the "you've moved at least once" / "client is now in game" latch — also reset by `method44`).
- **Parameters:** `i` = plane, `j` = world Y (south-north tile coord, 0..13312), `flag` = movement-armed latch, `k` = world X.
- **Returns:** Interpolated height. Returns `0` for out-of-bounds (`l<0 || i1<0 || l>103 || i1>103`).
- **Mechanics:** Splits `(k,j)` into tile index `(l, i1) = (k>>7, j>>7)` and sub-tile fractions `(k1, l1) = (k&0x7f, j&0x7f)`. If `j1<3` and bit `2` of `aByteArrayArrayArray1258[1][l][i1]` is set ("bridge tile" — see `MapRegion.md`), bumps to the higher plane (so the player walking onto a bridge gets the bridge surface, not the under-bridge plane). Then bilinearly blends the four corner heights from `anIntArrayArrayArray1214[plane][x][y]`.
- **Called by:** `method47:2091`, `:2101`; `method55:2670`, `:2681`; plus many sites in later chunks (`:7344`, `:7924`, `:8269`, `:1736`, `:1775`, `:818`, `:841`). Essentially the only legitimate way to ask "what Y does this world (x,z) sit at?" for actors and projectiles.
- **Calls:** none.

### `private static final String method43(int i, int j)` — `GameClientCore.java:1941`

- **Purpose:** Format an integer count with `K`/`M` suffix (RuneScape-style, e.g. coin amount display). Below 100 000 → raw; 100 000–9 999 999 → `j/1000 + "K"`; above → `j/1 000 000 + "M"`.
- **Parameters:** `i` is a static-call guard — if `i != -33245`, scribbles `anInt846 = -65`. Standard obfuscation anti-tamper / call-site signature. `j` is the value.
- **Returns:** Display string.
- **Called by:** Not called within this chunk; called from later chunks formatting widget item-stack overlays.
- **Calls:** `String.valueOf`.

### `public final void method44(boolean flag)` — `GameClientCore.java:1953`

- **Purpose:** Disconnect / teardown of the current gameplay session. Closes the network stream (`aClass24_1168.method267`), clears `aClass24_1168`, and (if `flag`) returns the UI to the credentials login screen, resets chat tabs, force-runs GC, and resets the title-screen flame buffers via `method15`.
- **Parameters:** `flag` — if true, perform full UI reset (otherwise just close the socket).
- **Side effects:** Sets `aBoolean1157 = false`, `loginScreenState = LOGIN_SCREEN_CREDENTIALS`, resets the four chat history rings (`aClass11Array1230[0..3].method210`), clears `anInt956/1227/1259`.
- **Called by:** `method62:3151`, plus several disconnect paths in later chunks (`:3489`, `:3507`).
- **Calls:** `aClass24_1168.method267`, `method23`, `aClass25_946.method274`, `aClass11.method210`, `method15`.

### `public final void method45(int i)` — `GameClientCore.java:1982`

- **Purpose:** Default the player's identity-kit (`anIntArray1065[0..6]`) to the first available `IdentityKitDefinition` of each body slot for the current sex (`aBoolean1047` = male). Used both at character creation and after pressing the male/female toggle buttons (`j == 324/325` in `method48`).
- **Parameters:** `i` — anti-tamper guard (sets `anInt1008 = -1` when nonzero).
- **Side effects:** Sets `aBoolean1031 = true` (dirty avatar — re-render).
- **Called by:** `method48:2189` and `:2194` (sex toggle), `method3` (or similar character-creation entry) at `:6077`.
- **Calls:** none.

### `private final void method46(int i, PacketBuffer class30_sub2_sub2, byte byte0)` — `GameClientCore.java:2002`

- **Purpose:** Decode the *added NPCs* segment of the NPC update packet (opcode 65 region). Loops while `class30_sub2_sub2.anInt1407 + 21 < i*8`, each iteration reading a bit-packed NPC spawn: 14-bit server index, 5-bit signed dY, 5-bit signed dX, 1-bit run flag, 12-bit NPC type id, 1-bit "has update-mask" flag.
- **Parameters:** `i` = packet length in bytes (used as a bit-budget cap). `byte0` must be `2` (anti-tamper). `class30_sub2_sub2` = the bit-stream view of the NPC update packet.
- **Side effects:** For each new NPC: allocates a slot in `aClass30_Sub2_Sub4_Sub1_Sub1Array835`, copies definition metadata (`anInt1540`/`anInt1504`/`anInt1554/1555/1556/1557/1511`), appends index to `anIntArray837` (active list) and (if update-mask bit set) `anIntArray894` (pending-mask list). Calls `Npc.method445` to place it relative to the local player. Sentinel `k == 16383` terminates the loop.
- **Called by:** A packet dispatcher at `:1126` (chunk A) — `method46(i, class30_sub2_sub2, (byte)2)`.
- **Calls:** `PacketBuffer.method419` (read bits), `PacketBuffer.method420` (finish bit access), `NpcDefinition.method159` (cache lookup), `Npc.method445` (place).
- **Notes:** Identical layout to RuneScape 317 packet 65's "added NPCs" tail — see RuneTek-3 protocol notes.

### `public final void method7(int i)` — `GameClientCore.java:2041` *(also declared in chunk A; this is the gameplay-screen variant)*

- **Purpose:** One iteration of the title-screen tick (when `!aBoolean1157`) or the gameplay tick (otherwise). Guard at top short-circuits while a modal "wait for socket" boolean is true (`aBoolean1252`, `aBoolean926`, `aBoolean1176`).
- **Parameters:** `i` — anti-tamper (must equal `anInt1058`).
- **Calls:** `method140(true)` when on title screen, otherwise `method62(anInt1218)` (the main game tick); then `method57(false)` to drain on-demand fetch results. Increments `anInt1161` (server-tick counter).
- **Called by:** `GameClient.run` upper-level driver loop.

### `public final void method47(int i, boolean flag)` — `GameClientCore.java:2055`

- **Purpose:** Enqueue all players' visual updates into the `SceneGraph` for the next frame. If `flag`, only enqueues the local player (`aClass30_Sub2_Sub4_Sub1_Sub2_1126`); otherwise iterates all `anInt891` tracked players (indices in `anIntArray892`).
- **Parameters:** `i` — anti-tamper. `flag` — local-only mode.
- **Mechanics:** For each player, classifies whether it should "draw far" (`aBoolean1699 = true` when the world has >50 players in lowmem or >200 in highmem and the player isn't animating). Players with active `aClass30_Sub2_Sub4_Sub6_1714` (active spotanim / forced-chat overhead) take the high-detail submission path (`SceneGraph.method286`). Otherwise: if the player is exactly tile-centred (`& 0x7f == 64`), de-dupes via `anIntArrayArray929`/`anInt1265` (one player per tile per frame). Standard submission via `SceneGraph.method285`. Height via `method42`.
- **Side effects:** Resets `anInt1261/1262` to 0 if the local player is on the current camera-focus tile.
- **Called by:** Per-frame rendering pipeline in later chunks (around `:5847`, `:6979` and the main draw routine).
- **Calls:** `method42`, `SceneGraph.method285`, `SceneGraph.method286`, `Player.method449`.

### `public final boolean method48(int i, Widget class9)` — `GameClientCore.java:2107`

- **Purpose:** Dispatch table for the *non-clickable* `Widget` button IDs (`Widget.anInt214`) for the avatar-design and options interfaces. Returns `true` when the button was consumed and the caller should stop further processing.
- **Parameters:** `i` — anti-tamper (clears `anInt1008` if `<=0`). `class9` — the clicked child widget.
- **Major branches:**
  - `j == 201/202` (`anInt900==2`): open "add/delete friend" name prompt (sets `anInt1064 = 1` or `2`, focuses the chat input via `aBoolean1223/1256`).
  - `j == 501/502`: same, but for the ignore list (`anInt1064 = 4/5`).
  - `j == 205`: trigger logout dialog (`anInt1011 = 250`).
  - `j == 300..313`: cycle one of 7 identity-kit body slots forward/backward (skipping non-matching/hidden kits).
  - `j == 314..323`: cycle one of 5 colour slots (`anIntArray990`) forward/backward.
  - `j == 324/325`: switch sex (sets `aBoolean1047` and calls `method45(0)` to reset defaults).
  - `j == 326`: commit avatar design — sends opcode `101` followed by `aBoolean1047`, 7 body slot ints, 5 colour ints.
  - `j == 613`: toggle public-chat-on-quickchat (`aBoolean1158`).
  - `j == 601..612`: send quick-chat message — opcode `218`, the typed text (`aString881`, encoded by `TextUtils.method583`), the quick-chat option index `(j-601)`, and the channel flag.
- **Called by:** `method17` or similar mouse-dispatch at `:3599` (and probably other call-sites in later chunks).
- **Calls:** `method45`, `method147`, `PacketBuffer.method397/398/404`, `TextUtils.method583`.

### `private final void method49(int i, byte byte0, PacketBuffer class30_sub2_sub2)` — `GameClientCore.java:2224`

- **Purpose:** Apply the *update-mask* tail of the player update packet (opcode 81). For each of the `anInt893` players whose index appeared with the "needs-update" bit, reads the mask byte (possibly extended into a 16-bit mask via the `0x40` extension flag) and delegates to `applyPlayerUpdateMasks`.
- **Parameters:** `i` — anti-tamper. `byte0` — must be `2`. `class30_sub2_sub2` = continuation of the packet bit-stream (now byte-aligned).
- **Called by:** The packet 81 handler in chunk C/D (not within this range — `method49` is referenced by `applyPlayerUpdateMasks` cluster).
- **Calls:** `PacketBuffer.method408`, `applyPlayerUpdateMasks` (declared at `:7275`).
- **Notes:** Matches the canonical 317 player-update flags structure (`0x40` extends mask to 16 bits to make room for `0x100`/`0x400` flags etc).

### `public final void method50(int i, int j, int k, int l, int i1, int j1)` — `GameClientCore.java:2242`

- **Purpose:** Paint *one minimap tile* into `aClass30_Sub2_Sub1_Sub1_1263` (the minimap source bitmap). Draws three slots per tile: a wall (`SceneGraph.method300`), a wall-decoration / fence (`method302`), and a ground decoration (`method303`), at minimap pixel position `48 + l*4` × `48 + (104-i)*4` (4 minimap pixels per world tile).
- **Parameters:** `i` = tile Y (0..103), `l` = tile X (0..103), `j1` = plane, `k` = wall colour, `i1` = wall colour for "interactive" walls (`k1>0`), `j` is anti-tamper (must be `<0` to short-circuit — actually returns immediately if `j>=0`, so legitimate calls pass a sentinel like `-960`).
- **Mechanics:** Per slot, queries the `SceneGraph` cull bits to discover the locId (`>>14 & 0x7fff`) and the shape/rotation (`method304`). If the object has a custom `anInt758` minimap icon, blits the icon sprite. Otherwise, paints into the 4×4 cell of `aClass30_Sub2_Sub1_Sub1_1263.anIntArray1439` using `k4 = 24624 + l*4 + (103-i)*512*4` as the top-left index. The wall shape (`i3`) and rotation (`k2`) determine which of the 4 edge-rows / 4 edge-cols / single-corner / diagonals get filled.
- **Called by:** Minimap-redraw loop in chunk A (calls at `:729` and `:731` show the standard "draw wall, then draw bridge wall one plane up" pair).
- **Calls:** `SceneGraph.method300/302/303/304`, `ObjectDefinition.method572`, `IndexedSprite.method361`.
- **Notes:** `i3 == 0/2` is "full wall" (4-pixel edge), `i3 == 3` is corner cap, `i3 == 9` is diagonal fence (drawn as a `0xeeeeee` or `0xee0000` diagonal line).

### `public final void method51(int i)` — `GameClientCore.java:2400`

- **Purpose:** Title-screen / minimap *asset bootstrap*: load static sprites from the `title` archive, build the runic palette ramps, allocate the flame-animation buffers, and (when first called) kick off async loading of the rest of the cache archives.
- **Parameters:** `i` — anti-tamper (toggles `aBoolean1231` if `<=0`).
- **Side effects:**
  - Loads `titlebox`, `titlebutton`, and 12 `runes` sprites (or 4-cycle variant for `fl_icon != 0` Christmas/seasonal variant).
  - Allocates `aClass30_Sub2_Sub1_Sub1_1201/1202` left/right title sprites; seeds them from `aClass15_1110/1111.anIntArray315[0..33919]` (the rendered title-buffer pixels).
  - Builds 4 colour-ramp palettes (`anIntArray850..853`) of 256 entries each.
  - Allocates `anIntArray1190/1191` (32k each, the rune-glow buffers) and `anIntArray828/829` (32k each, the flame cellular automaton).
  - Calls `method106(null, -135)` to seed glow buffers with no source sprite (all zeros).
  - Calls `method13(10, (byte)4, "Connecting to fileserver")` to draw the load bar.
  - On first call (`!aBoolean831`), sets `aBoolean880 = true; aBoolean831 = true` and spawns the cache-fetch worker thread via `method12(this, 2)`.
- **Called by:** `method64:3215`, and `:6339` (initial bootstrap after title archive loads).
- **Calls:** `method106`, `method13`, `method12`, `IndexedSprite`/`Sprite` constructors.

### `public static final void method52(boolean flag)` — `GameClientCore.java:2486`

- **Purpose:** Switch the static client config to *highmem* mode (low-detail flags off). Sets `SceneGraph.aBoolean436=false`, `Rasterizer3D.aBoolean1461=false`, `aBoolean960=false`, `MapRegion.aBoolean151=false`, `ObjectDefinition.aBoolean752=false`. (The lowmem-counterpart `method138` is commented out at `:2527` — only highmem is used.)
- **Parameters:** `flag` — anti-tamper (toggles `aBoolean919` if true).
- **Called by:** `initializeStandaloneClientRuntime:2528`.

### `public static final void main(String args[])` — `GameClientCore.java:2497`

- **Purpose:** Standalone (non-applet) launcher. Refuses any command-line args, runs `initializeStandaloneClientRuntime`, then constructs a `GameClient` and starts it via `method1(503, false, 765)` (the 503 is the on-demand "frame rate" arg, 765 is the canvas height).
- **Notes:** Catches and silently swallows any exception (`return;`), so a startup failure leaves no diagnostic on stdout.

### `protected static void initializeStandaloneClientRuntime()` — `GameClientCore.java:2517`

Convenience overload that calls the 2-arg form with `GameServerEndpoint.DEFAULT_ADDRESS` and `null` host component. Throws `UnknownHostException`.

### `protected static void initializeStandaloneClientRuntime(String serverAddress, Component embeddedHostSurface)` — `GameClientCore.java:2522`

- **Purpose:** Static singleton init — prints the version banner (`"RS2 user GameClient - release #317"`), zeroes `anInt957/958`, calls `method52(false)` (highmem), sets `aBoolean959 = true` (members world), sets `embeddedHostComponent`, clears `SignLink.mainapp`, sets `SignLink.storeid = 32`, and starts `SignLink` with the resolved server host via `SignLink.startpriv(InetAddress.getByName(socketHost))`.
- **Parameters:** `serverAddress` — `host[:port]` for the gameserver, fed through `GameServerEndpoint.fromConfiguredAddress`. `embeddedHostSurface` — optional AWT Component when running inside the desktop runtime.
- **Notes:** Replaces the legacy `init()` applet path; the lowmem-variant (`method138`) is commented out (`:2527`).

### `static void clearEmbeddedHost()` — `GameClientCore.java:2538`

Nulls `embeddedHostComponent`. Called when the embedded host surface shuts down.

### `public final void configureBootstrapLogin(String username, String password, String serverAddress, boolean autoSubmit)` — `GameClientCore.java:2543`

- **Purpose:** Pre-populate the legacy login screen from the modern desktop runtime. Clamps `username` to 12 chars, `password` to 20 chars (via `clampLoginInput`), normalises the configured server, sets `loginScreenState = LOGIN_SCREEN_CREDENTIALS`, focuses the username field, and arms `loginAutoSubmitPending` only if both fields are non-empty.

### `final LegacyLoginStatusSnapshot snapshotLegacyLoginStatus()` — `GameClientCore.java:2555`

Read-only snapshot returning `(aBoolean1157, atCredentialsScreen, loginAutoSubmitPending, loginStatusPrimary, loginStatusSecondary)`. Used by the desktop runtime to detect "the embedded legacy client has reached the credentials screen and is ready to auto-submit".

### `private static String clampLoginInput(String value, int maximumLength)` — `GameClientCore.java:2566`

`trim()` then truncate. Returns `""` for null.

### `private static String normalizeConfiguredServerAddress(String serverAddress)` — `GameClientCore.java:2577`

Resolves via `GameServerEndpoint.fromConfiguredAddress` and returns canonical `host:port`.

### `public final void method53(int i)` — `GameClientCore.java:2583`

- **Purpose:** Loading-screen state machine driver, called once per tick from `method62:2959`. Three phases:
  1. If we're in highmem (`aBoolean960`), about to load a new region (`anInt1023 == 2`), and the region key changed (`MapRegion.anInt131 != anInt918`) — wipe `aClass15_1165`, draw the "Loading - please wait." text (drop-shadowed at +1,+1), copy to AWT surface, advance to phase 1, stamp `aLong824`.
  2. Phase 1: poll `method54` until it returns `0` (success). If still loading after ~6 minutes (`0x57e40` ms = 360 000 ms = 6 min), report via `SignLink.reporterror` with diagnostic state.
  3. Phase 2: if the world id changed (`anInt918 != anInt985`), call `method24(true, anInt918)` to fully reset chat/scene for the new region.
- **Parameters:** `i` — anti-tamper, must equal `-48877` or returns immediately.
- **Calls:** `method54`, `method24`, `aClass15_1165.method237/238`, `Sprite.method381`.

### `public final int method54(byte byte0)` — `GameClientCore.java:2612`

- **Purpose:** Poll whether all map tiles + loc files needed for the current scene have arrived. If yes, hand the buffers to `MapRegion.method189` (which builds the static scene). Returns `0` on success; negative diagnostic codes otherwise.
- **Parameters:** `byte0` must equal `-95`. Returns `0` immediately if not.
- **Return codes:** `-1` = at least one terrain (`aByteArrayArray1183`) is missing while still expected, `-2` = at least one loc (`aByteArrayArray1247`) is missing, `-3` = `MapRegion.method189` reported "not all blocks decoded" for at least one region, `-4` = `aBoolean1080` is set (a "kick to lobby" sentinel was asserted), `0` = success — flips `anInt1023 → 2`, sets `MapRegion.anInt131 = anInt918`, calls `method22(true)` (full scene re-init), and sends opcode `121` ("client ready").
- **Called by:** `method53:2598`.
- **Calls:** `MapRegion.method189`, `method22`, `PacketBuffer.method397`.

### `public final void method55(int i)` — `GameClientCore.java:2656`

- **Purpose:** Per-tick projectile pump. Walks `aClass19_1013` backwards; for each `Projectile`:
  - Unlinks it if it's on a different world (`anInt1597 != anInt918`) or expired (`anInt1161 > anInt1572`).
  - If it has been launched (`anInt1161 >= anInt1571`), updates its end-point each tick to track its target (NPC if `anInt1590 > 0`, player if `<0`, ground if `0`), advances physics via `method456`, and submits it to the scene via `SceneGraph.method285`.
- **Parameters:** `i` — anti-tamper. The construct `while(i >= 0) method6();` is a deliberate infinite-loop bait that callers never trigger (always pass `-1`).
- **Called by:** Per-tick logic in chunk C (around the same place as `method108`).
- **Calls:** `Projectile.method455/456`, `method42`, `SceneGraph.method285`.

### `public final AppletContext getAppletContext()` — `GameClientCore.java:2689`

Override — if `SignLink.mainapp` is set, delegate to the embedded applet's context; otherwise the superclass. Used by `link to forum` etc.

### `public final void method56(int i)` — `GameClientCore.java:2697`

- **Purpose:** Render the static title-screen background. Loads `title.dat` from `aClass44_1053` into a Sprite, then for each of the 9 title sub-buffers (`aClass15_1107..1115`) clears it and blits a region of `title.dat` at the appropriate (negative) offset. Then horizontally mirrors the sprite (the in-place reverse loop at `:2719-2728`) and blits the mirrored copy with positive offsets — giving the symmetric left/right title chrome. Finally blits the `logo` sprite onto `aClass15_1107` (the top-centre).
- **Parameters:** `i` — anti-tamper. If non-zero, returns early *after* having done the mirror blits but before logo+`System.gc`.
- **Called by:** `method64:3214`, and `:6339` (post-archive bootstrap).
- **Calls:** `Sprite.method346/348`, `ProducingGraphicsBuffer.method237`.

### `public final void method57(boolean flag)` — `GameClientCore.java:2764`

- **Purpose:** Drain completed on-demand cache requests from the worker (`aClass42_Sub1_1068`) and apply each:
  - `anInt1419 == 0`: a Model — register via `Model.method460`. If its priority flags (`& 0x62`) indicate "interface model", set dirty flags.
  - `anInt1419 == 1`: an `AnimationFrame` — register via `AnimationFrame.method529`.
  - `anInt1419 == 2` *and* it's the currently-awaited song: install via `method21(aBoolean1228, 0, …)`.
  - `anInt1419 == 3` *and* we're in load-screen phase 1: store the map/loc buffer in `aByteArrayArray1183`/`aByteArrayArray1247` at the matching index.
  - Terminates when an item with `anInt1419 != 93` *or* `aClass42_Sub1_1068.method564 == false` comes through; for that terminator, calls `MapRegion.method173((byte)-107, new PacketBuffer(…), aClass42_Sub1_1068)`. (This is the on-demand reply-list-update handler.)
- **Parameters:** `flag` — anti-tamper (sets `anInt883 = -72`).
- **Called by:** `method7:2050`, plus the loading-loop at `:6385`.
- **Calls:** `aClass42_Sub1_1068.method561/559/564`, `Model.method460`, `AnimationFrame.method529`, `method21`, `MapRegion.method173`.

### `public final void method58(int i)` — `GameClientCore.java:2815`

- **Purpose:** One tick of the animated "title-screen flame/rune-glow" effect. Three passes:
  1. Inject random heat into `anIntArray828` at the bottom row (`y == c-2 == 254`): per column, 50% chance of value `255`; plus 100 random "sparks" at value `192` in the lower half.
  2. Smooth `anIntArray828 → anIntArray829` via a 4-neighbour box blur.
  3. Roll `anInt1275` forward by 128 and (if wrapped) pick a random rune from `aClass30_Sub2_Sub1_Sub2Array1152[0..11]` and re-seed `anIntArray1190` via `method106`. Then write back into `anIntArray828` from `anIntArray829 + 128` (so the smoke rises) minus a fifth of the glow value at the rotated offset.
  4. Shift `anIntArray969` up one row and append a fresh wobble value `sin(t/14)*16 + sin(t/15)*14 + sin(t/16)*12`.
  5. Decay `anInt1040/1041` (the left/right "spark" effects), and if both are 0, roll 1-in-2000 to spawn a new spark in one of the two slots.
- **Parameters:** `i` — anti-tamper (must equal `25106`).
- **Called by:** `method2`/equivalent at `:8692` (the title-screen frame loop in chunk D).
- **Calls:** `method106`.

### `public final boolean method59(byte abyte0[], byte byte0, int i)` — `GameClientCore.java:2883`

Trivial wrapper around `SignLink.wavesave(abyte0, i)`. Returns `true` if `abyte0 == null`. `byte0` must equal `116` (anti-tamper).

### `public final void method60(int i, byte byte0)` — `GameClientCore.java:2893`

- **Purpose:** Recursively reset the per-child draw-state of a widget tree rooted at `Widget.aClass9Array210[i]`. For each child, recurses if `anInt262 == 1` (inline interface), then zeroes its scroll positions (`anInt246`, `anInt208`).
- **Parameters:** `i` = root widget id. `byte0` must equal `6`.
- **Called by:** Widget-open paths in later chunks.

### `public final void method61(int i)` — `GameClientCore.java:2911`

- **Purpose:** Draw the *XP-counter / hit-splat overlay* anchored to a 3D world position. When `anInt855 == 2`, calls `method128` to project a 3D point through the camera. If `anInt963 > -1` (a hit-splat is queued), draws the splat sprite (`aClass30_Sub2_Sub1_Sub1Array1095[2]`) blinking at 10-frame intervals.
- **Parameters:** `i` — anti-tamper (toggles `aBoolean1224` if `>=0`).
- **Called by:** Per-frame draw routine in chunk C.
- **Calls:** `method128`, `Sprite.method348`.

### `public final void method62(int i)` — `GameClientCore.java:2922`

- **Purpose:** *The master per-tick game-logic routine.* Drives input, simulation, and outbound network. Phases:
  1. `GameTickProcessor.processInboundPackets(this)` — if returns false, early-out (no inbound packets yet this tick).
  2. Drain the mouse recorder via `GameTickIoProcessor.processMouseRecorderBatch` and update `anInt1237/1238/1022`.
  3. Synthesise the mouse-click packet via `GameTickIoProcessor.encodeMouseClickPacket` (anti-AFK telemetry sent to the server every click).
  4. Keyboard-activity anti-AFK: if any arrow keys are pressed for 20 ticks, sends opcode `86` carrying `anInt1184` (camera yaw) + `anInt1185` (camera pitch).
  5. Window focus changes → send opcode `3` with 1/0.
  6. Loading screen tick (`method53`), game-window tick (`method115`), npc tick (`method90`), idle-logout countdown (`anInt1009 > 750 → method68`).
  7. World prep (`method114`), animation updates (`method95`), additional update step (`method38`), increment `anInt945` (animation cycle).
  8. Inventory-drag-drop resolution: `selectedDragState`, `dragState` → if the mouse moved ≥5 px, find the new slot via `method82`; if the new slot is in the same widget, perform the slot swap and send opcode `214` (`widgetId`, `mode`, `fromSlot`, `toSlot`).
  9. `SceneGraph.anInt470` right-click pickup path → `method85` to walk-and-act on a world object.
  10. Cancel hover dialog if `aString844 != null` and clicked.
  11. Per-tick "phase 4" calls: `method20`, `method92`, `method78`, `method32`.
  12. Idle timer: at 4500 ticks, send opcode `202` (logout-warning).
  13. Camera drift via `CameraDriftUpdater.update` — produces a `DriftState` that's written back to all 12 drift fields (primary X/Y/shake, secondary pitch/yaw).
  14. Heartbeat: every 50 ticks, send opcode `0` (keep-alive).
  15. `GameTickIoProcessor.flushOutbound(aClass24_1168, aClass30_Sub2_Sub2_1192)` — final outbound write. Status `-1` → idle-disconnect (`method68`); `-2` → hard disconnect (`method44(true)`); `1` → success (reset heartbeat counter).
- **Parameters:** `i` — anti-tamper.
- **Called by:** `method7:2049`.

### `private final void method63(int i)` — `GameClientCore.java:3161`

- **Purpose:** Process the deferred `aClass19_1179` queue of `SceneObjectSpawnRequest`s. For each, if `anInt1294 == -1` (final/no-revert), zero its `anInt1302` and call `method89(false, req)` to apply it permanently; otherwise unlink it.
- **Parameters:** `i` — anti-tamper (`while(i >= 0) for(;;);` is a never-taken trap).
- **Called by:** `:635`.
- **Calls:** `method89`.

### `public final void method64(int i)` — `GameClientCore.java:3180`

- **Purpose:** Lazy-allocate the title-screen `ProducingGraphicsBuffer` set (`aClass15_1107..1115` plus `_1110`, `_1111`) at fixed pixel sizes. Each buffer is followed by a `Rasterizer2D.method334(aBoolean1206)` to write its dimensions into the rasterizer's clip state. After all buffers exist, if `aClass44_1053` (title archive) is loaded, calls `method56` + `method51` to populate them.
- **Parameters:** `i` — anti-tamper (`< 0 || > 0` nulls `aClass19ArrayArrayArray827`).
- **Side effects:** Nulls all gameplay-screen buffers (`super.aClass15_13`, `aClass15_1166`, etc).
- **Called by:** `method13:3224`, `:8597`.
- **Calls:** `Rasterizer2D.method334`, `method56`, `method51`.
- **Notes:** Sizes: title sides `128x265`, title centre `509x171`, login subform `360x132/200`, top side panels `202x238` and `203x238`, top corner squares `74x94` and `75x94`.

### `public final void method13(int i, byte byte0, String s)` — `GameClientCore.java:3220` *(override of chunk-A method)*

- **Purpose:** Draw the "Loading… [progress bar] [status]" screen for legacy bootstrap. Stores `anInt1079 = i`, `aString1049 = s`. Lazy-inits the title buffers via `method64`. Either delegates to `super.method13` (if no title archive yet) or draws the styled bar: white header "MoparScape is loading - Hold onto your butts...", a red `0x8c1111` bar 304×34 px at the centre, filled to `i*3` pixels (so `i` is on a 0..100 scale meaning percent). Below the bar prints `s` (status line). Finally, on first call after `method64` allocated buffers, blits all title sides/corners to the AWT surface.
- **Parameters:** `i` = progress 0..100. `byte0` must be `4`. `s` = status text.
- **Called by:** `method51:2477`, `method67:3370/3400/3459/3463` (the archive-fetch retry loop), and many places.

### `public final void method65(int i, int j, int k, int l, Widget class9, int i1, boolean flag, int j1, int k1)` — `GameClientCore.java:3263`

- **Purpose:** Process scrollbar interaction for a given widget. Three regions of the scrollbar at column `i` (16-pixel wide):
  - Top arrow `[i..i+16] × [i1..i1+16]` → scroll up by `anInt1213 * 4`.
  - Bottom arrow `[i..i+16] × [(i1+j)-16..i1+j]` → scroll down by `anInt1213 * 4`.
  - Thumb track `[i-anInt992..i+16+anInt992] × [i1+16..i1+j-16]` → jump-scroll to position `((j1-j) * (l - i1 - 16 - l1/2)) / (j - 32 - l1)`, where `l1 = max(8, ((j-32)*j)/j1)` is the thumb height. Sets `aBoolean972 = true` so subsequent calls keep `anInt992 = 32` (widened thumb hitbox while dragging).
- **Parameters:** `i` = scrollbar X, `j` = scrollbar visible height, `(k,l)` = mouse pos, `i1` = scrollbar Y, `class9` = widget whose `anInt224` is the scroll position, `flag` = "mark UI dirty on success", `j1` = total content height, `k1` = per-call increment for `anInt1007` (a generic interaction tally).
- **Called by:** `:900` (chunk A), `:6979` (chunk D — the chatbox scrollbar).

### `public final boolean method66(int i, int j, int k, int l)` — `GameClientCore.java:3304`

- **Purpose:** Walk-and-use a clicked scene object. Decodes the locId (`i >> 14 & 0x7fff`) and uses `SceneGraph.method304` to get the shape+rotation; for "wall" shapes (`10/11/22`) constructs a (width,height,wallBits) triple from `ObjectDefinition`, rotated by the tile orientation, and calls `method85(2, …)` to enqueue a click-to-walk-then-interact path. For other shapes calls `method85` with the shape+1 as the interaction kind. On success, primes the click-effect (`anInt914/915/916/917 = …`).
- **Parameters:** `i` = packed object cull bits (locId in high half), `(j,k)` = tile coords, `l` = anti-tamper (must be `<0`).
- **Called by:** `:3559`, `:3668`, `:3916`, `:3979`, `:4120`, `:4128`, `:4136` (chunk C right-click dispatchers).
- **Calls:** `ObjectDefinition.method572`, `SceneGraph.method304`, `method85`.

### `public final Archive method67(int i, String s, String s1, int j, byte byte0, int k)` — `GameClientCore.java:3343`

- **Purpose:** Download (or cache-load) a JAG archive by index. First tries the local file cache (`aClass14Array970[0].method233`). If that yields nothing, repeatedly attempts a 6-byte-header HTTP download from `s1 + j`, expanding to `headerLength + 6` bytes, with a progress callback every 100% step into `method13`. On success, stores back into the cache via `method234`. On failure (`IOException`, `NullPointerException`, `OOB`, generic), sleeps with an exponential backoff (max 60s) and updates `method13("<reason> - Retrying in N")` each retry. Returns the parsed `Archive` (constructed via `new Archive(44820, abyte0)`).
- **Parameters:** `i` = archive index in the cache, `s` = friendly name (e.g. `"title screen"`), `s1` = base URL filename (e.g. `"title"`), `j` = expected version hash, `byte0` = anti-tamper (must equal `-41`), `k` = progress-bar offset for `method13`.
- **Called by:** `:6334` (title), `:6346` (config), `:6347` (interface), `:6348` (media), `:6349` (textures), `:6350` (wordenc), `:6351` (sounds), `:6358` (versionlist).
- **Notes:** The CRC32 validation is commented out (matches the deobfuscated source as-is); the only consistency check is the 6-byte length-header.

## Cross-chunk dependencies (methods in other chunks)

This chunk calls back into methods declared in chunks A / C / D / E:

| Cross-chunk method | Declaring line | First call from chunk B | Purpose |
|---|---|---|---|
| `method6` | `:6233` | `method55:2659` | Single iteration of the game loop (probably window-pump sleep). |
| `method11` | `:4782` | `method64:3192` | Allocate an AWT `Component` for `ProducingGraphicsBuffer`. |
| `method12` | `:297` | `method51:2482` | Spawn worker thread (`SignLink.startthread`). |
| `method13` (super) | `:3220` (overridden here) | `method51:2477` | Draw progress UI. Self-recursive via this override. |
| `method15` | `:34` | `method44:1976` | Reset title flame buffers. |
| `method17` | `:118` | `method62:3046` | Hit-test mouse against widget rectangles. |
| `method20` | `:344` | `method62:3075` | Per-frame phase. |
| `method21` | `:460` | `method57:2789` | Install a freshly-streamed song. |
| `method22` | `:468` | `method54:2650` | Full scene reset. |
| `method23` | `:687` | `method44:1970` | Reset chat sub-tab state. |
| `method24` | `:700` | `method53:2608` | World/region transition reset. |
| `method32` | `:1152` | `method62:3078` | Per-frame phase. |
| `method38` | `:1698` | `method62:2967` | Per-frame phase. |
| `method39` | `:1732` | `method62:3084` | Per-frame phase. |
| `method68` | `:3485` | `method62:2964`,`:3146` | Idle-logout / disconnect. |
| `method69` | `:3519` | `method62:3050` | Process a queued click. |
| `method78` | `:5687` | `method62:3077` | Per-frame phase. |
| `method82` | `:5847` | `method62:3002` | Hit-test the inventory drag target. |
| `method85` | `:6165` | `method62:3059`, `method66:3331/3334` | Click-to-walk path solver. |
| `method89` | `:6203` | `method63:3172` | Apply a deferred scene-object spawn. |
| `method90` | `:6215` | `method62:2961` | NPC per-frame phase. |
| `method92` | `:6454` | `method62:3076` | Per-frame phase. |
| `method95` | `:6607` | `method62:2966` | Animation update phase. |
| `method106` | `:7214` | `method51:2474`, `method58:2850` | Seed rune-glow buffer from a sprite. |
| `method108` | `:7305` | `method62:3082` | Per-frame phase (in-game only). |
| `method114` | `:7583` | `method62:2965` | Per-frame phase. |
| `method115` | `:7601` | `method62:2960` | Per-frame phase. |
| `method116` | `:7640` | `method62:3047` | Process default click. |
| `method128` | `:8261` | `method61:2915` | 3D→2D projection. |
| `method132` | `:8407` | `method67:3375` | `DataInputStream` for HTTP archive fetch. |
| `method140` | `:9112` | `method7:2047` | Title-screen tick. |
| `method147` | `:10206` | `method48:2212` | Send chat command. |
| `applyPlayerUpdateMasks` | `:7275` | `method49:2237` | Apply player-update mask payload. |

## Cross-class call graph

- **`PacketBuffer`** (`method397/398/399/404/408/412/419/420/424/431/432/433`) — opcode write, payload write, bit-stream read.
- **`SceneGraph`** (`method274` chat-tab, `method281` add to scene, `method285` actor add, `method286` actor add with spotanim, `method300` wall query, `method302` wall-decoration query, `method303` ground-decoration query, `method304` shape+rotation query) — reads field `anInt470/471` (deferred-click world coords).
- **`MapRegion`** (`method173` on-demand reply list update, `method189` map-block ingestion) — reads/writes field `anInt131` (last region key).
- **`Rasterizer2D`** (`method334/336/337`) — clip-rect set, rectangle fill, frame outline.
- **`Rasterizer3D`** — reads `aBoolean1461` (low-detail flag).
- **`ObjectDefinition`** (`method572`) — locId lookup; reads `anInt744/761/768/758`.
- **`NpcDefinition`** (`method159`) — npc-def lookup.
- **`IdentityKitDefinition`** — reads `anInt655` (count), `aClass38Array656[i].aBoolean662/anInt657`.
- **`Model`** (`method460`), **`AnimationFrame`** (`method529`) — register an inbound asset.
- **`Sprite` / `IndexedSprite`** (`method346/348/361/381`) — blits.
- **`Widget`** — reads `anInt214` (id), `anInt262` (interface type), `anIntArray240` (children), `aBoolean235` (allow-swap), `anIntArray252/253` (slot fill/item id), `method204` (swap slots).
- **`Player` / `Npc` / `Projectile` / `Actor`** (`method445/449/455/456`) — spawn / cull / fly / advance.
- **`SignLink`** (`startpriv`, `wavesave`, `reporterror`, `storeid`) — JVM-singleton plumbing.
- **`GameTickProcessor`** / **`GameTickIoProcessor`** / **`CameraDriftUpdater`** / **`GameServerEndpoint`** — modern (re-extracted) helpers replacing previously-inline obfuscated routines.
- **`TextUtils.method583`** — pack a quick-chat string into 5-bit RuneScape encoding.
- **`OnDemandRequest`** (`anInt1419` type tag, `anInt1421` id, `aByteArray1420` payload) — DMA result type from the on-demand fetcher.
- **`ProducingGraphicsBuffer`** (`method237` clear, `method238` blit-to-AWT) — title sub-buffers.
- **`Archive`** — constructed via `new Archive(44820, byteData)`; `44820` is the canonical "I know I'm parsing a JAG archive" magic constant.

## Unclear methods / notes

- **`method59` purpose:** Likely "save a downloaded sound effect to local cache via `SignLink.wavesave`" — only call sites are in later chunks downloading the `sounds` archive; behaviour is conjectural since `SignLink.wavesave` is itself a thin wrapper.
- **`method63` triggering condition:** Only called once (`:635`), in what appears to be a "region transitioned, apply pending object spawns" pathway. Cross-reference with chunk A's call-site for full context.
- **`method58` magic constant `'Ā'` (256):** Width of the flame buffer is 128 but the loops use 256 for the *height*. This is the legacy fixed title resolution (128×256). The buffer length is 32768 = 128×256.
- **`method67` retry strategy:** The "Game updated - please reload page" branch (`j1 >= 3`) is dead in the current code because `j1` is only incremented inside the commented-out CRC validation. As-is the loop will retry forever (with backoff capped at 60s) on persistent errors.
- **`method46` anti-tamper structure** (`for(int j = 1; j > 0; j++);` when `byte0 != 2`) — infinite loop on tamper; same pattern appears in `method47`, `method55`, etc. Documented for reference but not exercised in practice.
- **`method7` overrides** the chunk-A `method7(int)` — both signatures take a single `int` and live in the same class, suggesting one is actually `method7(byte)` or similar; the chunk-A copy should be re-checked for parameter type.
