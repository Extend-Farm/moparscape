package io.github.ffakira.rsps.cache;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CacheStoreReader implements Closeable {

  private static final int INDEX_ENTRY_LENGTH = 6;
  private static final int SECTOR_LENGTH = 520;
  private static final int SECTOR_HEADER_LENGTH = 8;
  private static final int SECTOR_DATA_LENGTH = 512;

  private final RandomAccessFile dataFile;
  private final List<RandomAccessFile> indexFiles;

  public CacheStoreReader(CacheStoreLayout layout) {
    try {
      this.dataFile = new RandomAccessFile(layout.dataFile().toFile(), "r");
      this.indexFiles = openIndexFiles(layout.indexFiles());
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to open cache store " + layout.rootDirectory(), ioException);
    }
  }

  public CacheIndexEntry readIndexEntry(int storeIndex, int archiveId) {
    validateStoreIndex(storeIndex);
    if (archiveId < 0) {
      throw new IllegalArgumentException("Archive id cannot be negative: " + archiveId);
    }

    try {
      RandomAccessFile indexFile = indexFiles.get(storeIndex);
      long offset = (long) archiveId * INDEX_ENTRY_LENGTH;
      if (offset + INDEX_ENTRY_LENGTH > indexFile.length()) {
        return new CacheIndexEntry(archiveId, 0, 0);
      }

      byte[] bytes = new byte[INDEX_ENTRY_LENGTH];
      indexFile.seek(offset);
      indexFile.readFully(bytes);
      return new CacheIndexEntry(
          archiveId,
          readMedium(bytes, 0),
          readMedium(bytes, 3)
      );
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to read index entry " + archiveId + " from store " + storeIndex, ioException);
    }
  }

  public RawCacheArchive readArchive(int storeIndex, int archiveId) {
    CacheIndexEntry indexEntry = readIndexEntry(storeIndex, archiveId);
    if (!indexEntry.exists()) {
      throw new IllegalStateException(
          "Archive " + archiveId + " does not exist in store " + storeIndex
      );
    }

    byte[] archiveBytes = new byte[indexEntry.length()];
    int bytesRead = 0;
    int expectedChunk = 0;
    int sectorId = indexEntry.firstSector();
    int expectedStoreId = storeIndex + 1;
    try {
      while (bytesRead < archiveBytes.length) {
        if (sectorId <= 0 || (long) sectorId * SECTOR_LENGTH >= dataFile.length() + SECTOR_LENGTH) {
          throw new IllegalStateException(
              "Invalid sector pointer " + sectorId + " while reading archive " + archiveId
          );
        }

        dataFile.seek((long) sectorId * SECTOR_LENGTH);
        byte[] sectorHeader = new byte[SECTOR_HEADER_LENGTH];
        dataFile.readFully(sectorHeader);
        int headerArchiveId = readUnsignedShort(sectorHeader, 0);
        int chunkId = readUnsignedShort(sectorHeader, 2);
        int nextSectorId = readMedium(sectorHeader, 4);
        int storeId = sectorHeader[7] & 0xff;
        if (headerArchiveId != archiveId) {
          throw new IllegalStateException(
              "Sector archive id mismatch for archive " + archiveId + ": " + headerArchiveId
          );
        }
        if (chunkId != expectedChunk) {
          throw new IllegalStateException(
              "Sector chunk mismatch for archive " + archiveId + ": expected " + expectedChunk + ", got " + chunkId
          );
        }
        if (storeId != expectedStoreId) {
          throw new IllegalStateException(
              "Sector store mismatch for archive " + archiveId + ": expected " + expectedStoreId + ", got " + storeId
          );
        }

        int chunkLength = Math.min(SECTOR_DATA_LENGTH, archiveBytes.length - bytesRead);
        dataFile.readFully(archiveBytes, bytesRead, chunkLength);
        bytesRead += chunkLength;
        sectorId = nextSectorId;
        expectedChunk++;
      }
    } catch (IOException ioException) {
      throw new IllegalStateException("Failed to read archive " + archiveId + " from store " + storeIndex, ioException);
    }

    return new RawCacheArchive(storeIndex, indexEntry, archiveBytes);
  }

  @Override
  public void close() throws IOException {
    IOException firstFailure = null;
    try {
      dataFile.close();
    } catch (IOException ioException) {
      firstFailure = ioException;
    }

    for (RandomAccessFile indexFile : indexFiles) {
      try {
        indexFile.close();
      } catch (IOException ioException) {
        if (firstFailure == null) {
          firstFailure = ioException;
        }
      }
    }

    if (firstFailure != null) {
      throw firstFailure;
    }
  }

  private List<RandomAccessFile> openIndexFiles(List<Path> indexPaths) throws IOException {
    List<RandomAccessFile> randomAccessFiles = new ArrayList<>(indexPaths.size());
    for (Path indexPath : indexPaths) {
      randomAccessFiles.add(new RandomAccessFile(indexPath.toFile(), "r"));
    }
    return List.copyOf(randomAccessFiles);
  }

  private void validateStoreIndex(int storeIndex) {
    if (storeIndex < 0 || storeIndex >= indexFiles.size()) {
      throw new IllegalArgumentException("Store index out of range: " + storeIndex);
    }
  }

  private static int readMedium(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 16)
        | ((bytes[offset + 1] & 0xff) << 8)
        | (bytes[offset + 2] & 0xff);
  }

  private static int readUnsignedShort(byte[] bytes, int offset) {
    return ((bytes[offset] & 0xff) << 8) | (bytes[offset + 1] & 0xff);
  }
}
