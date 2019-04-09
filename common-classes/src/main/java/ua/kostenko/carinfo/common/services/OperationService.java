package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Operation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class OperationService implements CrudService<Operation> {
    private final PageableRepository<Operation> repository;

    @Autowired
    public OperationService(@NonNull @Nonnull PageableRepository<Operation> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Operation create(@NonNull @Nonnull Operation entity) {
        return null;
    }

    @Nullable
    @Override
    public Operation update(@NonNull @Nonnull Operation entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Operation entity) {
        return false;
    }

    @Override
    public List<Operation> findAll() {
        return null;
    }

    @Override
    public Page<Operation> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
