package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
class PurposeService extends CommonDbService<Purpose> {

    @Autowired
    protected PurposeService(@NonNull @Nonnull DBRepository<Purpose> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Purpose entity) {
        return StringUtils.isNotBlank(entity.getPurposeName());
    }

    @Override
    public Optional<Purpose> get(@NonNull @Nonnull Purpose entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(Purpose.PURPOSE_NAME, entity.getPurposeName());
        Purpose foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
