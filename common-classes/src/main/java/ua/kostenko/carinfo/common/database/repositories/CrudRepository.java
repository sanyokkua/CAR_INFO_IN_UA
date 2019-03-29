package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import org.springframework.dao.EmptyResultDataAccessException;
import ua.kostenko.carinfo.common.database.cache.Caching;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public interface CrudRepository<T> extends Caching<T> {
    @Nullable
    T create(@NonNull @Nonnull T entity);
    @Nullable
    T update(@NonNull @Nonnull T entity);
    boolean delete(@NonNull @Nonnull Long id);
    @Nullable
    T find(@NonNull @Nonnull Long id);
    List<T> findAll();
    T findByField(@NonNull String fieldValue);
    boolean isExistsId(@NonNull @Nonnull Long id);
    boolean isExists(@NonNull @Nonnull T entity);
    void createAll(Iterable<T> entities);

    static <T> T getNullableResultIfException(Supplier<T> supplier) {
        T t = null;
        try {
            t = supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            //TODO: ignored, returning null if any exception occurred
        }
        return t;
    }
}
