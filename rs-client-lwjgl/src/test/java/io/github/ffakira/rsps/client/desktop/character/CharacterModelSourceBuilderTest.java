package io.github.ffakira.rsps.client.desktop.character;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.cache.RawModelRepository;
import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.IdentityKitDefinitionCatalog;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class CharacterModelSourceBuilderTest {

  @Test
  void resolvesLegacyDefaultAppearanceSlotsForAnUnequippedMaleCharacter() {
    CharacterModelSourceBuilder sourceBuilder = sourceBuilder();

    int[] appearanceEntries = sourceBuilder.resolveAppearanceEntries(
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)).lookValues().stream().mapToInt(Integer::intValue).toArray(),
        List.of(),
        CharacterModelSourceBuilder.SequenceEquipmentOverrides.none()
    );

    assertThat(appearanceEntries).containsExactly(
        0,
        0,
        0,
        0,
        0x100 + 25,
        0,
        0x100 + 29,
        0x100 + 39,
        0x100 + 7,
        0x100 + 35,
        0x100 + 44,
        0
    );
  }

  @Test
  void appliesSequenceEquipmentOverridesToWeaponAndShieldAppearanceSlots() {
    CharacterModelSourceBuilder sourceBuilder = sourceBuilder();

    int[] appearanceEntries = sourceBuilder.resolveAppearanceEntries(
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)).lookValues().stream().mapToInt(Integer::intValue).toArray(),
        List.of(
            new BootstrapItemSlot(3, 1277, 1),
            new BootstrapItemSlot(5, 1171, 1)
        ),
        new CharacterModelSourceBuilder.SequenceEquipmentOverrides(0x200 + 4151, 0x100 + 12)
    );

    assertThat(appearanceEntries[3]).isEqualTo(0x200 + 4151);
    assertThat(appearanceEntries[5]).isEqualTo(0x100 + 12);
  }

  private CharacterModelSourceBuilder sourceBuilder() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));
    return new CharacterModelSourceBuilder(
        ItemDefinitionCatalog.load(manifest),
        IdentityKitDefinitionCatalog.load(manifest),
        new RawModelRepository(manifest.cacheStore())
    );
  }
}
