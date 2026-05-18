---
name: moparscape-task-updater
description: Use when maintaining task state, handoff notes, or progress tracking for Moparscape work. Covers updating active refactor notes in .cursor/plan, recording status, blockers, and next steps, and keeping the written task state aligned with the current code changes.
---

# Moparscape Task Updater

Use this skill when the task is to keep the work state current for humans or subagents.

## Read first

- `../moparscape-maintainer/references/repo-map.md`
- `../moparscape-maintainer/references/known-quirks.md`
- current files under `.cursor/plan/`

## Responsibility

- Keep progress notes current.
- Record what changed, what was verified, what remains blocked, and what should happen next.
- Make handoff state explicit enough that another agent can resume without rediscovery.

## Default targets

- `.cursor/plan/*.md` for task-specific status and refactor notes

## Update format

- objective
- current status
- completed work
- verification performed
- known blockers
- next recommended step

## Rules

- Update existing task files when possible instead of creating near-duplicates.
- Keep notes factual and current.
- Remove stale assumptions once disproven.
- If a tool-based plan exists in the active run, keep written task notes consistent with it.
- Do not turn task notes into a changelog dump; keep them useful for the next worker.
