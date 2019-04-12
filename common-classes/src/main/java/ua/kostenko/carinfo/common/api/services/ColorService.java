package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class ColorService extends CommonDbService <Color>{

    @Autowired
    protected ColorService(@NonNull @Nonnull DBRepository<Color> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Color entity) {
        return StringUtils.isNotBlank(entity.getColorName());
    }

    @Override
    public Optional<Color> get(@NonNull @Nonnull Color entity) {
        return Optional.empty();
    }
}
