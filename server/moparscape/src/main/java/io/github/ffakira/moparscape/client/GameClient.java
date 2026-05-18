package io.github.ffakira.moparscape.client;

public final class GameClient extends GameClientCore {

  public GameClient() {
    super();
  }

  public static GameClient launch(String username, String password) {
    return launch(username, password, null, true);
  }

  public static GameClient launch(
      String username,
      String password,
      String serverAddress,
      boolean autoSubmit
  ) {
    try {
      initializeStandaloneClientRuntime();
      GameClient gameClient = new GameClient();
      gameClient.configureBootstrapLogin(username, password, serverAddress, autoSubmit);
      gameClient.method1(503, false, 765);
      return gameClient;
    } catch (Exception exception) {
      throw new IllegalStateException("Unable to launch the desktop game client", exception);
    }
  }

  public static EmbeddedGameClientSession launchEmbedded(String username, String password) {
    return launchEmbedded(username, password, null, true);
  }

  public static EmbeddedGameClientSession launchEmbedded(
      String username,
      String password,
      String serverAddress,
      boolean autoSubmit
  ) {
    String normalizedServerAddress = serverAddress == null || serverAddress.isBlank()
        ? GameServerEndpoint.DEFAULT_ADDRESS
        : serverAddress;
    try {
      EmbeddedGameClientHost host = new EmbeddedGameClientHost(765, 503, normalizedServerAddress);
      initializeStandaloneClientRuntime(normalizedServerAddress, host);
      GameClient gameClient = new GameClient();
      gameClient.configureBootstrapLogin(username, password, normalizedServerAddress, autoSubmit);
      gameClient.method2(503, false, 765);
      return new EmbeddedGameClientSession(host, gameClient);
    } catch (Exception exception) {
      throw new IllegalStateException("Unable to launch the embedded game client", exception);
    }
  }
}
