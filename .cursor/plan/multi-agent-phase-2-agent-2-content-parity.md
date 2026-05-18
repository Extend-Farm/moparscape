# Agent 2 Packet: Content Parity

## Scope

Own only:

- `rs-content/src/main/java/io/github/ffakira/rsps/content/*`
- `rs-content/src/test/java/io/github/ffakira/rsps/content/*`

## Objective

Build the first content-facing APIs on top of `rs-cache`.

## Tasks

1. Expand top-level archive catalog usage
- use parsed archive containers instead of raw bytes where available
- expose lookup by known archive id/name

2. Implement first definition readers
- item definition index/data parity
- npc definition index/data parity
- object definition index/data parity
- map index parity

3. Add tests
- selected decoded ids match legacy-derived expectations
- fixtures should use the checked-in cache and legacy data files only

## Acceptance

- `./gradlew :rs-content:test` passes
- at least one selected item, npc, and object decode path exists
- content services do not depend directly on legacy client classes
