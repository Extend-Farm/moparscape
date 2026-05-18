# Moparscape Modernization Notes

This repository contains two Java codebases:

- `client/` -> emulator/server logic
- `server/moparscape/` -> legacy game client (decompiled/obfuscated lineage)

## Build

This project is now a Gradle multi-module build.

- Compile everything:
  - `./gradlew compileJava`
- Compile emulator only:
  - `./gradlew :emulator:compileJava`
- Compile game client only:
  - `./gradlew :game-client:compileJava`

## Run

Java 21 is required.

### Native rewrite path

Run the modern native client:

- `./gradlew :rs-client-lwjgl:run`

What it does:

- opens the native LWJGL title/login shell
- boots the new in-process `:rs-server-runtime`
- loads title and gameplay frame art from cache when available
- uses `client/characters` by default for persistence

Use PostgreSQL instead of character files:

1. `docker compose up -d postgres`
2. `./gradlew :rs-persistence-sql:migrateDevDatabase`
3. `RSPS_PERSISTENCE_MODE=postgres ./gradlew :rs-client-lwjgl:run`

### Legacy reference path

Run the old RSPS stack in two terminals from the repo root:

1. `./gradlew :emulator:run`
2. `./gradlew :game-client:run`

Notes:

- `:emulator` is the old server in `client/`
- `:game-client` is the old desktop client in `server/moparscape/`
- start the emulator first or the legacy client will fail to log in

## Parallel rewrite modules

The new architecture lives beside the legacy RSPS modules.

- Legacy reference executables:
  - `:emulator` -> `client/`
  - `:game-client` -> `server/moparscape/`
- New rewrite foundation:
  - `:rs-model` -> shared ids and world value types
  - `:rs-cache` -> cache store discovery/bootstrap
  - `:rs-content` -> content/bootstrap facade over live cache + legacy data paths
  - `:rs-sim-ecs` -> deterministic ECS simulation core
  - `:rs-persistence-api` -> repository contracts
  - `:rs-persistence-sql` -> PostgreSQL/Flyway persistence adapters
  - `:rs-protocol` -> new protocol message model and session contract
  - `:rs-server-runtime` -> actor-oriented headless runtime bootstrap
  - `:rs-client-core` -> client state machine and scene asset bootstrap
  - `:rs-client-lwjgl` -> first-pass LWJGL desktop shell
  - `:rs-test-fixtures` -> shared parity/fixture helpers

Run the new runtime foundation:

- `./gradlew :rs-server-runtime:run`

Run the new LWJGL shell:

- `./gradlew :rs-client-lwjgl:run`
- current mode: native LWJGL client backed by the new `:rs-server-runtime`
- title/login shell: loads from the cache `title` archive when available, with a native fallback only if title assets fail to load
- gameplay shell: loads the main frame art from the cache `media` archive
- tabs/chat/minimap: now exist in native form on the `rs-*` path
- persistence: defaults to `client/characters`, or uses PostgreSQL when `RSPS_PERSISTENCE_MODE=postgres`
- current limitation: the left world viewport is still mid-migration to proper RuneScape 3D world parity; it is not yet the full legacy scene graph/model/object pipeline

## Local PostgreSQL

The repo now defines its own development PostgreSQL setup. Do not depend on your machine-level `postgres` password.

- Docker service: `docker compose up -d postgres`
- Default JDBC URL: `jdbc:postgresql://127.0.0.1:55432/moparscape`
- Default username: `moparscape`
- Default password: `moparscape`
- Env template: copy `.env.example` to `.env` if you want to override defaults

Run the SQL tasks:

- test the connection:
  - `./gradlew :rs-persistence-sql:testDevDatabaseConnection`
- run Flyway migrations:
  - `./gradlew :rs-persistence-sql:migrateDevDatabase`
- import one character file:
  - `./gradlew :rs-persistence-sql:importCharacterToDevDatabase -Pcharacter=akira`

Runtime composition:

- default launcher persistence: character files under `client/characters`
- PostgreSQL launcher persistence: set `RSPS_PERSISTENCE_MODE=postgres`
- password storage in PostgreSQL is hashed; file-repository auth remains plaintext only during the compatibility window

## Architecture Boundary

The target architecture is strict:

- `:emulator` and `:game-client` are legacy reference modules
- production `rs-*` modules must not depend on or import legacy runtime code
- legacy usage is allowed only for parity tests, golden fixtures, planning, and reference documentation
- `:rs-client-lwjgl` must remain a native client, even when its behavior is being compared against the legacy client

For parallel execution and documentation discipline, see [docs/multi-agent-workflow.md](/home/akira/projects/moparscape/docs/multi-agent-workflow.md:1).

## Recent modernization changes

- Added Gradle wrapper and multi-project setup.
- Removed checked-in legacy `.class` binaries from `server/moparscape/`.
- Restored missing source for `Class7` and then renamed key classes in phases.

## Rename progress (server/moparscape)

### Phase 1 (runtime/bootstrap)

- `client` -> `GameClient`
- `Applet_Sub1` -> `GameShell`
- `Frame_Sub1` -> `GameFrame`
- `sign/signlink` -> `sign/SignLink`
- `Class42` -> `ResourceProvider`
- `Class42_Sub1` -> `OnDemandFetcher`
- `Class44` -> `Archive`
- `Class30_Sub2_Sub2` -> `PacketBuffer`
- `Class17` -> `IsaacCipher`
- `Class14` -> `CacheFileStore`
- `Class30_Sub2_Sub3` -> `OnDemandRequest`
- `Class15` -> `ProducingGraphicsBuffer`

### Phase 2 (scene/map/object)

- `Class7` -> `MapRegion`
- `Class11` -> `CollisionMap`
- `Class22` -> `FloorDefinition`
- `Class25` -> `SceneGraph`
- `Class46` -> `ObjectDefinition`
- `Class30_Sub2_Sub4_Sub5` -> `DynamicObject`
- `Class30_Sub2_Sub4_Sub6` -> `Model`

### Phase 3 (core containers)

- `Class30` -> `Node`
- `Class30_Sub2` -> `CacheableNode`
- `Class19` -> `Deque`
- `Class2` -> `CacheableNodeDeque`

### Phase 4 (utilities/infrastructure)

- `Class1` -> `NodeHashTable`
- `Class12` -> `NodeCache`
- `Class13` -> `BZip2Decompressor`
- `Class32` -> `BZip2State`
- `Class24` -> `BufferedConnection`
- `Class45` -> `Skills`
- `Class50` -> `TextUtils`

### Phase 5 (rendering primitives)

- `Class30_Sub2_Sub1` -> `Rasterizer2D`
- `Class30_Sub2_Sub1_Sub1` -> `Sprite`
- `Class30_Sub2_Sub1_Sub2` -> `IndexedSprite`
- `Class30_Sub2_Sub1_Sub3` -> `Rasterizer3D`
- `Class30_Sub2_Sub1_Sub4` -> `FontRenderer`

### Phase 6 (scene internals and animation defs)

- `Class20` -> `SequenceDefinition`
- `Class30_Sub3` -> `SceneTile`
- `Class43` -> `SceneTilePaint`
- `Class40` -> `SceneTileModel`
- `Class28` -> `InteractiveObject`
- `Class10` -> `WallObject`
- `Class26` -> `WallDecoration`
- `Class49` -> `GroundDecoration`
- `Class3` -> `GroundItemPile`
- `Class47` -> `Occluder`

### Phase 7 (definitions and animation metadata)

- `Class23` -> `SpotAnimationDefinition`
- `Class36` -> `AnimationFrame`
- `Class18` -> `AnimationSkeleton`
- `Class21` -> `ModelHeader`
- `Class37` -> `VarBitDefinition`
- `Class41` -> `VarpDefinition`
- `Class38` -> `IdentityKitDefinition`
- `Class33` -> `VertexNormal`

### Phase 8 (entities, renderables, and UI defs)

- `Class30_Sub2_Sub4` -> `Renderable`
- `Class30_Sub2_Sub4_Sub1` -> `Actor`
- `Class30_Sub2_Sub4_Sub1_Sub1` -> `Npc`
- `Class30_Sub2_Sub4_Sub1_Sub2` -> `Player`
- `Class30_Sub2_Sub4_Sub3` -> `GraphicsObject`
- `Class30_Sub2_Sub4_Sub4` -> `Projectile`
- `Class8` -> `ItemDefinition`
- `Class5` -> `NpcDefinition`
- `Class9` -> `Widget`
- `Class48` -> `MouseRecorder`

### Phase 9 (remaining classes and candidate naming)

- `Class4` -> `TileRotationUtils`
- `Class6` -> `SoundSynthesizer`
- `Class16` -> `SoundEffect`
- `Class29` -> `SoundEnvelope`
- `Class31` -> `PacketSizeTable`
- `Class34` -> `ChatCensor`
- `Class35` -> `ChatMessageCodec`
- `Class39` -> `SoundFilter`
- `Class30_Sub1` -> `SceneObjectSpawnRequest`
- `Class30_Sub2_Sub4_Sub2` -> `GroundItem`
- `Class27` -> `UnusedClientFlagsCandidate` (low-confidence placeholder by design)

### Phase 10 (core container API cleanup)

Refined high-traffic intrusive list/hash APIs for readability:

- `Node`: `method329` -> `unlink`, fields `aLong548/aClass30_549/aClass30_550` -> `key/prev/next`
- `CacheableNode`: `method330` -> `unlinkDual`, dual-link fields renamed to `previousDual/nextDual`
- `Deque`: renamed `method249..method256` to `addFirst/addLast/removeLast/last/first/previous/next/clear`
- `NodeHashTable`: `method148/method149` -> `get/put`, buckets metadata names clarified

## Next steps

- Continue phased legacy stabilization only where it supports parity fixtures or safe extraction.
- Keep the cache-backed native title/gameplay shell honest about its current boundaries.
- Port native widget/interface decoding so sidebar tabs stop relying on simplified stand-ins.
- Replace the temporary native world viewport with full RuneScape 3D scene parity:
  - object definitions
  - model decoding
  - object placement
  - actor rendering
  - proper scene graph/camera/minimap behavior
- Port cache/content decode, region load, entity visibility, and additional gameplay verticals into the new modules.
