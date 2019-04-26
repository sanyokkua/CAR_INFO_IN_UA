package ua.kostenko.carinfo.common.api;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CacheEventLogger implements CacheEventListener<Object, Object> {

    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        log.debug("Cache event type: {}. CacheKey {}, cacheOldValue {}, cacheNewValue {}", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }

}
