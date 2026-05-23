# Modern RSPS Architecture

This repository now contains two parallel stacks:

- the legacy MoparScape emulator/client used as a runnable behavior reference
- a new Java 21 rewrite foundation intended to replace it over time

The rewrite rule is strict:

- legacy code is reference-only for behavior, cache format research, and parity validation
- `rs-*` production runtime code must not import or execute legacy client/server classes

## Current module boundaries

- `:rs-model`
  - immutable ids and world-space value objects
- `:rs-cache`
  - live cache store discovery against `moparscape-reference/server/moparscape/cache/.file_store_32`
- `:rs-content`
  - shared bootstrap manifest plus native definition/archive decoders
- `:rs-sim-ecs`
  - deterministic simulation core with ECS-style component storage and systems
- `:rs-persistence-api`
  - persistence contracts that the runtime depends on
- `:rs-persistence-sql`
  - PostgreSQL adapters and Flyway migrations
- `:rs-protocol`
  - protocol message model independent from the legacy wire format
- `:rs-server-runtime`
  - actor-oriented runtime shell with a world shard actor
- `:rs-client-core`
  - client state transitions and scene asset bootstrap
- `:rs-client-lwjgl`
  - first desktop shell for the new client runtime
- `:rs-test-fixtures`
  - reusable repository/cache fixture helpers

## Runtime direction

- actor boundaries are used for sessions, login/runtime orchestration, and world shards
- ECS stays inside the deterministic shard simulation
- no actor-per-entity model is planned
- legacy modules remain the parity source until the new stack reaches `login + world`

## What is implemented already

- new Gradle module graph
- Java 21 conventions for all new modules
- cache store discovery against the real checked-in RS cache
- content bootstrap manifest
- initial ECS world, commands, events, and movement system
- repository contracts plus PostgreSQL/Flyway adapter scaffolding
- protocol message model
- headless server bootstrap
- in-process `login + move` gameplay sandbox in the LWJGL client

## Native World Boundary

The current native world path is intentionally narrower than the legacy RuneScape client.

What `rs-*` does today:

- loads live player bootstrap state through native persistence/runtime code
- loads terrain region archives through `:rs-cache` and `:rs-content`
- decodes floor colors and heights into a native `WorldScene`
- renders a temporary 3D terrain mesh in `:rs-client-lwjgl`
- renders a terrain-derived minimap into the cache-backed gameplay frame

What `rs-*` does **not** do yet:

- decode object region archives into placed world objects
- decode and register model archives for scene use
- build a native scene graph equivalent to legacy `MapRegion` + `SceneGraph`
- render object models, player models, or NPC models
- render a scene-backed minimap with `mapscene` / object overlays

This means the current world viewport is a terrain-only preview. It is not a broken RuneScape scene renderer; the full scene pipeline simply is not implemented yet.

See [native-world-pipeline.md](/home/akira/projects/moparscape/.codex/docs/native-world-pipeline.md:1) for the operational migration path.

## What is intentionally not done yet

- cache archive decoding parity
- real client/server networking beyond the temporary in-process bridge
- full world scene parity
- inventory/combat/skills/quest migration
- persistence migration from legacy flat files
