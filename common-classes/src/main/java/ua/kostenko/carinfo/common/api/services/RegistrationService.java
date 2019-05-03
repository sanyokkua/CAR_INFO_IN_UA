package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;

@Slf4j
@Service
class RegistrationService extends CommonDbService<Registration, String> {

    @Autowired
    protected RegistrationService(@NonNull @Nonnull DBRepository<Registration, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Registration entity) {
        boolean isValid = nonNull(entity.getOperationCode()) && //non NULLABLE
                nonNull(entity.getOperationName()) && //non NULLABLE
                nonNull(entity.getDepartmentCode()) && //non NULLABLE
                nonNull(entity.getKind()) && //non NULLABLE
                nonNull(entity.getColor()) && //non NULLABLE
                nonNull(entity.getPurpose()) && //non NULLABLE
                nonNull(entity.getBrand()) && //non NULLABLE
                nonNull(entity.getModel()) && //non NULLABLE
                nonNull(entity.getMakeYear()) && //non NULLABLE
                nonNull(entity.getPersonType()) && //non NULLABLE
                nonNull(entity.getRegistrationDate()); //non NULLABLE
        if (!isValid) {
            Map<String, Object> fields = new HashMap<>();
            fields.put("OperationCode", entity.getOperationCode());
            fields.put("OperationName", entity.getOperationName());
            fields.put("DepartmentCode", entity.getDepartmentCode());
            fields.put("Kind", entity.getKind());
            fields.put("Color", entity.getColor());
            fields.put("Purpose", entity.getPurpose());
            fields.put("Brand", entity.getBrand());
            fields.put("Model", entity.getModel());
            fields.put("MakeYear", entity.getMakeYear());
            fields.put("PersonType", entity.getPersonType());
            fields.put("RegistrationDate", entity.getRegistrationDate());
            String result = "Entity: " + entity.toString() + " has next invalid fields: ";
            StringBuilder builder = new StringBuilder();
            fields.entrySet().stream()
                  .filter(stringObjectEntry -> Objects.isNull(stringObjectEntry.getValue()))
                  .forEach(stringObjectEntry -> builder.append(stringObjectEntry.getKey()).append("\n"));
            result += builder.toString();
            log.warn(result);
        }
        return isValid;
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

    @Override
    public boolean exists(@NonNull @Nonnull Registration entity) {
        String indexField = entity.getIndexField();
        boolean existsByIndex = Objects.nonNull(indexField) && repository.existsByIndex(indexField);
        if (existsByIndex) {
            boolean exist = repository.exist(entity);
            log.debug("exists: Entity {} exists: {}", entity, exist);
            return exist;
        }
        return false;
    }
}
