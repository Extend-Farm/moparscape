# Persistence

## Current State

- The native runtime persists through PostgreSQL in `:rs-persistence-sql`:
  - canonical account identity in `accounts`
  - credentials in `account_credentials`
  - one character per account in `characters`
  - normalized child tables for positions, profile, appearance slots, skills, item slots, and social links
- The legacy emulator still has its own file-based storage under `moparscape-reference/client/characters`, but that is not the native runtime path.

## Important Rules

- Do not import `moparscape-reference/client/characters/*.txt` through the SQL module.
- Do not derive SQL account or character ids from username hashes.
- Preserve stored login and display name casing; use explicit lookup keys for case-insensitive queries instead of destructive name rewriting.
- Keep the runtime boundary at `AccountRepository` and `CharacterRepository` unless the login/bootstrap contract changes.
- Native PostgreSQL login may provision a missing account and starter character on first login.
- Hash passwords before persistence.

## Local PostgreSQL Workflow

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
- Provision one account and starter character:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:provisionDevAccount`
- Migrate and provision:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:migrateAndProvisionDevDatabase`
- Wipe and rebuild the schema:
  - `./gradlew :rs-persistence-sql:resetDevDatabase`
- Wipe, rebuild, and reprovision:
  - `RSPS_BOOTSTRAP_ACCOUNT_USERNAME=akira RSPS_BOOTSTRAP_ACCOUNT_PASSWORD='choose-a-real-development-password' RSPS_BOOTSTRAP_CHARACTER_NAME=Akira ./gradlew :rs-persistence-sql:resetAndProvisionDevDatabase`
- The default compose flow uses `RSPS_POSTGRES_PASSWORD` directly for local development.
- The SQL tooling still accepts either direct password env vars or `*_FILE` secret-file env vars, but not both at once.

## Schema Direction

- Keep account identity separate from credential material.
- Model repeating character state as rows, not array blobs.
- Use transactions around account and character saves.
- Add schema constraints for:
  - blank names
  - duplicate lookup keys
  - invalid planes
  - negative skill, slot, item, quantity, or social-link values

## Migration Boundary

- File-based persistence can remain available for reference-mode or compatibility flows in `:rs-server-runtime`.
- SQL persistence should be production-shaped on its own and must not depend on txt parsing utilities from the legacy character format.
- Development provisioning should stay idempotent:
  - account lookup by canonical key
  - password rotation by explicit reprovisioning
  - character creation only when missing
