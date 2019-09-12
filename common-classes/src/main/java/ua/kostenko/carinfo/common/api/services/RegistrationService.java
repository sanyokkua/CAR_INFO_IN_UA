package ua.kostenko.carinfo.common.api.services;

import static java.util.Objects.nonNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

@Slf4j
@Service
class RegistrationService extends CommonDbService<Registration, String> {

    @Autowired
    protected RegistrationService(@NonNull @Nonnull DBRepository<Registration, String> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Registration entity) {
        boolean isValid = nonNull(entity.getOperationCode()) && // non NULLABLE
                nonNull(entity.getOperationName()) && // non NULLABLE
                nonNull(entity.getDepartmentCode()) && // non NULLABLE
                nonNull(entity.getKindName()) && // non NULLABLE
                nonNull(entity.getColorName()) && // non NULLABLE
                nonNull(entity.getPurposeName()) && // non NULLABLE
                nonNull(entity.getBrandName()) && // non NULLABLE
                nonNull(entity.getModelName()) && // non NULLABLE
                nonNull(entity.getMakeYear()) && // non NULLABLE
                nonNull(entity.getPersonType()) && // non NULLABLE
                nonNull(entity.getRegistrationDate()); // non NULLABLE
        if (!isValid) {
            Map<String, Object> fields = new HashMap<>();
            fields.put("OperationCode", entity.getOperationCode());
            fields.put("OperationName", entity.getOperationName());
            fields.put("DepartmentCode", entity.getDepartmentCode());
            fields.put("Kind", entity.getKindName());
            fields.put("Color", entity.getColorName());
            fields.put("Purpose", entity.getPurposeName());
            fields.put("Brand", entity.getBrandName());
            fields.put("Model", entity.getModelName());
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
        builder.param(Registration.BRAND, entity.getBrandName());
        builder.param(Registration.MODEL, entity.getModelName());
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
