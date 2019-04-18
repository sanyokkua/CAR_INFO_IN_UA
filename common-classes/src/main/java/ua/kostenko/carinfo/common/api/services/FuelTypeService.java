package ua.kostenko.carinfo.common.api.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.database.repositories.DBRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Service
class FuelTypeService extends CommonDbService<FuelType> {

    @Autowired
    protected FuelTypeService(@NonNull @Nonnull DBRepository<FuelType> repository) {
        super(repository);
    }

    @Override
    public boolean isValid(@NonNull @Nonnull FuelType entity) {
        return StringUtils.isNotBlank(entity.getFuelTypeName());
    }

    @Override
    public Optional<FuelType> get(@NonNull @Nonnull FuelType entity) {
        ParamsHolderBuilder builder = new ParamsHolderBuilder();
        builder.param(FuelType.FUEL_NAME, entity.getFuelTypeName());
        FuelType foundEntity = this.repository.findOne(builder.build());
        return Optional.ofNullable(foundEntity);
    }
}
