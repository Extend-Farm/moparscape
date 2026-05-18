package io.github.ffakira.rsps.server.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.model.AccountId;
import io.github.ffakira.rsps.persistence.ItemContainerKind;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CharacterFileRepositoryTest {

  @TempDir
  Path tempDirectory;

  @Test
  void loadsFullBootstrapStateFromCharacterFile() throws Exception {
    Path charactersDirectory = Files.createDirectory(tempDirectory.resolve("characters"));
    Files.writeString(
        charactersDirectory.resolve("akira.txt"),
        """
        [ACCOUNT]
        character-username = akira
        character-password = password

        [CHARACTER]
        character-height = 0
        character-posx = 3250
        character-posy = 3227
        character-rights = 2
        character-ismember = 1
        character-lastlogin = 20260417
        character-energy = 100
        character-gametime = 7
        character-gamecount = 69080

        [EQUIPMENT]
        character-equip = 0 1048 1

        [LOOK]
        character-look = 0 -1
        character-look = 1 -1
        character-look = 2 -1
        character-look = 3 -1
        character-look = 4 -1
        character-look = 5 -1

        [SKILLS]
        character-skill = 0 99 14195464
        character-skill = 1 99 14000088

        [ITEMS]
        character-item = 0 555 999

        [BANK]
        character-bank = 0 1216 1

        [FRIENDS]
        character-friend = 0 123456789

        [IGNORES]
        character-ignore = 0 987654321

        [EOF]
        """,
        StandardCharsets.UTF_8
    );

    CharacterFileRepository repository = new CharacterFileRepository(charactersDirectory);
    AccountId accountId = repository.findByUsername("akira").orElseThrow().id();
    var snapshot = repository.loadByAccountId(accountId).orElseThrow();

    assertThat(snapshot.profile().rights()).isEqualTo((short) 2);
    assertThat(snapshot.profile().member()).isTrue();
    assertThat(snapshot.skills()).hasSize(2);
    assertThat(snapshot.inventorySlots()).singleElement()
        .satisfies(slot -> {
          assertThat(slot.containerKind()).isEqualTo(ItemContainerKind.INVENTORY);
          assertThat(slot.itemId()).isEqualTo(555);
        });
    assertThat(snapshot.equipmentSlots()).singleElement()
        .satisfies(slot -> assertThat(slot.itemId()).isEqualTo(1048));
    assertThat(snapshot.bankSlots()).singleElement()
        .satisfies(slot -> assertThat(slot.itemId()).isEqualTo(1216));
    assertThat(snapshot.friendLinks()).singleElement()
        .satisfies(link -> assertThat(link.targetValue()).isEqualTo(123456789L));
    assertThat(snapshot.ignoreLinks()).singleElement()
        .satisfies(link -> assertThat(link.targetValue()).isEqualTo(987654321L));
  }
}
