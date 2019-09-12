package ua.kostenko.carinfo.common.api.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

@Service
class PurposeService extends CommonDbService<Purpose, String> {

    @Autowired
    protected PurposeService(@NonNull @Nonnull DBRepository<Purpose, String> repository) {
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
