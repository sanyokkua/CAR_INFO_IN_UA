package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
class RegistrationService extends CommonDbService<Registration> {

    @Autowired
    protected RegistrationService(@NonNull @Nonnull DBRepository<Registration> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Registration entity) {
        return nonNull(entity.getOperationCode()) && //non NULLABLE
                nonNull(entity.getOperationName()) && //non NULLABLE
                nonNull(entity.getDepartmentCode()) && //non NULLABLE
                nonNull(entity.getDepartmentAddress()) && //non NULLABLE
                nonNull(entity.getDepartmentEmail()) && //non NULLABLE
                nonNull(entity.getKind()) && //non NULLABLE
                nonNull(entity.getColor()) && //non NULLABLE
                nonNull(entity.getPurpose()) && //non NULLABLE
                nonNull(entity.getBrand()) && //non NULLABLE
                nonNull(entity.getModel()) && //non NULLABLE
                nonNull(entity.getMakeYear()) && //non NULLABLE
                nonNull(entity.getPersonType()) && //non NULLABLE
                nonNull(entity.getRegistrationDate());//non NULLABLE
    }

    @Override
    public Optional<Registration> get(@NonNull @Nonnull Registration entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(Registration.BRAND, entity.getBrand());
        builder.param(Registration.MODEL, entity.getModel());
        builder.param(Registration.REGISTRATION_NUMBER, entity.getRegistrationNumber());
        builder.param(Registration.REGISTRATION_DATE, entity.getRegistrationDate());
        Registration foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
