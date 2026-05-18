package io.github.ffakira.rsps.content;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ItemDefinitionCatalogTest {

  @Test
  void loadsLiveCacheItemDefinitionsWithResolvedDisplayData() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ItemDefinitionCatalog catalog = ItemDefinitionCatalog.load(manifest);

    assertThat(catalog.size()).isEqualTo(6541);
    ItemDefinition waterRune = catalog.require(555);
    assertThat(waterRune.id()).isEqualTo(555);
    assertThat(waterRune.name()).isEqualTo("Water rune");
    assertThat(waterRune.description()).isEqualTo("One of the 4 basic elemental Runes.");
    assertThat(waterRune.stackable()).isTrue();
    assertThat(waterRune.value()).isEqualTo(4);
    assertThat(waterRune.membersOnly()).isFalse();
    assertThat(waterRune.noted()).isFalse();
    assertThat(waterRune.noteLinkItemId()).isEqualTo(-1);
    assertThat(waterRune.noteTemplateItemId()).isEqualTo(-1);
    assertThat(waterRune.recolorSources()).isEmpty();
    assertThat(waterRune.recolorTargets()).isEmpty();
    assertThat(waterRune.maleBodyModelIds()).isEmpty();
    assertThat(waterRune.femaleBodyModelIds()).isEmpty();

    ItemDefinition whitePartyhat = catalog.require(1048);
    assertThat(whitePartyhat.id()).isEqualTo(1048);
    assertThat(whitePartyhat.name()).isEqualTo("White partyhat");
    assertThat(whitePartyhat.description()).isEqualTo("A nice hat from a cracker.");
    assertThat(whitePartyhat.stackable()).isFalse();
    assertThat(whitePartyhat.value()).isEqualTo(1);
    assertThat(whitePartyhat.noted()).isFalse();
    assertThat(whitePartyhat.maleBodyModelIds()).isNotEmpty();
    assertThat(whitePartyhat.femaleBodyModelIds()).isNotEmpty();
  }

  @Test
  void resolvesNotedItemsFromCacheLinks() {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(Path.of("."));

    ItemDefinition noteDefinition = ItemDefinitionCatalog.load(manifest).require(7);

    assertThat(noteDefinition.name()).isEqualTo("Cannon base");
    assertThat(noteDefinition.noted()).isTrue();
    assertThat(noteDefinition.stackable()).isTrue();
    assertThat(noteDefinition.description()).isEqualTo("Swap this note at any bank for a Cannon base.");
    assertThat(noteDefinition.noteLinkItemId()).isEqualTo(6);
  }
}
