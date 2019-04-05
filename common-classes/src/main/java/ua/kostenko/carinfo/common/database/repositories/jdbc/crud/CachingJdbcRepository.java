package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.kostenko.carinfo.common.cache.Caching;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

abstract class CachingJdbcRepository<T> extends JdbcRepository<T> implements Caching<T> {
    final LoadingCache<String, T> cache;

    public CachingJdbcRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        cache = CacheBuilder.newBuilder().maximumSize(DEFAULT_CACHE_SIZE).expireAfterWrite(DEFAULT_CACHE_TIME_SECONDS, TimeUnit.SECONDS).build(getCacheLoader());
    }

    abstract CacheLoader<String, T> getCacheLoader();

    @Override
    public LoadingCache<String, T> getCache() {
        return cache;
    }
}
