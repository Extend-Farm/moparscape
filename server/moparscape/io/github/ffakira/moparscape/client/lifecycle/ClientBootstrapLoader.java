package io.github.ffakira.moparscape.client.lifecycle;

import io.github.ffakira.moparscape.cache.CacheFileStore;
import io.github.ffakira.moparscape.sign.SignLink;

public final class ClientBootstrapLoader {

    private ClientBootstrapLoader() {
    }

    public static boolean shouldUseSunJavaLoopTuning()
    {
        return SignLink.sunjava;
    }

    public static CacheFileStore[] createCacheStores()
    {
        if(SignLink.cache_dat == null)
            return null;
        CacheFileStore[] cacheStores = new CacheFileStore[5];
        for(int index = 0; index < 5; index++)
            cacheStores[index] = new CacheFileStore(0x7a120, SignLink.cache_dat, SignLink.cache_idx[index], index + 1, true);
        return cacheStores;
    }
}
