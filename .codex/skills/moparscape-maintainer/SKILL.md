---
name: moparscape-maintainer
description: Use when working in the Moparscape repo on emulator or desktop client startup, login, sidebar tabs, local build or run flows, persistence decisions, Java 21 modernization, or refactors touching GameClientCore, GameShell, GameFrame, moparscape-reference/client/server.java, moparscape-reference/client/client.java, or PlayerHandler. Covers repo-specific architecture, remote-config traps, safe modernization workflow, and Java 21 best practices for this codebase.
---

# Moparscape Maintainer

Use this skill for repo-specific work in `moparscape`.

## Quick start

- Read `references/repo-map.md` first.
- For build or launch issues, read `references/build-and-run.md`.
- For login, startup, or input problems, read `references/startup-and-login.md`.
- For missing tabs or sidebar issues, read `references/ui-and-tabs.md`.
- For recurring traps and failures, read `references/known-quirks.md`.
- For storage or migration questions, read `references/persistence.md`.
- For modernization standards and Java style, read `references/java-21.md`.

## Working rules

- Treat `moparscape-reference/client/` and `moparscape-reference/server/moparscape/` as separate codebases sharing a brittle protocol.
- Prefer small refactors with compile checks after each slice.
- Keep documentation current in the same slice when a refactor changes structure, workflow, module boundaries, run commands, visible limitations, or ownership expectations.
- Use Java 21 features when they improve clarity, but do not force broad mechanical rewrites through fragile decompiled code.
- When touching client startup or input ownership, verify the visible surface and the event surface are the same component.
- When touching login, tabs, or bootstrap packets, trace both emulator and desktop client before changing semantics.
- Prefer local defaults over dead remote HybridScape config endpoints.
- Use the fresh Gradle outputs, not old checked-in jars.
- When naming code, use descriptive domain names instead of `Reference` or `Legacy`.
- When describing behavior from the existing client, call it the reference client or reference behavior, not legacy behavior.
- In native render/animation/model code, enforce invariants at prepared-model or factory boundaries instead of burying neutral fallbacks in inner loops.
- In hot geometry assembly paths, prefer pre-sized primitive arrays and direct writes over temporary per-vertex or per-face object graphs when the output shape is already known.
- When multiple agents or collaborators are active, keep scopes narrow, reread the latest tree before editing shared files, record handoff/status notes in `.cursor/plan/` when the work spans slices, and never revert another contributor's changes.

## Validation

- Compile emulator changes with `./gradlew :emulator:compileJava`.
- Compile desktop client changes with `./gradlew :game-client:compileJava`.
- When packaging the desktop client, use `./gradlew :game-client:jar`.
- After server-side login/bootstrap changes, do a full emulator restart before retesting.
- After a refactor, verify the touched docs still match the code, commands, and current task split.
