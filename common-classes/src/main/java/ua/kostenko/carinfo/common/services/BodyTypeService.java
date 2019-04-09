package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.BodyType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class BodyTypeService implements CrudService<BodyType> {
    private final PageableRepository<BodyType> repository;

    @Autowired
    public BodyTypeService(@NonNull @Nonnull PageableRepository<BodyType> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public BodyType create(@NonNull @Nonnull BodyType entity) {
        return null;
    }

    @Nullable
    @Override
    public BodyType update(@NonNull @Nonnull BodyType entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull BodyType entity) {
        return false;
    }

    @Override
    public List<BodyType> findAll() {
        return null;
    }

    @Override
    public Page<BodyType> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
