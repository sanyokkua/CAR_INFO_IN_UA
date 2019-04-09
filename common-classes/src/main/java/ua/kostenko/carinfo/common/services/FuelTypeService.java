package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.FuelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class FuelTypeService implements CrudService<FuelType> {
    private final PageableRepository<FuelType> repository;

    @Autowired
    public FuelTypeService(@NonNull @Nonnull PageableRepository<FuelType> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public FuelType create(@NonNull @Nonnull FuelType entity) {
        return null;
    }

    @Nullable
    @Override
    public FuelType update(@NonNull @Nonnull FuelType entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull FuelType entity) {
        return false;
    }

    @Override
    public List<FuelType> findAll() {
        return null;
    }

    @Override
    public Page<FuelType> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
