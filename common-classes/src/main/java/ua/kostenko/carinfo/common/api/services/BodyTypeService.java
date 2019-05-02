package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
class BodyTypeService extends CommonDbService<BodyType, String> {

    @Autowired
    protected BodyTypeService(@NonNull @Nonnull DBRepository<BodyType, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull BodyType entity) {
        return StringUtils.isNotBlank(entity.getBodyTypeName());
    }

    @Override
    public Optional<BodyType> get(@NonNull @Nonnull BodyType entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName());
        BodyType foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
