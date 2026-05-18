# Persistence

## Recommendation

- Do not move to a database yet.

## Why not yet

- Startup, login bootstrap, sidebar setup, input ownership, and world loading are still being stabilized.
- A storage migration now would add moving parts while the runtime contract is still shifting.
- The current pain is architectural clarity, not query complexity.

## Current storage

- Character saves: flat files in `client/characters/`
- Bans: XML files in `client/Bans/`
- Passwords: legacy plaintext with case-insensitive comparison

## Better near-term work

- Keep file-based storage for now.
- First stabilize:
  - startup
  - login
  - sidebars
  - map/cache loading
  - refactor boundaries
- Then introduce a persistence boundary such as `PlayerStore` or `AccountStore`.

## Good time to adopt a database

- When you want hashed passwords and proper account management
- When bans, admins, and audit trails need real tooling
- When you need multi-world support or shared account state
- When game state writes need transactional guarantees

## Safer migration order

- Start with accounts, auth, bans, and metadata.
- Keep deep player state file-based until the schema is understood.
- Only move inventory, quest, and world state after the emulator-side model is cleaned up.
