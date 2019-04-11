package ua.kostenko.carinfo.common.api.services;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Slf4j
abstract class CommonDbService <T> implements DBService<T> {

    @Override
    public Optional<T> create(@Nonnull T entity) {
        return Optional.empty();
    }

    @Override
    public Optional<T> update(@Nonnull T entity) {
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public boolean exists(@Nonnull T entity) {
        return false;
    }

    @Override
    public boolean isValid(@Nonnull T entity) {
        return false;
    }

    @Override
    public Optional<T> get(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<T> get(@Nonnull ParamsHolderBuilder builder) {
        return Optional.empty();
    }

    @Override
    public List<T> getAll() {
        return Lists.newArrayList();
    }

    @Override
    public Page<T> getAll(@Nonnull ParamsHolderBuilder builder) {
        return Page.empty();
    }
}
