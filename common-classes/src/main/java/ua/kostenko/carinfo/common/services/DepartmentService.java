package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.ParamHolderBuilder;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Department;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@Service
public class DepartmentService  implements CrudService<Department> {
    private final PageableRepository<Department> repository;

    @Autowired
    public DepartmentService(@NonNull @Nonnull PageableRepository<Department> repository) {
        this.repository = repository;
    }

    @Nullable
    @Override
    public Department create(@NonNull @Nonnull Department entity) {
        return null;
    }

    @Nullable
    @Override
    public Department update(@NonNull @Nonnull Department entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Department entity) {
        return false;
    }

    @Override
    public List<Department> findAll() {
        return null;
    }

    @Override
    public Page<Department> find(@NonNull @Nonnull ParamHolderBuilder builder) {
        return null;
    }
}
