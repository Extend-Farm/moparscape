# UI And Tabs

## Sidebar model

- The desktop client keeps tab interface ids in `anIntArray1130[]`.
- Those ids default to `-1`, which means “tab not available”.
- Packet `71` updates those ids client-side.

## Current bootstrap behavior

- The emulator sends local default sidebar interfaces from `client.initializeDefaultSidebarInterfaces()`.
- `sConfig(1)` can still override them if the remote config ever loads, but local login should not depend on that endpoint.

## Failure pattern

- If only the combat tab shows, the sidebar ids were not populated.
- If the tab art is present but a specific tab opens the wrong screen, verify the interface id being sent for that tab.

## Practical rule

- Keep sidebar defaults explicit and local in the emulator.
- Do not rely on the old remote HybridScape config to seed UI state.
