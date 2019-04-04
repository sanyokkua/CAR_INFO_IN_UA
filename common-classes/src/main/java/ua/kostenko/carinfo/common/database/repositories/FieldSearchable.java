package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface FieldSearchable<T> {
    @Nullable
    T findByField(@Nonnull @NonNull String fieldValue);
}
