package ua.kostenko.carinfo.common.api.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

@Service
class KindService extends CommonDbService<Kind, String> {

    @Autowired
    protected KindService(@NonNull @Nonnull DBRepository<Kind, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Kind entity) {
        return StringUtils.isNotBlank(entity.getKindName());
    }

    @Override
    public Optional<Kind> get(@NonNull @Nonnull Kind entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(Kind.KIND_NAME, entity.getKindName());
        Kind foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
