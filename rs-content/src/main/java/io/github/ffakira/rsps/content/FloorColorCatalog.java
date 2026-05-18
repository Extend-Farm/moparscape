package io.github.ffakira.rsps.content;

public final class FloorColorCatalog {

  private final FloorColorDefinition[] definitionsById;

  private FloorColorCatalog(FloorColorDefinition[] definitionsById) {
    this.definitionsById = definitionsById;
  }

  public static FloorColorCatalog parse(byte[] bytes) {
    ByteCursor cursor = new ByteCursor(bytes);
    int definitionCount = cursor.readUnsignedShort();
    FloorColorDefinition[] definitions = new FloorColorDefinition[definitionCount + 1];
    definitions[0] = new FloorColorDefinition(0, -1, true);
    for (int definitionIndex = 1; definitionIndex <= definitionCount; definitionIndex++) {
      definitions[definitionIndex] = parseDefinition(cursor);
    }
    return new FloorColorCatalog(definitions);
  }

  public int colorFor(int floorId) {
    if (floorId <= 0 || floorId >= definitionsById.length) {
      return 0;
    }
    return definitionsById[floorId].rgb();
  }

  public int textureIdFor(int floorId) {
    if (floorId <= 0 || floorId >= definitionsById.length) {
      return -1;
    }
    return definitionsById[floorId].textureId();
  }

  private static FloorColorDefinition parseDefinition(ByteCursor cursor) {
    int rgb = 0;
    int textureId = -1;
    boolean occludes = true;
    while (true) {
      int opcode = cursor.readUnsignedByte();
      if (opcode == 0) {
        return new FloorColorDefinition(rgb, textureId, occludes);
      }
      switch (opcode) {
        case 1 -> rgb = cursor.readMedium();
        case 2 -> textureId = cursor.readUnsignedByte();
        case 3 -> {
          // Preserve compatibility with the old format; the flag is unused here.
        }
        case 5 -> occludes = false;
        case 6 -> cursor.skipString();
        case 7 -> cursor.readMedium();
        default -> throw new IllegalStateException("Unsupported floor definition opcode " + opcode);
      }
    }
  }

  private static final class ByteCursor {
    private final byte[] bytes;
    private int offset;

    private ByteCursor(byte[] bytes) {
      this.bytes = bytes;
    }

    private int readUnsignedByte() {
      return bytes[offset++] & 0xff;
    }

    private int readUnsignedShort() {
      return (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private int readMedium() {
      return (readUnsignedByte() << 16) | (readUnsignedByte() << 8) | readUnsignedByte();
    }

    private void skipString() {
      while (bytes[offset++] != 10) {
        // advance
      }
    }
  }
}
