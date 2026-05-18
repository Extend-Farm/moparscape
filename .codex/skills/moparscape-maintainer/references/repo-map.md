# Repo Map

## Modules

- `client/` is the emulator/server module.
- `server/moparscape/` is the desktop game client.
- Gradle project names:
  - `:emulator` -> `client/`
  - `:game-client` -> `server/moparscape/`
- Native rewrite modules:
  - `:rs-model` -> shared world/value types
  - `:rs-cache` -> cache store/bootstrap/archive access
  - `:rs-content` -> cache-backed content decoding
  - `:rs-sim-ecs` -> deterministic simulation core
  - `:rs-persistence-api` -> persistence contracts
  - `:rs-persistence-sql` -> PostgreSQL/Flyway persistence
  - `:rs-protocol` -> native client/server messages
  - `:rs-server-runtime` -> native runtime/auth/session orchestration
  - `:rs-client-core` -> native client state/view model
  - `:rs-client-lwjgl` -> native LWJGL desktop client

## Physical source layout

- Emulator sources are still flat under `client/` and remain in the default package.
- Desktop client sources now live under:
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/client`
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/cache`
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/net`
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/sign`

## Main entry points

- Emulator main class: `server`
- Desktop client main class: `io.github.ffakira.moparscape.client.GameClient`
- Native LWJGL client main class: `io.github.ffakira.rsps.client.lwjgl.LwjglClientMain`

## High-risk files

- Emulator bootstrap and listener:
  - `client/server.java`
  - `client/PlayerHandler.java`
  - `client/client.java`
- Desktop client bootstrap and windowing:
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameShell.java`
  - `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameFrame.java`
- Native client bootstrap and rendering:
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/LwjglClientMain.java`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/OpenGlTileRenderSystem.java`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/CacheBackedWorldSceneLoader.java`

## Persistence today

- Character saves live under `client/characters/`.
- Ban lists live under `client/Bans/`.
- Native runtime defaults to the character-file repository unless `RSPS_PERSISTENCE_MODE=postgres`.
- The emulator still has legacy remote-config hooks, but local startup should not depend on them.

## Native client status

- `:rs-client-lwjgl` is the active native client path.
- It already has a cache-backed title/login shell and cache-backed gameplay frame shell.
- Native tabs, chat, and minimap exist.
- The left world viewport is still mid-migration:
  - current state: native 3D terrain-focused world path
  - missing for full parity: object/model decode, scene graph parity, actor rendering, real widget/interface rendering

## Architecture boundary

- `client/` and `server/moparscape/` are legacy reference modules.
- `rs-*` modules must not import or execute legacy runtime code.
- Legacy code can still be read as a reference for migration planning, parity tests, and documentation.

## Rename context

- The root `README.md` tracks modernization and rename progress for the desktop client.
- Continue renames in small slices; do not rename large protocol-heavy regions blindly.
