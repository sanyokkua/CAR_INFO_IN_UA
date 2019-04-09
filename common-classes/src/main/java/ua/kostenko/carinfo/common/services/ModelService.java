package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class ModelService implements CrudService<Model> {
    private final PageableRepository<Model> repository;

    @Autowired
    public ModelService(@NonNull @Nonnull PageableRepository<Model> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Model create(@NonNull @Nonnull Model entity) {
        return null;
    }

    @Nullable
    @Override
    public Model update(@NonNull @Nonnull Model entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Model entity) {
        return false;
    }

    @Override
    public List<Model> findAll() {
        return null;
    }

    @Override
    public Page<Model> find(@Nonnull @NonNull ParamHolderBuilder builder) {
        return null;
    }
}
