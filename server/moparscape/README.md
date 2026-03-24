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

## Notes

- Remaining obfuscated class names are intentionally being renamed in small batches.
- Compile after each rename batch to keep refactors safe.
