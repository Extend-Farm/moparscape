---
name: moparscape-quality-guard
description: Use when reviewing or improving Moparscape code quality during refactors. Covers Java 21 modernization standards, narrow-scope refactors, naming, extraction boundaries, compile-first validation, and ensuring concise code comments or documentation are added where they meaningfully reduce ambiguity.
---

# Moparscape Quality Guard

Use this skill when the task is code quality, review, or cleanup around an active refactor.

## Read first

- `../moparscape-maintainer/references/java-21.md`
- `../moparscape-maintainer/references/repo-map.md`
- `../moparscape-maintainer/references/known-quirks.md`

## Responsibility

- Guard behavior during refactors.
- Push code toward clearer Java 21 style where safe.
- Ensure concise code comments or nearby docs exist where the legacy behavior would otherwise be opaque.
- Require docs or handoff notes to stay current when refactors change structure, ownership, or runtime behavior.

## Rules

- Prefer small extractions over broad rewrites.
- Do not introduce new files or types with `Legacy` in the name; describe compatibility or reference-client behavior in comments/docs instead.
- Keep protocol numbers, interface ids, and magic states behind named constants when touching them.
- Add comments only for non-obvious behavior boundaries or compatibility constraints.
- Do not add comment noise for self-explanatory code.
- Do not let a refactor land with stale workflow or architecture docs when the public behavior or file boundaries changed.
- Favor:
  - extracted helper methods
  - early returns
  - better names
  - `record` for small immutable carriers
  - `try-with-resources`
- Be conservative with:
  - streams in hot paths
  - collection rewrites in rendering or packet code
  - semantic cleanup mixed into behavioral changes
- In multi-agent work, keep file ownership narrow, record assumptions and follow-up boundaries, and do not overwrite or revert adjacent work you did not author.

## Review focus

- behavior regressions
- hidden coupling between emulator and desktop client
- stale remote-config dependencies
- mismatched render/input surfaces
- undocumented compatibility assumptions

## Validation

- Compile after each slice.
- If documentation or comments were added, confirm they still match the code after the final edit.
- If a refactor touched shared architecture or workflow, confirm the corresponding skill/reference/plan doc was updated in the same slice.
