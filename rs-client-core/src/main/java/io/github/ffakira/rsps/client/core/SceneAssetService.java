package io.github.ffakira.rsps.client.core;

import java.nio.file.Path;

public interface SceneAssetService {

  SceneBootstrapAssets loadInitialAssets(Path workingDirectory);
}
