# Multi-Agent Workflow

This document defines how to execute complex `rs-*` work in parallel without losing architectural clarity or documentation quality.

## Rule

- legacy modules are reference-only
- `rs-*` production code stays native
- every non-trivial slice must leave behind current documentation, not just code

## When to Split Work

Use multiple agents when a slice has clearly separable workstreams such as:

- cache/content decoding
- server/runtime bootstrap
- client rendering/UI
- persistence/schema
- documentation/task-state updates

Do not split work that shares the same write scope or where the parent thread is blocked on the result immediately.

## Ownership Model

Every delegated task must have explicit ownership.

- One agent owns one write scope.
- Shared files should stay with the parent thread unless the ownership boundary is unambiguous.
- Documentation ownership should be explicit too.

Typical split:

- code worker: one package or module slice
- doc worker: `docs/` or skill/reference updates
- task-state worker: `.cursor/plan/`
- explorer: read-only audit, parity findings, or legacy reference extraction

## Documentation Requirement

Every meaningful slice must update at least one of these surfaces when applicable:

- inline code comments for non-obvious compatibility boundaries
- `docs/*.md` for stable architecture or workflow knowledge
- `.codex/skills/moparscape-maintainer/references/*.md` for reusable repo knowledge
- `.cursor/plan/*.md` for active execution status, blockers, and next steps
- `README.md` when run modes, module boundaries, or user-facing workflow changes

Do not treat documentation as a follow-up task. It ships with the slice.

## Minimum Handoff

Each workstream should leave:

- objective
- files changed
- verification performed
- known boundary or limitation
- next recommended step

If the work is exploratory only, record the findings and the concrete implication for implementation order.

## Verification Discipline

- compile the smallest affected Gradle target first
- run broader tests only after the slice compiles
- if docs changed, verify the commands, paths, and module names still exist
- if comments were added, ensure they still match the final code after refactors settle

## Native World Parity Work

For the current world-parity effort, the default workstream split is:

- cache/content decode
- scene assembly/runtime bootstrap
- client rendering/UI shell
- documentation/task-state maintenance

That split keeps the temporary terrain preview, future object/model scene pipeline, and parity notes from collapsing back into god classes or stale plan files.
