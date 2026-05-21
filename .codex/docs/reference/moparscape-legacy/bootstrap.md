# Bootstrap pipeline (legacy moparscape)

The legacy `GameClientCore.startup()` body was extracted into a small set of
package-private `Bootstrap*` helpers under
`server/moparscape/src/main/java/io/github/ffakira/moparscape/client/`. Each
helper owns one phase of startup. They are all `final` classes with a private
constructor (pure static facades) and they mutate either supplied state objects
or the global static lookup tables in `Rasterizer3D`, `SequenceDefinition`,
`ObjectDefinition`, etc.

Progress feedback is reported through `core.method13(percent, (byte)4, status)`
— the legacy "Loading…" bar drawer documented in
[GameClientCore-B.md:302](GameClientCore-B.md). `core.method57(false)` is the
on-demand fetcher pump (also in chunk B). Together they let the bootstrap
helpers spin a status line + bar while waiting on JAG / OnDemand traffic.

## Pipeline overview

Invocation order inside the `try` block at
[GameClientCore.java:6249–6322](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L6249):

| # | Phase | Helper / inner call |
|---|---|---|
| 1 | Title-screen fonts + title archive | `bootstrapLoadTitleResources` (`:6331`) |
| 2 | Pull JAG archives + allocate SceneGraph + collision maps | `bootstrapLoadArchivesAndScene` (`:6343`) |
| 3 | Connect to update/on-demand server | `bootstrapConnectUpdateServer` (`:6362`) |
| 4 | Prime music + animation downloads | `bootstrapPrimeOnDemand` (`:6371`) → `BootstrapDemandLoader.requestAnimations` |
| 5 | Queue models / startup maps / priority / songs | `bootstrapLoadDemandQueues` (`:6398`) → `BootstrapDemandLoader.{requestModels,requestStartupMaps,queuePriorityModelRequests,queueMemberSongs}` |
| 6 | `method13(80, "Unpacking media")` then unpack 2-D chrome sprites | `BootstrapMediaLoader.loadCoreMedia` (`:6261`) |
| 7 | Minimap scene/function icons, hitmark and head-icon sprites | `BootstrapMediaLoader.{loadMapSceneSprites,loadMapFunctionSprites,loadHitmarkSprites,loadHeadIconSprites}` (`:6271-6274`) |
| 8 | Frame layout (mapmarker, scrollbar, redstone trims, mod icons) | `BootstrapMediaLayoutLoader.load` (`:6275`) |
| 9 | Allocate the nine title-screen back-buffer sprites + AWT graphics | `BootstrapBackBufferLoader.createBackBuffers` (`:6298`) |
| 10 | Randomise mapscene/mapfunction tint (per-session) | `BootstrapDemandLoader.applyRandomMapSpriteTint` (`:6308`) |
| 11 | Textures + config (`SequenceDefinition`, `ObjectDefinition`, `FloorDefinition`, `ItemDefinition`, `NpcDefinition`, `IdentityKitDefinition`, `SpotAnimationDefinition`, `VarpDefinition`, `VarBitDefinition`) | `BootstrapConfigLoader.loadTexturesAndConfig` (`:6309`) |
| 12 | Sound effect table (optional) | `BootstrapConfigLoader.loadSoundsIfNeeded` (`:6310`) |
| 13 | Widget unpack | `BootstrapConfigLoader.loadInterfaces` (`:6314`) |
| 14 | `method13(100, "Preparing game engine")` then derive minimap mask scanlines from `mapBack` alpha | `BootstrapGraphicsSetup.prepareMapbackClipMasks` (`:6316`) |
| 15 | Rasterizer3D scanline tables + scene-graph far-clip table | `BootstrapGraphicsSetup.initializeRasterizerAndSceneVisibility` (`:6317`) |
| 16 | Word-encoder + mouse recorder thread + final back-references | `BootstrapEngineFinalizer.finalizeStartup` (`:6321`) |

`bootstrapLoadTitleResources`, `bootstrapLoadArchivesAndScene`,
`bootstrapConnectUpdateServer`, `bootstrapPrimeOnDemand` and
`bootstrapLoadDemandQueues` remain inner methods of `GameClientCore` itself —
they were not extracted because they touch too many private fields. The seven
`Bootstrap*` classes below cover the work that *was* extracted.

## BootstrapBackBufferLoader.java

Allocates the **nine title-screen back-buffer sprites** that the legacy title
animation blits its smoke/flame effects through.

- [`createBackBuffers(Archive mediaArchive, Component component)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapBackBufferLoader.java#L11)
  — for each of the nine slots in
  `{backleft1, backleft2, backright1, backright2, backtop1, backvmid1,
  backvmid2, backvmid3, backhmid2}`, loads the corresponding `Sprite` from the
  media archive, wraps a `ProducingGraphicsBuffer` of the same dimensions
  around the AWT `Component`, then calls `sprite.method346(0, 0, -32357)` to
  draw the sprite into the buffer at `(0,0)` with the legacy magic transparency
  colour. Returns the populated 9-element array; `GameClientCore` immediately
  unpacks the result into `aClass15_903 … aClass15_911`
  ([`GameClientCore.java:6299-6307`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L6299)).

**1 method.**

## BootstrapConfigLoader.java

Unpacks the texture/config/sounds/interface payloads from the JAG archives into
the static definition tables.

- [`loadTexturesAndConfig(core, texturesArchive, configArchive, textureBrightnessByte, memberState)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L11)
  — calls `core.method13(83, "Unpacking textures")`, then
  `Rasterizer3D.method368(texturesArchive, 0)` (texture upload),
  `Rasterizer3D.method372(0.8, brightness)` (gamma), `Rasterizer3D.method367(20, true)`
  (anisotropic flag). Then `method13(86, "Unpacking config")` and calls
  `method257/576/260/193/162/535/264/546/533` on `SequenceDefinition`,
  `ObjectDefinition`, `FloorDefinition`, `ItemDefinition`, `NpcDefinition`,
  `IdentityKitDefinition`, `SpotAnimationDefinition`, `VarpDefinition`,
  `VarBitDefinition`. Final line sets `ItemDefinition.aBoolean182 = memberState`
  (the world members flag).
- [`loadSoundsIfNeeded(core, skipSounds, soundsArchive)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L30)
  — no-op when `skipSounds`; otherwise `method13(90, "Unpacking sounds")`,
  reads `sounds.dat` (length 891 is the legacy capped buffer size) and feeds it
  into `SoundEffect.method240(0, packetBuffer)`.
- [`loadInterfaces(core, interfaceArchive, mediaArchive, fonts)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L40)
  — `method13(95, "Unpacking interfaces")` then a single call to
  `Widget.method205(interfaceArchive, fonts, (byte)-84, mediaArchive)` which
  populates the global `Widget.aClass38Array195` table.

**3 methods.**

## BootstrapDemandLoader.java

Drives the on-demand fetcher (animations, models, startup map blocks, music
priorities) and the per-session minimap tint randomiser.

- [`requestAnimations(core, onDemandFetcher)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L10)
  — `method13(65, "Requesting animations")`. Queries the count via
  `method555(79, 1)` (table 1 = animations), requests each one
  (`method558(1, index)`), then polls `method552()` (queue depth) reporting
  `Loading animations - NN%` until empty. Returns `false` if the fetcher's
  error counter `anInt1349 > 3` (signal to abort startup as `ondemand` failure).
- [`requestModels(core, onDemandFetcher)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L29)
  — `method13(70, "Requesting models")`. For each model id, asks
  `method559(modelId, -203)` for the flag bitmask; if bit 0 is set the model is
  eagerly requested (`method558(0, modelId)`). Polls to completion the same way
  as animations.
- [`requestStartupMaps(core, onDemandFetcher)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L51)
  — `method13(75, "Requesting maps")`. Calls `method562(layer, _, regionX, regionY)`
  to resolve archive ids for the six map regions around Lumbridge (48,47 / 48,48 /
  48,49 / 47,47 / 47,48 / 148,48) in both layer-0 (terrain) and layer-1 (locs)
  forms, queueing each via `method558(3, …)`. Polls to drain.
- [`queuePriorityModelRequests(onDemandFetcher)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L77)
  — second-pass over the model table. The flag mask (returned by `method559`)
  is mapped to a priority byte using an if/else ladder:
  `0x08 → 10`, `0x20 → 9`, `0x10 → 8`, `0x40 → 7`, `0x80 → 6`, `0x02 → 5`,
  `0x04 → 4`, then bit 0 forces `3` (note the trailing `if` is unconditional —
  bit 0 wins over the else-chain). Non-zero priorities get queued via
  `method563(priority, 0, modelId, (byte)8)`.
- [`queueMemberSongs(onDemandFetcher)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L111)
  — iterates table 2 (music) from id 1; whenever `method569(songId, 5)`
  reports the song as member-only, queues it at priority 1.
- [`applyRandomMapSpriteTint(mapFunctionSprites, mapSceneSprites)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L119)
  — picks three colour deltas in `[-10, +10]` plus a brightness delta in
  `[-20, +20]`, then walks indices 0..99 applying
  `Sprite.method344(dR+dB, dG+dB, dBl+dB, 0)` to each mapfunction sprite and
  `IndexedSprite.method360(...)` to each mapscene sprite. This is what causes
  each session's minimap icons to have a slightly different tint.
- [`sleepBriefly()`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapDemandLoader.java#L134)
  — private helper, `Thread.sleep(100L)` swallowed-exception.

**7 methods (6 public + 1 private).**

## BootstrapEngineFinalizer.java

The "wire up the engine and start ancillary threads" tail of startup.

- [`finalizeStartup(core, wordEncArchive, gameClient, mouseRecorderRate)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapEngineFinalizer.java#L10)
  — loads the chat censor tables (`ChatCensor.method487(wordEncArchive)`),
  constructs a `MouseRecorder` and hands it to `core.method12(mouseRecorder, 10)`
  (the legacy `GameShell.method12` thread-spawn override documented in
  [GameClientCore-A.md:134](GameClientCore-A.md)), then back-fills the
  `aClient*` static references on `DynamicObject`, `ObjectDefinition`,
  `NpcDefinition` so those definition classes can call back into the running
  client. Returns the `MouseRecorder` which `GameClientCore` stores as
  `aClass48_879`.

**1 method.**

## BootstrapGraphicsSetup.java

Derives two unrelated pieces of derived state from the freshly loaded media:
(a) the per-row clip masks for the rounded minimap window, and (b) the
Rasterizer3D / SceneGraph scanline + visibility tables.

- Inner `State` class — bundles `viewportScanlineOffsets`,
  `chatboxScanlineOffsets`, `fullScreenScanlineOffsets` (the three flavours of
  `Rasterizer3D.anIntArray1472` that the renderer swaps between depending on
  which sub-region is being drawn).
- [`prepareMapbackClipMasks(mapBack, topClipStarts, topClipWidths, sideClipStarts, sideClipWidths)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L14)
  — scans the alpha mask of the `mapback` IndexedSprite. Top loop (rows 0–32,
  cols 0–33) extracts the start/width of the transparent run per row into the
  `top*` arrays — this gives the rounded compass area. Bottom loop (rows 5–155,
  cols 25–171, ignoring the compass square at col≤34 ∧ row≤34) does the same
  for the main minimap circle, storing into the `side*` arrays offset by
  `(-5, -25)`. These tables are later read every frame by the minimap drawer
  to skip the masked-out pixels.
- [`initializeRasterizerAndSceneVisibility(softwareVisibility)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L61)
  — calls `Rasterizer3D.method365(-950, h, w)` three times with `(479,96)` /
  `(190,261)` / `(512,334)` (gameplay viewport / chatbox region / fullscreen),
  snapshotting `Rasterizer3D.anIntArray1472` after each so the caller can swap
  in the right scanline offset table on demand. Then computes a 9-bucket
  far-clip table: for index 0..8, `angle = 128 + 32*i + 15`,
  `perspective = 600 + 3*angle`, then `(perspective * sin[angle]) >> 16`. Hands
  that table to `SceneGraph.method310(500, 800, 512, 334, visibilityMap, softwareVisibility)`
  which sets up the per-distance LOD culling masks. Returns the populated
  `State`.

**2 methods + 1 inner class.**

## BootstrapMediaLoader.java

Unpacks the core 2-D chrome (chatbox/inventory/minimap backgrounds, side icons,
compass) and the four 100-element sprite tables that the world renderer indexes
into.

- Inner `CoreMediaState` — bundles `invBack`, `chatBack`, `mapBack`,
  `backBase1`, `backBase2`, `backHMid1`, `sideIcons[13]`, `compass`, `mapEdge`.
- [`loadCoreMedia(mediaArchive)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLoader.java#L22)
  — `IndexedSprite` constructions for the six chrome panels, a 13-entry
  `sideIcons` array (the tabs), `Sprite` constructions for `compass` and
  `mapEdge`. The minimap edge is immediately rotated/sized via
  `mapEdge.method345(5059)` (the same dimension used by `SceneGraph` for the
  minimap viewport).
- [`loadMapSceneSprites(mediaArchive, mapSceneSprites)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLoader.java#L40)
  — loads up to 100 `IndexedSprite` entries from the `mapscene` archive into
  the caller's array (`aClass30_Sub2_Sub1_Sub2Array1060` in `GameClientCore`).
  The `try`/silent-`catch` swallows the inevitable end-of-archive exception.
- [`loadMapFunctionSprites(mediaArchive, mapFunctionSprites)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLoader.java#L51)
  — same pattern but for `Sprite mapfunction[0..99]`
  (`aClass30_Sub2_Sub1_Sub1Array1033`).
- [`loadHitmarkSprites(mediaArchive, hitmarkSprites)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLoader.java#L62)
  — `Sprite hitmarks[0..19]` into `aClass30_Sub2_Sub1_Sub1Array987`.
- [`loadHeadIconSprites(mediaArchive, headIconSprites)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLoader.java#L73)
  — `Sprite headicons[0..19]` into `aClass30_Sub2_Sub1_Sub1Array1095` (overhead
  prayer / clan / skull icons drawn by `method34`, see chunk A doc).

**5 methods + 1 inner class.**

## BootstrapMediaLayoutLoader.java

Unpacks the per-frame layout sprites: minimap dots/markers/crosses, the
scrollbar caps, the eight redstone trim variants used to frame on-screen
panels, and the moderator-icon set.

- Inner `State` — bundles `mapMarker0/1`, `crossSprites[8]`, `mapDotSprites[5]`,
  `scrollbar0/1`, `redstone1/2/3` plus the flipped, mirrored and
  both-transforms variants of redstone1/2 (and the mirrored-only variant of
  redstone3 — there is no flipped variant of `redstone3`), and `modIcons[2]`.
- [`load(mediaArchive)`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapMediaLayoutLoader.java#L30)
  — constructs each sprite from the media archive. The redstone trims are
  loaded fresh per variant (not derived from a shared base) and then mutated:
  `method358(0)` performs a vertical flip; `method359(true)` mirrors
  horizontally; `redstone*BothTransforms` get both. `crossSprites` is the
  per-frame click animation. `mapDotSprites` is the five minimap colour
  variants (item/NPC/player/friend/team). Returns the populated `State` which
  the caller spreads across roughly twenty `aClass30_Sub2_Sub1_Sub*` fields
  (`GameClientCore.java:6276-6296`).

**1 method + 1 inner class.**

## How the pipeline composes

```
GameClientCore.init() / startup loop  ([GameClientCore.java:6236])
        │
        ├─ ClientBootstrapLoader.shouldUseSunJavaLoopTuning() / createCacheStores()
        │
        ├─ bootstrapLoadTitleResources()                          (title fonts + 'title' Archive)
        │
        ├─ bootstrapLoadArchivesAndScene()                        (JAG: config/interface/media/textures/wordenc/sounds/versionlist
        │                                                          + SceneGraph + 4× CollisionMap)
        │
        ├─ bootstrapConnectUpdateServer(updateListArchive)        (OnDemandFetcher.method551 + AnimationFrame.method528 + Model.method459)
        │
        ├─ bootstrapPrimeOnDemand()                ──────────────► BootstrapDemandLoader.requestAnimations
        │     (returns false → "ondemand" failure)
        │
        ├─ bootstrapLoadDemandQueues()             ──────────────► BootstrapDemandLoader.requestModels
        │                                                          BootstrapDemandLoader.requestStartupMaps   (if cache present)
        │                                                          BootstrapDemandLoader.queuePriorityModelRequests
        │                                                          BootstrapDemandLoader.queueMemberSongs     (if sounds enabled)
        │
        ├─ method13(80, "Unpacking media")
        │
        ├─ BootstrapMediaLoader.loadCoreMedia      ──────────────► CoreMediaState → 9 chrome fields
        ├─ BootstrapMediaLoader.loadMapSceneSprites                IndexedSprite[100] (mapscene)
        ├─ BootstrapMediaLoader.loadMapFunctionSprites             Sprite[100]        (mapfunction)
        ├─ BootstrapMediaLoader.loadHitmarkSprites                 Sprite[20]
        ├─ BootstrapMediaLoader.loadHeadIconSprites                Sprite[20]
        ├─ BootstrapMediaLayoutLoader.load         ──────────────► State → ~20 chrome / minimap fields
        │
        ├─ BootstrapBackBufferLoader.createBackBuffers ─────────► 9× ProducingGraphicsBuffer (title smoke layer)
        │
        ├─ BootstrapDemandLoader.applyRandomMapSpriteTint          (mutates the two sprite arrays in place)
        │
        ├─ BootstrapConfigLoader.loadTexturesAndConfig ─────────► Rasterizer3D.method367/368/372
        │                                                          SequenceDefinition.method257
        │                                                          ObjectDefinition.method576
        │                                                          FloorDefinition.method260
        │                                                          ItemDefinition.method193 (+ aBoolean182 = members)
        │                                                          NpcDefinition.method162
        │                                                          IdentityKitDefinition.method535
        │                                                          SpotAnimationDefinition.method264
        │                                                          VarpDefinition.method546
        │                                                          VarBitDefinition.method533
        │
        ├─ BootstrapConfigLoader.loadSoundsIfNeeded ───────────► SoundEffect.method240        (skipped if aBoolean960)
        ├─ BootstrapConfigLoader.loadInterfaces    ────────────► Widget.method205             (populates Widget table)
        │
        ├─ method13(100, "Preparing game engine")
        │
        ├─ BootstrapGraphicsSetup.prepareMapbackClipMasks ─────► fills 4× int[] minimap-mask scanlines from mapback alpha
        ├─ BootstrapGraphicsSetup.initializeRasterizerAndSceneVisibility
        │                                          ────────────► Rasterizer3D.method365 (×3 scanline tables)
        │                                                          SceneGraph.method310     (per-LOD visibility)
        │
        └─ BootstrapEngineFinalizer.finalizeStartup ───────────► ChatCensor.method487
                                                                   new MouseRecorder + core.method12 (spawn thread)
                                                                   DynamicObject/ObjectDefinition/NpcDefinition.aClient* = gameClient
                                                                   returns MouseRecorder → aClass48_879
```

## Cross-class call graph

| Caller | Target class | Notable methods invoked |
|---|---|---|
| `BootstrapBackBufferLoader.createBackBuffers` | `Sprite`, `ProducingGraphicsBuffer` | `Sprite.method346(0,0,-32357)`; `new ProducingGraphicsBuffer(w,h,component,0)` |
| `BootstrapConfigLoader.loadTexturesAndConfig` | `Rasterizer3D` | `method368` (load), `method372` (gamma), `method367` (mip) |
| `BootstrapConfigLoader.loadTexturesAndConfig` | definition tables | `SequenceDefinition.method257`, `ObjectDefinition.method576`, `FloorDefinition.method260`, `ItemDefinition.method193`, `NpcDefinition.method162`, `IdentityKitDefinition.method535`, `SpotAnimationDefinition.method264`, `VarpDefinition.method546`, `VarBitDefinition.method533` |
| `BootstrapConfigLoader.loadSoundsIfNeeded` | `PacketBuffer`, `SoundEffect` | `new PacketBuffer(data, 891)`, `SoundEffect.method240(0, buf)` |
| `BootstrapConfigLoader.loadInterfaces` | `Widget` | `Widget.method205(archive, fonts, -84, mediaArchive)` |
| `BootstrapDemandLoader.requestAnimations` | `OnDemandFetcher`, `GameClientCore` | `method555(79,1)`, `method558(1,id)`, `method552()`, `anInt1349`; `core.method13`, `core.method57(false)` |
| `BootstrapDemandLoader.requestModels` | `OnDemandFetcher` | `method555(79,0)`, `method559(id,-203)`, `method558(0,id)`, `method552` |
| `BootstrapDemandLoader.requestStartupMaps` | `OnDemandFetcher` | `method562(layer,_,rx,ry)`, `method558(3,id)`, `method552` |
| `BootstrapDemandLoader.queuePriorityModelRequests` | `OnDemandFetcher` | `method555(79,0)`, `method559(id,-203)`, `method563(prio,0,id,(byte)8)` |
| `BootstrapDemandLoader.queueMemberSongs` | `OnDemandFetcher` | `method555(79,2)`, `method569(id,5)`, `method563((byte)1,2,id,(byte)8)` |
| `BootstrapDemandLoader.applyRandomMapSpriteTint` | `Sprite`, `IndexedSprite` | `Sprite.method344(dr,dg,db,0)`, `IndexedSprite.method360(dr,dg,db,0)` |
| `BootstrapEngineFinalizer.finalizeStartup` | `ChatCensor`, `MouseRecorder`, `GameClientCore` (super = `GameShell`) | `ChatCensor.method487`, `new MouseRecorder`, `core.method12(runnable, 10)` |
| `BootstrapEngineFinalizer.finalizeStartup` | definition tables | `DynamicObject.aClient1609 = gameClient`, `ObjectDefinition.aClient765 = gameClient`, `NpcDefinition.aClient82 = gameClient` |
| `BootstrapGraphicsSetup.prepareMapbackClipMasks` | `IndexedSprite` (fields) | reads `aByteArray1450`, `anInt1452` |
| `BootstrapGraphicsSetup.initializeRasterizerAndSceneVisibility` | `Rasterizer3D`, `SceneGraph` | `Rasterizer3D.method365(-950, h, w)` (×3) ⇒ snapshots `anIntArray1472`; reads `anIntArray1470` (sine table); `SceneGraph.method310(500, 800, 512, 334, visibility, sw)` |
| `BootstrapMediaLoader.loadCoreMedia` | `IndexedSprite`, `Sprite` | `new IndexedSprite(archive, name, sub)`, `Sprite.method345(5059)` (mapedge resize) |
| `BootstrapMediaLoader.load{MapScene,MapFunction,Hitmark,HeadIcon}Sprites` | `IndexedSprite`, `Sprite` | constructor only; swallows the EOF exception that ends the loop |
| `BootstrapMediaLayoutLoader.load` | `IndexedSprite`, `Sprite` | constructors + `IndexedSprite.method358(0)` (vertical flip), `IndexedSprite.method359(true)` (horizontal mirror) |
