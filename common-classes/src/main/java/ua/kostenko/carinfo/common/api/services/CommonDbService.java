package ua.kostenko.carinfo.common.api.services;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ua.kostenko.carinfo.common.Utils.processIgnoreException;

@Slf4j
abstract class CommonDbService<T> implements DBService<T> {
    protected final DBRepository<T> repository;

    protected CommonDbService(@NonNull @Nonnull DBRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> create(@Nonnull @NonNull T entity) {
        if (isValid(entity)) {
            return processIgnoreException(() -> {
                if (exists(entity)) {
                    log.info("create: entity {} already exists", entity);
                    return get(entity);
                } else {
                    T created = repository.create(entity);
                    log.info("create: created entity: {}", created);
                    return Optional.ofNullable(created);
                }
            });
        }
        log.warn("create: entity {} is not valid", entity);
        return Optional.empty();
    }

    @Override
    public Optional<T> update(@Nonnull T entity) {
        if (isValid(entity)) {
            return processIgnoreException(() -> {
                if (exists(entity)) {
                    log.info("update: entity {} exists, will be updated", entity);
                    T updated = repository.update(entity);
                    return Optional.ofNullable(updated);
                } else {
                    log.warn("update: entity: {} doesn't exist, nothing to update", entity);
                    return Optional.empty();
                }
            });
        }
        log.warn("update: entity {} is not valid", entity);
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) {
        boolean deleted = repository.delete(id);
        log.info("delete: Entity with id {} deleted = {}", id, deleted);
        return deleted;
    }

    @Override
    public boolean exists(@NonNull @Nonnull T entity) {
        boolean exist = repository.exist(entity);
        log.info("exists: Entity {} exists: {}", entity, exist);
        return exist;
    }

    @Override
    public Optional<T> get(long id) {
        log.info("get: Looking for entity with id: {}", id);
        T found = repository.findOne(id);
        log.info("get: Entity with id: {} is {}", id, found);
        return Optional.ofNullable(found);
    }

    @Override
    public Optional<T> get(@Nonnull ParamsHolderBuilder builder) {
        ParamsHolder paramsHolder = builder.build();
        log.info("get: Looking for entity with params: {}", paramsHolder);
        T found = repository.findOne(paramsHolder);
        log.info("get: Entity is {}", found);
        return Optional.ofNullable(found);
    }

    @Override
    public List<T> getAll() {
        log.info("getAll: trying to find all records");
        List<T> resultList = repository.find();
        if (Objects.nonNull(resultList)) {
            log.info("getAll: found {} records", resultList.size());
            return resultList;
        }
        log.info("getAll: found 0 records");
        return Lists.newArrayList();
    }

    @Override
    public Page<T> getAll(@Nonnull ParamsHolderBuilder builder) {
        ParamsHolder paramsHolder = builder.build();
        log.info("getAll: Looking for entities with params: {}", paramsHolder);
        Page<T> page = repository.find(paramsHolder);
        if (Objects.nonNull(page)) {
            log.info("getAll: found {} records for {}", page.getTotalElements(), paramsHolder);
            return page;
        }
        log.info("getAll: found 0 records for {}", paramsHolder);
        return Page.empty();
    }
}
