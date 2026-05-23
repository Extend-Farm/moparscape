package com.veyrmoor.client.core;

import com.veyrmoor.content.ContentManifest;

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
