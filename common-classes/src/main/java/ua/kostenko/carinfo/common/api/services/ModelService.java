package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class ModelService extends CommonDbService <Model>{

    @Autowired
    protected ModelService(@NonNull @Nonnull DBRepository<Model> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Model entity) {
        return StringUtils.isNotBlank(entity.getModelName());
    }

    @Override
    public Optional<Model> get(@NonNull @Nonnull Model entity) {
        return Optional.empty();
    }
}
