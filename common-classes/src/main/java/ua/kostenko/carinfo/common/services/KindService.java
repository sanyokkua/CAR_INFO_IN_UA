package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Kind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class KindService  implements CrudService<Kind> {
    private final PageableRepository<Kind> repository;

    @Autowired
    public KindService(@NonNull @Nonnull PageableRepository<Kind> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Kind create(@NonNull @Nonnull Kind entity) {
        return null;
    }

    @Nullable
    @Override
    public Kind update(@NonNull @Nonnull Kind entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Kind entity) {
        return false;
    }

    @Override
    public List<Kind> findAll() {
        return null;
    }

    @Override
    public Page<Kind> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
