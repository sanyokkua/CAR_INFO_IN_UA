package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.PageableRepository;

import javax.annotation.Nonnull;

@Slf4j
@Service
public class OperationService extends DefaultCrudService<Operation> {

    @Autowired
    public OperationService(@NonNull @Nonnull PageableRepository<Operation> repository) {
        super(repository);
    }


    @Override
    boolean isValid(Operation entity) {
        return StringUtils.isNotBlank(entity.getOperationName());
    }
}
