package io.github.ffakira.rsps.client.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.BootstrapAppearance;
import io.github.ffakira.rsps.protocol.BootstrapAnimationProfile;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import io.github.ffakira.rsps.protocol.BootstrapProfile;
import io.github.ffakira.rsps.protocol.BootstrapSkill;
import io.github.ffakira.rsps.protocol.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.CharacterBootstrapPayload;
import io.github.ffakira.rsps.protocol.EntityActionSequenceMessage;
import io.github.ffakira.rsps.protocol.LoginAccepted;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClientCoreTest {

  private static final ItemDefinition WATER_RUNE = new ItemDefinition(
      555,
      "Water rune",
      "One of the 4 basic elemental Runes.",
      true,
      4,
      false,
      false,
      -1,
      -1,
      ItemDefinition.InventoryAppearance.empty(),
      List.of(),
      List.of(),
      List.of(),
      List.of(),
      0,
      List.of(),
      0
  );

  private static final ItemDefinition WHITE_PARTYHAT = new ItemDefinition(
      1048,
      "White partyhat",
      "A nice hat from a cracker.",
      false,
      1,
      false,
      false,
      -1,
      -1,
      ItemDefinition.InventoryAppearance.empty(),
      List.of(),
      List.of(),
      List.of(),
      List.of(187),
      0,
      List.of(363),
      0
  );

  @Test
  void transitionsIntoWorldState() {
    ClientCore clientCore = new ClientCore();

    ClientEvent bootEvent = clientCore.bootstrap(new SceneBootstrapAssets(
        null,
        BootstrapPresentationCatalog.from(ItemDefinitionCatalog.of(
            WATER_RUNE,
            WHITE_PARTYHAT
        ))
    ));
    List<ClientEvent> loginEvents = clientCore.accept(new LoginAccepted(1L, 2L, new WorldPoint(3200, 3200, 0)));
    List<ClientEvent> worldEvents = clientCore.accept(new CharacterBootstrapMessage(
        new CharacterBootstrapPayload(
            1L,
            2L,
            "Akira",
            "lumbridge",
            new WorldPoint(3200, 3201, 0),
            new BootstrapProfile((short) 2, true, 100),
            new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
            List.of(new BootstrapItemSlot(0, 555, 1000)),
            List.of(new BootstrapItemSlot(0, 1048, 1)),
            List.of(new BootstrapSkill(0, 99, 14_000_000))
        )
    ));

    assertThat(bootEvent).isInstanceOf(ClientBootstrappedEvent.class);
    assertThat(loginEvents).singleElement().isInstanceOf(LoginSucceededEvent.class);
    assertThat(worldEvents).hasSize(2);
    assertThat(worldEvents.get(0)).isInstanceOf(CharacterBootstrappedEvent.class);
    assertThat(worldEvents.get(1)).isInstanceOf(WorldLoadedEvent.class);
    assertThat(clientCore.viewModel().localPlayerPosition()).isEqualTo(new WorldPoint(3200, 3201, 0));
    assertThat(clientCore.viewModel().bootstrap().appearance().animationProfile()).isEqualTo(BootstrapAnimationProfile.referencePlayer());
    assertThat(clientCore.viewModel().inventory().getFirst().itemId()).isEqualTo(555);
    assertThat(clientCore.viewModel().equipment().getFirst().itemId()).isEqualTo(1048);
    assertThat(clientCore.viewModel().skills().getFirst().currentLevel()).isEqualTo(99);
    assertThat(clientCore.viewModel().inventoryPresentation().getFirst()).isEqualTo(
        new BootstrapInventoryItemPresentation(0, 555, "Water rune", 1000, true, false, false)
    );
    assertThat(clientCore.viewModel().equipmentPresentation().getFirst()).isEqualTo(
        new BootstrapEquipmentItemPresentation(0, "Head", 1048, "White partyhat", 1, false, false, false)
    );
    assertThat(clientCore.viewModel().skillPresentation().getFirst()).isEqualTo(
        new BootstrapSkillPresentation(0, "Attack", 99, 14_000_000)
    );
  }

  @Test
  void tracksLocalPlayerActionSequenceFromRuntimeMessages() {
    ClientCore clientCore = new ClientCore();
    CharacterBootstrapPayload bootstrap = new CharacterBootstrapPayload(
        1L,
        2L,
        "Akira",
        "lumbridge",
        new WorldPoint(3200, 3201, 0),
        new BootstrapProfile((short) 2, true, 100),
        new BootstrapAppearance(List.of(-1, -1, -1, -1, -1, -1)),
        List.of(),
        List.of(),
        List.of()
    );

    clientCore.accept(new LoginAccepted(1L, 2L, bootstrap.worldPoint()));
    clientCore.accept(new CharacterBootstrapMessage(bootstrap));
    clientCore.accept(new EntityActionSequenceMessage(1, 866));

    assertThat(clientCore.viewModel().localPlayerActionSequenceId()).isEqualTo(866);

    clientCore.accept(new EntityActionSequenceMessage(1, -1));

    assertThat(clientCore.viewModel().localPlayerActionSequenceId()).isEqualTo(-1);
  }
}
