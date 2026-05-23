package com.veyrmoor.client.core;

import com.veyrmoor.protocol.bootstrap.CharacterBootstrapMessage;
import com.veyrmoor.protocol.bootstrap.CharacterBootstrapPayload;
import com.veyrmoor.protocol.chat.PublicChatMessage;
import com.veyrmoor.protocol.world.EntityActionSequenceMessage;
import com.veyrmoor.model.WorldPoint;
import com.veyrmoor.protocol.world.EntityPositionMessage;
import com.veyrmoor.protocol.session.HandshakeAccepted;
import com.veyrmoor.protocol.session.LoginAccepted;
import com.veyrmoor.protocol.session.LoginRejected;
import com.veyrmoor.protocol.ServerMessage;
import com.veyrmoor.protocol.world.WorldSnapshotMessage;
import java.util.ArrayList;
import java.util.List;

public class ClientCore {

  private static final int MAX_CHAT_MESSAGES = 100;

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
          viewModel = new ClientViewModel(
              handshakeAccepted.motd(),
              false,
              null,
              -1,
              null,
              null,
              null,
              null,
              null,
              viewModel.chatMessages()
          );
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
            null,
            viewModel.chatMessages()
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
            bootstrapPresentation,
            viewModel.chatMessages()
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
            preservePresentation(bootstrap),
            viewModel.chatMessages()
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
            preservePresentation(bootstrap),
            viewModel.chatMessages()
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
              viewModel.bootstrapPresentation(),
              viewModel.chatMessages()
          );
        }
      }
      case PublicChatMessage publicChatMessage -> viewModel = new ClientViewModel(
          viewModel.statusText(),
          viewModel.loggedIn(),
          viewModel.localPlayerPosition(),
          viewModel.localPlayerActionSequenceId(),
          viewModel.accountId(),
          viewModel.characterId(),
          viewModel.regionKey(),
          viewModel.bootstrap(),
          viewModel.bootstrapPresentation(),
          appendChatMessage(ClientChatMessage.publicChat(
              publicChatMessage.speakerDisplayName(),
              publicChatMessage.text()
          ))
      );
      case LoginRejected loginRejected -> {
        viewModel = new ClientViewModel(
            loginRejected.reason(),
            false,
            null,
            -1,
            null,
            null,
            null,
            null,
            null,
            viewModel.chatMessages()
        );
        events.add(new ClientDisconnectedEvent(loginRejected.reason()));
      }
      default -> throw new IllegalArgumentException("Unsupported server message: " + message.getClass().getName());
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

  private List<ClientChatMessage> appendChatMessage(ClientChatMessage chatMessage) {
    List<ClientChatMessage> currentMessages = viewModel.chatMessages();
    int preservedMessages = Math.min(currentMessages.size(), MAX_CHAT_MESSAGES - 1);
    int startIndex = currentMessages.size() - preservedMessages;
    ArrayList<ClientChatMessage> nextMessages = new ArrayList<>(preservedMessages + 1);
    nextMessages.addAll(currentMessages.subList(startIndex, currentMessages.size()));
    nextMessages.add(chatMessage);
    return List.copyOf(nextMessages);
  }
}
