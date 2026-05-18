# GameClientCore Method Extraction Plan

## Objective

Extract the remaining legacy methods from `GameClientCore` into focused handler classes while keeping behavior exactly the same.

Primary target:
- reduce `GameClientCore` from a logic-heavy class into a routing/orchestration class

## Code Formatting Rules (Required)

- Use opening brace on the same line:

```java
public GameClientCore() {
    // ...
}
```

- Use 2-space indentation.
- Keep existing method signatures and call order unless the extraction requires argument forwarding.
- Add one concise extraction comment per extracted method:
  - `// Exact extraction of legacy methodXXX ...`

## Extraction Strategy

1. **Exact extraction only**
   - Move method body as-is into a target handler.
   - Do not change logic in the same commit.
   - Replace original body with a delegation call.

2. **Small, safe batches**
   - 5-15 methods per batch, or one large method split.
   - Compile after every batch.

3. **One concern per handler**
   - Keep destination classes focused by subsystem.

## Current Destination Classes

- `IncomingPacketDispatcher` - packet decoding/read helpers.
- `SocialPacketHandler` - social/friend/chat request packet logic.
- `WidgetRenderHandler` - widget render branches.
- `WidgetContainerHandler` - widget type 0 recursion/scroll.
- `PlayerUpdateMaskHandler` - player update mask branches.
- `NpcUpdateMaskHandler` - npc update mask branches.
- `GameFrameHandler` - frame redraw logic.
- `SceneEffectHandler` - graphics/scene effects tick logic.

## Next Extraction Order

### Phase 1: Finish `method105`
- Extract remaining **type 2** inventory-grid branch exactly.
- Verify `method105` is only dispatch + guard logic.

### Phase 2: Finish `method102`
- Continue exact block extraction from `method102` into `GameFrameHandler`.
- Keep redraw flag semantics unchanged.

### Phase 3: Packet Pipeline
- Continue extracting grouped opcodes from `method145` to:
  - `IncomingPacketDispatcher`
  - `SocialPacketHandler`

### Phase 4: Scene/Camera and Update Loops
- Move remaining scene/camera update methods into focused handlers.

### Phase 5: Core Cleanup
- Remove obsolete wrappers only after all call sites are stable.
- Keep `GameClientCore` as:
  - lifecycle
  - routing
  - handler orchestration

## Method Tracking Template

Use this structure for each extraction batch:

| Method | Source | Destination | Status | Verified |
|---|---|---|---|---|
| method105 (type2) | GameClientCore | WidgetRenderHandler | todo | no |
| method102 block N | GameClientCore | GameFrameHandler | todo | no |

Statuses:
- `todo`
- `in_progress`
- `moved`
- `verified`

## Verification Checklist (Every Batch)

- `./gradlew :game-client:compileJava`
- No new lint errors in changed files
- Basic runtime smoke checks:
  - widgets render correctly
  - chat/social messages still route
  - scene/frame redraw still works

## Commit Convention

- Keep commits small and descriptive.
- Preferred style:
  - `Extract methodXXX <branch/section> into <HandlerName>.`
