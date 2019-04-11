package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.database.PageableSearch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface DBRepository<T> extends PageableSearch<T> {

    @Nullable T create(@NonNull @Nonnull final T entity);
    @Nullable T update(@NonNull @Nonnull final T entity);
    boolean delete(final long id);
    boolean existId(final long id);
    boolean exist(@NonNull @Nonnull final T entity);
    @Nullable T findOne(final long id);
    @Nullable T findOne(@Nonnull @NonNull final ParamsHolder searchParams);
    List<T> find();
}
