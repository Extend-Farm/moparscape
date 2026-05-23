package com.veyrmoor.server.runtime;

import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServerRuntimeMain {

  private static final Logger log = LoggerFactory.getLogger(ServerRuntimeMain.class);

  private ServerRuntimeMain() {
  }

  public static void main(String[] args) {
    ServerRuntimeConfiguration configuration = ServerRuntimeConfiguration.defaults(Path.of("."));
    BootstrappedServerRuntime runtime = new ServerRuntimeBootstrap().bootstrap(configuration);
    log.info(
        "Bootstrapped new RSPS runtime against cache store {}",
        runtime.contentManifest().cacheStore().rootDirectory()
    );
    runtime.worldShardActor().close();
  }
}
