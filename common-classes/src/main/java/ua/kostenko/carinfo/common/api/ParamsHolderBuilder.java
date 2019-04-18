package ua.kostenko.carinfo.common.api;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ParamsHolderBuilder {
    private static final int DEFAULT_RECORDS_NUMBER = 10;
    private int records;
    private int page;
    private Map<String, Long> longValues = new HashMap<>();
    private Map<String, Integer> integerValues = new HashMap<>();
    private Map<String, String> stringValues = new HashMap<>();
    private Map<String, Date> dateValues = new HashMap<>();

    public ParamsHolderBuilder records(@Nullable Integer records) {
        this.records = Objects.nonNull(records) && records > 0 ? records : DEFAULT_RECORDS_NUMBER;
        return this;
    }

    public ParamsHolderBuilder page(@Nullable Integer page) {
        this.page = Objects.nonNull(page) && page > 0 ? page : 0;
        return this;
    }

    public ParamsHolderBuilder param(@NonNull @Nonnull String key, @Nullable Integer param) {
        if (Objects.nonNull(param)) {
            integerValues.put(key, param);
        }
        return this;
    }

    public ParamsHolderBuilder param(@NonNull @Nonnull String key, @Nullable Long param) {
        if (Objects.nonNull(param)) {
            longValues.put(key, param);
        }
        return this;
    }

    public ParamsHolderBuilder param(@NonNull @Nonnull String key, @Nullable String param) {
        if (StringUtils.isNotBlank(param)) {
            stringValues.put(key, param);
        }
        return this;
    }

    public ParamsHolderBuilder param(@NonNull @Nonnull String key, @Nullable Date param) {
        if (Objects.nonNull(param)) {
            dateValues.put(key, param);
        }
        return this;
    }

    public ParamsHolder build() {
        int pageNumber = getPage(page);
        int recordsPerPage = getAmount(records);
        return ParamsHolder.builder()
                           .page(PageRequest.of(pageNumber, recordsPerPage))
                           .longValues(longValues)
                           .integerValues(integerValues)
                           .stringValues(stringValues)
                           .dateValues(dateValues)
                           .build();
    }

    private int getPage(@Nullable Integer uiPageNumber) {
        int page = Objects.nonNull(uiPageNumber) && uiPageNumber >= 0 ? uiPageNumber : 0;
        return page > 0 ? page - 1 : page;
    }

    private int getAmount(@Nullable Integer recordsPerPage) {
        return Objects.nonNull(recordsPerPage) && recordsPerPage > 0 ? recordsPerPage : DEFAULT_RECORDS_NUMBER;
    }

    @Override
    public String toString() {
        return "Params{" +
                "records=" + records +
                ", page=" + page +
                ", longValues=" + longValues.toString() +
                ", integerValues=" + integerValues.toString() +
                ", stringValues=" + stringValues.toString() +
                '}';
    }
}
