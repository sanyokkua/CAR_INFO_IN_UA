package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.AdministrativeObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class AdministrativeObjectService implements CrudService<AdministrativeObject> {
    private final PageableRepository<AdministrativeObject> repository;

    @Autowired
    public AdministrativeObjectService(@NonNull PageableRepository<AdministrativeObject> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public AdministrativeObject create(@Nonnull AdministrativeObject entity) {
        boolean exists = repository.isExists(entity);
        if (exists) {
            AdministrativeObject updated = repository.update(entity);
            return Objects.nonNull(updated) ? updated : null;
        } else {
            AdministrativeObject created = repository.create(entity);
            return Objects.nonNull(created) ? created : null;
        }
    }

    @Nullable
    @Override
    public AdministrativeObject update(@Nonnull AdministrativeObject entity) {
        boolean exists = repository.isExists(entity);
        if (exists) {
            AdministrativeObject updated = repository.update(entity);
            return Objects.nonNull(updated) ? updated : null;
        } else {
            throw new IllegalArgumentException("Entity: " + entity.toString() + " is not exists in DB so can't be updated");
        }
    }

    @Override
    public boolean delete(@Nonnull Long id) {
        return repository.delete(id);
    }

    @Override
    public boolean isExists(@Nonnull AdministrativeObject entity) {
        return repository.isExists(entity);
    }

    @Override
    public List<AdministrativeObject> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<AdministrativeObject> find(ParamHolderBuilder queryBuilder) {
        return repository.find(queryBuilder.build());
    }
}
