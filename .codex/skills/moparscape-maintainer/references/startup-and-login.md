# Startup And Login

## Network defaults

- Emulator listens on port `43594`.
- The desktop client is wired for local testing against `127.0.0.1:43594`.
- Emulator-side local bootstrap defaults live in `moparscape-reference/client/BootstrapConfig.java`.

## Desktop client startup

- Standalone desktop launch path starts from `GameClientCore.main(...)`.
- `GameServerEndpoint` is the desktop client's typed default for server host/port parsing.
- The login UI was simplified to username and password only.
- Input and rendering in frame mode must go through the inner game canvas exposed by `GameFrame.getGameComponent()`.
- `GameClientCore.method11(...)` must stay aligned with `GameShell.method11(...)` so the visible surface and event surface are the same component.

## Emulator login bootstrap

- Login/player bootstrap flows through:
  - `server.main(...)`
  - `PlayerHandler.process()`
  - `client.initialize()`
- `server.CheckVersion()` now loads the root remote bootstrap config through `RemoteBootstrapConfigProvider`.
- The root remote config can populate `server.remoteClientBootstrapConfigUrl`, but startup continues if that fetch fails.
- `client.initialize()` now seeds local default sidebars through `BootstrapConfig.applyDefaultSidebarInterfaces(...)`.
- `client.initializeOptionalRemoteBootstrapOverrides()` only runs when `server.remoteClientBootstrapConfigUrl` is present.
- `RemoteBootstrapConfigProvider.loadClientOverrides(...)` parses the optional client override feed for `Config1`, `Config2`, and `sidebar` entries.
- Remote client overrides are optional compatibility behavior, not required for local startup.

## Legacy emulator account storage

- The legacy emulator still stores accounts in `moparscape-reference/client/characters/<username>.txt`.
- That behavior is only for the legacy reference path:
  - passwords are stored in plaintext
  - login comparison lowercases passwords, so passwords are effectively case-insensitive
- The native runtime does not use that storage path.

## When debugging login

- If typing or clicking breaks, check component ownership between `GameFrame`, `GameShell`, and `GameClientCore.method11(...)`.
- If startup differs between runs, check whether the optional remote override URL was populated in `server.remoteClientBootstrapConfigUrl`.
- If login succeeds but the world is black, treat that as a map/cache/bootstrap issue, not an auth issue.
- If the emulator says a player is already online after a crash, restart the emulator first.
