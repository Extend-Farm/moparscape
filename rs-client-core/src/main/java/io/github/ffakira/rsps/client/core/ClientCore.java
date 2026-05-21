package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.protocol.CharacterBootstrapMessage;
import io.github.ffakira.rsps.protocol.CharacterBootstrapPayload;
import io.github.ffakira.rsps.protocol.EntityActionSequenceMessage;
import io.github.ffakira.rsps.model.WorldPoint;
import io.github.ffakira.rsps.protocol.EntityPositionMessage;
import io.github.ffakira.rsps.protocol.HandshakeAccepted;
import io.github.ffakira.rsps.protocol.LoginAccepted;
import io.github.ffakira.rsps.protocol.LoginRejected;
import io.github.ffakira.rsps.protocol.ServerMessage;
import io.github.ffakira.rsps.protocol.WorldSnapshotMessage;
import java.util.ArrayList;
import java.util.List;

public class ClientCore {

  private volatile ClientViewModel viewModel = new ClientViewModel("Booting", false, null);
  private volatile BootstrapPresentationCatalog bootstrapPresentationCatalog = BootstrapPresentationCatalog.empty();

  public ClientViewModel viewModel() {
    return viewModel;
  }

  public ClientEvent bootstrap() {
    return bootstrap(SceneBootstrapAssets.empty());
  }

  public ClientEvent bootstrap(SceneBootstrapAssets bootstrapAssets) {
    bootstrapPresentationCatalog = bootstrapAssets.bootstrapPresentationCatalog();
    viewModel = new ClientViewModel("Assets ready", false, null);
    return new ClientBootstrappedEvent(viewModel.statusText());
  }

  public List<ClientEvent> accept(ServerMessage message) {
    List<ClientEvent> events = new ArrayList<>();
    // This state machine intentionally distinguishes "authenticated" from "in world":
    // - HandshakeAccepted only updates the MOTD/status surface
    // - LoginAccepted marks the session as logged in, but there is still no world position
    // - CharacterBootstrapMessage installs the authoritative bootstrap snapshot and first
    //   usable world state
    // - later world/position messages mutate that state while preserving previously resolved
    //   presentation data where possible
    switch (message) {
      case HandshakeAccepted handshakeAccepted ->
          viewModel = new ClientViewModel(handshakeAccepted.motd(), false, null);
      case LoginAccepted loginAccepted -> {
        viewModel = new ClientViewModel(
            "Loading world...",
            true,
            null,
            -1,
            loginAccepted.accountId(),
            loginAccepted.characterId(),
            null,
            null,
            null
        );
        events.add(new LoginSucceededEvent(loginAccepted.accountId(), loginAccepted.characterId()));
      }
      case CharacterBootstrapMessage characterBootstrapMessage -> {
        CharacterBootstrapPayload bootstrap = characterBootstrapMessage.bootstrap();
        BootstrapCharacterPresentation bootstrapPresentation = bootstrapPresentationCatalog.present(bootstrap);
        viewModel = new ClientViewModel(
            "In world",
            true,
            bootstrap.worldPoint(),
            -1,
            bootstrap.accountId(),
            bootstrap.characterId(),
            bootstrap.regionKey(),
            bootstrap,
            bootstrapPresentation
        );
        events.add(new CharacterBootstrappedEvent(bootstrap));
        events.add(new WorldLoadedEvent(bootstrap.regionKey()));
      }
      case WorldSnapshotMessage worldSnapshotMessage -> {
        CharacterBootstrapPayload bootstrap = withWorldPosition(
            viewModel.bootstrap(),
            worldSnapshotMessage.regionKey(),
            worldSnapshotMessage.localPlayerPosition()
        );
        viewModel = new ClientViewModel(
            "In world",
            true,
            worldSnapshotMessage.localPlayerPosition(),
            viewModel.localPlayerActionSequenceId(),
            viewModel.accountId(),
            viewModel.characterId(),
            worldSnapshotMessage.regionKey(),
            bootstrap,
            preservePresentation(bootstrap)
        );
        events.add(new WorldLoadedEvent(worldSnapshotMessage.regionKey()));
      }
      case EntityPositionMessage entityPositionMessage -> {
        CharacterBootstrapPayload bootstrap = withWorldPosition(
            viewModel.bootstrap(),
            viewModel.regionKey(),
            entityPositionMessage.worldPoint()
        );
        viewModel = new ClientViewModel(
            viewModel.statusText(),
            viewModel.loggedIn(),
            entityPositionMessage.worldPoint(),
            viewModel.localPlayerActionSequenceId(),
            viewModel.accountId(),
            viewModel.characterId(),
            viewModel.regionKey(),
            bootstrap,
            preservePresentation(bootstrap)
        );
      }
      case EntityActionSequenceMessage entityActionSequenceMessage -> {
        if (viewModel.characterId() != null && viewModel.bootstrap() != null) {
          viewModel = new ClientViewModel(
              viewModel.statusText(),
              viewModel.loggedIn(),
              viewModel.localPlayerPosition(),
              entityActionSequenceMessage.actionSequenceId(),
              viewModel.accountId(),
              viewModel.characterId(),
              viewModel.regionKey(),
              viewModel.bootstrap(),
              viewModel.bootstrapPresentation()
          );
        }
      }
      case LoginRejected loginRejected -> {
        viewModel = new ClientViewModel(loginRejected.reason(), false, null);
        events.add(new ClientDisconnectedEvent(loginRejected.reason()));
      }
    }
    return List.copyOf(events);
  }

  private CharacterBootstrapPayload withWorldPosition(
      CharacterBootstrapPayload bootstrap,
      String regionKey,
      WorldPoint worldPoint
  ) {
    if (bootstrap == null) {
      return null;
    }
    return new CharacterBootstrapPayload(
        bootstrap.accountId(),
        bootstrap.characterId(),
        bootstrap.displayName(),
        regionKey,
        worldPoint,
        bootstrap.profile(),
        bootstrap.appearance(),
        bootstrap.inventory(),
        bootstrap.equipment(),
        bootstrap.skills()
    );
  }

  private BootstrapCharacterPresentation preservePresentation(CharacterBootstrapPayload bootstrap) {
    if (bootstrap == null) {
      return null;
    }
    BootstrapCharacterPresentation bootstrapPresentation = viewModel.bootstrapPresentation();
    if (bootstrapPresentation != null) {
      return bootstrapPresentation;
    }
    return bootstrapPresentationCatalog.present(bootstrap);
  }
}
