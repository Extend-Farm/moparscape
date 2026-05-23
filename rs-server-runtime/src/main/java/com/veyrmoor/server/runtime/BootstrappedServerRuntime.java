package com.veyrmoor.server.runtime;

import com.veyrmoor.content.ContentManifest;

public record BootstrappedServerRuntime(ContentManifest contentManifest, WorldShardActor worldShardActor) {
}
