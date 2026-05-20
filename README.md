# Moparscape

Moparscape contains two RSPS codepaths in one Gradle repo:

- the legacy Moparscape server and desktop client
- the native `rs-*` rewrite, including the LWJGL client

The legacy modules are the reference implementation. The `rs-*` modules are the active modernization path.

## Requirements

- Java 21
- Docker, if you want the repo-local PostgreSQL service

## Quick Start

### Run legacy Moparscape client + server

Use two terminals from the repo root.

1. Start the legacy server:

```bash
./gradlew :emulator:run
```

2. Start the legacy desktop client:

```bash
./gradlew :game-client:run
```

Notes:

- `:emulator` maps to `client/`
- `:game-client` maps to `server/moparscape/`
- start the server first or the legacy client will fail to log in

### Run the native LWJGL client

```bash
./gradlew :rs-client-lwjgl:run
```

Notes:

- this does **not** require `:emulator:run`
- it boots against the native in-process runtime from `:rs-server-runtime`
- default persistence is character files under `client/characters`
- current native status:
  - cache-backed title/login shell
  - cache-backed gameplay frame shell
  - native tabs/chat/minimap
  - world viewport still mid-migration to full RuneScape scene parity

### Run the native LWJGL client with PostgreSQL

```bash
docker compose up -d postgres
./gradlew :rs-persistence-sql:migrateDevDatabase
RSPS_PERSISTENCE_MODE=postgres ./gradlew :rs-client-lwjgl:run
```

## Common Commands

Compile everything:

```bash
./gradlew compileJava
```

Compile only the legacy server:

```bash
./gradlew :emulator:compileJava
```

Compile only the legacy desktop client:

```bash
./gradlew :game-client:compileJava
```

Compile only the native LWJGL client:

```bash
./gradlew :rs-client-lwjgl:compileJava
```

Build the legacy desktop client jar:

```bash
./gradlew :game-client:jar
```

The preferred legacy desktop client jar is:

- `server/moparscape/build/libs/game-client.jar`

Avoid relying on the stale checked-in legacy jar:

- `server/moparscape/moparclient.jar`

## Repository Layout

### Top-level folders

| Path | Purpose |
| --- | --- |
| `artifacts/` | Scratch output area for generated artifacts and one-off repo outputs. |
| `client/` | Legacy Moparscape server/emulator module (`:emulator`). Sources are still in the old flat layout. |
| `server/` | Container directory for the legacy desktop client module and its runtime assets. |
| `server/moparscape/` | Legacy desktop game client module (`:game-client`). This is the decompiled/renamed reference client. |
| `rs-model/` | Shared ids, world values, and low-level model types for the native rewrite. |
| `rs-cache/` | Cache store readers, archive loading, raw model decode, and cache-side assets. |
| `rs-content/` | Native content decoding on top of cache data, such as definitions and sequence metadata. |
| `rs-sim-ecs/` | Deterministic simulation foundation for the native rewrite. |
| `rs-persistence-api/` | Persistence interfaces used by the native runtime. |
| `rs-persistence-sql/` | PostgreSQL and Flyway support for the native runtime. |
| `rs-protocol/` | Native client/server protocol models and bootstrap messages. |
| `rs-server-runtime/` | Native runtime, login/session orchestration, and in-process game bootstrap. |
| `rs-client-core/` | Native client state and view-model layer. |
| `rs-client-lwjgl/` | Native LWJGL desktop client. |
| `rs-test-fixtures/` | Shared fixtures and parity helpers used by tests. |
| `.codex/docs/` | Repo-local architecture notes, parity docs, and deeper technical references. |
| `.codex/plan/` | Active task notes and handoff plans for ongoing work. |
| `.codex/` | Repo-local Codex skills, plans, references, and assistant-facing repo guidance. |
| `.cursor/` | Editor-local state used by Cursor; not part of the runtime or build. |
| `gradle/` | Gradle wrapper support files. |

### Important legacy subfolders

#### `client/`

- `*.java`, `*.cfg`, `*.json`
  - the legacy server keeps its source files and data files directly in `client/` instead of under `src/main/java`
- `characters/`
  - default character-file persistence for both the legacy server and the native runtime
- `Bans/`
  - legacy ban lists
- `data/`
  - legacy server-side data files
- `logs/`
  - legacy runtime logs
- `build/`, `bin/`
  - generated outputs; do not treat them as source of truth

#### `server/moparscape/`

- `src/main/java/io/github/ffakira/moparscape/client`
  - legacy client runtime, scene graph, UI, rendering, login, and gameplay code
- `src/main/java/io/github/ffakira/moparscape/cache`
  - legacy client cache/archive classes
- `src/main/java/io/github/ffakira/moparscape/net`
  - legacy client networking code
- `sign/`
  - legacy signlink/platform support folder kept with the reference client module
- `cache/`
  - legacy client-side cache/runtime assets used by the reference path
- `build.gradle.kts`, `README.md`, `moparclient.jar`
  - module-local build file, notes, and the stale checked-in legacy jar

### Native rewrite module roles

#### Runtime side

- `rs-server-runtime/`
  - native runtime bootstrap
  - login/session flow
  - persistence selection
- `rs-persistence-api/`
  - persistence contracts
- `rs-persistence-sql/`
  - PostgreSQL adapters and migrations
- `rs-protocol/`
  - bootstrap messages and runtime-facing protocol models

#### Client side

- `rs-client-core/`
  - native client state machine and data flow
- `rs-client-lwjgl/`
  - title/login shell
  - gameplay chrome
  - world viewport and minimap work in progress

#### Shared decode/content side

- `rs-cache/`
  - raw cache access
  - archive decode
  - raw model decode
- `rs-content/`
  - cache-backed definitions and content catalogs
- `rs-model/`
  - shared world/model value types

## Persistence and Database

Default persistence:

- character files under `client/characters`

Repo-local PostgreSQL defaults:

- host: `127.0.0.1`
- port: `55432`
- database: `moparscape`
- username: `moparscape`
- password: `moparscape`

Useful commands:

```bash
docker compose up -d postgres
./gradlew :rs-persistence-sql:testDevDatabaseConnection
./gradlew :rs-persistence-sql:migrateDevDatabase
./gradlew :rs-persistence-sql:importCharacterToDevDatabase -Pcharacter=akira
```

## Current Architecture Boundary

These rules are intentional:

- `:emulator` and `:game-client` are legacy reference modules
- `rs-*` modules must stay native
- native production code must not depend on or execute the legacy runtime
- legacy code is allowed as a reference for:
  - parity planning
  - tests
  - fixtures
  - documentation

## Current Native Client Status

`rs-client-lwjgl` is already useful, but it is not full RuneScape viewport parity yet.

What exists:

- cache-backed title/login shell
- cache-backed gameplay frame shell
- native tabs/chat/minimap
- native item icon work
- native actor and terrain work in progress

What is still incomplete:

- full scene-graph parity
- full terrain/raster parity
- full object/model ordering and clipping parity
- full minimap/radar parity
- full widget/interface decode parity

For deeper renderer and parity notes, see:

- [.codex/docs/native-world-pipeline.md](/home/akira/projects/moparscape/.codex/docs/native-world-pipeline.md:1)
- [.codex/docs/native-rasterizer-parity.md](/home/akira/projects/moparscape/.codex/docs/native-rasterizer-parity.md:1)
- [.codex/plan/terrain-minimap-parity.md](/home/akira/projects/moparscape/.codex/plan/terrain-minimap-parity.md:1)
