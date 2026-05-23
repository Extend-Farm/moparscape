package com.veyrmoor.client.core;

import com.veyrmoor.content.ContentBootstrapService;
import com.veyrmoor.content.ContentManifest;
import com.veyrmoor.content.ItemDefinitionCatalog;
import java.nio.file.Path;

public class DefaultSceneAssetService implements SceneAssetService {

  @Override
  public SceneBootstrapAssets loadInitialAssets(Path workingDirectory) {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
    return new SceneBootstrapAssets(manifest, BootstrapPresentationCatalog.from(ItemDefinitionCatalog.load(manifest)));
  }
}
