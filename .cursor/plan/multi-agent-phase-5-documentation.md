# Multi-Agent Phase 5 Documentation

## Purpose

Coordinate the documentation pass for the native client/runtime while the
world-render pipeline is still mid-migration.

This note is not a one-time cleanup pass. It is the standing documentation
discipline companion to
[multi-agent-phase-5-execution.md](/home/akira/projects/moparscape/.cursor/plan/multi-agent-phase-5-execution.md:1).
Every implementation slice in phase 5 must update documentation alongside code.

## Scope split

### Mainline

- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/OpenGlTileRenderSystem.java`
- `.cursor/plan/multi-agent-phase-5-documentation.md`

Mainline responsibility:

- annotate the renderer at the architectural boundaries
- document what is native, what is cache-backed, and what is still provisional
- ensure new runtime shortcuts are described as shortcuts, not parity

### Worker 1

- `docs/modern-rsps-architecture.md`
- `docs/native-world-pipeline.md`
- optional wording alignment in `phase-5-native-3d-world-parity-plan.md`

Responsibility:

- explain the native world pipeline target
- document the scene-preview vs full-scene-parity boundary
- document the next integration boundary around scene submission, unified render queue ownership, and raster parity
- update stable architecture docs when slice boundaries change

### Worker 2

- `README.md`
- `.codex/skills/moparscape-maintainer/references/known-quirks.md`
- `.codex/skills/moparscape-maintainer/references/repo-map.md`
- `.codex/skills/moparscape-maintainer/references/build-and-run.md`

Responsibility:

- keep repo-level instructions honest
- reflect current native-client capabilities and limitations
- update user-facing docs whenever build/run/runtime expectations change

### Worker 3

- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/LwjglClientMain.java`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/CacheBackedWorldSceneLoader.java`

Responsibility:

- add focused inline documentation for bootstrap, persistence wiring, and
  cache-backed scene construction
- leave enough local guidance that future refactors do not have to rediscover
  ownership or temporary boundaries
- when scene submission/raster work lands, document where viewport orchestration stops and queue/raster ownership begins

## Required Documentation Output Per Slice

Every completed slice should leave:

- a task-state update under `.cursor/plan/` if slice status, scope, or ownership changed
- stable architecture updates in `docs/` when contracts changed
- inline code comments only where file format, ownership, or temporary scaffolding would otherwise be rediscovered
- verification notes that say what actually ran and what did not

## Expected outcome

After this pass:

- the repo docs should describe the native client as a real rewrite with clear
  current boundaries, not as a parity-complete RuneScape client
- the most complex native LWJGL files should have enough inline guidance that
  future refactors do not need to rediscover the current architecture
- future workers should have an explicit expectation that documentation ships
  with implementation, not after it
