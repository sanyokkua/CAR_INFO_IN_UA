package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.PageableRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
abstract class DefaultCrudService<T> implements CrudService<T> {
    protected final PageableRepository<T> repository;

    DefaultCrudService(@NonNull @Nonnull PageableRepository<T> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public synchronized T create(@Nonnull T entity) {
        log.info("create: entity = {}", entity.toString());
        if (isValid(entity)) {
            log.info("create: entity is valid");
            if (repository.isExists(entity)) {
                log.info("create: entity exists");
                return repository.find(entity);
            } else {
                log.info("create: entity doesn't exist. Creating new entity");
                return repository.create(entity);
            }
        }
        log.info("create: entity is not valid, returning null");
        return null;
    }

    abstract boolean isValid(T entity);

    @Nullable
    @Override
    public synchronized T update(@Nonnull T entity) {
        log.info("update: entity = {}", entity.toString());
        if (isValid(entity)) {
            log.info("update: entity is valid");
            if (repository.isExists(entity)) {
                log.info("update: entity exists. Updating");
                return repository.update(entity);
            } else {
                log.warn("update: entity doesn't exist. throwing exception");
                throw new IllegalArgumentException("Entity is not exists");
            }
        }
        log.info("update: entity is not valid, returning null");
        return null;
    }

    @Override
    public synchronized boolean delete(@NonNull @Nonnull Long id) {
        log.info("delete: deleting entity with id: {}", id);
        boolean deleted = repository.delete(id);
        log.info("delete: entity with id: {} is deleted: ", id, deleted);
        return deleted;
    }

    @Override
    public synchronized boolean isExists(@NonNull @Nonnull T entity) {
        log.info("isExists: checking for existence entity = {}", entity.toString());
        boolean exists = repository.isExists(entity);
        log.info("isExists: entity = {} exists: {}", entity.toString(), exists);
        return exists;
    }

    @Override
    public synchronized List<T> findAll() {
        log.info("findAll");
        return repository.findAll();
    }

    @Override
    public synchronized Page<T> find(@NonNull @Nonnull ParamsHolderBuilder builder) {
        log.info("find. Parameters: {}");
        return repository.find(builder.build());
    }

    @Nullable
    @Override
    public synchronized T find(@Nonnull T entity) {
        return repository.find(entity);
    }

    @Nullable
    @Override
    public synchronized T find(@Nonnull Long id) {
        return repository.find(id);
    }
}
