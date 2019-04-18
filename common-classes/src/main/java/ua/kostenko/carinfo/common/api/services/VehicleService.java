package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
class VehicleService extends CommonDbService<Vehicle> {

    @Autowired
    protected VehicleService(@NonNull @Nonnull DBRepository<Vehicle> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull Vehicle entity) {
        return StringUtils.isNotBlank(entity.getBrandName()) && StringUtils.isNotBlank(entity.getModelName());
    }

    @Override
    public Optional<Vehicle> get(@NonNull @Nonnull Vehicle entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(Vehicle.BRAND_NAME, entity.getBrandName());
        builder.param(Vehicle.MODEL_NAME, entity.getModelName());
        Vehicle foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
