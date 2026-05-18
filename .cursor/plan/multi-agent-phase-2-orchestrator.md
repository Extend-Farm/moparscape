# Multi-Agent Orchestrator: Phase 2

## Goal

Coordinate the next rewrite phase around cache/content parity without conflicting edits.

## Agent Tracks

### Agent 1: Cache Store And Container Core

- Owns: `rs-cache/src/main/java/io/github/ffakira/rsps/cache/*`
- Focus:
  - sector/index reader hardening
  - archive container parsing
  - decompression support
  - low-level parity tests

### Agent 2: Content Definition Parity

- Owns: `rs-content/src/main/java/io/github/ffakira/rsps/content/*`
- Focus:
  - top-level archive services
  - selected item/npc/object/map index decoders
  - content-level parity tests

### Agent 3: Server Auth And Session Skeleton

- Owns:
  - `rs-server-runtime/src/main/java/io/github/ffakira/rsps/server/runtime/*`
  - `rs-persistence-api/src/main/java/io/github/ffakira/rsps/persistence/*`
  - `rs-persistence-sql/src/main/java/io/github/ffakira/rsps/persistence/sql/*`
  - `rs-protocol/src/main/java/io/github/ffakira/rsps/protocol/*`
- Focus:
  - login/session actor boundaries
  - auth flow contracts
  - stored account/character loading

### Agent 4: Client Login And World Bootstrap

- Owns:
  - `rs-client-core/src/main/java/io/github/ffakira/rsps/client/core/*`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/*`
- Focus:
  - login state progression
  - world bootstrap events
  - shell rendering of connection/login/world status

## Ordering Constraints

- Agent 1 unlocks Agent 2.
- Agent 3 and Agent 4 can proceed in parallel once protocol/login contracts are stable enough.
- Do not change legacy `:emulator` or `:game-client` except for parity-test support or bug fixes.

## Shared Rules

- Preserve Java 21 conventions in new code.
- Do not introduce dependencies from new `rs-*` modules back into legacy runtime modules, except test-only parity harnesses.
- Compile/test after each slice.
- Do not revert unrelated repo changes.
