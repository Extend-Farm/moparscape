# Source Layout Cleanup

## Objective

Normalize the desktop client project structure and remove checked-in compiled artifacts without changing runtime behavior.

## Current Status

Status: `completed`

## Completed Work

1. Removed checked-in compiled `.class` artifacts from the emulator source tree and the stale desktop-client `sign/signlink.class`.
2. Added `*.class` ignore coverage in the repo root `.gitignore`.
3. Moved desktop client Java sources into a standard Gradle source tree:
   - `server/moparscape/src/main/java/io/github/ffakira/rsps/client/desktop/...`
4. Switched the desktop client module back to Gradle’s standard Java source layout by removing the custom root-level `sourceSets` override from `server/moparscape/build.gradle.kts`.

## Verification

Verified on May 16, 2026:

```bash
./gradlew :game-client:clean :game-client:compileJava
./gradlew :emulator:compileJava
```

Results:
- `:game-client:clean :game-client:compileJava` `BUILD SUCCESSFUL`
- `:emulator:compileJava` `BUILD SUCCESSFUL`

## Known Follow-up

1. The emulator is still physically flat under `client/` and still uses the default package.
2. Emulator package migration should be a separate refactor from this layout cleanup.
3. Binary leftovers such as `server/moparscape/moparclient.jar` were not removed in this slice.

## Next Recommended Step

1. Keep the emulator build flat for now.
2. Continue refactoring bootstrap/login/input/world slices on top of the normalized desktop client tree rooted at `io.github.ffakira.rsps.client.desktop`.
3. Treat the world-package split as complete for the current native client surface: `world.minimap`, `world.terrain`, `world.object`, `world.visibility`, and `world.raster` are now explicit ownership boundaries.
4. Plan deeper parity work inside those packages before doing more package churn.
5. Plan a separate emulator packaging migration once the runtime behavior is more stable.
