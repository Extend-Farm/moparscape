package com.veyrmoor.server.runtime;

import com.veyrmoor.content.ContentBootstrapService;

public class ServerRuntimeBootstrap {

  public BootstrappedServerRuntime bootstrap(ServerRuntimeConfiguration configuration) {
    var contentManifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(configuration.workingDirectory());
    WorldShardActor worldShardActor = new WorldShardActor();
    worldShardActor.start();
    return new BootstrappedServerRuntime(contentManifest, worldShardActor);
  }
}
