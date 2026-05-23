package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;

import com.veyrmoor.client.core.ClientViewModel;
import java.util.Map;
import com.veyrmoor.model.StaffRole;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReportAbuseControllerTest {

  @Test
  void acceptsOnlyLegacyNameCharactersUpToTwelveCharacters() {
    ReportAbuseController controller = new ReportAbuseController(
        new ReportAbuseController.ResolvedInterface(550, 488, 314, 5984, 5985, Map.of(5971, 601))
    );

    controller.open();
    assertThat(controller.nameFieldText()).isEqualTo(ReportAbuseController.EMPTY_NAME_PLACEHOLDER);

    controller.open();
    for (int codePoint : "Akira 1234!_5678".codePoints().toArray()) {
      controller.appendCodePoint(codePoint);
    }

    assertThat(controller.reportedName()).isEqualTo("Akira 123456");
    assertThat(controller.nameFieldText()).isEqualTo("Akira 123456*");
  }

  @Test
  void showsMuteToggleOnlyForModeratorsAndAdmins() {
    ReportAbuseController controller = new ReportAbuseController(
        new ReportAbuseController.ResolvedInterface(550, 488, 314, 5984, 5985, Map.of(5971, 601))
    );

    controller.open();

    assertThat(controller.muteToggleText(viewModel(StaffRole.NONE))).isEmpty();
    assertThat(controller.muteToggleText(viewModel(StaffRole.MODERATOR)))
        .isEqualTo("Moderator option: Mute player for 48 hours: @red@<OFF>");
    assertThat(controller.muteToggleRgb()).isEqualTo(0xffffff);

    controller.handleActionWidget(controller.muteToggleComponentId(), viewModel(StaffRole.MODERATOR));

    assertThat(controller.muteRequested()).isTrue();
    assertThat(controller.muteToggleText(viewModel(StaffRole.ADMIN)))
        .isEqualTo("Moderator option: Mute player for 48 hours: @gre@<ON>");
    assertThat(controller.muteToggleRgb()).isEqualTo(0xffffff);
  }

  @Test
  void selectingARuleClosesTheModal() {
    ReportAbuseController controller = new ReportAbuseController(
        new ReportAbuseController.ResolvedInterface(550, 488, 314, 5984, 5985, Map.of(5971, 601))
    );

    controller.open();
    controller.appendCodePoint('A');
    controller.handleActionWidget(5971, viewModel(StaffRole.MODERATOR));

    assertThat(controller.isOpen()).isFalse();
    assertThat(controller.reportedName()).isEmpty();
    assertThat(controller.muteRequested()).isFalse();
  }

  private static ClientViewModel viewModel(StaffRole staffRole) {
    return new ClientViewModel(
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
            new BootstrapProfile(staffRole, false, 100),
            new BootstrapAppearance(List.of()),
            List.of(),
            List.of(),
            List.of()
        ),
        null,
        List.of()
    );
  }
}
