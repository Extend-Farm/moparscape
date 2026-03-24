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

## Next steps

- Continue phased renaming of remaining `Class1`..`Class50` files.
- Add lightweight package structure once class naming stabilizes.
- Add smoke tests and startup docs for both modules.
