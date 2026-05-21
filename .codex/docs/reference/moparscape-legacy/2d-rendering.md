# 2D rendering stack (legacy moparscape)

This document is a 1:1 reference for the four classes that make up the legacy
moparscape 2D renderer:

- `Rasterizer2D` — global pixel buffer, clipping rect, low-level rect/line draws.
- `Sprite` — ARGB sprite (one `int[]` per pixel) with multiple loaders, blits,
  alpha blends, and rotated draws.
- `IndexedSprite` — 8-bit palette-indexed sprite (one `byte[]` index + shared
  `int[]` palette) with cheap blits and palette recolouring.
- `FontRenderer` — glyph-stamping bitmap font with markup, shake/wave effects,
  shadow, and underline.

All four classes are obfuscated (`methodNNN`, `anIntNNN`). Names here are
inferred from data flow, callers in `GameClientCore.java`,
`WidgetRenderHandler.java`, `SceneGraph.java`, and `Rasterizer3D.java`, and the
constants involved.

Source files referenced:
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Rasterizer2D.java`
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/Sprite.java`
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/IndexedSprite.java`
- `server/moparscape/src/main/java/io/github/ffakira/moparscape/client/FontRenderer.java`

---

## Rasterizer2D.java

### Overview

Length: 280 lines. The base class for all 2D drawing. It owns the
**global frame buffer** (`anIntArray1378`) plus a **scissor rectangle**
(`anInt1381..anInt1384`), and exposes static rect-fill, alpha-rect-fill, and
single-pixel scanline helpers. `Sprite`, `IndexedSprite`, `FontRenderer` and
`Rasterizer3D` all extend it (or read its statics) so they share the same
target buffer and clip box.

This class never holds per-instance state; the constructor at
`Rasterizer2D.java:261` is empty. It also extends `CacheableNode`, so a
`Rasterizer2D` reference can be parked in the `Cache` LRU like everything else.

### Static fields (Rasterizer2D.java:265-279)

- `anInt1374` (line 265, default `1`) — opaque scratch int. Written from
  many branches that never execute (`while(k >= 0) anInt1374 = 275;` at
  `Rasterizer2D.java:16`); a Jad/obfuscator dummy used as a "side effect" to
  defeat dead-code elimination. Not functionally meaningful.
- `aBoolean1375` (line 266, default `true`) — unused dummy.
- `anInt1376` (line 267, default `-12499`) — sentinel value compared by
  `method341` (`Rasterizer2D.java:224`); calls must pass `anInt1376` or the
  draw is silently skipped. Acts as an obfuscation guard.
- `aBoolean1377` (line 268) — toggled by `method334` (line 64) when its
  guard parameter is `false`. Read nowhere; observable side-effect only.
- `anIntArray1378[]` (line 269) — **the global ARGB framebuffer**. All 2D
  pixel writes go here. Initialised by `method331` from a `Sprite`-owned
  array (`Rasterizer2D.java:15`) or set externally by callers that swap in
  off-screen buffers.
- `anInt1379` (line 270) — frame buffer **width** (stride for `y * width`).
- `anInt1380` (line 271) — frame buffer **height**.
- `anInt1381` (line 272) — scissor **top** (inclusive Y min).
- `anInt1382` (line 273) — scissor **bottom** (exclusive Y max).
- `anInt1383` (line 274) — scissor **left** (inclusive X min).
- `anInt1384` (line 275) — scissor **right** (exclusive X max).
- `anInt1385` (line 276) — `anInt1384 - 1`, cached for the 3D rasterizer
  (used as the `> anInt1385` clip test in `Rasterizer3D` and `SceneGraph`,
  e.g. `SceneGraph.java:1748`).
- `anInt1386` (line 277) — scissor centre X (`anInt1384 / 2`), used by 3D
  projection.
- `anInt1387` (line 278) — scissor centre Y (`anInt1382 / 2`), used by 3D
  projection.
- `aBoolean1388` (line 279) — public flag exposed to callers (toggled
  externally; not written inside this file).

### Methods

#### `method331(int height, int width, int dummy, int[] pixels)` — `Rasterizer2D.java:13-21`

Installs a backing buffer as the global frame buffer. Sets
`anIntArray1378 = pixels`, `anInt1379 = width` (param `j`),
`anInt1380 = height` (param `i`), then calls `method333(i, 0, false, j, 0)`
to reset the scissor rect to cover the entire buffer. The `dummy` param `k`
is a Jad guard (the `while(k >= 0)` body is unreachable). Called from
`Sprite.method343` (`Sprite.java:133`), which retargets the framebuffer to
that sprite's pixels so subsequent draws write into the sprite.

#### `method332(int sentinel)` — `Rasterizer2D.java:23-38`

Resets the scissor rectangle to the full frame buffer:
`anInt1381 = anInt1383 = 0`, `anInt1384 = anInt1379`,
`anInt1382 = anInt1380`, recomputes `anInt1385` and `anInt1386`. The
`sentinel` must equal `4` or the call is a no-op (`if (i < 4 || i > 4)
return;`). Note: this **does not** recompute `anInt1387`, unlike
`method333`. Called from `GameClientCore.java:262`, `:1549`, `:1563`.

#### `method333(int height, int x, boolean flag, int width, int y)` — `Rasterizer2D.java:40-59`

Sets a clipped scissor rectangle. Clamps `x`/`y` to `[0, ...]`, clamps
`width`/`height` against the buffer extents, then writes
`anInt1383 = x`, `anInt1381 = y`, `anInt1384 = width`, `anInt1382 = height`
and recomputes `anInt1385`, `anInt1386`, `anInt1387`. `flag` is an
obfuscation guard (when true it writes `anInt1374 = 458`). Called from
`GameClientCore.java:165, 1546, 1560`.

#### `method334(boolean flag)` — `Rasterizer2D.java:61-69`

Clears the frame buffer to black: writes `0` to every pixel in
`anIntArray1378[0 .. anInt1379*anInt1380)`. `flag` is a guard; when
false, the dummy `aBoolean1377` is toggled. Called from
`GameClientCore.java:3193`, `3205`, `5796`, `10189` (start-of-frame
wipes).

#### `method335(int rgb, int y, int width, int height, int alpha, int dummy, int x)` — `Rasterizer2D.java:71-108`

Alpha-blended filled rectangle. Clips against the scissor box, decomposes
`rgb` and the existing buffer pixel into RGB channels, blends with
`alpha/256` (`l1 = 256 - alpha`), and writes the result. Param order is the
obfuscated signature; from the caller at `WidgetRenderHandler.java:99`
`alpha = 256 - (widget.aByte254 & 0xff)` indicates the legacy widget
"transparency" byte. `dummy` (`j1`) is unused except `if(j1 == 0);` at
line 107.

#### `method336(int height, int y, int x, int rgb, int width, int dummy)` — `Rasterizer2D.java:110-138`

Solid filled rectangle (no alpha). Clips against the scissor box and
writes `rgb` directly into every pixel. The trailing `dummy` (`j1`) sets
`anInt1374 = -374` when non-zero. Called from `WidgetRenderHandler.java:94`
for opaque widget backgrounds.

#### `method337(int y, int height, int width, int rgb, int x, boolean flag)` — `Rasterizer2D.java:140-147`

Rectangle outline (4 sides, 1 pixel). Composes `method339` (horizontal
scanline) for the left and right edges and `method341` (vertical
scanline) for the top and bottom edges, using `anInt1376` as the
sentinel that lets `method341` actually draw. `flag` is a no-op guard.
Called from `WidgetRenderHandler.java:96`.

#### `method338(int height, int width, int alpha, int rgb, int y, int x, int sentinel)` — `Rasterizer2D.java:149-162`

Alpha-blended rectangle outline. Draws the left and right edges via
`method340` (alpha vertical line), the top and bottom edges via
`method342` (alpha horizontal line), but only if `height >= 3`. `sentinel`
must not equal `-17319`, otherwise the function enters a no-op dummy
loop. Called from `WidgetRenderHandler.java:101`.

#### `method339(int y, int rgb, int width, int x, byte sentinel)` — `Rasterizer2D.java:164-181`

Horizontal scanline of `width` opaque pixels of colour `rgb` starting at
`(x, y)`. Returns early if `y` is outside vertical clip, clips
`width`/`x` against horizontal clip. `sentinel` must equal `4` or the
function returns. Used internally by `method337` and externally by
`FontRenderer.method389` to draw underlines (`FontRenderer.java:255`).

#### `method340(int rgb, int width, int y, boolean flag, int alpha, int x)` — `Rasterizer2D.java:183-210`

Horizontal alpha-blended scanline. Same as `method339` but blends
`rgb` against the existing buffer using `alpha/256`. `flag` toggles a
dummy write to `anInt1374` when false. Internal helper for `method338`.

#### `method341(int y, int rgb, int height, int x, int sentinel)` — `Rasterizer2D.java:212-229`

Vertical scanline of `height` opaque pixels of colour `rgb` starting at
`(x, y)`. Walks down by `anInt1379` (one row per step). Returns early
if `x` outside horizontal clip or `sentinel != anInt1376`. Internal
helper for `method337`.

#### `method342(int rgb, int x, int alpha, int y, byte sentinel, int height)` — `Rasterizer2D.java:231-259`

Vertical alpha-blended scanline. Blends `rgb` with the buffer along a
column. `sentinel` must equal `3`. Internal helper for `method338`.

#### `Rasterizer2D()` — `Rasterizer2D.java:261-263`

Empty constructor. Present so subclasses can chain to it.

---

## Sprite.java

### Overview

Length: 642 lines. `final class Sprite extends Rasterizer2D`. A 32-bit ARGB
sprite that stores a contiguous `int[]` of pixels. Provides three loaders
(blank, JPG/PNG bytes via AWT, palette-encoded cache file), four blit
variants (opaque, colour-key, alpha-blended, masked by indexed sprite),
two rotated-scaled variants, plus geometry helpers (recolour, expand to
content-box).

Pixels of value `0` (true black, fully transparent) are the colour-key
in all transparent draws; an "opaque black" colour-keyed draw is
indistinguishable from "no draw".

### Fields (Sprite.java:625-642)

- `aBoolean1428`, `anInt1429`, `anInt1430`, `anInt1431`, `aByte1432`,
  `aBoolean1433`, `anInt1434`, `aBoolean1435`, `aBoolean1436`,
  `aBoolean1437`, `aBoolean1438` — Jad/obfuscator dummies. Read or written
  only inside guard branches. The one with semantic meaning is
  `aBoolean1436` (line 633), which `method354` passes to its private
  helper to select a no-op branch.
- `anIntArray1439[]` (line 636) — ARGB pixel buffer. Length is
  `anInt1440 * anInt1441` (the **content** rectangle, not the
  declared frame).
- `anInt1440` (line 637) — content rectangle width.
- `anInt1441` (line 638) — content rectangle height.
- `anInt1442` (line 639) — content X offset within the **frame**.
- `anInt1443` (line 640) — content Y offset within the **frame**.
- `anInt1444` (line 641) — declared **frame** width (the bounding box
  the cache file claims; used by `method345` to expand the pixel array
  to the full frame).
- `anInt1445` (line 642) — declared frame height.

The cache-file loader (`Sprite.java:70-127`) reads the frame size from
`index.dat`, then a per-frame `(xoff, yoff, w, h)` so that small icons
can be packed tightly inside a large bounding box.

### Methods

#### `Sprite(int width, int height)` — `Sprite.java:17-33`

Blank constructor. Allocates an `int[width*height]` filled with zeros and
treats the whole buffer as the content rectangle: `anInt1440 = anInt1444
= width`, `anInt1441 = anInt1445 = height`, offsets `0`. Used for
off-screen render targets (callers swap the framebuffer to this sprite
via `method343` and draw into it).

#### `Sprite(byte[] bytes, Component component)` — `Sprite.java:35-68`

Loads a JPEG/PNG/GIF via AWT `Toolkit.createImage` + `PixelGrabber`. Uses
a `MediaTracker` to wait for decode, then copies pixels into
`anIntArray1439`. Content rect == frame rect == image size. Logs
"Error converting jpg" on failure and leaves the sprite blank. Used for
title-screen background and other JFIF assets.

#### `Sprite(Archive cache, String name, int index)` — `Sprite.java:70-127`

Cache-file loader, the main one used in-game. Opens
`<name>.dat` and `index.dat` from the supplied `Archive`. Reads the
shared palette length and a `int[]` palette (24-bit RGB entries; entry
0 stays 0 for transparency, entries that decode to 0 are bumped to 1
on `Sprite.java:92-93`). Skips `index` entries in the index file by
advancing the read cursors. Then reads:
- content `(xOff, yOff)` (`anInt1442`, `anInt1443`),
- content `(w, h)` (`anInt1440`, `anInt1441`),
- row order byte (`0` = row-major, `1` = column-major),
- `w*h` palette indices from `<name>.dat`, expanded to ARGB via the
  palette into `anIntArray1439`.

Frame size came from `anInt1444`/`anInt1445` read earlier (lines 85-86).

#### `method343(int dummy)` — `Sprite.java:129-134`

Retargets the global frame buffer to **this** sprite. Calls
`Rasterizer2D.method331(anInt1441, anInt1440, -293, anIntArray1439)`,
making subsequent 2D draws write into the sprite. Used to build
composite icons off-screen. `dummy` is a Jad guard. Caller:
`GameClientCore.java:723` (`aClass30_Sub2_Sub1_Sub1_1263.method343(0)`).

#### `method344(int dr, int dg, int db, int dummy)` — `Sprite.java:136-170`

Per-pixel colour shift. For each non-zero pixel, adds `(dr, dg, db)` to
its R/G/B channels (clamped to `[1, 255]`, **not** `[0, 255]` —
transparent pixels stay transparent because they're skipped by the
`if (j1 != 0)` check). Used for hue-shifting item icons. The `dummy`
param triggers a guard write when non-zero.

#### `method345(int sentinel)` — `Sprite.java:172-189`

Expands the content rectangle to the full frame rectangle: allocates a
new `int[anInt1444 * anInt1445]` zero-filled buffer, copies content
pixels into it at `(anInt1442, anInt1443)`, replaces
`anIntArray1439`, sets `anInt1440 = anInt1444`, `anInt1441 = anInt1445`,
and resets offsets to `0`. `sentinel` must equal `5059` or it writes the
`anInt1429` dummy. Used to normalise sprites before further processing.

#### `method346(int x, int y, int sentinel)` — `Sprite.java:191-238`

**Opaque blit**: draws the sprite at `(x + anInt1442, y + anInt1443)`
into the global frame buffer with full clipping against the scissor
rect. Pixel value `0` is **not** treated as transparent — every pixel
is copied. Delegates to the unrolled private helper `method347`.
`sentinel` must equal `anInt1431` (`-32357`) or `aBoolean1438` is
toggled. Used for fullscreen chrome / overlay sprites in
`GameClientCore.java:2702`+.

#### `method347(int dst, int width, int height, int srcStride, int srcOff, int sentinel, int dstStride, int[] src, int[] dst2)` — `Sprite.java:240-264`

Private inner loop for `method346`. Loop-unrolled by 4 (`-(width >> 2)`
groups of 4, then `-(width & 3)` tail). Pure copy: `dst2[i++] = src[j++]`.
`sentinel` must equal `28339` or the dummy `anInt1431` is poked.

#### `method348(int x, int sentinel, int y)` — `Sprite.java:266-313`

**Colour-key blit**: same clipping as `method346`, but draws via the
`method349` helper that skips pixels with value `0`. This is the
standard "draw sprite with transparency" call. `sentinel` must equal
`16083` (a Jad guard). Callers: `WidgetRenderHandler.java:44`, `:289`,
`:303` (widget item icons), `GameClientCore.java` map dots.

#### `method349(int[] dst, int[] src, int pixel, int srcIdx, int dstIdx, int width, int height, int dstStride, int srcStride)` — `Sprite.java:315-359`

Private inner loop for `method348`. Loop-unrolled by 4. For each pixel,
reads `i = src[j++]`, if `i != 0` writes to `dst[k++]`, else just
advances `k`. This is the legacy "0 is transparent" rule.

#### `method350(int x, int y, int alpha, boolean flag)` — `Sprite.java:361-410`

**Alpha-blended colour-key blit**: like `method348` but each opaque pixel
is alpha-blended against the existing buffer with weight `alpha`. The
clip math is identical to `method346`/`method348`. `flag` is a Jad guard.
Delegates to `method351`. Called from `WidgetRenderHandler.java:264, 287`
for the dragged item ghost (alpha = `128`).

#### `method351(int srcIdx, int width, int[] dst, int pixel, int[] src, int srcStride, int height, int dstStride, int alpha, int dstIdx, int sentinel)` — `Sprite.java:412-437`

Private alpha-blend inner loop. Uses the standard packed `0xff00ff` /
`0xff00` two-channel trick: `((c & 0xff00ff) * a + (d & 0xff00ff) * (256-a)) & 0xff00ff00`
shifted right 8. Skips colour-key zeros. `sentinel` must equal `8` or
`aBoolean1428` is toggled.

#### `method352(int height, int rot, int[] rowWidths, int scale, int[] rowOffsets, int sentinel, int width, int srcOffY, int srcOffX, int dstY, int dstX)` — `Sprite.java:439-479`

**Rotated, scaled, opaque** blit (no colour key). Computes `sin/cos` of
`rot/326.11` (legacy fixed-point angular unit — 2048 per turn), scales
by `scale/256`, then traverses the sprite row by row using
per-row pixel counts (`rowWidths`) and per-row left offsets
(`rowOffsets`). The `rowWidths`/`rowOffsets` arrays are the "shape mask"
encoding used by the legacy minimap and orb sprites — a sprite occupies
only the pixels listed by row. `sentinel`: `if(l >= 0) anInt1434 = 362;`
acts as a guard. Wrapped in `try {...} catch(Exception) {}` so
out-of-bounds reads silently abort. Callers: `GameClientCore.java:8137,
8144, 8145` (minimap orbs + arrow).

#### `method353(int dstY, int rotX, int width, int dstX, int sentinel, int scale, int height, double rotRad, int rotY)` — `Sprite.java:481-524`

**Rotated, scaled, colour-key** blit. Like `method352` but takes the
rotation as a `double` in radians, uses a single bounding box (no
per-row shape arrays), and applies the colour-key rule (skip
`pixel == 0`). `sentinel` must equal `41960`. Wrapped in try/catch.
Caller: `GameClientCore.java:5838` (chrome arrow rotation).

#### `method354(IndexedSprite mask, boolean flag, int y, int x)` — `Sprite.java:526-573`

**Indexed-mask blit**: draws this sprite at `(x + anInt1442, y + anInt1443)`
but only into destination pixels where the supplied `IndexedSprite`'s
palette index byte is `0`. Used by the legacy minimap to draw map dots
through the round mask without bleeding past the chrome. `flag` is a
Jad guard. Delegates to `method355` (passes the indexed sprite's
`aByteArray1450`). Caller: `GameClientCore.java:9254` (minimap chrome).

#### `method355(int[] src, int width, byte[] mask, int height, int[] dst, int pixel, boolean flag, int dstStride, int dstIdx, int maskOff, int srcOff)` — `Sprite.java:575-623`

Private inner loop for `method354`. Loop-unrolled by 4. For each pixel,
writes only if `src != 0 && mask[i1] == 0`. `flag` is a Jad guard.

---

## IndexedSprite.java

### Overview

Length: 284 lines. `final class IndexedSprite extends Rasterizer2D`. A
**palette-indexed** sprite: `byte[]` of 8-bit indices into a shared
`int[]` palette. Storage is 4x smaller than `Sprite` for the same
pixel count. Provides one loader (cache file), three geometry transforms
(halve, expand to frame, mirror, flip), palette recolour, and one
colour-key blit. **No alpha, no rotation, no opaque blit.**

### Fields (IndexedSprite.java:273-284)

- `anInt1446` (line 273) — Jad dummy, only ever assigned in guard branches.
- `aBoolean1447` (line 274) — Jad dummy, toggled inside `method362` guard.
- `anInt1448` (line 275) — Jad dummy (`= 360` at construction).
- `aByte1449` (line 276) — Jad dummy (`= 3` at construction).
- `aByteArray1450[]` (line 277) — public **palette index buffer**,
  one byte per pixel. Length `anInt1452 * anInt1453`.
- `anIntArray1451[]` (line 278) — public **shared palette** (`int[]` of
  24-bit ARGB entries). Entry 0 is always transparent.
- `anInt1452` (line 279) — content rectangle width.
- `anInt1453` (line 280) — content rectangle height.
- `anInt1454` (line 281) — content X offset within frame.
- `anInt1455` (line 282) — content Y offset within frame.
- `anInt1456` (line 283) — declared **frame** width.
- `anInt1457` (line 284) — declared **frame** height.

### Methods

#### `IndexedSprite(Archive cache, String name, int index)` — `IndexedSprite.java:13-59`

Cache-file loader, mirrors `Sprite(Archive, String, int)` but stores
palette indices directly in `aByteArray1450` instead of expanding to
`int[]`. Reads palette into `anIntArray1451` (entry 0 stays 0 →
transparent). Then reads frame size (`anInt1456`, `anInt1457`), skips
`index` prior entries, reads content `(xOff, yOff, w, h)` and row order
byte (`0` row-major, `1` column-major), and finally `w*h` raw bytes.

#### `method356(boolean flag)` — `IndexedSprite.java:61-86`

**Halves** the sprite to half-resolution: halves the frame dimensions
(`anInt1456 /= 2`, `anInt1457 /= 2`), allocates a new byte buffer at the
new frame size, copies content pixels with both X and Y coordinates
shifted right by 1 (`(k + anInt1454 >> 1) + (j + anInt1455 >> 1) * anInt1456`).
This is a naive lossy downsample — overlapping writes overwrite. Sets
content to fill the new frame, offsets `0`. `flag` is a Jad guard.
Caller: `Rasterizer3D.java:102` (mipmap-style precompute).

#### `method357(boolean flag)` — `IndexedSprite.java:88-108`

**Expands** the content rectangle to the full frame rectangle: allocates
a new `byte[anInt1456 * anInt1457]` zero-filled, copies content pixels in
at `(anInt1454, anInt1455)`, then replaces `aByteArray1450`. Same as
`Sprite.method345`. Early-out if already full-sized. `flag` is a Jad
guard that short-circuits the function when true. Caller:
`Rasterizer3D.java:104`.

#### `method358(int sentinel)` — `IndexedSprite.java:110-125`

**Horizontal flip** (mirror in X). Reverses each row in place into a new
buffer, then adjusts `anInt1454` so the content stays anchored on the
opposite side of the frame. Operates only on the content rectangle.
`sentinel` must be `0` (`if(i != 0) return;`).

#### `method359(boolean flag)` — `IndexedSprite.java:127-142`

**Vertical flip** (mirror in Y). Reverses row order in place into a new
buffer; updates `anInt1455`. `flag` is a Jad guard that pokes
`anInt1446` when false.

#### `method360(int dr, int dg, int db, int sentinel)` — `IndexedSprite.java:144-174`

**Palette recolour**. For each palette entry in `anIntArray1451`, adds
`(dr, dg, db)` and clamps each channel to `[0, 255]`. Unlike
`Sprite.method344`, entry 0 is **not** skipped — but since entry 0 is
typically `0x000000` and is the transparent index, leaving it
unchanged is up to the caller (clamping `0 + d` to `[0, 255]` could
make it visible). The `sentinel` poke is the Jad guard.

#### `method361(int x, int sentinel, int y)` — `IndexedSprite.java:176-223`

**Colour-key blit** into the global frame buffer. The only draw call on
`IndexedSprite`. Clips against the scissor rect identically to
`Sprite.method348`, then delegates to `method362` which expands palette
indices to RGB on the fly. `sentinel` must equal `16083`. Heavy usage:
`GameClientCore.java:134` (top-left logo), `:196, 201` (chat icons),
`:1099, 1100` (minimap orbs), `:1614, 2266, 2357, 2394` (map dots).

#### `method362(int height, byte sentinel, int[] dst, byte[] src, int dstStride, int dstIdx, int width, int srcIdx, int[] palette, int srcStride)` — `IndexedSprite.java:225-271`

Private colour-key inner loop. Loop-unrolled by 4. For each pixel: read
`byte1 = src[i1++]`, if non-zero write `dst[k++] = palette[byte1 & 0xff]`,
else `k++`. `sentinel` must equal `9` or `aBoolean1447` is toggled.

### Differences from Sprite (palette indirection)

| Aspect | `Sprite` | `IndexedSprite` |
|---|---|---|
| Pixel storage | `int[]` ARGB | `byte[]` palette indices |
| Palette | none (pixels are pre-resolved) | shared `int[] anIntArray1451` |
| Memory | 4 bytes/pixel | 1 byte/pixel + small palette |
| Transparent value | pixel `== 0` | index `== 0` (and palette[0] is also 0) |
| Opaque blit | `method346` | not provided |
| Colour-key blit | `method348` | `method361` |
| Alpha blit | `method350` | not provided |
| Rotated blit | `method352`, `method353` | not provided |
| Indexed-masked blit | `method354` (consumes an `IndexedSprite` mask) | not provided |
| Recolour | `method344` (per-pixel, clamps to `[1,255]`) | `method360` (per-palette-entry, clamps to `[0,255]`) |
| Flip / mirror | not provided | `method358` (mirror), `method359` (flip) |
| Halve / downsample | not provided | `method356` |
| Expand to frame | `method345` | `method357` |

`Sprite.method354` is the bridge: an `IndexedSprite` is used as a
**boolean mask** (any non-zero index excludes that destination pixel),
not as a colour source.

---

## FontRenderer.java

### Overview

Length: 498 lines. `final class FontRenderer extends Rasterizer2D`. A
bitmap font. Glyphs 0..255 each carry their own bitmap, baseline offsets,
and advance. Supports plain draw, centred draw, right-aligned draw,
chat-style markup (`@col@`), wave/shake effects, drop shadow, and
underline.

### Glyph data layout

For glyph index `c` (Latin-1, indexed by `char`):

- `aByteArrayArray1491[c]` — `byte[]` bitmap of length
  `anIntArray1492[c] * anIntArray1493[c]`. Non-zero bytes are "ink",
  zero bytes are transparent. Row-major if cache row-order byte is `0`,
  column-major if `1`.
- `anIntArray1492[c]` — glyph bitmap **width** (line 46).
- `anIntArray1493[c]` — glyph bitmap **height** (line 47).
- `anIntArray1494[c]` — glyph **left baseline offset** (X delta from
  pen position to bitmap left edge) (lines 44, 69). After the
  initial cache read, this is **overwritten to `1`** for all glyphs
  (line 69) — the original cache value is discarded; only the
  empty-edge trim adjustment below survives.
- `anIntArray1495[c]` — glyph **top baseline offset** (Y delta from
  the font baseline to bitmap top) (line 45). Combined with
  `anInt1497` at draw time (`drawY -= anInt1497`) so positions are
  given in baseline-relative coordinates.
- `anIntArray1496[c]` — glyph **advance width** (X step after this
  glyph). Initialised to `bitmapWidth + 2` (line 70), then trimmed by
  `1` if the left column is blank (`k2 <= j1 / 7` test, lines 72-78)
  and by another `1` if the right column is blank (lines 80-85).
  When `anIntArray1494[c]` becomes `0` (left side empty), the left
  baseline offset is set to `0` (line 78).
- `anInt1497` — **maximum glyph height** across the printable range
  (chars `< 128`). Used as the font ascent (`drawY -= anInt1497`) so
  caller Y coordinates name the baseline, not the top of the box.

The space character (`' '` = 32) has a special-cased advance width
(line 90-94): if `flag` (constructor arg) is true, it copies the advance
of `'I'` (73); otherwise it copies the advance of `'i'` (105).

### Markup syntax supported

Recognised inside `method389` (`drawWithMarkup`) and `method390`
(`drawRandomised`) at the form `@xxx@` where `xxx` is a 3-letter code.
The parser sees the leading `@`, requires another `@` four chars later
(`i1 + 4 < s.length() && s.charAt(i1 + 4) == '@'`), then dispatches to
`method391` to convert the 3 chars to a colour.

Codes (from `method391`, `FontRenderer.java:289-331`):

| Code | Effect |
|---|---|
| `@red@` | colour `0xff0000` |
| `@gre@` | colour `0x00ff00` |
| `@blu@` | colour `0x0000ff` |
| `@yel@` | colour `0xffff00` |
| `@cya@` | colour `0x00ffff` |
| `@mag@` | colour `0xff00ff` |
| `@whi@` | colour `0xffffff` |
| `@bla@` | colour `0x000000` |
| `@lre@` | colour `0xff9040` (light red) |
| `@dre@` | colour `0x800000` (dark red) |
| `@dbl@` | colour `0x000080` (dark blue) |
| `@or1@` | colour `0xffb000` |
| `@or2@` | colour `0xff7000` |
| `@or3@` | colour `0xff3000` |
| `@gr1@` | colour `0xc0ff00` |
| `@gr2@` | colour `0x80ff00` |
| `@gr3@` | colour `0x40ff00` |
| `@str@` | toggle `aBoolean1499 = true` (start underline). `method389` ends by emitting a horizontal scanline underneath via `Rasterizer2D.method339` when this flag is set on exit (line 254-255). |
| `@end@` | toggle `aBoolean1499 = false` (end underline). |

There are **no** `<col=...>`, `<br>`, `<u>`, `<shad>`, or `<img=...>` tags
in this build — only the legacy `@xxx@` syntax. `<br>` line wrapping
is done at a higher layer (the caller pre-splits strings). Drop shadow
is a separate code path (`method389(flag, true, ...)` and
`method390(true, ...)` draw a black offset glyph first), not a markup
tag. Inline images would be at `aClass30_Sub2_Sub1_Sub2Array1219` in
`GameClientCore` (chat icons), drawn via `IndexedSprite.method361`
between text segments by the caller (see
`GameClientCore.java:196, 201, 219, 224`), not by the font renderer.

### Methods

#### `FontRenderer(boolean fixedWidth, String name, int sentinel, Archive cache)` — `FontRenderer.java:15-97`

Loads the font from `<name>.dat` / `index.dat`. Reads a 256-entry table
of `(leftOff, topOff, w, h, rowOrder)` plus glyph bitmaps. After load,
re-derives `anIntArray1494` (left offset → `1` everywhere except
when the left column is blank) and `anIntArray1496` (advance, trimming
blank left/right columns) so visual kerning is implicit. Finally fixes
the space character's advance to match either `'I'` or `'i'` depending
on `fixedWidth`. `sentinel`: when non-zero, the dummy `aBoolean1490` is
toggled.

#### `method380(String text, int dummy1, int y, byte sentinel, int rightX)` — `FontRenderer.java:99-106`

**Draw right-aligned**: calls `method385` with X = `rightX - method384(text, true)`
so the right edge of the rendered text lands at `rightX`. Param `dummy1`
(`i`) is **the colour** by usage — but it's not passed through; instead
`method385` is invoked with the colour as `k` (the third positional
arg). Re-reading: `method385(j, s, k, 822, ...)`, the colour is `k`,
which here is the original `i`. So `i` is the colour, `j` is the
baseline-relative Y. `sentinel != -80` triggers the dummy loop. **Note:
the obfuscated parameter order is non-obvious — callers must use the
exact sequence**. No callers found in the four context files; likely
used elsewhere.

#### `method381(int colour, String text, int sentinel, int y, int centreX)` — `FontRenderer.java:108-113`

**Draw centred**: calls `method385` with X = `centreX - method384(text, true) / 2`.
`sentinel` must equal `23693` or it pokes `anInt1489`. Heavily used in
`GameClientCore.java:137-153` (input prompts), `:1445, 1524, 1561, 2590,
3234, 3493` (overlays / centred labels).

#### `method382(int sentinel1, int centreX, int seed, String text, int y, boolean flag)` — `FontRenderer.java:115-119`

**Centred draw with markup and optional shadow**, used by the widget
renderer for `WIDGET_TEXT` items. `sentinel1` (`k`) must yield `74 / k`
non-zero (otherwise `ArithmeticException`). Delegates to `method389`
(see below) with `shadow = flag`, `colour = method391(anInt1485, ...)`
ignored — actually `j` (here named `seed`) is the **colour** to pass
along. `method389(false, flag, centreX - method383(anInt1485, text) / 2,
seed, text, y)`. Callers: `WidgetRenderHandler.java:25, 195`.

#### `method383(int sentinel, String text)` — `FontRenderer.java:121-134`

**Measure width**, markup-aware. Walks the string; when it sees
`@xxx@` it skips 5 chars (`@xxx@`) without counting them. Otherwise
accumulates `anIntArray1496[c]`. Returns `0` for null. `sentinel` must
yield `75 / i` non-zero. Used by `method380`, `method382` and several
callers (`GameClientCore.java:205, 216, 228, 250, 273`) to align
text-with-icon runs.

#### `method384(String text, boolean flag)` — `FontRenderer.java:136-149`

**Measure width**, plain (no markup awareness). Sums
`anIntArray1496[c]` for every char including `@`. Used by the
non-markup helpers (`method380`, `method381`, `method385`,
`method386`, `method387`, `method388`). `flag` is a Jad guard.
Callers: `GameClientCore.java:1395, 1544`.

#### `method385(int colour, String text, int y, int sentinel, int x)` — `FontRenderer.java:151-165`

**Draw left-aligned**, plain (no markup). Walks the string; for each
non-space char, calls `method392` to stamp the glyph at
`(x + leftOffset[c], y + topOffset[c] - anInt1497)` with the given
colour. Advances `x` by `anIntArray1496[c]`. `sentinel` triggers
`50 / k` (`ArithmeticException` if `k == 0`). The primary text
rendering call; heavy use across `GameClientCore.java:186, 204, 206,
215, 227, 229, 236, 242, 249, 250, 257, 272, 273, 1547, 1548, 1840`.
Also `WidgetRenderHandler.java:293, 294` (item amount labels with
shadow).

#### `method386(int colour, boolean dummy, String text, int phase, int seed, int y)` — `FontRenderer.java:167-183`

**Centred wave-Y draw**: centres the string by half its width, then
displaces each char's Y by `sin(i / 2 + seed / 5) * 5` to give a
sine-wave bounce. No markup. `dummy` is a Jad guard. Caller:
`GameClientCore.java:1529-1530` (yells / shouts), drawn twice — once at
`y+1` colour `0` (shadow), once at `y` with the real colour.

#### `method387(int x, String text, int phase, int y, byte sentinel, int colour)` — `FontRenderer.java:185-201`

**Centred shake draw**: centres the string, then displaces each char by
`sin(i / 5 + phase / 5) * 5` in **both** X and Y axes (separate sines)
for a jittery effect. `sentinel` must equal `5`. Caller:
`GameClientCore.java:1534-1535` (shake chat type).

#### `method388(int decay, String text, boolean dummy, int x, int y, int phase, int colour)` — `FontRenderer.java:203-224`

**Centred wave-with-decay draw**: amplitude `d = 7 - decay/8`, clamped
to `>= 0`. Displaces each char's Y by `sin(i / 1.5 + phase) * d`. The
amplitude shrinks as `decay` rises (counts down from 150 in the
caller), giving a fade-out wave. Caller: `GameClientCore.java:1539-1540`.

#### `method389(boolean dummy, boolean shadow, int x, int colour, String text, int y)` — `FontRenderer.java:226-256`

**Draw left-aligned with markup and optional drop shadow**. The full
text renderer used by the widget engine. Walks the string; on `@xxx@`
calls `method391` to update `colour`. For each char, stamps via
`method392` once for the shadow at `(x+1, y+1)` colour `0` (only if
`shadow`), then once at `(x, y)` with the current colour. On exit, if
`aBoolean1499` is set (from `@str@`), draws a horizontal underline
scanline via `Rasterizer2D.method339(y + anInt1497 * 0.7, 0x800000,
finalX - startX, startX, (byte)4)` — note the underline is dark red
(`0x800000`), hard-coded. `dummy` skips the underline when true.
Callers: `WidgetRenderHandler.java:27, 197` (widget text),
`GameClientCore.java:1840` (menu).

#### `method390(boolean shadow, int phase, int colour, String text, int seed, int sentinel, int y)` — `FontRenderer.java:258-287`

**Random-style "scrolling" alpha draw**: used for the welcome screen
"MoparScape is loading…" gold text and similar. Seeds a `Random` with
`seed`, computes a per-call brightness `j1 = 192 + (random & 0x1f)`,
then walks the string with markup support. Each char (and its shadow,
if `shadow`) is drawn via `method394` (alpha-blended glyph stamp) using
`j1` as the alpha. Between glyphs, with probability 1/4 it inserts a
1-pixel horizontal gap (`i++`), giving a slight randomised kerning
jitter. `sentinel` triggers `10 / l`. Caller:
`GameClientCore.java:8116`.

#### `method391(String code, int sentinel)` — `FontRenderer.java:289-332`

**Markup colour lookup**. Compares `code` against the 17 named colours
plus `@str@`/`@end@` (which side-effect `aBoolean1499`). Returns the
RGB constant or `-1` for unknown. `sentinel >= 0` triggers a Jad
guard (`aBoolean1484` toggle). The full set is documented above.

#### `method392(byte[] glyph, int x, int y, int w, int h, int colour)` — `FontRenderer.java:334-375`

**Glyph stamp** with clipping. Clips the glyph bitmap against the
scissor rect — adjusting srcIdx, dstIdx, srcStride, dstStride — and
calls `method393` for the actual scanline write. This is the
clipping driver shared by `method385`, `method386`, `method387`,
`method388`, `method389`. Note: uses `>=` clip tests with `- 1`
correction, slightly different from the sprite clippers which use `>`.

#### `method393(int[] dst, byte[] src, int colour, int srcIdx, int dstIdx, int w, int h, int dstStride, int srcStride)` — `FontRenderer.java:377-414`

Private inner loop for opaque glyph rendering. Loop-unrolled by 4.
For each glyph byte: if non-zero, write `colour` to `dst[k++]`, else
skip. Pure ink-or-nothing — the glyph byte's value is **discarded**;
only its zero/non-zero status matters. No antialiasing.

#### `method394(int alpha, int x, byte[] glyph, int w, int y, int h, boolean dummy, int colour)` — `FontRenderer.java:416-457`

**Alpha-blended glyph stamp** with clipping. Same as `method392` but
calls `method395` so the glyph is alpha-blended at `alpha/256` against
the buffer. Used by `method390` for the randomised brightness text.
`dummy` triggers a Jad guard.

#### `method395(byte[] glyph, int h, int dstIdx, int[] dst, int sentinel, int srcIdx, int w, int dstStride, int srcStride, int colour, int alpha)` — `FontRenderer.java:459-481`

Private alpha-blend inner loop. Pre-computes
`colour' = ((colour & 0xff00ff) * alpha & 0xff00ff00 + (colour & 0xff00) * alpha & 0xff0000) >> 8`
once, then for each ink byte blends `dst[j] = (dst[j] * (256-alpha) + colour')`
using the packed-channel trick. `sentinel` triggers `98 / k`.

---

## How they fit together

`Rasterizer2D` is the only one of the four classes that owns mutable
state at the class level: it holds the **global frame buffer pointer**
(`anIntArray1378`), the **scissor rectangle**
(`anInt1381..anInt1384`), and the **buffer extents** (`anInt1379`,
`anInt1380`). It exposes those statics publicly, and every other
renderer in the legacy client — including `Rasterizer3D`,
`SceneGraph`, `WidgetRenderHandler`, `Sprite`, `IndexedSprite`,
`FontRenderer` — reads or writes them directly.

A typical frame looks like:
1. `GameClientCore` calls `Rasterizer2D.method334` to clear the buffer.
2. `GameClientCore` calls `Rasterizer2D.method333` to set the scissor
   rect for a sub-region (e.g. the 3D viewport, the chat box, the
   inventory).
3. The renderers (3D, widgets, fonts, sprites) draw into the global
   buffer through that scissor.
4. The buffer is blitted to the AWT canvas elsewhere (not in these
   files).

`Sprite` and `IndexedSprite` are the **pixel producers**: their
`methodNNN` blit calls walk the global `anIntArray1378` directly,
honour the scissor rect (`anInt1381..anInt1384`), and write either
opaque, colour-keyed, alpha-blended, or masked pixels into it. They
share the same clip logic (a clipping prologue immediately before
calling the inner-loop helper).

`FontRenderer` is the **text producer**: it owns 256 per-glyph bitmaps
plus per-glyph metrics. Its `method392` / `method394` glyph stampers
are conceptually identical to `IndexedSprite.method361` — a
byte-buffer source key-blitted to the global `int[]` — but the byte
value is interpreted as boolean ink, not a palette index, and a single
colour parameter supplies the foreground.

`Rasterizer3D` extends `Rasterizer2D` (`Rasterizer3D.java:36-48`)
and writes triangles into the **same** `anIntArray1378`, using the
**same** clip rect; its precomputed `anIntArray1472` is just `y *
anInt1379` per scanline. So 2D widgets and 3D worlds composite by
sharing memory, not by blending layers.

`WidgetRenderHandler` is the top-level caller. It dispatches per widget
type:
- type-with-text → `fontRenderer.method382` / `method389`
  (markup-aware draw),
- bordered rects → `Rasterizer2D.method336` (fill) plus
  `Rasterizer2D.method337` (outline) or alpha variants
  `method335` + `method338`,
- items / icons → `Sprite.method348` (opaque-with-key) or
  `Sprite.method350` (alpha drag-ghost), plus
  `FontRenderer.method385` for the amount label with shadow.

The legacy minimap (in `GameClientCore` near line 8137) shows the
clearest combination: the round chrome is an `IndexedSprite` blitted
via `method361`, the rotated player arrow is a `Sprite` blitted via
`method353`, the masked map-dots use
`Sprite.method354(IndexedSprite mask, ...)`, and the
`Rasterizer2D.method332(4)` call resets the scissor rect to full-screen
between sub-passes.

## Cross-class call graph

Direction: caller → callee.

```
GameClientCore
  ├─ Rasterizer2D.method332(4)          (reset clip to full buffer)        :262 :1549 :1563
  ├─ Rasterizer2D.method333(...)         (set clip to sub-rect)             :165 :1546 :1560
  ├─ Rasterizer2D.method334(flag)        (clear buffer to 0)                :3193 :3195 :3197 :3199 :3201 :3205 :3207 :3209 :3211 :5796 :5800 :10189
  ├─ Sprite.method343(0)                 (retarget framebuffer to sprite)   :723
  ├─ Sprite.method346(...)               (opaque blit chrome tiles)         :2702..2752
  ├─ Sprite.method352(...)               (rotated scaled opaque, no key)    :8137 :8144 :8145
  ├─ Sprite.method353(...)               (rotated scaled colour-key)        :5838
  ├─ Sprite.method354(IndexedSprite,...) (masked blit through minimap)      :9254
  ├─ IndexedSprite.method361(...)        (colour-key indexed blit)          :134 :196 :201 :219 :224 :1099 :1100 :1614 :2266 :2357 :2394
  ├─ FontRenderer.method381(...)         (centred plain draw)               :137 :138 :142 :143 :147 :148 :152 :153 :1445 :1446 :1524 :1525 :1561 :1562 :1567 :1568 :2590 :2591 :3234 :3240 :3493 :3494 :3495 :3496
  ├─ FontRenderer.method383(...)         (markup-aware width measure)       :205 :216 :228 :250 :273
  ├─ FontRenderer.method384(...)         (plain width measure)              :1395 :1544
  ├─ FontRenderer.method385(...)         (left-aligned plain draw)          :186 :204 :206 :215 :227 :229 :236 :242 :249 :250 :257 :272 :273 :1547 :1548 :1840
  ├─ FontRenderer.method386(...)         (wave-Y draw with shadow)          :1529 :1530
  ├─ FontRenderer.method387(...)         (shake draw)                       :1534 :1535
  ├─ FontRenderer.method388(...)         (wave decay draw)                  :1539 :1540
  └─ FontRenderer.method390(...)         (random-brightness alpha draw)     :8116

WidgetRenderHandler
  ├─ Rasterizer2D.anInt1379/.../anInt1384 (read clip+buffer extents)        :136 :243 :265 :275
  ├─ Rasterizer2D.method335(...)         (alpha rect fill)                  :99
  ├─ Rasterizer2D.method336(...)         (solid rect fill)                  :94
  ├─ Rasterizer2D.method337(...)         (solid rect outline)               :96
  ├─ Rasterizer2D.method338(...)         (alpha rect outline)               :101
  ├─ Sprite.method348(x, 16083, y)       (colour-key blit, item icons)      :44 :289 :303
  ├─ Sprite.method350(x, y, 128, flag)   (alpha-blend blit, item drag)      :264 :287
  ├─ FontRenderer.method382(...)         (centred markup draw)              :25 :195
  ├─ FontRenderer.method385(...)         (item amount label + shadow)       :293 :294
  └─ FontRenderer.method389(...)         (left-aligned markup draw)         :27 :197

SceneGraph
  └─ Rasterizer2D.anInt1385              (clip-radius read for 3D culling)  :1748 :1775 :1847

Rasterizer3D
  ├─ Rasterizer2D.anInt1378              (writes triangles into the buffer) numerous (e.g. :386 :395 :408 :417 ...)
  ├─ Rasterizer2D.anInt1379              (frame stride)                     :36 :44 :45 :47 :48 :384 :395 :400 ...
  ├─ Rasterizer2D.anInt1380              (frame height for row tables)      :36 :44 :48
  ├─ Rasterizer2D.anInt1382              (Y clip)                           :354 :356-359 :492 :494-497 :628 :630-633 ...
  ├─ Rasterizer2D.anInt1385              (X clip-radius)                    :775-776 :849-850 :1230-1231
  ├─ IndexedSprite.method356(false)      (halve indexed texture)            :102
  └─ IndexedSprite.method357(false)      (expand indexed texture)           :104

Inside the four documented files (self-calls and base-class calls):
  Sprite.method343(int)            → Rasterizer2D.method331            :133
  Sprite.method346(int,int,int)    → Sprite.method347 (private)        :235
  Sprite.method348(int,int,int)    → Sprite.method349 (private)        :310
  Sprite.method350(int,int,int,b)  → Sprite.method351 (private)        :407
  Sprite.method354(IS,b,int,int)   → Sprite.method355 (private)        :570
  IndexedSprite.method361(...)     → IndexedSprite.method362 (private) :220
  Rasterizer2D.method331(...)      → Rasterizer2D.method333            :20
  Rasterizer2D.method337(...)      → Rasterizer2D.method339            :142 :143
  Rasterizer2D.method337(...)      → Rasterizer2D.method341            :144 :145
  Rasterizer2D.method338(...)      → Rasterizer2D.method340            :151 :156
  Rasterizer2D.method338(...)      → Rasterizer2D.method342            :159 :160
  FontRenderer.method380(...)      → FontRenderer.method385 + .method384  :101
  FontRenderer.method381(...)      → FontRenderer.method385 + .method384  :112
  FontRenderer.method382(...)      → FontRenderer.method389 + .method383  :118
  FontRenderer.method385(...)      → FontRenderer.method392             :160
  FontRenderer.method386(...)      → FontRenderer.method392             :179
  FontRenderer.method387(...)      → FontRenderer.method392             :197
  FontRenderer.method388(...)      → FontRenderer.method392             :220
  FontRenderer.method389(...)      → FontRenderer.method391             :236
  FontRenderer.method389(...)      → FontRenderer.method392             :246 :247
  FontRenderer.method389(...)      → Rasterizer2D.method339             :255  (underline)
  FontRenderer.method390(...)      → FontRenderer.method391             :269
  FontRenderer.method390(...)      → FontRenderer.method394             :279 :280
  FontRenderer.method392(...)      → FontRenderer.method393 (private)   :372
  FontRenderer.method394(...)      → FontRenderer.method395 (private)   :452
```
