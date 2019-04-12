package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class KindService extends CommonDbService <Kind>{

    @Autowired
    protected KindService(@NonNull @Nonnull DBRepository<Kind> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Kind entity) {
        return StringUtils.isNotBlank(entity.getKindName());
    }

    @Override
    public Optional<Kind> get(@NonNull @Nonnull Kind entity) {
        return Optional.empty();
    }
}
