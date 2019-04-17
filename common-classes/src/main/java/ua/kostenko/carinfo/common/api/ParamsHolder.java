package ua.kostenko.carinfo.common.api;

import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;

@Builder
public class ParamsHolder {
    private Pageable page;
    private Map<String, Long> longValues;
    private Map<String, Integer> integerValues;
    private Map<String, String> stringValues;
    private Map<String, Date> dateValues;

    private ParamsHolder() {
    }

    private ParamsHolder(Pageable page, Map<String, Long> longValues, Map<String, Integer> integerValues, Map<String, String> stringValues, Map<String, Date> dateValues) {
        this.page = page;
        this.longValues = longValues;
        this.integerValues = integerValues;
        this.stringValues = stringValues;
        this.dateValues = dateValues;
    }

    public Pageable getPage() {
        return this.page;
    }

    @Nullable
    public Long getLong(@NonNull @Nonnull String key) {
        if (Objects.nonNull(longValues)) {
            longValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public Integer getInt(@NonNull @Nonnull String key) {
        if (Objects.nonNull(integerValues)) {
            integerValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public String getString(@NonNull @Nonnull String key) {
        if (Objects.nonNull(stringValues)) {
            stringValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public Date getDate(@NonNull @Nonnull String key) {
        if (Objects.nonNull(dateValues)) {
            dateValues.getOrDefault(key, null);
        }
        return null;
    }
}
