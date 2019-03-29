package ua.kostenko.carinfo.common.database.services;

import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface CrudService<T> {
    @Nullable
    T findById(@Nonnull @NonNull Long id);
    @Nullable
    T findByField(@Nonnull @NonNull String field);
    @Nullable
    T create(@Nonnull @NonNull T entity);
    @Nullable
    T update(@Nonnull @NonNull T entity);
    void delete(@Nonnull @NonNull Long id);
    List<T> findAll();
    boolean isExists(@Nonnull @NonNull T entity);
}
