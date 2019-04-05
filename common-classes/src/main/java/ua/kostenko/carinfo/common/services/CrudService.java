package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.ParamHolderBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface CrudService<T> {
    @Nullable
    T create(@Nonnull @NonNull T entity);
    @Nullable
    T update(@Nonnull @NonNull T entity);
    boolean delete(@Nonnull @NonNull Long id);
    boolean isExists(@Nonnull @NonNull T entity);

    List<T> findAll();
    Page<T> find(ParamHolderBuilder paramHolder);
}
