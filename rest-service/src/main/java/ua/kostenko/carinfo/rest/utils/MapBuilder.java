package ua.kostenko.carinfo.rest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.NonNull;

public class MapBuilder<K, V> {

    private final Map<K, V> map;

    private MapBuilder() {
        this.map = new HashMap<>();
    }

    public static <K, V> MapBuilder<K, V> getBuilder() {
        return new MapBuilder<>();
    }

    public MapBuilder<K, V> put(@NonNull @Nonnull K key, @Nullable V value) {
        if (Objects.nonNull(value)) {
            map.put(key, value);
        }
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
