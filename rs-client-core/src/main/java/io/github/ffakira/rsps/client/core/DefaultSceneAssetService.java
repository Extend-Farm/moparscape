package io.github.ffakira.rsps.client.core;

import io.github.ffakira.rsps.content.ContentBootstrapService;
import io.github.ffakira.rsps.content.ContentManifest;
import io.github.ffakira.rsps.content.ItemDefinitionCatalog;
import java.nio.file.Path;

public class DefaultSceneAssetService implements SceneAssetService {

  @Override
  public SceneBootstrapAssets loadInitialAssets(Path workingDirectory) {
    ContentManifest manifest = new ContentBootstrapService().bootstrapFromWorkingDirectory(workingDirectory);
    return new SceneBootstrapAssets(manifest, BootstrapPresentationCatalog.from(ItemDefinitionCatalog.load(manifest)));
  }
}
