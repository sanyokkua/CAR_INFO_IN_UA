package ua.kostenko.carinfo.common.cache;

import com.google.common.cache.LoadingCache;
import lombok.NonNull;

import javax.annotation.Nonnull;

public interface Caching<T> {
    int DEFAULT_CACHE_SIZE = 100;
    int DEFAULT_CACHE_TIME_SECONDS = 30;

    default T getFromCache(@NonNull @Nonnull String key) {
        LoadingCache<String, T> cache = getCache();
        T result = null;
        try {
            result = cache.get(key);
        } catch (Exception e) {
            //ignoring
        }
        return result;
    }

    LoadingCache<String, T> getCache();

}
