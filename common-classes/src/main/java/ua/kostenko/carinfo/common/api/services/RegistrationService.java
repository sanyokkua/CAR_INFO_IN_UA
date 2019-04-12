package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
public class RegistrationService extends CommonDbService <Registration>{

    @Autowired
    protected RegistrationService(@NonNull @Nonnull DBRepository<Registration> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Registration entity) {
        return false;
    }

    @Override
    public Optional<Registration> get(@NonNull @Nonnull Registration entity) {
        return Optional.empty();
    }
}
