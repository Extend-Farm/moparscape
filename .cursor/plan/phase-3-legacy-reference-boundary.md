# Phase 3: Legacy Reference Boundary And Native Gameplay Path

## Objective

Reset the rewrite to the architecture we actually want:

- `:emulator` and `:game-client` are reference-only
- production `rs-*` modules do not import or execute legacy runtime code
- new login, asset loading, scene bootstrap, and gameplay all come from `rs-*` modules only

This phase exists because the current tree drifted into the wrong direction by embedding legacy client runtime inside the new LWJGL client.

## Non-Negotiable Rules

1. No production dependency from any `rs-*` module to `:game-client` or `:emulator`.
2. No production import from `io.github.ffakira.moparscape.*` or legacy default-package emulator classes inside `rs-*`.
3. Legacy modules may be used only for:
   - reference executables
   - parity tests
   - golden fixture extraction
   - format/protocol/layout documentation
4. If a new feature in `rs-*` requires legacy code at runtime, that feature is not ready and must stop at a documented stub or fallback.

## Current Violations To Remove

### Direct runtime coupling

- `rs-client-lwjgl/build.gradle.kts`
  - `implementation(project(":game-client"))`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/LwjglClientMain.java`
  - imports `GameClient`
  - imports `EmbeddedGameClientSession`
  - imports `LegacyFrameSnapshot`
  - imports `LegacyLoginStatusSnapshot`

### Title screen still using legacy client classes

- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/TitleScreenAssetLoader.java`
  - imports legacy `Archive`, `Sprite`, `IndexedSprite`, `FontRenderer`, `Rasterizer2D`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/TitleScreenTextRenderer.java`
  - imports legacy `FontRenderer`, `Rasterizer2D`
- `rs-client-lwjgl/src/main/java/io/github/ffakira/rsps/client/lwjgl/TitleScreenFonts.java`
  - imports legacy `FontRenderer`

### Acceptable legacy usage that may remain

- `rs-cache` parity tests depending on `:game-client`
- fixture extraction code under test-only paths

## Desired End State For This Phase

At the end of this phase:

- `./gradlew :rs-client-lwjgl:dependencies` shows no production dependency on `:game-client`
- `rg "io\\.github\\.ffakira\\.moparscape|EmbeddedGameClient|Legacy(Login|Frame)" rs-* -g '!**/test/**'` returns no production hits
- the new title/login path runs entirely from new modules
- post-login flow does not switch to or embed the legacy client
- if world gameplay is not yet ready, the client remains on an honest native loading or unavailable screen rather than handing off to legacy

## Workstreams

## Workstream 1: Containment And Rollback

### Goal

Remove the runtime bridge to legacy immediately.

### Deliverables

- remove `implementation(project(":game-client"))` from `:rs-client-lwjgl`
- remove embedded-client launch path from `LwjglClientMain`
- remove embedded framebuffer render path from `OpenGlTileRenderSystem`
- return post-login behavior to a native `rs-*` state machine:
  - `connecting`
  - `authenticating`
  - `loading assets`
  - `world unavailable`
  - or native world once ready

### Acceptance

- `:rs-client-lwjgl:compileJava` passes without `:game-client` in production classpath
- no legacy runtime imports remain in `rs-client-lwjgl` production code

## Workstream 2: Native Title Screen Asset Pipeline

### Goal

Stop using legacy rendering/data classes even for the login screen.

### Deliverables

- decode title archive through `rs-cache`
- implement native readers in `rs-content` or `rs-client-lwjgl` for:
  - title background image data
  - logo/title box/title button sprites
  - title fonts needed for login screens
  - rune masks / flame assets if kept
- replace legacy `FontRenderer`/`Sprite`/`IndexedSprite` usage with native equivalents

### Acceptance

- `TitleScreenAssetLoader`, `TitleScreenTextRenderer`, and related classes no longer import legacy packages
- title/login screen remains visually close to legacy while running fully through `rs-*`

## Workstream 3: Native Login/Auth Contract

### Goal

Make the visible login the only login.

### Deliverables

- define login handshake messages in `:rs-protocol`
- implement account authentication flow in `:rs-server-runtime`
- connect `:rs-client-core` login state to new protocol session
- surface success/failure/loading states through native view-models only

### Acceptance

- the new client authenticates against `:rs-server-runtime`
- there is no second hidden or visible legacy login stage

## Workstream 4: Native World Bootstrap

### Goal

Reach gameplay through new world data and new rendering, not through the old client.

### Deliverables

- region bootstrap data model in `:rs-content`
- native world scene loader in `:rs-client-core`
- native world renderer in `:rs-client-lwjgl`
- player spawn/update flow from `:rs-server-runtime` to `:rs-client-core`

### Acceptance

- after successful native login, client reaches a native world scene
- player appears in-world and can receive authoritative movement/state updates

## Workstream 5: Guardrails

### Goal

Prevent this architectural drift from returning.

### Deliverables

- build check or test that fails on production `rs-* -> legacy` dependencies
- architecture rule documenting allowed test-only parity usage
- repo docs updated to say legacy is reference-only

### Acceptance

- future accidental imports are caught by CI or test failures

## Sequencing

1. Containment And Rollback
2. Guardrails
3. Native Title Screen Asset Pipeline
4. Native Login/Auth Contract
5. Native World Bootstrap

Do not start new gameplay work until step 1 is complete.

## First Playable Milestone After This Reset

The first acceptable gameplay milestone is:

- new title/login screen
- native authentication against `:rs-server-runtime`
- native loading transition
- native world render
- authoritative player movement

Legacy runtime is not on the hot path at any point.

## Explicit Non-Goals For This Phase

- no embedded legacy frame
- no hidden legacy login
- no legacy asset renderer inside `rs-client-lwjgl`
- no attempt to reach “gameplay” by launching or wrapping `:game-client`
