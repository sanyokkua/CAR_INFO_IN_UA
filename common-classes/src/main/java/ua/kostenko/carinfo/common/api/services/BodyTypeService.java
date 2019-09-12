package ua.kostenko.carinfo.common.api.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

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
