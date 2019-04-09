package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Purpose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class PurposeService implements CrudService<Purpose> {
    private final PageableRepository<Purpose> repository;

    @Autowired
    public PurposeService(@NonNull @Nonnull PageableRepository<Purpose> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Purpose create(@NonNull @Nonnull Purpose entity) {
        return null;
    }

    @Nullable
    @Override
    public Purpose update(@NonNull @Nonnull Purpose entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Purpose entity) {
        return false;
    }

    @Override
    public List<Purpose> findAll() {
        return null;
    }

    @Override
    public Page<Purpose> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
