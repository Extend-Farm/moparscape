---
name: moparscape-doc-updater
description: Use when updating Moparscape repository documentation or repo-local knowledge files. Covers keeping README, repo-local skill references, run/build instructions, known-quirks notes, and refactor docs in sync with the actual code and current workflow.
---

# Moparscape Doc Updater

Use this skill when the task is to update documentation after code or workflow changes.

## Read first

- `../moparscape-maintainer/references/repo-map.md`
- `../moparscape-maintainer/references/build-and-run.md`
- `../moparscape-maintainer/references/known-quirks.md`
- `../moparscape-maintainer/references/java-21.md`

## Responsibility

- Keep repo docs aligned with reality.
- Prefer updating existing docs over creating duplicates.
- Capture stable repo knowledge, not temporary debugging chatter.
- Treat documentation as part of the refactor, not a follow-up task.

## Primary documentation targets

- `README.md`
- `.codex/skills/moparscape-maintainer/SKILL.md`
- `.codex/skills/moparscape-maintainer/references/*.md`
- `.cursor/plan/*.md` when the change is task-specific rather than repo-wide

## Rules

- Do not create overlapping docs for the same topic.
- Put stable repo knowledge in `.codex/skills/moparscape-maintainer/references/`.
- Put active refactor or execution notes in `.cursor/plan/`.
- Update commands, paths, module names, and known limitations when they change.
- Remove stale instructions when superseded.
- When a refactor changes architecture, ownership, or workflow, update the relevant doc in the same change.
- In multi-agent work, prefer additive updates over rewrites, preserve other contributors' notes unless replacing them with newer confirmed state, and make task ownership/status explicit.

## Validation

- Verify commands in docs still match current Gradle module names and paths.
- Verify any referenced file paths still exist.
- Keep docs concise and operational.
- Verify the docs describe the current refactor boundary, not an intended future state.
