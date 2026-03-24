# Game Client Module (`server/moparscape`)

This folder contains the legacy game client codebase, currently being modernized in-place.

## Build

From repository root:

- `./gradlew :game-client:compileJava`
- `./gradlew :game-client:run`

## Entry point

- Main class: `GameClient`

## Rename tracking

The following high-impact renames are already completed:

- Runtime/bootstrap:
  - `client` -> `GameClient`
  - `Applet_Sub1` -> `GameShell`
  - `Frame_Sub1` -> `GameFrame`
  - `signlink` -> `SignLink`
  - `Class42_Sub1` -> `OnDemandFetcher`
  - `Class44` -> `Archive`
  - `Class30_Sub2_Sub2` -> `PacketBuffer`
  - `Class17` -> `IsaacCipher`
  - `Class14` -> `CacheFileStore`
  - `Class30_Sub2_Sub3` -> `OnDemandRequest`
  - `Class15` -> `ProducingGraphicsBuffer`

- Scene/map/object:
  - `Class7` -> `MapRegion`
  - `Class11` -> `CollisionMap`
  - `Class22` -> `FloorDefinition`
  - `Class25` -> `SceneGraph`
  - `Class46` -> `ObjectDefinition`
  - `Class30_Sub2_Sub4_Sub5` -> `DynamicObject`
  - `Class30_Sub2_Sub4_Sub6` -> `Model`

- Core containers:
  - `Class30` -> `Node`
  - `Class30_Sub2` -> `CacheableNode`
  - `Class19` -> `Deque`
  - `Class2` -> `CacheableNodeDeque`

- Utilities/infrastructure:
  - `Class1` -> `NodeHashTable`
  - `Class12` -> `NodeCache`
  - `Class13` -> `BZip2Decompressor`
  - `Class32` -> `BZip2State`
  - `Class24` -> `BufferedConnection`
  - `Class45` -> `Skills`
  - `Class50` -> `TextUtils`

- Rendering primitives:
  - `Class30_Sub2_Sub1` -> `Rasterizer2D`
  - `Class30_Sub2_Sub1_Sub1` -> `Sprite`
  - `Class30_Sub2_Sub1_Sub2` -> `IndexedSprite`
  - `Class30_Sub2_Sub1_Sub3` -> `Rasterizer3D`
  - `Class30_Sub2_Sub1_Sub4` -> `FontRenderer`

- Scene internals and animation defs:
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

## Notes

- Remaining obfuscated class names are intentionally being renamed in small batches.
- Compile after each rename batch to keep refactors safe.
