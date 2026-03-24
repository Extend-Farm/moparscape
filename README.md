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

## Next steps

- Continue phased renaming of remaining `Class1`..`Class50` files.
- Add lightweight package structure once class naming stabilizes.
- Add smoke tests and startup docs for both modules.
