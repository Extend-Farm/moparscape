# Moparscape Modernization Notes

This repository contains two Java codebases:

- `client/` -> emulator/server logic
- `server/moparscape/` -> legacy game client (decompiled/obfuscated lineage)

## Build

This project is now a Gradle multi-module build.

- Compile everything:
  - `./gradlew compileJava`
- Compile emulator only:
  - `./gradlew :emulator:compileJava`
- Compile game client only:
  - `./gradlew :game-client:compileJava`

## Recent modernization changes

- Added Gradle wrapper and multi-project setup.
- Removed checked-in legacy `.class` binaries from `server/moparscape/`.
- Restored missing source for `Class7` and then renamed key classes in phases.

## Rename progress (server/moparscape)

### Phase 1 (runtime/bootstrap)

- `client` -> `GameClient`
- `Applet_Sub1` -> `GameShell`
- `Frame_Sub1` -> `GameFrame`
- `sign/signlink` -> `sign/SignLink`
- `Class42` -> `ResourceProvider`
- `Class42_Sub1` -> `OnDemandFetcher`
- `Class44` -> `Archive`
- `Class30_Sub2_Sub2` -> `PacketBuffer`
- `Class17` -> `IsaacCipher`
- `Class14` -> `CacheFileStore`
- `Class30_Sub2_Sub3` -> `OnDemandRequest`
- `Class15` -> `ProducingGraphicsBuffer`

### Phase 2 (scene/map/object)

- `Class7` -> `MapRegion`
- `Class11` -> `CollisionMap`
- `Class22` -> `FloorDefinition`
- `Class25` -> `SceneGraph`
- `Class46` -> `ObjectDefinition`
- `Class30_Sub2_Sub4_Sub5` -> `DynamicObject`
- `Class30_Sub2_Sub4_Sub6` -> `Model`

### Phase 3 (core containers)

- `Class30` -> `Node`
- `Class30_Sub2` -> `CacheableNode`
- `Class19` -> `Deque`
- `Class2` -> `CacheableNodeDeque`

### Phase 4 (utilities/infrastructure)

- `Class1` -> `NodeHashTable`
- `Class12` -> `NodeCache`
- `Class13` -> `BZip2Decompressor`
- `Class32` -> `BZip2State`
- `Class24` -> `BufferedConnection`
- `Class45` -> `Skills`
- `Class50` -> `TextUtils`

### Phase 5 (rendering primitives)

- `Class30_Sub2_Sub1` -> `Rasterizer2D`
- `Class30_Sub2_Sub1_Sub1` -> `Sprite`
- `Class30_Sub2_Sub1_Sub2` -> `IndexedSprite`
- `Class30_Sub2_Sub1_Sub3` -> `Rasterizer3D`
- `Class30_Sub2_Sub1_Sub4` -> `FontRenderer`

### Phase 6 (scene internals and animation defs)

- `Class20` -> `SequenceDefinition`
- `Class30_Sub3` -> `SceneTile`
- `Class43` -> `SceneTilePaint`
- `Class40` -> `SceneTileModel`
- `Class28` -> `InteractiveObject`
- `Class10` -> `WallObject`
- `Class26` -> `WallDecoration`
- `Class49` -> `GroundDecoration`
- `Class3` -> `GroundItemPile`
- `Class47` -> `Occluder`

### Phase 7 (definitions and animation metadata)

- `Class23` -> `SpotAnimationDefinition`
- `Class36` -> `AnimationFrame`
- `Class18` -> `AnimationSkeleton`
- `Class21` -> `ModelHeader`
- `Class37` -> `VarBitDefinition`
- `Class41` -> `VarpDefinition`
- `Class38` -> `IdentityKitDefinition`
- `Class33` -> `VertexNormal`

### Phase 8 (entities, renderables, and UI defs)

- `Class30_Sub2_Sub4` -> `Renderable`
- `Class30_Sub2_Sub4_Sub1` -> `Actor`
- `Class30_Sub2_Sub4_Sub1_Sub1` -> `Npc`
- `Class30_Sub2_Sub4_Sub1_Sub2` -> `Player`
- `Class30_Sub2_Sub4_Sub3` -> `GraphicsObject`
- `Class30_Sub2_Sub4_Sub4` -> `Projectile`
- `Class8` -> `ItemDefinition`
- `Class5` -> `NpcDefinition`
- `Class9` -> `Widget`
- `Class48` -> `MouseRecorder`

### Phase 9 (remaining classes and candidate naming)

- `Class4` -> `TileRotationUtils`
- `Class6` -> `SoundSynthesizer`
- `Class16` -> `SoundEffect`
- `Class29` -> `SoundEnvelope`
- `Class31` -> `PacketSizeTable`
- `Class34` -> `ChatCensor`
- `Class35` -> `ChatMessageCodec`
- `Class39` -> `SoundFilter`
- `Class30_Sub1` -> `SceneObjectSpawnRequest`
- `Class30_Sub2_Sub4_Sub2` -> `GroundItem`
- `Class27` -> `UnusedClientFlagsCandidate` (low-confidence placeholder by design)

### Phase 10 (core container API cleanup)

Refined high-traffic intrusive list/hash APIs for readability:

- `Node`: `method329` -> `unlink`, fields `aLong548/aClass30_549/aClass30_550` -> `key/prev/next`
- `CacheableNode`: `method330` -> `unlinkDual`, dual-link fields renamed to `previousDual/nextDual`
- `Deque`: renamed `method249..method256` to `addFirst/addLast/removeLast/last/first/previous/next/clear`
- `NodeHashTable`: `method148/method149` -> `get/put`, buckets metadata names clarified

## Next steps

- Continue phased renaming of remaining `Class1`..`Class50` files.
- Add lightweight package structure once class naming stabilizes.
- Add smoke tests and startup docs for both modules.
