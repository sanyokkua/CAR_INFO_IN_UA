package ua.kostenko.carinfo.common.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.PageableRepository;

import javax.annotation.Nonnull;

@Slf4j
@Service
public class AdministrativeObjectService extends DefaultCrudService<AdministrativeObject> {

    @Autowired
    public AdministrativeObjectService(@NonNull @Nonnull PageableRepository<AdministrativeObject> repository) {
        super(repository);
    }

    @Override
    boolean isValid(AdministrativeObject entity) {
        return StringUtils.isNotBlank(entity.getAdminObjName());
    }
}
