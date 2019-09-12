package ua.kostenko.carinfo.common.api.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

@Service
class ModelService extends CommonDbService<Model, String> {

    @Autowired
    protected ModelService(@NonNull @Nonnull DBRepository<Model, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Model entity) {
        return StringUtils.isNotBlank(entity.getModelName());
    }

    @Override
    public Optional<Model> get(@NonNull @Nonnull Model entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(Model.MODEL_NAME, entity.getModelName());
        Model foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
