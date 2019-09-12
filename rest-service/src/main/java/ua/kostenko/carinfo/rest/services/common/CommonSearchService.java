package ua.kostenko.carinfo.rest.services.common;

import java.sql.Date;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.GenericRecord;
import ua.kostenko.carinfo.common.api.services.DBService;

public abstract class CommonSearchService<T extends GenericRecord<I>, I> implements SearchService<T, I> {

    private final DBService<T> service;

    public CommonSearchService(DBService<T> service) {
        this.service = service;
    }

    @Override
    public T getById(@NonNull @Nonnull Long id) {
        return service.get(id).orElseGet(null);
    }

    @Override
    public Page<T> getAll(Pageable pageable) {
        return service.getAll(getBuilder(pageable));
    }

    @Override
    public Page<T> findForField(@Nonnull @NonNull String field, Pageable pageable) {
        String findForFieldParam = getFindForFieldParam();
        ParamsHolderBuilder builder = getBuilder(pageable);
        builder.param(findForFieldParam, field);
        return service.getAll(builder);
    }

    protected abstract String getFindForFieldParam();

    @Override
    public Page<T> findByParams(@Nonnull @NonNull Map<String, Object> params, Pageable pageable) {
        ParamsHolderBuilder builder = getBuilder(pageable);
        addParamsToBuilder(params, builder);
        return service.getAll(builder);
    }

    private void addParamsToBuilder(@NonNull @Nonnull Map<String, Object> params,
            @NonNull @Nonnull ParamsHolderBuilder builder) {
        params.forEach((key, value) -> {
            if (value instanceof String) {
                builder.param(key, (String) value);
            } else if (value instanceof Long) {
                builder.param(key, (Long) value);
            } else if (value instanceof Integer) {
                builder.param(key, (Integer) value);
            } else if (value instanceof Date) {
                builder.param(key, (Date) value);
            } else {
                throw new IllegalArgumentException(
                        "Type of this value is not supported. Type = " + value.getClass().getSimpleName());
            }
        });
    }

    @Override
    public int countAll() {
        return service.countAll();
    }

    @Override
    public int countForField(@Nonnull @NonNull String field) {
        String findForFieldParam = getFindForFieldParam();
        ParamsHolderBuilder builder = getBuilder(null);
        builder.param(findForFieldParam, field);
        return service.countAll(builder);
    }

    @Override
    public int countByParams(@Nonnull @NonNull Map<String, Object> params) {
        ParamsHolderBuilder builder = getBuilder(null);
        addParamsToBuilder(params, builder);
        return service.countAll(builder);
    }

    @Nonnull
    private ParamsHolderBuilder getBuilder(@Nullable Pageable pageable) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        if (Objects.nonNull(pageable)) {
            int pageNumber = pageable.getPageNumber();
            builder.page(pageNumber + 1);
        }
        return builder;
    }
}
