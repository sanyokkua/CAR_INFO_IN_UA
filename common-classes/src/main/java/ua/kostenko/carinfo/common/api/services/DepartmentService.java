package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class DepartmentService extends CommonDbService<Department> {

    @Autowired
    protected DepartmentService(@NonNull @Nonnull DBRepository<Department> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Department entity) {
        return Objects.nonNull(entity.getDepartmentCode());
    }

    @Override
    public Optional<Department> get(@NonNull @Nonnull Department entity) {
        return Optional.empty();
    }
}
