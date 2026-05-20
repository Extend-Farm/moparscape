package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import org.junit.jupiter.api.Test;

class IdentityKitDefinitionCatalogTest {

  @Test
  void mapsVisibleBodyPartsToLegacyKitIdsForBothSexes() {
    IdentityKitDefinitionCatalog catalog = IdentityKitDefinitionCatalog.parse(testDefinitions());

    assertThat(catalog.defaultBodyKitId(0, false)).isEqualTo(0);
    assertThat(catalog.defaultBodyKitId(1, false)).isEqualTo(3);
    assertThat(catalog.defaultBodyKitId(2, false)).isEqualTo(4);
    assertThat(catalog.defaultBodyKitId(3, false)).isEqualTo(5);
    assertThat(catalog.defaultBodyKitId(4, false)).isEqualTo(6);
    assertThat(catalog.defaultBodyKitId(5, false)).isEqualTo(7);

    assertThat(catalog.defaultBodyKitId(0, true)).isEqualTo(8);
    assertThat(catalog.defaultBodyKitId(1, true)).isEqualTo(11);
    assertThat(catalog.defaultBodyKitId(2, true)).isEqualTo(12);
    assertThat(catalog.defaultBodyKitId(3, true)).isEqualTo(13);
    assertThat(catalog.defaultBodyKitId(4, true)).isEqualTo(14);
    assertThat(catalog.defaultBodyKitId(5, true)).isEqualTo(15);
  }

  private byte[] testDefinitions() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    writeUnsignedShort(out, 16);
    writeDefinition(out, 0, false);
    writeDefinition(out, 1, false);
    writeDefinition(out, 2, true);
    writeDefinition(out, 2, false);
    writeDefinition(out, 3, false);
    writeDefinition(out, 4, false);
    writeDefinition(out, 5, false);
    writeDefinition(out, 6, false);
    writeDefinition(out, 7, false);
    writeDefinition(out, 8, false);
    writeDefinition(out, 9, true);
    writeDefinition(out, 9, false);
    writeDefinition(out, 10, false);
    writeDefinition(out, 11, false);
    writeDefinition(out, 12, false);
    writeDefinition(out, 13, false);
    return out.toByteArray();
  }

  private void writeDefinition(ByteArrayOutputStream out, int bodyPartId, boolean nonSelectable) {
    out.write(1);
    out.write(bodyPartId);
    out.write(2);
    out.write(1);
    writeUnsignedShort(out, bodyPartId + 100);
    if (nonSelectable) {
      out.write(3);
    }
    out.write(0);
  }

  private void writeUnsignedShort(ByteArrayOutputStream out, int value) {
    out.write((value >>> 8) & 0xff);
    out.write(value & 0xff);
  }
}
