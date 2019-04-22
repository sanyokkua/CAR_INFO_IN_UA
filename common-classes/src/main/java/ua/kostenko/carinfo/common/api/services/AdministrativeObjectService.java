package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
class AdministrativeObjectService extends CommonDbService<AdministrativeObject> {

    @Autowired
    protected AdministrativeObjectService(@NonNull @Nonnull DBRepository<AdministrativeObject> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@Nonnull AdministrativeObject entity) {
        return StringUtils.isNotBlank(entity.getAdminObjName());
    }

    @Override
    public Optional<AdministrativeObject> get(@NonNull @Nonnull AdministrativeObject entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName());
        AdministrativeObject foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
