# Phase 4: Native World, Character State, and UI Bootstrap

## Goal
- Replace the current `worldPoint-only` native runtime with a real RSPS bootstrap path.
- Keep `server/moparscape` and `client/` as reference-only.
- Do not import legacy runtime classes into `rs-*`.
- Port the legacy data flow, not the legacy code shape.

## Core Correction
- The cache does **not** directly "create inventory" or "load the character".
- In the legacy stack:
  - cache archives provide definitions, sprites, models, interfaces, terrain, objects, animations, fonts
  - server persistence provides live character state
  - server packets populate client-side containers and appearance state
  - client resolves item ids / model ids / widget ids through cache-backed definitions

## Legacy Reference Points

### Cache and interface bootstrap
- `GameClientCore.java:6260-6314`
  - unpacks media/config/interface archives
  - loads textures/config/interfaces during startup
- `Widget.java:15-220`
  - decodes interface widgets from the interface archive
- `ItemDefinition.java:37-60`
  - loads item definitions from `obj.dat` / `obj.idx`

### Inventory and widget item containers
- `InterfacePacketHandler.java:78-140`
  - `applyWidgetItemGridSnapshot(...)`
  - `applyWidgetItemSlotUpdates(...)`
- `WidgetRenderHandler.java:201-292`
  - inventory grids render from widget item ids/amounts
  - item visuals come from `ItemDefinition.method200(...)`

### Character appearance
- `client/Player.java:680-779`
  - `appendPlayerAppearance(...)`
  - server writes the player appearance block
- `PlayerUpdateMaskHandler.java:72-79`
  - client receives and decodes appearance update blocks

### Character persistence
- `PlayerHandler.java:430-560`
  - persists:
    - equipment
    - look
    - skills
    - inventory
    - bank
    - friends
    - ignores

## Current Native Gap

### What `rs-*` has now
- cache-backed terrain scene loading
- native title/login
- auth through file or Postgres repositories
- native movement and world position updates

### What `rs-*` does not have yet
- inventory snapshots
- equipment snapshots
- appearance snapshots
- skills / xp snapshots
- bank / friends / ignores
- interface/widget definitions on the client
- cache-backed item definitions/sprites/models on the client
- object region decoding in the world scene
- real UI containers for inventory/equipment/skills

### Proof of the current bottleneck
- `CharacterSnapshot` only contains:
  - `id`
  - `accountId`
  - `displayName`
  - `worldPoint`
- `rs-protocol` currently only carries:
  - `LoginAccepted`
  - `WorldSnapshotMessage`
  - `EntityPositionMessage`
- that is not enough to bootstrap a RuneScape player state

## Architecture Direction

### Server responsibility
- load canonical character state from persistence
- send normalized gameplay snapshots to the client
- remain authoritative for live state changes

### Client responsibility
- load cache definitions/assets natively
- render state from protocol snapshots
- resolve ids into names/sprites/models/widgets locally

### Persistence responsibility
- hold canonical character/account state
- not cache-derived assets

## Workstreams

### Workstream 1: Expand native persistence shape
- Replace `CharacterSnapshot` with a real native bootstrap aggregate.
- Introduce something like:
  - `CharacterBootstrap`
  - `CharacterAppearance`
  - `CharacterEquipment`
  - `CharacterInventory`
  - `CharacterSkills`
  - `CharacterSocialState`

#### Minimum fields to port first
- position / plane
- display name
- rights / member flag
- run energy
- appearance look array
- equipment slot ids + amounts
- inventory slot ids + amounts
- skill levels + xp

#### Source of truth
- Postgres first
- file repository remains compatibility input only until the cutover is done

### Workstream 2: Expand native Postgres repositories
- Make `PostgresCharacterRepository` load the bootstrap aggregate, not only position.
- Map these existing tables into the aggregate:
  - `character_positions`
  - `character_profiles`
  - `character_appearances`
  - `character_skill_progression`
  - `character_item_slots`
  - `character_social_links`

#### Acceptance
- logging in as `akira` produces a single in-memory bootstrap object with:
  - world point
  - equipment
  - inventory
  - appearance
  - skills

### Workstream 3: Expand native protocol
- Add new bootstrap/server messages.

#### Minimum message set
- `CharacterBootstrapAccepted`
  - account id
  - character id
  - world point
  - rights
  - member flag
  - run energy
- `AppearanceSnapshotMessage`
- `InventorySnapshotMessage`
- `EquipmentSnapshotMessage`
- `SkillSnapshotMessage`

#### Optional next messages
- `BankSnapshotMessage`
- `SocialSnapshotMessage`
- `InterfaceLayoutMessage`

#### Important rule
- Protocol messages carry runtime state, not cache assets.
- Item names/sprites/models stay client-local via cache decode.

### Workstream 4: Native cache definition bootstrap on the client
- Add native client-side loaders for:
  - item definitions
  - interface widgets
  - identity kits / appearance inputs
  - object definitions

#### Legacy behavior to port
- `ItemDefinition.method193(...)` / `method198(...)`
- `Widget.method205(...)`

#### Acceptance
- the native client can resolve:
  - inventory item name by id
  - equipment item model id by slot item id
  - widget layout for inventory/equipment/skills tabs

### Workstream 5: Native UI state instead of legacy widget arrays
- Do not clone `Widget.anIntArray253` / `anIntArray252` mechanically into the new client.
- Build explicit native view models:
  - `InventoryViewModel`
  - `EquipmentViewModel`
  - `SkillsViewModel`
  - later `BankViewModel`, `FriendsViewModel`

#### Renderer milestone
- first render a native inventory panel with:
  - slot grid
  - item ids resolved to cache-backed labels or sprites
  - equipment panel
  - skill values

### Workstream 6: Native appearance rendering
- Port the appearance model composition path natively.
- Use:
  - look array
  - equipment ids
  - item/identity-kit definitions from cache

#### Legacy behavior to mirror
- server writes appearance update block in `client/Player.java`
- client decodes it in `PlayerUpdateMaskHandler.applyPlayerAppearanceUpdate(...)`

#### Native target
- no binary legacy appearance block reuse
- directly model the semantic appearance state in `rs-protocol`

### Workstream 7: World scene beyond terrain
- Add native object archive decoding from region object archives.
- Build:
  - walls
  - static objects
  - collision metadata
  - later npc spawn overlays

#### Important boundary
- do this with native decoders in `rs-content`
- do not call legacy `MapRegion`

## Delivery Order

### Slice 1
- Expand `CharacterSnapshot` into a real bootstrap aggregate.
- Load `akira` fully from Postgres.
- Add protocol bootstrap messages for appearance, inventory, equipment, skills.

### Slice 2
- Add native client view models for inventory/equipment/skills.
- Render those panels with text-first placeholders if needed.
- Use cache-backed item definitions for names immediately.

### Slice 3
- Add native interface/widget decoding for real panel layouts.
- Replace placeholder panels with cache-driven layouts.

### Slice 4
- Add native appearance rendering for the local player.

### Slice 5
- Add native object region decoding and richer world scene composition.

## What Not To Do
- Do not import or execute legacy runtime code from `rs-*`.
- Do not push more fake world views as if they were gameplay parity.
- Do not make cache the source of runtime inventory/equipment state.
- Do not hide semantic state inside opaque legacy packet blocks.

## Acceptance Criteria for the Next Real Milestone
- Login as `akira` through the native runtime.
- Server loads full character bootstrap from Postgres.
- Client receives:
  - world point
  - appearance
  - equipment
  - inventory
  - skills
- Client resolves item/interface data through native cache decoders.
- Client renders:
  - world scene
  - local player marker or model
  - inventory panel
  - equipment panel
  - skills values

## Immediate Next Task
- Implement Workstream 1 and 2 first:
  - expand native character bootstrap aggregate
  - load the full `akira` state from Postgres
  - stop treating `CharacterSnapshot` as only `displayName + worldPoint`
