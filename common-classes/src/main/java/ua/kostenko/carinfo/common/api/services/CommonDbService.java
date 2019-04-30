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

@Slf4j
abstract class CommonDbService<T> implements DBService<T> {
    final DBRepository<T> repository;

    protected CommonDbService(@NonNull @Nonnull DBRepository<T> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> create(@Nonnull @NonNull T entity) {
        if (isValid(entity)) {
            Optional<T> result = Optional.empty();
            try {
                if (exists(entity)) {
                    log.debug("create: entity {} already exists", entity);
                    result = get(entity);
                } else {
                    log.debug("create: trying to create entity: {}", entity);
                    T created = repository.create(entity);
                    if (Objects.isNull(created) && exists(entity)) {
                        log.error("create: Entity was created but not found by query, retrying to get it once again");
                        Optional<T> entityOptional = get(entity);
                        if (entityOptional.isPresent()) {
                            log.debug("create: Entity was created and found by query after retrying to get it once again");
                            created = entityOptional.get();
                        } else {
                            log.error("create: Entity was created but not found by query even after retrying it to get once again");
                        }
                    }
                    if (Objects.nonNull(created)){
                        log.debug("create: created entity: {}", created);
                    }
                    result = Optional.ofNullable(created);
                }
            } catch (Exception ex) {
                log.warn("Exception occurred due extracting value.", ex);
            }
            return result;
        }
        log.warn("create: entity {} is not valid", entity);
        return Optional.empty();
    }

    @Override
    public Optional<T> update(@Nonnull T entity) {
        if (isValid(entity)) {
            Optional<T> t = Optional.empty();
            try {
                if (exists(entity)) {
                    log.info("update: entity {} exists, will be updated", entity);
                    T updated = repository.update(entity);
                    t = Optional.ofNullable(updated);
                } else {
                    log.warn("update: entity: {} doesn't exist, nothing to update", entity);
                    return t;
                }
            } catch (Exception ex) {
                log.warn("Exception occurred due extracting value.", ex);
            }
            return t;
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
        log.debug("exists: Entity {} exists: {}", entity, exist);
        return exist;
    }

    @Override
    public Optional<T> get(long id) {
        log.debug("get: Looking for entity with id: {}", id);
        T found = repository.findOne(id);
        log.debug("get: Entity with id: {} is {}", id, found);
        return Optional.ofNullable(found);
    }

    @Override
    public Optional<T> get(@Nonnull ParamsHolderBuilder builder) {
        ParamsHolder paramsHolder = builder.build();
        log.debug("get: Looking for entity with params: {}", paramsHolder);
        T found = repository.findOne(paramsHolder);
        log.debug("get: Entity is {}", found);
        return Optional.ofNullable(found);
    }

    @Override
    public List<T> getAll() {
        log.debug("getAll: trying to find all records");
        List<T> resultList = repository.find();
        if (Objects.nonNull(resultList)) {
            log.debug("getAll: found {} records", resultList.size());
            return resultList;
        }
        log.debug("getAll: found 0 records");
        return Lists.newArrayList();
    }

    @Override
    public Page<T> getAll(@Nonnull ParamsHolderBuilder builder) {
        ParamsHolder paramsHolder = builder.build();
        log.debug("getAll: Looking for entities with params: {}", paramsHolder);
        Page<T> page = repository.find(paramsHolder);
        if (Objects.nonNull(page)) {
            log.debug("getAll: found {} records for {}", page.getTotalElements(), paramsHolder);
            return page;
        }
        log.debug("getAll: found 0 records for {}", paramsHolder);
        return Page.empty();
    }
}
