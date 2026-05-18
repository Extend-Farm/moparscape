package io.github.ffakira.rsps.content;

import io.github.ffakira.rsps.cache.CacheStoreLayout;
import java.nio.file.Path;

public record ContentManifest(CacheStoreLayout cacheStore, Path legacyDataDirectory) {
}
