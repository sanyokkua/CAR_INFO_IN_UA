package ua.kostenko.carinfo.common.api;

import lombok.*;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Date;
import java.util.Map;
import java.util.Objects;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class ParamsHolder {
    private Pageable page;
    private Map<String, Long> longValues;
    private Map<String, Integer> integerValues;
    private Map<String, String> stringValues;
    private Map<String, Date> dateValues;

    public Pageable getPage() {
        return this.page;
    }

    @Nullable
    public Long getLong(@NonNull @Nonnull String key) {
        if (Objects.nonNull(longValues)) {
            return longValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public Integer getInt(@NonNull @Nonnull String key) {
        if (Objects.nonNull(integerValues)) {
            return integerValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public String getString(@NonNull @Nonnull String key) {
        if (Objects.nonNull(stringValues)) {
            return stringValues.getOrDefault(key, null);
        }
        return null;
    }

    @Nullable
    public Date getDate(@NonNull @Nonnull String key) {
        if (Objects.nonNull(dateValues)) {
            return dateValues.getOrDefault(key, null);
        }
        return null;
    }
}
