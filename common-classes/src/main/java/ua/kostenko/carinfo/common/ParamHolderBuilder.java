package ua.kostenko.carinfo.common;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ParamHolderBuilder {
    private static final int DEFAULT_RECORDS_NUMBER = 10;
    private int records;
    private int page;
    private Map<String, Long> longValues = new HashMap<>();
    private Map<String, Integer> integerValues = new HashMap<>();
    private Map<String, String> stringValues = new HashMap<>();

    public ParamHolderBuilder records(@Nullable Integer records) {
        this.records = Objects.nonNull(records) && records > 0 ? records : DEFAULT_RECORDS_NUMBER;
        return this;
    }

    public ParamHolderBuilder page(@Nullable Integer page) {
        this.page = Objects.nonNull(page) && page > 0 ? page : 0;
        return this;
    }

    public ParamHolderBuilder param(@NonNull @Nonnull String key, @Nullable Integer param) {
        if (Objects.nonNull(param)) {
            integerValues.put(key, param);
        }
        return this;
    }

    public ParamHolderBuilder param(@NonNull @Nonnull String key, @Nullable Long param) {
        if (Objects.nonNull(param)) {
            longValues.put(key, param);
        }
        return this;
    }

    public ParamHolderBuilder param(@NonNull @Nonnull String key, @Nullable String param) {
        if (StringUtils.isNotBlank(param)) {
            stringValues.put(key, param);
        }
        return this;
    }

    public ParamsHolder build() {
        return ParamsHolder.builder()
                           .page(PageRequest.of(page, records))
                           .longValues(longValues)
                           .integerValues(integerValues)
                           .stringValues(stringValues)
                           .build();
    }
}
