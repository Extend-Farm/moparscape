package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.content.ContentBootstrapService;

public class ServerRuntimeBootstrap {

  public BootstrappedServerRuntime bootstrap(ServerRuntimeConfiguration configuration) {
    var contentManifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(configuration.workingDirectory());
    WorldShardActor worldShardActor = new WorldShardActor();
    worldShardActor.start();
    return new BootstrappedServerRuntime(contentManifest, worldShardActor);
  }
}
