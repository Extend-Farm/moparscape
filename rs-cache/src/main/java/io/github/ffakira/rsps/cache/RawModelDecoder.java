package io.github.ffakira.rsps.cache;

public final class RawModelDecoder {

  private static final int MODEL_FOOTER_LENGTH = 18;

  public RawModelData decode(byte[] bytes) {
    if (bytes.length < MODEL_FOOTER_LENGTH) {
      throw new IllegalArgumentException("Model bytes are too short: " + bytes.length);
    }

    CacheDataReader footerReader = new CacheDataReader(bytes);
    footerReader.position(bytes.length - MODEL_FOOTER_LENGTH);
    int vertexCount = footerReader.readUnsignedShort();
    int faceCount = footerReader.readUnsignedShort();
    int texturedFaceCount = footerReader.readUnsignedByte();
    int faceTypeFlag = footerReader.readUnsignedByte();
    int facePriorityFlag = footerReader.readUnsignedByte();
    int faceAlphaFlag = footerReader.readUnsignedByte();
    int faceSkinFlag = footerReader.readUnsignedByte();
    int vertexSkinFlag = footerReader.readUnsignedByte();
    int xDataLength = footerReader.readUnsignedShort();
    int yDataLength = footerReader.readUnsignedShort();
    int zDataLength = footerReader.readUnsignedShort();
    int faceIndexDataLength = footerReader.readUnsignedShort();

    int offset = 0;
    int vertexFlagsOffset = offset;
    offset += vertexCount;
    int faceIndexTypeOffset = offset;
    offset += faceCount;

    int facePriorityOffset = offset;
    if (facePriorityFlag == 255) {
      offset += faceCount;
    } else {
      facePriorityOffset = -1;
    }

    int faceSkinOffset = offset;
    if (faceSkinFlag == 1) {
      offset += faceCount;
    } else {
      faceSkinOffset = -1;
    }

    int faceTypeOffset = offset;
    if (faceTypeFlag == 1) {
      offset += faceCount;
    } else {
      faceTypeOffset = -1;
    }

    int vertexSkinOffset = offset;
    if (vertexSkinFlag == 1) {
      offset += vertexCount;
    } else {
      vertexSkinOffset = -1;
    }

    int faceAlphaOffset = offset;
    if (faceAlphaFlag == 1) {
      offset += faceCount;
    } else {
      faceAlphaOffset = -1;
    }

    int faceIndexDataOffset = offset;
    offset += faceIndexDataLength;
    int faceColorOffset = offset;
    offset += faceCount * 2;
    int texturedFaceOffset = offset;
    offset += texturedFaceCount * 6;
    int xDataOffset = offset;
    offset += xDataLength;
    int yDataOffset = offset;
    offset += yDataLength;
    int zDataOffset = offset;

    int[] vertexX = new int[vertexCount];
    int[] vertexY = new int[vertexCount];
    int[] vertexZ = new int[vertexCount];
    int[] faceVertexA = new int[faceCount];
    int[] faceVertexB = new int[faceCount];
    int[] faceVertexC = new int[faceCount];
    int[] faceColorHsl = new int[faceCount];
    int[] faceRenderTypes = new int[faceCount];
    int[] facePriorities = new int[faceCount];
    int[] faceAlpha = new int[faceCount];
    int[] texturedFaceVertexA = new int[texturedFaceCount];
    int[] texturedFaceVertexB = new int[texturedFaceCount];
    int[] texturedFaceVertexC = new int[texturedFaceCount];

    CacheDataReader vertexFlagReader = new CacheDataReader(bytes);
    vertexFlagReader.position(vertexFlagsOffset);
    CacheDataReader xReader = new CacheDataReader(bytes);
    xReader.position(xDataOffset);
    CacheDataReader yReader = new CacheDataReader(bytes);
    yReader.position(yDataOffset);
    CacheDataReader zReader = new CacheDataReader(bytes);
    zReader.position(zDataOffset);
    if (vertexSkinOffset >= 0) {
      CacheDataReader vertexSkinReader = new CacheDataReader(bytes);
      vertexSkinReader.position(vertexSkinOffset);
      for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        // Skin groups are not consumed yet, but the reader must advance to keep parity.
        vertexSkinReader.readUnsignedByte();
      }
    }

    int x = 0;
    int y = 0;
    int z = 0;
    for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
      int flags = vertexFlagReader.readUnsignedByte();
      int deltaX = (flags & 1) != 0 ? xReader.readSignedSmart() : 0;
      int deltaY = (flags & 2) != 0 ? yReader.readSignedSmart() : 0;
      int deltaZ = (flags & 4) != 0 ? zReader.readSignedSmart() : 0;
      vertexX[vertexIndex] = x + deltaX;
      vertexY[vertexIndex] = y + deltaY;
      vertexZ[vertexIndex] = z + deltaZ;
      x = vertexX[vertexIndex];
      y = vertexY[vertexIndex];
      z = vertexZ[vertexIndex];
    }

    CacheDataReader faceColorReader = new CacheDataReader(bytes);
    faceColorReader.position(faceColorOffset);
    if (faceTypeOffset >= 0) {
      CacheDataReader faceTypeReader = new CacheDataReader(bytes);
      faceTypeReader.position(faceTypeOffset);
      for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
        faceRenderTypes[faceIndex] = faceTypeReader.readUnsignedByte();
      }
    }
    if (facePriorityOffset >= 0) {
      CacheDataReader facePriorityReader = new CacheDataReader(bytes);
      facePriorityReader.position(facePriorityOffset);
      for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
        facePriorities[faceIndex] = facePriorityReader.readUnsignedByte();
      }
    } else {
      java.util.Arrays.fill(facePriorities, facePriorityFlag);
    }
    if (faceAlphaOffset >= 0) {
      CacheDataReader faceAlphaReader = new CacheDataReader(bytes);
      faceAlphaReader.position(faceAlphaOffset);
      for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
        faceAlpha[faceIndex] = faceAlphaReader.readUnsignedByte();
      }
    }
    if (faceSkinOffset >= 0) {
      CacheDataReader faceSkinReader = new CacheDataReader(bytes);
      faceSkinReader.position(faceSkinOffset);
      for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
        faceSkinReader.readUnsignedByte();
      }
    }
    for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
      faceColorHsl[faceIndex] = faceColorReader.readUnsignedShort();
    }
    if (texturedFaceCount > 0) {
      CacheDataReader texturedFaceReader = new CacheDataReader(bytes);
      texturedFaceReader.position(texturedFaceOffset);
      for (int texturedFaceIndex = 0; texturedFaceIndex < texturedFaceCount; texturedFaceIndex++) {
        texturedFaceVertexA[texturedFaceIndex] = texturedFaceReader.readUnsignedShort();
        texturedFaceVertexB[texturedFaceIndex] = texturedFaceReader.readUnsignedShort();
        texturedFaceVertexC[texturedFaceIndex] = texturedFaceReader.readUnsignedShort();
      }
    }

    CacheDataReader faceIndexReader = new CacheDataReader(bytes);
    faceIndexReader.position(faceIndexDataOffset);
    CacheDataReader faceIndexTypeReader = new CacheDataReader(bytes);
    faceIndexTypeReader.position(faceIndexTypeOffset);
    int a = 0;
    int b = 0;
    int c = 0;
    int accumulator = 0;
    for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
      int triangleType = faceIndexTypeReader.readUnsignedByte();
      if (triangleType == 1) {
        a = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = a;
        b = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = b;
        c = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = c;
      } else if (triangleType == 2) {
        b = c;
        c = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = c;
      } else if (triangleType == 3) {
        a = c;
        c = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = c;
      } else if (triangleType == 4) {
        int swap = a;
        a = b;
        b = swap;
        c = faceIndexReader.readSignedSmart() + accumulator;
        accumulator = c;
      } else {
        throw new IllegalStateException("Unsupported model triangle type " + triangleType);
      }
      faceVertexA[faceIndex] = a;
      faceVertexB[faceIndex] = b;
      faceVertexC[faceIndex] = c;
    }

    return new RawModelData(
        vertexCount,
        faceCount,
        texturedFaceCount,
        vertexX,
        vertexY,
        vertexZ,
        faceVertexA,
        faceVertexB,
        faceVertexC,
        faceColorHsl,
        faceRenderTypes,
        facePriorities,
        faceAlpha,
        texturedFaceVertexA,
        texturedFaceVertexB,
        texturedFaceVertexC
    );
  }
}
