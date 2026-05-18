# Java 21

## Goal

- Modernize toward clear Java 21 code without breaking legacy protocol, cache, or UI behavior.

## Core rule

- Prefer incremental improvement over sweeping rewrites.

## Use when writing new or extracted code

- Prefer `switch` expressions over long fallthrough `switch` blocks when behavior is local and obvious.
- Prefer pattern matching for `instanceof` when it removes redundant casts.
- Prefer `record` for small immutable value carriers such as parsed packet results, config values, or helper DTOs.
- Prefer `try-with-resources` for readers, streams, and sockets created in modernized code.
- Prefer `Path`, `Files`, and `StandardCharsets.UTF_8` over legacy `FileReader` and ad hoc path strings when changing file IO.
- Prefer interfaces in types such as `List`, `Map`, and `Set`, with concrete implementations chosen locally.
- Prefer early returns and small extracted methods over deeply nested control flow.

## Use with caution in this repo

- Do not mass-convert decompiled classes just to use modern syntax.
- Do not replace stable primitive arrays with collections in hot protocol or rendering paths unless there is a clear gain and local verification.
- Do not introduce streams into tight loops, packet parsing, or rendering code where they obscure control flow.
- Do not introduce `Optional` in fields, serialization-heavy structures, or hot legacy paths.
- Do not change public or packet-facing semantics while “just refactoring”.

## Refactor style for legacy classes

- Rename fields and methods in narrow thematic groups.
- Extract helpers around stable behavior boundaries:
  - login/bootstrap
  - window/input ownership
  - tab/sidebar setup
  - packet parsing helpers
- Keep protocol numbers and interface ids behind named constants when you touch them.
- Preserve existing behavior first, then simplify structure.

## Validation

- Compile after each refactor slice.
- If a change touches emulator login/bootstrap, restart the emulator before retesting.
- If a change touches client input or rendering, verify the visible component, focus target, and event target remain aligned.
