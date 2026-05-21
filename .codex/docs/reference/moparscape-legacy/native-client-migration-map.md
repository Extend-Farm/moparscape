# Native client migration map (legacy moparscape)

This directory already contains enough reverse-engineering work to treat the legacy client as a set of documented subsystems instead of one 10k-line decompiled blob.

The practical question is no longer "what does `GameClientCore` do?" but "which native module owns that behavior now, and what is still missing for full parity?"

## Working rule

- `server/moparscape/` and the docs in this directory are reference-only.
- The live rewrite is the `rs-*` module set, especially `:rs-protocol`, `:rs-client-core`, `:rs-server-runtime`, and `:rs-client-lwjgl`.
- Do not import or execute legacy runtime code from the native client.
- Only extend the legacy docs when a native workstream hits behavior that is still undocumented.

## Legacy coverage already in place

The legacy client is already decomposed far enough to support subsystem-by-subsystem migration:

- `GameClientCore-A.md` through `GameClientCore-E.md` cover the full 10 960-line client class end to end.
- `bootstrap.md` covers archive/bootstrap startup.
- `network-io.md` covers ISAAC, packet dispatch, cache fetch, and endpoint helpers.
- `2d-rendering.md`, `Rasterizer3D.md`, `Model.md`, and `SceneGraph.md` cover the software render stack.
- `MapRegion.md`, `terrain-forms.md`, and `collision-routing.md` cover terrain, object placement, and route logic.
- `widgets.md`, `player-actor.md`, and `entity-definitions.md` cover the UI tree, actors, and cache definitions.

That means the reverse-engineering bottleneck is no longer bulk deobfuscation. The bottleneck is native parity work.

## Legacy-to-native ownership map

| Legacy subsystem | Reference docs | Native owner today | Current status | Highest-priority missing slice |
| --- | --- | --- | --- | --- |
| Title screen, login shell, loading flames | `GameClientCore-B.md`, `GameClientCore-E.md`, `bootstrap.md`, `2d-rendering.md` | `rs-client-lwjgl/.../login/*`, `NativeClientBootstrap`, `LoginInputHandler`, `TitleScreenRenderer` | Strong. Cache-backed title assets, welcome/credentials/loading states, and flame animation all exist natively. | Keep aligned as bootstrap changes, but this is not the main blocker anymore. |
| Asset/bootstrap pipeline | `bootstrap.md`, `network-io.md` | `NativeClientBootstrap`, `TitleScreenAssetLoader`, `GameplayFrameAssetLoader`, `TextureArchiveAssetLoader`, `ContentBootstrapService` | Strong. The native client can boot from the checked-in cache layout and already has fallback behavior for missing archives. | Mostly maintenance; no major reverse-engineering blocker here. |
| Session/protocol state machine | `GameClientCore-C.md`, `GameClientCore-E.md`, `network-io.md` | `rs-protocol`, `rs-client-core`, `GameplayClientSession`, `PlayerSessionActor`, `GameplayRuntimeCoordinator` | Minimal modern subset only. The protocol currently covers handshake, login, local-player bootstrap, movement, and local action-sequence echo. | Expand authoritative client-view state for NPCs, other players, chat, interfaces, social lists, ground items, and scene deltas. |
| Gameplay frame chrome, side tabs, chat shell | `GameClientCore-A.md`, `GameClientCore-D.md`, `widgets.md`, `2d-rendering.md` | `rs-client-lwjgl/.../gameplay/*`, especially `GameplayChromeRenderer` and `GameplaySidebarRenderer` | Partial. Inventory, equipment, and stats tabs use real bootstrap data; chatbox/status and many tabs are explicit placeholders. | Port widget/interface state and input instead of filling more tabs with synthetic content. |
| Terrain scene assembly | `MapRegion.md`, `terrain-forms.md`, `SceneGraph.md`, `GameClientCore-A.md` | `rs-client-lwjgl/.../world/terrain/*`, `CacheBackedWorldSceneLoader` | Strong parity workstream. Underlay/overlay ids, shapes, bridge semantics, floor colors, tile forms, and occlusion data already exist natively. | Continue parity work from `.codex/plan/terrain-minimap-parity.md`. |
| Minimap base raster + radar composition | `GameClientCore-A.md`, `GameClientCore-E.md`, `SceneGraph.md`, `terrain-forms.md` | `rs-client-lwjgl/.../world/minimap/*`, `GameplayMinimapRenderer` | Strong but still parity-sensitive. Base raster, wall marks, `mapscene`, `mapfunction`, and rotated radar are already native. | Finish rotation/frame alignment and keep it in lockstep with `.codex/plan/world-frame-parity.md`. |
| Static object decode and placement | `MapRegion.md`, `SceneGraph.md`, `entity-definitions.md`, `Model.md` | `rs-client-lwjgl/.../world/object/*`, `WorldSceneSubmissionBuilder` | Partial. Real object geometry exists when decode succeeds, and opcode `77` morph-backed scenery now resolves to a renderable child instead of dropping straight to a proxy. The viewport still relies on fallback proxy shapes for truly model-less objects and for the remaining placement/parity gaps. | Replace the remaining fallback proxy submission with decoded model parity and correct scene placement/occlusion across object types. |
| 3D viewport raster, visibility, occlusion | `Rasterizer3D.md`, `SceneGraph.md`, `Model.md` | `rs-client-lwjgl/.../world/raster/*`, `rs-client-lwjgl/.../world/visibility/*`, `WorldViewportRenderer` | Partial. There is a coherent native submission boundary now, but not full legacy triangle, culling, or visibility-traversal parity. | Finish the world-frame work and then port more of the `SceneGraph` visibility/render contract. |
| Actor models and animation | `player-actor.md`, `GameClientCore-D.md`, `GameClientCore-E.md` | `rs-client-lwjgl/.../character/*`, `LocalPlayerAnimationTracker`, `CharacterModelAssembler`, `WorldSceneSubmissionBuilder` | Partial and local-player-only. The native renderer can assemble and animate the local player appearance, and the merged player body now keeps legacy model scale plus seam-collapsed topology instead of synthetic actor-box normalization. | Add authoritative visible-entity state for NPCs and other players, then render them through the same scene submission path. |
| Input, pathing, click-to-walk, context menu | `collision-routing.md`, `GameClientCore-A.md`, `GameClientCore-C.md` | `DesktopInputRouter`, `WorldViewportClickPlanner`, `GameplayClientSession`, `GameplayContextMenu` | Partial. Walk-to and camera interaction work; the full legacy menu-opcode dispatcher does not exist natively. | Port action semantics only after widget/entity/social state exists, otherwise the menu layer has nothing authoritative to operate on. |
| Chat, social, report-abuse, settings, music, emotes | `GameClientCore-A.md`, `GameClientCore-C.md`, `GameClientCore-D.md`, `GameClientCore-E.md`, `widgets.md` | Mostly unowned or placeholder-only in `rs-client-lwjgl/.../gameplay/*` | Largely absent. Some text surfaces exist, but the real protocol/UI contracts are not ported. | Treat this as downstream of protocol breadth plus widget rendering, not as isolated UI polish work. |

## What is actually blocking "complete the client side"

1. The native protocol/view model is still too narrow.
   It only carries enough state for login, a local player spawn, movement, and a local action sequence. A full client needs authoritative state for visible players, NPCs, chat, interfaces, social lists, ground items, and scene events.

2. Widget rendering/input has not been ported.
   The legacy client is heavily widget-driven. Until the native path can decode, render, and interact with widget trees, most tabs and in-game UI surfaces will remain placeholders.

3. The world renderer is coherent but not at `SceneGraph` parity.
   Terrain/minimap work is well underway, but the viewport still lacks the legacy visibility traversal, full object-model submission parity, and multi-actor scene population.

4. NPC and other-player replication does not exist in the native state model yet.
   The current native actor path is effectively "local player only." That is a protocol/runtime gap first, and a rendering gap second.

5. Legacy menu semantics should be ported late, not early.
   `GameClientCore.method69(...)` is documented, but porting that action matrix before the native client has authoritative UI/entity state would create a brittle fake layer.

## Recommended execution order

1. Treat the legacy docs in this directory as the behavioral reference baseline.
   Only add more reverse-engineering notes when a native workstream hits an undocumented edge.

2. Expand the native state contract first.
   Focus on `rs-protocol`, `rs-client-core`, and `rs-server-runtime` until the client can receive authoritative visible-entity, chat, social, and interface state.

3. Port widget-tree decode/render/input next.
   This unlocks the majority of the fixed-frame client UI without inventing native-only placeholder semantics.

4. Continue the two active world-parity workstreams.
   - `.codex/plan/terrain-minimap-parity.md`
   - `.codex/plan/world-frame-parity.md`

5. Replace fallback world/object rendering with decoded parity.
   Once protocol breadth and world-frame correctness are stable, finish object submission and actor population inside the viewport.

6. Port menu actions and secondary tabs last.
   At that point the client will finally have real targets for spell/item-on-X, social actions, settings toggles, and interface-driven flows.

## Practical implication

`GameClientCore` is already reverse-engineered far enough to stop treating "finish the client" as one monolithic task.

The highest-value next slices are:

- native protocol/view-model expansion
- widget tree parity
- multi-entity scene replication
- final world/render parity

Everything else is downstream of those four.
