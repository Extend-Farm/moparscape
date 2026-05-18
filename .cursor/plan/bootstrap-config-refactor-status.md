# Bootstrap Config Refactor Status

## Objective

Replace hidden legacy bootstrap dependencies with explicit local configuration for emulator startup, login bootstrap, and default client UI/session setup.

Primary goals:
- remove the dead remote HybridScape config from the critical startup path
- centralize server port, client connection defaults, and sidebar/interface defaults
- keep current runtime behavior intact while making future refactors safer

## Current Status

Status: `in_progress`

The codebase is partially stabilized, and the bootstrap-config refactor now has a shared remote-bootstrap parsing boundary.

What is true now:
- emulator startup no longer hard-fails if the remote HybridScape config endpoint is unavailable
- login initialization no longer crashes when remote server config cannot be fetched
- local emulator bootstrap defaults now live in `client/BootstrapConfig.java`
- default sidebar interfaces are initialized locally during player bootstrap
- remote root-config parsing and remote client-override parsing are now shared in `client/RemoteBootstrapConfigProvider.java`
- the desktop client now has an explicit local default server address of `127.0.0.1:43594`

What is not true yet:
- there is still no single cross-module bootstrap model shared by emulator and desktop client
- the legacy remote feed still exists as an optional path through `BootstrapConfig.REMOTE_BOOTSTRAP_CONFIG_URL`
- some defaults are still split between `BootstrapConfig` and the desktop client's `GameServerEndpoint`
- sidebar defaults are centralized, but most interface ids are still unnamed raw values

## Completed Work So Far

1. Emulator bootstrap defaults were centralized.
   - `client/BootstrapConfig.java`
   - server port, root remote-bootstrap URL, default sidebars, and magic-tab selection now have one local home.

2. Shared remote-bootstrap parsing was extracted.
   - `client/RemoteBootstrapConfigProvider.java`
   - root-config parsing and client-override parsing no longer live in separate ad hoc readers.

3. Server startup was hardened against dead remote config.
   - `client/server.java`
   - `CheckVersion()` now loads the root remote config through `RemoteBootstrapConfigProvider`, captures the optional client override URL, and continues locally if the fetch fails.

4. Local sidebar bootstrap was introduced.
   - `client/client.java`
   - `initializeDefaultSidebarInterfaces()` now sends the baseline local sidebar/interface setup before any optional remote override pass.

5. Optional client overrides were isolated from default login bootstrap.
   - `client/client.java`
   - `initializeOptionalRemoteBootstrapOverrides()` now loads and applies remote overrides only when `server.remoteClientBootstrapConfigUrl` is present and readable.

6. Client connection defaults were partially localized.
   - `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameServerEndpoint.java`
   - host/port parsing now falls back to `127.0.0.1:43594`.

## Verification

Verified on May 16, 2026:

```bash
./gradlew :emulator:compileJava
./gradlew :game-client:compileJava
```

Results:
- `:emulator:compileJava` `BUILD SUCCESSFUL`
- `:game-client:compileJava` `BUILD SUCCESSFUL`

Behavioral verification already observed during current stabilization work:
- login reaches the credential screen
- local sidebar defaults can be restored without relying on the remote config feed
- remote override failures no longer block default startup

## Known Blockers

1. Bootstrap defaults are still split across modules.
   - emulator defaults live in `client/BootstrapConfig.java`
   - desktop client defaults live in `GameServerEndpoint`
   - there is still no shared contract describing the full local startup path end to end

2. The optional remote bootstrap path still exists.
   - `client/server.java` still reads `BootstrapConfig.REMOTE_BOOTSTRAP_CONFIG_URL`
   - the provider still depends on legacy keys such as `ServerVersion`, `MOTD`, and `byte11`
   - this path is no longer required for startup, but it still remains supported behavior

3. Sidebar setup still depends on magic numbers.
   - interface ids were moved out of `client.java`
   - most of them are still unnamed numeric values inside `BootstrapConfig`

4. Separate runtime issues still exist outside bootstrap.
   - world/map loading remains a separate problem from startup config
   - that issue should not be mixed into the bootstrap extraction itself

## Next Recommended Step

Finish the bootstrap slice by tightening naming and boundaries around the new shared provider.

Recommended slice:
1. Give the remaining default sidebar interface ids explicit names in `BootstrapConfig`.
2. Decide whether the remote bootstrap path should stay supported or be removed entirely.
3. If it stays, rename the remaining legacy remote keys and document them as compatibility behavior.
4. Keep the next pass bootstrap-only; do not combine it with map-loading or persistence changes.

## Exit Criteria For This Task

This task should be considered complete when:
- emulator and login bootstrap work without any remote config dependency
- server and client local defaults are explicit and documented
- sidebar defaults are named and centralized
- remote override behavior, if kept at all, is optional and isolated from the default startup path
