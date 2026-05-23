# Known Quirks

## Legacy remote config

- `moparscape-reference/client/server.java` still contains old HybridScape remote-config/version logic.
- `moparscape-reference/client/client.java::sConfig(...)` also references the old remote config endpoint.
- These paths must fail closed. Local startup and login should continue without them.

## Module naming is misleading

- `moparscape-reference/client/` is the emulator.
- `moparscape-reference/server/moparscape/` is the desktop client.

## Fresh build vs stale binary

- Prefer Gradle run tasks or `build/libs/game-client.jar`.
- Do not trust `moparscape-reference/server/moparscape/moparclient.jar` for modernized behavior.

## Native client shell vs world parity

- `:rs-client-lwjgl` now has a native cache-backed title shell and gameplay shell.
- The title screen loads from the cache `title` archive when possible.
- The gameplay frame loads from the cache `media` archive when possible.
- Native tabs, chat, and minimap exist on the `rs-*` path.
- The left gameplay viewport is still not full RuneScape parity.
- Today it is a temporary native 3D terrain-focused world path, not the full legacy-equivalent scene graph/model/object pipeline.
- Do not describe it as “the game matches RuneScape” until object/model/scene parity exists.

## World viewport failures

- A successful login with a black or incorrect game area is a separate world/map/cache/rendering problem.
- Do not conflate it with input, auth, sidebar, or title-screen issues.

## Refactor discipline

- `GameClientCore.java` is still a giant protocol-heavy class.
- Extract behavior in narrow slices:
  - login/bootstrap
  - input/windowing
  - tab/sidebar handling
  - packet handlers
- Compile after each slice.
