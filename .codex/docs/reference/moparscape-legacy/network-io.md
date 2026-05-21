# Network + cache I/O (legacy moparscape)

Reference for the four legacy client-side helpers that move bytes in and out of the gameplay client: the ISAAC stream cipher used for packet-opcode XOR, the per-tick incoming packet helpers, the bzip2 archive decompressor, and the modern server-endpoint resolver.

All file:line references are absolute paths. Method names ending in `methodNNN` remain as obfuscated names from the original Jad output; new helpers in `IncomingPacketDispatcher` and `GameServerEndpoint` have already been renamed.

---

## IsaacCipher.java

Source: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/IsaacCipher.java`

### Overview

Canonical implementation of Bob Jenkins' ISAAC (Indirection, Shift, Accumulate, Add, Count) cryptographic PRNG, used by the 317 protocol as a stream cipher: each emitted 32-bit word's low byte is consumed and XORed against (or subtracted from) the packet opcode at both ends of the socket. Two cipher instances are constructed at login — one for outbound (`aClass17_1000`) and one for inbound (`aClass30_Sub2_Sub2_1192.aClass17_1410`) — seeded with the four ISAAC keys derived from the login session keys (see `GameClientCore.java:5980` and `:5984`). Server-side opcode reads compute `opcode = (raw - cipher.method246()) & 0xff` (see `GameClientCore.java:9415-9416`).

The implementation follows the textbook ISAAC algorithm; the obfuscator merely renamed the methods. No deviations from canonical ISAAC.

### Fields (`IsaacCipher.java:192-199`)

| Field | Role |
| --- | --- |
| `anInt332` (init `-436`) | Decoy/anti-tamper integer set in the constructor; never read. |
| `anInt333` (init `-431`, becomes `-242` when `i >= 0`) | Decoy field touched in the constructor; never read elsewhere. |
| `anInt334` | Index into the `anIntArray335` result pool. Decremented each `method246()` call; when it reaches 0 a fresh batch is generated. |
| `anIntArray335[256]` | Output pool of 256 generated 32-bit words. Seeded with the caller-supplied key in the constructor. |
| `anIntArray336[256]` | The ISAAC `mm` (memory) state array. |
| `anInt337` | The accumulator `a`. |
| `anInt338` | The previous-result `b`. |
| `anInt339` | The counter `c`. |

### Methods

| Method | Line | Description |
| --- | --- | --- |
| `IsaacCipher(int i, int[] ai)` | `:13` | Constructor. Sets the two decoy ints, allocates `anIntArray336/335`, copies the seed key from `ai` into `anIntArray335`, then calls `method248()` to run the full ISAAC key-schedule. The unused parameter `i` is a guard byte preserved from the obfuscator. |
| `method246()` | `:27` | Public next-byte accessor. Returns the next 32-bit word from `anIntArray335[anInt334]`, refilling via `method247()` when exhausted. Callers consume the low byte for XOR. |
| `method247()` | `:37` | The core ISAAC mixing step: increments `c`, then for each of the 256 indices runs `a ^=` one of four shift patterns (`<<13, >>>6, <<2, >>>16`) selected by `i & 3`, adds `mm[i+128 & 0xff]`, indexes `mm[(x & 0x3fc) >> 2]` and `mm[(y >> 8 & 0x3fc) >> 2]`, and writes the new result word. Exactly matches the canonical ISAAC step. |
| `method248()` | `:62` | Key-schedule: initialises eight working ints to the golden ratio `0x9e3779b9`, runs four rounds of the canonical 24-step mix, then two passes that fold the seed (first from `anIntArray335`, then from `anIntArray336`) using the same mix. Finally calls `method247()` once and primes `anInt334 = 256`. Canonical ISAAC init. |

---

## IncomingPacketDispatcher.java

Source: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/IncomingPacketDispatcher.java`

### Overview

A package-private utility class (final, private constructor at `:7`) of static helpers extracted from the legacy `GameClientCore.method84` packet-dispatch loop. It does not run the read loop itself; the loop is still in `GameClientCore.java` (around `:9415` onward — opcode read, then a large `switch` over `anInt1008`). Each helper takes the shared `PacketBuffer` (`aClass30_Sub2_Sub2_1083`) and reads/decodes one packet body, returning either `void` (for handlers that mutate `Widget` or shared arrays) or primitive/`int[]` for handlers that hand a value back to the dispatch site.

The packet-table mapping is documented in `GameClientCore-E.md:202-256`; this file just owns the extracted bodies.

### Methods

| Method | Line | Opcode (per `GameClientCore-E.md`) | Description |
| --- | --- | --- | --- |
| `applyIgnoreListSnapshot(PacketBuffer, int packetSize, long[] ignoredNameHashes)` | `:10` | 214 | Reads `packetSize/8` 64-bit name hashes (`method414`) into the ignore-list array; returns count. |
| `resetCameraEffects(boolean[] enabledCameraEffects)` | `:18` | (utility, called from camera-reset paths) | Zeroes the 5-slot camera-oscillator enable flags. |
| `applyCameraEffectUpdate(PacketBuffer, boolean[], int[], int[], int[], int[])` | `:24` | 35 | Reads `slot, target, amplitude, frequency` (all `method408` u8), enables the slot and resets its phase. |
| `clearWidgetItemContainer(PacketBuffer)` | `:37` | 72 | Reads `widgetId` (`method434`), zeros every entry of `Widget.anIntArray253`. Note the legacy double-write (`-1` then `0`) is preserved verbatim. |
| `applyWidgetScrollPosition(PacketBuffer)` | `:48` | 70 | Reads `scrollX (method411), scrollY (method437), widgetId (method434)` and writes `widget.anInt263/anInt265`. |
| `applyWidgetModelId(PacketBuffer)` | `:58` | 75 | Reads `modelId, widgetId` (both `method436`); sets `anInt233=2` and `anInt234=modelId`. |
| `readMinimapState(PacketBuffer)` | `:66` |   | Reads one u8 minimap-state byte. |
| `readSystemUpdateTimer(PacketBuffer)` | `:71` |   | Reads u8 and multiplies by 30 (tick→countdown). |
| `applySkillUpdate(PacketBuffer, int[] currentLevels, int[] currentExperience, int[] computedLevels, int[] experienceThresholdTable)` | `:76` | 134 | Reads `skillId(u8), xp(u32 LE via method439), currentLevel(u8)`; updates the arrays and recomputes the displayed level by walking the 98-entry XP threshold table. NOTE the legacy code stores `xp` into `currentLevels[]` and `currentLevel` into `currentExperience[]` — the field swap is intentional (matches the obfuscated source). |
| `readInterfaceSettingUpdate(PacketBuffer)` | `:89` |   | Reads `value (method410 u16le)` then `index (method426)`; remaps 65535→-1. Returns `{index, value}`. |
| `readSongId(PacketBuffer)` | `:100` |   | Reads u16 song id; returns -1 if 65535. |
| `readSongDelayUpdate(PacketBuffer)` | `:109` |   | Reads `songId (u16), songDelay (i16le)`. |
| `readMapFlagUpdate(PacketBuffer)` | `:118` |   | Reads `flagX(u8), flagY(method427)`. |
| `readRegionPacketCoordinates(int opcode, PacketBuffer, int fallbackRegionX, int fallbackRegionY)` | `:127` | 73 | If opcode == 73 reads `regionX (i16le), regionY (u16le)`; otherwise returns the supplied fallbacks. |
| `readDynamicRegionY(PacketBuffer)` | `:141` |   | Reads `i16le` Y for the dynamic-region (instanced map) packet. |
| `readDynamicRegionChunks(PacketBuffer, int bitAccessGuard, int[][][] chunkTemplates)` | `:146` |   | Enters bit-access mode (`method418`), for each `(plane, chunkX∈[0,13), chunkY∈[0,13))` reads 1 bit; if set, reads 26 bits as the chunk template, else writes `-1`. Exits bit access (`method420`). |
| `readDynamicRegionX(PacketBuffer)` | `:168` |   | Reads `u16le` X for the dynamic-region packet. |
| `readSceneBasePosition(PacketBuffer)` | `:174` | 85 | Reads `baseX, baseY` (both `method427`). |
| `readChatboxStatus(PacketBuffer)` | `:184` | 110 | Reads one u8 chatbox/private-chat status byte. |
| `applyWidgetScrollClamp(PacketBuffer)` | `:190` | 79 | Reads `widgetId (u8), scroll (i16le)`; clamps to `[0, widget.anInt261 - widget.anInt267]` and writes `widget.anInt224` only when `widget != null` and `widget.anInt262 == 0`. |

### Where it ties into `GameClientCore` packet handlers

The dispatcher class is consumed exclusively by `GameClientCore.method84` (the per-tick incoming-packet handler). Call sites in `GameClientCore.java`:

- `:9515` — `resetCameraEffects`
- `:9521` — `clearWidgetItemContainer`
- `:9527` — `applyIgnoreListSnapshot` (with `anInt1007` packet-size)
- `:9552` — `applySkillUpdate`
- `:9558` — `readInterfaceSettingUpdate`
- `:9569` — `readSongId`
- `:9582` — `readSongDelayUpdate`
- `:9603` — `applyWidgetScrollPosition`
- `:9609` — `readRegionPacketCoordinates` (passes the dispatcher's `anInt1008` opcode and current `anInt1069/1070`)
- `:9616-9618` — `readDynamicRegionY` / `readDynamicRegionChunks` / `readDynamicRegionX`
- `:9690` — `readMinimapState`
- `:9696` — `applyWidgetModelId`
- `:9702` — `readSystemUpdateTimer`
- `:9708` — `readMapFlagUpdate`
- `:9721` — `applyCameraEffectUpdate`
- `:9785` — `readChatboxStatus`
- `:9814` — `applyWidgetScrollClamp`
- `:9851` — `readSceneBasePosition`

The dispatcher reads only — it never sends. The opcode that drives the switch is itself read at `GameClientCore.java:9415-9416` where `anInt1008` is computed as `(raw - aClass17_1000.method246()) & 0xff` (the inbound ISAAC step).

---

## BZip2Decompressor.java + BZip2State.java

Sources:
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BZip2Decompressor.java`
- `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BZip2State.java`

### Overview

Block-level bzip2 decoder used by the cache layer to inflate the per-archive payloads (`Archive.java:36` decodes a 6-byte-header archive, `Archive.java:78` decodes individual sub-files at their stored offsets). The implementation is a port of the reference bzip2 decoder restricted to a single block size category and stripped of CRC verification.

It implements the full canonical pipeline: bit-level Huffman decode → RLE-1 sentinel expansion → MTF (move-to-front) inverse with the 16x16 quadrant trick → BWT (Burrows-Wheeler) inverse via the cumulative-frequency `T` table → RLE-2 run expansion on the output. The shared `BZip2State` (singleton `aClass32_305` guarded by `synchronized`) holds all per-decode scratch.

#### 317-cache-specific deviations from canonical bzip2

This is **not** a full bzip2 stream decoder; it is the cache-format variant. Notable deviations to be aware of when porting:

1. **No `BZh` magic, no version byte, no CRC.** The canonical bzip2 stream starts `'B','Z','h', '0'..'9'`. The cache layer strips that and stores only the block body. The 6-byte header skipped in `Archive.java:36` is the *archive* header (compressed length / uncompressed length), not a bzip2 header. The decompressor in `method227:198-211` reads ten "byte0" values up front and discards all of them — these are the position-of-the-final-byte and the block CRC bytes whose values nobody verifies.
2. **Block size is hard-coded to 1 (100 KB).** `class32.anInt578 = 1` (`method227:192`); the working `anIntArray587` is sized `1 * 100_000` (`:194`). Canonical bzip2 supports 1–9; the cache only ever emits size-1 blocks.
3. **Randomised-block branch is panic-only.** Canonical bzip2 supports a "randomised" fallback for pathological BWT inputs; this decoder still parses the flag bit and prints `"PANIC! RANDOMISED BLOCK!"` (`method227:217`) but does not actually de-randomise — the cache never produces such blocks.
4. **End-of-stream sentinel is `byte0 == 23`** (`method227:199-200`) — the first symbol of the "end-of-archive" frame. Canonical bzip2 uses a 48-bit magic; the cache variant truncates to a single byte.
5. **No CRC validation at all.** The canonical block CRC and stream CRC are read into `anInt580` and friends but never checked against computed values.
6. **The MTF/symbol-decoding loop is fused with RLE-1 expansion** in `method227` rather than separated as in modern decoders. Symbols 0 and 1 encode RUNA/RUNB and are expanded inline (`:359-401`).
7. **`anIntArray587` is `static`** on `BZip2State` (`BZip2State.java:67`). The singleton/synchronized pattern means only one decode can run at a time across the whole JVM.

The bit-level Huffman build (`method232`), MTF quadrant maintenance (`method227:326-456`), and BWT inverse (`method226`) are otherwise textbook bzip2.

### `BZip2State` fields (`BZip2State.java:34-81`)

Selected fields; the obfuscated names are kept verbatim because the dispatcher in `BZip2Decompressor` accesses them by these names.

| Field | Role |
| --- | --- |
| `anInt554`..`anInt562` (final ints) | Algorithm constants. `4096` (MTF table size), `16` (MTF quadrant count), `258` (max Huffman symbols), `23` (max code length + 1), `1` (block-size multiplier), `6` (max Huffman tables), `50` (selector run length), `4` (RLE-2 base), `18002` (max selector count). |
| `aByteArray563`, `anInt564`, `anInt565` | Input buffer pointer, read cursor, remaining length. |
| `anInt566`, `anInt567` | 64-bit byte counter (low/high) for total input consumed. |
| `aByteArray568`, `anInt569`, `anInt570` | Output buffer pointer, write cursor, remaining capacity. |
| `anInt571`, `anInt572` | 64-bit output-byte counter. |
| `aByte573` | RLE-2 last byte. |
| `anInt574` | RLE-2 run length state machine (0..4+). |
| `aBoolean575` | "Randomised block" flag (panic-only). |
| `anInt576`, `anInt577` | Bit-decoder shift register and bit-count. |
| `anInt578` | Block-size multiplier (always 1 in cache). |
| `anInt579` | Block counter (incremented per block, used as a debug counter). |
| `anInt580` | BWT origin pointer (3 bytes read from the block header). |
| `anInt581`, `anInt582` | BWT inverse cursor + last-emitted byte. |
| `anIntArray583[256]` | BWT frequency table. |
| `anInt584` | BWT step counter. |
| `anIntArray585[257]`, `anIntArray586[257]` | Cumulative-frequency tables for BWT inverse. |
| `static anIntArray587[]` | The BWT `T` table — also reused for RLE-1 expansion output. Sized `100_000 * anInt578`. |
| `anInt588` | Number of symbols actually used in this block (set by `method231`). |
| `aBooleanArray589[256]`, `aBooleanArray590[16]` | Symbol-use bitmap and its 16x16 outer bitmap. |
| `aByteArray591[256]` | Compact symbol→byte map produced by `method231`. |
| `aByteArray592[4096]` | MTF sliding window. |
| `anIntArray593[16]` | MTF quadrant base pointers. |
| `aByteArray594[18002]`, `aByteArray595[18002]` | Selector arrays (MTF-decoded selectors + raw selectors). |
| `aByteArrayArray596[6][258]` | Huffman code-length tables for up to 6 trees. |
| `anIntArrayArray597/598/599[6][258]` | Per-tree limit/base/permutation tables for canonical Huffman decode. |
| `anIntArray600[6]` | Per-tree minimum code length. |
| `anInt601` | BWT block length (`i6` at end of decode). |

### Methods

| Method | Line | Description |
| --- | --- | --- |
| `method225(byte[] src, int srcLen, byte[] dst, int dstOffset, int unusedHeaderBytes)` | `:16` | **Public entry point.** Synchronised on the singleton `aClass32_305`. Wires the input buffer (`src`, `srcLen`) and output buffer (`dst` at `dstOffset`) into the state, zeros all running counters, calls `method227`, and returns `srcLen - state.anInt570` (number of output bytes written, since `anInt570` starts at `srcLen` and is decremented per emitted byte). Returns the decompressed length. |
| `method226(BZip2State)` | `:40` | **BWT inverse + RLE-2 expansion.** Iterates the cumulative-frequency permutation table `anIntArray587` from `anInt581`, emitting bytes. Tracks runs of identical bytes; on the 4th repeat consumes one more byte as a 0–255 run length (`i = (byte3 & 0xff) + 4`). Writes into the output buffer, decrementing `anInt570` (remaining capacity); pauses when capacity hits zero and resumes on the next caller invocation. |
| `method227(BZip2State)` | `:167` | **Main per-block decode loop.** Reads the block header (10 ignored bytes for the would-be magic/CRC at `:198-210`), the randomised flag (`:211-217`), the 3-byte BWT origin (`:219-224`), the 16+256 symbol-use bitmap (`:225-247`), then `method231` to compact the symbol list. Reads `numHuffmanTrees ∈ [2,6]` (3 bits) and `numSelectors` (15 bits), the unary-encoded selector MTF (`:253-279`), the per-tree code-length deltas (`:281-300`), builds each Huffman table with `method232` (`:302-316`), initialises the MTF quadrant pointers (`:326-335`), and runs the symbol-decode/RLE-1/MTF-inverse loop (`:337-481`) writing into `anIntArray587`. Finalises BWT cumulative tables (`:483-498`), seeds the BWT inverse cursor (`:499-505`), and calls `method226` to emit decoded bytes. Loops until end-of-block. |
| `method228(BZip2State)` | `:514` | Read one byte (`method230(8, …)`). |
| `method229(BZip2State)` | `:519` | Read one bit (`method230(1, …)`). |
| `method230(int n, BZip2State)` | `:524` | **Bit reader.** Returns the next `n` bits from the input stream, refilling the 32-bit shift register `anInt576` one byte at a time from `aByteArray563`. Maintains the long input-byte counter (`anInt566`/`anInt567`). |
| `method231(BZip2State)` | `:547` | Builds the compact symbol map `aByteArray591` from `aBooleanArray589` and sets `anInt588` to the symbol count. |
| `method232(int[] limit, int[] base, int[] perm, byte[] lens, int minLen, int maxLen, int alphabetSize)` | `:559` | **Canonical Huffman table builder.** Sorts symbols by code length into `perm`, counts code lengths into `base`, converts to a cumulative prefix, computes `limit[i] = firstcode + count - 1` (the canonical max-code-at-length array), and adjusts `base` for the canonical-decode subtraction trick used in `method227:352-356`. |

### BZip2 callers

- `cache/Archive.java:36` — `BZip2Decompressor.method225(abyte1, i, abyte0, j, 6)` decompresses the archive payload. The trailing `6` is the unused `unusedHeaderBytes` slot — `method225` never reads it.
- `cache/Archive.java:78` — `BZip2Decompressor.method225(abyte0, anIntArray729[k], aByteArray726, anIntArray730[k], anIntArray731[k])` decompresses individual sub-files inside an "individually-compressed" archive.

---

## GameServerEndpoint.java

Source: `/home/akira/projects/moparscape/server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameServerEndpoint.java`

### Overview

Modern, non-obfuscated Java `record` (`:6`) wrapping a `(host, port)` pair used by the desktop runtime to resolve the gameserver address from a configured `host[:port]` string. **It is not a world-list parser** — there is no list, no protocol parsing, no HTTP fetch. The default address is `127.0.0.1:43594` (`:8`); the default port (used when the string contains no colon) is `43594` (`:9`).

### Methods

| Method | Line | Description |
| --- | --- | --- |
| `GameServerEndpoint(String host, int port)` | `:6` | Record canonical constructor. |
| `host()` / `port()` | (generated) | Record accessors. |
| `static fromConfiguredAddress(String)` | `:11` | Normalises null/blank to `DEFAULT_ADDRESS`, splits on the first `:`; if no separator returns `(input, DEFAULT_PORT)`; otherwise returns `(substring before ':', parseConfiguredPort(input, DEFAULT_PORT))`. |
| `toCodeBaseUrl()` | `:25` | Returns `new URL("http://" + host)`, used to satisfy applet-style code-base requirements. Throws `MalformedURLException`. |
| `private static parseConfiguredPort(String, int fallback)` | `:29` | Uses `lastIndexOf(':')` (so an IPv6-style address with multiple colons takes the trailing segment as the port), defends against trailing `:` and `NumberFormatException`, returns `fallback` on either. |

### Callers in `GameClientCore.java`

- `:328-330` — `getConfiguredServerEndpoint()` wraps `fromConfiguredAddress(configuredServerAddress)`.
- `:2506`, `:2519`, `:9133`, `:9175`, `:10385` — `GameServerEndpoint.DEFAULT_ADDRESS` used as the standalone-runtime fallback.
- `:2534` — `fromConfiguredAddress(serverAddress).host()` used as the socket host.
- `:2579` — `fromConfiguredAddress(serverAddress)` resolves the endpoint for the login dispatcher.

Documented in `GameClientCore-A.md:154-158`, `GameClientCore-B.md:159-185`, and `GameClientCore-B.md:386` (listed as an extracted modern helper).

---

## Cross-class call graph

```
                      ┌────────────────────────────────────────┐
                      │            GameClientCore              │
                      │                                        │
   login flow ───►    │  - getConfiguredServerEndpoint() :328 ─┼──► GameServerEndpoint.fromConfiguredAddress
                      │  - initializeStandaloneClientRuntime   │      └─► .host()       (socket connect)
                      │      :2506/:2519/:2534/:2579           │      └─► DEFAULT_ADDRESS, DEFAULT_PORT
                      │                                        │
   session keys ──►   │  - method84 login handshake :5980/5984 ┼──► new IsaacCipher(-436, ai)  (outbound)
                      │                                        │  └► new IsaacCipher(-436, ai+50) (inbound)
                      │                                        │      (canonical ISAAC; method248 key-schedule)
                      │                                        │
   per-tick read ──►  │  - method84 packet loop                │
                      │      opcode read :9415-9416            ┼──► aClass17_1000.method246()
                      │      switch(anInt1008) :9515-9851      ┼──► IncomingPacketDispatcher.*
                      │        (see table above for per-opcode │      (reads PacketBuffer aClass30_Sub2_Sub2_1083,
                      │         mappings; full opcode table in │       mutates Widget.aClass9Array210 and
                      │         GameClientCore-E.md:202-256)   │       shared arrays in GameClientCore)
                      │                                        │
   cache load    ──►  │  (not direct — via Archive)            │
                      └────────────────────────────────────────┘
                                       │
                                       ▼
              ┌────────────────────────────────────────┐
              │            cache/Archive               │
              │  :36  BZip2Decompressor.method225(...) │──► whole-archive decompress
              │  :78  BZip2Decompressor.method225(...) │──► per-sub-file decompress
              └────────────────────────────────────────┘
                                       │
                                       ▼
              ┌────────────────────────────────────────┐
              │  BZip2Decompressor (sync on singleton) │
              │  method225 ─► method227 (block loop)   │
              │              ├─ method228/229/230 (bits)│
              │              ├─ method231 (symbol map) │
              │              ├─ method232 (Huffman)    │
              │              └─ method226 (BWT + RLE-2)│
              │  state held in BZip2State aClass32_305 │
              └────────────────────────────────────────┘
```

Symbols that connect the four files:
- `IsaacCipher` is constructed inside `GameClientCore.method84` login handshake and is then read once per inbound packet to deobfuscate the opcode that drives the `switch` over `IncomingPacketDispatcher` helpers.
- `IncomingPacketDispatcher` is purely a helper for that `switch`; no other class references it.
- `BZip2Decompressor`/`BZip2State` are only invoked by `cache/Archive` and operate on a JVM-singleton state object; they do not touch the network path.
- `GameServerEndpoint` is the only modern, non-obfuscated piece — used at login bootstrap and as the source of `DEFAULT_ADDRESS`.
