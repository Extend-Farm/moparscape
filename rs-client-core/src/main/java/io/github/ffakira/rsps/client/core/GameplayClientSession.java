package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.model.MovementMode;
import io.github.ffakira.rsps.protocol.DisconnectNotice;
import io.github.ffakira.rsps.protocol.HandshakeRequest;
import io.github.ffakira.rsps.protocol.LoginRequest;
import io.github.ffakira.rsps.protocol.MoveIntentMessage;
import io.github.ffakira.rsps.protocol.ProtocolVersion;
import io.github.ffakira.rsps.protocol.ServerMessage;
import java.nio.file.Path;
import java.util.List;

public final class GameplayClientSession implements AutoCloseable {

  private final ClientCore clientCore;
  private final ClientTransport transport;
  private final String clientDescriptor;
  private final SceneAssetService sceneAssetService;
  private final Path workingDirectory;

  public GameplayClientSession(ClientCore clientCore, ClientTransport transport, String clientDescriptor) {
    this(clientCore, transport, clientDescriptor, new DefaultSceneAssetService(), Path.of("."));
  }

  GameplayClientSession(
      ClientCore clientCore,
      ClientTransport transport,
      String clientDescriptor,
      SceneAssetService sceneAssetService,
      Path workingDirectory
  ) {
    this.clientCore = clientCore;
    this.transport = transport;
    this.clientDescriptor = clientDescriptor;
    this.sceneAssetService = sceneAssetService;
    this.workingDirectory = workingDirectory;
  }

  public List<ClientEvent> bootstrap() {
    SceneBootstrapAssets bootstrapAssets = sceneAssetService.loadInitialAssets(workingDirectory);
    return List.of(clientCore.bootstrap(bootstrapAssets));
  }

  public void connect() {
    transport.send(new HandshakeRequest(ProtocolVersion.current(), clientDescriptor));
  }

  public void login(String username, String password) {
    transport.send(new LoginRequest(username, password));
  }

  public void move(int deltaX, int deltaY, MovementMode movementMode) {
    if (!clientCore.viewModel().loggedIn()) {
      return;
    }
    transport.send(new MoveIntentMessage(deltaX, deltaY, movementMode));
  }

  public List<ClientEvent> accept(ServerMessage message) {
    return clientCore.accept(message);
  }

  public ClientViewModel viewModel() {
    return clientCore.viewModel();
  }

  @Override
  public void close() {
    if (clientCore.viewModel().loggedIn()) {
      transport.send(new DisconnectNotice("Client shutdown"));
    }
    transport.close();
  }
}
