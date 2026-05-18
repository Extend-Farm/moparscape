---
name: moparscape-runtime-verifier
description: Use when verifying whether Moparscape changes actually work at runtime. Covers compile checks, emulator and desktop client launch flows, smoke testing login and sidebar bootstrap, and separating runtime regressions from known cache or map-loading issues.
---

# Moparscape Runtime Verifier

Use this skill when the task is to verify behavior, not to redesign code.

## Read first

- `../moparscape-maintainer/references/repo-map.md`
- `../moparscape-maintainer/references/build-and-run.md`
- `../moparscape-maintainer/references/startup-and-login.md`
- `../moparscape-maintainer/references/ui-and-tabs.md`
- `../moparscape-maintainer/references/known-quirks.md`

## Responsibility

- Confirm whether a reported issue reproduces.
- Confirm whether a fix compiles and changes observed behavior.
- Separate:
  - emulator-side failure
  - desktop client failure
  - expected known legacy issue

## Workflow

1. Compile only the affected module first.
2. If the change touched startup, login, tabs, or persistence bootstrap, restart the emulator before retesting.
3. Use the fresh Gradle client path, not the stale legacy jar.
4. Record exact repro steps, observed result, and whether the issue is:
   - fixed
   - still broken
   - replaced by a different failure
5. If blocked by a known separate issue such as black world/map loading, say so explicitly.

## Validation checklist

- Emulator changes:
  - `./gradlew :emulator:compileJava`
- Desktop client changes:
  - `./gradlew :game-client:compileJava`
- Packaging-sensitive desktop client changes:
  - `./gradlew :game-client:jar`

## Reporting style

- Lead with pass/fail.
- Include the exact command used.
- Include the first concrete blocker, not a vague summary.
