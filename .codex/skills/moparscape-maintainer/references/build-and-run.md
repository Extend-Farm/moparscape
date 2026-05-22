# Build And Run

## Requirements

- Java 21
- Docker, if using the repo-local PostgreSQL service

## Common commands

- Compile everything:
  - `./gradlew compileJava`
- Compile emulator only:
  - `./gradlew :emulator:compileJava`
- Compile desktop client only:
  - `./gradlew :game-client:compileJava`
- Run emulator:
  - `./gradlew :emulator:run`
- Run desktop client:
  - `./gradlew :game-client:run`
- Run native LWJGL client:
  - `./gradlew :rs-client-lwjgl:run`
- Run native QUIC server:
  - `./gradlew :rs-transport-quic:run`
- Build desktop client jar:
  - `./gradlew :game-client:jar`

## Native client modes

- Default persistence for `:rs-client-lwjgl`:
  - character files under `client/characters`
- PostgreSQL persistence for `:rs-client-lwjgl`:
  - `RSPS_PERSISTENCE_MODE=postgres ./gradlew :rs-client-lwjgl:run`
- The native client does not require `:emulator:run`; it boots against the native in-process runtime.
- To use the external QUIC server instead of the in-process runtime:
  - `RSPS_RUNTIME_MODE=quic ./gradlew :rs-client-lwjgl:run`
- Native QUIC defaults:
  - host: `localhost`
  - bind host: `127.0.0.1`
  - port: `43594`
  - certificate dir: `artifacts/quic`
- Current native client state:
  - cache-backed title/login shell
  - cache-backed gameplay frame shell
  - native tabs/chat/minimap
  - left world viewport still mid-migration to proper RuneScape 3D parity

## Local PostgreSQL

- Start the repo-local database:
  - `docker compose up -d postgres`
- Test connectivity:
  - `./gradlew :rs-persistence-sql:testDevDatabaseConnection`
- Apply migrations:
  - `./gradlew :rs-persistence-sql:migrateDevDatabase`

## Preferred binaries and run paths

- Preferred desktop client jar:
  - `server/moparscape/build/libs/game-client.jar`
- Avoid the stale legacy jar:
  - `server/moparscape/moparclient.jar`
- Prefer Gradle for the native client:
  - `./gradlew :rs-client-lwjgl:run`
- Prefer Gradle for the native QUIC server:
  - `./gradlew :rs-transport-quic:run`

## Retest discipline

- After emulator changes in login, sidebars, or startup, fully restart the emulator.
- If the server crashed during login, restart it before trusting any “already online” state.
- After native client changes in title/gameplay shell or world rendering, rerun `:rs-client-lwjgl:run` from a fresh process instead of trusting an old window.
