# Phase 2: Cache And Content Parity

## Objective

Move the new rewrite stack from repository/bootstrap awareness to real cache/content parity work.

This phase is complete when the new modules can:

- read raw archives from the real MoparScape cache store without legacy runtime help
- load the known top-level cache archives (`title`, `config`, `interface`, `media`, `textures`, `sounds`)
- begin parsing archive containers and selected named files with parity tests against the legacy client
- expose those decoded assets through new `rs-content` services instead of direct legacy class calls

## Starting State

Already implemented:

- Gradle multi-module rewrite foundation
- `rs-cache` cache store discovery
- `rs-content` content bootstrap manifest
- `rs-sim-ecs` deterministic ECS core
- `rs-server-runtime` actor-oriented shell
- `rs-client-core` and `rs-client-lwjgl` bootstrap shells

New work started in this slice:

- low-level `CacheStoreReader` parity against legacy `CacheFileStore`
- top-level archive catalog loading in `rs-content`

## Deliverables

1. `rs-cache`
- low-level sector/index reader
- archive container header parser
- next step: archive container decompression and named-entry parsing

2. `rs-content`
- top-level archive catalog
- next step: selected definition decoders for items, npcs, objects, and map indices

3. Parity tests
- raw archive parity vs legacy `CacheFileStore`
- selected content file parity vs legacy `Archive` access paths

## Acceptance Criteria

- `./gradlew :rs-cache:test :rs-content:test` passes
- at least one selected top-level archive is verified against legacy bytes
- next agent slices can work in parallel without overlapping write scopes
