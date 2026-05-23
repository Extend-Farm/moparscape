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

- Persistence for `:rs-client-lwjgl`:
  - PostgreSQL through `:rs-persistence-sql`
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
- Start the repo-local database plus the one-shot bootstrap container:
  - `docker compose up --build postgres postgres-init`
- Test connectivity:
  - `./gradlew :rs-persistence-sql:testDevDatabaseConnection`
- Apply migrations:
  - `./gradlew :rs-persistence-sql:migrateDevDatabase`
- Create a new native account:
  - `./gradlew :rs-client-lwjgl:run`
  - enter a new username and password in the login frame
- Provision one account and starter character from environment variables:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:provisionDevAccount`
- Migrate and provision in one command:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:migrateAndProvisionDevDatabase`
- Wipe and rebuild the schema:
  - `./gradlew :rs-persistence-sql:resetDevDatabase`
- Wipe, rebuild, and reprovision:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:resetAndProvisionDevDatabase`
- The SQL module does not import `moparscape-reference/client/characters/*.txt`.
- The default compose flow uses `RSPS_POSTGRES_PASSWORD` directly for local development.
- The SQL tooling still accepts either direct password env vars or `*_FILE` secret-file env vars, but not both at once.

## Preferred binaries and run paths

- Preferred desktop client jar:
  - `moparscape-reference/server/moparscape/build/libs/game-client.jar`
- Avoid the stale legacy jar:
  - `moparscape-reference/server/moparscape/moparclient.jar`
- Prefer Gradle for the native client:
  - `./gradlew :rs-client-lwjgl:run`
- Prefer Gradle for the native QUIC server:
  - `./gradlew :rs-transport-quic:run`

## Retest discipline

- After emulator changes in login, sidebars, or startup, fully restart the emulator.
- If the server crashed during login, restart it before trusting any “already online” state.
- After native client changes in title/gameplay shell or world rendering, rerun `:rs-client-lwjgl:run` from a fresh process instead of trusting an old window.
