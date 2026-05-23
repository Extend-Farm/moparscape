package com.veyrmoor.client.desktop.gameplay.sidebar.widget;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.gameplay.sidebar.GameplayStatsSidebarModel;
import com.veyrmoor.content.InterfaceComponentDefinition;
import com.veyrmoor.model.StaffRole;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.BootstrapSkill;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.List;
import org.junit.jupiter.api.Test;

class SidebarWidgetTextResolverTest {

  @Test
  void interpolatesPrayerSkillTokensFromTheCurrentBootstrapSnapshot() {
    InterfaceComponentDefinition component = new InterfaceComponentDefinition(
        687,
        5608,
        4,
        0,
        0,
        0,
        0,
        0,
        -1,
        new int[0],
        new int[0],
        new int[][]{
            {1, 5, 0},
            {2, 5, 0}
        },
        null,
        null,
        false,
        new InterfaceComponentDefinition.TextBlock(false, 1, true, "Prayer: %1/%2", ""),
        null,
        null,
        null,
        null,
        0,
        false,
        "",
        "",
        -1,
        ""
    );

    ClientViewModel viewModel = new ClientViewModel(
        "In world",
        true,
        null,
        -1,
        1L,
        2L,
        null,
        new CharacterBootstrapPayload(
            1L,
            2L,
            "Akira",
            null,
            null,
            new BootstrapProfile(StaffRole.NONE, false, 100),
            new BootstrapAppearance(List.of()),
            List.of(),
            List.of(),
            List.of(new BootstrapSkill(5, 61, GameplayStatsSidebarModel.experienceForLevel(62)))
        ),
        null,
        List.of()
    );

    assertThat(SidebarWidgetTextResolver.interpolate(component, component.textBlock().defaultText(), viewModel))
        .isEqualTo("Prayer: 61/62");
  }

  @Test
  void interpolatesRunEnergyAndWorldCoordinatesFromTheCurrentViewModel() {
    InterfaceComponentDefinition component = new InterfaceComponentDefinition(
        688,
        5608,
        4,
        0,
        0,
        0,
        0,
        0,
        -1,
        new int[0],
        new int[0],
        new int[][]{
            {11, 0},
            {18, 0},
            {19, 0}
        },
        null,
        null,
        false,
        new InterfaceComponentDefinition.TextBlock(false, 1, true, "Run: %1 X: %2 Y: %3", ""),
        null,
        null,
        null,
        null,
        0,
        false,
        "",
        "",
        -1,
        ""
    );

    ClientViewModel viewModel = new ClientViewModel(
        "In world",
        true,
        new WorldPoint(3208, 3210, 0),
        -1,
        1L,
        2L,
        null,
        new CharacterBootstrapPayload(
            1L,
            2L,
            "Akira",
            null,
            null,
            new BootstrapProfile(StaffRole.NONE, false, 82),
            new BootstrapAppearance(List.of()),
            List.of(),
            List.of(),
            List.of()
        ),
        null,
        List.of()
    );

    assertThat(SidebarWidgetTextResolver.interpolate(component, component.textBlock().defaultText(), viewModel))
        .isEqualTo("Run: 82 X: 3208 Y: 3210");
  }
}
