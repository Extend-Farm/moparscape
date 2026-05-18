package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.content.ContentManifest;

public record SceneBootstrapAssets(
    ContentManifest manifest,
    BootstrapPresentationCatalog bootstrapPresentationCatalog
) {

  public SceneBootstrapAssets {
    bootstrapPresentationCatalog =
        bootstrapPresentationCatalog == null ? BootstrapPresentationCatalog.empty() : bootstrapPresentationCatalog;
  }

  public static SceneBootstrapAssets empty() {
    return new SceneBootstrapAssets(null, BootstrapPresentationCatalog.empty());
  }
}
