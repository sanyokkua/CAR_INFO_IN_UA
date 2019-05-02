package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
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
