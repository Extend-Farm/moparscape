# Multi-Agent Orchestrator: Phase 3

## Goal

Unwind legacy runtime coupling and move the new stack back onto a native gameplay path.

## Shared Rule

No agent may introduce or preserve a production dependency from any `rs-*` module to `:game-client` or `:emulator`.

## Agent Tracks

### Agent 1: Boundary Cleanup

- Owns:
  - `rs-client-lwjgl/build.gradle.kts`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/LwjglClientMain.java`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/OpenGlTileRenderSystem.java`
- Focus:
  - remove embedded legacy runtime path
  - restore a native post-login state machine
  - eliminate production dependency on `:game-client`

### Agent 2: Title Screen Native Asset Path

- Owns:
  - `rs-cache/src/main/java/io/github/ffakira/rsps/cache/*`
  - `rs-content/src/main/java/io/github/ffakira/rsps/content/*`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/TitleScreen*`
- Focus:
  - native title archive readers
  - native sprite/font decoding for the login screen
  - removal of legacy rendering classes from title screen code

### Agent 3: Native Auth And Session

- Owns:
  - `rs-protocol/src/main/java/io/github/ffakira/rsps/protocol/*`
  - `rs-server-runtime/src/main/java/io/github/ffakira/rsps/server/runtime/*`
  - `rs-client-core/src/main/java/io/github/ffakira/rsps/client/core/*`
  - `rs-persistence-api/src/main/java/io/github/ffakira/rsps/persistence/*`
- Focus:
  - native login handshake
  - auth/session state progression
  - login success/failure/loading view-model contract

### Agent 4: Native World Bootstrap

- Owns:
  - `rs-content/src/main/java/io/github/ffakira/rsps/content/*`
  - `rs-client-core/src/main/java/io/github/ffakira/rsps/client/core/*`
  - `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/*`
  - `rs-sim-ecs/src/main/java/io/github/ffakira/rsps/sim/*`
- Focus:
  - region bootstrap data
  - scene/view model for world loading
  - native world rendering and player bootstrap

## Ordering Constraints

- Agent 1 must finish before any new gameplay code lands in `rs-client-lwjgl`.
- Agent 2 can run in parallel with Agent 3 once Agent 1 removes the legacy runtime bridge.
- Agent 4 depends on the outputs of Agent 2 and Agent 3.

## Acceptance Gate Before Advancing

- `rs-client-lwjgl` has no production dependency on `:game-client`
- production `rs-*` code has no import of `io.github.ffakira.moparscape.*`
- title/login remains functional after removing the legacy runtime bridge
