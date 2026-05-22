package io.github.ffakira.rsps.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ffakira.rsps.client.core.EquipmentLoadout;
import io.github.ffakira.rsps.content.ItemDefinition;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import io.github.ffakira.rsps.protocol.BootstrapItemSlot;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameplayCombatSidebarModelTest {

  @Test
  void resolvesPickaxeAttackStylesFromTheEquippedWeaponSlot() {
    EquipmentLoadout loadout = EquipmentLoadout.from(List.of(
        new BootstrapItemSlot(EquipmentLoadout.WEAPON_SLOT, 1265, 1)
    ));
    GameplayCombatSidebarModel combatModel = GameplayCombatSidebarModel.from(loadout, ItemDefinitionCatalog.of(
        itemDefinition(1265, "Bronze pickaxe")
    ));

    assertThat(combatModel.weaponName()).isEqualTo("Bronze pickaxe");
    assertThat(combatModel.interfaceId()).isEqualTo(5570);
    assertThat(combatModel.itemComponentId()).isEqualTo(5571);
    assertThat(combatModel.weaponNameComponentId()).isEqualTo(5573);
    assertThat(combatModel.attackStyles()).containsExactly(
        new GameplayCombatSidebarModel.AttackStyle("Spike", "Accurate", "Stab"),
        new GameplayCombatSidebarModel.AttackStyle("Impale", "Aggressive", "Stab"),
        new GameplayCombatSidebarModel.AttackStyle("Smash", "Aggressive", "Crush"),
        new GameplayCombatSidebarModel.AttackStyle("Block", "Defensive", "Stab")
    );
  }

  @Test
  void fallsBackToUnarmedWhenNoWeaponIsEquipped() {
    GameplayCombatSidebarModel combatModel = GameplayCombatSidebarModel.from(
        EquipmentLoadout.empty(),
        ItemDefinitionCatalog.empty()
    );

    assertThat(combatModel.weaponName()).isEqualTo("Unarmed");
    assertThat(combatModel.interfaceId()).isEqualTo(5855);
    assertThat(combatModel.itemComponentId()).isEqualTo(-1);
    assertThat(combatModel.weaponNameComponentId()).isEqualTo(5857);
    assertThat(combatModel.attackStyles()).extracting(GameplayCombatSidebarModel.AttackStyle::name)
        .containsExactly("Punch", "Kick", "Block");
  }

  @Test
  void resolvesWhipInterfaceIdsFromTheReferenceCombatSidebarMapping() {
    EquipmentLoadout loadout = EquipmentLoadout.from(List.of(
        new BootstrapItemSlot(EquipmentLoadout.WEAPON_SLOT, 4151, 1)
    ));
    GameplayCombatSidebarModel combatModel = GameplayCombatSidebarModel.from(loadout, ItemDefinitionCatalog.of(
        itemDefinition(4151, "Abyssal whip")
    ));

    assertThat(combatModel.interfaceId()).isEqualTo(12290);
    assertThat(combatModel.itemComponentId()).isEqualTo(12291);
    assertThat(combatModel.weaponNameComponentId()).isEqualTo(12293);
  }

  private static ItemDefinition itemDefinition(int id, String name) {
    return new ItemDefinition(
        id,
        name,
        "",
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
        List.of(),
        0,
        List.of(),
        0
    );
  }
}
