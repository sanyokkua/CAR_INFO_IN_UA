package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Brand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class BrandService  implements CrudService<Brand> {
    private final PageableRepository<Brand> repository;

    @Autowired
    public BrandService(@NonNull @Nonnull PageableRepository<Brand> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Brand create(@NonNull @Nonnull Brand entity) {
        return null;
    }

    @Nullable
    @Override
    public Brand update(@NonNull @Nonnull Brand entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Brand entity) {
        return false;
    }

    @Override
    public List<Brand> findAll() {
        return null;
    }

    @Override
    public Page<Brand> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
