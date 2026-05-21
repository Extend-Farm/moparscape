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
- Ensure concise code comments or nearby docs exist where the reference-client behavior would otherwise be opaque.
- Require docs or handoff notes to stay current when refactors change structure, ownership, or runtime behavior.

## Rules

- Prefer small extractions over broad rewrites.
- Do not introduce new files or types with `Legacy` or `Reference` in the name.
- Prefer descriptive domain names for helpers and types instead of compatibility labels.
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
  - pre-sized collections when the count is already known
  - direct primitive-array flow in render/animation code when it avoids temporary wrappers
  - direct writes into pre-sized primitive buffers when final geometry counts are already known
  - enforcing invariants at record constructors, factories, or preparation boundaries instead of adding silent hot-path fallbacks
  - bounds-safe helper methods when consuming decoded or partially-available animation/render data
- Be conservative with:
  - streams in hot paths
  - collection rewrites in rendering or packet code
  - semantic cleanup mixed into behavioral changes
  - identity/default fallbacks that can hide broken prepared-model or decode invariants
- In multi-agent work, keep file ownership narrow, record assumptions and follow-up boundaries, and do not overwrite or revert adjacent work you did not author.

## Review focus

- behavior regressions
- hidden coupling between emulator and desktop client
- stale remote-config dependencies
- mismatched render/input surfaces
- undocumented compatibility assumptions
- avoidable allocations in hot render, animation, and cache-decode paths
- silent fallback behavior that turns invalid prepared-model state into harder-to-debug render output

## Character/render notes

- In character and scene render assembly, prefer explicit semantic names such as `appendLitFaces` when the method is copying a specific compatibility state, not generic geometry.
- When a helper consumes animation contributions or decode output, do not assume perfect list alignment unless the invariant is enforced earlier. Prefer local bounds checks or constructor-time guarantees.
- If an identity transform or other neutral default is not part of the real model contract, enforce non-null invariants at prepared-model creation instead of inventing a fallback deeper in the hot path.

## Validation

- Compile after each slice.
- If documentation or comments were added, confirm they still match the code after the final edit.
- If a refactor touched shared architecture or workflow, confirm the corresponding skill/reference/plan doc was updated in the same slice.
