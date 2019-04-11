package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FieldSearchable<T> {
    @Nullable
    T findByField(@Nonnull @NonNull String fieldValue);
    @Nullable
    T find(@NonNull @Nonnull T entity);
}
