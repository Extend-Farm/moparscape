package com.veyrmoor.client.desktop.gameplay.sidebar;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.protocol.bootstrap.BootstrapItemSlot;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Test;

class ItemBonusCatalogTest {

  @Test
  void parsesReferenceItemBonusesAndTotalsEquippedItems() throws Exception {
    ItemBonusCatalog bonusCatalog = ItemBonusCatalog.parse(new StringReader(
        """
        item = 35\tExcalibur\tA_sword.\t160\t160\t181\t20\t29\t-2\t0\t0\t0\t3\t2\t1\t0\t25\t0
        item = 995\tCoins\tGold.\t1\t1\t1\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0
        item = 2570\tRing_of_life\tA_ring.\t1\t1\t1\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t0\t1
        """
    ));

    ItemBonusCatalog.EquipmentBonuses totalBonuses = bonusCatalog.totalBonuses(List.of(
        new BootstrapItemSlot(3, 35, 1),
        new BootstrapItemSlot(12, 2570, 1)
    ));

    assertThat(totalBonuses.attackStab()).isEqualTo(20);
    assertThat(totalBonuses.attackSlash()).isEqualTo(29);
    assertThat(totalBonuses.attackCrush()).isEqualTo(-2);
    assertThat(totalBonuses.defenceStab()).isZero();
    assertThat(totalBonuses.defenceSlash()).isEqualTo(3);
    assertThat(totalBonuses.defenceCrush()).isEqualTo(2);
    assertThat(totalBonuses.defenceMagic()).isEqualTo(1);
    assertThat(totalBonuses.strength()).isEqualTo(25);
    assertThat(totalBonuses.prayer()).isEqualTo(1);
  }

  @Test
  void ignoresMissingDefinitionsWhenTotalingEquipment() throws Exception {
    ItemBonusCatalog bonusCatalog = ItemBonusCatalog.parse(new StringReader(
        "item = 35\tExcalibur\tA_sword.\t160\t160\t181\t20\t29\t-2\t0\t0\t0\t3\t2\t1\t0\t25\t0\n"
    ));

    ItemBonusCatalog.EquipmentBonuses totalBonuses = bonusCatalog.totalBonuses(List.of(
        new BootstrapItemSlot(3, 35, 1),
        new BootstrapItemSlot(12, 99999, 1)
    ));

    assertThat(totalBonuses.attackStab()).isEqualTo(20);
    assertThat(totalBonuses.prayer()).isZero();
  }
}
