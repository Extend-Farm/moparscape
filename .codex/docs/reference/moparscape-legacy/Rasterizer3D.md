# Rasterizer3D (legacy moparscape)

## Overview

- **Source path**: [`server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Rasterizer3D.java`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Rasterizer3D.java)
- **Line count**: 2247 lines
- **Superclass**: `Rasterizer2D` (provides the destination pixel buffer `anIntArray1378`, frame width `anInt1379`, height `anInt1380`, and the clip bounds `anInt1382` (bottom) / `anInt1385` (right)).
- **Purpose**: This is the legacy software 3D triangle rasterizer used by the original 317-era client. It owns the global state for the row-table that converts a screen Y to a pixel-buffer offset, the precomputed reciprocal / sin / cos tables, the texture cache (decoded ARGB textures with three pre-shaded brightness levels), the global HSL palette `anIntArray1482` (the 65536-entry `hsl16-to-rgb` lookup table), and the inner row-fill routines used by `SceneGraph` and `Model` to draw flat-shaded, Gouraud-shaded and textured triangles. It does not own per-frame geometry: it only consumes the projected screen-space coordinates the caller supplies.
- **Key concepts**:
  - **HSL16 palette**: Models and tiles store colour as a packed 16-bit `(hue<<10) | (saturation<<7) | luminance` index. `anIntArray1482[hsl16]` is the precomputed ARGB after gamma correction (`method372` builds it; `method373` is the gamma-pow helper).
  - **Row table `anIntArray1472`**: `anIntArray1472[y]` is the byte offset of scanline `y` inside `Rasterizer2D.anIntArray1378`. Populated by `method364` / `method365`.
  - **Texture cache**: 50 indexed source sprites in `aClass30_Sub2_Sub1_Sub2Array1474`, decoded to RGB in `anIntArrayArray1483`, then per-texture expanded into a 4-level (or 64×64-tiled low-mem) lookup `anIntArrayArray1479[texId]` by `method371`. The four levels are full-bright, ~7/8, ~3/4, ~5/8 brightness (`>>>` 0/1/2/3 used as the brightness selector via `j1 >> 23`).
  - **Low-memory mode `aBoolean1461`**: when true, textures are decoded at 64×64 with palette holes preserved (16384 entries × 4 levels = 65536), and the sampler uses 6-bit U/V (masks `0xfc0` + `>>6`). When false, textures are 128×128 (16384 entries × 4 levels = 65536 also; the high-detail path uses 7-bit U/V with masks `0x3f80` + `>>7`).
  - **Brightness/alpha flags**:
    - `aBoolean1462` — *clip horizontal*: when true, `method375`/`method377`/`method379` clamp the scanline against `Rasterizer2D.anInt1385`.
    - `aBoolean1463` — *opaque texture*: set in `method378` from `!aBooleanArray1475[texId]`; selects the unrolled "no transparent texel" inner loop in `method379`.
    - `aBoolean1464` — *interpolate over 4-pixel spans*: when true the Gouraud row routine `method375` advances colour every 4 pixels (perspective-correct-style chunking). `ItemDefinition.method___` toggles it off for HUD item icons (per-pixel shade).
    - `anInt1465` — global alpha 0..255 for translucent triangles (0 = opaque, otherwise dest = src·(256-α)/256 + dst·α/256).
  - **Texture animation flags / state**: `anInt1480[texId]` is an LRU timestamp updated by `method371`; `anInt1481` is the monotonic clock. `anIntArrayArray1478` is the free-list of decoded texture buffers (capacity `anInt1477`).
  - **Texture animation cooldown**: `anInt1458 = -436` / `anInt1459 = -477` and `aBoolean1460 = true` appear to be vestigial obfuscation-noise fields (no semantically-meaningful reads outside the dead `while` loops); see notes per method.

## Static fields

### Texture cache fields

| Legacy name | Proposed name | Type | Purpose |
|---|---|---|---|
| `aClass30_Sub2_Sub1_Sub2Array1474` | `textureSourceSprites` | `IndexedSprite[50]` | Source indexed-colour textures loaded from the `textures` archive by `method368`. |
| `anIntArrayArray1483` | `textureSourcePalettes` | `int[50][]` | Per-texture RGB palette after gamma correction by `method372` (one ARGB int per palette index). |
| `aBooleanArray1475` | `textureHasTransparency` | `boolean[50]` | True if any decoded texel is 0 (transparent). Computed inside `method371`; consumed by `method378` to set `aBoolean1463`. |
| `anIntArray1476` | `textureAverageColor` | `int[50]` | Memoised average ARGB for each texture (lazily filled by `method369`). Used by minimap (see `MapRegion.java:281`). |
| `anIntArrayArray1479` | `textureTexels` | `int[50][]` | Per-texture decoded ARGB texel arrays (size 16384 or 65536 incl. brightness levels). `null` until first use; recycled via `anIntArrayArray1478`. |
| `anIntArrayArray1478` | `texelBufferPool` | `int[anInt1477][]` | Free-list of recyclable texel buffers (sized 16384 in low-mem mode or 65536 otherwise). |
| `anInt1477` | `texelBufferPoolSize` | `int` | Current count of free buffers in `texelBufferPool` (treated as a stack). |
| `anInt1473` | `loadedTextureCount` | `int` | Number of textures successfully loaded from the archive (set in `method368`). |
| `anIntArray1480` | `textureLastUseTick` | `int[50]` | LRU timestamp for each texture (written by `method371`). |
| `anInt1481` | `textureClock` | `int` | Monotonic counter incremented every `method371` call. |
| `aBoolean1461` | `lowMemoryTextures` | `boolean` | True → 64×64 texture variant + 4096-entry brightness levels. Toggled from `GameClientCore` (low-detail mode). |

### Palette / lookup fields

| Legacy name | Proposed name | Type | Purpose |
|---|---|---|---|
| `anIntArray1482` | `hslToRgbPalette` | `int[65536]` | The 16-bit HSL → ARGB lookup; rebuilt by `method372` with the current gamma. Indexed by `hsl16` everywhere triangle colours are sampled. |
| `anIntArray1468` | `reciprocal16` | `int[512]` | `32768 / i` table — used as a 1/Δx reciprocal for Gouraud span lengths in `method375` / `method379`. |
| `anIntArray1469` | `reciprocal65536` | `int[2048]` | `65536 / i` table — exposed to `Model` (assigned into `Model.anIntArray1692`). |
| `anIntArray1470` | `sinTable` | `int[2048]` | `(int)(65536·sin(k·2π/2048))` — exposed to `Model` (`Model.anIntArray1689`) and used by `MapRegion`. |
| `anIntArray1471` | `cosTable` | `int[2048]` | `(int)(65536·cos(k·2π/2048))` — exposed to `Model` (`Model.anIntArray1690`) and used by `MapRegion`. |

### Viewport / row table fields

| Legacy name | Proposed name | Type | Purpose |
|---|---|---|---|
| `anIntArray1472` | `scanlineOffsets` | `int[height]` | `scanlineOffsets[y] = y · width`. Rebuilt by `method364` (full-screen) or `method365` (sub-rect). |
| `anInt1466` | `viewportCenterX` | `int` | Half the active viewport width; used as the screen-space X origin in `method378` and externally by `SceneGraph`/`Model` for projection. |
| `anInt1467` | `viewportCenterY` | `int` | Half the active viewport height; Y origin (see `method378` — `k8 = i - anInt1467`). |

### Render-state fields

| Legacy name | Proposed name | Type | Purpose |
|---|---|---|---|
| `anInt1465` | `globalAlpha` | `int` | 0..255 alpha (0 = opaque). Per-triangle, set by callers before each `method374`/`method376`/`method378`. |
| `aBoolean1462` | `clipScanlineToRight` | `boolean` | Toggled per-triangle by callers to enable right-edge clipping inside the row fillers. |
| `aBoolean1463` | `textureIsOpaque` | `boolean` | Set inside `method378` from `!textureHasTransparency[texId]`; selects the fast opaque inner loop in `method379`. |
| `aBoolean1464` | `chunkedGouraud` | `boolean` | When true, `method375` interpolates colour over 4-pixel runs (the OSRS default). Disabled by `ItemDefinition` for per-pixel sharpness on inventory icons. |

### Vestigial / noise fields

| Legacy name | Type | Notes |
|---|---|---|
| `anInt1458` | `int` (init `-436`) | Only written inside a dead `while(j >= 0) anInt1458 = 7;` loop in `method370`. No meaningful read. |
| `anInt1459` | `int` (init `-477`) | Never read or written; appears only as the literal `-477` passed to `method370(i, -477)` (the second parameter is unused). |
| `aBoolean1460` | `boolean` (init `true`) | Only toggled inside `method368` guarded by `if(i != 0)` (called only with `i = 0`); never read. Obfuscation noise. |

## Methods

All methods are `public static final` unless noted. Listed in source order.

---

### `method363(int i)` — proposed name: `releaseAllTextureMemory`

- **Signature**: `public static final void method363(int i)`
- **Purpose**: Nulls every cache reference (textures, palettes, buffers, row table) so the GC can reclaim the rasterizer's working set. Used when the client tears down 3D state.
- **Parameters**: `i` — unused (dummy obfuscation parameter; only used in a dead `for(int j = 1; j > 0; j++);` loop).
- **Returns**: void.
- **Called by**: [`GameClientCore.java:4756`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L4756) (`Rasterizer3D.method363(-501)`).
- **Notes**: The line `anIntArray1468 = null;` appears twice on lines 15–16 — a Jad decompiler glitch; one of them was probably `anIntArray1469`.

---

### `method364(byte byte0)` — proposed name: `resetScanlineOffsetsFromRasterizer2D`

- **Signature**: `public static final void method364(byte byte0)`
- **Purpose**: Allocates `scanlineOffsets` for the full `Rasterizer2D` framebuffer (`anInt1380` rows × `anInt1379` pitch) and sets `viewportCenter{X,Y}` to its centre.
- **Parameters**: `byte0` — obfuscation sentinel; must be `6` for the method to do useful work, otherwise the dead loop runs.
- **Called by**: [`ItemDefinition.java:310`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ItemDefinition.java#L310) (`Rasterizer3D.method364((byte)6)`) — switches the rasterizer to draw item icons into the full sprite buffer.

---

### `method365(int i, int j, int k)` — proposed name: `setViewportScanlineOffsets`

- **Signature**: `public static final void method365(int i, int j, int k)`
- **Purpose**: Allocates `scanlineOffsets` for a viewport of pitch `j` and height `k`, with `viewportCenter{X,Y} = (j/2, k/2)`.
- **Parameters**: `i` = obfuscation sentinel (the early-return `if(i >= 0) return;` guard means callers always pass a negative); `j` = stride in pixels (also viewport width); `k` = viewport height.
- **Called by**: [`BootstrapGraphicsSetup.java:64,66,68`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L64) with the three game viewports (`479×96`, `190×261`, `512×334`).
- **Notes**: The `for(anIntArray1472 = new int[k]; i >= 0;) return;` is a Jad-mangled way of writing `if (i >= 0) return; else anIntArray1472 = new int[k];`.

---

### `method366(int i)` — proposed name: `clearTextureCaches`

- **Signature**: `public static final void method366(int i)`
- **Purpose**: Drops the pooled texel buffers (`textureTexels = null` for all 50) and clears the texel buffer pool reference. Called when the engine wants to fully invalidate cached textures (e.g. after a brightness / gamma change).
- **Parameters**: `i` — must be `0` (guard `if(i < 0 || i > 0) return;`).
- **Called by**: [`GameClientCore.java:475`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L475).

---

### `method367(int i, boolean flag)` — proposed name: `initTextureBufferPool`

- **Signature**: `public static final void method367(int i, boolean flag)`
- **Purpose**: Lazily allocates the texel buffer pool with `i` buffers, sized either 16384 (low-mem) or 65536 (default), and resets every per-texture decoded buffer slot.
- **Parameters**: `i` = pool capacity (callers use `20`); `flag` = obfuscation guard (must be `true` to skip the dead loop).
- **Called by**: [`BootstrapConfigLoader.java:16`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L16), [`GameClientCore.java:657`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L657).

---

### `method368(Archive class44, int i)` — proposed name: `loadTextureArchive`

- **Signature**: `public static final void method368(Archive class44, int i)`
- **Purpose**: Loads up to 50 textures from the supplied archive (one `IndexedSprite` per file numbered `"0".."49"`); for each one calls `method356`/`method357` on the sprite (the indexed-sprite vertical-flip / orientation step). Increments `loadedTextureCount` per successful load. Swallows per-texture exceptions.
- **Parameters**: `class44` = the `textures` archive; `i` = obfuscation parameter (only `0` is used at call sites; non-zero would flip `aBoolean1460`).
- **Called by**: [`BootstrapConfigLoader.java:14`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L14).
- **Notes**: The branch `aClass30_Sub2_Sub1_Sub2Array1474[j].anInt1456 == 128` calls the 64×64 prep path (`method356(false)`) when low-mem is active.

---

### `method369(int i, int j)` — proposed name: `getTextureAverageColor`

- **Signature**: `public static final int method369(int i, int j)`
- **Purpose**: Returns the average ARGB of texture `i`, gamma-corrected with `1.4`. Memoised in `textureAverageColor[i]`. Used by the minimap to colour ground tiles whose underlay is a texture.
- **Parameters**: `i` = texture id; `j` = obfuscation guard (must equal `12660`, else returns `2`).
- **Returns**: gamma-corrected average ARGB (non-zero — coerced to `1` if the average is `0`).
- **Called by**: [`MapRegion.java:281`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L281).
- **Calls**: `method373` (gamma).

---

### `method370(int i, int j)` — proposed name: `releaseTextureTexels`

- **Signature**: `public static final void method370(int i, int j)`
- **Purpose**: Returns the decoded texel buffer for texture `i` back to the pool and nulls the slot. Triggered both internally (`method372` after rebuilding the palette) and by the client when a texture should be reloaded.
- **Parameters**: `i` = texture id; `j` = unused (obfuscation; the `while(j >= 0) anInt1458 = 7;` loop is dead because all call sites pass `-477`).
- **Called by**: [`GameClientCore.java:1645,1679,1693`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1645) (specific texture ids 17, 24, 34 — animated water/lava textures), and internally from `method372`.

---

### `method371(int i)` — proposed name: `getOrDecodeTextureTexels`

- **Signature**: `public static final int[] method371(int i)`
- **Purpose**: Returns the decoded texel array for texture `i`. If absent, picks a buffer from the pool (or evicts the LRU texture's buffer), then expands the indexed source sprite through the gamma-corrected palette into 4 brightness levels (`>>>0`, `>>>3`, `>>>2`, `>>>2 + >>>3`) packed into the 65536-int (or 16384-int low-mem) buffer. Also updates `textureHasTransparency[i]` (true if any decoded texel is 0).
- **Parameters**: `i` = texture id.
- **Returns**: the decoded texel array (the same array stored in `textureTexels[i]`).
- **Called by**: `method378` (every textured triangle draw).
- **Notes**:
  - Low-mem branch (`aBoolean1461 == true`): 4096 texels × 4 levels = 16384 entries; mask `0xf8f8ff` strips the lowest 3 bits per channel so the brightness math stays inside 8-bit per channel.
  - High-detail branch: if source is 64×64 (`anInt1452 == 64`) it nearest-neighbour upscales to 128×128 before applying the brightness levels.

---

### `method372(double d, byte byte0)` — proposed name: `buildHslPaletteWithGamma`

- **Signature**: `public static final void method372(double d, byte byte0)`
- **Purpose**: Rebuilds the 65536-entry `hslToRgbPalette` (`anIntArray1482`) using gamma exponent `d`. Iterates 512 (hue,saturation) pairs × 128 luminance values, computes RGB via the standard HSL→RGB algorithm, gamma-corrects with `method373`, and writes the result. Also re-gamma-corrects each loaded texture palette (`textureSourcePalettes[t]`) and releases their decoded texel buffers so they will be re-decoded with the new palette.
- **Parameters**: `d` = gamma exponent (typical values `0.6`..`0.9`); `byte0` = obfuscation guard (must be `9`).
- **Called by**: [`BootstrapConfigLoader.java:15`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L15), [`GameClientCore.java:1240,1242,1244,1246`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1240) (user brightness setting).
- **Calls**: `method373`, `method370`.
- **Notes**: Adds a small jitter `±0.015` to the gamma each call (`Math.random()`). HSL layout: top 9 bits = (hue<<3 | sat-fraction), low 7 bits = luminance — matching the layout of OSRS' `Model` colour-format used by `SceneGraph`/`Model`.

---

### `method373(int i, double d)` — proposed name: `applyGammaToRgb`

- **Signature**: `public static int method373(int i, double d)`
- **Purpose**: Applies `pow(channel/256, d)·256` independently to R, G, B and returns the packed RGB.
- **Parameters**: `i` = packed RGB888; `d` = gamma exponent.
- **Returns**: gamma-corrected packed RGB888.
- **Called by**: `method369`, `method372`.
- **Notes**: Pure helper — no side effects.

---

### `method374(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2)` — proposed name: `drawGouraudTriangle`

- **Signature**: `public static final void method374(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2)`
- **Purpose**: Rasterizes a Gouraud-shaded (HSL-interpolated, non-textured) triangle by sorting the three vertices by Y, computing edge X gradients (`<<16`) and HSL gradients (`<<15`), clipping to the framebuffer, then iterating scanlines and calling `method375` for each row span.
- **Parameters**:
  - `i, j, k` — vertex screen Y coordinates (vertex A/B/C).
  - `l, i1, j1` — vertex screen X coordinates.
  - `k1, l1, i2` — vertex HSL16 colour indices.
- **Returns**: void.
- **Called by**: `SceneGraph.method___` for solid tiles (e.g. [`SceneGraph.java:1758,1769,1786,1797,1857,1868`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1758)); `Model.method___` (e.g. [`Model.java:1842,1974,2007,2008`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1842)).
- **Calls**: `method375` for each scanline; reads `scanlineOffsets`, writes through `Rasterizer2D.anIntArray1378`.
- **Notes**: This is the classic 3-way Y-sort, 6-case triangle rasterizer. Edge "swaps" (e.g. `if(i != j && j3 < j2 || i == j && j3 > l2)`) decide whether the left or right edge owns the steeper gradient and pick the correct `>>16` ordering for the `method375` call.

---

### `method375(int ai[], int i, int j, int k, int l, int i1, int j1, int k1)` — proposed name: `fillGouraudScanline`

- **Signature**: `public static final void method375(int ai[], int i, int j, int k, int l, int i1, int j1, int k1)`
- **Purpose**: Inner scanline fill for `method374`. Looks up `hslToRgbPalette[colour >> 8]` per pixel and writes (or alpha-blends if `globalAlpha != 0`) into `ai`.
- **Parameters**: `ai` = destination buffer (always `Rasterizer2D.anIntArray1378`); `i` = current row offset (precomputed by caller as `scanlineOffsets[y]`); `j` = scratch (caller passes 0; reused as colour register); `k` = scratch / loop counter; `l, i1` = span start/end X; `j1, k1` = HSL16 values at start/end (`<<7` shifted by caller via `>> 7`).
- **Called by**: `method374`.
- **Notes**:
  - When `chunkedGouraud` is true, colour is interpolated in 4-pixel chunks (`l1 = (k1 - j1) * anIntArray1468[k] >> 15`) — the OSRS default for world geometry.
  - When false, every pixel computes its own gradient (`i2 = (k1 - j1) / (i1 - l)`).
  - Alpha path uses the per-channel split `((c & 0xff00ff) * w >> 8) & 0xff00ff` trick (interleaved R/B parallel mul).

---

### `method376(int i, int j, int k, int l, int i1, int j1, int k1)` — proposed name: `drawFlatTriangle`

- **Signature**: `public static final void method376(int i, int j, int k, int l, int i1, int j1, int k1)`
- **Purpose**: Rasterizes a flat-shaded triangle (single ARGB colour). Same Y-sorted, 6-case edge structure as `method374` but with no colour gradient — calls `method377` per scanline.
- **Parameters**:
  - `i, j, k` — vertex screen Y.
  - `l, i1, j1` — vertex screen X.
  - `k1` — solid ARGB colour to fill with.
- **Called by**: `Model.method___` ([`Model.java:1847,1977,2014,2015`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1847)). Used for faces flagged "no shade" — the caller pre-looks-up the HSL via `anIntArray1691[anIntArray1634[i]]` (which is `Rasterizer3D.anIntArray1482`).

---

### `method377(int ai[], int i, int j, int k, int l, int i1)` — proposed name: `fillFlatScanline`

- **Signature**: `public static final void method377(int ai[], int i, int j, int k, int l, int i1)`
- **Purpose**: Fills a horizontal span `[l, i1)` of `ai` with colour `j`. Unrolled 4×. Honours `aBoolean1462` (right-clip) and `anInt1465` (alpha).
- **Called by**: `method376`.

---

### `method378(int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3, int j3, int k3, int l3, int i4, int j4, int k4)` — proposed name: `drawTexturedTriangle`

- **Signature**: as above (19 ints).
- **Purpose**: Rasterizes a textured, Gouraud-lit (single colour selected per pixel from a 4-level brightness texture) triangle. Computes the three texture-space anchor vectors from the supplied 3D anchor points (`j2, k2, l2` etc. are the world-space P/M/N coordinates the texture is anchored to in the source model), builds the three 2D projection coefficients (`l4, i5, j5`, etc.), then performs the same Y-sorted 6-case scanline walk and calls `method379` per scanline.
- **Parameters**:
  - `i, j, k` — vertex screen Y.
  - `l, i1, j1` — vertex screen X.
  - `k1, l1, i2` — HSL16 colour per vertex (used only to derive the texture brightness shift: `j1 >> 23`).
  - `j2, i3, l3` — texture anchor P (origin) X, Y, Z in camera/view space.
  - `k2, j3, i4` — texture anchor M (U-axis end) X, Y, Z.
  - `l2, k3, j4` — texture anchor N (V-axis end) X, Y, Z.
  - `k4` — texture id; first thing the method does is `int ai[] = method371(k4)`.
- **Called by**: `SceneGraph.method___` ([`SceneGraph.java:1763,1765,1793,1862,1864`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1763)); `Model.method___` ([`Model.java:1856,1865,1985,1993,2024,2025,2034,2035`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1856)).
- **Calls**: `method371` (texture decode), `method379` (per-scanline fill).
- **Notes**:
  - The math `l4 = l2 * i3 - k3 * j2 << 14` etc. produces the three pairs of plane coefficients used by `method379` to recover affine-perspective texture coordinates per scanline: it's the standard "compute U/Z, V/Z, 1/Z gradients in screen space".
  - Vertex 1-based deltas are recovered first (`k2 = j2 - k2;` overwrites the param with the M-P delta, etc.) — this is the canonical OSRS texture-anchor encoding.
  - The brightness selector `i8 = j1 >> 23` extracts which of the 4 pre-shaded texture levels to read from (top bits of the HSL16 << 9 luminance).

---

### `method379(int ai[], int ai1[], int i, int j, int k, int l, int i1, int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3)` — proposed name: `fillTexturedScanline`

- **Signature**: as above (15 args).
- **Purpose**: Inner scanline fill for `method378`. Performs perspective-correct texture mapping: divides `(U, V, 1/Z)` every 8 pixels and linearly interpolates `(u, v)` between divisions (the classic Quake-era 8-pixel affine span trick). Reads texels from `ai1` (the 4-level texture lookup), shifts right by the brightness selector `i8 = j1 >> 23`, and writes to `ai`.
- **Parameters**: `ai` = framebuffer; `ai1` = decoded texture (from `method371`); `i, j` = current `(u, v)` in 12-bit fractional (init from caller); `k` = row offset; `l, i1` = span start/end X; `j1, k1` = colour/brightness words at start/end of span (with the texture-level selector in the top bits); `l1, i2, j2` = the U/Z, V/Z, 1/Z accumulators (seeded from `method378`'s `l4, k5, j6`); `k2, l2, i3` = their per-pixel deltas (seeded from `i5, l5, k6`).
- **Called by**: `method378`.
- **Notes**:
  - Two structural branches: `aBoolean1461` (low-mem 64×64 textures, mask `0xfc0`, shift `>>6`) vs default 128×128 (mask `0x3f80`, shift `>>7`).
  - Within each, two sub-branches: `aBoolean1463` (opaque texture — fast straight write) vs translucent (test for texel != 0 before write — the "magenta = transparent" stencil after the colour has been multiplied through the palette).
  - The clamp `if(i4 < 7) i4 = 7; else if(i4 > 16256) i4 = 16256;` is a guard against degenerate perspective division at silhouette edges (prevents wild UV in dust-thin tris).

---

## Static initializer

Initialises `reciprocal16` (`32768/i`), `reciprocal65536` (`65536/i`), `sinTable` and `cosTable` (`65536·sin/cos(k·2π/2048)`). No method dependencies.

## Cross-class call graph

External callers, grouped by class.

### `BootstrapConfigLoader.java`
- [`:14`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L14) → `method368(texturesArchive, 0)` (load textures)
- [`:15`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L15) → `method372(0.8, brightnessByte)` (build palette)
- [`:16`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapConfigLoader.java#L16) → `method367(20, true)` (init texel pool)

### `BootstrapGraphicsSetup.java`
- [`:64,66,68`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/BootstrapGraphicsSetup.java#L64) → `method365(-950, w, h)` (per-viewport row table)

### `GameClientCore.java`
- [`:475`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L475) → `method366(anInt846)` (clear texture caches)
- [`:657`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L657) → `method367(20, true)` (init texel pool)
- [`:1240,1242,1244,1246`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1240) → `method372(gamma, byte)` (rebuild palette on brightness change)
- [`:1645,1679,1693`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L1645) → `method370(textureId, -477)` (release animated textures 17/24/34)
- [`:2489,9041`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L2489) → write `aBoolean1461` (toggle low-mem textures)
- [`:4756`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/GameClientCore.java#L4756) → `method363(-501)` (full teardown)

### `ItemDefinition.java`
- [`:307`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ItemDefinition.java#L307) → write `aBoolean1464 = false` (per-pixel Gouraud for item icons)
- [`:310`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ItemDefinition.java#L310) → `method364((byte)6)` (use full sprite buffer as render target)
- [`:386`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/ItemDefinition.java#L386) → write `aBoolean1464 = true` (restore)

### `SceneGraph.java`
- [`:1736–1743`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1736) → read `anInt1466`, `anInt1467` (project shape-tile UV anchors)
- [`:1744,1831`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1744) → write `anInt1465 = 0` (opaque alpha for tile fill)
- [`:1747,1749,1774,1776,1846,1848`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1747) → write `aBoolean1462` (per-tile clip flag)
- [`:1758,1769,1786,1797,1857,1868`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1758) → `method374(...)` (Gouraud tile triangle)
- [`:1763,1765,1793,1862,1864`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/SceneGraph.java#L1763) → `method378(...)` (textured tile triangle)

### `Model.java`
- [`:1458,1459,1573,1574,1581,1582,1871,1872`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1458) → read `anInt1466/anInt1467` (project vertices to screen)
- [`:1830,1963,1967,1999`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1830) → write `aBoolean1462` (per-face clip flag)
- [`:1832,1834`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1832) → write `anInt1465` (per-face alpha)
- [`:1842,1974,2007,2008`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1842) → `method374(...)` (Gouraud face)
- [`:1847,1977,2014,2015`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1847) → `method376(...)` (flat-shaded face)
- [`:1856,1865,1985,1993,2024,2025,2034,2035`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L1856) → `method378(...)` (textured face)
- [`:2135,2136,2137,2138`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Model.java#L2135) → alias `anIntArray1470/1471/1482/1469` into `Model.anIntArray1689/1690/1691/1692` (`sin/cos/hslToRgb/reciprocal`).

### `MapRegion.java`
- [`:269,289`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L269) → read `anIntArray1482[hsl16]` (minimap underlay colour)
- [`:281`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L281) → `method369(textureId, 12660)` (minimap textured underlay colour)
- [`:1057`](../../../../server/moparscape/src/main/java/io/github/ffakira/moparscape/client/MapRegion.java#L1057) → read `anIntArray1471[...]` (cosine table for blending math)
