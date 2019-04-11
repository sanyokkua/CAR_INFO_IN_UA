package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.PageableRepository;

import javax.annotation.Nonnull;

@Slf4j
@Service
public class PurposeService extends DefaultCrudService<Purpose> {

    @Autowired
    public PurposeService(@NonNull @Nonnull PageableRepository<Purpose> repository) {
        super(repository);
    }

    @Override
    boolean isValid(Purpose entity) {
        return StringUtils.isNotBlank(entity.getPurposeName());
    }

}
