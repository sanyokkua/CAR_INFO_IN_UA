package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class BodyTypeService extends CommonDbService <BodyType>{

    @Autowired
    protected BodyTypeService(@NonNull @Nonnull DBRepository<BodyType> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull BodyType entity) {
        return StringUtils.isNotBlank(entity.getBodyTypeName());
    }

    @Override
    public Optional<BodyType> get(@NonNull @Nonnull BodyType entity) {
        return Optional.empty();
    }
}
