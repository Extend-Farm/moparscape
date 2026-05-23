package com.veyrmoor.content;

import com.veyrmoor.cache.CacheStoreLayout;
import java.nio.file.Path;

public record ContentManifest(CacheStoreLayout cacheStore, Path legacyDataDirectory) {
}
