# Repo Map

## Modules

- `moparscape-reference/client/` is the emulator/server module.
- `moparscape-reference/server/moparscape/` is the desktop game client.
- Gradle project names:
  - `:emulator` -> `moparscape-reference/client/`
  - `:game-client` -> `moparscape-reference/server/moparscape/`
- Native rewrite modules:
  - `:rs-model` -> shared world/value types
  - `:rs-cache` -> cache store/bootstrap/archive access
  - `:rs-content` -> cache-backed content decoding
  - `:rs-sim-ecs` -> deterministic simulation core
  - `:rs-persistence-api` -> persistence contracts
  - `:rs-persistence-sql` -> PostgreSQL/Flyway persistence
  - `:rs-protocol` -> native client/server framing, codecs, and domain packet models
  - `:rs-server-runtime` -> native runtime/auth/session orchestration
  - `:rs-client-core` -> native client state/view model
  - `:rs-client-lwjgl` -> native LWJGL desktop client
  - `:rs-transport-quic` -> QUIC transport and external native server

## Physical source layout

- Emulator sources are still flat under `moparscape-reference/client/` and remain in the default package.
- Desktop client sources now live under:
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/client`
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/cache`
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/net`
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/sign`

## Main entry points

- Emulator main class: `server`
- Desktop client main class: `io.github.ffakira.moparscape.client.GameClient`
- Native LWJGL client main class: `io.github.ffakira.rsps.client.desktop.app.DesktopClientMain`
- Native QUIC server main class: `io.github.ffakira.rsps.transport.quic.QuicServerMain`

## High-risk files

- Emulator bootstrap and listener:
  - `moparscape-reference/client/server.java`
  - `moparscape-reference/client/PlayerHandler.java`
  - `moparscape-reference/client/client.java`
- Desktop client bootstrap and windowing:
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java`
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameShell.java`
  - `moparscape-reference/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameFrame.java`
- Native client bootstrap and rendering:
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/app/DesktopClientMain.java`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/render/opengl/OpenGlTileRenderSystem.java`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/desktop/world/CacheBackedWorldSceneLoader.java`

## Persistence today

- Legacy emulator character saves live under `moparscape-reference/client/characters/`.
- Ban lists live under `moparscape-reference/client/Bans/`.
- Native runtime uses PostgreSQL repositories from `:rs-persistence-sql`.
- The emulator still has legacy remote-config hooks, but local startup should not depend on them.

## Native client status

- `:rs-client-lwjgl` is the active native client path.
- It already has a cache-backed title/login shell and cache-backed gameplay frame shell.
- Native tabs, chat, and minimap exist.
- The left world viewport is still mid-migration:
  - current state: native 3D terrain-focused world path
  - missing for full parity: object/model decode, scene graph parity, actor rendering, real widget/interface rendering

## Architecture boundary

- `moparscape-reference/client/` and `moparscape-reference/server/moparscape/` are reference modules.
- `rs-*` modules must not import or execute legacy runtime code.
- Legacy code can still be read as a reference for migration planning, parity tests, and documentation.

## Rename context

- The root `README.md` tracks modernization and rename progress for the desktop client.
- Continue renames in small slices; do not rename large protocol-heavy regions blindly.
