package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class ColorService  implements CrudService<Color>{
    private final PageableRepository<Color> repository;

    @Autowired
    public ColorService(@NonNull @Nonnull PageableRepository<Color> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Color create(@NonNull @Nonnull Color entity) {
        return null;
    }

    @Nullable
    @Override
    public Color update(@NonNull @Nonnull Color entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Color entity) {
        return false;
    }

    @Override
    public List<Color> findAll() {
        return null;
    }

    @Override
    public Page<Color> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
