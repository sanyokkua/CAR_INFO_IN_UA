package ua.kostenko.carinfo.common.database.repositories;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.GenericRecord;
import ua.kostenko.carinfo.common.database.PageableSearch;

public interface DBRepository<T extends GenericRecord<R>, R> extends PageableSearch<T> {

    @Nullable
    T create(@NonNull @Nonnull final T entity);

    @Nullable
    T update(@NonNull @Nonnull final T entity);

    boolean delete(final long id);

    boolean existId(final long id);

    boolean exist(@NonNull @Nonnull final T entity);

    @Nullable
    T findOne(final long id);

    @Nullable
    T findOne(@Nonnull @NonNull final ParamsHolder searchParams);

    List<T> find();

    boolean existsByIndex(@NonNull @Nonnull final R indexField);

    int countAll();

    int countAll(@Nonnull @NonNull final ParamsHolder searchParams);
}
