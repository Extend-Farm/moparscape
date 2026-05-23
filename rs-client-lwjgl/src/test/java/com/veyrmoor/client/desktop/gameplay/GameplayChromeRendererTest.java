package com.veyrmoor.client.desktop.gameplay;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import com.veyrmoor.client.core.ClientChatMessage;
import com.veyrmoor.client.core.ClientViewModel;
import com.veyrmoor.client.desktop.login.TitleScreenAssetLoader;
import com.veyrmoor.client.desktop.login.TitleScreenFonts;
import com.veyrmoor.client.desktop.render.common.ScreenRect;
import com.veyrmoor.model.StaffRole;
import com.veyrmoor.client.desktop.world.WorldCameraState;
import com.veyrmoor.protocol.bootstrap.BootstrapAppearance;
import com.veyrmoor.protocol.bootstrap.BootstrapProfile;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameplayChromeRendererTest {

  @Test
  void minimapUiYawUsesTheSameSignAsWorldCameraYaw() {
    WorldCameraState cameraState = new WorldCameraState(26.0f, 90.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f);

    assertThat(GameplayChromeRenderer.minimapUiYawDegrees(cameraState)).isEqualTo(90.0f);
  }

  @Test
  void minimapUiYawDefaultsToZeroWithoutCameraState() {
    assertThat(GameplayChromeRenderer.minimapUiYawDegrees(null)).isZero();
  }

  @Test
  void minimapMarkerOffsetMatchesNorthUpRadarWithoutCameraRotation() {
    WorldCameraState cameraState = new WorldCameraState(26.0f, 0.0f, 20.0f, -1.0f, 33.0f, 34.0f, 0.0f);
    float expectedScale = 256.0f / 251.0f;

    float[] eastMarker = GameplayChromeRenderer.minimapMarkerOffset(10.0f, 0.0f, cameraState);
    float[] northMarker = GameplayChromeRenderer.minimapMarkerOffset(0.0f, 10.0f, cameraState);

    assertThat(eastMarker[0]).isCloseTo(10.0f * expectedScale, within(0.0001f));
    assertThat(eastMarker[1]).isZero();
    assertThat(northMarker[0]).isZero();
    assertThat(northMarker[1]).isCloseTo(-10.0f * expectedScale, within(0.0001f));
  }

  @Test
  void usesRawMapFunctionIdsAsDirectSpriteLookupIndices() {
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(0)).isZero();
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(6)).isEqualTo(6);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(26)).isEqualTo(26);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(55)).isEqualTo(55);
    assertThat(GameplayChromeRenderer.legacyMapFunctionSpriteIndex(60)).isEqualTo(60);
  }

  @Test
  void formatsSceneActionHintWithoutExtraSuffixForTwoOptions() {
    assertThat(GameplayChromeRenderer.formatSceneActionHint("Walk here", 2)).isEqualTo("Walk here");
  }

  @Test
  void formatsSceneActionHintWithMoreOptionsSuffixBeyondTwoEntries() {
    assertThat(GameplayChromeRenderer.formatSceneActionHint("Walk here", 3)).isEqualTo("Walk here / 1 more options");
    assertThat(GameplayChromeRenderer.formatSceneActionHint("Walk here", 5)).isEqualTo("Walk here / 3 more options");
  }

  @Test
  void anchorsSceneActionHintToTheReferenceViewportOrigin() {
    ScreenRect worldViewport = GameplayLayout.worldViewportInnerRect();

    assertThat(GameplayChromeRenderer.sceneActionHintLeft(worldViewport)).isEqualTo(worldViewport.left());
    assertThat(GameplayChromeRenderer.sceneActionHintBaselineY(worldViewport)).isEqualTo(worldViewport.top() + 11.0f);
  }

  @Test
  void picksTheLatestLocalPublicChatLineForOverheadRendering() {
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
            List.of()
        ),
        null,
        List.of(
            ClientChatMessage.system("Welcome."),
            ClientChatMessage.publicChat("Other", "Hello"),
            ClientChatMessage.publicChat("Akira", "First"),
            ClientChatMessage.publicChat("Akira", "Newest")
        )
    );

    assertThat(GameplayChromeRenderer.latestLocalPublicChatMessage(viewModel))
        .extracting(ClientChatMessage::text)
        .isEqualTo("Newest");
  }

  @Test
  void constrainsOverheadChatTextToTheWorldViewportWidth() {
    TitleScreenFonts fonts = TitleScreenAssetLoader.loadFromWorkingDirectory(Path.of(".")).fonts();
    float maxWidth = GameplayLayout.worldViewportInnerRect().width() - 8.0f;

    String constrained = GameplayChromeRenderer.fitOverheadChatText(
        fonts.bold(),
        "Aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        maxWidth
    );

    assertThat(fonts.bold().measureText(constrained)).isLessThanOrEqualTo(Math.round(maxWidth));
    assertThat(constrained).endsWith("...");
  }

  private static double centerX(ScreenRect rect) {
    return rect.left() + rect.width() * 0.5;
  }

  private static double centerY(ScreenRect rect) {
    return rect.top() + rect.height() * 0.5;
  }
}
