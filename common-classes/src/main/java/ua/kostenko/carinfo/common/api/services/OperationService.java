package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class OperationService extends CommonDbService <Operation>{

    @Autowired
    protected OperationService(@NonNull @Nonnull DBRepository<Operation> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Operation entity) {
        return StringUtils.isNotBlank(entity.getOperationName());
    }

    @Override
    public Optional<Operation> get(@NonNull @Nonnull Operation entity) {
        return Optional.empty();
    }
}
