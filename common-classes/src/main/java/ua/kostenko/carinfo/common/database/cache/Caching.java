package ua.kostenko.carinfo.common.database.cache;

import com.google.common.cache.LoadingCache;
import lombok.NonNull;

import javax.annotation.Nonnull;

public interface Caching<T> {
    int DEFAULT_CACHE_SIZE = 100;
    int DEFAULT_CACHE_TIME_SECONDS = 30;

    default T getFromCache(@NonNull @Nonnull String key) {
        return getCache().getUnchecked(key);
    }

    LoadingCache<String, T> getCache();

}
