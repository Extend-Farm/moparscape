# GameClientCore Field Groups (11408-11904)

This document groups the large legacy field block into subsystem-oriented buckets for safe, incremental renaming.
No field merges/removals are proposed in this batch.

## Grouping Rules
- Preserve behavior and storage layout; rename only.
- Prefer subsystem prefixes (`ui`, `chat`, `scene`, `network`, `input`) for new names.
- Mark uncertain mappings with `@TODO`.

## UI Buffers and UI Assets
- `aClass15_903..aClass15_911`, `aClass15_1107..aClass15_1115`, `aClass15_1123..aClass15_1125`, `aClass15_1163..aClass15_1166`
  - Proposed: `uiBuffer*` (high confidence)
- `aClass30_Sub2_Sub1_Sub2_865..869`, `aClass30_Sub2_Sub1_Sub2_966..967`, `aClass30_Sub2_Sub1_Sub2_1024..1029`, `aClass30_Sub2_Sub1_Sub2_1143..1147`, `aClass30_Sub2_Sub1_Sub2_1196..1198`
  - Proposed: `uiIndexedSprite*` or subsystem-specific names after usage mapping (medium confidence)
- `aClass30_Sub2_Sub1_Sub1_870..871`, `aClass30_Sub2_Sub1_Sub1_931..932`, `aClass30_Sub2_Sub1_Sub1_1001`, `aClass30_Sub2_Sub1_Sub1_1074..1078`, `aClass30_Sub2_Sub1_Sub1_1122`, `aClass30_Sub2_Sub1_Sub1_1201..1202`, arrays `aClass30_Sub2_Sub1_Sub1Array*`
  - Proposed: `uiSprite*` (medium confidence)

## Chat and Social State
- `aLongArray925` ignore hashes, `anInt822` ignore count
  - Proposed: `ignoredNameHashes`, `ignoreCount` (high confidence)
- `aLongArray955`, `aStringArray1082`, `anIntArray826`, `anInt899`
  - Proposed: `friendNameHashes`, `friendDisplayNames`, `friendWorlds`, `friendCount` (high confidence)
- `anIntArray942`, `aStringArray943`, `aStringArray944`
  - Proposed: `chatTypes`, `chatSenders`, `chatMessages` (high confidence)
- `anInt1169`, `anIntArray1240`
  - Proposed: `recentChatWriteIndex`, `recentChatIds` (high confidence)

## Widget/Input/Drag Interaction
- `anInt1084`, `anInt1085`, `anInt1086`, `anInt1087`, `anInt1088`
  - Proposed: `dragWidgetId`, `dragSlot`, `dragState`, `dragStartMouseX`, `dragStartMouseY` (high confidence)
- `anInt1244`, `anInt1245`, `anInt1246`
  - Proposed: `selectedDragWidgetId`, `selectedDragSlot`, `selectedDragState` (high confidence)
- `anInt1282`, `anInt1283`, `anInt1284`
  - Proposed: `itemUseState`, `itemUseSlot`, `itemUseWidgetId` (high confidence)
- `anInt1026`, `anInt1039`, `anInt1048`
  - Proposed: `focusedWidgetIdPrimary`, `focusedWidgetIdSecondary`, `focusedWidgetIdTertiary` (medium confidence)

## Scene/World/Region
- `aClass25_946`
  - Proposed: `sceneGraph` (high confidence)
- `anInt918`, `anInt1268`, `anInt1269`
  - Proposed: `currentPlane`, `sceneBaseY`, `sceneBaseX` (high confidence)
- `anIntArrayArrayArray1214`, `anIntArrayArrayArray1129`, `aByteArrayArrayArray1258`
  - Proposed: `tileHeights`, `tileOverlayIds@TODO`, `tileRenderFlags` (medium confidence)
- `aClass11Array1230`
  - Proposed: `collisionMaps` (high confidence)

## Network/Session and Buffers
- `aClass24_1168`
  - Proposed: `gameConnection` (high confidence)
- `aClass30_Sub2_Sub2_1083`, `aClass30_Sub2_Sub2_1192`, `aClass30_Sub2_Sub2_834`, `aClass30_Sub2_Sub2_847`
  - Proposed: `incomingPacketBuffer`, `outgoingPacketBuffer`, `playerUpdateBuffer`, `chatDecodeBuffer@TODO` (medium confidence)
- `aClass30_Sub2_Sub2Array895`
  - Proposed: `playerAppearanceBuffers` (high confidence)
- `aClass17_1000`
  - Proposed: `networkCipher` (high confidence)

## Timing/Cycle and Camera
- `anInt1161`
  - Proposed: `gameTick` (high confidence)
- `anInt945`
  - Proposed: `gameTickDelta` (medium confidence)
- `anInt1184`, `anInt1185`, `anInt1186`, `anInt1187`
  - Proposed: `cameraPitch`, `cameraYaw`, `cameraYawVelocity`, `cameraPitchVelocity` (high confidence)

## Duplicates and Merge Candidates (Deferred)
- Multiple `ProducingGraphicsBuffer` fields appear duplicate by type but are likely separate UI layers.
- Multiple sprite/indexed-sprite arrays are likely distinct asset banks.
- Action for later phase: verify identical lifecycle + write sites before considering consolidation.

## Next Rename Pass Suggestion
1. Apply high-confidence chat/social and drag/input renames first.
2. Apply scene/world high-confidence renames.
3. Rename buffer/sprite families only after per-field usage mapping.
