package io.github.ffakira.rsps.server.runtime;

import io.github.ffakira.rsps.content.ContentManifest;

public record BootstrappedServerRuntime(ContentManifest contentManifest, WorldShardActor worldShardActor) {
}
