package io.github.ffakira.rsps.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;

class CacheStoreReaderTest {

  @Test
  void readsRootArchiveBytesFromStoreZero() throws Exception {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));

    try (CacheStoreReader reader = new CacheStoreReader(layout)) {
      RawCacheArchive archive = reader.readArchive(0, 1);
      CacheArchiveCompressionHeader header = archive.compressionHeader();

      assertThat(archive.indexEntry().archiveId()).isEqualTo(1);
      assertThat(archive.bytes().length).isEqualTo(archive.indexEntry().length());
      assertThat(header.decompressedLength()).isPositive();
      assertThat(header.storedLength()).isPositive();
    }
  }

  @Test
  void parsesTitleArchiveNamedEntriesFromStoreZero() throws Exception {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));

    try (CacheStoreReader reader = new CacheStoreReader(layout)) {
      RawCacheArchive rootArchive = reader.readArchive(0, 1);
      CacheArchiveContainer container = new CacheArchiveContainerParser().parse(rootArchive);
      byte[] titleBytes = container.readEntry("title.dat");
      byte[] logoBytes = container.readEntry("logo.dat");

      BufferedImage titleImage = ImageIO.read(new ByteArrayInputStream(titleBytes));

      assertThat(container.entryReferences()).hasSize(10);
      assertThat(titleImage).isNotNull();
      assertThat(titleImage.getWidth()).isEqualTo(383);
      assertThat(titleImage.getHeight()).isEqualTo(503);
      assertThat(logoBytes).isNotEmpty();
    }
  }

  @Test
  void parsesConfigArchiveNamedEntriesFromStoreZero() throws Exception {
    CacheStoreLayout layout = new CacheStoreLocator().locateFromWorkingDirectory(Path.of("."));

    try (CacheStoreReader reader = new CacheStoreReader(layout)) {
      RawCacheArchive configArchive = reader.readArchive(0, 2);
      CacheArchiveContainer container = new CacheArchiveContainerParser().parse(configArchive);
      byte[] objectIndexBytes = container.readEntry("obj.idx");
      byte[] objectDataBytes = container.readEntry("obj.dat");

      assertThat(container.entryReferences()).isNotEmpty();
      assertThat(objectIndexBytes.length).isGreaterThan(100);
      assertThat(objectDataBytes.length).isGreaterThan(1000);
      assertThat(objectIndexBytes[0] & 0xff).isNotNegative();
    }
  }
}
